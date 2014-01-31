/** Makes the editor/viewer run.
 */
public class DisplayRunner
{
	/**
	 * Creates a new DisplayRunner and runs it.
	 */
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(
				new Runnable()
				{
					public void run()
					{
						new Host();
						return;
					}
				});
		
		return;
	}
}
