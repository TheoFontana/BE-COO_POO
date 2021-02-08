import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.System;

public class PresenceManagementServer extends Thread {
    
    // DatagramSocket to receive data from users of the PMS
    private DatagramSocket serverSocket;
    private Listener udpListener;
    private static int udpListenerSocket = 1339;
    
    // Stores all users managed by the PMS
    private ArrayList<ManagedUser> managedUsers = new ArrayList<ManagedUser>();

    // Constructor 
    public PresenceManagementServer() {}

    // Override run() method of class Thread
    @Override
    public void run() {
        this.startServer();
    }

    // Starts the server
    private void startServer() {
        try {
            this.serverSocket = new DatagramSocket(this.udpListenerSocket);
            System.out.println("[LOG] Started listening on port " + Integer.toString(this.udpListenerSocket));
            // Start listener
            this.udpListener = new Listener(this.serverSocket);
            this.udpListener.start();
        } catch (Exception ex) {
            System.out.println("[ERROR] Couldn't bind port " + Integer.toString(this.udpListenerSocket) + " for listening");
            System.exit(-1);
        }
    }

    // Search an user by pseudo into managedUsers
    // returns null if the user doesn't exist
    private ManagedUser searchByIP(String ip) {
        ManagedUser client = null;
        for (int i = 0; i < managedUsers.size(); i++) {
            if (managedUsers.get(i).getIP().equals(ip)) {
                client = managedUsers.get(i);
            }
        }
        return client; 
    }

    // Search an user by pseudo into managedUsers
    // returns null if the user doesn't exist
    private ManagedUser searchByPseudo(String pseudo) {
        ManagedUser client = null;
        for (int i = 0; i < managedUsers.size(); i++) {
            if (managedUsers.get(i).getPseudo().equals(pseudo)) {
                client = managedUsers.get(i);
            }
        }
        return client; 
    }
    
    // Called when a client notify a change
    public void clientNotify(String IPAddress, String newPseudo) {
        ManagedUser user = searchByIP(IPAddress);
        if (user != null) {
            user.setPseudo(newPseudo);
        }
    }

    // Called when a client publish its status
    public void clientPublish(String IPAddress, String status) {
        ManagedUser client = searchByIP(IPAddress);
        if (client != null) {
            switch (status) {
                case "ACT":
                    client.setActive();
                    break;
                case "ABS":
                    client.setAbsent();
                    break;
                default:
                    System.out.println("[ERROR] in clientPublish : Unknown status");
                    return;
            }
            System.out.println("[LOG] in clientPublish : user " + client + " publish its status : " + client.getStatus());
        }
    }
    
    // Register a new user to the server
    private void clientRegister(String ip, String pseudo) {
        ManagedUser newUser = new ManagedUser(ip, pseudo);
        if (!this.managedUsers.contains(newUser) && this.searchByPseudo(pseudo) == null) {
            managedUsers.add(newUser);
            System.out.println("[LOG] in addClient : Registered a new user " + newUser);
        } else {
            System.out.println("[LOG] in addClient : User " + newUser + " already registered");
        }
    }

    // Remove an user from the server
    private void clientUnsubscribe(String ip) {
        ManagedUser client = this.searchByIP(ip);
        if (client != null) {
            this.managedUsers.remove(client);
            System.out.println("[LOG] Removed client " + client);
        }
    }

    // Called when a client requests all other connected users
    private void clientRequest(String ip) {
        try {
            String toSend = "PMSREG:";
            for (int i = 0; i < managedUsers.size(); i++) {
                toSend += (managedUsers.get(i).getPseudo() + "," + managedUsers.get(i).getIP() + ":");
            }
            byte[] dataToSend = toSend.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, InetAddress.getByName(ip), 1338);
            this.serverSocket.send(packet);
            System.out.println("[LOG] in PMS clientRequest : indicating presence of " + toSend);
        } catch (Exception ex) {
            System.out.println("[ERROR] in clientRequest : " + ex.toString());
        }
    }

    // Send all connected users to all clients
    private void updateAllClients() {
        try {
            String toSend = "PMSREG:";
            for (int i = 0; i < managedUsers.size(); i++) {
                toSend += (managedUsers.get(i).getPseudo() + "," + managedUsers.get(i).getIP() + ":");
            }
            byte[] dataToSend = toSend.getBytes("UTF-8");
            for (int i = 0; i < managedUsers.size(); i++) {
                DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, InetAddress.getByName(managedUsers.get(i).getIP()), 1338);
                this.serverSocket.send(packet);
                System.out.println("[LOG] in updateAllClients : indicating presence of " + toSend);
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] in updateAllClients : " + ex.toString());
        }
    }

    // Called when a client request to open a connection with an other user
    // ip is used to identify the user requesting the connection
    // otherPseudo is used to identify the user to connect to
    private void clientOpen(String ip, String otherPseudo) {
        ManagedUser client = searchByIP(ip);
        ManagedUser dest = searchByPseudo(otherPseudo);
        if (client != null && dest != null) {
            spawnTunnel(client, dest); 
        }
    }

    private void broadcastPresence() {
        try {
            String toSend = "PMSCONNECT";
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] dataToSend = toSend.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, InetAddress.getByName("255.255.255.255"), 1338);
            socket.send(packet);
            System.out.println("[LOG] in PMS broadcastPresence : broadcasting presence");
        } catch (Exception ex) {
            System.out.println("[ERROR] in PMS broadcastPresence : " + ex.toString());
        }
    }

    // handle a request received from the listener
    private void handleRequest(String content, String IPAddress) {
        System.out.println("[LOG] Received " + content + " from " + IPAddress);
        String [] splitted = content.split(":");
        String [] arguments = Arrays.copyOfRange(splitted, 1, splitted.length);
        for (int i = 0; i < arguments.length; i++) {
            System.out.println("[DEBUG] Args["+i+"] : " + arguments[i]);
        }
        String command = splitted[0];
        switch (command.toUpperCase()) {
            case "SUB":
                // Client subscribe
                System.out.println("[DEBUG] Detected SUB command");
                if (arguments.length == 1) {
                    clientRegister(IPAddress, arguments[0]);
                    updateAllClients();
                }
                break;
            case "PUB":
                // Client publish
                System.out.println("[DEBUG] Detected PUB command");
                if (arguments.length == 1) {
                    clientPublish(IPAddress, arguments[0]);
                }
                break;
            case "NOT":
                // Client notify
                System.out.println("[DEBUG] Detected NOT command");
                if (arguments.length == 1) {
                    clientNotify(IPAddress, arguments[0]);
                }
                break;
            case "REQ":
                // Client request
                System.out.println("[DEBUG] Detected REQ command");
                clientRequest(IPAddress);
                break;
            case "OPN":
                // Client opens connection with an other user
                System.out.println("[DEBUG] Detected OPN command");
                if (arguments.length == 1) {
                    clientOpen(IPAddress, arguments[0]);
                }
                break;
            case "UNS":
                // Client unsubscribe
                System.out.println("[DEBUG] Detected UNS command");
                clientUnsubscribe(IPAddress);
                break;
            case "AYH":
                // Probe sent to ask if the PMS is online (Are you here ?)
                System.out.println("[DEBUG] Detected AYH command");
                broadcastPresence();
                break;
            default:
                System.out.println("[DEBUG] Unrecognized command");
                break;
        }
    }

    // udb listener for incoming probes
    private class Listener extends Thread {
        private DatagramSocket socket;
        private byte buffer[] = new byte[512];

        // Constructor
        public Listener(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    this.socket.receive(packet);
                    String receivedData = new String(packet.getData(), 0, packet.getLength()).replace("\n", "");
                    handleRequest(receivedData, packet.getAddress().getHostAddress());
                }
            } catch (IOException ex) {
                System.out.println("[ERROR] Error receiving data in udp listener : " + ex);
            }
        }
    }

    // creates a tunnel between 2 users
    private void spawnTunnel(ManagedUser user1, ManagedUser user2) {
        new Tunnel(user1, user2).start();
    }
}
