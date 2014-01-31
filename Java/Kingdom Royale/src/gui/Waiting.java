package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import host_process.Game_Status;
import host_process.Main;
import host_process.Shell;
import host_process.State;
import host_process.User;
import io.Data_Packer_Unpacker;
import io.IO;
import io.Responder;

public class Waiting
{
	public static void create(Main host, Shell win, IO net)
	{
		response = new Responder("Request Responder",host,win,net);
		
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		Cancel = new JButton("Cancel");
		Cancel.setPreferredSize(new Dimension(75,25));
		Cancel.setActionCommand("WAITING:CANCEL");
		Cancel.setToolTipText("Return to the LAN lobby.");
		Cancel.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Cancel.setMargin(new Insets(0,0,0,0));
		Cancel.addActionListener(win);
		
		String[] text = {"Waiting for a full set of players.","Game: " + host.ops.name,"Estimated Maximum Game Time: " + host.ping_game_data.estimate_remaining_time,"Food: " + (host.ops.food ? "On" : "Off"),"Strict Time: " + (host.ops.strict_time ? "On" : "Off"),"Eliminate Blank Time: " + (host.ops.eliminate_extra_time ? "On" : "Off"),"Local Players: " + new Integer(host.ops.players_at_local_computer).toString(),"Current Number of Players: " + new Integer(host.ping_game_data.players_left).toString() + " / 6","Bet: " + (host.ops.betting ? host.ops.AmountBet() : "No Bet")};
		Labels = new JLabel[text.length];
		
		for(int i = 0;i < text.length;i++)
		{
			Labels[i] = new JLabel();
			Labels[i].setText(text[i]);
			Labels[i].setFont(new Font("Times New Roman",Font.PLAIN,12));
		}
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		for(int i = 0;i < Labels.length;i++)
			h1.addComponent(Labels[i],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Cancel,GroupLayout.Alignment.TRAILING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		
		Vertical.addContainerGap();
		Vertical.addComponent(Labels[0],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGap(12);
		for(int i = 1;i < Labels.length;i++)
			Vertical.addComponent(Labels[i],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(Cancel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(240,240));
		win.setSize(new Dimension(240,240));
        win.setVisible(true);
		
        if(!host.hotseat)
        {
        	Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    		win.setCursor(hourglassCursor);
    		
        	for(int i = 1;i < 256;i++)
    		{
    			net.ChangeAddress("224.0.0." + i);
    			net.send(Data_Packer_Unpacker.PackPing());
    			
    			int j;
    			
    			for(j = 0;j < 10;j++)
    			{
    				try
    				{
    					Thread.sleep(10L);
    				}
    				catch(InterruptedException e)
    				{}
    				
    				byte[] BTemp = net.fetch();
    				
    				if(BTemp != null)
    					if(Data_Packer_Unpacker.UnpackPingResponse(BTemp) != null)
    						j = 10;
    			}
    			
    			if(j == 11)
    				continue;
    			
    			host.ping_game_data.address = "224.0.0." + new Integer(i).toString();
    			i = 255;
    		}
        	
        	if(host.ping_game_data.address == null)
        	{
            	JOptionPane.showMessageDialog(null,"What's up with you. Creating a game when the network is so crowded. Shame!","Error",JOptionPane.ERROR_MESSAGE);
            	host.SetState(State.LAN_LOBBY);
            }
            else
            	response.start();
        	
        	Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    		win.setCursor(normalCursor);
        }
        
		return;
	}
	
	public static JButton Cancel;
	public static JLabel[] Labels;
	public static Responder response;
}
