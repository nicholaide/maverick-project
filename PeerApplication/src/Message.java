import java.io.Serializable;

public class Message implements Serializable {

	 
	 private int messageId = 0;
	 private String message;
	 private String src;	
	 private String dest;
	 private String kind;
	 
	 public Message(String src,String dest, String kind,Object data)
	 {
		 this.src = src;
		 this.dest = dest;
		 this.kind = kind;
		 this.message = (String)data;
		
	 }

	
	 public String getSrc()
	 {
		 return this.src;
	 }
	 	 
	 public String getData()
	 {
		 return this.message;
	 }
	 
	 public String getDest()
	 {
		 return this.dest;
	 }
	 
	 public String getKind()
	 {
		 return this.kind;
	 }
	 public int getID()
	 {
		 return this.messageId;
	 }
	 public void set_id(int id){
		this.messageId = id; 
		 }
}
