package edu.iastate.cs228.hw5.api;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Interface for traversal and path-finding algorithms to be
 * displayed by animated graph visualization tools.  The algorithm
 * is assumed to be of the following general form
 * 
 * <code>
 * add start vertex to open set
 * while open set is not empty
 *     remove a vertex u from open set
 *     add u to closed set
 *     if u is the goal vertex
 *         done = true
 *         break
 *     for each neighbor v of u
 *         if v is not in open set or closed set
 *             add v to open set
 *             record u as the predecessor of v
 * done = true
 * </code>
 * 
 * One "step" (for the purposes of the step() method below) consists 
 * essentially of one execution of the while loop body.  The general
 * usage of a GraphAnimation instance thus has the form:
 * <code>
 * while not done()
 *     step()
 * </code>
 * 
 */
public interface GraphAnimation<E>
{
  /**
   * Performs one step of the algorithm and returns the node added to
   * the closed set.  The returned value may be null if no vertex is 
   * added to the closed set (some implementations of Dijkstra's
   * algorithm allow this).  The returned value is always null when
   * the algorithm is finished.
   * @return the node added to the closed set, possibly null.
   */
  public E step();
  
  /**
   * Determines whether the algorithm is finished.
   * @return true if the algorithm is finished, false otherwise
   */
  public boolean done();
  
  /**
   * Returns the path from the start vertex to the given vertex 
   * in the search tree.  Returns an 
   * empty list if the given vertex has not been reached.
   * @param goal the destination vertex
   * @return List of vertices representing a path from the start 
   *   vertex to the given vertex, or an empty list if the vertex 
   *   has not been reached 
   */
  public List<E> getPath(E vertex);

  /**
   * Returns the total weight of a path from the start vertex to the 
   * given vertex in the search tree.  
   * Returns -1 if the given vertex not been reached.  Not all
   * implementations are required to maintain a distance map; such 
   * implementations should return 0 for reachable vertices.
   * @param vertex the destination vertex
   * @return weight total weight of the path
   */
   public int getDistance(E vertex);
  
  /**
   * Returns the predecessor of the given vertex in the search tree.
   * Returns null if the given vertex is the start vertex or if the
   * given vertex is not in the open set nor in the closed set.
   * @param vertex 
   * @return predecessor of the given vertex, or null
   */
  public E getPredecessor(E vertex);
  
  /**
   * Returns an unmodifiable Collection view of the elements of 
   * the closed set.
   * @return a Collection view of the closed set.
   */
  public Collection<E> closedSet();
  
  /**
   * Returns an iterator over the elements of the open set.
   * It is permissible for vertices to appear more than once.
   * In general the remove() operation is not supported.
   * @return an iterator over the elements of the open set.
   */
  public Iterator<E> openSet();

}
