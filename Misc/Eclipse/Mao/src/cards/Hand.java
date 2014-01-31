package cards;

import image.ImageFoil;

public class Hand
{
	public Hand()
	{
		Cards = new Card[0];
		
		return;
	}
	
	public Card PlayCard(int i)
	{
		if(i < 0 || i > Cards.length - 1)
			return null;
		
		Card CTemp = Cards[i];
		
		for(i++;i < Cards.length;i++)
			Cards[i-1] = Cards[i];
		
		DecArray();
		
		return CTemp;
	}
	
	public int CardsInHand()
	{
		return Cards.length;
	}
	
	public void AddCard(Card c)
	{
		IncArray();
		Cards[Cards.length-1] = c;
		
		return;
	}
	
	public String toString()
	{
		String ret = "";
		
		for(int i = 0;i < Cards.length;i++)
			ret += Cards[i].toString() + "\n";
		
		return ret;
	}
	
	protected void DecArray()
	{
		Card[] ITemp = new Card[Cards.length - 1];
		System.arraycopy(Cards,0,ITemp,0,Cards.length - 1);
		Cards = ITemp;
		
		return;
	}
	
	protected void IncArray()
	{
		Card[] ITemp = new Card[Cards.length + 1];
		System.arraycopy(Cards,0,ITemp,0,Cards.length);
		ITemp[ITemp.length - 1] = null;
		Cards = ITemp;
		
		return;
	}
	
	protected Card[] Cards;
}
