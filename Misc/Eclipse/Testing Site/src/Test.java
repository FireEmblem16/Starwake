import java.util.Random;
import tree.TreeDiagram;

public class Test
{
	public static void main(String[] arg)
	{
		A a = new B();
		
		/*TreeDiagram<Integer> tree = new TreeDiagram<Integer>("Yggdrasil");
		Random rand = new Random();
		
		Integer[] ints = new Integer[(int)(rand.nextDouble() * 100.0) + 1];
		for(int i = 0;i < ints.length; i++)
			ints[i] = new Integer(rand.nextInt() / (2 << 16));
		
		tree.SetData(ints[0]);
		
		for(int i = 1; i < ints.length;i++)
		{
			add(tree,ints[i]);
			tree.ReturnToTrunk();
		}
		
		print(tree);*/
		return;
	}
	
	public static void add(TreeDiagram<Integer> tree, Integer val)
	{
		Integer top = tree.GetData();
		
		if(val < top)
		{
			if(!tree.EnterBranchShallow("l"))
			{
				tree.AddBranchFastAndEnter("l");
				tree.SetData(val);
				
				return;
			}
			
			add(tree,val);
		}
		else
		{
			if(!tree.EnterBranchShallow("r"))
			{
				tree.AddBranchFastAndEnter("r");
				tree.SetData(val);
				
				return;
			}
			
			add(tree,val);
		}
		
		return;
	}
	
	public static void print(TreeDiagram<Integer> tree)
	{
		if(tree.EnterBranchShallow("l"))
		{
			print(tree);
			tree.ExitBranch();
		}
		
		System.out.println(tree.GetData());
		
		if(tree.EnterBranchShallow("r"))
		{
			print(tree);
			tree.ExitBranch();
		}
		
		return;
	}
}
