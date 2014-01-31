package gui.datadisplay.listener;

import image.animation.Animation;
import item.map.MapItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/**
 * Waits for an action so that we can change the default animation of an item.
 */
public class BooleanToggler implements ActionListener
{
	/**
	 * Constructs and initializes this object so that when the action we are
	 * listening for occurs we change a parameter in [pickfrom].
	 */
	public BooleanToggler(MapItem pickfrom, JCheckBox selector, String parameter)
	{
		param = parameter;
		select = selector;
		item = pickfrom;
		
		return;
	}
	
	/**
	 * Hopefully changes the default animation in [item] and
	 * switches the current animation to the new default value.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(item == null || param == null || select == null)
			return;
		
		item.SetParam(param,new Boolean(select.isSelected()));
		return;
	}
	
	/**
	 * The item in which to change the parameter value.
	 */
	protected MapItem item;
	
	/**
	 * We will select the value to change to from this.
	 */
	protected JCheckBox select;
	
	/**
	 * The container of [item].
	 */
	protected String param;
}
