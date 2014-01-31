package gui.datadisplay.listener;

import item.map.MapItem;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Updates an integer value in a MapItem when a change occurs.
 */
public class IntChanger implements ChangeListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public IntChanger(MapItem mitem, String parameter)
	{
		item = mitem;
		param = parameter;
		
		return;
	}
	
	/**
	 * The state changed so we need to update [item].
	 */
	public void stateChanged(ChangeEvent e)
	{
		if(param == null || item == null)
			return;
		
		if(!(e.getSource() instanceof JSpinner))
			return;
		
		JSpinner source = (JSpinner)e.getSource();
		
		if(!(source.getValue() instanceof Integer))
			return;
		
		item.SetParam(param,source.getValue());
		return;
	}
	
	/**
	 * This is the item we want to change based on whatever we are watching.
	 */
	protected MapItem item;
	
	/**
	 * This contains the parameter we want to change.
	 */
	protected String param;
}
