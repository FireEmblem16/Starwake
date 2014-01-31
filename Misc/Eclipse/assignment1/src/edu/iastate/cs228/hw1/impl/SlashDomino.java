package edu.iastate.cs228.hw1.impl;

import java.awt.Color;
import java.awt.Point;
import edu.iastate.cs228.hw1.Cell;

/**
 * Creates a polyomino the consists or two blocks aligned diagonal to each other.
 * @author Donald Nye
 */
public class SlashDomino extends Polyomino
{
	/**
	 * Creates a new SlashDomino.
	 * @param position
	 * The inital position of the SlashDomino.
	 * @param colors
	 * The colors for the cells of the SlashDomino to be.
	 */
	public SlashDomino(Point position, Color[] colors)
	{
		super(position,2,2);

		cells[0] = new Cell(new Block(colors[0]),new Point(0,0));
		cells[1] = new Cell(new Block(colors[1]),new Point(1,1));
		
		return;
	}
}
