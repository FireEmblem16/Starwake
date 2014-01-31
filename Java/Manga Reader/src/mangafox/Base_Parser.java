package mangafox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Base_Parser
{
	public Base_Parser(File file)
	{
		this.file = file;
		screwed = false;
		
		try
		{
			fin = new FileReader(file);
			in = new BufferedReader(fin);
			
			String[] look = new String[3];
			look[0] = "";
			look[1] = "";
			look[2] = "";
			
			while(!(look[2].contains("<div>") && look[1].contains("class=\"date\"") && look[0].contains("class=\"edit\"")))
			{
				String next = in.readLine();
				
				if(next == null)
				{
					HasMore = false;
					break;
				}
				else
					HasMore = true;
				
				if(next.contains(" is not available in Manga Fox."))
				{
					HasMore = false;
					screwed = true;
					break;
				}
				
				look[2] = look[1];
				look[1] = look[0];
				look[0] = next;
			}
		}
		catch(IOException e)
		{
			HasMore = false;
		}
		
		return;
	}
	
	/**
	 * Returns null if the file is out of links.
	 */
	public String[] NextLink()
	{
		String ret[] = new String[2];
		
		if(HasMore)
		{
			try
			{
				String link = in.readLine();
				
				for(int i = 0;i < 10;i++)
				{
					if(link.contains("/manga/"))
						break;
					
					if(i == 9)
					{
						HasMore = false;
						break;
					}
					
					link = in.readLine();
				}
				
				if(HasMore)
				{
					int index1 = link.indexOf("\"") + 1;
					int index2 = link.indexOf("\"",index1);
					ret[0] = link.substring(index1,index2);
					if(ret[0].contains(".html"))
					{
						ret[1] = ret[0].substring(ret[0].lastIndexOf('/') + 1);
						ret[0] = ret[0].substring(0,ret[0].lastIndexOf('/') + 1);
					}
				}
			}
			catch(IOException e)
			{
				HasMore = false;
			}
			catch(NullPointerException e)
			{
				HasMore = false;
			}
		}
		
		return ret;
	}
	
	public boolean HasMoreLinks()
	{
		return HasMore;
	}
	
	public boolean IsUnavailable()
	{
		return screwed;
	}
	
	protected boolean HasMore;
	protected boolean screwed;
	
	protected BufferedReader in;
	protected File file;
	protected FileReader fin;
}
