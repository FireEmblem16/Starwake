package edu.iastate.cs228.hw4;

/**
 * Exception type for parse errors in ExpressionTrees.createTree().
 */
public class ParseException extends Exception
{
  public ParseException(String msg)
  {
    super(msg);
  }
}
