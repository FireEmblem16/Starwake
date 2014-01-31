package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import host_process.Game_Options;
import host_process.Main;
import host_process.Shell;
import host_process.WinChecker;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class Post_Game
{
	public static void create(Main host, Shell win)
	{
		String msg = "You have lost Kingdom Royale.";
		
		Winners = new JLabel();
		Winners.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		if(WinChecker.CheckGameOver())
		{
			msg = "";
			
			for(int i = 0;i < host.ping_game_data.Players.length;i++)
				msg += host.ping_game_data.Players[i] + (i < host.ping_game_data.Players.length ?  " and " : " ");
			
			msg += (host.ping_game_data.Players.length > 1 ? "has" : "have") + " won Kingdom Royale.";
		}
		
		Winners.setText(msg);
		
		Stakes = new JLabel();
		Stakes.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		String bet = "";
		
		if(host.ping_game_data.Players.length == 6)
			bet = "The player who was the PC is due " + Game_Options.ToMoney(host.ops.bet_amount * 7) + ".";
		else
			bet = "Each winner is due " + Game_Options.ToMoney(host.ops.bet_amount * 7 / host.ping_game_data.Players.length) + ".";
		
		Stakes.setText("The stakes were " + host.ops.AmountBet() + " per person.\n" + bet);
		
		Continue = new JButton("Continue");
		Continue.setPreferredSize(new Dimension(75,25));
		Continue.setActionCommand("POST_GAME:CONTINUE");
		Continue.setToolTipText("Return to the game type selection screen.");
		Continue.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Continue.setMargin(new Insets(0,0,0,0));
		Continue.addActionListener(win);
		
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		h1.addComponent(Winners,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		if(host.ops.betting)
			h1.addComponent(Stakes,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Continue,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		
		Vertical.addContainerGap();
		Vertical.addComponent(Winners,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		if(host.ops.betting)
			Vertical.addComponent(Stakes,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Continue,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(350,105 + (host.ops.betting ? 30 : 0)));
		win.setSize(new Dimension(350,105 + (host.ops.betting ? 30 : 0)));
        win.setVisible(true);
		
		return;
	}
	
	public static JButton Continue;
	public static JLabel Stakes;
	public static JLabel Winners;
}
