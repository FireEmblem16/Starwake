package rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import cards.Card;

public class RuleManager
{
	public RuleManager()
	{
		Rules = new Rule[0];
		NumOfRules = 0;
		
		return;
	}
	
	public RuleManager(String storage)
	{
		Rules = new Rule[0];
		NumOfRules = 0;
		
		FileInputStream cin = null;
		Scanner in = null;
		
		try
		{
			cin = new FileInputStream(new File(storage));
			in = new Scanner(cin);
		}
		catch(FileNotFoundException e)
		{
			return;
		}
		
		while(in.hasNextLine())
			AddRule(in.nextLine(),in.nextLine(),Boolean.parseBoolean(in.nextLine()));
		
		try
		{
			in.close();
			cin.close();
		}
		catch(IOException e){}
		
		return;
	}
	
	public int NumOfRules()
	{
		return Rules.length;
	}
	
	public Rule RequestRule(int i)
	{
		if(i < 0 || i > Rules.length - 1)
			return null;
		
		return Rules[i];
	}
	
	public Rule[] RequestRules()
	{
		return Rules;
	}
	
	public void AddRule(Rule r)
	{
		IncArray();
		Rules[Rules.length - 1] = r;
		NumOfRules++;
		
		Sort();
		
		return;
	}
	
	public void AddRule(String name, String clause)
	{
		IncArray();
		Rules[Rules.length - 1] = new Rule(name,clause,false);
		NumOfRules++;
		
		Sort();
		
		return;
	}
	
	public void AddRule(String name, String clause, boolean viewable)
	{
		IncArray();
		Rules[Rules.length - 1] = new Rule(name,clause,viewable);
		NumOfRules++;
		
		Sort();
		
		return;
	}
	
	public void SetViewable(int i, boolean view)
	{
		if(i < 0 || i > Rules.length - 1)
			return;
		
		Rules[i].SetViewable(view);
		
		return;
	}
	
	public void Sort()
	{
		Sort(0,Rules.length - 1);
		
		return;
	}
	
	public void SortAccending()
	{
		Sort();
		
		return;
	}
	
	public void SortDecending()
	{
		SortDecending(0,Rules.length - 1);
		
		return;
	}
	
	public boolean RemoveRule(String name, String clause)
	{
		Rule RTemp = new Rule(name,clause);
		
		for(int i = 0;i < Rules.length - 1;i++)
			if(RTemp.equals(Rules[i]))
			{
				for(int j = i+1;j < Rules.length;j++)
					Rules[j-1] = Rules[j];
				
				DecArray();
				NumOfRules--;
				
				return true;
			}
		
		return false;
	}
	
	public String toString()
	{
		String ret = "";
		
		for(int i = 0;i < Rules.length;i++)
			ret += Rules[i].toString() + "\n";
		
		if(!ret.equals(""))
			ret = ret.substring(0,ret.length() - 1);
		
		return ret;
	}
	
	protected void Sort(int f, int l)
	{
		if(f >= l)
			return;
		
		int s = Partition(f,l);
		Sort(f,s);
		Sort(s + 1,l);
		
		return;
	}
	
	protected void SortDecending(int f, int l)
	{
		if(f >= l)
			return;
		
		int s = PartitionDecending(f,l);
		SortDecending(f,s);
		SortDecending(s + 1,l);
		
		return;
	}
	
	protected int Partition(int f, int l)
	{
		int j = l + 1;
		Rule p = Rules[f], temp = null;
		
		for(int i = f - 1;i < j;)
		{
			i++;
			
			while(Rules[i].GT(p))
				i++;
			
			j--;
			
			while(Rules[j].LT(p))
				j--;
			
			if(i < j)
			{
				temp = Rules[i];
				Rules[i] = Rules[j];
				Rules[j] = temp;
			}
		}
		
		return j;
	}
	
	protected int PartitionDecending(int f, int l)
	{
		int j = l + 1;
		Rule p = Rules[f], temp = null;
		
		for(int i = f - 1;i < j;)
		{
			i++;
			
			while(Rules[i].LT(p))
				i++;
			
			j--;
			
			while(Rules[j].GT(p))
				j--;
			
			if(i < j)
			{
				temp = Rules[i];
				Rules[i] = Rules[j];
				Rules[j] = temp;
			}
		}
		
		return j;
	}
	
	protected void DecArray()
	{
		Rule[] ITemp = new Rule[Rules.length - 1];
		System.arraycopy(Rules,0,ITemp,0,Rules.length - 1);
		Rules = ITemp;
		
		return;
	}
	
	protected void IncArray()
	{
		Rule[] ITemp = new Rule[Rules.length + 1];
		System.arraycopy(Rules,0,ITemp,0,Rules.length);
		ITemp[ITemp.length - 1] = null;
		Rules = ITemp;
		
		return;
	}
	
	protected int NumOfRules;
	protected Rule[] Rules;
}
