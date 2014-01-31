package gui.tool.listener;

import grid.Board;
import grid.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/**
 * Changes a map to be a hex map or not based on a check box.
 */
public class HexListener implements ActionListener
{
	/**
	 * Constructs and initializes this listener.
	 */
	public HexListener(Board board)
	{
		b = board;
		return;
	}
	
	/**
	 * Updates a map's hex option.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(b == null || e == null || e.getSource() == null || !(e.getSource() instanceof JCheckBox))
			return;
		
		Map m = b.GetDisplayedMap();
		
		if(m == null)
			return;
		
		JCheckBox val = (JCheckBox)e.getSource();
		
		m.SetHex(val.isSelected());
		b.ForceRepaint(); // We want to force a repaint since we just drastically changed the way the map is drawn
		return;
	}
	
	/**
	 * Contains the board who's currently displayed map we want to change.
	 */
	protected Board b;
}
