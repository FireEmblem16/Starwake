package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents a forest that grants a +1 defensive bonus and +20 to avoid.
 * It also costs 2 to move through.
 */
public class Forest extends MapItem
{
	/**
	 * Constructs and initializes this forest.
	 */
	public Forest(Map m)
	{
		super(m);
		
		cost = 2;
		defbonus = 1;
		avoidbonus = 20;
		
		return;
	}
}
