
package edu.iastate.cs228.hw1.impl;

import java.awt.Point;

import edu.iastate.cs228.hw1.Cell;
import edu.iastate.cs228.hw1.IGameIcon;
import edu.iastate.cs228.hw1.IPolyomino;

/**
 * Impliments the IPolyomino interface to store data and
 * provide functions relevant to polyominos.
 * @author Donald Nye
 */
public abstract class Polyomino implements IPolyomino
{
	/**
	 * The cells of this Polyomino.
	 */
	protected Cell[] cells;
	
	/**
	 * The bounding width of this Polyomino's possible locations.
	 */
	protected int width;
	
	/**
	 * The number of cells in this Polyomino.
	 */
	private int count;
	
	/**
	 * The position on the grid of this Polyomino.
	 */
	private Point position;
	
	/**
	 * Creates a new Polyomino.
	 * @param position
	 * The location to put this Polyomino on the grid.
	 * @param width
	 * The bounding width of this Polyomino's possible locations.
	 * @param count
	 * The number of cells in this Polyomino.
	 */
	protected Polyomino(Point position, int width, int count)
	{
		this.cells = new Cell[count];
		this.count = count;
		this.width = width;
		this.position = new Point(position);
	}
	
	/**
	 * Returns a deep copy of the cells of this Polyomino.
	 * @return
	 * The cells of this Polyomino.
	 */
	public Cell[] getCells()
	{
		Cell[] absCells = new Cell[count];
		
		for(int i = 0; i < count; i++)
		{
			Point p = cells[i].getPosition();
			Point newP = new Point(p.x + position.x, p.y + position.y);
			
			IGameIcon newB = (IGameIcon)cells[i].getIcon().clone();
			absCells[i] = new Cell(newB, newP);
		}
		
		return absCells;
	}
	
	/**
	 * Returns the position of the Polyomino.
	 */
	public Point getPosition()
	{
		return new Point(position);  
	}
	
	public void shiftDown()
	{
		position.y++;
	}

	public void shiftLeft()
	{
		position.x--;
	}

	public void shiftRight()
	{
		position.x++;
	}

	@Override
	public void transform()
	{
		flip();
	}

	/**
	 * Reflects the Polyomino horizontally.
	 */
	private void flip()
	{
		for(Cell c : cells)
		{
			Point p = c.getPosition();
			p.x = width - (p.x + 1);
		}
		
		return;
	}
  
	/**
	 * Rotates the Polyomino 90 degrees.
	 */
	private void rotate()
	{
		for(Cell c : cells)
		{
			Point p = c.getPosition();
			int x = p.x;
			
			p.x = p.y;
			p.y = width - (x + 1);
		}
		
		return;
	}

	@Override
	public void cycle()
	{
		IGameIcon init = cells[0].getIcon();
		
		for (int i = 1; i < cells.length; ++i)
			cells[i - 1].setIcon(cells[i].getIcon());
		
		cells[cells.length - 1].setIcon(init);
		
		return;
	}
	
	@Override
	public Polyomino clone()
	{
		Polyomino cloned = null;
		
		try
		{
			cloned = (Polyomino)super.clone();
			cloned.position = new Point(this.position);
			cloned.cells = new Cell[count];
			
			for (int i = 0; i < count; i++)
				cloned.cells[i] = new Cell(this.cells[i]);
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return cloned;
	}
  
	/**
	 * Compares the two Poylominos for equality.
	 * @param obj
	 * The Polyomino to compare to this.
	 * @return
	 * true if the polynominos' cells are in the same position on the grid.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null || obj.getClass() != getClass())
			return false;
	
		Polyomino poly = (Polyomino)obj;
	
		if(!getPosition().equals(poly.getPosition()))
			return false;
	
		Cell[] cells1 = getCells();
		Cell[] cells2 = poly.getCells();
	
		if(cells1.length != cells2.length)
			return false;
	
		for(int i = 0;i < cells1.length;i++)
			if(!cells1[i].getPosition().equals(cells2[i].getPosition()))
				return false;
	  
		return true;
	}
}
