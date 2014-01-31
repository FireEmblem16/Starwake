package host_process;

import java.lang.InterruptedException;

public class Timer extends Thread
{
	public Timer(Main host, Shell win)
	{
		super("Clock");
		
		this.host = host;
		this.win = win;
		time_stamp = System.currentTimeMillis();
		
		return;
	}
	
	public void run()
	{
		long time_gap = 1000 * 60 * 60 * 24;
		
		while(true)
		{
			while(System.currentTimeMillis() - time_stamp < time_gap)
			{
				try
				{
					Thread.sleep(1000 * 60);
				}
				catch(InterruptedException e)
				{}
				
				win.SetNotice2Text("Time until update: " + new Long((time_gap - (System.currentTimeMillis() - time_stamp)) / (1000 * 60)).toString() + " minutes");
			}
			
			host.Update();
			reset();
		}
	}
	
	public void reset()
	{
		time_stamp = System.currentTimeMillis() - 1;
		
		return;
	}
	
	protected long time_stamp;
	protected Main host;
	protected Shell win;
}
