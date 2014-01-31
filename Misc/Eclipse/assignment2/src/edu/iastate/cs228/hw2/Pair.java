package edu.iastate.cs228.hw2;

/**
 * A simple generic class with a type parameter E. Type notations: <?> means any
 * type. <? super E> means any supertype of E. <? extends E> means any subtype
 * of E. <E extends B> means that E is a subtype of B. <E super B> means that E
 * is a supertype of B. E is a subtype and supertype of itself. E is a subtype
 * of T if and only T is a supertype of E.
 */

import java.util.Comparator;

public class Pair<E> // <E> means that E is a type parameter for the class.
{
	private E first;
	private E second;

	public Pair(E aFirst, E aSecond)
	{
		first = aFirst;
		second = aSecond;
	} // constructor

	public E getFirst()
	{
		return first;
	} // getFirst

	public E getSecond()
	{
		return second;
	} // getSecond

	public void setFirst(E data)
	{
		first = data;
	}

	public void setSecond(E data)
	{
		second = data;
	}

	public void swap()
	{
		E tmp = first;
		first = second;
		second = tmp;
	}

	public String toString()
	{
		return "[" + first + ", " + second + "]";
	}

	@Override public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(obj.getClass() != this.getClass())
			return false;
		Pair<?> other = (Pair<?>)obj; // downcast with a question mark ? meaning
										// any type.
		// first or second may be null.
		return ((first == other.getFirst() || first != null
		&& first.equals(other.getFirst())) && (second == other.getSecond() || second != null
		&& second.equals(other.getSecond())));
	}

	// A generic method for using a comparator to order the components of a
	// pair.
	public static <T>void orderPair(Pair<T> pair, Comparator<? super T> comp)
	// public static <T> void orderPair(Pair<T> pair, Comparator<T> comp)
	// The commented-out line is not flexible; it would not compile for the
	// test example in ComparatorExample.java.
	{
		T one = pair.getFirst();
		T two = pair.getSecond();
		if(comp.compare(one,two) > 0)
		{
			pair.swap();
		}
	}

	// A generic method for using the natural ordering of type T to order the
	// components of a pair.
	public static <T extends Comparable<? super T>>void orderPair(Pair<T> pair)
	// public static <T extends Comparable<T>> void orderPair(Pair<T> pair)
	// The commented-out line is not flexible; it would not compile for the
	// test example in ComparableExample.java.
	{
		T one = pair.getFirst();
		T two = pair.getSecond();
		if(one.compareTo(two) > 0)
		{
			pair.swap();
		}
	}

} // Pair