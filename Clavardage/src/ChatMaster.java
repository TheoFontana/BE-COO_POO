import java.io.*;
import java.net.*;
import java.lang.System;

// Class implementing chat networking logic, receive tcp connections from 
// other users, connect to other users
public class ChatMaster extends Thread {
	
	// Entry socket for clients to connect to
	private ServerSocket serverSocket;
	// Port associated with the server socket
	private int serverPort;
	// Indicates if the server thread is running
	private boolean running;
	// Number of simultaneously connected clients
	private int connectedClients;
	// Reference to the local user of the application
	private User localUser = null;

	// Constructor
	public ChatMaster(int port) {
		this.serverPort = port;
	}

	// Override run() method of class Thread
	@Override
	public void run() {
		this.startServer();
	}

	// Starts the server
	private void startServer() {
		try {
			// Start listening
			this.serverSocket = new ServerSocket(this.serverPort);
			System.out.println("[LOG] Started listening on port " + Integer.toString(this.serverPort));
			this.running = true;
			// Infinite loop to accept connections from other users
			while(running){
				Socket connection = this.serverSocket.accept();
				// Create a new Client thread to handle this connection
				new ClientHandler(connection).start();
			}
		} catch (IOException exception) {
			System.out.println("[ERROR] Couldn't bind port " + Integer.toString(this.serverPort) + " for listening!");
			System.exit(-1);
		}
	}

	// Spawns a client to connect to another user
	public ChatClient openConnection(String IP) {
		return new ChatClient(IP, this.serverPort);
	}

	// Is the server running ?
	public boolean isRunning() {
		return this.running;
	}

	// Stops the server
	public void stopServer() {
		this.running = false;
	}

	// Getters + Setters
	public void setLocalUser(User user) {
		this.localUser = user;
	}

	public User getLocalUser() {
		return this.localUser;
	}

}