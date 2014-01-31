package edu.iastate.cs228.hw4.nodes;

import java.util.Map;


import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.UnboundIdentifierException;

public class PlusNode extends TreeNode
{
  public PlusNode(TreeNode left, TreeNode right)
  {
    super(TokenType.PLUS, left, right);
  }
  
  @Override
  public int eval(Map<String, Integer> env) throws UnboundIdentifierException
  {
    return left.eval(env) + right.eval(env);
  }
  
  @Override
  protected String getText()
  {
    return "+";
  }
}
