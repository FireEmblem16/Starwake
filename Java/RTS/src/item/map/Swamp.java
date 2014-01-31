package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a swamp or some other similar thing.
 * It grants an avoid bonus of -15 and costs 4 to move through.
 * A swamp is swimmable.
 */
public class Swamp extends MapItem
{
	/**
	 * Constructs and initializes this swamp.
	 */
	public Swamp(Map m)
	{
		super(m);
		
		cost = 4;
		avoidbonus = -15;
		swimable = true;
		
		return;
	}
}
