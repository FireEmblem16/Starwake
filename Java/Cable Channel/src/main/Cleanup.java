package main;

public class Cleanup extends Thread
{
	public Cleanup(Host app)
	{
		host = app;
		
		return;
	}
	
	public void run()
	{
		for(int tryagain = 0;!host.videos.cleanup(host.GetSettings()) && tryagain < 3;tryagain++);
		host.cleanup();
		host.player.cleanup();
		
		return;
	}
	
	private Host host;
}
