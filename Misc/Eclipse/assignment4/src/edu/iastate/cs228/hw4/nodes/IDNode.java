package edu.iastate.cs228.hw4.nodes;

import java.util.Map;


import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.UnboundIdentifierException;

public class IDNode extends TreeNode
{
  private String id;
  
  public IDNode(String text)
  {
    super(TokenType.ID);
    this.id = text;
  }
  
  @Override
  public int eval(Map<String, Integer> env) throws UnboundIdentifierException
  {
    if (env != null)
    {
      Integer ret = env.get(id);
      if (ret != null)
      {
        return ret;
      } 
    }
    throw new UnboundIdentifierException("Undefined variable: " + id);
  }

  @Override
  protected String getText()
  {
    return id;
  }
}
