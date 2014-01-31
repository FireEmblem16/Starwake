package gameplay;

import cards.Card;
import cards.Hand;

public class Player
{
	public Player(String name)
	{
		Name = name;
		hand = new Hand();
		
		return;
	}
	
	public int CardsInHand()
	{
		return hand.CardsInHand();
	}
	
	public String RequestName()
	{
		return Name;
	}
	
	public void AddCard(Card c)
	{
		hand.AddCard(c);
		
		return;
	}
	
	public Card Discard(int i)
	{
		if(i < 0 || i > hand.CardsInHand() - 1)
			return hand.PlayCard(i);
		
		return null;
	}
	
	public String toString()
	{
		return "Cards in Hand: " + hand.CardsInHand();
	}
	
	protected Hand hand;
	protected String Name;
}
