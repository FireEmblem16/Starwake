using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.Tiles;

namespace TheGame.Engine.Abilities.PassiveAbilities
{
	/// <summary>
	/// Represents an ability that requires no activation to use.
	/// </summary>
	public interface PassiveAbility : Ability
	{
		/// <summary>
		/// Called before the movement phase.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void BeforeMove(GameState state, Move m);

		/// <summary>
		/// Called after the movement phase.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void AfterMove(GameState state, Move m);

		/// <summary>
		/// Called before a non-attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void BeforeAbility(GameState state, Move m);

		/// <summary>
		/// Called after a non-attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void AfterAbility(GameState state, Move m);

		/// <summary>
		/// Called before an attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void BeforeAttack(GameState state, Move m, ref float strength, ref ATTACK_TYPE type);

		/// <summary>
		/// Called after an attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that damage was dealt to.</param>
		/// <param name="amount">The amount of damage that was dealt.</param>
		void AfterAttack(GameState state, Move m, Unit u, float amount);

		/// <summary>
		/// Called before damage is dealt to this unit.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void BeforeDamage(GameState state, Move m, ref float damage, ATTACK_TYPE type);

		/// <summary>
		/// Called after damage is dealt to this unit.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that dealt the damage.</param>
		/// <param name="damage">The amount of damage that was dealt.</param>
		void AfterDamage(GameState state, Move m, Unit u, float damage);

		/// <summary>
		/// Called before the unit is healed.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="amount">The amount to be healed.</param>
		void BeforeHeal(GameState state, Move m, ref float amount);

		/// <summary>
		/// Called after the unit is healed.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="amount">The amount of health that was healed.</param>
		void AfterHeal(GameState state, Move m, float amount);

		/// <summary>
		/// Called before the post movement phase if this unit can move during it.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void BeforePostMove(GameState state, Move m);

		/// <summary>
		/// Called after the post movement phase if this unit can move during it.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void AfterPostMove(GameState state, Move m);

		/// <summary>
		/// Called when the owning unit is killed.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit killing this unit.</param>
		void OnDeath(GameState state, Move m, Unit u);

		/// <summary>
		/// Called when the owning unit kills another unit.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		/// <param name="u">The unit that was killed by the owning unit.</param>
		void OnKill(GameState state, Move m, Unit u);

		/// <summary>
		/// Called at the end of the turn.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		void EndOfTurn(GameState state, Move m);
	}
}
