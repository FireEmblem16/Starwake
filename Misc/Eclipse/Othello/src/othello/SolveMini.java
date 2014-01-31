///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Omacron Storm /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// othello Package ////////////////////////////////////
//////////////////////////////////// Solve Mini ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
//////////////////////////// Solves the mini Othello board //////////////////////////// 
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
package othello;

import tree.Root;
import tree.TreeDiagram;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Solves a mini Othello board.
 */
public class SolveMini
{
	/**
	 * The entry point of the solver.
	 * @param args
	 * An unused command line variable.
	 * @return <b>void</b>
	 */
	public static void main(String[] args)
	{
		SolveMini Viewer = new SolveMini();
		Viewer.root = new Root("Starting Board");
		
		Viewer.Explore();
		
		return;
	}
	
	/**
	 * Creates all of the boards for Othello.
	 * @param Base
	 * The board that the next move will be based on.
	 * @param player
	 * The current player. 1 is white. 2 is black.
	 * @return <b>void</b>
	 */
	protected void Recurse(MiniBoard Base, int player)
	{	
		for(int i = 0;i < Base.GetNumberOfLegalMoves(player);i++)
		{
			MiniBoard next = new MiniBoard();
			next.CopyBoard(Base);
			
			root.AddBranchFast();
			if(!root.EnterBranch(i + 1))
				System.out.println("Fail");
			
			int[] NxMove = next.GetLegalMove(i+1,player);
			boolean suc = (player == 1) ? next.PlaceWhite(NxMove[0],NxMove[1]) : next.PlaceBlack(NxMove[0],NxMove[1]); 
			root.SetData(next);
			
			boolean eX = true;
			TreeDiagram TTemp = null;
			
			if((TTemp = next.Exists(next,root.GetTrunk())) != null)
			{
				root.ExitBranch();
				root.EditBranch(root.GetBranches().length,TTemp);
				
				eX = false;
			}
			
			if(eX)
			{
				this.Recurse(next,(player == 1) ? 2 : 1);
				root.ExitBranch();
			}
		}
		
		if(Base.GetNumberOfLegalMoves(player) == 0 && Base.GetNumberOfLegalMoves((player == 1) ? 2 : 1) > 0)
		{
			MiniBoard next = new MiniBoard();
			next.CopyBoard(Base);
			
			root.AddBranchFast();
			if(!root.EnterBranch(1))
				System.out.println("Fail");
			
			if(player == 1)
				next.PingWhite();
			else
				next.PingBlack();
			
			root.SetData(next);
			
			this.Recurse(Base,(player == 1) ? 2 : 1);
			root.ExitBranch();
		}
		
		return;
	}
	
	/**
	 * Creates all of the boards for Othello.
	 * @param Base
	 * The board that the next move will be based on.
	 * @param player
	 * The current player. 1 is white. 2 is black.
	 * @param turn
	 * The current level of plays. Used to determine relative hierarchy when solving.
	 * @return <b>void</b>
	 */
	protected void DebugRecurse(MiniBoard Base, int player, int turn)
	{	
		System.out.println(turn);
		
		for(int i = 0;i < Base.GetNumberOfLegalMoves(player);i++)
		{
			MiniBoard next = new MiniBoard();
			next.CopyBoard(Base);
			
			root.AddBranchFast();
			if(!root.EnterBranch(i + 1))
				System.out.println("Fail");
			
			int[] NxMove = next.GetLegalMove(i+1,player);
			boolean suc = (player == 1) ? next.PlaceWhite(NxMove[0],NxMove[1]) : next.PlaceBlack(NxMove[0],NxMove[1]); 
			root.SetData(next);
			
			boolean eX = true;
			TreeDiagram TTemp = null;
			
			if((TTemp = next.Exists(next,root.GetTrunk())) != null)
			{
				root.ExitBranch();
				root.EditBranch(root.GetBranches().length,TTemp);
				
				eX = false;
			}
			
			if(eX)
			{
				this.DebugRecurse(next,(player == 1) ? 2 : 1,turn + 1);
				root.ExitBranch();
			}
		}
		
		if(Base.GetNumberOfLegalMoves(player) == 0 && Base.GetNumberOfLegalMoves((player == 1) ? 2 : 1) > 0)
		{
			MiniBoard next = new MiniBoard();
			next.CopyBoard(Base);
			
			root.AddBranchFast();
			if(!root.EnterBranch(1))
				System.out.println("Fail");
			
			if(player == 1)
				next.PingWhite();
			else
				next.PingBlack();
			
			root.SetData(next);
			
			this.DebugRecurse(Base,(player == 1) ? 2 : 1,turn + 1);
			root.ExitBranch();
		}
		
		return;
	}
	
	/**
	 * Allows for the viewing of the generated boards of Othello.
	 * @return <b>void</b>
	 */
	protected void Explore()
	{
		Scanner in = new Scanner(System.in);
		String cmd = "";
		
		System.out.println("Awaiting you commands!");
		
		while(cmd != null)
		{
			cmd = in.nextLine();
			
			try
			{
				if(cmd.equals("p -c"))
					System.out.println(root);
				else if(cmd.equals("p -s"))
					System.out.println(root.GetStem());
				else if(cmd.equals("p -d"))
					System.out.println(root.GetData());
				else if(cmd.equals("b"))
				{
					if(!root.ExitBranch())
						System.out.println("Could not exit Branch.");
				}
				else if(cmd.equals("Create"))
				{
					this.root = new Root("Starting Board");
					MiniBoard start = new MiniBoard();
					
					start.PlaceWhite(3,3);
					start.PlaceWhite(4,4);
					start.PlaceBlack(3,4);
					start.PlaceBlack(4,3);
					
					this.root.SetData(start);
					
					this.Recurse(start,1);
				}
				else if(cmd.equals("DebugCreate"))
				{
					this.root = new Root("Starting Board");
					MiniBoard start = new MiniBoard();
					
					start.PlaceWhite(3,3);
					start.PlaceWhite(4,4);
					start.PlaceBlack(3,4);
					start.PlaceBlack(4,3);
					
					this.root.SetData(start);
					
					this.DebugRecurse(start,1,1);
				}
				else if(cmd.equals("DebugCreateBoard"))
				{
					this.root = new Root("Starting Board");
					MiniBoard start = new MiniBoard();

					byte[][] ITemp = new byte[6][6];
					
					for(int i = 0;i < 6;i++)
					{
						String STemp = in.nextLine();
						StringTokenizer t = new StringTokenizer(STemp);
						for(int j = 0;j < 6;j++)
							ITemp[i][j] = (byte)Integer.parseInt(t.nextToken());
					}
					
					start.CopyBoard(ITemp);
					this.root.SetData(start);
					
					this.DebugRecurse(start,1,1);
				}
				else if(cmd.equals("ANALIZE!"))
				{
					this.Analize(this.root.GetTrunk());
				}
				else if(cmd.equals("ANALIZE! Next Move"))
				{
					for(int i = 1;i < this.root.GetBranches().length + 1;i++)
						if(((MiniBoard)this.root.GetBranch(i).GetData()).GetStorageValue() == 1)
							System.out.println("Branch Board: " + i + ", Winner: White");
						else if(((MiniBoard)this.root.GetBranch(i).GetData()).GetStorageValue() == 2)
							System.out.println("Branch Board: " + i + ", Winner: Black");
						else
							System.out.println("Branch Board: " + i + ", Winner: Tied Board");
				}
				else if(cmd.equals("ANALIZE! DEBUG!"))
				{
					this.DebugAnalize(this.root.GetCurrentBranch(),1);
				}
				else if(cmd.equals("f"))
				{
					byte[][] ITemp = new byte[6][6];
					
					for(int i = 0;i < 6;i++)
					{
						String STemp = in.nextLine();
						StringTokenizer t = new StringTokenizer(STemp);
						for(int j = 0;j < 6;j++)
							ITemp[i][j] = (byte)Integer.parseInt(t.nextToken());
					}
					
					MiniBoard MTemp = new MiniBoard();
					MTemp.CopyBoard(ITemp);
					TreeDiagram TTemp = MTemp.Exists(MTemp,root.GetTrunk());
					
					if(TTemp != null)
					{
						String STemp = TTemp.GetName();
						TTemp.SetName("Enter Me");
						root.EnterBranch("Enter Me");
						TTemp.SetName(STemp);
					}
					else
						System.out.println("No such board exists.");
				}
				else if(cmd.equals("t"))
					root.ReturnToTrunk();
				else if(cmd.equals("s"))
					root.ReturnToSeed();
				else if(cmd.equals("d"))
					System.out.println(root.Depth());
				else if(cmd.equals("#a"))
					System.out.println(root.GetTotalNumberOfSprouts());
				else if(cmd.equals("#c"))
					System.out.println(root.GetNumberOfSprouts());
				else if(cmd.equals("exit"))
					cmd = null;
				else if(cmd.substring(0,3).equals("a -"))
					try
					{
						int ITemp = Integer.parseInt(cmd.substring(3,cmd.length()));
						
						for(int i = 0;i < ITemp;i++)
							root.AddBranch();
					}
					catch(NumberFormatException e)
					{
						if(!root.AddBranch(cmd.substring(3,cmd.length())))
							System.out.println("Could not add branch as \'" + cmd.substring(3,cmd.length()) + "\'. Changing to deafault name.");
					}
				else if(cmd.substring(0,3).equals("e -"))
					try
					{
						if(!root.EnterBranch(Integer.parseInt(cmd.substring(3,cmd.length()))))
							System.out.println("Could not enter branch");
					}
					catch(NumberFormatException e)
					{
						if(!root.EnterBranch(cmd.substring(3,cmd.length())))
							System.out.println("Could not enter branch");
					}
				else if(cmd.substring(0,3).equals("r -"))
					try
					{
						int ITemp = Integer.parseInt(cmd.substring(3,cmd.length()));
						
						if(!root.RemoveBranch(ITemp))
							System.out.println("Could not remove branch " + cmd.substring(3,cmd.length()) + ".");
					}
					catch(NumberFormatException e)
					{
						if(!root.RemoveBranch(cmd.substring(3,cmd.length())))
							System.out.println("Could not remove branch \'" + cmd.substring(3,cmd.length()) + "\'.");
					}
			}
			catch(StringIndexOutOfBoundsException e){}
		}
	}
	
	/**
	 * Analizes the board to determine winning plays.
	 * @param Tree
	 * The branch to start in.
	 * @return void
	 * Simply sets the StoargeValues in the MiniBoards.
	 */
	public void Analize(TreeDiagram Tree)
	{
		if(Tree.GetBranches().length == 0)
		{
			if(((MiniBoard)Tree.GetData()).CountBlackSpace() > ((MiniBoard)Tree.GetData()).CountWhiteSpace())
				((MiniBoard)Tree.GetData()).SetStorageValue(2);
			else if(((MiniBoard)Tree.GetData()).CountBlackSpace() < ((MiniBoard)Tree.GetData()).CountWhiteSpace())
				((MiniBoard)Tree.GetData()).SetStorageValue(1);
			else
				((MiniBoard)Tree.GetData()).SetStorageValue(3);
		}
		else
		{
			for(int i = 0;i < Tree.GetBranches().length;i++)
				this.Analize(Tree.GetBranch(i + 1));
			
			switch(((MiniBoard)Tree.GetData()).GetLastMover())
			{
			case 1:
				((MiniBoard)Tree.GetData()).SetStorageValue(3);
				
				for(int i = 0;i < Tree.GetBranches().length;i++)
					if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() == 2)
					{
						((MiniBoard)Tree.GetData()).SetStorageValue(2);
						i = Tree.GetBranches().length - 1;
					}
				
				if(((MiniBoard)Tree.GetData()).GetStorageValue() == 3)
					for(int i = 0;i < Tree.GetBranches().length;i++)
						if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() != 3)
						{
							((MiniBoard)Tree.GetData()).SetStorageValue(1);
							i = Tree.GetBranches().length - 1;
						}
				
				break;
			case 2:
				((MiniBoard)Tree.GetData()).SetStorageValue(3);
				
				for(int i = 0;i < Tree.GetBranches().length;i++)
					if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() == 1)
					{
						((MiniBoard)Tree.GetData()).SetStorageValue(1);
						i = Tree.GetBranches().length - 1;
					}
				
				if(((MiniBoard)Tree.GetData()).GetStorageValue() == 3)
					for(int i = 0;i < Tree.GetBranches().length;i++)
						if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() != 3)
						{
							((MiniBoard)Tree.GetData()).SetStorageValue(2);
							i = Tree.GetBranches().length - 1;
						}
				
				break;
			case 0:
			default:
				break;
			}
		}
		
		return;
	}
	
	/**
	 * Analizes the board to determine winning plays.
	 * Displays data to the command line indicating current depth.
	 * @param Tree
	 * The branch to start in.
	 * @param depth
	 * The current depth into the tree.
	 * @return void
	 * Simply sets the StoargeValues in the MiniBoards.
	 */
	public void DebugAnalize(TreeDiagram Tree, int depth)
	{
		System.out.println(depth);
		
		if(Tree.GetBranches().length == 0)
		{
			if(((MiniBoard)Tree.GetData()).CountBlackSpace() > ((MiniBoard)Tree.GetData()).CountWhiteSpace())
				((MiniBoard)Tree.GetData()).SetStorageValue(2);
			else if(((MiniBoard)Tree.GetData()).CountBlackSpace() < ((MiniBoard)Tree.GetData()).CountWhiteSpace())
				((MiniBoard)Tree.GetData()).SetStorageValue(1);
			else
				((MiniBoard)Tree.GetData()).SetStorageValue(3);
		}
		else
		{
			for(int i = 0;i < Tree.GetBranches().length;i++)
				this.DebugAnalize(Tree.GetBranch(i + 1),depth + 1);
			
			switch(((MiniBoard)Tree.GetData()).GetLastMover())
			{
			case 1:
				((MiniBoard)Tree.GetData()).SetStorageValue(3);
				
				for(int i = 0;i < Tree.GetBranches().length;i++)
					if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() == 2)
					{
						((MiniBoard)Tree.GetData()).SetStorageValue(2);
						i = Tree.GetBranches().length - 1;
					}
				
				if(((MiniBoard)Tree.GetData()).GetStorageValue() == 3)
					for(int i = 0;i < Tree.GetBranches().length;i++)
						if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() != 3)
						{
							((MiniBoard)Tree.GetData()).SetStorageValue(1);
							i = Tree.GetBranches().length - 1;
						}
				
				break;
			case 2:
				((MiniBoard)Tree.GetData()).SetStorageValue(3);
				
				for(int i = 0;i < Tree.GetBranches().length;i++)
					if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() == 1)
					{
						((MiniBoard)Tree.GetData()).SetStorageValue(1);
						i = Tree.GetBranches().length - 1;
					}
				
				if(((MiniBoard)Tree.GetData()).GetStorageValue() == 3)
					for(int i = 0;i < Tree.GetBranches().length;i++)
						if(((MiniBoard)Tree.GetBranch(i + 1).GetData()).GetStorageValue() != 3)
						{
							((MiniBoard)Tree.GetData()).SetStorageValue(2);
							i = Tree.GetBranches().length - 1;
						}
				
				break;
			case 0:
			default:
				break;
			}
		}
		
		return;
	}
	
	public Root root;
}