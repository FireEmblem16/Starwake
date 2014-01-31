package edu.iastate.cs228.hw5;

import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.PriorityQueue;
import edu.iastate.cs228.hw5.api.Graph;
import edu.iastate.cs228.hw5.api.Graph.Edge;

/**
 * Contains code to peform a serach of a graph using the
 * A* algorithm over the provided graph from point A to point B.
 * @author Donald Nye
 */
public class AStarFinder<E> extends ComplexFinder<E>
{
	/**
	 * Creates a new instance of this object.
	 * @param graph the graph to be searched
	 * @param start the start node
	 * @param goal the goal node
	 */
	public AStarFinder(Graph<E> graph, E start, E goal)
	{
		super(graph, start, goal);
		
		openSet = new PriorityQueue<Edge<E>>(11,
									new Comparer(graph));
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
	
	/**
	 * We create this shell for a comparator becuase it looks
	 * nicer like this and it makes it easier to use it.
	 */
	private class Comparer implements Comparator<Edge<E>>
	{
		/**
		 * Creates a new instance of this object.
		 * @param graph the graph this comparator should use
		 * to determine position relative to the goal
		 */
		public Comparer(Graph<E> graph)
		{
			this.graph = graph;
			return;
		}
		
		@Override public int compare(Edge<E> e1, Edge<E> e2)
		{
			int e1w = e1.weight + graph.h(e1.vertex, goal);
			int e2w = e2.weight + graph.h(e2.vertex, goal);
			
			if(e1w > e2w)
				return 1;
			
			if(e1w < e2w)
				return -1;
			
			return 0;
		}
		
		/**
		 * The graph we are operating on.
		 */
		private Graph<E> graph;
	}
}
