import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Contains the necessary data and functions to display our images.
 */
public class ImageDisplay extends JPanel
{
	/**
	 * Create the object.
	 */
	public ImageDisplay(Host h)
	{
		super();
		
		image = null;
		this.h = h;
		
		return;
	}
	
	/**
	 * Sets the image that should be displayed by this ImageDisplay. If [img] is null the
	 * image will be set to null, thus clearing the image and having no image be displayed.
	 */
	public void SetImage(BufferedImage img)
	{
		// Change the image
		image = img;
		
		// If we've decided to null the image set the size to the normal max
		if(image == null)
		{
			setPreferredSize(new Dimension(600,600));
			return;
		}
		
		// Set the size of the window
		setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
		
		// Update the GUI so that scroll bars will appear or disappear and the image will be redrawn.
		h.CreateAndDisplayGUI();
		
		return;
	}
	
	/**
	 * Returns the image currently being displayed.
	 */
	public BufferedImage GetImage()
	{
		return image;
	}
	
	/**
	 * Paint this object.
	 */
	public void paint(Graphics g)
	{
		super.paint(g);
		
		// If we have an image to draw do so
		if(image != null) // Center the image in the area available to display it in
			((Graphics2D)g).drawImage(image,null,(int)((getSize().getWidth() - image.getWidth()) / 2.0),(int)((getSize().getHeight() - image.getHeight()) / 2.0));
		
		return;
	}
	
	/**
	 * Contains the image that we want to display.
	 */
	public BufferedImage image;
	
	/**
	 * We keep this here so that we can update the GUI.
	 */
	private Host h;
}
