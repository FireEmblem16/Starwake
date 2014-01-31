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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import app.KeyBindings;

public class LinkOpenerDialog extends JDialog implements KeyListener, WindowListener
{
	public LinkOpenerDialog(String file, Frame displayto, KeyBindings binds)
	{
		super(displayto,"New Browser",true);
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
		
		nextdir = null;
		
		filter = new FileFilter()
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
		
		linkholder = new File(file);
		
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
		
		JLabel[] labels = new JLabel[listing.size()];
		
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
		Scanner in;
		try
		{in = new Scanner(linkholder);}
		catch(IOException e)
		{return new ArrayList<String>();}
		
		ArrayList<String> input = new ArrayList<String>();
		
		while(in.hasNextLine())
			input.add(in.nextLine());
		
		File[] links = new File[input.size()];
		
		for(int i = 0;i < links.length;i++)
		{
			links[i] = new File(input.get(i));
			
			if(!links[i].exists() || !links[i].isDirectory())
				if(!input.get(i).equals(""))
					return new ArrayList<String>();
				else
					links[i] = null;
		}
		
		ArrayList<String> dirs = new ArrayList<String>();
		
		for(int i = 0;i < links.length;i++)
			if(links[i] == null)
				continue;
			else
				dirs.add("[" + (links[i].listFiles(filter) == null ? 0 : links[i].listFiles(filter).length) + "] " + links[i].getName());
		
		in.close();
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
		else if("Right".equals(binds.GetLatestNameOnCode(e.getKeyCode()))) // Left means nothing here
		{
			actioned = false;
			
			String d = GetLog(linkholder.getName(),selected);
			File curdir = null;
			
			if(d != null)
				curdir = new File(d);
			
			if(curdir != null && curdir.exists())
			{
				nextdir = curdir;
				
				ready = true;
				setModal(false);
				setVisible(false);
			}
		}
		else if("Escape".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			ready = true;
			setModal(false);
			setVisible(false);
			
			return;
		}
		else if("Delete".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
		{
			if(listing == null || listing.size() == 0)
				return;
			
			GetLog(linkholder.getName(),selected);
			listing.remove(selected);
			
			// If we were at the bottom of the list we need to update our cursor
			if(selected != 0 && selected == listing.size())
				selected--;
			
			DisplayListing();
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
			repaint();
		
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
	
	public String GetLog(String Name, int index)
	{
		if(index < 0)
			return null;
		
		try
		{
			FileReader file = new FileReader(new File("Logs/" + Name));
			Scanner in = new Scanner(file);
			
			ArrayList<String> File = new ArrayList<String>();
			
			while(in.hasNextLine())
				File.add(in.nextLine());
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
			
			if(index > File.size() - 1)
				return null;
			
			String ret = File.get(index);
			File.remove(index);
			
			FileWriter file2 = new FileWriter(new File("Logs/" + Name));
			BufferedWriter out = new BufferedWriter(file2);
			
			if(out == null)
				return null;
			
			for(int i = 0;i < File.size();i++)
			{
				out.write(File.get(i));
				out.newLine();
			}
			
			try
			{
				out.close();
				file2.close();
			}
			catch(IOException e)
			{}
			
			return ret;
		}
		catch(IOException e)
		{}
		
		return null;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2 = (Graphics2D)panel.getGraphics();
		
		if(listing.size() > 0)
			g2.drawRect(4,6 + 17 * selected,267,16);
		
		return;
	}
	
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
	 * The files currently listed.
	 */
	protected ArrayList<String> listing;
	
	/**
	 * The currently selected item in the listing.
	 */
	protected int selected;
	
	/**
	 * This file holds the links.
	 */
	protected File linkholder;
}
