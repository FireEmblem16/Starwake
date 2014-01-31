package gui.datadisplay.listener;

import item.Item;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 * Waits for the enter key input event at which point it changes information
 * in an Item that pertains to the image it will use to draw.
 */
public class ImageDataChanger implements KeyListener
{
	/**
	 * Constructs and initializes this object so that it waits for the enter
	 * key to be entered and changes the param [param] in [change] to whatever
	 * value is in [getfrom] at the time of the event.
	 */
	public ImageDataChanger(Item change, JTextField getfrom, String param)
	{
		item = change;
		data = getfrom;
		parameter = param;
		
		return;
	}
	
	/**
	 * Changes what we all know need be changed.
	 */
	public void keyPressed(KeyEvent e)
	{
		if(item == null || data == null || parameter == null || e == null)
			return;
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			item.SetParam(parameter,data.getText());
		
		return;
	}

	/**
	 * Does nothing useful.
	 */
	public void keyReleased(KeyEvent e)
	{return;}

	/**
	 * Does nothing useful.
	 */
	public void keyTyped(KeyEvent e)
	{return;}
	
	/**
	 * Contains the item who's data we are concerned about.
	 */
	protected Item item;
	
	/**
	 * Contains the data we want to change the specified parameter to.
	 */
	protected JTextField data;
	
	/**
	 * This is the parameter we want to change.
	 */
	protected String parameter;
}
