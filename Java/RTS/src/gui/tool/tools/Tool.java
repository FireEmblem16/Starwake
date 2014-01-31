package gui.tool.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import engine.Pointer;
import grid.Location;
import grid.Map;
import image.container.MappedBufferedImage;
import item.Item;
import item.Type;

/**
 * Outlines what it means to be a tool.
 */
public abstract class Tool
{
	/**
	 * Constructs and initializes common components of this tool.
	 */
	protected Tool(ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image)
	{
		try
		{img = new MappedBufferedImage("Tool Pic",ImageIO.read(new File(image)),null);}
		catch(IOException e)
		{}
		
		this.type = type;
		this.size = size;
		this.Class = Class;
		selected = selected_locations;
		select_center = center;
		
		return;
	}
	
	/**
	 * Performs this tool's action on [map] starting at [loc].
	 */
	public abstract void DoAction(Map map, Location loc);
	
	/**
	 * Returns an image representation of this tool.
	 */
	public MappedBufferedImage GetImageRepresentation()
	{
		return img;
	}
	
	/**
	 * Returns the size of the tool we want to use. Defaults to one.
	 */
	protected int GetSize()
	{
		if(size == null || !(size.getValue() instanceof Integer))
			return 1;
		
		return (Integer)size.getValue();
	}
	
	/**
	 * Returns a new instance of the class we are trying to work with.
	 */
	protected Item GetInstanceOfSelectedClass(Map map)
	{
		if(Class == null || !(Class.getSelectedItem() instanceof String))
			return null;
		
		return Item.CreateNew(map,(String)Class.getSelectedItem());
	}
	
	/**
	 * Returns the layer we are trying to interact with.
	 * Returns null if the layer was invalid or if type was All Layers.
	 */
	protected Type GetLayer()
	{
		if(type == null || !(type.getSelectedItem() instanceof String))
			return null;
		
		String val = (String)type.getSelectedItem();
		val = val.toLowerCase();
		
		if(val.equals("all layers"))
			return null;
		
		return Type.GetType(val);
	}
	
	/**
	 * Returns true if we are trying to interact with all layers and false otherwise.
	 */
	protected boolean IsAllLayers()
	{
		if(type == null || !(type.getSelectedItem() instanceof String))
			return false;
		
		String val = (String)type.getSelectedItem();
		val = val.toLowerCase();
		
		if(val.equals("all layers"))
			return true;
		
		return false;
	}
	
	/**
	 * Returns an arraylist of locations in a square centered at [center] of side length
	 * [width]. If [width] is even then we will have the square be futher towards the top-left.
	 * If [center] is null returns null. If [wdith] is invalid returns a list containing only [center].
	 */
	protected ArrayList<Location> GetLocations(Location center, int width)
	{
		if(center == null)
			return null;
		
		ArrayList<Location> ret = new ArrayList<Location>();
		
		if(width < 1)
		{
			ret.add(center);
			return ret;
		}
		
		for(int i = -(width / 2);i < (width + 1) / 2;i++)
			for(int j = -(width / 2);j < (width + 1) / 2;j++)
				ret.add(new Location(center.x + i,center.y + j,center.map));
		
		return ret;
	}
	
	/**
	 * Contains all of the locations currently selected.
	 */
	protected ArrayList<Location> selected;
	
	/**
	 * Contains an image representation of this tool.
	 */
	protected MappedBufferedImage img;
	
	/**
	 * Contains the type of item we are trying to paint.
	 */
	protected JComboBox type;
	
	/**
	 * Contains the class of item we want to create.
	 */
	protected JComboBox Class;
	
	/**
	 * Contains the "size" of the tool.
	 */
	protected JSpinner size;
	
	/**
	 * Contains the location of the center of the selected location.
	 */
	protected Pointer<Location> select_center;
}
