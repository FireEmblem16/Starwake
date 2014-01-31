package main;

import io.FileInformation;
import java.util.Random;

public class Randomizer
{
	public static FileInformation getvideo(Settings settings)
	{
		if(settings.weight_op_2 == Settings.PLAY_LEAST_OFTEN_PLAYED)
		{
			int[] pre_weights = new int[Host.tree.GetNumberOfSprouts()];
			
			for(int i = 0;i < Host.tree.GetNumberOfSprouts();i++)
				pre_weights[i] = Host.tree.GetBranch(i + 1).GetData().weight;
			
			int len = pre_weights.length;
			
			for(int i = 0;i < pre_weights.length;i++)
				if(pre_weights[i] == 0)
					len--;
			
			int[] weights = new int[len];
			
			for(int i = 0,start = 0;i < weights.length;i++)
				for(int j = start;j < pre_weights.length;j++)
					if(pre_weights[j] != 0)
					{
						weights[i] = pre_weights[j];
						start = j + 1;
						break;
					}
			
			int[] place = new int[weights.length];
			
			place[0] = 1;
			
			for(int i = 1;i < weights.length;i++)
				for(int j = 0,min_index = -1;j < i;j++)
				{
					if(weights[j] > weights[i])
					{
						if(min_index == -1 || weights[j] < weights[min_index])
							min_index = j;
					}
					
					if(j == i - 1)
					{
						if(min_index == -1)
						{
							place[i] = i + 1;
							break;
						}
						
						place[i] = weights[min_index];
						
						for(j = 0;j < i;j++)
							if(place[j] > place[i])
								place[j]++;
					}
				}
			
			for(int i = 1,low_index = -1,high_index = -1;i < (place.length + 1) / 2 + 1;i++)
				for(int j = 0;j < place.length;j++)
				{
					if(place[j] == i)
						low_index = j;
					else if(place[j] == place.length + 1 - i)
						high_index = j;
					
					if(low_index != -1 && high_index != -1)
					{
						weights[low_index] ^= weights[high_index];
						weights[high_index] ^= weights[low_index];
						weights[low_index] ^= weights[high_index];
					
						break;
					}
				}
			
			for(int i = 0,j = 0;i < Host.tree.GetNumberOfSprouts();i++)
				if(Host.tree.GetBranch(i + 1).GetData().weight != 0)
					Host.tree.GetBranch(i + 1).GetData().weight = weights[j++];
		}
		
		int sum = 0;
		
		for(int i = 0;i < Host.tree.GetNumberOfSprouts();i++)
			sum += Host.tree.GetBranch(i + 1).GetData().weight;
		
		int rnd = rand.nextInt(sum) + 1;
		sum = 0;
		
		for(int i = 0;i < Host.tree.GetNumberOfSprouts();i++)
		{
			sum += Host.tree.GetBranch(i + 1).GetData().weight;
			
			if(sum >= rnd)
			{
				Host.tree.EnterBranch(i + 1);
				
				if(Host.tree.GetNumberOfSprouts() == 0)
				{
					FileInformation ret = null;
					
					if(settings.weight_op_2 == Settings.PLAY_IN_ORDER)
					{
						Host.tree.ExitBranch();
						int first_val = -1;
						int last_val = -1;
						
						for(int j = 0;j < Host.tree.GetNumberOfSprouts();j++)
							if(!Host.tree.GetBranch(j + 1).GetData().IsDir())
							{
								if(j == 0)
									first_val = Host.tree.GetBranch(j + 1).GetData().plays;
								
								if(j < Host.tree.GetNumberOfSprouts() - 1)
								{
									if(last_val > -1)
									{
										if(Host.tree.GetBranch(j + 1).GetData().plays < last_val)
										{
											ret = Host.tree.GetBranch(j + 1).GetData();
											break;
										}
									}
									else
										last_val = Host.tree.GetBranch(j + 1).GetData().plays;
								}
								else
								{
									if(Host.tree.GetBranch(j + 1).GetData().plays < first_val)
									{
										ret = Host.tree.GetBranch(j + 1).GetData();
										break;
									}
									else
									{
										for(j = 0;j < Host.tree.GetNumberOfSprouts();j++)
											if(!Host.tree.GetBranch(j + 1).GetData().IsDir())
											{
												ret = Host.tree.GetBranch(j + 1).GetData();
												break;
											}
										
										break;
									}
								}
							}
						
						if(ret != null && ret.plays > -1)
							ret.plays++;
						
						Host.tree.ReturnToTrunk();
						Host.reweightree(settings);
					}
					else
					{
						Host.tree.GetData().plays++;
						ret = Host.tree.GetData();
						Host.tree.ReturnToTrunk();
						Host.reweightree(settings);
					}
					
					return ret;
				}
				else
					return getvideo(settings);
			}
		}
		
		Host.tree.ReturnToTrunk();
		return null;
	}
	
	public static Random rand = new Random();
}
