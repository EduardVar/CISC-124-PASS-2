/**
 * Author:	Eduard Varshavsky
 * NetID:	18ev
 * Date:	February 19, 2019
 * Desc:	Class meant to store query details and query functionality to find
 * 			the answer to a query
 */

//Imports utility packages to use within class
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

public class Query
{
	// *** CLASS VARIABLES ***
	
	//Stores the query ID number inside a String.
	String queryId;

	// *** CONSTRUCTOR METHODS ***
	
	/**
	 * Constructor used to create a new Query object. Sets the queryId inside
	 * @param fullQuery is a String of the entire line of the query
	 */
	public Query(String fullQuery)
	{
		//Splits the given query line into tokens split by ':'
		StringTokenizer tokens = new StringTokenizer(fullQuery, ":");
		
		//Loops through twice to get to the required token
		for (int i = 0; i < 2; i++)
			//Gets the next token out of tokens
			tokens.nextToken();		
		
		//Sets the query ID to the third token in the sequence
		this.queryId = tokens.nextToken();
	}
	
	// *** OBJECT-BEHAVIOUR METHODS ***
	
	/**
	 * This function gets all the Query information needed to format an answer
	 * @param termDic is a hashmap with a String key of an ID and a Term object
	 * for the corresponding Term associated with that ID
	 * @return a String of the entire query answer
	 */
	public String getFullQuery(Map<String, Term> termDic)
	{	
		//Set the current term to the one give by query id using the dictionary
		Term current = termDic.get(queryId);
			
		//Creates a String for the content of the query answers, adds a heading
		String returnQuery = "[query_answer]";	
		
		//Adds term heading and adds content of current term to returnQuery
		returnQuery += "\n[Term]\n" + current.getContent();
		
		//Creates a new list of terms to store the is_a IDs of the current term
		ArrayList<String> currenAlts = current.getAltIds();
		
		//Keeps looping until the current term doesn't have any parents 
		while (currenAlts.isEmpty() == false)
		{
			//Sets the current term to that of their first is_a from termDic
			current = termDic.get(currenAlts.get(0));
			
			//Adds on term heading with current terms content to returnQuery
			returnQuery += "\n[Term]\n" + current.getContent();
			
			//Sets currentParents to the current term's is_a IDs
			currenAlts = current.getAltIds();
		}
		
		//Returns complete answer returnQuery and adds newline for formatting
		return returnQuery + "\n";
	}
	
	// *** MUTATOR METHODS ***
	
	// *** ACCESSOR METHODS ***
}
