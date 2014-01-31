package io;

import gui.Game;
import gui.Waiting;
import host_process.Main;
import host_process.Shell;
import host_process.State;
import host_process.TurnChecker;
import host_process.WinChecker;
import javax.swing.JOptionPane;

public class Responder extends Thread
{
	public Responder(String name, Main host, Shell win, IO net)
	{
		super(name);
		setDaemon(true);
		
		this.net = net;
		this.win = win;
		data = host;
		run = true;
		
		return;
	}
	
	public void run()
	{
		switch(data.GetState())
		{
		case BASE:
		case LAN_OPTIONS:
		case HOTSEAT_OPTIONS:
		case LAN_LOBBY:
		case POST_GAME:
			break;
		case WAITING:
			while(run)
			{
				byte[] BTemp = net.fetch();
				
				if(BTemp != null)
					if(Data_Packer_Unpacker.UnpackPing(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackPingResponse(data.ping_game_data,data.GetUser()));
					else if(Data_Packer_Unpacker.UnpackJoinRequest(BTemp) != null)
					{
						long[] data = Data_Packer_Unpacker.UnpackJoinRequest(BTemp);
						
						if(data[0] + this.data.ping_game_data.players_left > 6)
							net.send(Data_Packer_Unpacker.PackJoinDeny(data[1]));
						else
						{
							net.send(Data_Packer_Unpacker.PackJoinConfirm(data[1]));
							this.data.ping_game_data.players_left += data[0];
							
							Waiting.Labels[Waiting.Labels.length - 2].setText("Current Number of Players: " + new Integer(this.data.ping_game_data.players_left).toString() + " / 6");
							win.repaint();
						
							if(this.data.ping_game_data.players_left == 6)
								this.data.SetState(host_process.State.GAME_START);
						}
					}
					else if(Data_Packer_Unpacker.UnpackLeave(BTemp) != null)
					{
						int[] ret = Data_Packer_Unpacker.UnpackLeave(BTemp);
						this.data.ping_game_data.players_left -= ret[0];
						
						Waiting.Labels[Waiting.Labels.length - 2].setText("Current Number of Players: " + new Integer(data.ping_game_data.players_left).toString() + " / 6");
						win.repaint();
					}
					else
						;
			}
		case GAME_START:
		case GAME:
			while(run)
			{
				byte[] BTemp = net.fetch();
				
				if(BTemp != null)
					if(Data_Packer_Unpacker.UnpackPing(BTemp) != null)
						net.send(Data_Packer_Unpacker.PackPingResponse(data.ping_game_data,data.GetUser()));
					else if(Data_Packer_Unpacker.UnpackJoinRequest(BTemp) != null)
					{
						long[] data = Data_Packer_Unpacker.UnpackJoinRequest(BTemp);
						net.send(Data_Packer_Unpacker.PackJoinDeny(data[1]));
					}
					else if(Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp) != null)
					{
						String[] ret = Data_Packer_Unpacker.UnpackLeaveDuringGame(BTemp);
						this.data.ping_game_data.players_left -= ret.length;
						
						for(int i = 0;i < ret.length;i++)
						{
							for(int j = 0;j < this.data.ping_game_data.Players.length;j++)
								if(this.data.ping_game_data.Players[j].equals(ret[i]))
								{
									this.data.ping_game_data.Players[j] = null;
									
									for(int l = j;l < this.data.ping_game_data.Players.length - 1;l++)
										 this.data.ping_game_data.Players[l] = this.data.ping_game_data.Players[l+1];
									
									String[] STemp = new String[this.data.ping_game_data.Players.length - 1];
									for(int l = 0;l < STemp.length;l++)
										STemp[l] = this.data.ping_game_data.Players[l];
									
									this.data.ping_game_data.Players = STemp;
									j = this.data.ping_game_data.Players.length - 1;
								}
						}
					}
					else if(Data_Packer_Unpacker.UnpackEndTurn(BTemp) != null)
					{
						String Class = Data_Packer_Unpacker.UnpackEndTurn(BTemp);
						TurnChecker.SetTurnOver(Class);
						
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
							case TurnChecker.C_BLOCK:
							case TurnChecker.E_BLOCK:
								Game.switch_view("Player Select");
							}
						}
						else
							Game.switch_view("Player Select");
					}
					else if(Data_Packer_Unpacker.UnpackDeath(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackDeath(BTemp);
						String Class = (String)ret[0];
						String Name = (String)ret[1];
						byte method = ((Byte)ret[2]).byteValue();
						
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
						
						if(data.IsAlone())
							data.GetUser().LogGame(data.local_players[0].PC,WinChecker.DidWin(data.local_players[0].Class),data.ops.betting,(int)(100 * data.ops.bet_amount),(int)(-100 * data.ops.bet_amount),System.currentTimeMillis() - data.ops.start_time);
						
						WinChecker.KillClass(Class,data);
						
						if(data.ops.players_at_local_computer == 0)
							data.SetState(host_process.State.POST_GAME);
						else if(data.GetState() == host_process.State.GAME)
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
									if(data.local_players.length == 1)
										Game.switch_view(data.local_players[0].Class);
									else
										Game.switch_view("Player Select");
								else
									Game.refresh();
								
								break;
							}
						}
					}
					else if(Data_Packer_Unpacker.UnpackGiveFood(BTemp) != null)
					{
						Object[] ret = Data_Packer_Unpacker.UnpackGiveFood(BTemp);
						String dest = (String)ret[0];
						int amount = ((Byte)ret[1]).intValue();
						
						for(int i = 0;i < data.local_players.length;i++)
							if(data.local_players[i].Name.equals(dest))
							{
								data.local_players[i].food_supply += amount;
								
								if(data.local_players[i].Class.equals(Game.current_view))
									Game.refresh();
							}
						
						JOptionPane.showMessageDialog(null,dest + " has recieved " + new Integer(amount).toString() + " food unit" + (amount > 1 ? "s." : "."),"Gift!",JOptionPane.INFORMATION_MESSAGE);
					}
					else if(Data_Packer_Unpacker.UnpackSubstitution(BTemp) != null)
					{
						for(int i = 0;i < data.local_players.length;i++)
							if(data.local_players[i].Class.equals("King"))
							{
								((gui.King)data.local_players[i].role).can_swap = false;
								((gui.King)data.local_players[i].role).swap = true;
								
								if(Game.current_view.equals("King"))
									Game.refresh();
							}
							else if(data.local_players[i].Class.equals("Double"))
								((gui.King)data.local_players[i].role).swap = true;
					}
					else if(Data_Packer_Unpacker.UnpackMurderRequest(BTemp) != null)
					{
						String target = Data_Packer_Unpacker.UnpackMurderRequest(BTemp);
						
						if(WinChecker.IsDead("Sorcerer"))
						{
							for(int i = 0;i < data.local_players.length;i++)
								if(data.local_players[i].Class.equals("Knight"))
									if(((gui.Knight)data.local_players[i].role).target == null)
									{
										((gui.Knight)data.local_players[i].role).target = new String[1];
										((gui.Knight)data.local_players[i].role).target[0] = target;
										
										if(Game.current_view.equals("Knight"))
											Game.refresh();
									}
									else
									{
										String[] STemp = new String[((gui.Knight)data.local_players[i].role).target.length + 1];
										
										for(int j = 0;j < ((gui.Knight)data.local_players[i].role).target.length;j++)
											STemp[j] = ((gui.Knight)data.local_players[i].role).target[i];
										
										STemp[STemp.length - 1] = target;
										((gui.Knight)data.local_players[i].role).target = STemp;
										
										if(Game.current_view.equals("Knight"))
											Game.refresh();
									}
						}
						else
						{
							for(int i = 0;i < data.local_players.length;i++)
								if(data.local_players[i].Class.equals("Sorcerer"))
									if(((gui.Sorcerer)data.local_players[i].role).target == null)
									{
										((gui.Sorcerer)data.local_players[i].role).target = new String[1];
										((gui.Sorcerer)data.local_players[i].role).target[0] = target;
										
										if(Game.current_view.equals("Sorcerer"))
											Game.refresh();
									}
									else
									{
										String[] STemp = new String[((gui.Sorcerer)data.local_players[i].role).target.length + 1];
										
										for(int j = 0;j < ((gui.Sorcerer)data.local_players[i].role).target.length;j++)
											STemp[j] = ((gui.Sorcerer)data.local_players[i].role).target[i];
										
										STemp[STemp.length - 1] = target;
										((gui.Sorcerer)data.local_players[i].role).target = STemp;
										
										if(Game.current_view.equals("Sorcerer"))
											Game.refresh();
									}
						}
					}
					else if(Data_Packer_Unpacker.UnpackSorcery(BTemp) != null)
					{
						String target = Data_Packer_Unpacker.UnpackSorcery(BTemp);
						
						for(int i = 0;i < data.local_players.length;i++)
							if(data.local_players[i].Name.equals(target))
								if(!data.local_players[i].Class.equals("Prince"))
								{
									host_process.Player_Data[] players = new host_process.Player_Data[1];
									players[0] = data.local_players[i];
									
									net.send(Data_Packer_Unpacker.PackLeaveDuringGame(players));
									net.send(Data_Packer_Unpacker.PackDeath(players[0].Class,players[0].Name,Data_Packer_Unpacker.SORCERY));
								}
					}
					else if(Data_Packer_Unpacker.UnpackDeathBlow(BTemp) != null)
					{
						String target = Data_Packer_Unpacker.UnpackDeathBlow(BTemp);
						
						for(int i = 0;i < data.local_players.length;i++)
							if(data.local_players[i].Name.equals(target))
							{
								host_process.Player_Data[] players = new host_process.Player_Data[1];
								players[0] = data.local_players[i];
								
								net.send(Data_Packer_Unpacker.PackLeaveDuringGame(players));
								net.send(Data_Packer_Unpacker.PackDeath(players[0].Class,players[0].Name,Data_Packer_Unpacker.DEATH_BLOW));
							}
					}
					else if(Data_Packer_Unpacker.UnpackRevolutionize(BTemp) != null)
					{
						String target = Data_Packer_Unpacker.UnpackRevolutionize(BTemp);
						
						for(int i = 0;i < data.local_players.length;i++)
							if(data.local_players[i].Name.equals(target))
							{
								host_process.Player_Data[] players = new host_process.Player_Data[1];
								players[0] = data.local_players[i];
								
								if(players[0].Class.equals("King") && ((gui.King)players[0].role).swap)
									net.send(Data_Packer_Unpacker.PackRevolutionize2());
								else
								{
									net.send(Data_Packer_Unpacker.PackLeaveDuringGame(players));
									net.send(Data_Packer_Unpacker.PackDeath(players[0].Class,players[0].Name,Data_Packer_Unpacker.ASSASINATION));
								}
							}
					}
					else if(Data_Packer_Unpacker.UnpackRevolutionize2(BTemp) != null)
					{
						for(int i = 0;i < data.local_players.length;i++)
							if(data.local_players[i].Class.equals("Double"))
							{
								host_process.Player_Data[] players = new host_process.Player_Data[1];
								players[0] = data.local_players[i];
								
								net.send(Data_Packer_Unpacker.PackLeaveDuringGame(players));
								net.send(Data_Packer_Unpacker.PackDeath(players[0].Class,players[0].Name,Data_Packer_Unpacker.ASSASINATION));
							}
					}
					else if(Data_Packer_Unpacker.UnpackMeetings(BTemp) != null)
					{
						String[] temp = Data_Packer_Unpacker.UnpackMeetings(BTemp);
						data.meetings.AddMeeting(temp[0],temp[1]);
					}
					else
						;
			}
		default:
			break;
		}
		
		return;
	}
	
	public void halt()
	{
		run = false;
		
		return;
	}
	
	protected IO net;
	protected Main data;
	protected Shell win;
	
	private boolean run;
}
