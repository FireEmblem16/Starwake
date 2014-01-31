package gui.eventhandlers;

import gui.TextViewer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import app.KeyBindings;

public class TextViewerListener implements ActionListener
{
	public TextViewerListener(KeyBindings binds, File file)
	{
		view = new TextViewer(binds,file);
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		view.setVisible(true);
		return;
	}
	
	protected TextViewer view;
}