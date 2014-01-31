package host_process;

import gui.Double;
import gui.King;
import gui.Knight;
import gui.Prince;

public class WinChecker
{
	public static void create()
	{
		alive = new boolean[6];
		for(int i = 0;i < alive.length;i++)
			alive[i] = true;
		
		return;
	}
	
	public static boolean DidWin(String Class)
	{
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		boolean win = true;
		int index = -1;
		
		for(int i = 0;i < classes.length;i++)
			if(Class.equals(classes[i]))
				index = i;
		
		switch(index)
		{
		case 0:
			if(alive[0] && !(alive[1] || alive[5]))
				win = false;
			
			break;
		case 1:
			if(alive[1] && !(alive[0] || alive[2] || alive[5]))
				win = false;
			
			break;
		case 2:
			if(alive[2] && !(alive[1] || alive[5]))
				win = false;
			
			break;
		case 3:
			if(!alive[3])
				win = false;
			
			break;
		case 4:
			if(alive[4] && !(alive[0] || alive[1]))
				win = false;
			
			break;
		case 5:
			if(alive[5] && !(alive[0] || alive[1] || alive[2]))
				win = false;
			
			break;
		default:
			win = false;
			break;
		}
		
		return win;
	}
	
	public static boolean CheckGameOver()
	{
		boolean game_over = true;
		
		if(alive[0] && (alive[1] || alive[5]))
			game_over = false;
		
		if(alive[1] && (alive[0] || alive[2] || alive[5]))
			game_over = false;
		
		if(alive[2] && (alive[1] || alive[5]))
			game_over = false;
		
		if(alive[4] && (alive[0] || alive[1]))
			game_over = false;
		
		if(alive[5] && (alive[0] || alive[1] || alive[2]))
			game_over = false;
		
		return game_over;
	}
	
	/**
	 * Calls SetDeath within its function call.
	 */
	public static void KillClass(String Class, Main host)
	{
		TurnChecker.SetTurnOver(Class);
		
		if(Class.equals("King"))
		{
			for(int i = 0;i < host.ops.players_at_local_computer;i++)
				if(host.local_players[i].Class.equals("Prince"))
					if(((Prince)host.local_players[i].role).first_murder_requirement)
						((Prince)host.local_players[i].role).murder = true;
					else
						((Prince)host.local_players[i].role).first_murder_requirement = true;
		}
		else if(Class.equals("Double"))
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
		else if(Class.equals("Sorcerer"))
		{
			for(int i = 0;i < host.ops.players_at_local_computer;i++)
				if(host.local_players[i].Class.equals("Knight"))
					((Knight)host.local_players[i].role).murder = true;
		}
		
		for(int i = 0;i < host.ops.players_at_local_computer;i++)
			if(host.local_players[i].Class.equals(Class))
			{
				Player_Data[] temp = new Player_Data[host.local_players.length - 1];
				
				for(int j = 0;j < temp.length;j++)
					if(j < i)
						temp[j] = host.local_players[j];
					else
						temp[j] = host.local_players[j+1];
				
				host.local_players = temp;
				host.ops.players_at_local_computer--;
				i = host.ops.players_at_local_computer - 1;
			}
		
		SetDeath(Class);
		
		return;
	}
	
	public static void SetDeath(String class_dead)
	{
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		
		for(int i = 0;i < classes.length;i++)
			if(class_dead.equals(classes[i]))
				alive[i] = false;
		
		return;
	}
	
	public static boolean IsDead(String Class)
	{
		String[] classes = {"King","Prince","Double","Sorcerer","Knight","Revolutionary"};
		
		for(int i = 0;i < classes.length;i++)
			if(Class.equals(classes[i]))
				return !alive[i];
		
		return false;
	}
	
	public static boolean[] alive;
}