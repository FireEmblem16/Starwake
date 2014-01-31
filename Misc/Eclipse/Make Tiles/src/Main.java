import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
TILENAME New Tile(2)
LABEL 
NORTHBIND 0
EASTBIND 0
SOUTHBIND 0
WESTBIND 0
NORTHLABEL 
EASTLABEL 
SOUTHLABEL 
WESTLABEL 
CREATE



*/

public class Main
{
	public static void main(String[] args) throws IOException
	{
		String loc = "L:\\TAS\\";
		String read = "BC9g.tds";
		String write = "BC9h.tds";
		
		Scanner in = new Scanner(new File(loc + read));
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		ArrayList<Tile> rtiles = new ArrayList<Tile>();
		Tile tile = new Tile();
		
		while(tile != null)
		{
			while(in.hasNext() && !"TILENAME".equals(in.next()));
			
			tile = ReadTile(in);
			
			if(tile != null)
				tiles.add(tile);
		}
		
		/*int start_index = -1;
		
		for(int i = 0;i < tiles.size();i++)
			if(tiles.get(i).name.equals("two_45"))
			{
				start_index = i;
				break;
			}*/
		
		for(int i = /*start_index*/0;i < tiles.size();i++)
			if(!tiles.get(i).label.equals("O") && !tiles.get(i).label.equals("D") && !tiles.get(i).name.contains("_REF") && !tiles.contains(tiles.get(i).name + "_REF"))
				rtiles.add(tiles.get(i).REFLECT());
			else if(tiles.get(i).name.contains("_REF") && !tiles.get(i).label.equals("DR"))
				tiles.remove(i--);
		
		FileWriter out = new FileWriter(new File(loc + write));
		
		for(int i = 0;i < tiles.size();i++)
			out.write(tiles.get(i).toString());
		
		for(int i = 0;i < rtiles.size();i++)
			out.write(rtiles.get(i).toString());
		
		out.close();
		return;
	}
	
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
	
	public static class Tile
	{
		public Tile()
		{
			name = label = nb = eb = sb = wb = nl = el = sl = wl = tilecolor = textcolor = null;
			return;
		}
		
		public Tile REFLECT()
		{
			Tile ret = new Tile();
			
			ret.name = name + "_REF";
			ret.label = label + "R";
			ret.nb = eb;
			ret.eb = nb;
			ret.sb = wb;
			ret.wb = sb;
			ret.nl = el;
			ret.el = nl;
			ret.sl = wl;
			ret.wl = sl;
			ret.nl = (el == null ? null : (el.contains("_REF") ? el.replace("_REF","") : (el +  "_REF")));
			ret.el = (nl == null ? null : (nl.contains("_REF") ? nl.replace("_REF","") : (nl +  "_REF")));
			ret.sl = (wl == null ? null : (wl.contains("_REF") ? wl.replace("_REF","") : (wl +  "_REF")));
			ret.wl = (sl == null ? null : (sl.contains("_REF") ? sl.replace("_REF","") : (sl +  "_REF")));
			ret.tilecolor = tilecolor;
			ret.textcolor = textcolor;
			
			/*if(ret.nl != null)
			{
				if("r_pulse_22_REF".equals(ret.name))
				{
					int a = 5;
					a = 4;
				}
				
				if(ret.nl.equals("d"))
					ret.nl = "l";
				else if(ret.nl.equals("l"))
					ret.nl = "d";
				else if(ret.nl.equals("r"))
					ret.nl = "u";
				else if(ret.nl.equals("u"))
					ret.nl = "r";
				else if(ret.nl.contains("d_"))
					ret.nl = ret.nl.replace("d_","l_");
				else if(ret.nl.contains("l_"))
					ret.nl = ret.nl.replace("l_","d_");
				else if(ret.nl.contains("u_"))
					ret.nl = ret.nl.replace("u_","r_");
				else if(ret.nl.contains("r_"))
					ret.nl = ret.nl.replace("r_","u_");
				else if(ret.nl.contains("down"))
					ret.nl = ret.nl.replace("down","left");
				else if(ret.nl.contains("left"))
					ret.nl = ret.nl.replace("left","down");
				else if(ret.nl.contains("up"))
					ret.nl = ret.nl.replace("up","right");
				else if(ret.nl.contains("right"))
					ret.nl = ret.nl.replace("right","up");
			}
			
			if(ret.el != null)
			{
				if(ret.el.equals("d"))
					ret.el = "l";
				else if(ret.el.equals("l"))
					ret.el = "d";
				else if(ret.el.equals("r"))
					ret.el = "u";
				else if(ret.el.equals("u"))
					ret.el = "r";
				else if(ret.el.contains("d_"))
					ret.el = ret.el.replace("d_","l_");
				else if(ret.el.contains("l_"))
					ret.el = ret.el.replace("l_","d_");
				else if(ret.el.contains("u_"))
					ret.el = ret.el.replace("u_","r_");
				else if(ret.el.contains("r_"))
					ret.el = ret.el.replace("r_","u_");
				else if(ret.el.contains("down"))
					ret.el = ret.el.replace("down","left");
				else if(ret.el.contains("left"))
					ret.el = ret.el.replace("left","down");
				else if(ret.el.contains("up"))
					ret.el = ret.el.replace("up","right");
				else if(ret.el.contains("right"))
					ret.el = ret.el.replace("right","up");
			}
			
			if(ret.sl != null)
			{
				if(ret.sl.equals("d"))
					ret.sl = "l";
				else if(ret.sl.equals("l"))
					ret.sl = "d";
				else if(ret.sl.equals("r"))
					ret.sl = "u";
				else if(ret.sl.equals("u"))
					ret.sl = "r";
				else if(ret.sl.contains("d_"))
					ret.sl = ret.sl.replace("d_","l_");
				else if(ret.sl.contains("l_"))
					ret.sl = ret.sl.replace("l_","d_");
				else if(ret.sl.contains("u_"))
					ret.sl = ret.sl.replace("u_","r_");
				else if(ret.sl.contains("r_"))
					ret.sl = ret.sl.replace("r_","u_");
				else if(ret.sl.contains("down"))
					ret.sl = ret.sl.replace("down","left");
				else if(ret.sl.contains("left"))
					ret.sl = ret.sl.replace("left","down");
				else if(ret.sl.contains("up"))
					ret.sl = ret.sl.replace("up","right");
				else if(ret.sl.contains("right"))
					ret.sl = ret.sl.replace("right","up");
			}
			
			if(ret.wl != null)
			{
				if(ret.wl.equals("d"))
					ret.wl = "l";
				else if(ret.wl.equals("l"))
					ret.wl = "d";
				else if(ret.wl.equals("r"))
					ret.wl = "u";
				else if(ret.wl.equals("u"))
					ret.wl = "r";
				else if(ret.wl.contains("d_"))
					ret.wl = ret.wl.replace("d_","l_");
				else if(ret.wl.contains("l_"))
					ret.wl = ret.wl.replace("l_","d_");
				else if(ret.wl.contains("u_"))
					ret.wl = ret.wl.replace("u_","r_");
				else if(ret.wl.contains("r_"))
					ret.wl = ret.wl.replace("r_","u_");
				else if(ret.wl.contains("down"))
					ret.wl = ret.wl.replace("down","left");
				else if(ret.wl.contains("left"))
					ret.wl = ret.wl.replace("left","down");
				else if(ret.wl.contains("up"))
					ret.wl = ret.wl.replace("up","right");
				else if(ret.wl.contains("right"))
					ret.wl = ret.wl.replace("right","up");
			}*/
			
			return ret;
		}
		
		public boolean equals(Object obj)
		{
			return name.equals(obj);
		}
		
		public String toString()
		{
			String ret = "";
			String newline = new Character((char)0x0A).toString();
			
			ret += "TILENAME " + name + newline;
			ret += "LABEL " + label + newline;
			ret += "NORTHBIND " + nb + newline;
			ret += "EASTBIND " + eb + newline;
			ret += "SOUTHBIND " + sb + newline;
			ret += "WESTBIND " + wb + newline;
			ret += "NORTHLABEL " + (nl == null ? "" : nl) + newline;
			ret += "EASTLABEL " + (el == null ? "" : el) + newline;
			ret += "SOUTHLABEL " + (sl == null ? "" : sl) + newline;
			ret += "WESTLABEL " + (wl == null ? "" : wl) + newline;
			ret += "TILECOLOR" + tilecolor + newline;
			
			if(textcolor != null)
				ret += "TEXTCOLOR" + textcolor + newline;
			
			ret += "CREATE" + newline + newline;
			
			return ret;
		}
		
		public String name;
		public String label;
		public String nb;
		public String eb;
		public String sb;
		public String wb;
		public String nl;
		public String el;
		public String sl;
		public String wl;
		public String tilecolor;
		public String textcolor;
	}
}
