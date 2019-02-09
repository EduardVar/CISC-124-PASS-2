import java.util.ArrayList;
import java.util.StringTokenizer;

public class Term
{
	String fullId = "", id = "";
	String content = "";
 
	ArrayList<String> altIds = new ArrayList<>();
	ArrayList<Term> parents = new ArrayList<>();
	
	public Term()
	{
	}
	
	public boolean setId(String fullId)
	{
		this.fullId = fullId;
		
		StringTokenizer tokens = new StringTokenizer(fullId, ":");
		
		for (int i = 0; i < 2; i++)
			tokens.nextToken();		

		this.id = tokens.nextToken();
		
		return true;
	}
	
	public void addNewAltId(String addFullAltId)
	{
		altIds.add(getOnlyId(addFullAltId));
	}
	
	public String getOnlyId(String fullAltId)
	{
		StringTokenizer tokens = new StringTokenizer(fullAltId, ":");
		
		for (int i = 0; i < 2; i++)
			tokens.nextToken();		
		
		String current = tokens.nextToken();
		
		return current.substring(0, current.indexOf(" "));
	}
	
	public void addParent(Term parent)
	{
		parents.add(parent);
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}

	public String getId()
	{
		return id;
	}

	public String getContent()
	{
		return content;
	}

	public ArrayList<String> getAltIds()
	{
		return altIds;
	}
	
	public ArrayList<Term> getParents()
	{
		return parents;
	}
}
