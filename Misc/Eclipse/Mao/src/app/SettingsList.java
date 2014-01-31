package app;

import gameplay.PlayManager;
import image.ImageManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import client.ClientApp;

public class SettingsList extends JList
{
	public SettingsList(ClientApp app)
	{
		super();
		
		if(System.getenv("Debug") == null)
			Gallery = new ImageManager("pics/Border/");
		else if(System.getenv("Debug").equals("true"))
			Gallery = new ImageManager("bin/pics/Border/");
		else
			Gallery = new ImageManager("");
		
		StartingCards = new Spinner(app,7);
		
		Penalties = new CheckBox[12];
		PenaltyValues = new Spinner[12];
		
		Penalties[0] = new CheckBox("Bad Penalty");
		Penalties[1] = new CheckBox("Delay of Gameplay");
		Penalties[2] = new CheckBox("Failiur to Say Mao");
		Penalties[3] = new CheckBox("Failiur to Start Game");
		Penalties[4] = new CheckBox("Misplay");
		Penalties[5] = new CheckBox("Not Declaring Win");
		Penalties[6] = new CheckBox("!Enforcing Own Rule");
		Penalties[7] = new CheckBox("Out of Order");
		Penalties[8] = new CheckBox("Saying PoO in PoO");
		Penalties[9] = new CheckBox("Talking");
		Penalties[10] = new CheckBox("Touching Cards(Beg)");
		Penalties[11] = new CheckBox("Touching Cards(PoO)");
		
		for(int i = 0;i < 12;i++)
			PenaltyValues[i] = new Spinner(app,1);
		
		Gallery.add("1.png");
		Gallery.add("2.png");
		Gallery.add("3.png");
		Gallery.add("4.png");
		Gallery.add("5.png");
		
		setCellRenderer(new ListRenderer());
		setLayoutOrientation(JList.HORIZONTAL_WRAP);
		
		setFixedCellWidth(147);
		setFixedCellHeight(20);
		
		Games = new DefaultListModel();
		{
			{
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel("           Basic Rules"));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				
				AddSeperator();
			}
			
			{
				for(int i = 0;i < app.RequestClientKnownRules().NumOfRules();i++)
					Games.addElement(new CheckBox(app.RequestClientKnownRules().RequestRule(i).RequestName()));
				
				AddSeperator();
			}
			
			{
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel("         Game Values"));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				
				AddSeperator();
			}
			
			{
				Games.addElement(new JLabel("Starting Cards"));
				Games.addElement(StartingCards);
				
				AddSeperator();
			}
			
			{
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel("            Penalties"));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				
				AddSeperator();
			}
			
			{
				for(int i = 0;i * 5 < Penalties.length;i++)
				{
					for(int j = 0;j < 5;j++)
						if(i * 5 + j < Penalties.length)
							Games.addElement(Penalties[i * 5 + j]);
						else
							Games.addElement(new JLabel(""));
					
					for(int j = 0;j < 5;j++)
						if(i * 5 + j < PenaltyValues.length)
							Games.addElement(PenaltyValues[i * 5 + j]);
				}
				
				AddSeperator();
			}
			
			{
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel("         Server Rules"));
				Games.addElement(new JLabel(""));
				Games.addElement(new JLabel(""));
				
				AddSeperator();
			}
		}
		setModel(Games);
		
		if(Games.size() % 5 == 0)
			setVisibleRowCount(Games.size() / 5);
		else
			setVisibleRowCount(Games.size() / 5 + 1);
		
		return;
	}
	
	public CheckBox[] GetPenaltiesEnabled()
	{
		return Penalties;
	}
	
	public Spinner GetStartingCards()
	{
		return StartingCards;
	}
	
	public Spinner[] GetPenaltyValues()
	{
		return PenaltyValues;
	}
	
	protected void AddSeperator()
	{
		while(Games.size() % 5 != 0)
			Games.addElement(new JLabel(""));
		
		for(int i = 1;i < 6;i++)
			Games.addElement(Gallery.GetImageFoil(i + ".png"));
		
		return;
	}
	
	protected CheckBox[] Penalties;
	protected DefaultListModel Games;
	protected ImageManager Gallery;
	protected Spinner StartingCards;
	protected Spinner[] PenaltyValues;
}
