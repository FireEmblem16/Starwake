package item;

import java.io.FileWriter;
import java.io.IOException;
import grid.Map;

/**
 * This represents an item in the game's engine.
 */
public class GameObject extends Item
{
	/**
	 * Constructs and initializes this object.
	 */
	public GameObject(Map m)
	{
		super(m);
		return;
	}
	
	/**
	 * Writes children specific XML data.
	 */
	public void WriteExtraXML(FileWriter out) throws IOException
	{return;}
}
