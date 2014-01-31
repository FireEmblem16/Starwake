using System.Collections.Generic;
using Engine.Game;

namespace Engine.Player.AI.DecisionTree
{
	/// <summary>
	/// A selector node in a decision tree.
	/// Satisfied if any child is found to be satisfied. Returns after the first satisfied child is found.
	/// </summary>
	/// <remarks>Like an or gate.</remarks>
	public class Selector : Node
	{
		/// <summary>
		/// Initializes this selector.
		/// </summary>
		/// <param name="to_decorate">If this is not null then this node will decorate the given node for evaluation.</param>
		public Selector(Node to_decorate = null) : base(to_decorate,false)
		{return;}

		/// <summary>
		/// Evaluates this node in the decision tree.
		/// If the return value is true then move will contain the desired move (if any).
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="move">If this node wants to make a move then it will be placed in this parameter.</param>
		/// <returns>Returns true if this node is satisfied and false otherwise.</returns>
		/// <remarks>Although move must be initialized, that does not mean it is not null.</remarks>
		protected internal override bool Evaluate(GameState state, ref Move move)
		{
			foreach(Node n in Children)
				if(n.Evaluate(state,ref move))
					return true;

			move = null; // Just in case it was set somewhere
			return false;
		}

		/// <summary>
		/// Creates a deep copy of this AIBehavior.
		/// </summary>
		/// <returns>Returns a copy of this AIBehavior.</returns>
		public override AIBehavior Clone()
		{
			Selector ret = new Selector(InnerNode);
			List<Node> children = new List<Node>();

			foreach(Node n in Children)
				children.Add(n.Clone() as Node);

			ret.AddChildren(children);
			return ret;
		}
	}
}
