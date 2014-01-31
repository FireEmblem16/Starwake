package gui.eventhandlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Disposes of the given MapEditor.
 */
public class Disposer implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public Disposer(JFrame mainmenu)
	{
		main = mainmenu;
		return;
	}
	
	/**
	 * Disposes of the MapEditor we were given.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(main != null)
			main.dispose();
		
		File base = new File("base.html");
		File pic = new File("picture.html");
		File page = new File("page.html");
		
		if(base.exists())
			base.delete();
		
		if(pic.exists())
			pic.delete();
		
		if(page.exists())
			page.delete();
		
		System.exit(0);
		return;
	}
	
	/**
	 * If this isn't null then we want to dispose of this as well, consequently ending the program.
	 */
	protected JFrame main;
}
