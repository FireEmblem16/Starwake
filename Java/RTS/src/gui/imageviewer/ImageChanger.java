package gui.imageviewer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComboBox;
import gui.ImagePanel;
import image.container.MappedBufferedImage;

/**
 * Changes an image panel to the image we clicked.
 */
public class ImageChanger implements MouseListener
{
	/**
	 * Constrcuts and initializes this object.
	 */
	public ImageChanger(MappedBufferedImage image, ImagePanel display, JComboBox changeme)
	{
		img = image;
		disp = display;
		change = changeme;
		
		return;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(img == null || disp == null || change == null)
			return;
		
		change.setSelectedItem(img.GetName());
		disp.ChangeImage(img);
		
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void mouseEntered(MouseEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void mouseExited(MouseEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void mousePressed(MouseEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void mouseReleased(MouseEvent e)
	{return;}
	
	/**
	 * Contains the image we want to display in [disp].
	 */
	protected MappedBufferedImage img;
	
	/**
	 * Contains where we want to display [img].
	 */
	protected ImagePanel disp;
	
	/**
	 * Contains the combo box that we want to change to the name of [img].
	 */
	protected JComboBox change;	
}
