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
	
	public String getFullQuery(Map<String, Term> termDicUp)
	{	
		//Set the current term to the one give by query
		Term current = termDicUp.get(queryId);
			
		String returnQuery = "[query_answer]";	
		returnQuery += "\n" + current.getFullTerm();

		String currAlt = current.getFirstAlt();
		
		while ((termDicUp.get(currAlt).getAltIds()) != null)
		{
			current = termDicUp.get(currAlt);
			
			returnQuery += "\n" + current.getFullTerm();
			
			currAlt = current.getFirstAlt();
		}
		
		returnQuery += "\n" + termDicUp.get(currAlt).getFullTerm();
		
		return returnQuery;
	}
	
	public String getMaxPath()
	{
		String outString = "";
		
		return outString;
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
