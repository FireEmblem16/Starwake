package gui.eventhandlers;

import gui.Frame;
import gui.LinkOpenerDialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import app.KeyBindings;

public class LinkOpener implements ActionListener
{
	public LinkOpener(String file, Frame displayto, KeyBindings binds)
	{
		host = displayto;
		this.binds = binds;
		loc = file;
		
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		File[] display = null;
		LinkOpenerDialog chooser = new LinkOpenerDialog(loc,host,binds);
		
		while(!chooser.Ready())
			try{Thread.sleep(100);}catch(InterruptedException err){}
		
		File f = chooser.GetChoosenDirectory();
		
		if(f == null)
			return;
		
		display = f.listFiles(new FileFilter()
		{
			public boolean accept(File file)
			{
				if(file.isDirectory())
					return false;
				
				String name = file.getName();
				
				if(!name.contains("."))
					return false;
				
				String ext = name.substring(name.lastIndexOf(".") + 1);
				ext = ext.toLowerCase();
				
				if(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("tiff") || ext.equals("ppm") || ext.equals("bmp") || ext.equals("gif") || ext.equals("png"))
					return true;
				
				return false;
			}
		});
			
		host.DisplayImages(display);
		return;
	}
	
	protected Frame host;
	protected KeyBindings binds;
	protected String loc;
}
