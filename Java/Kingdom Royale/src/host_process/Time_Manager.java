package host_process;

import gui.Game;

public class Time_Manager extends Thread
{
	public Time_Manager(Main host, String name)
	{
		super(name);
		this.host = host;
		
		run = true;
		
		time_lengths = new long[6];
		
		if(host.ops.eliminate_extra_time)
		{
			if(host.ops.game_speed.equals("8 Days"))
			{
				time_lengths[0] = 1000 * 60 * 60 * 12;
				time_lengths[1] = 1000 * 60 * 60 * 2;
				time_lengths[2] = 1000 * 60 * 60 * 4;
				time_lengths[3] = 1000 * 60 * 60 * 2;
				time_lengths[4] = 1000 * 60 * 60 * 2;
				time_lengths[5] = 1000 * 60 * 60 * 2;
			}
			else if(host.ops.game_speed.equals("1 Day"))
			{
				time_lengths[0] = 1000 * 60 * 60 * 12 / 8;
				time_lengths[1] = 1000 * 60 * 60 * 2 / 8;
				time_lengths[2] = 1000 * 60 * 60 * 4 / 8;
				time_lengths[3] = 1000 * 60 * 60 * 2 / 8;
				time_lengths[4] = 1000 * 60 * 60 * 2 / 8;
				time_lengths[5] = 1000 * 60 * 60 * 2 / 8;
			}
			else if(host.ops.game_speed.equals("2 Hours"))
			{
				time_lengths[0] = 1000 * 30;
				time_lengths[1] = 1000 * 60 * 2;
				time_lengths[2] = 1000 * 60 * 9;
				time_lengths[3] = 1000 * 60 * 2;
				time_lengths[4] = 1000 * 60;
				time_lengths[5] = 1000 * 30;
			}
		}
		else
		{
			if(host.ops.game_speed.equals("8 Days"))
			{
				time_lengths[0] = 0;
				time_lengths[1] = 1000 * 60 * 60 * 2;
				time_lengths[2] = 1000 * 60 * 60 * 4;
				time_lengths[3] = 1000 * 60 * 60 * 2;
				time_lengths[4] = 1000 * 60 * 20;
				time_lengths[5] = 0;
			}
			else if(host.ops.game_speed.equals("1 Day"))
			{
				time_lengths[0] = 0;
				time_lengths[1] = 1000 * 60 * 60 * 2 / 8;
				time_lengths[2] = 1000 * 60 * 60 * 4 / 8;
				time_lengths[3] = 1000 * 60 * 60 * 2 / 8;
				time_lengths[4] = 1000 * 60 * 20 / 8;
				time_lengths[5] = 0;
			}
			else if(host.ops.game_speed.equals("2 Hours"))
			{
				time_lengths[0] = 0;
				time_lengths[1] = 1000 * 60 * 2;
				time_lengths[2] = 1000 * 60 * 9;
				time_lengths[3] = 1000 * 60 * 2;
				time_lengths[4] = 1000 * 60;
				time_lengths[5] = 0;
			}
		}
		
		return;
	}
	
	public void run()
	{
		while(run)
		{
			time_stamp = System.currentTimeMillis();
			
			if(TurnChecker.round - 1 > 0 && TurnChecker.round - 1 < 7)
			{
				while(System.currentTimeMillis() - time_stamp < time_lengths[TurnChecker.round - 1])
				{
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException e)
					{}
				}
				
				TurnChecker.create();
			}
		}
	}
	
	public void halt()
	{
		run = false;
		
		return;
	}
	
	public long time_stamp;
	public long[] time_lengths;
	public Main host;
	
	private boolean run;
}
