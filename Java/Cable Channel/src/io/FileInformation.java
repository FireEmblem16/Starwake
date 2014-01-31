package io;

import java.net.URL;

public class FileInformation
{
	public FileInformation(URL loc, boolean is_dir)
	{
		location = loc;
		directory = is_dir;
		
		plays = 0;
		weight = 0;
		
		return;
	}
	
	public boolean IsDir()
	{
		return directory;
	}
	
	public URL GetURL()
	{
		return location;
	}
	
	public String toString()
	{
		return location.toString().substring(location.toString().lastIndexOf('/') + 1);
	}
	
	public int plays;
	public int weight;
	
	private boolean directory;
	private URL location;
}