package gui.editor;

import java.awt.Dimension;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import engine.Engine;
import engine.KeyBindings;
import engine.Pointer;
import engine.RunnerTask;
import grid.Board;
import grid.Location;
import gui.datadisplay.DataDisplay;
import gui.editor.listener.AboutViewer;
import gui.editor.listener.BoardCreator;
import gui.editor.listener.BoardOpener;
import gui.editor.listener.BoardRepaintListener;
import gui.editor.listener.BoardSaver;
import gui.editor.listener.BoardSettingsListener;
import gui.editor.listener.Disposer;
import gui.editor.listener.HelpViewer;
import gui.editor.listener.WindowViewer;
import gui.tool.ToolBox;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * Creates an editor to modify maps.
 */
public class MapEditor extends JFrame implements MouseListener, MouseMotionListener, WindowListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public MapEditor(JFrame main_menu)
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		mainmenu = main_menu;
		mainmenu.setVisible(false);
		
		scrollbars = new JScrollPane();
		getRootPane().getContentPane().removeAll();
		add(scrollbars);
		
		scrollbars_updater = new EditorScrollbarUpdater(scrollbars);
		boardeditor = new BoardSettings();
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		CreateMenu();
		LoadBoard((String)null);
		
		setSize(640,480);
		setTitle("Board Editor");
		setVisible(true);
		
		scrollbars.getViewport().addMouseListener(this);
		scrollbars.getViewport().addMouseMotionListener(this);
		addWindowListener(this);
		return;
	}
	
	/**
	 * Creates the menu for the editor.
	 */
	public void CreateMenu()
	{
		menu = new MenuBar();
		
		Menu m = null;
		MenuItem mi = null;
		
		m = new Menu("File");
		
		mi = new MenuItem("New");
		mi.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		mi.addActionListener(new BoardCreator(this,scrollbars_updater));
		m.add(mi);
		
		mi = new MenuItem("Open");
		mi.setShortcut(new MenuShortcut(KeyEvent.VK_O));
		mi.addActionListener(new BoardOpener(this));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new MenuItem("Save");
		mi.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		mi.addActionListener(new BoardSaver(this,false));
		m.add(mi);
		
		mi = new MenuItem("Save As...");
		mi.addActionListener(new BoardSaver(this,true));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new MenuItem("Close Editor");
		mi.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
		mi.addActionListener(new Disposer(this));
		m.add(mi);
		
		mi = new MenuItem("Exit");
		mi.addActionListener(new Disposer(this,mainmenu));
		m.add(mi);
		
		menu.add(m);
		
		m = new Menu("Board");
		
		mi = new MenuItem("Settings");
		mi.addActionListener(new BoardSettingsListener(boardeditor));
		m.add(mi);
		
		mi = new MenuItem("Force Repaint");
		mi.addActionListener(new BoardRepaintListener(board));
		m.add(mi);
		
		menu.add(m);
		
		m = new Menu("View");
		
		mi = new MenuItem("Toolbox");
		mi.addActionListener(new WindowViewer(box == null ? null : box));
		m.add(mi);
		
		mi = new MenuItem("Item Editor");
		mi.addActionListener(new WindowViewer(box == null ? null : box.GetDataDisplay()));
		m.add(mi);
		
		mi = new MenuItem("Image Selector");
		mi.addActionListener(new WindowViewer(box == null ? null : box.GetImageSelector()));
		m.add(mi);
		
		mi = new MenuItem("Image Viewer");
		mi.addActionListener(new WindowViewer(box == null ? null : (box.GetImageSelector() == null ? null : box.GetImageSelector().GetImageViewer())));
		m.add(mi);
		
		menu.add(m);
		
		m = new Menu("Help");
		
		mi = new MenuItem("Help");
		mi.addActionListener(new HelpViewer());
		m.add(mi);
		
		m.addSeparator();
		
		mi = new MenuItem("About");
		mi.addActionListener(new AboutViewer());
		m.add(mi);
		
		System.out.println("MapEditor: CreateMenu(): Add license stuff to the menu");
		
		menu.add(m);
		
		setMenuBar(menu);
		return;
	}
	
	/**
	 * Loads a board as specified by the file [config].
	 */
	public void LoadBoard(String configfile)
	{
		if(configfile == null)
		{
			board = null;
			
			if(box != null)
			{
				box.dispose();
				box = null;
			}
			
			if(boardeditor != null)
				boardeditor.Display(null,null);
			
			scrollbars_updater.SetBoard(null);
			scrollbars_updater.update();
			
			setTitle("Board Editor");
			CreateMenu();
			return;
		}
		
		// We use null for [binds] because we don't want key respones to occur in edit mode
		board = new Board(configfile,null,scrollbars_updater,true);
		
		if(box != null)
			box.dispose();
		
		box = new ToolBox(board);
		box.Display();
		
		if(boardeditor != null)
			boardeditor.Display(board,box.RequestImageSelector());
		
		setTitle("Board Editor - " + board.GetConfigName());
		CreateMenu();
		return;
	}
	
	/**
	 * Loads a board as specified by the [b].
	 */
	public void LoadBoard(Board b)
	{
		board = b;
		
		if(board == null)
		{
			if(box != null)
			{
				box.dispose();
				box = null;
			}
			
			if(boardeditor != null)
				boardeditor.Display(null,null);
			
			scrollbars_updater.SetBoard(null);
			scrollbars_updater.update();
			
			setTitle("Board Editor");
			CreateMenu();
			return;
		}
		
		if(box != null)
			box.dispose();
		
		box = new ToolBox(board);
		box.Display();
		
		if(boardeditor != null)
			boardeditor.Display(board,box.RequestImageSelector());
		
		setTitle("Board Editor - " + board.GetConfigName());
		CreateMenu();
		return;
	}
	
	/**
	 * Returns the board that is currently loaded.
	 */
	public Board GetLoadedBoard()
	{
		return board;
	}
	
	/**
	 * When this window closes we need to bring the main menu back.
	 */
	public void dispose()
	{
		if(box != null)
			box.dispose();
		
		if(boardeditor != null)
			boardeditor.dispose();
		
		super.dispose();
		
		if(mainmenu != null)
			mainmenu.setVisible(true);
		
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void mouseClicked(MouseEvent e)
	{return;}
	
	/**
	 * Uses the selected tool.
	 */
	public void mousePressed(MouseEvent e)
	{
		if(board == null)
			return;
		
		Point p = scrollbars.getViewport().getMousePosition();
		
		// This shouldn't happen
		if(p == null)
			return;
		
		int x;
		int y;
		
		if(board.GetDisplayedMap() != null && board.GetDisplayedMap().IsHex())
		{
			x = 0;
			y = 0;
		}
		else // If the displayed map was null then DoAction will instantly return so the values of x and y are inconsequential
		{
			x = p.x / board.GetCellWidth() + 1;
			y = p.y / board.GetCellHeight() + 1;
		}
		
		// We don't need to check if this is the same as the last one since we are garunteed to have made
		// the concious decision to let the mouse button go and click it again
		last = new Point(x,y);
		
		box.GetSelectedTool().DoAction(board.GetDisplayedMap(),new Location(x,y,board.GetDisplayedMapName()));
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void mouseReleased(MouseEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void mouseEntered(MouseEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void mouseExited(MouseEvent e)
	{return;}
	
	/**
	 * Uses the selected tool if we have entered a square different from the last square we used the tool on.
	 */
	public void mouseDragged(MouseEvent e)
	{
		if(board == null)
			return;
		
		Point p = scrollbars.getViewport().getMousePosition();
		
		// This can happen
		if(p == null)
			return;
		
		int x;
		int y;
		
		if(board.GetDisplayedMap() != null && board.GetDisplayedMap().IsHex())
		{
			x = 0;
			y = 0;
		}
		else // If the displayed map was null then DoAction will instantly return so the values of x and y are inconsequential
		{
			x = p.x / board.GetCellWidth() + 1;
			y = p.y / board.GetCellHeight() + 1;
		}
		
		// Since we are draging we don't want duplicate updates unless we actually leave the square and come back
		if(new Point(x,y).equals(last))
			return;
		
		last = new Point(x,y);
		
		box.GetSelectedTool().DoAction(board.GetDisplayedMap(),new Location(x,y,board.GetDisplayedMapName()));
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void mouseMoved(MouseEvent e)
	{return;}
	
	/**
	 * Gets all windows to be always on top if we have focus.
	 */
	public void windowActivated(WindowEvent e)
	{
		if(box != null && (e.getOppositeWindow() == null || !box.isAlwaysOnTop()))
		{
			box.WindowsToFront();
			requestFocus();
		}
		
		if(boardeditor != null && (e.getOppositeWindow() == null || !boardeditor.isAlwaysOnTop()))
		{
			boardeditor.WindowsToFront();
			requestFocus();
		}
		
		return;
	}

	/**
	 * Does nothing.
	 */
	public void windowClosed(WindowEvent e)
	{return;}

	/**
	 * This is called when the X button is pushed. And thus we need to close the main menu as well.
	 */
	public void windowClosing(WindowEvent e)
	{
		if(mainmenu != null)
		{
			mainmenu.dispose();
			mainmenu = null;
		}
		
		return;
	}

	/**
	 * Gets windows off of the always on to setting when we don't have focus.
	 */
	public void windowDeactivated(WindowEvent e)
	{
		if(e.getOppositeWindow() == null && box != null)
			box.WindowsToBack();
		
		if(e.getOppositeWindow() == null && boardeditor != null)
			boardeditor.WindowsToBack();
		
		return;
	}

	/**
	 * Forces the board displayed to repaint when the window is brought back
	 * to normal from a minimized state. Gets windows back on always on top.
	 */
	public void windowDeiconified(WindowEvent e)
	{
		if(board != null)
			board.ForceRepaint();
		
		windowActivated(e);
		return;
	}

	/**
	 * Gets windows off of always on top.
	 */
	public void windowIconified(WindowEvent e)
	{
		windowDeactivated(e);
		return;
	}

	/**
	 * Does nothing.
	 */
	public void windowOpened(WindowEvent e)
	{return;}
	
	/**
	 * This contians the main menu.
	 */
	protected JFrame mainmenu;
	
	/**
	 * Contains the last location we used a tool on.
	 */
	protected Point last;
	
	/**
	 * Contains the scroll bars for this JDialog window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * Updates the scrollbars when necessary.
	 */
	protected EditorScrollbarUpdater scrollbars_updater;
	
	/**
	 * Contains the board currently being edited.
	 */
	protected Board board;
	
	/**
	 * Contains the tool box which we will use to edit the map.
	 */
	protected ToolBox box;
	
	/**
	 * Contains the menu.
	 */
	protected MenuBar menu;
	
	/**
	 * This is a window that will allow us to edit the hidden properties of a board.
	 */
	protected BoardSettings boardeditor;
}
