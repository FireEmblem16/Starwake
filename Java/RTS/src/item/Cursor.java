package item;

import engine.KeyBindings;
import grid.Location;
import grid.Map;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Contains information on how we should go about drawing a cursor
 * as well as where to do so.
 */
public class Cursor extends Item implements KeyListener
{
	/**
	 * Constrcuts and initializes this cursor.
	 */
	public Cursor(Map m)
	{
		super(m);
		
		iskeylistener = true;
		type = Type.CURSOR;
		return;
	}

	/**
	 * Will move the cursor if a key bound to it is pressed or held if
	 * the timestamp has passed the lag timeout.
	 */
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode() == binds.GetBinding("Left"))
		{
			Location l = loc;
			if(map.AddItem(this,new Location(loc.x - 1,loc.y,loc.map))) // Note AddItem changes [loc] for us
				map.RemoveItem(type,l); // If we successfully added the cursor then we need to remove the old one
		}
		else if(e.getKeyCode() == binds.GetBinding("Right"))
		{
			Location l = loc;
			if(map.AddItem(this,new Location(loc.x + 1,loc.y,loc.map))) // Note AddItem changes [loc] for us
				map.RemoveItem(type,l); // If we successfully added the cursor then we need to remove the old one
		}
		else if(e.getKeyCode() == binds.GetBinding("Up"))
		{
			Location l = loc;
			if(map.AddItem(this,new Location(loc.x,loc.y - 1,loc.map))) // Note AddItem changes [loc] for us
				map.RemoveItem(type,l); // If we successfully added the cursor then we need to remove the old one
		}
		else if(e.getKeyCode() == binds.GetBinding("Down"))
		{
			Location l = loc;
			if(map.AddItem(this,new Location(loc.x,loc.y + 1,loc.map))) // Note AddItem changes [loc] for us
				map.RemoveItem(type,l); // If we successfully added the cursor then we need to remove the old one
		}
		
		return;
	}

	/**
	 * Probably eats up about 30 clock cycles and nothing else.
	 */
	public void keyReleased(KeyEvent e)
	{return;}

	/**
	 * Probably eats up about 30 clock cycles and nothing else.
	 */
	public void keyTyped(KeyEvent e)
	{return;}
	
	/**
	 * Attempts to alter a parameter of this item as given by [param] to the value [val].
	 * [val] is not allowed to be null and param is assumed to be all lower case.
	 */
	public boolean SetParam(String param, Object val)
	{
		if(param == null)
			return false;
		
		if(param.equals("bindings"))
		{
			if(val == null || val instanceof KeyBindings)
			{
				binds = (KeyBindings)val;
				return true;
			}
		}
		
		return super.SetParam(param,val);
	}
	
	/**
	 * Writes children specific XML data.
	 */
	public void WriteExtraXML(FileWriter out) throws IOException
	{return;}
	
	/**
	 * Contains all of the game's key bindings.
	 */
	protected KeyBindings binds;
}
