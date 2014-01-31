package gui.editor;

import grid.Board;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Readds a board to the given scrollbars so that the scrollbars recalculate
 * if they need to exist and how much they need to exist.
 */
public class EditorScrollbarUpdater
{
	/**
	 * Constructs and initializes this object.
	 */
	public EditorScrollbarUpdater(JScrollPane scrolls)
	{
		scrollbars = scrolls;
		return;
	}
	
	/**
	 * Sets the board we are displaying.
	 */
	public void SetBoard(Board b)
	{
		board = b;
		return;
	}
	
	/**
	 * Updates the scrollbars.
	 */ 
	public void update()
	{
		if(scrollbars == null || board == null)
		{
			scrollbars.getViewport().removeAll();
			return;
		}
		
		int X = scrollbars.getHorizontalScrollBar().getValue();
		int Y = scrollbars.getVerticalScrollBar().getValue();
		
		scrollbars.getViewport().removeAll();
		scrollbars.getViewport().add(board);
		
		scrollbars.getHorizontalScrollBar().setValue(X);
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		return;
	}
	
	/**
	 * Contians the scrollbars we want to work with.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * Contains the board we want to work with.
	 */
	protected Board board;
}
