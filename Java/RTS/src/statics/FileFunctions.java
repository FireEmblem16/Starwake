package statics;

import java.io.File;

/**
 * Gives us some fun file functions that java doesn't have.
 */
public class FileFunctions
{
	/**
	 * Returns the relative path to [file] from [base].
	 */
	public static String relative(File base, File file)
	{
		if(base == null || file == null)
			return null;
		
		int lastindex = base.getAbsolutePath().lastIndexOf('\\');
		
		if(lastindex == -1)
			lastindex = base.getAbsolutePath().lastIndexOf('/');
		
		if(lastindex == -1)
			lastindex = base.getAbsolutePath().length();
		
		return file.getAbsolutePath().substring(base.getAbsolutePath().substring(0,lastindex).length() + 1);
	}
}
