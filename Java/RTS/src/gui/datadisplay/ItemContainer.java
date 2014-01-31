package gui.datadisplay;

import gui.datadisplay.listener.AnimationAdder;
import gui.datadisplay.listener.AnimationChanger;
import gui.datadisplay.listener.AnimationRemover;
import gui.datadisplay.listener.AnimationToggler;
import gui.datadisplay.listener.BooleanToggler;
import gui.datadisplay.listener.ImageDataChanger;
import gui.datadisplay.listener.IntChanger;
import item.Item;
import item.map.MapItem;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 * Contains gui information on a single item.
 */
public class ItemContainer extends Container
{
	/**
	 * Constructs and initializes this object.
	 */
	public ItemContainer(Item item)
	{
		super();
		
		Setup(item);
		return;
	}
	
	/**
	 * Initializes or updates the the contents of this container with [item]'s data.
	 */
	public void Setup(Item item)
	{
		if(item == null)
			return;
		
		disp = item;
		
		classtype = new JLabel("Class: " + Item.GetClassName(disp.getClass()) + "   Type: " + disp.GetType().toString());
		location = new JLabel("Location: " + disp.GetLocation().toString());
		
		if(disp instanceof MapItem)
		{
			MapItem mitem = (MapItem)disp;
			
			passable = new JCheckBox("Can Walk On");
			passable.setSelected(mitem.IsPassable());
			passable.addActionListener(new BooleanToggler(mitem,passable,"passable"));
			
			flyable = new JCheckBox("Can Fly Over");
			flyable.setSelected(mitem.IsFlyable());
			flyable.addActionListener(new BooleanToggler(mitem,flyable,"flyable"));
			
			swimable = new JCheckBox("Can Swim In");
			swimable.setSelected(mitem.IsSwimable());
			swimable.addActionListener(new BooleanToggler(mitem,swimable,"swimable"));
			
			climbable = new JCheckBox("Can Climb Over");
			climbable.setSelected(mitem.IsClimbable());
			climbable.addActionListener(new BooleanToggler(mitem,climbable,"climbable"));
		}
		
		animate = new JCheckBox("Animate");
		animate.setSelected(disp.GetInitialAnimation() != null);
		animate.addActionListener(new AnimationToggler(disp,animate,this));
		
		animation_label = new JLabel("Initial Animation");
		
		remover = new JButton("Remove");
		remover.setPreferredSize(new Dimension(90,25));
		
		adder = new JButton("Add Animation");
		adder.setPreferredSize(new Dimension(150,25));
		adder.addActionListener(new AnimationAdder(disp,this));
		
		palletlabel = new JLabel("Pallet Name ");
		palletnamelabel = new JLabel("Image Name");
		
		pallet = new JTextField(disp.GetPallet());
		pallet.addKeyListener(new ImageDataChanger(disp,pallet,"pallet"));
		pallet.setToolTipText("Hit enter to submit changes.");
		pallet.setPreferredSize(new Dimension(150,25));
		
		palletname = new JTextField(disp.GetPalletName());
		palletname.addKeyListener(new ImageDataChanger(disp,palletname,"pallet-name"));
		palletname.setToolTipText("Hit enter to submit changes.");
		palletname.setPreferredSize(new Dimension(150,25));
		
		animation = new JComboBox(disp.GetAnimationNames());
		animation.setSelectedItem(disp.GetInitialAnimation());
		
		remove = new JComboBox(disp.GetAnimationNames());
		remove.setSelectedIndex(0);
		
		animation.addActionListener(new AnimationChanger(disp,animation));
		remover.addActionListener(new AnimationRemover(disp,remove,this));
		
		if(disp instanceof MapItem)
		{
			MapItem mitem = (MapItem)disp;
			
			movelabel = new JLabel("Move Cost");
			avoidlabel = new JLabel("Avoid Bonus");
			deflabel = new JLabel("Defence Bonus");
			lifelabel = new JLabel("Life Gain");
			
			move = new JSpinner(new SpinnerNumberModel(mitem.GetCost(),-999,999,1));
			move.addChangeListener(new IntChanger(mitem,"cost"));
			move.setPreferredSize(new Dimension(40,20));
			
			avoidbonus = new JSpinner(new SpinnerNumberModel(mitem.GetAvoidBonus(),-999,999,1));
			avoidbonus.addChangeListener(new IntChanger(mitem,"avoid-bonus"));
			avoidbonus.setPreferredSize(new Dimension(40,20));
			
			defbonus = new JSpinner(new SpinnerNumberModel(mitem.GetDefenseBonus(),-999,999,1));
			defbonus.addChangeListener(new IntChanger(mitem,"def-bonus"));
			defbonus.setPreferredSize(new Dimension(40,20));
			
			lifegain = new JSpinner(new SpinnerNumberModel(mitem.GetLifeGain(),-999,999,1));
			lifegain.addChangeListener(new IntChanger(mitem,"life-gain"));
			lifegain.setPreferredSize(new Dimension(40,20));
		}
		
		return;
	}
	
	/**
	 * Updates all of the information of this container.
	 */
	public void Update()
	{
		classtype.setText("Class: " + Item.GetClassName(disp.getClass()) + "   Type: " + disp.GetType().toString());
		
		location.setText("Location: " + disp.GetLocation().toString());
		
		if(disp instanceof MapItem)
		{
			MapItem mitem = (MapItem)disp;
			
			passable.setSelected(mitem.IsPassable());
			flyable.setSelected(mitem.IsFlyable());
			swimable.setSelected(mitem.IsSwimable());
			climbable.setSelected(mitem.IsClimbable());
		}
			
		animate.setSelected(disp.GetInitialAnimation() != null);
		
		animation = new JComboBox(disp.GetAnimationNames());
		animation.setSelectedItem(disp.GetInitialAnimation());
		
		remove = new JComboBox(disp.GetAnimationNames());
		remove.setSelectedIndex(0);
		
		animation.addActionListener(new AnimationChanger(disp,animation));
		remover.addActionListener(new AnimationRemover(disp,remove,this));
		
		pallet.setText(disp.GetPallet());
		palletname.setText(disp.GetPalletName());
		
		if(disp instanceof MapItem)
		{
			MapItem mitem = (MapItem)disp;
			
			move.setValue(new Integer(mitem.GetCost()));
			avoidbonus.setValue(new Integer(mitem.GetAvoidBonus()));
			defbonus.setValue(new Integer(mitem.GetDefenseBonus()));
			lifegain.setValue(new Integer(mitem.GetLifeGain()));
		}
		
		return;
	}
	
	/**
	 * Returns the horizontal and vertical layout of this container.
	 */
	public LayoutGroup GetLayout(GroupLayout layout)
	{
		LayoutGroup ret = new LayoutGroup();
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		h1.addComponent(classtype,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(location,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		if(disp instanceof MapItem)
		{
			SequentialGroup h2 = layout.createSequentialGroup();
			SequentialGroup h3 = layout.createSequentialGroup();
			SequentialGroup h3_2 = layout.createSequentialGroup();
			SequentialGroup h4 = layout.createSequentialGroup();
			
			h3.addComponent(movelabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3.addGap(32);
			h3.addComponent(move,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3.addGap(5);
			h3.addComponent(avoidlabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3.addGap(5);
			h3.addComponent(avoidbonus,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h3);
			
			h4.addComponent(deflabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h4.addGap(5);
			h4.addComponent(defbonus,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h4.addGap(5);
			h4.addComponent(lifelabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h4.addGap(26);
			h4.addComponent(lifegain,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h4);
			
			h2.addComponent(passable,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h2.addGap(5);
			h2.addComponent(flyable,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h2);
			
			h3_2.addComponent(swimable,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3_2.addGap(8);
			h3_2.addComponent(climbable,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h3_2);
		}
		
		h1.addComponent(animate,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		if(animate.isSelected())
		{
			SequentialGroup h3 = layout.createSequentialGroup();
			SequentialGroup h4 = layout.createSequentialGroup();
			
			h3.addComponent(animation_label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3.addGap(5);
			h3.addComponent(animation,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h3);
			
			h4.addComponent(remover,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h4.addGap(5);
			h4.addComponent(remove,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h4);
			
			h1.addComponent(adder,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		}
		else
		{
			SequentialGroup h3 = layout.createSequentialGroup();
			SequentialGroup h4 = layout.createSequentialGroup();
			
			h3.addComponent(palletlabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h3.addGap(5);
			h3.addComponent(pallet,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h3);
			
			h4.addComponent(palletnamelabel,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h4.addGap(5);
			h4.addComponent(palletname,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			h1.addGroup(h4);
		}
		
		Horizontal.addGap(10);
		Horizontal.addGroup(h1);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
		
		Vertical.addComponent(classtype,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addComponent(location,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		if(disp instanceof MapItem)
		{
			ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			ParallelGroup v2_2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			
			Vertical.addGap(5);
			v2.addComponent(movelabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2.addComponent(move,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2.addComponent(avoidlabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2.addComponent(avoidbonus,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v2);
			
			Vertical.addGap(5);
			v3.addComponent(deflabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v3.addComponent(defbonus,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v3.addComponent(lifelabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v3.addComponent(lifegain,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v3);
			
			v1.addComponent(passable,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v1.addComponent(flyable,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v1);
			
			v2_2.addComponent(swimable,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2_2.addComponent(climbable,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v2_2);
		}
		
		Vertical.addComponent(animate,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		if(animate.isSelected())
		{
			ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			
			v2.addComponent(animation_label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2.addComponent(animation,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v2);
			
			v3.addComponent(remover,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v3.addComponent(remove,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGap(5);
			Vertical.addGroup(v3);
			
			Vertical.addGap(5);
			Vertical.addComponent(adder,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		}
		else
		{
			ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
			
			v2.addComponent(palletlabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v2.addComponent(pallet,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGroup(v2);
			
			v3.addComponent(palletnamelabel,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			v3.addComponent(palletname,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
			Vertical.addGap(5);
			Vertical.addGroup(v3);
		}
		
		ret.horizontal = Horizontal;
		ret.vertical = Vertical;
		
		return ret;
	}
	
	/**
	 * Returns true if this container has changed since the last time HasChanged was called.
	 */
	public boolean HasChanged()
	{
		if(changed)
		{
			changed = false;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Sets this container so that HasChanged will return true.
	 */
	public void SetChanged()
	{
		changed = true;
		return;
	}
	
	/**
	 * This is true if this container has changed since the last time its
	 * state of changedness...was checked.
	 */
	protected boolean changed;
	
	/**
	 * Contains the item we are displaying.
	 */
	protected Item disp;
	
	/**
	 * Contains a label representing what [disp]'s class and type are.
	 */
	protected JLabel classtype;
	
	/**
	 * Contains a label representing where [disp] is on a map.
	 */
	protected JLabel location;
	
	/**
	 * Contains a label that reads "Move Cost".
	 */
	protected JLabel movelabel;
	
	/**
	 * Contains the movement cost of this item.
	 */
	protected JSpinner move;
	
	/**
	 * Contains a label that reads "Avoid Bonus".
	 */
	protected JLabel avoidlabel;
	
	/**
	 * Contains the avoid bonus of this item.
	 */
	protected JSpinner avoidbonus;
	
	/**
	 * Contains a label that reads "Defence Bonus".
	 */
	protected JLabel deflabel;
	
	/**
	 * Contains the def bonus of this item.
	 */
	protected JSpinner defbonus;
	
	/**
	 * Contains a label that reads "Life Gain".
	 */
	protected JLabel lifelabel;
	
	/**
	 * Contains the life gain of this item.
	 */
	protected JSpinner lifegain;
	
	/**
	 * Contains a GUI representation of whether or not [disp] is passable.
	 */
	protected JCheckBox passable;
	
	/**
	 * Contains a GUI representation of whether or not [disp] is flyable.
	 */
	protected JCheckBox flyable;
	
	/**
	 * Contains a GUI representation of whether or not [disp] is swimable.
	 */
	protected JCheckBox swimable;
	
	/**
	 * Contains a GUI representation of whether or not [disp] is climbable.
	 */
	protected JCheckBox climbable;
	
	/**
	 * Contains a GUI representation of whether or not [disp] is flyable.
	 */
	protected JCheckBox animate;
	
	/**
	 * Contains a label that informs the user that the animation
	 * combo box changes the initial animation.
	 */
	protected JLabel animation_label;
	
	/**
	 * Contains all the animations of [disp] and the selected value
	 * is the animation that [disp] will start on.
	 */
	protected JComboBox animation;
	
	/**
	 * When this button is clicked we need to remove whatever is in [remove].
	 */
	protected JButton remover;
	
	/**
	 * This contains all of the animations of [disp] and we want to remove the selected
	 * one when the button [remover] is clicked.
	 */
	protected JComboBox remove;
	
	/**
	 * When this button is clicked we need to add an animation based on a file we choose
	 * from a JFileChooser.
	 */
	protected JButton adder;
	
	/**
	 * Contains a label that informs the user that the adjacent text field changes
	 * the pallet we will get our image from.
	 */
	protected JLabel palletlabel;
	
	/**
	 * Contains the name of the pallet we want to use for [disp].
	 */
	protected JTextField pallet;
	
	/**
	 * Contains a label that informs the user that the adjacent text field change
	 * the name of the image we want to fetch from the pallet.
	 */
	protected JLabel palletnamelabel;
	
	/**
	 * Contains the name of the image we want to use for [disp].
	 */
	protected JTextField palletname;
}
