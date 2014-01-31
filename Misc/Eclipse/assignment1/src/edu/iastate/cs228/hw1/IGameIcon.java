package edu.iastate.cs228.hw1;

import java.awt.Color;

/**
 * Interface specifying an icon or block in a Tetris-like
 * game.
 */
public interface IGameIcon extends Cloneable
{
  /**
   * Returns the color associated with this block.
   * @return the color associated with this block
   */
  Color getColorHint();
  
  /**
   * Determines whether this block matches another block.  In most
   * cases this means that the associated colors match.
   * @param block the block to check
   * @return true if the other block is non-null and matches
   * this block
   */
  boolean matches(IGameIcon block);
  
  /**
   * Sets the marked status of this block.
   * @param marked the marked status
   */
  void setMarked(boolean marked);
  
  /**
   * Determines whether this block is in the marked state.
   * @return the marked status of this block
   */
  boolean isMarked();
  
  /**
   * Returns a copy of this object.
   * @return a copy of this object
   */
  Object clone();
}
