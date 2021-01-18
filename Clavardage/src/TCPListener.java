import java.lang.System;
import java.net.*;
// Listen for incoming tcp connections on port 1337
class TCPListener extends Thread {

    private ChatMaster chat;

	// Entry socket for clients to connect to
	private ServerSocket serverSocket;
	// Port associated with the server socket
	private final int serverPort = 1337;
    
    public TCPListener(ChatMaster chat) {
        this.chat = chat;
    }

    @Override
    public void run() {
        this.startServer();
    }

    private void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort); 
            System.out.println("[LOG] in TCPListener : Started listening on port " + this.serverPort);
            while (true) {
                // Accept all incoming connections 
                Socket connection = this.serverSocket.accept();
                this.handleConnection(connection);
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] in TCPListener : " + ex.toString());
        }
    }

    // Handles an incoming connection
    private void handleConnection(Socket connection) {
        System.out.println("[LOG] in TCPListener : handling new connection from " + connection.getInetAddress().getHostAddress());
        ForeignUser user = this.chat.searchUserByIP(connection.getInetAddress().getHostAddress());
        if (user != null) {
            new ClientHandler(connection, this.chat, user).start();
        }
    }
}
