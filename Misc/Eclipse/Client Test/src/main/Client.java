package main;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client extends Thread
{
	public static void main(String[] args) throws IOException
	{
		int port = 65535;
		InetAddress address;
		DatagramSocket socket;
		DatagramPacket packet;
		byte[] buf = new byte[256];
		
		args = new String[1];
		args[0] = "192.168.10.150";
		
		if(args.length!=1)
		{
			System.out.println("Usage: java QuoteClient <localhost>");
			return;
		}
		
		socket = new DatagramSocket(port);
		address = InetAddress.getByName(args[0]);
		packet = new DatagramPacket(buf,buf.length,address,port);
		
		socket.send(packet);
		
		packet = new DatagramPacket(buf,buf.length);
		socket.receive(packet);
		
		System.out.println("Quote of the Moment: " + new String(packet.getData(),0,packet.getLength()));
		
		return;
	}	
}
