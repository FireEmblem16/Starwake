package host_process;

import gui.Base;
import gui.Game;
import gui.Game_Start;
import gui.LAN_Lobby;
import gui.Options;
import gui.Post_Game;
import gui.Waiting;
import io.Data_Packer_Unpacker;
import io.IO;
import java.awt.image.BufferedImage;
import java.awt.Cursor;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main
{
	public static void main(String[] args)
	{
		Main application = new Main();
		
		Shutdown_Hook ShutdownHook = new Shutdown_Hook(application,application.GetIO());
		Runtime.getRuntime().addShutdownHook(ShutdownHook);
		
		application.run();
		
		return;
	}
	
	public Main()
	{
		ops = new Game_Options();
		net = new IO("Sender","Reciever",4446,4447,"224.0.0.1",1024);
		ping_game_data = new Game_Status("","","",0,0,true,false,true);
		
		state = State.BASE;
		Lstate = State.START_UP;
		hotseat = false;
		alone = false;
		
		win = new Shell("Kingdom Royale",ops);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NewUser();
		
		local_players = new Player_Data[0];
		
		try
		{
			BufferedImage image = null;
			image = ImageIO.read(new File("icon"));
			win.setIconImage(image);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		MenuBar Menu = new MenuBar();
		
		Menu User = new Menu();
		User.setLabel("User");
		User.addActionListener(win);
		User.setActionCommand("MENU:USER");
		
		MenuItem Create = new MenuItem();
		Create.setLabel("Create User");
		Create.addActionListener(win);
		Create.setActionCommand("MENU:USER:CREATE");
		
		MenuItem Load = new MenuItem();
		Load.setLabel("Load User");
		Load.addActionListener(win);
		Load.setActionCommand("MENU:USER:LOAD");
		
		MenuItem Unload = new MenuItem();
		Unload.setLabel("Unload User");
		Unload.addActionListener(win);
		Unload.setActionCommand("MENU:USER:QUIT");
		
		MenuItem Stats = new MenuItem();
		Stats.setLabel("User Statistics");
		Stats.addActionListener(win);
		Stats.setActionCommand("MENU:USER:STATS");
		
		User.add(Create);
		User.add(Load);
		User.add(Unload);
		User.addSeparator();
		User.add(Stats);
		
		Menu Rules = new Menu();
		Rules.setLabel("Rules");
		Rules.addActionListener(win);
		Rules.setActionCommand("MENU:RULES");
		
		MenuItem Intro = new MenuItem();
		Intro.setLabel("Introduction");
		Intro.addActionListener(win);
		Intro.setActionCommand("MENU:RULES:INTRO");
		
		MenuItem Classes = new MenuItem();
		Classes.setLabel("Classes");
		Classes.addActionListener(win);
		Classes.setActionCommand("MENU:RULES:CLASSES");
		
		MenuItem Winning = new MenuItem();
		Winning.setLabel("Winning");
		Winning.addActionListener(win);
		Winning.setActionCommand("MENU:RULES:WINNING");
		
		MenuItem Time = new MenuItem();
		Time.setLabel("Time Table");
		Time.addActionListener(win);
		Time.setActionCommand("MENU:RULES:TIMETABLE");
		
		MenuItem Post = new MenuItem();
		Post.setLabel("Postscript");
		Post.addActionListener(win);
		Post.setActionCommand("MENU:RULES:POST");
		
		Rules.add(Intro);
		Rules.add(Classes);
		Rules.add(Winning);
		Rules.add(Time);
		Rules.add(Post);
		
		Menu About = new Menu();
		About.setLabel("About");
		About.addActionListener(win);
		About.setActionCommand("MENU:ABOUT");
		
		MenuItem Bets = new MenuItem();
		Bets.setLabel("Betting");
		Bets.addActionListener(win);
		Bets.setActionCommand("MENU:ABOUT:BETS");
		
		MenuItem Help = new MenuItem();
		Help.setLabel("Help");
		Help.addActionListener(win);
		Help.setActionCommand("MENU:ABOUT:HELP");
		
		MenuItem Docs = new MenuItem();
		Docs.setLabel("Credits");
		Docs.addActionListener(win);
		Docs.setActionCommand("MENU:ABOUT:DOCS");
		
		About.add(Bets);
		About.add(Help);
		About.addSeparator();
		About.add(Docs);
		
		Menu.add(User);
		Menu.add(Rules);
		Menu.add(About);
		
		win.setMenuBar(Menu);
		ChangeLayout();
		
		return;
	}
	
	public void run()
	{
		String str = null;
		
		while(true)
		{
			str = win.GetAction();
			
			if(str.contains("MENU"))
			{
				if(str.contains("USER"))
					if(str.contains("CREATE"))
					{
						if(state == State.BASE || state == State.LAN_OPTIONS || state == State.LAN_LOBBY || state == State.HOTSEAT_OPTIONS || state == State.WAITING)
						{
							String User = null;
							
							try
							{
								User = JOptionPane.showInputDialog("Enter your player name.");
								
								if(User != null && !User.equals(""))
								{
									if(LoadUser(User) != null)
										throw new FileNotFoundException();
									
									if(user.name != null && !user.name.equals(""))
										SaveUser();
									
									NewUser();
									user.name = User;
									
									JOptionPane.showMessageDialog(null,"Data will only be logged when playing on this computer alone.","Load Plaer",JOptionPane.INFORMATION_MESSAGE);
								}
							}
							catch(FileNotFoundException e)
							{
								if(JOptionPane.showConfirmDialog(null,"Overwrite the existing player data?","Create Player",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
								{
									NewUser();
									user.name = User;
								}
							}
						}
						else
							JOptionPane.showMessageDialog(null,"You can not change player info during a game.","Notice",JOptionPane.INFORMATION_MESSAGE);;
					}
					else if(str.contains("LOAD"))
					{
						if(state == State.BASE || state == State.LAN_OPTIONS || state == State.LAN_LOBBY || state == State.HOTSEAT_OPTIONS || state == State.WAITING)
						{
							String User = JOptionPane.showInputDialog("Enter your player name.");
							User TUser = LoadUser(User);
							
							if(TUser == null)
								JOptionPane.showMessageDialog(null,"The player \'" + User + "\' was not found.","Load Player",JOptionPane.INFORMATION_MESSAGE);
							else
							{
								if(user.name != null && !user.name.equals(""))
									SaveUser();
								
								user = TUser;
								JOptionPane.showMessageDialog(null,"Data will only be logged when playing on this computer alone.","Load Plaer",JOptionPane.INFORMATION_MESSAGE);
							}
						}
						else
							JOptionPane.showMessageDialog(null,"You can not change player info during a game.","Notice",JOptionPane.INFORMATION_MESSAGE);;
					}
					else if(str.contains("QUIT"))
					{
						if(state == State.BASE || state == State.LAN_OPTIONS || state == State.LAN_LOBBY || state == State.HOTSEAT_OPTIONS || state == State.WAITING)
						{
							SaveUser();
							NewUser();
						}
						else
							JOptionPane.showMessageDialog(null,"You can not change player info during a game.","Notice",JOptionPane.INFORMATION_MESSAGE);;
					}
					else if(str.contains("STATS"))
						DisplayUserStats(user);
					else
						;
				else if(str.contains("RULES"))
					if(str.contains("INTRO"))
					{
						try
						{
							Scanner in = new Scanner(new File("Intro"));
							String disp = "";
							
							while(in.hasNextLine())
								disp += in.nextLine() + "\n";
						
							disp = disp.substring(0,disp.length() - 1);
							
							in.close();
							
							JOptionPane.showMessageDialog(null,disp,"Intro",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
					else if(str.contains("CLASSES"))
					{
						try
						{
							Scanner in = new Scanner(new File("Classes"));
							String disp = "";
							
							while(in.hasNextLine())
								disp += in.nextLine() + "\n";
						
							disp = disp.substring(0,disp.length() - 1);

							in.close();
							
							JOptionPane.showMessageDialog(null,disp,"Classes",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
					else if(str.contains("WINNING"))
					{
						try
						{
							Scanner in = new Scanner(new File("Winning"));
							String disp = "";
							
							while(in.hasNextLine())
								disp += in.nextLine() + "\n";
						
							disp = disp.substring(0,disp.length() - 1);

							in.close();
							
							JOptionPane.showMessageDialog(null,disp,"Winning the Game",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
					else if(str.contains("TIMETABLE"))
					{
						try
						{
							Scanner in = new Scanner(new File("Time Table"));
							String disp = "";
							
							while(in.hasNextLine())
								disp += in.nextLine() + "\n";
						
							disp = disp.substring(0,disp.length() - 1);

							in.close();
							
							JOptionPane.showMessageDialog(null,disp,"Full Length Time Table",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
					else if(str.contains("POST"))
					{
						try
						{
							Scanner in = new Scanner(new File("Postscript"));
							String disp = "";
							
							while(in.hasNextLine())
								disp += in.nextLine() + "\n";
						
							disp = disp.substring(0,disp.length() - 1);

							in.close();
							
							JOptionPane.showMessageDialog(null,disp,"Postscript",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
					else
						;
				else if(str.contains("ABOUT"))
					if(str.contains("BETS"))
					{
						try
						{
							Scanner in = new Scanner(new File("Bets"));
							String disp = "";
							
							while(in.hasNextLine())
								disp += in.nextLine() + "\n";
						
							disp = disp.substring(0,disp.length() - 1);

							in.close();
							
							JOptionPane.showMessageDialog(null,disp,"Betting on the Game",JOptionPane.INFORMATION_MESSAGE);
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
					}
					else if(str.contains("HELP"))
						JOptionPane.showMessageDialog(null,"As if you would get any\nHelp in this type of game.\n\nBut if you have networking issues,\nTwo people tried to do the same thing at the same time.","Help",JOptionPane.INFORMATION_MESSAGE);
					else if(str.contains("DOCS"))
						JOptionPane.showMessageDialog(null,"This program is not be be sold or\ndistributed without permission of Omacron.\n©2010 Omacron-LURYI.\n\nUtsuro no Hako to Zero no Maria\nWritten by Eiji Mikage, please support\nthe release if you can read Japanese\nor the series is released in your country.","Credits",JOptionPane.INFORMATION_MESSAGE);
					else
						;
			}
			else if(str.contains("BASE"))
			{
				if(str.contains("LAN"))
					SetState(State.LAN_OPTIONS);
				else if(str.contains("HOTSEAT"))
					SetState(State.HOTSEAT_OPTIONS);
			}
			else if(str.contains("OPTIONS"))
			{
				if(str.contains("CANCEL"))
					SetState(State.BASE);
				else if(str.contains("JOIN"))
					SetState(State.LAN_LOBBY);
				else if(str.contains("CREATE"))
				{
					try
					{
						int players = Integer.parseInt(Options.Local_Players.getText());
						
						if(players > 6 || players < 1)
							throw new NumberFormatException();
						else
						{
							ops.players_at_local_computer = players;
							local_players = new Player_Data[ops.players_at_local_computer];
							
							for(int i = 0;i < local_players.length;i++)
								local_players[i] = new Player_Data();
						}
						
						try
						{
							if(ops.betting)
							{
								int money = Options.ParseMoney(Options.Bet_Amount.getText());
								
								if(money < 1)
									throw new NumberFormatException();
								else
									ops.bet_amount = money;
							}
							else
								ops.bet_amount = 0;
							
							try
							{
								String val = (String)Options.Game_Speed.getValue();
								
								if(!val.equals("8 Days") && !val.equals("1 Day") && !val.equals("2 Hours"))
									throw new NumberFormatException();
								else
									ops.game_speed = val;
								
								try
								{
									if(Options.Game_Name.getText().length() > 255 || Options.Game_Name.getText().length() < 1)
										throw new NumberFormatException();
									
									ops.name = Options.Game_Name.getText();
									ping_game_data = new Game_Status(ops.name,(state == State.HOTSEAT_OPTIONS) ? "Playing" : "Waiting",ops.game_speed,ops.players_at_local_computer,ops.bet_amount,ops.food,ops.eliminate_extra_time,ops.strict_time);
									
									if(state == State.HOTSEAT_OPTIONS || ops.players_at_local_computer == 6)
									{
										state = State.GAME_START;
										ping_game_data.address = null;
										
										hotseat = true;
									}
									else
									{
										state = State.WAITING;
										ping_game_data.address = null;
										
										hotseat = false;
									}
									
									ChangeLayout();
								}
								catch(NumberFormatException e)
								{
									JOptionPane.showMessageDialog(null,"The game's name can not exceed 255 characters and must be at least 1.","Error",JOptionPane.ERROR_MESSAGE);
								}
							}
							catch(NumberFormatException e)
							{
								JOptionPane.showMessageDialog(null,"If you don't want to use the given times, turn off strict time.","Error",JOptionPane.ERROR_MESSAGE);
							}
						}
						catch(NumberFormatException e)
						{
							JOptionPane.showMessageDialog(null,"If you're going to bet, at least make it a real amount.","Error",JOptionPane.ERROR_MESSAGE);
						}
					}
					catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(null,"The number of players at this computer must be between 1 and 6.","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				else if(str.contains("BETTING"))
				{
					ops.betting = ops.betting ? false : true;
					Options.Bet_Amount.enable(ops.betting);
					
					win.repaint();
				}
				else if(str.contains("EMLIMINATE_EXTRA_TIME"))
				{
					ops.eliminate_extra_time = ops.eliminate_extra_time ? false : true;
				}
				else if(str.contains("FOOD"))
				{
					ops.food = ops.food ? false : true;
				}
				else if(str.contains("STRICT_TIME"))
				{
					ops.strict_time = ops.strict_time ? false : true;
					Options.Game_Speed.enable(ops.strict_time);
					
					win.repaint();
				}
				else
					;
			}
			else if(str.contains("LAN_LOBBY"))
			{
				if(str.contains("CANCEL"))
					SetState(State.BASE);
				else if(str.contains("JOIN"))
				{
					try
					{
						Game_Status GTemp = new Game_Status();
						
						GTemp.address = null;
						GTemp.name = (String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),0);
						GTemp.players_left = Integer.parseInt((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),1));
						GTemp.status = (String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),2);
						GTemp.bet = Options.ParseMoney((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),3));
						GTemp.food = ((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),4)).equals("On") ? true : false;
						GTemp.estimate_remaining_time = (String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),5);
						GTemp.skip_empty_time = ((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),6)).equals("On") ? true : false;
						GTemp.strict_time = ((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),7)).equals("On") ? true : false;
						
						int index = -1;
						
						for(int i = 0;i < LAN_Lobby.game_stats.length;i++)
							if(GTemp.equals(LAN_Lobby.game_stats[i]))
								index = i;
						
						if(index == -1)
							JOptionPane.showMessageDialog(null,"First select a game that exists to join.","Error",JOptionPane.ERROR_MESSAGE);
						else
						{
							GTemp.address = LAN_Lobby.game_stats[index].address;
							ping_game_data = GTemp;
							
							ops.bet_amount = ping_game_data.bet;
							ops.betting = ping_game_data.bet == 0 ? false : true;
							ops.eliminate_extra_time = ping_game_data.skip_empty_time;
							ops.food = ping_game_data.food;
							ops.game_speed = ping_game_data.estimate_remaining_time;
							ops.name = ping_game_data.name;
							ops.players_at_local_computer = new Integer(JOptionPane.showInputDialog("How many people will play at this computer?","1"));
							ops.strict_time = ping_game_data.strict_time;
							
							if(ops.players_at_local_computer + ping_game_data.players_left > 6)
								JOptionPane.showMessageDialog(null,"There are too many people in this game for you to join.","Error",JOptionPane.ERROR_MESSAGE);
							else
							{
								long iD = System.currentTimeMillis();
								net.send(Data_Packer_Unpacker.PackJoinRequest(iD,ops.players_at_local_computer));
								
								int i = 0;
								byte[] BTemp = null;
								
								Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
					    		win.setCursor(hourglassCursor);
								
								while(BTemp == null && i < 20)
								{
									try
									{
										Thread.sleep(10L);
									}
									catch(InterruptedException e)
									{}
									
									BTemp = net.fetch();
									
									if(BTemp != null)
									{
										long[] ret = Data_Packer_Unpacker.UnpackJoinDeny(BTemp);
										
										if(ret != null && ret[0] == iD)
										{
											JOptionPane.showMessageDialog(null,"You were unable to join the game.\nProbably because you have not refreshed the game list recently.","Error",JOptionPane.ERROR_MESSAGE);
											break;
										}
										
										ret = Data_Packer_Unpacker.UnpackJoinConfirm(BTemp);
										
										if(ret == null)
											BTemp = null;
										else if(ret[0] == iD)
										{
											net.flush();
											
											local_players = new Player_Data[ops.players_at_local_computer];
											
											for(int j = 0;j < local_players.length;j++)
												local_players[j] = new Player_Data();
											
											ping_game_data.players_left += ops.players_at_local_computer;
											
											if(ping_game_data.players_left == 6)
												SetState(State.GAME_START);
											else
												SetState(State.WAITING);
											
											break;
										}
										else
											BTemp = null;
									}
									
									i++;
								}
								
								Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
					    		win.setCursor(normalCursor);
					    		
					    		if(i == 20)
					    			JOptionPane.showMessageDialog(null,"The game you were trying to reach did not respond.","Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					catch(IndexOutOfBoundsException e)
					{
						JOptionPane.showMessageDialog(null,"First select a game that exists to join.","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				else if(str.contains("REFRESH"))
				{
					LAN_Lobby.create(win,net,null);
				}
				else if(str.contains("PING_ADDRESS"))
				{
					LAN_Lobby.create(win,net,JOptionPane.showInputDialog("Input the address to obtain data from.","224.0.0."));
				}
				else if(str.contains("PLAYER_DATA"))
				{	
					try
					{
						Game_Status GTemp = new Game_Status();
						
						GTemp.address = null;
						GTemp.name = (String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),0);
						GTemp.players_left = Integer.parseInt((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),1));
						GTemp.status = (String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),2);
						GTemp.bet = gui.Options.ParseMoney((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),3));
						GTemp.food = ((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),4)).equals("On") ? true : false;
						GTemp.estimate_remaining_time = (String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),5);
						GTemp.skip_empty_time = ((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),6)).equals("On") ? true : false;
						GTemp.strict_time = ((String)LAN_Lobby.games.getValueAt(LAN_Lobby.games.getSelectedRow(),7)).equals("On") ? true : false;
						
						int index = -1;
						
						for(int i = 0;i < LAN_Lobby.game_stats.length;i++)
							if(GTemp.equals(LAN_Lobby.game_stats[i]))
								index = i;
						
						if(index == -1)
							JOptionPane.showMessageDialog(null,"First select a game that exists to view the players in.","Error",JOptionPane.ERROR_MESSAGE);
						else
							for(int i = 0;i < LAN_Lobby.players[index].length;i++)
								if(LAN_Lobby.players[index][i] != null)
									DisplayUserStats(LAN_Lobby.players[index][i]);
					}
					catch(IndexOutOfBoundsException e)
					{
						JOptionPane.showMessageDialog(null,"First select a game that exists to view the players in.","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
				else
					;
			}
			else if(str.contains("WAITING"))
				if(str.contains("CANCEL"))
				{
					net.send(Data_Packer_Unpacker.PackLeave(ops.players_at_local_computer));
					SetState(State.LAN_LOBBY);
				}
				else
					;
			else if(str.contains("GAME_START"))
				if(str.contains("CONTINUE"))
					SetState(State.GAME);
				else
					;
			else if(str.contains("GAMEPLAY"))
			{
				if(str.contains("PLAYER"))
				{
					int player = Integer.parseInt(str.substring(str.length() - 1,str.length()));
					
					if(player <= local_players.length)
						Game.switch_view(local_players[player].Class);
				}
				else if(str.contains("VIEW_CLASS"))
				{
					int player = Integer.parseInt(str.substring(str.length() - 1,str.length()));

					if(player <= local_players.length)
						JOptionPane.showMessageDialog(null,"You are the " + local_players[player].Class + ".","Class",JOptionPane.INFORMATION_MESSAGE);
				}
				else if(str.contains("END_TURN"))
				{
					String player = Game.current_view;
					
					if(hotseat)
					{
						TurnChecker.SetTurnOver(player);
						
						if(TurnChecker.round_over())
						{
							TurnChecker.create();
							
							switch(TurnChecker.round)
							{
							case TurnChecker.A_BLOCK:
							case TurnChecker.B_BLOCK:
							case TurnChecker.D_BLOCK:
							case TurnChecker.F_BLOCK:
								Game.switch_view("Waiting");
								break;
							case TurnChecker.C_BLOCK:
							case TurnChecker.E_BLOCK:
								Game.switch_view("Player Select");
								break;
							}
						}
						else
							Game.switch_view("Player Select");
					}
					else
					{
						net.send(Data_Packer_Unpacker.PackEndTurn(player));
						
						if(local_players.length > 1)
						{
							for(int i = 0;i < local_players.length;i++)
								if(!TurnChecker.IsTurnOver(local_players[i].Class) && !Game.current_view.equals("Waiting") && !Game.current_view.equals("Player Select"))
									Game.switch_view("Player Select");
								else
									if(!Game.current_view.equals("Waiting"))
										Game.switch_view("Waiting");
						}
						else
							Game.switch_view("Waiting");
					}
				}
				else if(str.contains("END_ROUND"))
				{
					if(hotseat)
					{
						TurnChecker.create();
						
						switch(TurnChecker.block)
						{
						case TurnChecker.A_BLOCK:
						case TurnChecker.B_BLOCK:
						case TurnChecker.D_BLOCK:
						case TurnChecker.F_BLOCK:
							Game.switch_view("Waiting");
							break;
						case TurnChecker.C_BLOCK:
						case TurnChecker.E_BLOCK:
							Game.switch_view("Player Select");
							break;
						}
					}
					else
					{
						Game.EndRound.enable(false);
						Game.EndRound.setVisible(false);
						
						for(int i = 0;i < local_players.length;i++)
							net.send(Data_Packer_Unpacker.PackEndTurn(local_players[i].Class));
						
						Game.refresh();
					}
				}
				else if(str.contains("SEIPAKU"))
				{
					Player_Data[] player = new Player_Data[1];
					for(int i = 0;i < local_players.length;i++)
						if(local_players[i].Class.equals(Game.current_view))
						{
							player[0] = local_players[i];
							i = local_players.length - 1;
						}
					
					if(!hotseat)
					{
						net.send(Data_Packer_Unpacker.PackLeaveDuringGame(player));
						net.send(Data_Packer_Unpacker.PackDeath(player[0].Class,player[0].Name,Data_Packer_Unpacker.KNIFE));
					}
					else
					{
						ping_game_data.players_left--;
						
						for(int j = 0;j < ping_game_data.Players.length;j++)
							if(ping_game_data.Players[j].equals(player[0].Name))
							{
								ping_game_data.Players[j] = null;
								
								for(int l = j;l < ping_game_data.Players.length - 1;l++)
									 ping_game_data.Players[l] = ping_game_data.Players[l+1];
								
								String[] STemp = new String[ping_game_data.Players.length - 1];
								for(int l = 0;l < STemp.length;l++)
									STemp[l] = ping_game_data.Players[l];
								
								ping_game_data.Players = STemp;
								j = ping_game_data.Players.length - 1;
							}
						
						String Class = (String)player[0].Class;
						String Name = (String)player[0].Name;
						byte method = ((Byte)Data_Packer_Unpacker.KNIFE).byteValue();
						
						String Method = "";
						
						switch(method)
						{
						case Data_Packer_Unpacker.ASSASINATION:
							Method = "strangulated";
							break;
						case Data_Packer_Unpacker.DEATH_BLOW:
							Method = "beheaded";
							break;
						case Data_Packer_Unpacker.KNIFE:
							Method = "stabbed several times by a knife";
							break;
						case Data_Packer_Unpacker.RULES_VIOLATION:
							Method = "turned into a mummy";
							break;
						case Data_Packer_Unpacker.SORCERY:
							Method = "burnt to a crisp";
							break;
						case Data_Packer_Unpacker.STARVATION:
							Method = "turned into a mummy";
							break;
						case Data_Packer_Unpacker.ERROR:
						default:
							Method = "dead in a room filled with unspeakable horrors";
							break;
						}
						
						JOptionPane.showMessageDialog(null,Name + " has been found " + Method + ".","Death!",JOptionPane.INFORMATION_MESSAGE);
						WinChecker.KillClass(Class,this);
						
						if(ops.players_at_local_computer == 0 || WinChecker.CheckGameOver())
							SetState(host_process.State.POST_GAME);
						else if(GetState() == host_process.State.GAME)
						{
							switch(TurnChecker.round)
							{
							case TurnChecker.A_BLOCK:
							case TurnChecker.B_BLOCK:
							case TurnChecker.D_BLOCK:
							case TurnChecker.F_BLOCK:
								Game.refresh();
								break;
							case TurnChecker.C_BLOCK:
							case TurnChecker.E_BLOCK:
								Game.switch_view("Player Select");
								break;
							}
						}
					}
				}
				else if(str.contains("RETURN"))
				{
					Game.switch_view("Player Select");
				}
				else if(str.contains("GIVE_FOOD"))
				{
					String msg = "Who do you want to give food to?\n";

					for(int i = 0;i < ping_game_data.Players.length;i++)
						msg += ping_game_data.Players[i] + "\n";
					
					msg += "Any other input cancels.";
					
					String dest = JOptionPane.showInputDialog(msg);
					
					for(int i = 0;i < ping_game_data.Players.length;i++)
						if(dest.equals(ping_game_data.Players[i]))
						{
							int index = -1;
							for(int j = 0;j < local_players.length;j++)
								if(local_players[j].Class.equals(Game.current_view))
								{
									index = j;
									j = local_players.length - 1;
								}
							
							if(index != -1)
							{
								String in = JOptionPane.showInputDialog("How much food do you want to give?\nYou currently have " + local_players[index].food_supply + " food.");
								
								try
								{
									byte amount = Byte.parseByte(in);
									
									if(amount > local_players[index].food_supply)
										throw new NumberFormatException();
									
									local_players[index].food_supply -= amount;
									
									if(!hotseat)
										net.send(Data_Packer_Unpacker.PackGiveFood(local_players[i].Name,amount));
									else
									{
										String dest2 = local_players[i].Name;
										int amount2 = ((Byte)amount).intValue();
										
										for(int j = 0;j < local_players.length;j++)
											if(local_players[j].Name.equals(dest2))
												local_players[j].food_supply += amount2;
										
										Game.Food.setText("You have " + new Integer(local_players[index].food_supply).toString() + " food.");
										JOptionPane.showMessageDialog(null,dest + " has recieved " + new Integer(amount2).toString() + " food unit" + (amount2 > 1 ? "s." : "."),"Gift!",JOptionPane.INFORMATION_MESSAGE);
									}
								}
								catch(NumberFormatException e)
								{
									JOptionPane.showMessageDialog(null,"You must give a valid amount of food.","Error",JOptionPane.ERROR_MESSAGE);
								}
							}
							else
								JOptionPane.showMessageDialog(null,"The players " + dest + " does not exist.","Error",JOptionPane.ERROR_MESSAGE);
						}
				}
				else if(str.contains("SUBSTITUTION"))
				{
					for(int i = 0;i < local_players.length;i++)
						if(local_players[i].Class.equals("King"))
							if(((gui.King)local_players[i].role).can_swap)
							{
								if(!hotseat)
									net.send(Data_Packer_Unpacker.PackSubstitution());
								else
								{
									for(int j = 0;j < local_players.length;j++)
										if(local_players[j].Class.equals("King"))
										{
											((gui.King)local_players[j].role).can_swap = false;
											((gui.King)local_players[j].role).swap = true;
											
											if(Game.current_view.equals("King"))
												Game.refresh();
										}
								}
								
								i = local_players.length - 1;
							}
					
					Game.refresh();
				}
				else if(str.contains("MURDER_REQUEST"))
				{
					String msg = "Who do you want to have murdered?\n";

					for(int i = 0;i < ping_game_data.Players.length;i++)
						msg += ping_game_data.Players[i] + "\n";
					
					msg += "Any other input cancels.";
					
					String dest = JOptionPane.showInputDialog(msg);
					
					for(int i = 0;i < ping_game_data.Players.length;i++)
						if(dest != null && dest.equals(ping_game_data.Players[i]))
							for(int j = 0;j < local_players.length;j++)
								if(local_players[j].Class.equals(Game.current_view))
								{
									if(Game.current_view.equals("King"))
										((gui.King)local_players[j].role).murder_acted = true;
									else if(Game.current_view.equals("Prince"))
										((gui.Prince)local_players[j].role).murder_acted = true;
									else if(Game.current_view.equals("Double"))
										((gui.Double)local_players[j].role).murder_acted = true;
									
									if(!hotseat)
										net.send(Data_Packer_Unpacker.PackMurderRequest(dest));
									else
									{
										String target = dest;
										
										if(WinChecker.IsDead("Sorcerer"))
										{
											for(int i2 = 0;i2 < local_players.length;i2++)
												if(local_players[i2].Class.equals("Knight"))
													if(((gui.Knight)local_players[i2].role).target == null)
													{
														((gui.Knight)local_players[i2].role).target = new String[1];
														((gui.Knight)local_players[i2].role).target[0] = target;
														
														if(Game.current_view.equals("Knight"))
															Game.refresh();
													}
													else
													{
														String[] STemp = new String[((gui.Knight)local_players[i2].role).target.length + 1];
														
														for(int j2 = 0;j2 < ((gui.Knight)local_players[i2].role).target.length;j2++)
															STemp[j2] = ((gui.Knight)local_players[i2].role).target[j2];
														
														STemp[STemp.length - 1] = target;
														((gui.Knight)local_players[i2].role).target = STemp;
														
														if(Game.current_view.equals("Knight"))
															Game.refresh();
													}
										}
										else
										{
											for(int i2 = 0;i2 < local_players.length;i2++)
												if(local_players[i2].Class.equals("Sorcerer"))
													if(((gui.Sorcerer)local_players[i2].role).target == null)
													{
														((gui.Sorcerer)local_players[i2].role).target = new String[1];
														((gui.Sorcerer)local_players[i2].role).target[0] = target;
														
														if(Game.current_view.equals("Sorcerer"))
															Game.refresh();
													}
													else
													{
														String[] STemp = new String[((gui.Sorcerer)local_players[i2].role).target.length + 1];
														
														for(int j2 = 0;j2 < ((gui.Sorcerer)local_players[i2].role).target.length;j2++)
															STemp[j2] = ((gui.Sorcerer)local_players[i2].role).target[j2];
														
														STemp[STemp.length - 1] = target;
														((gui.Sorcerer)local_players[i2].role).target = STemp;
														
														if(Game.current_view.equals("Sorcerer"))
															Game.refresh();
													}
										}
									}
									
									j = local_players.length - 1;
									i = ping_game_data.Players.length - 1;
								}
					
					Game.refresh();
				}
				else if(str.contains("SORCERY"))
				{
					String msg = "You have been requested to kill ";
					
					for(int i = 0;i < local_players.length;i++)
						if(local_players[i].Class.equals("Sorcerer"))
						{
							msg += ((gui.Sorcerer)local_players[i].role).target[0];
							
							if(((gui.Sorcerer)local_players[i].role).target.length > 1)
								for(int j = 1;j < ((gui.Sorcerer)local_players[i].role).target.length;j++)
									msg += " and " + ((gui.Sorcerer)local_players[i].role).target[j];
							
							msg += ".\nType the name you wish to kill or anything else to cancel.";
							
							String target = JOptionPane.showInputDialog(msg);
							
							for(int j = 0;local_players[i].role instanceof gui.Sorcerer && ((gui.Sorcerer)local_players[i].role).target != null && j < ((gui.Sorcerer)local_players[i].role).target.length;j++)
								if(((gui.Sorcerer)local_players[i].role).target[j].equals(target))
								{
									boolean hotseat_extra_var = true;
									
									if(!hotseat)
										net.send(Data_Packer_Unpacker.PackSorcery(target));
									else
									{
										for(int i2 = 0;i2 < local_players.length;i2++)
											if(local_players[i2].Name.equals(target))
												if(!local_players[i2].Class.equals("Prince"))
												{
													host_process.Player_Data[] players = new host_process.Player_Data[1];
													players[0] = local_players[i2];
													
													ping_game_data.players_left--;
													
													for(int j2 = 0;j2 < ping_game_data.Players.length;j2++)
														if(ping_game_data.Players[j2].equals(players[0].Name))
														{
															ping_game_data.Players[j2] = null;
															
															for(int l = j2;l < ping_game_data.Players.length - 1;l++)
																 ping_game_data.Players[l] = ping_game_data.Players[l+1];
															
															String[] STemp = new String[ping_game_data.Players.length - 1];
															for(int l = 0;l < STemp.length;l++)
																STemp[l] = ping_game_data.Players[l];
															
															ping_game_data.Players = STemp;
															j2 = ping_game_data.Players.length - 1;
														}
													
													String Class = (String)players[0].Class;
													String Name = (String)players[0].Name;
													byte method = ((Byte)Data_Packer_Unpacker.SORCERY).byteValue();
													
													String Method = "";
													
													switch(method)
													{
													case Data_Packer_Unpacker.ASSASINATION:
														Method = "strangulated";
														break;
													case Data_Packer_Unpacker.DEATH_BLOW:
														Method = "beheaded";
														break;
													case Data_Packer_Unpacker.KNIFE:
														Method = "stabbed several times by a knife";
														break;
													case Data_Packer_Unpacker.RULES_VIOLATION:
														Method = "turned into a mummy";
														break;
													case Data_Packer_Unpacker.SORCERY:
														Method = "burnt to a crisp";
														break;
													case Data_Packer_Unpacker.STARVATION:
														Method = "turned into a mummy";
														break;
													case Data_Packer_Unpacker.ERROR:
													default:
														Method = "dead in a room filled with unspeakable horrors";
														break;
													}
													
													for(int l = 0;l < local_players.length;l++)
														if(local_players[l].Name.equals(Name))
															if(l < i)
																i--;
															else if(l == i)
																hotseat_extra_var = false;
													
													JOptionPane.showMessageDialog(null,Name + " has been found " + Method + ".","Death!",JOptionPane.INFORMATION_MESSAGE);
													WinChecker.KillClass(Class,this);
													
													if(ops.players_at_local_computer == 0 || WinChecker.CheckGameOver())
														SetState(host_process.State.POST_GAME);
													else if(GetState() == host_process.State.GAME)
													{
														switch(TurnChecker.round)
														{
														case TurnChecker.A_BLOCK:
														case TurnChecker.B_BLOCK:
														case TurnChecker.D_BLOCK:
														case TurnChecker.F_BLOCK:
															Game.refresh();
															break;
														case TurnChecker.C_BLOCK:
														case TurnChecker.E_BLOCK:
															if(Game.current_view.equals(Class))
																if(local_players.length == 1)
																	Game.switch_view(local_players[0].Class);
																else
																	Game.switch_view("Player Select");
															else
																Game.refresh();
															
															break;
														}
													}
												}
									}
									
									if(hotseat_extra_var)
									{
										((gui.Sorcerer)local_players[i].role).has_murdered = true;
										((gui.Sorcerer)local_players[i].role).target = null;
									
										if(Game.current_view.equals("Sorcerer"))
											Game.refresh();
									}
									else
										if(TurnChecker.round_over())
										{
											TurnChecker.create();
											Game.switch_view("Waiting");
										}
										else
											Game.switch_view("Player Select");
								}
							
							i = local_players.length - 1;
						}
				}
				else if(str.contains("DEATHBLOW"))
				{
					String msg = "You have been requested to kill ";
					
					for(int i = 0;i < local_players.length;i++)
						if(local_players[i].Class.equals("Knight"))
						{
							msg += ((gui.Knight)local_players[i].role).target[0];
							
							if(((gui.Knight)local_players[i].role).target.length > 1)
								for(int j = 1;j < ((gui.Knight)local_players[i].role).target.length;j++)
									msg += " and " + ((gui.Knight)local_players[i].role).target[j];
							
							msg += ".\nType the name you wish to kill or anything else to cancel.";
							
							String target = JOptionPane.showInputDialog(msg);
							
							for(int j = 0;local_players[i].role instanceof gui.Knight && ((gui.Knight)local_players[i].role).target != null && j < ((gui.Knight)local_players[i].role).target.length;j++)
								if(((gui.Knight)local_players[i].role).target[j].equals(target))
								{
									boolean hotseat_extra_var = true;
									
									if(!hotseat)
										net.send(Data_Packer_Unpacker.PackDeathBlow(target));
									else
									{
										for(int i2 = 0;i2 < local_players.length;i2++)
											if(local_players[i2].Name.equals(target))
												if(true)
												{
													host_process.Player_Data[] players = new host_process.Player_Data[1];
													players[0] = local_players[i2];
													
													ping_game_data.players_left--;
													
													for(int j2 = 0;j2 < ping_game_data.Players.length;j2++)
														if(ping_game_data.Players[j2].equals(players[0].Name))
														{
															ping_game_data.Players[j2] = null;
															
															for(int l = j2;l < ping_game_data.Players.length - 1;l++)
																 ping_game_data.Players[l] = ping_game_data.Players[l+1];
															
															String[] STemp = new String[ping_game_data.Players.length - 1];
															for(int l = 0;l < STemp.length;l++)
																STemp[l] = ping_game_data.Players[l];
															
															ping_game_data.Players = STemp;
															j2 = ping_game_data.Players.length - 1;
														}
													
													String Class = (String)players[0].Class;
													String Name = (String)players[0].Name;
													byte method = ((Byte)Data_Packer_Unpacker.DEATH_BLOW).byteValue();
													
													String Method = "";
													
													switch(method)
													{
													case Data_Packer_Unpacker.ASSASINATION:
														Method = "strangulated";
														break;
													case Data_Packer_Unpacker.DEATH_BLOW:
														Method = "beheaded";
														break;
													case Data_Packer_Unpacker.KNIFE:
														Method = "stabbed several times by a knife";
														break;
													case Data_Packer_Unpacker.RULES_VIOLATION:
														Method = "turned into a mummy";
														break;
													case Data_Packer_Unpacker.SORCERY:
														Method = "burnt to a crisp";
														break;
													case Data_Packer_Unpacker.STARVATION:
														Method = "turned into a mummy";
														break;
													case Data_Packer_Unpacker.ERROR:
													default:
														Method = "dead in a room filled with unspeakable horrors";
														break;
													}
													
													for(int l = 0;l < local_players.length;l++)
														if(local_players[l].Name.equals(Name))
															if(l < i)
																i--;
															else if(l == i)
																hotseat_extra_var = false;
													
													JOptionPane.showMessageDialog(null,Name + " has been found " + Method + ".","Death!",JOptionPane.INFORMATION_MESSAGE);
													WinChecker.KillClass(Class,this);
													
													if(ops.players_at_local_computer == 0 || WinChecker.CheckGameOver())
														SetState(host_process.State.POST_GAME);
													else if(GetState() == host_process.State.GAME)
													{
														switch(TurnChecker.round)
														{
														case TurnChecker.A_BLOCK:
														case TurnChecker.B_BLOCK:
														case TurnChecker.D_BLOCK:
														case TurnChecker.F_BLOCK:
															Game.refresh();
															break;
														case TurnChecker.C_BLOCK:
														case TurnChecker.E_BLOCK:
															if(Game.current_view.equals(Class))
																if(local_players.length == 1)
																	Game.switch_view(local_players[0].Class);
																else
																	Game.switch_view("Player Select");
															else
																Game.refresh();
															
															break;
														}
													}
												}
									}
									
									if(hotseat_extra_var)
									{
										((gui.Knight)local_players[i].role).has_murdered = true;
										((gui.Knight)local_players[i].role).target = null;
									
										if(Game.current_view.equals("Knight"))
											Game.refresh();
									}
									else
										if(TurnChecker.round_over())
										{
											TurnChecker.create();
											Game.switch_view("Waiting");
										}
										else
											Game.switch_view("Player Select");
								}
							
							i = local_players.length - 1;
						}
				}
				else if(str.contains("REVOLUTIONIZE"))
				{
					String msg = "Who do you want to revolutionize?\n";

					for(int i = 0;i < ping_game_data.Players.length;i++)
						msg += ping_game_data.Players[i] + "\n";
					
					msg += "Any other input cancels.";
					
					String dest = JOptionPane.showInputDialog(msg);
					
					for(int i = 0;dest != null && i < ping_game_data.Players.length;i++)
						if(dest.equals(ping_game_data.Players[i]))
							for(int j = 0;j < local_players.length;j++)
								if(local_players[j].Class.equals("Revolutionary"))
								{
									((gui.Revolutionary)local_players[j].role).target = dest;
									
									if(!hotseat)
										net.send(Data_Packer_Unpacker.PackRevolutionize(dest));
									else
									{
										for(int i2 = 0;i2 < local_players.length;i2++)
											if(local_players[i2].Name.equals(dest))
												if(true)
												{
													host_process.Player_Data[] players = new host_process.Player_Data[1];
													players[0] = local_players[i2];
													
													ping_game_data.players_left--;
													
													for(int j2 = 0;j2 < ping_game_data.Players.length;j2++)
														if(ping_game_data.Players[j2].equals(players[0].Name))
														{
															ping_game_data.Players[j2] = null;
															
															for(int l = j2;l < ping_game_data.Players.length - 1;l++)
																 ping_game_data.Players[l] = ping_game_data.Players[l+1];
															
															String[] STemp = new String[ping_game_data.Players.length - 1];
															for(int l = 0;l < STemp.length;l++)
																STemp[l] = ping_game_data.Players[l];
															
															ping_game_data.Players = STemp;
															j2 = ping_game_data.Players.length - 1;
														}
													
													String Class = (String)players[0].Class;
													String Name = (String)players[0].Name;
													byte method = ((Byte)Data_Packer_Unpacker.ASSASINATION).byteValue();
													
													String Method = "";
													
													switch(method)
													{
													case Data_Packer_Unpacker.ASSASINATION:
														Method = "strangulated";
														break;
													case Data_Packer_Unpacker.DEATH_BLOW:
														Method = "beheaded";
														break;
													case Data_Packer_Unpacker.KNIFE:
														Method = "stabbed several times by a knife";
														break;
													case Data_Packer_Unpacker.RULES_VIOLATION:
														Method = "turned into a mummy";
														break;
													case Data_Packer_Unpacker.SORCERY:
														Method = "burnt to a crisp";
														break;
													case Data_Packer_Unpacker.STARVATION:
														Method = "turned into a mummy";
														break;
													case Data_Packer_Unpacker.ERROR:
													default:
														Method = "dead in a room filled with unspeakable horrors";
														break;
													}
													
													JOptionPane.showMessageDialog(null,Name + " has been found " + Method + ".","Death!",JOptionPane.INFORMATION_MESSAGE);
													WinChecker.KillClass(Class,this);
													
													if(ops.players_at_local_computer == 0 || WinChecker.CheckGameOver())
														SetState(host_process.State.POST_GAME);
													else if(GetState() == host_process.State.GAME)
													{
														switch(TurnChecker.round)
														{
														case TurnChecker.A_BLOCK:
														case TurnChecker.B_BLOCK:
														case TurnChecker.D_BLOCK:
														case TurnChecker.F_BLOCK:
															Game.refresh();
															break;
														case TurnChecker.C_BLOCK:
														case TurnChecker.E_BLOCK:
															if(Game.current_view.equals(Class))
																if(local_players.length == 1)
																	Game.switch_view(local_players[0].Class);
																else
																	Game.switch_view("Player Select");
															else
																Game.refresh();
															
															break;
														}
													}
												}
									}
									
									j = local_players.length - 1;
									i = ping_game_data.Players.length - 1;
								}
				}
				else if(str.contains("VIEW_SECRET_PARTNERS"))
					meetings.DisplayMeetingTable();
				else
					;
			}
			else if(str.contains("POST_GAME"))
			{
				if(str.contains("CONTINUE"))
				{
					SetState(State.BASE);
					
					if(local_players.length == 1 && alone)
						user.LogGame(local_players[0].PC,WinChecker.DidWin(local_players[0].Class),ops.betting,(int)(100 * ops.bet_amount),(int)(-100 * ops.bet_amount),System.currentTimeMillis() - ops.start_time);
				}
			}
			else
				;
		}
	}
	
	public void ChangeLayout()
	{
		if(Lstate == state)
			return;
		
		switch(Lstate)
		{
		case BASE:
		case LAN_OPTIONS:
		case HOTSEAT_OPTIONS:
		case LAN_LOBBY:
		case POST_GAME:
			break;
		case GAME_START:
			if(local_players.length == 1)
				alone = true;
			
			break;
		case WAITING:
			if(!hotseat)
				Waiting.response.halt();
			
			break;
		case GAME:
			if(!hotseat)
				Game_Start.response.halt();
			
			if(ops.strict_time)
				Game.timer.halt();
			
			break;
		default:
			break;
		}
		
		Lstate = state;
		
		switch(state)
		{
		case BASE:
			Base.create(win);
			break;
		case LAN_OPTIONS:
			Options.create(this,win);
			break;
		case HOTSEAT_OPTIONS:
			Options.create(this,win);
			break;
		case LAN_LOBBY:
			LAN_Lobby.create(win,net,"");
			break;
		case WAITING:
			Waiting.create(this,win,net);
			break;
		case GAME_START:
			Game_Start.create(this,win,net);
			break;
		case GAME:
			Game.create(this,win);
			break;
		case POST_GAME:
			Post_Game.create(this,win);
			break;
		default:
			break;
		}
		
		return;
	}
	
	public void DisplayUserStats(User user)
	{
		String disp = "Player Name: ";
		
		if(user.name == null || user.name.equals(""))
			disp += "[Guest]\n";
		else
			disp += user.name + "\n";
		
		disp += "\nWins: " + user.wins + "          Losses: " + user.losses;
		disp += "\nWin Percentage: " + user.GetWinPercentage() + "%";
		
		disp += "\n\nPC Games: " + user.pc_games + "     PC Wins: " + user.pc_wins;
		disp += "\nPC Win Percentage: " + user.GetPCWinPercentage() + "%";
		
		disp += "\n\nMoney Games: " + user.money_games + "     Money Bet: " + user.GetMoneyBet();
		disp += "\nMoney Won: " + user.GetMoneyWon() + "     Money Lost: " + user.GetMoneyLost();
		disp += "\nNet Gain: " + user.GetNetMoney();
		disp += "\nPercent Money Games: " + user.GetPCWinPercentage() + "%";
		
		disp += "\n\nTime Spent Playing:";
		disp += "\n" + user.GetTimeSpentPlaying();
		
		JOptionPane.showMessageDialog(null,disp,"Player Statistics",JOptionPane.INFORMATION_MESSAGE);
		
		return;
	}
	
	public User LoadUser(String name)
	{
		if(name == null || name.equals(""))
			return null;
		
		User ret = new User();
		File f = new File("Users/" + name);
		
		if(!f.exists())
			return null;
		
		try
		{
			Scanner in = new Scanner(f);
			
			ret.losses = new Integer(in.nextLine());
			ret.money_bet = new Integer(in.nextLine());
			ret.money_games = new Integer(in.nextLine());
			ret.money_lost = new Integer(in.nextLine());
			ret.money_won = new Integer(in.nextLine());
			ret.name = in.nextLine();
			ret.pc_games = new Integer(in.nextLine());
			ret.pc_wins = new Integer(in.nextLine());
			ret.time = new Long(in.nextLine());
			ret.wins = new Integer(in.nextLine());
			
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return ret;
	}
	
	public boolean SaveUser()
	{
		if(user.name == null || user.name.equals(""))
			return false;
		
		File f = new File("Users/" + user.name);
		
		if(!f.exists())
		{
			try
			{
				if(!f.createNewFile())
				{
					f.delete();
					f.createNewFile();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}	
		
		BufferedWriter out = null;
		FileWriter file = null;
		
		try
		{
			file = new FileWriter(f);
			out = new BufferedWriter(file);
			
			out.write(new Integer(user.losses).toString());
			out.newLine();
			out.write(new Integer(user.money_bet).toString());
			out.newLine();
			out.write(new Integer(user.money_games).toString());
			out.newLine();
			out.write(new Integer(user.money_lost).toString());
			out.newLine();
			out.write(new Integer(user.money_won).toString());
			out.newLine();
			out.write(user.name);
			out.newLine();
			out.write(new Integer(user.pc_games).toString());
			out.newLine();
			out.write(new Integer(user.pc_wins).toString());
			out.newLine();
			out.write(new Long(user.time).toString());
			out.newLine();
			out.write(new Integer(user.wins).toString());
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		
		try
		{
			out.close();
			file.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void NewUser()
	{
		user = new User();
		user.losses = 0;
		user.money_games = 0;
		user.money_lost = 0;
		user.money_won = 0;
		user.money_bet = 0;
		user.name = null;
		user.time = 0L;
		user.wins = 0;
		user.pc_games = 0;
		user.pc_wins = 0;
		
		return;
	}
	
	public void SetState(State state)
	{
		this.state = state;
		ChangeLayout();
		
		return;
	}
	
	public State GetState()
	{
		return state;
	}
	
	public boolean IsHotseat()
	{
		return hotseat;
	}
	
	public User GetUser()
	{
		return user;
	}
	
	public IO GetIO()
	{
		return net;
	}
	
	public boolean IsAlone()
	{
		return alone;
	}
	
	public boolean hotseat;
	public volatile Game_Options ops;
	public volatile Game_Status ping_game_data;
	public volatile Player_Data[] local_players;
	public volatile Secret_Meeting_Manager meetings;
	
	protected boolean alone;
	protected IO net;
	protected Shell win;
	protected State Lstate;
	protected State state;
	protected User user;
}