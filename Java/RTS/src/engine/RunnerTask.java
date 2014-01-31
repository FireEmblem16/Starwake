package engine;

import java.util.TimerTask;

/**
 * When this task is executed it will call the run function of [r].
 */
public class RunnerTask extends TimerTask
{
	/**
	 * Constructs and initializes this object.
	 */
	public RunnerTask(Runnable r)
	{
		super();
		
		runme = r;
		return;
	}
	
	/**
	 * Calls runme.run().
	 */
	public void run()
	{
		runme.run();
		return;
	}
	
	/**
	 * This is the object we want to run.
	 */
	protected Runnable runme;
}
