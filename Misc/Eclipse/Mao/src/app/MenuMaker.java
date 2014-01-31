package app;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import client.ClientApp;

public class MenuMaker
{
	public static void CreateGameConfig(ClientApp app)
	{
		MenuBar menu = new MenuBar();
		Menu sub = null;
		MenuItem item = null;
		
		sub = new Menu("Game");
		
		item = new MenuItem("Disconnect");
		item.setActionCommand("Disconnet");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Set Defaults");
		item.setActionCommand("Set D in Game");
		item.addActionListener(app);
		sub.add(item);
		
		sub.addSeparator();
		
		item = new MenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("LAN");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("LJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("LHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Internet");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("IJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("IHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Rules");
		
		item = new MenuItem("View Rule Set");
		item.setActionCommand("View Rules");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("View Known Rules");
		item.setActionCommand("View History");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Ban Rules");
		item.setActionCommand("Ban");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Help");
		
		item = new MenuItem("Help");
		item.setActionCommand("Help");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("About");
		item.setActionCommand("About");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		app.SetMode(5);
		app.setMenuBar(menu);
		return;
	}
	
	public static void CreateIHostConfig(ClientApp app)
	{
		MenuBar menu = new MenuBar();
		Menu sub = null;
		MenuItem item = null;
		
		sub = new Menu("User");
		
		item = new MenuItem("Login");
		item.setActionCommand("Login");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Create Account");
		item.setActionCommand("Create");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Set Defaults");
		item.setActionCommand("Set D");
		item.addActionListener(app);
		sub.add(item);
		
		sub.addSeparator();
		
		item = new MenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("LAN");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("LJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("LHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Internet");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("IJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("IHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Rules");
		
		item = new MenuItem("View Known Rules");
		item.setActionCommand("View History");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Help");
		
		item = new MenuItem("Help");
		item.setActionCommand("Help");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("About");
		item.setActionCommand("About");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		app.SetMode(4);
		app.setMenuBar(menu);
		return;
	}
	
	public static void CreateIJoinConfig(ClientApp app)
	{
		MenuBar menu = new MenuBar();
		Menu sub = null;
		MenuItem item = null;
		
		sub = new Menu("User");
		
		item = new MenuItem("Login");
		item.setActionCommand("Login");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Create Account");
		item.setActionCommand("Create");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Set Defaults");
		item.setActionCommand("Set D");
		item.addActionListener(app);
		sub.add(item);
		
		sub.addSeparator();
		
		item = new MenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("LAN");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("LJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("LHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Internet");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("IJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("IHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Help");
		
		item = new MenuItem("Help");
		item.setActionCommand("Help");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("About");
		item.setActionCommand("About");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		app.SetMode(3);
		app.setMenuBar(menu);
		return;
	}
	
	public static void CreateLHostConfig(ClientApp app)
	{
		MenuBar menu = new MenuBar();
		Menu sub = null;
		MenuItem item = null;
		
		sub = new Menu("User");
		
		item = new MenuItem("Login");
		item.setActionCommand("Login");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Create Account");
		item.setActionCommand("Create");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Set Defaults");
		item.setActionCommand("Set D");
		item.addActionListener(app);
		sub.add(item);
		
		sub.addSeparator();
		
		item = new MenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("LAN");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("LJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("LHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Internet");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("IJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("IHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Rules");
		
		item = new MenuItem("View Known Rules");
		item.setActionCommand("View History");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Help");
		
		item = new MenuItem("Help");
		item.setActionCommand("Help");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("About");
		item.setActionCommand("About");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		app.SetMode(2);
		app.setMenuBar(menu);
		return;
	}
	
	public static void CreateLJoinConfig(ClientApp app)
	{
		MenuBar menu = new MenuBar();
		Menu sub = null;
		MenuItem item = null;
		
		sub = new Menu("User");
		
		item = new MenuItem("Login");
		item.setActionCommand("Login");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Create Account");
		item.setActionCommand("Create");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Set Defaults");
		item.setActionCommand("Set D");
		item.addActionListener(app);
		sub.add(item);
		
		sub.addSeparator();
		
		item = new MenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("LAN");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("LJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("LHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Internet");
		
		item = new MenuItem("Join Game");
		item.setActionCommand("IJoin");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("Host Game");
		item.setActionCommand("IHost");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		sub = new Menu("Help");
		
		item = new MenuItem("Help");
		item.setActionCommand("Help");
		item.addActionListener(app);
		sub.add(item);
		
		item = new MenuItem("About");
		item.setActionCommand("About");
		item.addActionListener(app);
		sub.add(item);
		
		menu.add(sub);
		
		app.SetMode(1);
		app.setMenuBar(menu);
		return;
	}
}
