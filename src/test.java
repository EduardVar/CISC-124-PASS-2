import java.util.StringTokenizer;

public class test
{
	public static void main(String[] args)
	{
		String line = "query: H:0010982";
		StringTokenizer tokens = new StringTokenizer(line, ":");
		
		for (int i = 0; i < 2; i++)
		{	
			tokens.nextToken();		
		}
		
		System.out.println(tokens.nextToken());
	}
}
