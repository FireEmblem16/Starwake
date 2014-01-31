package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * Utilities for sorting arrays of objects using the insertion sort algorithm.
 */
public class SortUtil
{
	/**
	 * Sorts the given array using the insertion sort algorithm. The array is
	 * sorted in ascending order according to the given Comparator.
	 * @param arr the array to be sorted
	 * @param comp the Comparator to use for ordering
	 */
	public static <T>void insertionSort(T[] arr, Comparator<? super T> comp)
	{
		insertionSort(arr, 0, arr.length - 1, comp);
		return;
	}

	/**
	 * Sorts the given array using the insertion sort algorithm. The array is
	 * sorted in ascending order according to the natural ordering of type T.
	 * @param arr the array to be sorted
	 */
	public static <T extends Comparable<? super T>>void insertionSort(T[] arr)
	{
		Comparator<T> comp = new Comparator<T>()
		{
			@Override public int compare(T t1, T t2)
			{
				return t1.compareTo(t2);
			}
		};
		
		insertionSort(arr, 0, arr.length - 1, comp);
		return;
	}

	/**
	 * Sorts the subarray between first and last (inclusive) using the insertion
	 * sort algorithm. The subarray is sorted in ascending order according to
	 * the given Comparator.
	 * @param arr the array to be sorted
	 * @param comp the Comparator to use for ordering
	 * @return the number of comparisons of array elements performed during this
	 * call
	 */
	public static <T>int insertionSort(T[] arr, int first, int last,
		Comparator<? super T> comp)
	{
		if(arr == null)
			throw new NullPointerException();
		
		int c = 0;
		
		for(int i = first + 1; i <= last; i++)
			for(int j = first; j < i; j++)
			{
				c++;
				
				if(comp.compare(arr[i],arr[j]) < 0)
				{
					T temp = arr[i];
					
					for(int l = i; l > j; l--)
						arr[l] = arr[l-1];
					
					arr[j] = temp;
					j = i - 1;
				}
			}
		
		return c;
	}

}
