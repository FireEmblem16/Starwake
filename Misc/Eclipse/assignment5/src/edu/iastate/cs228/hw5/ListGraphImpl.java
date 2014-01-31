package edu.iastate.cs228.hw5;

import edu.iastate.cs228.hw5.api.Graph.Edge;
import edu.iastate.cs228.hw5.api.ListGraph;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Graph implementation based on adjacency lists.
 * @author Donald Nye
 */
public class ListGraphImpl<E> implements ListGraph<E>
{
	/**
	 * Creates a new instance of this object.
	 */
	public ListGraphImpl()
	{
		map = new HashMap<E, HashSet<Edge<E>>>();
		
		return;
	}
	
	@Override public boolean addEdge(E u, E v, int weight)
	{
		if(u.equals(v))
			throw new RuntimeException("Circular-loop");
		
		if(!map.containsKey(u) || !map.containsKey(v))
			throw new NoSuchElementException("Missing Vertex");
		
		Iterator<Edge<E>> iter = map.get(u).iterator();
		
		while(iter.hasNext())
		{
			Edge<E> e = iter.next();
			
			if(e.vertex.equals(v))
				if(e.weight == weight)
					return false;
				else
				{
					e.weight = weight;
					return true;
				}
		}
		
		return map.get(u).add(new Edge<E>(v,weight));
	}

	@Override public boolean addVertex(E vertex)
	{
		if(map.containsKey(vertex))
			return false;
		
		map.put(vertex,new HashSet<Edge<E>>());
		return true;
	}

	@Override public Iterator<E> vertices()
	{
		return map.keySet().iterator();
	}

	@Override public Iterator<Edge<E>> getNeighbors(E vertex)
	{
		HashSet<Edge<E>> e = map.get(vertex);
		
		if(e == null)
			return null;
		
		return Collections.unmodifiableCollection(e).iterator();
	}

	@Override public int h(E current, E goal)
	{
		return 0;
	}
	
	/**
	 * Determines if this graph has the vertex [v] in it.
	 * @param v the vertex to look for
	 * @return true if the vertex exists in the graph and false otherwise
	 */
	private boolean hasVertex(E v)
	{
		return map.containsKey(v);
	}
	
	/**
	 * Determines if this graph has an edge from [v1] to [v2] in it.
	 * @param v1 the starting vertex
	 * @param v2 the ending vertex
	 * @return true if the vertex edge exists in the graph and false otherwise
	 */
	private boolean hasEdge(E v1, E v2)
	{
		if(!map.containsKey(v1))
			return false;
		
		for(Edge<E> e : map.get(v1))
			if(e.vertex.equals(v2))
				return true;
		
		return false;
	}
	
	/**
	 * Contains the set of vertecies in this graph and their edges.
	 */
	private HashMap<E, HashSet<Edge<E>>> map;
}