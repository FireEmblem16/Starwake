using System;
using System.Collections.Generic;
using TheGame.Engine.Abilities.PassiveAbilities.Ailment;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.PassiveAbilities.AttackEnhancement
{
	/// <summary>
	/// Represents a unit's ability to zombify units when killing them.
	/// </summary>
	public class Necromancy : PassiveAbility
	{
		/// <summary>
		/// Creates a new necromancy ability.
		/// </summary>
		public Necromancy()
		{return;}

		/// <summary>
		/// Registers this ability with the ability factory.
		/// </summary>
		public static void Initialize()
		{
			AbilityFactory.RegisterAbility(new Pair<string,GenerateAbility>(name,Create));
			return;
		}

		/// <summary>
		/// Creates a new ability of this type.
		/// </summary>
		protected static Ability Create()
		{return new Necromancy();}

		/// <summary>
		/// Called when the ability is added to a unit.
		/// </summary>
		/// <param name="u">The unit this ability is added to.</param>
		public void OnAdd(Unit u)
		{return;}

		/// <summary>
		/// Called when the ability is removed from a unit.
		/// </summary>
		/// <param name="u">The unit this ability is removed from.</param>
		public void OnRemove(Unit u)
		{return;}

		/// <summary>
		/// Increases the level of the ability by one if able.
		/// </summary>
		/// <returns>Returns true if the ability was leveled up and false otherwise.</returns>
		public bool LevelUp()
		{return false;}
		
		/// <summary>
		/// Decreases the level of the ability by one if able.
		/// </summary>
		/// <returns>Returns true if the ability was deleveled and false otherwise.</returns>
		public bool Unlevel()
		{return false;}

		/// <summary>
		/// Called before the movement phase.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void BeforeMove(GameState state, Move m)
		{return;}

		/// <summary>
		/// Called after the movement phase.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void AfterMove(GameState state, Move m)
		{return;}

		/// <summary>
		/// Called before a non-attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void BeforeAbility(GameState state, Move m)
		{return;}

		/// <summary>
		/// Called after a non-attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void AfterAbility(GameState state, Move m)
		{return;}

		/// <summary>
		/// Called before an attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public virtual void BeforeAttack(GameState state, Move m, ref float strength, ref ATTACK_TYPE type)
		{
			type |= ATTACK_TYPE.NECROMANCER;
			return;
		}

		/// <summary>
		/// Called after an attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that damage was dealt to.</param>
		/// <param name="amount">The amount of damage that was dealt.</param>
		public void AfterAttack(GameState state, Move m, Unit u, float amount)
		{return;}

		/// <summary>
		/// Called before damage is dealt to this unit.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void BeforeDamage(GameState state, Move m, ref float damage, ATTACK_TYPE type)
		{return;}

		/// <summary>
		/// Called after damage is dealt to this unit.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that dealt the damage.</param>
		/// <param name="damage">The amount of damage that was dealt.</param>
		public void AfterDamage(GameState state, Move m, Unit u, float damage)
		{return;}

		/// <summary>
		/// Called before the unit is healed.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="amount">The amount to be healed.</param>
		public void BeforeHeal(GameState state, Move m, ref float amount)
		{return;}

		/// <summary>
		/// Called after the unit is healed.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="amount">The amount of health that was healed.</param>
		public void AfterHeal(GameState state, Move m, float amount)
		{return;}

		/// <summary>
		/// Called before the post movement phase if this unit can move during it.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void BeforePostMove(GameState state, Move m)
		{return;}

		/// <summary>
		/// Called after the post movement phase if this unit can move during it.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void AfterPostMove(GameState state, Move m)
		{return;}

		/// <summary>
		/// Called when the owning unit is killed.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit killing this unit.</param>
		public void OnDeath(GameState state, Move m, Unit u)
		{return;}

		/// <summary>
		/// Called when the owning unit kills another unit.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that was killed by the owning unit.</param>
		public void OnKill(GameState state, Move m, Unit u)
		{
			// We need to wait until EOT so that any dying ally units are removed from the board (and this unit as well)
			// Also we can't zombify a zombie nor can we zombify something with one health so don't bother trying
			if(!u.HasAbility(typeof(Zombified)) && u.AbilityHealth.MaxHP > 1)
				KilledUnit = u;

			return;
		}

		/// <summary>
		/// Called at the end of the turn.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void EndOfTurn(GameState state, Move m)
		{
			// Check if we have killed anything this turn
			if(KilledUnit == null)
				return;

			// The enemy has already been removed so we just need to find a place to put it and heal it to half health
			List<Pair<int,int>> free_locs = new List<Pair<int,int>>();
			
			if(m.Ability.Target.val2 - 1 >= 0)
				if(state.Map.GetAllyAt(new Pair<int,int>(m.Ability.Target.val1,m.Ability.Target.val2 - 1)) == null)
					free_locs.Add(new Pair<int,int>(m.Ability.Target.val1,m.Ability.Target.val2 - 1));
			
			if(m.Ability.Target.val2 + 1 < state.Map.Height)
				if(state.Map.GetAllyAt(new Pair<int,int>(m.Ability.Target.val1,m.Ability.Target.val2 + 1)) == null)
					free_locs.Add(new Pair<int,int>(m.Ability.Target.val1,m.Ability.Target.val2 + 1));

			if(m.Ability.Target.val1 - 1 >= 0)
				if(state.Map.GetAllyAt(new Pair<int,int>(m.Ability.Target.val1 - 1,m.Ability.Target.val2)) == null)
					free_locs.Add(new Pair<int,int>(m.Ability.Target.val1 - 1,m.Ability.Target.val2));

			if(m.Ability.Target.val1 + 1 < state.Map.Width)
				if(state.Map.GetAllyAt(new Pair<int,int>(m.Ability.Target.val1 + 1,m.Ability.Target.val2)) == null)
					free_locs.Add(new Pair<int,int>(m.Ability.Target.val1 + 1,m.Ability.Target.val2));

			// If we have no free space then we don't get a zombie
			if(free_locs.Count == 0)
				return;

			// Pick a random location to zombify at
			state.Map.AddAllyAt(KilledUnit,free_locs[state.rand.Next(free_locs.Count)]);
			
			// Get the zombie abilities sorted out
			KilledUnit.RemoveAllStatusEffects(); // Note that zombies can have status effects normally but when zombified it is cured of all of them
			KilledUnit.AbilityHealth.Resurrect(KilledUnit.AbilityHealth.MaxHP / 2);
			KilledUnit.AddAbility(new Zombified());

			KilledUnit = null;
			return;
		}

		/// <summary>
		/// The name of the ability.
		/// </summary>
		public string Name
		{
			get
			{return name;}
		}

		/// <summary>
		/// Documents and explains how the ability works, what it does and the costs.
		/// </summary>
		public string Documentation
		{
			get
			{return "Necromancy\n----------\n\tNecromancers are feared, shunned and murdered wherever they go. That is, until enemies come calling. Then lords are only too happy to have them hanging around on their side of battle and let their enemies destroy each other. Of course, they always pretend that they are begrudging in the employ of necromancy.\n\tWhen a necromancer is the one to deal a final blow to an enemy it is revived in an adjacent available location with half health. If none of the four adjacent locations are available to place a new ally then no zombie is created.\n\tZombies heal at half the normal rate and take 3/4 damage from non-marine attacks.\n\nCost: 42 points";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Necromancy";

		/// <summary>
		/// The level the ability is currently at.
		/// </summary>
		public int Level
		{
			get
			{return 1;}
		}

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		public int MaxLevel
		{
			get
			{return 1;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public int PointCost
		{
			get
			{return 42;}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public int PointCostToNextLevel
		{
			get
			{return -1;}
		}

		/// <summary>
		/// If true then this ability is visible to the user when editing.
		/// </summary>
		public bool VisibleToUser
		{
			get
			{return true;}
		}

		/// <summary>
		/// A place to store the unit being killed on a turn.
		/// </summary>
		protected Unit KilledUnit = null;
	}
}
