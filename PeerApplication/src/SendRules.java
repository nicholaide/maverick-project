import java.util.List;
import java.util.Map;


public class SendRules {


	private List<Object> list;
	public SendRules(List<Object> configList){
		list = configList;
	}
	
	/*ID parameter must come from MessagePasser;
	should this return on action rather than a boolean?
	*/
	public String checkSendRuleMatch(String src,String dest,String kind, int N, int id){
		
		//iterate over the configuration list
		for (int i = 0; i < list.size(); i++) {
		
			boolean b_src = this.checkSrc(i, src);
			boolean b_dest = this.checkDest(i, dest);
			boolean b_kind = this.checkKind(i, kind);
			boolean b_id = this.checkID(i,id);
			boolean b_N = this.checkN(i, N);
			
			if (b_src && b_dest && b_kind && b_id && b_N)
			{
			  return this.getAction(i);
			  
			}
		}

		//no rule was found
		return "NOP";
	
	}
	
	private String getAction(int listItem)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		return (String)o.get("Action");
	}
	
	private boolean checkSrc(int listItem, String src)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
			if (((String)o.get("Src")).equals(src))
			     return true;
			
			/* Nulls are wild cards, and are returned 
			 * as true (i.e. they match anything) */
			else if (o.get("Src").equals(null))
				 return true;		
			else
				return false;
		
	}

   
	
	private boolean checkDest(int listItem, String dest)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		if (((String)o.get("Dest")).equals(dest))
		     return true;
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		else if (o.get("Dest").equals(null))
			 return true;		
		else
			return false;
	}
	
	private boolean checkKind(int listItem, String kind)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		if (((String)o.get("Kind")).equals(kind))
		     return true;
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		else if (o.get("Kind").equals(null))
			 return true;		
		else
			return false;
	}
	
	
	private boolean checkID(int listItem, int id)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		if (((String)o.get("ID")).equals(id))
		     return true;
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		else if (o.get("ID").equals(null))
			 return true;		
		else
			return false;
	}
	
	private boolean checkN(int listItem, int N)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		if (((String)o.get("Nth")).equals(N))
		     return true;
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		else if (o.get("Nth").equals(null))
			 return true;		
		else
			return false;
	}
	
}
	
	
	

