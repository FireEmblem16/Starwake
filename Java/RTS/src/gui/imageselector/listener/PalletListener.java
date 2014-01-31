package gui.imageselector.listener;

import gui.ImagePanel;
import gui.NamedArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 * Listens for changes in pallets and updates other information as needed.
 */
public class PalletListener implements ActionListener
{
	/**
	 * This object will wait for actions to be issued and will update
	 * [changeme] with the data inside [data]. Also updates [image].
	 */
	public PalletListener(JComboBox changeme, NamedArrayList<NamedArrayList> data, ImagePanel image, ViewListener view)
	{
		change = changeme;
		vals = data;
		img = image;
		this.view = view;
		
		return;
	}

	/**
	 * Updates the information in [change].
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(change == null || vals == null || img == null)
			return;
		
		if(!(e.getSource() instanceof JComboBox))
			return;
		
		JComboBox source = (JComboBox)e.getSource();
		
		if(!(source.getSelectedItem() instanceof String))
			return;
		
		String pallet = (String)source.getSelectedItem();
		
		for(int i = 0;i < vals.size();i++)
			if(vals.get(i).equals(pallet))
			{
				change.removeAllItems();
				NamedArrayList items = vals.get(i);
				
				// We use this to update [view]
				String[] images = new String[items.size()];
				
				// Add all new items
				for(int j = 0;j < items.size();j++)
				{
					change.addItem(items.get(j).toString());
					images[j] = items.get(j).toString();
				}
				
				// Update our image viewer
				view.Update(images,(String)source.getSelectedItem());
				
				break;
			}
		
		img.ChangeImage(null);
		return;
	}
	
	/**
	 * Contains the combo box we want to change.
	 */
	protected JComboBox change;
	
	/**
	 * Contains all of the data we want to update with.
	 */
	protected NamedArrayList<NamedArrayList> vals;
	
	/**
	 * Contians an image that we need toupdate.
	 */
	protected ImagePanel img;
	
	/**
	 * Contains the image viewer we should update.
	 */
	protected ViewListener view;
}
