
package edu.iastate.cs228.hw5.api;

import java.util.Iterator;
import java.util.NoSuchElementException;

public interface ListGraph<E> extends Graph<E>
{
  /**
   * Adds a vertex to this graph. Returns false if there is already
   * a vertex in the graph that is equal to the given vertex (in the
   * sense of Object.equals()).
   * @param vertex vertex to be added
   * @return false if vertex is already present, true otherwise
   **/
  public abstract boolean addVertex(E vertex);

  /**
   * Adds a directed edge from u to v; if an edge from u to v already exists,
   * this edge should replace the existing one if it has different
   * weight.  Returns true if the any modification is made.
   * @param u source vertex of the edge
   * @param v destination vertex of the edge
   * @return true if the graph is modified, false otherwise
   * @throws NoSuchElementException if one of the vertices not already
   *     present in the graph
   **/
  public abstract boolean addEdge(E u, E v, int weight);

  /**
   * Returns an iterator over the vertex set.  The iterator is
   * not required to support the remove() operation.
   * @return an iterator over the vertex set
   **/
  public abstract Iterator<E> vertices();
}
