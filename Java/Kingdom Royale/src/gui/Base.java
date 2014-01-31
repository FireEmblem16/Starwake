package gui;

import host_process.Shell;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

public class Base
{
	public static void create(Shell win)
	{
		win.getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(win.getContentPane());
		win.getContentPane().setLayout(layout);
		
        LAN = new JButton("LAN");
        LAN.setPreferredSize(new Dimension(100,25));
        LAN.setActionCommand("BASE:LAN");
        LAN.setToolTipText("Create or join a LAN game.");
        LAN.setFont(new Font("Times New Roman",Font.PLAIN,10));
        LAN.setMargin(new Insets(0,0,0,0));
        LAN.addActionListener(win);
        
        Hotseat = new JButton("Hotseat");
        Hotseat.setPreferredSize(new Dimension(100,25));
        Hotseat.setActionCommand("BASE:HOTSEAT");
        Hotseat.setToolTipText("Create a hotseat game on this computer.");
        Hotseat.setFont(new Font("Times New Roman",Font.PLAIN,10));
        Hotseat.setMargin(new Insets(0,0,0,0));
        Hotseat.addActionListener(win);
        
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        
        h1.addComponent(LAN,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h1.addComponent(Hotseat,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        
        Horizontal.addGap(50);
        Horizontal.addGroup(h1);
        layout.setHorizontalGroup(Horizontal);
        
        SequentialGroup Vertical = layout.createSequentialGroup();
        
        Vertical.addContainerGap();
        Vertical.addComponent(LAN,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        Vertical.addComponent(Hotseat,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        
        layout.setVerticalGroup(Vertical);
        
        win.setMinimumSize(new Dimension(220,135));
        win.setSize(new Dimension(220,135));
        win.setVisible(true);
        
		return;
	}
	
	public static JButton LAN;
	public static JButton Hotseat;
}
