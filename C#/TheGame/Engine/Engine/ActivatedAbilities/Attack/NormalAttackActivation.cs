using System;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.ActivatedAbilities.Attack
{
	/// <summary>
	/// Represents an activation of a normal attack.
	/// </summary>
	public class NormalAttackActivation : ActivatedAbility
	{
		/// <summary>
		/// Creates a new activation of a normal attack.
		/// </summary>
		/// <param name="target">The target location of the attack.</param>
		/// <param name="damage">The basic damage to be dealt before passive abilities and damage modifiers take effect.</param>
		/// <param name="type">The type of damage being dealt before passive abilities take effect.</param>
		public NormalAttackActivation(Pair<int,int> target, int damage, ATTACK_TYPE type)
		{
			Target = target;
			Damage = damage;
			BasicType = type;

			DamageDealt = -1;
			EffectiveType = ATTACK_TYPE.TYPELESS;
			return;
		}

		/// <summary>
		/// Uses the ability on the given board.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit using this ability.</param>
		public void Use(GameState state, Move m)
		{
			Unit enemy = state.Map.GetEnemyAt(Target);

			if(enemy == null)
				return; // Attack missed so nothing happens

			float damage = Damage;
			ATTACK_TYPE type = BasicType;

			// Trigger precombat passive abilities
			m.Actor.TriggerPassives(TurnPhase.BEFORE_ATTACK,state,m,ref damage,ref type,enemy); // Extra parameters are not used
			enemy.TriggerPassives(TurnPhase.BEFORE_DAMAGE,state,m,ref damage,ref type,m.Actor);
			
			// Record information
			DamageDealt = (int)damage < 0 ? 0 : (int)damage; // We round down so this is fine
			EffectiveType = type;
			
			// Deal damage and trigger post combat abilities
			enemy.AbilityHealth.Damage(DamageDealt);
			enemy.TriggerPassives(TurnPhase.AFTER_DAMAGE,state,m,ref damage,ref type,m.Actor);
			m.Actor.TriggerPassives(TurnPhase.AFTER_ATTACK,state,m,ref damage,ref type,enemy);

			// If we killed the enemy, trigger the kill/death passive abilities and then remove it from the board
			if(enemy.AbilityHealth.Dead)
			{
				m.Actor.TriggerPassives(TurnPhase.ON_KILL,state,m,ref damage,ref type,enemy);
				enemy.TriggerPassives(TurnPhase.ON_DEATH,state,m,ref damage,ref type,m.Actor);
				
				state.Map.RemoveEnemy(Target); // Move still holds a unit reference for the dead unit's move so we can do this now
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
				win.Write(name + " swung at air.");
			else
				win.Write(name + " attacked " + target + " for " + DamageDealt + " damage.");

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
			{return true;}
		}

		/// <summary>
		/// The basic damage of this attack before passive effects are added.
		/// </summary>
		public int Damage
		{get; protected set;}

		/// <summary>
		/// The attack type of this attack before passive effects are added.
		/// </summary>
		public ATTACK_TYPE BasicType
		{get; protected set;}

		/// <summary>
		/// Contains the actual amount of damage done.
		/// If negative then no damage has been dealt yet or the unit did not hit anything.
		/// </summary>
		public int DamageDealt
		{get; protected set;}

		/// <summary>
		/// Contains the type of damage dealt.
		/// This value is typeless until the attack is used. DamageDealt will be non-negative once the attack is used.
		/// </summary>
		public ATTACK_TYPE EffectiveType
		{get; protected set;}
	}
}
