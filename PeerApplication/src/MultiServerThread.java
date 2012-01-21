
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
	    	ObjectOutputStream serverOut = null;
	    	
	    	ObjectInputStream serverIn = null ; 
		try {
     	    serverOut = new ObjectOutputStream(socket.getOutputStream());
            serverOut.flush();
     	    serverIn = new ObjectInputStream(socket.getInputStream());
            
            
            try {
				rxMessage = (Message)serverIn.readObject();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("Object Class not found");
				e1.printStackTrace();
			}
           
            System.out.println("Trying to put message in queue");
             try {
            	 System.out.println("Putting in queue "+rxMessage.getData()); 
				q.put(rxMessage);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Can't put in ");
				e.printStackTrace();
			}
		   

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    
		
		
		try{
		    serverOut.close();
		    } catch(IOException e) {
		        System.out.println("Can't close serverOut");
		    	e.printStackTrace();
		    }
		
		
	
	    try{
	    serverIn.close();
	    } catch(IOException e) {
	        System.out.println("Can't close serverIn");
	    	e.printStackTrace();
	    }
	    }
	    
	    }
	


