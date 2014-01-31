package gui.tool.tools;

import item.Item;
import item.Type;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import engine.Pointer;
import grid.Location;
import grid.Map;
import gui.imageselector.ImageSelector;

/**
 * Selects images from a map to be the currently selected image.
 * If "All Layers" is selected then we select the first layer to have an image
 * as ordered by the map's paint function, background first, cursor last.
 */
public class EyedropTool extends Tool
{
	/**
	 * Initializes and constructs this object.
	 */
	public EyedropTool(ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image, ImageSelector selector)
	{
		super(selected_locations,center,type,Class,size,image);
		imgselector = selector;
		
		return;
	}

	/**
	 * Changes the selected image to what we just clicked on, size of tool is always one.
	 */
	public void DoAction(Map map, Location loc)
	{
		if(map == null || loc == null || Class == null || selected == null)
			return;
		
		Type t = GetLayer();
		selected.clear();
		select_center.ptr = null;
		
		if(t == null)
		{
			if(IsAllLayers())
			{
				Item item = null;
				t = Type.GetNext(null);
				
				while(t != null && item == null)
				{
					item = map.GetItem(t,loc);
					t = Type.GetNext(t);
				}
				
				if(item == null)
					imgselector.Select("No Pallet Selected","No Image Selected");
				else
					imgselector.Select(item.GetPallet(),item.GetPalletName());
			}
			else
				return;
		}
		else
		{
			Item item = map.GetItem(t,loc);
			
			if(item == null)
				imgselector.Select("No Pallet Selected","No Image Selected");
			else
				imgselector.Select(item.GetPallet(),item.GetPalletName());
		}
		
		return;
	}
	
	/**
	 * This is where we will change the image selection.
	 */
	protected ImageSelector imgselector;
}
