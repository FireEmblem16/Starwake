using System.Collections.Generic;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.ActivatedAbilities;
using TheGame.Engine.ActivatedAbilities.Attack;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.ActivatableAbility.Attack
{
	/// <summary>
	/// Represents a normal melee attack.
	/// </summary>
	public class NormalAttack : Attack
	{
		/// <summary>
		/// Creates a new normal melee attack.
		/// </summary>
		public NormalAttack()
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
		{return new NormalAttack();}

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
		/// Creates an activation of this ability.
		/// </summary>
		/// <param name="target">The target location for this ability.</param>
		/// <returns>Returns a activation of this ability.</returns>
		public ActivatedAbility Activate(Pair<int,int> target)
		{return new NormalAttackActivation(target,AttackDamage,AttackType);}

		/// <summary>
		/// Obtains all the possible places this unit could attack to from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can attack to.</returns>
		public List<Pair<int,int>> GetAttackLocations(int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();
			ret.Add(new Pair<int,int>(x,y));

			return ret;
		}

		/// <summary>
		/// Obtains all the possible places this unit could attack from the given origin.
		/// Considerations will be made as to whether or not this unit can actually attack to a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider the attack on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can move to.</returns>
		public List<Pair<int,int>> GetAttackLocations(Board b, int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();
			ret.Add(new Pair<int,int>(x,y));

			return ret;
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
			{return "Melee Attack\n------------\n\tA basic melee attack for crushing your enemies. Some people choose massive battle axes or savage clubs but most settle on spears or swords, perhaps accompanied by a shield. Some fighters have even been known to take the field with nothing but their bare hands for offense.\n\tEach level of this ability corresponds to a single point of damage at range zero.\n\tThis ability is a default ability. A unit may only have one default ability. A default ability is used when a unit moves or when the unit is otherwise unable to choose an ability to use.\n\nCost: 2 points for odd levels and 1 point for even levels; first level is free\nMax Level: 30";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Melee Attack";

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
			{return 30;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public int PointCost
		{
			get
			{return 3 * (Level - 1) / 2;}
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
					return Level % 2 == 0 ? 2 : 1;

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
		/// The label to use for this ability during gameplay.
		/// </summary>
		public string Label
		{
			get
			{return "Melee Attack";}
		}

		/// <summary>
		/// The amount of damage this attack deals without other buffs and debuffs.
		/// </summary>
		public int AttackDamage
		{
			get
			{return Level;}
		}

		/// <summary>
		/// The type(s) of this attack not considering additional modifiers such as being mounted, grounded, flying, a necromancer and so on.
		/// </summary>
		public ATTACK_TYPE AttackType
		{
			get
			{return ATTACK_TYPE.TYPELESS;}
		}

		/// <summary>
		/// If true then this is a ranged attack.
		/// </summary>
		/// <remarks>Note that it is also possible to determine if the attack is ranged from its type.</remarks>
		public bool IsRanged
		{
			get
			{return false;}
		}

		/// <summary>
		/// If true then this ability is a default ability used when no other ability is specified.
		/// </summary>
		/// <example>An example of this is the default melee attack which occurs by default after movement.</example>
		public bool IsDefaultAbility
		{
			get
			{return true;}
		}
	}
}
