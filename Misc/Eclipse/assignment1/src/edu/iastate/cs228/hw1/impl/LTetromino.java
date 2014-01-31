package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import edu.iastate.cs228.hw1.Cell;

/**
 * Creates a polyomino with three blocks centered vertically and a block in the top left.
 * @author Donald Nye
 */
public class LTetromino extends Polyomino
{
	/**
	 * Creates a new LTetromino.
	 * @param position
	 * The inital position of the LTetromino.
	 * @param colors
	 * The colors for the cells of the LTetromino to be.
	 */
	public LTetromino(Point position, Color[] colors)
	{
		super(position,3,4);

		cells[0] = new Cell(new Block(colors[0]),new Point(0,0));
		cells[1] = new Cell(new Block(colors[1]),new Point(1,0));
		cells[2] = new Cell(new Block(colors[2]),new Point(1,1));
		cells[3] = new Cell(new Block(colors[3]),new Point(1,2));
		
		return;
	}
}
