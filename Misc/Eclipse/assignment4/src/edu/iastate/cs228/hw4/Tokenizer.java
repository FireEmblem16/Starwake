package edu.iastate.cs228.hw4;
import java.util.Iterator;

import edu.iastate.cs228.hw4.TokenType;

/**
 * Lexical scanner for arithmetic expressions.
 */
public class Tokenizer implements Iterable<Token>
{
  /**
   * The expression scanned by this Tokenizer.
   */
  private final String expression;
  
  /**
   * Constructs a Tokenizer for the given expression.
   * @param expr
   */
  public Tokenizer(String expr)
  {
    this.expression = expr.trim();
  }
  
  /**
   * Returns an iterator over the token stream.
   */
  @Override
  public Iterator<Token> iterator()
  {
    return new Iter(expression);
  }
  
  /**
   * Returns a String representation of the token stream.
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    Iterator<Token> iter = iterator();
    while (iter.hasNext())
    {
      sb.append(iter.next().toString());
      if (iter.hasNext())
      {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }
  
  /**
   * Helper method determines whether a given character is an operator symbol.
   * @param c
   * @return
   */
  private boolean isOperator(char c)
  {
    return (c == '(' || c == ')' || c == '+' || c == '-' || c == '*' || c == '/' || c == '^');
  }

  /**
   * Iterator that produces the token stream for the expression represented
   * by this Tokenizer.  Note that remove() is not implemented.
   */
  private class Iter implements Iterator<Token>
  {
    /**
     * Mutable representation of expression.  After each call to next(),
     * this value will contain only the text yet to be scanned.
     */
    private String expr;
    
    public Iter(String expr)
    {
      this.expr = expr.trim();
    }
    
    @Override
    public boolean hasNext()
    {
      return expr.length() != 0;
    }

    @Override
    public Token next()
    {
      char c = expr.charAt(0);
      if (isOperator(c))
      {
        expr = expr.substring(1).trim();
        switch (c)
        {
          case '(': return new Token(TokenType.LPAREN);
          case ')': return new Token(TokenType.RPAREN);
          case '-': return new Token(TokenType.MINUS);
          case '+': return new Token(TokenType.PLUS);
          case '/': return new Token(TokenType.DIV);
          case '*': return new Token(TokenType.TIMES);
          case '^': return new Token(TokenType.EXP);
        }
      }
      
      // find next whitespace or operator
      int i = 0;
      while (i < expr.length())
      {
        c = expr.charAt(i);
        if (Character.isWhitespace(c) || isOperator(c)) break;
        ++i;
      }
      String temp = expr.substring(0, i);
      if (i < expr.length())
      {
        expr = expr.substring(i).trim();
      }
      else
      {
        expr = "";
      }

      if (Character.isDigit(temp.charAt(0)))
      {
        return new Token(TokenType.INT, temp);
      }
      else
      {
        return new Token(TokenType.ID, temp);
      }
    }

    @Override
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  
}
