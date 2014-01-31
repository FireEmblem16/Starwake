package gui;

import img.MappedBufferedImage;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JDialog;
import sun.java2d.pipe.BufferedBufImgOps;

/**
 * This will simply draw the image given to it.
 */
public class ImagePanel extends Component
{
	/**
	 * Constructs and initializes this object.
	 */
	public ImagePanel(MappedBufferedImage img)
	{
		image = img;
		changed = false;
		
		if(img == null)
			return;
		
		setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
		return;
	}
	
	/**
	 * Changes the image we want to draw.
	 */
	public void ChangeImage(MappedBufferedImage img)
	{
		changed = true;
		image = img;
		
		if(img != null)
			setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
		
		return;
	}
	
	/**
	 * Returns true if this image has changed since the last time this function was called.
	 */
	public boolean HasChanged()
	{
		boolean b = changed;
		changed = false;
		
		return b;
	}
	
	/**
	 * Draws this component onto the screen.
	 */
	public void paint(Graphics g)
	{
		if(image == null)
			return;
		
		((Graphics2D)g).drawImage(image,null,0,0);
		return;
	}
	
	/**
	 * Returns the width of the image.
	 */
	public int GetImageWidth()
	{
		return image.getWidth();
	}
	
	/**
	 * Returns the heigth of the image.
	 */
	public int GetImageHeight()
	{
		return image.getHeight();
	}
	
	/**
	 * This will be true if we have changed this image since the last time HasChanged was called.
	 */
	protected boolean changed;
	
	/**
	 * This is the image we want to draw.
	 */
	protected MappedBufferedImage image;
}
