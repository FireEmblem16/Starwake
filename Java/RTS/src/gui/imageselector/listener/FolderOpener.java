package gui.imageselector.listener;

import gui.ImagePanel;
import gui.NamedArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JComboBox;

/**
 * Waits for an action to be issued and updates a combo box with the next level of values.
 * An object of this class should listen on both the open and back buttons and the two buttons
 * should have their action commands registered as "Open" and "Back" respectively.
 */
public class FolderOpener implements ActionListener
{
	/**
	 * Constrcuts and initializes this object.
	 */
	public FolderOpener(JComboBox root_pallet, JComboBox openme, NamedArrayList<NamedArrayList> data, ImagePanel nullme, ViewListener view)
	{
		open = openme;
		vals = data;
		img = nullme;
		root = root_pallet;
		this.view = view;
		
		if(root == null)
			return;
		
		cd = new LinkedList<String>();
		cd.add(root.getSelectedItem().toString());
		
		return;
	}

	/**
	 * Updates the contents of [open].
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(open == null || vals == null || img == null || root == null)
			return;
		
		// If we're not were we think we are then 
		if(cd.size() == 0 || !cd.get(0).equals(root.getSelectedItem().toString()))
		{
			cd.clear();
			cd.add(root.getSelectedItem().toString());
		}
		
		// Check if we are trying to go forward or backward in the "file system"
		if(e.getActionCommand().equals("Open"))
		{
			// This is root
			NamedArrayList<NamedArrayList> loc = vals;
			
			// Get to the end of our path
			for(int i = 0;i < cd.size();i++)
				for(int j = 0;j < loc.size();j++)
					if(loc.get(j).toString().equals(cd.get(i)))
					{
						loc = loc.get(j);
						break;
					}
			
			// This is where we want to go
			NamedArrayList<NamedArrayList> newloc = null;
			
			// Find the item we are trying to open
			for(int i = 0;i < loc.size();i++)
				if(open.getSelectedItem().toString().equals(loc.get(i).toString()))
				{
					// We can only open something if it is a "folder"
					if(loc.get(i).size() != 0)
					{
						newloc = loc.get(i);
						cd.add(loc.get(i).toString());
					}
					
					break;
				}
			
			// If we didn't find what we were looking for then don't do anything
			if(newloc == null)
				return;
			
			// Remove everything from the item selector
			open.removeAllItems();
			
			// We use this to update [view]
			String[] images = new String[newloc.size()];
			
			// Add all new items
			for(int i = 0;i < newloc.size();i++)
			{
				open.addItem(newloc.get(i).toString());
				images[i] = newloc.get(i).toString();
			}
			
			// Update our image viewer
			view.Update(images,cd.get(0));
		}
		else if(e.getActionCommand().equals("Back"))
		{
			// If we only have the pallet name in our path then we can't go back
			if(cd.size() == 1)
				return;
			
			// We can go back by remvoing the last item and then searching through [vals] again
			String old = cd.removeLast();
			
			// This is root
			NamedArrayList<NamedArrayList> loc = vals;
			
			// Get to the end of our path
			for(int i = 0;i < cd.size();i++)
				for(int j = 0;j < loc.size();j++)
					if(loc.get(j).toString().equals(cd.get(i)))
					{
						loc = loc.get(j);
						break;
					}
			
			// Remove everything from the item selector
			open.removeAllItems();
			
			// We use this to update [view]
			String[] images = new String[loc.size()];
			
			// Add all new items
			for(int i = 0;i < loc.size();i++)
			{
				open.addItem(loc.get(i).toString());
				images[i] = loc.get(i).toString();
			}
			
			// Update our image viewer
			view.Update(images,cd.get(0));
			
			// Set the selected item so we appear to have properly kept track of item selection history
			open.setSelectedItem(old);
		}
		else
			return;
		
		// We have returned early if we were given invalid settings
		img.ChangeImage(null);
		return;
	}
	
	/**
	 * This is what we want to update and will get what we are opening from.
	 */
	protected JComboBox open;
	
	/**
	 * This is where we store all of our data.
	 */
	protected NamedArrayList<NamedArrayList> vals;
	
	/**
	 * When an action is fired this will have it's image nulled.
	 */
	protected ImagePanel img;
	
	/**
	 * This will contain the root value for us to enter into [vals] with.
	 */
	protected JComboBox root;
	
	/**
	 * Contains the path that we have currently taken to get to this directory.
	 */
	protected LinkedList<String> cd;
	
	/**
	 * This displays all of the images in the current level.
	 */
	protected ViewListener view;
}
