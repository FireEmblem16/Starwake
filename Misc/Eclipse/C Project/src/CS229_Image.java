import java.awt.Color;

/**
 * Contains a cs299 image.
 */
public class CS229_Image
{
	/**
	 * Loads the image file.
	 */
	public CS229_Image(String file)
	{
		loaded = false;
		
		// Get the imag file into memory
		Load(file);
		
		return;
	}
	
	/**
	 * When this object goes away we need to free memory it is using.
	 */
	public void finalize()
	{
		if(loaded)
		{
			int err = Free();
			
			if(err == 0)
				loaded = false;
			else
				ReportError(err);
		}
		
		return;
	}
	
	/**
	 * Gets the width of the image.
	 */
	public int GetWidth()
	{
		return width;
	}
	
	/**
	 * Gets the height of the image.
	 */
	public int GetHeight()
	{
		return height;
	}
	
	/**
	 * Returns the name of the open image name or null if no image is open.
	 */
	public String GetOpenImageName()
	{
		if(loaded)
			return file_open;
		
		return null;
	}
	
	/**
	 * Loads a CS229 Image file from loc.
	 */
	public boolean Load(String loc)
	{
		if(loaded)
		{
			int e = Free();
			
			if(e == 0)
				loaded = false;
			else
			{
				ReportError(e);
				return false;
			}
		}
		
		int err = Load(loc.toCharArray());
		
		if(err == 0)
			loaded = true;
		else
		{
			ReportError(err);
			return false;
		}
		
		file_open = loc;
		return true;
	}
	
	/**
	 * Saves a CS229 Image file to loc.
	 */
	public boolean Save(String loc)
	{
		if(!loaded)
			return false;
		
		int err = Save(loc.toCharArray());
		
		if(err != 0)
		{
			ReportError(err);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Convolutes a pixel. kernel is the path to the kernel file.
	 */
	public boolean Convolute(String kernel)
	{
		if(!loaded)
			return false;
		
		int err = Convolute(kernel.toCharArray());
		
		if(err != 0)
		{
			ReportError(err);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gets a pixel. Color should be an array of at least one if in
	 * blackwhite mode and at least three if in color mode.
	 */
	public Color GetPixel(int x, int y)
	{
		if(!loaded)
			return null;
		
		int len = color_mode == MODE_COLOR ? 3 : 1;
		int[] colors = new int[len];
		
		// Get the pixel we want
		int err = GetPixel(x,y,colors);
		
		// Error check
		if(err != 0)
		{
			ReportError(err);
			return null;
		}
		
		// Do any promotions that are needed
		switch(first_channel_size)
		{
		case FOUR_BIT_CHANNEL:
			colors[0] *= 0x10;
			break;
		case SIXTEEN_BIT_CHANNEL:
			colors[0] /= 0x100;
			break;
		}
		
		if(color_mode == MODE_COLOR)
		{
			// Do any promotions that are needed
			switch(second_channel_size)
			{
			case FOUR_BIT_CHANNEL:
				colors[1] *= 0x10;
				break;
			case SIXTEEN_BIT_CHANNEL:
				colors[1] /= 0x100;
				break;
			}
			
			// Do any promotions that are needed
			switch(third_channel_size)
			{
			case FOUR_BIT_CHANNEL:
				colors[2] *= 0x10;
				break;
			case SIXTEEN_BIT_CHANNEL:
				colors[2] /= 0x100;
				break;
			}
		}
		
		// Black and white images can be promoted to color by given all rgb values the same value
		return color_mode == MODE_COLOR ? new Color(colors[0],colors[1],colors[2]) : new Color(colors[0],colors[0],colors[0]);
	}
	
	/**
	 * Load the native library.
	 */
	static
	{
		System.loadLibrary("CS229_Image");
	}
	
	/**
	 * Loads a CS229 Image file from loc.
	 */
	private native int Load(char[] loc);
	
	/**
	 * Saves a CS229 Image file to loc.
	 */
	private native int Save(char[] loc);
	
	/**
	 * Convolutes a pixel. kernel is the path to the kernel file.
	 */
	private native int Convolute(char[] kernel);
	
	/**
	 * Gets a pixel. Color should be an array of at least one if in
	 * blackwhite mode and at least three if in color mode.
	 */
	private native int GetPixel(int x, int y, int[] color);
	
	/**
	 * Frees up the data used by the image.
	 */
	private native int Free();
	
	/**
	 * Reports an error as defined by the native library.
	 */
	public native int ReportError(int err);
	
	/**
	 * Lets us know if we have a picture loaded.
	 */
	private boolean loaded;
	
	/**
	 * The name of the image we are currently displaying.
	 */
	private String file_open;
	
	/**
	 * The magic number of the image.
	 */
	private int magic_number;
	
	/**
	 * The color mode of the image.
	 */
	private int color_mode;
	
	/**
	 * Contains the size of the first channel.
	 */
	private int first_channel_size;
	
	/**
	 * Contains the size of the second channel.
	 */
	private int second_channel_size;
	
	/**
	 * Contains the size of the third channel.
	 */
	private int third_channel_size;
	
	/**
	 * Contains the width of the image.
	 */
	private int width;
	
	/**
	 * Contains the height of the image.
	 */
	private int height;
	
	/**
	 * Contains the image data pointer.
	 */
	private int data;
	
	/**
	 * cs229 images have 0x42 as their magic number.
	 */
	private final int CS229_MAGIC_NUMBER = 0x42;

	/**
	 * cs229 images stores channel sizes as these numbers.
	 */
	private final int FOUR_BIT_CHANNEL = 0x1;
	private final int EIGHT_BIT_CHANNEL = 0x2;
	private final int SIXTEEN_BIT_CHANNEL = 0x4;

	/**
	 * cs229 images use these values to indicate color mode.
	 */
	private final int MODE_BLACKWHITE = 0x0;
	private final int MODE_COLOR = 0xFF;
}
