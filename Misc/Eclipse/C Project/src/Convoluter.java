import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Convolutes an image file if necessary and guides users to loading
 * the kernel they desire.
 */
public class Convoluter extends Thread implements Observer
{
	/**
	 * Constructs this object.
	 */
	public Convoluter(ImageDisplay disp)
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
		if(((String)memo).equals("Convolute"))
		{
			if(display.GetImage() instanceof CS229BufferedImage)
			{
				// We'll select a file with this
				JFileChooser chooser = new JFileChooser();
				chooser.setMultiSelectionEnabled(false);
				
				// Get all of the images we can view
				String[] extensions = new String[1];
				extensions[0] = "kernel";
				
				// Create our filter
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Kernel Files",extensions);
				
				// Set the file filter
				chooser.setFileFilter(filter);
				
				// Set the default directory
				chooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "../../Kernels"));
				
				// Show the dialog box
				chooser.showOpenDialog(null);
				
				// If we didn't choose anything then forget the rest of the function
				if(chooser.getSelectedFile() == null)
					return;
				
				// Get the file name
				String file = chooser.getSelectedFile().getAbsolutePath();
				
				// Get the image to convolute
				CS229_Image img = ((CS229BufferedImage)display.GetImage()).GetRawImage();
				
				// Do the convolution
				img.Convolute(file);
				((CS229BufferedImage)display.GetImage()).SetData();
			}
			else
				System.err.println("Convolution operations can only be preformed on cs229 images.");
		}
		
		return;
	}
	
	/**
	 * This is where we will put the image.
	 */
	private ImageDisplay display;
}
