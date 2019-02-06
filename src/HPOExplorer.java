import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class HPOExplorer
{
	private static Term[] terms = new Term[13941];
	private static Query[] queries = new Query[5];
	private static Map<String, Term> termDicUp = new HashMap<>();
	
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
						{					
							addTerm.setIds(line);
							hasId = true;
							
						}
						else if (line.contains("is_a:"))
							addTerm.addNewAltId(line);
						else if (line.contains("is_obsolete: true"))
							valid = false;
						else
							if (!(line.equals("[Term]")) && line != null)
								content += (content == "") ? line : "\n" + line;
					}
					
					if (line == null || line.equals("[Term]"))
					{
						if (valid)
						{
							//Sets content for term and add dictionary reference
							addTerm.setContent(content);
							terms[i] = addTerm;

							termDicUp.put(terms[i].getId(), terms[i]);
						}
						
						isDone = true;
					}
				}	
				
				i++;
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
			int i = 0;
			
			while ((line = br.readLine()) != null)
			{
				queries[i] = new Query(line);
				i++;
			}
			
			br.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeQuerries()
	{
		try
		{
			FileOutputStream outFile = new FileOutputStream("results.txt");
			OutputStreamWriter outStream = new OutputStreamWriter(outFile, "utf-8");
			BufferedWriter writer = new BufferedWriter(outStream);
			
			for (int i = 0; i < queries.length; i++)
			{
				writer.write((queries[i].getFullQuery(termDicUp)));
//				else
//					writer.write(queries[i].getFullQuery(termDicUp));
				
			}
			
			writer.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		readInHPO();
		readInQuerries();
		writeQuerries();
	}
}
