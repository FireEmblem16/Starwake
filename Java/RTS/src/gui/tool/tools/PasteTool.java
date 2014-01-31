package gui.tool.tools;

import image.container.MappedBufferedImage;
import image.container.Pallet;
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
 * Moves all selected items to the selected location but does not remove the old selected items.
 */
public class PasteTool extends Tool
{
	/**
	 * Initializes and constructs this object.
	 */
	public PasteTool(ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image)
	{
		super(selected_locations,center,type,Class,size,image);
		return;
	}

	/**
	 * Moves all of the selected items to the selected location but does not remove the old selected items.
	 * Using this tool does not unselect items but rather changes the selection to the new images.
	 * Since pasting works based on what is selected the paste will always be the same so long as the full
	 * set of selected images are pasted onto the map.
	 */
	public void DoAction(Map map, Location loc)
	{
		if(map == null || loc == null || Class == null || selected == null || select_center == null || select_center.ptr == null)
			return;
		
		if(select_center.ptr.equals(loc))
			return;
		
		if(loc.x < 1 || loc.x > map.GetWidth() || loc.y < 1 || loc.y > map.GetHeight())
			return;
		
		Type t = GetLayer();
		ArrayList<Item> items = new ArrayList<Item>();
		
		if(t == null)
			if(IsAllLayers())
			{
				t = Type.GetNext(null);
				
				while(t != null)
				{
					for(int i = 0;i < selected.size();i++)
					{
						Item item = map.GetItem(t,selected.get(i));
						
						if(item != null)
							items.add(item);
					}
					
					t = Type.GetNext(t);
				}
			}
			else
				return;
		else
			for(int i = 0;i < selected.size();i++)
			{
				Item item = map.GetItem(t,selected.get(i));
				
				if(item != null)
					items.add(item);
			}
		
		ArrayList<Location> new_locs = new ArrayList<Location>();
		
		int x_offset = loc.x - select_center.ptr.x;
		int y_offset = loc.y - select_center.ptr.y;
		
		if(t == null)
			if(IsAllLayers())
			{
				t = Type.GetNext(null);
				
				while(t != null)
				{
					for(int i = 0;i < items.size();i++)
					{
						Item item = items.get(i).Clone();
						
						Location new_loc = new Location(items.get(i).GetLocation().x + x_offset,items.get(i).GetLocation().y + y_offset,items.get(i).GetLocation().map);
						item.SetLocation(new_loc);
						
						if(!new_locs.contains(new_loc))
							new_locs.add(new_loc);
						
						map.RemoveItem(t,new_loc);
						map.AddItem(item,new_loc);
					}
					
					t = Type.GetNext(t);
				}
			}
			else
				return;
		else
			for(int i = 0;i < items.size();i++)
			{
				Item item = items.get(i).Clone();
				
				Location new_loc = new Location(items.get(i).GetLocation().x + x_offset,items.get(i).GetLocation().y + y_offset,items.get(i).GetLocation().map);
				item.SetLocation(new_loc);
				
				if(!new_locs.contains(new_loc))
					new_locs.add(new_loc);
				
				map.RemoveItem(t,new_loc);
				map.AddItem(item,new_loc);
			}
		
		// We need to change our selection data since we moved them
		selected.clear();
		selected.addAll(new_locs);
		
		select_center.ptr = loc;
		return;
	}
}
