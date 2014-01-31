package test;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

//panel used to draw image on
public class ImagePanel extends JPanel
{
	//path of image
	private String path;
	
	//image object
	private Image img;
	
	public ImagePanel(String path)	 throws IOException
	{
		//save path
		this.path = path;	
		
		//load image
		img = ImageIO.read(new File(path));
		
	}
	
	//override paint method of panel
	public void paint(Graphics g)
	{
		//draw the image
		if( img != null)
			g.drawImage(img,0,0, this);
	}
	
}
