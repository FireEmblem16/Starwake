package image.animation;

import xml.*;

/**
 * Contains data on a key frame of an animation.
 */
public class KeyFrame
{
	/**
	 * Creates a new KeyFrame.
	 */
	public KeyFrame(Parser parser)
	{
		boolean end = false;
		lag = 1000; // This is the default time
		
		while(parser.HasNextTag() && !end)
		{
			Tag tag = parser.GetNext();
			
			// If we hit a header then we should be done
			if(tag.IsHeader())
				end = true;
			else if(tag.IsDescriptor())
			{
				if(tag.GetName().equals("pallet-name"))
					palletname = (String)tag.GetValue();
				else if(tag.GetName().equals("pallet"))
					pallet = (String)tag.GetValue();
				else if(tag.GetName().equals("time"))
					lag = (Long)tag.GetValue();
			}
		}
		
		if(lag < 0)
			lag = 0;
		
		return;
	}
	
	/**
	 * Creates a copy of this KeyFrame.
	 */
	public KeyFrame(KeyFrame k)
	{
		if(k == null)
			return;
		
		palletname = k.palletname;
		pallet = k.pallet;
		lag = k.lag;
		
		return;
	}
	
	/**
	 * Returns the name of the image drawn in this KeyFrame.
	 */
	public String GetPalletName()
	{
		return palletname;
	}
	
	/**
	 * Returns the name of the pallet this KeyFrame's image should be in.
	 */
	public String GetPallet()
	{
		return pallet;
	}
	
	/**
	 * Returns the length of time this frame should last.
	 */
	public long GetDuration()
	{
		return lag;
	}
	
	/**
	 * This name of the image we want to display.
	 */
	protected String palletname;
	
	/**
	 * This is the name of the pallet to fetch our image from.
	 */
	protected String pallet;
	
	/**
	 * This is how long we should display this image for.
	 */
	protected long lag;
}
