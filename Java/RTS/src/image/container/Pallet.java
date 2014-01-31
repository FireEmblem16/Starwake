package image.container;

import grid.Goal;
import grid.Map;
import image.ImageMaker;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import xml.Parser;
import xml.Tag;

/**
 * Contains all of the images for the game maped to string names.
 */
public class Pallet
{
	/**
	 * Constructs and initializes the pallet.
	 */
	public Pallet(String config)
	{
		next_generic = new Integer(1);
		images = new Hashtable<String,MappedBufferedImage>();
		name = "";
		configfile = config;
		
		if(config == null)
			return;
		
		Parser parser = new Parser(config);
		
		while(parser.HasNextTag())
		{
			Tag tag = parser.GetNext();
			
			if(tag.IsHeader())
			{
				if(tag.GetName().equals("image"))
					next_generic = new ImageMaker(parser).AddImages(images,next_generic);
			}
			else if(tag.IsDescriptor())
			{
				if(tag.GetName().equals("name"))
					name = (String)tag.GetValue();
			}
		}
		
		return;
	}
	
	/**
	 * Adds an image to this pallet. Returns null if [name] was not yet a
	 * bound value or the item previously bound to [name] otherwise.
	 */
	public BufferedImage AddImage(MappedBufferedImage image, String name)
	{
		return images.put(name,image);
	}
	
	/**
	 * Removes and returns the image in this pallet that is bound to [name].
	 * If there is no such binding then null is returned.
	 */
	public MappedBufferedImage RemoveImage(String name)
	{
		return images.remove(name);
	}
	
	/**
	 * Obtains and returns the image that is mapped to [image].
	 * Returns null if there is no image mapped to [image].
	 */
	public MappedBufferedImage GetEntry(String image)
	{
		return images.get(image);
	}
	
	/**
	 * Returns the name of this pallet.
	 */
	public String GetName()
	{
		return name;
	}
	
	/**
	 * Sets the name of this pallet.
	 */
	public void SetName(String n)
	{
		name = n;
		return;
	}
	
	/**
	 * Returns the path to this pallet's config file.
	 */
	public String GetConfigPath()
	{
		return configfile;
	}
	
	/**
	 * Returns all images in this pallet.
	 */
	public ArrayList<MappedBufferedImage> GetAllEntries()
	{
		ArrayList<MappedBufferedImage> ret = new ArrayList<MappedBufferedImage>();
		
		Enumeration<MappedBufferedImage> e = images.elements();
		
		while(e.hasMoreElements())
			ret.add(e.nextElement());
		
		return ret;
	}
	
	/**
	 * Contains all of the images in our pallet.
	 */
	protected Hashtable<String,MappedBufferedImage> images;
	
	/**
	 * Contains the next unused generic name.
	 */
	protected Integer next_generic;
	
	/**
	 * The name of this pallet.
	 */
	protected String name;
	
	/**
	 * Contains the path to the config file of this pallet.
	 */
	protected String configfile;
}
