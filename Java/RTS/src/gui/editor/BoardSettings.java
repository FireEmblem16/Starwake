package gui.editor;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import grid.Board;
import grid.Goal;
import gui.datadisplay.LayoutGroup;
import gui.editor.listener.BoardNameChanger;
import gui.editor.listener.GoalChanger;
import gui.editor.listener.PalletAdder;
import gui.editor.listener.PalletRemover;
import gui.editor.listener.VisibilityToggler;
import gui.imageselector.ImageSelector;

/**
 * Allows us to edit a board's hidden values like goal, name and pallets.
 */
public class BoardSettings extends JDialog
{
	/**
	 * Constructs and initializes this object.
	 */
	public BoardSettings()
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
		
		setSize(Math.min(300,dim.width / 4),Math.min(215,dim.height / 4));
		setLocation(getWidth() + dim.width / 10 > dim.width ? 0 : dim.width / 10,getHeight() + dim.height / 5 > dim.height ? 0 : dim.height / 10);
		
		setAlwaysOnTop(true);
		setTitle("Edit Board Settings");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		Construct(null);
		Display(null,null);
		setVisible(false);
		return;
	}
	
	/**
	 * Constructs the GUI elements of this window.
	 * We need [palletdisplayer] to update the pallets we have on our board.
	 */
	protected void Construct(ImageSelector palletdisplayer)
	{
		boardname_label = new JLabel("Board Name");
		goal_label = new JLabel("Goal");
		pallets_label = new JLabel("Pallets");
		
		boardname = new JTextField(disp == null ? "" : disp.GetName());
		boardname.setToolTipText("Hit enter to submit changes.");
		boardname.addKeyListener(new BoardNameChanger(disp));
		boardname.setPreferredSize(new Dimension(150,25));
		boardname.setEnabled(disp == null ? false : true);
		
		goal = new JComboBox(Goal.GetGoalNames());
		goal.setPreferredSize(new Dimension(150,25));
		goal.setSelectedItem("Rout");
		goal.setEnabled(disp == null ? false : true);
		
		timelimit = new JSpinner(new SpinnerNumberModel(disp == null ? 1 : (disp.GetGoal().length < 1 ? 1 : disp.GetGoal().length),1,Integer.MAX_VALUE,1));
		timelimit.setPreferredSize(new Dimension(40,20));
		timelimit.setVisible(disp == null ? false : disp.GetGoal().length != -1);
		timelimit.setEnabled(disp == null ? false : true);
		
		timelimit_on = new JCheckBox("Limit Turns");
		timelimit_on.setSelected(disp == null ? false : disp.GetGoal().length != -1);
		timelimit_on.setEnabled(disp == null ? false : true);
		
		Vector v = new Vector();
		
		if(disp != null)
		{
			for(int i = 0;i < disp.GetPallets().size();i++)
				v.add(disp.GetPallets().get(i).GetName());
			
			Object[] palletnames = v.toArray();
			Arrays.sort(palletnames);
			v.clear();
			
			for(int i = 0;i < palletnames.length;i++)
				v.add(palletnames[i]);
		}
		
		v.add(0,"No Pallet Selected");
		pallets = new JComboBox(v);
		pallets.setSelectedIndex(0);
		pallets.setEnabled(disp == null ? false : true);
		
		addpallet = new JButton("Add Pallet");
		addpallet.setPreferredSize(new Dimension(125,25));
		addpallet.setEnabled(disp == null ? false : true);
		
		removepallet = new JButton("Remove Pallet");
		removepallet.setPreferredSize(new Dimension(125,25));
		removepallet.setEnabled(disp == null ? false : true);
		
		if(disp != null)
		{
			goal.setSelectedItem(disp.GetGoal());
			goal.addActionListener(new GoalChanger(disp.GetGoal()));
			
			timelimit.addChangeListener(new GoalChanger(disp.GetGoal()));
			timelimit_on.addActionListener(new VisibilityToggler(disp.GetGoal(),timelimit));
			
			addpallet.addActionListener(new PalletAdder(pallets,disp,palletdisplayer));
			removepallet.addActionListener(new PalletRemover(pallets,disp,palletdisplayer));
		}
		
		return;
	}
	
	/**
	 * Update and display information on Board [b].
	 * We need [palletdisplayer] to update the pallets we have on our board.
	 */
	public void Display(Board b, ImageSelector palletdisplayer)
	{
		disp = b;
		Construct(palletdisplayer);
		
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
		Horizontal.addContainerGap();
		
		h2.addComponent(boardname_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(boardname,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addComponent(goal_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(goal,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h4.addComponent(timelimit_on,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h4.addGap(5);
		h4.addComponent(timelimit,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h4);
		
		h5.addComponent(pallets_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h5.addGap(5);
		h5.addComponent(pallets,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h5);
		
		h6.addComponent(addpallet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h6.addGap(5);
		h6.addComponent(removepallet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h6);
		
		Horizontal.addGroup(h1);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v4 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		ParallelGroup v5 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		Vertical.addContainerGap();
		
		v1.addComponent(boardname_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(boardname,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		
		Vertical.addGap(10);
		v2.addComponent(goal_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(goal,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		
		Vertical.addGap(5);
		v3.addComponent(timelimit_on,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v3.addComponent(timelimit,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v3);
		
		Vertical.addGap(5);
		v4.addComponent(pallets_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v4.addComponent(pallets,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v4);
		
		Vertical.addGap(10);
		v5.addComponent(addpallet,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v5.addComponent(removepallet,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v5);
		
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
	 * Brings all of the windows to the front of the screen with alwaysOnTop.
	 */
	public void WindowsToFront()
	{
		setAlwaysOnTop(true);
		return;
	}
	
	/**
	 * Sends all windows to the back of the screen with alwaysOnTop being set to false.
	 */
	public void WindowsToBack()
	{
		setAlwaysOnTop(false);
		return;
	}
	
	/**
	 * Disposes of this window and all windows it manages.
	 */
	public void dispose()
	{
		super.dispose();
		return;
	}
	
	/**
	 * This is the board we want to display information about.
	 */
	protected Board disp;
	
	/**
	 * Contains the scroll bars for this JDialog window.
	 */
	protected JScrollPane scrollbars;
	
	/**
	 * Contains a label that reads "Board Name".
	 */
	protected JLabel boardname_label;
	
	/**
	 * Contains the board's name.
	 */
	protected JTextField boardname;
	
	/**
	 * Contains a label that reads "Goal".
	 */
	protected JLabel goal_label;
	
	/**
	 * Contains the goal of the displayed board.
	 */
	protected JComboBox goal;
	
	/**
	 * Contains a checkbox that will be check if the board's goal has a turn limit.
	 */
	protected JCheckBox timelimit_on;
	
	/**
	 * Contains a spinner that allows us to set the turn limit of this board.
	 * It should only be visible if the turn limit feature is enabled.
	 */
	protected JSpinner timelimit;
	
	/**
	 * Contains a label that reads "Pallets".
	 */
	protected JLabel pallets_label;
	
	/**
	 * Contains a combobox that allows us to view the board's pallets and select pallets to remove.
	 */
	protected JComboBox pallets;
	
	/**
	 * When this button is pushed it should allow us to look for a pallet file to add to the board.
	 */
	protected JButton addpallet;
	
	/**
	 * When this button is pushed we should remove the currently selected pallet.
	 */
	protected JButton removepallet;
}
