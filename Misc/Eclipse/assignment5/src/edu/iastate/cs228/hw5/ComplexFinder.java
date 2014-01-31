package edu.iastate.cs228.hw5;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * Has common code for DijkstraFinder and AStarFinder.
 * @author Donald Nye
 */
public abstract class ComplexFinder<E> implements GraphAnimation<E>
{
	/**
	 * Constructs a basic shell for a ComplexFinder.
	 * nodes. The goal may be null.
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 */
	public ComplexFinder(Graph<E> graph, E start, E goal)
	{
		this.graph = graph;
		this.start = start;
		this.goal = goal;
		
		predMap.put(start, new Edge<E>(null, 0));
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
		
		while((pred = predMap.get(current).vertex) != null)
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
		return predMap.get(vertex).vertex;
	}

	@Override public Iterator<E> openSet()
	{
		// Wrap the open set in a read-only view
		return new VertexIterator(Collections.unmodifiableCollection(openSet));
	}

	@Override public int getDistance(E vertex)
	{
		return predMap.get(vertex).weight;
	}
	
	/**
	 * Creates a shell of an interator to fetch data from within
	 * an iterator to iterate over.
	 */
	private class VertexIterator<E> implements Iterator<E>
	{
		/**
		 * Create this shell class.
		 * @param c the collection to iterate over.
		 */
		private VertexIterator(Collection c)
		{
			iter = c.iterator();
			return;
		}
		
		@Override public boolean hasNext()
		{
			return iter.hasNext();
		}

		@Override public E next()
		{
			if(!hasNext())
				throw new NoSuchElementException();
			
			return iter.next().vertex;
		}

		@Override public void remove()
		{
			throw new UnsupportedOperationException();
		}
		
		private Iterator<Edge<E>> iter;
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
	protected PriorityQueue<Edge<E>> openSet;

	/**
	 * Predecessors of nodes discovered in the search. Each node in the open set
	 * or closed set has an entry in this map. (The predecessor of the start
	 * node is null.)
	 */
	protected Map<E,Edge<E>> predMap = new HashMap<E,Edge<E>>();

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