package gui.datadisplay.listener;

import image.animation.Animation;
import item.Item;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/**
 * Waits for an action so that we can change the default animation of an item.
 */
public class AnimationChanger implements ActionListener
{
	/**
	 * Constructs and initializes this object so that when the action we are
	 * listening for occurs we change an animation that we choose from [selector].
	 */
	public AnimationChanger(Item pickfrom, JComboBox selector)
	{
		selectfrom = selector;
		item = pickfrom;
		
		return;
	}
	
	/**
	 * Hopefully changes the default animation in [item] and
	 * switches the current animation to the new default value.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(item == null)
			return;
		
		if(selectfrom == null)
		{
			item.SetParam("animation-name",null);
			item.ChangeAnimation(null);
			
			return;
		}
		
		if(!(selectfrom.getSelectedItem() instanceof String))
			return;
		
		String animation = (String)selectfrom.getSelectedItem();
		String[] animations = item.GetAnimationNames();
		
		if(item.ChangeAnimation(animation))
			item.SetParam("animation-name",animation);
		else
		{
			item.ChangeAnimation(null);
			item.SetParam("animation-name",null);
		}
		
		return;
	}
	
	/**
	 * The item to remove the animation from.
	 */
	protected Item item;
	
	/**
	 * We will select the animation to remove from this.
	 */
	protected JComboBox selectfrom;
}
