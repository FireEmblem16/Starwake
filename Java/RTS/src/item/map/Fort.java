package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a fort or some other similar thing.
 * It grants an avoid bonus of 30, a defence bonus of 2, a recovery bonus of 5 and costs 2 to move through.
 */
public class Fort extends MapItem
{
	/**
	 * Constructs and initializes this fort.
	 */
	public Fort(Map m)
	{
		super(m);
		
		cost = 2;
		avoidbonus = 30;
		defbonus = 2;
		lifegain = 5;
		
		return;
	}
}
