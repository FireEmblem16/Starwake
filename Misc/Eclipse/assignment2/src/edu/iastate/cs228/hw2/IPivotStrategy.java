package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * An IPivotStrategy represents a strategy for selecting the pivot value in a
 * partitioning algorithm, including a minimum length for the subarray.
 */
public interface IPivotStrategy<T>
{
	/**
	 * Returns the index of the element selected as the pivot value within the
	 * subarray between first and last (inclusive).
	 * @param arr the array in which to select the pivot
	 * @param first beginning of the subarray
	 * @param last end of the subarray
	 * @param comp the comparator to be used
	 * @return index of the element selected as the pivot value
	 * @throws IllegalArgumentException if the length of the subarray (last -
	 * first + 1) is less than the value returned by minLength().
	 */
	int indexOfPivotElement(T[] arr, int first, int last,
		Comparator<? super T> comp);

	/**
	 * Returns the minimum length of the subarray to which this partitioning
	 * strategy can be applied.
	 * @return minimum size of the subarray required to apply this pivot
	 * selection strategy
	 */
	int minLength();

	/**
	 * Returns the number of comparisons performed in the most recent call to
	 * indexOfPivotElement
	 * @return number of comparisons
	 */
	int getComparisons();

	/**
	 * Returns the number of swaps performed in the most recent call to
	 * indexOfPivotElement. For algorithms that do not use swapping, this method
	 * returns an estimate of swaps equivalent to one-third the number of times
	 * that an array element was assigned.
	 * @return equivalent number of swaps
	 */
	int getSwaps();

}