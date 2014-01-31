package io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Reciever extends Thread
{
	public Reciever(String name, String adr, int iport, int packetsize)
	{
		super(name);
		setDaemon(true);
		
		run = true;
		data = new Storage(packetsize);
		err = Error.NO_ERROR;
		
		while(err != Error.THIRD_FAIL && run)
		{
			try
			{
				address = InetAddress.getByName(adr);
				socket = new MulticastSocket(iport);
				socket.joinGroup(address);
				
				buf = new byte[packetsize];
				packet = new DatagramPacket(buf,buf.length);
				run = false;
			}
			catch(IOException e)
			{
				address = null;
				socket = null;
				buf = null;
				packet = null;
				
				switch(err)
				{
				case NO_ERROR:
					err = Error.FIRST_FAIL;
					break;
				case FIRST_FAIL:
					err = Error.SECOND_FAIL;
					break;
				case SECOND_FAIL:
					err = Error.THIRD_FAIL;
					break;
				default:
					err = Error.UNKNOWN_ERROR;
					break;
				}
				
				e.printStackTrace();
			}
		}
		
		if(err == Error.THIRD_FAIL)
			err = Error.SETUP_FAIL;
		else
			run = true;
		
		return;
	}
	
	public void ChangeAddress(String adr)
	{
		try
		{
			socket.leaveGroup(address);
			address = InetAddress.getByName(adr);
			socket.joinGroup(address);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public void run()
	{
		if(err == Error.SETUP_FAIL || err == Error.UNKNOWN_ERROR)
			end();
		
		while(run)
		{
			try
			{
				socket.receive(packet);
				data.add(packet.getData());
				
				err = Error.NO_ERROR;
			}
			catch(IOException e)
			{
				if(err == Error.THIRD_FAIL)
					end();
				
				switch(err)
				{
				case NO_ERROR:
					err = Error.FIRST_FAIL;
					break;
				case FIRST_FAIL:
					err = Error.SECOND_FAIL;
					break;
				case SECOND_FAIL:
					err = Error.THIRD_FAIL;
					break;
				default:
					err = Error.FIRST_FAIL;
					break;
				}
				
				e.printStackTrace();
			}
		}
		
		try
		{
			socket.leaveGroup(address);
		}
		catch(IOException e)
		{
			end();
		}
		
		return;
	}
	
	public void end()
	{
		run = false;
		data.flush();
		
		return;
	}
	
	public byte[] fetch()
	{
		return data.pop();
	}
	
	public byte[] fetchnewest()
	{
		return data.remove();
	}
	
	public void flush()
	{
		data.flush();
		
		return;
	}
	
	protected boolean run;
	protected byte[] buf;
	protected DatagramPacket packet;
	protected Error err;
	protected InetAddress address;
	protected MulticastSocket socket;
	protected Storage data;
}
