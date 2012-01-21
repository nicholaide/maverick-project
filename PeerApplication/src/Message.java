import java.io.Serializable;

public class Message implements Serializable {

	 static int count = 0;
	 private int messageId = 0;
	 private String destinationIP;
	 private int destinationPort;
	 private String message;
		
	
	 public Message(String destIP, int destPort, Object data)
	 {
		 this.destinationIP = destIP ;
		 this.destinationPort = destPort;
		 this.message = (String)data;
		 count++;
		 set_id(count);
	 }

	 public String getDstIP()
	 {
		 return this.destinationIP;
	 }
	 
	 public int getDstPort()
	 {
		 return destinationPort;
	 }
	 
	 /*this is the data- to be changed to Object (data) */
	 public String getObject()
	 {
		 return message;
	 }
	 
	 public String getData()
	 {
		 return this.message;
	 }
	 
	 public void set_id(int id){
		this.messageId = id; 
		 
	 }
}
