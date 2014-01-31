package gui.editor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import gui.editor.MapEditor;

/**
 * Disposes of the given MapEditor.
 */
public class Disposer implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public Disposer(MapEditor edit)
	{
		editor = edit;
		main = null;
		
		return;
	}
	
	/**
	 * Constructs and initializes this object.
	 */
	public Disposer(MapEditor edit, JFrame mainmenu)
	{
		editor = edit;
		main = mainmenu;
		
		return;
	}
	
	/**
	 * Disposes of the MapEditor we were given.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(editor == null)
			return;
		
		editor.dispose();
		
		if(main != null)
			main.dispose();
		
		return;
	}
	
	/**
	 * Contains the MapEditor we want to dispose of.
	 */
	protected MapEditor editor;
	
	/**
	 * If this isn't null then we want to dispose of this as well, consequently ending the program.
	 */
	protected JFrame main;
}
