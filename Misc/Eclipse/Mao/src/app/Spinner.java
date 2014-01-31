package app;

import javax.swing.JSpinner;
import client.ClientApp;

public class Spinner extends JSpinner
{
	public Spinner(ClientApp app, int init)
	{
		super();

		addMouseListener(app);
		setValue(new Integer(init));
		
		return;
	}
	
	public int RequestValue()
	{
		return Integer.parseInt(getValue().toString());
	}
	
	public void Dec()
	{
		setValue(getPreviousValue());
		
		return;
	}
	
	public void Inc()
	{
		setValue(getNextValue());
		
		return;
	}
	
	public void Set(Object Int)
	{
		setValue(Int);
		
		return;
	}
}
