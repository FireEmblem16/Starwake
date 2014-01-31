package grid;

/**
 * Indicates what the goal of the board is.
 */
public class Goal
{
	/**
	 * Defaults to Routing with no time limit.
	 */
	public Goal()
	{return;}
	
	/**
	 * Constructs this goal to have a specific type of goal with a time limit.
	 */
	public Goal(int type, int len)
	{
		Type = type;
		
		if(len > 0)
			length = len;
		
		return;
	}
	
	/**
	 * Returns the int version of the string version of this goal.
	 * Returns -1 if [type] is not valid.
	 */
	public static int GetType(String type)
	{
		if(type == null)
			return -1;
		
		type = type.toLowerCase();
		
		if(type.equals("rout"))
			return ROUT;
		else if(type.equals("sieze"))
			return SIEZE;
		else if(type.equals("defend"))
			return DEFEND;
		else if(type.equals("survive"))
			return SURVIVE;
		
		return -1;
	}
	
	/**
	 * Returns the string representation of this goal as it would appear in a config file.
	 */
	public String toString()
	{
		String ret = "";
		
		if(Type == ROUT)
			ret += "Rout";
		else if(Type == SIEZE)
			ret += "Sieze";
		else if(Type == DEFEND)
			ret += "Defend";
		else if(Type == SURVIVE)
			ret += "Survive";
		
		if(length != -1)
			ret += "#" + new Integer(length).toString();
		
		return ret;
	}
	
	/**
	 * Returns the string representation of this goal's name.
	 */
	public String GetName()
	{
		String ret = "";
		
		if(Type == ROUT)
			ret += "Rout";
		else if(Type == SIEZE)
			ret += "Sieze";
		else if(Type == DEFEND)
			ret += "Defend";
		else if(Type == SURVIVE)
			ret += "Survive";
		
		return ret;
	}
	
	/**
	 * Returns an array of string representations of goals.
	 */
	public static String[] GetGoalNames()
	{
		final String[] ret = {"Defend","Rout","Sieze","Survive"};
		return ret;
	}
	
	public int Type = ROUT;
	public int length = -1;
	
	public static final int ROUT = 0;
	public static final int SIEZE = 1;
	public static final int DEFEND = 2;
	public static final int SURVIVE = 3;
}
