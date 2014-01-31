import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TileSpace
{
	public TileSpace(ArrayList<Tile> origins, ArrayList<Point> locs)
	{
		tiles = new HashMap<Point,Tile>();
		frontier = new ArrayList<Point>();
		rand = new Random();
		
		if(origins.size() != locs.size())
			return;
		
		for(int i = 0;i < origins.size();i++)
		{
			tiles.put(locs.get(i),origins.get(i));
			UpdateFrontier(locs.get(i));
		}
		
		return;
	}
	
	public void AddTile(ArrayList<Tile> tileset)
	{
		if(frontier.size() == 0)
			return;
		
		Point next = frontier.remove(rand.nextInt(frontier.size()));
		ArrayList<Binding> psudotiles = GetPotentialBinds(next);
		ArrayList<Tile> potentialtiles = new ArrayList<Tile>();
		
		for(int i = 0;i < psudotiles.size();i++)
			potentialtiles.addAll(GetTilesWithBinding(tileset,psudotiles.get(i)));
		
		Tile newtile = potentialtiles.get(rand.nextInt(potentialtiles.size()));
		tiles.put(next,newtile);
		UpdateFrontier(next);
		
		return;
	}
	
	public ArrayList<Tile> GetTilesWithBinding(ArrayList<Tile> tileset, Binding b)
	{
		ArrayList<Tile> ret = new ArrayList<Tile>();
		
		for(int i = 0;i < tileset.size();i++)
			if(tileset.get(i).HasBinding(b))
				ret.add(tileset.get(i));
		
		return ret;
	}

	public ArrayList<Binding> GetPotentialBinds(Point p)
	{
		ArrayList<Binding> ret = new ArrayList<Binding>();
		Tile[] cross = new Tile[4];
		
		cross[0] = tiles.get(new Point(p.x - 1,p.y));
		cross[1] = tiles.get(new Point(p.x,p.y + 1));
		cross[2] = tiles.get(new Point(p.x + 1,p.y));
		cross[3] = tiles.get(new Point(p.x,p.y - 1));
		
		if(cross[0] != null)
		{
			if("2".equals(cross[0].eb))
				ret.add(new Binding(cross[0].el,"w",null,null));
			else if("1".equals(cross[0].eb))
				if(cross[1] != null && "1".equals(cross[1].sb))
					ret.add(new Binding(cross[0].el,"w",cross[1].sl,"n"));
				else if(cross[2] != null && "1".equals(cross[2].wb))
					ret.add(new Binding(cross[0].el,"w",cross[2].wl,"e"));
				else if(cross[3] != null && "1".equals(cross[3].nb))
					ret.add(new Binding(cross[0].el,"w",cross[3].nl,"s"));
		}
		
		if(cross[1] != null)
		{
			if("2".equals(cross[1].sb))
				ret.add(new Binding(cross[1].sl,"n",null,null));
			else if("1".equals(cross[1].sb))
				if(cross[2] != null && "1".equals(cross[2].wb))
					ret.add(new Binding(cross[1].sl,"n",cross[2].wl,"e"));
				else if(cross[3] != null && "1".equals(cross[3].nb))
					ret.add(new Binding(cross[1].sl,"n",cross[3].nl,"s"));
		}
		
		if(cross[2] != null)
		{
			if("2".equals(cross[2].wb))
				ret.add(new Binding(cross[2].wl,"e",null,null));
			else if("1".equals(cross[2].wb))
				if(cross[3] != null && "1".equals(cross[3].nb))
					ret.add(new Binding(cross[2].wl,"e",cross[3].nl,"s"));
		}
		
		if(cross[3] != null)
		{
			if("2".equals(cross[3].nb))
				ret.add(new Binding(cross[3].nl,"s",null,null));
		}
		
		return ret;
	}
	
	public void UpdateFrontier(Point p)
	{
		Tile[] box = new Tile[13];
		
		box[0] = tiles.get(new Point(p.x,p.y));
		
		if(box[0] == null)
			return;
		
		box[1] = tiles.get(new Point(p.x - 1,p.y - 1));
		box[2] = tiles.get(new Point(p.x - 1,p.y));
		box[3] = tiles.get(new Point(p.x - 1,p.y + 1));
		box[4] = tiles.get(new Point(p.x,p.y + 1));
		box[5] = tiles.get(new Point(p.x + 1,p.y + 1));
		box[6] = tiles.get(new Point(p.x + 1,p.y));
		box[7] = tiles.get(new Point(p.x + 1,p.y - 1));
		box[8] = tiles.get(new Point(p.x,p.y - 1));
		box[9] = tiles.get(new Point(p.x - 2,p.y));
		box[10] = tiles.get(new Point(p.x,p.y + 2));
		box[11] = tiles.get(new Point(p.x + 2,p.y));
		box[12] = tiles.get(new Point(p.x,p.y - 2));
		
		if(box[2] == null)
		{
			if("2".equals(box[0].wb) || "1".equals(box[0].wb) && (box[1] != null && "1".equals(box[1].nb) || box[3] != null && "1".equals(box[3].sb) || box[9] != null && "1".equals(box[9].eb)))
				if(!frontier.contains(new Point(p.x - 1,p.y)))
					frontier.add(new Point(p.x - 1,p.y));
		}
		
		if(box[4] == null)
		{
			if("2".equals(box[0].nb) || "1".equals(box[0].nb) && (box[3] != null && "1".equals(box[3].eb) || box[5] != null && "1".equals(box[5].wb) || box[10] != null && "1".equals(box[10].sb)))
				if(!frontier.contains(new Point(p.x,p.y + 1)))
					frontier.add(new Point(p.x,p.y + 1));
		}
		
		if(box[6] == null)
		{
			if("2".equals(box[0].eb) || "1".equals(box[0].eb) && (box[5] != null && "1".equals(box[5].sb) || box[7] != null && "1".equals(box[7].nb) || box[11] != null && "1".equals(box[11].wb)))
				if(!frontier.contains(new Point(p.x + 1,p.y)))
					frontier.add(new Point(p.x + 1,p.y));
		}
		
		if(box[8] == null)
		{
			if("2".equals(box[0].sb) || "1".equals(box[0].sb) && (box[7] != null && "1".equals(box[7].wb) || box[1] != null && "1".equals(box[1].eb) || box[12] != null && "1".equals(box[12].nb)))
				if(!frontier.contains(new Point(p.x,p.y - 1)))
					frontier.add(new Point(p.x,p.y - 1));
		}
		
		return;
	}
	
	public String toString()
	{
		String ret = "";
		
		Set<Point> setlocs = tiles.keySet();
		Point[] locs = new Point[setlocs.size()];
		locs = setlocs.toArray(locs);
		
		for(int i = 0;i < locs.length;i++)
			ret += locs[i].toString() + " " + tiles.get(locs[i]).name + "\n";
		
		return ret;
	}
	
	public ArrayList<Point> frontier;
	public HashMap<Point,Tile> tiles;
	public Random rand;
}
