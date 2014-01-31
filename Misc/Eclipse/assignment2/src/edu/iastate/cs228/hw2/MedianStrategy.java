package edu.iastate.cs228.hw2;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Random;

/**
 * Chooses a pivot by randomly selecting k elements and finding the median
 * value of those elements and returning the index of that element.
 * @author Donald Nye
 */
public class MedianStrategy<T> implements IPivotStrategy<T>
{
	/**
	 * Creates an object of this class which has an unseeded random
	 * number generator and returns [k] in minLength().
	 * @param k The number of random elements to sample.
	 * @author Donald Nye
	 */
	public MedianStrategy(int k)
	{
		if(k < 1 || k % 2 == 0)
			throw new IllegalArgumentException();
		
		rand = new Random();
		this.k = k;
		min = k;
		
		return;
	}
	
	/**
	 * Creates an object of this class which has an unseeded random
	 * number generator.
	 * @param k The number of random elements to sample.
	 * @param minE The number to be returned by minLength().
	 * @author Donald Nye
	 */
	public MedianStrategy(int k, int minE)
	{
		if(k < 1 || k % 2 == 0 || minE < k)
			throw new IllegalArgumentException();
		
		rand = new Random();
		this.k = k;
		min = minE;
		
		return;
	}
	
	/**
	 * Creates an object of this class.
	 * @param k The number of random elements to sample.
	 * @param minE The number to be returned by minLength().
	 * @param seed The seed to be given to the random number generator
	 * belonging to this object.
	 * @author Donald Nye
	 */
	public MedianStrategy(int k, int minE, Random rand)
	{
		if(k < 1 || k % 2 == 0 || minE < k)
			throw new IllegalArgumentException();
		
		this.rand = rand;
		this.k = k;
		min = minE;
		
		return;
	}
	
	@Override public int getComparisons()
	{
		return c;
	}

	@Override public int getSwaps()
	{
		return s;
	}

	@Override public int indexOfPivotElement(T[] arr, int first, int last,
		Comparator comp)
	{
		if(arr == null)
			throw new NullPointerException();
		
		T[] ary = (T[])Array.newInstance(arr[0].getClass(), last - first + 1);
		
		for(int i = 0; i < k; i++)
			ary[i] = arr[rand.nextInt(last - first + 1) + first];
		
		QuickSorter<T> qs = new QuickSorter<T>(comp);
		qs.sort(ary);
		
		c = qs.getComparisons();
		s = qs.getSwaps();
		
		Object mid = ary[k / 2];
		
		for(int i = first; i < last + 1; i++)
			if(mid == arr[i])
				return i;
		
		return -1;
	}

	@Override public int minLength()
	{
		return min;
	}

	/**
	 * Contains the number of comparisons used to determine the index of
	 * the pivot to use.
	 */
	private int c;
	
	/**
	 * Contains the number of swaps used to determine the index of the pivot
	 * to use.
	 */
	private int s;
	
	/**
	 * Contains the number of elements to use to determine the pivot index.
	 */
	private int k;
	
	/**
	 * Contains the minimum number of elements required for quicksort to run.
	 */
	private int min;
	
	/**
	 * Provides the object with random numbers.
	 */
	private Random rand;
}
