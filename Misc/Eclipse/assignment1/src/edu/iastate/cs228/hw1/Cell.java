package edu.iastate.cs228.hw1;

import java.awt.Point;

/**
 * Container for an IGameIcon and a location.
 */
public class Cell
{
  /**
   * The IGameIcon represented by this Cell.
   */
  private IGameIcon icon;
  
  /**
   * The location of this Cell.
   */
  private Point position;
  
  /**
   * Constructs a Cell from the given icon and position. 
   * Note this constructor does not clone the arguments.
   * @param icon
   * @param position
   */
  public Cell(IGameIcon icon, Point position)
  {
    this.icon = icon;
    this.position = position;
  }
  
  /**
   * Copy constructor creates a deep copy of the given Cell.
   * @param existing the given Cell
   */
  public Cell(Cell existing)
  {
    this.icon = (IGameIcon) existing.getIcon().clone();
    this.position = new Point(existing.position);
  }
  
  /**
   * Returns the location of this Cell.
   * @return the location of this Cell
   */
  public Point getPosition()
  {
    return position;
  }
  
  /**
   * Returns the icon associated with this Cell.
   * @return the icon associated with this Cell
   */
  public IGameIcon getIcon()
  {
    return icon;
  }
  
  /**
   * Sets the icon associated with this Cell.
   * @param b the new icon
   */
  public void setIcon(IGameIcon b)
  {
    icon = b;
  }
}
