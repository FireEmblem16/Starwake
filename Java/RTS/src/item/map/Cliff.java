package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a cliff or some other similar thing.
 * It grants an avoid bonus of -5, a def bonus of 1 and costs 3 to move through.
 * Cliffs may be climbed or walked on.
 */
public class Cliff extends MapItem
{
	/**
	 * Constructs and initializes this cliff.
	 */
	public Cliff(Map m)
	{
		super(m);
		
		cost = 3;
		avoidbonus = -5;
		defbonus = 1;
		climbable = true;
		
		return;
	}
}
