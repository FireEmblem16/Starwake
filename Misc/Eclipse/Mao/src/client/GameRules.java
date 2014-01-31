package client;

import rules.Rule;
import rules.RuleManager;

public class GameRules
{
	public GameRules()
	{
		Rules = new RuleManager();
		NumOfStartingCards = 0;
	
		return;
	}
	
	public GameRules(String storage)
	{
		Rules = new RuleManager(storage);
		NumOfStartingCards = 0;
	
		return;
	}
	
	public int NumberOfRules()
	{
		return Rules.NumOfRules();
	}
	
	public int RequestStartingCardNumber()
	{
		return NumOfStartingCards;
	}
	
	public Rule RequestRule(int i)
	{
		return Rules.RequestRule(i);
	}
	
	public RuleManager RequestRules()
	{
		return Rules;
	}
	
	public void AddRule(Rule r)
	{
		Rules.AddRule(r);
		
		return;
	}
	
	public void RemoveRule(Rule r)
	{
		Rules.RemoveRule(r.RequestName(),r.RequestRule());
		
		return;
	}
	
	public void SetNumOfStartingCards(int n)
	{
		NumOfStartingCards = n;
		
		return;
	}
	
	public String toString()
	{
		return Rules.toString();
	}
	
	protected int NumOfStartingCards;
	protected RuleManager Rules;
}
