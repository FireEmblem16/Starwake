package gui.tool.listener;

import grid.Board;
import grid.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 * This will add a map to a board when it gets an action fired to it.
 */
public class AddMapListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public AddMapListener(Board b, JComboBox maps, JSpinner widthspinner, JSpinner heightspinner)
	{
		board = b;
		this.maps = maps;
		mapwidth = widthspinner;
		mapheight = heightspinner;
		
		return;
	}
	
	/**
	 * Adds a map to [board].
	 */
	public void actionPerformed(ActionEvent e)
	{
		String name = (String)JOptionPane.showInputDialog(null,"Input the name of the new map or hit Cancel to abort.","Choose Map Name",
				JOptionPane.OK_CANCEL_OPTION,new ImageIcon("images/system/map.png"),null,null);
		
		// If [name] is null then we hit cancel, we also don't allow empty names
		if(name != null && !name.equals(""))
		{
			if(!board.AddMap(new Map(board,name)))
				return;
			
			maps.removeAllItems();
			
			ArrayList<String> mapnames = board.GetMapNames();
			mapnames.add(0,"No Map Selected");
			
			for(int i = 0;i < mapnames.size();i++)
				maps.addItem(mapnames.get(i));
			
			// As a design choice we switch to the new map
			maps.setSelectedItem(name);
		}
		
		return;
	}
	
	/**
	 * Contains the board we want to add a map to.
	 */
	protected Board board;
	
	/**
	 * Contains all of the map names in [board].
	 */
	protected JComboBox maps;
	
	/**
	 * This is the spinner that contains the current map's width.
	 */
	protected JSpinner mapwidth;
	
	/**
	 * This is the spinner that contains the current map's height.
	 */
	protected JSpinner mapheight;
}
