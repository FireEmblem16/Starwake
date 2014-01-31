public class Tile
	{
		public Tile()
		{
			name = label = nb = eb = sb = wb = nl = el = sl = wl = tilecolor = textcolor = null;
			return;
		}
		
		public Tile(Tile t2)
		{
			if(t2 == null)
				name = label = nb = eb = sb = wb = nl = el = sl = wl = tilecolor = textcolor = null;
			else
			{
				name = t2.name;
				label = t2.label;
				nb = t2.nb;
				eb = t2.eb;
				sb = t2.sb;
				wb = t2.wb;
				nl = t2.nl;
				sl = t2.sl;
				el = t2.el;
				wl = t2.wl;
				tilecolor = t2.tilecolor;
				textcolor = t2.textcolor;
			}
			
			return;
		}
		
		/**
		 * Rotates clockwise.
		 */
		public Tile rotate(int angle)
		{
			if(angle % 90 != 0)
				return null;
			
			while(angle > 360)
				angle -= 360;
			
			while(angle <= 0)
				angle += 360;
			
			if(angle == 360)
				return new Tile(this);
			
			Tile ret = new Tile();
			
			ret.name = name + "_ROT" + (angle % 270 == 0 ? "270" : (angle % 180 == 0 ? "180" : "90"));
			ret.label = label;
			
			ret.nb = angle % 270 == 0 ? eb : (angle % 180 == 0 ? sb : wb);
			ret.eb = angle % 270 == 0 ? sb : (angle % 180 == 0 ? wb : nb);
			ret.sb = angle % 270 == 0 ? wb : (angle % 180 == 0 ? nb : eb);
			ret.wb = angle % 270 == 0 ? nb : (angle % 180 == 0 ? eb : sb);
			
			ret.nl = angle % 270 == 0 ? el : (angle % 180 == 0 ? sl : wl);
			ret.el = angle % 270 == 0 ? sl : (angle % 180 == 0 ? wl : nl);
			ret.sl = angle % 270 == 0 ? wl : (angle % 180 == 0 ? nl : el);
			ret.wl = angle % 270 == 0 ? nl : (angle % 180 == 0 ? el : sl);
			
			ret.textcolor = textcolor;
			ret.tilecolor = tilecolor;
			
			return ret;
		}
		
		public Tile reflect()
		{
			Tile ret = new Tile();
			
			ret.name = name + "_REF";
			ret.label = label;
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
			
			return ret;
		}
		
		public boolean equals(String obj)
		{
			return name.equals(obj);
		}
		
		public boolean equals(Tile obj)
		{
			if(eb != obj.eb && !eb.equals(obj.eb))
				return false;
			
			if(nb != obj.nb && !nb.equals(obj.nb))
				return false;
			
			if(sb != obj.sb && !sb.equals(obj.sb))
				return false;
			
			if(wb != obj.wb && !wb.equals(obj.wb))
				return false;
			
			if(el != obj.el && !el.equals(obj.el))
				return false;
			
			if(nl != obj.nl && !nl.equals(obj.nl))
				return false;
			
			if(sl != obj.sl && !sl.equals(obj.sl))
				return false;
			
			if(wl != obj.wl && !wl.equals(obj.wl))
				return false;
			
			return true;
		}
		
		/**
		 * If only one binding label and direction is specified it is assumed to have
		 * strength two. If two labels and directions are specified then they are both
		 * assumed to have strength one. If a label is given but a direction is not that
		 * is an error. If a direction is given but not a label then it is ignored.
		 */
		public boolean HasBinding(Binding b)
		{
			if(b == null)
				return false;
			
			if(b.l1 == null && b.l2 == null)
				return false;
			
			if(b.l1 == null && b.l2 != null)
				return CheckBond(b.l2,b.d2,"2");
			else if(b.l1 != null && b.l2 == null)
				return CheckBond(b.l1,b.d1,"2");
			else if(!CheckBond(b.l1,b.d1,"1") || !CheckBond(b.l2,b.d2,"1"))
				return false;
			
			return true;
		}
		
		/**
		 * If [label] or [strength] is null we return false so stop doing that crap you cunts.
		 */
		public boolean CheckBond(String label, String dir, String strength)
		{
			if(label == null || strength == null)
				return false;
			
			if("n".equals(dir))
			{
				if(!label.equals(nl) || !strength.equals(nb))
					return false;
			}
			else if("s".equals(dir))
			{
				if(!label.equals(sl) || !strength.equals(sb))
					return false;
			}
			else if("e".equals(dir))
			{
				if(!label.equals(el) || !strength.equals(eb))
					return false;	
			}
			else if("w".equals(dir))
			{
				if(!label.equals(wl) || !strength.equals(wb))
					return false;	
			}
			
			return true;
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
			
			if(tilecolor != null)
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