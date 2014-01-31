package edu.iastate.cs228.hw5.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Subinterface of GraphAnimation with additional methods for 
 * bidirectional search algorithms.  A bidirectional search is
 * assumed to encapsulate two instances of GraphAlgorithm, one
 * that searches forward from the start to the goal vertex, and 
 * one that searches in reverse from the goal to the start.
 */
public interface BidirectionalGraphAnimation<E> extends GraphAnimation<E>
{
  /**
   * Same as GraphAnimation.getPath for the reverse search.  The
   * path originates at the goal.
   */
  public List<E> getPathReverse(E vertex);
  
  /**
   * Same as GraphAnimation.getDistance for the reverse search.
   */
  public int getDistanceReverse(E vertex);
  
  /**
   * Same as GraphAnimation.getPredecessor for the reverse search.
   * Returns the predecessor of the given vertex on a path 
   * originating at the goal.
   */  
  public E getPredecessorReverse(E vertex);
  
  /**
   * Same as GraphAnimation.closedSet for the reverse search.
   */  
  public Collection<E> closedSetReverse();
  
  /**
   * Same as GraphAnimation.openSet for the reverse search.
   */  
  public Iterator<E> openSetReverse();
  
  /**
   * Returns the complete path from the start to the goal, or
   * an empty list if a path has not been found.
   * @return complete path from the start to the goal
   */
  public List<E> getCompletePath();
  
  /**
   * Returns the total weight of the complete path from the start
   * to the goal, or -1 if a path has not yet been found.
   * @return total weight path from start to goal
   */
  public int getCompleteDistance();
}
