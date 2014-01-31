package rules;

public class Rule
{
	public Rule(String name, String clause)
	{
		Name = name;
		Info = clause;
		Viewable = false;
		
		return;
	}
	
	public Rule(String name, String clause, boolean openview)
	{
		Name = name;
		Info = clause;
		Viewable = openview;
		
		return;
	}
	
	public boolean CanViewRule()
	{
		return Viewable;
	}
	
	public boolean equals(Rule r)
	{
		if(!Name.equals(r.RequestName()))
			return false;
		
		if(!Info.equals(r.RequestRule()))
			return false;
		
		return true;
	}
	
	public boolean GT(Rule r)
	{
		if(r.Name.compareTo(Name) > 0)
			return true;
		else if(r.Name.compareTo(Name) == 0)
			if(r.Info.compareTo(Info) > 0)
				return true;
			else if(!r.Viewable && Viewable)
				return true;
		
		return false;
	}
	
	public boolean LT(Rule r)
	{
		if(r.Name.compareTo(Name) < 0)
			return true;
		else if(r.Name.compareTo(Name) == 0)
			if(r.Info.compareTo(Info) < 0)
				return true;
			else if(!r.Viewable && Viewable)
				return true;
		
		return false;
	}
	
	public String RequestName()
	{
		return Name;	
	}
	
	public String RequestRule()
	{
		return Info;
	}
	
	public void SetViewable(boolean view)
	{
		Viewable = view;
		
		return;
	}
	
	public String toString()
	{
		if(Viewable)
			return Name + "\n" + Info;
		
		return Name;
	}
	
	protected boolean Viewable;
	protected String Info;
	protected String Name;
}
