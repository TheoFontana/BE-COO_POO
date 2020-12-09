import java.io.*;
import java.net.*;
import java. util.*;




public class Chat implements Runnable {
	
	final ServerSocket serveur;
	final Socket client;
	final BufferedReader in;
	final PrintWriter out;
	final Scanner scan = new Scanner(System.in);
	
	try {
		
		serveur = new ServerSocket(5000);
		client = serveur.accept();
		out = new PrintWriter(client.getOutputStream());
		in = new BufferedReader(new InputStreamReader (client.getInputStream());
		Thread envoi = new Thread();
		envoi.start();
		String msg;
		
		public void run() {
			while(true) {
				msg = scan.nextLine();
				out.println(msg);
				out.flush();
			}
		}
		
		
		Thread recois = new Thread();
		recois.start();
		String msg;
		
		public void run() {
			try {
				msg = in.readLine();
				
				while(msg!=null) {
					System.out.println("Client:" msg);
					msg = in .readLine();
				}
				
				out.close();
				client.close();
				serveur.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


-----------------------------------------------------------


import java.io.*;
import java.net.*;
import java.util.*;


public class ChatClient implements Runnable {
	
	final Socket client;
	final BufferedReader in;
	final PrintWriter out;
	final scan = new Scanner(System.in);
	
	try {
		// Adresse local en attendant savoir comment recuperer adresse du serveur
		client = new Socket("127.0.0.1", 5000);
		
		out = new PrintWriter(client.getOutputStream());
		
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
		Thread envoyer = new Thread();
		envoyer.start();
		
		String msgenv;
		
		public void run() {
			while(true) {
				msgenv = sc.nextLine();
				out.println(msgenv);
				out.flush();
			}
		}
		
		
		Thread recevoir = new Thread();
		
		String msgrc;
		
		public void run() {
			try {
				msgrc = in.readLine();
				while(msgrc != null) {
					System.out.println("Serveur:" + msgrc);
					msgrc = in.readLine();
				}
				out.close();
				client.close();
				
				} catch (IOEXception e) {
					e.printStackTrace();
				}
		}
		
	}
}