using System;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.ActivatedAbilities.Miscellaneous
{
	/// <summary>
	/// A punishment ability that does nothing.
	/// In general it can be expected that the ability is used when an invalid order was attempted.
	/// </summary>
	public class Hold : ActivatedAbility
	{
		/// <summary>
		/// Creates a new hold command.
		/// </summary>
		/// <param name="target">The target of the command. Has no meaning except for external code referencing it.</param>
		public Hold(Pair<int,int> target)
		{
			Target = target;
			return;
		}

		/// <summary>
		/// Uses the ability on the given board.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit using this ability.</param>
		public void Use(GameState state, Move m)
		{return;}

		/// <summary>
		/// Logs the ability use to the given window at the specified location.
		/// Returns the number of lines used.
		/// </summary>
		/// <param name="win">The window to output to.</param>
		/// <param name="output">The location to start output at.</param>
		/// <param name="name">The name of the unit using this ability.</param>
		/// <param name="target">The name (or description otherwise) of the target of this ability. Null if there is no target.</param>
		/// <returns>Returns the number of lines used.</returns>
		public int Log(Window win, Pair<int,int> output, string name, string target = null)
		{
			win.CursorPosition = output;
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			
			win.Write(name + " was forced to hold.");
			return 1;
		}

		/// <summary>
		/// The target location of the ability.
		/// </summary>
		public Pair<int,int> Target
		{get; protected set;}

		/// <summary>
		/// If true then this ability is an attack ability.
		/// </summary>
		public bool IsAttack
		{
			get
			{return false;}
		}
	}
}
