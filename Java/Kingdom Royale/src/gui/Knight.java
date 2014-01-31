package gui;

public class Knight extends Class
{
	public Knight()
	{
		super();
		murder = false;
		target = null;
		has_murdered = false;
		
		return;
	}
	
	public boolean murder;
	public String[] target;
	public boolean has_murdered;
}
