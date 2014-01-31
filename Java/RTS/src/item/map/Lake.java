package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a lake or some other similar thing.
 * It grants an avoid bonus of 15 and costs 2 to move through.
 */
public class Lake extends MapItem
{
	/**
	 * Constructs and initializes this lake.
	 */
	public Lake(Map m)
	{
		super(m);
		
		cost = 2;
		avoidbonus = 15;
		passable = false;
		swimable = true;
		
		return;
	}
}
