package host_process;

import gui.Class;

public class Player_Data
{
	public Player_Data()
	{
		PC = false;
		food_supply = 7;
		Class = null;
		Name = null;
		role = null;
		
		return;
	}
	
	public boolean PC;
	public Class role;
	public int food_supply;
	public String Class;
	public String Name;
}
