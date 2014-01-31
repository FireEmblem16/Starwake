
public class Statics
{
	public static int mod_memory_size(int v)
	{
		while(v < 0)
			v += memory_size;
		
		v %= memory_size;
		return v;
	}
	
	public static int memory_size = 8000;
}
