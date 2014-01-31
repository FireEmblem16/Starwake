package Reader;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class MyRenderer extends DefaultTreeCellRenderer
{
    public MyRenderer(Icon icon1, Icon icon2, Icon icon3, Icon icon4)
    {
        Book = icon1;
        Link = icon2;
        Bookmark = icon3;
        Folder = icon4;
        
        return;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
        
        if(IsBook(value))
            setIcon(Book);
        else if(IsLink(value))
            setIcon(Link);
        else if(IsBookmark(value))
            setIcon(Bookmark);
        else if(IsFolder(value))
            setIcon(Folder);

        return this;
    }
    
    protected boolean IsBook(Object value)
    {
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    	
    	if(((MetaData)node.getUserObject()).Type.equals("Book"))
    		return true;
    	
    	return false;
    }

    protected boolean IsLink(Object value)
    {
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    	
    	if(((MetaData)node.getUserObject()).Type.equals("Link"))
    		return true;
    	
    	return false;
    }
    
    protected boolean IsBookmark(Object value)
    {
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    	
    	if(((MetaData)node.getUserObject()).Type.equals("Bookmark"))
    		return true;
    	
    	return false;
    }
    
    protected boolean IsFolder(Object value)
    {
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
    	
    	if(((MetaData)node.getUserObject()).Type.equals("Folder"))
    		return true;
    	
    	return false;
    }
    
    protected Icon Book;
    protected Icon Link;
    protected Icon Bookmark;
    protected Icon Folder;
}
