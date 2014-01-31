///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Omacron Storm /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// othello Package ////////////////////////////////////
/////////////////////////////////////// Board /////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////// Contains the structure for an Othello Board. //////////////////// 
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
package othello;

/**
 * Contains the data for an Othello Board.
 */
public class Board
{
	/**
	 * Creates a new Othello board.
	 * @return <b>Board*</b>
	 * Returns a pointer to a new Board.
	 */
	public Board()
	{
		this.board = new byte[8][8];
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
				this.board[i][j] = 0;
		
		return;
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
		if(x > 8 || x < 1 || y > 8 || y < 1)
			return false;
		
		if(this.board[x-1][y-1] != 0)
			return false;
		
		this.board[x-1][y-1] = 1;
		
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
		if(x > 8 || x < 1 || y > 8 || y < 1)
			return false;
		
		if(this.board[x-1][y-1] != 0)
			return false;
		
		this.board[x-1][y-1] = 2;
		
		return true;
	}
	
	/**
	 * Returns the (move)th legal move for white.
	 * @param move
	 * The offset into possible moves for white. Offsets are constant.
	 * @return <b>int[]</b>
	 * Returns (0,0) if there is no legal move or the move exceeded the number possible.
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
	 * Returns (0,0) if there is no legal move or the move exceeded the number possible.
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
	 * Returns (0,0) if there is no legal move or the move exceeded the number possible.
	 * Else returns the location on the board for the (move)th legal move for color [color].
	 */
	public int[] GetLegalMove(int move, int color)
	{
		int[] space = {0,0};
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
				if(this.IsLegalMove(i+1,j+1,color))
					if(move == 1)
					{
						space[0] = i + 1;
						space[1] = j + 1;
						
						i = 8;
						j = 8;
					}
					else
						move--;
		
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
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
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
								return false;
					break;
				case 1:
					if(this.board[x+1][y-1] == opp)
						for(int Tx = x + 1,Ty = y - 1;Tx < 8 && Ty >= 0;Tx++,Ty--)
							if(this.board[Tx][Ty] == color)
								return true;
							else if(this.board[Tx][Ty] == 0)
								return false;
					break;
				case 2:
					if(this.board[x+1][y] == opp)
						for(int Tx = x + 1;Tx < 8;Tx++)
							if(this.board[Tx][y] == color)
								return true;
							else if(this.board[Tx][y] == 0)
								return false;
					break;
				case 3:
					if(this.board[x+1][y+1] == opp)
						for(int Tx = x + 1,Ty = y + 1;Tx < 8 && Ty < 8;Tx++,Ty++)
							if(this.board[Tx][Ty] == color)
								return true;
							else if(this.board[Tx][Ty] == 0)
								return false;
					break;
				case 4:
					if(this.board[x][y+1] == opp)
						for(int Ty = y + 1;Ty < 8;Ty++)
							if(this.board[x][Ty] == color)
								return true;
							else if(this.board[x][Ty] == 0)
								return false;
					break;
				case 5:
					if(this.board[x-1][y+1] == opp)
						for(int Tx = x - 1,Ty = y + 1;Tx >= 0 && Ty < 8;Tx--,Ty++)
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
		this.board = new byte[8][8];
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
				this.board[i][j] = 0;
		
		return;
	}
	
	/**
	 * Sets the board based on another board.
	 * @param Base
	 * The board to copy.
	 * @return <b>boolean</b>
	 * Returns true if the board could be copied and false if it could not.
	 */
	public boolean CopyBoard(Board Base)
	{
		byte[][] BTemp = Base.GetBoard();
		
		if(BTemp == null)
			return false;
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
				this.board[i][j] = BTemp[i][j];
		
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
		if(Base == null || Base.length != 8)
			return false;
			
		try
		{
			for(int i = 0;i < 8;i++)
				for(int j = 0;j < 8;j++)
					this.board[i][j] = Base[i][j];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		
		return true;
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
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
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
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
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
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
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
	
	public String toString()
	{
		String object = "";
		
		for(int i = 0;i < 8;i++)
			for(int j = 0;j < 8;j++)
				if(j < 5)
					object += (this.board[i][j] == 0 ? "Free " : (this.board[i][j] == 1 ? "White" : "Black")) + " ";
				else
					object += (this.board[i][j] == 0 ? "Free " : (this.board[i][j] == 1 ? "White" : "Black")) + "\n";
		
		object += "White Pieces: " + this.CountWhiteSpace() + "\n";
		object += "Black Pieces: " + this.CountBlackSpace() + "\n";
		object += "Free Spaces: " + this.CountFreeSpace();
		
		if(this.CountFreeSpace() == 0 || this.CountBlackSpace() == 0 || this.CountWhiteSpace() == 0)
			object += "\n" + "Winner: " + ((this.CountBlackSpace() == 0) ? "White" :
				(this.CountWhiteSpace() == 0) ? "Black" :
					(this.CountBlackSpace() > this.CountWhiteSpace() ? "Black" :
						(this.CountWhiteSpace() > this.CountBlackSpace() ? "White" : "Tie Game")));
		else
			object += "\n" + "Number of Legal White Moves: " + this.GetNumberOfLegalWhiteMoves() + "\n" + "Number of Legal Black Moves: " + this.GetNumberOfLegalBlackMoves();
		
		return object;
	}
	
	/**
	 * Contains the data about current board position.
	 * A 0 indicates the space is empty.
	 * A 1 indicates the space is owned by white.
	 * A 2 indicates the space is owned by black.
	 */
	protected byte[][] board;
}
