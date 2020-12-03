import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Chat {
	
	public static void main(String[] args) {

	        try {
	            ServerSocket ss = new ServerSocket(1234);

	            while(true) {
	                System.out.println("Awaiting connection");
	                Socket link = ss.accept();
	                new Thread(new Runner(link)).start();
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }


	    }



}


class Runner implements Runnable {

	    final Socket link;

	    public Runner(Socket link) {
	        this.link = link;
	    }

	    @Override
	    public void run() {
	        System.out.println("Thread started");
	        try {
	            InputStream is = link.getInputStream();
	            int rcv = 0;
	            while ((rcv = is.read()) != 0) {
	                System.out.println("Received: "+rcv);
	            }
	            System.out.println("Finishing thread");
	            is.close();
	            link.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
	



