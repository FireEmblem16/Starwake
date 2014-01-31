package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import gui.editor.BoardSettings;

/**
 * Hides or displays the window that allows board settings to be edited.
 */
public class BoardSettingsListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public BoardSettingsListener(BoardSettings window)
	{
		win = window;
		return;
	}
	
	/**
	 * Hides [win] if it is visible and shows it if it is not.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(win == null)
			return;
		
		win.setVisible(!win.isVisible());
		return;
	}
	
	/**
	 * Contains the window we want to show or hide
	 */
	protected BoardSettings win;
}
