package mangafox;

import gui.Frame;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class MangaFoxDestinationChooser implements ActionListener
{
	public MangaFoxDestinationChooser(Frame saveto)
	{
		host = saveto;
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setCurrentDirectory(host.GetMangaFoxSaveLocation() == null ? null : (host.GetMangaFoxSaveLocation().equals("") ? null : new File(host.GetMangaFoxSaveLocation())));
		int retVal = fc.showOpenDialog(host);
		
		String newloc = fc.getSelectedFile().getAbsolutePath() + "/";
		
		while(newloc.indexOf((int)'\\') != -1)
			newloc = newloc.substring(0,newloc.indexOf((int)'\\')) + "/" + newloc.substring(newloc.indexOf((int)'\\') + 1);
		
		if(retVal == JFileChooser.APPROVE_OPTION)
			host.ChangeMangaFoxSaveLocation(newloc);
		
		return;
	}
	
	protected Frame host;
}
