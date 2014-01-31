package gui.editor.listener;

import grid.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This will force the given board to repaint itself.
 */
public class BoardRepaintListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public BoardRepaintListener(Board board)
	{
		b = board;
		return;
	}
	
	/**
	 * Forces [b] to repaint.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(b == null)
			return;
		
		b.ForceRepaint();
		return;
	}
	
	/**
	 * This is the board we want to forcibly repaint.
	 */
	protected Board b;
}
