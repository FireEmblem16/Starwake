package item;

import engine.KeyBindings;
import grid.Location;
import grid.Map;
import image.animation.Animation;
import image.container.Pair;
import image.container.Pallet;
import item.map.Flatland;
import item.map.Forest;
import item.map.Pillar;
import item.map.RooflessWall;
import item.map.Wall;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import item.map.*;
import xml.*;

/**
 * Contains the basic information about on how to draw an item
 * and the basic information about what it means to be an item.
 */
public abstract class Item implements Observer
{
	/**
	 * Constructs and initializes the data of this item.
	 */
	public Item(Map m)
	{
		iskeylistener = false;
		loc = new Location(0,0,"");
		current_animation = null;
		animations = new ArrayList<Animation>();
		map = m;
		
		return;
	}
	
	/**
	 * Creates a new map item from a parser.
	 * Assumes the parser has just read in the <Item> tag.
	 * We take [mapname] as a parameter so that we do not need to alter [loc] when adding it to a map later.
	 * Some objects, like cursor, require a map to function.
	 */
	public static Item Create(Parser parser, String mapname, Map map, KeyBindings binds)
	{
		if(parser == null || !parser.HasNextTag())
			return null;
		
		// This tag should be a <class> tag
		Tag tag = parser.GetNext();
		
		if(!tag.GetName().equals("class"))
			return null;
		
		// Get the item we created in the tag's parsing function
		Item item = (Item)tag.GetValue();
		
		// Go ahead and add the location's map name
		item.SetParam("loc-map",mapname);
		
		// We need to set this and we don't want to give parser a lot of unnecessary information so just do this
		item.SetParam("map",map);
		
		// Some items need the game's key bindings to have their logic work
		item.SetParam("bindings",binds);
		
		// Loop through the <Item> definition and define all of it's values
		while(parser.HasNextTag())
		{
			tag = parser.GetNext();
			
			// If we found our closing tag then we finished loading this item
			if(tag.GetName().equals("/item"))
				break;
			
			// If we have a header we just ignore it, otherwise we attempt to change a variable
			if(tag.IsDescriptor())
				if(tag.GetName().equals("animation"))
					item.animations.add(new Animation((String)tag.GetValue(),item));
				else
					item.SetParam(tag.GetName(),tag.GetValue());
		}
		
		// Check that [loc] is well defined, we don't check the map name since it is garunteed to be defined
		Location l = item.GetLocation();
		if(l.x == 0 || l.y == 0)
			return null;
		
		// We need to observe all animations
		for(int i = 0;i < item.animations.size();i++)
			item.animations.get(i).addObserver(item);
		
		return item;
	}
	
	/**
	 * Creates a clone of this item. Do not use clone().
	 * Completely copies all variables that exist in this item.
	 */
	public Item Clone()
	{
		String classname = getClass().toString();
		classname = classname.substring(classname.lastIndexOf('.') + 1);
		
		Item clone = CreateNew(map,classname);
		
		if(loc != null)
			clone.loc = new Location(loc.x,loc.y,loc.map);
		else
			clone.loc = null;
		
		clone.iskeylistener = iskeylistener;
		
		if(this instanceof MapItem)
		{
			MapItem mthis = (MapItem)this;
			MapItem mclone = (MapItem)clone;
			
			mclone.SetPassable(mthis.IsPassable());
			mclone.SetFlyable(mthis.IsFlyable());
			mclone.SetSwimable(mthis.IsSwimable());
			mclone.SetClimbable(mthis.IsClimbable());
			
			mclone.SetCost(mthis.GetCost());
			mclone.SetAvoidBonus(mthis.GetAvoidBonus());
			mclone.SetDefenceBonus(mthis.GetDefenseBonus());
			mclone.SetLifeGain(mthis.GetLifeGain());
		}
		
		if(this instanceof Cursor)
		{
			Cursor mthis = (Cursor)this;
			Cursor mclone = (Cursor)clone;
			
			mclone.binds = mthis.binds;
		}
		
		if(this instanceof Node)
		{
			
		}
		
		if(this instanceof GameObject)
		{
			
		}
		
		clone.type = type;
		clone.pallet = pallet;
		clone.palletname = palletname;
		clone.map = map;
		clone.animation_name = animation_name;
		
		if(animations != null)
		{
			clone.animations = new ArrayList<Animation>();
			
			for(int i = 0;i < animations.size();i++)
				if(animations.get(i) == null)
					clone.animations.add(null);
				else
					clone.animations.add(new Animation(animations.get(i)));
		}
		else
			clone.animations = null;
		
		if(current_animation == null)
			clone.current_animation = null;
		else
			for(int i = 0;i < clone.animations.size();i++)
				if(clone.animations.get(i) != null && clone.animations.get(i).GetName().equals(current_animation.GetName()))
				{
					clone.current_animation = clone.animations.get(i);
					break;
				}
		
		return clone;
	}
	
	/**
	 * If [type] is not a Item type then null is returned. Otherwise returns a new
	 * object of the appropriate class.
	 * For example type = forest returns new Forest().
	 */
	public static Item CreateNew(Map map, String type)
	{
		if(type == null)
			return null;
		
		type = type.toLowerCase();
		
		if(type.equals("flatland"))
			return new Flatland(map);
		else if(type.equals("forest"))
			return new Forest(map);
		else if(type.equals("node"))
			return new Node(map);
		else if(type.equals("cursor"))
			return new Cursor(map);
		else if(type.equals("roofless wall"))
			return new RooflessWall(map);
		else if(type.equals("pillar"))
			return new Pillar(map);
		else if(type.equals("wall"))
			return new Wall(map);
		else if(type.equals("beach"))
			return new Beach(map);
		else if(type.equals("ocean"))
			return new Ocean(map);
		else if(type.equals("lake"))
			return new Lake(map);
		else if(type.equals("mountain"))
			return new Mountain(map);
		else if(type.equals("cliff"))
			return new Cliff(map);
		else if(type.equals("peak"))
			return new Peak(map);
		else if(type.equals("swamp"))
			return new Swamp(map);
		else if(type.equals("fort"))
			return new Fort(map);
		else if(type.equals("game object"))
			return new GameObject(map);
		
		return null;
	}
	
	/**
	 * Returns an array of all the names of items classes.
	 */
	public static String[] GetClassNames()
	{
		final String[] names = {"Beach","Cliff","Cursor","Flatland","Forest","Fort","Game Object","Lake","Mountain","Node","Ocean","Peak","Pillar","Roofless Wall","Swamp","Wall"};
		return names;
	}
	
	/**
	 * Returns the name of the given class as a String with proper spacing.
	 * Works for all classes but formating works only for Item classes.
	 * Returns null if [c] is null.
	 */
	public static String GetClassName(Class c)
	{
		if(c == null)
			return null;
		
		String classname = c.toString();
		classname = classname.substring(classname.lastIndexOf('.') + 1);
		
		String temp = classname.toLowerCase();
		
		if(temp.equals("gameobject"))
			classname = "Game Object";
		else if(temp.equals("rooflesswall"))
			classname = "Roofless Wall";
		
		return classname;
	}
	
	/**
	 * Sets the location of this item.
	 */
	public void SetLocation(Location l)
	{
		loc = l;
		return;
	}
	
	/**
	 * Returns the location of this item.
	 */
	public Location GetLocation()
	{
		return loc;
	}
	
	/**
	 * Sets the type of this object.
	 */
	public void SetType(Type t)
	{
		type = t;
		return;
	}
	
	/**
	 * Returns the type of this item.
	 */
	public Type GetType()
	{
		return type;
	}
	
	/**
	 * Sets the name of the pallet we will get our image from.
	 */
	public void SetPallet(String pname)
	{	
		pallet = pname;
		return;
	}
	
	/**
	 * Returns the name of the pallet we will get our image from.
	 */
	public String GetPallet()
	{
		return pallet;
	}
	
	/**
	 * Sets the name of the image as defined by the pallet that this item will want to draw.
	 */
	public void SetPalletName(String pname)
	{	
		palletname = pname;
		return;
	}
	
	/**
	 * Returns the pallet name of the image this item will want to draw.
	 */
	public String GetPalletName()
	{
		return palletname;
	}
	
	/**
	 * Returns the name of current animation or null if there is no animation running.
	 */
	public String GetCurrentAnimation()
	{
		if(current_animation == null)
			return null;
		
		return current_animation.GetName();
	}
	
	/**
	 * Returns the name of the initial animation.
	 */
	public String GetInitialAnimation()
	{
		return animation_name;
	}
	
	/**
	 * Returns the map this item is on.
	 */
	public Map GetMap()
	{
		return map;
	}
	
	/**
	 * Changes the current animation to one named [name] is possible.
	 * If [name] is null then animation will be turned off until changed again.
	 * If [name] is invalid then no change to the current animation is made.
	 * Returns true if the change was made successfully and false otherwise.
	 */
	public boolean ChangeAnimation(String name)
	{
		if(name == null)
		{
			if(current_animation != null)
				current_animation.Stop();
			
			current_animation = null;
			return true;
		}
		
		// If we are changing to the current animation we only need to restart it
		// If we treated it normally then we will double schedule the animtion in the sync clock
		if(current_animation != null && name.equals(current_animation.GetName()))
		{
			if(current_animation.Playing())
				current_animation.SetFrame(0);
			else
			{
				current_animation.SetFrame(0);
				current_animation.Start();
			}
			
			return true;
		}
		
		for(int i = 0;i < animations.size();i++)
			if(name.equals(animations.get(i).GetName()))
			{
				if(current_animation != null)
					current_animation.Stop();
				
				current_animation = animations.get(i);
				
				if(current_animation != null)
					current_animation.Start();
				
				return true;
			}
		
		return false;
	}
	
	/**
	 * Returns all of the animations in this item.
	 * The first item in the returned array, even if this item has no animations
	 * is always "No Animation".
	 */
	public String[] GetAnimationNames()
	{
		final String[] nullret = {"No Animation"};
		
		if(animations == null || animations.size() == 0)
			return nullret;
		
		String[] ret = new String[animations.size() + 1];
		ret[0] = "No Animation";
		
		for(int i = 0;i < animations.size();i++)
			ret[i + 1] = animations.get(i).GetName();
		
		return ret;
	}
	
	/**
	 * Returns all of the animations of this item.
	 */
	public ArrayList<Animation> GetAnimations()
	{
		return animations;
	}
	
	/**
	 * Restarts this animation to it's initial settings.
	 */
	public void RestartAnimation()
	{
		ChangeAnimation(animation_name);
		return;
	}
	
	/**
	 * Stops the animation of this object.
	 */
	public void StopAnimation()
	{
		ChangeAnimation(null);
		return;
	}
	
	/**
	 * Returns true if this item wants to listen for key input and false otherwise.
	 */
	public boolean IsKeyListener()
	{
		return iskeylistener;
	}
	
	/**
	 * Returns a list of locations that this object will overlap when painted.
	 */
	public ArrayList<Location> Overlaps(ArrayList<Pallet> pallets, int cellwidth, int cellheight, boolean ishex)
	{
		ArrayList<Location> ret = new ArrayList<Location>();
		
		// Check for critical existance failure
		if(cellwidth < 1 || cellheight < 1)
			return ret; // This makes no sense so paint nothing
		
		Pallet p = null;
		
		// Find where our image is supposed to be
		for(int i = 0;i < pallets.size() && p == null;i++)
			if(pallets.get(i).GetName().equals(pallet))
				p = pallets.get(i);
		
		// Check that we found a pallet
		if(p == null)
			return ret; // We can't draw anything so we paint nothing
		
		// Get our image
		BufferedImage img = p.GetEntry(palletname);
		
		// If we don't have an image to draw we can't do math so just return
		if(img == null)
			return ret; // Again, we can't paint anything so we paint nothing
		
		// Calculate the number of cells an image of this size will take up
		int w = 2 * (int)Math.floor((img.getWidth() - 1) / cellwidth) + 1;
		int h = 2 * (int)Math.floor((img.getHeight() - 1) / cellheight) + 1;
		
		// We have one less cell if we are only one pixel over due to integer truncation
		if(img.getWidth() % cellwidth == 1)
			w--;
		if(img.getHeight() % cellheight == 1)
			h--;
		
		// Add all locations we overlap
		for(int i = -(w / 2);i < (w + 1) / 2;i++)
			for(int j = -(h / 2);j < (h + 1) / 2;j++)
				ret.add(new Location(loc.x + i,loc.y + j,loc.map));
		
		return ret;
	}
	
	/**
	 * Returns true if this item's image will overlap location [l].
	 */
	public boolean Overlaps(Location l, ArrayList<Pallet> pallets, int cellwidth, int cellheight, boolean ishex)
	{
		// Check for critical existance failure
		if(cellwidth < 1 || cellheight < 1)
			return false; // This makes no sense so assume no
		
		Pallet p = null;
		
		// Find where our image is supposed to be
		for(int i = 0;i < pallets.size() && p == null;i++)
			if(pallets.get(i).GetName().equals(pallet))
				p = pallets.get(i);
		
		// Check that we found a pallet
		if(p == null)
			return false; // We can't draw anything so assume no
		
		// Get our image
		BufferedImage img = p.GetEntry(palletname);
		
		// If we don't have an image to draw we can't do math so just return
		if(img == null)
			return false; // Again, we can't paint anything so assume no
		
		// Calculate the number of cells an image of this size will take up
		int w = 2 * (int)Math.floor((img.getWidth() - 1) / cellwidth) + 1;
		int h = 2 * (int)Math.floor((img.getHeight() - 1) / cellheight) + 1;
		
		// We have one less cell if we are only one pixel over due to integer truncation
		if(img.getWidth() % cellwidth == 1)
			w--;
		if(img.getHeight() % cellheight == 1)
			h--;
		
		// Get the max and min x and y values
		int min_x = loc.x - (w / 2);
		int max_x = loc.x + (w + 1) / 2 - 1;
		int min_y = loc.y - (h / 2);
		int max_y = loc.y + (h + 1) / 2 - 1;
		
		// If we are in our bounds then we will overlap [l].
		if(l.x >= min_x && l.x <= max_x && l.y >= min_y && l.y <= max_y)
			return true;
		
		return false;
	}
	
	/**
	 * Paints this item wherever it is supposed to be drawn to.
	 */
	public void paint(Graphics2D g, int cellwidth, int cellheight, ArrayList<Pallet> pallets, boolean ishex)
	{
		// Check for critical existance failure
		if(cellwidth < 1 || cellheight < 1)
			return; // This makes no sense so do nothing
		
		Pallet p = null;
		
		for(int i = 0;i < pallets.size() && p == null;i++)
			if(pallets.get(i).GetName().equals(pallet))
				p = pallets.get(i);
		
		if(p == null)
		{
			g.clearRect((loc.x - 1) * cellwidth,(loc.y - 1) * cellheight,cellwidth,cellheight);
			return;
		}
		
		// Get our image
		BufferedImage img = p.GetEntry(palletname);
		
		// If we don't have an image to draw we can't do math so just return
		if(img == null)
		{
			erase(g,cellwidth,cellheight,ishex);
			return;
		}
		
		int x;
		int y;
		
		if(ishex)
		{
			// Hexes will need to be offseted so that they are tesselated
			// We will also still encounter the problem of drawing an extra pixel towards
			// the top-left on odd sizes due to integer limitations
			if((loc.y & 0x1) == 1)
				x = (loc.x - 1) * cellwidth + cellwidth / 2 - img.getWidth() / 2;
			else
				x = loc.x * cellwidth - img.getWidth() / 2;
			
			y = loc.y * cellheight / 2 - img.getHeight() / 2;
		}
		else
		{
			// When centering images we recognize that due to the limitation of integers,
			// odd sized dimensions will be drawn with an extra pixel towards the top-left
			x = (loc.x - 1) * cellwidth + cellwidth / 2 - img.getWidth() / 2;
			y = (loc.y - 1) * cellheight + cellheight / 2 - img.getHeight() / 2;
		}
			
		// If this is the first layer then we want to paint blankness in case our background doesn't fill the cell
		if(type == Type.GetNext(null))
			erase(g,cellwidth,cellheight,ishex);
		
		// PAINT!!!
		g.drawImage(p.GetEntry(palletname),null,x,y);
		return;
	}
	
	/**
	 * Creates a hexagon of the appropriate size for an eraser centered at (0,0).
	 */
	protected Polygon CreateHex(int w, int h)
	{
		int[] xpoints = {0,0,0,0,0,0};
		int[] ypoints = {0,0,0,0,0,0};
		
		return new Polygon(xpoints,ypoints,6);
	}
	
	/**
	 * Clears the cell this item occupies. Not to be used lightly.
	 */
	public void erase(Graphics2D g, int cellwidth, int cellheight, boolean ishex)
	{
		// Check for critical existance failure
		if(cellwidth < 1 || cellheight < 1)
			return; // This makes no sense so do nothing
		
		if(ishex)
		{
			int x;
			int y;
			
			if((loc.y & 0x1) == 1)
				x = (loc.x - 1) * cellwidth + cellwidth / 2;
			else
				x = loc.x * cellwidth;
			
			y = loc.y * cellheight / 2;
			
			Color c = g.getColor();
			g.setColor(g.getBackground());
			
			Polygon p = CreateHex(cellwidth,cellheight);
			p.translate(x,y);
			g.draw(p);
			
			g.setColor(c);
		}
		else
			g.clearRect((loc.x - 1) * cellwidth,(loc.y - 1) * cellheight,cellwidth,cellheight);
		
		return;
	}
	
	/**
	 * We watch all animations and when one changes something then we need to repaint this location.
	 * Expects a Pair<String> in [obj] which contains our last image.
	 */
	public void update(Observable O, Object obj)
	{
		// This should never be true but just in case check for it
		if(map == null || !(O instanceof Animation) || !(obj instanceof Pair))
			return;
		
		Pair p = (Pair)obj;
		
		if(!(p.t1 instanceof String && p.v instanceof Item))
			return;
		
		map.RequireRepaint((Pair<String,Item>)p);
		return;
	}
	
	/**
	 * Attempts to alter a parameter of this item as given by [param] to the value [val].
	 * [val] is not allowed to be null and param is assumed to be all lower case.
	 */
	public boolean SetParam(String param, Object val)
	{
		if(param == null)
			return false;
		
		if(param.equals("loc"))
		{
			if(val instanceof Location)
			{
				loc = (Location)val;
				return true;
			}
		}
		else if(param.equals("x"))
		{
			if(val instanceof Integer)
			{
				loc.x = (Integer)val;
				return true;
			}
		}
		else if(param.equals("y"))
		{
			if(val instanceof Integer)
			{
				loc.y = (Integer)val;
				return true;
			}
		}
		else if(param.equals("loc-map"))
		{
			if(val instanceof String)
			{
				loc.map = (String)val;
				return true;
			}
		}
		else if(param.equals("type"))
		{
			if(val instanceof Type)
			{
				type = (Type)val;
				
				map.RequireRepaint(loc);
				return true;
			}
		}
		else if(param.equals("pallet-name"))
		{
			if(val == null || val instanceof String)
			{
				palletname = (String)val;
				
				map.RequireRepaint(loc);
				return true;
			}
		}
		else if(param.equals("pallet"))
		{
			if(val == null || val instanceof String)
			{
				pallet = (String)val;
				
				map.RequireRepaint(loc);
				return true;
			}
		}
		else if(param.equals("animations"))
		{
			if(val == null || val instanceof ArrayList)
			{
				if(val != null && ((ArrayList)val).size() > 0)
				{
					if(((ArrayList)val).get(0) instanceof Animation)
					{
						// We don't want to observe outdated animations
						for(int i = 0;i < animations.size();i++)
							animations.get(i).deleteObserver(this);
						
						animations = (ArrayList<Animation>)val;
						ChangeAnimation(animation_name);
						
						// We want to observe all animations
						for(int i = 0;i < animations.size();i++)
							animations.get(i).addObserver(this);
						
						return true;
					}
				}
				else
				{
					// We don't want to observe outdated animations
					for(int i = 0;i < animations.size();i++)
						animations.get(i).deleteObserver(this);
					
					animations = new ArrayList<Animation>();
					return true;
				}
			}
		}
		else if(param.equals("animation-name"))
		{
			if(val == null || val instanceof String)
			{
				animation_name = (String)val;
				return true;
			}
		}
		else if(param.equals("current-animation"))
		{
			if(val == null || val instanceof Animation)
			{
				current_animation = (Animation)val;
				return true;
			}
		}
		else if(param.equals("map"))
		{
			if(val == null || val instanceof Map)
			{
				map = (Map)val;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Provides a nice debug view.
	 */
	public String toString()
	{
		return loc.toString() + ", Type: " + type.toString() + ", palletname: " + palletname;
	}
	
	/**
	 * Writes this item's xml data to the given file stream. To extend the functionality
	 * of this function use WriteExtraXML(BufferedWriter out).
	 */
	public final void WriteXML(FileWriter out) throws IOException
	{
		if(out == null)
			return;
		
		out.write("\t\t<Item>\n");
		
		out.write("\t\t\t<class = " + GetClassName(getClass()) + ">\n");
		out.write("\t\t\t<x = " + new Integer(loc.x).toString() + ">\n");
		out.write("\t\t\t<y = " + new Integer(loc.y).toString() + ">\n");
		
		if((animations == null || animations.size() == 0) && animation_name == null && current_animation == null)
		{
			out.write("\t\t\t<pallet = " + pallet + ">\n");
			out.write("\t\t\t<pallet-name = " + palletname + ">\n");
		}
		else
		{
			if(animations != null)
				for(int i = 0;i < animations.size();i++)
					out.write("\t\t\t<animation = " + animations.get(i).GetConfigName() + ">\n");
			
			if(animation_name != null)
				out.write("\t\t\t<animation-name = " + animation_name + ">\n");
		}
		
		WriteExtraXML(out);
		out.write("\t\t</Item>\n");
		
		return;
	}
	
	/**
	 * Writes children specific XML data.
	 */
	public abstract void WriteExtraXML(FileWriter out) throws IOException;
	
	/**
	 * This is true if this item wants to listen to keys.
	 */
	protected boolean iskeylistener;
	
	/**
	 * Contains the current location of this Item.
	 */
	protected Location loc;
	
	/**
	 * Contains what type this item is.
	 */
	protected Type type;
	
	/**
	 * This is the name of the pallet we want to get our picture from.
	 */
	protected String pallet;
	
	/**
	 * This is the name of the image in a pallet we will use to draw this item.
	 */
	protected String palletname;
	
	/**
	 * We need this for many things including animation updates.
	 */
	protected Map map;
	
	/**
	 * Contains the name of the animation that we want to initially run.
	 */
	protected String animation_name;
	
	/**
	 * Contains the animation we currently want to execute.
	 */
	protected Animation current_animation;
	
	/**
	 * Contains all of the animations of this item.
	 */
	protected ArrayList<Animation> animations;
}
