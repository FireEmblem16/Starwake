package gui.tool.listener;

import gui.imageselector.ImageSelector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

/**
 * When an action is fired to this object will hide or show the given JDialog.
 */
public class ViewerListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public ViewerListener(JDialog dataview)
	{
		dataviewer = dataview;
		return;
	}
	
	/**
	 * Hides [dataviewer] if it is visible and shows it if it is hidden.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(dataviewer == null)
			return;
		
		dataviewer.setVisible(!dataviewer.isVisible());
		return;
	}
	
	/**
	 * Contains the JDialog we want to show or hide.
	 */
	protected JDialog dataviewer;
}
