
public class Pair<S,T>
{
	public Pair(S s, T t)
	{
		this.s = s;
		this.t = t;
		
		return;
	}
	
	public boolean equals(Object obj)
	{
		if(this.getClass().isAssignableFrom(obj.getClass()))
			return equals((Pair)obj);
		
		return false;
	}
	
	public boolean equals(Pair<S,T> p)
	{
		return s.equals(p.s) && t.equals(p.t);
	}
	
	public String toString()
	{return "(" + s.toString() + "," + t.toString() + ")";}
	
	public T t;
	public S s;
}
