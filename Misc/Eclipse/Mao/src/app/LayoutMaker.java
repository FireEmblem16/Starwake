package app;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import client.ClientApp;

public class LayoutMaker
{
	public static void CreateGameConfig(ClientApp app)
	{
		app.getContentPane().removeAll();
		
		return;
	}
	
	public static void CreateIHostConfig(ClientApp app)
	{
		app.getContentPane().removeAll();
		
		return;
	}
	
	public static void CreateIJoinConfig(ClientApp app)
	{
		app.getContentPane().removeAll();
		
		return;
	}
	
	public static void CreateLHostConfig(ClientApp app)
	{
		ScrollPane Settings = new ScrollPane(app);
		Button Start = new Button(app,"Start Game","Start","Begin the Game");
		Button Cancel = new Button(app,"Cancel","Cancel","Return to the LAN Join Screen");
		
		app.getContentPane().removeAll();
        GroupLayout frame = new GroupLayout(app.getContentPane());
        {
			ParallelGroup HR = frame.createParallelGroup(GroupLayout.Alignment.LEADING);
			{
				ParallelGroup h1 = frame.createParallelGroup(GroupLayout.Alignment.LEADING);
				{
					SequentialGroup h2 = frame.createSequentialGroup();
					{
						h2.addGap(20);
						h2.addComponent(Settings,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
					}
					h1.addGroup(Alignment.CENTER,h2);
					
					SequentialGroup h3 = frame.createSequentialGroup();
					{
						h3.addComponent(Start,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
						h3.addGap(5);
						h3.addComponent(Cancel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
					}
					h1.addGroup(Alignment.CENTER,h3);
				}
				HR.addGroup(Alignment.TRAILING,h1);
			}
			frame.setHorizontalGroup(HR);
			
			ParallelGroup Vert = frame.createParallelGroup(GroupLayout.Alignment.LEADING);
			{
				SequentialGroup v1 = frame.createSequentialGroup();
				{
					v1.addGap(5);
					v1.addComponent(Settings,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
					v1.addGap(5);
					
					ParallelGroup v2 = frame.createParallelGroup(GroupLayout.Alignment.LEADING);
					{
						v2.addGap(5);
						v2.addComponent(Start,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
						v2.addComponent(Cancel,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
					}
					v1.addGroup(v2);
				}
				Vert.addGroup(v1);
			}
			frame.setVerticalGroup(Vert);
		}
		app.getContentPane().setLayout(frame);
		
		
		return;
	}
	
	public static void CreateLJoinConfig(ClientApp app)
	{
		app.getContentPane().removeAll();
		
		return;
	}
}
