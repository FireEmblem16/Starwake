package app;

import gui.Frame;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Allows the viewing and management of manga.
 */
public class Runner implements Runnable
{
	/**
	 * Entry point.
	 */
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runner());
		return;
	}
	
	/**
	 * Construct everything that runs behind the scenes.
	 */
	public Runner()
	{
		RegisterDefaultControls();
		
		return;
	}
	
	public void run()
	{
		frame = new Frame("MCI Viewer",binds);
		
		return;
	}
	
	/**
	 * Sets up the current default controls for this computer.
	 */
	protected void RegisterDefaultControls()
	{
		// We need to define [binds] if it hasn't been made yet
		if(binds == null)
			binds = new KeyBindings();
		
		// Add or change all of our key bindings
		binds.ChangeBinding("Left",KeyEvent.VK_LEFT);
		binds.ChangeBinding("Right",KeyEvent.VK_RIGHT);
		binds.ChangeBinding("Down",KeyEvent.VK_DOWN);
		binds.ChangeBinding("Up",KeyEvent.VK_UP);
		binds.ChangeBinding("Home",KeyEvent.VK_HOME);
		binds.ChangeBinding("End",KeyEvent.VK_END);
		binds.ChangeBinding("Escape",KeyEvent.VK_ESCAPE);
		binds.ChangeBinding("Page Down",KeyEvent.VK_PAGE_DOWN);
		binds.ChangeBinding("Page Up",KeyEvent.VK_PAGE_UP);
		binds.ChangeBinding("Enter",KeyEvent.VK_ENTER);
		binds.ChangeBinding("Zoom In",107);
		binds.ChangeBinding("Zoom Out",109);
		binds.ChangeBinding("Bookmark",KeyEvent.VK_B);
		binds.ChangeBinding("Bookmarks",KeyEvent.VK_M);
		binds.ChangeBinding("NSFW",KeyEvent.VK_H);
		binds.ChangeBinding("New",KeyEvent.VK_N);
		binds.ChangeBinding("Delete",KeyEvent.VK_DELETE);
		//binds.ChangeBinding("Rotate",KeyEvent.VK_R);
		//binds.ChangeBinding("Reflect V",KeyEvent.VK_V);
		//binds.ChangeBinding("Reflect H",KeyEvent.VK_C);
		
		return;
	}
	
	static
	{
		File out = new File("stdout");
		File err = new File("stderr");
		
		if(!out.exists())
			try{out.createNewFile();}catch(IOException e){}
		
		if(!err.exists())
			try{err.createNewFile();}catch(IOException e){}
		
		PrintStream p1 = null;
		PrintStream p2 = null;
			
		try
		{
			p1 = new PrintStream(new File("stdout"));
			p2 = new PrintStream(new File("stderr"));
		}
		catch(FileNotFoundException e)
		{}
		
		stdout = p1;
		stderr = p2;
	}
	
	/**
	 * Replacement for stdout.
	 */
	public static final PrintStream stdout;
	/**
	 * Replacement for stderr.
	 */
	public static final PrintStream stderr;
	
	/**
	 * This contains all of the key bindings.
	 */
	protected KeyBindings binds;
	
	/**
	 * This is where everything will be displayed.
	 */
	protected Frame frame;
}
