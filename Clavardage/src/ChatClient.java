import java.io.*;
import java.net.*;
import java.lang.System;

// Used to connect to another user and communicate with him
public class ChatClient extends Thread {
    // Socket to the remote application
    private Socket socket;
    // Thread to send data
    private Sender sender;
    // Thread to receive data
    private Receiver receiver;

    public ChatClient(String IP, int port) {
        try {
            this.socket = new Socket(IP, port); 
            System.out.println("[LOG] Connected to " + IP + ":" + port);
        } catch (IOException ex) {
            System.out.println("[ERROR] Couldn't connect to " + IP + ":" + port);
        }
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sender = new Sender(out);
            sender.start();

            receiver = new Receiver(in);
            receiver.start();

            receiver.join();
        } catch (Exception e) {
            System.out.println("[ERROR] (ChatClient) : " + e);
        } finally {
            System.out.println("[LOG] Client " + this.socket + " exited");
            try {
                if (this.socket != null) {
                    this.socket.close();
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