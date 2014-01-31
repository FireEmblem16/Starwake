package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * This class encapsulates the configuration and instrumentation for a sorting
 * utility based on the quicksort algorithm.
 */
public class QuickSorter<T>
{
	/**
	 * Constructs a QuickSorter that will sort an array in ascending order
	 * according to the given Comparator, using the default pivot selection
	 * strategy.
	 * @param comp Comparator to use for sorting
	 */
	public QuickSorter(Comparator<T> comp)
	{
		this.comp = comp;
		stg = new BasicStrategy();
		threesort = false;

		return;
	}

	/**
	 * Constructs a QuickSorter that will sort an array in ascending order
	 * according to the given Comparator, using the given pivot selection
	 * strategy.
	 * @param comp Comparator to use for sorting
	 * @param strategy the pivot selection strategy
	 */
	public QuickSorter(Comparator<T> comp, IPivotStrategy<T> strategy)
	{
		this.comp = comp;
		threesort = false;
		
		if(stg == null)
			stg = new BasicStrategy();
		else
			stg = strategy;

		return;
	}

	/**
	 * Sorts the given array using the quicksort algorithm.
	 * @param arr array to be sorted
	 */
	public void sort(T[] arr)
	{
		if(arr == null)
			throw new NullPointerException();
		
		c = 0;
		d = 0;
		s = 0;

		quickSortRec(arr,0,arr.length - 1,1);
	}

	/**
	 * Returns the maximum depth of recursion for the most recent call to
	 * sort().
	 * @return maximum depth of recursion
	 */
	public int getMaxDepth()
	{
		return d;
	}

	/**
	 * Returns the number of comparisons of array elements performed during the
	 * most recent call to sort().
	 * @return number of comparisons performed
	 */
	public int getComparisons()
	{
		return c;
	}

	/**
	 * Returns the number of exchanges of array elements performed during the
	 * most recent call to sort(). This value includes an approximation of the
	 * number of exchanges performed during insertion sort operations.
	 * @return number of exchanges of array elements
	 */
	public int getSwaps()
	{
		return s;
	}

	/**
	 * Sets this sorter to use three-way partitioning.
	 * @param useThreeWayPartition true to use three-way partitioning, false to
	 * use normal partitioning
	 */
	public void setUseThreeWay(boolean useThreeWayPartition)
	{
		threesort = useThreeWayPartition;
		return;
	}

	/**
	 * Returns true if this sorter is currently configured to use three-way
	 * partitioning, false otherwise.
	 * @return true if three-way partitioning is being used, false otherwise
	 */
	public boolean getUseThreeWay()
	{
		return threesort;
	}

	/**
	 * Sorts the subarray consisting of positions first through last.
	 * @param arr array to be sorted
	 * @param first index of first position in subarray
	 * @param last index of last position in subarray
	 * @param depth depth of recursion prior to this call
	 */
	private void quickSortRec(T[] arr, int first, int last, int depth)
	{
		if(d < depth)
			d = depth;

		if(first >= last)
			return;
		
		if(last - first + 1 < stg.minLength())
		{
			int comps = SortUtil.insertionSort(arr, first, last, comp);

			c += comps;
			s += (last - first + comps) / 3;
			return;
		}

		if(threesort)
		{
			Pair<Integer> p = partitionThreeWay(arr, first, last);

			quickSortRec(arr, first, ((Integer)p.getFirst()).intValue() - 1,
				depth + 1);
			quickSortRec(arr, ((Integer)p.getSecond()).intValue() + 1, last,
				depth + 1);
		}
		else
		{
			int p = partitionNormal(arr,first,last);

			quickSortRec(arr, first, p, depth + 1);
			quickSortRec(arr, p + 1, last, depth + 1);
		}

		return;
	}

	/**
	 * Returns the index where [arr] is seperated into elements greater than or
	 * equal to the pivot value and elements less than or equal to the pivot
	 * value.
	 * @param arr array to be sorted
	 * @param first index of first position in subarray
	 * @param last index of last position in subarray
	 * @author Donald Nye
	 */
	public int partitionNormal(T[] arr, int first, int last)
	{
		int l = first;
		int r = last;
		
		T pivot = arr[stg.indexOfPivotElement(arr, first, last, comp)];
		c += stg.getComparisons();
		s += stg.getSwaps();
		
		while(true)
		{
			while(comp.compare(arr[l], pivot) < 0)
			{
				l++;
				c++;
			}
			c++;

			while(comp.compare(arr[r], pivot) > 0)
			{
				r--;
				c++;
			}
			c++;

			if(l < r)
			{
				T temp = arr[l];
				arr[l++] = arr[r];
				arr[r--] = temp;

				s++;
			}
			else
				return r;
		}
	}

	/**
	 * Returns a pair of Integers such that all elements of [arr] to the left of
	 * the first Integer are less than the pivot value and all elements of [arr]
	 * to the right of the second Integer are greater than the pivot value.
	 * @param arr array to be sorted
	 * @param first index of first position in subarray
	 * @param last index of last position in subarray
	 * @author Donald Nye
	 */
	public Pair<Integer> partitionThreeWay(T[] arr, int first, int last)
	{
		int p0 = first;
		int p1 = last;
		int i = first;

		T pivot = arr[stg.indexOfPivotElement(arr, first, last, comp)];
		c += stg.getComparisons();
		s += stg.getSwaps();
		
		while(i <= p1)
		{
			c += 2;

			if(comp.compare(arr[i], pivot) < 0)
			{
				T temp = arr[i];
				arr[i++] = arr[p0];
				arr[p0++] = temp;

				c--;
				s++;
			}
			else if(comp.compare(arr[i], pivot) > 0)
			{
				T temp = arr[i];
				arr[i] = arr[p1];
				arr[p1--] = temp;
				
				s++;
			}
			else
				i++;
		}

		return new Pair(new Integer(p0), new Integer(p1));
	}

	/**
	 * If true then this object will sort such that duplicates of the pivot are
	 * not resorted.
	 */
	private boolean threesort;

	/**
	 * Contains a way to compare objects of type T.
	 */
	private Comparator comp;

	/**
	 * Contains functions to determine the index of the pivot element to be used
	 * and determine the minimum size of an array to sort with quicksort.
	 */
	private IPivotStrategy<T> stg;

	/**
	 * The number of comparisons in the latest sort.
	 */
	private int c;

	/**
	 * The maximum depth of recursion in the lastest sort.
	 */
	private int d;

	/**
	 * The number of swaps in the lastest sort.
	 */
	private int s;
}
