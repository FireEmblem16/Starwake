package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a beach or some other similar thing.
 * It grants an avoid bonus of 5 from the sand/water and costs 2 to move through.
 */
public class Beach extends MapItem
{
	/**
	 * Constructs and initializes this beach.
	 */
	public Beach(Map m)
	{
		super(m);
		
		cost = 2;
		avoidbonus = 5;
		swimable = true;
		
		return;
	}
}
