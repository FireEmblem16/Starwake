import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		new Main(args);
		return;
	}
	
	public Main(String[] args) throws IOException
	{
		Scanner in = new Scanner(new File("tilesetin.tds"));
		FileWriter out = new FileWriter(new File("tilesetout.tds"));
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		Tile tile = new Tile();
		
		while(tile != null)
		{
			while(in.hasNext() && !"TILENAME".equals(in.next()));
			
			tile = ReadTile(in);
			
			if(tile != null)
				tiles.add(tile);
		}
		
		if(tiles.size() == 0)
			return;
		
		ArrayList<Tile> origins = new ArrayList<Tile>();
		ArrayList<Point> locs = new ArrayList<Point>();
		origins.add(tiles.get(0));
		locs.add(new Point(0,0));
		
		TileSpace space = new TileSpace(origins,locs);
		
		for(int i = 0;i < 100;i++)
			space.AddTile(tiles);
		
		return;
	}
	
	/**
	 * Requires TILENAME to have already been read in.
	 */
	public static Tile ReadTile(Scanner in)
	{
		Tile ret = new Tile();
		
		if(in.hasNext())
		{
			ret.name = in.next();
			in.next();
			ret.label = in.next();
			in.next();
			ret.nb = in.next();
			in.next();
			ret.eb = in.next();
			in.next();
			ret.sb = in.next();
			in.next();
			ret.wb = in.next();
			in.next();
			
			if(!ret.nb.equals("0"))
				ret.nl = in.next();
			
			in.next();

			if(!ret.eb.equals("0"))
				ret.el = in.next();
			
			in.next();

			if(!ret.sb.equals("0"))
				ret.sl = in.next();
			
			in.next();

			if(!ret.wb.equals("0"))
				ret.wl = in.next();
			
			if(!"CREATE".equals(in.next()))
			{
				ret.tilecolor = in.nextLine();
				
				if(!"CREATE".equals(in.next()))
					ret.textcolor = in.nextLine();
			}
		}
		else
			ret = null;
		
		return ret;
	}
	
	public static void WriteTile(FileWriter out, Tile t) throws IOException
	{
		if(out == null)
			return;
		
		out.write(t.toString());
		return;
	}
}
