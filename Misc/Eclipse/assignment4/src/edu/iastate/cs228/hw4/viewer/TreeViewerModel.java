package edu.iastate.cs228.hw4.viewer;

import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import edu.iastate.cs228.hw4.AstNode;

/**
 * Data model for the SPL tree viewer.
 */
public class TreeViewerModel implements TreeModel
{
  private Vector<TreeModelListener> treeModelListeners =
    new Vector<TreeModelListener>();
  
  private AstNode root;

  public TreeViewerModel(AstNode r)
  {
    root = r;
  }
  
  @Override
  public void addTreeModelListener(TreeModelListener l)
  {
    treeModelListeners.add(l);
  }

  @Override
  public Object getChild(Object node, int i)
  {
    return ((AstNode) node).getChild(i);
  }

  @Override
  public int getChildCount(Object node)
  {
    return ((AstNode) node).getChildCount();
  }

  @Override
  public int getIndexOfChild(Object node, Object child)
  {
    AstNode n = (AstNode) node;
    for (int i = 0; i < n.getChildCount(); ++i)
    {
      if (n.getChild(i).equals(child))
      {
        return i;
      }
    }
    return -1;
  }

  @Override
  public Object getRoot()
  {
    return root;
  }

  @Override
  public boolean isLeaf(Object node)
  {
    return ((AstNode) node).getChildCount() == 0;
  }

  @Override
  public void removeTreeModelListener(TreeModelListener l)
  {
    treeModelListeners.remove(l);
  }

  // Required by TreeModel, but not used here
  @Override
  public void valueForPathChanged(TreePath arg0, Object arg1)
  {
  }


}
