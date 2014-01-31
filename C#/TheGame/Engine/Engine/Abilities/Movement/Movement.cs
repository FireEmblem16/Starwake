using System.Collections.Generic;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Movement
{
	/// <summary>
	/// The abstraction of different types of movement possible.
	/// </summary>
	public interface Movement : Ability
	{
		/// <summary>
		/// Obtains all the possible places this unit could move to from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can move to.</returns>
		List<Pair<int,int>> GetAvailableLocations(int x, int y);

		/// <summary>
		/// Obtains all the possible places this unit could move to from the given origin.
		/// Considerations will be made as to whether or not this unit can actually make it to a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider movement on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can move to.</returns>
		List<Pair<int,int>> GetAvailableLocations(Board b, int x, int y);

		/// <summary>
		/// How many spaces this unit can move in a turn.
		/// </summary>
		int MovementRange
		{get;}

		/// <summary>
		/// If true then this unit is allowed to move in the post combat movement phase.
		/// </summary>
		bool CanMovePostCombat
		{get;}
		
		/// <summary>
		/// If true then this unit can enter water tiles.
		/// </summary>
		bool CanSwim
		{get;}

		/// <summary>
		/// If true then 
		/// </summary>
		bool Mounted
		{get;}

		/// <summary>
		/// If true then this unit can fly and enter any tile not forbidden to flying units.
		/// </summary>
		bool CanFly
		{get;}
	}
}
