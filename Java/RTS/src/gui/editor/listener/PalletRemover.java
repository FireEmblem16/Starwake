package gui.editor.listener;

import grid.Board;
import gui.imageselector.ImageSelector;
import image.container.Pallet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JComboBox;

/**
 * Removes the pallet selected from a JComboBox from a board.
 */
public class PalletRemover implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public PalletRemover(JComboBox what, Board where, ImageSelector update)
	{
		this.what = what;
		this.where = where;
		this.update = update;
		
		return;
	}
	
	/**
	 * Removes a pallet.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(what == null || where == null || what.getSelectedIndex() < 1)
			return;
		
		if(!(what.getSelectedItem() instanceof String))
			return;
		
		ArrayList<Pallet> pallets = where.GetPallets();
		String name = (String)what.getSelectedItem();
		
		if(name == null || pallets == null)
			return;
		
		for(int i = 0;i < pallets.size();i++)
			if(pallets.get(i) != null && name.equals(pallets.get(i).GetName()))
			{
				pallets.remove(i);
				
				if(update != null)
					update.Display(where.GetPallets());
				
				break;
			}
		
		Vector v = new Vector();
		
		for(int i = 0;i < where.GetPallets().size();i++)
			v.add(where.GetPallets().get(i).GetName());
		
		Object[] palletnames = v.toArray();
		Arrays.sort(palletnames);
		v.clear();
		
		for(int i = 0;i < palletnames.length;i++)
			v.add(palletnames[i]);
		
		v.add(0,"No Pallet Selected");
		what.removeAllItems();
		
		for(int i = 0;i < v.size();i++)
			what.addItem(v.get(i));
		
		update.Display(where.GetPallets());
		where.ForceRepaint();
		return;
	}
	
	/**
	 * This is the item where we can find the name of the pallet we want to remove.
	 */
	protected JComboBox what;
	
	/**
	 * This is the board we want to remove the pallet from.
	 */
	protected Board where;
	
	/**
	 * If this isn't null we want to update this.
	 */
	protected ImageSelector update;
}
