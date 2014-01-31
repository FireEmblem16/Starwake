package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * Subtype of QuickSorter for convenience in sorting types with a natural
 * ordering.
 */
public class ComparableQuickSorter<T extends Comparable<? super T>> extends
QuickSorter<T>
{
	/**
	 * Constructs a ComparableQuickSorter that will sort an array in ascending
	 * order according to the natural ordering of type T, using the default
	 * pivot selection strategy.
	 */
	public ComparableQuickSorter()
	{
		super(new Comparator<T>()
		{
			@Override public int compare(T lhs, T rhs)
			{
				return lhs.compareTo(rhs);
			}
		});
	}

	/**
	 * Constructs a ComparableQuickSorter that will sort an array in ascending
	 * order according to the natural ordering of type T, using the given pivot
	 * selection strategy.
	 * @param strategy the pivot selection strategy
	 */
	public ComparableQuickSorter(IPivotStrategy<T> strategy)
	{
		super(new Comparator<T>()
		{
			@Override public int compare(T lhs, T rhs)
			{
				return lhs.compareTo(rhs);
			}
		},strategy);
	}
}
