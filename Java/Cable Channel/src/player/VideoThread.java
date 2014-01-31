package player;

import java.io.File;
import main.Host;

public class VideoThread extends Thread
{
	public VideoThread(Host app)
	{
		setDaemon(true);
		setName("Player");
		host = app;
		
		return;
	}
	
	public void run()
	{
		while(true)
		{
			f = host.videos.next(host.clock,host.GetSettings());
			
			if(f == null)
			{
				try
				{
					Thread.sleep(1000L);
				}
				catch(InterruptedException e)
				{}
				
				continue;
			}	
			
			try
			{
				p = Runtime.getRuntime().exec("mplayerc.exe \"" + f.toString() + "\" /play /close /fullscreen /new /monitor " + host.GetSettings().monitor);
				p.waitFor();
			}
			catch(Exception e)
			{}
		}
	}
	
	public void cleanup()
	{
		if(f != null)
			f.delete();
		
		return;
	}
	
	private File f;
	private Host host;
	public static Process p;
}
