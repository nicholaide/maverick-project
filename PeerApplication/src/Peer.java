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
		   ObjectOutputStream out = null;
		   ObjectInputStream in = null;	
           
           System.out.println(this.toString()+": "+"In setupConnectionToProcess");
			 try {
				 System.out.println(this.toString()+": "+ip+" "+pnumber);
				    System.out.println(this.toString()+": "+"Before Socket");
			        kkSocket = new Socket(ip, pnumber);
			        System.out.println(this.toString()+": "+"Before out");
			        out = new ObjectOutputStream(kkSocket.getOutputStream());
			        out.flush();
			        System.out.println(this.toString()+": "+"Before in");
			        in = new ObjectInputStream(kkSocket.getInputStream());
			    } catch (UnknownHostException e) {
			        System.out.println(this.toString()+": "+"Don't know about host: taranis.");
			        System.exit(1);
			    } catch (IOException e) {
			        System.out.println(this.toString()+": "+"Couldn't get I/O for the connection to:"+ip);
			        System.exit(1);
			        
			    }
			    System.out.println("Before sending data");
			    System.out.println(this.toString()+": "+message.getData());
		
			    
			    out.writeObject(message);
			    out.flush();
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
			Message m = null;
			try {
				 m = queue.take();
			} catch (InterruptedException e) {
				System.out.println(this.toString()+": "+"Retrieval from queue failed");
				e.printStackTrace();
			}
			

			System.out.println(this.toString()+" retreiving: "+m.getData());
		
			
			return m;
			
		}
		@Override
		public String toString()
		{
			return this.name;
		}
		
	
}
