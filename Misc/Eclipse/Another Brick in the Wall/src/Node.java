import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Node<T> implements Iterable
{
	public Node(T ID)
	{
		this.ID = ID;
		children = new ArrayList<Pair<Node<T>,Integer>>();
		
		return;
	}
	
	public int MinDistance(Node<T> sink)
	{
		ArrayList<Pair<Node<T>,Integer>> visited = new ArrayList<Pair<Node<T>,Integer>>();
		visited.add(new Pair<Node<T>,Integer>(this,0));
		
		PriorityQueue<Pair<Node<T>,Integer>> edges = new PriorityQueue<Pair<Node<T>,Integer>>(children.size(),new Comparator<Pair<Node<T>,Integer>>()
		{
			public int compare(Pair<Node<T>,Integer> p1, Pair<Node<T>,Integer> p2)
			{return p1.t.compareTo(p2.t);}
		});
		edges.addAll(children);
		
		while(!edges.isEmpty())
		{
			Pair<Node<T>,Integer> next = edges.poll();
			
			if(next.s.equals(sink))
				return next.t;
				
			boolean cont = false;
			
			for(Pair<Node<T>,Integer> p : visited)
				if(next.s.equals(p.s))
				{
					cont = true;
					break;
				}
			
			if(cont)
				continue;
			
			visited.add(next);
			
			for(Pair<Node<T>,Integer> p : next.s.children)
				edges.add(new Pair<Node<T>,Integer>(p.s,next.t + p.t));
		}
		
		return -1;
	}
	
	public ArrayList<Node<T>> MinPath(Node<T> sink)
	{
		ArrayList<Pair<Pair<Node<T>,Node<T>>,Integer>> visited = new ArrayList<Pair<Pair<Node<T>,Node<T>>,Integer>>();
		visited.add(new Pair<Pair<Node<T>,Node<T>>,Integer>(new Pair<Node<T>,Node<T>>(this,null),0));
		
		PriorityQueue<Pair<Pair<Node<T>,Node<T>>,Integer>> edges = new PriorityQueue<Pair<Pair<Node<T>,Node<T>>,Integer>>(children.size(),new Comparator<Pair<Pair<Node<T>,Node<T>>,Integer>>()
		{
			public int compare(Pair<Pair<Node<T>,Node<T>>,Integer> p1, Pair<Pair<Node<T>,Node<T>>,Integer> p2)
			{return p1.t.compareTo(p2.t);}
		});
		
		for(Pair<Node<T>,Integer> p : children)
			edges.add(new Pair<Pair<Node<T>,Node<T>>,Integer>(new Pair<Node<T>,Node<T>>(p.s,this),p.t));
		
		while(!edges.isEmpty())
		{
			Pair<Pair<Node<T>,Node<T>>,Integer> next = edges.poll();
			
			if(next.s.s.equals(sink))
			{
				LinkedList<Node<T>> ret = new LinkedList<Node<T>>();
				
				Pair<Pair<Node<T>,Node<T>>,Integer> current = next;
				ret.addFirst(current.s.s);
				
				do // There is at least one node (the sink/start)
				{
					Node<T> search = current.s.t;
					
					for(Pair<Pair<Node<T>,Node<T>>,Integer> p : visited)
						if(search.equals(p.s.s))
						{
							current = p;
							break;
						}
					
					ret.addFirst(current.s.s);
				}
				while(current.s.t != null);
				
				return new ArrayList<Node<T>>(ret);
			}
				
			boolean cont = false;
			
			for(Pair<Pair<Node<T>,Node<T>>,Integer> p : visited)
				if(next.s.s.equals(p.s))
				{
					cont = true;
					break;
				}
			
			if(cont)
				continue;
			
			visited.add(next);
			
			for(Pair<Node<T>,Integer> p : next.s.s.children)
				edges.add(new Pair<Pair<Node<T>,Node<T>>,Integer>(new Pair<Node<T>,Node<T>>(p.s,next.s.s),next.t + p.t));
		}
		
		return null;
	}
	
	public void AddChild(Node<T> n, int weight)
	{
		children.add(new Pair<Node<T>,Integer>(n,weight));
		return;
	}
	
	public boolean RemoveChild(Node<T> n)
	{
		return children.remove(new Pair<Node<T>,Integer>(n,0));
	}
	
	public Iterator<Pair<Node<T>,Integer>> iterator()
	{return children.iterator();}
	
	public boolean equals(Object obj)
	{
		if(this.getClass().isAssignableFrom(obj.getClass()))
			return equals((Node)obj);
		
		if(ID.getClass().isAssignableFrom(obj.getClass()))
			return ID.equals(obj);
		
		return false;
	}
	
	public boolean equals(Node n)
	{return ID.equals(n.ID);}
	
	public String toString()
	{return ID.toString();}
	
	public T ID;
	public ArrayList<Pair<Node<T>,Integer>> children;
}
