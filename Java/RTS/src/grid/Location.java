package grid;

import java.awt.Point;

/**
 * Essentially a point but with an identification for what map it's on.
 */
public class Location extends Point implements Comparable
{
	/**
	 * Constructs and initializes this Location to (0,0).
	 */
	public Location(String map)
	{
		super();
		
		this.map = map;
		return;
	}
	
	/**
	 * Constructs and initializes this Location to ([x],[y]).
	 */
	public Location(int x, int y, String map)
	{
		super(x,y);
		
		this.map = map;
		return;	
	}
	
	/**
	 * Returns true if this location's [x] and [y] values are equal.
	 * Makes no checks on [map].
	 */
	public boolean equals(Object obj)
	{
		if(obj == null || !(obj instanceof Location))
			return false;
		
		Location l2 = (Location)obj;
		
		return (x == l2.x && y == l2.y);
	}
	
	/**
	 * A location is smaller if it is closer to (0,0) in left to right, top to bottom order.
	 */
	public int compareTo(Object obj)
	{
		if(obj == null || !(obj instanceof Location))
			return 0;
		
		Location l2 = (Location)obj;
		return (y > l2.y ? 1 : (x > l2.x && y == l2.y ? 1 : (x == l2.x && y == l2.y ? 0 : -1)));
	}
	
	/**
	 * Provides a nice debug view.
	 */
	public String toString()
	{
		return "<" + x + "," + y + "," + map + ">";
	}
	
	/**
	 * Should contain the name of the map this location is on.
	 */
	public String map;
}
