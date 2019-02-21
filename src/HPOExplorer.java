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
	 * 
	 */
	public static void readInHPO()
	{
		File hpoFile = new File("HPO.txt");
		
		BufferedReader br;			
		
		try
		{
			br = new BufferedReader(new FileReader(hpoFile));
			
			String line = "";
			
			boolean initialRead = false;
			
			while (line != null)
			{
				while (!initialRead)
				{
					line = br.readLine();	
					initialRead = line.equals("[Term]");
				}
				
				Term addTerm = new Term();
				String content = "";
				
				boolean isDone = false;
				boolean valid = true;	
				boolean hasId = false;
				
				while (!isDone)
				{	
					if ((line = br.readLine()) != null)
					{	
						if (!hasId)
							hasId = addTerm.setId(line);
						else if (line.contains("is_a:"))
							addTerm.addNewAltId(line);
						else if (line.contains("is_obsolete: true"))
							valid = false;
						
						if (!(line.equals("[Term]")) && line != null)
							content += (content == "") ? line : "\n" + line;
					}
					
					if (line == null || line.equals("[Term]"))
					{
						if (valid)
						{
							//Sets content for term and add dictionary reference
							addTerm.setContent(content);

							termDic.put(addTerm.getId(), addTerm);
						}
						
						isDone = true;
					}
				}
			}
			
			br.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void readInQuerries()
	{
		File queriesFile = new File("queries.txt");
		
		BufferedReader br;			
		
		try
		{
			br = new BufferedReader(new FileReader(queriesFile));
			
			String line = "";
			
			while ((line = br.readLine()) != null)
				queries.add(new Query(line));
			
			br.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void solveMaxPath()
	{
		ArrayList<Term> maxPath = new ArrayList<>();
		
		for (Term term : termDic.values())
		{
			ArrayList<Term> currPath = maxPathToRoot(term);
			
			if (currPath.size() > maxPath.size())
				maxPath = currPath;
		}
		
		writeToFile("maxpath.txt", getMaxQuery(maxPath));
	}
	
	public static ArrayList<Term> maxPathToRoot(Term current)
	{
		ArrayList<Term> maxPathToRoot = new ArrayList<>();
		
		for (String altId : current.getAltIds())
		{
			ArrayList<Term> currPath = maxPathToRoot(termDic.get(altId));
			
			if (currPath.size() > maxPathToRoot.size())		
				maxPathToRoot = currPath;
		}
		
		maxPathToRoot.add(0, current);		
		return maxPathToRoot;
	}
	
	public static void writeToFile(String fileName, String content)
	{
		try
		{
			FileOutputStream outFile = new FileOutputStream(fileName);
			OutputStreamWriter outStream = new OutputStreamWriter(outFile, "utf-8");
			BufferedWriter writer = new BufferedWriter(outStream);
			
			writer.write(content);		
			writer.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void solveQueries()
	{
		String solvedOutput = "";
		
		for (int i = 0; i < queries.size(); i++)
			solvedOutput += queries.get(i).getFullQuery(termDic);
		
		writeToFile("results.txt", solvedOutput);
	}
	
	
	//Write to maxPath file here
	public static String getMaxQuery(ArrayList<Term> longest)
	{
		String returnQuery = "[max_path=" + (longest.size() - 1) + "]";

		for (Term term : longest)
			returnQuery += "\n[Term]\n" + term.getContent();
		
		return returnQuery + "\n";
	}
	
	public static void main(String[] args)
	{
		readInHPO();
		readInQuerries();
		solveQueries();
		solveMaxPath();
	}
}
