package gui.eventhandlers;

import gui.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

public class DefaultChooser implements ActionListener
{
	public DefaultChooser(Frame saveto)
	{
		host = saveto;
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false);
		fc.setCurrentDirectory(host.GetDefaultDirectory() == null ? null : host.GetDefaultDirectory());
		int retVal = fc.showOpenDialog(host);
		
		host.SetDefaultDirectory(fc.getSelectedFile());
		return;
	}
	
	protected Frame host;
}
