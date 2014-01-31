package client;

import gameplay.PlayManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import app.LayoutMaker;
import app.MenuMaker;
import app.SettingsList;
import app.Spinner;
import rules.RuleManager;

public class ClientApp extends JFrame implements ActionListener
{
	public ClientApp()
	{
		super();
		setTitle("The Game of Mao");
		
		if(System.getenv("Debug") == null)
			setIconImage(new ImageIcon("icons/Omacron-LURYI.png").getImage());
		else if(System.getenv("Debug").equals("true"))
			setIconImage(new ImageIcon("bin/icons/Omacron-LURYI.png").getImage());
		else
			setIconImage(new ImageIcon("").getImage());
		
		MenuMaker.CreateLJoinConfig(this);
		LayoutMaker.CreateLJoinConfig(this);
		MenuMode = 1;
		
		Action = false;
		
		if(System.getenv("Debug") == null)
			AllRules = new RuleManager("data/rules");
		else if(System.getenv("Debug").equals("true"))
			AllRules = new RuleManager("bin/data/rules");
		else
			AllRules = new RuleManager();
		
		ClientRuleSet = new GameRules();
		
		ClientRuleSet.SetNumOfStartingCards(7);
		
		return;
	}
	
	public GameRules RequestClientRules()
	{
		return ClientRuleSet;
	}
	
	public RuleManager RequestClientKnownRules()
	{
		return AllRules;
	}
	
	public void SetMode(int mode)
	{
		MenuMode = mode;
		
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		
		if(cmd.equals("LJoin"))
		{
			if(MenuMode != 5 && MenuMode != 1)
			{
				MenuMaker.CreateLJoinConfig(this);
				LayoutMaker.CreateLJoinConfig(this);
			}
			
			if(MenuMode == 5)
			{
				Action = true;
				actionPerformed(new ActionEvent(e.getSource(),e.getID(),"Disconnect"));
				
				if(Action)
				{
					MenuMaker.CreateLJoinConfig(this);
					LayoutMaker.CreateLJoinConfig(this);
					Action = false;
				}
			}
			
			MenuMode = 1;
		}
		else if(cmd.equals("LHost"))
		{
			if(MenuMode != 5 && MenuMode != 2)
			{
				MenuMaker.CreateLHostConfig(this);
				LayoutMaker.CreateLHostConfig(this);
			}
			
			if(MenuMode == 5)
			{
				Action = true;
				actionPerformed(new ActionEvent(e.getSource(),e.getID(),"Disconnect"));
				
				if(Action)
				{
					MenuMaker.CreateLHostConfig(this);
					LayoutMaker.CreateLHostConfig(this);
					Action = false;
				}
			}
			
			MenuMode = 2;
		}
		else if(cmd.equals("IJoin"))
		{
			if(MenuMode != 5 && MenuMode != 3)
			{
				MenuMaker.CreateIJoinConfig(this);
				LayoutMaker.CreateIJoinConfig(this);
			}
			
			if(MenuMode == 5)
			{
				Action = true;
				actionPerformed(new ActionEvent(e.getSource(),e.getID(),"Disconnect"));
				
				if(Action)
				{
					MenuMaker.CreateIJoinConfig(this);
					LayoutMaker.CreateIJoinConfig(this);
					Action = false;
				}
			}

			MenuMode = 3;
		}
		else if(cmd.equals("IHost"))
		{
			if(MenuMode != 5 && MenuMode != 4)
			{
				MenuMaker.CreateIHostConfig(this);
				LayoutMaker.CreateIHostConfig(this);
			}
			
			if(MenuMode == 5)
			{
				Action = true;
				actionPerformed(new ActionEvent(e.getSource(),e.getID(),"Disconnect"));
				
				if(Action)
				{
					MenuMaker.CreateIHostConfig(this);
					LayoutMaker.CreateIHostConfig(this);
					Action = false;
				}
			}
			
			MenuMode = 4;
		}
		else if(cmd.equals("Disconnect"))
		{
			boolean Continue = false;
			
			// Ask if user is sure they want to disconnect
			
			
			// Add disconnect code
			
			
			if(Continue) // Return Action as true iff user continues to disconnect
				return;
			
			Action = false;
		}
		else if(cmd.equals("Login"))
		{
			
		}
		else if(cmd.equals("Create"))
		{
			
		}
		else if(cmd.equals("Set D"))
		{
			
		}
		else if(cmd.equals("Set D in Game"))
		{
			
		}
		else if(cmd.equals("Exit"))
		{
			System.exit(0);
		}
		else if(cmd.equals("Config Rules"))
		{
			
		}
		else if(cmd.equals("Config Default"))
		{
			
		}
		else if(cmd.equals("Ban Rules"))
		{
			
		}
		else if(cmd.equals("View Rules"))
		{
			
		}
		else if(cmd.equals("Help"))
		{
			Help();
		}
		else if(cmd.equals("About"))
		{
			About();
		}
		else if(cmd.equals("Start"))
		{
			
			
			MenuMode = 5;
		}
		else if(cmd.equals("Cancel"))
		{
			if(MenuMode == 4)
			{
				actionPerformed(new ActionEvent(e.getSource(),e.getID(),"IJoin"));
			}
			else if(MenuMode == 2)
			{
				actionPerformed(new ActionEvent(e.getSource(),e.getID(),"LJoin"));
			}
		}
		
		return;
	}
	
	protected void About()
	{
		String msg = "Created by Omacron-LURYI ©2010\nThis is a free shareware program.\n  Not intended for sale or resale.";
		JOptionPane.showMessageDialog(null,msg,"About",JOptionPane.INFORMATION_MESSAGE);
		
		return;
	}
	
	protected void Help()
	{
		return;
	}
	
	protected boolean Action;
	protected GameRules ClientRuleSet;
	protected int MenuMode;
	protected PlayManager Game;
	protected RuleManager AllRules;
}
