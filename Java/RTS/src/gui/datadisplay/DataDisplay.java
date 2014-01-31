package gui.datadisplay;

import item.Item;
import item.Type;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import engine.Engine;
import engine.RunnerTask;

/**
 * Displays and allows the editing of item data in a nice GUI format.
 */
public class DataDisplay extends JDialog implements Runnable
{
	/**
	 * Constructs and initalizes this object. When this JDialog is closed it is only hidden.
	 */
	public DataDisplay()
	{
		super();
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		scrollbars = new JScrollPane();
		getRootPane().getContentPane().removeAll();
		add(scrollbars);
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        
		setSize(Math.min(310,dim.width / 4),4 * dim.height / 5);
		setLocation(dim.width - getWidth() - (dim.width <= getWidth() + 90 ? 0 : 90),dim.height / 10);
		
		setAlwaysOnTop(true);
		setTitle("Item Editor");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		Engine.GetBackgroundClock().schedule(new RunnerTask(this),10,250);
		
		setVisible(true);
		return;
	}
	
	/**
	 * Constructs and displays the GUI components to view and edit Item properties.
	 */
	public void Display(ArrayList<Item> items)
	{
		if(items == null)
			return;
		
		panels = new ArrayList<ItemContainer>();
		
		Type next_t = Type.GetNext(null);
		
		while(next_t != null)
		{
			for(int i = 0;i < items.size();i++)
				if(items.get(i) != null && items.get(i).GetType() == next_t)
					panels.add(new ItemContainer(items.get(i)));
			
			next_t = Type.GetNext(next_t);
		}
		
		Container panel = new Container();
		
		panel.removeAll();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		Vertical.addContainerGap();
		
		for(int i = 0;i < panels.size();i++)
		{
			LayoutGroup group = panels.get(i).GetLayout(layout);
			
			h1.addGroup(group.horizontal);
			Vertical.addGroup(group.vertical);
			Vertical.addGap(5);
		}
		
		Horizontal.addContainerGap();
		Horizontal.addGroup(h1);
		
		layout.setHorizontalGroup(Horizontal);
		layout.setVerticalGroup(Vertical);
		
		int X = scrollbars.getHorizontalScrollBar().getValue();
		int Y = scrollbars.getVerticalScrollBar().getValue();
		
		scrollbars.getViewport().removeAll();
		scrollbars.getViewport().add(panel);
		
		scrollbars.getHorizontalScrollBar().setValue(X);
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		return;
	}
	
	/**
	 * Constructs and displays the GUI components to view and edit Item properties.
	 */
	protected void Display()
	{
		Container panel = new Container();
		
		panel.removeAll();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		Vertical.addContainerGap();
		
		for(int i = 0;i < panels.size();i++)
		{
			LayoutGroup group = panels.get(i).GetLayout(layout);
			
			h1.addGroup(group.horizontal);
			Vertical.addGroup(group.vertical);
			Vertical.addGap(5);
		}
		
		Horizontal.addContainerGap();
		Horizontal.addGroup(h1);
		
		layout.setHorizontalGroup(Horizontal);
		layout.setVerticalGroup(Vertical);
		
		int X = scrollbars.getHorizontalScrollBar().getValue();
		int Y = scrollbars.getVerticalScrollBar().getValue();
		
		scrollbars.getViewport().removeAll();
		scrollbars.getViewport().add(panel);
		
		scrollbars.getHorizontalScrollBar().setValue(X);
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		return;
	}
	
	/**
	 * Runs the update functions of all ItemPanels that this JDialog displays.
	 */
	public void run()
	{
		if(panels == null)
			return;
		
		// If any of the panels have changed since our last update we need to redraw them
		for(int i = 0;i < panels.size();i++)
			if(panels.get(i).HasChanged())
			{
				ArrayList<Item> items = new ArrayList<Item>();
				
				for(int j = 0;j < panels.size();j++)
					panels.get(j).Update();
				
				Display();
				break;
			}
		
		return;
	}
	
	/**
	 * Brings all of the windows to the front of the screen with alwaysOnTop.
	 */
	public void WindowsToFront()
	{
		setAlwaysOnTop(true);
		return;
	}
	
	/**
	 * Sends all windows to the back of the screen with alwaysOnTop being set to false.
	 */
	public void WindowsToBack()
	{
		setAlwaysOnTop(false);
		return;
	}
	
	/**
	 * Disposes of this window and all windows it manages.
	 */
	public void dispose()
	{
		super.dispose();
		return;
	}
	
	/**
	 * Contains all panels we want to draw on this JFrame.
	 */
	protected ArrayList<ItemContainer> panels;
	
	/**
	 * Contains the scroll bars for this JDialog window.
	 */
	protected JScrollPane scrollbars;
}
