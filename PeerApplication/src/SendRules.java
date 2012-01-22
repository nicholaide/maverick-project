import java.util.List;
import java.util.Map;


public class SendRules {


	private List<Object> list;
	static int ruleCount[];
	public SendRules(List<Object> configList){
		ruleCount = new int[50];
		for(int i=0;i<50;i++)
			ruleCount[i] = 0;
		
		list = configList;
	}
	
	/*ID parameter must come from MessagePasser;
	should this return on action rather than a boolean?
	*/
	public String checkSendRuleMatch(String src,String dest,String kind,int id){
		
		boolean b_src = false;
		boolean b_dest = false;
		boolean b_kind = false;
		boolean b_id = false;
		boolean b_N = false;
		//iterate over the configuration list
		for (int i = 0; i < list.size(); i++) {
		
			b_src = this.checkSrc(i, src);
			b_dest = this.checkDest(i, dest);
			b_kind = this.checkKind(i, kind);
			b_id = this.checkID(i,id);
	//		boolean b_N = this.checkN(i, N);
			
			if (b_src && b_dest && b_kind && b_id)
			{
			  ruleCount[i] ++;	
			  break;
			  
			}
					
			
		}

		
		for (int i = 0; i < list.size(); i++) {
			
			b_src = this.checkSrc(i, src);
			b_dest = this.checkDest(i, dest);
			b_kind = this.checkKind(i, kind);
			b_id = this.checkID(i,id);
	        b_N = this.checkN(i, this.getN(i));
		
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
		if (((Integer)o.get("ID"))==id)
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
		if (((Integer)o.get("Nth"))==N)
		     return true;
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		else if (o.get("Nth").equals(null))
			 return true;		
		else
			return false;
	}
	
	private int getN(int listItem){
	Map<String, Object> o = (Map<String, Object>)list.get(listItem);
	return (Integer)o.get("Nth");
	
	}
	}
	

	
	
	

