import java.util.ArrayList;
import java.util.StringTokenizer;

public class Term
{
	//Created Strings objects to store the term ID and it's text contents
	String id = "", content = "";
 
	//List of String objects to store the is_a IDs of a term
	ArrayList<String> altIds = new ArrayList<>();
	
	/**
	 * Constructor that creates a Term that's empty with nothing set
	 */
	public Term()
	{
	}
	
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
}
