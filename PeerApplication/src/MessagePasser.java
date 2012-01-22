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
	public MessagePasser(String configuration_filename, String local_name){
	//Creat YAML object and add to three Configuration , SendRules , ReceiveRules
    //Check if YAML file changed...probably need a thread
		msgcount = 0;
		filename = configuration_filename;
		localname = local_name;
	    delayQ = new LinkedBlockingQueue<Message>();
	    delayreceiveQ = new LinkedBlockingQueue<Message>(); 
		try {
			config = new Configuration(this.parseYamlConfig(configuration_filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not find file");
			e.printStackTrace();
		}
		this.processName = local_name;
		this.processPort = config.getPort(local_name);
	
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
		sendRules = new SendRules(this.parseYamlSendRules(filename));
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
				delayedMessage = delayQ.poll();
				p.setupConnectionToProcess(config.getIP(delayedMessage.getDest()),config.getPort(delayedMessage.getDest()),message);
			}
				
			
		}
		
		else if ((action.equals("delay")))
		{   
			
			System.out.println("Delaying packets");
			delayQ.add(message);
			
		}
		
		
		p.setupConnectionToProcess(config.getIP(message.getDest()),config.getPort(message.getDest()),message);
	    
		while(delayQ.size()>0){
			delayedMessage = delayQ.poll();
			p.setupConnectionToProcess(config.getIP(delayedMessage.getDest()),config.getPort(delayedMessage.getDest()),message);
		}
	}
	
	Message receive(){
		Message message = p.retrieveMessage();
		String action = null;
		Message delayedMessage = null;
		System.out.println("checking against Receive Rules"); 
		try{
		receiveRules = new ReceiveRules(this.parseYamlReceiveRules(filename));
		} catch(FileNotFoundException e){
			System.out.println("File not found");
			e.printStackTrace();
		}
		action = receiveRules.checkReceiveRuleMatch(message.getSrc(),message.getDest(),message.getKind(),message.getID());
		
		if(action.equals("drop"))
		{
			System.out.println("Dropping received message");
			return null;
		}
		
		else if ((action.equals("duplicate")))
		{   
			
			System.out.println("Receive Duplicating packets");
			System.out.println(message.getData());
			
			while(delayreceiveQ.size()>0){
				delayedMessage = delayreceiveQ.poll();
				System.out.println(message.getData());	
			}
		      		
			return message;
		}
		
		else if ((action.equals("delay")))
		{   
			
			System.out.println("Receive Delaying packets");
			delayQ.add(message);
			
		}
		
		
		return message;
	
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
				System.out.println("Application Received "+(passer.receive()).getData());
				
			}
	}
	
}
