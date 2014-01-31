package host_process;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main extends Thread implements ActionListener
{
	public static void main(String[] args)
	{
		new Main().run();
		return;
	}
	
	public Main()
	{
		super("Host");
		
		win = new Shell(this);
		wget = new WebGet();
		
		clock = new Timer(this,win);
		clock.start();
		
		website = "http://www.mangafox.com";
		
		FileReader file = null;
		BufferedReader in = null;
		
		try
		{
			if(!new File("Logs").exists())
				new File("Logs").mkdir();
			
			file = new FileReader(new File("Storage"));
			in = new BufferedReader(file);
			
			String temp = in.readLine();
			
			if(temp == null || !new File(temp).exists())
				dest = "Manga";
			else
				dest = temp;
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		action = null;
		
		return;
	}
	
	public void run()
	{
		while(true)
		{
			String str = action;
			action = null;
			
			if(str != null)
			{
				if(str.equals("Change Destination"))
				{
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.setCurrentDirectory(dest == null ? null : (dest.equals("") ? null : new File(dest)));
					int retVal = fc.showOpenDialog(win);
					
					if(retVal == JFileChooser.APPROVE_OPTION)
					{
						dest = fc.getSelectedFile().getAbsolutePath() + "\\";
						
						FileWriter file = null;
						BufferedWriter out = null;
						
						try
						{
							file = new FileWriter(new File("Storage"));
							out = new BufferedWriter(file);
							
							out.write(dest);
							out.newLine();
							
							try
							{
								out.close();
								file.close();
							}
							catch(IOException e)
							{}
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				else if(str.equals("Add"))
					win.Add();
				else if(str.equals("Remove"))
					win.Remove();
				else if(str.equals("Update"))
					Update();
				else if(str.equals("Win_Add"))
				{
					win.Base();
					Add();
				}
				else if(str.equals("Win_Remove"))
				{
					win.Base();
					Remove();
				}
			}
		}
	}
	
	public void Add()
	{
		try
		{
			if(win.GetAdd()[0] == null || win.GetAdd()[1] == null || win.GetAdd()[0].equals("") || win.GetAdd()[1].equals(""))
				throw new IOException();
			
			FileReader file = new FileReader(new File("Fetch"));
			BufferedReader in = new BufferedReader(file);
			
			int c = Integer.parseInt(in.readLine());
			String[] File = new String[(c << 1) + 1];
			File[0] = new Integer(c+1).toString();
			
			for(int i = 1;i < File.length;i++)
				File[i] = in.readLine();
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
			
			boolean exists = false;
			
			for(int i = 0;i < File.length;i++)
				if(File[i].equals(win.GetAdd()[0]))
					exists = true;
			
			if(!exists)
			{
				FileWriter file2 = new FileWriter(new File("Fetch"));
				BufferedWriter out = new BufferedWriter(file2);
				
				for(int i = 0;i < File.length;i++)
				{
					out.write(File[i]);
					out.newLine();
				}
				
				out.write(win.GetAdd()[0]);
				out.newLine();
				out.write(win.GetAdd()[1]);
				
				try
				{
					out.close();
					file2.close();
				}
				catch(IOException e)
				{}
				
				if(!new File(dest + win.GetAdd()[0] + "\\" + win.GetAdd()[0] + " Chapter " + new Integer(1).toString()).exists())
				{
					FileWriter file2a = new FileWriter(new File("Logs\\" + win.GetAdd()[0]));
					BufferedWriter out2a = new BufferedWriter(file2a);
					
					out2a.write(new Integer(0).toString());
					out2a.newLine();
					
					try
					{
						out2a.close();
						file2a.close();
					}
					catch(IOException e)
					{}
					
					if(!new File(dest + win.GetAdd()[0]).exists())
						new File(dest + win.GetAdd()[0]).mkdir();
				}
				else
				{
					for(int i = 1;new File(dest + win.GetAdd()[0] + "\\" + win.GetAdd()[0] + " Chapter " + new Integer(i).toString()).exists();i++)
						if(!new File(dest + win.GetAdd()[0] + "\\" + win.GetAdd()[0] + " Chapter " + new Integer(i+1).toString()).exists())
						{
							FileWriter file2a = new FileWriter(new File("Logs\\" + win.GetAdd()[0]));
							BufferedWriter out2a = new BufferedWriter(file2a);
							
							out2a.write(new Integer(0).toString());
							out2a.newLine();
							
							try
							{
								out2a.close();
								file2a.close();
							}
							catch(IOException e)
							{}
							
							if(!new File(dest + win.GetAdd()[0]).exists())
								new File(dest + win.GetAdd()[0]).mkdir();
						}
					
					for(int i = 1;new File(dest + win.GetAdd()[0] + "\\" + win.GetAdd()[0] + " Chapter " + new Integer(i).toString()).exists();i++)
						Log(win.GetAdd()[0],dest + win.GetAdd()[0] + "\\" + win.GetAdd()[0] + " Chapter " + new Integer(i).toString());
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public void Remove()
	{
		try
		{
			FileReader file = new FileReader(new File("Fetch"));
			BufferedReader in = new BufferedReader(file);
			
			int c = Integer.parseInt(in.readLine());
			String[] File = new String[(c << 1) + 1];
			File[0] = new Integer(c-1).toString();
			
			for(int i = 1;i < File.length;i++)
				File[i] = in.readLine();
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
			
			FileWriter file2 = new FileWriter(new File("Fetch"));
			BufferedWriter out = new BufferedWriter(file2);
			
			for(int i = 0;i < File.length;i++)
				if(!File[i].equals(win.GetRemove()))
				{
					out.write(File[i]);
					out.newLine();
				}
				else
					i++;
			
			try
			{
				out.close();
				file2.close();
			}
			catch(IOException e)
			{}
			
			new File("Logs\\" + win.GetRemove()).delete();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public void Update()
	{
		try
		{
			FileReader file = new FileReader(new File("Fetch"));
			BufferedReader in = new BufferedReader(file);
			
			int c = -1;
			c = Integer.parseInt(in.readLine());
			
			// Loop for each series
			for(int i = 0;i < c;i++)
			{
				String Name = in.readLine();
				String Link = in.readLine();
				
				win.SetNoticeText("Looking into " + Name);
				
				File File = new File("base.html");
				if(!wget.get(new URL(Link),"webpage",File))
				{
					System.err.println("Could not update " + Name + ". The link's name has most likely changed.");
					
					if(!File.delete())
						File.deleteOnExit();
					
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
							System.err.println("The series " + Name + " is no longer available...bastards.");
						
						break;
					}
					
					String volume =  next_link.substring(0,next_link.lastIndexOf('/'));
					String chapter = volume.substring(volume.lastIndexOf('/') + 2);
					volume = volume.substring(0,volume.lastIndexOf('/'));
					volume = volume.substring(volume.lastIndexOf('/') + 2);
					int index2 = volume.lastIndexOf("/") + 2;
					
					String folder = dest + Name + "\\" + Name + " Volume " + volume + "\\" + Name + " Chapter " + (Float.parseFloat(chapter) % 1 == 0 ? new Integer(Integer.parseInt(chapter)).toString() : new Float(Float.parseFloat(chapter)).toString());
					
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
							
							if(!wget.get(new URL(/*website + */next_link + link_ext + "?no_warning=1"),"webpage",File2))
							{
								err_c++;
								
								if(err_c < 6)
								{
									System.err.println("Could not fetch " + website + next_link + link_ext + ". Retrying.");
									
									if(!File2.delete())
										File2.deleteOnExit();
									
									continue;
								}
								
								System.err.println("Could not fetch " + website + next_link + link_ext + ".");
								
								if(!File2.delete())
									File2.deleteOnExit();
								
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
									System.err.println("Could not find picture. Retrying.");
									
									if(!File2.delete())
										File2.deleteOnExit();
									
									continue;
								}
								
								System.err.println("Could not fetch picture.");
								
								if(!File2.delete())
									File2.deleteOnExit();
								
								break;
							}
							
							int index = Cparser.GetPictureLink().lastIndexOf("/") + 1;
							if(!wget.get(new URL(Cparser.GetPictureLink()),"remote file",new File(folder + "\\" + Cparser.GetPictureLink().substring(index))))
							{
								err_c++;
								
								if(err_c < 6)
								{
									System.err.println("Could not fetch " + Cparser.GetPictureLink() + ". Retrying.");
									
									if(!File2.delete())
										File2.deleteOnExit();
									
									continue;
								}
								else
								{
									System.err.println("Could not fetch " + Cparser.GetPictureLink() + ".");
									
									if(!File2.delete())
										File2.deleteOnExit();
									
									break;
								}
							}
							
							win.SetNoticeText(Name + "  " + chapter + "  " + Cparser.GetPictureLink().substring(Cparser.GetPictureLink().lastIndexOf('/') + 1));
							win.repaint();
							
							if(!File2.delete())
								File2.deleteOnExit();
							
							if(Cparser.IsLastPage())
								break;
						}
						
						if(err_c < 6)
						{
							Log("New.txt",Name + " Chapter " + (Float.parseFloat(chapter) % 1 == 0 ? new Integer(Integer.parseInt(chapter)).toString() : new Float(Float.parseFloat(chapter)).toString()));
							Log(Name,folder);
						}
						else
							System.err.println(Name + " Chapter " + (Float.parseFloat(chapter) % 1 == 0 ? new Integer(Integer.parseInt(chapter)).toString() : new Float(Float.parseFloat(chapter)).toString()) + " could not be downloaded.");
					}
				}
				
				if(!File.delete())
					File.deleteOnExit();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public boolean CheckLog(String Name, String text)
	{
		try
		{
			FileReader file = new FileReader(new File("Logs\\" + Name));
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
			FileReader file = new FileReader(new File("Logs\\" + Name));
			Scanner in = new Scanner(file);
			
			ArrayList<String> File = new ArrayList<String>();
			
			while(in.hasNextLine())
				File.add(in.nextLine());
			
			try
			{
				in.close();
				file.close();
			}
			catch(IOException e)
			{}
			
			Object[] strs = File.toArray();
			Arrays.sort(strs);
			
			FileWriter file2 = new FileWriter(new File("Logs\\" + Name));
			BufferedWriter out = new BufferedWriter(file2);
			
			if(out == null)
				return;
			
			for(int i = 0;i < strs.length;i++)
			{
				out.write(strs[i].toString());
				out.newLine();
			}
			
			out.write(text);
			
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
	
	public void actionPerformed(ActionEvent e)
	{
		action = e.getActionCommand();
		
		return;
	}
	
	protected Shell win;
	protected String dest;
	protected String website;
	protected Timer clock;
	protected volatile String action;
	protected WebGet wget;
}
