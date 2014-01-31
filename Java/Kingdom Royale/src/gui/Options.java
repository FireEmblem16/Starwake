package gui;

import host_process.Main;
import host_process.Shell;
import host_process.State;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerListModel;

public class Options
{
	public static void create(Main app, Shell win)
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Cancel = new JButton("Cancel");
		Cancel.setPreferredSize(new Dimension(75,25));
		Cancel.setActionCommand("OPTIONS:CANCEL");
		Cancel.setToolTipText("Return to the game type selection screen.");
		Cancel.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Cancel.setMargin(new Insets(0,0,0,0));
		Cancel.addActionListener(win);
		
		Join = new JButton("Join Game");
		Join.setPreferredSize(new Dimension(75,25));
		Join.setActionCommand("OPTIONS:JOIN");
		Join.setToolTipText("Enter the LAN lobby.");
		Join.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Join.setMargin(new Insets(0,0,0,0));
		Join.addActionListener(win);
		
		if(app.GetState() == State.HOTSEAT_OPTIONS)
			Join.setEnabled(false);
		else
			Join.setEnabled(true);
		
		Create = new JButton("Create Game");
		Create.setPreferredSize(new Dimension(75,25));
		Create.setActionCommand("OPTIONS:CREATE");
		Create.setToolTipText("Create a game with the existing settings.");
		Create.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Create.setMargin(new Insets(0,0,0,0));
		Create.addActionListener(win);
		
		Betting = new JCheckBox("Betting");
		Betting.setPreferredSize(new Dimension(100,25));
		Betting.setActionCommand("OPTIONS:BETTING");
		Betting.setToolTipText("Place bets on this game.");
		Betting.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Betting.setMargin(new Insets(0,0,0,0));
		Betting.setSelected(win.game_ops.betting);
		Betting.addActionListener(win);
		
		Eliminate_Extra_Time = new JCheckBox("Eliminate Extra Time");
		Eliminate_Extra_Time.setPreferredSize(new Dimension(150,25));
		Eliminate_Extra_Time.setActionCommand("OPTIONS:ELIMINATE_EXTRA_TIME");
		Eliminate_Extra_Time.setToolTipText("Have the game remove empty time from play.");
		Eliminate_Extra_Time.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Eliminate_Extra_Time.setMargin(new Insets(0,0,0,0));
		Eliminate_Extra_Time.setSelected(win.game_ops.eliminate_extra_time);
		Eliminate_Extra_Time.addActionListener(win);
		
		Food = new JCheckBox("Food");
		Food.setPreferredSize(new Dimension(100,25));
		Food.setActionCommand("OPTIONS:FOOD");
		Food.setToolTipText("Play this game with food.");
		Food.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Food.setMargin(new Insets(0,0,0,0));
		Food.setSelected(win.game_ops.food);
		Food.addActionListener(win);
		
		Strict_Time = new JCheckBox("Strict Time");
		Strict_Time.setPreferredSize(new Dimension(100,25));
		Strict_Time.setActionCommand("OPTIONS:STRICT_TIME");
		Strict_Time.setToolTipText("Have the game automate the movement of time.");
		Strict_Time.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Strict_Time.setMargin(new Insets(0,0,0,0));
		Strict_Time.setSelected(win.game_ops.strict_time);
		Strict_Time.addActionListener(win);
		
		Bet_Amount = new JTextField();
		Bet_Amount.setPreferredSize(new Dimension(50,25));
		Bet_Amount.setText(win.game_ops.AmountBet());
		Bet_Amount.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Bet_Amount.enable(win.game_ops.betting);
		
		Game_Name = new JTextField();
		Game_Name.setPreferredSize(new Dimension(172,25));
		Game_Name.setText(win.game_ops.name);
		Game_Name.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		Local_Players = new JTextField();
		Local_Players.setPreferredSize(new Dimension(20,25));
		Local_Players.setText(new Integer(win.game_ops.players_at_local_computer).toString());
		Local_Players.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		if(app.GetState() == State.HOTSEAT_OPTIONS)
		{
			win.game_ops.players_at_local_computer = 6;
			Local_Players.setText("6");
			Local_Players.setEnabled(false);
		}
		else
			Local_Players.setEnabled(true);
		
		String[] speeds = new String[3];
		
		speeds[0] = "2 Hours";
		speeds[1] = "1 Day";
		speeds[2] = "8 Days";
		
		Game_Speeds = new SpinnerListModel(speeds);
		Game_Speeds.setValue(win.game_ops.game_speed);
		
		Game_Speed = new JSpinner(Game_Speeds);
		Game_Speed.setToolTipText("Set the \'maximum game\' length and, by consequence, time multiplication.");
		Game_Speed.setEnabled(win.game_ops.strict_time);
		Game_Speed.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Game_Speed.setValue(win.game_ops.game_speed);
		
		String[] text = {"Time Rules","-----------------------------------------------------------","Game Speed","-----------------------------------------------------------","Mechanics Rules","-----------------------------------------------------------","Players at this Computer","-----------------------------------------------------------","Betting","-----------------------------------------------------------","USD Amount","-----------------------------------------------------------","Game Name"};
		Labels = new JLabel[text.length];
		
		for(int i = 0;i < text.length;i++)
		{
			Labels[i] = new JLabel();
			Labels[i].setText(text[i]);
			Labels[i].setFont(new Font("Times New Roman",Font.PLAIN,12));
		}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		SequentialGroup h5 = layout.createSequentialGroup();
		SequentialGroup h6 = layout.createSequentialGroup();
		
		h1.addComponent(Labels[0],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Labels[1],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Strict_Time,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(Labels[2],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(Game_Speed,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h1.addComponent(Eliminate_Extra_Time,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Labels[3],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Labels[4],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Labels[5],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Food,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h3.addComponent(Labels[6],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(Local_Players,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h1.addComponent(Labels[7],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Labels[8],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Labels[9],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Betting,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h4.addComponent(Labels[10],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(Bet_Amount,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		h1.addComponent(Labels[11],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h5.addComponent(Labels[12],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(5);
		h5.addComponent(Game_Name,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h5);
		
		h6.addComponent(Join,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h6.addGap(5);
		h6.addComponent(Create,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h6.addGap(5);
		h6.addComponent(Cancel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h6);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v4 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v5 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(Labels[0],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Labels[1],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Strict_Time,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v1.addComponent(Labels[2],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Game_Speed,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addComponent(Eliminate_Extra_Time,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Labels[3],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Labels[4],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Labels[5],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Food,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v2.addComponent(Labels[6],GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Local_Players,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		Vertical.addComponent(Labels[7],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Labels[8],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Labels[9],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Betting,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v3.addComponent(Labels[10],GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(Bet_Amount,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v3);
		
		Vertical.addComponent(Labels[11],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		v4.addComponent(Labels[12],GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v4.addComponent(Game_Name,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v4);
		Vertical.addGap(5);
		
		v5.addComponent(Join,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v5.addComponent(Create,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v5.addComponent(Cancel,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v5);
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(270,430));
		win.setSize(new Dimension(270,430));
        win.setVisible(true);
		
		return;
	}
	
	/**
	 * Returns -1 if given an invalid number.
	 */
	public static int ParseMoney(String usd) throws NumberFormatException
	{
		int ret = 0;
		
		if(usd.contains("$"))
			usd = usd.substring(usd.indexOf("$") + 1);
		
		if(usd.indexOf(".") == 0)
			usd = "0" + usd;
		
		try
		{
			double USD = Float.parseFloat(usd);
			ret = (int)(USD * 100);
		}
		catch(NumberFormatException e)
		{
			throw new NumberFormatException();
		}
		
		return ret;
	}
	
	public static String UnparseMoney(int usd)
	{
		return "$" + new Integer(usd/100).toString() + "." + new Integer(usd%100).toString() + (usd % 10 == 0 ? "0" : "");
	}
	
	public static JButton Cancel;
	public static JButton Join;
	public static JButton Create;
	public static JCheckBox Betting;
	public static JCheckBox Eliminate_Extra_Time;
	public static JCheckBox Food;
	public static JCheckBox Strict_Time;
	public static JLabel[] Labels;
	public static JSpinner Game_Speed;
	public static JTextField Bet_Amount;
	public static JTextField Game_Name;
	public static JTextField Local_Players;
	public static SpinnerListModel Game_Speeds;
}
