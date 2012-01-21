import java.io.*;
import java.util.*;
import java.net.*;

import org.yaml.snakeyaml.Yaml;

public class MessagePasser {
	 private Peer p;
     private Configuration config;
     private String processName;
     private int processPort;
	public MessagePasser(String configuration_filename, String local_name){
	//Creat YAML object and add to three Configuration , SendRules , ReceiveRules
    //Check if YAML file changed...probably need a thread
		
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
	
	public Message createMessage(String destProcess,Object data)
	{
		return (new Message(config.getIP(destProcess),config.getPort(destProcess),data));
	}
	
	public void send(Message message) throws IOException{
		p.setupConnectionToProcess(message.getDstIP(),message.getDstPort(), message);
		
	}
	
	Message receive(){
		Message m = null;
		return m;
	}

	public static void main(String[] args){
		String fileName = null;
		String processName = null;
		String choice = null;
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
			    System.out.println("Enter local name of destination and message") ;
			    try{
					destinationProcess = stdin.readLine();
					} catch(IOException e){
					System.out.println("Could not enter filename");
					e.printStackTrace();
					}
					try{
						msg = stdin.readLine();
						} catch(IOException e){
						System.out.println("Could not enter filename");
						e.printStackTrace();
						}
			
			            try{
						passer.send(passer.createMessage(destinationProcess,msg));
			            } catch(IOException e) {
			            	System.out.println("IO Exception while sending message");
			            	e.printStackTrace();
			            }
			
			}
			else if (choice.equals("R"))
			{
				
				
			}
	}
	
}
