import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;


public class Peer {

//	static private int count = 0;
//	private int id = 0;
	private int portno;
	private String name;
	ServerSocket serverSocket;
	BlockingQueue<Message> queue;
	public Peer(String processName,int pnumber)
	{//   count ++;
	 //   id = count;
		 try {
	            serverSocket = new ServerSocket(pnumber);
	        } catch (IOException e) {
	            System.err.println(this.toString()+": "+"Could not listen on port: 4444.");
	            System.exit(-1);
	        }
	    queue = new LinkedBlockingQueue<Message>(); 
	    name = processName;
	}
		public void setupConnectionToProcess(String ip,int pnumber, Message message) throws IOException{
		   Socket kkSocket = null; 
		   PrintWriter out = null;
           BufferedReader in = null;	
           
           System.out.println(this.toString()+": "+"In setupConnectionToProcess");
			 try {
			        kkSocket = new Socket(ip, pnumber);
			        out = new PrintWriter(kkSocket.getOutputStream(), true);
			        in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
			    } catch (UnknownHostException e) {
			        System.err.println(this.toString()+": "+"Don't know about host: taranis.");
			        System.exit(1);
			    } catch (IOException e) {
			        System.err.println(this.toString()+": "+"Couldn't get I/O for the connection to:"+ip);
			        System.exit(1);
			        
			    }
			    System.out.println(this.toString()+": "+message.getData());
			    out.println(message);
			    System.out.println(this.toString()+": "+"Closing connections");
	            out.close();
	            in.close();
	            kkSocket.close();
		}
		
	
		
		public void setupConnectionAsProcess(int pnumber)
		{
			  System.out.println(this.toString()+": "+"In setupConnectionASProcess");
			 
		        try {
					new MultiServerThread(serverSocket.accept(),queue).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(this.toString()+": "+"Could not create thread");
					e.printStackTrace();
				}
			 
		}
		
		public Message retrieveMessage()
		{
			try {
				System.out.println(this.toString()+" received: "+(String)queue.take());
			} catch (InterruptedException e) {
				System.out.println(this.toString()+": "+"Retrieval from queue failed");
				e.printStackTrace();
			}
			
			
		}
		@Override
		public String toString()
		{
			return this.name;
		}
		
	
}
