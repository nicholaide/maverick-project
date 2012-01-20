import java.io.*;
import java.net.*;



public class SampleClient {

static private int count = 0;
private int id = 0;
private int portno;
Socket kkSocket ;
PrintWriter out ;
BufferedReader in ;
public SampleClient(int portnumber)
{   count ++;
    id = count;
	portno = portnumber;
	kkSocket = null;
	out = null;
	in = null;
	try {
		setupConnection(portno);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Client Can't Connect to Server");
		e.printStackTrace();
	}
}
	
public void setupConnection(int portnumber) throws IOException {

    try {
        kkSocket = new Socket("127.0.0.1", portnumber);
        out = new PrintWriter(kkSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
    } catch (UnknownHostException e) {
        System.err.println("Don't know about host: taranis.");
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Couldn't get I/O for the connection to: taranis.");
        System.exit(1);
    }


 

   
	
	
}

@Override
public String toString()
{
	return ("Client"+id+" ");
}
public void sendMessage(String message)
{
System.out.println(this.toString()+message);
out.println(message);

	
}
	
public void receiveMessage(){
	
	String rxMessage = null;
	try {
		rxMessage = in.readLine();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("Can't read from server");
		e.printStackTrace();
	}
	
	if(rxMessage.equals("Bye."))
	this.closeConnection();
	else
	System.out.println("Server: "+rxMessage);
	
	
}

public void closeConnection(){
	
	System.out.println(this.toString()+" is Closing Connection");
	 out.close();
	 try {
		in.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		System.out.println("in Stream : Failed to close connection");
		e.printStackTrace();
	}
	
	try{
     kkSocket.close();
	} catch (IOException e) {
	
		System.out.println("socket Stream : Failed to close connection");
		e.printStackTrace();
	}
	}
}

	

