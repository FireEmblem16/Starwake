package img;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Essentially just a named BufferedImage with a flip and rotate function.
 */
public class MappedBufferedImage extends BufferedImage
{
	/**
	 * Constructs and initializes this object.
	 */
	public MappedBufferedImage(String n, BufferedImage img, ArrayList<String> filtervals)
	{
		super(img.getWidth(),img.getHeight(),TYPE_INT_ARGB);
		
		for(int i = 0;i < img.getWidth();i++)
			for(int j = 0;j < img.getHeight();j++)
				setRGB(i,j,img.getRGB(i,j));
		
		name = n;
		filter = filtervals;
		return;
	}
	
	/**
	 * Constructs and initializes this object but with width and height switched
	 * and all of the image's data rotated left or right as specified by [rotate_right].
	 */
	protected MappedBufferedImage(MappedBufferedImage img, boolean rotate_right)
	{
		super(img.getHeight(),img.getWidth(),TYPE_INT_ARGB);
		name = img.name;
		filter = img.filter;
		
		// Since our image's height and width have been switched we need to rotate one direction
		// and [rotate_right] is true if we need to rotate clockwise and false for counter clockwise
		if(rotate_right)
			for(int i = 0;i < img.getWidth();i++)
				for(int j = 0;j < img.getHeight();j++)
					setRGB((getHeight() - 1) - j,i,img.getRGB(i,j));
		else
			for(int i = 0;i < img.getWidth();i++)
				for(int j = 0;j < img.getHeight();j++)
					setRGB(j,(getWidth() - 1) - i,img.getRGB(i,j));
		
		return;
	}
	
	/**
	 * Flips this image along the vertical or horizontal axis as specified by [direction].
	 * [direction] is valid for "horizontal" and "vertical".
	 */
	public MappedBufferedImage Flip(String direction)
	{
		if(direction == null || !(direction.equals("horizontal") || direction.equals("vertical")))
			return this;
		
		if(direction.equals("horizontal"))
			for(int i = 0;i < getWidth();i++)
				for(int j = 0;j < getHeight() / 2;j++)
				{
					int old = getRGB(i,j);
					setRGB(i,j,getRGB(i,(getHeight() - 1) - j));
					setRGB(i,(getHeight() - 1) - j,old);
				}
		else
			for(int i = 0;i < getWidth() / 2;i++)
				for(int j = 0;j < getHeight();j++)
				{
					int old = getRGB(i,j);
					setRGB(i,j,getRGB((getWidth() - 1) - i,j));
					setRGB((getWidth() - 1) - i,j,old);
				}
		
		return this;
	}
	
	/**
	 * Rotates this image but [degrees] clockwise. [degrees] must be a multiple of 90 to be valid.
	 * Also if [degrees] is a multiple of 360 no operations will be performed.
	 */
	public MappedBufferedImage Rotate(int degrees)
	{
		if(degrees % 90 != 0 || degrees % 360 == 0)
			return this;
		
		// Get our angle into view
		while(degrees < 0)
			degrees += 360;
		while(degrees >= 360)
			degrees -= 360;
		
		// If we need to rotate 180 degrees then we need only flip both vertically and horizontally which is slightly faster
		if(degrees == 180)
			return Flip("horizontal").Flip("vertical");
		
		return new MappedBufferedImage(this,(degrees == 90 ? true : false));
	}
	
	/**
	 * Returns the name of this image.
	 */
	public String GetName()
	{
		return name;
	}
	
	/**
	 * Sets the name of this image.
	 */
	public void SetName(String n)
	{
		name = n;
		return;
	}
	
	/**
	 * Returns the filter of this image.
	 */
	public ArrayList<String> GetFilter()
	{
		return filter;
	}
	
	/**
	 * Contains the name of this image.
	 */
	protected String name;
	
	/**
	 * Contains the filter values for this image.
	 */
	protected ArrayList<String> filter;
}
