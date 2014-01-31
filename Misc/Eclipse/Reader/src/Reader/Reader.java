package Reader;

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Reader extends JFrame implements ActionListener, TreeSelectionListener, ItemListener, MouseListener
{
	public static void main(String[] Args)
	{
		Reader application = null;
		
		try
		{
			application = new Reader(800,600);
		}
		catch(IOException e){}
		
		Shutdown ShutdownHook = new Shutdown(application);
		Runtime.getRuntime().addShutdownHook(ShutdownHook);
		
		application.Run();
		
		return;
	}
	
	public Reader(int winW, int winH) throws IOException
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//When creating an installer make sure to deal with this.
		//Occurs only once in the binary files. Edit directly.
		CurrentLink = null;
		CurrentPage = 0;
		QualifiedName = "C:/Users/FireEmblem16/Documents/Workbench/Reader/bin/";
		mode = 1;
		
		Tree = new JTree();
		
		top = new DefaultMutableTreeNode(new MetaData("Yggdrassil","","Root"));
	    CreateNodes(top,new Scanner(new File(QualifiedName + "Lists/Books.list")));
	    Tree = new JTree(top);
	    Tree.putClientProperty("JTree.lineStyle","Angled");
	    Tree.setRootVisible(false);
	    
	    Tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	    Tree.addTreeSelectionListener(this);
	    Tree.addMouseListener(this);
	    
	    TreePopup = new JPopupMenu();
	    
	    JMenuItem MI = new JMenuItem("New Folder");
	    MI.addActionListener(this);
	    MI.setActionCommand("New Folder");
	    TreePopup.add(MI);
	    
	    MI = new JMenuItem("New Shortuct");
	    MI.addActionListener(this);
	    MI.setActionCommand("New Link");
	    TreePopup.add(MI);
	    
	    MI = new JMenuItem("New Bookmark");
	    MI.addActionListener(this);
	    MI.setActionCommand("New Bookmark");
	    TreePopup.add(MI);
	    
	    TreePopup.addSeparator();
	    
	    MI = new JMenuItem("Copy");
	    MI.addActionListener(this);
	    MI.setActionCommand("Copy");
	    TreePopup.add(MI);
	    
	    MI = new JMenuItem("Cut");
	    MI.addActionListener(this);
	    MI.setActionCommand("Cut");
	    TreePopup.add(MI);
	    
	    MI = new JMenuItem("Paste");
	    MI.addActionListener(this);
	    MI.setActionCommand("Paste");
	    TreePopup.add(MI);
	    
	    TreePopup.addSeparator();
	    
	    MI = new JMenuItem("Relink");
	    MI.addActionListener(this);
	    MI.setActionCommand("Relink");
	    TreePopup.add(MI);
	    
	    MI = new JMenuItem("Delete");
	    MI.addActionListener(this);
	    MI.setActionCommand("Delete");
	    TreePopup.add(MI);
	    
	    MI = new JMenuItem("Rename");
	    MI.addActionListener(this);
	    MI.setActionCommand("Rename");
	    TreePopup.add(MI);
	    
	    TreePopup.addSeparator();
	    
	    MI = new JMenuItem("Properties");
	    MI.addActionListener(this);
	    MI.setActionCommand("Properties");
	    TreePopup.add(MI);
	    
	    ImageIcon Book = new ImageIcon(QualifiedName + "Book.jpg");
	    ImageIcon Link = new ImageIcon(QualifiedName + "Green Arrow.jpg");
	    ImageIcon Bookmark = new ImageIcon(QualifiedName + "Bookmark.jpg");
	    ImageIcon Folder = new ImageIcon(QualifiedName + "Folder.jpg");
	    Tree.setCellRenderer(new MyRenderer(Book,Link,Bookmark,Folder));
		
	    TreeView = new JScrollPane(Tree);
	    TreeView.setPreferredSize(new Dimension(300,300));
	    TreeView.setEnabled(true);
		
		CreateMenu();
		CreateFramework();
		
        setLocation(0,0);
		setSize(winW,winH);
		setMinimumSize(new Dimension(550,300));
		setResizable(true);
		setVisible(true);
		
		return;
	}
	
	public void finalize()
	{
		FileOutputStream out;
		PrintStream fout;
		
		try
		{
			out = new FileOutputStream(QualifiedName + "Lists/Books.list");
			fout = new PrintStream(out);
		}
		catch(IOException e)
		{
			return;
		}
		
		Print(top,fout);
		fout.println("End");
		
		fout.close();
		
		return;
	}
	
	public void Print(DefaultMutableTreeNode node, PrintStream out)
	{
		MetaData meta = (MetaData)node.getUserObject();
		
		if(meta.Type.equals("Book"))
		{
			out.println("Type");
			out.println(meta.Type);
			out.println(meta.Link);
			out.println(meta.Name);
			out.println(meta.Pages);
		}
		else if(meta.Type.equals("Link"))
		{
			out.println("Type");
			out.println(meta.Type);
			out.println(meta.Link);
			out.println(meta.Name);
			out.println(meta.Pages);
		}
		else if(meta.Type.equals("Bookmark"))
		{
			out.println("Type");
			out.println(meta.Type);
			out.println(meta.Link);
			out.println(meta.Name);
			out.println(meta.Pages);
		}
		else if(meta.Type.equals("Folder"))
		{
			out.println("Type");
			out.println(meta.Type);
			out.println(meta.Name);
		}
		
		if(node.getChildCount() > 0)
		{
			out.println("Level");
		
			for(int i = 0;i < node.getChildCount();i++)
				Print((DefaultMutableTreeNode)node.getChildAt(i),out);
		
			out.println("Back");
		}
		
		return;
	}
	
	public void Run()
	{
		
		
		return;
	}
	
	public void mousePressed(MouseEvent e)
	{
	    if (e.isPopupTrigger())
	    {
	    	TreePath T = Tree.getPathForLocation(e.getX(), e.getY());
	    	
	    	if(T == null)
	    		Tree.setSelectionPath(new TreePath(top));
	    	else
	    		Tree.setSelectionPath(T);
	    	
	    	TreePopup.show((Component)e.getSource(),e.getX(), e.getY());
	    }
	    
	    return;
	}
	public void mouseReleased(MouseEvent e)
	{
	    if (e.isPopupTrigger())
	    {
	    	TreePath T = Tree.getPathForLocation(e.getX(), e.getY());
	    	
	    	if(T == null)
	    		Tree.setSelectionPath(new TreePath(top));
	    	else
	    		Tree.setSelectionPath(T);
	    	
	    	TreePopup.show((Component)e.getSource(),e.getX(), e.getY());
	    }
	    
	    return;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		return;
	}

	public void mouseEntered(MouseEvent e)
	{
		return;
	}

	public void mouseExited(MouseEvent e)
	{
		return;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("About"))
			About();
		else if(e.getActionCommand().equals("Books"))
			try
			{
				mode = 1;
				CreateFramework();
			}
			catch(IOException e2)
			{
				System.err.println("Failed to create framework.");
			}
		else if(e.getActionCommand().equals("Go"))
		{
			if(CurrentLink != null)
			{
				try
				{
					Integer.parseInt(Page.getText());
				}
				catch(NumberFormatException e2)
				{
					Page.setText(CurrentPage + "");
					return;
				}
			
				if(Integer.parseInt(Page.getText()) > Integer.parseInt(TotalPages.getText()))
					return;
			
				Scanner in;
				
				try
				{
					in = new Scanner(new File(QualifiedName + CurrentLink.Link));
				}
				catch(FileNotFoundException e2)
				{
					return;
				}
				
				Area.setMargin(new Insets(0,5,0,0));
				Area.setText("");
				
				CurrentPage = Integer.parseInt(Page.getText());
				
				for(int i = 0;i < CurrentPage * 50;i++)
					in.nextLine();
				
				for(int i = 0;i < 50 && in.hasNextLine();i++)
					Area.append(in.nextLine() + "\n");
				
				Area.setText(Area.getText().substring(0,Area.getText().length() - 1));
				
				in.close();
			}
		}
		else if(e.getActionCommand().equals("Exit"))
			System.exit(1);
		else if(e.getActionCommand().equals("Next"))
		{
			if(CurrentLink != null)
			{
				try
				{
					Integer.parseInt(Page.getText());
				}
				catch(NumberFormatException e2)
				{
					Page.setText(CurrentPage + "");
					return;
				}
			
				if(CurrentPage + 1 > Integer.parseInt(TotalPages.getText()))
					return;
			
				Scanner in;
				
				try
				{
					in = new Scanner(new File(QualifiedName + CurrentLink.Link));
				}
				catch(FileNotFoundException e2)
				{
					return;
				}
				
				Area.setMargin(new Insets(0,5,0,0));
				Area.setText("");
				
				CurrentPage++;
				Page.setText(CurrentPage + "");
				
				for(int i = 0;i < CurrentPage * 50;i++)
					in.nextLine();
				
				for(int i = 0;i < 50 && in.hasNextLine();i++)
					Area.append(in.nextLine() + "\n");
				
				Area.setText(Area.getText().substring(0,Area.getText().length() - 1));
				
				in.close();
			}
		}
		else if(e.getActionCommand().equals("Previous"))
		{
			if(CurrentLink != null)
			{
				try
				{
					Integer.parseInt(Page.getText());
				}
				catch(NumberFormatException e2)
				{
					Page.setText(CurrentPage + "");
					return;
				}
			
				if(CurrentPage - 1 < 0)
					return;
			
				Scanner in;
				
				try
				{
					in = new Scanner(new File(QualifiedName + CurrentLink.Link));
				}
				catch(FileNotFoundException e2)
				{
					return;
				}
				
				Area.setMargin(new Insets(0,5,0,0));
				Area.setText("");
				
				CurrentPage--;
				Page.setText(CurrentPage + "");
				
				for(int i = 0;i < CurrentPage * 50;i++)
					in.nextLine();
				
				for(int i = 0;i < 50 && in.hasNextLine();i++)
					Area.append(in.nextLine() + "\n");
				
				Area.setText(Area.getText().substring(0,Area.getText().length() - 1));
				
				in.close();
			}
		}
		else if(e.getActionCommand().equals("Delete"))
		{
			DefaultMutableTreeNode dmtn, node;
	        
	        TreePath path = Tree.getSelectionPath();
	        dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
	        
	        node = (DefaultMutableTreeNode)dmtn.getParent();
	        int nodeIndex = node.getIndex(dmtn);
	        dmtn.removeAllChildren();
	        node.remove(nodeIndex);
	        
	        ((DefaultTreeModel)Tree.getModel()).nodeStructureChanged((TreeNode)node);
		}
		else if(e.getActionCommand().equals("New Folder"))
		{
			String STemp = JOptionPane.showInputDialog("Folder Name");
			
			DefaultMutableTreeNode dmtn;
	        
	        TreePath path = Tree.getSelectionPath();
	        dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
	        dmtn.add(new DefaultMutableTreeNode(new MetaData(STemp,"","Folder")));
	        
	        ((DefaultTreeModel)Tree.getModel()).nodeStructureChanged((TreeNode)dmtn);
		}
		else if(e.getActionCommand().equals("New Bookmark"))
		{
			String STemp = JOptionPane.showInputDialog("Bookmark Name");
			
			DefaultMutableTreeNode dmtn;
	        
	        TreePath path = Tree.getSelectionPath();
	        dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
	        dmtn.add(new DefaultMutableTreeNode(new MetaData(STemp,"","Bookmark",0)));
	        
	        ((DefaultTreeModel)Tree.getModel()).nodeStructureChanged((TreeNode)dmtn);
		}
		else if(e.getActionCommand().equals("New Link"))
		{
			String STemp = JOptionPane.showInputDialog("Shortcut Name");
			
			DefaultMutableTreeNode dmtn;
	        
	        TreePath path = Tree.getSelectionPath();
	        dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
	        dmtn.add(new DefaultMutableTreeNode(new MetaData(STemp,"","Link",0)));
	        
	        ((DefaultTreeModel)Tree.getModel()).nodeStructureChanged((TreeNode)dmtn);
		}
		
		return;
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		if(e.getItemSelectable() == this.Enabled)
		{
			if(Enabled.getState())
			{
				Enabled.setLabel("Disable Editing");
				Enabled.setShortcut(new MenuShortcut(KeyEvent.VK_D));
			}
			else
			{
				Enabled.setLabel("Enable Editing");
				Enabled.setShortcut(new MenuShortcut(KeyEvent.VK_E));
			}
			
			try
			{
				CreateFramework();
			}
			catch(IOException e1){}
		}
		
		return;
	}
	
	public void valueChanged(TreeSelectionEvent arg0)
	{
		DefaultMutableTreeNode node = null;
		TreePath path = Tree.getSelectionPath();
		
		if(path == null)
			return;
		
        node = (DefaultMutableTreeNode)path.getLastPathComponent();
		
		MetaData meta = (MetaData)node.getUserObject();
		Scanner in;
		
		if(meta == null)
			return;
		else if(meta.Type.equals("Book"))
		{
			try
			{
				CreateBookFramework();
			}
			catch(IOException e){}
			
			try
			{
				in = new Scanner(new File(QualifiedName + meta.Link));
			}
			catch(FileNotFoundException e)
			{
				return;
			}
			
			CurrentLink = meta;
			
			Area.setMargin(new Insets(0,5,0,0));
			Area.setText("");
		
			for(int i = 0;i < 50 && in.hasNextLine();i++)
				Area.append(in.nextLine() + "\n");
		
			Area.setText(Area.getText().substring(0,Area.getText().length() - 1));
			
			CurrentPage = 0;
			Page.setText(CurrentPage + "");
			TotalPages.setText(meta.Pages + "");
			
			in.close();
		}
		else if(meta.Type.equals("Bookmark") || meta.Type.equals("Link"))
		{
			try
			{
				CreateBookFramework();
			}
			catch(IOException e){}
			
			StringTokenizer Tokenizer = new StringTokenizer(meta.Link);
			String STemp = Tokenizer.nextToken();
			
			try
			{
				in = new Scanner(new File(QualifiedName + STemp));
			}
			catch(FileNotFoundException e)
			{
				return;
			}
			
			CurrentLink = new MetaData(meta.Name,STemp,"Book",meta.Pages);
			
			Area.setMargin(new Insets(0,5,0,0));
			Area.setText("");
			
			int j = Integer.parseInt(Tokenizer.nextToken());
			
			for(int i = 0;i < j * 50 ;i++)
				in.nextLine();
			
			for(int i = 0;i < 50 && in.hasNextLine();i++)
				Area.append(in.nextLine() + "\n");
		
			Area.setText(Area.getText().substring(0,Area.getText().length() - 1));
			
			CurrentPage = j;
			Page.setText(CurrentPage + "");
			TotalPages.setText(meta.Pages + "");
			
			in.close();
		}
		
		return;
	}
	
	protected void About()
	{
		String msg = "Created by Omacron-LURYI ©2010\nThis is a free shareware program.\n  Not intended for sale or resale.";
		JOptionPane.showMessageDialog(null,msg,"About",JOptionPane.INFORMATION_MESSAGE);
		
		return;
	}
	
	protected void CreateBookFramework() throws IOException
	{
		setIconImage(new ImageIcon(QualifiedName + "Omacron-LURYI.png").getImage());
		
		JButton[] Buttons = new JButton[3];
		
		for(int i = 0;i < 3;i++)
		{
			Buttons[i] = new JButton();
			Buttons[i].setPreferredSize(new Dimension(150,25));
			Buttons[i].addActionListener(this);
			Buttons[i].setEnabled(true);
		}
		
		Buttons[0].setText("Books");
		Buttons[0].setActionCommand("Books");
		Buttons[0].setToolTipText("Contains a list of all your books.");
		
		Buttons[1].setText("Bookmarks");
		Buttons[1].setActionCommand("Bookmarks");
		Buttons[1].setToolTipText("Contains a list of your bookmarks.");
		
		Buttons[2].setText("Search");
		Buttons[2].setActionCommand("Search");
		Buttons[2].setToolTipText("Searches though your library.");
		
	    String AreaText = "";
	    
	    if(Area != null)
	    	AreaText = Area.getText();
	    
	    Area = new JTextArea();
		Area.setLineWrap(false);
       	Area.setEditable(Enabled.getState());
       	Area.setTabSize(5);
       	Area.setText(AreaText);
	    
		JScrollPane AreaView = new JScrollPane(Area);
	    AreaView.setPreferredSize(new Dimension(650,550));
	    AreaView.setEnabled(true);
	    
	    Page = new JTextField();
	    Page.setEnabled(true);
	    Page.setPreferredSize(new Dimension(63,25));
	    TotalPages = new JTextField();
	    TotalPages.setEnabled(false);
	    TotalPages.setPreferredSize(new Dimension(63,25));
	    
	    getContentPane().removeAll();
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        // Horizontal Group
        ParallelGroup HR = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup h1 = layout.createSequentialGroup();
        SequentialGroup h5 = layout.createSequentialGroup();
        ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup h4 = layout.createSequentialGroup();
        ParallelGroup h3 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        SequentialGroup h6 = layout.createSequentialGroup();
        
        JLabel Of = new JLabel(" of ");
        
        h5.addComponent(Buttons[0],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h5.addGap(5);
        
        h4.addComponent(Page,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h4.addComponent(Of,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h4.addComponent(TotalPages,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h5.addGroup(h4);
        
        h2.addGroup(h5);
        
        JButton Previous = new JButton("Prev");
        JButton Go = new JButton("Go");
        JButton Next = new JButton("Next");
        Previous.setPreferredSize(new Dimension(44,25));
        Go.setPreferredSize(new Dimension(45,25));
        Next.setPreferredSize(new Dimension(44,25));
        Previous.setFont(new Font("Times New Roman",Font.PLAIN,10));
        Go.setFont(new Font("Times New Roman",Font.PLAIN,10));
        Next.setFont(new Font("Times New Roman",Font.PLAIN,10));
        Previous.setMargin(new Insets(0,0,0,0));
        Go.setMargin(new Insets(0,0,0,0));
        Next.setMargin(new Insets(0,0,0,0));
        Previous.setActionCommand("Previous");
        Go.setActionCommand("Go");
        Next.setActionCommand("Next");
        Previous.addActionListener(this);
        Go.addActionListener(this);
        Next.addActionListener(this);
        
        h6.addComponent(Buttons[1],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h6.addGap(5);
        h6.addComponent(Previous,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h6.addGap(5);
        h6.addComponent(Go,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h6.addGap(5);
        h6.addComponent(Next,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        h2.addGroup(h6);
        
        h2.addComponent(Buttons[2],GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        
        h2.addComponent(TreeView,GroupLayout.Alignment.LEADING,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        
        h1.addContainerGap();
        h1.addGroup(h2);
        h1.addContainerGap();
        
        h3.addComponent(AreaView,GroupLayout.Alignment.LEADING,200,GroupLayout.PREFERRED_SIZE,(int)this.getMaximumSize().getWidth());
        
        h1.addGap(5);
        h1.addContainerGap();
        h1.addGroup(h3);
        h1.addContainerGap();
        
        HR.addGroup(Alignment.TRAILING,h1);
        
        layout.setHorizontalGroup(HR);
        
        // Vertical Group
        ParallelGroup Vert = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup v1 = layout.createSequentialGroup();
        SequentialGroup v2 = layout.createSequentialGroup();
        ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup v4 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        
        v1.addContainerGap();
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        
        v3.addComponent(Buttons[0],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        
    	v3.addComponent(Page,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
    	v3.addComponent(Of);
    	v3.addComponent(TotalPages,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        v1.addGroup(v3);
        
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
    	
        v4.addComponent(Buttons[1],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        v4.addComponent(Previous,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        v4.addComponent(Go,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        v4.addComponent(Next,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        v1.addGroup(v4);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        
       	v1.addComponent(Buttons[2],GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        
        v1.addComponent(TreeView,141,GroupLayout.PREFERRED_SIZE,(int)this.getMaximumSize().getHeight());
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        v1.addContainerGap();
        v1.addGap(11);
        
        v2.addContainerGap();
        v2.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        v2.addComponent(AreaView,100,GroupLayout.PREFERRED_SIZE,(int)this.getMaximumSize().getHeight());
        v2.addContainerGap();
        
        Vert.addGroup(v1);
        Vert.addGroup(v2);
        
        layout.setVerticalGroup(Vert);
		
		return;
	}
	
	protected void CreateFramework() throws IOException
	{
		switch(mode)
		{
		case 1:
			CreateBookFramework();
			break;
		default:
			System.exit(0);
			break;
		}
		
		return;
	}
	
	protected void CreateMenu()
	{
		MenuBar Menu = new MenuBar();
		
		String[] Headers = {"File","Edit","Help"};
		Menu[] OP = new Menu[Headers.length];
		
		String[][] OPs = {{"New Book","Open Book","Save Book","Save Book As","","Import","Export","","Exit"}
						 ,{"%JCheckBox","","Find","","Undo","Copy","Paste",null,null}
						 ,{"Help","About",null,null,null,null,null,null,null}};
		
		int[][] Shortcuts = {{KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_S,0,0,KeyEvent.VK_I,KeyEvent.VK_X,0,0}
							,{0,0,KeyEvent.VK_F,0,KeyEvent.VK_Z,KeyEvent.VK_C,KeyEvent.VK_V,0,0}
							,{KeyEvent.VK_H,0,0,0,0,0,0,0,0}};
        
        for(int i = 0;i < OP.length;i++)
        	OP[i] = new Menu();
        
        for(int i = 0;i < Headers.length;i++)
        {
        	OP[i].setLabel(Headers[i]);
        	
        	for(int j = 0;j < OPs[i].length;j++)
        	{
        		if(OPs[i][j] == null)
        			continue;
        		
        		if(OPs[i][j].equals(""))
        		{
        			OP[i].addSeparator();
        			continue;
        		}
        		else if(OPs[i][j].equals("%JCheckBox"))
        		{
        			Enabled = new CheckboxMenuItem();
        			Enabled.setState(false);
        			Enabled.setActionCommand("Enable/Disable");
        			Enabled.setLabel("Enable Editing");
        			Enabled.setShortcut(new MenuShortcut(KeyEvent.VK_E));
        			Enabled.addItemListener(this);
        			
        			OP[i].add(Enabled);
        			
        			continue;
        		}
        		
        		MenuItem MI = new MenuItem();
                MI.setLabel(OPs[i][j]);
                
                if(!(Shortcuts[i][j] == 0))
                	MI.setShortcut(new MenuShortcut(Shortcuts[i][j]));
                
                OP[i].add(MI);
        	}
        	
        	OP[i].addActionListener(this);
        	Menu.add(OP[i]);
        }
        
        this.setMenuBar(Menu);
		
		return;
	}
	
	protected void CreateNodes(DefaultMutableTreeNode Stem, Scanner in)
	{
		DefaultMutableTreeNode Item = Stem;
		String Data = "";
		
		while(in.hasNextLine() && !(Data = in.nextLine()).equals("Back"))
		{
			if(Data.equals("Level"))
			{
				CreateNodes(Item,in);
			}
			else if(Data.equals("End"))
				return;
			else if(Data.equals("Type"))
			{
				Data = in.nextLine();
				
				if(Data.equals("Folder"))
				{
					Data = in.nextLine();
					Item = new DefaultMutableTreeNode(new MetaData(Data,"","Folder"));
					Stem.add(Item);
				}
				else if(Data.equals("Book"))
				{
					Data = in.nextLine();
					Item = new DefaultMutableTreeNode(new MetaData(in.nextLine(),Data,"Book",Integer.parseInt(in.nextLine())));
					Stem.add(Item);
				}
				else if(Data.equals("Link"))
				{
					Data = in.nextLine();
					Item = new DefaultMutableTreeNode(new MetaData(in.nextLine(),Data,"Link",Integer.parseInt(in.nextLine())));
					Stem.add(Item);
				}
				else if(Data.equals("Bookmark"))
				{
					Data = in.nextLine();
					Item = new DefaultMutableTreeNode(new MetaData(in.nextLine(),Data,"Bookmark",Integer.parseInt(in.nextLine())));
					Stem.add(Item);
				}
			}
			else
			{
				Item = new DefaultMutableTreeNode(new MetaData("",Data,"Error"));
				Stem.add(Item);
			}
		}
		
		return;
	}
	
	protected CheckboxMenuItem Enabled;
	protected DefaultMutableTreeNode top;
	protected int CurrentPage;
	protected int mode;
	protected JPopupMenu TreePopup;
	protected JTextArea Area;
	protected JTextField Page;
	protected JTextField TotalPages;
	protected JTree Tree;
	protected JScrollPane TreeView;
	protected MetaData CurrentLink;
	protected String QualifiedName;
}
