import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

/**
 * Listens to events and lets observers know they occured.
 */
public class Listener extends Observable implements ActionListener
{
	/**
	 * Constructs this object.
	 */
	public Listener()
	{
		super();
		return;
	}
	
	/**
	 * Listens for any events that occur.
	 */
	public void actionPerformed(ActionEvent e)
	{
		setChanged();
		notifyObservers(e.getActionCommand());
		
		return;
	}
}
