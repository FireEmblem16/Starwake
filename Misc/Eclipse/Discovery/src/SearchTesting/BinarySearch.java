package SearchTesting;

public class BinarySearch
{
	public static int Search(int[] a, int target)
	{
		int index = 0;
		
		for(int low = 0,high = a.length - 1, mid = 0, dif = 0;low <= high;)
		{
			mid = (low + high) / 2;
			dif = a[mid] - target;
			
			if(dif == 0)
				return mid;
			else if(dif < 0)
				low = mid + 1;
			else
				high = mid + 1;
		}
		
		return -1;
	}
}