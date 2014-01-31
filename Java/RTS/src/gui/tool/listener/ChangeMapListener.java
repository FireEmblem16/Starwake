package gui.tool.listener;

import grid.Board;
import grid.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

/**
 * Changes the displayed map of a board based on the value in a JComboBox.
 */
public class ChangeMapListener implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public ChangeMapListener(Board b, JCheckBox ishex, JSpinner widthspinner, JSpinner heightspinner, JSpinner cellwidthspinner, JSpinner cellheightspinner)
	{
		board = b;
		hexcheck = ishex;
		mapwidth = widthspinner;
		mapheight = heightspinner;
		mapcellwidth = cellwidthspinner;
		mapcellheight = cellheightspinner;
		
		return;
	}
	
	/**
	 * Changes the currently displayed map to reflect the String value in a JComboBox.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == null || !(e.getSource() instanceof JComboBox))
			return;
		
		JComboBox source = (JComboBox)e.getSource();
		
		if(!(source.getSelectedItem() instanceof String))
			return;
		
		String map = (String)source.getSelectedItem();
		
		if(source.getSelectedIndex() == 0)
			map = null;
		
		board.ChangeDisplayedMap(map,true);
		
		Map m = board.GetDisplayedMap();
		
		if(m != null)
		{
			mapwidth.setValue(m.GetWidth());
			mapheight.setValue(m.GetHeight());
			mapcellwidth.setValue(m.GetCellWidth());
			mapcellheight.setValue(m.GetCellHeight());
			hexcheck.setSelected(m.IsHex());
		}
		
		return;
	}
	
	/**
	 * Contains the board which has the displayed map value we want to change.
	 */
	protected Board board;
	
	/**
	 * This check box will be checked if the current map should be drawn as a hex grid.
	 */
	protected JCheckBox hexcheck;
	
	/**
	 * This is the spinner that contains the current map's width.
	 */
	protected JSpinner mapwidth;
	
	/**
	 * This is the spinner that contains the current map's height.
	 */
	protected JSpinner mapheight;
	
	/**
	 * This is the spinner that contains the current map's cell width.
	 */
	protected JSpinner mapcellwidth;
	
	/**
	 * This is the spinner that contains the current map's cell height.
	 */
	protected JSpinner mapcellheight;
}
