package gui.tool;

import grid.Board;
import grid.Location;
import gui.ImagePanel;
import gui.datadisplay.DataDisplay;
import gui.imageselector.ImageSelector;
import gui.tool.listener.AddMapListener;
import gui.tool.listener.CellDimensionListener;
import gui.tool.listener.ChangeMapListener;
import gui.tool.listener.DimensionListener;
import gui.tool.listener.HexListener;
import gui.tool.listener.RemoveMapListener;
import gui.tool.listener.ToolSelector;
import gui.tool.listener.ViewerListener;
import gui.tool.tools.EraseTool;
import gui.tool.tools.EyedropTool;
import gui.tool.tools.FillTool;
import gui.tool.tools.MoveTool;
import gui.tool.tools.PasteTool;
import gui.tool.tools.PencilTool;
import gui.tool.tools.SelectTool;
import gui.tool.tools.Tool;
import image.container.Pallet;
import item.Item;
import item.Type;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import engine.Engine;
import engine.Pointer;
import engine.RunnerTask;

/**
 * Allows users to pick tools to modify a map.
 */
public class ToolBox extends JDialog implements Runnable
{
	/**
	 * Constructs and initializes this JDialog.
	 */
	public ToolBox(Board board)
	{
		super();
		
		try
		{setIconImage(ImageIO.read(new File("icon.ico")));}
		catch(IOException e)
		{}
		
		scrollbars = new JScrollPane();
		getRootPane().getContentPane().removeAll();
		add(scrollbars);
		
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        
		setSize(Math.min(300,dim.width / 4),2 * dim.height / 5);
		setLocation(dim.width - getWidth() - (dim.width <= getWidth() + Math.min(300,dim.width / 4) + 100
				? (dim.width <= getWidth() + 100 ? 0 : 100) : Math.min(300,dim.width / 4) + 100),1 * dim.height / 10);
		
		setAlwaysOnTop(true);
		setTitle("Toolbox");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		Engine.GetBackgroundClock().schedule(new RunnerTask(this),100,100);
		
		dataviewer = new DataDisplay();
		dataviewer.Display(null);
		this.board = board;
		
		Construct(board.GetPallets());
		Display();
		
		setVisible(true);
		return;
	}
	
	/**
	 * Constructs all of the items in this JDialog.
	 */
	protected void Construct(ArrayList<Pallet> pallets)
	{
		selected_locations = new ArrayList<Location>();
		selected_center = new Pointer<Location>();
		
		layer_label = new JLabel("Layer");
		size_label = new JLabel("Size");
		
		Vector<String> v = new Vector();
		Type t = Type.GetNext(null);
		
		while(t != null)
		{
			v.add(t.toString());
			t = Type.GetNext(t);
		}
		
		v.add("All Layers");
		layer_selector = new JComboBox(v);
		
		size = new JSpinner(new SpinnerNumberModel(1,1,99,1));
		size.setPreferredSize(new Dimension(35,25));
		
		Class_Label = new JLabel("Class");
		Class = new JComboBox(Item.GetClassNames());
		Class.setSelectedItem("Flatland");
		
		image_selector = new ImageSelector();
		image_selector.Display(pallets);
		
		selected_tool_disp = new ImagePanel(null);
		selected_tool = new Pointer<Tool>();
		selected_tool.ptr = new SelectTool(dataviewer,selected_locations,selected_center,layer_selector,Class,size,"images/system/select.png");
		
		selector = new JButton(new ImageIcon("images/system/select.png"));
		selector.setToolTipText("Select map elements.");
		selector.setPreferredSize(new Dimension(45,26));
		selector.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,selected_tool.ptr));
		
		pencil = new JButton(new ImageIcon("images/system/pencil.png"));
		pencil.setToolTipText("Draw selected image on the map in selected layer.");
		pencil.setPreferredSize(new Dimension(45,26));
		pencil.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,new PencilTool(selected_locations,selected_center,layer_selector,Class,size,"images/system/pencil.png",image_selector)));
		
		eraser = new JButton(new ImageIcon("images/system/eraser.png"));
		eraser.setToolTipText("Erase items in selected layer.");
		eraser.setPreferredSize(new Dimension(45,26));
		eraser.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,new EraseTool(selected_locations,selected_center,layer_selector,Class,size,"images/system/eraser.png")));
		
		move = new JButton(new ImageIcon("images/system/move.png"));
		move.setToolTipText("Move selected items.");
		move.setPreferredSize(new Dimension(45,26));
		move.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,new MoveTool(selected_locations,selected_center,layer_selector,Class,size,"images/system/move.png")));
		
		paste = new JButton(new ImageIcon("images/system/paste.png"));
		paste.setToolTipText("Copy and pastes all selected items.");
		paste.setPreferredSize(new Dimension(45,26));
		paste.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,new PasteTool(selected_locations,selected_center,layer_selector,Class,size,"images/system/paste.png")));
		
		fill = new JButton(new ImageIcon("images/system/fill.png"));
		fill.setToolTipText("Paint all unbounded spaces around a single space.");
		fill.setPreferredSize(new Dimension(45,26));
		fill.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,new FillTool(selected_locations,selected_center,layer_selector,Class,size,"images/system/fill.png",image_selector)));
		
		eyedrop = new JButton(new ImageIcon("images/system/eyedrop.png"));
		eyedrop.setToolTipText("Select an image from the map.");
		eyedrop.setPreferredSize(new Dimension(45,26));
		eyedrop.addActionListener(new ToolSelector(selected_tool_disp,selected_tool,new EyedropTool(selected_locations,selected_center,layer_selector,Class,size,"images/system/eyedrop.png",image_selector)));
		
		view = new JButton(new ImageIcon("images/system/view.png"));
		view.setToolTipText("Open or close the image selector.");
		view.setPreferredSize(new Dimension(45,26));
		view.addActionListener(new ViewerListener(image_selector));
		
		dataview = new JButton(new ImageIcon("images/system/inspect.png"));
		dataview.setToolTipText("Open or close the item viewer.");
		dataview.setPreferredSize(new Dimension(45,26));
		dataview.addActionListener(new ViewerListener(dataviewer));
		
		width_label = new JLabel("Map Width");
		height_label = new JLabel("Map Height");
		
		width = new JSpinner(new SpinnerNumberModel(board.GetDisplayedMap() == null ? 1 : board.GetDisplayedMap().GetWidth(),1,9999,1));
		width.setPreferredSize(new Dimension(50,25));
		width.addChangeListener(new DimensionListener(board,true));
		
		height = new JSpinner(new SpinnerNumberModel(board.GetDisplayedMap() == null ? 1 : board.GetDisplayedMap().GetHeight(),1,9999,1));
		height.setPreferredSize(new Dimension(50,25));
		height.addChangeListener(new DimensionListener(board,false));
		
		cellwidth_label = new JLabel("Cell Width");
		cellheight_label = new JLabel("Cell Height");
		
		cellwidth = new JSpinner(new SpinnerNumberModel(board.GetCellWidth(),0,999,1));
		cellwidth.setPreferredSize(new Dimension(40,25));
		cellwidth.addChangeListener(new CellDimensionListener(board,true));
		
		cellheight = new JSpinner(new SpinnerNumberModel(board.GetCellHeight(),0,999,1));
		cellheight.setPreferredSize(new Dimension(40,25));
		cellheight.addChangeListener(new CellDimensionListener(board,false));
		
		ishex = new JCheckBox("Draw as Hexgrid");
		ishex.setSelected(board.GetDisplayedMap() == null ? false : board.GetDisplayedMap().IsHex());
		ishex.addActionListener(new HexListener(board));
		
		maplabel = new JLabel("Displayed/Initial Map");
		
		v = new Vector<String>(board.GetMapNames());
		v.add(0,"No Map Selected");
		
		selectmap = new JComboBox(v);
		selectmap.setPreferredSize(new Dimension(125,25));
		selectmap.addActionListener(new ChangeMapListener(board,ishex,width,height,cellwidth,cellheight));
		
		if(board.GetDisplayedMapName() == null)
			selectmap.setSelectedIndex(0);
		else
			selectmap.setSelectedItem(board.GetDisplayedMapName());
		
		addmap = new JButton("Add Map");
		addmap.setPreferredSize(new Dimension(110,25));
		addmap.addActionListener(new AddMapListener(board,selectmap,width,height));
		
		removemap = new JButton("Remove Map");
		removemap.setPreferredSize(new Dimension(110,25));
		removemap.addActionListener(new RemoveMapListener(board,selectmap));
		
		return;
	}
	
	/**
	 * Draws this toolbox.
	 */
	public void Display()
	{
		if(selected_tool.ptr != null)
			selected_tool_disp.ChangeImage(selected_tool.ptr.GetImageRepresentation());
		else
			selected_tool_disp.ChangeImage(null);
		
		Container panel = new Container();
		
		panel.removeAll();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
		SequentialGroup h4 = layout.createSequentialGroup();
		SequentialGroup h5 = layout.createSequentialGroup();
		SequentialGroup h6 = layout.createSequentialGroup();
		SequentialGroup h7 = layout.createSequentialGroup();
		SequentialGroup h8 = layout.createSequentialGroup();
		SequentialGroup h9 = layout.createSequentialGroup();
		Horizontal.addContainerGap();
		
		h2.addComponent(selector,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(55);
		h2.addComponent(pencil,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(eraser,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(view,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addComponent(move,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(paste,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(fill,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(eyedrop,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(dataview,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(layer_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(layer_selector,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(size_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(size,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		h5.addComponent(Class_Label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(5);
		h5.addComponent(Class,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(30);
		h5.addComponent(selected_tool_disp,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h5);
		
		h6.addComponent(width_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h6.addGap(5);
		h6.addComponent(width,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h6.addGap(10);
		h6.addComponent(height_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h6.addGap(5);
		h6.addComponent(height,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h6);
		
		h7.addComponent(cellwidth_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h7.addGap(8);
		h7.addComponent(cellwidth,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h7.addGap(10);
		h7.addComponent(cellheight_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h7.addGap(8);
		h7.addComponent(cellheight,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h7);
		
		h1.addComponent(ishex,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h8.addComponent(maplabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h8.addGap(5);
		h8.addComponent(selectmap,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h8);
		
		h9.addComponent(addmap,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h9.addGap(8);
		h9.addComponent(removemap,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h9);
		
		Horizontal.addGroup(h1);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v4 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v5 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v6 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v7 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v8 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		Vertical.addContainerGap();
		
		v1.addComponent(selector,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(pencil,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(eraser,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(view,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addGap(5);
		
		v2.addComponent(move,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(paste,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(fill,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(eyedrop,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(dataview,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		Vertical.addGap(5);
		
		v3.addComponent(layer_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(layer_selector,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(size_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(size,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v3);
		
		Vertical.addGap(5);
		
		v4.addComponent(Class_Label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v4.addComponent(Class,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v4.addComponent(selected_tool_disp,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v4);
		
		Vertical.addGap(20);
		
		v5.addComponent(width_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v5.addComponent(width,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v5.addComponent(height_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v5.addComponent(height,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v5);
		
		Vertical.addGap(5);
		
		v6.addComponent(cellwidth_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v6.addComponent(cellwidth,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v6.addComponent(cellheight_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v6.addComponent(cellheight,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v6);
		
		Vertical.addGap(5);
		
		v7.addComponent(maplabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v7.addComponent(selectmap,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v7);
		
		Vertical.addGap(5);
		Vertical.addComponent(ishex,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGap(5);
		
		v8.addComponent(addmap,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v8.addComponent(removemap,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v8);
		
		layout.setHorizontalGroup(Horizontal);
		layout.setVerticalGroup(Vertical);
		
		int X = scrollbars.getHorizontalScrollBar().getValue();
		int Y = scrollbars.getVerticalScrollBar().getValue();
		
		scrollbars.getViewport().removeAll();
		scrollbars.getViewport().add(panel);
		
		scrollbars.getHorizontalScrollBar().setValue(X);
		scrollbars.getVerticalScrollBar().setValue(Y);
		
		return;
	}
	
	/**
	 * If [selected_tool_disp]'s image has changed then we need to repaint.
	 */
	public void run()
	{
		if(selected_tool_disp != null && selected_tool_disp.HasChanged())
			repaint();
		
		return;
	}
	
	/**
	 * Returns the currently selected tool.
	 */
	public Tool GetSelectedTool()
	{
		return selected_tool.ptr;
	}
	
	/**
	 * Returns a list of the last selected locations.
	 * Returns an empty array list if no locations are selected.
	 */
	public ArrayList<Location> GetSelectedLocations()
	{
		return selected_locations;
	}
	
	/**
	 * Returns the window this toolbox displays item data on.
	 */
	public DataDisplay GetDataDisplay()
	{
		return dataviewer;
	}
	
	/**
	 * Returns the window this toolbox uses to select images and pallets to use for its tools.
	 */
	public ImageSelector GetImageSelector()
	{
		return image_selector;
	}
	
	/**
	 * Brings all of the windows to the front of the screen with alwaysOnTop.
	 */
	public void WindowsToFront()
	{
		setAlwaysOnTop(true);
		image_selector.WindowsToFront();
		dataviewer.WindowsToFront();
		
		return;
	}
	
	/**
	 * Sends all windows to the back of the screen with alwaysOnTop being set to false.
	 */
	public void WindowsToBack()
	{
		setAlwaysOnTop(false);
		image_selector.WindowsToBack();
		dataviewer.WindowsToBack();
		
		return;
	}
	
	/**
	 * Returns the window selector of this toolbox.
	 */
	public ImageSelector RequestImageSelector()
	{
		return image_selector;
	}
	
	/**
	 * Disposes of this window and all windows it manages.
	 */
	public void dispose()
	{
		super.dispose();
		
		if(image_selector != null)
			image_selector.dispose();
		
		if(dataviewer != null)
			dataviewer.dispose();
		
		return;
	}
	
	/**
	 * Contains the scroll bars for this JDialog window.
	 */
	protected JScrollPane scrollbars;

	/**
	 * When this button is pushed it will change the current tool to a
	 * device which selects items from the map currently being edited.
	 */
	protected JButton selector;
	
	/**
	 * When this button is pushed it will change the current tool to a
	 * pencil that will draw the currently selected image.
	 */
	protected JButton pencil;
	
	/**
	 * When this button is pushed it will change the current tool to an
	 * eraser that erases the selected layer.
	 */
	protected JButton eraser;
	
	/**
	 * When this button is pushed it will change the current tool to a
	 * hand that will move the current selection.
	 */
	protected JButton move;
	
	/**
	 * When this button is pushed it will change the current tool to a
	 * brush that will paste the current selection.
	 */
	protected JButton paste;
	
	/**
	 * When this button is pushed it will change the current tool to a
	 * paint bucket that will fill a space until it comes into contact
	 * with something already in the same layer. Does not try diagonally.
	 */
	protected JButton fill;
	
	/**
	 * When this tool is used it will select the image in the current layer.
	 * If the current layer is "All Layers" then it will select the first layer that has an image.
	 */
	protected JButton eyedrop;
	
	/**
	 * When this button is pushed it will show or hide the image selector.
	 */
	protected JButton view;
	
	/**
	 * When this button is pushed it will show or hide the data viewer.
	 */
	protected JButton dataview;
	
	/**
	 * Contains a label that says "Layer".
	 */
	protected JLabel layer_label;
	
	/**
	 * Contains the layer we currently want to paint to, including the
	 * layer "All Layers".
	 */
	protected JComboBox layer_selector;
	
	/**
	 * Contains a label that says "Size".
	 */
	protected JLabel size_label;
	
	/**
	 * This contains a number indicating how big a square we want to draw or erase.
	 */
	protected JSpinner size;
	
	/**
	 * Contains an image that displays the selected tool.
	 */
	protected ImagePanel selected_tool_disp;
	
	/**
	 * This contains a way for the user to select images through a GUI.
	 */
	protected ImageSelector image_selector;
	
	/**
	 * Contains a label that reads "Class".
	 */
	protected JLabel Class_Label;
	
	/**
	 * Contains the class of item we want to work with.
	 */
	protected JComboBox Class;
	
	/**
	 * Contains the currently selected tool.
	 */
	protected Pointer<Tool> selected_tool;
	
	/**
	 * Contains all locations last selected with the select tool.
	 */
	protected ArrayList<Location> selected_locations;
	
	/**
	 * Contains the center of the selected locations.
	 */
	protected Pointer<Location> selected_center;
	
	/**
	 * Conatins an JDialog which allows us to view data.
	 */
	protected DataDisplay dataviewer;
	
	/**
	 * Contains a label that says "Width".
	 */
	protected JLabel width_label;
	
	/**
	 * This contains a number indicating how wide the map is.
	 */
	protected JSpinner width;
	
	/**
	 * Contains a label that says "Height".
	 */
	protected JLabel height_label;
	
	/**
	 * This contains a number indicating how tall the map is.
	 */
	protected JSpinner height;
	
	/**
	 * Contains a label that says "Cell Width".
	 */
	protected JLabel cellwidth_label;
	
	/**
	 * This contains a number indicating how wide the map cells are.
	 */
	protected JSpinner cellwidth;
	
	/**
	 * Contains a label that says "Cell Height".
	 */
	protected JLabel cellheight_label;
	
	/**
	 * This contains a number indicating how tall the map cells are.
	 */
	protected JSpinner cellheight;
	
	/**
	 * This will control the current map's option to be displayed as a hex grid or square grid.
	 */
	protected JCheckBox ishex;
	
	/**
	 * Reads "Displayed Map".
	 */
	protected JLabel maplabel;
	
	/**
	 * When this button is pushed we need to add a new map to [board].
	 */
	protected JButton addmap;
	
	/**
	 * When this button is pushed we need to remove the currently selected map.
	 */
	protected JButton removemap;
	
	/**
	 * This controls what map we are currently viewing.
	 */
	protected JComboBox selectmap;
	
	/**
	 * Contains the board we are editing.
	 */
	protected Board board;
}
