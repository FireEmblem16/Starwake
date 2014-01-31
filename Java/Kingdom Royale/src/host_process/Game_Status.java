package host_process;

public class Game_Status
{
	public Game_Status()
	{
		return;
	}
	
	public Game_Status(String name, String status, String time_left, int players_in_game, int bet, boolean food_on, boolean skip_empty_time, boolean strict_time)
	{
		food = food_on;
		this.skip_empty_time = skip_empty_time;
		this.bet = bet;
		players_left = players_in_game;
		estimate_remaining_time = time_left;
		this.name = name;
		this.status = status;
		address = "";
		this.strict_time = strict_time;
		
		return;
	}
	
	/**
	 * Returns true iff every setting is the same.
	 * If the address of [this] is null, it will be forgiven.
	 */
	public boolean equals(Game_Status stats)
	{
		if(food != stats.food)
			return false;
		
		if(skip_empty_time != stats.skip_empty_time)
			return false;
		
		if(bet != stats.bet)
			return false;
		
		if(players_left != stats.players_left)
			return false;
		
		if(address != null)
			if(!address.equals(stats.address))
				return false;
		
		if(!estimate_remaining_time.equals(stats.estimate_remaining_time))
			return false;
		
		if(!name.equals(stats.name))
			return false;
		
		if(!status.equals(stats.status))
			return false;
		
		if(strict_time != stats.strict_time)
			return false;
		
		return true;
	}
	
	public boolean food;
	public boolean skip_empty_time;
	public boolean strict_time;
	public int bet;
	public int players_left;
	public String address;
	public String estimate_remaining_time;
	public String name;
	public String status;
	public String[] Players;
}
