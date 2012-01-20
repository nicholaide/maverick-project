import java.io.IOException;
import java.io.*;
import java.util.*;

public class PeerApplication {

	private Peer nace;
	private int hostPort;
	
	
	public PeerApplication(int portnumber)
	{
		this.nace = new Peer("Alice");
		this.hostPort = portnumber;
		Thread t = new SocketListenThread(nace,hostPort);
		t.start();

	}
	
	
	public void getInput(String destIP,int destport,String m){
		Message sendpacket = new Message(destIP,destport,m);
		try{
		nace.setupConnectionToProcess(destIP, destport, m);
		} catch(IOException e){
			System.out.println("Can't setup connection");
			e.printStackTrace();
		}
		
		}
	
	public static void main(String[] args)
	{
	   String destIP = null;
	   int destport = 0;
	   String m = null;
	   int sourceport = 4800;	
	   BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
       PeerApplication app = new PeerApplication(sourceport);	
	   while(true)
	   { System.out.println("Enter sending message parameters....");
	   try {
	   destIP = stdin.readLine();
	   } catch(IOException e){
		   System.out.println("Problem reading");
		   e.printStackTrace();
	   }
	   
	   try {
		   destport = Integer.parseInt( stdin.readLine());
		   } catch(IOException e){
			   System.out.println("Problem reading");
			   e.printStackTrace();
		   }
		   
		   try {
			   m = stdin.readLine();
			   } catch(IOException e){
				   System.out.println("Problem reading");
				   e.printStackTrace();
			   }
	 	app.getInput(destIP, destport, m);
	   
	   }
	}
	

}


