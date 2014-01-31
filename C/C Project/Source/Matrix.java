/**
 * Does some simple matrix math.
 */
public class Matrix
{
	/**
	 * Multiplies arr1 and arr2 together if possible and returns the value in ret.
	 * If not possible ret will be unmodified.
	 */
	public native void multiply(int arr1[][], int arr2[][], int arr3[][]);
	
	/**
	 * Load our native library.
	 */
	static
	{
		System.loadLibrary("Matrix");
	}
}
