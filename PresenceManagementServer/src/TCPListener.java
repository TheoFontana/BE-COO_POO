import java.lang.System;
import java.net.*;
// Listen for incoming tcp connections 
class TCPListener extends Thread {

    private PresenceManagementServer pms;

	// Entry socket for clients to connect to
	private ServerSocket serverSocket;
	// Port associated with the server socket
	private int serverPort;
    
    public TCPListener(PresenceManagementServer pms, int port) {
        this.pms = pms;
        this.serverPort = port;
    }

    @Override
    public void run() {
        this.startServer();
    }

    private void startServer() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort); 
            System.out.println("[LOG] in TCPListener startServer : Started listening on port " + this.serverPort);
            while (true) {
                // Accept all incoming connections 
                Socket connection = this.serverSocket.accept();
                this.handleConnection(connection);
            }
        } catch (Exception ex) {
            System.out.println("[ERROR] in TCPListener startServer : " + ex.toString());
        }
    }

    // Handles an incoming connection
    public void handleConnection(Socket connection) {
        //System.out.println("[LOG] in TCPListener : Handling a new connection " + connection);
    }
}
