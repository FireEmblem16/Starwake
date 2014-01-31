import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Saves an image file if necessary and guides users to saving
 * the image where they desire.
 */
public class Saver extends Thread implements Observer
{
	/**
	 * Constructs this object.
	 */
	public Saver(ImageDisplay disp)
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
		
		// If we were told to save a file then do so
		if(((String)memo).equals("Save"))
		{
			// Check if we even have anything to save
			if(display.GetImage() == null)
				return;
			
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
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Kernel Files",extensions);
			
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
			
			// Write the image differently based on what kind it is
			if(standard)
			{
				try
				{
					ImageIO.write(display.GetImage(),file.substring(file.lastIndexOf('.') + 1),new File(file));
				}
				catch(IOException e)
				{
					System.err.println("Image could not be writen to " + file + ".");
				}
			}
			else
				((CS229BufferedImage)display.GetImage()).GetRawImage().Save(file);
		}
		
		return;
	}
	
	/**
	 * This is where we will get the image from.
	 */
	private ImageDisplay display;
}
