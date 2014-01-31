using TheGame.Engine.Abilities.PassiveAbilities.Ailment;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.PassiveAbilities.AttackEnhancement
{
	/// <summary>
	/// Represents a weapon poison that deals damage over time until healed.
	/// At least one damage must be dealt for the poison to take effect.
	/// </summary>
	public class Poison : PassiveAbility
	{
		/// <summary>
		/// Creates a new poison ability.
		/// </summary>
		public Poison()
		{
			Level = 1;
			return;
		}

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
		{return new Poison();}

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
		{
			if(Level < MaxLevel)
			{
				Level++;
				return true;
			}

			return false;
		}
		
		/// <summary>
		/// Decreases the level of the ability by one if able.
		/// </summary>
		/// <returns>Returns true if the ability was deleveled and false otherwise.</returns>
		public bool Unlevel()
		{
			if(Level > 1)
			{
				Level--;
				return true;
			}

			return false;
		}

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
		{} // The attack itself is not poison damage so do not add the type

		/// <summary>
		/// Called after an attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that damage was dealt to.</param>
		/// <param name="amount">The amount of damage that was dealt.</param>
		public void AfterAttack(GameState state, Move m, Unit u, float amount)
		{
			if(amount > 0)
				u.AddAbility(new Poisoned(Level));

			return;
		}

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
		{return;}

		/// <summary>
		/// Called at the end of the turn.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public void EndOfTurn(GameState state, Move m)
		{return;}

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
			{return "Poisoned Attack\n---------------\n\tIt's been said that poison is the weapon of cravens, women and cripples but it's also been said that all's fair in love and war. A little bit of poison to add bite to attacks may be dishonorable but will certainly get the job done.\n\tEach level of this ability grants a single damage of lasting poison damage each round. Once injured by this ability for a positive amount of damage the wounded will be poisoned until dead or cleansed of poison.\n\tMost healing abilities will cure poison.\n\nCost: 5 points per level\nMax Level: 10";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Poisoned Attack";

		/// <summary>
		/// The level the ability is currently at.
		/// </summary>
		public int Level
		{get; protected set;}

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		public int MaxLevel
		{
			get
			{return 10;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public int PointCost
		{
			get
			{return 5 * Level;}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public int PointCostToNextLevel
		{
			get
			{
				if(Level < MaxLevel)
					return 5;

				return -1;
			}
		}

		/// <summary>
		/// If true then this ability is visible to the user when editing.
		/// </summary>
		public bool VisibleToUser
		{
			get
			{return true;}
		}
	}
}
