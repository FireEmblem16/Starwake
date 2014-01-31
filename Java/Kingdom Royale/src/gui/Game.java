package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import host_process.Main;
import host_process.Shell;
import host_process.Time_Manager;
import host_process.TurnChecker;
import io.Responder;

public class Game
{
	public static void create(Main host, Shell win)
	{
		Game.host = host;
		Game.win = win;
		
		End = new JButton("End Turn");
		End.setPreferredSize(new Dimension(75,25));
		End.setActionCommand("GAMEPLAY:END_TURN");
		End.setToolTipText("Designate that you wish to proceed in the game.");
		End.setFont(new Font("Times New Roman",Font.PLAIN,12));
		End.setMargin(new Insets(0,0,0,0));
		End.addActionListener(win);
		End.setVisible(!host.ops.strict_time);
		End.enable(!host.ops.strict_time);
		
		EndRound = new JButton("End Round");
		EndRound.setPreferredSize(new Dimension(75,25));
		EndRound.setActionCommand("GAMEPLAY:END_ROUND");
		EndRound.setToolTipText("Designate that the group wishes to proceed in the game.");
		EndRound.setFont(new Font("Times New Roman",Font.PLAIN,12));
		EndRound.setMargin(new Insets(0,0,0,0));
		EndRound.addActionListener(win);
		EndRound.setVisible(!host.ops.strict_time);
		EndRound.enable(!host.ops.strict_time);
		
		Seipaku = new JButton("Suicide/Knifed");
		Seipaku.setPreferredSize(new Dimension(75,25));
		Seipaku.setActionCommand("GAMEPLAY:SEIPAKU");
		Seipaku.setToolTipText("End your own life/Tell others you were killed by another player's knife.");
		Seipaku.setFont(new Font("Times New Roman",Font.PLAIN,10));
		Seipaku.setMargin(new Insets(0,0,0,0));
		Seipaku.addActionListener(win);
		
		Select_Secret_Partner = new JButton("View Meetings");
		Select_Secret_Partner.setPreferredSize(new Dimension(75,25));
		Select_Secret_Partner.setActionCommand("GAMEPLAY:VIEW_SECRET_PARTNERS");
		Select_Secret_Partner.setToolTipText("View who's meeting with whom.");
		Select_Secret_Partner.setFont(new Font("Times New Roman",Font.PLAIN,10));
		Select_Secret_Partner.setMargin(new Insets(0,0,0,0));
		Select_Secret_Partner.addActionListener(win);
		
		Return = new JButton("Return");
		Return.setPreferredSize(new Dimension(75,25));
		Return.setActionCommand("GAMEPLAY:RETURN");
		Return.setToolTipText("Return to the player selection menu.");
		Return.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Return.setMargin(new Insets(0,0,0,0));
		Return.addActionListener(win);
		Return.setVisible(host.local_players.length > 1);
		Return.enable(host.local_players.length > 1);
		
		GiveFood = new JButton("Give Food");
		GiveFood.setPreferredSize(new Dimension(75,25));
		GiveFood.setActionCommand("GAMEPLAY:GIVE_FOOD");
		GiveFood.setToolTipText("Gives food to a player.");
		GiveFood.setFont(new Font("Times New Roman",Font.PLAIN,12));
		GiveFood.setMargin(new Insets(0,0,0,0));
		GiveFood.addActionListener(win);
		
		Utility = new JButton("Substitution");
		Utility.setPreferredSize(new Dimension(75,25));
		Utility.setActionCommand("GAMEPLAY:SUBSTITUTION");
		Utility.setToolTipText("Switch places with the double.");
		Utility.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Utility.setMargin(new Insets(0,0,0,0));
		Utility.addActionListener(win);
		
		ViewClass = new JButton("View Class");
		ViewClass.setPreferredSize(new Dimension(75,25));
		ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS6");
		ViewClass.setToolTipText("View your class.");
		ViewClass.setFont(new Font("Times New Roman",Font.PLAIN,12));
		ViewClass.setMargin(new Insets(0,0,0,0));
		ViewClass.addActionListener(win);
		
		Food = new JLabel();
		Food.setText("");
		Food.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Food.setVisible(host.ops.food);
		
		timer = new Time_Manager(host,"Timer"); 
		
		switch_view("Waiting");
		
		return;
	}
	
	protected static void switch_wait()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		switch(TurnChecker.round)
		{
		case TurnChecker.A_BLOCK:
		case TurnChecker.B_BLOCK:
		case TurnChecker.D_BLOCK:
		case TurnChecker.F_BLOCK:
			EndRound.enable(!host.ops.strict_time);
			EndRound.setVisible(!host.ops.strict_time);
			break;
		default:
			EndRound.enable(false);
			EndRound.setVisible(false);
			break;
		}
		
		JLabel Notice = new JLabel();
		Notice.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		switch(TurnChecker.block)
		{
		case TurnChecker.A_BLOCK:
			Notice.setText("You are in A-Block");
			break;
		case TurnChecker.B_BLOCK:
			Notice.setText("You are in B-Block");
			break;
		case TurnChecker.C_BLOCK:
			Notice.setText("You are in C-Block");
			break;
		case TurnChecker.D_BLOCK:
			Notice.setText("You are in D-Block");
			break;
		case TurnChecker.E_BLOCK:
			Notice.setText("You are in E-Block");
			break;
		case TurnChecker.F_BLOCK:
			Notice.setText("You are in F-Block");
			break;
		default:
			Notice.setText("Error");
			break;
		}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		h1.addComponent(Notice,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(EndRound,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Horizontal.addGap(10);
        Horizontal.addGroup(h1);
        
        layout.setHorizontalGroup(Horizontal);
        
        SequentialGroup Vertical = layout.createSequentialGroup();
        
        Vertical.addContainerGap();
        Vertical.addComponent(Notice,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        Vertical.addComponent(EndRound,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        
        layout.setVerticalGroup(Vertical);
        
        win.setMinimumSize(new Dimension(220,120));
        win.setSize(new Dimension(220,120));
        win.setVisible(true);
		
		return;
	}
	
	protected static void manyseat_create()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Players = new JButton[host.local_players.length];
		
		for(int i = 0;i < Players.length;i++)
		{
			Players[i] = new JButton(host.local_players[i].Name);
			Players[i].setPreferredSize(new Dimension(75,25));
			Players[i].setActionCommand("GAMEPLAY:PLAYER" + new Integer(i).toString());
			Players[i].setToolTipText("Enter the game interface for " + host.local_players[i].Name + ".");
			Players[i].setFont(new Font("Times New Roman",Font.PLAIN,12));
			Players[i].setMargin(new Insets(0,0,0,0));
			Players[i].addActionListener(win);
			
			Players[i].enable(!host.local_players[i].role.end_turn);
			Players[i].setVisible(!host.local_players[i].role.end_turn);
		}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		for(int i = 0;i < Players.length;i++)
			h1.addComponent(Players[i],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Horizontal.addGap(50);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		
		Vertical.addContainerGap();
		for(int i = 0;i < Players.length;i++)
		{
			Vertical.addComponent(Players[i],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			
			if(i != Players.length - 1)
				Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		}
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(220,80 + 30 * Players.length));
		win.setSize(new Dimension(220,80 + 30 * Players.length));
        win.setVisible(true);
		
		return;
	}
	
	public static void switch_view(String Class)
	{
		if(Class == null)
			return;
		
		current_view = Class;
		
		if(Class.equals("King"))
			switch_king();
		else if(Class.equals("Prince"))
			switch_prince();
		else if(Class.equals("Double"))
			switch_double();
		else if(Class.equals("Sorcerer"))
			switch_sorcerer();
		else if(Class.equals("Knight"))
			switch_knight();
		else if(Class.equals("Revolutionary"))
			switch_revolutionary();
		else if(Class.equals("Waiting"))
			switch_wait();
		else
			manyseat_create();
		
		return;
	}
	
	public static void refresh()
	{
		switch_view(current_view);
		
		return;
	}
	
	protected static void switch_king()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Murder = new JButton("Murder");
		Murder.setPreferredSize(new Dimension(75,25));
		Murder.setActionCommand("GAMEPLAY:MURDER_REQUEST");
		Murder.setToolTipText("Request the Sorcerer (or Knight) to commit murder.");
		Murder.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Murder.setMargin(new Insets(0,0,0,0));
		Murder.addActionListener(win);
		
		Select_Secret_Partner.setVisible(TurnChecker.block == TurnChecker.C_BLOCK);
		Select_Secret_Partner.enable(TurnChecker.block == TurnChecker.C_BLOCK);
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals("King"))
			{
				Murder.enable(!((King)host.local_players[i].role).murder_acted && TurnChecker.block == TurnChecker.C_BLOCK);
				Murder.setVisible(!((King)host.local_players[i].role).murder_acted && TurnChecker.block == TurnChecker.C_BLOCK);
				
				Utility.enable(((King)host.local_players[i].role).can_swap);
				Utility.setVisible(((King)host.local_players[i].role).can_swap);
				
				ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS" + new Integer(i).toString());
				Food.setText("You have " + new Integer(host.local_players[i].food_supply).toString() + " food." + (host.local_players[i].PC ? " Also you are the PC." : ""));
				
				i = host.local_players.length - 1;
			}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		SequentialGroup h5 = layout.createSequentialGroup();
		
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(GiveFood,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(190);
		h2.addComponent(Murder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addGap(265);
		h3.addComponent(Utility,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addGap(265);
		h4.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		h5.addComponent(Seipaku,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(5);
		h5.addComponent(ViewClass,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(30);
		h5.addComponent(End,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(5);
		h5.addComponent(Return,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h5);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(GiveFood,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Murder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addComponent(Utility,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGap(5);
		Vertical.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		v2.addComponent(Seipaku,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(ViewClass,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(End,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Return,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(400,210));
		win.setSize(new Dimension(400,210));
        win.setVisible(true);
		
		return;
	}
	
	protected static void switch_prince()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Murder = new JButton("Murder");
		Murder.setPreferredSize(new Dimension(75,25));
		Murder.setActionCommand("GAMEPLAY:MURDER_REQUEST");
		Murder.setToolTipText("Request the Sorcerer (or Knight) to commit murder.");
		Murder.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Murder.setMargin(new Insets(0,0,0,0));
		Murder.addActionListener(win);

		Select_Secret_Partner.setVisible(TurnChecker.block == TurnChecker.C_BLOCK);
		Select_Secret_Partner.enable(TurnChecker.block == TurnChecker.C_BLOCK);
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals("Prince"))
			{
				Murder.enable(((Prince)host.local_players[i].role).murder && !((Prince)host.local_players[i].role).murder_acted && TurnChecker.block == TurnChecker.C_BLOCK);
				Murder.setVisible(((Prince)host.local_players[i].role).murder && !((Prince)host.local_players[i].role).murder_acted && TurnChecker.block == TurnChecker.C_BLOCK);
				
				ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS" + new Integer(i).toString());
				Food.setText("You have " + new Integer(host.local_players[i].food_supply).toString() + " food.");
				
				i = host.local_players.length - 1;
			}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(GiveFood,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(190);
		h2.addComponent(Murder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addGap(265);
		h3.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(Seipaku,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(ViewClass,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(30);
		h4.addComponent(End,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(Return,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(GiveFood,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Murder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addGap(25);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		v2.addComponent(Seipaku,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(ViewClass,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(End,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Return,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(400,210));
		win.setSize(new Dimension(400,210));
        win.setVisible(true);
		
		return;
	}
	
	protected static void switch_double()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Murder = new JButton("Murder");
		Murder.setPreferredSize(new Dimension(75,25));
		Murder.setActionCommand("GAMEPLAY:MURDER_REQUEST");
		Murder.setToolTipText("Request the Sorcerer (or Knight) to commit murder.");
		Murder.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Murder.setMargin(new Insets(0,0,0,0));
		Murder.addActionListener(win);

		Select_Secret_Partner.setVisible(TurnChecker.block == TurnChecker.C_BLOCK);
		Select_Secret_Partner.enable(TurnChecker.block == TurnChecker.C_BLOCK);
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals("Double"))
			{
				Murder.enable(((Double)host.local_players[i].role).murder && !((Double)host.local_players[i].role).murder_acted && TurnChecker.block == TurnChecker.C_BLOCK);
				Murder.setVisible(((Double)host.local_players[i].role).murder && !((Double)host.local_players[i].role).murder_acted && TurnChecker.block == TurnChecker.C_BLOCK);
				
				ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS" + new Integer(i).toString());
				Food.setText("You have " + new Integer(host.local_players[i].food_supply).toString() + " food.");
				
				i = host.local_players.length - 1;
			}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(GiveFood,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(190);
		h2.addComponent(Murder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);

		h3.addGap(265);
		h3.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(Seipaku,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(ViewClass,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(30);
		h4.addComponent(End,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(Return,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(GiveFood,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Murder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addGap(25);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		v2.addComponent(Seipaku,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(ViewClass,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(End,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Return,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(400,210));
		win.setSize(new Dimension(400,210));
        win.setVisible(true);
		
		return;
	}
	
	protected static void switch_sorcerer()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Murder = new JButton("Sorcery");
		Murder.setPreferredSize(new Dimension(75,25));
		Murder.setActionCommand("GAMEPLAY:SORCERY");
		Murder.setToolTipText("Burn the selected target to death.");
		Murder.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Murder.setMargin(new Insets(0,0,0,0));
		Murder.addActionListener(win);

		Select_Secret_Partner.setVisible(TurnChecker.block == TurnChecker.C_BLOCK);
		Select_Secret_Partner.enable(TurnChecker.block == TurnChecker.C_BLOCK);
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals("Sorcerer"))
			{
				Murder.enable(((Sorcerer)host.local_players[i].role).target != null && TurnChecker.block == TurnChecker.C_BLOCK && !((Sorcerer)host.local_players[i].role).has_murdered);
				Murder.setVisible(((Sorcerer)host.local_players[i].role).target != null && TurnChecker.block == TurnChecker.C_BLOCK && !((Sorcerer)host.local_players[i].role).has_murdered);
				
				ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS" + new Integer(i).toString());
				Food.setText("You have " + new Integer(host.local_players[i].food_supply).toString() + " food.");
				
				i = host.local_players.length - 1;
			}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(GiveFood,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(190);
		h2.addComponent(Murder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);

		h3.addGap(265);
		h3.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(Seipaku,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(ViewClass,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(30);
		h4.addComponent(End,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(Return,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(GiveFood,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Murder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addGap(25);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		v2.addComponent(Seipaku,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(ViewClass,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(End,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Return,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(400,210));
		win.setSize(new Dimension(400,210));
        win.setVisible(true);
		
		return;
	}
	
	protected static void switch_knight()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Murder = new JButton("Deathblow");
		Murder.setPreferredSize(new Dimension(75,25));
		Murder.setActionCommand("GAMEPLAY:DEATHBLOW");
		Murder.setToolTipText("Behead the selected target to death.");
		Murder.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Murder.setMargin(new Insets(0,0,0,0));
		Murder.addActionListener(win);

		Select_Secret_Partner.setVisible(TurnChecker.block == TurnChecker.C_BLOCK);
		Select_Secret_Partner.enable(TurnChecker.block == TurnChecker.C_BLOCK);
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals("Knight"))
			{
				Murder.enable(((Knight)host.local_players[i].role).target != null && TurnChecker.block == TurnChecker.C_BLOCK && !((Knight)host.local_players[i].role).has_murdered);
				Murder.setVisible(((Knight)host.local_players[i].role).target != null && TurnChecker.block == TurnChecker.C_BLOCK && !((Knight)host.local_players[i].role).has_murdered);
				
				ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS" + new Integer(i).toString());
				Food.setText("You have " + new Integer(host.local_players[i].food_supply).toString() + " food.");
				
				i = host.local_players.length - 1;
			}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(GiveFood,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(190);
		h2.addComponent(Murder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);

		h3.addGap(265);
		h3.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(Seipaku,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(ViewClass,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(30);
		h4.addComponent(End,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(Return,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(GiveFood,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Murder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addGap(25);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		v2.addComponent(Seipaku,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(ViewClass,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(End,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Return,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(400,210));
		win.setSize(new Dimension(400,210));
        win.setVisible(true);
		
		return;
	}
	
	protected static void switch_revolutionary()
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Murder = new JButton("Assasinate");
		Murder.setPreferredSize(new Dimension(75,25));
		Murder.setActionCommand("GAMEPLAY:REVOLUTIONIZE");
		Murder.setToolTipText("Strangulate a person.");
		Murder.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Murder.setMargin(new Insets(0,0,0,0));
		Murder.addActionListener(win);

		Select_Secret_Partner.setVisible(TurnChecker.block == TurnChecker.C_BLOCK);
		Select_Secret_Partner.enable(TurnChecker.block == TurnChecker.C_BLOCK);
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals("Revolutionary"))
			{
				Murder.enable(((Revolutionary)host.local_players[i].role).target == null && TurnChecker.block == TurnChecker.E_BLOCK);
				Murder.setVisible(((Revolutionary)host.local_players[i].role).target == null && TurnChecker.block == TurnChecker.E_BLOCK);
				
				ViewClass.setActionCommand("GAMEPLAY:VIEW_CLASS" + new Integer(i).toString());
				Food.setText("You have " + new Integer(host.local_players[i].food_supply).toString() + " food.");
				
				i = host.local_players.length - 1;
			}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(GiveFood,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(190);
		h2.addComponent(Murder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);

		h3.addGap(265);
		h3.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(Seipaku,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(ViewClass,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(30);
		h4.addComponent(End,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(Return,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(GiveFood,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Murder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addGap(25);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		Vertical.addComponent(Select_Secret_Partner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		v2.addComponent(Seipaku,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(ViewClass,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(End,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Return,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(400,210));
		win.setSize(new Dimension(400,210));
        win.setVisible(true);
		
		return;
	}
	
	public static JButton End;
	public static JButton EndRound;
	public static JButton GiveFood;
	public static JButton Murder;
	public static JButton Seipaku;
	public static JButton Select_Secret_Partner;
	public static JButton Return;
	public static JButton Utility;
	public static JButton ViewClass;
	public static JButton[] Players;
	public static JLabel Food;
	public static Main host;
	public static Responder response;
	public static Shell win;
	public static String current_view;
	public static Time_Manager timer;
}
