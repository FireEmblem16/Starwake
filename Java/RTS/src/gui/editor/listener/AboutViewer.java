package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import gui.editor.MapEditor;

/**
 * Shows the about window.
 */
public class AboutViewer implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public AboutViewer()
	{return;}
	
	/**
	 * Shows the about popup.
	 */
	public void actionPerformed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(null,"Virtual Tabletop, a table for every wayward human\n\n" +
										   "http://www.virtualtabletop.com\n(c) Copyright 2011, Omacron-NAOMI",
										   "About",JOptionPane.OK_OPTION,new ImageIcon("images/system/about.png"));
		return;
	}
}
