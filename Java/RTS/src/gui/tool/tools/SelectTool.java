package gui.tool.tools;

import item.Item;
import item.Type;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import engine.Pointer;
import grid.Location;
import grid.Map;
import gui.datadisplay.DataDisplay;

/**
 * Selects items.
 */
public class SelectTool extends Tool
{
	/**
	 * Initializes and constructs this object.
	 */
	public SelectTool(DataDisplay dataview, ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image)
	{
		super(selected_locations,center,type,Class,size,image);
		dataviewer = dataview;
		
		return;
	}

	/**
	 * Displays all data around the selected region.
	 */
	public void DoAction(Map map, Location loc)
	{
		if(map == null || loc == null || Class == null || selected == null)
			return;
		
		int hw = GetSize();
		Type t = GetLayer();
		
		ArrayList<Location> locs = GetLocations(loc,hw);
		ArrayList<Item> items = new ArrayList<Item>();
		
		selected.clear();
		select_center.ptr = loc;
		
		for(int i = 0;i < locs.size();i++)
		{
			ArrayList<Item> newitems = map.GetItems(locs.get(i));
			
			if(newitems != null)
				for(int j = 0;j < newitems.size();j++)
				{
					selected.add(locs.get(i));
					
					if(newitems.get(j) != null)
						items.add(newitems.get(j));
				}
		}
		
		dataviewer.Display(items);
		return;
	}
	
	/**
	 * This is where we want to display the data we have selected.
	 */
	protected DataDisplay dataviewer;
}
