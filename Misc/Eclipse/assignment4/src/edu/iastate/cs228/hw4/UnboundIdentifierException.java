package edu.iastate.cs228.hw4;

/**
 * Exception type for evaluation of expression trees via TreeNode.eval(), 
 * thrown when an identifier in an expression has no corresponding
 * value in the environment.
 */
public class UnboundIdentifierException extends Exception
{
  public UnboundIdentifierException(String msg)
  {
    super(msg);
  }
}
