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
 * Draws items where selected until a bound is reached.
 */
public class FillTool extends Tool
{
	/**
	 * Initializes and constructs this object.
	 */
	public FillTool(ArrayList<Location> selected_locations, Pointer<Location> center, JComboBox type, JComboBox Class, JSpinner size, String image, ImageSelector img_selector)
	{
		super(selected_locations,center,type,Class,size,image);
		selector = img_selector;
		
		return;
	}

	/**
	 * Draws the image selected in [img_selector] around [loc] in [map] until boxed
	 * in by the edge of the map or images in all four cardinal directions. Size of
	 * the tool is irrelevant and always works as if it was one.
	 */
	public void DoAction(Map map, Location loc)
	{
		if(map == null || loc == null || Class == null || selected == null)
			return;
		
		Type t = GetLayer();
		MappedBufferedImage img = selector.GetSelection();
		selected.clear();
		select_center.ptr = null;
		
		if(t == null)
			if(IsAllLayers())
			{
				t = Type.GetNext(null);
				
				while(t != null)
				{
					ArrayList<Location> locs = new ArrayList<Location>();
					
					String[] imagedata = null;
					Item ITemp = map.GetItem(t,loc);
					
					if(ITemp != null)
					{
						imagedata = new String[2];
						
						imagedata[0] = map.GetItem(t,loc).GetPallet();
						imagedata[1] = map.GetItem(t,loc).GetPalletName();
					}
					
					GetFillArea(locs,map,loc,t,imagedata);
					
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
		{
			ArrayList<Location> locs = new ArrayList<Location>();
			String[] imagedata = {map.GetItem(t,loc).GetPallet(),map.GetItem(t,loc).GetPalletName()};
			GetFillArea(locs,map,loc,t,imagedata);
			
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
		}
		
		return;
	}
	
	/**
	 * Searches for the bounds of the area to fill. If [img] is to be used it must be of length two and
	 * the first value is the pallet's name and the second value is the key in the pallet.
	 */
	protected void GetFillArea(ArrayList<Location> locs, Map map, Location loc, Type t, String[] img)
	{
		if(map == null || loc == null || t == null)
			return;
		else if(img != null)
			if(img.length != 2 || img[0] == null || img[1] == null)
				return;
		
		if(loc.x < 1 || loc.x > map.GetWidth() || loc.y < 1 || loc.y > map.GetHeight())
			return;
		
		if(locs.contains(loc))
			return;
		
		Item item = map.GetItem(t,loc);
		
		// If we aren't trying to match images already given then we need only check if the space is empty
		if(img == null)
		{
			if(item == null)
				locs.add(loc);
			else
				return;
		}
		else
		{
			// We are given the image in the initial space if there is one so we don't need to switch if [item] is null because it's not if it's valid
			if(item != null && img[0].equals(item.GetPallet()) && img[1].equals(item.GetPalletName()))
				locs.add(loc);
			else
				return;
		}
		
		GetFillArea(locs,map,new Location(loc.x - 1,loc.y,loc.map),t,img);
		GetFillArea(locs,map,new Location(loc.x + 1,loc.y,loc.map),t,img);
		GetFillArea(locs,map,new Location(loc.x,loc.y - 1,loc.map),t,img);
		GetFillArea(locs,map,new Location(loc.x,loc.y + 1,loc.map),t,img);
		
		return;
	}
	
	/**
	 * Contains the image we want to draw.
	 */
	protected ImageSelector selector;
}
