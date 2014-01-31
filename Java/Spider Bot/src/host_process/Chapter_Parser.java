package host_process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Chapter_Parser
{
	public Chapter_Parser(File file)
	{
		this.file = file;
		first_key = false;
		HasMore = true;
		
		return;
	}
	
	public String NextLink()
	{
		String ret = null;
		
		if(!first_key)
		{
			try
			{
				FileReader fin = new FileReader(file);
				BufferedReader in = new BufferedReader(fin);
				
				String line = "";
				
				while(line != null && !line.contains("next page"))
					line = in.readLine();
				
				if(line == null)
				{
					HasMore = false;
					return null;
				}
				
				if(line.contains("next_chapter()"))
					HasMore = false;
				
				int index1 = line.indexOf("\"") + 1;
				int index2 = line.indexOf("\"",index1);
				ret = line.substring(index1,index2);
				
				while(!line.contains("<img "))
					line = in.readLine();
				
				index1 = line.indexOf("<img ");
				index2 = line.indexOf("\"",index1) + 1;
				int index3 = line.indexOf("\"",index2);
				pic_link = line.substring(index2,index3);
				
				try
				{
					in.close();
					fin.close();
				}
				catch(IOException e)
				{}
			}
			catch(IOException e)
			{}
		
			first_key = true;
		}
	
		return ret;
	}
	
	public String GetPictureLink()
	{
		return pic_link;
	}
	
	public boolean IsLastPage()
	{
		return !HasMore;
	}
	
	protected boolean first_key;
	protected boolean HasMore;
	protected File file;
	protected String pic_link;
}
