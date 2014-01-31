using System.Collections.Generic;
using TheGame.Engine.ActivatedAbilities;
using TheGame.Engine.ActivatedAbilities.Healing;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.ActivatableAbility.Healing
{
	/// <summary>
	/// Represents a touch based healing spell.
	/// </summary>
	public class TouchHealing : HealSpell
	{
		/// <summary>
		/// Creates a new touch healing ability.
		/// </summary>
		public TouchHealing()
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
		{return new TouchHealing();}

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
		{return new TouchHealingActivation(target,RecoveryAmount);}

		/// <summary>
		/// Obtains all the possible places this unit could heal from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can heal.</returns>
		public List<Pair<int,int>> GetHealLocations(int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();
			ret.Add(new Pair<int,int>(x,y));

			return ret;
		}

		/// <summary>
		/// Obtains all the possible places this unit could heal from the given origin.
		/// Considerations will be made as to whether or not this unit can actually heal a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider the spell on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can heal.</returns>
		public List<Pair<int,int>> GetHealLocations(Board b, int x, int y)
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
			{return "Healing Hands\n-------------\n\tThe ability to care for wounded allies on the battlefield. Medics are some of the most valued and scarce units; even more so the battle medic that swings swords while plying bandages.\n\tEach level of this ability grants a single point of healing power. Using this ability heals all adjacent allies. Healing power is not divided among each unit. Rather it takes full effect on every ally.\n\tMost status ailments will be removed when healed of a non-trivial amount of damage.\n\nCost: 3 points per level\nMax Level: 25";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Healing Hands";

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
			{return 25;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public int PointCost
		{
			get
			{return 3 * Level;}
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
					return 3;

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
			{return "Heal";}
		}

		/// <summary>
		/// The amount of damage this spell heals without other buffs and debuffs.
		/// </summary>
		public int RecoveryAmount
		{
			get
			{return Level;}
		}

		/// <summary>
		/// If true then this is a ranged spell.
		/// </summary>
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
			{return false;}
		}
	}
}
