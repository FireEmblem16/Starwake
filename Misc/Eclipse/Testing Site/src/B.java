
public class B extends A implements Comparable
{
	public B()
	{
		System.out.println("BC");
	}
	
	public void init()
	{
		super.init();
		System.out.println("IB");
	}
	
	public static void r()
	{
		
		
		return;
	}
	
	void aa()
	{
		return;
	}
	
	int testme()
	{
		return 1;
	}
	
	public int compareTo(Object t)
	{
		return 0;
	}
}
