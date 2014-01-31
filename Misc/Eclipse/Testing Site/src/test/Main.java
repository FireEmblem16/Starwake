package test;

import java.util.Scanner;
import tree.TreeDiagram;

/**
 * A small interface to test TreeDiagram as a class.
 */
public class Main
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		TreeDiagram<Integer> Tree = new TreeDiagram<Integer>("Yggdrasil");
		String cmd = "";
		
		while(cmd != null)
		{
			cmd = in.nextLine();
			
			try
			{
				if(cmd.equals("p -c"))
					System.out.println(Tree);
				else if(cmd.equals("p -s"))
					System.out.println(Tree.GetStem());
				else if(cmd.equals("p -d"))
					System.out.println(Tree.GetData());
				else if(cmd.equals("b"))
				{
					if(!Tree.ExitBranch())
						System.out.println("Could not exit Branch.");
				}
				else if(cmd.equals("t"))
					Tree.ReturnToTrunk();
				else if(cmd.equals("s"))
					Tree.ReturnToSeed();
				else if(cmd.equals("d"))
					System.out.println(Tree.Depth());
				else if(cmd.equals("#a"))
					System.out.println(Tree.GetTotalNumberOfSprouts());
				else if(cmd.equals("#c"))
					System.out.println(Tree.GetNumberOfSprouts());
				else if(cmd.equals("exit"))
					cmd = null;
				else if(cmd.substring(0,3).equals("a -"))
					try
					{
						int ITemp = Integer.parseInt(cmd.substring(3,cmd.length()));
						
						for(int i = 0;i < ITemp;i++)
							Tree.AddBranch();
					}
					catch(NumberFormatException e)
					{
						if(!Tree.AddBranch(cmd.substring(3,cmd.length())))
							System.out.println("Could not add branch as \'" + cmd.substring(3,cmd.length()) + "\'. Changing to deafault name.");
					}
				else if(cmd.substring(0,3).equals("e -"))
					try
					{
						if(!Tree.EnterBranch(Integer.parseInt(cmd.substring(3,cmd.length()))))
							System.out.println("Could not enter branch");
					}
					catch(NumberFormatException e)
					{
						if(!Tree.EnterBranch(cmd.substring(3,cmd.length())))
							System.out.println("Could not enter branch");
					}
				else if(cmd.substring(0,3).equals("r -"))
					try
					{
						int ITemp = Integer.parseInt(cmd.substring(3,cmd.length()));
						
						if(!Tree.RemoveBranch(ITemp))
							System.out.println("Could not remove branch " + cmd.substring(3,cmd.length()) + ".");
					}
					catch(NumberFormatException e)
					{
						if(!Tree.RemoveBranch(cmd.substring(3,cmd.length())))
							System.out.println("Could not remove branch \'" + cmd.substring(3,cmd.length()) + "\'.");
					}
			}
			catch(StringIndexOutOfBoundsException e){}
		}
		
		return;
	}
}