package main;

import java.io.*;
import java.net.*;
import java.util.*;

public class ListenThread extends Thread
{
	public ListenThread() throws IOException
	{
		this("Quote Server");
		
		return;
	}

	public ListenThread(String name) throws IOException
	{
		super(name);
		
		try
		{
			in = new BufferedReader(new FileReader("one-liners.txt"));
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Could not open quote file. Serving time instead.");
		}
		
		return;
	}
	
	public void run()
	{	
		try
		{
			socket = new DatagramSocket(65535);
		}
		catch(SocketException e){}
		morequotes = true;
		
		while(morequotes)
		{
			try
			{
				byte[] buf = new byte[256];
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				socket.receive(packet);
				
				String response = null;
				
				if(in==null)
					response = new Date().toString();
				else
					response = GetNextQuote();
				
				buf=response.getBytes();
				
				packet = new DatagramPacket(buf,buf.length,packet.getAddress(),packet.getPort());
				socket.send(packet);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				morequotes=false;
			}
		}
		
		socket.close();
		System.out.println("Terminating thread 1");
		
		return;
	}
	
	protected synchronized String GetNextQuote()
	{
		String rvalue = null;
		try
		{
			if((rvalue=in.readLine())==null)
			{
				in.close();
				//morequotes = false;
				rvalue = "No more quotes. Die!";

				try
				{
					in = new BufferedReader(new FileReader("one-liners.txt"));
				}
				catch(FileNotFoundException e)
				{
					System.err.println("Could not open quote file. Serving time instead.");
				} 
			}
		}
		catch(IOException e)
		{
			rvalue = "IOException in server.";
		}
		
		return rvalue;
	}
	
	protected boolean morequotes;
	protected volatile BufferedReader in;
	protected volatile DatagramSocket socket;
}
