import java.net.*;
import java.lang.System;

// Represents a connection between two users subscribed to the PMS
public class Tunnel extends Thread {
    
    private ManagedUser user1;
    private ManagedUser user2;

    private RemoteClientHandler handler1;
    private RemoteClientHandler handler2;

    private String bufferUser1 = "";
    private String bufferUser2 = "";

    public Tunnel(ManagedUser user1, ManagedUser user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    @Override
    public void run() {
        try {
            Socket sock1 = new Socket(this.user1.getIP(), 1337);
            Socket sock2 = new Socket(this.user2.getIP(), 1340);
            this.handler1 = new RemoteClientHandler(sock1, sock2);
            this.handler2 = new RemoteClientHandler(sock2, sock1);
            this.handler1.start();
            this.handler2.start();
            System.out.println("[LOG] in Tunnel : created two sockets " + sock1.toString() + " and " + sock2.toString());
            System.out.println("[LOG] Created a new Tunnel between " + this.user1 + " and " + this.user2);
            /*
            while (true) {
              if (!this.handler1.getBufferedData().equals(this.bufferUser1)) {
                  this.bufferUser1 = this.handler1.getBufferedData(); 
                  System.out.println("[LOG] in Tunnel : received from handler1 : " + this.bufferUser1);
              }
              if (!this.handler2.getBufferedData().equals(this.bufferUser2)) {
                  this.bufferUser2 = this.handler2.getBufferedData(); 
                  System.out.println("[LOG] in Tunnel : received from handler2 : " + this.bufferUser2);
              }
            }
            */
        } catch (Exception ex) {
            System.out.println("[ERROR] in Tunnel : " + ex.toString());
        }
    }
}
