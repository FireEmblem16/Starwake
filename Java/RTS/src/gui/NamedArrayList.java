package gui;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * An arraylist with a name.
 */
public class NamedArrayList<T> extends ArrayList<T> implements Comparable
{
	/**
	 * Constructs and initializes this array list.
	 */
	public NamedArrayList(String n)
	{
		super();
		name = n;
		
		return;
	}
	
	/**
	 * Constructs and initializes this array list.
	 */
	public NamedArrayList(String n, Collection<? extends T> c)
	{
		super(c);
		name = n;
		
		return;
	}
	
	/**
	 * Constructs and initializes this array list.
	 */
	public NamedArrayList(String n, int i)
	{
		super(i);
		name = n;
		
		return;
	}
	
	/**
	 * Looks through the contents of this array list for something that returns a string of [n]
	 * and returns that. Returns null if no such element is found.
	 */
	public T Find(String n)
	{
		if(n == null)
			return null;
		
		for(int i = 0;i < size();i++)
			if(n.equals(get(i).toString()))
				return get(i);
		
		return null;
	}
	
	/**
	 * Operates normally unless [obj] is a String in which case it checks the name for equality.
	 */
	public boolean equals(Object obj)
	{
		if(obj instanceof String)
		{
			if(((String)obj).equals(name))
				return true;
		}
		
		return super.equals(obj);
	}
	
	/**
	 * Compares this with another object, preferably a NamedArrayList.
	 */
	public int compareTo(Object obj)
	{
		if(!(obj instanceof NamedArrayList))
			return 0;
		
		if(name.equals("No Image Selected") || name.equals("No Pallet Selected"))
			return -1;
		
		NamedArrayList o = (NamedArrayList)obj;
		
		if(o.size() == 0 && size() > 0)
			return -1;
		
		if(size() == 0 && o.size() > 0)
			return 1;
		
		return name.compareTo(o.name);
	}
	
	/**
	 * Determines if this array list has the given object in it by toString
	 * comparison if the given object is a String and normally otherwise. 
	 */
	public boolean contains(Object obj)
	{
		if(obj instanceof String)
		{
			String str = (String)obj;
			
			if(str == null)
				return false;
			
			for(int i = 0;i < size();i++)
				if(str.equals(get(i).toString()))
					return true;
		}
		
		return super.contains(obj);
	}
	
	/**
	 * Returns the name of this array list.
	 */
	public String toString()
	{
		return name;
	}
	
	/**
	 * Contains the name of this array list.
	 */
	protected String name;
}
