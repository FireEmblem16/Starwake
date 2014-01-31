package tree;

public class Tree<E>
{
	public Tree()
	{return;}
	
	public Tree(E init)
	{
		data = init;
		return;
	}
	
	public Tree(E init, int w)
	{
		data = init;
		weight = w;
		
		return;
	}
	
	public String toString()
	{
		if(data == null)
			return "";
		
		return data.toString();
	}
	
	public E data;
	public int weight;
	public Tree<E> left;
	public Tree<E> right;
}
