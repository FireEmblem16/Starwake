package gui.editor.listener;

import grid.Board;
import gui.imageselector.ImageSelector;
import image.container.Pallet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import statics.FileFunctions;

/**
 * Adds the pallet selected from a JFileChooser to a board.
 */
public class PalletAdder implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public PalletAdder(JComboBox disp, Board where, ImageSelector update)
	{
		this.disp = disp;
		this.where = where;
		this.update = update;
		
		return;
	}
	
	/**
	 * Removes a pallet.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(where == null)
			return;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		
		String[] extensions = {"pallet","Pallet"};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pallet Files",extensions);
		chooser.setFileFilter(filter);
		
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		
		if(chooser.getSelectedFile() == null)
			return;
		
		String str = FileFunctions.relative(new File("."),chooser.getSelectedFile());
		Pallet p = new Pallet(str);
		
		where.GetPallets().add(p);
		System.out.println(disp == null);
		if(disp == null)
			return;
		
		Vector v = new Vector();
		
		for(int i = 0;i < where.GetPallets().size();i++)
			v.add(where.GetPallets().get(i).GetName());
		
		Object[] palletnames = v.toArray();
		Arrays.sort(palletnames);
		v.clear();
		
		for(int i = 0;i < palletnames.length;i++)
			v.add(palletnames[i]);
		
		v.add(0,"No Pallet Selected");
		disp.removeAllItems();
		
		for(int i = 0;i < v.size();i++)
			disp.addItem(v.get(i));
		
		update.Display(where.GetPallets());
		where.ForceRepaint();
		return;
	}
	
	/**
	 * This is the item where we can find the name of the pallet we want to remove.
	 */
	protected JComboBox disp;
	
	/**
	 * This is the board we want to add the pallet to.
	 */
	protected Board where;
	
	/**
	 * If this isn't null we want to update this.
	 */
	protected ImageSelector update;
}
