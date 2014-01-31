package sort;

import java.util.Random;
import tree.Tree;

public class Sort
{
	public static void main(String[] args)
	{
		Random rand = new Random();
		
		Integer[] ints = new Integer[1024];
		for(int i = 0;i < ints.length; i++)
			ints[i] = new Integer(rand.nextInt() / (2 << 16));
		
		Tree<Integer> root = new Tree<Integer>();
		
		for(int i = 0;i < ints.length;i++)
			add(root,ints[i]);
		
		printweird(root,1/*,1*/);
		
		/*for(int i = 0, j = 0;i < calldepths.length && (j == 0 || calldepths[i] != 0);i++)
		{
			if(j == 0 && calldepths[i] != 0)
				j = 1;
			
			if(j != 0)
				System.out.println("depth " + i + ": " + calldepths[i]);
		}*/
		
		return;
	}
	
	public static void printweird(Tree root, int lcr/*, int calldepth*/)
	{
		/*if(calldepths == null)
			calldepths = new int[1000];*/
		
		if(root == null) // If root is null we hit a super leaf and should gtfo
		{
			/*calldepths[calldepth]++;*/
			return;
		}
		
		if(lcr == 1) // If we are a root node then we don't have data
		{
			printweird(root.left,0/*,calldepth + 1*/);
			printweird(root.right,2/*,calldepth + 1*/);
		}
		else if(lcr == 0) // If we are a left node everything below us is smaller than us
		{
			printweird(root.left,0/*,calldepth + 1*/);
			printweird(root.right,2/*,calldepth + 1*/);
			System.out.println(root.data/* + "   calldepth: " + calldepth*/);
		}
		else if(lcr == 2) // If we are a right node then everything below us is bigger than us
		{
			System.out.println(root.data/* + "   calldepth: " + calldepth*/);
			printweird(root.left,0/*,calldepth + 1*/);
			printweird(root.right,2/*,calldepth + 1*/);
		}
		
		return;
	}
	
	public static void printbredth(Tree root)
	{
		if(root == null)
			return;
		
		if(root.data != null)
			System.out.println(root);
		
		printbredth(root.left);
		printbredth(root.right);
		
		return;
	}
	
	public static void printlcr(Tree root)
	{
		if(root == null)
			return;
		
		printlcr(root.left);
		
		if(root.data != null)
			System.out.println(root);
		
		printlcr(root.right);
		
		return;
	}
	
	public static void printlrc(Tree root)
	{
		if(root == null)
			return;
		
		printlrc(root.left);
		printlrc(root.right);
		
		if(root.data != null)
			System.out.println(root);
		
		return;
	}
	
	public static void add(Tree<Integer> root, Integer val)
	{
		// If we don't have a left value then we can just dump val into it
		if(root.left == null)
		{
			root.left = new Tree<Integer>(val);
			return;
		}
		
		// If we don't have a right value we can dump val into it iff it is >= than the left value 
		if(root.right == null)
		{
			if(root.left.data < val)
			{
				root.right = new Tree<Integer>(val);
				return;
			}
			
			root.right = root.left;
			root.left = new Tree<Integer>(val);
			return;
		}
		
		// If the value is smaller than the left side, add to that side
		if(root.left.data >= val)
		{
			add(root.left,val);
			return;
		}
		
		// If the value is larger than the right side, add to that side
		if(root.right.data <= val)
		{
			add(root.right,val);
			return;
		}
		
		Integer temp = val;
		
		// Swap this value with whichever one it is closer to and then add to that side
		if(val - root.left.data <= root.right.data - val)
		{
			temp = root.left.data;
			root.left.data = val;
			val = temp;
			
			add(root.left,val);
		}
		else
		{
			temp = root.right.data;
			root.right.data = val;
			val = temp;
			
			add(root.right,val);
		}
		
		return;
	}
	
	//public static int[] calldepths;
}
