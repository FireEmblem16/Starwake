package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import edu.iastate.cs228.hw1.PolyominoFactory;

/**
 * Creates and returns a Polyomino specified by the function call.
 * @author Donald Nye
 */
public class CS228PolyominoFactory implements PolyominoFactory
{
	@Override
	public ITriomino getITriomino(Point position, Color[] colors)
	{
		return new ITriomino(position,colors);
	}

	@Override
	public LTetromino getLTetromino(Point position, Color[] colors)
	{
		return new LTetromino(position,colors);
	}

	@Override
	public SlashDomino getSlashDomino(Point position, Color[] colors)
	{
		return new SlashDomino(position,colors);
	}
}
