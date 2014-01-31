package io;

public class Storage
{
	public Storage(int packetsize)
	{
		size = packetsize;
		data = new byte[0][size];
		
		return;
	}
	
	/**
	 * Removes all data from the stack.
	 */
	public void flush()
	{
		data = new byte[0][size];
		
		return;
	}
	
	/**
	 * Adds a piece of data to the end of the stack.
	 */
	public void add(byte[] buf)
	{
		if(buf.length > size)
			return;
		
		byte[][] BTemp = new byte[data.length + 1][size];
		System.arraycopy(data,0,BTemp,0,data.length);
		System.arraycopy(buf,0,BTemp[BTemp.length - 1],0,buf.length);
		data = BTemp;
		
		return;
	}
	
	/**
	 * Adds a piece of data to the front of the stack.
	 */
	public void push(byte[] buf)
	{
		if(buf.length > size)
			return;
		
		byte[][] BTemp = new byte[data.length + 1][size];
		System.arraycopy(data,0,BTemp,1,data.length);
		System.arraycopy(buf,0,BTemp[0],0,buf.length);
		data = BTemp;
		
		return;
	}
	
	/**
	 * Removes a piece of data from the end of the stack.
	 */
	public byte[] remove()
	{
		if(data.length == 0)
			return null;
		
		byte[] BTemp = data[data.length - 1];
		byte[][] BBTemp = new byte[data.length - 1][size];
		System.arraycopy(data,0,BBTemp,0,BBTemp.length);
		data = BBTemp;
		
		return BTemp;
	}
	
	/**
	 * Removes a piece of data from the front of the stack.
	 */
	public byte[] pop()
	{
		if(data.length == 0)
			return null;
		
		byte[] BTemp = data[0];
		byte[][] BBTemp = new byte[data.length - 1][size];
		System.arraycopy(data,1,BBTemp,0,BBTemp.length);
		data = BBTemp;
		
		return BTemp;
	}
	
	protected byte[][] data;
	protected int size;
}
