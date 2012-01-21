import java.util.List;
import java.util.Map;


public class SendRules {


	private List<Object> list;
	public SendRules(List<Object> configList){
		list = configList;
	}
	
	
	public boolean checkSendRuleMatch(String src,String dest,String kind,int id){
		
		
		
	}
	
	
	
	public boolean checkSrc(String src)
	{
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> o = (Map<String, Object>)list.get(i);
			if (((String)o.get("Src")).equals(src))
			     return true;
			
			
		}
		
		return false;
	}

   
	
	public boolean checkDest(String Dest)
	{
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> o = (Map<String, Object>)list.get(i);
			if (((String)o.get("Dest")).equals(Dest))
			     return true;
			
			
		}
		
		return false;
	}
	
	public boolean checkKind(String Kind)
	{
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> o = (Map<String, Object>)list.get(i);
			if (((String)o.get("Kind")).equals(Kind))
			     return true;
			
			
		}
		
		return false;
	}
	
	
	public boolean checkID(int id))
	{
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> o = (Map<String, Object>)list.get(i);
			if (((Integer)o.get("ID"))==id)
			     return true;		
			
		}
		
		return false;
	}
	
	
	
}
