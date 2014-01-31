package edu.iastate.cs228.hw5.api;

/**
 * Abstract factory for creation of GraphAnimation instances.  An 
 * implementation of GraphAnimation can be used to perform a single
 * traversal or search of a given graph.  Between steps of the search,
 * and after the algorithm has finished, the object can be queried 
 * regarding paths and distances to vertices.  Here is an example 
 * of running a shortest-path algorithm for nodes in a graph
 * of Foo objects, given a particular starting vertex:
 * 
 * <code>
 *    PathFinderFactory factory = ... // obtain an instance of a factory
 *    Graph<Foo> g = ...              // create the graph
 *    Foo start = ...                 // choose starting point
 *    GraphAnimation<Foo> finder = factory.createDijkstraFinder(g, start, null);
 *    while (!finder.done())
 *    {
 *      Foo vertex = finder.step();
 *      System.out.println(vertex.toString() + " added to closed set");
 *    }
 *    Foo v = ...                     // a vertex to query about
 *    List<Foo> path = finder.getPath(v);
 *    System.out.println(v.toString() + " path: " + path.toString());
 * </code>
 */
public interface PathFinderFactory
{
  /**
   * Creates a GraphAnimation instance that performs a breadth-first
   * traversal of the given graph, beginning at the start node.  The
   * start node must be non-null.  The algorithm terminates when the
   * goal is added to the closed set; if goal is null or is not reachable
   * from start, the algorithm terminates when all nodes reachable from start
   * have been added to the closed set.  
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node, or null
   * @return GraphAnimation instance for breadth-first traversal
   * @throws NullPointerException if graph or start vertex is null
   */
  <E> GraphAnimation<E> createBFSFinder(Graph<E> graph, E start, E goal);
  
  /**
   * Creates a GraphAnimation instance that performs a depth-first
   * traversal of the given graph, beginning at the start node.  The
   * start node must be non-null.  The algorithm terminates when the
   * goal is added to the closed set; if goal is null or is not reachable
   * from start, the algorithm terminates when all nodes reachable from start
   * have been added to the closed set.
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node, or null
   * @return GraphAnimation instance for depth-first traversal
   * @throws NullPointerException if graph or start vertex is null
   */  
  <E> GraphAnimation<E> createDFSFinder(Graph<E> graph, E start, E goal);
  
  /**
   * Creates a GraphAnimation instance that uses Dijkstra's algorithm
   * to find the minimum-weight path to one or more nodes within the
   * the given graph, beginning at the start node.  The
   * start node must be non-null.  The algorithm terminates when the
   * goal is added to the closed set; if goal is null or is not reachable
   * from start, the algorithm terminates when all nodes reachable from start
   * have been added to the closed set.
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node, or null
   * @return GraphAnimation instance for search using Dijkstra's algorithm
   * @throws NullPointerException if graph or start vertex is null
   */  
  <E> GraphAnimation<E> createDijkstraFinder(Graph<E> graph, E start, E goal);
  
  /**
   * Creates a GraphAnimation instance that uses an A* algorithm
   * to find the minimum-weight path from start to goal.  The
   * start node must be non-null.  The algorithm terminates when the
   * goal is added to the closed set; if goal is null or is not reachable
   * from start, the algorithm terminates when all nodes reachable from start
   * have been added to the closed set.
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node, or null
   * @return GraphAnimation instance for search using A* algorithm
   * @throws NullPointerException if graph or start vertex is null
   */  
  <E> GraphAnimation<E> createAStarFinder(Graph<E> graph, E start, E goal);
  
  /**
   * Creates a BidirectionalGraphAnimation instance that performs two breadth-first
   * traversals of the given graph, beginning at the start node and at the goal node.
   * The start and goal must be non-null.  The algorithm normally terminates when the 
   * two closed sets intersect.  
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node
   * @return BidirectionalGraphAnimation instance for breadth-first traversal
   * @throws NullPointerException if graph, start, or goal is null
   */
  <E> BidirectionalGraphAnimation<E> createBidirectionalBFSFinder(Graph<E> graph, E start, E goal);

  /**
   * Creates a BidirectionalGraphAnimation instance that performs two depth-first
   * traversals of the given graph, beginning at the start node and at the goal node.
   * The start and goal must be non-null.  The algorithm normally terminates when the 
   * two closed sets intersect.
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node
   * @return BidirectionalGraphAnimation instance for depth-first traversal
   * @throws NullPointerException if graph, start, or goal is null
   */
  <E> BidirectionalGraphAnimation<E> createBidirectionalDFSFinder(Graph<E> graph, E start, E goal);

  /**
   * Creates a BidirectionalGraphAnimation instance that uses Dikjstra's algorithm
   * at both the start node and the goal node to find a path from start to goal. 
   * The start and goal must be non-null.  The algorithm may terminate when the 
   * two closed sets intersect. (However, the resulting path is not guaranteed to be
   * a minimum-weight path unless additional termination conditions are added.)
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node
   * @return BidirectionalGraphAnimation instance for Dijkstra's algorithm
   * @throws NullPointerException if graph, start, or goal is null
   */
  <E> BidirectionalGraphAnimation<E> createBidirectionalDijkstraFinder(Graph<E> graph, E start, E goal);

  /**
   * Creates a BidirectionalGraphAnimation instance that uses the A* algorithm
   * at both the start node and the goal node to find a path from start to goal. 
   * The start and goal must be non-null.  The algorithm may terminate when the 
   * two closed sets intersect. (However, the resulting path is not guaranteed to be
   * a minimum-weight path without additional constraints on the heuristic function
   * used by the graph and additional termination conditions.)
   * @param graph the graph to traverse
   * @param start the start node
   * @param goal the goal node
   * @return BidirectionalGraphAnimation instance for the A* algorithm
   * @throws NullPointerException if graph, start, or goal is null
   */
  <E> BidirectionalGraphAnimation<E> createBidirectionalAStarFinder(Graph<E> graph, E start, E goal);

}
