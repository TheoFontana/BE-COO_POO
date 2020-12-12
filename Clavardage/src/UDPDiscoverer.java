import java.net.*;
import java.io.IOException;
import java.lang.System;

// Starts a listener to detect new user on the network
// Periodically sends a packet to signal its presence on the network
public class UDPDiscoverer extends Thread {
    // port to listen to
    private int serverPort;
    // Listener socket
    private DatagramSocket listenerSocket;
    // Reference to the chat master
    private ChatMaster chat;
    // Listen for incoming probes
    private Listener listener;
    // Periodically send probes
    private Prober prober;

    // Constructor
    public UDPDiscoverer(int port, ChatMaster chat) {
        this.serverPort = port;
        this.chat = chat;
    }

    @Override
    public void run() {
        try {
            this.listenerSocket = new DatagramSocket(this.serverPort);
            System.out.println("[LOG] Started UDP discoverer on port " + this.serverPort);
            // Start listener
            this.listener = new Listener(this.chat, this.listenerSocket);
            this.listener.start();
            // Start prober
            this.prober = new Prober(this.chat);
            this.prober.start();
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

        // Constructor
        public Listener(ChatMaster chat, DatagramSocket socket) {
            this.chat = chat;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    this.socket.receive(packet);
                    String receivedData = new String(packet.getData(), 0, packet.getLength()).replace("\n", "");
                    System.out.println("[LOG] Received " + receivedData + " on UDP listener");
                    // Parse received data into a ForeigUser and add ForeignUser to otherUsers if it's not in it already
                    ForeignUser user = ForeignUser.fromString(receivedData);
                    if (!this.chat.isUserInList(user)){
                        this.chat.addUser(user);
                    }
                }
            } catch (IOException ex) {
                System.out.println("[ERROR] Error receiving data in udp listener : " + ex);
            }
        }
    }

    // Periodically send probes
    private class Prober extends Thread {
        private DatagramSocket socket;
        // Reference to ChatMaster to get user info
        private ChatMaster chat;

        // Constructor
        public Prober(ChatMaster chat) {
            this.chat = chat;
        }

        @Override
        public void run() {
            try {
                this.socket = new DatagramSocket();
                this.socket.setBroadcast(true);
                System.out.println("[LOG] Started udp prober");
                while (true) {
                    try {
                        byte[] toSend = this.chat.getUserInfo().getBytes("UTF-8");
                        DatagramPacket packet = new DatagramPacket(toSend, toSend.length, InetAddress.getByName("255.255.255.255"), 1338);
                        socket.send(packet);
                        sleep(10000);
                    } catch (NullPointerException ex) {}
                    catch (InterruptedException ex) {
                        System.out.println("[ERROR] Error in sleep in udp prober : " + ex);
                    }
                }
            } catch (IOException ex) {
                System.out.println("[ERROR] Error sending data in udp prober : " + ex);
            }
        }

    }

}
