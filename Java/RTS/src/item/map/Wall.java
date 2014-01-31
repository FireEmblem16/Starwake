package item.map;

import grid.Map;

/**
 * Walls may not be flown over and my not walked through. 
 */
public class Wall extends MapItem
{
	/**
	 * Constructs and initializes this wall.
	 */
	public Wall(Map m)
	{
		super(m);
		
		passable = false;
		flyable = false;
		return;
	}
}
