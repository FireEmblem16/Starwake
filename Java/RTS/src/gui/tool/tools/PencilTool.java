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
 * Draws items where selected.
 */
public class PencilTool extends Tool
{
	/**
	 * Initializes and constructs this object.
	 */
	public PencilTool(ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image, ImageSelector img_selector)
	{
		super(selected_locations,center,type,Class,size,image);
		selector = img_selector;
		
		return;
	}

	/**
	 * Draws the image selected in [img_selector] around [loc] in [map] in the specified square size.
	 */
	public void DoAction(Map map, Location loc)
	{
		if(map == null || loc == null || Class == null || selected == null)
			return;
		
		int hw = GetSize();
		Type t = GetLayer();
		
		MappedBufferedImage img = selector.GetSelection();
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
					{
						Item item = GetInstanceOfSelectedClass(map);
						item.SetType(t);
						
						Pallet p = selector.GetSelectedPallet();
						
						if(p == null)
							item.SetPallet("");
						else
							item.SetPallet(p.GetName());
						
						if(img == null)
							item.SetPalletName("");
						else
							item.SetPalletName(img.GetName());
						
						if(map.ContainsTypeAt(t,locs.get(i)))
							map.RemoveItem(t,locs.get(i));
						
						map.AddItem(item,locs.get(i));
					}
					
					t = Type.GetNext(t);
				}
			}
			else
				return;
		else
			for(int i = 0;i < locs.size();i++)
			{
				Item item = GetInstanceOfSelectedClass(map);
				item.SetType(t);
				
				Pallet p = selector.GetSelectedPallet();
				
				if(p == null)
					item.SetPallet("");
				else
					item.SetPallet(p.GetName());
				
				if(img == null)
					item.SetPalletName("");
				else
					item.SetPalletName(img.GetName());
				
				if(map.ContainsTypeAt(t,locs.get(i)))
					map.RemoveItem(t,locs.get(i));
				
				map.AddItem(item,locs.get(i));
			}
		
		return;
	}
	
	/**
	 * Contains the image we want to draw.
	 */
	protected ImageSelector selector;
}
