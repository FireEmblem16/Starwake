package app;

import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox
{
	public CheckBox()
	{
		super();
		Lag = false;
		
		return;
	}
	
	public CheckBox(String name)
	{
		super(name);
		Lag = false;
		
		return;
	}
	
	public boolean IsLaging()
	{
		return Lag;
	}
	
	public void RemoveLag()
	{
		Lag = false;

		return;
	}
	
	public void SetLag()
	{
		Lag = true;

		return;
	}
	
	protected boolean Lag;
}
