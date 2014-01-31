package app;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import client.ClientApp;

public class Button extends JButton
{
	public Button(ClientApp app, String name, String cmd)
	{
		super(name);
		
		setPreferredSize(new Dimension(75,20));
		setFont(new Font(getFont().getFontName(),Font.PLAIN,10));
		setMargin(new Insets(0,0,0,0));
		setActionCommand(cmd);
		addActionListener(app);
		setEnabled(true);
		
		return;
	}
	
	public Button(ClientApp app, String name, String cmd, String alt)
	{
		super(name);
		
		setPreferredSize(new Dimension(75,20));
		setFont(new Font(getFont().getFontName(),Font.PLAIN,10));
		setMargin(new Insets(0,0,0,0));
		setActionCommand(cmd);
		addActionListener(app);
		setToolTipText(alt);
		setEnabled(true);
		
		return;
	}
}
