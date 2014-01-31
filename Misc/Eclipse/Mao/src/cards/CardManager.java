package cards;

import java.awt.Image;
import image.ImageFoil;
import image.ImageManager;

public class CardManager
{
	public CardManager()
	{
		if(System.getenv("Debug") == null)
			Gallery = new ImageManager("pics/");
		else if(System.getenv("Debug").equals("true"))
			Gallery = new ImageManager("bin/pics/");
		else
			Gallery = new ImageManager("");
		
		String[] names = {"Ace","King","Queen","Jack","10","9","8","7","6","5","4","3","2"};
		String[] suits = {"Clubs","Spades","Hearts","Dimonds"};
		
		for(int i = 1,n = -1;i < 53;i++)
		{	
			if((i-1) % 4 == 0)
				n++;
			
			if(!Gallery.add("Cards/" + names[n] + " of " + suits[(i-1) % 4] + ".png"))
				System.err.println("Data not found");
		}
		
		return;
	}
	
	public void MoveImage(int i, int x, int y)
	{
		Gallery.MoveImage(i,x,y);
		
		return;
	}
	
	public void MoveImage(String ImgName, int x, int y)
	{
		Gallery.MoveImage(ImgName,x,y);
		
		return;
	}
	
	public Image RequestImage(int i)
	{
		return Gallery.GetImage(i);
	}
	
	public Image RequestImage(String ImgName)
	{
		return Gallery.GetImage(ImgName);
	}
	
	public ImageFoil RequestImageFoil(int i)
	{
		return Gallery.GetImageFoil(i);
	}
	
	public ImageFoil RequestImageFoil(String ImgName)
	{
		return Gallery.GetImageFoil(ImgName);
	}
	
	public void ResizeImage(int i, int width, int height)
	{
		Gallery.ResizeImage(i,width,height);
		
		return;
	}
	
	public void ResizeImage(String ImgName, int width, int height)
	{
		Gallery.ResizeImage(ImgName,width,height);
		
		return;
	}
	
	public String toString()
	{
		return Gallery.toString();
	}
	
	protected ImageManager Gallery;
}
