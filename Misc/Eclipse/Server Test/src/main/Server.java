package main;

import java.io.*;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		//new ListenThread().start();
		//System.out.println("Starting first thread.");
		
		new MultiCastThread().start();
		System.out.println("Starting second thread.");
		
		return;
	}
}