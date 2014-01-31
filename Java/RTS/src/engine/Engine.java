package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import grid.Board;
import grid.Location;
import gui.datadisplay.DataDisplay;
import gui.editor.BoardSettings;
import gui.editor.EditorScrollbarUpdater;
import gui.editor.MapEditor;
import gui.tool.ToolBox;

/**
 * Runs what amounts to a table top RPG.
 */
public class Engine extends JFrame implements Runnable
{
	/**
	 * Runs the application. args[0] should be the path to our game-scope
	 * configuration file. If it is not then we will look for the default file name.
	 * If it does not exist then we will use pure defaults for all configurations.
	 */
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Engine());
		return;
	}
	
	/**
	 * Constructs and initializes this object.
	 */
	public Engine()
	{
		RegisterDefaultGameControls();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		scrollbars = new JScrollPane();
		getRootPane().getContentPane().removeAll();
		add(scrollbars);
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		setSize(425,425);
		setTitle("Virtual Tabletop - A table for every wayward human");
		setVisible(true);
		
		return;
	}
	
	/**
	 * Glues the game elements together.
	 */
	public void run()
	{
		new MapEditor(this);
		return;
	}
	
	/**
	 * Sets up the current default controls for this computer.
	 */
	protected void RegisterDefaultGameControls()
	{
		// We need to define [binds] if it hasn't been made yet
		if(binds == null)
			binds = new KeyBindings();
		
		// Add or change all of our key bindings
		binds.ChangeBinding("Left",KeyEvent.VK_LEFT);
		binds.ChangeBinding("Right",KeyEvent.VK_RIGHT);
		binds.ChangeBinding("Up",KeyEvent.VK_UP);
		binds.ChangeBinding("Down",KeyEvent.VK_DOWN);
		binds.ChangeBinding("B",KeyEvent.VK_Z);
		binds.ChangeBinding("A",KeyEvent.VK_X);
		binds.ChangeBinding("Start",KeyEvent.VK_ENTER);
		binds.ChangeBinding("Select",KeyEvent.VK_SHIFT);
		binds.ChangeBinding("R",KeyEvent.VK_S);
		binds.ChangeBinding("L",KeyEvent.VK_A);
		
		return;
	}
	
	/**
	 * Returns the background timer of this system.
	 */
	public static Timer GetBackgroundClock()
	{
		return backgroundclock;
	}
	
	/**
	 * Initializes the background timer.
	 */
	static
	{
		backgroundclock = new Timer(true);
	}
	
	/**
	 * This contains all of the game's key bindings.
	 */
	protected KeyBindings binds;
	
	/**
	 * Contains the scroll bars for this JFrame window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * This is a thread which runs in the background and will execute tasks
	 * given to it in a timely manner.
	 */
	protected static Timer backgroundclock;
}
