using System.Collections.Generic;
using TheGame.Engine.Abilities.ActivatableAbility;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.ActivatableAbility.Attack
{
	/// <summary>
	/// Outlines the attacking ability of this unit.
	/// </summary>
	public interface Attack : ActivationAbility
	{
		/// <summary>
		/// Obtains all the possible places this unit could attack to from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can attack to.</returns>
		List<Pair<int,int>> GetAttackLocations(int x, int y);

		/// <summary>
		/// Obtains all the possible places this unit could attack from the given origin.
		/// Considerations will be made as to whether or not this unit can actually attack to a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider the attack on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can attack.</returns>
		List<Pair<int,int>> GetAttackLocations(Board b, int x, int y);

		/// <summary>
		/// The amount of damage this attack deals without other buffs and debuffs.
		/// </summary>
		int AttackDamage
		{get;}

		/// <summary>
		/// The type(s) of this attack not considering additional modifiers such as being mounted, grounded, flying, a necromancer and so on.
		/// </summary>
		ATTACK_TYPE AttackType
		{get;}

		/// <summary>
		/// If true then this is a ranged attack.
		/// </summary>
		/// <remarks>Note that it is also possible to determine if the attack is ranged from its type.</remarks>
		bool IsRanged
		{get;}
	}
}
