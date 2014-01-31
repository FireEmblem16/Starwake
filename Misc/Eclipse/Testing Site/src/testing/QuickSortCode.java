package testing;
/**
 * Quick Sort A weakness of Selection Sort and Insertion Sort is that they get
 * one element in order in a pass (one iteration of the outer loop). Quick Sort
 * addresses this weakness by getting many elements in order in one pass. In
 * Quick Sort, we select an element called a pivot, partition the sequence into
 * two parts such that one part is less than or equal to the pivot, and the
 * other part is greater than or equal to the pivot, and sort each part
 * recursively. In one pass, we get all the elements in part one less than or
 * equal to all the elements in part two.
 */

// A version of Quick Sort for an array of integers.

import java.util.Random;

public class QuickSortCode
{

	public static void quickSort(int[] a)
	{
		if(a == null || a.length == 0)
			throw new RuntimeException("Null pointer or zero size");
		
		quickSortRec(a,0,a.length - 1);
	}

	// Note that the code needs to be revised if a different method is used to
	// select the pivot.
	// Otherwise, the code may not work correctly.
	private static void quickSortRec(int[] a, int start, int end)
	{
		if(start >= end)
			return;
		int pivot = a[start]; // A simple way to select the pivot.
		int left = start;
		int right = end;

		while(true)
		{
			while(a[left] < pivot)
				left++;
			
			while(a[right] > pivot)
				right--;
			
			if(left < right)
			{
				int t = a[left];
				a[left++] = a[right];
				a[right--] = t;
			} // swap
			else
				break;
		} // partition
		
		quickSortRec(a,start,right); // recursive calls
		quickSortRec(a,right + 1,end);
	} // quickSortRec

	public static void main(String[] args)
	{
		Random rand = new Random();
		final int RANGE = 100; // constant identifier
		
		int[] b = {5,6,5,5,5,5,5,5,6,5}; 
		
		Integer[] a = new Integer[5];
		a[0] = new Integer("0");
		Class test = a.getClass();
		Class test2 = a[0].getClass();
		
		quickSort(b);
		
		for(int ele:b)
			System.out.print(ele + " ");
		
		System.out.print("\n");
	}

}
