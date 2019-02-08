import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

public class Query
{
	String fullQuery, queryId;
	
	public Query(String fullQuery)
	{
		this.fullQuery = fullQuery;
		
		StringTokenizer tokens = new StringTokenizer(fullQuery, ":");
		
		for (int i = 0; i < 2; i++)
			tokens.nextToken();		
		
		this.queryId = tokens.nextToken();
	}
	
	public String getFullQuery(Map<String, Term> termDic)
	{	
		//Set the current term to the one give by query
		Term current = termDic.get(queryId);
			
		String returnQuery = "[query_answer]";	
		returnQuery += "\n[Term]\n" + current.getContent();
		
		ArrayList<Term> currentParents = current.getParents();
		
		while (currentParents.isEmpty() == false)
		{
			current = currentParents.get(0);
			
			returnQuery += "\n[Term]\n" + current.getContent();
			
			currentParents = current.getParents();
		}
		
		return returnQuery + "\n";
	}

	public String getFullQuery()
	{
		return fullQuery;
	}

	public String getQueryId()
	{
		return queryId;
	}
}
