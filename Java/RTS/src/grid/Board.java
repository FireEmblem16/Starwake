package grid;

import gui.editor.EditorScrollbarUpdater;
import image.container.Pair;
import image.container.Pallet;
import item.Item;
import item.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import statics.FileFunctions;
import engine.KeyBindings;
import xml.*;

/**
 * Contains a set of map and item data that will be drawn as the board.
 * The elements each contain information as to what actions can be taken on
 * them and the data of an item such as character stats.
 * Boards are one indexed for locations.
 * Any KeyListeners that this Board has may be obtained through the use of the
 * function getKeyListeners and should be added to a JFrame or some such thing
 * in order for them to actually function.
 */
public class Board extends JPanel implements Observer
{	
	/**
	 * Generates a board of width [w] and height [h]. Loads a configuration file
	 * called [config] in order to construct 
	 */
	public Board(String config, KeyBindings binds, EditorScrollbarUpdater scrollsupdate, boolean accept_incomplete_maps)
	{
		forcerepaint = false;
		this.config = config;
		
		update_scrollbars = scrollsupdate;
		
		if(update_scrollbars != null)
			update_scrollbars.SetBoard(this);
		
		if(config == null)
			throw new NullPointerException("A configuration file must be passed to all boards.");
		
		map = new ArrayList<Map>();
		pallets = new ArrayList<Pallet>();
		goal = new Goal();
		name = "";
		
		// For some reason there seems to be some asynchronous stuff happening so do this
		if(paintat == null)
			paintat = new ArrayList<Location>();
		
		Parser parser = new Parser(config);
		
		while(parser.HasNextTag())
		{
			Tag tag = parser.GetNext();
			
			if(tag.IsHeader())
			{
				if(tag.GetName().equals("map"))
				{
					Map m = new Map(this,parser,binds);
					
					if(accept_incomplete_maps || m.IsValid())
					{
						m.addObserver(this);
						map.add(m);
						
						// Add all key listeners in [m]
						ArrayList<KeyListener> listeners = m.GetKeyListeners();
						
						for(int i = 0;i < listeners.size();i++)
							addKeyListener(listeners.get(i));
					}
				}
				else
					continue;
			}
			else if(tag.IsDescriptor())
			{
				if(tag.GetName().equals("name"))
					name = (String)tag.GetValue();
				else if(tag.GetName().equals("goal"))
					goal = (Goal)tag.GetValue();
				else if(tag.GetName().equals("pallet-path"))
					pallets.add(new Pallet((String)tag.GetValue()));
				else if(tag.GetName().equals("display-map"))
					dispmap = (String)tag.GetValue();
			}
		}
		
		UpdatePreferredSize();
		ForceRepaint();
		return;
	}
	
	/**
	 * Creates a board with no real data in it.
	 */
	public Board(File path, EditorScrollbarUpdater update_scrolls)
	{
		if(path == null)
			throw new NullPointerException("A configuration file must be passed to all boards.");
		
		forcerepaint = false;
		name = "Behold My Default Name!!!";
		goal = new Goal();
		map = new ArrayList<Map>();
		dispmap = null;
		pallets = new ArrayList<Pallet>();
		paintat = null;
		update_scrollbars = update_scrolls;
		config = FileFunctions.relative(new File("."),path);
		
		if(update_scrollbars != null)
			update_scrollbars.SetBoard(this);
		
		UpdatePreferredSize();
		return;
	}
	
	/**
	 * Updates the size of this component.
	 */
	public void UpdatePreferredSize()
	{
		Map m = GetDisplayedMap();
		
		if(m != null)
			setPreferredSize(new Dimension(m.GetCellWidth() * m.GetWidth(),m.GetCellHeight() * m.GetHeight()));
		else
			setPreferredSize(new Dimension(0,0));
		
		if(update_scrollbars != null)
			update_scrollbars.update();
		
		return;
	}
	
	/**
	 * Resizes the height and width of the cells of map [m].
	 */
	public void ResizeCell(String m, int new_cellwidth, int new_cellheight)
	{
		if(m == null || new_cellheight < 0 || new_cellwidth < 0)
			return;
		
		Map mp = GetMap(m);
		
		if(mp != null)
		{
			mp.ResizeCell(new_cellwidth,new_cellheight);
			UpdatePreferredSize();
			ForceRepaint();
		}
		
		return;
	}
	
	/**
	 * Returns the map that is currently being displayed.
	 */
	public Map GetDisplayedMap()
	{
		if(dispmap == null)
			return null;
		
		for(int i = 0;i < map.size();i++)
			if(dispmap.equals(map.get(i).GetName()))
				return map.get(i);
		
		return null;
	}
	
	/**
	 * Returns the map called [m] or null if [m] does not exist.
	 */
	public Map GetMap(String m)
	{
		if(m == null)
			return null;
		
		for(int i = 0;i < map.size();i++)
			if(m.equals(map.get(i).GetName()))
				return map.get(i);
		
		return null;
	}
	
	/**
	 * Returns the name of the map currently being displayed or null if there is no such map.
	 */
	public String GetDisplayedMapName()
	{
		return dispmap;
	}
	
	/**
	 * Returns true if there exists a map named [m] and we were able to switch to it
	 * and returns false otherwise.
	 */
	public boolean ChangeDisplayedMap(String m, boolean accept_incomplete_maps)
	{
		if(m != null && m.equals(dispmap))
			return true;
		
		if(map == null || m == null)
		{
			Map mm = GetDisplayedMap();
			
			if(mm != null)
				mm.StopAnimation();
			
			dispmap = null;
			
			UpdatePreferredSize();
			ForceRepaint();
			return true;
		}
		
		for(int i = 0;i < map.size();i++)
			if(m.equals(map.get(i).GetName()))
				if(accept_incomplete_maps || map.get(i).IsValid())
				{
					Map mm = GetDisplayedMap();
					
					if(mm != null)
						mm.StopAnimation();
					
					dispmap = m;
					GetDisplayedMap().RestartAnimation();
					
					UpdatePreferredSize();
					ForceRepaint();
					return true;
				}
		
		return false;
	}
	
	/**
	 * Returns the names of all maps in this board. If this board has
	 * no maps then it returns an empty array list.
	 */
	public ArrayList<String> GetMapNames()
	{
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i = 0;i < map.size();i++)
			ret.add(map.get(i).GetName());
		
		return ret;
	}
	
	/**
	 * Adds a map to this board. Returns true if the map was added
	 * and returns false if some condition for adding the map was false.
	 */
	public boolean AddMap(Map m)
	{
		if(m == null || m.GetName() == null || m.GetName().equals(""))
			return false;
		
		boolean exists = false;
		
		for(int i = 0;i < map.size();i++)
			if(map.get(i).GetName().equals(m.GetName()))
			{
				exists = true;
				break;
			}
		
		if(exists)
			return false;
		
		m.addObserver(this);
		map.add(m);
		
		// Add all key listeners in [m]
		ArrayList<KeyListener> listeners = m.GetKeyListeners();
		
		for(int i = 0;i < listeners.size();i++)
			addKeyListener(listeners.get(i));
		
		return true;
	}
	
	/**
	 * Removes the indicated map. Returns false if it is unable to.
	 */
	public boolean RemoveMap(String m)
	{
		if(m == null)
			return false;
		
		for(int i = 0;i < map.size();i++)
			if(map.get(i).GetName().equals(m))
			{
				Map mm = map.remove(i);
				
				if(m.equals(dispmap))
				{
					dispmap = null;
					
					UpdatePreferredSize();
					ForceRepaint();
				}
				
				mm.deleteObserver(this);
				
				// Add all key listeners in [m]
				ArrayList<KeyListener> listeners = mm.GetKeyListeners();
				
				for(int j = 0;j < listeners.size();j++)
					removeKeyListener(listeners.get(j));
				
				return true;
			}
		
		return false;
	}
	
	/**
	 * Returns a list of pallets on this board.
	 */
	public ArrayList<Pallet> GetPallets()
	{
		return pallets;
	}
	
	/**
	 * Returns the width of the cells on this board.
	 */
	public int GetCellWidth()
	{
		Map m = GetDisplayedMap();
		
		if(m == null)
			return 0;
		
		return m.GetCellWidth();
	}
	
	/**
	 * Returns the height of the cells on this board.
	 */
	public int GetCellHeight()
	{
		Map m = GetDisplayedMap();
		
		if(m == null)
			return 0;
		
		return m.GetCellHeight();
	}
	
	/**
	 * Returns the name of this board.
	 */
	public String GetName()
	{
		return name;
	}
	
	/**
	 * Returns the path to this board's config file.
	 */
	public String GetConfigName()
	{
		return config;
	}
	
	/**
	 * Paints this board wherever it is supposed to be drawn to.
	 */
	public void paint(Graphics g)
	{
		// For some reason there seems to be some asynchronous stuff happening so check for null
		if(forcerepaint)
		{
			// If we're forcing the repaint then clear everything to ensure perfect repainting.
			super.paint(g);
			
			// Now that we called the super paint function we should check if we have anything to do at all.
			if(dispmap == null)
				return;
			
			// If we are forcing a repaint we want to restart all of our animations to make sure everything happens properly
			Map m = GetDisplayedMap();
			
			if(m != null)
				m.RestartAnimation();
			
			forcerepaint = false;
			
			for(int i = 0;i < map.size();i++)
				map.get(i).paint(g,dispmap,pallets);
		}
		else if(dispmap == null)
			return;
		else if(paintat == null || paintat.size() == 0)
			for(int i = 0;i < map.size();i++)
				map.get(i).paint(g,dispmap,pallets);
		else
		{
			for(int i = 0;i < map.size();i++)
				map.get(i).PaintLocation(g,paintat,pallets);
			
			paintat.clear();
		}
		
		paintat = null;
		return;
	}
	
	/**
	 * We will need to repaint the location [l] if this function is called.
	 * If we are notified of the same location more than once we ignore the later
	 * notifications since they will already be painted.
	 */
	public void update(Observable obj, Object l)
	{
		if(l == null || !(l instanceof Location || l instanceof Pair))
			return;
		
		// For some reason there seems to be some asynchronous stuff happening so do this
		if(paintat == null)
			paintat = new ArrayList<Location>();
		
		// If the first check passes then we know we have a Location
		if(l instanceof Location && !paintat.contains(l))
			paintat.add((Location)l); // If we aren't already supposed to redraw this then do so
		else if(l instanceof Pair)
		{
			Pair p = (Pair)l;
			
			// [p] must be of the proper format or we have nothing to do
			if(!(p.t1 instanceof String && p.v instanceof Item))
				return;
			
			// CAST!!!
			Pair<String,Item> pd = (Pair<String,Item>)p;
			
			// Save our new image names
			String oldpallet = pd.v.GetPallet();
			String oldpalletname = pd.v.GetPalletName();
			
			// Return our old image names to the item for the Overlaps function
			pd.v.SetPallet(pd.t1);
			pd.v.SetPalletName(pd.t2);
			
			// We need to figure out what name we are drawing
			Map m = GetMap(pd.v.GetLocation().map);
			
			// Get all locations that were overlapped, note that the item's location in question is repainted so we will get the new image's overlaps as well
			ArrayList<Location> locstoadd = null;
			
			if(m == null)
				locstoadd = new ArrayList<Location>(); // If we don't have a map then we logically don't overlap anything
			else
				locstoadd = pd.v.Overlaps(pallets,m.GetCellWidth(),m.GetCellHeight(),m.IsHex());
			
			// Return our new image names to the item
			pd.v.SetPallet(oldpallet);
			pd.v.SetPalletName(oldpalletname);
			
			// Add each new location to [paintat]
			for(int i = 0;i < locstoadd.size();i++)
				if(!paintat.contains(locstoadd.get(i)))
					paintat.add(locstoadd.get(i));
		}
		
		repaint();
		return;
	}
	
	/**
	 * Forces this board to repaint everything.
	 */
	public void ForceRepaint()
	{
		forcerepaint = true;
		
		repaint();
		return;
	}
	
	/**
	 * Writes this item's xml data to the given file stream. Extra spacing and new lines are
	 * added to the file in order to make it more readable in the event of manual editing.
	 */
	public void WriteXML(FileWriter out) throws IOException
	{
		if(out == null)
			return;
		
		out.write("<Board>\n");
		
		out.write("\t<name = " + name + ">\n");
		out.write("\t<goal = " + goal.toString() + ">\n");
		out.write("\t<display-map = " + dispmap + ">\n\n");
		
		for(int i = 0;i < pallets.size();i++)
			out.write("\t<pallet-path = " + pallets.get(i).GetConfigPath() + ">\n");
		
		out.write("\n");
		
		for(int i = 0;i < map.size();i++)
		{
			map.get(i).WriteXML(out);
			out.write("\n");
		}
		
		out.write("</Board>");
		return;
	}
	
	/**
	 * Returns the goal of this board.
	 */
	public Goal GetGoal()
	{
		return goal;
	}
	
	/**
	 * This is true if we want to force the entire board to be repainted.
	 */
	protected boolean forcerepaint;
	
	/**
	 * Contains the name of the board, probably the same as the title of the chapter.
	 */
	protected String name;
	
	/**
	 * Contains the goal of this board.
	 */
	protected Goal goal;
	
	/**
	 * Contains all of the information on the maps of this board.
	 */
	protected ArrayList<Map> map;
	
	/**
	 * This is the name of the map we want to display.
	 */
	protected String dispmap;
	
	/**
	 * Contains all of the pallets of this board.
	 */
	protected ArrayList<Pallet> pallets;
	
	/**
	 * If this is not null then we are supposed to paint only this location on a call to paint.
	 */
	protected volatile ArrayList<Location> paintat;
	
	/**
	 * When we resize somehow we need to call update in this.
	 */
	protected EditorScrollbarUpdater update_scrollbars;
	
	/**
	 * Contains the path to this board's config file.
	 */
	protected String config;
}
