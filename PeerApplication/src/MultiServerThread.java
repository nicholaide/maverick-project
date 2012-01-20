
import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;

public class MultiServerThread extends Thread {
	
	

	    private Socket socket = null;
	    private BlockingQueue<Object> q = null;
        private String inputline = null;
        private String outputline = null;
        
	    public MultiServerThread(Socket socket,BlockingQueue<Object> peerqueue) {
		super("MultiServerThread");
		this.socket = socket;
        this.q = peerqueue;
	    }

	    public void run() {

		try {
     	    PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inputline = serverIn.readLine();
		    
            System.out.println("Trying to put message in queue");
             try {
				q.put(inputline);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Can't put in ");
				e.printStackTrace();
			}
		   
		  //  outputLine = "Hi from Server";
		  //  out.println(outputLine);

	/*	    while ((inputLine = in.readLine()) != null) {
			outputLine = inputLine;
			out.println(outputLine);
			if (outputLine.equals("Bye."))
			    break;
		    }
		    out.close();
		    in.close();
		    socket.close(); */

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}


