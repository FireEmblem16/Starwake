package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * Default implementation of a pivot selection strategy that always selects the
 * first element of the subarray as the pivot.
 */
public class BasicStrategy<T> implements IPivotStrategy<T>
{
	@Override public int indexOfPivotElement(T[] arr, int first, int last,
	Comparator<? super T> comp)
	{
		return first;
	}

	@Override public int minLength()
	{
		return 1;
	}

	@Override public int getComparisons()
	{
		return 0;
	}

	@Override public int getSwaps()
	{
		return 0;
	}
}
