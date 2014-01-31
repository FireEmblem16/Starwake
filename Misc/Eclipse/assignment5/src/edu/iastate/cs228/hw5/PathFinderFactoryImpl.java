
package edu.iastate.cs228.hw5;

import edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.ListGraph;
import edu.iastate.cs228.hw5.api.PathFinderFactory;

/**
 * For the searching algorithms I created two abstract classes from which
 * I created two child classes. BFS and DFS inherited from SimpleFinder
 * and A* and Dijkstra inherited from Complex Finder. I used the given
 * code for BFS and pretty much just copy and pasted it to DFS. The type of
 * data structure was changed to Stack though so that I could emulate the
 * recursive nature of DFS. Dijkstra and A* both utilize a priority queue to
 * order the nodes for processing so that the "shortest" path is traverst
 * first. I use quotes because A* adds the distance to the goal to the path
 * length to travel towards the goal. This makes it greedy but that's just
 * what A* does so whatever. To implement a bidirectional search I created
 * a shell class which has two search algorithms inside it of the same type.
 * To get a reverse search I merely switched the start and goal vertex passed
 * to the algorithm constructors. The algorithm is done when there exists a
 * single node that exists in both the closed set of both the normal and
 * the reverse graph search. The method I used to retrieve the path for
 * the bidirectional searcher is to add the path for the normal search then to
 * add the path in reverse from the reverse search. Of course I omitted the
 * repeat vertex in the middle. 
 * 
 * Most of the code was copy and pasted because it made it easy and fast to
 * create the code and the alogrithms used in each are nearly identical. The
 * data structures used in each optimizes the speed of the algorithm. Space
 * is of concern in some of the algorithms however I usually will choose speed
 * over space. The only time I won't is when space is of VITAL importance, with
 * this project not having space being important. An OS is a good example of
 * when space is very important but they tend not to take up to much memory
 * until they get big anyways so it's not of much concern.
 */
public class PathFinderFactoryImpl implements PathFinderFactory
{
  @Override
  public <E> GraphAnimation<E> createAStarFinder(Graph<E> graph, E start, E goal)
  {
    return new AStarFinder(graph, start, goal);
  }

  @Override
  public <E> GraphAnimation<E> createBFSFinder(Graph<E> graph, E start, E goal)
  {
    return new BFSFinder<E>(graph, start, goal);
  }

  @Override
  public <E> GraphAnimation<E> createDFSFinder(Graph<E> graph, E start, E goal)
  {
    return new DFSFinder<E>(graph, start, goal);
  }

  @Override
  public <E> GraphAnimation<E> createDijkstraFinder(Graph<E> graph, E start,
      E goal)
  {
    return new DijkstraFinder(graph, start, goal);
  }

  @Override
  public <E> BidirectionalGraphAnimation<E> createBidirectionalAStarFinder(
      Graph<E> graph, E start, E goal)
  {
    return new BiFinder(graph, start, goal, AStarFinder.class);
  }

  @Override
  public <E> BidirectionalGraphAnimation<E> createBidirectionalBFSFinder(
      Graph<E> graph, E start, E goal)
  {
    return new BiFinder(graph, start, goal, BFSFinder.class);
  }

  @Override
  public <E> BidirectionalGraphAnimation<E> createBidirectionalDFSFinder(
      Graph<E> graph, E start, E goal)
  {
	  return new BiFinder(graph, start, goal, DFSFinder.class);
  }

  @Override
  public <E> BidirectionalGraphAnimation<E> createBidirectionalDijkstraFinder(
      Graph<E> graph, E start, E goal)
  {
	  return new BiFinder(graph, start, goal, DijkstraFinder.class);
  }
}