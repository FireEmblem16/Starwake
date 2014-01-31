using System;
using System.Collections.Generic;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.ActivatedAbilities.Healing
{
	/// <summary>
	/// Represents a use of a touch healing spell.
	/// </summary>
	public class TouchHealingActivation : ActivatedAbility
	{
		/// <summary>
		/// Creates a new activation of a touch healing spell.
		/// </summary>
		/// <param name="target">The target location of the healing spell.</param>
		/// <param name="damage">The basic damage to be healed before passive abilities and damage modifiers take effect.</param>
		public TouchHealingActivation(Pair<int,int> target, int damage)
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
			List<Unit> allies = new List<Unit>();
			Unit temp = null;

			// Look for adjacent allies
			// Note that the get functions do not throw out of bounds errors
			// Check west
			temp = state.Map.GetAllyAt(new Pair<int,int>(Target.val1 - 1,Target.val2));

			if(temp != null)
				allies.Add(temp);

			// Check east
			temp = state.Map.GetAllyAt(new Pair<int,int>(Target.val1 + 1,Target.val2));

			if(temp != null)
				allies.Add(temp);

			// Check north
			temp = state.Map.GetAllyAt(new Pair<int,int>(Target.val1,Target.val2 - 1));

			if(temp != null)
				allies.Add(temp);

			// Check south
			temp = state.Map.GetAllyAt(new Pair<int,int>(Target.val1,Target.val2 + 1));

			if(temp != null)
				allies.Add(temp);

			// Check if we have anything to heal
			if(allies.Count == 0)
				return; // Ability missed so nothing happens

			ATTACK_TYPE dummy = ATTACK_TYPE.TYPELESS;
			float damage = 0.0f;
			DamageRecovered = 0;

			// Loop for every ally
			foreach(Unit u in allies)
			{
				// Reset how much is healed
				damage = Recovery;

				// Trigger preheal passives
				u.TriggerPassives(TurnPhase.BEFORE_HEAL,state,m,ref damage,ref dummy,u);

				// Record recovery
				DamageRecovered += (int)damage < 0 ? 0 : (int)damage;

				// Perform the actual healing and trigger postheal passives
				u.AbilityHealth.Heal((int)damage);
				u.TriggerPassives(TurnPhase.AFTER_HEAL,state,m,ref damage,ref dummy,u);
			}

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
				win.Write(name + " healed some lillies.");
			else
				win.Write(name + " healed " + target + " for a total of " + DamageRecovered + " HP.");

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
