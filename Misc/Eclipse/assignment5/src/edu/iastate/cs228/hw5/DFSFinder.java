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
import java.util.Stack;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.GraphAnimation;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * Performs a depth first search through a graph.
 * @author Donald Nye
 */
public class DFSFinder<E> extends SimpleFinder<E>
{
	public DFSFinder(Graph<E> graph, E start, E goal)
	{
		super(graph, start, goal);
		
		openSet = new Stack<E>();
		((Stack<E>)openSet).push(start);
		
		return;
	}
	
	@Override public E step()
	{
		if(openSet.isEmpty())
			done = true;
		
		if(done)
			return null;
		
		E current = ((Stack<E>)openSet).pop();
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
				((Stack<E>)openSet).push(neighbor);
				predMap.put(neighbor,current);
			}
		}
		
		return current;
	}
}