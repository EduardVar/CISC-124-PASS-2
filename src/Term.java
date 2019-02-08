import java.util.ArrayList;
import java.util.StringTokenizer;

public class Term
{
	String fullId = "", id = "";
	
	ArrayList<String>  fullAlts = new ArrayList<>(); 
	ArrayList<String> altIds = new ArrayList<>();
	
	String content = "";
	
	//ADD POINTERS AS ARRAY FOR PARENTS AND CHILDREN!
	ArrayList<Term> parents = new ArrayList<>();
	
	public Term()
	{
	}
	
	public void setId(String fullId)
	{
		this.fullId = fullId;
		
		StringTokenizer tokens = new StringTokenizer(fullId, ":");
		
		for (int i = 0; i < 2; i++)
			tokens.nextToken();		

		this.id = tokens.nextToken();
	}
	
	public void addNewAltId(String addFullAltId)
	{
		fullAlts.add(addFullAltId);
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
	
	public String getFullTerm()
	{
		String fullTerm = fullId + "\n" + content;
		
		if (fullAlts.isEmpty() == false)
			for (int i = 0; i < fullAlts.size(); i++)
				fullTerm += (i == 0) ? fullAlts.get(i) : "\n" + fullAlts.get(i);
		
		return fullTerm + "\n";
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
