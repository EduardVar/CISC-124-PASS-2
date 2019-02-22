/**
 * Author:	Eduard Varshavsky
 * NetID:	18ev
 * Date:	February 15, 2019
 * Desc:	Main class of the project mean to execute most of the logic for
 * 			reading, writing, and solving HPO queries and tasks
 */

//Imports utility and file IO packages to use within class
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HPOExplorer
{	
	//List that stores Query objects and is accessible anywhere in the program
	private static ArrayList<Query> queries = new ArrayList<>();
	
	//Hash Map with String keys corresponding to Term values, used to store
	//Term objects with the key being their ID
	private static Map<String, Term> termDic = new HashMap<>();
	
	/**
	 * This function is used to read in the data from the HPO file and sort
	 * into Term object that will be sorted in the termDic HashMap object
	 * Doesn't have any parameters or return variables
	 */
	public static void readInHPO()
	{
		//Creates a new File object hpoFile that reads in the HPO.txt text file
		File hpoFile = new File("HPO.txt");
		
		//Creates a new BufferedReader object called br
		BufferedReader br;			
		
		//Tries to execute code in this try block
		try
		{
			//Attempts to make a new BufferedReader using a FileReader object
			//instantiating using the hpoFile File object
			br = new BufferedReader(new FileReader(hpoFile));
			
			//Creates a new blank String to store the current line of the file
			String line = "";
			
			//Boolean created to store if initial read to first term happened
			boolean initialRead = false;
			
			//Keeps looping until the current line read is null
			while (line != null)
			{
				//Keeps looping until initialRead boolean is true
				while (!initialRead)
				{
					//Will set line to the next line in the buffered reader
					line = br.readLine();	
					
					//Sets initialRead to if current line has a term heading
					initialRead = line.equals("[Term]");
				}
				
				//Creates a new Term object addTerm and a new empty String
				//called content to store the term's contents 
				Term addTerm = new Term();
				String content = "";
				
				//Creates boolean isDone to store if term is done being added
				boolean isDone = false;
				
				//Creates boolean valid to store if term is valid to be added
				boolean valid = true;	
				
				//Creates boolean hasId to store if term has had its ID added
				boolean hasId = false;
				
				//Keeps looping until isDone is true (when term is added)
				while (!isDone)
				{	
					//Checks if the line set from reading br is null
					if ((line = br.readLine()) != null)
					{	
						//Checks if addTerm has an id, or a different condition
						if (!hasId)
							//Sets addTerm's ID to line and sets hasId to true
							hasId = addTerm.setId(line);
						else if (line.contains("is_a:"))
							//Adds a new is_a ID to addTerm using the line
							addTerm.addNewAltId(line);
						else if (line.contains("is_obsolete: true"))
							//Sets valid to false since the term is obsolete
							valid = false;
						
						//Checks if the line is a term heading or null
						if (!(line.equals("[Term]")) && line != null)
							//Adds what's on the line to the content, has a
							//case to check if there is anything in content
							content += (content == "") ? line : "\n" + line;
					}
					
					//Checks if the line is null or if it's a term heading
					if (line == null || line.equals("[Term]"))
					{
						//Checks if addTerm is valid based off valid boolean
						if (valid)
						{
							//Sets content for addTerm
							addTerm.setContent(content);

							//Uses addTerm's String ID as the key and addTerm
							//as the Term object value
							termDic.put(addTerm.getId(), addTerm);
						}
						
						//Sets isDone to true to stop looping for addTerm
						isDone = true;
					}
				}
			}
			
			//Closes br to avoid any issues with the BufferedReader object
			br.close();
		}
		catch (IOException e)
		{
			//Prints out the error message for the exception
			e.printStackTrace();
		}	
	}
	
	/**
	 * This function is used to read in the data from the queries file and sort
	 * into Query objects that will be sorted in the queries list
	 * Doesn't have any parameters or return variables
	 */
	public static void readInQuerries()
	{
		//Creates a new File object hpoFile that reads in queries.txt text file
		File queriesFile = new File("queries.txt");
		
		//Creates a new BufferedReader object called br
		BufferedReader br;			
		
		//Tries to execute code in this try block
		try
		{
			//Attempts to make a new BufferedReader using a FileReader object
			//instantiating using the hpoFile File object
			br = new BufferedReader(new FileReader(queriesFile));
			
			//Creates a new blank String to store the current line of the file
			String line = "";
			
			//Keeps looping until the current line is null
			while ((line = br.readLine()) != null)
				//Adds a new Query object initialized with line to queries list
				queries.add(new Query(line));
			
			//Closes br BufferedReader object to reduce resources being used
			br.close();
		}
		catch (IOException e)
		{
			//Prints out the error message for the exception
			e.printStackTrace();
		}
	}
	
	/**
	 * This function is used as the main function to solve for the longest path
	 * in the HPO file
	 */
	public static void solveMaxPath()
	{
		//Creates a List of Term objects to store the longest path
		ArrayList<Term> maxPath = new ArrayList<>();
		
		//Goes through each Term object in termDic
		for (Term term : termDic.values())
		{
			//Sets a List of termObjects to the return List of that Term's max
			//path to the root
			ArrayList<Term> currPath = maxPathToRoot(term);
			
			//Checks if the currPath list is longer than current maxPath list
			if (currPath.size() > maxPath.size())
				//Sets maxPath to the current Term object's max path
				maxPath = currPath;
		}
		
		//Writes to a maxpath.txt file using the term query from maxPath
		writeToFile("maxpath.txt", getMaxQuery(maxPath));
	}
	
	/**
	 * This function is used to find the maximum path to the root term of a
	 * given Term object
	 * @param current Term object is the Term trying to find the max path
	 * @return a List of Term object that is the maximum path to root term
	 */
	public static ArrayList<Term> maxPathToRoot(Term current)
	{
		//Creates new List of Term Objects for the maximum path to root term
		ArrayList<Term> maxPathToRoot = new ArrayList<>();
		
		//Goes through each is_a ID of the current term
		for (String altId : current.getAltIds())
		{
			//Sets the current path to the maximum path to root of the current
			//is_a ID optional path
			ArrayList<Term> currPath = maxPathToRoot(termDic.get(altId));
			
			//Checks which optional path is longer than the maxPathToRoot list
			if (currPath.size() > maxPathToRoot.size())		
				//Sets the maxPathToRoot to that of the currPath 
				maxPathToRoot = currPath;
		}
		
		//Adds to maxPathToRoot the current term to the beginning of the list
		//Current will be the Term with the longest path to root!
		maxPathToRoot.add(0, current);		
		
		//Will return the maxPathToRoot list
		return maxPathToRoot;
	}
	
	/**
	 * This function is used to get a formatted query of the max path to root
	 * @param longest is a List of Term objects that is the max path to root
	 * @return a String object of the formatted query for the max path
	 */
	public static String getMaxQuery(ArrayList<Term> longest)
	{
		//String to store query to return, initialized with max_path heading
		String returnQuery = "[max_path=" + (longest.size() - 1) + "]";

		//Goes through each term in the list of Term objects
		for (Term term : longest)
			//Adds to returnQuery a Term heading and Term object's contents
			returnQuery += "\n[Term]\n" + term.getContent();
		
		//Returns returnQuery String with a new line at the end for formatting
		return returnQuery + "\n";
	}
	
	/**
	 * This function is used to solve each query and write them to a file
	 */
	public static void solveQueries()
	{
		//Creates solvedOutput String to store what to write to the file
		String solvedOutput = "";
		
		//Goes through each Query object in queries list
		for (Query query : queries)
			//Adds to solvedOuput the full query of the query object
			solvedOutput += query.getFullQuery(termDic);
		
		//Writes to results.txt text file the solvedOuput string
		writeToFile("results.txt", solvedOutput);
	}
	
	/**
	 * This function is a general function to writing to a file
	 * @param fileName is a String for the name of the file being written to
	 * @param content is a String for what is to be written to the file
	 */
	public static void writeToFile(String fileName, String content)
	{
		//Attempts to run the code in this block
		try
		{
			//Creates a new FileOutputSteam object using fileName given
			FileOutputStream outFile = new FileOutputStream(fileName);
			
			//Creates a new OutputStreamWriter object in utf-8 using outFile
			OutputStreamWriter outStream = new OutputStreamWriter(outFile, 
					"utf-8");
			
			//Makes a new BufferedWrite object called writer using outStream
			BufferedWriter writer = new BufferedWriter(outStream);
			
			//Writes to writer with the content given as a parameter
			writer.write(content);
			
			//Closes writer to not use any extra resources
			writer.close();
		}
		catch (IOException e) 
		{
			//Prints out the error message for the exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Main function of program used to run functions to complete it's purpose
	 * @param args is an Array of String objects for any configurations
	 */
	public static void main(String[] args)
	{
		//Runs functions to read in data from files
		readInHPO();
		readInQuerries();
		
		//Solves the queries and max path challenges
		solveQueries();
		solveMaxPath();
	}
}
