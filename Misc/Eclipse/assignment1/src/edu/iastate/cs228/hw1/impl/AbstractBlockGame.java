package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import edu.iastate.cs228.hw1.IGameIcon;
import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.GameStatus;
import edu.iastate.cs228.hw1.IGame;
import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;

/**
 * A partial implementation of the IGame interface for 
 * Tetris-like falling block games. Subclasses must implement
 * the determineCellsToCollapse() and determineScore() methods.
 */
public abstract class AbstractBlockGame implements IGame
{
  /**
   * Possible list of colors for the Blocks.
   */
  public static final Color[] COLORS =
  {
    Color.CYAN, 
    Color.BLUE, 
    Color.ORANGE, 
    Color.YELLOW, 
    Color.GREEN, 
    Color.MAGENTA, 
    Color.RED
  };
  
  /**
   * Width of the game grid.
   */
  protected static final int WIDTH = 12;

  /**
   * Height of the game grid.
   */
  protected static final int HEIGHT = 24;

  /**
   * The polyomino that is subject to motion during the step() method
   * or via invocations of the shiftX() or rotate() methods.
   */
  protected IPolyomino current;

  /**
   * A WIDTH x HEIGHT grid of cells that may be occupied by either
   * the current polyomino or by frozen polyominoes (that can no longer
   * be moved).  Unoccupied cells are null.
   */
  protected IGameIcon[][] grid;

  /**
   * Status of the game after each invocation of step(), as described
   * in the GameStatus documentation.
   */
  private GameStatus gameStatus;

  /**
   * Generator for new polyominoes.  The BasicGenerator implementation
   * will uniformly select one of the seven tetromino types.
   */
  private IPolyominoGenerator generator;

  /**
   * State variable indicating which blocks to be deleted when the
   * status is COLLAPSING.  The implementation maintains the invariant that
   * cellsToCollapse.size() is nonzero if and only if gameStatus is COLLAPSING.
   */
  private List<Point> cellsToCollapse;

  /**
   * Constructs a new AbstractBlockGame.
   */
  protected AbstractBlockGame(IPolyominoGenerator generator)
  {
    grid = new IGameIcon[getHeight()][getWidth()];
    this.generator = generator;
    current = generator.getNext();
    gameStatus = GameStatus.NEW_POLYOMINO;
  }

  /**
   * Returns a list of locations for all cells that form part of
   * a collapsible group.  This list may contain duplicates.
   * @return list of locations for cells to be collapsed
   */
  protected abstract List<Point> determineCellsToCollapse();
  
  /**
   * Returns the current score.
   * @return the current score
   */
  protected abstract int determineScore();

  @Override
  public int getHeight()
  {
    return HEIGHT;
  }

  @Override
  public IGameIcon getCell(int row, int col)
  {
    return grid[row][col];
  }

  @Override
  public IPolyomino getCurrent()
  {
    if (gameStatus == GameStatus.COLLAPSING || gameStatus == GameStatus.GAME_OVER)
    {
      throw new IllegalStateException();
    }
    return current;
  }

  @Override
  public int getWidth()
  {
    return WIDTH;
  }

  @Override
  public Point[] getCellsToCollapse()
  {
    if (cellsToCollapse.size() == 0)
    {
      throw new IllegalStateException();
    }
    return cellsToCollapse.toArray(new Point[cellsToCollapse.size()]);
  }

  @Override
  public boolean transform()
  {
	current.transform();
	
	boolean ret = collides(current);
	
	if(ret)
		current.transform();
	  
    return !ret;
  }

  @Override
  public boolean shiftLeft()
  {
	current.shiftLeft();
	
	boolean ret = collides(current);
	
	if(ret)
		current.shiftRight();
	
	return !ret;
  }

  @Override
  public boolean shiftRight()
  {
	current.shiftRight();
	
	boolean ret = collides(current);
	
	if(ret)
		current.shiftLeft();
	
	return !ret;
  }

  @Override
  public int getScore()
  {
    return determineScore();
  }

  @Override
  public void cycle()
  {
    current.cycle();
  }
  

  @Override
  public boolean gameOver()
  {
    return gameStatus == GameStatus.GAME_OVER;
  }

  @Override
  public GameStatus step()
  {
    switch (gameStatus)
    {
      case GAME_OVER:
        // do nothing
        break;
      case NEW_POLYOMINO:
      case FALLING:
        if (gameStatus == GameStatus.NEW_POLYOMINO)
        {
          gameStatus = GameStatus.FALLING;
        }
        if (canShiftDown())
        {
          current.shiftDown();
        }
        else
        {
          // Add blocks of the current polyomino to the grid, maybe 
          // temporarily, in order to check whether it has completed
          // a collapsible group
          for (Cell c : current.getCells())
          {
            Point p = c.getPosition();
            if (p.y >= 0 && p.y < HEIGHT && p.x >= 0 && p.x < WIDTH)
            {
              grid[p.y][p.x] = c.getIcon();
            }
          }
          cellsToCollapse = determineCellsToCollapse();
          if (cellsToCollapse.size() != 0)
          {
            // current polyomino completes a collapsible group,
            // so prepare to collapse
            gameStatus = GameStatus.COLLAPSING;
          }
          else
          {
            // current polyomino is stopped, but has not completed a
            // collapsible group, so it might be moved sideways; 
            // take its blocks back out of the grid
            for (Cell c : current.getCells())
            {
              Point p = c.getPosition();
              if (p.y >= 0 && p.y < HEIGHT && p.x >= 0 && p.x < WIDTH)
              {
                grid[p.y][p.x] = null;
              }
            }
            gameStatus = GameStatus.STOPPED;
          }
        }
        break;
      case STOPPED:
        // If the polyomino was previously stopped, it still may be possible
        // to shift it downwards since it could have been moved to the side
        // during the last step
        if (canShiftDown())
        {
          current.shiftDown();
          gameStatus = GameStatus.FALLING;
        }
        else
        {
          // we only get in the stopped state when the polyomino doesn't complete
          // a collapsible group; start a new polyomino at the top
          for (Cell c : current.getCells())
          {
            Point p = c.getPosition();
            if (p.y >= 0 && p.y < HEIGHT && p.x >= 0 && p.x < WIDTH)
            {
              grid[p.y][p.x] = c.getIcon();
            }
          }
          current = generator.getNext();
          if (collides(current))
          {
            gameStatus = GameStatus.GAME_OVER;
          }
          else
          {
            gameStatus = GameStatus.NEW_POLYOMINO;
          }
        }
        break;
      case COLLAPSING:
        collapseCells(cellsToCollapse); 
        cellsToCollapse = determineCellsToCollapse();       
        if (cellsToCollapse.size() == 0)
        {
          // done collapsing, try to start a new polyomino
          current = generator.getNext();
          if (collides(current))
          {
            gameStatus = GameStatus.GAME_OVER;
          }
          else
          {
            gameStatus = GameStatus.NEW_POLYOMINO;
          }
        }
        break;
    }
    return gameStatus;
  }

  /**
   * Returns a clone of the current polyomino.
   * @return a clone of the current polyomino
   */
  private IPolyomino getCurrentCloned()
  {
    return (IPolyomino)getCurrent().clone();
  }

  /**
   * Determines whether the current polyomino can be shifted down. Does not
   * modify the game state.
   * @return true if the current polyomino can be shifted down, false otherwise
   */
  private boolean canShiftDown()
  {
    IPolyomino t = (IPolyomino) current.clone();
    t.shiftDown();
    return !collides(t);
  }

  /**
   * Determines whether the given polyomino overlaps
   * with the occupied cells of the grid, or extends beyond the sides
   * or bottom of the grid.  (A polyomino in its initial position
   * MAY extend above the grid.)
   *
   * @param t a polyomino
   * @return true if the cells of the given polyomino extend beyond the
   *   sides or bottom of the grid or overlap with any occupied cells of
   *   the grid
   */
  private boolean collides(IPolyomino t)
  {
    for (Cell c : t.getCells())
    {
      Point p = c.getPosition();
      if (p.x < 0 || p.x > WIDTH - 1 || p.y > HEIGHT - 1)
      {
        return true;
      }

      // row, column
      if (p.y >= 0 && grid[p.y][p.x] != null)
      {
        return true;
      }
    }
    return false;
  }


  /**
   * Delete the blocks at the indicated positions and shift
   * blocks above them down.  Only blocks lying within a column
   * above a deleted block are shifted down.
   * @param cellsToCollapse list of locations of cells to 
   * collapse.  The list may contain duplicates.
   */
  private void collapseCells(List<Point> cellsToCollapse)
  {
    for (Point p : cellsToCollapse)
    {
      grid[p.y][p.x].setMarked(true);
    }
    for (int col = 0; col < WIDTH; ++col)
    {
      int start = HEIGHT - 1;
      boolean done = false;
      while (!done)
      {
        // go up the column and find the first marked block
        while (start > 0 && (grid[start][col] == null || !grid[start][col].isMarked()))
        {
          --start;
        }
        if (grid[start][col] != null && grid[start][col].isMarked())
        {
          // go past all the marked cells, setting them null
          int j = start;
          while (j >= 0 && grid[j][col] != null && grid[j][col].isMarked()) 
          {
            //System.out.println("nulling " + j + ", " + col);
            grid[j][col] = null;
            --j;
          }
          if (j >= 0)
          {
            // found something non-marked, so
            // shift down everything at j and above
            int shift = start - j;
            for (int k = j; k >= 0; --k)
            {
              grid[k + shift][col] = grid[k][col];
            }
          }
          else 
          {
            done = true;
          }

        }
        else 
        {
          // no marked cells
          done = true;
        }

      }
    }
  }

}
