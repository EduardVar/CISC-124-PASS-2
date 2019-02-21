/**
 * Author:	Eduard Varshavsky
 * NetID:	18ev
 * Date:	February 15, 2019
 * Desc:	Class meant to store term attribute and term functionality to set
 * 			and return term attributes
 */

//Imports utility packages to use within class
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Term
{
	// *** CLASS VARIABLES ***
	
	//Created Strings objects to store the term ID and it's text contents
	String id = "", content = "";
 
	//List of String objects to store the is_a IDs of a term
	ArrayList<String> altIds = new ArrayList<>();
	
	// *** CONSTRUCTOR METHODS ***
	
	/**
	 * Constructor that creates a Term that's empty with nothing set
	 */
	public Term()
	{
	}
	
	// *** OBJECT-BEHAVIOUR METHODS ***
	
	/**
	 * Function used to format and add an is_a id to altIds
	 * @param fullAltId is the full line of the is_a ID (includes name and num)
	 */
	public void addNewAltId(String fullAltId)
	{
		//Splits line with ID into tokens using StringTokenizer, split by ':'
		StringTokenizer tokens = new StringTokenizer(fullAltId, ":");
		
		//Loops through twice (to get to the token before the ID)
		for (int i = 0; i < 2; i++)
			//Gets the next token
			tokens.nextToken();	
		
		//Creates a current String object of the last token in tokens
		String current = tokens.nextToken();

		//Adds the substring of current from beginning to the first space (end
		//of ID to the list of is_a IDs
		altIds.add(current.substring(0, current.indexOf(" ")));
	}
	
	// *** MUTATOR METHODS ***
	
	/**
	 * Function used to set the ID of the term object
	 * @param fullId is the line of the term containing the term's ID
	 * @return true to let call know setting the ID was successful
	 */
	public boolean setId(String fullId)
	{
		//Splits line with ID into tokens using StringTokenizer, split by ':'
		StringTokenizer tokens = new StringTokenizer(fullId, ":");
		
		//Loops through twice (to get to the token before the ID)
		for (int i = 0; i < 2; i++)
			//Gets the next token
			tokens.nextToken();		

		//Sets the ID to the final token
		this.id = tokens.nextToken();
		
		//Returns true to show function was successful (used in setting bools)
		return true;
	}
	
	/**
	 * This functions sets the content of the Term object
	 * @param content is a String meant to contains all content of the Term
	 */
	public void setContent(String content)
	{
		//Sets instance content attribute to the parameter version
		this.content = content;
	}
	
	// *** ACCESSOR METHODS ***

	/**
	 * Function to get the Term object's id
	 * @return String of the Term objects id attribute
	 */
	public String getId()
	{
		//Returns the id
		return id;
	}

	/**
	 * Function to get the Term object's content
	 * @return String of the Term objects content attribute
	 */
	public String getContent()
	{
		//Returns the content
		return content;
	}

	/**
	 * Function to get the Term object's altIds (the is_a ids)
	 * @return List of String objects of the Term object's is_a ids
	 */
	public ArrayList<String> getAltIds()
	{
		//Returns the altIds
		return altIds;
	}
}
