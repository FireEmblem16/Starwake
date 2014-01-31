package gui.tool.tools;

import item.Type;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import engine.Pointer;
import grid.Location;
import grid.Map;

/**
 * Erase items where selected.
 */
public class EraseTool extends Tool
{
	/**
	 * Initializes and constructs this object.
	 */
	public EraseTool(ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image)
	{
		super(selected_locations,center,type,Class,size,image);
		return;
	}

	/**
	 * Erases everything around [loc] in [map] in the specified square size.
	 */
	public void DoAction(Map map, Location loc)
	{
		if(map == null || loc == null || Class == null || selected == null)
			return;
		
		int hw = GetSize();
		Type t = GetLayer();
		
		ArrayList<Location> locs = GetLocations(loc,hw);
		selected.clear();
		select_center.ptr = null;
		
		if(t == null)
			if(IsAllLayers())
			{
				t = Type.GetNext(null);
				
				while(t != null)
				{
					for(int i = 0;i < locs.size();i++)
						map.RemoveItem(t,locs.get(i));
					
					t = Type.GetNext(t);
				}
			}
			else
				return;
		else
			for(int i = 0;i < locs.size();i++)
				map.RemoveItem(t,locs.get(i));
		
		return;
	}
}
