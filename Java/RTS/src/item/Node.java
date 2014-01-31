package item;

import java.io.FileWriter;
import java.io.IOException;
import grid.Map;

/**
 * Represents a node in pathfinding for AI. These are placed at critical
 * locations such as doorways where pathfinding can fail easily.
 */
public class Node extends Item
{
	/**
	 * Constructs and initializes this node.
	 */
	public Node(Map m)
	{
		super(m);
		
		type = Type.NODE;
		return;
	}
	
	/**
	 * Writes children specific XML data.
	 */
	public void WriteExtraXML(FileWriter out) throws IOException
	{return;}
}
