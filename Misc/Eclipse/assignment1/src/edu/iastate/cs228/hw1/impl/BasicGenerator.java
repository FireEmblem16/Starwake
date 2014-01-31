package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;
import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.IPolyominoGenerator;

/**
 * Creates polyominos with 60/20/20 weighting.
 * @author Donald Nye
 */
public class BasicGenerator implements IPolyominoGenerator
{
	/**
	 * Creates a new BasicGenerator.
	 */
	public BasicGenerator()
	{
		rand = new Random(System.currentTimeMillis());

		return;
	}

	@Override
	public IPolyomino getNext()
	{
		IPolyomino ret = null;
		Color[] colors =
		{null,null,null,null};
		int rand_num = rand.nextInt(10);

		if(rand_num < 6)
		{
			for(int i = 0;i < 3;i++)
				colors[i] = AbstractBlockGame.COLORS[rand.nextInt(7)];

			ret = new ITriomino(new Point(5,-2),colors);
		}
		else if(rand_num < 8)
		{
			for(int i = 0;i < 4;i++)
				colors[i] = AbstractBlockGame.COLORS[rand.nextInt(7)];

			ret = new LTetromino(new Point(5,-2),colors);
		}
		else
		{
			for(int i = 0;i < 2;i++)
				colors[i] = AbstractBlockGame.COLORS[rand.nextInt(7)];

			ret = new SlashDomino(new Point(5,-1),colors);
		}

		return ret;
	}

	/**
	 * Creates random numbers.
	 */
	private Random rand;
}
