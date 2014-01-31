package gui.tool.listener;

import grid.Board;
import grid.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Changes the dimensions of the currently displayed map when actions are fired to this object.
 */
public class DimensionListener implements ChangeListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public DimensionListener(Board b, boolean width)
	{
		board = b;
		height = !width;
		
		return;
	}
	
	/**
	 * Changes the size of the current map being displayed.
	 */
	public void stateChanged(ChangeEvent e)
	{
		if(board == null)
			return;
		
		if(!(e.getSource() instanceof JSpinner))
			return;
		
		JSpinner source = (JSpinner)e.getSource();
		
		if(!(source.getValue() instanceof Integer))
			return;
		
		int new_val = (Integer)source.getValue();
		
		Map m = board.GetDisplayedMap();
		
		if(m == null)
			return;
		
		// We could run out of memory or perhaps we allowed a bad value in the spinner
		if(!m.Resize(height ? m.GetWidth() : new_val,height ? new_val : m.GetHeight()))
		{
			source.setValue(new Integer(height ? m.GetHeight() : m.GetWidth()));
			JOptionPane.showMessageDialog(null,"The desired map size requires more memory than is available.","Out of Memory",JOptionPane.OK_OPTION,new ImageIcon("images/system/error.png"));
		}
		
		return;
	}
	
	/**
	 * This is the board that we can get the map we want to change from.
	 */
	protected Board board;
	
	/**
	 * This is true if we are trying to edit height and false if we want to edit width.
	 */
	protected boolean height;
}
