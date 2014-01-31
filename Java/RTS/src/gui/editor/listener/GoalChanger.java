package gui.editor.listener;

import grid.Goal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Updates the goal data in the goal passed to this object when actions are fired.
 */
public class GoalChanger implements ActionListener, ChangeListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public GoalChanger(Goal goal)
	{
		g = goal;
		return;
	}
	
	/**
	 * Changes the type of goal depending on the value in the source.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(g == null || e.getSource() == null || !(e.getSource() instanceof JComboBox))
			return;
		
		Object obj = ((JComboBox)e.getSource()).getSelectedItem();
		
		if(!(obj instanceof String))
			return;
		
		int n = Goal.GetType((String)obj);
		
		if(n != -1)
			g.Type = n;
		
		return;
	}
	
	/**
	 * Changes the length of the goal depending on the value in the source.
	 */
	public void stateChanged(ChangeEvent e)
	{
		if(g == null || e.getSource() == null || !(e.getSource() instanceof JSpinner))
			return;
		
		Object obj = ((JSpinner)e.getSource()).getValue();
		
		if(!(obj instanceof Integer) || ((Integer)obj).intValue() < 1)
			return;
		
		g.length = ((Integer)obj).intValue();
		return;
	}
	
	/**
	 * Contains the goal we want to update.
	 */
	protected Goal g;
}
