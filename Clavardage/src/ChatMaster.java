import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.lang.System;

// Class implementing chat networking logic, receive tcp connections from 
// other users, connect to other users
public class ChatMaster extends Thread {
	
    // Listens for other users to connect to this listener
    private TCPListener tcplistener;
	// Number of simultaneously connected clients
	private int connectedClients;
	// Stores infos for all other users on the network
	public ArrayList<ForeignUser> otherUsers;
	// Object to discover other users and tell them we're available
	private UDPDiscoverer discoverer;
	// Reference to the local user of the application
	private LocalUser localUser = null;

    // Reference to the GUIFacade object to call its methods
    private GUIFacade gui;
    
	// Constructor
	public ChatMaster() {
		this.otherUsers = new ArrayList<ForeignUser>();
        this.gui = new GUIFacade(this);
	}

	// Override run() method of class Thread
	@Override
	public void run() {
		this.startServer();
	}

	// Starts the server
	private void startServer() {
        this.tcplistener = new TCPListener(this);
        this.tcplistener.start();
        this.discoverer = new UDPDiscoverer(this);
        this.discoverer.start();
        this.discoverer.getOtherUsers();
	}

	// Getters + Setters
	public void setLocalUser(LocalUser user) {
		this.localUser = user;
	}

	public LocalUser getLocalUser() {
		return this.localUser;
	}

    public void setGuiFacade(GUIFacade gui) {
        this.gui = gui;
    }
    
    public GUIFacade getGui() {
        return this.gui;
    }

	// Add an other ForeignUser to the list that stores
	// other users on the network
	public void addUser(ForeignUser user) {
		this.otherUsers.add(user);
	}

	// Check if the ForeignUser in argument is already stored
	// in the list of other users
	public boolean isUserInList(ForeignUser user) {
		return this.otherUsers.contains(user);
	}

    public String getUserInfo() {
        if (this.localUser != null) {
            return this.localUser.getPseudo(); 
        } else {
            return new String("");
        }
    } 
    
    // Send content to dest
    public void sendMessage(String content, ForeignUser dest) {    
        Socket sock = dest.getSock();
        if (sock != null) {
            try {
                PrintWriter out = new PrintWriter(sock.getOutputStream());
                out.println(content);
                out.flush();
                System.out.println("[LOG] in ChatMaster sendMessage : sending to " + dest + " : " + content);
                this.updateHistory(new Message(0, dest.getId(), content));
            } catch (Exception ex) {
                System.out.println("[ERROR] in ChatMaster sendMessage : " + ex.toString());
            }
        } else {
            try {
                sock = new Socket(dest.getIP(), 1337);
                new ClientHandler(sock, this, dest).start();
                this.sendMessage(content, dest);
            } catch (Exception ex) {
                System.out.println("[ERROR] in ChatMaster sendMessage : " + ex.toString());
            }
        }
    }
    
    public boolean isPseudoTaken(String pseudo) {
        for (int i = 0; i < otherUsers.size(); i ++) {
            if (otherUsers.get(i).getPseudo().equals(pseudo)) {
                return true;
            }
        }
        return false;
    }

    // Search through the array otherUsers for a ForeignUser whose IP matches ip
    public ForeignUser searchUserByIP(String ip) {
        System.out.println("[LOG] in searchUserByIP : Searching for user with ip " + ip);
        for (int i = 0; i < otherUsers.size(); i ++) {
            if (otherUsers.get(i).getIP().equals(ip)) {
                System.out.println("[LOG] in searchUserByIP : Found " + otherUsers.get(i));
                return otherUsers.get(i);
            }
        }
        return null;
    }

    public void updateHistory(Message m) {
        System.out.println("[LOG] Adding " + m.serialize() + " to history");
        if (this.localUser != null) {
            this.localUser.getHistory().addMessage(m);
            for (int i = 0; i < otherUsers.size(); i ++) {
                System.out.println("[LOG] updateHistory : Setting " + otherUsers.get(i).getId() + " -> " + otherUsers.get(i).getPseudo());
                this.localUser.getHistory().setAssociation(otherUsers.get(i).getId(), otherUsers.get(i).getPseudo());
            }
        }
    }

    // function called when clicking logout on the gui
    public void logout() {
        System.out.println("[LOG] in logout: Logging out");
        for (int i = 0; i < otherUsers.size(); i ++) {
            sendMessage("Close connection", otherUsers.get(i));
        }
        this.discoverer.disconnect();
        this.otherUsers.clear();
        if (this.localUser != null) {
            this.localUser.getHistory().save();
            this.localUser.getHistory().clearHistory();
        }
        this.localUser = null;
        this.discover();
    }

    public void discover() {
        this.discoverer.getOtherUsers();
    }

    public void removeForeignUser(ForeignUser user) {
        this.otherUsers.remove(user);
    }

}
