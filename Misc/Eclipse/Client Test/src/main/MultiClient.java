package main;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiClient
{
	public static void main(String[] args) throws IOException
	{
		InetAddress address;
		MulticastSocket socket;
		DatagramPacket packet;
		socket = new MulticastSocket(4446);
		socket.joinGroup(InetAddress.getByName("224.0.0.1"));	//Lan goes from 224.0.0.1 to 224.0.0.255
																//Do not end with .0
		
		byte[] buf = new byte[256];
		packet = new DatagramPacket(buf,buf.length);
		
		socket.receive(packet);
		System.out.println("Quote of the Moment: " + new String(packet.getData(),0,packet.getLength()));
		
		socket.leaveGroup(InetAddress.getByName("224.0.0.1"));
		
		return;
	}
}
