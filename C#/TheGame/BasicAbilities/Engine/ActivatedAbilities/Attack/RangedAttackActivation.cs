using System;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.ActivatedAbilities.Attack
{
	/// <summary>
	/// Represents the activation of a ranged attack.
	/// </summary>
	public class RangedAttackActivation : NormalAttackActivation
	{
		/// <summary>
		/// Creates a new activation of a normal attack.
		/// </summary>
		/// <param name="target">The target location of the attack.</param>
		/// <param name="damage">The basic damage to be dealt before passive abilities and damage modifiers take effect.</param>
		/// <param name="type">The type of damage being dealt before passive abilities take effect.</param>
		public RangedAttackActivation(Pair<int,int> target, int damage, ATTACK_TYPE type) : base(target,damage,type)
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
		public override int Log(Window win, Pair<int,int> output, string name, string target = null)
		{
			win.CursorPosition = output;
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);

			if(target == null)
				win.Write(name + " fired at (" + Target.val1 + "," + Target.val2 + ") and missed.");
			else
				win.Write(name + " fired at " + target + " for " + DamageDealt + " damage.");

			return 1;
		}
	}
}
