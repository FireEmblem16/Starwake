package edu.iastate.cs228.hw4.nodes;

import java.util.Map;


import edu.iastate.cs228.hw4.TokenType;
import edu.iastate.cs228.hw4.UnboundIdentifierException;

public class ExpNode extends TreeNode
{
  public ExpNode(TreeNode left, TreeNode right)
  {
    super(TokenType.EXP, left, right);
  }
  
  @Override
  public int eval(Map<String, Integer> env) throws UnboundIdentifierException
  {
    int base = left.eval(env);
    int exponent = right.eval(env);
    if (exponent < 0)
    {
      throw new ArithmeticException("Negative exponent " + exponent);
    }
    return power(base, exponent);
  }
  
  // recursive power function
  private int power(int base, int exponent)
  {
    if (exponent == 0) return 1;
    if (exponent == 1) return base;
    int p = exponent / 2;
    int result = power(base, p);
    result *= result;
    if (exponent % 2 != 0)
    {
      result *= base;
    }
    return result;
  }
  
  @Override
  protected String getText()
  {
    return "^";
  }
}
