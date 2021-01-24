import java.net.*;
import java.io.IOException;
import java.lang.System;
import java.util.*;

// Starts a listener to detect new user on the network
// Periodically sends a packet to signal its presence on the network
public class UDPDiscoverer extends Thread {
    // port to listen to
    private final int serverPort = 1338;
    // Listener socket
    private DatagramSocket listenerSocket;
    // Reference to the chat master
    private ChatMaster chat;
    // Listen for incoming probes
    private Listener listener;

    // Constructor
    public UDPDiscoverer(ChatMaster chat) {
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            this.listenerSocket = new DatagramSocket(this.serverPort);
            System.out.println("[LOG] Started UDP discoverer on port " + this.serverPort);
            // Start listener
            this.listener = new Listener(this.chat, this.listenerSocket, this);
            this.listener.start();
        } catch (Exception ex) {
            System.out.println("[ERROR] Couldn't create udp socket on port " + this.serverPort);
            System.exit(-3);
        }
    }

    // Listen for incoming probes
    private class Listener extends Thread {
        // Reference to the ChatMaster to save other connected users
        private ChatMaster chat;
        private DatagramSocket socket;
        private byte buffer[] = new byte[512];
        private UDPDiscoverer parent;

        // Constructor
        public Listener(ChatMaster chat, DatagramSocket socket, UDPDiscoverer parent) {
            this.chat = chat;
            this.socket = socket;
            this.parent = parent;
        }

        @Override
        public void run() {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    this.socket.receive(packet);
                    String receivedData = new String(packet.getData(), 0, packet.getLength()).replace("\n", "");
                    System.out.println("[LOG] Received " + receivedData + " on UDP listener from " + packet.getAddress().getHostAddress());
                    String [] splitted = receivedData.split(":");
                    String [] arguments = Arrays.copyOfRange(splitted, 1, splitted.length);
                    for (int i = 0; i < arguments.length; i++) {
                        System.out.println("[DEBUG] Args["+i+"] : " + arguments[i]);
                    }
                    String command = splitted[0];
                    switch (command.toUpperCase()) {
                        case "REQ":
                            // Send back localUser info
                            String userInfo = this.chat.getUserInfo();
                            if (!userInfo.equals("")) {
                                this.parent.broadcastData("REG:"+this.chat.getUserInfo(), 1338); 
                            }
                            break;
                        case "DISCONNECT":
                            // Disconnect an user
                            ForeignUser user = this.chat.searchUserByIP(packet.getAddress().getHostAddress());
                            if (user != null) {
                                System.out.println("[LOG] Removing an user : " + user);
                                this.chat.removeForeignUser(user);
                                this.chat.getGui().foreignUserDeconnection(user);
                            }
                            break;
                        case "REG":
                            // Add a new Foreign User if the received msg has the format REG:<pseudo>
                            ForeignUser newUser = new ForeignUser(packet.getAddress().getHostAddress().hashCode(), arguments[0], packet.getAddress().getHostAddress());
                            if (!this.chat.isUserInList(newUser)) {
                                System.out.println("[LOG] Registering a new user : " + newUser);
                                this.chat.addUser(newUser);
                                this.chat.getGui().foreignUserConnection(newUser);
                            }
                            break;
                        case "CHG":
                            // Change pseudo of a user. Format : CHG:<old_pseudo>:<new_pseudo>
                            if (arguments.length == 2) {
                                this.chat.changeUserPseudo(arguments[0], arguments[1]);
                            }
                            break;
                        case "PMSREG":
                            // PresenceManagementServer indicates the presence of multiple other users
                            ForeignUser newUser2 = null;
                            for (int i = 0; i < arguments.length; i++) {
                                System.out.println(arguments[i]);
                                String [] userSplitted = arguments[i].split(",");
                                if (userSplitted.length == 2) {
                                    newUser2 = new ForeignUser(userSplitted[1].hashCode(), userSplitted[0], userSplitted[1]);
                                    newUser2.setRemote();
                                    if (!this.chat.isUserInList(newUser2)) {
                                        System.out.println("[LOG] Registering a new user : " + newUser2);
                                        this.chat.addUser(newUser2);
                                        this.chat.getGui().foreignUserConnection(newUser2);
                                    }
                                }
                            }
                            break;
                        case "PMSCONNECT":
                            // PresenceManagementServer indicates its own presence on the LAN
                            this.chat.connectToPresenceManagementServer(packet.getAddress().getHostAddress());
                            break;
                        default:
                            System.out.println("[ERROR] in UDPDiscoverer : unrecognized command : " + command);
                            break;
                    }
                }
            } catch (IOException ex) {
                System.out.println("[ERROR] Error receiving data in udp listener : " + ex);
            }
        }
    }

    private void broadcastData(String toSend, int port) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] dataToSend = toSend.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, InetAddress.getByName("255.255.255.255"), port);
            socket.send(packet);
        } catch (Exception ex) {
            System.out.println("[ERROR] in UDPDiscoverer broadcastData : " + ex.toString());
        }
    }

    public void getOtherUsers() {
        // Detecting local users
        this.broadcastData("REQ", 1338);
        // Detecting PresenceManagementServer if any
        if (this.chat.pmsinterface == null) {
            this.broadcastData("AYH", 1339);
        }
    }

    public void disconnect() {
        this.broadcastData("DISCONNECT", 1338);
    }

    public void changePseudo(String newPseudo) {
        this.broadcastData("CHG:"+this.chat.getLocalUser().getPseudo()+":"+newPseudo, 1338);
        if (this.chat.pmsinterface != null) {
            this.chat.pmsinterface.notify(newPseudo);
        }
    }
}
