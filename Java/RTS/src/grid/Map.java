package grid;

import image.container.Pair;
import image.container.Pallet;
import item.Item;
import item.Type;
import item.map.Flatland;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import engine.KeyBindings;
import xml.*;

/**
 * Contains information on background image data as well as what the
 * those background images represent, such as doors, houses and fences.
 * Maps are one indexed for locations.
 */
public class Map extends Observable
{
	/**
	 * Creates a map from the config file in [parser]. As maps are expected to be
	 * completely filled an array of the appropriate size is generated.
	 */
	public Map(Board parent, Parser parser, KeyBindings binds)
	{
		// We assume the worst and prove to be valid
		valid = false;
		
		// We keep this out here to avoid loosing a header
		Tag tag = null;
		
		// We need to keep this so we can fetch pallets and cell sizes when we otherwise can't obtain them
		board = parent;
		
		// Assume we aren't a hex map
		ishex = false;

		// Define default drawing specifications
		cellwidth = 0;
		cellheight = 0;
		
		// Get a new scope
		{
			boolean n = false;
			boolean w = false;
			boolean h = false;
			boolean end = false;
			
			// In a map we need the name of the map as well as the height and width for it to function at all
			while(parser.HasNextTag() && !end)
			{
				tag = parser.GetNext();
				
				// If we hit a header then we should be done
				if(tag.IsHeader())
					end = true;
				else if(tag.IsDescriptor())
				{
					if(tag.GetName().equals("name"))
					{
						name = (String)tag.GetValue();
						n = true;
					}
					else if(tag.GetName().equals("width"))
					{
						width = (Integer)tag.GetValue();
						w = true;
					}
					else if(tag.GetName().equals("height"))
					{
						height = (Integer)tag.GetValue();
						h = true;
					}
					else if(tag.GetName().equals("cell-width"))
						cellwidth = (Integer)tag.GetValue();
					else if(tag.GetName().equals("cell-height"))
						cellheight = (Integer)tag.GetValue();
					else if(tag.GetName().equals("hex"))
							ishex = (Boolean)tag.GetValue();
				}
			}
			
			// If we don't have n == w == h == true then we didn't load everything and we need to read until the current tag is /menu, then return
			if(!n || !w || !h)
			{
				while(!tag.GetName().equals("/menu"))
					tag = parser.GetNext();
				
				return;
			}
		}
		
		// Create the array for this map
		items = (ArrayList<Item>[][])new ArrayList[width][height];
		
		// Create all of the ArrayLists
		for(int i = 0;i < width;i++)
			for(int j = 0;j < height;j++)
				items[i][j] = new ArrayList<Item>();
		
		// Add anything we find to the map
		while(parser.HasNextTag())
		{
			if(tag.IsHeader())
			{
				if(tag.GetName().equals("/map"))
					break;
				else if(tag.GetName().equals("item"))
				{
					Item item = Item.Create(parser,name,this,binds);
					
					if(item != null && !ContainsTypeAt(item.GetType(),item.GetLocation()))
						AddItemNoNotify(item,item.GetLocation());
				}
				else
					;
			}
			else if(tag.IsDescriptor())
			{
				if(tag.GetName().equals("hex"))
					ishex = (Boolean)tag.GetValue();
				else if(tag.GetName().equals("cell-width"))
					cellwidth = (Integer)tag.GetValue();
				else if(tag.GetName().equals("cell-height"))
					cellheight = (Integer)tag.GetValue();
			}
			
			tag = parser.GetNext();
		}
		
		// Check that the map is well defined
		for(int i = 1;i <= width;i++)
			for(int j = 1;j <= height;j++)
				if(!ContainsTypeAt(Type.BACKGROUND,new Location(i,j,name)))
					return;
		
		// We cleared the validity test so mark this map as valid and return
		valid = true;
		return;
	}
	
	/**
	 * Constructs an empty map of height and width one.
	 */
	public Map(Board parent, String name)
	{
		width = 1;
		height = 1;
		cellwidth = 0;
		cellheight = 0;
		ishex = false;
		
		board = parent;
		items = (ArrayList<Item>[][])new ArrayList[width][height];
		
		// Create all of the ArrayLists
		for(int i = 0;i < width;i++)
			for(int j = 0;j < height;j++)
				items[i][j] = new ArrayList<Item>();
		
		this.name = name;
		valid = false;
		return;
	}
	
	/**
	 * Resizes this map. If the new size is smaller then old data will be dropped.
	 * Newly available spaces will not have anything initialized into them.
	 * If either the new width or height is invalid nothing will be changed.
	 * Returns true if the changes were made and false if a dimension was invalid or
	 * if we ran out of memory.
	 */
	public boolean Resize(int new_width, int new_height)
	{
		if(new_height < 1 || new_width < 1)
			return false;
		
		ArrayList<Item>[][] new_items;
		
		try
		{
			new_items = (ArrayList<Item>[][])new ArrayList[new_width][new_height];
		
			for(int i = 0;i < new_width;i++)
				for(int j = 0;j < new_height;j++)
				{
					ArrayList<Item> ITemp = GetItems(new Location(i + 1,j + 1,name));
					
					if(ITemp == null)
						new_items[i][j] = new ArrayList<Item>();
					else
						new_items[i][j] = ITemp;
				}
		}
		catch(OutOfMemoryError e)
		{return false;}
		
		width = new_width;
		height = new_height;
		items = new_items;
		
		board.UpdatePreferredSize();
		board.ForceRepaint();
		return true;
	}
	
	/**
	 * Adds an item to this map at the specified location if possible.
	 */
	public boolean AddItem(Item item, Location loc)
	{
		// Bounds check
		if(loc.x < 1 || loc.x > width || loc.y < 1 || loc.y > height)
			return false;
		
		// We can not have two of the same type of items in the same space
		if(ContainsTypeAt(item.GetType(),loc))
			return false;
		
		loc.map = name;
		item.SetLocation(loc);
		items[loc.x - 1][loc.y - 1].add(item);
		
		// We don't need to check what this will overlap because the item now exists so the image
		// will figure out what it overlaps without error
		setChanged();
		notifyObservers(loc);
		
		return true;
	}
	
	/**
	 * Adds an item to this map at the specified location if possible without
	 * notifying that a change in the map has occured.
	 */
	protected boolean AddItemNoNotify(Item item, Location loc)
	{
		// Bounds check
		if(loc.x < 1 || loc.x > width || loc.y < 1 || loc.y > height)
			return false;
		
		// We can not have two of the same type of items in the same space
		if(ContainsTypeAt(item.GetType(),loc))
			return false;
		
		loc.map = name;
		item.SetLocation(loc);
		items[loc.x - 1][loc.y - 1].add(item);
		
		return true;
	}
	
	/**
	 * Removes the item of type [t] from this map at [loc].
	 * Returns false if there is no item of type [t] to remove.
	 */
	public boolean RemoveItem(Type t, Location loc)
	{
		// Bounds check
		if(loc.x < 1 || loc.x > width || loc.y < 1 || loc.y > height)
			return false;
		
		ArrayList<Item> spot = items[loc.x - 1][loc.y - 1];
		
		for(int i = 0;i < spot.size();i++)
			if(spot.get(i).GetType() == t)
			{
				// We need to notify our observer of every location we overlap
				// We must do this since as this item no longer exists if won't clear locations it overlapped
				// Other items at this space will remain so we don't have to worry about their overlaps
				Item rfg = spot.remove(i);
				
				// The location of [rfg] could, and probably has changed
				Location Ltemp = rfg.GetLocation();
				rfg.SetLocation(loc);
				
				// Get all of [rfg]'s overlaps
				ArrayList<Location> newlocs = rfg.Overlaps(board.GetPallets(),board.GetCellWidth(),board.GetCellHeight(),ishex);
				
				// Change [rfg]'s location back
				rfg.SetLocation(Ltemp);
				
				// Notify observers of every location we overlap
				for(int j = 0;j < newlocs.size();j++)
				{
					setChanged();
					notifyObservers(newlocs.get(j));
				}
				
				return true;
			}
		
		return false;
	}
	
	/**
	 * Returns an item of type [t] from this map at [loc].
	 * Returns null if there is no such item.
	 */
	public Item GetItem(Type t, Location loc)
	{
		// Bounds check
		if(loc.x < 1 || loc.x > width || loc.y < 1 || loc.y > height)
			return null;
		
		ArrayList<Item> spot = items[loc.x - 1][loc.y - 1];
		
		for(int i = 0;i < spot.size();i++)
			if(spot.get(i).GetType() == t)
				return spot.get(i);
		
		return null;
	}
	
	/**
	 * Returns a list of all items at [loc] or null if [loc] is out of bounds.
	 */
	public ArrayList<Item> GetItems(Location loc)
	{
		// Bounds check
		if(loc.x < 1 || loc.x > width || loc.y < 1 || loc.y > height)
			return null;
		
		return items[loc.x - 1][loc.y - 1];
	}
	
	/**
	 * Returns true if there is an item of type [t] at [loc] on this map.
	 */
	public boolean ContainsTypeAt(Type t, Location loc)
	{
		// Bounds check
		if(loc.x < 1 || loc.x > width || loc.y < 1 || loc.y > height)
			return false;
		
		ArrayList<Item> spot = items[loc.x - 1][loc.y - 1];
		
		for(int i = 0;i < spot.size();i++)
			if(spot.get(i).GetType() == t)
				return true;
		
		return false;
	}
	
	/**
	 * Returns an item of type [t] in [spot].
	 * Returns null if there is no such item.
	 */
	public Item GetItem(Type t, ArrayList<Item> spot)
	{
		if(spot == null)
			return null;
		
		for(int i = 0;i < spot.size();i++)
			if(spot.get(i).GetType() == t)
				return spot.get(i);
		
		return null;
	}
	
	/**
	 * Restarts all animations of items in this map.
	 */
	public void RestartAnimation()
	{
		for(int i = 0;i < width;i++)
			for(int j = 0;j < height;j++)
			{
				ArrayList<Item> Items = items[i][j];
				
				for(int k = 0;k < Items.size();k++)
					Items.get(k).RestartAnimation();
			}
		
		return;
	}
	
	/**
	 * Halts all animations on this map.
	 */
	public void StopAnimation()
	{
		for(int i = 0;i < width;i++)
			for(int j = 0;j < height;j++)
			{
				ArrayList<Item> Items = items[i][j];
				
				for(int k = 0;k < Items.size();k++)
					Items.get(k).StopAnimation();
			}
		
		return;
	}
	
	/**
	 * Returns the width of this map.
	 */
	public int GetWidth()
	{
		return width;
	}
	
	/**
	 * Returns the height of this map.
	 */
	public int GetHeight()
	{
		return height;
	}
	
	/**
	 * Returns the width of the cells on this board.
	 */
	public int GetCellWidth()
	{
		return cellwidth;
	}
	
	/**
	 * Resizes the height and width of the cells.
	 */
	public void ResizeCell(int new_cellwidth, int new_cellheight)
	{
		if(new_cellheight < 0 || new_cellwidth < 0)
			return;
		
		cellwidth = new_cellwidth;
		cellheight = new_cellheight;
		return;
	}
	
	/**
	 * Returns the height of the cells on this board.
	 */
	public int GetCellHeight()
	{
		return cellheight;
	}
	
	/**
	 * Returns true if [spot] has an item of type [t] in it.
	 */
	protected boolean ContainsType(Type t, ArrayList<Item> spot)
	{
		if(spot == null)
			return false;
		
		for(int i = 0;i < spot.size();i++)
			if(spot.get(i).GetType() == t)
				return true;
		
		return false;
	}
	
	/**
	 * Returns an ArrayList of KeyListeners that are on this map.
	 * If there are none, an empty ArrayList is returned, not null.
	 */
	public ArrayList<KeyListener> GetKeyListeners()
	{
		ArrayList<KeyListener> ret = new ArrayList<KeyListener>();
		
		if(!IsValid())
			return ret;
		
		for(int i = 0;i < items.length;i++)
		{
			ArrayList<Item>[] stage1 = items[i];
			
			for(int j = 0;j < stage1.length;j++)
			{
				ArrayList<Item> stage2 = stage1[j];
				
				for(int k = 0;k < stage2.size();k++)
					if(stage2.get(k).IsKeyListener())
						ret.add((KeyListener)stage2.get(k));
			}
		}
		
		return ret;
	}
	
	/**
	 * Returns the name of this map.
	 */
	public String GetName()
	{
		return name;
	}
	
	/**
	 * Returns true if this map was properly constructed and false otherwise.
	 */
	public boolean IsValid()
	{
		return valid;
	}
	
	/**
	 * Sets this map to draw as a hex grid.
	 */
	public void SetHex(boolean b)
	{
		ishex = b;
		return;
	}
	
	/**
	 * Returns true if this map should be drawn as a hex grid.
	 */
	public boolean IsHex()
	{
		return ishex;
	}
	
	/**
	 * Orders the elements in [items] so that they are in the following order:
	 * BACKGROUND
	 * SPAWN
	 * CHARACTER
	 * TINT
	 * HIGHLIGHT
	 * ARROW
	 * NODE
	 * CURSOR
	 */
	protected void OrderElements(ArrayList<Item> items)
	{
		ArrayList<Item> newlist = new ArrayList<Item>();
		
		Item temp = null;
		
		temp = GetItem(Type.BACKGROUND,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.SPAWN,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.CHARACTER,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.TINT,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.HIGHLIGHT,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.ARROW,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.NODE,items);
		if(temp != null)
			newlist.add(temp);
		
		temp = GetItem(Type.CURSOR,items);
		if(temp != null)
			newlist.add(temp);
		
		items.clear();
		items.addAll(newlist);
		
		return;
	}
	
	/**
	 * Paints this board wherever it is supposed to be drawn to.
	 */
	public void paint(Graphics g, String dispmap, ArrayList<Pallet> pallets)
	{
		if(dispmap == null || !dispmap.equals(name))
			return;
		
		// Get the first layer we need to paint
		Type topaint = Type.GetNext(null);
		
		// We need to paint in layers since images can spill over their rectangle
		while(topaint != null)
		{
			for(int i = 1;i <= height;i++)
				for(int j = 1;j <= width;j++)
				{
					Item item = GetItem(topaint,new Location(j,i,""));
					
					if(item != null)
						item.paint((Graphics2D)g,cellwidth,cellheight,pallets,ishex);
					else if(topaint == Type.BACKGROUND)
					{
						Eraser.SetLocation(new Location(j,i,""));
						Eraser.erase((Graphics2D)g,cellwidth,cellheight,ishex);
					}
				}
			
			// Get the next layer
			topaint = Type.GetNext(topaint);
		}
		
		return;
	}
	
	/**
	 * Paints a specific part of this map.
	 * This function is less efficient than paint for tiny map sizes.
	 * However for small or greater sized maps, this function is vastly superior.
	 */
	public void PaintLocation(Graphics g, ArrayList<Location> where, ArrayList<Pallet> pallets)
	{
		// Null check
		if(where == null)
			return;
		
		// This will contain every modified location
		ArrayList<Location> locstopaint = new ArrayList<Location>();
		
		// These are off-map locations that need to be cleared
		ArrayList<Location> locstoclear = new ArrayList<Location>();
		
		// Remove any invalid locations
		for(int i = 0;i < where.size();i++)
		{
			// Test the map name
			if(where.get(i).map == null || !where.get(i).map.equals(name))
				continue;
			
			// Test the map bounds
			if(where.get(i).x < 1 || where.get(i).x > width || where.get(i).y < 1 || where.get(i).y > height)
			{
				locstoclear.add(where.get(i));
				continue;
			}
			
			// This location passed the tests so add it
			locstopaint.add(where.get(i));
		}
		
		// If we have nothing to do just ignore the rest of the function
		if(locstopaint.size() == 0)
			return;
		
		// This will contain the index of the next item we need to check for overlap
		int index = 0;
		
		// Check every item that we are overlapping
		while(index < locstopaint.size())
		{
			// Get the next location we need to check
			Location l = locstopaint.get(index++);
			
			// Get all the items at [l]
			ArrayList<Item> items = GetItems(l);
			
			// If we have null then [l] is out of bounds and we need not paint it
			if(items == null)
				continue;
			
			// Go through every item at [l] looking for overlaps
			for(int i = 0;i < items.size();i++)
			{
				// Figure out what the [i]th item overlaps
				ArrayList<Location> newlocs = items.get(i).Overlaps(pallets,cellwidth,cellheight,ishex);
				
				// Add all locations we aren't already painting
				for(int j = 0;j < newlocs.size();j++)
					if(!locstopaint.contains(newlocs.get(j)))
						locstopaint.add(newlocs.get(j));
			}
		}
		
		// We need to see what we are overlapped by so we can redraw it as well
		for(int j = 1;j <= height;j++)
			for(int i = 1;i <= width;i++)
			{
				// Get items at (i,j)
				ArrayList<Item> items = GetItems(new Location(i,j,name));
				
				// This should never happen but maybe specs will change
				if(items == null)
					continue;
				
				// If we have no items, not likely, or we are already painting (i,j) then who cares about this space's tests 
				if(items.size() == 0 || locstopaint.contains(items.get(0).GetLocation()))
					continue;
				
				// Check each item to see if we are already redrawing it
				for(int k = 0;k < items.size();k++)
					for(int l = 0;l < locstopaint.size();l++) // Check each place we are drawing to see if if is overlapped by anything
						if(items.get(k).Overlaps(locstopaint.get(l),pallets,cellwidth,cellheight,ishex))
						{
							// If we need to redraw this space at all then we redraw all of it and don't bother checking the rest
							locstopaint.add(items.get(k).GetLocation());
							
							// Now that we know we need to redraw this we need to redraw everything that it overlaps
							ArrayList<Location> locs = items.get(k).Overlaps(pallets,cellwidth,cellheight,ishex);
							
							// Add all new locations
							for(int n = 0;n < locs.size();n++)
								if(!locstopaint.contains(locs.get(n)))
									locstopaint.add(locs.get(n));
							
							// It is possible that items in [locs] overlap other items so we need to summon recursion
							// We don't let this function continue painting everything we've already found since we could
							// potentially find it again in the recursive call 
							PaintLocation(g,locstopaint,pallets);
							return;
						}
			}
		
		// Get a sorted list of locations to paint
		Object[] locs = locstopaint.toArray();
		Arrays.sort(locs);
		
		// Get the first layer we need to paint
		Type topaint = Type.GetNext(null);
		
		// We need to paint in layers since images can spill over their rectangle
		while(topaint != null)
		{
			// Paint every location that needs to be in the current layer
			for(int i = 0;i < locs.length;i++)
			{
				Item item = GetItem(topaint,(Location)locs[i]);
				
				if(item != null)
					item.paint((Graphics2D)g,cellwidth,cellheight,pallets,ishex);
				else if(topaint == Type.BACKGROUND)
				{
					Eraser.SetLocation((Location)locs[i]);
					Eraser.erase((Graphics2D)g,cellwidth,cellheight,ishex);
				}
			}
			
			// Get the next layer
			topaint = Type.GetNext(topaint);
		}
		
		// If we have any off-map problems take care of them
		for(int i = 0;i < locstoclear.size();i++)
		{
			Eraser.SetLocation(locstoclear.get(i));
			Eraser.erase((Graphics2D)g,cellwidth,cellheight,ishex);
		}
			
		return;
	}
	
	/**
	 * Adds [l] to the list of locations that need to be repainted at the next paint call.
	 */
	public void RequireRepaint(Location l)
	{
		setChanged();
		notifyObservers(l);
		
		return;
	}
	
	/**
	 * Adds the location [oldimg.v] is at to the list of locations that need repainting and
	 * repaints as if that item had the pallet in [oldimg.t1] and had the palletname in [oldimg.t2].
	 */
	public void RequireRepaint(Pair<String,Item> oldimg)
	{
		setChanged();
		notifyObservers(oldimg);
		
		return;
	}
	
	/**
	 * Writes this item's xml data to the given file stream.
	 */
	public void WriteXML(FileWriter out) throws IOException
	{
		if(out == null)
			return;
		
		out.write("\t<Map>\n");
		
		out.write("\t\t<name = " + name + ">\n");
		out.write("\t\t<width = " + new Integer(width).toString() + ">\n");
		out.write("\t\t<height = " + new Integer(height).toString() + ">\n\n");

		out.write("\t\t<cell-width = " + new Integer(cellwidth).toString() + ">\n");
		out.write("\t\t<cell-height = " + new Integer(cellheight).toString() + ">\n");
		out.write("\t\t<hex = " + new Boolean(ishex).toString() + ">\n\n");
		
		for(int i = 0;i < width;i++)
			for(int j = 0;j < height;j++)
			{
				ArrayList<Item> spot = items[i][j];
				
				for(int k = 0;k < spot.size();k++)
				{
					spot.get(k).WriteXML(out);
					out.write("\n");
				}
			}
		
		out.write("\t</Map>\n");
		return;
	}
	
	/**
	 * False if this Map was not constructed properly.
	 */
	protected boolean valid;
	
	/**
	 * Contains the width of this map.
	 */
	protected int width;
	
	/**
	 * Contains the height of this map.
	 */
	protected int height;
	
	/**
	 * The width that a cell should be when being drawn.
	 */
	protected int cellwidth;
	
	/**
	 * The height that a cell should be when being drawn.
	 */
	protected int cellheight;
	
	/**
	 * This is true if we want the map to be drawn as a hex map.
	 */
	protected boolean ishex;
	
	/**
	 * Contians the name of this map.
	 */
	protected String name;
	
	/**
	 * This contains the board this map is attached to.
	 */
	protected Board board;
	
	/**
	 * This will contains all of our images to be drawn and their data.
	 */
	protected ArrayList<Item>[][] items;
	
	/**
	 * We use this as an eraser for all maps. All it does is hold a location and have the erase function.
	 */
	protected static Item Eraser = new Flatland(null);
}
