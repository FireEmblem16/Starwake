package gui.tool.listener;

import grid.Board;
import grid.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Changes the dimensions or the cells of the currently displayed map when actions are fired to this object.
 */
public class CellDimensionListener implements ChangeListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public CellDimensionListener(Board b, boolean width)
	{
		board = b;
		height = !width;
		
		return;
	}
	
	/**
	 * Changes the size of the cells of the current map being displayed.
	 */
	public void stateChanged(ChangeEvent e)
	{
		if(board == null)
			return;
		
		if(!(e.getSource() instanceof JSpinner))
			return;
		
		JSpinner source = (JSpinner)e.getSource();
		
		if(!(source.getValue() instanceof Integer))
			return;
		
		int new_val = (Integer)source.getValue();
		
		board.ResizeCell(board.GetDisplayedMapName(),height ? board.GetCellWidth() : new_val,height ? new_val : board.GetCellHeight());
		return;
	}
	
	/**
	 * This is the board that we can get the map we want to change from.
	 */
	protected Board board;
	
	/**
	 * This is true if we are trying to edit height and false if we want to edit width.
	 */
	protected boolean height;
}
