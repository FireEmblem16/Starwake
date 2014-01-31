package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents what amounts to be a ocean or some other similar thing.
 * It grants an avoid bonus of 10 and costs 3 to move through.
 * Ocean can not be walked on but it can be swam in.
 */
public class Ocean extends MapItem
{
	/**
	 * Constructs and initializes this beach.
	 */
	public Ocean(Map m)
	{
		super(m);
		
		cost = 3;
		avoidbonus = 10;
		swimable = true;
		passable = false;
		
		return;
	}
}
