package test;
import javax.swing.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

//example of using image panel
class ImageFrame
{
	public static void main(String[] args) throws IOException
	{
		
		try
		{
		
			//create frame
			JFrame f = new JFrame();
			
			//ask for image file
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(f);
			
			//create panel with selected file
			ImagePanel panel = new ImagePanel( chooser.getSelectedFile().getPath() );
			
			//add panel to pane
			f.getContentPane().add(panel);
			
			
			//show frame
			f.setBounds(0,0,800,800);
			f.setVisible(true);
		}
		catch(Exception e)
		{
			System.out.println ( "Please verify that you selected a valid image file");	
		}		
	}
}