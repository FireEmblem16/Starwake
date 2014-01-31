package edu.iastate.cs228.hw2;

import java.util.Comparator;
import java.util.Random;

/**
 * Allows us to test and compare various sorting options by sorting arrays of
 * Integers.
 * @author Donald Nye
 */
public class SortMain
{
	/**
	 * Requires three parameters in [args].
	 * @param size number of elements in test array.
	 * @param range values to be generated for the test array are in [range] 0
	 * through [range] - 1
	 * @param seed - seed for the rand number generator for creating the test
	 * arrays.
	 * @param args Contains the three formal parameters in the order listed.
	 */
	public static void main(String[] args)
	{
		int size;
		int range;
		long seed;
		
		try
		{
			size = Integer.parseInt(args[0]);
			range = Integer.parseInt(args[1]);
			seed = Long.parseLong(args[2]);
		}
		catch(RuntimeException e)
		{
			System.err.println("This program requires two ints and a long " +
				"all seperated by a space to execute.");
			
			return;
		}
		
		Random rand = new Random();
		
		Comparator<Integer> rcomp = new Comparator<Integer>()
		{
			@Override public int compare(Integer t1, Integer t2)
			{
				return -1 * t1.compareTo(t2);
			}
		};
		
		// Sort using BasicStrategy and normal partitioning.
		Test(fetchArray(size, range, seed, rand), null, null, false);
		
		// Sort in reverse using BasicStrategy and normal partitioning.
		Test(fetchArray(size, range, seed, rand), rcomp, null, false);
		
		// Sort using MedianStrategy and normal partitioning.
		// Because we have to pass rand instead of seed we must reseed
		// after we fetch the array.
		Integer[] arr1 = fetchArray(size, range, seed, rand);
		rand.setSeed(seed);
		Test(arr1, null, new MedianStrategy(1,10,rand), false);
		
		// Sort using BasicStrategy and three-way partitioning.
		Test(fetchArray(size, range, seed, rand), null, null, true);
		
		// All the following sorts use this array.
		Integer[] arr2 = fetchArray(size, range, seed, rand);
		
		// Sort using MedianStrategy and three-way partitioning.
		rand.setSeed(seed);
		Test(arr2, null, new MedianStrategy(1,10,rand), true);
		
		// Sort the already sorted array with
		// BasicStrategy and normal partitioning.
		Test(arr2, null, null, false);
		
		// Sort the already sorted array with
		// BasicStrategy and three-way partitioning.
		Test(arr2, null, null, true);
		
		// Sort the already sorted array with
		// MedianStrategy and normal partitioning.
		rand.setSeed(seed);
		Test(arr2, null, new MedianStrategy(3,10,rand), false);
		
		return;
	}
	
	/**
	 * Generates an array of Integers based on the parameters.
	 * @param size The number of elements to be in the array.
	 * @param range The maximum value minus one of any element.
	 * Minimum value is zero.
	 * @param seen The seed for the random number generator.
	 * @return Returns an array of Integers with value between
	 * zero and range minus one.
	 * @param rand A random number generator to generate numbers.
	 * @author Donald Nye
	 */
	private static Integer[] fetchArray(int size, int range,
		long seed, Random rand)
	{
		Integer[] ret = new Integer[size];
		rand.setSeed(seed);
		
		for(int i = 0; i < size; i++)
			ret[i] = new Integer(rand.nextInt(range));
		
		return ret;
	}
	
	/**
	 * Runs a test sort of the array [arr].
	 * @param arr The array to sort.
	 * @param comp The method elements of [arr] should be compared by.
	 * @param stg The method of choosing pivots to be used.
	 * @param threeway Should three-way partitioning be used or not.
	 * @author Donald Nye
	 */
	private static void Test(Integer[] arr, Comparator comp,
		IPivotStrategy stg, boolean threeway)
	{
		QuickSorter<Integer> qs = null;
		
		try
		{
			if(comp == null)
				qs = new ComparableQuickSorter<Integer>(stg);
			else
				qs = new QuickSorter<Integer>(comp,stg);
			
			qs.setUseThreeWay(threeway);
			
			long start = System.currentTimeMillis();
			qs.sort(arr);
			long end = System.currentTimeMillis();
			
			String stg_name = stg == null ? "BasicStrategy" :
				stg.getClass().toString();
			stg_name = stg_name.substring(stg_name.lastIndexOf(".") + 1);
			
			output(stg_name, qs.getUseThreeWay(), end - start,
				qs.getComparisons(), qs.getSwaps(), qs.getMaxDepth());
		}
		catch(StackOverflowError e)
		{
			System.err.println("Stack Overflow! Depth of recursion: "
				+ qs.getMaxDepth() + "\n");
		}
		
		return;
	}
	
	/**
	 * Prints a set of output to reveal data from the execution of a sort.
	 * @param stg The name of the strategy being used.
	 * @param threeway Whether three-way partioning is being used or not.
	 * @param time The time it took to execute the sort
	 * @param c The number of comparisons in the sort.
	 * @param s The number of swaps in the sort.
	 * @param d The maximum depth of the recursive calls.
	 * @author Donald Nye
	 */
	private static void output(String stg, boolean threeway, long time, int c,
		int s, int d)
	{
		System.out.printf("Pivot Strategy: %s - Three Way Partitioning: %b\n",
			stg, threeway);
		
		System.out.printf("Comparisons: %d - Swaps: %d - Maximum Depth: %d\n",
			c, s, d);
		
		System.out.printf("Ellapsed Time: %d\n\n",time);
		
		return;
	}
}
