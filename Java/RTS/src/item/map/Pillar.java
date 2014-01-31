package item.map;

import grid.Map;

/**
 * Pillars are useful for gaining an advantage to avoid.
 */
public class Pillar extends MapItem
{
	/**
	 * Constructs and initializes this pillar.
	 */
	public Pillar(Map m)
	{
		super(m);
		
		cost = 2;
		avoidbonus = 10;
		
		return;
	}
}
