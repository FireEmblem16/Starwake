using System;
using TheGame.Engine.ActivatedAbilities;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine
{
	/// <summary>
	/// Represents a move by a unit in a game.
	/// </summary>
	public class Move
	{
		/// <summary>
		/// Creates a new move.
		/// </summary>
		/// <param name="u">The unit that is making the move.</param>
		/// <param name="from">Where the unit is moving from.</param>
		/// <param name="to">Where the unit is moving to.</param>
		/// <param name="a">The ability the unit wants to use.</param>
		public Move(Unit u, Pair<int,int> from, Pair<int,int> to, ActivatedAbility a)
		{
			Actor = u;
			Origin = from;
			Destination = to;
			Ability = a;

			PostCombatDestination = null;
			return;
		}

		/// <summary>
		/// Logs the move to the given window at the specified location.
		/// Returns the number of lines used.
		/// </summary>
		/// <param name="win">The window to output to.</param>
		/// <param name="output">The location to start output at.</param>
		/// <returns>Returns the number of lines used.</returns>
		public int Log(Window win, Pair<int,int> output)
		{
			win.CursorPosition = output;
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			
			if(Stationary)
				win.Write(Actor.Name + " fortifies their position.");
			else
				win.Write(Actor.Name + " moves from (" + Origin.val1 + "," + Origin.val2 + ") to (" + Destination.val1 + "," + Destination.val2 + ").");

			return Ability.Log(win,new Pair<int,int>(output.val1,output.val2 + 1),Actor.Name) + 1;
		}

		/// <summary>
		/// The unit this move is for.
		/// </summary>
		public Unit Actor
		{get; protected set;}

		/// <summary>
		/// The location of the unit before moving.
		/// </summary>
		public Pair<int,int> Origin
		{get; protected set;}

		/// <summary>
		/// The location the unit desires to move to.
		/// </summary>
		public Pair<int,int> Destination
		{get; protected set;}

		/// <summary>
		/// If null then no post combat movement was made and otherwise contains the desired location.
		/// </summary>
		public Pair<int,int> PostCombatDestination
		{get; set;}

		/// <summary>
		/// If true then this unit does not move with this move.
		/// If false then this unit is (at least attempting) moving this turn.
		/// </summary>
		public bool Stationary
		{
			get
			{
				if(Origin.Equals(Destination))
					return true;

				return false;
			}
		}

		/// <summary>
		/// The ability the unit wants to use during this move.
		/// Often this will be Attack.
		/// </summary>
		public ActivatedAbility Ability
		{get; protected set;}
	}
}
