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
 * Sample implementation of GraphAnimation based on breadth-first search.
 */
public class BFSFinder<E> extends SimpleFinder<E>
{
	/**
	 * Constructs a BFSFinder for the given graph and the given start and goal
	 * nodes. The goal may be null.
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 */
	public BFSFinder(Graph<E> graph, E start, E goal)
	{
		super(graph, start, goal);
		
		openSet = new LinkedList<E>();
		((Deque<E>)openSet).add(start);
		
		return;
	}
	
	@Override public E step()
	{
		if(openSet.isEmpty())
			done = true;
		
		if(done)
			return null;
		
		E current = ((Deque<E>)openSet).remove();
		closedSet.add(current);
		
		if(current.equals(goal))
		{
			done = true;
			return current;
		}
		
		Iterator<Edge<E>> iter = graph.getNeighbors(current);
		
		while(iter.hasNext())
		{
			E neighbor = iter.next().vertex;
			
			if(!neighbor.equals(start) && predMap.get(neighbor) == null)
			{
				((Deque<E>)openSet).add(neighbor);
				predMap.put(neighbor,current);
			}
		}
		
		return current;
	}
}