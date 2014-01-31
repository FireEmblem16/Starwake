package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a peak or some other similar thing.
 * It grants an avoid bonus of 50, a defensive bonus of 4 and costs 1 to move through.
 * A peak can only be reached by a flying unit.
 */
public class Peak extends MapItem
{
	/**
	 * Constructs and initializes this peak.
	 */
	public Peak(Map m)
	{
		super(m);
		
		defbonus = 4;
		avoidbonus = 50;
		passable = false;
		
		return;
	}
}
