using System;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.ActivatedAbilities.Healing
{
	/// <summary>
	/// Represents the use of a ranged healing spell.
	/// </summary>
	public class RangedHealingActivation : ActivatedAbility
	{
		/// <summary>
		/// Creates a new activation of a touch healing spell.
		/// </summary>
		/// <param name="target">The target location of the healing spell.</param>
		/// <param name="damage">The basic damage to be healed before passive abilities and damage modifiers take effect.</param>
		public RangedHealingActivation(Pair<int,int> target, int damage)
		{
			Target = target;
			Recovery = damage;

			DamageRecovered = -1;
			return;
		}

		/// <summary>
		/// Uses the ability on the given board.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit using this ability.</param>
		public void Use(GameState state, Move m)
		{
			Unit ally = state.Map.GetAllyAt(Target);

			if(ally == null || ally == m.Actor) // A unit can not heal itself
				return; // Ability missed so nothing happens

			ATTACK_TYPE dummy = ATTACK_TYPE.TYPELESS;
			float damage = Recovery;
			
			// Trigger preheal passives
			ally.TriggerPassives(TurnPhase.BEFORE_HEAL,state,m,ref damage,ref dummy,ally);

			// Record recovery
			DamageRecovered = (int)damage < 0 ? 0 : (int)damage;

			// Perform the actual healing and trigger postheal passives
			ally.AbilityHealth.Heal(DamageRecovered);
			ally.TriggerPassives(TurnPhase.AFTER_HEAL,state,m,ref damage,ref dummy,ally);
			
			return;
		}

		/// <summary>
		/// Logs the ability use to the given window at the specified location.
		/// Returns the number of lines used.
		/// </summary>
		/// <param name="win">The window to output to.</param>
		/// <param name="output">The location to start output at.</param>
		/// <param name="name">The name of the unit using this ability.</param>
		/// <param name="target">The name (or description otherwise) of the target of this ability. Null if there is no target.</param>
		/// <returns>Returns the number of lines used.</returns>
		public virtual int Log(Window win, Pair<int,int> output, string name, string target = null)
		{
			win.CursorPosition = output;
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);

			if(target == null)
				win.Write(name + " healed some lillies at (" + Target.val1 + "," + Target.val2 + ").");
			else
				win.Write(name + " healed " + target + " for " + DamageRecovered + " HP.");

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

		/// <summary>
		/// The basic damage of this attack before passive effects are added.
		/// </summary>
		public int Recovery
		{get; protected set;}

		/// <summary>
		/// Contains the actual amount of health recovered.
		/// If negative then no damage has been dealt yet or the unit did not heal anything.
		/// </summary>
		public int DamageRecovered
		{get; protected set;}
	}
}
