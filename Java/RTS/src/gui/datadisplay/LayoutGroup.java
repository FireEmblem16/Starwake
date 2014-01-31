package gui.datadisplay;

import javax.swing.GroupLayout.Group;

/**
 * Contains a horizontal and vertical layout for a component.
 */
public class LayoutGroup
{
	/**
	 * Constrcuts and initializes this object.
	 */
	public LayoutGroup()
	{return;}
	
	/**
	 * This should contain a horizontal layout.
	 */
	public Group horizontal;
	
	/**
	 * This should contain a vertical layout for the same component as [horizontal].
	 */
	public Group vertical;
}