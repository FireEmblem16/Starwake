package cards;

public class DiscardPile
{
	public DiscardPile(int num)
	{
		Contents = new Card[num];
		NumOfCards = num;
		
		return;
	}
	
	public Card RemoveTop()
	{
		Card CTemp = Contents[0];
		
		for(int i = 1;i < Contents.length;i++)
			Contents[i-1] = Contents[i];
		
		Contents[Contents.length-1] = null;
		
		return CTemp;
	}
	
	public Card RequestCardNumber(int i)
	{
		if(i >= 0 && i < Contents.length)
			return null;
		
		return Contents[i];
	}
	
	public Card[] RequestContents()
	{
		return Contents;
	}
	
	public int NumberOfDiscards()
	{
		int ret = -1;
		
		for(int i = 0;i < Contents.length;i++)
			if(Contents[i] == null)
			{
				ret = i;
				i = Contents.length - 1;
			}
		
		if(ret == -1)
			ret = NumOfCards;
		
		return ret;
	}
	
	public void Add(Card discard)
	{
		for(int i = Contents.length - 1;i > 0;i--)
			Contents[i] = Contents[i-1];
		
		Contents[0] = discard;
		
		return;
	}
	
	public void AddToBottom(Card discard)
	{
		int index = 0;
		for(;index < Contents.length && Contents[index] != null;index++);
		Contents[index] = discard;
		
		return;
	}
	
	public void Empty()
	{
		Contents = new Card[NumOfCards];
		
		return;
	}
	
	protected Card[] Contents;
	protected int NumOfCards;
}
