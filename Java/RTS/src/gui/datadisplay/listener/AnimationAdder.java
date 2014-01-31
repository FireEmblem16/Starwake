package gui.datadisplay.listener;

import gui.datadisplay.ItemContainer;
import image.animation.Animation;
import item.Item;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Waits for an action so that we can add an animation to an item.
 */
public class AnimationAdder implements ActionListener
{
	/**
	 * Constructs and initializes this object so that when the action
	 * we are listening for occurs we add an animation that we choose
	 * from a FileChooser to [addto] and set the [changed] value in
	 * [setchanged] to true.
	 */
	public AnimationAdder(Item addto, ItemContainer setchanged)
	{
		container = setchanged;
		item = addto;
		
		return;
	}
	
	/**
	 * Hopefully adds something to [item] and changes container.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(container == null || item == null)
			return;
		
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		
		String[] extensions = {"animation","Animation"};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Animation Files",extensions);
		chooser.setFileFilter(filter);
		
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		
		if(chooser.getSelectedFile() == null)
			return;
		
		Animation a = new Animation(chooser.getSelectedFile().getAbsolutePath(),item);
		
		if(a.GetName() == null)
			return;
		
		String[] animations = item.GetAnimationNames();
		
		for(int i = 0;i < animations.length;i++)
			if(animations[i].equals(a.GetName()))
				return;
		
		a.addObserver(item);
		item.GetAnimations().add(a);
		container.SetChanged();
		
		return;
	}
	
	/**
	 * The item to add a new animation to.
	 */
	protected Item item;
	
	/**
	 * The container of [item].
	 */
	protected ItemContainer container;
}
