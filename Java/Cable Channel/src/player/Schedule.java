package player;

import io.FileInformation;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import main.ClockThread;
import main.Host;
import main.Randomizer;
import main.Settings;

public class Schedule
{
	public Schedule()
	{
		list = new LinkedList<Video>();
		list.add(new Video(null));
		
		return;
	}
	
	public boolean add(ClockThread clock, FileInformation video, Settings settings)
	{
		if(video == null)
			return false;
		
		if(!list.add(new Video(video)))
			return false;
		
		if(list.size() < settings.pre_load)
			clock.Signal(list.get(list.size() - 1));
		
		Host.update_html(settings);
		return true;
	}
	
	public boolean add(ClockThread clock, FileInformation video, Settings settings, int index)
	{
		if(video == null)
			return false;
		
		if(index < 0 || index > list.size() - 1)
			return false;
		
		list.add(index,new Video(video));
		
		if(index < settings.pre_load)
		{
			clock.Signal(list.get(index));
			
			if(list.size() >= settings.pre_load)
				clock.Signal(list.get(settings.pre_load - 1));
		}
		
		Host.update_html(settings);
		return true;
	}
	
	public boolean alter(ClockThread clock, FileInformation video, Settings settings, int index)
	{
		if(video == null)
			return false;
		
		if(index < 0 || index > list.size() - 1)
			return false;
		
		if(index < settings.pre_load)
			clock.Signal(list.get(index));
		
		list.remove(index);
		list.add(index,new Video(video));
		
		if(index < settings.pre_load)
			clock.Signal(list.get(index));
		
		Host.update_html(settings);
		return true;
	}
	
	public boolean remove(ClockThread clock, Settings settings, int index)
	{
		if(index < 0 || index > list.size() - 1)
			return false;
		
		if(index < settings.pre_load)
			clock.Signal(list.get(index));
		
		list.remove(index);
		
		if(list.size() == 0)
			list.add(new Video(null));
		
		if(index < settings.pre_load)
			clock.Signal(list.get(index));
		
		while(days_loaded() < settings.prep_days)
			add(clock,Randomizer.getvideo(settings),settings);
		
		Host.update_html(settings);
		return true;
	}
	
	public File next(ClockThread clock, Settings settings)
	{
		if(list.size() == 0)
			return null;
		
		clock.Signal(list.get(0));
		list.remove();
		
		if(list.size() == 0)
			list.add(new Video(null));
		
		if(list.size() > settings.pre_load - 1)
			clock.Signal(list.get(settings.pre_load - 1));
		
		Host.update_html(settings);
		
		if(list.size() == 0)
			return null;
		
		return list.get(0).gethandle();
	}
	
	public Video get(int index)
	{
		if(index < 0 || index > list.size() - 1)
			return null;
		
		return list.get(index);
	}
	
	public boolean cleanup(Settings settings)
	{
		boolean ret = true;
		
		for(int i = 0;i <= settings.pre_load && i < list.size();i++)
			if(!list.get(i).unload())
				ret = false;
		
		if(list.size() > settings.pre_load)
			list.get(settings.pre_load).unload();
		
		return ret;
	}
	
	/**
	 * Needs to be fixed to actually count how long videos are.
	 */
	public int days_loaded()
	{
		return list.size();
	}
	
	public int size()
	{
		return list.size();
	}
	
	public class Video
	{
		private Video(FileInformation video)
		{
			f = null;
			
			if(video == null)
			{
				loc = null;
				name = null;
				
				return;
			}
			
			loc = video.GetURL();
			name = video.toString();
			loaded = false;
			
			return;
		}
		
		public boolean load(Settings settings)
		{
			if(loc == null)
			{
				loaded = false;
				return false;
			}
			
			f = new File(settings.mpc_loc.substring(0,settings.mpc_loc.lastIndexOf('/') + 1) + new Long(System.currentTimeMillis()).toString());
			
			if(!Host.wget.get(loc,"remote file",f))
			{
				f = null;
				loaded = false;
				
				return false;
			}
			
			try
			{
				f.deleteOnExit();
			}
			catch(ExceptionInInitializerError e)
			{
				f.delete();
			}
			
			loaded = true;
			return true;
		}
		
		public boolean unload()
		{
			if(f == null)
			{
				loaded = false;
				return false;
			}
			
			boolean ret = f.delete();
			f = null;
			
			loaded = ret;
			return ret;
		}
		
		public boolean loaded()
		{
			return loaded;
		}
		
		public File gethandle()
		{
			return f;
		}
		
		public String toString()
		{
			if(name == null)
				return null;
			
			String ret = name;
			
			while(ret.contains("%20"))
				ret = ret.replace("%20"," ");
			
			return ret;
		}
		
		private boolean loaded;
		private File f;
		private String name;
		private URL loc;
	}
	
	private LinkedList<Video> list;
}
