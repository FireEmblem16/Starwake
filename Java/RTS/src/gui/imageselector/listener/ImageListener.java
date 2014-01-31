package gui.imageselector.listener;

import gui.ImagePanel;
import image.container.Pallet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 * Listens for changes in selected images and updates as needed.
 */
public class ImageListener implements ActionListener
{
	/**
	 * This object will wait for actions to be issued and will update as needed.
	 */
	public ImageListener(JComboBox holdspalletname, ArrayList<Pallet> data, ImagePanel image)
	{
		palletname = holdspalletname;
		pallet = data;
		img = image;
		
		return;
	}

	/**
	 * Updates.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(palletname == null || pallet == null || pallet.size() == 0 || img == null)
			return;
		
		if(!(e.getSource() instanceof JComboBox))
			return;
		
		JComboBox source = (JComboBox)e.getSource();
		
		if(!(source.getSelectedItem() instanceof String))
			return;
		
		String image = (String)source.getSelectedItem();
		
		// Even if this is a String a String toStringed will still be the same string
		String pallet_name = palletname.getSelectedItem().toString();
		
		// Find the pallet we want to fetch the image from
		for(int i = 0;i < pallet.size();i++)
			if(pallet.get(i).GetName().equals(pallet_name))
			{
				img.ChangeImage(pallet.get(i).GetEntry(image.toString()));
				break;
			}
		
		return;
	}
	
	/**
	 * Contains the combo box we can get the current pallet name from.
	 */
	protected JComboBox palletname;
	
	/**
	 * Contains all of the data we want to update with.
	 */
	protected ArrayList<Pallet> pallet;
	
	/**
	 * Contians an image that we need toupdate.
	 */
	protected ImagePanel img;
}
