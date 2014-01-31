package image.container;

/**
 * Contains two of any object plus a third object.
 */
public class Pair<T,V>
{
	/**
	 * Constructs and initializes this object.
	 */
	public Pair(T T1, T T2)
	{
		t1 = T1;
		t2 = T2;
		
		return;
	}
	
	/**
	 * Constructs and initializes this object.
	 */
	public Pair(T T1, T T2, V V)
	{
		t1 = T1;
		t2 = T2;
		v = V;
		
		return;
	}
	
	/**
	 * Contains the first paired up object.
	 */
	public T t1;
	
	/**
	 * Contains the second paired up object.
	 */
	public T t2;
	
	/**
	 * Contains a helper object.
	 */
	public V v;
}
