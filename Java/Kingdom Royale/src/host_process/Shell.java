package host_process;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

public class Shell extends JFrame implements ActionListener
{
	public Shell(String name, Game_Options ops)
	{
		super(name);
		
		game_ops = ops;
		action = "";
		
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		action = e.getActionCommand();
		
		return;
	}
	
	public String GetAction()
	{
		String STemp = action;
		action = "";
		
		return STemp;
	}
	
	public Game_Options game_ops;
	protected String action;
}
