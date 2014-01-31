package gui.eventhandlers;

import gui.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Let's us zoom.
 */
public class ZoomListener implements ActionListener
{
	/**
	 * Constructs this object.
	 */
	public ZoomListener(Frame base, boolean zoom_reset, float zoom_to)
	{
		zoom_on = base;
		reset_zoom = zoom_reset;
		zoom_factor = zoom_to;
		
		return;
	}
	
	/**
	 * Zoooooms!
	 */
	public void actionPerformed(ActionEvent e)
	{
		zoom_on.DisplayImage(zoom_on.GetCurrentImage(),reset_zoom,zoom_factor);
		return;
	}
	
	/**
	 * This is where we should zoom.
	 */
	protected Frame zoom_on;
	
	/**
	 * This is how we should zoom.
	 */
	protected float zoom_factor;
	
	/**
	 * If true we should reset the zoom factor.
	 */
	protected boolean reset_zoom;
}
