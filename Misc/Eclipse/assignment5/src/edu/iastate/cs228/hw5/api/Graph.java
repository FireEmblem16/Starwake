package edu.iastate.cs228.hw5.api;
import java.util.Iterator;

/**
 * General interface for a graph of arbitrary objects.
 */
public interface Graph<E>
{
  /**
   * Returns a read-only iterator over the vertices adjacent to the given vertex.
   * @param vertex 
   * @return
   */
  public Iterator<Edge<E>> getNeighbors(E vertex);
  
  /**
   * Returns an estimate of the total weight of a minimum-weight path 
   * from current to goal.  The returned value must be less than or 
   * equal to the true weight of a minimum-weight path. Default implementations
   * should return zero.
   * @param current
   * @param goal
   * @return a lower bound on the total weight of a minimum weight 
   *    path from current to goal
   */
  public int h(E current, E goal);

  /**
   * Encapsulates a weighted, directed edge from a known vertex
   * to the given vertex.
   */
  public class Edge<T>
  {
    /**
     * The adjacent vertex.
     */
    public T vertex;
    
    /**
     * The weight of the edge.
     */
    public int weight; 
    
    /**
     * Constructs an Edge to the given vertex with given weight.
     * @param vertex
     * @param weight
     */
    public Edge(T vertex, int weight)
    {
      this.vertex = vertex;
      this.weight = weight;
    }
  } 
}
