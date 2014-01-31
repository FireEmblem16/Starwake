package item.map;

import item.Type;
import grid.Map;

/**
 * This class represents a tile filed with lava/fire or whatever. No matter what it does, it causes pain, and lots of it.
 * It also costs 2 to move through.
 */
public class Lava extends MapItem
{
	/**
	 * Constructs and initializes this lava.
	 */
	public Lava(Map m)
	{
		super(m);
		lifegain = -10;
		
		return;
	}
}
