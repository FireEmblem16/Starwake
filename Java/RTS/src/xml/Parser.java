package xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import statics.StringFunctions;

/**
 * Given a file, will attempt to parse the xml formatted data within.
 */
public class Parser
{
	/**
	 * Initializes the parser and opens the file.
	 */
	public Parser(String file)
	{
		ready = false;
		finished = false;
		
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
	 * Returns the next tag in file this parser is reading from.
	 * Returns null if we are out of Tags to return.
	 */
	public Tag GetNext()
	{
		if(!HasNextTag())
			return null;
		
		String str = in.nextLine();
		str = RemoveBufferSpace(RemoveBrackets(RemoveComments(str)));
		
		Tag ret = new Tag(str);
		
		if(ready)
		{
			if(board && ret.GetName().equals("/board"))
				finished = true;
			else if(pallet && ret.GetName().equals("/pallet"))
				finished = true;
			else if(!(board || pallet) && ret.GetName().equals("/animation"))
				finished = true;
			
			return ret;
		}
		
		if(ret.GetName().equals("board"))
		{
			ready = true;
			board = true;
			pallet = false;
			
			return ret;
		}
		else if(ret.GetName().equals("pallet"))
		{
			ready = true;
			board = false;
			pallet = true;
			
			return ret;
		}
		else if(ret.GetName().equals("animation"))
		{
			ready = true;
			board = false;
			pallet = false;
			
			return ret;
		}
		
		return GetNext();
	}
	
	/**
	 * Returns true if there are more tags in the file being parsed.
	 */
	public boolean HasNextTag()
	{
		return in != null && !finished && in.hasNextLine();
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
	 * Removes angle brackets from the given string.
	 * Assumes no comments exist.
	 */
	protected String RemoveBrackets(String str)
	{
		int left = str.indexOf('<');
		int right = str.lastIndexOf('>');
		
		if(left != -1 && right != -1 && left < right)
			str = str.substring(left + 1,right);
		
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
	
	/**
	 * True if we have read in the <Board> tag.
	 */
	protected boolean ready;
	
	/**
	 * True if we have read in the </Board> tag.
	 */
	protected boolean finished;
	
	/**
	 * This is true if we are parsing a board and false otherwise.
	 */
	protected boolean board;
	
	/**
	 * This is true if we are parsing a pallet and false otherwise.
	 */
	protected boolean pallet;
}
