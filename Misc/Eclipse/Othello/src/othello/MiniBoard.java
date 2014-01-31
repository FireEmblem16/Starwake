///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Omacron Storm /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// othello Package ////////////////////////////////////
//////////////////////////////////// Mini Board ///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////// Contains the structure for an Othello Board. //////////////////// 
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
package othello;

import tree.TreeDiagram;

/**
 * Contains the data for a mini Othello Board.
 */
public class MiniBoard
{
	/**
	 * Creates a new mini Othello board.
	 * @return <b>Board*</b>
	 * Returns a pointer to a new Board.
	 */
	public MiniBoard()
	{
		this.board = new byte[6][6];
		this.LastMove = 0;
		this.StorageValue = 0;
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				this.board[i][j] = 0;
		
		return;
	}
	
	/**
	 * Obtains a pointer to the board.
	 * @return <b>byte[][]</b>
	 * Returns a pointer to the board of this object.
	 */
	public byte[][] ObtainBoard()
	{
		return this.board;
	}
	
	/**
	 * Places a white piece on the board.
	 * @param x
	 * The x coordinate of the board to be placed at.
	 * @param y
	 * The y coordinate of the board to be placed at.
	 * @return <b>boolean</b>
	 * Returns true if the space was empty, else returns false.
	 */
	public boolean PlaceWhite(int x, int y)
	{
		if(x > 6 || x < 1 || y > 6 || y < 1)
			return false;
		
		if(this.board[x-1][y-1] != 0)
			return false;
		
		this.board[x-1][y-1] = 1;
		Flip(x-1,y-1,1);
		this.LastMove = 1;
		
		return true;
	}
	
	/**
	 * Places a black piece on the board.
	 * @param x
	 * The x coordinate of the board to be placed at.
	 * @param y
	 * The y coordinate of the board to be placed at.
	 * @return <b>boolean</b>
	 * Returns true if the space was empty, else returns false.
	 */
	public boolean PlaceBlack(int x, int y)
	{
		if(x > 6 || x < 1 || y > 6 || y < 1)
			return false;
		
		if(this.board[x-1][y-1] != 0)
			return false;
		
		this.board[x-1][y-1] = 2;
		Flip(x-1,y-1,2);
		this.LastMove = 2;
		
		return true;
	}
	
	/**
	 * Flips the appropriate pieces to make a move.
	 * @param x
	 * The x coordinate to flip from.
	 * @param y
	 * The y coordinate to flip from.
	 * @param color
	 * The color to flip for. 1 is white. 2 is black.
	 */
	protected void Flip(int x, int y, int color)
	{
		int opp = (color == 1) ? 2 : 1;
		
		for(int i = 0;i < 8;i++)
		{
			try
			{
				switch(i)
				{
				case 0:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int j = y - 1;j >= 0;j--)
						{
							if(this.board[x][j] == color)
							{
								j = 0;
								continue;
							}
						
							this.board[x][j] = (byte)color;
						}
					}
					break;
				case 1:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int j = y - 1, l = x + 1;j >= 0 && l < 6;j--,l++)
						{
							if(this.board[l][j] == color)
							{
								j = 0;
								continue;
							}
						
							this.board[l][j] = (byte)color;
						}
					}
					break;
				case 2:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int l = x + 1;l < 6;l++)
						{
							if(this.board[l][y] == color)
							{
								l = 6;
								continue;
							}
						
							this.board[l][y] = (byte)color;
						}
					}
					break;
				case 3:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int j = y + 1, l = x + 1;j < 6 && l < 6;j++,l++)
						{
							if(this.board[l][j] == color)
							{
								j = 6;
								continue;
							}
						
							this.board[l][j] = (byte)color;
						}
					}
					break;
				case 4:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int j = y + 1;j < 6;j++)
						{
							if(this.board[x][j] == color)
							{
								j = 6;
								continue;
							}
						
							this.board[x][j] = (byte)color;
						}
					}
					break;
				case 5:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int j = y + 1, l = x - 1;j < 6 && l >= 0;j++,l--)
						{
							if(this.board[l][j] == color)
							{
								j = 6;
								continue;
							}
						
							this.board[l][j] = (byte)color;
						}
					}
					break;
				case 6:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int l = x - 1;l >= 0;l--)
						{
							if(this.board[l][y] == color)
							{
								l = 0;
								continue;
							}
						
							this.board[l][y] = (byte)color;
						}
					}
					break;
				case 7:
					if(this.IsLegalMoveDirI(x+1,y+1,color,i))
					{
						for(int j = y - 1, l = x - 1;j >= 0 && l >= 0;j--,l--)
						{
							if(this.board[l][j] == color)
							{
								j = 0;
								continue;
							}
						
							this.board[l][j] = (byte)color;
						}
					}
					break;
				default:
					return;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{}
		}
		
		return;
	}
	
	/**
	 * Returns the (move)th legal move for white.
	 * @param move
	 * The offset into possible moves for white. Offsets are constant.
	 * @return <b>int[]</b>
	 * Returns null if there is no legal move or the move exceeded the number possible.
	 * Else returns the location on the board for the (move)th legal move for white.
	 */
	public int[] GetLegalWhiteMove(int move)
	{
		return this.GetLegalMove(move,1);
	}
	
	/**
	 * Returns the (move)th legal move for black.
	 * @param move
	 * The offset into possible moves for black. Offsets are constant.
	 * @return <b>int[]</b>
	 * Returns null if there is no legal move or the move exceeded the number possible.
	 * Else returns the location on the board for the (move)th legal move for black.
	 */
	public int[] GetLegalBlackMove(int move)
	{
		return this.GetLegalMove(move,2);
	}
	
	/**
	 * Returns the (move)th legal move for color [color].
	 * @param move
	 * The offset into possible moves for white. Offsets are constant.
	 * @param color
	 * The color to get a legal move for. 1 is white. 2 is black.
	 * @return <b>int[]</b>
	 * Returns null if there is no legal move or the move exceeded the number possible.
	 * Else returns the location on the board for the (move)th legal move for color [color].
	 */
	public int[] GetLegalMove(int move, int color)
	{
		int[] space = {0,0};
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(this.IsLegalMove(i+1,j+1,color))
					if(move == 1)
					{
						space[0] = i + 1;
						space[1] = j + 1;
						
						i = 6;
						j = 6;
					}
					else
						move--;
		
		if(space[0] == 0 && space[1] == 0)
			space = null;
		
		return space;
	}
	
	/**
	 * Returns the number of legal moves for White.
	 * @return <b>int</b>
	 * Returns White's number of legal moves.
	 */
	public int GetNumberOfLegalWhiteMoves()
	{
		return GetNumberOfLegalMoves(1);
	}
	
	/**
	 * Returns the number of legal moves for Black.
	 * @return <b>int</b>
	 * Returns Black's number of legal moves.
	 */
	public int GetNumberOfLegalBlackMoves()
	{
		return GetNumberOfLegalMoves(2);
	}
	
	/**
	 * Returns the number of legal moves for color [color].
	 * @param color
	 * The color to get a legal move for. 1 is white. 2 is black.
	 * @return <b>int</b>
	 * Returns the number of legal moves for color [color].
	 */
	public int GetNumberOfLegalMoves(int color)
	{
		int num = 0;
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(this.IsLegalMove(i+1,j+1,color))
					num++;
		
		return num;
	}
	
	/**
	 * Determines if a space on the board is a legal move for white.
	 * @param x
	 * The x coordinate to determine for legality.
	 * @param y
	 * The y coordinate to determine for legality.
	 * @return <b>boolean</b>
	 * Returns true if the move is legal. Else returns false.
	 */
	public boolean IsLegalWhiteMove(int x, int y)
	{
		return this.IsLegalMove(x,y,1);
	}
	
	/**
	 * Determines if a space on the board is a legal move for black.
	 * @param x
	 * The x coordinate to determine for legality.
	 * @param y
	 * The y coordinate to determine for legality.
	 * @return <b>boolean</b>
	 * Returns true if the move is legal. Else returns false.
	 */
	public boolean IsLegalBlackMove(int x, int y)
	{
		return this.IsLegalMove(x,y,2);
	}
	
	/**
	 * Determines if a space on the board is a legal move for color [color].
	 * @param x
	 * The x coordinate to determine for legality.
	 * @param y
	 * The y coordinate to determine for legality.
	 * @param color
	 * The color to check legality for. 1 is white. 2 is black.
	 * @return <b>boolean</b>
	 * Returns true if the move is legal. Else returns false.
	 */
	protected boolean IsLegalMove(int x, int y, int color)
	{
		x--;
		y--;
		
		try
		{
			if(this.board[x][y] != 0)
				return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{}
		
		int opp = (color == 1) ? 2 : 1;
		
		for(int i = 0;i < 8;i++)
			try
			{
				switch(i)
				{
				case 0:
					if(this.board[x][y-1] == opp)
						for(int Ty = y - 1;Ty >= 0;Ty--)
							if(this.board[x][Ty] == color)
								return true;
							else if(this.board[x][Ty] == 0)
							{
								Ty = 0;
								continue;
							}
					break;
				case 1:
					if(this.board[x+1][y-1] == opp)
						for(int Tx = x + 1,Ty = y - 1;Tx < 6 && Ty >= 0;Tx++,Ty--)
							if(this.board[Tx][Ty] == color)
								return true;
							else if(this.board[Tx][Ty] == 0)
							{
								Tx = 6;
								continue;
							}
					break;
				case 2:
					if(this.board[x+1][y] == opp)
						for(int Tx = x + 1;Tx < 6;Tx++)
							if(this.board[Tx][y] == color)
								return true;
							else if(this.board[Tx][y] == 0)
							{
								Tx = 6;
								continue;
							}
					break;
				case 3:
					if(this.board[x+1][y+1] == opp)
						for(int Tx = x + 1,Ty = y + 1;Tx < 6 && Ty < 6;Tx++,Ty++)
							if(this.board[Tx][Ty] == color)
								return true;
							else if(this.board[Tx][Ty] == 0)
							{
								Ty = 6;
								continue;
							}
					break;
				case 4:
					if(this.board[x][y+1] == opp)
						for(int Ty = y + 1;Ty < 6;Ty++)
							if(this.board[x][Ty] == color)
								return true;
							else if(this.board[x][Ty] == 0)
							{
								Ty = 6;
								continue;
							}
					break;
				case 5:
					if(this.board[x-1][y+1] == opp)
						for(int Tx = x - 1,Ty = y + 1;Tx >= 0 && Ty < 6;Tx--,Ty++)
							if(this.board[Tx][Ty] == color)
								return true;
							else if(this.board[Tx][Ty] == 0)
							{
								Tx = 0;
								continue;
							}
					break;
				case 6:
					if(this.board[x-1][y] == opp)
						for(int Tx = x - 1;Tx >= 0;Tx--)
							if(this.board[Tx][y] == color)
								return true;
							else if(this.board[Tx][y] == 0)
							{
								Tx = 0;
								continue;
							}
					break;
				case 7:
					if(this.board[x-1][y-1] == opp)
						for(int Tx = x - 1,Ty = y - 1;Tx >= 0 && Ty >= 0;Tx--,Ty--)
							if(this.board[Tx][Ty] == color)
								return true;
							else if(this.board[Tx][Ty] == 0)
								return false;
					break;
				default:
					return false;
				}
			}
			catch(ArrayIndexOutOfBoundsException e)
			{}
		
		return false;
	}
	
	/**
	 * Determines if a space on the board is a legal move for color [color] in direction i.
	 * @param x
	 * The x coordinate to determine for legality.
	 * @param y
	 * The y coordinate to determine for legality.
	 * @param color
	 * The color to check legality for. 1 is white. 2 is black.
	 * @param i
	 * The direction to asssure. 0 is left, and proceeds counter clockwise.
	 * @return <b>boolean</b>
	 * Returns true if the move is legal. Else returns false.
	 */
	protected boolean IsLegalMoveDirI(int x, int y, int color, int i)
	{
		x--;
		y--;
		
		try
		{
			if(this.board[x][y] != color)
				return false;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{}
		
		int opp = (color == 1) ? 2 : 1;
		
		try
		{
			switch(i)
			{
			case 0:
				if(this.board[x][y-1] == opp)
					for(int Ty = y - 1;Ty >= 0;Ty--)
						if(this.board[x][Ty] == color)
							return true;
						else if(this.board[x][Ty] == 0)
							return false;
				break;
			case 1:
				if(this.board[x+1][y-1] == opp)
					for(int Tx = x + 1,Ty = y - 1;Tx < 6 && Ty >= 0;Tx++,Ty--)
						if(this.board[Tx][Ty] == color)
							return true;
						else if(this.board[Tx][Ty] == 0)
							return false;
				break;
			case 2:
				if(this.board[x+1][y] == opp)
					for(int Tx = x + 1;Tx < 6;Tx++)
						if(this.board[Tx][y] == color)
							return true;
					else if(this.board[Tx][y] == 0)
							return false;
				break;
			case 3:
				if(this.board[x+1][y+1] == opp)
					for(int Tx = x + 1,Ty = y + 1;Tx < 6 && Ty < 6;Tx++,Ty++)
						if(this.board[Tx][Ty] == color)
							return true;
						else if(this.board[Tx][Ty] == 0)
							return false;
				break;
			case 4:
				if(this.board[x][y+1] == opp)
					for(int Ty = y + 1;Ty < 6;Ty++)
						if(this.board[x][Ty] == color)
							return true;
						else if(this.board[x][Ty] == 0)
							return false;
				break;
			case 5:
				if(this.board[x-1][y+1] == opp)
					for(int Tx = x - 1,Ty = y + 1;Tx >= 0 && Ty < 6;Tx--,Ty++)
						if(this.board[Tx][Ty] == color)
							return true;
						else if(this.board[Tx][Ty] == 0)
							return false;
				break;
			case 6:
				if(this.board[x-1][y] == opp)
					for(int Tx = x - 1;Tx >= 0;Tx--)
						if(this.board[Tx][y] == color)
							return true;
						else if(this.board[Tx][y] == 0)
							return false;
				break;
			case 7:
				if(this.board[x-1][y-1] == opp)
					for(int Tx = x - 1,Ty = y - 1;Tx >= 0 && Ty >= 0;Tx--,Ty--)
						if(this.board[Tx][Ty] == color)
							return true;
						else if(this.board[Tx][Ty] == 0)
							return false;
				break;
			default:
				return false;
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{}
		
		return false;
	}
	
	/**
	 * Resets the current board.
	 * @return <b>void</b>
	 */
	public void ResetBoard()
	{
		this.board = new byte[6][6];
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				this.board[i][j] = 0;
		
		this.LastMove = 0;
		
		return;
	}
	
	/**
	 * Sets the board based on another board.
	 * @param Base
	 * The board to copy.
	 * @return <b>boolean</b>
	 * Returns true if the board could be copied and false if it could not.
	 */
	public boolean CopyBoard(MiniBoard Base)
	{
		byte[][] BTemp = Base.GetBoard();
		
		if(BTemp == null)
			return false;
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				this.board[i][j] = BTemp[i][j];
		
		this.LastMove = Base.GetLastMover();
		
		return true;
	}
	
	/**
	 * Sets the board based on another board.
	 * @param Base
	 * The board to copy.
	 * @return <b>boolean</b>
	 * Returns true if the board could be copied and false if it could not.
	 */
	public boolean CopyBoard(byte[][] Base)
	{
		if(Base == null || Base.length != 6 && Base[0].length != 6)
			return false;
		
		try
		{
			for(int i = 0;i < 6;i++)
				for(int j = 0;j < 6;j++)
					this.board[i][j] = Base[i][j];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		
		this.LastMove = 1;
		
		return true;
	}
	
	/**
	 * Sets white as the last player to move.
	 * @return <b>void</b>
	 */
	public void PingWhite()
	{
		this.LastMove = 1;
		
		return;
	}
	
	/**
	 * Sets black as the last player to move.
	 * @return <b>void</b>
	 */
	public void PingBlack()
	{
		this.LastMove = 2;
		
		return;
	}
	
	/**
	 * Gets the board data from this instance of Board.
	 * @return <b>byte[][]</b>
	 * Returns the current board.
	 */
	public byte[][] GetBoard()
	{
		return this.board;
	}
	
	/**
	 * Counts the number of empty spaces.
	 * @return <b>int</b>
	 * Returns the number of empty spaces as an integer.
	 */
	public int CountFreeSpace()
	{
		int ITemp = 0;
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(this.board[i][j] == 0)
					ITemp++;
		
		return ITemp;
	}
	
	/**
	 * Counts the number of spaces occupied by white.
	 * @return <b>int</b>
	 * Returns the number of spaces occupied by white as an integer.
	 */
	public int CountWhiteSpace()
	{
		int ITemp = 0;
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(this.board[i][j] == 1)
					ITemp++;
		
		return ITemp;
	}
	
	/**
	 * Counts the number of spaces occupied by white.
	 * @return <b>int</b>
	 * Returns the number of spaces occupied by white as an integer.
	 */
	public int CountBlackSpace()
	{
		int ITemp = 0;
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(this.board[i][j] == 2)
					ITemp++;
		
		return ITemp;
	}
	
	/**
	 * Determines the winner of the game.
	 * @return <b>int</b>
	 * Returns 1 if white won. Returns 2 if black won.
	 * Returns 3 if there was a tie. Returns 0 if no one won. 
	 */
	public int GetWinner()
	{
		if(this.CountBlackSpace() > this.CountWhiteSpace())
			return 2;
		else if(this.CountWhiteSpace() > this.CountBlackSpace())
			return 1;
		else if(this.CountWhiteSpace() == this.CountBlackSpace())
			return 3;
		
		return 0;
	}
	
	/**
	 * Returns the last person to move.
	 * @return <b>int</b>
	 * Returns a 0 if no one moved last.
	 * Returns a 1 if white moved last.
	 * Returns a 2 if black moved last.
	 */
	public int GetLastMover()
	{
		return this.LastMove;
	}
	
	/**
	 * Sets the value of the storage integer.
	 * @param i
	 * The value to store.
	 * @return <b>void</b>
	 */
	public void SetStorageValue(int i)
	{
		this.StorageValue = i;
		
		return;
	}
	
	/**
	 * Returns data.
	 * @return <b>int</b>
	 * Returns an integer that contains data pertaining to solving.
	 */
	public int GetStorageValue()
	{
		return this.StorageValue;
	}
	
	public String toString()
	{
		String object = "";
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(j < 5)
					object += (this.board[i][j] == 0 ? "Free " : (this.board[i][j] == 1 ? "White" : "Black")) + " ";
				else
					object += (this.board[i][j] == 0 ? "Free " : (this.board[i][j] == 1 ? "White" : "Black")) + "\n";
		
		object += "White Pieces: " + this.CountWhiteSpace() + "\n";
		object += "Black Pieces: " + this.CountBlackSpace() + "\n";
		object += "Free Spaces: " + this.CountFreeSpace() + "\n";
		object += "Last Move By: " + ((this.LastMove == 0) ? "No one" : ((this.LastMove == 1) ? "White" : "Black"));
		
		if(this.CountFreeSpace() == 0 || this.GetNumberOfLegalBlackMoves() == 0 && this.GetNumberOfLegalWhiteMoves() == 0)
			object += "\n" + "Winner: " + ((this.CountBlackSpace() == 0) ? "White" :
				(this.CountWhiteSpace() == 0) ? "Black" :
					(this.CountBlackSpace() > this.CountWhiteSpace() ? "Black" :
						(this.CountWhiteSpace() > this.CountBlackSpace() ? "White" : "Tie Game")));
		else
			object += "\n" + "Number of Legal White Moves: " + this.GetNumberOfLegalWhiteMoves() + "\n" + "Number of Legal Black Moves: " + this.GetNumberOfLegalBlackMoves();
		
		if(this.StorageValue != 0)
			object += "\n" + "Winning Board For: " + ((this.StorageValue == 1) ? "White" : ((this.StorageValue == 2) ? "Black" : "Tied Game"));
		
		return object;
	}
	
	/**
	 * Determines if the MiniBoard [board] already exists.
	 * @param board
	 * The board to check for existance.
	 * @param Branch
	 * The TreeDiagram to search in.
	 * @return <b>TreeDiagram*</b>
	 * Returns a pointer to the branch that contains the board.
	 * Returns null if the data was not found.
	 */
	public TreeDiagram Exists(MiniBoard board, TreeDiagram Branch)
	{
		TreeDiagram TTemp = null;
		
		if(board != Branch.GetData())
			if(((MiniBoard)Branch.GetData()).equals(board))
				return Branch;
			else
				for(int i = 0;i < Branch.GetBranches().length;i++)
					if((TTemp = this.Exists(board,Branch.GetBranches()[i])) != null)
						return TTemp;
		
		return null;
	}
	
	/**
	 * Determines if two MiniBoards are equal.
	 * @param Brd
	 * The MiniBoard to compare this board to.
	 * @return <b>boolean</b>
	 * Returns true if [Brd] is the same, a rotation, or a reflection of this board.
	 */
	public boolean equals(MiniBoard Brd)
	{
		if(this == Brd)
			return false;
		
		byte[][] BrD = Brd.GetBoard();
		byte[][] board = new byte[6][6];
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				board[i][j] = BrD[i][j];
		
		for(int i = 0;i < 4;i++)
		{
			if(i > 0)
				this.Rotate(board);
			
			if(this.equals(this.board,board))
				return true;
		
			this.Reflect(board);
			
			if(this.equals(this.board,board))
				return true;
			
			if(i < 3)
				this.Reflect(board);
		}
		
		return false;
	}
	
	/**
	 * Determines if two byte[][]s are equal.
	 * @param boardA
	 * The first board to be compared.
	 * @param boardB
	 * The second board to be compared.
	 * @return <b>boolean</b>
	 * Returns true if all elements are equal.
	 */
	protected boolean equals(byte[][] boardA, byte[][] boardB)
	{
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				if(boardA[i][j] == boardB[i][j])
				{
					if(i + j == 10)
						return true;
				}
				else
				{
					i = 5;
					j = 5;
				}
		
		return false;
	}
	
	/**
	 * Rotates a board 90 degrees counter clockwise.
	 * @param board
	 * The board to rotate.
	 * @return <b>void</b>
	 */
	protected void Rotate(byte[][] board)
	{
		byte[][] BTemp = new byte[6][6];
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				BTemp[i][j] = board[j][5-i];
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				board[i][j] = BTemp[i][j];
				
		return;
	}
	
	/**
	 * Reflects a board vertically.
	 * @param board
	 * The board to reflect.
	 * @return <b>void</b>
	 */
	protected void Reflect(byte[][] board)
	{
		byte[][] BTemp = new byte[6][6];
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				BTemp[i][j] = board[5-i][j];
		
		for(int i = 0;i < 6;i++)
			for(int j = 0;j < 6;j++)
				board[i][j] = BTemp[i][j];
		
		return;
	}
	
	/**
	 * Contains the data about current board position.
	 * A 0 indicates the space is empty.
	 * A 1 indicates the space is owned by white.
	 * A 2 indicates the space is owned by black.
	 */
	protected byte[][] board;
	
	/**
	 * Contains data representing who moved last on this board.
	 * A 0 means no one has moved.
	 * A 1 means white moved last.
	 * A 2 means black moved last.
	 */
	protected int LastMove;
	
	/**
	 * Contains data relative to solving Othello.
	 * A 0 means it is not determined yet.
	 * A 1 means it is a winning board for white.
	 * A 2 means it is a winning board for black.
	 * A 3 means it is a tied board.
	 */
	protected int StorageValue;
}
