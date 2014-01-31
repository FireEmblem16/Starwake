package efficient;

import java.util.Random;
import tree.Tree;

public class Sort
{
	public static void main(String[] args)
	{
		//CreateMinimalStatistics();
		
		// If we know a bit about the data already we can balance it around two numbers we think
		// are the average values of the set which will improve performance by a bit
		// Note that worst case, low amount of value distribution like all ones, is still completely awful
		//CreateMinimalStatisticsAroundX(new Integer(-50000));
		//CreateMinimalStatisticsAroundX(new Integer(-1000));
		//CreateMinimalStatisticsAroundX(new Integer(0));
		//CreateMinimalStatisticsAroundX(new Integer(1000));
		//CreateMinimalStatisticsAroundX(new Integer(50000));
		
		// -------------------------------------------------------------------------------------------------
		
		Integer[] ints = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		Integer[] ints2 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32};
		Integer[] ints3 = {1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
		Integer[] ints4 = {1,1,1,1,2,3,2,3,1,2,2,3,3,3,1,2,1,3,2,3,2,1,1,2,3,3,2,1,2,2,3,2,1,1,2,3,2,1,1,2,4,2,1,3,2,2,1,4};
		
		System.out.println("Number of elements sorted: " + ints.length + " --- Log(n,2) is between: " + roughlog(ints.length) + " and " + (roughlog(ints.length) + 1));
		RunMinimalTest(ints);
		RunMinimalTestBalancedAroundX(ints,new Integer(1));
		
		System.out.println("\nNumber of elements sorted: " + ints2.length + " --- Log(n,2) is between: " + roughlog(ints2.length) + " and " + (roughlog(ints2.length) + 1));
		RunMinimalTest(ints2);
		RunMinimalTestBalancedAroundX(ints2,new Integer(16));
		
		System.out.println("\nNumber of elements sorted: " + ints3.length + " --- Log(n,2) is between: " + roughlog(ints3.length) + " and " + (roughlog(ints3.length) + 1));
		RunMinimalTest(ints3);
		RunMinimalTestBalancedAroundX(ints3,new Integer(2));
		
		System.out.println("\nNumber of elements sorted: " + ints4.length + " --- Log(n,2) is between: " + roughlog(ints4.length) + " and " + (roughlog(ints4.length) + 1));
		RunMinimalTest(ints4);
		RunMinimalTestBalancedAroundX(ints4,new Integer(2));
		
		return;
	}
	
	public static int roughlog(Integer n)
	{
		if(n <= 1)
			return 0;
		
		int ret = 1;
		for(int i = 1;i << 2 < n;i*=2,ret++);
		return ret;
	}
	
	public static void CreateSomeStatistics()
	{
		Random rand = new Random();
		
		double ave = 0.0, high = 0.0, low = 1000000.0;
		
		for(int i = 0;i < 1024;i++)
		{
			calldepths = null;
			
			double temp = GetStats(rand);
			ave += temp;
			
			if(temp < low)
				low = temp;
			
			if(temp > high)
				high = temp;
		}
		
		ave /= 1024.0;
		
		System.out.println("\nTotal Average Leaf Depth over 1024 runs each with 1024 elements: " + ave);
		System.out.println("Highest Average Leaf Depth: " + high + " --- Lowest Average Leaf Depth: " + low);
		
		return;
	}
	
	public static void CreateSomeStatisticsAroundX(Integer X)
	{
		Random rand = new Random();
		
		double ave = 0.0, high = 0.0, low = 1000000.0;
		
		for(int i = 0;i < 1024;i++)
		{
			calldepths = null;
			
			double temp = BalanceAroundX(rand,X);
			ave += temp;
			
			if(temp < low)
				low = temp;
			
			if(temp > high)
				high = temp;
		}
		
		ave /= 1024.0;
		
		System.out.println("\nBalanced around: " + X);
		System.out.println("Total Average Leaf Depth over 1024 runs each with 1024+2 elements: " + ave);
		System.out.println("Highest Average Leaf Depth: " + high + " --- Lowest Average Leaf Depth: " + low);
		
		return;
	}
	
	public static void CreateMinimalStatistics()
	{
		Random rand = new Random();
		
		double ave = 0.0, high = 0.0, low = 1000000.0;
		
		for(int i = 0;i < 1024;i++)
		{
			calldepths = null;
			
			double temp = GetMinimalStats(rand);
			ave += temp;
			
			if(temp < low)
				low = temp;
			
			if(temp > high)
				high = temp;
		}
		
		ave /= 1024.0;
		
		System.out.println("\nTotal Average Leaf Depth over 1024 runs each with 1024 elements: " + ave);
		System.out.println("Highest Average Leaf Depth: " + high + " --- Lowest Average Leaf Depth: " + low);
		
		return;
	}
	
	public static void CreateMinimalStatisticsAroundX(Integer X)
	{
		Random rand = new Random();
		
		double ave = 0.0, high = 0.0, low = 1000000.0;
		
		for(int i = 0;i < 1024;i++)
		{
			calldepths = null;
			
			double temp = BalanceAroundXMinimize(rand,X);
			ave += temp;
			
			if(temp < low)
				low = temp;
			
			if(temp > high)
				high = temp;
		}
		
		ave /= 1024.0;
		
		System.out.println("\nBalanced around: " + X);
		System.out.println("Total Average Leaf Depth over 1024 runs each with 1024+2 elements: " + ave);
		System.out.println("Highest Average Leaf Depth: " + high + " --- Lowest Average Leaf Depth: " + low);
		
		return;
	}
	
	public static void RunTest(Integer[] ints)
	{
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		calldepths = null;
		printcalldepth(root,1,0);
		return;
	}
	
	public static void RunTestBalancedAroundX(Integer[] ints, Integer X)
	{
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		root.left = new Tree<Integer>(X,0);
		root.right = new Tree<Integer>(X,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		calldepths = null;
		printcalldepth(root,1,0);
		return;
	}
	
	public static void RunMinimalTest(Integer[] ints)
	{
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		calldepths = null;
		printstatsonly(root,1,0);
		return;
	}
	
	public static void RunMinimalTestBalancedAroundX(Integer[] ints, Integer X)
	{
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		root.left = new Tree<Integer>(X,0);
		root.right = new Tree<Integer>(X,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		calldepths = null;
		printstatsonly(root,1,0);
		return;
	}
	
	public static double GetStats(Random rand)
	{
		Integer[] ints = new Integer[1024];
		for(int i = 0;i < ints.length; i++)
			ints[i] = new Integer(rand.nextInt() / (2 << 16));
		
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		return printstatsonly(root,1,0);
	}
	
	public static double BalanceAroundX(Random rand, Integer X)
	{
		Integer[] ints = new Integer[1024];
		for(int i = 0;i < ints.length; i++)
			ints[i] = new Integer(rand.nextInt() / (2 << 16));
		
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		root.left = new Tree<Integer>(X,0);
		root.right = new Tree<Integer>(X,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		return printstatsonly(root,1,0);
	}
	
	public static double GetMinimalStats(Random rand)
	{
		Integer[] ints = new Integer[1024];
		for(int i = 0;i < ints.length; i++)
			ints[i] = new Integer(rand.nextInt() / (2 << 16));
		
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		return getstatsonly(root,1,0);
	}
	
	public static double BalanceAroundXMinimize(Random rand, Integer X)
	{
		Integer[] ints = new Integer[1024];
		for(int i = 0;i < ints.length; i++)
			ints[i] = new Integer(rand.nextInt() / (2 << 16));
		
		Tree<Integer> root = new Tree<Integer>(0,0);
		
		root.left = new Tree<Integer>(X,0);
		root.right = new Tree<Integer>(X,0);
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		return getstatsonly(root,1,0);
	}
	
	public static void add(Tree<Integer> root, Integer val)
	{
		if(root == null)
			return;
		
		// If we don't have a left value then we can just dump val into it
		if(root.left == null)
		{
			root.left = new Tree<Integer>(val,0);
			return;
		}
		
		// If we don't have a right value we can dump val into it iff it is <= than the left value
		if(root.right == null)
		{
			if(root.left.data > val)
			{
				root.right = new Tree<Integer>(val,0);
				return;
			}
			
			root.right = root.left;
			root.left = new Tree<Integer>(val,0);
			return;
		}
		
		// If we can send the number either way send it the way with less weight
		if(root.right.data == root.left.data && root.right.data == val)
		{
			if(root.right.weight < root.left.weight)
			{
				root.right.weight++;
				add(root.right,val);
			}
			else
			{
				root.left.weight++;
				add(root.left,val);
			}
			
			return;
		}
		
		// If the value is bigger than the left side, add to that side
		if(root.left.data <= val)
		{
			root.left.weight++;
			add(root.left,val);
			return;
		}
		
		// If the value is smaller than the right side, add to that side
		if(root.right.data >= val)
		{
			root.right.weight++;
			add(root.right,val);
			return;
		}
		
		Integer temp = val;
		
		// Swap this value with whichever one it is closer to and then add to that side
		if(root.right.data - val <= val - root.left.data)
		{
			temp = root.right.data;
			root.right.data = val;
			val = temp;
			
			root.right.weight++;
			add(root.right,val);
		}
		else
		{
			temp = root.left.data;
			root.left.data = val;
			val = temp;
			
			root.left.weight++;
			add(root.left,val);
		}
		
		return;
	}
	
	public static void printtree(Tree root, int lcr)
	{
		if(root == null)
			return;
		
		if(lcr == 1) // If we are a root node then we don't have data
		{
			printtree(root.right,2);
			printtree(root.left,0);
		}
		else if(lcr == 0) // If we are a left node everything below us is bigger than us
		{
			System.out.println(root.data);
			printtree(root.right,2);
			printtree(root.left,0);
		}
		else if(lcr == 2) // If we are a right node then everything below us is smaller than us
		{
			printtree(root.right,2);
			printtree(root.left,0);
			System.out.println(root.data);
		}
	}
	
	/*
	 * Returns the average depth of all leaves.
	 */
	public static double printcalldepth(Tree root, int lcr, int calldepth)
	{
		if(calldepths == null)
			calldepths = new int[1000];
		
		if(root == null) // If root is null we hit a super leaf and should gtfo
		{
			calldepths[calldepth - 1]++;
			return 0.0;
		}
		
		if(lcr == 1) // If we are a root node then we don't have data
		{
			if(root.right != null)
				printcalldepth(root.right,2,calldepth + 1);
			
			if(root.left != null)
				printcalldepth(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
			
			System.out.print("Call Depths:");
			
			double t = 0, n = 0;
			
			for(int i = 0, j = 0, k = 0;(calldepths[i] != 0 && k == 0) || j == 0;i++)
				if(calldepths[i] != 0)
				{
					j++;
					
					for(int l = i + 1;l < calldepths.length - 1 && calldepths[l] == 0;l++)
						if(calldepths[l + 1] != 0)
						{
							j = 0;
							break;
						}
					
					n += calldepths[i];
					t += calldepths[i] * i;
					
					System.out.print(" " + i + ": " + calldepths[i]);
				}
			
			System.out.println("\nNumber of Leaves: " + n + " Average Leaf Depth: " + (t / n));
			return (t / n);
		}
		else if(lcr == 0) // If we are a left node everything below us is bigger than us
		{
			System.out.println(root.data + "   calldepth: " + calldepth);
			
			if(root.right != null)
				printcalldepth(root.right,2,calldepth + 1);
			
			if(root.left != null)
				printcalldepth(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
		}
		else if(lcr == 2) // If we are a right node then everything below us is smaller than us
		{
			if(root.right != null)
				printcalldepth(root.right,2,calldepth + 1);
			
			if(root.left != null)
				printcalldepth(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
			
			System.out.println(root.data + "   calldepth: " + calldepth);
		}
		
		return 0.0;
	}
	
	/*
	 * Returns the average depth of all leaves.
	 */
	public static double printstatsonly(Tree root, int lcr, int calldepth)
	{
		if(calldepths == null)
			calldepths = new int[1000];
		
		if(root == null) // If root is null we hit a super leaf and should gtfo
		{
			calldepths[calldepth - 1]++;
			return 0.0;
		}
		
		if(lcr == 1) // If we are a root node then we don't have data
		{
			if(root.right != null)
				printstatsonly(root.right,2,calldepth + 1);
			
			if(root.left != null)
				printstatsonly(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
			
			System.out.print("Call Depths:");
			
			double t = 0, n = 0;
			
			for(int i = 0, j = 0, k = 0;(calldepths[i] != 0 && k == 0) || j == 0;i++)
				if(calldepths[i] != 0)
				{
					j++;
					
					for(int l = i + 1;l < calldepths.length - 1 && calldepths[l] == 0;l++)
						if(calldepths[l + 1] != 0)
						{
							j = 0;
							break;
						}
					
					n += calldepths[i];
					t += calldepths[i] * i;
					
					System.out.print(" " + i + ": " + calldepths[i]);
				}
			
			System.out.println("\nNumber of Leaves: " + n + " Average Leaf Depth: " + (t / n));
			return (t / n);
		}
		else if(lcr == 0) // If we are a left node everything below us is bigger than us
		{
			if(root.right != null)
				printstatsonly(root.right,2,calldepth + 1);
			
			if(root.left != null)
				printstatsonly(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
		}
		else if(lcr == 2) // If we are a right node then everything below us is smaller than us
		{
			if(root.right != null)
				printstatsonly(root.right,2,calldepth + 1);
			
			if(root.left != null)
				printstatsonly(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
		}
		
		return 0.0;
	}
	
	/*
	 * Returns the average depth of all leaves.
	 */
	public static double getstatsonly(Tree root, int lcr, int calldepth)
	{
		if(calldepths == null)
			calldepths = new int[1000];
		
		if(root == null) // If root is null we hit a super leaf and should gtfo
		{
			calldepths[calldepth - 1]++;
			return 0.0;
		}
		
		if(lcr == 1) // If we are a root node then we don't have data
		{
			if(root.right != null)
				getstatsonly(root.right,2,calldepth + 1);
			
			if(root.left != null)
				getstatsonly(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
			
			double t = 0, n = 0;
			
			for(int i = 0, j = 0, k = 0;(calldepths[i] != 0 && k == 0) || j == 0;i++)
				if(calldepths[i] != 0)
				{
					j++;
					
					for(int l = i + 1;l < calldepths.length - 1 && calldepths[l] == 0;l++)
						if(calldepths[l + 1] != 0)
						{
							j = 0;
							break;
						}
					
					n += calldepths[i];
					t += calldepths[i] * i;
				}
			
			return (t / n);
		}
		else if(lcr == 0) // If we are a left node everything below us is bigger than us
		{
			if(root.right != null)
				getstatsonly(root.right,2,calldepth + 1);
			
			if(root.left != null)
				getstatsonly(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
		}
		else if(lcr == 2) // If we are a right node then everything below us is smaller than us
		{
			if(root.right != null)
				getstatsonly(root.right,2,calldepth + 1);
			
			if(root.left != null)
				getstatsonly(root.left,0,calldepth + 1);
			
			if(root.left == null && root.right == null)
				calldepths[calldepth]++;
		}
		
		return 0.0;
	}
	
	public static int[] calldepths;
}
