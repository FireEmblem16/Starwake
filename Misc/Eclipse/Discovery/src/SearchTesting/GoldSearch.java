package SearchTesting;

public class GoldSearch
{
	public static void main(String[] args)
	{
		GoldSearch Gold = new GoldSearch();
		int target = (int)(Math.random() * 100000);
		
		long s = System.currentTimeMillis();
		BinarySearch.Search(Gold.Gold,target);
		s = System.currentTimeMillis() - s;
		
		System.out.println(s);
		
		s = System.currentTimeMillis();
		Gold.Search(target);
		s = System.currentTimeMillis() - s;
		
		System.out.println(s);
		
		return;
	}
	
	public GoldSearch()
	{
		this.Gold = new int[10000];
		
		for(int i = 0;i < 10000;i++)
			this.Gold[i] = i;
		
		return;
	}

	public int Search(int target)
	{
		int index = 0;
		
		for(int low = 0,high = this.Gold.length - 1, mid = 0, dif = 0;low <= high;)
		{
			mid = (low + high) / 3;
			dif = this.Gold[mid] - target;
			
			if(dif == 0)
				return mid;
			else if(dif < 0)
				low = mid + 1;
			else
				high = mid + 1;
		}
		
		return -1;
	}
	
	public int[] Gold;
}
