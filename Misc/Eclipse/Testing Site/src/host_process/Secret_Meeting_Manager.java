package host_process;

import javax.swing.JOptionPane;

public class Secret_Meeting_Manager
{
	public static void main(String[] args)
	{
		Secret_Meeting_Manager meetings = new Secret_Meeting_Manager();
		meetings.AddMeeting("A","B");
		meetings.AddMeeting("B","C");
		meetings.AddMeeting("F","D");
		meetings.AddMeeting("D","B");
		meetings.AddMeeting("C","B");
		meetings.AddMeeting("E","D");
		
		meetings.DisplayMeetingTable();
		
		return;
	}
	
	public Secret_Meeting_Manager()
	{
		Meetings = new String[6][2];
		
		for(int i = 0;i < 6;i++)
		{
			Meetings[i][0] = null;
			Meetings[i][1] = null;
		}
		
		return;
	}
	
	public void AddMeeting(String chooser, String meeted)
	{
		for(int i = 0;i < 6;i++)
			if(Meetings[i][0] == null)
			{
				Meetings[i][0] = chooser;
				Meetings[i][1] = meeted;
				
				i = 5;
			}
		
		Sort();
		
		return;
	}
	
	public void DisplayMeetingTable()
	{
		String table = "The players have choosen to meet as such:\n";
		
		for(int i = 0;i < 6;i++)
			if(Meetings[i][0] != null)
				table += Meetings[i][0] + " chose to meet with " + Meetings[i][1] + ".\n";
		
		table += "\nThese meetings should take place in this order.\nIf meetings conflict take care of the first in the list before the others.\n";
		
		JOptionPane.showMessageDialog(null,table,"Secret Meetings",JOptionPane.INFORMATION_MESSAGE);
		
		return;
	}
	
	protected void Sort()
	{
		String[][] STemp = new String[6][2];
		
		for(int i = 0;i < 6;i++)
		{
			STemp[i][0] = null;
			STemp[i][1] = null;
		}
		
		for(int j = 0;j < 6;j++)
			if(Meetings[j][0] != null)
			{
				STemp[0][0] = Meetings[j][0];
				STemp[0][1] = Meetings[j][1];
				
				Meetings[j][0] = null;
				Meetings[j][1] = null;
				
				j = 5;
			}
		
		for(int j = 0;j < 6;j++)
			if(Meetings[j][0] != null)
				if(Meetings[j][0].equals(STemp[0][1]) && Meetings[j][1].equals(STemp[0][0]))
				{
					STemp[1][0] = Meetings[j][0];
					STemp[1][1] = Meetings[j][1];
					
					Meetings[j][0] = null;
					Meetings[j][1] = null;
					
					j = 5;
				}
		
		for(int i = 0;i < 6;i++)
			if(STemp[i][0] == null)
			{
				for(int j = 0;j < 6;j++)
					if(Meetings[j][0] != null)
						if(Meetings[j][0].equals(STemp[i-1][1]) && Meetings[j][1].equals(STemp[i-1][0]))
						{
							STemp[i][0] = Meetings[j][0];
							STemp[i][1] = Meetings[j][1];
							
							Meetings[j][0] = null;
							Meetings[j][1] = null;
							
							j = 5;
						}
				
				if(STemp[i][0] != null)
				{
					for(int j = 0;j < 6;j++)
						if(Meetings[j][0] != null)
							if(Meetings[j][0].equals(STemp[0][1]) && Meetings[j][1].equals(STemp[0][0]))
							{
								STemp[i+1][0] = Meetings[j][0];
								STemp[i+1][1] = Meetings[j][1];
								
								Meetings[j][0] = null;
								Meetings[j][1] = null;
								
								j = 5;
							}
				}
				else
					for(int j = 0;j < 6;j++)
						if(Meetings[j][0] != null)
						{
							STemp[i][0] = Meetings[j][0];
							STemp[i][1] = Meetings[j][1];
							
							Meetings[j][0] = null;
							Meetings[j][1] = null;
							
							j = 5;
						}
			}
		
		Meetings = STemp;
		return;
	}
	
	protected String[][] Meetings;
}
