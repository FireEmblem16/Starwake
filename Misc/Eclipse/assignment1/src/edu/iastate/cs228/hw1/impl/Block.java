package edu.iastate.cs228.hw1.impl;

import java.awt.Color;

import edu.iastate.cs228.hw1.IGameIcon;

/**
 * Simple implementation an IGameIcon as a colored block.
 * Two blocks match if they have the same color.
 */
public class Block implements IGameIcon
{
  /**
   * The color associated with this block.
   */
  private final Color color;
  
  /**
   * The marked status of this block.
   */
  private boolean marked;
  
  /**
   * Constructs a Block with the given color in the unmarked state.
   * @param c color to be associated with this block
   */
  public Block(Color c)
  {
    this.color = c;
  }

  @Override
  public Color getColorHint()
  {
    return color;
  }

  @Override
  public boolean isMarked()
  {
    return marked;
  }

  @Override
  public boolean matches(IGameIcon block)
  {
    return (block != null && block.getColorHint() == this.color);
  }

  @Override
  public void setMarked(boolean marked)
  {
    this.marked = marked;
  }
  
  @Override
  public Object clone()
  {
    try
    {
      return (Block) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      return null;
    }
  }
}
