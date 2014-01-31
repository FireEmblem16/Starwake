package edu.iastate.cs228.hw4.nodes;

import java.util.Map;


import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.UnboundIdentifierException;

public class IntNode extends TreeNode
{
  private int value;
  
  public IntNode(int value)
  {
    super(TokenType.INT);
    this.value = value;
  }
  
  @Override
  public int eval(Map<String, Integer> env) throws UnboundIdentifierException
  {
    return value;
  }
  
  @Override
  protected String getText()
  {
    return "" + value;
  }
}
