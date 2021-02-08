import java.net.*;
import java.lang.System;
import java.util.ArrayList;

// Class to interface with the PresenceManagementServer
class PMSInterface {
    private ChatMaster chat;
    private final int pmsListenerUdpPort = 1339;
    private String pmsIP;
    private ArrayList<String> usersConnectedToPMS;

    private DatagramSocket socket;

    public PMSInterface(String ip) {
        this.pmsIP = ip;
        try {
            this.socket = new DatagramSocket();
        } catch (Exception ex) {
            System.out.println("[ERROR] in PMSInterface constructor : " + ex.toString());
        }
    }

    // Helper function to send udp data
    private void sendData(String data) {
        try {
            byte[] toSend = data.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(toSend, toSend.length, InetAddress.getByName(this.pmsIP), this.pmsListenerUdpPort);
            this.socket.send(packet);
            System.out.println("[LOG] in PMSInterface register : registered to PMS @"+this.pmsIP);
        } catch (Exception ex) {
            System.out.println("[ERROR] in PMSInterface register : " + ex.toString());
        }
    }
    
    // Register to the PMS
    public void register(String pseudo) {
        this.sendData("SUB:"+pseudo);
        System.out.println("[LOG] in PMSInterface register : registered to PMS @"+this.pmsIP);
    }

    // Opens a connection with an other user connected to the PMS based on his pseudo
    public void openConnection(String pseudo) {
        this.sendData("OPN:"+pseudo);
        System.out.println("[LOG] in PMSInterface openConnection : trying opening connection with " + pseudo);
    }

    // Request list of other users connected to the PMS
    public void discoverUsers() {
        this.sendData("REQ");
        System.out.println("[LOG] in PMSInterface discoverUsers : discovering new users");
    }

    public void publish(String status) {}

    public void notify(String newPseudo) {
        this.sendData("NOT:"+newPseudo);
        System.out.println("[LOG] in PMSInterface : notified pseudo change : " + newPseudo);
    }
    
    // Unsubscribe from the PMS
    public void unsubscribe() {
        this.sendData("UNS");
    }
}
