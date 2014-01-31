import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int X = in.nextInt();
		
		for(int c = 0;c < X;c++)
		{
			int height = in.nextInt();
			int width = in.nextInt();
			
			char[][] wall = new char[height][];
			
			for(int i = 0;i < height;i++)
				wall[i] = in.next().toCharArray();
			
			ArrayList<RepresentativeSet<Point>> blocks = new ArrayList<RepresentativeSet<Point>>(height * width);
			ArrayList<Point> fast_check = new ArrayList<Point>(height * width);
			
			for(int i = 0;i < height;i++)
				for(int j = 0;j < width;j++)
					fast_check.add(new Point(j,i));
			
			while(!fast_check.isEmpty())
			{
				RepresentativeSet<Point> next = GetBlock(fast_check.get(0),wall);
				blocks.add(next);
				
				for(Point rem : next)
					fast_check.remove(rem);
			}
			
			Node<Point> start = new Node<Point>(new Point(0,-1));
			Node<Point> end = new Node<Point>(new Point(0,height));
			
			ArrayList<Node<Point>> nodes = new ArrayList<Node<Point>>(blocks.size() + 2);
			
			for(RepresentativeSet<Point> R : blocks)
				nodes.add(new Node<Point>(R.GetRepresentative()));
			
			for(RepresentativeSet<Point> R : blocks)
			{
				Node<Point> n = GetNode(nodes,R.GetRepresentative());
				
				ArrayList<Point> outline = GetOutline(R,height,width);
				ArrayList<RepresentativeSet<Point>> neighbors = new ArrayList<RepresentativeSet<Point>>(outline.size());
				
				for(Point p : outline)
				{
					RepresentativeSet<Point> block_p = GetBlockByRepresentative(blocks,p);
					
					if(!neighbors.contains(block_p))
						neighbors.add(block_p);
				}
				
				if(IsUpperBoundary(R))
				{
					start.AddChild(n,1);
					n.AddChild(start,1);
				}
				
				if(IsLowerBoundary(R,height))
				{
					end.AddChild(n,0); // Because we are counting blocks we don't want to count an extra one on this path
					n.AddChild(end,0);
				}
				
				for(RepresentativeSet<Point> S : neighbors)
					n.AddChild(GetNode(nodes,S.GetRepresentative()),1);
			}
			
			System.out.println(start.MinDistance(end));
		}
		
		return;
	}
	
	public static Node<Point> GetNode(ArrayList<Node<Point>> nodes, Point p)
	{
		for(Node<Point> n : nodes)
			if(n.ID.equals(p))
				return n;
		
		return null;
	}
	
	public static RepresentativeSet<Point> GetBlockByRepresentative(ArrayList<RepresentativeSet<Point>> blocks, Point p)
	{
		for(RepresentativeSet<Point> R : blocks)
			if(R.InSet(p))
				return R;
		
		return null;
	}
	
	public static RepresentativeSet<Point> GetBlock(Point p, char[][] wall)
	{
		char look_for = GetCharInWall(p,wall);
		
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(p);
		
		for(int i = 0;i < points.size();i++)
		{
			ArrayList<Point> adj = GetNeighbors(points.get(i),wall.length,wall[0].length);
			
			for(Point next : adj)
				if(look_for == GetCharInWall(next,wall) && !points.contains(next))
					points.add(next);
		}
		
		return new RepresentativeSet<Point>(points);
	}
	
	public static ArrayList<Point> GetOutline(RepresentativeSet<Point> block, int height, int width)
	{
		ArrayList<Point> ret = new ArrayList<Point>();
		
		for(Point p : block)
		{
			ArrayList<Point> adj = GetNeighbors(p,height,width);
			
			for(Point adj0 : adj)
				if(!ret.contains(adj) && !block.InSet(adj0))
					ret.add(adj0);
		}
		
		return ret;
	}
	
	public static ArrayList<Point> GetNeighbors(Point p, int height, int width)
	{
		ArrayList<Point> ret = new ArrayList<Point>(4);
		
		Point up = new Point(p.x,p.y - 1);
		Point down = new Point(p.x,p.y + 1);
		Point left = new Point(p.x - 1,p.y);
		Point right = new Point(p.x + 1,p.y);
		
		if(IsInBounds(up,height,width))
			ret.add(up);
		
		if(IsInBounds(down,height,width))
			ret.add(down);
		
		if(IsInBounds(left,height,width))
			ret.add(left);
		
		if(IsInBounds(right,height,width))
			ret.add(right);
		
		return ret;
	}
	
	public static boolean IsInBounds(Point p, int height, int width)
	{return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;}
	
	public static char GetCharInWall(Point p, char[][] wall)
	{return wall[p.y][p.x];}
	
	public static boolean IsUpperBoundary(RepresentativeSet<Point> block)
	{
		for(Point p : block)
			if(p.y == 0)
				return true;
		
		return false;
	}
	
	public static boolean IsLowerBoundary(RepresentativeSet<Point> block, int height)
	{
		for(Point p : block)
			if(p.y == height - 1)
				return true;
		
		return false;
	}
}
