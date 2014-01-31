package image;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Provides more functionality to the ImageIcon class.
 */
public class ImageFoil extends JPanel
{
	public ImageFoil(String qualify, String name) throws IOException
	{
		if(name.contains("/"))
		{
			StringTokenizer tokener = new StringTokenizer(name);
			tokener.nextToken("/");
			Name = tokener.nextToken("/");
		}
		else
			Name = name;
		
		if(Name.contains("."))
			Name = Name.substring(0,Name.indexOf("."));
		
		Path = name;
		img = ImageIO.read(new File(qualify + name));
		
		x = 0;
		y = 0;
		
		height = img.getHeight(this);
		width = img.getWidth(this);
		
		return;
	}
	
	public Dimension GetPosition()
	{
		return new Dimension(x,y);
	}
	
	public Dimension GetSize()
	{
		return new Dimension(width,height);
	}
		
	public void paint(Graphics g)
	{
		if(img == null)
			return;
		
		g.drawImage(img,x,y,width,height,this);
		return;
	}
	
	public void SetPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		return;
	}
	
	public void SetDimensions(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		return;
	}
	
	public String toString()
	{
		return Name;
	}
	
	public Image img;
	public String Name;
	public String Path;
	
	protected int height;
	protected int x;
	protected int y;
	protected int width;
}
