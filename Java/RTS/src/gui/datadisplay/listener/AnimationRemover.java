package gui.datadisplay.listener;

import gui.datadisplay.ItemContainer;
import image.animation.Animation;
import item.Item;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/**
 * Waits for an action so that we can remove an animation from an item.
 */
public class AnimationRemover implements ActionListener
{
	/**
	 * Constructs and initializes this object so that when the action
	 * we are listening for occurs we remove an animation that we choose
	 * from [selector] from [removefrom] and set the [changed] value in
	 * [setchanged] to true.
	 */
	public AnimationRemover(Item removefrom, JComboBox selector, ItemContainer setchanged)
	{
		container = setchanged;
		selectfrom = selector;
		item = removefrom;
		
		return;
	}
	
	/**
	 * Hopefully removes something from [item] and changes container.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(selectfrom == null || container == null || item == null)
			return;
		
		if(!(selectfrom.getSelectedItem() instanceof String))
			return;
		
		String animation = (String)selectfrom.getSelectedItem();
		String[] animations = item.GetAnimationNames();
		
		for(int i = 1;i < animations.length;i++)
			if(animation.equals(animations[i]))
			{
				if(animation.equals(item.GetCurrentAnimation()))
				{
					item.ChangeAnimation(null);
					item.SetParam("animation-name",null);
				}
				
				item.GetAnimations().get(i - 1).deleteObserver(item);
				item.GetAnimations().remove(i - 1);
				container.SetChanged();
				
				return;
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
	
	/**
	 * The container of [item].
	 */
	protected ItemContainer container;
}
