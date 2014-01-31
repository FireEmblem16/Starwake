package host_process;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.SpinnerListModel;

public class Shell extends JFrame
{
	public Shell(Main host)
	{
		super("Spider Bot");
		
		this.host = host;
		
		Base();
		return;
	}
	
	public void Base()
	{
		getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		Notice = new JLabel();
		Notice.setText("Welcome to the Spider Bot!");
		Notice.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		Time = new JLabel();
		Time.setText("");
		Time.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		JButton Destination = new JButton("Destination");
		Destination.setPreferredSize(new Dimension(75,25));
		Destination.setActionCommand("Change Destination");
		Destination.setToolTipText("Change the location the spider bot downloads to.");
		Destination.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Destination.setMargin(new Insets(0,0,0,0));
		Destination.addActionListener(host);
		
		JButton Update = new JButton("Update Now");
		Update.setPreferredSize(new Dimension(75,25));
		Update.setActionCommand("Update");
		Update.setToolTipText("Have the spider bot update immediately.");
		Update.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Update.setMargin(new Insets(0,0,0,0));
		Update.addActionListener(host);
		
		JButton Add = new JButton("Add");
		Add.setPreferredSize(new Dimension(75,25));
		Add.setActionCommand("Add");
		Add.setToolTipText("Add another manga to the spiderbot's list.");
		Add.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Add.setMargin(new Insets(0,0,0,0));
		Add.addActionListener(host);
		
		JButton Remove = new JButton("Remove");
		Remove.setPreferredSize(new Dimension(75,25));
		Remove.setActionCommand("Remove");
		Remove.setToolTipText("Remove a manga from the spiderbot's list.");
		Remove.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Remove.setMargin(new Insets(0,0,0,0));
		Remove.addActionListener(host);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
        
		Horizontal.addGap(10);
		h1.addComponent(Notice,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addComponent(Time,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		
		h2.addComponent(Destination,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(5);
		h2.addComponent(Update,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addComponent(Add,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(Remove,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		Horizontal.addGroup(h1);
        layout.setHorizontalGroup(Horizontal);
        
        SequentialGroup Vertical = layout.createSequentialGroup();
        ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        
        Vertical.addContainerGap();
        Vertical.addComponent(Notice,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        Vertical.addComponent(Time,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        
        v1.addComponent(Destination,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Update,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		
		v2.addComponent(Add,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Remove,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        
        layout.setVerticalGroup(Vertical);
        
		setMinimumSize(new Dimension(192,135));
        setSize(new Dimension(192,135));
        setVisible(true);
        
        return;
	}
	
	public void Add()
	{
		getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		JLabel Name_Label = new JLabel();
		Name_Label.setText("Manga Name:");
		Name_Label.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		Name = new JTextField();
		Name.setPreferredSize(new Dimension(172,25));
		Name.setText("");
		Name.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		JLabel Page_Label = new JLabel();
		Page_Label.setText("Manga Base Page:");
		Page_Label.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		Page = new JTextField();
		Page.setPreferredSize(new Dimension(172,25));
		Page.setText("");
		Page.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		JButton Continue = new JButton("Add");
		Continue.setPreferredSize(new Dimension(75,25));
		Continue.setActionCommand("Win_Add");
		Continue.setToolTipText("Add a new manga for the spider bot.");
		Continue.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Continue.setMargin(new Insets(0,0,0,0));
		Continue.addActionListener(host);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		SequentialGroup h2 = layout.createSequentialGroup();
		SequentialGroup h3 = layout.createSequentialGroup();
        
		Horizontal.addGap(10);
		
		h2.addComponent(Name_Label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h2.addGap(25);
		h2.addComponent(Name,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h2);
		
		h3.addComponent(Page_Label,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h3.addGap(5);
		h3.addComponent(Page,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		h1.addGroup(h3);
		
		h1.addComponent(Continue,GroupLayout.Alignment.TRAILING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Horizontal.addGroup(h1);
		
		layout.setHorizontalGroup(Horizontal);
        
        SequentialGroup Vertical = layout.createSequentialGroup();
        ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        
        Vertical.addContainerGap();
        
        v1.addComponent(Name_Label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Name,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		
		v2.addComponent(Page,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v2.addComponent(Page_Label,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v2);
		Vertical.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		
		Vertical.addComponent(Continue,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		layout.setVerticalGroup(Vertical);
        
		setMinimumSize(new Dimension(300,145));
        setSize(new Dimension(300,145));
        setVisible(true);
		
		return;
	}
	
	public void Remove()
	{
		getContentPane().removeAll();
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		FileReader file = null;
		BufferedReader in = null;
		String[] targets = null;
		
		try
		{
			file = new FileReader(new File("Fetch"));
			in = new BufferedReader(file);
			
			int i = Integer.parseInt(in.readLine());
			targets = new String[i];
			
			for(int j = 0;j < i;j++)
			{
				targets[j] = in.readLine();
				in.readLine();
			}
			
			in.close();
			file.close();
			
			if(i == 0)
			{
				targets = new String[1];
				targets[0] = "";
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		SpinnerListModel Manga = new SpinnerListModel(targets);
		
		Spinner = new JSpinner(Manga);
		Spinner.setToolTipText("Choose a manga to remove.");
		Spinner.setFont(new Font("Times New Roman",Font.PLAIN,12));
		
		JButton Continue = new JButton("Remove");
		Continue.setPreferredSize(new Dimension(75,25));
		Continue.setActionCommand("Win_Remove");
		Continue.setToolTipText("Remove a manga from the spider bot.");
		Continue.setFont(new Font("Times New Roman",Font.PLAIN,12));
		Continue.setMargin(new Insets(0,0,0,0));
		Continue.addActionListener(host);
		
		SequentialGroup Horizontal = layout.createSequentialGroup();
		
		Horizontal.addGap(10);
		Horizontal.addComponent(Spinner,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Horizontal.addGap(5);
		Horizontal.addComponent(Continue,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		layout.setHorizontalGroup(Horizontal);
		
		SequentialGroup Vertical = layout.createSequentialGroup();
	    ParallelGroup v1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
	    
	    Vertical.addContainerGap();
		
	    v1.addComponent(Spinner,GroupLayout.Alignment.CENTER,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		v1.addComponent(Continue,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
		Vertical.addGroup(v1);
	    
		layout.setVerticalGroup(Vertical);
        
		setMinimumSize(new Dimension(300,85));
        setSize(new Dimension(300,85));
        setVisible(true);
		
		return;
	}
	
	public String[] GetAdd()
	{
		String[] ret = new String[2];
		
		ret[0] = Name.getText();
		ret[1] = Page.getText();
		
		return ret; 
	}
	
	public String GetRemove()
	{
		return (String)Spinner.getValue();
	}
	
	public void SetNoticeText(String text)
	{
		Notice.setText(text);
		
		return;
	}
	
	public void SetNotice2Text(String text)
	{
		Time.setText(text);
		
		return;
	}
	
	protected JLabel Notice;
	protected JLabel Time;
	protected JSpinner Spinner;
	protected JTextField Name;
	protected JTextField Page;
	protected Main host;
}
