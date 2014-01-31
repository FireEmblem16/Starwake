using System.Collections.Generic;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.ActivatableAbility.Healing
{
	/// <summary>
	/// Outlines the healing abilities of that a unit can learn.
	/// </summary>
	public interface HealSpell : ActivationAbility
	{
		/// <summary>
		/// Obtains all the possible places this unit could heal from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can heal.</returns>
		List<Pair<int,int>> GetHealLocations(int x, int y);

		/// <summary>
		/// Obtains all the possible places this unit could heal from the given origin.
		/// Considerations will be made as to whether or not this unit can actually heal a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider the spell on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can heal.</returns>
		List<Pair<int,int>> GetHealLocations(Board b, int x, int y);

		/// <summary>
		/// The amount of damage this spell heals without other buffs and debuffs.
		/// </summary>
		int RecoveryAmount
		{get;}

		/// <summary>
		/// If true then this is a ranged spell.
		/// </summary>
		bool IsRanged
		{get;}
	}
}
