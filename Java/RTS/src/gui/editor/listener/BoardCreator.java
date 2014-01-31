package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import grid.Board;
import gui.editor.EditorScrollbarUpdater;
import gui.editor.MapEditor;

/**
 * Creates a new board and loads in into the editor.
 */
public class BoardCreator implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public BoardCreator(MapEditor editor, EditorScrollbarUpdater update_scrolls)
	{
		edit = editor;
		update_scrollbars = update_scrolls;
		
		return;
	}
	
	/**
	 * Creates a new board and loads it into [edit].
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(edit == null)
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
		
		String path = chooser.getSelectedFile().getAbsolutePath();
		String pathlower = path.toLowerCase();
		
		if(!pathlower.contains(".board"))
		{
			path += ".board";
			pathlower += ".board";
		}
		else
		{
			pathlower = pathlower.substring(pathlower.indexOf(".board"));
			
			if(!pathlower.equals(".board"))
				path += ".board";
		}
		
		edit.LoadBoard(new Board(new File(path),update_scrollbars));
		return;
	}
	
	/**
	 * This is where we want to laod the map to.
	 */
	protected MapEditor edit;
	
	/**
	 * We'll need to give this to our new board when we create it.
	 */
	protected EditorScrollbarUpdater update_scrollbars;
}
