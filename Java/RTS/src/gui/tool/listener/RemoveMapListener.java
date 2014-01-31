package gui.tool.listener;

import grid.Board;
import grid.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

/**
 * This will add a map to a board when it gets an action fired to it.
 */
public class RemoveMapListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public RemoveMapListener(Board b, JComboBox maps)
	{
		board = b;
		this.maps = maps;
		
		return;
	}
	
	/**
	 * Adds a map to [board].
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(!(maps.getSelectedItem() instanceof String))
			return;
		
		if(maps.getSelectedIndex() == 0)
			return;
		
		String map = (String)maps.getSelectedItem();
		
		if(!board.RemoveMap(map))
			return;
		
		maps.removeAllItems();
		
		ArrayList<String> mapnames = board.GetMapNames();
		mapnames.add(0,"No Map Selected");
		
		for(int i = 0;i < mapnames.size();i++)
			maps.addItem(mapnames.get(i));
		
		// As a design choice we to no map displayed
		maps.setSelectedIndex(0);
		
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
}
