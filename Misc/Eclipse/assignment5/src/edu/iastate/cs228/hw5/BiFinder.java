package edu.iastate.cs228.hw5;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import edu.iastate.cs228.hw5.api.BidirectionalGraphAnimation;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.ListGraph;

/**
 * Perfoms a search algorithm of a specified type from both the
 * start to the goal and from the goal to the start until they
 * meet at which point the search is considered completed.
 * @author Donald Nye
 */
public class BiFinder<E> implements BidirectionalGraphAnimation<E>
{
	/**
	 * Creates a new instance of this object.
	 * @param graph the graph to search in
	 * @param start point A to connect to point B
	 * @param goal point B to connect to point A
	 * @param type the search algorithm to use.
	 */
	public BiFinder(Graph<E> graph, E start, E goal, Class type)
	{
		if(type == BFSFinder.class)
		{
			graph1 = new BFSFinder(graph, start, goal);
			graph2 = new BFSFinder(graph, goal, start);
		}
		else if(type == DFSFinder.class)
		{
			graph1 = new DFSFinder(graph, start, goal);
			graph2 = new DFSFinder(graph, goal, start);
		}
		else if(type == DijkstraFinder.class)
		{
			graph1 = new DijkstraFinder(graph, start, goal);
			graph2 = new DijkstraFinder(graph, goal, start);
		}
		else if(type == AStarFinder.class)
		{
			graph1 = new AStarFinder(graph, start, goal);
			graph2 = new AStarFinder(graph, goal, start);
		}
		else
			throw new RuntimeException(
					"The algorithm provided is not available.");
		
		return;
	}
	
	@Override public int getCompleteDistance()
	{
		if(!done)
			return -1;
		
		E junction = connection();
		return graph1.getDistance(junction) + graph2.getDistance(junction);
	}

	@Override public List<E> getCompletePath()
	{
		if(!done)
			return new LinkedList<E>();
		
		LinkedList<E> ret = new LinkedList<E>();
		E junction = connection();
		
		for(E item : graph1.getPath(junction))
			ret.add(item);
		
		Stack<E> convenience = new Stack<E>();
		
		for(E item : graph2.getPath(junction))
			convenience.push(item);
		
		// We don't need two junctions.
		convenience.pop();
		
		while(!convenience.isEmpty())
			ret.add(convenience.pop());
		
		return ret;
	}
	
	@Override public Collection<E> closedSetReverse()
	{
		return graph2.closedSet();
	}

	@Override public int getDistanceReverse(E vertex)
	{
		return graph2.getDistance(vertex);
	}

	@Override public List<E> getPathReverse(E vertex)
	{
		return graph2.getPath(vertex);
	}

	@Override public E getPredecessorReverse(E vertex)
	{
		return graph2.getPredecessor(vertex);
	}

	@Override public Iterator<E> openSetReverse()
	{
		return graph2.openSet();
	}

	@Override public boolean done()
	{
		return done;
	}
	
	@Override public Collection<E> closedSet()
	{
		return graph1.closedSet();
	}

	@Override public int getDistance(E vertex)
	{
		return graph1.getDistance(vertex);
	}

	@Override public List<E> getPath(E vertex)
	{
		return graph1.getPath(vertex);
	}

	@Override public E getPredecessor(E vertex)
	{
		return graph1.getPredecessor(vertex);
	}

	@Override public Iterator<E> openSet()
	{
		return graph1.openSet();
	}

	/**
	 * Returns true if there is a path connection graph1 and graph2
	 * and returns false otherwise.
	 * @return true if a connection is found, false otherwise.
	 */
	private E connection()
	{
		for(E item : graph1.closedSet())
			for(E comp : graph2.closedSet())
				if(item.equals(comp))
					return item;
		
		return null;
	}
	
	@Override public E step()
	{
		if(connection() != null)
			done = true;
		
		if(done)
			return null;
		
		if(lgraph_graph1)
		{
			lgraph_graph1 = false;
			return graph2.step();
		}
		
		lgraph_graph1 = true;
		return graph1.step();
	}
	
	/**
	 * This is true if the item in question has been found
	 * and false otherwise.
	 */
	private boolean done = false;
	
	/**
	 * Records the last graph used for step.
	 */
	private boolean lgraph_graph1 = false;
	
	/**
	 * Contains a normal search algorithm.
	 */
	private GraphAnimation<E> graph1;
	
	/**
	 * Contains a reverse search algorithm.
	 */
	private GraphAnimation<E> graph2;
}