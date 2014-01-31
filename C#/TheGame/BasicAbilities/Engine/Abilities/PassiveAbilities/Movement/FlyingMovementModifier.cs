using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.PassiveAbilities.Movement
{
	/// <summary>
	/// The damage modifying ability for flying units.
	/// </summary>
	public class FlyingMovementModifier : GroundMovementModifier
	{
		/// <summary>
		/// Creates a new flying movement modifier.
		/// </summary>
		public FlyingMovementModifier()
		{return;}

		/// <summary>
		/// Called before an attack ability is used.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="m">The current move of the unit.</param>
		public override void BeforeAttack(GameState state, Move m, ref float strength, ref ATTACK_TYPE type)
		{
			type |= ATTACK_TYPE.FLYING;
			return;
		}

		/// <summary>
		/// The name of the ability.
		/// </summary>
		public override string Name
		{
			get
			{return name;}
		}

		/// <summary>
		/// Documents and explains how the ability works, what it does and the costs.
		/// </summary>
		public override string Documentation
		{
			get
			{return "Flying Type\n-----------\nRepresents that the owner of this ability fights with flying attacks.";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected new const string name = "Flying Type";
	}
}
