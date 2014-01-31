package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a mountain or some other similar thing.
 * It grants an avoid bonus of 25, a defence bonus of 2 and costs 3 to move through.
 * A Mountain may be climbed but not walked on.
 */
public class Mountain extends MapItem
{
	/**
	 * Constructs and initializes this maountain.
	 */
	public Mountain(Map m)
	{
		super(m);
		
		cost = 3;
		avoidbonus = 25;
		defbonus = 2;
		passable = false;
		climbable = true;
		
		return;
	}
}
