package mangafox;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class MangaFoxMangaAdder implements ActionListener
{
	public MangaFoxMangaAdder(String destination)
	{
		dest = destination;
		busy = false;
		
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(!busy)
		{
			new Adder(this);
			busy = true;
		}
		
		return;
	}
	
	public class Adder extends JFrame implements ActionListener, WindowListener
	{
		public Adder(MangaFoxMangaAdder destination)
		{
			super("Add Manga to Manga Fox Spider");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			host = destination;
			
			try
			{setIconImage(ImageIO.read(new File("icon.ico")));}
			catch(IOException e)
			{}
			
			getContentPane().removeAll();
			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			
			JLabel Name_Label = new JLabel();
			Name_Label.setText("Manga Name:");
			Name_Label.setFont(new Font("Times New Roman",Font.PLAIN,12));
			
			Name = new JTextField();
			Name.setPreferredSize(new Dimension(172,25));
			Name.setText("");
			Name.setFont(new Font("Times New Roman",Font.PLAIN,12));
			
			JLabel Page_Label = new JLabel();
			Page_Label.setText("Manga Base Page:");
			Page_Label.setFont(new Font("Times New Roman",Font.PLAIN,12));
			
			Page = new JTextField();
			Page.setPreferredSize(new Dimension(172,25));
			Page.setText("");
			Page.setFont(new Font("Times New Roman",Font.PLAIN,12));
			
			JButton Continue = new JButton("Add");
			Continue.setPreferredSize(new Dimension(75,25));
			Continue.setActionCommand("Win_Add");
			Continue.setToolTipText("Add a new manga for the spider bot.");
			Continue.setFont(new Font("Times New Roman",Font.PLAIN,12));
			Continue.setMargin(new Insets(0,0,0,0));
			Continue.addActionListener(this);
			
			SequentialGroup Horizontal = layout.createSequentialGroup();
			ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			SequentialGroup h2 = layout.createSequentialGroup();
			SequentialGroup h3 = layout.createSequentialGroup();
	        
			Horizontal.addGap(10);
			
			h2.addComponent(Name_Label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h2.addGap(25);
			h2.addComponent(Name,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h2);
			
			h3.addComponent(Page_Label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3.addGap(5);
			h3.addComponent(Page,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h3);
			
			h1.addComponent(Continue,GroupLayout.Alignment.TRAILING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Horizontal.addGroup(h1);
			
			layout.setHorizontalGroup(Horizontal);
	        
	        SequentialGroup Vertical = layout.createSequentialGroup();
	        ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	        
	        Vertical.addContainerGap();
	        
	        v1.addComponent(Name_Label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v1.addComponent(Name,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v1);
			Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			
			v2.addComponent(Page,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2.addComponent(Page_Label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v2);
			Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
			
			Vertical.addComponent(Continue,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			layout.setVerticalGroup(Vertical);
	        
			setResizable(false);
	        setSize(new Dimension(300,145));
	        setAlwaysOnTop(true);
	        setVisible(true);
			
			return;
		}
		
		public String[] GetAdd()
		{
			String[] ret = new String[2];
			
			ret[0] = Name.getText();
			ret[1] = Page.getText();
			
			return ret; 
		}
		
		public void Log(String Name, String text)
		{
			try
			{
				FileReader file = new FileReader(new File("Logs/" + Name));
				BufferedReader in = new BufferedReader(file);
				
				int c = Integer.parseInt(in.readLine());
				String[] File = new String[c+1];
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
				
				FileWriter file2 = new FileWriter(new File("Logs/" + Name));
				BufferedWriter out = new BufferedWriter(file2);
				
				if(out == null)
					return;
				
				for(int i = 0;i < File.length;i++)
				{
					out.write(File[i]);
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
			try
			{
				if(GetAdd()[0] == null || GetAdd()[1] == null || GetAdd()[0].equals("") || GetAdd()[1].equals(""))
					throw new IOException();
				
				FileReader file = new FileReader(new File("MangaFoxFetch"));
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
				catch(IOException err)
				{}
				
				boolean exists = false;
				
				for(int i = 0;i < File.length;i++)
					if(File[i].equals(GetAdd()[0]))
						exists = true;
				
				if(!exists)
				{
					FileWriter file2 = new FileWriter(new File("MangaFoxFetch"));
					BufferedWriter out = new BufferedWriter(file2);
					
					for(int i = 0;i < File.length;i++)
					{
						out.write(File[i]);
						out.newLine();
					}
					
					out.write(GetAdd()[0]);
					out.newLine();
					out.write(GetAdd()[1]);
					
					try
					{
						out.close();
						file2.close();
					}
					catch(IOException err)
					{}
					
					if(!new File(host.dest + GetAdd()[0] + "/" + GetAdd()[0] + " Chapter " + new Integer(1).toString()).exists())
					{
						FileWriter file2a = new FileWriter(new File("Logs/" + GetAdd()[0]));
						BufferedWriter out2a = new BufferedWriter(file2a);
						
						out2a.write(new Integer(0).toString());
						out2a.newLine();
						
						try
						{
							out2a.close();
							file2a.close();
						}
						catch(IOException err)
						{}
						
						if(!new File(host.dest + GetAdd()[0]).exists())
							new File(host.dest + GetAdd()[0]).mkdir();
					}
					else
					{
						for(int i = 1;new File(host.dest + GetAdd()[0] + "/" + GetAdd()[0] + " Chapter " + new Integer(i).toString()).exists();i++)
							if(!new File(host.dest + GetAdd()[0] + "/" + GetAdd()[0] + " Chapter " + new Integer(i+1).toString()).exists())
							{
								FileWriter file2a = new FileWriter(new File("Logs/" + GetAdd()[0]));
								BufferedWriter out2a = new BufferedWriter(file2a);
								
								out2a.write(new Integer(0).toString());
								out2a.newLine();
								
								try
								{
									out2a.close();
									file2a.close();
								}
								catch(IOException err)
								{}
								
								if(!new File(host.dest + GetAdd()[0]).exists())
									new File(host.dest + GetAdd()[0]).mkdir();
							}
						
						for(int i = 1;new File(host.dest + GetAdd()[0] + "/" + GetAdd()[0] + " Chapter " + new Integer(i).toString()).exists();i++)
							Log(GetAdd()[0],host.dest + GetAdd()[0] + "/" + GetAdd()[0] + " Chapter " + new Integer(i).toString());
					}
				}
			}
			catch(IOException err)
			{}
			
			host.busy = false;
			dispose();
			return;
		}
		
		/**
		 * Does nothing.
		 */
		public void windowActivated(WindowEvent e)
		{return;}

		/**
		 * Does nothing.
		 */
		public void windowClosed(WindowEvent e)
		{return;}

		/**
		 * Makes sure we have our settings right for next time.
		 */
		public void windowClosing(WindowEvent e)
		{
			host.busy = false;
			return;
		}

		/**
		 * Does nothing.
		 */
		public void windowDeactivated(WindowEvent e)
		{return;}

		/**
		 * Does nothing.
		 */
		public void windowDeiconified(WindowEvent e)
		{return;}

		/**
		 * Does nothing.
		 */
		public void windowIconified(WindowEvent e)
		{return;}

		/**
		 * Does nothing.
		 */
		public void windowOpened(WindowEvent e)
		{return;}
		
		protected MangaFoxMangaAdder host;
		
		protected JLabel Notice;
		protected JLabel Time;
		protected JTextField Name;
		protected JTextField Page;
	}
	
	public boolean busy;
	public String dest;
}
