package gui.editor.listener;

import grid.Goal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;

/**
 * Toggles the given component to be visible or not.
 */
public class VisibilityToggler implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public VisibilityToggler(Goal goal, JSpinner togglethis)
	{
		toggle = togglethis;
		g = goal;
		
		return;
	}
	
	/**
	 * Does what must be done.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(g == null || toggle == null || !(e.getSource() instanceof JCheckBox))
			return;
		
		JCheckBox check = (JCheckBox)e.getSource();
		
		if(check.isSelected())
		{
			if(!(toggle.getValue() instanceof Integer))
				return;
			
			toggle.setVisible(true);
			g.length = ((Integer)toggle.getValue()).intValue();
		}
		else
		{
			toggle.setVisible(false);
			g.length = -1;
		}
		
		return;
	}
	
	/**
	 * We we toggle visibility on we want to set this length value to the value in [toggle] and
	 * when we toggle visibility off we want to set this length value to -1.
	 */
	protected Goal g;
	
	/**
	 * This is the spinner who's visibility we want to goggle.
	 */
	protected JSpinner toggle;
}
