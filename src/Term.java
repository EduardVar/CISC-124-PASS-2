import java.util.StringTokenizer;

public class Term
{
	String fullId = "", id = "";
	
	String[] fullAlts = null, altIds = null;
	
	String content = "";
	
	public Term()
	{
	}
	
	public void setIds(String fullId)
	{
		this.fullId = fullId;
		
		StringTokenizer tokens = new StringTokenizer(fullId, ":");
		
		for (int i = 0; i < 2; i++)
			tokens.nextToken();		

		this.id = tokens.nextToken();
	}
	
	public void addNewAltId(String addFullAltId)
	{
		if (fullAlts == null)
		{
			fullAlts = new String[0];
			altIds = new String[0];
		}
		
		int originalSize = altIds.length;
		
		String[] newFullAlts = new String[originalSize + 1];
		String[] newAltIds = new String[originalSize + 1];
		
		String addedAltId = getOnlyId(addFullAltId);
		
		for (int i = 0; i < newAltIds.length; i++)
		{
			newFullAlts[i] = i < originalSize ? fullAlts[i] : addFullAltId;
			newAltIds[i] = i < originalSize ? altIds[i] : addedAltId;
		}
			
		fullAlts = newFullAlts;
		altIds = newAltIds;
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
		
		if (fullAlts != null)
		{
			for (int i = 0; i < fullAlts.length; i++)
				fullTerm += (i == 0) ? fullAlts[i] : "\n" + fullAlts[i];
		}	
		
		return fullTerm + "\n";
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}

	public String getFullId()
	{
		return fullId;
	}

	public String getId()
	{
		return id;
	}

	public String getContent()
	{
		return content;
	}

	public String[] getFullAlts()
	{
		return fullAlts;
	}

	public String[] getAltIds()
	{
		return altIds;
	}
	
	public String getFirstAlt()
	{
		if (altIds == null)
			return null;
		else
			return altIds[0];
	}
	
//	public void printAltIds()
//	{
//		for (String string : altIds)
//		{
//			System.out.println(string);
//		}
//	}
}
