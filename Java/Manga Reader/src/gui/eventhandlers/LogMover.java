package gui.eventhandlers;

import gui.Frame;
import gui.LogMoverDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JFrame;

public class LogMover implements ActionListener
{
	public LogMover()
	{
		mov = new LogMoverDialog();
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{		
		mov.setVisible(true);
		return;
	}

	protected LogMoverDialog mov;
}
