package cards;

import image.ImageFoil;
import java.util.Random;

public class Deck
{
	public Deck()
	{
		CardData = new CardManager();
		Discard = new DiscardPile(NumOfCards);
		Cards = new Card[NumOfCards];
		rand = new Random(System.currentTimeMillis());
		
		Reshuffle();
		
		return;
	}
	
	/**
	 * Removes the top card from the deck and reshuffles the deck if needed.
	 */
	public Card Draw()
	{
		if(Cards[0] == null)
			ShuffleDiscard();
		
		Card CTemp = Cards[0];
		
		for(int i = 1;i < Cards.length;i++)
			Cards[i-1] = Cards[i];
		
		Cards[Cards.length-1] = null;
		
		return CTemp;
	}
	
	public DiscardPile RequestDiscards()
	{
		return Discard;
	}
	
	public ImageFoil RequestCard(int i)
	{
		return CardData.RequestImageFoil(i);
	}
	
	public ImageFoil RequestCard(String name)
	{
		return CardData.RequestImageFoil(name);
	}
	
	public int RemainingCards()
	{
		int ret = -1;
		
		for(int i = 0;i < Cards.length;i++)
			if(Cards[i] == null)
			{
				ret = i;
				i = Cards.length - 1;
			}
		
		if(ret == -1)
			ret = NumOfCards;
		
		return ret;
	}
	
	/**
	 * Renews the deck with a full set of cards randomly distributed.
	 */
	public void Reshuffle()
	{
		Discard.Empty();
		
		for(int i = 0;i < NumOfCards;i++)
		{
			int index = rand.nextInt(NumOfCards);
			
			if(Cards[index] == null)
			{
				Cards[index] = new Card(CardData.RequestImageFoil(i));
				continue;
			}
			
			i--;
		}
		
		return;
	}
	
	/**
	 * Shuffles the cards remaining in the deck.
	 */
	public void Shuffle()
	{
		Card[] CTemp = new Card[Cards.length];
		int NumOfCards = 0;
		
		for(int i = 0;i < Cards.length && Cards[i] != null;i++)
			NumOfCards++;
		
		for(int i = 0;i < NumOfCards;i++)
		{
			int index = rand.nextInt(NumOfCards);
			
			if(CTemp[index] == null)
			{
				CTemp[index] = Cards[i];
				continue;
			}
			
			i--;
		}
		
		for(int i = NumOfCards;i < Cards.length;i++)
			CTemp[i] = Cards[i];
		
		Cards = CTemp;
		
		return;
	}
	
	/**
	 * Shuffles the discard pile into the deck.
	 * Assumes the deck is currently empty.
	 */
	public boolean ShuffleDiscard()
	{
		if(Discard.NumberOfDiscards() == 0)
			Reshuffle();
		
		Card[] CTemp = new Card[Cards.length];
		System.arraycopy(Discard.RequestContents(),0,CTemp,0,this.NumOfCards);
		
		int NumOfCards = 0;
		
		for(int i = 0;i < CTemp.length && CTemp[i] != null;i++)
			NumOfCards++;
		
		for(int i = 0;i < NumOfCards;i++)
		{
			int index = rand.nextInt(NumOfCards);
			
			if(Cards[index] == null)
			{
				Cards[index] = CTemp[i];
				continue;
			}
			
			i--;
		}
		
		for(int i = NumOfCards;i < Cards.length;i++)
			Cards[i] = CTemp[i];
		
		return true;
	}
	
	public String toString()
	{
		String ret = "Remaining Cards: ";
		
		for(int i = 0;i < Cards.length;i++)
			if(Cards[i] == null)
			{
				ret += i + "\n";
				i = Cards.length - 1;
			}
		
		if(ret.length() == 17)
			ret += NumOfCards + "\n";
		
		return ret;
	}
	
	protected Card[] Cards;
	protected CardManager CardData;
	protected DiscardPile Discard;
	protected int NumOfCards = 52;
	protected Random rand;
}
