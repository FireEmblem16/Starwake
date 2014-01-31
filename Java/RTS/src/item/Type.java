package item;

/**
 * Indicates what type an item is.
 */
public enum Type
{
	BACKGROUND,
	UTILITY,
	CHARACTER,
	SPAWN,
	NODE,
	CURSOR,
	ARROW,
	HIGHLIGHT,
	TINT;
	
	/**
	 * Returns null if [str] is invalid.
	 */
	public static Type GetType(String str)
	{
		if(str == null)
			return null;
		
		str = str.toLowerCase();
		
		if(str.equals("background"))
			return BACKGROUND;
		else if(str.equals("utility"))
			return UTILITY;
		else if(str.equals("character"))
			return CHARACTER;
		else if(str.equals("spawn"))
			return SPAWN;
		else if(str.equals("node"))
			return NODE;
		else if(str.equals("cursor"))
			return CURSOR;
		else if(str.equals("arrow"))
			return ARROW;
		else if(str.equals("highlight"))
			return HIGHLIGHT;
		else if(str.equals("tint"))
			return TINT;
		
		return null;
	}
	
	/**
	 * Returns the next type in the list after [t] with null returning
	 * BACKGROUND and CURSOR returning null.
	 * BACKGROUND
	 * UTILITY
	 * SPAWN
	 * CHARACTER
	 * TINT
	 * HIGHLIGHT
	 * ARROW
	 * NODE
	 * CURSOR
	 */
	public static Type GetNext(Type t)
	{
		if(t == null)
			return Type.BACKGROUND;
		if(t == Type.BACKGROUND)
			return Type.UTILITY;
		if(t == Type.UTILITY)
			return Type.SPAWN;
		if(t == Type.SPAWN)
			return Type.CHARACTER;
		if(t == Type.CHARACTER)
			return Type.TINT;
		if(t == Type.TINT)
			return Type.HIGHLIGHT;
		if(t == Type.HIGHLIGHT)
			return Type.ARROW;
		if(t == Type.ARROW)
			return Type.NODE;
		if(t == Type.NODE)
			return Type.CURSOR;
		
		return null;
	}
	
	/**
	 * Returns the string represenation of this Type.
	 */
	public String toString()
	{
		if(this == Type.BACKGROUND)
			return "Background";
		if(this == Type.UTILITY)
			return "Utility";
		if(this == Type.SPAWN)
			return "Spawn";
		if(this == Type.CHARACTER)
			return "Character";
		if(this == Type.TINT)
			return "Tint";
		if(this == Type.HIGHLIGHT)
			return "Highlight";
		if(this == Type.ARROW)
			return "Arrow";
		if(this == Type.NODE)
			return "Node";
		if(this == Type.CURSOR)
			return "Cursor";
		
		return null;
	}
}
