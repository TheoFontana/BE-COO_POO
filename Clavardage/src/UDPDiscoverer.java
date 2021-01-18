import java.net.*;
import java.io.IOException;
import java.lang.System;

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
                    System.out.println("[LOG] Received " + receivedData + " on UDP listener from " + packet.getAddress());
                    if (receivedData.equals("REQ")) {
                        // Asks pseudo of all other users
                        String userInfo = this.chat.getUserInfo();
                        if (!userInfo.equals("")) {
                            this.parent.broadcastData("REG:"+this.chat.getUserInfo()); 
                        }
                    } else if (receivedData.equals("DISCONNECT")) { 
                        // Disconnect an user
                        ForeignUser user = this.chat.searchUserByIP(packet.getAddress().getHostAddress());
                        if (user != null) {
                            System.out.println("[LOG] Removing an user : " + user);
                            this.chat.removeForeignUser(user);
                            this.chat.getGui().foreignUserDeconnection(user);
                        }
                    } else {
                        String [] splitted = receivedData.split(":");
                        if (splitted.length != 2 || !splitted[0].equals("REG")) { continue; }
                        else {
                            // Add a new Foreign User if the received msg has the format REG:<pseudo>
                            ForeignUser user = new ForeignUser(packet.getAddress().getHostAddress().hashCode(), splitted[1], packet.getAddress().getHostAddress());
                            if (!this.chat.isUserInList(user)) {
                                System.out.println("[LOG] Registering a new user : " + user);
                                this.chat.addUser(user);
                                this.chat.getGui().foreignUserConnection(user);
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("[ERROR] Error receiving data in udp listener : " + ex);
            }
        }
    }

    private void broadcastData(String toSend) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            byte[] dataToSend = toSend.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, InetAddress.getByName("255.255.255.255"), 1338);
            socket.send(packet);
        } catch (Exception ex) {
            System.out.println("[ERROR] in UDPDiscoverer broadcastData : " + ex.toString());
        }
    }

    public void getOtherUsers() {
        this.broadcastData("REQ");
    }

    public void disconnect() {
        this.broadcastData("DISCONNECT");
    }
}
