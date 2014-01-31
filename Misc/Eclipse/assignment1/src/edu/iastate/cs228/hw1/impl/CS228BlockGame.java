package edu.iastate.cs228.hw1.impl;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.GameStatus;

/**
 * Impliments IGame and extends AbstractBlockGame to play 3-7.
 * @author Donald Nye
 */
public class CS228BlockGame extends AbstractBlockGame
{
	/**
	 * Creates a new CS228BlockGame.
	 */
	public CS228BlockGame()
	{
		super(new BasicGenerator());
		
		Random rand = new Random(System.currentTimeMillis());
		points = 0;
		
		for(int i = getHeight() - 8; i < getHeight(); i++)
			for(int j = i % 2; j < getWidth(); j += 2)
				grid[i][j] = new Block(AbstractBlockGame.COLORS[rand.nextInt(7)]);
		
		return;
	}

	@Override
	public List<Point> determineCellsToCollapse()
	{	
		List<Point> destory = new ArrayList<Point>();
		
		try
		{
			getCurrent();
			
			Cell[] cells = current.getCells();
			for(int i = 0; i < cells.length; i++)
				if(!destory.contains(cells[i]))
				{
					List<Point> temp = new ArrayList<Point>();
					recurse(temp,cells[i].getPosition());
					
					if(temp.size() > 2)
						for(int j = 0; j < temp.size(); j++)
							if(!destory.contains(temp.get(j)))
								destory.add(temp.get(j));
				}
		}
		catch(IllegalStateException e)
		{
			for(int i = 0; i < grid.length; i++)
				for(int j = 0; j < grid[i].length; j++)
					if(grid[i][j] != null && !destory.contains(grid[i][j]))
					{
						List<Point> temp = new ArrayList<Point>();
						recurse(temp,new Point(j,i));
						
						if(temp.size() > 2)
							for(int l = 0; l < temp.size(); l++)
								if(!destory.contains(temp.get(l)))
									destory.add(temp.get(l));
					}
		}
		
		points += destory.size();
		return destory;
	}

	/**
	 * Looks for chains of similar blocks to the one at <b>cur</b>.
	 * Returns a list of those similar blocks in <b>targets</b>.
	 * @param cur
	 * A point specifing the block for which similar nearby blocks should be found.
	 * @param targets
	 * A list containing similar blocks. Should be passed with no elements initially.
	 */
	private void recurse(List<Point> targets, Point cur)
	{
		targets.add(cur);
		
		if(cur.y > 0 && grid[cur.y - 1][cur.x] != null && grid[cur.y][cur.x] != null
				&& !targets.contains(new Point(cur.x,cur.y - 1))
				&& grid[cur.y - 1][cur.x].getColorHint().equals(grid[cur.y][cur.x].getColorHint()))
			recurse(targets,new Point(cur.x,cur.y - 1));
		
		if(cur.y < getHeight() - 1 && grid[cur.y + 1][cur.x] != null
				&& grid[cur.y][cur.x] != null && !targets.contains(new Point(cur.x,cur.y + 1))
				&& grid[cur.y + 1][cur.x].getColorHint().equals(grid[cur.y][cur.x].getColorHint()))
			recurse(targets,new Point(cur.x,cur.y + 1));
		
		if(cur.x > 0 && grid[cur.y][cur.x - 1] != null && grid[cur.y][cur.x] != null
				&& !targets.contains(new Point(cur.x - 1,cur.y))
				&& grid[cur.y][cur.x - 1].getColorHint().equals(grid[cur.y][cur.x].getColorHint()))
			recurse(targets,new Point(cur.x - 1,cur.y));
		
		if(cur.x < getWidth() - 1 && grid[cur.y][cur.x + 1] != null
				&& grid[cur.y][cur.x] != null&& !targets.contains(new Point(cur.x + 1,cur.y))
				&& grid[cur.y][cur.x + 1].getColorHint().equals(grid[cur.y][cur.x].getColorHint()))
			recurse(targets,new Point(cur.x + 1,cur.y));
		
		return;
	}
	
	@Override
	public int determineScore()
	{
		return points;
	}
	
	/**
	 * The current score.
	 */
	private int points;
}
