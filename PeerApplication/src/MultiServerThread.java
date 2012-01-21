
import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

public class MultiServerThread extends Thread {
	
	

	    private Socket socket = null;
	    private BlockingQueue<Message> q = null;
        private String inputline = null;
        private String outputline = null;
        private Message rxMessage = null;
	    public MultiServerThread(Socket socket,BlockingQueue<Message> peerqueue) {
		super("MultiServerThread");
		this.socket = socket;
        this.q = peerqueue;
	    }

	    public void run() {
	    	PrintWriter serverOut = null;
	    	BufferedReader serverIn = null ; 
		try {
     	    serverOut = new PrintWriter(socket.getOutputStream(), true);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            rxMessage = serverIn.readLine();
		    serverIn.
            System.out.println("Trying to put message in queue");
             try {
            	 System.out.println("Putting in queue"+inputline); 
				q.put(inputline);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Can't put in ");
				e.printStackTrace();
			}
		   

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    
	    serverOut.close();
	    try{
	    serverIn.close();
	    } catch(IOException e) {
	        System.out.println("Can't close serverIn");
	    	e.printStackTrace();
	    }
	    }
	    
	    }
	


