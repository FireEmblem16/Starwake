package edu.iastate.cs228.hw4;

/**
 * Tokens for lexical scanning of an arithmetic expression.  Tokens
 * are immutable.
 */
public class Token
{  
  /**
   * Type of this token.
   */
  private final TokenType type;
  
  /**
   * Text for this token; applies only to INT and ID tokens.
   */
  private final String text;
  
  /**
   * Constructs a Token with the given type and text.
   * @param type
   * @param text
   */
  public Token(TokenType type, String text)
  {
    this.type = type;
    this.text = text;
  }

  /**
   * Constructs a token with given type and null text.
   * @param type
   */
  public Token(TokenType type)
  {
    this(type, null);
  }
  
  /**
   * Determines whether this token is an operand (INT or ID).
   * @return
   */
  public boolean isOperand()
  {
    return type.isOperand();
  }
  
  /**
   * Determines whether this token is an operator or parenthesis.
   * @return
   */
  public boolean isOperator()
  {
    return type.isOperator();
  }
  
  /**
   * Returns the precedence of this token as an integer.  This value
   * is meaningful only in comparison to other tokens (and has no meaning
   * at all for INT and ID tokens).
   * @return
   */
  public int precedence()
  {
    return type.precedence();
  }
  
  /**
   * Returns the type of this token.
   * @return
   */
  public TokenType type()
  {
    return type;
  }

  /**
   * Returns the text of this token (possibly null).
   * @return
   */
  public String text()
  {
    return text;
  }
  
  /**
   * Returns a String representation of this token.
   */
  @Override
  public String toString()
  {
    if (text == null)
    {
      return type.toString();
    }
    else
    {
      return type.toString() + ":" + text;
    }
  }
}
