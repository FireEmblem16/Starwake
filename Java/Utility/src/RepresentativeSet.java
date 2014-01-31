import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RepresentativeSet<T> implements Iterable<T>
{
	public RepresentativeSet(Collection<T> c)
	{
		items = new ArrayList<T>(c);
		return;
	}
	
	public boolean InSet(T obj)
	{
		for(T t : items)
			if(t.equals(obj))
				return true;
		
		return false;
	}
	
	public T GetRepresentative()
	{return items.get(0);}
	
	public Iterator<T> iterator()
	{return items.iterator();}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof RepresentativeSet)
			return equals((RepresentativeSet)obj);
		
		if(GetRepresentative().getClass().isAssignableFrom(obj.getClass()))
			return InSet((T)obj);
		
		return false;
	}
	
	public boolean equals(RepresentativeSet s)
	{return GetRepresentative().equals(s.GetRepresentative());}
	
	public String toString()
	{return GetRepresentative().toString();}
	
	protected ArrayList<T> items;
}
