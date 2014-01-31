package statics;

/**
 * Contains all of the array functions java doesn't seem to have.
 */
public class ArrayFunctions
{
	/**
	 * Returns true if [arr] contains [item] as proclaimed by the equals function.
	 */
	public static <T> boolean conatains(T[] arr, T item)
	{
		for(int i = 0;i < arr.length;i++)
			if(arr[i].equals(item))
				return true;
		
		return false;
	}
}
