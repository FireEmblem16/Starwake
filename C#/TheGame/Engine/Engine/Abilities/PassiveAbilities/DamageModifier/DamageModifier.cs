using System;

namespace TheGame.Engine.Abilities.PassiveAbilities.DamageModifier
{
	/// <summary>
	/// Represents a weakness or protection against certain kinds of attacks.
	/// </summary>
	public interface DamageModifier : PassiveAbility
	{}
	
	/// <summary>
	/// The different types of attacks.
	/// </summary>
	[Flags]
	public enum ATTACK_TYPE
	{
		TYPELESS = 0x00,
		GROUND = 0x01,
		MOUNTED = 0x02,
		FLYING = 0x04,
		MARINE = 0x08,
		REGICIDE = 0x10,
		RANGED = 0x20,
		NECROMANCER = 0x40,
		POISON = 0x80
	}
}
