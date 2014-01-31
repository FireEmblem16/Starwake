package edu.iastate.cs228.hw4.nodes;

import java.util.Map;

import edu.iastate.cs228.hw4.AstNode;
import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.UnboundIdentifierException;

/**
 * Base class for expression tree nodes supporting polymorphic evaluation.
 */
public abstract class TreeNode implements AstNode
{
  /**
   * Left child.
   */
  protected TreeNode left;
  
  /**
   * Right child.
   */
  protected TreeNode right;

  /**
   * Type of this node.
   */
  protected final TokenType type;
  
  /**
   * Constructs a leaf node.
   */
  protected TreeNode(TokenType type)
  {
    this.type = type;
  }
  
  /**
   * Constructs an internal node with the given children.
   * @param left
   * @param right
   */
  protected TreeNode(TokenType type, TreeNode left, TreeNode right)
  {
    this.type = type;
    this.left = left;
    this.right = right;
  }
  
  /**
   * Returns the value of the subtree rooted at this node, using the 
   * values in the given Map for the interpretation of ID nodes.
   * @param env 
   * @return value of the expression represented by this node
   * @throws UnboundIdentifierException if the subtree contains
   *     variables that do not have associated values in the
   *     given environment
   */
  public abstract int eval(Map<String, Integer> env) throws UnboundIdentifierException;
  
  /**
   * Returns a String representation of this node. Must be implemented by
   * all subtypes.
   * @return a String representation of this node
   */
  protected abstract String getText();

  /**
   * Returns the left child of this node.
   * @return the left child
   */
  public TreeNode left()
  {
    return left;
  }
  
  /**
   * Returns the right child of this node.
   * @return the right child
   */
  public TreeNode right()
  {
    return right;
  }
  
  /**
   * Returns the type of this node as a TokenType.
   * @return type of this node
   */
  public TokenType type()
  {
    return type;
  }
  
  /**
   * Sets the left child of this node.
   * @param node the new child node
   */
  public void setLeftChild(TreeNode node)
  {
    left = node;
  }
  
  /**
   * Sets the right child of this node.
   * @param node the new child node
   */
  public void setRightChild(TreeNode node)
  {
    right = node;
  }
  
  /**
   * Determines whether this node is a leaf.
   * @return true if this node has no children,
   *     false otherwise
   */
  public boolean isLeaf()
  {
    return left == null && right == null;
  }
  
  @Override
  public String toString()
  {
    return getText();
  }
  
  @Override
  public AstNode getChild(int i)
  {
    if (i == 0)
    {
      if (left != null)
      {
        return left;
      }
      else
      {
        return right;
      }
    }
    else
    {
      return right;
    }
  }
  
  @Override
  public int getChildCount()
  {
    int ret = 0;
    if (left != null) ++ret;
    if (right != null) ++ret;
    return ret;
  }
}
