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
	private static ArrayList<Term> terms = new ArrayList<>();
	private static ArrayList<Query> queries = new ArrayList<>();
	
	private static Map<String, Term> termDic = new HashMap<>();
	
	public static void readInHPO()
	{
		File hpoFile = new File("HPO.txt");
		
		BufferedReader br;			
		
		try
		{
			br = new BufferedReader(new FileReader(hpoFile));
			
			String line = "";
			int i = 0;
			
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
							terms.add(addTerm);

							termDic.put(terms.get(i).getId(), terms.get(i));
						}
						else
							terms.add(new Term());
						
						isDone = true;
					}
				}
				
				i++;
			}
			
			br.close();
			buildPointers();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void buildPointers()
	{
		for (Term term : terms)
		{
			ArrayList<Term> parents = new ArrayList<>();
			
			if (term != null)
			{
				for (String altId : term.getAltIds())
					parents.add(termDic.get(altId));
				
				for (Term parent : parents)
					term.addParent(parent);
			}
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
		
		for (Term term : terms)
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
		
		for (Term parent : current.getParents())
		{
			ArrayList<Term> currPath = maxPathToRoot(parent);
			
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
		String returnQuery = "[max_path=" + longest.size() + "]";

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
		
		//TO DO: CLEAN UP CODE, COMBINE WRITE TO FUNCTION AND READ FUNCTIONS TO SIMPLIFY CODE, MAKE LESS CODE IN MAIN (Put some of the formatting and stuff in TERM)
	}
}
