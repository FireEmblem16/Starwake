package girly;

import gui.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFileChooser;
import app.Runner;
import web.WebGet;

public class GirlyFetcher implements ActionListener
{
	public GirlyFetcher(Frame defaults)
	{
		host = defaults;
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(!busy)
		{
			busy = true;
			new Fetcher(this).start();
		}
		
		return;
	}
	
	protected class Fetcher extends Thread
	{
		public Fetcher(GirlyFetcher g)
		{
			super();
			setDaemon(true);
			
			this.g = g;
			return;
		}
		
		public void run()
		{
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setMultiSelectionEnabled(false);
			fc.setCurrentDirectory(g.host.GetDefaultDirectory() == null ? null : g.host.GetDefaultDirectory());
			int retVal = fc.showOpenDialog(host);
			
			File f = fc.getSelectedFile();
			
			if(f == null)
				return;
		
			WebGet wget = new WebGet();
			String website = "http://girlyyy.com";
			int num = 764;
			
			Runner.stdout.println("Fetching Girly");
			
			if(!new File("Logs/Girly").exists())
				try{new File("Logs/Girly").createNewFile();}catch(IOException e){}
			
			for(int i = 1;i <= num;i++)
				try
				{
					File page = new File("page.html");
					if(!wget.get(new URL(website + "/go/" + i),"webpage",page))
					{
						Runner.stderr.println("Could not obtain Girly page " + i + ".");
						continue;
					}
					
					Scanner in = new Scanner(page);
					
					while(in.hasNextLine())
					{
						String next = in.nextLine();
						
						while(next != null && next.contains("girly_"))
						{
							int eindex = next.indexOf("\"",next.indexOf("girly_"));
							int sindex = next.substring(0,eindex).lastIndexOf("\"") + 1;
							
							String link = next.substring(sindex,eindex);
							
							if(!link.contains("http://"))
								link = website + link;
							
							String picname = link.substring(link.lastIndexOf("/") + 1);
							File pic = new File(f.getPath() + "/" + picname);
							
							Runner.stdout.println("Checking from Girly page " + i + ": " + picname);
							
							if(!CheckLog("Girly",pic.getPath()))
							{
								if(!wget.get(new URL(link),"webpage",pic))
									Runner.stderr.println("Could not obtain Girly page " + i + ".");
								else
								{
									if(!CheckLog("New",f.getPath()))
										Log("New",f.getPath());
									
									if(!CheckLog("Girly",pic.getPath()))
										Log("Girly",pic.getPath());
								}
							}
							
							next = next.substring(eindex + 1);
						}
					}
					
					in.close();
				}
				catch(IOException err)
				{Runner.stderr.println("Could not obtain Girly page " + i + ".");}
			
			Runner.stdout.println("Girly crawl complete");
			g.busy = false;
			
			return;
		}
		
		public boolean CheckLog(String Name, String text)
		{
			try
			{
				FileReader file = new FileReader(new File("Logs/" + Name));
				BufferedReader in = new BufferedReader(file);
				
				String next = in.readLine();
				
				while(next != null && !next.equals(text))
					next = in.readLine();
				
				try
				{
					in.close();
					file.close();
				}
				catch(IOException e)
				{}
				
				if(next != null && next.equals(text))
					return true;
			}
			catch(IOException e)
			{}
			
			return false;
		}
		
		public void Log(String Name, String text)
		{
			try
			{
				FileReader file = new FileReader(new File("Logs/" + Name));
				Scanner in = new Scanner(file);
				
				ArrayList<String> File = new ArrayList<String>();
				
				while(in.hasNextLine())
					File.add(in.nextLine());
				
				File.add(text);
				
				try
				{
					in.close();
					file.close();
				}
				catch(IOException e)
				{}
				
				Object[] strs = File.toArray();
				Arrays.sort(strs);
				
				FileWriter file2 = new FileWriter(new File("Logs/" + Name));
				BufferedWriter out = new BufferedWriter(file2);
				
				if(out == null)
					return;
				
				for(int i = 0;i < strs.length;i++)
				{
					out.write(strs[i].toString());
					out.newLine();
				}
				
				try
				{
					out.close();
					file2.close();
				}
				catch(IOException e)
				{}
			}
			catch(IOException e)
			{}
			
			return;
		}
		
		protected GirlyFetcher g;
	}
	
	protected Frame host;
	protected boolean busy;
}
