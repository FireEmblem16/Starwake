import zip.UnZip;

public class Grader
{
	public static void main(String[] args) throws Exception
	{
		ClassLoader loader = Grader.class.getClassLoader();
		
		try
		{
			Class<?> myclass = loader.loadClass("foo");
			System.out.println(myclass.getName());
		}
		catch(Exception e)
		{System.out.println("Oh noes!");}
		
		// Root directory is just above bin
		try
		{
			UnZip.unZipIt("test.zip","bin");
		}
		catch(Exception e)
		{System.out.println("What kind of world do we live in that this has happened?");}
		
		// Running javac on the LZEncryption class compiles everything it needs
		
		
		try
		{
			String str = LZEncryption.encode("Do not meddle in the affairs of wizards, for they are subtle and quick to anger.");
			System.out.println(str);
			System.out.println(ToBinary(str));
			System.out.println(LZEncryption.decode(str));
			
			str = LZEncryption.encode("https://www.google.com/search?sourceid=chrome-psyapi2&ion=1&espv=2&ie=UTF-8&q=nostalgia%20critic%20jurassic%20park");
			System.out.println(str);
			System.out.println(ToBinary(str));
			System.out.println(LZEncryption.decode(str));
		}
		catch(Exception e)
		{System.out.println("Someone did not handle their exceptions...");}
		
		return;
	}
	
	public static String ToBinary(String str)
	{
		final char[] masks = {0x8000,0x4000,0x2000,0x1000,0x800,0x400,0x200,0x100,0x80,0x40,0x20,0x10,0x8,0x4,0x2,0x1};
		String ret = "";
		
		for(int i = 0;i < str.length();i++)
		{
			char c = str.charAt(i);
			
			for(int j = 0;j < 16;j++)
				if((c & masks[j]) == 0)
					ret += "0";
				else
					ret += "1";
		}
		
		return ret;
	}
	
	public static String FromBinary(String str)
	{
		final char[] bits = {0x8000,0x4000,0x2000,0x1000,0x800,0x400,0x200,0x100,0x80,0x40,0x20,0x10,0x8,0x4,0x2,0x1};
		String ret = "";
		
		for(int i = 0;i < str.length();i += 16)
		{
			char c = 0x0000;
			
			for(int j = 0;j < 16;j++)
				if(str.charAt(i + j) == '1')
					c |= bits[j];
			
			ret += c;
		}
		
		return ret;
	}
}
