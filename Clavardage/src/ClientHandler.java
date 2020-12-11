import java.io.*;
import java.net.*;
import java.lang.System;


// Class to handle connection from another user
public class ClientHandler extends Thread {
    // Reference to the socket to communicate with the client
    private Socket clientSocket = null;
    // Thread to send data 
    private Sender sender;
    // Thread to receive data
    private Receiver receiver;

    // Constructor
    public ClientHandler(Socket s) {
        this.clientSocket = s;
    }

    // Override of the run() method of class Thread
    @Override 
    public void run() {
        try {
            System.out.println("[LOG] New Client : " + clientSocket);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            sender = new Sender(out);
            sender.start();

            receiver = new Receiver(in);
            receiver.start();

            receiver.join();
        } catch (Exception ex) {
            System.out.println("[ERROR] (ClientHandler) : " + ex);
        } finally {
            System.out.println("[LOG] Client " + this.clientSocket + " exited");
            try {
                if (this.clientSocket != null) {
                    this.clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] Couldn't close client socket");
            }
        }
    }

    // Class used to send data in an async way
    private class Sender extends Thread {
        public String dataToSend = "";
        PrintWriter out;

        public Sender(PrintWriter out) {
            this.out = out;
        }

        @Override
        public void run() {
            while (true) {
                if (!dataToSend.equals("")) {
                    out.println(dataToSend);
                    out.flush();
                    dataToSend = "";
                }
            }
        }
    }

    // Class used to receive data in an async way
    private class Receiver extends Thread {
        public String buffer;
        BufferedReader in;

        public Receiver(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                buffer = in.readLine();
                while (!buffer.equals("Close connection")) {
                    System.out.println("[LOG] Received : " + buffer);
                    buffer = in.readLine();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] (ChatClient receiver) : " + e);
            }
        }
    }

    // Add data to send in the buffer of the sender thread
    public void sendData(String content) {
        this.sender.dataToSend = content;
    }

    // Retrieve data in the buffer of the receiver Thread
    public String getBufferedData() {
        return this.receiver.buffer;
    }
}