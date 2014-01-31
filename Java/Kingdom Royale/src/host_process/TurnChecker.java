package host_process;

import javax.swing.JOptionPane;
import gui.Game;
import io.Data_Packer_Unpacker;
import io.IO;

public class TurnChecker
{
	public static void create()
	{
		turns = new boolean[6];
		
		for(int i = 0;i < turns.length;i++)
			turns[i] = false;
		
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		
		for(int i = 0;i < host.local_players.length;i++)
			host.local_players[i].role.end_turn = false;
		
		for(int i = 0;i < 6;i++)
			if(WinChecker.IsDead(classes[i]))
				SetTurnOver(classes[i]);
		
		block++;
		
		if(block == 7)
		{
			block = 1;
			round++;
			
			host.meetings = new Secret_Meeting_Manager();
			
			for(int i = 0;i < host.local_players.length;i++)
				if(host.local_players[i].Class.equals("King"))
				{
					((gui.King)host.local_players[i].role).murder_acted = false;
					
					if(((gui.King)host.local_players[i].role).swap)
						((gui.King)host.local_players[i].role).swap = false;
				}
				else if(host.local_players[i].Class.equals("Double"))
				{
					if(((gui.Double)host.local_players[i].role).swapped || WinChecker.IsDead("King"))
					{
						((gui.Double)host.local_players[i].role).swapped = false;
						((gui.Double)host.local_players[i].role).murder = true;
					}
				}
				else if(host.local_players[i].Class.equals("Prince"))
					((gui.Prince)host.local_players[i].role).murder_acted = false;
				else if(host.local_players[i].Class.equals("Revolutionary"))
					((gui.Revolutionary)host.local_players[i].role).target = null;
		}
		
		if(block == 3)
		{
			host.meetings = new Secret_Meeting_Manager();
			
			for(int j = 0;j < host.local_players.length;j++)
			{
				String target = null;
				String msg = "Who do you want to have a secret meeting with?\n";
				
				for(int i = 0;i < host.ping_game_data.Players.length;i++)
					if(!host.ping_game_data.Players[i].equals(host.local_players[j].Name))
						msg += host.ping_game_data.Players[i] + "\n";
				
				String msg2 = msg;
				
				while(target == null)
				{
					String dest = JOptionPane.showInputDialog(msg);
					
					if(dest != null)
						for(int i = 0;i < host.ping_game_data.Players.length;i++)
							if(dest.equals(host.ping_game_data.Players[i]))
								target = dest;
					
					if(target == null)
					{
						msg = "Maybe you should choose a real player.\n" + msg2;
						continue;
					}
					
					if(target.equals(host.local_players[j].Name))
					{
						target = null;
						msg = "You can't meet with yourself.\n" + msg2;
					}
				}
				
				if(!host.hotseat)
					net.send(Data_Packer_Unpacker.PackMeetings(host.local_players[j].Name,target));
				else
				{
					host.meetings.AddMeeting(host.local_players[j].Name,target);
				}
			}
		}
		
		if(block == 4)
			for(int i = 0;i < host.local_players.length;i++)
				if(host.local_players[i].Class.equals("Sorcerer"))
				{
					((gui.Sorcerer)host.local_players[i].role).target = null;
					((gui.Sorcerer)host.local_players[i].role).has_murdered = false;
				}
				else if(host.local_players[i].Class.equals("Knight"))
				{
					((gui.Knight)host.local_players[i].role).target = null;
					((gui.Knight)host.local_players[i].role).has_murdered = false;
				}
		
		if(block == 6 && host.ops.food)
		{
			Player_Data[] dead = new Player_Data[host.local_players.length];
			
			for(int i = 0;i < host.local_players.length;i++)
			{
				host.local_players[i].food_supply--;
				
				if(host.local_players[i].food_supply < 0)
					for(int j = 0;j < dead.length;j++)
						if(dead[j] == null)
						{
							dead[j] = host.local_players[i];
							j = dead.length;
						}
			}
			
			int len = -1;
			
			for(int i = 0;i < dead.length;i++)
				if(dead[i] == null)
					len = i;
			
			if(len != -1)
			{
				Player_Data[] temp = new Player_Data[len];
				
				for(int i = 0;i < temp.length;i++)
					temp[i] = dead[i];
				
				dead = temp;
			}
			
			if(dead.length > 0)
			{
				if(!host.hotseat)
				{
					net.send(Data_Packer_Unpacker.PackLeaveDuringGame(dead));
					for(int i = 0;i < dead.length;i++)
						net.send(Data_Packer_Unpacker.PackDeath(dead[i].Class,dead[i].Name,Data_Packer_Unpacker.STARVATION));
				}
				else
				{
					for(int i = 0;i < dead.length;i++)
					{
						for(int i2 = 0;i2 < host.local_players.length;i2++)
						{
							if(dead[i] != null && host.local_players[i2].Name.equals(dead[i].Name))
							{
								if(true)
								{
									host.ping_game_data.players_left -= dead.length;
									
									for(int j2 = 0;j2 < host.ping_game_data.Players.length;j2++)
										if(host.ping_game_data.Players[j2].equals(dead[i].Name))
										{
											host.ping_game_data.Players[j2] = null;
											
											for(int l = j2;l < host.ping_game_data.Players.length - 1;l++)
												host.ping_game_data.Players[l] = host.ping_game_data.Players[l+1];
											
											String[] STemp = new String[host.ping_game_data.Players.length - 1];
											for(int l = 0;l < STemp.length;l++)
												STemp[l] = host.ping_game_data.Players[l];
											
											host.ping_game_data.Players = STemp;
											j2 = host.ping_game_data.Players.length - 1;
										}
									
									String Class = (String)dead[i].Class;
									String Name = (String)dead[i].Name;
									byte method = ((Byte)Data_Packer_Unpacker.STARVATION).byteValue();
									
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
									WinChecker.KillClass(Class,host);
									
									if(host.ops.players_at_local_computer == 0 || WinChecker.CheckGameOver())
										host.SetState(host_process.State.POST_GAME);
									else if(host.GetState() == host_process.State.GAME)
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
												if(host.local_players.length == 1)
													Game.switch_view(host.local_players[0].Class);
												else
													Game.switch_view("Player Select");
											else
												Game.refresh();
											
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return;
	}
	
	public static void create(Main host, IO net)
	{
		turns = new boolean[6];
		TurnChecker.host = host;
		TurnChecker.net = net;
		
		host.meetings = new Secret_Meeting_Manager();
		
		for(int i = 0;i < turns.length;i++)
			turns[i] = false;
		
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		
		for(int i = 0;i < host.local_players.length;i++)
			host.local_players[i].role.end_turn = false;
		
		for(int i = 0;i < 6;i++)
			if(WinChecker.IsDead(classes[i]))
				SetTurnOver(classes[i]);
		
		block = 1;
		round = 1;
		
		return;
	}
	
	public static void SetTurnOver(String class_over)
	{
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		
		for(int i = 0;i < classes.length;i++)
			if(class_over.equals(classes[i]))
				turns[i] = true;
		
		for(int i = 0;i < host.local_players.length;i++)
			if(host.local_players[i].Class.equals(class_over))
				host.local_players[i].role.end_turn = true;
		
		return;
	}
	
	public static boolean IsTurnOver(String class_over)
	{
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		
		for(int i = 0;i < classes.length;i++)
			if(class_over.equals(classes[i]))
				return turns[i];
		
		return true;
	}
	
	public static boolean round_over()
	{
		for(int i = 0;i < turns.length;i++)
			if(!turns[i])
				return false;
		
		return true;
	}
	
	public static boolean[] turns;
	public static int round;
	public static int block;
	public static IO net;
	public static Main host;
	
	public static final int A_BLOCK = 1;
	public static final int B_BLOCK = 2;
	public static final int C_BLOCK = 3;
	public static final int D_BLOCK = 4;
	public static final int E_BLOCK = 5;
	public static final int F_BLOCK = 6;
}
