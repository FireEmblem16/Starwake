
public abstract class A
{
	public A()
	{
		System.out.println("AC");
		init();
	}
	
	public void init()
	{
		System.out.println("IA");
	}
	
	public final void a()
	{
		return;
	}
	
	abstract void aa();
	
	int testme()
	{
		return 0;
	}
}
