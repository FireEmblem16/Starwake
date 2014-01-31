using System.Collections.Generic;
using Engine.Game;

namespace Engine.Player.AI.DecisionTree
{
	/// <summary>
	/// Checks if a condition in the game state is satisfied.
	/// </summary>
	/// <remarks>A terminal node.</remarks>
	public class Condition : Node
	{
		/// <summary>
		/// Creates a new condition checking node.
		/// </summary>
		public Condition(ConditionCheck satisfaction) : base() // This node can not decorate anything and it is also a terminal node
		{
			sat = satisfaction;
			return;
		}

		/// <summary>
		/// Evaluates this node in the decision tree.
		/// If the return value is true then move will contain the desired move (if any).
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="move">If this node wants to make a move then it will be placed in this parameter.</param>
		/// <returns>Returns true if this node is satisfied and false otherwise.</returns>
		/// <remarks>Although move must be initialized, that does not mean it is not null.</remarks>
		protected internal override bool Evaluate(GameState state, ref Move move)
		{return sat(state,ref move);}

		/// <summary>
		/// Creates a deep copy of this AIBehavior.
		/// </summary>
		/// <returns>Returns a copy of this AIBehavior.</returns>
		public override AIBehavior Clone()
		{
			Condition ret = new Condition(sat);
			List<Node> children = new List<Node>();

			foreach(Node n in Children)
				children.Add(n.Clone() as Node);

			ret.AddChildren(children);
			return ret;
		}

		/// <summary>
		/// What needs to be satisfied for the condition to be true.
		/// </summary>
		protected ConditionCheck sat;
	}

	/// <summary>
	/// Determines if a condition in the game state is met.
	/// </summary>
	/// <param name="state">The current game state.</param>
	/// <param name="move">The move to make. If a move should be made based on this condition return that move here.</param>
	/// <returns>Returns true if the condition was satisfied and false if it was not.</returns>
	public delegate bool ConditionCheck(GameState state, ref Move move);
}
