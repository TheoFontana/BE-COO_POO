import java.io.*;
import java.net.*;
import java.lang.System;

public class RemoteClientHandler extends Thread {
    // Reference to the socket to communicate with the client
    private Socket clientSocket = null;
    
    private Socket dest = null;

    // Thread to send data 
    private Sender sender;
    // Thread to receive data
    private Receiver receiver;

    // Constructor
    public RemoteClientHandler(Socket s, Socket destSocket) {
        this.clientSocket = s;
        this.dest = destSocket;
        try {
            this.sender = new Sender(new PrintWriter(this.dest.getOutputStream()));
            this.receiver = new Receiver(this, new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream())));
        } catch (Exception ex) {
            System.out.println("[ERROR] in RemoteClientHandler constructor : " + ex.toString());
        }
    }

    @Override
    public void run() {
        try {
            this.sender.start();
            this.receiver.start();
            this.receiver.join();
        } catch (Exception ex) {
            System.out.println("[ERROR] in RemoteClientHandler run : " + ex.toString());
        } finally {
            try {
                if (this.clientSocket != null) {
                    this.clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] in RemoteClientHandler : Couldn't close client socket");
            }
            System.out.println("[LOG] in RemoteClientHandler : Client ended connection " + this.clientSocket);
        }
    }

    public String toString() {
        return this.clientSocket.toString();
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
                // To keep the thread active
                out.flush();
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
        public String buffer = "";
        BufferedReader in;
        private RemoteClientHandler rch;

        public Receiver(RemoteClientHandler rch, BufferedReader in) {
            this.in = in;
            this.rch = rch;
        }

        @Override
        public void run() {
            try {
                buffer = in.readLine();
                while (!buffer.equals("Close connection")) {
                    System.out.println("[LOG] in RemoteClientHandler : Received : " + buffer);
                    rch.sendData(buffer);
                    buffer = in.readLine();
                }
            } catch (IOException e) {
                System.out.println("[ERROR] in RemoteClientHandler : " + e);
            }
        }
    }

    // Add data to send in the buffer of the sender thread
    public void sendData(String content) {
        System.out.println("[LOG] in RemoteClientHandler : sending " + content);
        this.sender.dataToSend = content;
    }

    // Retrieve data in the buffer of the receiver Thread
    public String getBufferedData() {
        return this.receiver.buffer;
    }
}
