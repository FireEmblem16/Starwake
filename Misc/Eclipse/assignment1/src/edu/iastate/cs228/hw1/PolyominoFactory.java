package edu.iastate.cs228.hw1;

import edu.iastate.cs228.hw1.impl.ITriomino;
import edu.iastate.cs228.hw1.impl.LTetromino;
import edu.iastate.cs228.hw1.impl.SlashDomino;

import java.awt.Point;
import java.awt.Color;

/**
 * Factory interface for creating polyominos of a specific kind.
 * This interface is to make it easier for us to test your code.
 * You should implement it as edu.iastate.cs228.hw1.impl.CS228PolyominoFactory
 **/
public interface PolyominoFactory {

	/**
	 * Returns an ITriomino in the given position with the
	 * blocks having the colors specified in the colors array.
	 * The order of the colors in the array should be the order of
	 * the colors on the ITriomino from top to bottom.
	 *
	 * @param position The position of the polyomino
	 * @param colors The top to bottom list of colors for the polyomino
	 *
	 * @return an ITriomino with the given position and colors
	 **/
	public ITriomino getITriomino(Point position, Color[] colors);

	/**
	 * Returns an LTetromino in the given position with the
	 * blocks having the colors specified in the colors array.
	 * The order of the colors in the array should be the order of
	 * the colors on the LTetromino in English reading order (left to right, top to bottom)
	 *
	 * @param position The position of the polyomino
	 * @param colors The top to bottom list of colors for the polyomino
	 *
	 * @return an LTetromino with the given position and colors
	 **/
	public LTetromino getLTetromino(Point position, Color[] colors);

	/**
	 * Returns a SlashDomino in the given position with the
	 * blocks having the colors specified in the colors array.
	 * The order of the colors in the array should be the order of
	 * the colors on the SlashDomino in English reading order (left to right, top to bottom)
	 *
	 * @param position The position of the polyomino
	 * @param colors The top to bottom list of colors for the polyomino
	 *
	 * @return an SlashDomino with the given position and colors
	 **/
	public SlashDomino getSlashDomino(Point position, Color[] colors);

}
