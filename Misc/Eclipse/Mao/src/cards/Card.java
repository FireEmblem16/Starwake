package cards;

import image.ImageFoil;

public class Card
{
	public Card(ImageFoil card)
	{
		img = card;
		Name = card.Name;
		
		return;
	}
	
	public Card(CardManager cardmngr, String card)
	{
		img = cardmngr.RequestImageFoil(card);
		Name = card;
		
		return;
	}
	
	public void MoveCard(int x, int y)
	{
		img.SetPosition(x,y);
		
		return;
	}
	
	public void ResizeImage(int width, int height)
	{
		img.SetDimensions(width,height);
		
		return;
	}
	
	public ImageFoil RequestComponent()
	{
		return img;
	}
	
	public String toString()
	{
		return Name;
	}
	
	protected ImageFoil img;
	protected String Name;
}
