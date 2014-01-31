package gui.imageselector;

import gui.ImagePanel;
import gui.NamedArrayList;
import gui.imageselector.listener.FolderOpener;
import gui.imageselector.listener.ImageListener;
import gui.imageselector.listener.PalletListener;
import gui.imageselector.listener.ViewListener;
import gui.imageviewer.ImageViewer;
import image.container.MappedBufferedImage;
import image.container.Pallet;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import engine.Engine;
import engine.RunnerTask;

/**
 * Displays pallets in a nice easy to view and select form.
 */
public class ImageSelector extends JDialog implements Runnable
{
	/**
	 * Constructs and initalizes this object. When this JDialog is closed it is only hidden.
	 */
	public ImageSelector()
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
        
		setSize(Math.min(300,dim.width / 4),2 * dim.height / 5);
		setLocation(dim.width - getWidth() - (dim.width <= getWidth() + Math.min(300,dim.width / 4) + 100
				? (dim.width <= getWidth() + 100 ? 0 : 100) : Math.min(300,dim.width / 4) + 100),dim.height / 2);
		
		setAlwaysOnTop(true);
		setTitle("Image Selector");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		Engine.GetBackgroundClock().schedule(new RunnerTask(this),100,250);
		
		setVisible(true);
		return;
	}
	
	/**
	 * Initializes [data].
	 * Structure:
	 * "No Pallet Selected" + All pallet names as NamedArrayLists
	 * Each array list contains the pallets sorted by their filters
	 * in alphabetical order with "folders" first.
	 * If [first] is true we will take it to mean that we have an array list of pallets which could quite possibly be of length zero.
	 */
	public void CreateData(ArrayList data, NamedArrayList<NamedArrayList> top, int filterdepth, String imagename, boolean first)
	{
		// Sanity check
		if(data == null || top == null)
			return;
		
		if(first || data.size() > 0 && data.get(0) instanceof Pallet)
		{
			// Create space for the pallet names
			String[] palletnames = new String[pallets.size()];
			
			// Get all of the pallet names
			for(int i = 0;i < pallets.size();i++)
				palletnames[i] = pallets.get(i).GetName();
			
			// Sort the names
			Arrays.sort(palletnames);
			
			// Go through each pallet and add the images
			for(int i = 0;i < palletnames.length;i++)
			{
				// Add the pallet first
				NamedArrayList<NamedArrayList> pallet = new NamedArrayList<NamedArrayList>(palletnames[i]);
				top.add(pallet);
				
				// RECURSION!!!
				CreateData(GetPallet(palletnames[i]).GetAllEntries(),pallet,0,imagename,false);
				
				// Get the array of the top array list
				Object[] topitems = top.toArray();
				
				// SORT!!!
				Arrays.sort(topitems);
				
				// Emtpy the list so we can refill it sorted
				top.clear();
				
				// Add the sorted version
				for(int j = 0;j < topitems.length;j++)
					((NamedArrayList)top).add(topitems[j]);
			}
			
			// Add the no pallet and image selected item
			top.add(0,new NamedArrayList<NamedArrayList>("No Pallet Selected"));
			top.get(0).add(new NamedArrayList("No Image Selected"));
		}
		else if(data.size() > 0 && data.get(0) instanceof MappedBufferedImage)
		{
			// CAST!!!
			ArrayList<MappedBufferedImage> images = (ArrayList<MappedBufferedImage>)data;
			
			// Go through each item and put it where it needs to go
			for(int i = 0;i < images.size();i++)
			{
				// Get the filter of the curent image
				ArrayList<String> filter = images.get(i).GetFilter();
				
				// If the filter is null or has no element at [filterdepth] then we just stick it where we currently are
				if(filter == null || filter.size() <= filterdepth)
					top.add(new NamedArrayList<NamedArrayList>(images.get(i).GetName()));
				else
				{
					// See if we have an array list for this filter name yet
					NamedArrayList<NamedArrayList> next = (NamedArrayList<NamedArrayList>)top.Find(filter.get(filterdepth));
					
					// If we don't have the array list yet we have to make one
					if(next == null)
					{
						next = new NamedArrayList<NamedArrayList>(filter.get(filterdepth));
						top.add(next);
					}
					
					// RECURSION!!!
					CreateData(filter,next,filterdepth + 1,images.get(i).GetName(),false);
				}
				
				// Get the array of the top array list
				Object[] topitems = top.toArray();
				
				// SORT!!!
				Arrays.sort(topitems);
				
				// Emtpy the list so we can refill it sorted
				top.clear();
				
				// Add the sorted version
				for(int j = 0;j < topitems.length;j++)
					((NamedArrayList)top).add(topitems[j]);
			}
			
			// Add the no image selected item
			top.add(0,new NamedArrayList<NamedArrayList>("No Image Selected"));
		}
		else if(data.size() > 0 && data.get(0) instanceof String)
		{
			// CAST!!!
			ArrayList<String> filter = (ArrayList<String>)data;
			
			// If we are past all of our filters then we need to 
			if(filter.size() <= filterdepth)
				top.add(new NamedArrayList(imagename));
			else
			{
				// See if we have an array list for this filter name yet
				NamedArrayList<NamedArrayList> next = (NamedArrayList<NamedArrayList>)top.Find(filter.get(filterdepth));
				
				// If we don't have the array list yet we have to make one
				if(next == null)
				{
					next = new NamedArrayList<NamedArrayList>(filter.get(filterdepth));
					top.add(next);
				}
				
				// RECURSION!!!
				CreateData(filter,next,filterdepth + 1,imagename,false);
			}
			
			// Get the array of the top array list
			Object[] topitems = top.toArray();
			
			// SORT!!!
			Arrays.sort(topitems);
			
			// Emtpy the list so we can refill it sorted
			top.clear();
			
			// Add the sorted version
			for(int i = 0;i < topitems.length;i++)
				((NamedArrayList)top).add(topitems[i]);
			
			// Add the no image selected item if we haven't already
			if(!top.contains("No Image Selected"))
				top.add(0,new NamedArrayList<NamedArrayList>("No Image Selected"));
		}
		
		return;
	}
	
	/**
	 * Constructs and displays the GUI components to view and edit Item properties.
	 */
	public void Display(ArrayList<Pallet> p)
	{
		if(p == null)
			return;
		
		pallets = p;
		selected = new ImagePanel(null);
		
		data = new NamedArrayList<NamedArrayList>("Yggdrasil");
		CreateData(pallets,data,0,null,true);
		
		palletlabel = new JLabel("Pallet ");
		
		Object[] objs = data.toArray(); // This is guaranteed to exist
		String[] pallet_str = new String[objs.length];
		
		for(int i = 0;i < pallet_str.length;i++)
			pallet_str[i] = objs[i].toString();
		
		palletselector = new JComboBox(pallet_str);
		
		imagelabel = new JLabel("Image");
		imageselector = new JComboBox(data.get(0).toArray()); // This is guaranteed to exist
		
		enter = new JButton("Open");
		enter.setPreferredSize(new Dimension(65,25));
		enter.setActionCommand("Open");
		
		view = new JButton("View");
		view.setPreferredSize(new Dimension(65,25));
		view.setToolTipText("Opens a panel that shows all images in the current level.");
		
		back = new JButton("Back");
		back.setPreferredSize(new Dimension(65,25));
		back.setActionCommand("Back");
		
		image_viewer = new ImageViewer(imageselector,selected,pallets);
		ViewListener v = new ViewListener(image_viewer);
		FolderOpener a = new FolderOpener(palletselector,imageselector,data,selected,v);
		
		palletselector.addActionListener(new PalletListener(imageselector,data,selected,v));
		imageselector.addActionListener(new ImageListener(palletselector,pallets,selected));
		enter.addActionListener(a);
		view.addActionListener(v);
		back.addActionListener(a);
		
		Construct();
		return;
	}
	
	/**
	 * Constructs the GUI layout.
	 */
	public void Construct()
	{
		Container panel = new Container();
		
		panel.removeAll();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		Horizontal.addContainerGap();
		
		h2.addComponent(palletlabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(palletselector,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addComponent(imagelabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(imageselector,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(enter,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(view,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(back,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		h1.addComponent(selected,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Horizontal.addGroup(h1);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		Vertical.addContainerGap();
		
		v1.addComponent(palletlabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(palletselector,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addGap(5);
		
		v2.addComponent(imagelabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(imageselector,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);

		Vertical.addGap(5);
		
		v3.addComponent(enter,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(view,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(back,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v3);
		
		Vertical.addGap(5);
		Vertical.addComponent(selected,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
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
	 * Runs all updates necessary and repaints this JDialog if needed.
	 */
	public void run()
	{
		if(selected != null && selected.HasChanged())
		{
			Construct();
			repaint();
		}
		
		return;
	}
	
	/**
	 * Returns a pallet by name.
	 */
	protected Pallet GetPallet(String name)
	{
		if(name == null || name.equals("No Pallet Selected"))
			return null;
		
		for(int i = 0;i < pallets.size();i++)
			return pallets.get(i);
		
		return null;
	}
	
	/**
	 * Returns the currently selected pallet.
	 */
	public Pallet GetSelectedPallet()
	{
		return GetPallet((String)palletselector.getSelectedItem());
	}
	
	/**
	 * Returns the currently selected image or null if nothing is selected.
	 */
	public MappedBufferedImage GetSelection()
	{
		Pallet pallet = GetPallet((String)palletselector.getSelectedItem());
		
		if(pallet == null)
			return null;
		
		return pallet.GetEntry(imageselector.getSelectedItem().toString());
	}
	
	/**
	 * Selectes the image called [name] in the pallet called [pallet].
	 * If either is invalid nothing happens.
	 */
	public void Select(String palletname, String name)
	{
		if(palletname == null || name == null)
			return;
		
		Pallet pallet = GetPallet(palletname);
		
		if(pallet == null)
		{
			// Null values are always index zero and this garuntees the second combo box is at index zero
			palletselector.setSelectedIndex(0);
			
			return;
		}
		
		// We need to ensure we are at the base level for this code to work so select the null pallet then selector the pallet normally if needed
		if(palletselector.getSelectedItem().equals(palletname))
			palletselector.setSelectedIndex(0);
		
		palletselector.setSelectedItem(palletname);
		
		MappedBufferedImage img = pallet.GetEntry(name);
		
		if(img == null)
		{
			imageselector.setSelectedIndex(0);
			return;
		}
		
		ArrayList<String> filter = img.GetFilter();
		
		if(filter == null)
		{
			if(img.GetName() != null)
			{
				// For some reason this happens, but doesn't really...
				try
				{imageselector.setSelectedItem(img.GetName());}
				catch(ArrayIndexOutOfBoundsException e)
				{}
			}
			else
				imageselector.setSelectedIndex(0);
			
			return;
		}
		
		for(int i = 0;i < filter.size();i++)
		{
			imageselector.setSelectedItem(filter.get(i));
			enter.doClick();
		}
		
		imageselector.setSelectedItem(img.GetName());
		return;
	}
	
	/**
	 * Returns the window this window uses as an image selector.
	 */
	public ImageViewer GetImageViewer()
	{
		return image_viewer;
	}
	
	/**
	 * Brings all of the windows to the front of the screen with alwaysOnTop.
	 */
	public void WindowsToFront()
	{
		setAlwaysOnTop(true);
		image_viewer.WindowsToFront();
		
		return;
	}
	
	/**
	 * Sends all windows to the back of the screen with alwaysOnTop being set to false.
	 */
	public void WindowsToBack()
	{
		setAlwaysOnTop(false);
		image_viewer.WindowsToBack();
		
		return;
	}
	
	/**
	 * Disposes of this window and all windows it manages.
	 */
	public void dispose()
	{
		super.dispose();
		
		if(image_viewer != null)
			image_viewer.dispose();
		
		return;
	}
	
	/**
	 * Contains a JDialog that displays all images in the current scope.
	 */
	protected ImageViewer image_viewer;
	
	/**
	 * Contains all panels we want to draw on this JFrame.
	 */
	protected ArrayList<Pallet> pallets;
	
	/**
	 * Contains all of our data to display in [imageselector].
	 */
	protected NamedArrayList<NamedArrayList> data;
	
	/**
	 * Contains the scroll bars for this JDialog window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * Contains a label that informs the user that the palletselector
	 * combo box changes the pallet we are looking in.
	 */
	protected JLabel palletlabel;
	
	/**
	 * We want to look in the pallet as specified by this combo box.
	 */
	protected JComboBox palletselector;
	
	/**
	 * Contains a label that informs the user that the imageselector
	 * combo box changes the image we want to use.
	 */
	protected JLabel imagelabel;
	
	/**
	 * We want to look for images by using this combo box.
	 */
	protected JComboBox imageselector;
	
	/**
	 * When this button is pressed we should enter the selected "folder"
	 * if one is selected and do nothing if we have an image selected.
	 */
	protected JButton enter;
	
	/**
	 * When this button is pressed we want to view all images in the
	 * selected "folder" if one is selected and allow an image to be selected.
	 */
	protected JButton view;
	
	/**
	 * When this button is pressed if we are in a "folder" we want to
	 * return to the previous level or do nothing if we are not in a folder.
	 */
	protected JButton back;
	
	/**
	 * Displays the currently selected image or a nothing if there is no selection.
	 */
	protected ImagePanel selected;
}
