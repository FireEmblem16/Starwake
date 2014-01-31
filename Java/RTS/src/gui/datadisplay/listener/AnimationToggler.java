package gui.datadisplay.listener;

import gui.datadisplay.ItemContainer;
import image.animation.Animation;
import item.Item;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/**
 * Waits for an action so that we can change animation to be on or off.
 */
public class AnimationToggler implements ActionListener
{
	/**
	 * Constructs and initializes this object so that we can toggle animation on and off.
	 */
	public AnimationToggler(Item pickfrom, JCheckBox selector, ItemContainer setchanged)
	{
		container = setchanged;
		select = selector;
		item = pickfrom;
		
		return;
	}
	
	/**
	 * Toggles animation on or off.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(item == null || container == null || select == null)
			return;
		
		if(select.isSelected())
		{
			item.SetParam("animation-name","");
			container.SetChanged();
		}
		else
		{
			item.SetParam("animation-name",null);
			item.ChangeAnimation(null);
			
			container.SetChanged();
		}
		
		return;
	}
	
	/**
	 * The item to remove the animation from.
	 */
	protected Item item;
	
	/**
	 *  We will select the value to change to from this.
	 */
	protected JCheckBox select;
	
	/**
	 * The container of [item].
	 */
	protected ItemContainer container;
}
