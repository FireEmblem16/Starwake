﻿using System.Collections.Generic;
using Engine.Game;

namespace Engine.Player.AI.DecisionTree
{
	/// <summary>
	/// A sequencer node in a decision tree.
	/// Satisfied if all children are satisfied and exits immediately if any one child is found to be unsatisfied.
	/// </summary>
	/// <remarks>Like an and gate.</remarks>
	public class Sequencer : Node
	{
		/// <summary>
		/// Initializes this sequencer.
		/// </summary>
		/// <param name="to_decorate">If this is not null then this node will decorate the given node for evaluation.</param>
		public Sequencer(Node to_decorate = null) : base(to_decorate,false)
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
				if(!n.Evaluate(state,ref move))
				{
					move = null; // Just in case it was set somewhere
					return false;
				}

			return true;
		}

		/// <summary>
		/// Creates a deep copy of this AIBehavior.
		/// </summary>
		/// <returns>Returns a copy of this AIBehavior.</returns>
		public override AIBehavior Clone()
		{
			Sequencer ret = new Sequencer(InnerNode);
			List<Node> children = new List<Node>();

			foreach(Node n in Children)
				children.Add(n.Clone() as Node);

			ret.AddChildren(children);
			return ret;
		}
	}
}
