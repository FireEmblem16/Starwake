package edu.iastate.cs228.hw1;

/**
 * Interface for polyominoes used by Tetris-like games.  Each polyomino
 * has a position and a set of Cells. The position is an (x,y) pair representing
 * the upper left corner of the bounding square. The position is represented
 * by an instance of java.awt.Point.  The initial position is presumed to be 
 * given by a constructor; thereafter it can be modified by the shiftX()
 * and shiftY() methods.  Note that no bounds checking is done in 
 * implementations of this interface; therefore, the position can have 
 * negative coordinates.  
 */
public interface IPolyomino extends Cloneable
{
  /**
   * Returns an array of Cell objects representing the cells 
   * occupied by this polyomino with their absolute positions.
   * @return the cells occupied by this polyomino
   */
  Cell[] getCells();
  
  /**
   * Shifts the position of this polyomino down (increasing the y-coordinate) 
   * by one cell.  No bounds checking is done.
   * 
   * @throws IllegalStateException
   *   if this object is in the frozen state
   */
  void shiftDown();
  
  /**
   * Shifts the position of this polyomino left (decreasing the x-coordinate) 
   * by one cell.  No bounds checking is done.
   */
  void shiftLeft();
  
  /**
   * Shifts the position of this polyomino right (increasing the x-coordinate) 
   * by one cell.  No bounds checking is done. 
   */  
  void shiftRight();
  
  /**
   * Transforms this polyomino without altering its position
   * according to the rules of the game to be implemented.  
   * Typical operations are rotation and reflection within
   * the bounding square. No bounds checking is done.
   */    
  void transform();
  
  /**
   * Cycles the blocks forward within the occupied cells of this
   * polyomino, based on the top-to-bottom ordering of the 
   * cells in the original position.  The last block should
   * wrap around to the first cell.  (This method has no 
   * apparent effect when all blocks are the same color.)
   */
  void cycle();
  
  /**
   * Returns a deep copy of this object.
   * @return a deep copy of this object.
   */
  Object clone();
}
