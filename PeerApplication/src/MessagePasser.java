import java.io.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;

import org.yaml.snakeyaml.Yaml;

public class MessagePasser {
	 private Peer p;
     private Configuration config;
     private String processName;
     private int processPort;
     private SendRules sendRules;
     private ReceiveRules receiveRules; 
     private String filename;
     private String localname;
     private static int msgcount ;
     private BlockingQueue<Message> delayQ;
     private BlockingQueue<Message> delayreceiveQ;
     
     private long fileModifiedTime = 0;
     private File configFile;
     
	public MessagePasser(String configuration_filename, String local_name) {
	//Create YAML object and add to three Configuration , SendRules , ReceiveRules
    //Check if YAML file changed...probably need a thread
		msgcount = 0;
		filename = configuration_filename;
		localname = local_name;
	    delayQ = new LinkedBlockingQueue<Message>();
	    delayreceiveQ = new LinkedBlockingQueue<Message>(); 
		try {
			config = new Configuration(this.parseYamlConfig(configuration_filename));
			
			//NEW
			configFile = new File(filename);
		} catch (FileNotFoundException e) {
		
			System.out.println("Could not find file");
			e.printStackTrace();
		}
		
		//NEW
		try {
			fileModifiedTime = configFile.lastModified();
		}
		catch (Exception e)
		{
			System.out.println("Could not determine file's last modification time");
			e.printStackTrace();
		}
		
		this.processName = local_name;
		this.processPort = config.getPort(local_name);
		
		
		/**********NEW
		 */
		sendRules = new SendRules();
		receiveRules = new ReceiveRules();
		/*********/
	
		p = new Peer(processName,processPort);
		 Thread t = new SocketListenThread(this.p,this.processPort);
		 t.start();
	
	}
	
	public List<Object> parseYamlConfig(String file) throws FileNotFoundException {
		 InputStream input = new FileInputStream(new File(file));
		 Yaml yaml = new Yaml();
		 Map<String, Object> object = (Map<String, Object>) yaml.load(input);
		 List<Object> l =(List<Object>)object.get("Configuration");
		 return l;
	}
	
	public List<Object> parseYamlSendRules(String file) throws FileNotFoundException {
		 InputStream input = new FileInputStream(new File(file));
		 Yaml yaml = new Yaml();
		 Map<String, Object> object = (Map<String, Object>) yaml.load(input);
		 List<Object> l =(List<Object>)object.get("SendRules");
		 return l;
	}
	
	public List<Object> parseYamlReceiveRules(String file) throws FileNotFoundException {
		 InputStream input = new FileInputStream(new File(file));
		 Yaml yaml = new Yaml();
		 Map<String, Object> object = (Map<String, Object>) yaml.load(input);
		 List<Object> l =(List<Object>)object.get("ReceiveRules");
		 return l;
	}
	
	public Message createMessage(String destProcess,String kind,Object data)
	{
		return (new Message(localname,destProcess,kind,data));
	}
	
	public void send(Message message) throws IOException{
	    msgcount ++;
		String action = null;
		Message delayedMessage = null;
		System.out.println("checking against Send Rules"); 
		
		//sendRules = new SendRules(this.parseYamlSendRules(filename));
		
		//NEW
		if (fileChanged())
		{   System.out.println("In file changed");
			sendRules = new SendRules(this.parseYamlSendRules(filename));
		}
		
		else
		{   System.out.println("In else");
			sendRules.setRules(this.parseYamlSendRules(filename));
		}
		
		
		message.set_id(msgcount);
		action = sendRules.checkSendRuleMatch(message.getSrc(),message.getDest(),message.getKind(),message.getID());
		
		if(action.equals("drop"))
		{
			System.out.println("Dropping message");
		}
		
		else if ((action.equals("duplicate")))
		{   
			
			System.out.println("Duplicating packets");
			p.setupConnectionToProcess(config.getIP(message.getDest()),config.getPort(message.getDest()),message);
			p.setupConnectionToProcess(config.getIP(message.getDest()),config.getPort(message.getDest()),message);
			
			while(delayQ.size()>0){
				System.out.println("Flushing queue");
				delayedMessage = delayQ.poll();
			p.setupConnectionToProcess(config.getIP(delayedMessage.getDest()),config.getPort(delayedMessage.getDest()),delayedMessage);
			}
				
			
		}
		
		else if ((action.equals("delay")))
		{   
			
			System.out.println("Delaying packets");
			delayQ.add(message);
			
		}
		
	
		else if ((action.equals("NOP")))
		{   
			System.out.println("In NOP");
		p.setupConnectionToProcess(config.getIP(message.getDest()),config.getPort(message.getDest()),message);
	    
		while(delayQ.size()>0){
			System.out.println("Flushing queue");
			delayedMessage = delayQ.poll();
			p.setupConnectionToProcess(config.getIP(delayedMessage.getDest()),config.getPort(delayedMessage.getDest()),delayedMessage);
		}
	
		}
	
	
	}
	
	
	//NEW
	private boolean fileChanged() {
		long newModifiedTime = 0;
		
		try {
			newModifiedTime = configFile.lastModified();
		}
		catch (Exception e){
			System.out.println("Could not determine file's last modification time");
			e.printStackTrace();
		}
		
                 if(this.fileModifiedTime != newModifiedTime)
                    this.fileModifiedTime = newModifiedTime;
		
		return !(this.fileModifiedTime==newModifiedTime);
	}

	Message receive(){
		Message message = p.retrieveMessage();
		String action = null;
		Message delayedMessage = null;
		System.out.println("checking against Receive Rules"); 
		
		
		//NEW
		if (fileChanged()) {
			
			try{
			receiveRules = new ReceiveRules(this.parseYamlReceiveRules(filename));
			} catch(FileNotFoundException e){
				System.out.println("File not found");
				e.printStackTrace();
			}
		}
		else {
			try{
				receiveRules.setRules(this.parseYamlReceiveRules(filename));
			} catch(FileNotFoundException e){
				System.out.println("File not found");
				e.printStackTrace();
			}
		
		}
		
		/*try{
		receiveRules = new ReceiveRules(this.parseYamlReceiveRules(filename));
		} catch(FileNotFoundException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
		*/
		action = receiveRules.checkReceiveRuleMatch(message.getSrc(),message.getDest(),message.getKind(),message.getID());
		
		if(action.equals("drop"))
		{
			System.out.println("Dropping received message");
			return message;
		}
		
		else if ((action.equals("duplicate")))
		{   
			
			System.out.println("Receive Duplicating packets");
			System.out.println(message.getData());
			System.out.println(message.getData());
			//Put another sys out
			while(delayreceiveQ.size()>0){
				System.out.println("Flushing queue");
				delayedMessage = delayreceiveQ.poll();
				System.out.println(delayedMessage.getData());	
			}
		      		
			return message;
		}
		
		else if ((action.equals("delay")))
		{   
			
			System.out.println("Receive Delaying packets");
			delayQ.add(message);
			return message;
			
		}
		
		else{
		System.out.println("NOP");
		System.out.println(message.getData());
		while(delayreceiveQ.size()>0){
			System.out.println("Flushing queue");
			delayedMessage = delayreceiveQ.poll();
			System.out.println(delayedMessage.getData());	
		}
	      	
		return message;
	
		}
		
		
	}

	public static void main(String[] args){
		String fileName = null;
		String processName = null;
		String choice = null;
		String kind = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Starting Application....");
		System.out.println("Please enter file name and local process name");
		try{
		fileName = stdin.readLine();
		} catch(IOException e){
		System.out.println("Could not enter filename");
		e.printStackTrace();
		}
		
		try{
			processName = stdin.readLine();
			} catch(IOException e){
			System.out.println("Could not enter filename");
			e.printStackTrace();
			}
		
		MessagePasser passer = new MessagePasser(fileName,processName);
		while (true)
		{		
		System.out.println("Press S to send message and R to receive message") ;
		try{
			choice = stdin.readLine();
			} catch(IOException e){
			System.out.println("Could not enter filename");
			e.printStackTrace();
			}
			
			if(choice.equals("S"))
			{   String destinationProcess = null;
			    String msg = null;
			    System.out.println("Enter local name of destination and message and kind of message(Ack/Nack)") ;
			    try{
					destinationProcess = stdin.readLine();
					} catch(IOException e){
					System.out.println("Could not enter filename");
					e.printStackTrace();
					}
					try{
						msg = stdin.readLine();
						} catch(IOException e){
						System.out.println("Could not enter message");
						e.printStackTrace();
						}
						try{
							kind = stdin.readLine();
							} catch(IOException e){
							System.out.println("Could not enter kind");
							e.printStackTrace();
							}
			            try{
						passer.send(passer.createMessage(destinationProcess,kind,msg));
			            } catch(IOException e) {
			            	System.out.println("IO Exception while sending message");
			            	e.printStackTrace();
			            }
			
			}
			else if (choice.equals("R"))
			{
				//Check for null Handling heres
				Message m = null;
				m = passer.receive();
				if(m!=null)
				System.out.println("Application Received "+m.getData());
				else
				System.out.println("Application Received Null :"+"packet got delayed");	
			}
	
		}
		
		}
	
}
