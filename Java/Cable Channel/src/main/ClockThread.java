package main;

import io.FileInformation;
import java.util.ArrayList;
import player.Schedule.Video;

public class ClockThread extends Thread
{
	public ClockThread(Host app)
	{
		setDaemon(true);
		setName("Clock");
		
		host = app;
		signaled = false;
		signals = new ArrayList<Video>();
		
		return;
	}
	
	public void Signal(Video video)
	{
		signaled = true;
		signals.add(video);
		
		return;
	}
	
	public void run()
	{
		while(true)
		{
			while(!signaled)
			{
				try
				{sleep(1000L);}
				catch(InterruptedException e)
				{}
				
				// We don't load if we are one day down because playing one video will put us down a day since ints are truncated, not rounded.
				// Thus we need to wait until we are two below the goal to ensure that a full day of programming has been played.
				if(host.videos.days_loaded() < host.GetSettings().prep_days - 1)
					break;
			}
			
			if(signaled)
			{
				signaled = false;
				host.reweightree(host.GetSettings());
				
				while(signals.size() > 0)
				{
					Video video = signals.get(0);
					signals.remove(0);
					
					if(video.loaded())
						video.unload();
					else
						video.load(host.GetSettings());
				}
			}
			else
			{
				host.tree.ReturnToTrunk();
				host.reweightree(host.GetSettings());
				
				if(--host.GetSettings().lag <= 0)
				{
					FileInformation v = Randomizer.getvideo(host.GetSettings());
					
					host.tree.ReturnToTrunk();
					host.tree.EnterBranch(v);
					host.tree.ExitBranch();
					
					for(int i = 0;i < host.tree.GetNumberOfSprouts();i++)
						if(host.tree.GetBranch(i + 1).GetNumberOfSprouts() == 0)
							host.videos.add(this,host.tree.GetBranch(i + 1).GetData(),host.GetSettings());
					
					host.GetSettings().lag = host.GetSettings().special_lag;
				}
				else
					while(host.videos.days_loaded() < host.GetSettings().prep_days)
						host.videos.add(this,Randomizer.getvideo(host.GetSettings()),host.GetSettings());
			}
		}
	}
	
	private boolean signaled;
	private Host host;
	private ArrayList<Video> signals;
}
