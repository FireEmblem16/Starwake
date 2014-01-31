import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A wrapper for cs299 images.
 */
public class CS229BufferedImage extends BufferedImage
{
	/**
	 * Construct the image to be very small.
	 */
	public CS229BufferedImage(CS229_Image img)
	{
		// Construct our buffer
		super(img.GetWidth(),img.GetHeight(),TYPE_INT_RGB);
		
		// Populate image with data
		for(int i = 0;i < img.GetWidth();i++)
			for(int j = 0;j < img.GetHeight();j++)
				setRGB(i,j,img.GetPixel(i,j).getRGB());
		
		return;
	}
	
	/**
	 * Returns the raw image data of this BufferedImage.
	 */
	public CS229_Image GetRawImage()
	{
		return image;
	}
	
	/**
	 * We hold the raw image data so we can manipulate it later.
	 */
	private CS229_Image image;
}
