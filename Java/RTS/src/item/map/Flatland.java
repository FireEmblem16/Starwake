package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a flat plain or some other similar thing.
 * It grants no bonuses nor penalties and costs but 1 to move through.
 */
public class Flatland extends MapItem
{
	/**
	 * Constructs and initializes this flatland.
	 */
	public Flatland(Map m)
	{
		super(m);
		return;
	}
}
