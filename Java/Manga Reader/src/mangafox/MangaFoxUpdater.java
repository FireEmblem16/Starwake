package mangafox;

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
import app.Runner;
import web.WebGet;

public class MangaFoxUpdater implements ActionListener
{
	public MangaFoxUpdater(String destination)
	{
		dest = destination;
		website = "http://www.mangafox.com";
		wget = new WebGet();
		busy = false;
		
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(!busy)
		{
			busy = true;
			new Updater(this).start();
		}
		
		return;
	}
	
	protected class Updater extends Thread
	{
		public Updater(MangaFoxUpdater m)
		{
			super();
			setDaemon(true);
			
			this.m = m;
			return;
		}
		
		public void run()
		{
			Update();
			return;
		}
		
		public void Update()
		{
			try
			{
				FileReader file = new FileReader(new File("MangaFoxFetch"));
				BufferedReader in = new BufferedReader(file);
				
				int c = -1;
				c = Integer.parseInt(in.readLine());
				
				// Loop for each series
				for(int i = 0;i < c;i++)
				{
					String Name = in.readLine();
					String Link = in.readLine();
					
					Runner.stdout.println("Looking into " + Name);
					
					File File = new File("base.html");
					if(!wget.get(new URL(Link),"webpage",File))
					{
						Runner.stderr.println("Could not update " + Name + ". The link's name has most likely changed.");
						continue;
					}
					
					Base_Parser parser = new Base_Parser(File);
					
					// Loop for each chapter
					while(true)
					{
						String[] next_link_arr = parser.NextLink();
						String next_link = next_link_arr[0];
						
						if(!parser.HasMoreLinks())
						{
							if(parser.IsUnavailable())
								Runner.stderr.println("The series " + Name + " is no longer available...bastards.");
							
							break;
						}
						
						String volume =  next_link.substring(0,next_link.lastIndexOf('/'));
						String chapter = volume.substring(volume.lastIndexOf('/') + 2);
						volume = volume.substring(0,volume.lastIndexOf('/'));
						volume = volume.substring(volume.lastIndexOf('/') + 2);
						int index2 = volume.lastIndexOf("/") + 2;
						
						String folder = dest + Name + "/" + Name + " Volume " + volume + "/" + Name + " Chapter " + (Float.parseFloat(chapter) % 1 == 0 ? new Integer(Integer.parseInt(chapter)).toString() : new Float(Float.parseFloat(chapter)).toString());
						
						if(!CheckLog(Name,folder))
						{
							String link_ext = next_link_arr[1];
							String link_ext_last = "";
							new File(folder).mkdir();
							
							int err_c = 0;
							
							// Loop for each page
							while(true)
							{
								if(link_ext == null)
									link_ext = link_ext_last;
								
								File File2 = new File("picture.html");
								
								if(!wget.get(new URL(next_link + link_ext + "?no_warning=1"),"webpage",File2))
								{
									err_c++;
									
									if(err_c < 6)
									{
										Runner.stderr.println("Could not fetch " + website + next_link + link_ext + ". Retrying.");
										continue;
									}
									
									Runner.stderr.println("Could not fetch " + website + next_link + link_ext + ".");
									
									break;
								}
								
								Chapter_Parser Cparser = new Chapter_Parser(File2);
								link_ext_last = link_ext;
								link_ext = Cparser.NextLink();
								
								if(Cparser.GetPictureLink() == null)
								{
									err_c++;
									
									if(err_c < 6)
									{
										Runner.stderr.println("Could not find picture. Retrying.");
										continue;
									}
									
									Runner.stderr.println("Could not fetch picture.");
									
									break;
								}
								
								int index = Cparser.GetPictureLink().lastIndexOf("/") + 1;
								if(!wget.get(new URL(Cparser.GetPictureLink()),"remote file",new File(folder + "/" + Cparser.GetPictureLink().substring(index))))
								{
									err_c++;
									
									if(err_c < 6)
									{
										Runner.stderr.println("Could not fetch " + Cparser.GetPictureLink() + ". Retrying.");
										continue;
									}
									else
									{
										Runner.stderr.println("Could not fetch " + Cparser.GetPictureLink() + ".");
										break;
									}
								}
								
								Runner.stdout.println(Name + "  " + chapter + "  " + Cparser.GetPictureLink().substring(Cparser.GetPictureLink().lastIndexOf('/') + 1));
								
								if(Cparser.IsLastPage())
									break;
							}
							
							if(err_c < 6)
							{
								Log("New",folder);
								Log(Name,folder);
							}
							else
								Runner.stderr.println(Name + " Chapter " + (Float.parseFloat(chapter) % 1 == 0 ? new Integer(Integer.parseInt(chapter)).toString() : new Float(Float.parseFloat(chapter)).toString()) + " could not be downloaded.");
						}
					}
				}
			}
			catch(IOException e)
			{}
			
			Runner.stdout.println("Manga Fox spider crawl finished.");
			m.busy = false;
			
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
		
		protected MangaFoxUpdater m;
	}
	
	protected boolean busy;
	protected String dest;
	protected String website;
	protected WebGet wget;
}
