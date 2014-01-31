package edu.iastate.cs228.hw5;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * Has common code for BFS and DFS searches.
 * @author Donald Nye
 */
public abstract class SimpleFinder<E> implements GraphAnimation<E>
{
	/**
	 * Constructs a the basic shell for a SimpleFinder.
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 */
	public SimpleFinder(Graph<E> graph, E start, E goal)
	{
		this.graph = graph;
		this.start = start;
		this.goal = goal;
		
		predMap.put(start,null);
		return;
	}

	@Override public Collection<E> closedSet()
	{
		return Collections.unmodifiableCollection(closedSet);
	}

	@Override public boolean done()
	{
		return done;
	}

	@Override public List<E> getPath(E vertex)
	{
		E current = vertex;
		
		List<E> path = new LinkedList<E>();
		path.add(current);
		
		E pred = null;
		
		while((pred = predMap.get(current)) != null)
		{
			path.add(0,pred);
			current = pred;
		}
		
		if(start.equals(path.get(0)))
		{
			return path;
		}
		else
		{
			// vertex is not reachable, return empty list
			return new LinkedList<E>();
		}
	}

	@Override public E getPredecessor(E vertex)
	{
		return predMap.get(vertex);
	}

	@Override public Iterator<E> openSet()
	{
		// Wrap the open set in a read-only view
		return Collections.unmodifiableCollection(openSet).iterator();
	}

	/**
	 * Since the algorithm does not maintain a distance map, this method always
	 * returns 0 for reachable vertices.
	 */
	@Override public int getDistance(E vertex)
	{
		List<E> path = getPath(vertex);
		
		if(path.size() == 0)
		{
			return -1;
		}
		
		return 0;
	}

	/**
	 * The graph to be traversed.
	 */
	protected Graph<E> graph;

	/**
	 * The start node vertex.
	 */
	protected E start;

	/**
	 * The goal vertex (may be null).
	 */
	protected E goal;

	/**
	 * The open set.
	 */
	protected Collection<E> openSet;

	/**
	 * Predecessors of nodes discovered in the search. Each node in the open set
	 * or closed set has an entry in this map. (The predecessor of the start
	 * node is null.)
	 */
	protected Map<E,E> predMap = new HashMap<E,E>();

	/**
	 * Vertices in the closed set.
	 */
	protected Set<E> closedSet = new HashSet<E>();

	/**
	 * Flag indicating that the algorithm has terminated, either by finding a
	 * goal node or by visiting all nodes reachable from the start.
	 */
	protected boolean done = false;
}