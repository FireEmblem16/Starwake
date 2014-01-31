package gameplay;

import image.ImageFoil;
import client.ClientApp;
import cards.Card;
import cards.Deck;
import cards.DiscardPile;
import rules.Rule;
import rules.RuleManager;

public class PlayManager
{
	public PlayManager(ClientApp data)
	{
		client = data;
		
		return;
	}
	
	public ImageFoil RequestCard(int i)
	{
		if(deck == null)
			return null;
		
		return deck.RequestCard(i);
	}
	
	public ImageFoil RequestCard(String name)
	{
		if(deck == null)
			return null;
		
		return deck.RequestCard(name);
	}
	
	public void AddPlayer(String name)
	{
		IncArray();
		Players[Players.length - 1] = new Player(name);
		
		for(int i = 0;i < client.RequestClientRules().RequestStartingCardNumber();i++)
			Players[i].AddCard(Draw());
		
		return;
	}
	
	public void RemovePlayer(String name)
	{
		for(int i = 0;i < Players.length - 1;i++)
			if(Players[i].RequestName().equals(name))
			{
				while(Players[i].CardsInHand() > 0)
					deck.RequestDiscards().AddToBottom(Players[i].Discard(0));
				
				for(int j = i+1;j < Players.length;j++)
				{
					Players[j-1] = Players[j];
					DecArray();
					return;
				}
			}
		
		return;
	}
	
	public void start()
	{
		Rules = new RuleManager();
		deck = new Deck();
		
		Rule[] BasicRules = client.RequestClientRules().RequestRules().RequestRules();
		
		for(int i = 0;i < BasicRules.length;i++)
			Rules.AddRule(BasicRules[i]);
		
		return;
	}
	
	protected Card Draw()
	{
		return deck.Draw();
	}
	
	protected void DecArray()
	{
		Player[] ITemp = new Player[Players.length - 1];
		System.arraycopy(Players,0,ITemp,0,Players.length - 1);
		Players = ITemp;
		
		return;
	}
	
	protected void IncArray()
	{
		Player[] ITemp = new Player[Players.length + 1];
		System.arraycopy(Players,0,ITemp,0,Players.length);
		ITemp[ITemp.length - 1] = null;
		Players = ITemp;
		
		return;
	}
	
	protected ClientApp client;
	protected Deck deck;
	protected Player[] Players;
	protected RuleManager Rules;
}
