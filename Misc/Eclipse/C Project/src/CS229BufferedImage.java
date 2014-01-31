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
		image = img;
		
		SetData();
		return;
	}
	
	public void SetData()
	{
		// Populate image with data
		for(int i = 0;i < image.GetWidth();i++)
			for(int j = 0;j < image.GetHeight();j++)
			{
				Color c = image.GetPixel(i,j);
				
				if(c != null)
					setRGB(i,j,c.getRGB());
			}
		
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