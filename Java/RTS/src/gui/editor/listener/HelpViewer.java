package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import gui.editor.MapEditor;

/**
 * Shows the help window.
 */
public class HelpViewer implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public HelpViewer()
	{return;}
	
	/**
	 * Shows the about popup.
	 */
	public void actionPerformed(ActionEvent e)
	{
		JOptionPane.showMessageDialog(null,"http://www.virtualtabletop.com/help\n(c) Copyright 2011, Omacron-NAOMI","Help",JOptionPane.OK_OPTION,new ImageIcon());
		return;
	}
}
