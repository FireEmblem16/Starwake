package xml;

import item.Item;
import item.Type;
import java.util.ArrayList;
import grid.Goal;
import statics.StringFunctions;

/**
 * Contains information on a single line in an xml based file.
 */
public class Tag
{
	/**
	 * Constructs and parsers the string [str].
	 */
	public Tag(String str)
	{
		if(str.contains("="))
			isheader = false;
		else
			isheader = true;
		
		// If this is a header tag then the entire given string is the name
		if(isheader)
		{
			identifier = str;
			val = null;
		}
		else
		{
			identifier = str.substring(0,str.indexOf('='));
			identifier = RemoveBufferSpace(identifier);
			
			val = ParseValue(identifier.toLowerCase(),RemoveBufferSpace(str.substring(str.indexOf('=') + 1))); 
		}
		
		identifier = identifier.toLowerCase();
		return;
	}
	
	/**
	 * Returns an object representing the parsed version of [val].
	 * Returns null if [name] or [val] is invalid.
	 */
	protected Object ParseValue(String name, String val)
	{
		if(name == null || val == null)
			return null;
		
		try
		{
			if(name.equals("name") || name.equals("pallet-name") || name.equals("name-scheme") || name.equals("path") || name.equals("pallet-path") ||
			   name.equals("pallet") || name.equals("display-map") || name.equals("animation") || name.equals("animation-name") || name.equals("next-animation"))
				return val;
			else if(name.equals("flip") || name.equals("type"))
				return val.toLowerCase();
			else if(name.equals("filter"))
			{
				ArrayList<String> filters = new ArrayList<String>();
				String str = (String)val;
				
				if(str == null || str.equals(""))
					return filters;
				
				while(str.indexOf('/') != -1)
				{
					if(!str.substring(0,str.indexOf('/')).equals(""))
						filters.add(str.substring(0,str.indexOf('/')));
					
					str = str.substring(str.indexOf('/') + 1);
				}
				
				if(!str.equals(""))
					filters.add(str);
				
				return filters;
			}
			else if(name.equals("hex"))
				return new Boolean(val);
			else if(name.equals("loop"))
				return new Boolean(val);
			else if(name.equals("passable"))
				return new Boolean(val);
			else if(name.equals("flyable"))
				return new Boolean(val);
			else if(name.equals("swimable"))
				return new Boolean(val);
			else if(name.equals("climbable"))
				return new Boolean(val);
			else if(name.equals("class"))
				return Item.CreateNew(null,val.toLowerCase());
			else if(name.equals("width") || name.equals("cell-width"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("height") || name.equals("cell-height"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("padding-left"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("padding-top"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("padding-right"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("padding-bottom"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("u"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("v"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("x"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("y"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("cost"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("avoid-bonus"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("def-bonus"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("life-gain"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("start-frame"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("time"))
				return new Long(Long.parseLong(val));
			else if(name.equals("passable"))
				return new Boolean(val);
			else if(name.equals("rotate"))
				return new Integer(Integer.parseInt(val));
			else if(name.equals("goal"))
			{
				int poundindex = val.indexOf('#');
				
				if(poundindex == -1)
					return null;
				
				return new Goal(Goal.GetType(RemoveBufferSpace(val.substring(0,poundindex)).toLowerCase()),Integer.parseInt(RemoveBufferSpace(val.substring(poundindex + 1))));
			}
		}
		catch(Exception e)
		{}
		
		return null;
	}
	
	/**
	 * Returns true if this is a header tag.
	 */
	public boolean IsHeader()
	{
		return isheader;
	}
	
	/**
	 * Returns true if this is a descriptive tag.
	 */
	public boolean IsDescriptor()
	{
		return !isheader;
	}
	
	/**
	 * Returns the name of this descriptor tag or the name of the header.
	 */
	public String GetName()
	{
		return identifier;
	}
	
	/**
	 * Returns the value of this tag.
	 * Returns null if this tag is a header tag or if there was an invalid value.
	 */
	public Object GetValue()
	{
		return val;
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
	 * Returns what we hope is the original form of the tag.
	 */
	public String toString()
	{
		return "<" + identifier + (isheader ? "" : " = " + val.toString()) + ">";
	}
	
	/**
	 * True if this tag is a header tag.
	 * False if it contains information about a header tag.
	 */
	protected boolean isheader;
	
	/**
	 * Either contains the header name or the descriptor name.
	 */
	protected String identifier;
	
	/**
	 * Contains the value of the descriptor already parsed for convinience.
	 * Contains null if this is a header tag or if the value was invalid.
	 */
	protected Object val;
}
