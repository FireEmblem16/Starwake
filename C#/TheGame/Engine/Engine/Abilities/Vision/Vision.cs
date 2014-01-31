using System.Collections.Generic;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Vision
{
	/// <summary>
	/// The outline of the sight ability.
	/// </summary>
	public interface Vision : Ability
	{
		/// <summary>
		/// Obtains all the possible places this unit can see to from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can see.</returns>
		List<Pair<int,int>> GetVisibleLocations(int x, int y);

		/// <summary>
		/// Obtains all the possible places this unit can see to from the given origin.
		/// Considerations will be made as to whether or not this unit can actually see to a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider sight on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can see.</returns>
		List<Pair<int,int>> GetVisibleLocations(Board b, int x, int y);

		/// <summary>
		/// How far the unit can see.
		/// </summary>
		int SightRadius
		{get;}

		/// <summary>
		/// How far the unit can possibly see.
		/// </summary>
		int MaxRadius
		{get;}

		/// <summary>
		/// If true then this unit can see invisible units.
		/// </summary>
		bool SeeInvisible
		{get;}
	}
}
