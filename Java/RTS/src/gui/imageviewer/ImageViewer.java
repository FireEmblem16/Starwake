package gui.imageviewer;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import gui.ImagePanel;
import image.container.MappedBufferedImage;
import image.container.Pallet;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import engine.Engine;
import engine.RunnerTask;

/**
 * Displays a series of images in sequence vertically. Note that "folders" will not be dispalyed.
 */
public class ImageViewer extends JDialog
{
	/**
	 * Constructs and initializes this object so that we can select an image by clicking
	 * on an image displayed on this JDialog.
	 */
	public ImageViewer(JComboBox changeme, ImagePanel displayonme, ArrayList<Pallet> pallets)
	{
		change = changeme;
		panel = displayonme;
		this.pallets = pallets;
		
		scrollbars = new JScrollPane();
		getRootPane().getContentPane().removeAll();
		add(scrollbars);
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        
		setSize(Math.min(300,dim.width / 4),4 * dim.height / 5);
		setLocation(dim.width - getWidth() - (dim.width <= getWidth() + 2 * Math.min(300,dim.width / 4) + 100
				? (dim.width <= getWidth() + 100 ? 0 : 100) : 2 * Math.min(300,dim.width / 4) + 100),dim.height / 10);
		
		setAlwaysOnTop(true);
		setTitle("Folder Explorer");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		setVisible(false);
		return;
	}
	
	/**
	 * Constructs and displays this window.
	 */
	public void Display(String[] imagenames, String palletname)
	{
		if(pallets == null || change == null || panel == null || palletname == null || imagenames == null)
			return;
		
		Pallet p = null;
		
		for(int i = 0;i < pallets.size();i++)
			if(pallets.get(i).GetName().equals(palletname))
			{
				p = pallets.get(i);
				break;
			}
		
		if(p == null)
			return;
		
		ArrayList<ImagePanel> images = new ArrayList<ImagePanel>();
		
		for(int i = 0;i < imagenames.length;i++)
		{
			MappedBufferedImage img = p.GetEntry(imagenames[i]);
			
			if(img == null)
				continue;
			
			ImagePanel img_p = new ImagePanel(img);
			img_p.addMouseListener(new ImageChanger(img,panel,change));
			
			images.add(img_p);
		}
		
		Container panel = new Container();
		
		panel.removeAll();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Horizontal.addContainerGap();
		
		for(int i = 0;i < images.size();i++)
			h1.addComponent(images.get(i),GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Horizontal.addGroup(h1);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		
		Vertical.addContainerGap();
		
		for(int i = 0;i < images.size();i++)
		{
			Vertical.addComponent(images.get(i),GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGap(10);
		}
			
		layout.setHorizontalGroup(Horizontal);
		layout.setVerticalGroup(Vertical);
		
		int X = scrollbars.getHorizontalScrollBar().getValue();
		int Y = scrollbars.getVerticalScrollBar().getValue();
		
		scrollbars.getViewport().removeAll();
		scrollbars.getViewport().add(panel);
		
		scrollbars.getHorizontalScrollBar().setValue(X);
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		repaint();
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
	 * Contains the scroll bars for this JDialog window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * Contains all of the pallets we can possibly look in.
	 */
	protected ArrayList<Pallet> pallets;
	
	/**
	 * When we select an item we want to change the selected value in this combo box to match it.
	 */
	protected JComboBox change;
	
	/**
	 * When we select an item we want to change the image we dispaly here to match the selected item.
	 */
	protected ImagePanel panel;
}
