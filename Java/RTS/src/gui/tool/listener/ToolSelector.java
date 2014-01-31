package gui.tool.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import engine.Pointer;
import gui.ImagePanel;
import gui.tool.tools.Tool;

/**
 * Changes the selected tool of a ToolBox when this object is aware of an action occuring.
 */
public class ToolSelector implements ActionListener
{
	/**
	 * Constructs and initializes this object.
	 */
	public ToolSelector(ImagePanel panel, Pointer<Tool> box, Tool tool)
	{
		ptr = box;
		this.tool = tool;
		img = panel;
		
		return;
	}
	
	/**
	 * Chagnes the selected tool of [box].
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(ptr == null)
			return;
		
		ptr.ptr = tool;
		img.ChangeImage(tool.GetImageRepresentation());
		
		return;
	}

	/**
	 * This is the tool we will set to be selected.
	 */
	protected Tool tool;
	
	/**
	 * This is where we will set the tool to be selected at.
	 */
	protected Pointer<Tool> ptr;
	
	/**
	 * Contains the location of a panel where we can display an image represenation of the tool we select.
	 */
	protected ImagePanel img;
}
