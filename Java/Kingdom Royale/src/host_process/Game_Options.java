package host_process;

public class Game_Options
{
	public Game_Options()
	{
		betting = true;
		eliminate_extra_time = false;
		food = true;
		strict_time = true;
		bet_amount = 500;
		game_speed = "8 Days";
		players_at_local_computer = 1;
		start_time = 0;
		name = "";
		
		return;
	}
	
	public String AmountBet()
	{
		return "$" + (bet_amount / 100) + "." + (bet_amount % 100) + ((bet_amount % 10) == 0 ? "0" : "");
	}
	
	public static String ToMoney(int bet)
	{
		return "$" + (bet / 100) + "." + (bet % 100) + ((bet % 10) == 0 ? "0" : "");
	}
	
	public boolean betting;
	public boolean eliminate_extra_time;
	public boolean food;
	public boolean strict_time;
	public int bet_amount;
	public int players_at_local_computer;
	public long start_time;
	public String game_speed;
	public String name;
}
