package item.map;

import grid.Map;

/**
 * RooflessWalls may be flown over but not walked through.
 * RooflessWalls also give an avoid bonus of 15.
 */
public class RooflessWall extends MapItem
{
	/**
	 * Constructs and initializes this wall.
	 */
	public RooflessWall(Map m)
	{
		super(m);
		
		avoidbonus = 15;
		passable = false;
		flyable = true;
		
		return;
	}
}
