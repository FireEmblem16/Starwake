package edu.iastate.cs228.hw1.test;

import java.awt.Color;
import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.GameStatus;
import edu.iastate.cs228.hw1.IPolyomino;
import edu.iastate.cs228.hw1.impl.*;
import edu.iastate.cs228.hw1.ui.GameMain;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.junit.Assert.*;

/**
 * Runs test cases relevant to the 3-7 game.
 * 
 * @author Donald Nye
 */
public class GameTester extends TestCase
{
	/**
	 * Runs the JUnit4 TestRunner.
	 */
	public static void main(String[] args)
	{
		String[] TestClasses =
		{GameTester.class.getName()};
		junit.textui.TestRunner.main(TestClasses);

		return;
	}

	/**
	 * Creates a TestSuite of this class.
	 */
	public static junit.framework.Test suite()
	{
		return new TestSuite(GameTester.class);
	}

	/**
	 * Runs before every test to create gen.
	 */
	@Before
	protected void setUp()
	{
		gen = new BasicGenerator();

		return;
	}

	/**
	 * Tests if the game runs without exceptions.
	 */
	@Test
	public void testRuns()
	{
		try
		{
			GameMain app = new GameMain();

			app.main(null);
			assertTrue(true);
		}
		catch(Exception e)
		{
			fail("Game threw an exception during creation somehow.");
		}

		return;
	}

	/**
	 * Tests BasicGenerator for bugs.
	 */
	@org.junit.Test
	public void testGenerator()
	{
		for(int i = 0;i < 10;i++)
		{
			IPolyomino poly = gen.getNext();

			// Check that a valid Polyomino is returned.
			if(gen == null
					|| !(poly.getClass() == ITriomino.class
							|| poly.getClass() == LTetromino.class || poly
							.getClass() == SlashDomino.class))
				fail("BasicGenerator produced unexpected results.");
		}

		return;
	}

	/**
	 * Tests that polyominos are constructed properly.
	 */
	@org.junit.Test
	public void testPolyominos()
	{
		Cell[] cells = null;
		
		Color[] colors =
		{
			Color.CYAN,
			Color.BLUE,
			Color.ORANGE,
			Color.YELLOW,
			Color.GREEN,
			Color.MAGENTA,
			Color.RED
		};
		
		Polyomino poly = new ITriomino(new Point(1,1),colors);
		cells = poly.getCells();
		
		// Check that an ITriomino has 3 cells
		assertTrue(cells.length == 3);
		// Check that the Polyomino was constructed at (1,1)
		assertEquals(poly.getPosition(),new Point(1,1));
		
		// Check that the colors were properly assigned.
		for(int i = 0;i < cells.length;i++)
			assertEquals(cells[i].getIcon().getColorHint(),colors[i]);
		
		// Wash, rinse and repeat for LTetromino
		poly = new LTetromino(new Point(2,1),colors);
		cells = poly.getCells();
		
		assertTrue(cells.length == 4);
		assertEquals(poly.getPosition(),new Point(2,1));
		
		for(int i = 0;i < cells.length;i++)
			assertEquals(cells[i].getIcon().getColorHint(),colors[i]);
		
		// Wash, rinse and repeat for SlashDomino
		poly = new SlashDomino(new Point(1,2),colors);
		cells = poly.getCells();
		
		assertTrue(cells.length == 2);
		assertEquals(poly.getPosition(),new Point(1,2));
		
		for(int i = 0;i < cells.length;i++)
			assertEquals(cells[i].getIcon().getColorHint(),colors[i]);
		
		return;
	}

	/**
	 * Tests the getCells function for errors.
	 */
	@org.junit.Test
	public void testGetCells()
	{
		IPolyomino poly;

		for(int i = 0;i < 10;i++)
		{
			poly = gen.getNext();
			Cell[] CTemp = poly.getCells();

			// Make sure null isn't returned
			assertTrue(CTemp != null);
			// Make sure there are cells
			assertTrue(CTemp.length != 0);

			// Check that no cells are null
			for(int j = 0;j < CTemp.length;j++)
				assertTrue(CTemp[j] != null);
		}

		return;
	}

	/**
	 * Tests the getPosition function.
	 */
	@org.junit.Test
	public void testGetPosition()
	{
		Polyomino poly = (Polyomino)gen.getNext();
		Point pt = poly.getPosition();

		// Check that the Polyomino has a position
		assertTrue(pt != null);
		// Check that the Polyomino starts at x value 5
		assertTrue(pt.x == 5);
		// Check that the Polyomino starts at the right y value
		if(poly.getClass() == SlashDomino.class)
			assertEquals(pt.y,-1);
		else
			assertEquals(pt.y,-2);

		return;
	}

	/**
	 * Checks to see the Polyomino functions execute as expected.
	 */
	@org.junit.Test
	public void testPolyominoMechanics()
	{
		Polyomino poly = (Polyomino)gen.getNext();
		Polyomino backupPoly = poly.clone();
		// Ensure the clone funciton doesn't return null
		assertTrue(backupPoly != null);

		// Check that the transform function is working
		poly.transform();
		assertTrue(!poly.equals(backupPoly)
				|| poly.getClass() == ITriomino.class);

		// Check that the transform function is consistent
		backupPoly.transform();
		assertTrue(poly.equals(backupPoly));

		// Check that the cycle function has no effect on equality
		poly.cycle();
		assertTrue(poly.equals(backupPoly));

		// Repeat the previous experiment
		backupPoly.cycle();
		assertTrue(poly.equals(backupPoly));

		return;
	}

	/**
	 * Verifies that CS228BlockGame's functions execute as expected.
	 */
	@org.junit.Test
	public void testGameMechanics()
	{
		CS228BlockGame game = new CS228BlockGame();
		assertFalse(game.gameOver());

		// Check the hight of the game
		assertEquals(game.getHeight(),24);
		// Check the width of the game
		assertEquals(game.getWidth(),12);

		// Make sure current is not null
		assertTrue(game.getCurrent() != null);
		// Ensure that the initial state is falling
		assertTrue(game.step() == GameStatus.FALLING);
		// Check that the score starts at zero
		assertEquals(game.getScore(),0);

		// Make sure the following functions work
		assertTrue(game.transform());
		
		// Sanity check on moving down
		int iTemp = game.getCurrent().getCells()[0].getPosition().y;
		game.step();
		assertEquals(iTemp,game.getCurrent().getCells()[0].getPosition().y-1);
		
		// Sanity check on moving left
		iTemp = game.getCurrent().getCells()[0].getPosition().x;
		assertTrue(game.shiftLeft());
		assertEquals(iTemp,game.getCurrent().getCells()[0].getPosition().x+1);
		
		// Sanity check on moving right
		iTemp = game.getCurrent().getCells()[0].getPosition().x;
		assertTrue(game.shiftRight());
		assertEquals(iTemp,game.getCurrent().getCells()[0].getPosition().x-1);
		
		// Check that the board was initialized
		Block c = (Block)game.getCell(23,11);
		assertTrue(c != null);

		// Check that the board was initialized properly
		c = (Block)game.getCell(22,11);
		assertTrue(c == null);

		return;
	}

	/**
	 * Runs after every test to null gen.
	 */
	@After
	protected void tearDown()
	{
		gen = null;

		return;
	}

	/**
	 * Provides Polyominos.
	 */
	private BasicGenerator gen;
}
