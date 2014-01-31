package edu.iastate.cs228.hw4;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import edu.iastate.cs228.hw4.nodes.*;

/**
 * Utilities for parsing and evaluating arithmetic expressions.
 */
public class ExpressionTrees
{
	/**
	 * Returns a String containing the toString() value of each node in a
	 * postorder traversal of the given tree. The values are separated by a
	 * single space. (The negation operation will be displayed as "NEG".)
	 * @param root node at the root of the tree
	 * @return String representation of the tree in postorder
	 */
	public static String getPostfixString(TreeNode root)
	{
		// Just to be safe we return an empty string
		// if given a null node.
		if(root == null)
			return "";
		
		// If we have a leaf then [root] is an integer
		// so we just return its value.
		if(root.isLeaf())
			return root.toString();
		
		// Since we return "" with null we can ignore the
		// possiblity of not having a left child.
		String ret = getPostfixString(root.left());
		
		// If we don't have a left child we don't need a space.
		// We also don't need to deal with NEG since we need
		// to return it as NEG instead of -.
		ret += (root.left() == null ? "" : " ") +
							getPostfixString(root.right());
		
		// We always have a space here.
		ret += " " + root.toString();
		
		return ret;
	}

	/**
	 * Returns a String containing the toString() value of each node in an
	 * inorder traversal of the given tree, where expressions represented by
	 * subtrees are enclosed in parentheses where necessary to indicate the
	 * correct order of operations. Binary operators are surrounded by a space.
	 * Negation should be represented by an ordinary minus sign, NOT followed by
	 * a space.
	 * @param root node at the root of the tree
	 * @return String representation of the tree in parenthesized inorder form
	 */
	public static String getInfixString(TreeNode root)
	{
		return createInfixString(root,"",false);
	}

	/**
	 * Returns the priority level of the TreeNode [n]
	 * @param n the Node for which priority is to be determined
	 * @return the priority of [n] from 0-3. Returns -1 if [n] is invalid
	 * or if [n] is not an operator.
	 */
	private static int getPriority(TreeNode n)
	{
		if(n == null)
			return -1;
		
		return getPriority(n.toString());
	}
	
	/**
	 * Returns the priority level of the String [val].
	 * @param val the String for which priority is to be determined
	 * @return the priority of [val] from 0-3. Returns -1 if [val] is invalid
	 * or if [val] is not an operator.
	 */
	private static int getPriority(String val)
	{
		int ret = -1;
		
		// ( and ) never get a node so we ignore them.
		if(val.equals("^"))
			ret = 3;
		else if(val.equals("NEG"))
			ret = 2;
		else if(val.equals("*") || val.equals("/"))
			ret = 1;
		else if(val.equals("+") || val.equals("-"))
			ret = 0;
		
		return ret;
	}
	
	/**
	 * Returns true if that weird parentheses around similar priority
	 * condition is required. I guess because everyone else implemented
	 * left to right they need this to maintain tree order but my code
	 * works just fine with or without the parentheses.
	 * @param root Node to look into
	 * @param root_priority the priority of the operation above [root]
	 * @param right if this is a right node
	 * @return what is said above
	 */
	private static boolean gotcha(TreeNode root, String root_priority,
									boolean right)
	{
		return getPriority(root) == getPriority(root_priority) &&
				getPriority(root) == 0 && root.toString().equals("-")
				&& (!root_priority.equals("-") || right)
				|| getPriority(root) == getPriority(root_priority) &&
				getPriority(root) == 1 && root.toString().equals("/")
				&& (!root_priority.equals("/") || right)
				|| getPriority(root) == getPriority(root_priority) &&
				getPriority(root) == 3 && !right; // This one isn't a gotcha.
	}
	
	/**
	 * Actually create the String returned by getInfixString.
	 * @param root node at the root of the tree
	 * @param root_priority the last operator preformed
	 * @param right if this is a right node, for gotcha
	 * @return String representation of the tree in parenthesized inorder form
	 */
	private static String createInfixString(TreeNode root, String root_priority,
											boolean right)
	{
		// If we have a null value return an empty string
		// just to be safe. We don't need exceptions.
		if(root == null)
			return "";
		
		// If it's a left we have a number and should simply
		// return its value.
		if(root.isLeaf())
			return root.toString();
		
		// We use a bunch so might as well keep it.
		int priority = getPriority(root);
		
		// If we have a lesser priority then we need a
		// ( to make sure it is done first.
		String ret = (priority < getPriority(root_priority) || gotcha(root,
				 root_priority, right) ? "(" : "") +
				 createInfixString(root.left(), root.toString(), false);
		
		// If we have only a right child then we have a
		// NEG operator and need to output a - instead of NEG
		ret += root.left() == null ? "-" : " " + root.toString();
		
		// If we have a lesser priority we need to close the ).
		// Also if we have no left child then we have a
		// NEG and don't need a space.
		ret += (root.left() == null ? "" : " ") +
			createInfixString(root.right(), root.toString(), true) +
			(priority < getPriority(root_priority) ||
			gotcha(root, root_priority, right) ? ")" : "");
		
		return ret;
	}
	
	/**
	 * Transforms the given tree so that each subtree not containing variables
	 * is replaced by a single IntNode containing the value of the subtree. If
	 * the tree contains at least one variable, then the return value is the
	 * given root node; otherwise, the return value is a single new IntNode
	 * containing the value of the tree.
	 * @param root node at the root of the given tree
	 * @return transformed tree in which all constant subtrees are replaced by a
	 * single node
	 */
	public static TreeNode reduceConstants(TreeNode root)
	{
		// Sanity check.
		if(root == null)
			return null;
		
		// If we have a constant or an ID we have no need to do anything.
		if(root.type().isOperand())
			return root;
		
		// First simplify the left and right child.
		root.setLeftChild(reduceConstants(root.left()));
		root.setRightChild(reduceConstants(root.right()));
		
		// In clear English if the left and right children are
		// not constants (or not there) we can not evaluate [root]
		// beyond what we already have. If we have the right child
		// and the operation of [root] is a negation then we can evaluate
		// [root]. If we can't evaluate [root] we return [root]. At that
		// point [root] should be a IDNode but no promises.
		if(root.left() == null && root.type() != TokenType.NEGATION ||
				root.right() == null || root.left() != null &&
				root.left().type().isOperator() ||
				root.right().type().isOperator())
			return root;
		
		try
		{
			// We try to evaluate [root]. If we can that's great.
			// If we can't we end up returning [root].
			return new IntNode(root.eval(null));
		}
		catch(UnboundIdentifierException e)
		{
			// If this is triggered then we have an ID in [root] and
			// thus we can't evaluate it so we return it.
			return root;
		}
	}

	/**
	 * Returns the result of evaluating the given expression tree using the
	 * supplied values to create the environment. This method performs an
	 * initial traversal of the tree to create the environment, and then invokes
	 * the eval() method of the root node. <br>
	 * The given values are associated with identifiers as follows: Identifiers
	 * are ordered according to the first occurrence in a left-to-right
	 * traversal of the tree. The i-th identifier in this ordering is associated
	 * with the i-th Integer argument. If there are more values supplied than
	 * identifiers, the extra values are ignored. If not enough values are
	 * supplied, an UnboundIdentifierException is thrown.
	 * @param root node at the root of the tree
	 * @param values sequence of Integers to be used as values of variables in
	 * the expression
	 * @return result of evaluating the tree using the supplied values to
	 * construct the environment
	 * @throws UnboundIdentifierException if not enough values are supplied
	 */
	public static int evaluate(TreeNode root, Integer... values)
	throws UnboundIdentifierException
	{
		// Here we create our integer map.
		Hashtable<String, Integer> map = new Hashtable<String, Integer>();
		
		// Here we create a stack so we can do an in order search for variables.
		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.push(root);
		
		// We use this as an index for [values].
		int c = 0;
		
		// We need to get to every leaf in the correct order.
		while(stack.size() > 0)
		{
			// We get this so we can work with it easier.
			TreeNode current = stack.pop();
			
			// Only leaves can have variables.
			if(current.isLeaf())
			{
				// Check that this isn't a constant.
				if(current.type() == TokenType.ID)
					// Make sure we don't already have the key.
					if(!map.containsKey(current.toString()))
						// Make sure we have a bind for the key.
						if(values == null || values.length < c)
							throw new UnboundIdentifierException(
									"The " + c + "th variables in" +
									"root was not bounded.");
						else
						{
							// We have a new variable so create a key for it.
							map.put(current.toString(),values[c++]);
							continue;
						}
			}
			else
			{
				// If we don't have a variable we should push the right and
				// left children of [current] onto the stack in that order.
				stack.push(current.right());
				stack.push(current.left());
			}
		}
		
		// We need only call eval on root to get the value we seek.
		return root.eval(map);
	}

	/**
	 * Returns the number of times [c] occurs in [str].
	 * @param str the String to look through
	 * @param c the character to find
	 * @return number of times [c] appears in [str].
	 */
	private static int numberOf(String str, char c)
	{
		int ret = 0;
		
		for(int i = 0; i < str.length(); i++)
			if(str.charAt(i) == c)
				ret++;
		
		return ret;
	}
	
	/**
	 * Returns the index of the [n]th time [c] appears in [str].
	 * @param str the String to look in
	 * @param c the character to look for
	 * @param n the number of [c]s to look in
	 * @return the index of the [n]th [c] in [str].
	 */
	private static int nthIndexOf(String str, char c, int n)
	{
		if(numberOf(str, c) < n || n < 1)
			return -1;
		
		int count = 0;
		
		for(int i = 0; i < str.length(); i++)
			if(str.charAt(i) == c)
				if(++count == n)
					return i;
		
		// This is unreachable code but the compiler doesn't know that.
		return -1;
	}
	
	/**
	 * Creates an expression tree (abstract syntax tree) for the given
	 * arithmetic expression.
	 * @param expr the expression to be parsed
	 * @return root of the expression tree
	 * @throws ParseException if the expression is invalid
	 */
	public static TreeNode createTree(String expr) throws ParseException
	{
		// First we go through and check if we have a valid set of
		// parentheses so we don't need to worry about illegal sets later.
		if(numberOf(expr, '(') > numberOf(expr, ')'))
			throw new ParseException("Unmatched parenthesis: " +
			"Too many left parentheses.\n\tLast matched parenthesis " +
			"located at index " + nthIndexOf(expr, '(', numberOf(expr, ')'))
			+ " of the given expression.\n\tFirst unmatched parenthesis" +
			" located at index " + nthIndexOf(expr, '(',
			numberOf(expr, ')') + 1) + " of the given expression.");
		else if(numberOf(expr, '(') < numberOf(expr, ')'))
			throw new ParseException("Unmatched parenthesis: " +
			"Not enough left parentheses. Last matched parenthesis" +
			"located at index " + nthIndexOf(expr, ')', numberOf(expr, '('))
			+ " of the given expression. First unmatched parenthesis" +
			"located at index " + nthIndexOf(expr, ')',
			numberOf(expr, '(') + 1) + " of the given expression.");
		
		// Create the variables we need to store data.
		Stack<Token> operators = new Stack<Token>();
		Stack<Token> operands = new Stack<Token>();
		Iterator<Token> tkns = new Tokenizer(expr).iterator();
		
		// We use this to throw errors.
		boolean op_last = true;
		
		// We add Tokens onto the stack so we can pop them off from
		// right to left later.
		while(tkns.hasNext())
		{
			// Get the next Token.
			Token next = tkns.next();
			
			// If we have an operator put it with the other operators.
			if(next.isOperator())
			{
				// Change false minuses to negations.
				// Also left parentheses can occur after operators. 
				if(op_last && next.type() == TokenType.MINUS)
					next = new Token(TokenType.NEGATION);
				else if(op_last && next.type() != TokenType.LPAREN
								&& next.type() != TokenType.RPAREN)
					throw new ParseException("An operator was " +
					"discovered where an operand was expected.\n" +
					"\tGiven value was " + next.toString() + ".\n" +
					"\tAlready processed Tokens include " +
					operators.toString() + " operators and " +
					operands.toString() + " operands.");
				else if(next.type() == TokenType.RPAREN)
					op_last = false; // A right parenthesis must have
									 // an operator after it.
				else // We're good so flip the expected item.
					op_last = true;
				
				// Add the operator.
				operators.push(next);
			}
			else // We have an operand so put it with the other operands.
			{
				// Sanity check.
				if(next.type() == TokenType.NEGATION)
					; // Ignore this token.
				else if(op_last)
					op_last = false; // Flip the expected item.
				else
					throw new ParseException("An operand was " +
					"discovered where an operand was expected.\n" +
					"\tGiven value was " + next.toString() + ".\n" +
					"\tAlready processed Tokens include " +
					operators.toString() + " operators and " +
					operands.toString() + " operands.");
				
				// Add the operand.
				operands.push(next);
			}
		}
		
		// We were asked to create a tree with no operands or we
		// are trying to create a sub expression tree with no
		// operands. Both are equally invalid so we throw an error.
		if(operands.size() == 0)
			throw new ParseException("Attempted to create a tree" +
			" with no expressions or create a sub tree (tree from"
			+ " an expression inside parentheses).\n\tBoth are "
			+ "equally invalid. Remaining operators of the " +
			"expression attempted to be evaluated include " +
			operands.toString() + ".");
		
		// This holds our tree.
		TreeNode root = null;
		
		// For every operator in our expression we need to do something.
		while(operators.size() > 0)
		{
			// This should never happen due to the algorithm we have.
			// Sanity checks are still good though.
			if(operators.size() < 1)
				throw new ParseException("We ran out of operators.\n\t" +
				"Remaining operands include " + operands.toString()+ ".");
			
			// Get the next operator.
			Token op = operators.pop();
			
			// We never will run into a valid left parenthesis in our
			// algorithm since we evaluate right to left and evaluate
			// parentheses expressions as seperate trees.
			if(op.type() == TokenType.LPAREN)
				throw new ParseException("Unmatched parentheses was found."
				+ "\n\tA left parenthesis was found to the right of its " +
				"only viable right parenthesis.\n\tRemaining operands " +
				"include " + operands.toString() + "\n\tRemaining " +
				"operators include " + operators.toString() + ".");
			
			// If we have a higher priority then the next operation we
			// are going to use the current value as [root]'s right
			// operand. In the case that we have lower priority we
			// use [root]'s left value as the right value of our
			// next operation.
			Object right = getPriority(root) <
				getPriority(tokenStringToNodeString(op)) ?
				root == null ? null : root.left() : root;
			
			// In the case of negative exponentials we need this clause. 
			if(root != null && root.type() == TokenType.NEGATION
					&& op.type() == TokenType.EXP)
				right = root;
				
			// This gets initialized to null always. We don't pop
			// because we don't need to if we have a negation operation.
			Object left = null;
			
			// If we would encounter a right parenthesis we need to
			// create a tree from it. We then assign that tree to be
			// the left hand value of our current operation. We don't
			// worry about having a negation operation as our current operand
			// because we evaluate from right to left and that means that
			// a negation operation must have already been preformed.
			if(operators.size() > 0 && (op.type() == TokenType.RPAREN ||
							operators.peek().type() == TokenType.RPAREN))
			{
				// We want to get rid of that useless parenthesis.
				if(op.type() != TokenType.RPAREN)
				{
					operators.pop();
					
					// If [right] is null that means we entered into a
					// sub expression without getting a right value for it.
					// If this is so we need to not corrupt the stack by
					// popping the right value for our operand off the stack.
					if(right == null)
						right = operands.pop();
				}
				
				// We need to fetch things from right to left
				// until we encounter a left parenthesis that closes
				// the right one we just found. We don't worry about
				// having a weird mix of parenthesis in between since
				// we will evaluate the sub tree to be invalid if that
				// every occurs.
				int unclosed_parens = 1;
				
				// We are going to store our Tokens in here for
				// later processing. Since they are currently in
				// reverse order we can pop them off in order later
				// for effective String building.
				Stack<Token> tree = new Stack();
				
				// We know we have the same number of right and left
				// parenthesis so we don't have to worry about
				// overrunning the expression.
				while(unclosed_parens > 0)
				{
					// Sanity check. This offers partial protection against
					// the case where we have (), (()) or something similar.
					if(operands.size() < 0)
						throw new ParseException("We ran out of " +
						"operands while attempting to evaluate a " +
						"parenthetical expression.\n\tRemaining " +
						"operators include " + operators.toString()
						+ ".");
					
					// We only want to push an operand if we are doing
					// something with it. If we have a right parenthesis
					// then we don't have an operator to do anything with
					// it and we will generate an invalid expression when
					// we previously probably had a valid one.
					if(operators.peek().type() != TokenType.RPAREN)
						tree.push(operands.pop());
					
					// In case some idiot desides to do something stupid
					// like alternating minuses and left parentheses
					// we look through this until all the conditions are
					// completely and totally false.
					while(operators.size() > 0 &&
						(operators.peek().type() == TokenType.LPAREN ||
						operators.peek().type() == TokenType.NEGATION))
					{
						// We need to take care of all those pesky unary
						// operators that would cause problems in our push
						// and poping of operands.
						while(operators.size() > 0 &&
							operators.peek().type() == TokenType.NEGATION)
							tree.push(operators.pop());
						
						// We take care of dealing with counting parenthesis
						// before popping operators so that we don't
						// accidentally pop an extra left parenthesis on
						// the stack. That would be bad. We also need to
						// remove all of the left parenthesis because they
						// don't count as operators for moving leftwards.
						// Basically we need a minus or times or something
						// similar after a left parenthesis so we pop all
						// of them instead of just the first one. If we
						// have to close a right parenthesis, in other
						// words [unclosed_parens] > 0, we need to add
						// a left parenthesis to [tree]. Otherwise we need
						// to ignore it but continue removing left
						// parentheses to check for errors.
						while(operators.size() > 0 &&
							operators.peek().type() == TokenType.LPAREN)
							if(--unclosed_parens > 0)
								tree.push(operators.pop());
							else
								operators.pop();
					}
					
					// If we encounter a right parenthesis then we need
					// to increment the number of unclosed parentheses.
					if(operators.size() > 0 &&
							operators.peek().type() == TokenType.RPAREN)
						if(unclosed_parens == 0)
						{
							// If we already closed all of our
							// parenthesis then we can't have a right
							// parenthesis or we have an invalid
							// expression. It might already be checked
							// for but I doubt it.
							
							// We don't do this inline because we have
							// a stupid 80 character line limit. I know
							// that absurdly long lines are terrible but
							// 80 characters is practically nothing...
							String str = "Found a parenthesis when we "
								+ "expected an operator during a sub "
								+ "expression evaluation.\n\tRemaining"
								+ "operators include " +
								operators.toString() + " and remaining "
								+ "operands include " +
								operands.toString() + ".";
							
							// Throw that exception!
							throw new ParseException(str);
						}
						else
							++unclosed_parens;
					
					// If we closed our parenthesis we don't need to do
					// anything else with the expression.
					if(unclosed_parens == 0)
						continue;
					else if(unclosed_parens < 0)
					{
						// If we had too many left parentheses in a row
						// we have an invalid expression. This shouldn't
						// happen but sanity checks are good.
						throw new ParseException("Unmatched parentheses" +
						"was found.\n\tA left parenthesis was found to " +
						"the right of its only viable right parenthesis" +
						".\n\tRemaining operands include " +
						operands.toString() + "\n\tRemaining " +
						"operators include " + operators.toString() + ".");
					}
					
					// We have something good to put on the stack so
					// why not go ahead an do that.
					tree.push(operators.pop());
				}
				
				// We will pass this to createTree so we can have
				// it evaluated for us.
				String sub_expr = "";
				
				// We have all of our Tokens in the corrent order
				// so all we have to do is fiddle through the
				// weird toString()s that we have provided.
				while(tree.size() > 0)
				{
					// We save this so we don't have to do any
					// annoying peek then pop stuff.
					Token tkn = tree.pop();
					
					// If we have an operand then we can use text()
					// to get the corrent value to send. Contrary to
					// popular opinion negation operation can have a
					// space before what they negate so we don't worry
					// about extra spaces. If we have an operator we
					// need to use the function we created earlier to
					// transform it into the TreeNode toString value.
					if(tkn.isOperand())
						sub_expr += tkn.text() + " ";
					else if(tkn.isOperator())
						if(tkn.type() == TokenType.NEGATION)
							sub_expr += "- ";
						else
							sub_expr += tokenStringToNodeString(tkn) + " ";
				}
				
				// We have an extra space in our expression.
				// While it technically doesn't matter it is bad form.
				sub_expr = sub_expr.substring(0,sub_expr.length() - 1);
				
				// Create the left node of the current operation or
				// create the root of the tree if we have our first
				// operator as a parenthesis.
				if(op.type() != TokenType.RPAREN)
					left = createTree(sub_expr);
				else
				{
					root = createTree(sub_expr);
					continue;
				}
			}
			// If we don't have a unary operator then we need to get
			// a second value (or left value) for our operation.
			// If [root] is null then we need to get our first value.
			else if(op.type() != TokenType.NEGATION || root == null)
				if(operands.size() < 1) // Sanity check.
				{
					// Add the current operator to our error.
					operators.push(op);
					
					throw new ParseException("We ran out of " +
					"operands.\n\tRemaining operators include " +
					operators.toString() + ".");
				}
				else // Get a our first or second value.
					left = operands.pop();
			
			// If [right] is null then we need to swap left into it.
			// We do this because negations use [right] as their
			// base value and also to preserve the order of the
			// expression if we need a second value.
			if(right == null)
			{
				// Move [left] to [right].
				right = left;
				
				// If we only have one value and we don't have a unary
				// operator we need a second one so grab it.
				if(op.type() != TokenType.NEGATION)
					left = operands.pop();
			}
			
			// If we have a higher priority than [root] we need to add
			// the current operation to the left of [root]. This never
			// overrides the left value of [root] because we use it
			// to generate this new operation along the way.
			if(getPriority(root) <
					getPriority(tokenStringToNodeString(op)) &&
					root != null && !(root != null &&
					root.type() == TokenType.NEGATION
					&& op.type() == TokenType.EXP))
				root.setLeftChild(getNode(op, left, right));
			else // We have a lower priority so make it the new [root].
				root = getNode(op, left, right);
		}
		
		if(operands.size() == 1 && root == null)
			// If we only have one operand then we should simply
			// return it. We didn't do this check earlier because
			// we had to make sure that the expression passes all
			// the requirements to be valid.
			return getNode(operands.pop());
		
		// Return our tree.
		return root;
	}
	
	/**
	 * Transforms tkn into something usable for getPriority.
	 * @param tkn a Token to who's String we should make into a
	 * Node's String.
	 * @return the String representation of an equivalent operation
	 * Node. Returns null if [tkn] is an invalid Token.
	 */
	private static String tokenStringToNodeString(Token tkn)
	{
		if(tkn == null)
			return null;
		
		String name = tkn.type().name();
		
		if(name.equals("TIMES"))
			return "*";
		else if(name.equals("DIV"))
			return "/";
		else if(name.equals("MINUS"))
			return "-";
		else if(name.equals("PLUS"))
			return "+";
		else if(name.equals("NEGATION"))
			return "NEG";
		else if(name.equals("EXP"))
			return "^";
		else if(name.equals("LPAREN"))
			return "(";
		else if(name.equals("RPAREN"))
			return ")";
		
		return null;
	}
	
	/**
	 * Creates a TreeNode from an operator and two operands.
	 * @param op the operation we will preform. if [op] is a negation
	 * operation then either left or right should be null.
	 * @param left the left side of the operation.
	 * @param right the right side of the operation.
	 * @return a TreeNode representing the operation of [left] [op] [right]
	 * @throws ParseException if [op] is not an operator or if [left] or
	 * [right] are not valid Tokens or TreeNodes.
	 */
	private static TreeNode getNode(Token op, Object left, Object right)
	throws ParseException
	{
		switch(op.type())
		{
		case DIV:
			return new DivNode(getNode(left), getNode(right));
		case EXP:
			return new ExpNode(getNode(left), getNode(right));
		case MINUS:
			return new MinusNode(getNode(left), getNode(right));
		case NEGATION:
			return new NegationNode(getNode(right == null ? left : right));
		case PLUS:
			return new PlusNode(getNode(left), getNode(right));
		case TIMES:
			return new TimesNode(getNode(left), getNode(right));
		default:
			throw new ParseException("An invalid operation was " +
			"attempted.\n\tGiven value was " + op.toString() + ".");
		}
	}
	
	/**
	 * Returns a TreeNode representation of [c].
	 * @param c the object to TreeNodeify
	 * @return if [c] is a Token returns an IntNode or IDNode
	 * depending on the contents of [c]. If [c] is TreeNode
	 * returns [c]. Returns null if [c] is of another type.
	 * @throws ParseException if [c] is not a valid Token.
	 */
	private static TreeNode getNode(Object c) throws ParseException
	{
		if(c.getClass() == Token.class)
		{
			switch(((Token)c).type())
			{
			case INT:
				try
				{
					new Integer(((Token)c).text());
				}
				catch(NumberFormatException e)
				{
					throw new ParseException("An INT Token could not " +
					"be parsed as an int.\n\tGiven value was " +
					((Token)c).toString() + ".");
				}
				
				return new IntNode(new Integer(((Token)c).text()));
			case ID:
				return new IDNode(((Token)c).text());
			default:
				throw new ParseException("An invalid constant was " +
					"attempted to be used.\n\tGiven value was" +
					((Token)c).toString() + ".");
			}
		}
		else if(c instanceof TreeNode)
			return (TreeNode)c;
		
		return null;
	}
}
