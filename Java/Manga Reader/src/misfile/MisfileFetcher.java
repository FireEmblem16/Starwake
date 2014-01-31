package misfile;

import gui.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFileChooser;
import app.Runner;
import web.WebGet;

public class MisfileFetcher implements ActionListener
{
	public MisfileFetcher(Frame defaults)
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
		public Fetcher(MisfileFetcher g)
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
			String website = "http://www.misfile.com/overlay.php?pageCalled=";
			
			Runner.stdout.println("Fetching Misfile");
			
			if(!new File("Logs/Misfile").exists())
				try{new File("Logs/Misfile").createNewFile();}catch(IOException e){}
			
			for(int i = 1, e = 0;true;i++)
				try
				{
					if(CheckLog("Misfile",website + i))
						continue;
					
					File page = new File("page.html");
					if(!wget.get(new URL(website + i),"webpage",page))
					{
						Runner.stderr.println("Could not obtain Misfile image " + i + ".");
						continue;
					}
					
					Scanner in = new Scanner(page);
					
					if(!in.hasNext())
						;
					else if("unable".equals(in.next()))
					{
						in.close();
						
						if(e == 5)
							break;
						else
						{
							Runner.stderr.println("Misfile page " + i + " does not exist.\nContinuing to verify if this is simply an archive error.");
							e++;
							continue;
						}
					}
					
					in.close();
					e = 0;
					
					String picnum = (i < 10 ? "000" + i : (i < 100 ? "00" + i : (i < 1000 ? "0" + i : new Integer(i).toString()))) + ".jpg";
					File pic = new File(f.getPath() + "/Misfile " + picnum);
					
					copyFile(page,pic);
					Runner.stdout.println("Obatined Misfile page " + i + ".");
					
					if(!CheckLog("New",f.getPath()))
						Log("New",f.getPath());
					
					Log("Misfile",website + i);
				}
				catch(IOException err)
				{Runner.stderr.println("Could not obtain Misfile page " + i + ".");}
			
			Runner.stdout.println("Misfile crawl complete");
			g.busy = false;
			
			return;
		}
		
		protected void copyFile(File sourceFile, File destFile) throws IOException
		{
		    if(!destFile.exists())
		        destFile.createNewFile();

		    FileChannel source = null;
		    FileChannel destination = null;

		    try
		    {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source,0,source.size());
		    }
		    finally
		    { 
		    	if(source != null)
		    		source.close();
		    	
		    	if(destination != null)
		    		destination.close();
		    }
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
		
		protected MisfileFetcher g;
	}
	
	protected Frame host;
	protected boolean busy;
}
