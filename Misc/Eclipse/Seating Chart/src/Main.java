import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int N = in.nextInt();
		
		while(N != 0)
		{
			String[] first = new String[N]; // Sorted
			Pair<String,Integer>[] second = (Pair<String,Integer>[])new Pair[N]; // Inverted
			
			for(int i = 0;i < N;i++)
				first[i] = in.next();
			
			for(int i = 0;i < N;i++)
				second[i] = new Pair<String,Integer>(in.next(),i);
			
			Comparator<Pair<String,Integer>> sort = new Comparator<Pair<String,Integer>>()
			{
				public int compare(Pair<String,Integer> p1, Pair<String,Integer> p2)
				{return p1.s.compareTo(p2.s);}
			};
			
			Arrays.sort(second,sort);
			int ret = 0;
			
			for(int i = 0;i < N;i++)
			{
				int index = Arrays.binarySearch(second,new Pair<String,Integer>(first[i],0),sort);
				int diff = i - second[index].t;
				
				if(diff > 0)
					ret += diff;
			}
			
			System.out.println(ret);
			N = in.nextInt();
		}
		
		return;
	}
	
	public static class Pair<S,T>
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
}
