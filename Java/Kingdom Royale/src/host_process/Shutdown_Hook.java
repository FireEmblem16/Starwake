package host_process;

import io.Data_Packer_Unpacker;
import io.IO;

public class Shutdown_Hook extends Thread
{
	public Shutdown_Hook(Main data, IO net)
	{
		super("Shutdown Hook");
		this.data = data;
		
		return;
	}
	
	public void run()
	{
		switch(data.state)
		{
		case START_UP:
		case BASE:
		case LAN_OPTIONS:
		case LAN_LOBBY:
		case HOTSEAT_OPTIONS:
			break;
		case WAITING:
			net.send(Data_Packer_Unpacker.PackLeave(data.ops.players_at_local_computer));
			break;
		case GAME_START:
		case GAME:
			if(data.IsAlone())
				data.user.LogGame(data.local_players[0].PC,false,data.ops.betting,(int)(100 * data.ops.bet_amount),(int)(-100 * data.ops.bet_amount),System.currentTimeMillis() - data.ops.start_time);
			
			if(!data.hotseat)
				net.send(Data_Packer_Unpacker.PackLeaveDuringGame(data.local_players));
			
			for(int i = 0;i < data.local_players.length;i++)
				if(data.local_players[i].Class != null)
					if(!data.hotseat)
						net.send(Data_Packer_Unpacker.PackDeath(data.local_players[i].Class,data.local_players[i].Name,Data_Packer_Unpacker.RULES_VIOLATION));
			break;
		case POST_GAME:
			if(data.IsAlone())
				data.user.LogGame(data.local_players[0].PC,WinChecker.DidWin(data.local_players[0].Class),data.ops.betting,(int)(100 * data.ops.bet_amount),(int)(-100 * data.ops.bet_amount),System.currentTimeMillis() - data.ops.start_time);
			
			break;
		default:
			if(data.IsAlone())
				data.user.LogGame(data.local_players[0].PC,false,data.ops.betting,(int)(100 * data.ops.bet_amount),(int)(-100 * data.ops.bet_amount),System.currentTimeMillis() - data.ops.start_time);
			
			break;
		}
		
		data.SaveUser();
		
		return;
	}
	
	Main data;
	IO net;
}
