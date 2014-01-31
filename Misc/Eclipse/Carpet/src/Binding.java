public class Binding
{
	public Binding(String label1, String dir1, String label2, String dir2)
	{
		l1 = label1;
		l2 = label2;
		
		d1 = dir1;
		d2 = dir2;
			
		return;
	}
	
	public String toString()
	{
		return l1 + ": " + d1 + " --- " + l2 + ": " + d2;
	}
	
	public String l1;
	public String l2;
	public String d1;
	public String d2;
}