package gui.editor.listener;

import grid.Board;
import item.Item;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 * Waits for the enter key input event at which point it changes the name of the given board.
 */
public class BoardNameChanger implements KeyListener
{
	/**
	 * Constructs and initializes this object so that it waits for the enter
	 * key to be entered and changes the name of [b] to the new value.
	 */
	public BoardNameChanger(Board b)
	{
		board = b;
		return;
	}
	
	/**
	 * Changes what we all know need be changed.
	 */
	public void keyPressed(KeyEvent e)
	{
		if(board == null || e == null || !(e.getSource() instanceof JTextField))
			return;
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			board.setName(((JTextField)e.getSource()).getText());
		
		return;
	}

	/**
	 * Does nothing useful.
	 */
	public void keyReleased(KeyEvent e)
	{return;}

	/**
	 * Does nothing useful.
	 */
	public void keyTyped(KeyEvent e)
	{return;}
	
	/**
	 * Contains the board who's name we want to change.
	 */
	protected Board board;
}
