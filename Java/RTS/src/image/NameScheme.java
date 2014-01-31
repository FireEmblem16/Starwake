package image;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import statics.StringFunctions;

/**
 * Given a file, will attempt to parse the file for a lines of newline delineated names for images.
 */
public class NameScheme
{
	/**
	 * Initializes the parser and opens the file.
	 */
	public NameScheme(String file)
	{
		if(file == null)
			return;
		
		try
		{
			File f = new File(file);
			in = new Scanner(f);
		}
		catch(FileNotFoundException e)
		{}
		
		return;
	}
	
	/**
	 * Returns the next name in file this namescheme is reading from.
	 * Returns null if we are out of names to return.
	 */
	public ArrayList<String> GetNext()
	{
		if(!HasNext())
			return null;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		String str = "";
		
		while(str.equals("") && in.hasNextLine())
		{
			str = in.nextLine();
			str = RemoveBufferSpace(RemoveComments(str));
		}
		
		if(str.equals(""))
			return null;
		
		ret.add(str);
		
		if(in.hasNextLine())
		{
			str = in.nextLine();
			
			if(str == null || str.equals(""))
				return ret;
			
			while(str.indexOf('/') != -1)
			{
				ret.add(str.substring(0,str.indexOf('/')));
				str = str.substring(str.indexOf('/') + 1);
			}
			
			ret.add(str);
		}
		
		return ret;
	}
	
	/**
	 * Returns true if there are more names left. 
	 */
	public boolean HasNext()
	{
		return in != null && in.hasNextLine();
	}
	
	/**
	 * Removes any comments from the given string.
	 */
	protected String RemoveComments(String str)
	{
		if(str.indexOf("//") != -1)
			str = str.substring(0,str.indexOf("//"));
		
		return str;
	}
	
	/**
	 * Removes any whitespace around the edges of the given string.
	 */
	protected String RemoveBufferSpace(String str)
	{
		final Character[] whitespace = {' ','\t','\n','\r'};
		
		int left = StringFunctions.FindFirstNotOf(str,whitespace);
		int right = StringFunctions.FindLastNotOf(str,whitespace);
		
		return str.substring((left == -1 ? 0 : left),(right == -1 ? str.length() : right + 1));
	}
	
	/**
	 * Contains the input stream for this parser.
	 */
	protected Scanner in;
}
