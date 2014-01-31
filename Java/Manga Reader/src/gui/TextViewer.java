package gui;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import app.KeyBindings;

public class TextViewer extends JFrame implements KeyListener, ComponentListener
{
	public TextViewer(KeyBindings b, File f)
	{
		super(f == null ? "Text Viewer" : f.getName());
		
		file = f;
		binds = b;
		logtimer = new Timer(true);
		
		text = new JTextPane();
		text.setEditable(false);
		text.setFont(new Font("Times New Roman",Font.PLAIN,12));
		text.setBackground(Color.LIGHT_GRAY);
		text.addKeyListener(this);
		text.setMinimumSize(new Dimension(getWidth() - 35,getHeight()));
		
		scrollbars = new JScrollPane(text);
		scrollbars.getViewport().setBackground(Color.LIGHT_GRAY);
		scrollbars.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
		scrollbars.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollbars.setMinimumSize(new Dimension(getWidth(),getHeight()));
		
		scrollbars.getVerticalScrollBar().addAdjustmentListener(new TextAdjustmentListener(text));
		
		getContentPane().removeAll();
		getContentPane().add(scrollbars);
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		setSize(new Dimension(800,250));
		setBackground(Color.LIGHT_GRAY);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 800) / 2,(Toolkit.getDefaultToolkit().getScreenSize().height) / 2);
		
		logtimer.schedule(new Updater(logtimer,this),0);
		addComponentListener(this);
		setVisible(false);
		return;
	}
	
	protected void UpdateText()
	{
		if(text == null)
			return;
		
		Scanner in;
		ArrayList<String> strs = new ArrayList<String>();
		try{in = new Scanner(file);}catch(Exception e){return;}
		
		while(in.hasNextLine())
		{
			strs.add(in.nextLine() + (in.hasNextLine() ? "\n" : ""));
			
			if(strs.size() > 0x200)
				strs.remove(0);
		}
		
		String t = "";
		
		for(int i = 0;i < strs.size();i++)
			t += strs.get(i);
		
		if(!t.equals(text.getText()))
		{
			text.setText(t);
			repaint();
		}
		
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void componentHidden(ComponentEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */
	public void componentMoved(ComponentEvent e)
	{return;}
	
	/**
	 * Makes sure that the image is displayed in the middle of the window.
	 */
	public void componentResized(ComponentEvent e)
	{
		text.setMinimumSize(new Dimension(getWidth() - 35,getHeight()));
		scrollbars.setMinimumSize(new Dimension(getWidth(),getHeight()));
		
		return;
	}
	
	/**
	 * Does nothing.
	 */
	public void componentShown(ComponentEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */ 
	public void keyPressed(KeyEvent e)
	{
		if("Escape".equals(binds.GetLatestNameOnCode(e.getKeyCode())))
			setVisible(false);
		
		return;
	}
	
	/**
	 * Does nothing.
	 */ 
	public void keyTyped(KeyEvent e)
	{return;}
	
	/**
	 * Does nothing.
	 */ 
	public void keyReleased(KeyEvent e)
	{return;}
	
	protected class Updater extends TimerTask
	{
		public Updater(Timer t, TextViewer updatethis)
		{
			tt = t;
			ut = updatethis;
			
			return;
		}
		
		public void run()
		{
			ut.UpdateText();
			tt.schedule(new Updater(tt,ut),100);
			
			return;
		}
		
		Timer tt;
		TextViewer ut;
	}
	
	protected class TextAdjustmentListener implements AdjustmentListener
	{
		public TextAdjustmentListener(JTextPane t)
		{
			this.t = t;
			return;
		}
		
		public void adjustmentValueChanged(AdjustmentEvent e)
		{
			if(t == null)
				return;
			
			t.select(t.getText().length() - 1,t.getText().length() - 2);
			return;
		}
		
		protected JTextPane t;
	}
	
	protected JScrollPane scrollbars;
	protected JTextPane text;
	protected File file;
	protected KeyBindings binds;
	protected Timer logtimer;
}
