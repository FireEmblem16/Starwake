package Reader;

public class MetaData
{
	public MetaData(String name, String link, String type)
	{
		Name = name;
		Link = link;
		Type = type;
		this.Pages = 0;
		
		return;
	}
	
	public MetaData(String name, String link, String type, int Pages)
	{
		Name = name;
		Link = link;
		Type = type;
		this.Pages = Pages;
		
		return;
	}
	
	public String toString()
	{
		return Name;
	}
	
	public int Pages;
	public String Link;
	public String Name;
	public String Type;
}
