import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		Scanner in = new Scanner(new File("in.txt"));
		FileWriter fout = new FileWriter(new File("out.txt"));
		
		int T = in.nextInt();
		int C = 1;
		
		while(C <= T)
		{
			boolean square = false;
			int N = in.nextInt();
			
			String[] board = new String[N];
			
			for(int i = 0;i < N;i++)
				board[i] = in.next();
			
			ArrayList<Point> dark = new ArrayList<Point>(N * N);
			
			for(int i = 0;i < N;i++)
				for(int j = 0;j < N;j++)
					if(board[i].charAt(j) == '#')
						dark.add(new Point(i,j));
			
			if(IsSquare(dark.size()))
			{
				int left = N;
				int right = -1;
				int top = N;
				int bottom = -1;
				
				for(Point p : dark)
				{
					if(p.x < left)
						left = p.x;
					
					if(p.x > right)
						right = p.x;
					
					if(p.y < top)
						top = p.y;
					
					if(p.y > bottom)
						bottom = p.y;
				}
				
				int width = right - left + 1;
				int height = bottom - top + 1;
				
				if(width == height && width * height == dark.size())
					square = true;
			}
			
			fout.write("Case #" + C++ + ": " + (square ? "YES" : "NO") + (C <= T ? "\n" : ""));
		}
		
		in.close();
		fout.close();
		
		return;
	}
	
	public static boolean IsSquare(int N)
	{
		int s = (int)Math.sqrt(N);
		return s * s == N;
	}
}
