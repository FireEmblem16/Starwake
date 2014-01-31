package image.container;

import image.NameScheme;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import statics.ArrayFunctions;

/**
 * Contains an image and it's relevant data.
 * More or less this class is just a struct.
 */
public class ImageContainer
{
	/**
	 * Creates a new image container.
	 */
	public ImageContainer()
	{
		u = -1;
		v = -1;
		width = -1;
		height = -1;
		leftpadding = -1;
		rightpadding = -1;
		toppadding = -1;
		bottompadding = -1;
		rotate = 0;
		name = null;
		namescheme = null;
		path = null;
		flip = null;
		
		return;
	}
	
	/**
	 * Interperets the data in this object and generates images from it.
	 */
	public ArrayList<MappedBufferedImage> Generate()
	{
		// We will be returning this
		ArrayList<MappedBufferedImage> ret = new ArrayList<MappedBufferedImage>();
		
		// Check that we have a path
		if(path == null || !new File(path).exists())
			return ret;
		
		// This will be true if we intend to dissect one image into many
		boolean dissect = false;
		
		// If we have any padding at all then we need to dissect the image
		if(leftpadding > -1 || rightpadding > -1 || toppadding > -1 || bottompadding > -1)
			if(width > 0 || height > 0)
				dissect = true;
		
		// We need the padding values to be well defined if we are dissecting
		if(dissect)
		{
			if(leftpadding < 0)
				leftpadding = 0;
			if(rightpadding < 0)
				rightpadding = 0;
			if(toppadding < 0)
				toppadding = 0;
			if(bottompadding < 0)
				bottompadding = 0; 
		}
		
		// We always use uv values so make sure they are both defined
		if(u < 1)
			u = 0;
		if(v < 1)
			v = 0;
		
		// This will hold our base image
		BufferedImage img = null;
		
		// Read in the image so we can start the magic
		try
		{img = ImageIO.read(new File(path));}
		catch(IOException e)
		{return ret;}
		
		// Check if we have undefined width or hight values
		if(width < 1)
			width = img.getWidth() - u;
		if(height < 1)
			height = img.getHeight() - v;
		
		// If we are dissecting the image we need to perform many more operations
		if(dissect)
		{
			NameScheme scheme = new NameScheme(namescheme);
			
			for(int y = 0;v + (toppadding + bottompadding + height) * (y + 1) <= img.getHeight();y++)
			{
				for(int x = 0;u + (leftpadding + rightpadding + width) * (x + 1) <= img.getWidth();x++)
				{
					// Get the pixel data from our image
					Raster r = img.getData(new Rectangle(u + leftpadding + (width + rightpadding + leftpadding) * x,v + toppadding + (width + bottompadding + toppadding) * y,width,height));
					int[] pixels = null;
					pixels = r.getPixels(u + leftpadding + (width + rightpadding + leftpadding) * x,v + toppadding + (width + bottompadding + toppadding) * y,width,height,pixels);
					
					// Convert integers to bytes
					byte[] pixels_byte = new byte[pixels.length];
					for(int i = 0;i < pixels_byte.length;i++)
						pixels_byte[i] = (byte)pixels[i];
					
					// Create a buffer, raster and then the new buffered image
					DataBuffer buffer = new DataBufferByte(pixels_byte,width * height,0); 
					WritableRaster wr = Raster.createWritableRaster(r.createCompatibleWritableRaster().getSampleModel(),buffer,null);
					
					// We can make use of [name] since we are creating many pictures from one and thus get our names from a name scheme or generic names
					if(scheme.HasNext())
					{
						// Get the next namescheme value
						ArrayList<String> vals = scheme.GetNext();
						
						// Make sure we have a valid name or else go to default values
						if(vals != null && vals.size() > 0)
							name = vals.get(0);
						else
						{
							name = null;
							filter = null;
						}
						
						// If we have a filter than set it
						if(vals != null && vals.size() > 1)
						{
							filter = new ArrayList<String>();
							
							for(int i = 1;i < vals.size();i++)
								filter.add(vals.get(i));
						}
					}
					else
					{
						name = null; // We can tell the pallet to use a generic name by setting [name] to null
						filter = null;
					}
					
					ret.add(new MappedBufferedImage(name,new BufferedImage(img.getColorModel(),wr,false,null),filter).Flip(flip).Rotate(rotate));
				}
			}
		}
		else
		{
			// If we don't have enough picture to crop then throw it out, also note that all paddings are zero by construction
			if(u + width > img.getWidth() || v + height > img.getHeight())
				return ret;
			else if(img.getWidth() == width && img.getHeight() == height) // Check if we need to do anything or if we can just return the whole image
				ret.add(new MappedBufferedImage(name,img,filter).Flip(flip).Rotate(rotate)); // Notice that uv must both be zero or else we would have errored already
			else
			{
				// Get the pixel data from our image
				Raster r = img.getData(new Rectangle(u,v,width,height));
				int[] pixels = null;
				pixels = r.getPixels(u,v,width,height,pixels);
				
				// Convert integers to bytes
				byte[] pixels_byte = new byte[pixels.length];
				for(int i = 0;i < pixels_byte.length;i++)
					pixels_byte[i] = (byte)pixels[i];
				
				// Create a buffer, raster and then the new buffered image
				DataBuffer buffer = new DataBufferByte(pixels_byte,width * height,0); 
				WritableRaster wr = Raster.createWritableRaster(r.createCompatibleWritableRaster().getSampleModel(),buffer,null);
				ret.add(new MappedBufferedImage(name,new BufferedImage(img.getColorModel(),wr,false,null),filter).Flip(flip).Rotate(rotate));
			}
		}
		
		return ret;
	}
	
	public int u;
	public int v;
	
	public int width;
	public int height;
	
	public int leftpadding;
	public int rightpadding;
	public int toppadding;
	public int bottompadding;
	
	public int rotate;
	
	public String name;
	public String namescheme;
	public String path;
	public String flip;
	
	public ArrayList<String> filter;
}
