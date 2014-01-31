package main;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiCastThread extends ListenThread
{
	public MultiCastThread() throws IOException
	{
		super("MultiCast Server");
		
		return;
	}
	
	public void run()
	{
		try
		{
			socket = new MulticastSocket(4447);
		}
		catch(IOException e){}
		
		morequotes = true;
		
		while(morequotes)
		{
			try
			{
				byte[] buf = new byte[256];
				String response = null;
				
				if(in==null)
					response = new Date().toString();
				else
					response = GetNextQuote();
				
				buf=response.getBytes();
				
				DatagramPacket packet = new DatagramPacket(buf,buf.length,InetAddress.getByName("224.0.0.0"),4446);
				socket.send(packet);
				
				try
				{
	                sleep((long)5000);
	            }
				catch(InterruptedException e){}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				morequotes=false;
			}
		}
		
		socket.close();
		System.out.println("Terminating thread 2");
		
		return;
	}
	
	MulticastSocket socket;
}
