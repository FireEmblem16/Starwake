package item.map;

import item.Item;
import item.Type;
import java.io.FileWriter;
import java.io.IOException;
import xml.Parser;
import xml.Tag;
import grid.Location;
import grid.Map;

/**
 * Contains the basic information about on how to draw a background image
 * and the basic information about what it means to be a place in a map.
 */
public abstract class MapItem extends Item
{
	/**
	 * Constructs and initializes this map item.
	 */
	public MapItem(Map m)
	{
		super(m);
		
		cost = 1;
		defbonus = 0;
		avoidbonus = 0;
		lifegain = 0;
		
		passable = true;
		flyable = true;
		swimable = false;
		climbable = false;
		
		type = Type.BACKGROUND;
		return;
	}
	
	/**
	 * Sets the cost of moving through this space.
	 */
	public void SetCost(int c)
	{
		cost = c;
		return;
	}
	
	/**
	 * Returns the cost of moving through this space.
	 */
	public int GetCost()
	{
		return cost;
	}
	
	/**
	 * Sets the defensive bonus of this space.
	 */
	public void SetDefenceBonus(int d)
	{
		defbonus = d;
		return;
	}
	
	/**
	 * Returns the defensive bonus of this space.
	 */
	public int GetDefenseBonus()
	{
		return defbonus;
	}
	
	/**
	 * Sets the avoid bonus of this space.
	 */
	public void SetAvoidBonus(int a)
	{
		avoidbonus = a;
		return;
	}
	
	/**
	 * Returns the avoid bonus of this space.
	 */
	public int GetAvoidBonus()
	{
		return avoidbonus;
	}
	
	/**
	 * Sets the life gained by being in this space.
	 */
	public void SetLifeGain(int l)
	{
		lifegain = l;
		return;
	}
	
	/**
	 * Returns the life gained by being in this spcae.
	 */
	public int GetLifeGain()
	{
		return lifegain;
	}
	
	/**
	 * Returns true if units can walk over this space and false otherwise.
	 */
	public boolean IsPassable()
	{
		return passable;
	}
	
	/**
	 * Sets the ability for units to walk over this item.
	 */
	public void SetPassable(boolean yes)
	{
		passable = yes;
		return;
	}
	
	/**
	 * Returns true if units in the air can fly over this space and false otherwise.
	 */
	public boolean IsFlyable()
	{
		return flyable;
	}
	
	/**
	 * Sets the ability for units to fly over this item.
	 */
	public void SetFlyable(boolean yes)
	{
		flyable = yes;
		return;
	}
	
	/**
	 * Returns true if units in the air can swim through this space and false otherwise.
	 */
	public boolean IsSwimable()
	{
		return swimable;
	}
	
	/**
	 * Sets the ability for units to swim through this item.
	 */
	public void SetSwimable(boolean yes)
	{
		swimable = yes;
		return;
	}
	
	/**
	 * Returns true if units in the air can climb over this space and false otherwise.
	 */
	public boolean IsClimbable()
	{
		return climbable;
	}
	
	/**
	 * Sets the ability for units to climb over this item.
	 */
	public void SetClimbable(boolean yes)
	{
		climbable = yes;
		return;
	}
	
	/**
	 * Attempts to alter a parameter of this map item as given by [param] to the value [val].
	 * [val] is not allowed to be null and param is assumed to be all lower case.
	 */
	public boolean SetParam(String param, Object val)
	{
		if(param == null)
			return false;
		
		if(param.equals("cost"))
		{
			if(val instanceof Integer)
			{
				cost = (Integer)val;
				return true;
			}
		}
		else if(param.equals("def-bonus"))
		{
			if(val instanceof Integer)
			{
				defbonus = (Integer)val;
				return true;
			}
		}
		else if(param.equals("avoid-bonus"))
		{
			if(val instanceof Integer)
			{
				avoidbonus = (Integer)val;
				return true;
			}
		}
		else if(param.equals("life-gain"))
		{
			if(val instanceof Integer)
			{
				lifegain = (Integer)val;
				return true;
			}
		}
		else if(param.equals("passable"))
		{
			if(val instanceof Boolean)
			{
				passable = (Boolean)val;
				return true;
			}
		}
		else if(param.equals("flyable"))
		{
			if(val instanceof Boolean)
			{
				flyable = (Boolean)val;
				return true;
			}
		}
		else if(param.equals("swimable"))
		{
			if(val instanceof Boolean)
			{
				swimable = (Boolean)val;
				return true;
			}
		}
		else if(param.equals("climbable"))
		{
			if(val instanceof Boolean)
			{
				climbable = (Boolean)val;
				return true;
			}
		}
		
		return super.SetParam(param,val);
	}
	
	/**
	 * Provides a nice debug view.
	 */
	public String toString()
	{
		return super.toString() + ", cost: " + cost + ", defbonus: " + defbonus + ", avoidbonus: " + avoidbonus + ", lifegain: " + lifegain;
	}
	
	/**
	 * Writes children specific XML data.
	 */
	public void WriteExtraXML(FileWriter out) throws IOException
	{
		if(out == null)
			return;
		
		out.write("\n");
		
		out.write("\t\t\t<passable = " + new Boolean(passable).toString() + ">\n");
		out.write("\t\t\t<flyable = " + new Boolean(flyable).toString() + ">\n");
		out.write("\t\t\t<swimable = " + new Boolean(swimable).toString() + ">\n");
		out.write("\t\t\t<climbable = " + new Boolean(climbable).toString() + ">\n");
		
		out.write("\n");
		
		out.write("\t\t\t<cost = " + new Integer(cost).toString() + ">\n");
		out.write("\t\t\t<avoid-bonus = " + new Integer(avoidbonus).toString() + ">\n");
		out.write("\t\t\t<def-bonus = " + new Integer(defbonus).toString() + ">\n");
		out.write("\t\t\t<life-gain = " + new Integer(lifegain).toString() + ">\n");
		
		return;
	}
	
	/**
	 * Indicates the cost of moving through this square.
	 */
	protected int cost;
	
	/**
	 * Indicates how much defense is gained by being in this space.
	 */
	protected int defbonus;
	
	/**
	 * Indicates how much avoid bonus is gained by being in this space.
	 */
	protected int avoidbonus;
	
	/**
	 * Indictes how much life is gained by being in this space at the start of the turn.
	 */
	protected int lifegain;
	
	/**
	 * If this is true anyone on the ground may pass through this item.
	 */
	protected boolean passable;
	
	/**
	 * If this is true then anyone in the air may pass through this item.
	 */
	protected boolean flyable;
	
	/**
	 * If this is true then anyone that can swim can pass through this item.
	 */
	protected boolean swimable;
	
	/**
	 * If this is true then anyone that can climb can pass through this item.
	 */
	protected boolean climbable;
}
