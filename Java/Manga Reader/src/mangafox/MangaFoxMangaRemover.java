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
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerListModel;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class MangaFoxMangaRemover implements ActionListener
{
	public MangaFoxMangaRemover()
	{
		busy = false;
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(!busy)
		{
			new Remover(this);
			busy = true;
		}
		
		return;
	}
	
	public class Remover extends JFrame implements ActionListener, WindowListener
	{
		public Remover(MangaFoxMangaRemover h)
		{
			super("Remove Manga from Manga Fox Spider");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			host = h;
			
			try
			{setIconImage(ImageIO.read(new File("icon.ico")));}
			catch(IOException e)
			{}
			
			getContentPane().removeAll();
			GroupLayout layout = new GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			
			FileReader file = null;
			BufferedReader in = null;
			String[] targets = null;
			
			try
			{
				file = new FileReader(new File("MangaFoxFetch"));
				in = new BufferedReader(file);
				
				int i = Integer.parseInt(in.readLine());
				targets = new String[i];
				
				for(int j = 0;j < i;j++)
				{
					targets[j] = in.readLine();
					in.readLine();
				}
				
				in.close();
				file.close();
				
				if(i == 0)
				{
					targets = new String[1];
					targets[0] = "";
				}
			}
			catch(IOException e)
			{}
			
			Arrays.sort(targets);
			
			for(int i = 0;i < targets.length / 2;i++)
			{
				String temp = targets[i];
				targets[i] = targets[targets.length - 1 - i];
				targets[targets.length - 1 - i] = temp;
			}
			
			SpinnerListModel Manga = new SpinnerListModel(targets);
			
			Spinner = new JSpinner(Manga);
			Spinner.setPreferredSize(new Dimension(225,25));
			Spinner.setToolTipText("Choose a manga to remove.");
			Spinner.setFont(new Font("Times New Roman",Font.PLAIN,12));
			Spinner.setValue(targets[targets.length - 1]);
			
			JButton Continue = new JButton("Remove");
			Continue.setPreferredSize(new Dimension(75,25));
			Continue.setActionCommand("Win_Remove");
			Continue.setToolTipText("Remove a manga from the spider bot.");
			Continue.setFont(new Font("Times New Roman",Font.PLAIN,12));
			Continue.setMargin(new Insets(0,0,0,0));
			Continue.addActionListener(this);
			
			SequentialGroup Horizontal = layout.createSequentialGroup();
			
			Horizontal.addGap(10);
			Horizontal.addComponent(Spinner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Horizontal.addGap(5);
			Horizontal.addComponent(Continue,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			layout.setHorizontalGroup(Horizontal);
			
			SequentialGroup Vertical = layout.createSequentialGroup();
		    ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		    
		    Vertical.addContainerGap();
			
		    v1.addComponent(Spinner,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v1.addComponent(Continue,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v1);
		    
			layout.setVerticalGroup(Vertical);
	        
			setResizable(false);
	        setSize(new Dimension(330,75));
	        setAlwaysOnTop(true);
	        setVisible(true);
			
			return;
		}
		
		public String GetRemove()
		{
			return (String)Spinner.getValue();
		}
		
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				FileReader file = new FileReader(new File("MangaFoxFetch"));
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
				catch(IOException err)
				{}
				
				FileWriter file2 = new FileWriter(new File("MangaFoxFetch"));
				BufferedWriter out = new BufferedWriter(file2);
				
				for(int i = 0;i < File.length;i++)
					if(!File[i].equals(GetRemove()))
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
				catch(IOException err)
				{}
				
				new File("Logs/" + GetRemove()).delete();
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
		
		protected MangaFoxMangaRemover host;
		
		protected JLabel Notice;
		protected JLabel Time;
		protected JSpinner Spinner;
		protected JTextField Name;
		protected JTextField Page;
	}
	
	public boolean busy;
}
