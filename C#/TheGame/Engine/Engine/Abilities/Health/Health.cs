using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TheGame.Engine.Abilities.Health
{
	/// <summary>
	/// The abstraction of how health is handled.
	/// </summary>
	public interface Health : Ability
	{
		/// <summary>
		/// Damages this unit.
		/// </summary>
		/// <param name="damage">The damage to deal to the unit. Negative damage will not heal the unit.</param>
		/// <returns>Returns the amount of HP the unit has after being damaged.</returns>
		int Damage(int damage);

		/// <summary>
		/// Heals this unit.
		/// </summary>
		/// <param name="health">How much health to heal.</param>
		/// <returns>Returns the amount of HP the unit has after being healed.</returns>
		int Heal(int health);

		/// <summary>
		/// Puts this unit at exactly the given health without any passive abilities being triggered.
		/// </summary>
		/// <param name="health">The health the unit is to have. If it is higher than the max it is lowered to the max.</param>
		void Resurrect(int health);

		/// <summary>
		/// How much health this unit has.
		/// </summary>
		int HP
		{get;}

		/// <summary>
		/// The maximum health this unit has.
		/// </summary>
		int MaxHP
		{get;}

		/// <summary>
		/// True if the unit is dead and false otherwise.
		/// </summary>
		bool Dead
		{get;}
	}
}
