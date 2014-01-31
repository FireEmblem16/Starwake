package main;

import io.FileInformation;
import io.WebGet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import player.Schedule;
import player.Schedule.Video;
import player.VideoThread;
import tree.TreeDiagram;

public class Host extends Thread
{
	public static void main(String[] args)
	{
		new Host().start();
		return;
	}
	
	public Host()
	{
		wget = new WebGet();
		tree = new TreeDiagram<FileInformation>("Yggdrasil");
		videos = new Schedule();
		settings = null;
		in = new Scanner(System.in);
		
		Runtime.getRuntime().addShutdownHook(new Cleanup(this));
		return;
	}
	
	private String posequestion(String msg)
	{
		System.out.print(msg);
		return in.nextLine();
	}
	
	private Settings loadsettings(File f)
	{
		try
		{
			Scanner file = new Scanner(f);
			Settings s = new Settings();
			
			if(!file.hasNextInt())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - pre_load");
			}
			
			s.pre_load = file.nextInt();
			file.nextLine();
			
			if(!file.hasNextInt())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - prep_days");
			}
			
			s.prep_days = file.nextInt();
			file.nextLine();
			
			if(!file.hasNextInt())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - special_lag");
			}
			
			s.special_lag = file.nextInt();
			file.nextLine();
			
			if(!file.hasNextInt())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - weight_op_1");
			}
			
			s.weight_op_1 = file.nextInt();
			file.nextLine();
			
			if(!file.hasNextInt())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - weight_op_2");
			}
			
			s.weight_op_2 = file.nextInt();
			file.nextLine();
			
			if(!file.hasNextInt())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - monitor");
			}
			
			s.monitor = file.nextInt();
			file.nextLine();
			
			if(!file.hasNextLine())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - index");
			}
			
			s.index = file.nextLine();
			
			if(!file.hasNextLine())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - location");
			}
			
			s.location = file.nextLine();
			
			if(!new File(s.location).exists())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - location - location does not exist");
			}
			
			if(!new File(s.location).isFile())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - location - the location must be a file");
			}
			
			if(!file.hasNextLine())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - mpc_loc");
			}
			
			s.mpc_loc = file.nextLine();
			
			if(!new File(s.index).exists())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - mpc_loc - Media Player Classic does not exist");
			}
			
			if(!new File(s.index).isFile())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - mpc_loc - Media Player Classic must be a file");
			}
			
			if(!file.hasNextLine())
			{
				file.close();
				throw new FileNotFoundException("The settings file was corrupted - root");
			}
			
			StringBuilder str = new StringBuilder();
			
			s.root = file.nextLine();
			
			try
			{
				if(!wget.get(new URL(s.root),"remote file",str))
				{
					file.close();
					throw new FileNotFoundException("Root could not be accessed.");
				}
			}
			catch(MalformedURLException e)
			{
				file.close();
				throw new FileNotFoundException("Settings file was corrupted - root - malformed URL");
			}
			
			if(str.length() == 0)
			{
				file.close();
				throw new FileNotFoundException("Settings file was corrupted - root - no URL");
			}
			
			file.close();
			return s;
		}
		catch(FileNotFoundException e)
		{
			if(e.getMessage() == null || e.getMessage().length() == 0)
				System.err.println("Settings file could not be found");
			else
				System.err.println(e.getMessage());
			
			return new Settings();
		}
	}
	
	private void savesettings()
	{
		File f = new File(settings.location);
		
		if(f.exists())
			f.delete();
		
		FileWriter out;
		
		try
		{
			f.createNewFile();
			out = new FileWriter(f);
			out.write(settings.toString());
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("The settings file could not be opened for saving.");
			return;
		}
		
		return;
	}
	
	public static boolean createtree(String base)
	{
		URL url;
		
		while(base.contains(" "))
		{
			String temp = base.substring(0,base.indexOf(' ')) + "%20";
			temp += base.substring(base.indexOf(' ') + 1);
			base = temp;
		}
		
		try
		{
			url = new URL(base);
		}
		catch(MalformedURLException e)
		{
			System.err.println("The root directory for video is invalid.");
			return false;
		}
		
		StringBuilder str = new StringBuilder();
		
		if(!wget.get(url,"remote file",str))
		{
			System.err.println("File information could not be obtained. Perhpas you don't have Internet.\nMissing file name: " + url.toString());
			return false;
		}
		
		String html = str.toString();
		
		while(html.contains("<li>"))
		{
			String next = html.substring(0,html.indexOf("\n"));
			html = html.substring(html.indexOf('\n') + 1);
			
			if(!next.contains("<li>"))
				continue;
			
			if(next.contains("Parent Directory"))
				continue;
			
			next = next.substring(next.indexOf("\">") + 3);
			next = next.substring(0,next.indexOf("</a></li>"));
			
			while(next.contains(" "))
			{
				String temp = next.substring(0,next.indexOf(' ')) + "%20";
				temp += next.substring(next.indexOf(' ') + 1);
				next = temp;
			}
			
			next = base + "/" + next;
			
			boolean is_dir = false;
			
			if(next.substring(next.length() - 1).equals("/"))
			{
				is_dir = true;
				next = next.substring(0,next.length() - 1);
			}
			
			URL next_url;
			
			try
			{
				next_url = new URL(next);
			}
			catch(MalformedURLException e)
			{
				System.err.println("The root directory contains an invalid link somewhere.\nInvalid value found: " + next);
				return false;
			}
			
			FileInformation info = new FileInformation(next_url,is_dir);
			
			tree.AddBranch(info.toString());
			tree.EnterBranch(tree.GetNumberOfSprouts());
			tree.SetData(info);
			
			if(info.IsDir())
				createtree(next_url.toString());
			
			tree.ExitBranch();
		}
		
		return true;
	}
	
	public static void zerotree()
	{
		for(int i = 0;i < tree.GetNumberOfSprouts();i++)
		{
			tree.GetData().weight = 0;
			tree.EnterBranch(i + 1);
			zerotree();
			tree.ExitBranch();
		}
		
		return;
	}
	
	public static void weightree(Settings settings)
	{
		if(tree.GetNumberOfSprouts() == 0)
		{
			if(tree.GetData().IsDir())
			{
				tree.GetData().weight = 0;
				
				return;
			}
			
			switch(settings.weight_op_2)
			{
			case Settings.PLAY_LEAST_OFTEN_PLAYED:
			case Settings.PLAY_MOST_OFTEN_PLAYED:
				tree.GetData().weight = tree.GetData().plays + 1;
				break;
			case Settings.PLAY_IN_ORDER:
			case Settings.PLAY_RANDOM:
				tree.GetData().weight = 1;
				break;
			}
			
			return;
		}
		
		boolean has_file = false;
		
		for(int i = 0;i < tree.GetNumberOfSprouts();i++)
		{
			tree.EnterBranch(i + 1);
			weightree(settings);
			tree.ExitBranch();
			
			switch(settings.weight_op_1)
			{
			case Settings.FILES_IN_FOLDER:
				if(!tree.GetBranch(i + 1).GetData().IsDir() || tree.GetBranch(i + 1).GetNumberOfSprouts() != 0)
					tree.GetData().weight += tree.GetBranch(i + 1).GetData().weight;
				
				break;
			case Settings.FOLDERS_IN_FOLDER:
				if(tree.GetBranch(i + 1).GetData().IsDir())
					tree.GetData().weight += tree.GetBranch(i + 1).GetData().weight;
				else
					has_file = true;
				
				break;
			case Settings.BOTH:
				tree.GetData().weight += tree.GetBranch(i + 1).GetData().weight;
				break;
			case Settings.TOTAL_RANDOM:
				if(tree.GetBranch(i + 1).GetData().weight != 0)
					tree.GetData().weight = 1;
				
				break;
			}
		}
		
		if(settings.weight_op_1 == Settings.FOLDERS_IN_FOLDER)
			if(tree.GetData().weight == 0 && has_file)
				tree.GetData().weight = 1;
		
		return;
	}
	
	public static void reweightree(Settings settings)
	{
		zerotree();
		weightree(settings);
	
		return;
	}
	
	public Settings GetSettings()
	{
		return settings;
	}
	
	public static void update_html(Settings settings)
	{
		File html = new File(settings.index);
		
		if(html.exists())
			html.delete();
		
		int try_again = 0;
		
		while(try_again < 3)
		{
			try
			{
				html.createNewFile();
				try_again = 3;
			}
			catch(IOException e)
			{
				try_again++;
			}
		}
		
		StringBuilder output = new StringBuilder();
		
		try
		{
			File f = new File(".\\base");
			Scanner in = new Scanner(f);
			
			while(in.hasNextLine())
				output.append(in.nextLine() + (in.hasNextLine() ? "\n" : ""));
			
			in.close();
		}
		catch(Exception e)
		{
			System.err.println("The html file could not be updated - base file outline could not be loaded");
			return;
		}
		
		for(int i = 0;i < 10;i++)
		{
			int index = output.indexOf("EDITME");
			output.replace(index,index + 6,"");
			
			Video video = videos.get(i);
			
			if(video == null)
				output.replace(index - 4,index + 5,"");
			else
				output.insert(index,video.toString());
		}
		
		Video video = videos.get(0);
		int index = output.indexOf("INSERT");
		
		for(int i = 1;video != null;i++)
		{
			output.insert(index,video.toString() + "<br />\n\t\t\t\t\t\t\t   ");
			
			index = output.indexOf("INSERT");
			video = videos.get(i);
		}
		
		output.replace(index,index + 6,"");
		
		try
		{
			FileWriter out = new FileWriter(html);
			out.write(output.toString());
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("The html file could not be updated - could not open file for writing");
			return;
		}
		
		return;
	}
	
	private void execute_info(StringTokenizer tkns)
	{
		if(tkns != null)
			if(tkns.hasMoreTokens())
			{
				System.err.println("Too many arguments.");
				return;
			}
		
		System.out.println("You're running The Cable Channel ©2010 Version 1.5");
		System.out.println("Created by Omacron Naomi");
		System.out.println("");
		
		return;
	}
	
	private void execute_help(StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			System.err.println("Too many arguments.");
			return;
		}
		
		System.out.println("Available commands, all have help available when given the option -h");
		System.out.println("add - adds a specified location to the video tree");
		System.out.println("exit - exits the program and cleans up temp data");
		System.out.println("help - displays this message");
		System.out.println("info - displays the about section of this program");
		System.out.println("monitor - displays or changes the monitor videos should be played on");
		System.out.println("next - stops playback of the current video and proceeds to play the next on the schedule");
		System.out.println("node - performs an operation on the viewing of the video tree");
		System.out.println("remove - removes the current node from the video tree");
		System.out.println("rescan - rescans the root directory for videos, destroys play count");
		System.out.println("root - displays the current location of the root directory or moves it if a valid link is specified");
		System.out.println("schedule - performs an operation on the video schedule");
		System.out.println("weight_op_1 - changes the way directories are choosen for video randomization");
		System.out.println("weight_op_2 - changes the way files are choosen during the randomization process");
		System.out.println("");
		
		return;
	}
	
	private void execute_wop1(String cmd, StringTokenizer tkns)
	{
		try
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
		}
		catch(NoSuchElementException e)
		{
			System.err.println("Too few arguments.");
			
			cmd = "";
			return;
		}
		
		if(tkns.hasMoreTokens())
		{
			System.err.println("Too many arguments.");
			return;
		}
		
		cmd = cmd.toLowerCase();
		
		if(cmd.equals("h"))
		{
			System.out.println("weights_op_1 [settings]");
			System.out.println("-c - displays the current setting");
			System.out.println("-fd - chooses direcories based on the number of files in them and their sub directories");
			System.out.println("-dd - chooses directories based on the number of directories in them and their sub directories");
			System.out.println("-bfd - chooses directories based on the number of directories and files in them and their sub directories");
			System.out.println("-r - chooses directories at random");
			System.out.println("");
		}
		else if(cmd.equals("c"))
			System.out.println(settings.get_weight_op_1_name() + "\n");
		else if(cmd.equals("fd"))
		{
			settings.weight_op_2 = settings.FILES_IN_FOLDER;
			reweightree(settings);
		}
		else if(cmd.equals("dd"))
		{
			settings.weight_op_2 = settings.FOLDERS_IN_FOLDER;
			reweightree(settings);
		}
		else if(cmd.equals("bfd"))
		{
			settings.weight_op_2 = settings.BOTH;
			reweightree(settings);
		}
		else if(cmd.equals("r"))
		{
			settings.weight_op_2 = settings.TOTAL_RANDOM;
			reweightree(settings);
		}
		
		return;
	}
	
	private void execute_wop2(String cmd, StringTokenizer tkns)
	{
		try
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
		}
		catch(NoSuchElementException e)
		{
			System.err.println("Too few arguments.");
			
			cmd = "";
			return;
		}
		
		if(tkns.hasMoreTokens())
		{
			System.err.println("Too many arguments.");
			return;
		}
		
		cmd = cmd.toLowerCase();
		
		if(cmd.equals("h"))
		{
			System.out.println("weights_op_2 [settings]");
			System.out.println("-c - displays the current setting");
			System.out.println("-io - plays videos in the order they appear in a directory");
			System.out.println("-lo - plays videos that have a lesser play count in a directory");
			System.out.println("-mo - plays videos that have a greater play count in a directory");
			System.out.println("-pr - plays videos at random in a directory");
			System.out.println("");
		}
		else if(cmd.equals("c"))
			System.out.println(settings.get_weight_op_2_name() + "\n");
		else if(cmd.equals("pr"))
		{
			settings.weight_op_2 = settings.PLAY_RANDOM;
			reweightree(settings);
		}
		else if(cmd.equals("io"))
		{
			settings.weight_op_2 = settings.PLAY_IN_ORDER;
			createtree(settings.root);
			reweightree(settings);
		}
		else if(cmd.equals("lo"))
		{
			settings.weight_op_2 = settings.PLAY_LEAST_OFTEN_PLAYED;
			reweightree(settings);
		}
		else if(cmd.equals("mo"))
		{
			settings.weight_op_2 = settings.PLAY_MOST_OFTEN_PLAYED;
			reweightree(settings);
		}
		
		return;
	}
	
	private void execute_rescan(StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			System.err.println("Too many arguments.");
			return;
		}
		
		createtree(settings.root);
		reweightree(settings);
		return;
	}
	
	private void execute_root(String cmd, StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
			
			if(tkns.hasMoreTokens())
			{
				System.err.println("Too many arguments.");
				return;
			}
			
			if(cmd.toLowerCase().equals("h"))
			{
				System.out.println("root [location]");
				System.out.println("Displays the current location of the root directory.");
				System.out.println("");
				System.out.println("-[location] - changes the root directory to this target if [location] is a valid link");
				System.out.println("");
				
				return;
			}
			
			try
			{
				if(wget.get(new URL(cmd),"remote file",new StringBuilder()))
				{
					String old = settings.root;
					settings.root = cmd;
					
					if(!old.equals(cmd))
					{
						createtree(settings.root);
						reweightree(settings);
					}
					else
					{
						System.err.println("The specified root directory is the same as the old root directory.");
					}
				}
				else
					System.err.println("The specified root directory could not be resolved.");
			}
			catch(MalformedURLException e)
			{
				System.err.println("An invalid link was provided.");
			}
			
			return;
		}
		
		System.out.println("Current root directory: " + settings.root + "\n");
		return;
	}
	
	private void execute_add(String cmd, StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
			
			if(tkns.hasMoreTokens())
			{
				System.err.println("Too many arguments.");
				return;
			}
			
			if(cmd.toLowerCase().equals("h"))
			{
				System.out.println("add [location]");
				System.out.println("Adds the specified file xor directory to the video tree at the current node. Is not preserved if the tree is destroyed.");
				System.out.println("");
				System.out.println("-[location] - adds this file xor directory and its contents to the video tree at the current node");
				System.out.println("");
				
				return;
			}
			
			TreeDiagram root = tree;
			tree = new TreeDiagram<FileInformation>("Fake Root");
			
			if(!createtree(cmd))
			{
				System.err.println("The specified location was invalid.");
				tree = root;
				
				return;
			}
			
			TreeDiagram<FileInformation> temp = tree;
			tree = root;
			
			tree.GetCurrentBranch().AddBranch(temp.GetBranch(1));
			reweightree(settings);
		}
		else
			System.err.println("Not enough arguments.");
		
		return;
	}
	
	private void execute_remove(String cmd, StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
			
			if(tkns.hasMoreTokens())
			{
				System.err.println("Too many arguments.");
				return;
			}
			
			if(cmd.toLowerCase().equals("h"))
			{
				System.out.println("remove");
				System.out.println("Removes the current node from the video node.");
				System.out.println("");
				
				return;
			}
		}
		else
		{
			tree.RemoveCurrent();
			tree.ReturnToTrunk();
			reweightree(settings);
		}
		
		return;
	}
	
	private void execute_node(String cmd, StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
			
			if(tkns.hasMoreTokens())
			{
				System.err.println("Too many arguments.");
				return;
			}
			
			cmd = cmd.toLowerCase();
			
			if(cmd.equals("h"))
			{
				System.out.println("node [-#/-b/-ls/-t]");
				System.out.println("Displays the current node if no additional arguments are specified or performs some other operation on the video tree.");
				System.out.println("No garuntees that the current node will not change spontaniously. When the playing video changes the node will return to root.");
				System.out.println("Some function calls will return you to the root node as well. A call to node -s will return you to the node you were at.");
				System.out.println("");
				System.out.println("-# - enters the [#]th node of the current node if it exists");
				System.out.println("-b - enters the node directly above the current node if it exists");
				System.out.println("-ls - lists all the items in the current node");
				System.out.println("-s - enters the node that was last entered");
				System.out.println("-t - enters the trunk of the video tree");
				System.out.println("");
			}
			else if(cmd.equals("b"))
				tree.ExitBranch();
			else if(cmd.equals("ls"))
			{
				System.out.println();
				
				for(int i = 0;i < tree.GetNumberOfSprouts();i++)
					System.out.println(tree.GetBranch(i + 1).GetData().toString());
				
				System.out.println();
			}
			else if(cmd.equals("s"))
				tree.ReturnToSeed();
			else if(cmd.equals("t"))
				tree.ReturnToTrunk();
			else
			{
				int branch = 0;
				
				try
				{
					branch = Integer.parseInt(cmd);
				}
				catch(NumberFormatException e)
				{
					System.err.println("An invalid argument was specified.");
					return;
				}
				
				tree.EnterBranch(branch);
			}
		}
		else
			System.out.println("Current node: " + tree.GetCurrentBranch().GetData().toString() + "\n");
		
		return;
	}
	
	private void execute_schedule(String cmd, StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			tkns.nextToken("-");
			cmd = tkns.nextToken();
			cmd = cmd.toLowerCase();
			
			if(cmd.equals("h"))
			{
				System.out.println("schedule [-#] [-a/-r/-ls]");
				System.out.println("Displays the video title specified by # by default. If a or r is specified the operation changes.");
				System.out.println("");
				System.out.println("-# - performs the operation on the [#]th video of the schedule (0 is the default)");
				System.out.println("-a - adds the current node of the video tree if possible");
				System.out.println("-ls - lists all the videos in the schedule");
				System.out.println("-r - removes the specified element of the schedule if possible");
				System.out.println("");
			}
			else if(cmd.equals("a"))
				if(tree.GetNumberOfSprouts() == 0)
					videos.add(clock,tree.GetCurrentBranch().GetData(),settings);
				else
					System.err.println("Directories can not be added to the schedule.");
			else if(cmd.equals("ls"))
			{
				System.out.println();
				
				for(int i = 0;i < videos.size();i++)
					System.out.println(videos.get(i));
				
				System.out.println();
			}
			else if(cmd.equals("r"))
				videos.remove(clock,settings,0);
			else
			{
				int item = 0;
				
				try
				{
					if(cmd.indexOf(' ') != 0)
						cmd = cmd.substring(0,cmd.length() - 1);
					
					item = Integer.parseInt(cmd);
				}
				catch(NumberFormatException e)
				{
					System.err.println("An invalid index was specified.");
					return;
				}
				
				if(tkns.hasMoreTokens())
				{
					cmd = tkns.nextToken("-");
					cmd = cmd.toLowerCase();
					
					if(tkns.hasMoreTokens())
					{
						System.err.println("Too many arguments.");
						return;
					}
					
					if(cmd.equals("a"))
						if(tree.GetNumberOfSprouts() == 0)
							videos.add(clock,tree.GetCurrentBranch().GetData(),settings,item);
						else
							System.err.println("Directories can not be added to the schedule.");
					else if(cmd.equals("ls"))
					{
						System.out.println();
						
						for(int i = item;i < videos.size();i++)
							System.out.println(videos.get(i));
						
						System.out.println();
					}
					else if(cmd.equals("r"))
						videos.remove(clock,settings,item);
					else
					{
						System.err.println("An invalid argument was specified.");
						return;
					}
				}
				else
					System.out.println("[" + new Integer(item).toString() + "]th video: " + (videos.get(item) == null ? "no such video" : videos.get(item).toString()));
			}
		}
		else
			System.out.println("Current video: " + (videos.get(0) == null ? "no such video" : videos.get(0).toString()));
		
		return;
	}
	
	private void execute_monitor(String cmd, StringTokenizer tkns)
	{
		if(tkns.hasMoreTokens())
		{
			cmd = tkns.nextToken("-");
			cmd = tkns.nextToken("-");
			
			if(tkns.hasMoreTokens())
			{
				System.err.println("Too many arguments.");
				return;
			}
			
			if(cmd.toLowerCase().equals("h"))
			{
				System.out.println("monitor [-#]");
				System.out.println("Displays the current monitor videos will be played on or changes the monitor to be played on.");
				System.out.println("");
				System.out.println("-# - the monitor videos should be played on");
				System.out.println("");
				
				return;
			}
			
			try
			{
				settings.monitor = Integer.parseInt(cmd);
				
			}
			catch(NumberFormatException e)
			{
				System.err.println("An invalid argument was specified.");
				return;
			}
		}
		else
			System.out.println("Current monitor: " + settings.monitor);
		
		return;
	}
	
	private void execute_next()
	{
		if(VideoThread.p == null)
		{
			System.err.println("No video is currently playing.");
			return;
		}
		
		VideoThread.p.destroy();
		return;
	}
	
	public void run()
	{
		execute_info(null);
		settings = loadsettings(new File(posequestion("Location of settings file: ")));
		
		try
		{
			tree.SetData(new FileInformation(new URL(settings.root),true));
		}
		catch(MalformedURLException e)
		{
			System.err.println("The value of root is not valid.");
			
			in.close();
			return;
		}
		
		createtree(settings.root);
		reweightree(settings);
		
		clock = new ClockThread(this);
		clock.start();
		
		while(videos.days_loaded() < settings.prep_days)
			videos.add(clock,Randomizer.getvideo(settings),settings);
		
		while(!videos.get(1).loaded())
		{
			try
			{sleep(1000L);}
			catch(InterruptedException e)
			{}
		}
		
		player = new VideoThread(this);
		player.start();
		
		String cmd = "";
		
		StringTokenizer ltkns = new StringTokenizer("schedule");
		
		while(cmd != null && !cmd.equals("exit"))
		{
			String ret = posequestion("Enter a command: ").toLowerCase();
			StringTokenizer tkns = null;
			
			if(ret != null && !ret.equals(""))
			{
				tkns = new StringTokenizer(ret);
				ltkns = tkns;
			}
			else
				tkns = ltkns;
			
			if(tkns.hasMoreTokens())
				cmd = tkns.nextToken();
			else
				cmd = "";
			
			if(cmd.equals("info"))
				execute_info(tkns);
			else if(cmd.equals("help"))
				execute_help(tkns);
			else if(cmd.equals("weight_op_1"))
				execute_wop1(cmd,tkns);
			else if(cmd.equals("weight_op_2"))
				execute_wop2(cmd,tkns);
			else if(cmd.equals("rescan"))
				execute_rescan(tkns);
			else if(cmd.equals("root"))
				execute_root(cmd,tkns);
			else if(cmd.equals("add"))
				execute_add(cmd,tkns);
			else if(cmd.equals("remove"))
				execute_remove(cmd,tkns);
			else if(cmd.equals("node"))
				execute_node(cmd,tkns);
			else if(cmd.equals("schedule"))
				execute_schedule(cmd,tkns);
			else if(cmd.equals("monitor"))
				execute_monitor(cmd,tkns);
			else if(cmd.equals("next"))
				execute_next();
		}
		
		if(VideoThread.p != null)
			VideoThread.p.destroy();
		
		return;
	}
	
	public void cleanup()
	{
		savesettings();
		in.close();
		
		return;
	}
	
	private Scanner in;
	private volatile Settings settings;
	
	public static ClockThread clock;
	public static Schedule videos;
	public volatile static TreeDiagram<FileInformation> tree;
	public static VideoThread player;
	public volatile static WebGet wget;
}