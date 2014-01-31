package image;

import java.awt.Image;
import java.io.IOException;

/**
 * Handles and manages a set of images for convinient use.
 */
public class ImageManager
{
	public ImageManager(String PathQualifier)
	{
		Data = new ImageFoil[0];
		QualifiedName = PathQualifier;
		
		if(!QualifiedName.substring(QualifiedName.length() - 1).equals("/"))
			QualifiedName += "/";
		
		return;
	}
	
	/**
	 * Adds a single image file called name.
	 */
	public boolean add(String name)
	{
		try
		{
			ImageFoil ITemp = new ImageFoil(QualifiedName,name);
			IncArray();
			Data[Data.length - 1] = ITemp;
		}
		catch(IOException e)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Adds all image files in <b>list</b>.
	 * Returns the number of images actually added.
	 */
	public int add(String[] list)
	{
		int ret = list.length;
		
		for(int i = 0;i < list.length;i++)
		{
			try
			{
				ImageFoil ITemp = new ImageFoil(QualifiedName,list[i]);
				IncArray();
				Data[Data.length - 1] = ITemp;
			}
			catch(IOException e)
			{
				ret--;
			}
		}
		
		return ret;
	}
	
	/**
	 * Indexes are 0 based.
	 */
	public Image GetImage(int i)
	{
		if(i >= 0 && i < Data.length)
			return Data[i].img;
		
		return null;
	}
	
	public Image GetImage(String ImgName)
	{
		for(int i = 0;i < Data.length;i++)
			if(Data[i].Name.equals(ImgName) || Data[i].Path.equals(ImgName))
				return Data[i].img;
			
		return null;
	}
	
	/**
	 * Indexes are 0 based.
	 */
	public ImageFoil GetImageFoil(int i)
	{
		if(i >= 0 && i < Data.length)
			return Data[i];
		
		return null;
	}
	
	public ImageFoil GetImageFoil(String ImgName)
	{
		for(int i = 0;i < Data.length;i++)
			if(Data[i].Name.equals(ImgName) || Data[i].Path.equals(ImgName))
				return Data[i];
			
		return null;
	}
	
	/**
	 * Indexes are 0 based.
	 */
	public String GetImageName(int i)
	{
		if(i >= 0 && i < Data.length)
			return Data[i].Name;
		
		return null;
	}
	
	/**
	 * Indexes are 0 based.
	 */
	public void MoveImage(int i, int x, int y)
	{
		if(i >= 0 && i < Data.length)
			Data[i].SetPosition(x,y);
		
		return;
	}
	
	public void MoveImage(String ImgName, int x, int y)
	{
		for(int i = 0;i < Data.length;i++)
			if(Data[i].Name.equals(ImgName) || Data[i].Path.equals(ImgName))
				Data[i].SetPosition(x,y);
			
		return;
	}
	
	/**
	 * Removes a single image file called name.
	 */
	public boolean remove(String name)
	{
		for(int i = 0;i < Data.length;i++)
			if(Data[i].Name.equals(name) || Data[i].Path.equals(name))
			{
				for(int j = i;j < Data.length - 1;j++)
					Data[j] = Data[j+1];
				
				DecArray();
				
				return true;
			}
		
		return false;
	}
	
	/**
	 * Adds all image files in <b>list</b>.
	 * Returns the number of images actually added.
	 */
	public int remove(String[] list)
	{
		int ret = 0;
		
		for(int l = 0;l < list.length;l++)
			for(int i = 0;i < Data.length;i++)
				if(Data[i].Name.equals(list[l]) || Data[i].Path.equals(list[l]))
				{
					for(int j = i;j < Data.length - 1;j++)
						Data[j] = Data[j+1];
					
					DecArray();
					i = Data.length;
					ret++;
				}
		
		return ret;
	}
	
	/**
	 * Indexes are 0 based.
	 */
	public void ResizeImage(int i, int width, int height)
	{
		if(i >= 0 && i < Data.length)
			Data[i].SetDimensions(width,height);
		
		return;
	}
	
	public void ResizeImage(String ImgName, int width, int height)
	{
		for(int i = 0;i < Data.length;i++)
			if(Data[i].Name.equals(ImgName) || Data[i].Path.equals(ImgName))
				Data[i].SetDimensions(width,height);
			
		return;
	}
	
	public void SetQualifier(String path)
	{
		QualifiedName = path;
		
		return;
	}
	
	public String toString()
	{
		String ret = "";
		
		for(int i = 0;i < Data.length;i++)
			ret += Data[i] + "\n";
		
		ret = ret.substring(0,ret.length() - 1);
		
		return ret;
	}
	
	protected void DecArray()
	{
		ImageFoil[] ITemp = new ImageFoil[Data.length - 1];
		System.arraycopy(Data,0,ITemp,0,Data.length - 1);
		Data = ITemp;
		
		return;
	}
	
	protected void IncArray()
	{
		ImageFoil[] ITemp = new ImageFoil[Data.length + 1];
		System.arraycopy(Data,0,ITemp,0,Data.length);
		ITemp[ITemp.length - 1] = null;
		Data = ITemp;
		
		return;
	}
	
	protected ImageFoil[] Data;
	protected String QualifiedName;
}
