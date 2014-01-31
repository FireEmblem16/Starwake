package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import edu.iastate.cs228.hw1.Cell;

/**
 * Creates a polyomino the consists of three blocks aligned vertically and centered.
 * @author Donald Nye
 */
public class ITriomino extends Polyomino
{
	/**
	 * Creates a new ITriomino.
	 * @param position
	 * The inital position of the ITriomino.
	 * @param colors
	 * The colors for the cells of the ITriomino to be.
	 */
	public ITriomino(Point position, Color[] colors)
	{
		super(position,3,3);

		cells[0] = new Cell(new Block(colors[0]),new Point(1,0));
		cells[1] = new Cell(new Block(colors[1]),new Point(1,1));
		cells[2] = new Cell(new Block(colors[2]),new Point(1,2));
		
		return;
	}
}
