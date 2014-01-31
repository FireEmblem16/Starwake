package bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		WebGet wget = new WebGet();
		File file = new File("webpage.html");;
		
		for(int i = START;i <= END;i++)
		{
			System.out.println("Obtaining: " + IntToStringBuffered(i) + ".gif");
			
			try
			{
				if(!wget.get(new URL("http://www.giantitp.com/comics/oots" + IntToStringBuffered(i) + ".html"),"webpage",file))
					System.err.println("Unable to obtain oots comic number " + new Integer(i).toString());
			}
			catch(MalformedURLException e)
			{
				System.err.println("Unable to obtain oots comic number " + new Integer(i).toString() + "'s webpage");
				continue;
			}
			
			String imagename = "";
			
			try
			{
				Scanner in = new Scanner(file);
				
				while(imagename != null && !imagename.contains("<TD align=\"center\"><IMG"))
					imagename = in.nextLine();
				
				if(imagename == null)
					throw new FileNotFoundException();
				
				imagename = "http://www.giantitp.com" + imagename.substring(imagename.indexOf("src=") + 5);
				imagename = imagename.substring(0,imagename.indexOf('\"'));
			}
			catch(FileNotFoundException e)
			{
				System.err.println("Something went horribly wrong on oots comic number " + new Integer(i).toString());
				continue;
			}
			
			File File = new File("C:\\Users\\The Science Guy\\Desktop\\Books\\Comics\\Order Of The Stick\\" + IntToStringBuffered(i) + ".gif");
			
			try
			{
				if(!wget.get(new URL(imagename),"remote file",File))
					System.err.println("Unable to obtain oots comic number " + new Integer(i).toString());
			}
			catch(MalformedURLException e)
			{
				System.err.println("Unable to obtain oots comic number " + new Integer(i).toString());
			}
		}
		
		file.delete();
		return;
	}
	
	public static String IntToStringBuffered(int val)
	{
		if(val < 10)
			return "000" + new Integer(val).toString();
		
		if(val < 100)
			return "00" + new Integer(val).toString();
		
		if(val < 1000)
			return "0" + new Integer(val).toString();
		
		return new Integer(val).toString();
	}
	
	public static int START = 781;
	public static int END = 781;
}
