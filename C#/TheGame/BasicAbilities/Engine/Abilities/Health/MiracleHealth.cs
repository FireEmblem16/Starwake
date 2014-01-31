using TheGame.Utility;

namespace TheGame.Engine.Abilities.Health
{
	/// <summary>
	/// A health system where the unit survives with 1 HP if it would be killed while it had more than 1 HP.
	/// </summary>
	public class MiracleHealth : NormalHealth
	{
		/// <summary>
		/// Creates a new miracle health ability.
		/// </summary>
		public MiracleHealth() : base()
		{return;}

		/// <summary>
		/// Registers this ability with the ability factory.
		/// </summary>
		public static new void Initialize()
		{
			AbilityFactory.RegisterAbility(new Pair<string,GenerateAbility>(name,Create));
			return;
		}

		/// <summary>
		/// Creates a new ability of this type.
		/// </summary>
		protected static new Ability Create()
		{return new MiracleHealth();}

		/// <summary>
		/// Damages this unit.
		/// </summary>
		/// <param name="damage">The damage to deal to the unit. Negative damage will not heal the unit.</param>
		/// <returns>Returns the amount of HP the unit has after being damaged.</returns>
		public override int Damage(int damage)
		{
			if(damage <= 0)
				return HP;

			int old_hp = HP;
			HP -= damage;

			if(HP < 0 && old_hp == 1)
				HP = 0;
			else if(HP <= 0 && old_hp > 1)
				HP = 1;

			return HP;
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
			{return "Second Life\n-----------\n[See Basic Health]\n\tSome soldiers suffer greatly. Some see few enough wounds. Some become heroes. And then there are those that are just plain lucky. These are those soldiers.\n\tThis ability grants all the effects of Basic Health but also if the soldier would take a fatal blow it, instead survives at 1 health.\n\nCost: 2 points per level; first level costs 10\nMax Level: 20";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected new const string name = "Health: Second Life";

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public override int PointCost
		{
			get
			{return 2 * Level + 8;}
		}
	}
}
