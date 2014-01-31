using TheGame.Utility;

namespace TheGame.Engine.Abilities.Health
{
	/// <summary>
	/// Creates a normal health system with no special tricks.
	/// </summary>
	public class NormalHealth : Health
	{
		/// <summary>
		/// Creates a new normal health ability.
		/// </summary>
		public NormalHealth()
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
		{return new NormalHealth();}

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
		/// Damages this unit.
		/// </summary>
		/// <param name="damage">The damage to deal to the unit. Negative damage will not heal the unit.</param>
		/// <returns>Returns the amount of HP the unit has after being damaged.</returns>
		public virtual int Damage(int damage)
		{
			if(damage <= 0)
				return HP;

			HP -= damage;

			if(HP < 0)
				HP = 0;

			return HP;
		}

		/// <summary>
		/// Heals this unit.
		/// </summary>
		/// <param name="health">How much health to heal. Negative health will not damage the unit.</param>
		/// <returns>Returns the amount of HP the unit has after being healed.</returns>
		public int Heal(int health)
		{
			if(health <= 0)
				return HP;

			HP += health;

			if(HP > MaxHP)
				HP = MaxHP;

			return HP;
		}

		/// <summary>
		/// Puts this unit at exactly the given health without any passive abilities being triggered.
		/// </summary>
		/// <param name="health">The health the unit is to have. If it is higher than the max it is lowered to the max.</param>
		public void Resurrect(int health)
		{
			HP = health;

			if(HP > MaxHP)
				HP = MaxHP;

			return;
		}

		/// <summary>
		/// The name of the ability.
		/// </summary>
		public virtual string Name
		{
			get
			{return name;}
		}

		/// <summary>
		/// Documents and explains how the ability works, what it does and the costs.
		/// </summary>
		public virtual string Documentation
		{
			get
			{return "Basic Health\n------------\n\tA unit's basic stamina and health. Some think that as wounds are taken soldiers get weaker and need to rest. This is a popular misconception started by soldiers so that they fight less (fighting while wounded does in fact hurt). As long as a soldier has one health left it can fight as if it were at full health. However once that last health is lost the soldier dies.\n\tEach level of this ability grants 1 health.\n\nCost: 2 points per level; first level is free\nMax Level: 20";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Health: Basic Health";

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
			{return 20;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public virtual int PointCost
		{
			get
			{return 2 * (Level - 1);}
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
					return 2;

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

		/// <summary>
		/// How much health this unit has.
		/// </summary>
		public int HP
		{get; protected set;}

		/// <summary>
		/// The maximum health this unit has.
		/// </summary>
		public int MaxHP
		{
			get
			{return Level;}
		}

		/// <summary>
		/// True if the unit is dead and false otherwise.
		/// </summary>
		public bool Dead
		{
			get
			{return HP <= 0;}
		}
	}
}
