package edu.iastate.cs228.hw5;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.PriorityQueue;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * Contains code to perform a search of a graph using Diskstra's
 * algorthim over the graph provided from point A to point B.
 * @author Donald Nye
 */
public class DijkstraFinder<E> extends ComplexFinder<E>
{
	/**
	 * Creates a new instance of this object.
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 */
	public DijkstraFinder(Graph<E> graph, E start, E goal)
	{
		super(graph, start, goal);
		
		openSet = new PriorityQueue<Edge<E>>(11,
			new Comparator<Edge<E>>()
			{
				@Override public int compare(Edge<E> e1, Edge<E> e2)
				{
					if(e1.weight > e2.weight)
						return 1;
					
					if(e1.weight < e2.weight)
						return -1;
					
					return 0;
				}
			}
		);
		openSet.add(new Edge<E>(start,0));
		
		return;
	}

	@Override public E step()
	{
		if(openSet.isEmpty())
			done = true;
		
		if(done)
			return null;
		
		Edge<E> current = openSet.poll();
		closedSet.add(current.vertex);
		
		if(current.vertex.equals(goal))
		{
			done = true;
			return current.vertex;
		}
		
		Iterator<Edge<E>> iter = graph.getNeighbors(current.vertex);
		
		while(iter.hasNext())
		{
			Edge<E> neighbor = iter.next();
			
			if(!neighbor.vertex.equals(start) &&
					(predMap.get(neighbor.vertex) == null ||
					predMap.get(neighbor.vertex).weight >
					neighbor.weight + predMap.get(current.vertex).weight))
			{
				predMap.put(neighbor.vertex, new Edge<E>(current.vertex,
							neighbor.weight + predMap.get(current.vertex).weight));
				openSet.add(new Edge<E>(neighbor.vertex, neighbor.weight + predMap.get(current.vertex).weight));
			}
		}
		
		return current.vertex;
	}
}