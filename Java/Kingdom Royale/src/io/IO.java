package io;

public class IO
{
	public IO(String oname, String iname, int iport, int oport, String address, int packetsize)
	{
		in = new Reciever(iname,address,iport,packetsize);
		out = new Sender(oname,address,oport,iport,packetsize);
		
		in.start();
		out.start();
		
		return;
	}
	
	public void end()
	{
		in.end();
		in.stop();
		
		out.end();
		out.stop();
		
		return;
	}
	
	public void ChangeAddress(String adr)
	{
		in.ChangeAddress(adr);
		out.ChangeAddress(adr);
		
		return;
	}
	
	public void send(byte[] snd)
	{
		out.send(snd);
		
		return;
	}
	
	public void sendfast(byte[] snd)
	{
		out.sendfast(snd);
		
		return;
	}
	
	public byte[] fetch()
	{
		return in.fetch();
	}
	
	public byte[] fetchnewest()
	{
		return in.fetchnewest();
	}
	
	public void flush()
	{
		in.flush();
		out.flush();
		
		return;
	}
	
	protected Reciever in;
	protected Sender out;
}
