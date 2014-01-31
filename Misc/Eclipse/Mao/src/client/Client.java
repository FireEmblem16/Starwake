package client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import gameplay.PlayManager;

public class Client extends Thread
{
	public static void main(String[] arg)
	{
		new Client().start();
		
		return;
	}
	
	public Client()
	{
		app = new ClientApp();
		game = new PlayManager(app);
		
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setBounds(0,0,800,600);
		app.setResizable(false);
		app.setVisible(true);
		
		return;
	}
	
	public void run()
	{
		
		
		return;
	}
	
	protected ClientApp app;
	protected PlayManager game;
}
