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
	
	
	//NEW
	public SendRules()
	{
		ruleCount = new int[50];
		for(int i=0;i<50;i++)
			ruleCount[i] = 0;
		
	}
	
	
	//NEW
	public void setRules(List<Object> configList)
	{
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
			System.out.println("Outside n"+b_src+b_dest+b_kind+b_id);
			if (b_src && b_dest && b_kind && b_id)
			{
			  	
			  ruleCount[i] ++;	
			  System.out.println("Incremented "+i+" rule to "+ruleCount[i]);
			  break;
			  
			}
					
			
		}

		
		for (int i = 0; i < list.size(); i++) {
			System.out.println("In 2nd loop");
			b_src = this.checkSrc(i, src);
			b_dest = this.checkDest(i, dest);
			b_kind = this.checkKind(i, kind);
			b_id = this.checkID(i,id);
	        b_N = this.checkN(i, this.getN(i));
		
	        if (b_src && b_dest && b_kind && b_id && b_N)
			{ 		System.out.println("In 2nd loop match");
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
			
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		if (o.get("Src")==(null))
			 return true;
		
		
		
		else if (((String)o.get("Src")).equals(src))
			     return true;
			
				
			else
				return false;
		
	}

   
	
	private boolean checkDest(int listItem, String dest)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		if (o.get("Dest")==(null))
			 return true;	
		
		
		else if (((String)o.get("Dest")).equals(dest))
		     return true;
			
		else
			return false;
	}
	
	private boolean checkKind(int listItem, String kind)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		if (o.get("Kind")==(null))
			 return true;		
		
		
		else if (((String)o.get("Kind")).equals(kind))
		     return true;
		
		
		else
			return false;
	}
	
	
	private boolean checkID(int listItem, int id)
	{
		Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		if (o.get("ID")==null)
			 return true;
		
		else if (((Integer)o.get("ID"))==id)
		     return true;		
		else
			return false;
	}
	
	private boolean checkN(int listItem, int N)
	{
       Map<String, Object> o = (Map<String, Object>)list.get(listItem);
		
		/* Nulls are wild cards, and are returned 
		 * as true (i.e. they match anything) */
		if (o.get("Nth")==null)
			return true;		
		
		else if (((Integer)o.get("Nth"))==N)
		     return true;
		
	
		else
			return false;
	}
	
	private int getN(int listItem){
	
	return ruleCount[listItem];
	
	}




}
	

	
	
	

