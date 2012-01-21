import java.util.List;
import java.util.Map;


public class ReceiveRules {

	private List<Object> list;
	public ReceiveRules(List<Object> configList){
		list = configList;
	}
	
	public String getIP(String name)
	{
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> o = (Map<String, Object>)list.get(i);
			if (((String)o.get("Name")).equals(name))
			     return (String)o.get("IP");
			
			
		}
		
		return "Not Found";
	}

    public int getPort(String name)
    {
    	
    	for (int i = 0; i < list.size(); i++) {
			Map<String, Object> o = (Map<String, Object>)list.get(i);
			if (((String)o.get("Name")).equals(name))
			     return Integer.parseInt((String)o.get("Port"));
			
		}
		
		return -1;
    	
    	
    	
    }
	
	
	
}