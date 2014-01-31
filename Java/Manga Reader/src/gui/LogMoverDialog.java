package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class LogMoverDialog extends JFrame implements ActionListener
{
	public LogMoverDialog()
	{
		super("Change Paths in Log Files");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		JLabel Name_Label = new JLabel();
		Name_Label.setText("Replace This:");
		Name_Label.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		Name = new JTextField();
		Name.setPreferredSize(new Dimension(172,25));
		Name.setText("");
		Name.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		JLabel Page_Label = new JLabel();
		Page_Label.setText("With This:");
		Page_Label.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		Page = new JTextField();
		Page.setPreferredSize(new Dimension(172,25));
		Page.setText("");
		Page.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		JButton Continue = new JButton("Update");
		Continue.setPreferredSize(new Dimension(75,25));
		Continue.setActionCommand("Win_Add");
		Continue.setToolTipText("Look through all logs for the first string and replace it with the second.");
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
		h3.addGap(39);
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
		
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		ArrayList<File> paths = GetLogs();
		
		for(int i = 0;i < paths.size();i++)
		{
			ArrayList<String> logged = new ArrayList<String>();
			String newlogged = "";
			
			while(newlogged != null)
			{
				newlogged = GetLog(paths.get(i),0);
				logged.add(newlogged);
			}
			
			for(int j = 0;j < logged.size() - 1;j++) // We have a garunteed null at the last position
				Log(paths.get(i),logged.get(j).replace(Name.getText(),Page.getText()));
		}
		
		setVisible(false);
		return;
	}
	
	public ArrayList<File> GetLogs()
	{
		ArrayList<File> ret = new ArrayList<File>();
		File f = new File("Logs/");
		File[] logs = f.listFiles();
		
		for(int i = 0;i < logs.length;i++)
			ret.add(logs[i]);
		
		return ret;
	}

	public String GetLog(File path, int index)
	{
		if(index < 0)
			return null;
		
		try
		{
			FileReader file = new FileReader(path);
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
			
			if(index > File.size() - 1)
				return null;
			
			String ret = File.get(index);
			File.remove(index);
			
			FileWriter file2 = new FileWriter(path);
			BufferedWriter out = new BufferedWriter(file2);
			
			if(out == null)
				return null;
			
			for(int i = 0;i < File.size();i++)
			{
				out.write(File.get(i));
				out.newLine();
			}
			
			try
			{
				out.close();
				file2.close();
			}
			catch(IOException e)
			{}
			
			return ret;
		}
		catch(IOException e)
		{}
		
		return null;
	}
	
	public void Log(File path, String text)
	{
		try
		{
			FileReader file = new FileReader(path);
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
			
			FileWriter file2 = new FileWriter(path);
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
	
	protected JLabel Notice;
	protected JLabel Time;
	protected JTextField Name;
	protected JTextField Page;
}
