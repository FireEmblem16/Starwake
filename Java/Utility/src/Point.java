
public class Point
{
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		return;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof Point)
			return equals((Point)obj);
		
		return false;
	}
	
	public boolean equals(Point p)
	{return x == p.x && y == p.y;}
	
	public String toString()
	{return "(" + x + "," + y + ")";}
	
	public int x;
	public int y;
}
