package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import gui.editor.MapEditor;

/**
 * Opens a board and displays in in a MapEditor.
 */
public class BoardOpener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public BoardOpener(MapEditor edit)
	{
		editor = edit;
		return;
	}
	
	/**
	 * Opens a board as choosen from a file chooser.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(editor == null)
			return;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		
		String[] extensions = {"board","Board"};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Board Files",extensions);
		chooser.setFileFilter(filter);
		
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		
		if(chooser.getSelectedFile() == null)
			return;
		
		editor.LoadBoard(chooser.getSelectedFile().getAbsolutePath());
		return;
	}
	
	/**
	 * Contains the MapEditor this is listening on.
	 */
	protected MapEditor editor;
}
