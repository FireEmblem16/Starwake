package gui.imageselector.listener;

import gui.ImagePanel;
import gui.imageviewer.ImageViewer;
import image.container.Pallet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 * Waits for an action and shows or hides an ImageViewer as necessary
 * and updates it if needed.
 */
public class ViewListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public ViewListener(ImageViewer viewer)
	{
		view = viewer;
		return;
	}

	/**
	 * Hides or shows [view].
	 */
	public void actionPerformed(ActionEvent e)
	{
		view.setVisible(!view.isVisible());
		return;
	}
	
	/**
	 * Updates the contents of [view].
	 */
	public void Update(String[] imagenames, String palletname)
	{
		view.Display(imagenames,palletname);
		return;
	}
	
	/**
	 * This contains the image viewer in question.
	 */
	protected ImageViewer view;
}
