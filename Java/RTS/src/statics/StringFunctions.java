package statics;

/**
 * Contains all of the string functions that java does not seem to provide.
 */
public class StringFunctions
{
	/**
	 * Returns the index of the first character in [str] that is in [chars].
	 * Returns -1 if no such index exists.
	 */
	public static <T> int FindFirstOf(String str, Character[] chars)
	{
		if(str == null)
			return -1;
		
		for(int i = 0;i < str.length();i++)
			if(ArrayFunctions.conatains(chars,new Character(str.charAt(i))))
				return i;
		
		return -1;
	}
	
	/**
	 * Returns the index of the first character in [str] that is not in [chars].
	 * Returns -1 if no such index exists.
	 */
	public static <T> int FindFirstNotOf(String str, Character[] chars)
	{
		if(str == null)
			return -1;
		
		for(int i = 0;i < str.length();i++)
			if(!ArrayFunctions.conatains(chars,new Character(str.charAt(i))))
				return i;
		
		return -1;
	}
	
	/**
	 * Returns the index of the last character in [str] that is in [chars].
	 * Returns -1 if no such index exists.
	 */
	public static <T> int FindLastOf(String str, Character[] chars)
	{
		if(str == null)
			return -1;
		
		for(int i = str.length() - 1;i > -1;i--)
			if(ArrayFunctions.conatains(chars,new Character(str.charAt(i))))
				return i;
		
		return -1;
	}
	
	/**
	 * Returns the index of the last character in [str] that is not in [chars].
	 * Returns -1 if no such index exists.
	 */
	public static <T> int FindLastNotOf(String str, Character[] chars)
	{
		if(str == null)
			return -1;
		
		for(int i = str.length() - 1;i > -1;i--)
			if(!ArrayFunctions.conatains(chars,new Character(str.charAt(i))))
				return i;
		
		return -1;
	}
}
