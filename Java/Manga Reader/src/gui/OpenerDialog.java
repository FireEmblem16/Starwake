package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import app.KeyBindings;

public class OpenerDialog extends JDialog implements KeyListener, WindowListener
{
	public OpenerDialog(Frame displayto, KeyBindings binds)
	{
		super(displayto,"Folder Browser",true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		scrollbars = new JScrollPane();
		scrollbars.getViewport().setBackground(Color.LIGHT_GRAY);
		scrollbars.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
		
		getContentPane().removeAll();
		getContentPane().add(scrollbars);
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		host = displayto;
		this.binds = binds;
		ready = false;
		
		curdir = host.GetCurrentDirectory();
		nextdir = null;
		
		filter = new FileFilter()
		{
			public boolean accept(File file)
			{
				if(file.isDirectory())
					return true;
				
				String name = file.getName();
				
				if(!name.contains("."))
					return false;
				
				String ext = name.substring(name.lastIndexOf(".") + 1);
				ext = ext.toLowerCase();
				
				if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("tiff") || ext.equals("ppm") || ext.equals("bmp") || ext.equals("gif") || ext.equals("png"))
					return true;
				
				return false;
			}
		};
		
		filter2 = new FileFilter()
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
		};
		
		listing = GetListings();
		selected = 0;
		
		DisplayListing();
		
		addKeyListener(this);
		addWindowListener(this);
		
		setTitle("Folder Browser");
		setSize(300,500);
		setResizable(false);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 300) / 2,(Toolkit.getDefaultToolkit().getScreenSize().height - 500) / 2);
		
		setVisible(true);
		return;
	}
	
	public void DisplayListing()
	{
		panel = new Container();
		
		panel.removeAll();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		Horizontal.addGap(5);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		Vertical.addGap(7);
		
		labels = new JLabel[listing.size()];
		
		for(int i = 0;i < listing.size();i++)
		{
			labels[i] = new JLabel();
			labels[i].setFont(new Font("Times New Roman",Font.PLAIN,12));
			
			String data = listing.get(i);
			
			FontMetrics metrics = new FontMetrics(labels[i].getFont()){};
			Rectangle2D bounds = metrics.getStringBounds(data,null);   
			int pixwidth = (int)bounds.getWidth();
			
			if(pixwidth > 255)
			{
				// We know the string is too big so we must chop off at least one character
				int j = data.length() > 99 ? 100 : data.length();
				while((int)metrics.getStringBounds(data.substring(0,--j),null).getWidth() > 255);
				
				data = data.substring(0,j);
				
				while(data.charAt(data.length() - 1) == ' ')
					data = data.substring(0,data.length() - 1);
				
				data += "…";
			}
			
			labels[i].setText(data);
			
			h1.addComponent(labels[i],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addComponent(labels[i],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGap(3);
		}
		
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		layout.setVerticalGroup(Vertical);
		
		scrollbars.getViewport().removeAll();
		scrollbars.getViewport().add(panel);
		
		repaint();
		return;
	}
	
	public boolean Ready()
	{
		return ready;
	}
	
	public File GetChoosenDirectory()
	{
		return nextdir;
	}
	
	protected ArrayList<String> GetListings()
	{
		if(curdir == null)
			curdir = host.GetCurrentDirectory();
		
		if(curdir == null)
			curdir = host.GetDefaultDirectory();
		
		if(curdir == null)
			curdir = new File(".");
		
		File[] files = curdir.listFiles(filter);
		
		ArrayList<String> dirs = new ArrayList<String>();
		ArrayList<File> fs = new ArrayList<File>();
		
		for(int i = 0;i < files.length;i++)
			if(files[i].isDirectory())
			{
				dirs.add("[?] " + files[i].getName());
			}
			else
				fs.add(files[i]);
		
		dirs.add(0,"[..]");
		dirs.add(0,"[" + new Integer(fs.size()).toString() + "] " + "[.]");
		
		return dirs;
	}
	
	/**
	 * Does nothing.
	 */ 
	public void keyPressed(KeyEvent e)
	{
		boolean downwards = false;
		boolean actioned = true;
		
		if("Down".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			selected++;
			downwards = true;
		}
		else if("Page Down".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			selected += 10;
			downwards = true;
		}
		else if("End".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			selected = listing.size() - 1;
			downwards = true;
		}
		else if("Up".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			selected--;
		else if("Page Up".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			selected -= 10;
		else if("Home".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			selected = 0;
		else if("Left".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			// DisplayListing will repaint for us if it matters and if we don't call it then we won't
			actioned = false;
			
			File f = curdir.getParentFile();
			
			// Returns null if [curdir] was already at the top level
			if(f != null)
			{
				File[] files = curdir.listFiles(filter2);
				String match = "[?] " + curdir.getName();
				curdir = f;
				
				listing = GetListings();
				
				for(selected = listing.size() - 1;selected > 0;selected--)
					if(match.equals(listing.get(selected)))
						break;
				
				DisplayListing();
			}
		}
		else if("Right".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			// DisplayListing will repaint for us if it matters and if we don't call it then we won't
			actioned = false;
			
			if(selected == 0)
			{
				String str = listing.get(0).substring(1,listing.get(0).indexOf("]"));
				int size = Integer.parseInt(str);
				
				if(size != 0)
				{
					nextdir = curdir;
					
					ready = true;
					setModal(false);
					setVisible(false);
				}
			}
			else if(selected == 1)
			{
				e.setKeyCode(binds.GetBinding("Left"));
				keyPressed(e);
				return;
			}
			else
			{
				curdir = new File(curdir.getPath() + "/" + listing.get(selected).substring(listing.get(selected).indexOf("]") + 2));
				
				listing = GetListings();
				selected = 0;
				
				DisplayListing();
			}
		}
		else if("Escape".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			ready = true;
			setModal(false);
			setVisible(false);
			
			return;
		}
		else
			actioned = false;
		
		selected = selected > listing.size() - 1 ? listing.size() - 1 : selected;
		selected = selected < 0 ? 0 : selected;
		
		Rectangle scrollwin = new Rectangle(0,scrollbars.getVerticalScrollBar().getValue(),scrollbars.getWidth() - 14,scrollbars.getHeight());
		Rectangle bar = new Rectangle(4,6 + 17 * selected,267,16);
		int Y = 0;
		
		if(scrollwin.contains(bar))
			Y = scrollbars.getVerticalScrollBar().getValue();
		else if(downwards)
			Y = (selected - 26) * 17;
		else
			Y = selected * 17;
			
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		if(actioned)
		{
			if(labels[selected].getText().charAt(1) == '?')
			{
				String str = labels[selected].getText();
				str = str.replace("[?]","[" + (curdir.listFiles(filter)[selected - 2].listFiles(filter2) == null ? 0 : curdir.listFiles(filter)[selected - 2].listFiles(filter2).length) + "]");
				labels[selected].setText(str);
			}
			
			repaint();
		}
		
		return;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D)panel.getGraphics();
		
		g2.drawRect(4,6 + 17 * selected,267,16);
		
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
		ready = true;
		setModal(false);
		setVisible(false);
		
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
	 * Contains the actual image data.
	 */
	protected Container panel;
	
	/**
	 * Contains the scroll bars for this window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * This contains all of the key bindings.
	 */
	protected KeyBindings binds;
	
	/**
	 * Where everything gets displayed to.
	 */
	protected Frame host;
	
	/**
	 * Our current location.
	 */
	protected File curdir;
	
	/**
	 * Our choosen location.
	 */
	protected File nextdir;
	
	/**
	 * Let's observers know if we have selected a directory.
	 */
	protected boolean ready;
	
	/**
	 * We will only be able to display files that pass this filter test.
	 */
	protected FileFilter filter;
	
	/**
	 * We will not include directories in this filter.
	 */
	protected FileFilter filter2;
	
	/**
	 * The files currently listed.
	 */
	protected ArrayList<String> listing;
	
	/**
	 * The currently selected item in the listing.
	 */
	protected int selected;
	
	/**
	 * The stuff we will draw.
	 */
	protected JLabel[] labels;
}
