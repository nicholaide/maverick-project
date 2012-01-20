import java.net.Socket;


public class SocketListenThread extends Thread {
	 
	  Peer peer = null;
	  int port = 0;
	  public SocketListenThread(Peer p,int pnumber) {
			super("SocketListenThread");
	        this.peer = p;
	        this.port = pnumber;
		    }

	  public void run()
	  {
		  while(true)
		  peer.setupConnectionAsProcess(port);
	  }
}
