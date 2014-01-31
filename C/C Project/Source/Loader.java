import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Loads an image file if necessary and guides users to loading
 * the image they desire.
 */
public class Loader extends Thread implements Observer
{
	/**
	 * Constructs this object.
	 */
	public Loader(ImageDisplay disp)
	{
		display = disp;
		return;
	}
	
	/**
	 * Lets us know that an event has occured.
	 */
	public void update(Observable where, Object memo)
	{
		// Check that we have a valid memo
		if(memo == null || !(memo.getClass() == String.class))
			return;
		
		// If we were told to load a file then do so
		if(((String)memo).equals("Load"))
		{
			// We'll select a file with this
			JFileChooser chooser = new JFileChooser();
			chooser.setMultiSelectionEnabled(false);
			
			// Get all of the images we can view
			String[] extensions = new String[ImageIO.getWriterFormatNames().length + 2];
			extensions[0] = "cs229";
			extensions[1] = "dat";
			for(int i = 0;i < ImageIO.getWriterFormatNames().length;i++)
				extensions[i + 2] = ImageIO.getWriterFormatNames()[i];
			
			// Create our filter
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",extensions);
			
			// Set the file filter
			chooser.setFileFilter(filter);
			
			// Show the dialog box
			chooser.showOpenDialog(null);
			
			// If we didn't choose anything then forget the rest of the function
			if(chooser.getSelectedFile() == null)
				return;
			
			// Get the file name
			String file = chooser.getSelectedFile().getAbsolutePath();
			
			// We need to see if we selected a standard file
			boolean standard = false;
			String[] standards = ImageIO.getWriterFormatNames();
			
			// Check if we got a standard file
			for(int i = 0;i < standards.length;i++)
				if(file.lastIndexOf('.') != -1 && standards[i].equals(file.substring(file.lastIndexOf('.') + 1)))
					standard = true;
			
			// Load the image differently based on what kind it is
			if(standard)
			{
				try
				{
					display.SetImage(ImageIO.read(new File(file)));
				}
				catch(IOException e)
				{
					System.err.println("The file you choose to view, " + file + ", could not be read.");
				}
			}
			else
				display.SetImage(new CS229BufferedImage(new CS229_Image(file)));
		}
		
		return;
	}
	
	/**
	 * This is where we will put the image.
	 */
	private ImageDisplay display;
}
