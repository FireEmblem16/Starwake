package gui.editor.listener;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Displays or hides the window given to it when an action occurs.
 */
public class WindowViewer implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public WindowViewer(Window window)
	{
		win = window;
		return;
	}
	
	/**
	 * Shows the given window if it is hidden and hides it if it is visible.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(win == null)
			return;
		
		win.setVisible(!win.isVisible());
		return;
	}
	
	/**
	 * Contains the window we want to show or hide.
	 */
	protected Window win;
}
