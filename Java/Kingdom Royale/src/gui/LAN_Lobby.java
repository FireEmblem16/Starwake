package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;
import java.net.InetAddress;
import host_process.Game_Status;
import host_process.Shell;
import host_process.User;
import io.Data_Packer_Unpacker;
import io.IO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.table.AbstractTableModel;

public class LAN_Lobby
{
	public static void create(Shell win, IO prober, String probe_address)
	{
		game_stats = new Game_Status[0];
		players = new User[0][6];
		
		if(probe_address == null)
			Probe(win,prober);
		else if(probe_address.equals(""))
		{
			String[][] data = new String[game_stats.length][8]; 
			for(int i = 0;i < data.length;i++)
			{
				data[i][0] = game_stats[i].name;
				data[i][1] = new Integer(game_stats[i].players_left).toString();
				data[i][2] = game_stats[i].status;
				data[i][3] = Options.UnparseMoney(game_stats[i].bet);
				data[i][4] = game_stats[i].food ? "On" : "Off";
				data[i][5] = game_stats[i].estimate_remaining_time;
				data[i][6] = game_stats[i].skip_empty_time ? "On" : "Off";
				data[i][7] = game_stats[i].strict_time ? "On" : "Off";
			}
			
			String[] column_names = {"Game Name","Players","Status","Bet","Food","Remaining Time","Fast Play","Strict Time"};
			games = new JTable(new MyTableModel(column_names,data));
			games.setPreferredScrollableViewportSize(new Dimension(660,325));
			games.setFillsViewportHeight(true);
			games.setRowSelectionAllowed(true);
			games.setColumnSelectionAllowed(false);
			games.setCellSelectionEnabled(false);
			games.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			games.setAutoCreateRowSorter(true);
			
			games_container = new JScrollPane(games);
		}
		else
			Probe(win,prober,probe_address);
		
		Cancel = new JButton("Cancel");
		Cancel.setPreferredSize(new Dimension(75,25));
		Cancel.setActionCommand("LAN_LOBBY:CANCEL");
		Cancel.setToolTipText("Return to the game type selection screen.");
		Cancel.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Cancel.setMargin(new Insets(0,0,0,0));
		Cancel.addActionListener(win);
		
		Join = new JButton("Join");
		Join.setPreferredSize(new Dimension(75,25));
		Join.setActionCommand("LAN_LOBBY:JOIN");
		Join.setToolTipText("Join the selected game.");
		Join.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Join.setMargin(new Insets(0,0,0,0));
		Join.addActionListener(win);
		
		Refresh = new JButton("Refresh");
		Refresh.setPreferredSize(new Dimension(75,25));
		Refresh.setActionCommand("LAN_LOBBY:REFRESH");
		Refresh.setToolTipText("Join the selected game.");
		Refresh.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Refresh.setMargin(new Insets(0,0,0,0));
		Refresh.addActionListener(win);
		
		Ping_Address = new JButton("Ping Address");
		Ping_Address.setPreferredSize(new Dimension(75,25));
		Ping_Address.setActionCommand("LAN_LOBBY:PING_ADDRESS");
		Ping_Address.setToolTipText("Refresh the table with data from a specified address.");
		Ping_Address.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Ping_Address.setMargin(new Insets(0,0,0,0));
		Ping_Address.addActionListener(win);
		
		Player_Stats = new JButton("Player Stats");
		Player_Stats.setPreferredSize(new Dimension(75,25));
		Player_Stats.setActionCommand("LAN_LOBBY:PLAYER_DATA");
		Player_Stats.setToolTipText("Display data from all players in the game.");
		Player_Stats.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Player_Stats.setMargin(new Insets(0,0,0,0));
		Player_Stats.addActionListener(win);
		
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		
		h1.addComponent(games_container,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addGap(1);
		h2.addComponent(Player_Stats,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(Refresh,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(Ping_Address,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(270);
		h2.addComponent(Join,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(Cancel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		Vertical.addContainerGap();
		Vertical.addComponent(games_container,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGap(2);
		
		v1.addComponent(Player_Stats,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Refresh,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Ping_Address,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Join,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Cancel,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		layout.setVerticalGroup(Vertical);
		
		win.setMinimumSize(new Dimension(700,450));
		win.setSize(new Dimension(700,450));
        win.setVisible(true);
		
		return;
	}
	
	/**
	 * Fills the game_stats array with fresh data and updates the table.
	 * Probably will require a manual repaint of the window.
	 */
	public static void Probe(Shell win, IO prober)
	{
		Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
		win.setCursor(hourglassCursor);
		
		for(int i = 1;i < 256;i++)
		{
			byte[] BTemp = null;
			
			prober.ChangeAddress("224.0.0." + i);
			prober.send(Data_Packer_Unpacker.PackPing());
			
			int j;
			
			for(j = 0;j < 10;j++)
			{
				try
				{
					Thread.sleep(10L);
				}
				catch(InterruptedException e)
				{}
				
				BTemp = prober.fetch();
				
				if(BTemp != null)
					if(Data_Packer_Unpacker.UnpackPingResponse(BTemp) != null)
						j = 10;
			}
			
			if(j == 10)
				continue;
			
			for(int l = 0;l < 6;l++)
			{
				if(BTemp == null)
				{
					l = 5;
					continue;
				}
				
				Object[] data = io.Data_Packer_Unpacker.UnpackPingResponse(BTemp);
				
				if(data == null)
					l--;
				else
				{
					if(l == 0)
						add_game_stat((Game_Status)data[1]);
					
					players[players.length-1][l] = (User)data[2];
					
					if(l == 5)
						prober.flush();
				}
				
				BTemp = prober.fetch();
			}
		}
		
		String[][] data = new String[game_stats.length][8]; 
		for(int i = 0;i < data.length;i++)
		{
			data[i][0] = game_stats[i].name;
			data[i][1] = new Integer(game_stats[i].players_left).toString();
			data[i][2] = game_stats[i].status;
			data[i][3] = Options.UnparseMoney(game_stats[i].bet);
			data[i][4] = game_stats[i].food ? "On" : "Off";
			data[i][5] = game_stats[i].estimate_remaining_time;
			data[i][6] = game_stats[i].skip_empty_time ? "On" : "Off";
			data[i][7] = game_stats[i].strict_time ? "On" : "Off";
		}
		
		String[] column_names = {"Game Name","Players","Status","Bet","Food","Remaining Time","Fast Play","Strict Time"};
		games = new JTable(new MyTableModel(column_names,data));
		games.setPreferredScrollableViewportSize(new Dimension(660,325));
		games.setFillsViewportHeight(true);
		games.setRowSelectionAllowed(true);
		games.setColumnSelectionAllowed(false);
		games.setCellSelectionEnabled(false);
		games.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		games.setAutoCreateRowSorter(true);
		
		games_container = new JScrollPane(games);
		
		Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		win.setCursor(normalCursor);
		
		return;
	}
	
	public static void Probe(Shell win, IO prober, String address)
	{
		Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
		win.setCursor(hourglassCursor);
		
		try
		{
			byte[] BTemp = null;
			InetAddress.getByName(address);
			
			prober.ChangeAddress(address);
			prober.send(Data_Packer_Unpacker.PackPing());
			
			int j;
			
			for(j = 0;j < 10;j++)
			{
				try
				{
					Thread.sleep(10L);
				}
				catch(InterruptedException e)
				{}
				
				BTemp = prober.fetch();
				
				if(BTemp != null)
					if(Data_Packer_Unpacker.UnpackPingResponse(BTemp) != null)
						j = 10;
			}
			
			if(j == 10)
			{
				game_stats = new Game_Status[0];
				players = new User[0][6];
			}
			else
			{
				for(int l = 0;l < 6;l++)
				{
					if(BTemp == null)
					{
						l = 5;
						continue;
					}
					
					Object[] data = io.Data_Packer_Unpacker.UnpackPingResponse(BTemp);
					
					if(data == null)
						l--;
					else
					{
						if(l == 0)
							add_game_stat((Game_Status)data[1]);
						
						players[players.length-1][l] = (User)data[2];
						
						if(l == 5)
							prober.flush();
					}
					
					BTemp = prober.fetch();
				}
			}
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null,"The given address was found to be...unreachable.","Error",JOptionPane.ERROR_MESSAGE);
		}
		
		String[][] data = new String[game_stats.length][8]; 
		for(int i = 0;i < data.length;i++)
		{
			data[i][0] = game_stats[i].name;
			data[i][1] = new Integer(game_stats[i].players_left).toString();
			data[i][2] = game_stats[i].status;
			data[i][3] = Options.UnparseMoney(game_stats[i].bet);
			data[i][4] = game_stats[i].food ? "On" : "Off";
			data[i][5] = game_stats[i].estimate_remaining_time;
			data[i][6] = game_stats[i].skip_empty_time ? "On" : "Off";
			data[i][7] = game_stats[i].strict_time ? "On" : "Off";
		}
		
		String[] column_names = {"Game Name","Players","Status","Bet","Food","Remaining Time","Fast Play","Strict Time"};
		games = new JTable(new MyTableModel(column_names,data));
		games.setPreferredScrollableViewportSize(new Dimension(660,325));
		games.setFillsViewportHeight(true);
		games.setRowSelectionAllowed(true);
		games.setColumnSelectionAllowed(false);
		games.setCellSelectionEnabled(false);
		games.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		games.setAutoCreateRowSorter(true);
		
		games_container = new JScrollPane(games);
		
		Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
		win.setCursor(normalCursor);
		
		return;
	}
	
	public static void add_game_stat(Game_Status stat)
	{
		int index = -1;
		
		for(int i = 0;i < game_stats.length;i++)
			if(stat.equals(game_stats[i]))
				index = i;
		
		if(index == -1)
		{
			Game_Status[] GTemp = new Game_Status[game_stats.length + 1];
			User[][] UTemp = new User[players.length + 1][6];
			
			System.arraycopy(game_stats,0,GTemp,0,game_stats.length);
			System.arraycopy(players,0,UTemp,0,players.length);
			
			GTemp[GTemp.length - 1] = stat;
			for(int i = 0;i < 6;i++)
				UTemp[UTemp.length - 1] = null;
			
			game_stats = GTemp;
			players = UTemp;
			
			players[players.length - 1] = new User[game_stats[game_stats.length - 1].players_left];
		}
		
		return;
	}
	
	public static void remove_game_stat(int j)
	{
		if(j > game_stats.length || j < 0)
			return;
		
		for(int i = 0;i < game_stats.length - 1;i++)
			if(i >= j)
			{
				game_stats[i] = game_stats[i+1];
				players[i] = players[i+1];
			}
		
		Game_Status[] GTemp = new Game_Status[game_stats.length - 1];
		User[][] UTemp = new User[players.length - 1][6];
		
		System.arraycopy(game_stats,0,GTemp,0,game_stats.length - 1);
		System.arraycopy(players,0,UTemp,0,players.length - 1);
		
		game_stats = GTemp;
		players = UTemp;
		
		return;
	}
	
	public static class MyTableModel extends AbstractTableModel
	{
		public MyTableModel(String[] column_names, Object[][] data)
		{
			columnNames = column_names;
			this.data = data;
			
			return;
		}
		
        public int getColumnCount()
        {
            return columnNames.length;
        }

        public int getRowCount()
        {
            return data.length;
        }

        public String getColumnName(int col)
        {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col)
        {
            return data[row][col];
        }

        public boolean isCellEditable(int row, int col)
        {
            return false;
        }
        
        protected String[] columnNames;
        protected Object[][] data;
    }
	
	public static User[][] players;
	public static Game_Status[] game_stats;
	
	public static JButton Player_Stats;
	public static JButton Ping_Address;
	public static JButton Refresh;
	public static JButton Join;
	public static JButton Cancel;
	public static JScrollPane games_container;
	public static JTable games;
}