import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Main
{
	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(new File("corewars.txt"));
		int n = in.nextInt();
		String[] names = {"TROGDOR, THE BURNINATOR","GILGAMEX"};
		
		for(int i = 0;i < n;i++)
			new Main(in,2,names,false);
		
		return;
	}
	
	public Main(Scanner in, int number_of_warriors, String[] warrior_names, boolean run_concurrently)
	{
		DataSegment[] memory = new DataSegment[Statics.memory_size];
		WarriorHeart[] warriors = new WarriorHeart[number_of_warriors];
		
		for(int i = 0;i < Statics.memory_size;i++)
			memory[i] = new DataSegment(i,"DAT",0,"#",0,"#");
		
		for(int i = 0;i < number_of_warriors;i++)
		{
			int m = in.nextInt();
			int a = in.nextInt();
			in.nextLine();
			
			warriors[i] = new WarriorHeart(memory,warrior_names[i],i + 1,a,run_concurrently,number_of_warriors);
			
			for(int j = 0;j < m;j++,a++)
			{
				String line = in.nextLine();
				Scanner tokener = null;
				
				String opcode = line.substring(0,3);
				
				String a_mode = line.substring(4,5);
				line = line.substring(5);
				tokener = new Scanner(line);
				int a_value = tokener.nextInt();
				
				line = line.substring(line.indexOf(' ') + 1);
				
				String b_mode = line.substring(0,1);
				line = line.substring(1);
				tokener = new Scanner(line);
				int b_value = tokener.nextInt();
				
				memory[a] = new DataSegment(a,opcode,a_value,a_mode,b_value,b_mode);
			}
		}
		
		if(run_concurrently)
		{
			
		}
		else
		{
			int warrior_turn = 1;
			
			while(turn_count < max_turns)
			{
				if(!warriors[warrior_turn - 1].step())
				{
					for(int i = 0;i < number_of_warriors;i++)
						warriors[i].notify_of_death();
					
					for(int i = warrior_turn - 1;i < number_of_warriors - 1;i++)
						warriors[i] = warriors[i + 1];
					
					warriors[number_of_warriors - 1] = null;
					
					number_of_warriors--;
					warrior_turn--;
				}
				
				turn_count++;
				warrior_turn++;
				
				if(warrior_turn > number_of_warriors)
					warrior_turn = 1;
				
				if(number_of_warriors == 1)
				{
					System.out.println(warriors[0].getName() + " is the winner.");
					break;
				}
			}
			
			if(turn_count == max_turns && warriors.length > 1 || warriors.length == 0)
				System.out.println("Programs are tied.");
		}
		
		return;
	}
	
	public int turn_count = 0;
	public int max_turns = 32000;
}
