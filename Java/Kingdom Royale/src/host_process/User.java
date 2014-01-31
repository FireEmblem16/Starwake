package host_process;

/**
 * This class should be treated as a struct.
 */
public class User
{
	/**
	 * The bet amount is doubled automatically if the player was the PC.
	 */
	public void LogGame(boolean was_PC, boolean won, boolean money_game, int amount_bet, int net_earnings, long time_spent)
	{
		if(name == null || name.equals(""))
			return;
		
		if(was_PC)
		{
			pc_games++;
			amount_bet *= 2;
			net_earnings *= 2;
			
			if(won)
				pc_wins++;
		}
		
		if(won)
			wins++;
		else
			losses++;
		
		if(money_game)
		{
			money_games++;
			
			if(net_earnings > 0)
				money_won += net_earnings;
			else if(net_earnings < 0)
				money_lost += net_earnings;
			
			money_bet += amount_bet;
		}
		
		time += time_spent;
		
		return;
	}
	
	public double GetWinPercentage()
	{
		if(wins + losses == 0)
			return Double.NaN;
		
		return (double)wins / (double)(wins + losses) * 100.0;
	}
	
	public double GetPCWinPercentage()
	{
		if(pc_games == 0)
			return Double.NaN;
		
		return (double)pc_wins / (double)pc_games * 100.0;
	}
	
	public double GetPercentageMoneyGames()
	{
		if(wins + losses == 0)
			return Double.NaN;
		
		return (double)money_games / (double)(wins + losses) * 100.0;
	}
	
	public String GetMoneyWon()
	{
		return "$" + (money_won / 100) + "." + (money_won % 100);
	}
	
	public String GetMoneyLost()
	{
		return "$" + (money_lost / 100) + "." + (money_lost % 100);
	}
	
	public String GetNetMoney()
	{
		return "$" + ((money_won - money_lost) / 100) + "." + ((money_won - money_lost) % 100);
	}
	
	public String GetMoneyBet()
	{
		return "$" + (money_bet / 100) + "." + (money_bet % 100);
	}
	
	public String GetTimeSpentPlaying()
	{
		String ret = "";
		long time = this.time;
		
		long milliseconds = time % 1000;
		time /= 1000;
		
		long seconds = time % 60;
		time /= 60;
		
		long minutes = time % 60;
		time /= 60;
		
		long hours = time % 24;
		time /= 24;
		
		long years = time / 365;
		time -= years * 365;
		
		long days = time;
		time -= days;
		
		if(time != 0)
			return "FPU failiur.";
		
		if(years != 0)
			ret += years + " Years ";
		
		if(days != 0)
			ret += days + " Days ";
		
		if(hours != 0)
			ret += hours + " Hours ";
		
		if(minutes != 0)
			ret += minutes + " Minutes ";
		
		if(seconds != 0)
			ret += seconds + " Seconds ";
		
		if(milliseconds != 0)
			ret += milliseconds + " Milliseconds";
		
		if(ret.equals(""))
			ret = "No time spent playing.";
		
		return ret;
	}
	
	public String name;
	public int wins;
	public int losses;
	public int money_games;
	public int money_won;
	public int money_lost;
	public int money_bet;
	public int pc_games;
	public int pc_wins;
	public long time;
}
