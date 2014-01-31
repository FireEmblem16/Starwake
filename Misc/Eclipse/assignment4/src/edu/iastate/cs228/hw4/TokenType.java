package edu.iastate.cs228.hw4;

/**
 * Token types, in order of precedence. The same values are used as node types
 * in TreeNode.
 */
public enum TokenType
{
	LPAREN, RPAREN, MINUS, PLUS, DIV, TIMES, NEGATION, EXP, ID, INT;

	/**
	 * Determines whether this token is an operand (INT or ID).
	 * @return true if this is an INT or ID, false otherwise
	 */
	public boolean isOperand()
	{
		return this == INT || this == ID;
	}

	/**
	 * Determines whether this token is an operator or parenthesis.
	 * @return true if this is an operator or a parenthesis token
	 */
	public boolean isOperator()
	{
		return !isOperand();
	}

	/**
	 * Returns the precedence of this TokenType as an integer. This value is
	 * meaningful only in comparison to other tokens (and has no meaning at all
	 * for INT and ID tokens).
	 * @return precedence of this token
	 */
	public int precedence()
	{
		int ret = this.ordinal();

		// adjust so that PLUS and MINUS have the same precedence, and
		// DIV and TIMES have the same precedence
		if(this == MINUS || this == DIV)
			++ret;
		return ret;
	}
}
