import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class Lollies
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		
		ArrayList<Day> days = new ArrayList<Day>();
		
		String oldtitle = in.next();
		boolean first = true;
		
		while(!oldtitle.equals("#"))
		{
			String title = "";
			days.clear();
			
			while(true)
			{
				String a = in.next();
				
				if(!IsNumber(a))
				{
					title = a;
					break;
				}
				
				days.add(new Day(new Integer(days.size() + 1), new Integer(a),in.nextInt()));
			}
			
			Day[] DAYS = new Day[days.size()];
			days.toArray(DAYS);
			Arrays.sort(DAYS);
			days.clear();
			
			for(int i = 0;i < DAYS.length;i++)
				days.add(DAYS[i]);
			
			Object[][] max = new Object[4][days.size() + 1];
			
			for(int i = 0;i < max[0].length - 1;i++)
			{
				max[0][i] = i;
				max[1][i] = days.get(i).day;
				max[2][i] = 0;
				max[3][i] = new LinkedList<Day>();
			}
			
			max[0][max[0].length - 1] = max[0].length;
			max[1][max[1].length - 1] = Integer.MAX_VALUE;
			max[2][max[2].length - 1] = 0;
			max[3][max[3].length - 1] = new LinkedList<Day>();
			
			Arrays.sort(max[1]);
			
			for(int i = 1;i < max[0].length;i++)
			{
				ArrayList<Integer> max_p = new ArrayList<Integer>();
				max_p.add((Integer)max[2][i - 1]);
				
				ArrayList<Day> days_in_range = new ArrayList<Day>();
				
				for(Day d : days)
					if(d.r() > (Integer)max[1][i - 1] && d.r() <= (Integer)max[1][i])
						days_in_range.add(d);
				
				ArrayList<Integer> max_accomp = new ArrayList<Integer>();
				ArrayList<Integer> help = new ArrayList<Integer>();
				
				for(Day d : days_in_range)
					if(i == 0)
					{
						max_accomp.add(0);
						help.add(0);
					}
					else
					{
						max_accomp.add((Integer)max[2][d.day - 1]);
						help.add(d.day - 1);
					}
				
				for(int j = 0;j < days_in_range.size();j++)
					max_p.add(days_in_range.get(j).lollies + max_accomp.get(j));
				
				int maxi = 0;
				
				for(int j = 0;j < max_p.size();j++)
					if(max_p.get(j) > max_p.get(maxi))
						maxi = j;
					else if(maxi != 0 && max_p.get(j) == max_p.get(maxi)) // If they are equal we need to crawl through the days lists to see who collects faster by time, not lolly-time
					{													  // Also note that since no two sets of days can have one being a subset of the other we will always have one that is better
						LinkedList<Day> current = (LinkedList<Day>)max[3][help.get(maxi - 1)];
						LinkedList<Day> potential = (LinkedList<Day>)max[3][help.get(j - 1)];
						
						for(int k = 0;k < Math.min(current.size(),potential.size());k++)
							if(current.get(k).day < potential.get(k).day)
								break;
							else if(current.get(k).day > potential.get(k).day)
							{
								maxi = j;
								break;
							}
					}
				
				max[2][i] = max_p.get(maxi);
				
				if(maxi == 0)
					max[3][i] = new LinkedList<Day>((LinkedList<Day>)max[3][i - 1]);
				else
				{
					LinkedList<Day> r = new LinkedList<Day>((LinkedList<Day>)max[3][help.get(maxi - 1)]);
					r.add(days_in_range.get(maxi - 1));
					max[3][i] = r;
				}
			}
			
			if(first)
				first = false;
			else
				System.out.println("\n");
			
			System.out.print("In " + oldtitle + " " + max[2][max[0].length - 1] + " loll" + ((Integer)max[2][max[0].length - 1] == 1 ? "y" : "ies") + " can be obtained:" + ((Integer)max[2][max[0].length - 1] == 0 ? "" : "\n"));
			
			for(int i = 0;i < ((LinkedList<Day>)max[3][max[3].length - 1]).size();i++)
				System.out.print(((LinkedList<Day>)max[3][max[3].length - 1]).get(i) + (i == ((LinkedList<Day>)max[3][max[3].length - 1]).size() - 1 ? "" : "\n"));
			
			oldtitle = title;
		}
		
		return;
	}
	
	public static boolean IsNumber(String str)
	{
		try
		{new Integer(str);}
		catch(NumberFormatException e)
		{return false;}
		
		return true;
	}
	
	public static class Day implements Comparable<Day>
	{
		public Day(Integer n, Integer lol, Integer del)
		{
			day = n;
			lollies = lol;
			delay = del;
			
			return;
		}
		
		public int r()
		{
			return day + delay;
		}
		
		public int compareTo(Day d2)
		{
			if(r() > d2.r())
				return 1;
			else if(r() < d2.r())
				return -1;
					
			
			return 0;
		}
		
		public String toString()
		{
			return "On day " + day + " collect " + lollies + " loll" + (lollies == 1 ? "y" : "ies") + ".";
		}
		
		public Integer day;
		public Integer delay;
		public Integer lollies;
	}
}