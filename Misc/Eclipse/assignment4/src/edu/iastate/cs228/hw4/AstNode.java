package edu.iastate.cs228.hw4;

/**
 * Minimal interface for nodes used by the AST viewer.
 */
public interface AstNode
{
  /**
   * Returns the i-th child of this node.
   * @param i
   * @return the i-th child of this node
   */
  public AstNode getChild(int i);
  
  /**
   * Returns the number of children of this node.
   * @return the number of children of this node
   */
  public int getChildCount();
}
