import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/** Makes the editor/viewer run.
 */
public class Host extends JFrame
{
	/**
	 * Displays images and such things.
	 */
	public Host()
	{
		// We don't want java running after we're done using it
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Intialize GUI
		InitializeGUI();
		
		// Create the GUI
		CreateAndDisplayGUI();
		
		// Give our icon some flare
		try
		{
			setIconImage(ImageIO.read(new File("icon.ico")));
		}
		catch(IOException e)
		{}
		
		// Set the basic settings of the JFrame
		setTitle("CS229 Image Viewer");
		setSize(628,695);
		setResizable(false);
		setVisible(true);
		
		return;
	}
	
	/**
	 * Initializes the GUI.
	 */
	private void InitializeGUI()
	{
		// Initialize our observation object
		observe = new Listener();
		
		// Create our ImageDisplay component.
		disp = new ImageDisplay(this);
		
		// Put the image in a scroll pane for big images
		scroll = new JScrollPane(disp);
		scroll.setLocation(5,5);
		scroll.setPreferredSize(new Dimension(603,603));
		
		// Create a button to allow users to load images
		load = new JButton("Load");
		load.addActionListener(observe);
		load.setPreferredSize(new Dimension(100,30));
		load.setActionCommand("Load");
		load.setToolTipText("Load a new image file.");
		load.setMnemonic(KeyEvent.VK_L);
		
		// Create a button to allow users to analize images
		convolute = new JButton("Convolute");
		convolute.addActionListener(observe);
		convolute.setPreferredSize(new Dimension(100,30));
		convolute.setActionCommand("Convolute");
		convolute.setToolTipText("Convolutes the current image file.");
		convolute.setMnemonic(KeyEvent.VK_C);
		
		// Create a button to allow users to save images
		save = new JButton("Save");
		save.setPreferredSize(new Dimension(100,30));
		save.setActionCommand("Save");
		save.setToolTipText("Save the current image file.");
		save.setMnemonic(KeyEvent.VK_S);
		save.addActionListener(observe);
		
		// Create some observers
		AddObserver(new Loader(disp));
		AddObserver(new Convoluter(disp));
		AddObserver(new Saver(disp));
		
		return;
	}
	
	/**
	 * Creates all of the GUI components and displays them on the screen.
	 */
	public void CreateAndDisplayGUI()
	{	
		// Work out the layout for the screen
		getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		// Create the necessary horizontal groups
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		
		// Add the image
		h1.addComponent(scroll,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		// Add the buttons
		h2.addGap(250);
		h2.addComponent(load,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(convolute,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(save,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		// Set the horizontal layout group
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		layout.setHorizontalGroup(Horizontal);
		
		// Create the necessary vertical groups
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		// Add the image
		Vertical.addContainerGap();
		Vertical.addComponent(scroll,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGap(10);
		
		// Add the buttons
		v1.addComponent(load,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(convolute,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(save,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		// Set the vertical layout group
		layout.setVerticalGroup(Vertical);
		
		return;
	}
	
	/**
	 * Does all the work of adding an observer.
	 */
	private void AddObserver(Thread observer)
	{
		// Only do this if we have an observing thread
		if(!(observer instanceof Observer))
			return;
		
		// Start the thread
		javax.swing.SwingUtilities.invokeLater(observer);
		
		// Add the observer
		observe.addObserver((Observer)observer);
		
		return;
	}
	
	/**
	 * Allows objects to "hear" events that this object notices.
	 */
	private Listener observe;
	
	/**
	 * This image we want to display.
	 */
	private ImageDisplay disp;
	
	/**
	 * We use this to view more of the picture than we otherwise would be able to.
	 */
	private JScrollPane scroll;
	
	/**
	 * This button will tell the program to load images.
	 */
	private JButton load;
	
	/**
	 * This button will apply a convolution to the image.
	 */
	private JButton convolute;
	
	/**
	 * This button will save an image.
	 */
	private JButton save;
}
