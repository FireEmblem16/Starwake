package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import host_process.Main;
import host_process.Shell;
import host_process.State;
import host_process.TurnChecker;
import host_process.WinChecker;
import io.Data_Packer_Unpacker;
import io.IO;
import io.Responder;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class Game_Start
{
	public static void create(Main host, Shell win, IO net)
	{
		WinChecker.create();
		
		if(!host.hotseat)
		{
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    		win.setCursor(hourglassCursor);
    		
			String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
			boolean done = false;
			boolean goal_done[] = new boolean[host.ops.players_at_local_computer];
			for(int i = 0;i < goal_done.length;i++)
				goal_done[i] = false;
			
			while(!done)
			{
				long[] iD = new long[host.ops.players_at_local_computer];
				for(int i = 0;i < iD.length;i++)
					iD[i] = 0;
				
				for(int i = 0;i < iD.length;i++)
					if(!goal_done[i])
					{
						iD[i] = System.currentTimeMillis();
						String attempt = classes[(int)(Math.random() * classes.length)];
						net.send(Data_Packer_Unpacker.PackClassRace(iD[i],attempt));
						
						if(iD[i] == 0)
							iD[i] = System.currentTimeMillis();
					}
				
				int responses_left = host.ping_game_data.players_left - (6 - classes.length);
				String [] responses = new String[host.ping_game_data.players_left - (6 - classes.length)];
				long[] responses_iD = new long[host.ping_game_data.players_left - (6 - classes.length)];
				
				while(responses_left > 0)
				{
					byte[] BTemp = net.fetch();
					
					if(Data_Packer_Unpacker.UnpackClassRace(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackClassRace(BTemp);
						
						for(int i = 0;i < responses.length;i++)
							if(responses[i] == null || responses[i].equals(""))
							{
								responses[i] = (String)ret[1];
								responses_iD[i] = ((Long)ret[0]).longValue();
								responses_left--;
								i = responses.length - 1;
							}
					}
					else if(Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp) != null)
					{
						String[] ret = Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp);
						host.ping_game_data.players_left -= ret.length;
						responses_left -= ret.length;
						
						for(int i = 0;i < ret.length;i++)
							JOptionPane.showMessageDialog(null,ret.length + " players have left the game.","Death!",JOptionPane.INFORMATION_MESSAGE);
					}
					else if(Data_Packer_Unpacker.UnpackJoinRequest(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackJoinDeny(Data_Packer_Unpacker.UnpackJoinRequest(BTemp)[1]));
					else if(Data_Packer_Unpacker.UnpackPing(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackPingResponse(host.ping_game_data,host.GetUser()));
					else if(Data_Packer_Unpacker.UnpackDeath(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackDeath(BTemp);						
						WinChecker.KillClass((String)ret[0],host);
					}
					else
						;
				}
				
				for(int i = 0;i < responses.length - 1;i++)
					for(int j = i + 1; j < responses.length;j++)
						if(responses[i] != null && responses[j] != null && responses[i].equals(responses[j]))
							if(responses_iD[i] > responses_iD[j])
								responses[j] = null;
							else if(responses_iD[i] < responses_iD[j])
								responses[i] = null;
							else
							{
								responses[i] = null;
								responses[j] = null;
							}
				
				for(int i = 0;i < responses.length;i++)
					for(int j = 0;j < classes.length;j++)
						if(classes[j] != null && classes[j].equals(responses[i]))
						{
							classes[j] = null;
							j = classes.length - 1;
						}
				
				int c = 0;
				
				for(int i = 0;i < classes.length;i++)
					if(classes[i] != null)
						c++;
				
				String[] STemp = new String[c];
				
				for(int i = 0;i < classes.length;i++)
					if(classes[i] != null)
						for(int j = 0;j < STemp.length;j++)
							if(STemp[j] == null || STemp[j] == "")
							{
								STemp[j] = classes[i];
								j = STemp.length - 1;
							}
				
				classes = STemp;
				
				for(int i = 0;i < iD.length;i++)
					if(!goal_done[i])
						for(int j = 0;j < responses_iD.length;j++)
							if(responses_iD[j] == iD[i])
							{
								goal_done[i] = true;
								host.local_players[i].Class = responses[j];
								j = responses_iD.length - 1;
							}
				
				if(classes.length == 0)
					done = true;
				
				if(!done)
				{
					try
					{
						Thread.sleep((long)(Math.random() * 50));
					}
					catch(InterruptedException e)
					{}
				}
			}
			
			for(int i = 0;i < host.ops.players_at_local_computer;i++)
				if(host.local_players[i].Class.equals("King"))
					host.local_players[i].role = new King();
				else if(host.local_players[i].Class.equals("Prince"))
					host.local_players[i].role = new Prince();
				else if(host.local_players[i].Class.equals("Double"))
					host.local_players[i].role = new Double();
				else if(host.local_players[i].Class.equals("Sorcerer"))
					host.local_players[i].role = new Sorcerer();
				else if(host.local_players[i].Class.equals("Knight"))
					host.local_players[i].role = new Knight();
				else if(host.local_players[i].Class.equals("Revolutionary"))
					host.local_players[i].role = new Revolutionary();
			
			TurnChecker.create(host,net);
			
			for(int j = 0;j < classes.length;j++)
			{
				WinChecker.SetDeath(classes[j]);
				
				if(classes[j].equals("King"))
				{
					for(int i = 0;i < host.ops.players_at_local_computer;i++)
						if(host.local_players[i].Class.equals("Prince"))
							if(((Prince)host.local_players[i].role).first_murder_requirement)
								((Prince)host.local_players[i].role).murder = true;
							else
								((Prince)host.local_players[i].role).first_murder_requirement = true;
						else if(host.local_players[i].Class.equals("Double"))
							((Double)host.local_players[i].role).murder = true;
				}
				else if(classes[j].equals("Double"))
				{
					for(int i = 0;i < host.ops.players_at_local_computer;i++)
						if(host.local_players[i].Class.equals("Prince"))
							if(((Prince)host.local_players[i].role).first_murder_requirement)
								((Prince)host.local_players[i].role).murder = true;
							else
								((Prince)host.local_players[i].role).first_murder_requirement = true;
						else if(host.local_players[i].Class.equals("King"))
							((King)host.local_players[i].role).can_swap = false;
				}
				else if(classes[j].equals("Sorcerer"))
				{
					for(int i = 0;i < host.ops.players_at_local_computer;i++)
						if(host.local_players[i].Class.equals("Knight"))
							((Knight)host.local_players[i].role).murder = true;
				}
			}
			
			String[] names = new String[host.ping_game_data.players_left];
			done = false;
			for(int i = 0;i < goal_done.length;i++)
				goal_done[i] = false;
			
			while(!done)
			{
				long[] iD = new long[host.ops.players_at_local_computer];
				for(int i = 0;i < iD.length;i++)
					iD[i] = 0;
				
				for(int i = 0;i < iD.length;i++)
					if(!goal_done[i])
					{
						iD[i] = System.currentTimeMillis();
						String attempt = "";
						
						while(attempt == null || attempt.equals("") || attempt.length() > 128)
						{
							if(iD.length == 1 && host.GetUser().name != null && host.GetUser().name.length() < 129)
								attempt = JOptionPane.showInputDialog("Player " + i + ", enter your name.",host.GetUser().name);
							else
								attempt = JOptionPane.showInputDialog("Player " + i + ", enter your name.");
							
							if(attempt != null && attempt.length() > 128)
								JOptionPane.showMessageDialog(null,"Names cannot exceed 128 characters.","Error",JOptionPane.INFORMATION_MESSAGE);
						}
						
						net.send(Data_Packer_Unpacker.PackClassRace(iD[i],attempt));
						
						if(iD[i] == 0)
							iD[i] = System.currentTimeMillis();
					}
				
				int responses_left = 0;
				
				for(int i = 0;i < names.length;i++)
					if(names[i] == null || names[i].equals(""))
						responses_left++;
				
				String [] responses = new String[responses_left];
				long[] responses_iD = new long[responses.length];
				
				while(responses_left > 0)
				{
					byte[] BTemp = net.fetch();
					
					if(Data_Packer_Unpacker.UnpackNameRace(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackNameRace(BTemp);
						
						for(int i = 0;i < responses.length;i++)
							if(responses[i] == null || responses[i].equals(""))
							{
								responses[i] = (String)ret[1];
								responses_iD[i] = ((Long)ret[0]).longValue();
								responses_left--;
								i = responses.length - 1;
							}
					}
					else if(Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp) != null)
					{
						String[] ret = Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp);
						host.ping_game_data.players_left -= ret.length;
						responses_left -= ret.length;
						
						for(int i = 0;i < ret.length;i++)
							for(int j = 0;j < names.length;j++)
								if(names[j] != null && names[j].equals(ret[i]))
									names[j] = null;
						
						String[] STemp = new String[names.length - ret.length];
						
						for(int i = 0;i < STemp.length;i++)
							for(int j = 0;j < names.length;j++)
								if(names[j] != null && !names[j].equals(""))
								{
									STemp[i] = names[j];
									names[j] = null;
									j = names.length - 1;
								}
						
						names = STemp;
						
						for(int i = 0;i < ret.length;i++)
							JOptionPane.showMessageDialog(null,(ret[i].length() > 0 ? ret[i] : "A player") + " has left the game.","Death!",JOptionPane.INFORMATION_MESSAGE);
					}
					else if(Data_Packer_Unpacker.UnpackJoinRequest(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackJoinDeny(Data_Packer_Unpacker.UnpackJoinRequest(BTemp)[1]));
					else if(Data_Packer_Unpacker.UnpackPing(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackPingResponse(host.ping_game_data,host.GetUser()));
					else if(Data_Packer_Unpacker.UnpackDeath(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackDeath(BTemp);						
						WinChecker.KillClass((String)ret[0],host);
					}
					else
						;
				}
				
				for(int i = 0;i < responses.length - 1;i++)
					for(int j = i + 1; j < responses.length;j++)
						if(responses[i] != null && responses[j] != null && responses[i].equals(responses[j]))
							if(responses_iD[i] > responses_iD[j])
								responses[j] = null;
							else if(responses_iD[i] < responses_iD[j])
								responses[i] = null;
							else
							{
								responses[i] = null;
								responses[j] = null;
							}
				
				for(int i = 0;i < responses.length;i++)
					for(int j = 0;j < names.length;j++)
						if(names[j] != null && responses[i] != null && names[j].equals(responses[i]))
						{
							responses[i] = null;
							j = names.length - 1;
						}
				
				for(int i = 0;i < iD.length;i++)
					if(!goal_done[i])
						for(int j = 0;j < responses_iD.length;j++)
							if(responses_iD[j] == iD[i])
							{
								goal_done[i] = true;
								host.local_players[i].Name = responses[j];
								j = responses_iD.length - 1;
							}
				
				done = true;
				
				for(int i = 0;i < names.length;i++)
					if(names[i] == null || names[i].equals(""))
						done = false;
				
				if(!done)
				{
					try
					{
						Thread.sleep((long)(Math.random() * 50));
					}
					catch(InterruptedException e)
					{}
				}
			}
			
			host.ping_game_data.Players = names;
			done = false;
			
			while(!done)
			{
				long[] iD = new long[host.ops.players_at_local_computer];
				for(int i = 0;i < iD.length;i++)
					iD[i] = 0;
				
				for(int i = 0;i < iD.length;i++)
				{
					iD[i] = System.currentTimeMillis();
					net.send(Data_Packer_Unpacker.PackPCRace(iD[i]));
					
					if(iD[i] == 0)
						iD[i] = System.currentTimeMillis();
				}
				
				int responses_left = host.ping_game_data.players_left;
				long[] responses = new long[host.ping_game_data.players_left];
				
				while(responses_left > 0)
				{
					byte[] BTemp = net.fetch();
					
					if(Data_Packer_Unpacker.UnpackPCRace(BTemp) != null)
					{
						long[] ret = Data_Packer_Unpacker.UnpackPCRace(BTemp);
						
						for(int i = 0;i < responses.length;i++)
							if(responses[i] == 0)
							{
								responses[i] = ret[0];
								responses_left--;
								i = responses.length - 1;
							}
					}
					else if(Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp) != null)
					{
						String[] ret = Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp);
						host.ping_game_data.players_left -= ret.length;
						responses_left -= ret.length;
						
						for(int i = 0;i < ret.length;i++)
						{
							for(int j = 0;j < host.ping_game_data.Players.length;j++)
								if(host.ping_game_data.Players[j].equals(ret[i]))
								{
									host.ping_game_data.Players[j] = null;
									
									for(int l = j;l < host.ping_game_data.Players.length - 1;l++)
										host.ping_game_data.Players[l] = host.ping_game_data.Players[l+1];
									
									String[] STemp = new String[host.ping_game_data.Players.length - 1];
									for(int l = 0;l < STemp.length;l++)
										STemp[l] = host.ping_game_data.Players[l];
									
									host.ping_game_data.Players = STemp;
									j = host.ping_game_data.Players.length - 1;
								}
						
								JOptionPane.showMessageDialog(null,ret[i] + " has left the game.","Death!",JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else if(Data_Packer_Unpacker.UnpackJoinRequest(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackJoinDeny(Data_Packer_Unpacker.UnpackJoinRequest(BTemp)[1]));
					else if(Data_Packer_Unpacker.UnpackPing(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackPingResponse(host.ping_game_data,host.GetUser()));
					else if(Data_Packer_Unpacker.UnpackDeath(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackDeath(BTemp);						
						WinChecker.KillClass((String)ret[0],host);
					}
					else
						;
				}
				
				boolean pair = false;
				long first_iD = 0xFFFFFFFFFFFFFFFFL;
				
				for(int i = 0;i < responses.length;i++)
					if(responses[i] < first_iD)
					{
						first_iD = responses[i];
						pair = false;
					}
					else if(responses[i] == first_iD)
						pair = true;
				
				if(!pair)
				{
					for(int i = 0;i < iD.length;i++)
						if(iD[i] == first_iD)
							host.local_players[i].PC = true;
						else
							host.local_players[i].PC = false;
					
					done = true;
				}
				
				if(!done)
				{
					try
					{
						Thread.sleep((long)(Math.random() * 50));
					}
					catch(InterruptedException e)
					{}
				}
			}
			
			for(int i = 0;i < host.local_players.length;i++)
				host.local_players[i].food_supply = 7;
			
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    		win.setCursor(normalCursor);
			
			response = new Responder("Game Responder",host,win,net);
			Game.response = response;
			response.start();
			
			if(WinChecker.CheckGameOver())
				host.SetState(State.POST_GAME);
		}
		else
		{
			String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
			host.ping_game_data.Players = new String[6];
			int PC = (int)(Math.random() * 6);
			
			for(int i = 0;i < 6;i++)
			{
				String C = null;
				
				while(C == null)
				{
					int index = (int)(Math.random() * 6);
					C = classes[index];
					classes[index] = null;
				}
				
				host.local_players[i].Class = C;
				
				C = null;
				
				while(C == null)
				{
					C = JOptionPane.showInputDialog("Enter Player " + new Integer(i+1).toString() + "'s name.");
					
					if(C == null || C.length() > 128)
					{
						JOptionPane.showMessageDialog(null,"Names can not exceed 128 characters in length and must be at least 1.","Error",JOptionPane.ERROR_MESSAGE);
						C = null;
					}
					
					for(int j = 0;j < 6;j++)
						if(host.local_players[j] != null)
							if(C != null && host.local_players[j].Name != null)
								if(host.local_players[j].Name.equals(C))
								{
									JOptionPane.showMessageDialog(null,"The name " + C + " is already taken.","Error",JOptionPane.ERROR_MESSAGE);
									C = null;
								}
				}
				
				host.ping_game_data.Players[i] = C;
				host.local_players[i].Name = C;
				host.local_players[i].food_supply = 7;
				
				if(i == PC)
					host.local_players[i].PC = true;
				else
					host.local_players[i].PC = false;
			}
			
			for(int i2 = 0;i2 < host.ops.players_at_local_computer;i2++)
				if(host.local_players[i2].Class.equals("King"))
					host.local_players[i2].role = new King();
				else if(host.local_players[i2].Class.equals("Prince"))
					host.local_players[i2].role = new Prince();
				else if(host.local_players[i2].Class.equals("Double"))
					host.local_players[i2].role = new Double();
				else if(host.local_players[i2].Class.equals("Sorcerer"))
					host.local_players[i2].role = new Sorcerer();
				else if(host.local_players[i2].Class.equals("Knight"))
					host.local_players[i2].role = new Knight();
				else if(host.local_players[i2].Class.equals("Revolutionary"))
					host.local_players[i2].role = new Revolutionary();
			
			TurnChecker.create(host,net);
		}
		
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Continue = new JButton("Continue");
		Continue.setPreferredSize(new Dimension(75,25));
		Continue.setActionCommand("GAME_START:CONTINUE");
		Continue.setToolTipText("Play the game.");
		Continue.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Continue.setMargin(new Insets(0,0,0,0));
		Continue.addActionListener(win);
		
		String[] text = {"","",""};
		Class = new JLabel[text.length];
		
		if(host.ops.players_at_local_computer > 1)
			text[0] = "Don't forget to check what class you are.";
		else
		{
			text[0] = "You are the " + host.local_players[0].Class + (host.local_players[0].PC ? " and the PC." : "");
			
			if(host.local_players[0].PC)
			{
				text[1] = "If no one dies you are the only winner.";
				
				if(host.ops.betting)
					text[2] = "You also are betting twice as much as everyone else.";
			}
		}
		
		for(int i = 0;i < text.length;i++)
		{
			Class[i] = new JLabel();
			Class[i].setFont(new Font("Times New Roman",Font.PLAIN,12));
			Class[i].setText(text[i]);
		}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		for(int i = 0;i < Class.length;i++)
			h1.addComponent(Class[i],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Continue,GroupLayout.Alignment.TRAILING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		
		Vertical.addContainerGap();
		
		for(int i = 0;i < Class.length;i++)
			Vertical.addComponent(Class[i],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Vertical.addGap(5);
		Vertical.addComponent(Continue,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		layout.setVerticalGroup(Vertical);
		
		if(host.ops.players_at_local_computer > 1)
		{
			win.setMinimumSize(new Dimension(235,118));
			win.setSize(new Dimension(235,118));
			win.setVisible(true);
		}
		else
		{
			win.setMinimumSize(new Dimension(235,118 + (host.local_players[0].PC ? 10 : 0)));
			win.setSize(new Dimension(235,118 + (host.local_players[0].PC ? 10 : 0)));
			win.setVisible(true);
		}
		
		host.ops.start_time = System.currentTimeMillis();
		host.ping_game_data.status = "Ongoing";
		
		return;
	}
	
	public static IO net;
	public static JLabel[] Class;
	public static JButton Continue;
	public static Responder response;
}
