package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import gui.editor.MapEditor;

/**
 * Saves the board currently displayed in a map editor.
 */
public class BoardSaver implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public BoardSaver(MapEditor edit, boolean askname)
	{
		editor = edit;
		ask = askname;
		
		return;
	}
	
	/**
	 * Saves the loaded board.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(editor == null || editor.GetLoadedBoard() == null)
			return;
		
		if(ask)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(false);
			
			String[] extensions = {"board","Board"};
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Board Files",extensions);
			chooser.setFileFilter(filter);
			
			chooser.setCurrentDirectory(new File("."));
			chooser.showOpenDialog(null);
			
			if(chooser.getSelectedFile() == null)
				return;
			
			try
			{
				FileWriter out = new FileWriter(chooser.getSelectedFile().getAbsolutePath());
				editor.GetLoadedBoard().WriteXML(out);
				out.close();
			}
			catch(IOException ee)
			{}
		}
		else
		{
			try
			{
				FileWriter out = new FileWriter(editor.GetLoadedBoard().GetConfigName());
				editor.GetLoadedBoard().WriteXML(out);
				out.close();
			}
			catch(IOException ee)
			{}
		}
		
		return;
	}
	
	/**
	 * Contains the MapEditor this is listening on.
	 */
	protected MapEditor editor;
	
	/**
	 * This is true if we want the user to selected where and what to save the board as.
	 */
	protected boolean ask;
}
