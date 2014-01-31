package gui;

import girly.GirlyFetcher;
import gui.eventhandlers.DefaultChooser;
import gui.eventhandlers.Disposer;
import gui.eventhandlers.LinkOpener;
import gui.eventhandlers.LogMover;
import gui.eventhandlers.Opener;
import gui.eventhandlers.TextViewerListener;
import gui.eventhandlers.ZoomListener;
import img.MappedBufferedImage;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import mangafox.MangaFoxDestinationChooser;
import mangafox.MangaFoxMangaAdder;
import mangafox.MangaFoxMangaRemover;
import mangafox.MangaFoxUpdater;
import misfile.MisfileFetcher;
import app.KeyBindings;

/**
 * Allows the manipulation of a manga mangager.
 */
public class Frame extends JFrame implements WindowListener, KeyListener, ComponentListener
{
	/**
	 * Construct this object.
	 */
	public Frame(String title, KeyBindings binds)
	{
		super(title);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		scrollbars = new JScrollPane();
		scrollbars.getViewport().setBackground(Color.BLACK);
		
		getContentPane().removeAll();
		getContentPane().add(scrollbars);
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		this.binds = binds;
		fullscreen = false;
		
		LoadSettings();
		CreateMenu();
		
		if(start_maximized)
			setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		
		setSize(win_size);
		setVisible(true);
		
		addKeyListener(this);
		addWindowListener(this);
		addComponentListener(this);
		
		displaying = -1;
		displaythese = null;
		
		return;
	}
	
	/**
	 * Loads the settings for this window.
	 */
	protected void LoadSettings()
	{
		try
		{
			Scanner in = new Scanner(new File("settings"));
			
			start_maximized = true;
			
			if(in.hasNext())
				if(!"M".equals(in.nextLine()))
					start_maximized = false;
			
			win_size = new Dimension();
			
			if(in.hasNextInt())
				win_size.height = in.nextInt();
			else
				win_size.height = 480;
			
			if(in.hasNextInt())
				win_size.width = in.nextInt();
			else
				win_size.width = 600;
			
			in.nextLine();
			manga_fox_destination = "";
			
			if(in.hasNextLine())
				manga_fox_destination = in.nextLine();
			
			default_dir = null;
			
			if(in.hasNextLine())
				default_dir = new File(in.nextLine());
			
			if(default_dir.getPath().equals(""))
				default_dir = null;
			
			in.close();
		}
		catch(IOException e)
		{
			start_maximized = true;
			win_size = new Dimension(600,480);
			manga_fox_destination = "";
			default_dir = new File(".");
		}
		
		return;
	}
	
	/**
	 * Writes all of the settings out.
	 */
	protected void WriteSettings()
	{
		try
		{
			FileWriter out = new FileWriter(new File("settings"));
			
			out.write((((getExtendedState() & MAXIMIZED_BOTH) != 0) ? "M" : "m") + "\n");
			out.write(new Integer(getSize().height).toString() + " " + new Integer(getSize().width).toString() + "\n");
			out.write(manga_fox_destination + "\n");
			
			String loc = default_dir == null ? "" : default_dir.getPath();
			
			while(loc.indexOf((int)'\\') != -1)
				loc = loc.substring(0,loc.indexOf((int)'\\')) + "/" + loc.substring(loc.indexOf((int)'\\') + 1);
			
			out.write(loc + "\n");
			
			out.close();
		}
		catch(IOException e)
		{}
		
		return;
	}
	
	/**
	 * Creates the menu.
	 */
	protected void CreateMenu()
	{
		JMenuBar menu = new JMenuBar();
		
		JMenu m = null;
		JMenuItem mi = null;
		
		m = new JMenu("File");
		
		mi = new JMenuItem("Open");
		mi.addActionListener(new Opener(this,binds));
		m.add(mi);
		
		mi = new JMenuItem("Choose Default Directory");
		mi.addActionListener(new DefaultChooser(this));
		m.add(mi);
		
		mi = new JMenuItem("Move Log Root Pointer");
		mi.addActionListener(new LogMover());
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("Exit");
		mi.addActionListener(new Disposer(this));
		m.add(mi);
		
		menu.add(m);
		
		m = new JMenu("Zoom");
		
		mi = new JMenuItem("Zoom Out : -");
		mi.addActionListener(new ZoomListener(this,false,1.0f / 1.1f));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("10%");
		mi.addActionListener(new ZoomListener(this,true,0.1f));
		m.add(mi);
		
		mi = new JMenuItem("25%");
		mi.addActionListener(new ZoomListener(this,true,0.25f));
		m.add(mi);
		
		mi = new JMenuItem("50%");
		mi.addActionListener(new ZoomListener(this,true,0.5f));
		m.add(mi);
		
		mi = new JMenuItem("75%");
		mi.addActionListener(new ZoomListener(this,true,0.75f));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("100%");
		mi.addActionListener(new ZoomListener(this,true,0.0f));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("125%");
		mi.addActionListener(new ZoomListener(this,true,1.25f));
		m.add(mi);
		
		mi = new JMenuItem("150%");
		mi.addActionListener(new ZoomListener(this,true,1.5f));
		m.add(mi);
		
		mi = new JMenuItem("175%");
		mi.addActionListener(new ZoomListener(this,true,1.75f));
		m.add(mi);
		
		mi = new JMenuItem("200%");
		mi.addActionListener(new ZoomListener(this,true,2.0f));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("Zoom In : +");
		mi.addActionListener(new ZoomListener(this,false,1.1f));
		m.add(mi);
		
		menu.add(m);
		
		m = new JMenu("Manga Fox");
		
		mi = new JMenuItem("Fetch Manga");
		mi.addActionListener(new MangaFoxUpdater(manga_fox_destination));
		m.add(mi);
		
		mi = new JMenuItem("Save To");
		mi.addActionListener(new MangaFoxDestinationChooser(this));
		m.add(mi);
		
		m.addSeparator();
		
		mi = new JMenuItem("Add Manga");
		mi.addActionListener(new MangaFoxMangaAdder(manga_fox_destination));
		m.add(mi);
		
		mi = new JMenuItem("Remove Manga");
		mi.addActionListener(new MangaFoxMangaRemover());
		m.add(mi);
		
		menu.add(m);
		
		m = new JMenu("Comics");
		
		mi = new JMenuItem("Fetch Girly");
		mi.addActionListener(new GirlyFetcher(this));
		m.add(mi);
		
		mi = new JMenuItem("Fetch Misfile");
		mi.addActionListener(new MisfileFetcher(this));
		m.add(mi);
		
		menu.add(m);
		
		m = new JMenu("Links");
		
		mi = new JMenuItem("New");
		mi.addActionListener(new LinkOpener("Logs/New",this,binds));
		m.add(mi);
		
		mi = new JMenuItem("Bookmarks");
		mi.addActionListener(new LinkOpener("Logs/Bookmarks",this,binds));
		m.add(mi);
		
		menu.add(m);
		
		m = new JMenu("Info");
		
		mi = new JMenuItem("View Log");
		mi.addActionListener(new TextViewerListener(binds,new File("stdout")));
		m.add(mi);
		
		mi = new JMenuItem("View Error");
		mi.addActionListener(new TextViewerListener(binds,new File("stderr")));
		m.add(mi);
		
		menu.add(m);
		
		setJMenuBar(menu);
		menu.setVisible(true);
		return;
	}
	
	/**
	 * We need this so that the toolbar can change the mangafox save location.
	 */
	public void ChangeMangaFoxSaveLocation(String dest)
	{
		manga_fox_destination = dest;
		return;
	}
	
	/**
	 * We need this so that the toolbar can access this when trying to change the mangafox save location.
	 */
	public String GetMangaFoxSaveLocation()
	{
		return manga_fox_destination;
	}
	
	/**
	 * Queues a list of images to display. If [images] is null no changes are made.
	 */
	public void DisplayImages(File[] images)
	{
		if(images == null || images.length == 0)
			return;
		
		try
		{
			displaythese = images;
			DisplayImage(ImageIO.read(displaythese[0]),true,0.0f);
			displaying = 0;
			
			cur_rotation = 0;
			flip_h = false;
			flip_v = false;
			
			setTitle("MCI Viewer - 1 / " + images.length);
		}
		catch(IOException e)
		{}
		
		return;
	}
	
	/**
	 * Displays an image. If [img] is null no change is made.
	 */
	public void DisplayImage(BufferedImage img, boolean reset_zoom, float zoom_in)
	{
		if(img == null)
			return;
		
		BufferedImage f = null;
		draw = img;
		
		if(reset_zoom)
			if(zoom_in == 0.0f)
				zoom = 1.0f;
			else
				zoom = zoom_in;
		else
			zoom *= zoom_in;
		
		if(fullscreen)
		{
			int H = scrollbars.getHeight() - 3;
			int W = scrollbars.getWidth() - 3;
			
			float WR = img.getWidth() / (float)W;
			float HR = img.getHeight() / (float)H;
			
			if(HR > WR)
				f = ResizeImage(img,(int)(img.getWidth() / HR * zoom),(int)(H * zoom));
			else if(WR > HR)
				f = ResizeImage(img,(int)(W * zoom),(int)(img.getHeight() / WR * zoom));
		}
		else if(zoom > 0.99f && zoom < 1.01f)
			f = draw;
		else
			f = ResizeImage(img,(int)(img.getWidth() * zoom),(int)(img.getHeight() * zoom));
		
		MappedBufferedImage map_img = new MappedBufferedImage("",f,null);
		
		// These return [this]
		if(flip_h)
			map_img.Flip("horizontal");
		
		if(flip_v)
			map_img.Flip("vertical");
		
		// This one returns a new image
		map_img = map_img.Rotate(cur_rotation);
		
		p = new ImagePanel(map_img);
		
		int X = scrollbars.getHorizontalScrollBar().getValue();
		int Y = scrollbars.getVerticalScrollBar().getValue();
		
		scrollbars.getViewport().removeAll();
		GroupLayout layout = new GroupLayout(scrollbars.getViewport());
		scrollbars.getViewport().setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		Horizontal.addGap(scrollbars.getWidth() / 2 - p.GetImageWidth() / 2 < 0 ? 0 : scrollbars.getWidth() / 2 - p.GetImageWidth() / 2);
		Horizontal.addComponent(p,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		Vertical.addGap(scrollbars.getHeight() / 2 - p.GetImageHeight() / 2 < 0 ? 0 : scrollbars.getHeight() / 2 - p.GetImageHeight() / 2);
		Vertical.addComponent(p,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		layout.setHorizontalGroup(Horizontal);
		layout.setVerticalGroup(Vertical);
		
		scrollbars.getHorizontalScrollBar().setValue(X);
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		repaint();
		return;
	}
	
	/**
	 * Returns the image currently being displayed.
	 */
	public BufferedImage GetCurrentImage()
	{
		return draw;
	}
	
	/**
	 * Resizes an image.
	 */
	protected BufferedImage ResizeImage(BufferedImage img, int width, int height)
	{
		BufferedImage rimg = new BufferedImage(width,height,img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType());
		Graphics2D g = rimg.createGraphics();
		g.drawImage(img,0,0,width,height,null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		return rimg;
	}
	
	/**
	 * Returns the parent of the current directory being viewed.
	 */
	public File GetCurrentDirectoryParent()
	{
		if(displaythese == null || displaythese.length <= displaying || displaying < 0 || displaythese[displaying] == null || displaythese[displaying].getParentFile() == null)
			return null;
		
		return displaythese[displaying].getParentFile().getParentFile();
	}
	
	/**
	 * Returns the current directory being viewed.
	 */
	public File GetCurrentDirectory()
	{
		if(displaythese == null || displaythese.length <= displaying || displaying < 0)
			return null;
		
		return displaythese[displaying].getParentFile();
	}
	
	/**
	 * Sets the default directory.
	 */
	public void SetDefaultDirectory(File f)
	{
		default_dir = f;
		return;
	}
	
	/**
	 * Returns a directory that represents the home directory.
	 */
	public File GetDefaultDirectory()
	{
		if(default_dir == null || !default_dir.exists())
			return new File(System.getenv("USERPROFILE"));
		
		return default_dir;
	}
	
	/**
	 * Handles key actions.
	 */ 
	public void keyPressed(KeyEvent e)
	{
		if("Left".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			displaying--;
			
			cur_rotation = 0;
			flip_h = false;
			flip_v = false;
			
			if(displaythese != null)
				setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
			
			if(displaythese != null && displaying > -1 && displaying < displaythese.length)
			{
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
			else
			{
				displaying++;
				
				if(displaythese != null)
					setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
				
				new Opener(this,binds).actionPerformed(null);
			}
		}
		else if("Page Up".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			int p = displaythese.length / 20;
			displaying -= p;
			
			cur_rotation = 0;
			flip_h = false;
			flip_v = false;
			
			if(displaythese != null)
				setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
			
			if(displaythese != null && displaying > -1 && displaying < displaythese.length)
			{
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
			else
			{
				displaying = 0;
				
				if(displaythese != null)
					setTitle("MCI Viewer - 1 / " + displaythese.length);
				
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
		}
		else if("End".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			if(displaythese != null)
			{
				displaying = displaythese.length - 1;
				
				cur_rotation = 0;
				flip_h = false;
				flip_v = false;

				if(displaythese != null)
					setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
				
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
		}
		else if("Right".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			displaying++;
			
			cur_rotation = 0;
			flip_h = false;
			flip_v = false;

			if(displaythese != null)
				setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
			
			if(displaythese != null && displaying > -1 && displaying < displaythese.length)
			{
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
			else
			{
				displaying--;

				if(displaythese != null)
					setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
				
				new Opener(this,binds).actionPerformed(null);	
			}
		}
		else if("Page Down".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			int p = displaythese.length / 20;
			displaying += p;
			
			cur_rotation = 0;
			flip_h = false;
			flip_v = false;
			
			if(displaythese != null)
				setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
			
			if(displaythese != null && displaying > -1 && displaying < displaythese.length)
			{
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
			else
			{
				displaying = displaythese.length - 1;
				
				if(displaythese != null)
					setTitle("MCI Viewer - " +  (displaying + 1) + " / " + displaythese.length);
				
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
		}
		else if("Home".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			if(displaythese != null)
			{
				displaying = 0;
				
				cur_rotation = 0;
				flip_h = false;
				flip_v = false;

				if(displaythese != null)
					setTitle("MCI Viewer - 1 / " + displaythese.length);
				
				try
				{
					DisplayImage(ImageIO.read(displaythese[displaying]),true,0.0f);
				}
				catch(IOException err)
				{}
			}
		}
		else if("Zoom In".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			DisplayImage(draw,false,1.1f);
		else if("Zoom Out".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			DisplayImage(draw,false,1.0f / 1.1f);
		else if("Enter".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			if(fullscreen)
			{
				fullscreen = false;
				
				removeNotify();
				setUndecorated(false);
				
				if(!start_maximized)
					setExtendedState(getExtendedState() & (~MAXIMIZED_BOTH));
				
				// This weird thing happens to be a java problem, not a problem with my code
				setSize(win_size);
				
				addNotify();
				getJMenuBar().setVisible(true);
			}
			else
			{
				// We can use start_maximized as our storage state since we will always write out to start maximized without using that variable 
				fullscreen = true;
				start_maximized = ((getExtendedState() & MAXIMIZED_BOTH) != 0);
				
				if(!start_maximized)
					win_size = getSize();
				
				removeNotify();
				
				setUndecorated(true);
				setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
				
				addNotify();
				getJMenuBar().setVisible(false);
			}
		else if("Escape".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			WriteSettings();
			new Disposer(this).actionPerformed(null); // WindowClosing will not catch this
		}
		else if("NSFW".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			setState((getState() & Frame.ICONIFIED) == 0 ? Frame.ICONIFIED : getState() & ~Frame.ICONIFIED);
		else if("Bookmark".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			String book = GetCurrentDirectory().getPath();
			
			while(book.indexOf((int)'\\') != -1)
				book = book.substring(0,book.indexOf((int)'\\')) + "/" + book.substring(book.indexOf((int)'\\') + 1);
			
			if(GetCurrentDirectory() != null && !CheckLog("Bookmarks",book))
			{
				File[] files = GetCurrentDirectory().listFiles(new FileFilter()
				{
					public boolean accept(File file)
					{
						if(file.isDirectory())
							return false;
						
						String name = file.getName();
						
						if(!name.contains("."))
							return false;
						
						String ext = name.substring(name.lastIndexOf(".") + 1);
						ext = ext.toLowerCase();
						
						if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("tiff") || ext.equals("ppm") || ext.equals("bmp") || ext.equals("gif") || ext.equals("png"))
							return true;
						
						return false;
					}
				});
				
				if(files != null && files.length > 0)
					Log("Bookmarks",book);
			}
		}
		else if("Rotate".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			cur_rotation += 90;
			cur_rotation = cur_rotation % 360;
			
			DisplayImage(draw,false,1.0f);
		}
		else if("Reflect H".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			flip_h = !flip_h;
			DisplayImage(draw,false,1.0f);
		}
		else if("Reflect V".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			flip_v = !flip_v;
			DisplayImage(draw,false,1.0f);
		}
		else if("New".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			new LinkOpener("Logs/New",this,binds).actionPerformed(null);
		else if("Bookmarks".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			new LinkOpener("Logs/Bookmarks",this,binds).actionPerformed(null);
		
		return;
	}
	
	/**
	 * Does nothing.
	 */ 
	public void keyTyped(KeyEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */ 
	public void keyReleased(KeyEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void windowActivated(WindowEvent e)
	{return;}

	/**
	 * Does nothing.
	 */
	public void windowClosed(WindowEvent e)
	{return;}

	/**
	 * Makes sure we have our settings right for next time.
	 */
	public void windowClosing(WindowEvent e)
	{
		WriteSettings();
		return;
	}

	/**
	 * Does nothing.
	 */
	public void windowDeactivated(WindowEvent e)
	{return;}

	/**
	 * Does nothing.
	 */
	public void windowDeiconified(WindowEvent e)
	{return;}

	/**
	 * Does nothing.
	 */
	public void windowIconified(WindowEvent e)
	{return;}

	/**
	 * Does nothing.
	 */
	public void windowOpened(WindowEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void componentHidden(ComponentEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void componentMoved(ComponentEvent e)
	{return;}
	
	/**
	 * Makes sure that the image is displayed in the middle of the window.
	 */
	public void componentResized(ComponentEvent e)
	{
		if(draw != null)
			DisplayImage(draw,false,1.0f);
		
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void componentShown(ComponentEvent e)
	{return;}
	
	/**
	 * Checks if [text] occurs in [Name].
	 */
	public boolean CheckLog(String Name, String text)
	{
		try
		{
			FileReader file = new FileReader(new File("Logs/" + Name));
			BufferedReader in = new BufferedReader(file);
			
			String next = in.readLine();
			
			while(next != null && !next.equals(text))
				next = in.readLine();
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
			
			if(next != null && next.equals(text))
				return true;
		}
		catch(IOException e)
		{}
		
		return false;
	}
	
	/**
	 * Adds [text] to [Name] in sorted order.
	 */
	public void Log(String Name, String text)
	{
		try
		{
			FileReader file = new FileReader(new File("Logs/" + Name));
			Scanner in = new Scanner(file);
			
			ArrayList<String> File = new ArrayList<String>();
			
			while(in.hasNextLine())
				File.add(in.nextLine());
			
			File.add(text);
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
			
			Object[] strs = File.toArray();
			Arrays.sort(strs);
			
			FileWriter file2 = new FileWriter(new File("Logs/" + Name));
			BufferedWriter out = new BufferedWriter(file2);
			
			if(out == null)
				return;
			
			for(int i = 0;i < strs.length;i++)
			{
				out.write(strs[i].toString());
				out.newLine();
			}
			
			try
			{
				out.close();
				file2.close();
			}
			catch(IOException e)
			{}
		}
		catch(IOException e)
		{}
		
		return;
	}
	
	/**
	 * Contains the scroll bars for this window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * If true we should start with the window maximized.
	 */
	protected boolean start_maximized;
	
	/**
	 * This is the default size for the window.
	 */
	protected Dimension win_size;
	
	/**
	 * This contains all of the key bindings.
	 */
	protected KeyBindings binds;
	
	/**
	 * This is our fullscreen status.
	 */
	protected boolean fullscreen;
	
	/**
	 * We want to draw this image.
	 */
	protected BufferedImage draw;
	
	/**
	 * This is the image we will be drawing.
	 */
	protected ImagePanel p;
	
	/**
	 * This is our zoom factor.
	 */
	protected float zoom;
	
	/**
	 * This is where our Manga Fox spider will save manga to.
	 */
	protected String manga_fox_destination;
	
	/**
	 * We want to display all of these images.
	 */
	protected File[] displaythese;
	
	/**
	 * We are displaying the image in this index.
	 */
	protected int displaying;
	
	/**
	 * The current rotation the image is at.
	 */
	protected int cur_rotation;
	
	/**
	 * True if we need to reflect across the horizontal.
	 */
	protected boolean flip_h;
	
	/**
	 * True if we need to reflect across the vertical.
	 */
	protected boolean flip_v;
	
	/**
	 * This is the default directory for anything that doesn't have it's own default.
	 */
	protected File default_dir;
}
