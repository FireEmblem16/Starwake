using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.ActivatedAbilities
{
	/// <summary>
	/// Represents an ability being used by a unit.
	/// </summary>
	public interface ActivatedAbility
	{
		/// <summary>
		/// Uses the ability on the given board.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit using this ability.</param>
		void Use(GameState state, Move m);

		/// <summary>
		/// Logs the ability use to the given window at the specified location.
		/// Returns the number of lines used.
		/// </summary>
		/// <param name="win">The window to output to.</param>
		/// <param name="output">The location to start output at.</param>
		/// <param name="name">The name of the unit using this ability.</param>
		/// <param name="target">The name (or description otherwise) of the target of this ability. Null if there is no target.</param>
		/// <returns>Returns the number of lines used.</returns>
		int Log(Window win, Pair<int,int> output, string name, string target = null);

		/// <summary>
		/// The target location of the ability.
		/// </summary>
		Pair<int,int> Target
		{get;}

		/// <summary>
		/// If true then this ability is an attack ability.
		/// </summary>
		bool IsAttack
		{get;}
	}
}
