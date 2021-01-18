import java.io.*;
import java.net.*;
import java.lang.System;

// Class to handle connection from another user
public class ClientHandler extends Thread {
    // Reference to the socket to communicate with the client
    private Socket clientSocket = null;
    // Thread to receive data
    private Receiver receiver;

    // Reference to the gui to make updates 
    private GUIFacade gui;

    // ForeignUser associated with this connection
    private ForeignUser user;

    private ChatMaster chat;

    // Constructor
    public ClientHandler(Socket s, ChatMaster chat, ForeignUser foreignUser) {
        this.chat = chat;
        this.gui = chat.getGui();
        this.clientSocket = s;
        this.user = foreignUser;
        this.user.setSock(this.clientSocket);
    }

    // Override of the run() method of class Thread
    @Override 
    public void run() {
        try {
            System.out.println("[LOG] in ClientHandler : New Client : " + clientSocket);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            receiver = new Receiver(in, this);
            receiver.start();

            receiver.join();
        } catch (Exception ex) {
            System.out.println("[ERROR] in ClientHandler : " + ex);
        } finally {
            System.out.println("[LOG] in ClientHandler : Client " + this.clientSocket + " exited");
            // Reset ForeignUser socket
            this.user.setSock(null);
            this.chat.removeForeignUser(this.user);
            this.gui.foreignUserDeconnection(this.user);
            try {
                if (this.clientSocket != null) {
                    this.clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] in ClientHandler : Couldn't close client socket");
            }
        }
    }

    // Class used to receive data in an async way
    private class Receiver extends Thread {
        public String buffer;
        BufferedReader in;
        ClientHandler parent;

        public Receiver(BufferedReader in, ClientHandler parent) {
            this.in = in;
            this.parent = parent;
        }

        @Override
        public void run() {
            try {
                buffer = in.readLine();
                String [] splitted = buffer.split(":");
                String command = splitted[0];
                while (!buffer.equals("Close connection")) {
                    System.out.println("[LOG] in ClientHandler Received : " + buffer);
                    this.parent.chat.updateHistory(new Message(this.parent.user.getId(), 0, buffer));
                    this.parent.gui.messageReception(this.parent.getUser(), buffer);
                    buffer = in.readLine();
                }
            } catch (Exception e) {
                System.out.println("[ERROR] in ChatClient receiver : " + e);
            }
        }
    }

    // Retrieve data in the buffer of the receiver Thread
    public String getBufferedData() {
        return this.receiver.buffer;
    }

    public ForeignUser getUser() {
        return this.user;
    }

}
