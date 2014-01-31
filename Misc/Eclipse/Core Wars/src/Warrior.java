
public class Warrior
{
	public Warrior(int number, int starting_ip)
	{
		n = number;
		ip = starting_ip;
		state = State.ALIVE;
		
		validate_ip();
		return;
	}
	
	public void increment_ip()
	{
		ip++;
		validate_ip();
		
		return;
	}
	
	public void jump(int offset)
	{
		ip += offset;
		validate_ip();
		
		return;
	}
	
	public void goto_instruction(int new_ip)
	{
		ip = new_ip;
		validate_ip();
		
		return;
	}
	
	public void validate_ip()
	{
		ip = Statics.mod_memory_size(ip);
		return;
	}
	
	public enum State
	{ALIVE,DEAD}
	
	public int ip;
	public int n;
	public State state;
}
