import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;


public class Peer {

	static private int count = 0;
	private int id = 0;
	private int portno;
	ServerSocket serverSocket;
	BlockingQueue<Object> queue;
	public Peer()
	{   count ++;
	    id = count;
	    serverSocket = null;
	    queue = new LinkedBlockingQueue<Object>(); 
	
	}
		public void setupConnectionToProcess(String ip,int pnumber, String message) throws IOException{
		   Socket kkSocket = null; 
		   PrintWriter out = null;
           BufferedReader in = null;	
           
           System.out.println("In setupConnectionToProcess");
			 try {
			        kkSocket = new Socket("127.0.0.1", pnumber);
			        out = new PrintWriter(kkSocket.getOutputStream(), true);
			        in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			    } catch (UnknownHostException e) {
			        System.err.println("Don't know about host: taranis.");
			        System.exit(1);
			    } catch (IOException e) {
			        System.err.println("Couldn't get I/O for the connection to:"+ip);
			        System.exit(1);
			        
			    }
			    
			    out.println(message);
	
		}
		
	
		
		public void setupConnectionAsProcess(int pnumber)
		{
			  System.out.println("In setupConnectionASProcess");
			   try {
		            serverSocket = new ServerSocket(pnumber);
		        } catch (IOException e) {
		            System.err.println("Could not listen on port: 4444.");
		            System.exit(-1);
		        }
			 
		        try {
					new MultiServerThread(serverSocket.accept(),queue).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Could not create thread");
					e.printStackTrace();
				}
			 
		}
		
		@Override
		public String toString()
		{
			return ("Matt"+id+" ");
		}
		
	
}
