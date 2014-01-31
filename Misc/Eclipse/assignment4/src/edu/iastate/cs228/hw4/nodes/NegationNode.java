package edu.iastate.cs228.hw4.nodes;

import java.util.Map;


import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.UnboundIdentifierException;

public class NegationNode extends TreeNode
{
  public NegationNode(TreeNode child)
  {
    super(TokenType.NEGATION, null, child);
  }
  
  @Override
  public int eval(Map<String, Integer> env) throws UnboundIdentifierException
  {
    return -right.eval(env);
  }
  
  @Override
  protected String getText()
  {
    return "NEG";
  }
}
