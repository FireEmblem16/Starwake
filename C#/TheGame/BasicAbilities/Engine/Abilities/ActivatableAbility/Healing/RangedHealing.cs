#pragma warning disable 0162

using System;
using System.Collections.Generic;
using TheGame.Engine.ActivatedAbilities;
using TheGame.Engine.ActivatedAbilities.Healing;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.ActivatableAbility.Healing
{
	/// <summary>
	/// Represents a ranged healing spell.
	/// </summary>
	public class RangedHealing : HealSpell
	{
		/// <summary>
		/// Creates a new ranged healing spell.
		/// </summary>
		public RangedHealing()
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
		{return new RangedHealing();}

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
		{return new RangedHealingActivation(target,RecoveryAmount);}

		/// <summary>
		/// Obtains all the possible places this unit could heal from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can heal.</returns>
		public List<Pair<int,int>> GetHealLocations(int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();
			
			for(int i = -Range;i <= Range;i++)
				for(int j = -Range;j <= Range;j++)
					if(Math.Abs(i) + Math.Abs(j) <= Range)
						ret.Add(new Pair<int,int>(x + i,y + j));

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
			DetermineRange(b,ret,Range,x,y,new Pair<int,int>(x,y));

			return ret;
		}

		/// <summary>
		/// Travels through the board and determines where the unit can cast at.
		/// </summary>
		/// <param name="b">The board to cast on.</param>
		/// <param name="list">The list of places already determined to be castable.</param>
		/// <param name="magic_left">The distance left that the unit can cast.</param>
		/// <param name="x">The x coordinate to check.</param>
		/// <param name="y">The y coordinate to check.</param>
		/// <param name="origin">The original location we started from.</param>
		protected void DetermineRange(Board b, List<Pair<int,int>> list, int magic_left, int x, int y, Pair<int,int> origin)
		{
			// If we're out of bounds forget it
			if(x < 0 || x >= b.Width || y < 0 || y >= b.Height)
				return;

			// If we've already visited this place then it will be in the list (can't fire through doesn't mean we can't shoot at it)
			if(list.Contains(new Pair<int,int>(x,y)))
				return;

			// This tile can be cast on
			list.Add(new Pair<int,int>(x,y));

			// If we are out of magic power we are done
			if(magic_left == 0)
				return;

			// If this tile can't be cast through we shouldn't bother going deeper from this tile
			if(!b.GetTile(x,y).CanCastThrough)
				return;

			// We only want to check tiles that increase the distance from the origin (so we don't double back around a mountain or something)
			int taxicab_dist = Math.Abs(x - origin.val1) + Math.Abs(y - origin.val2);
			
			// Check left
			if(Math.Abs((x - 1) - origin.val1) + Math.Abs(y - origin.val2) > taxicab_dist && x >= 0)
				DetermineRange(b,list,magic_left - 1,x - 1,y,origin);

			// Check right
			if(Math.Abs((x + 1) - origin.val1) + Math.Abs(y - origin.val2) > taxicab_dist && x < b.Width)
				DetermineRange(b,list,magic_left - 1,x + 1,y,origin);

			// Check up
			if(Math.Abs(x - origin.val1) + Math.Abs((y - 1) - origin.val2) > taxicab_dist && y >= 0)
				DetermineRange(b,list,magic_left - 1,x,y - 1,origin);

			// Check down
			if(Math.Abs(x - origin.val1) + Math.Abs((y + 1) - origin.val2) > taxicab_dist && y < b.Height)
				DetermineRange(b,list,magic_left - 1,x,y + 1,origin);

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
			{return "Far Healing\n-----------\n\tThe ability to heal allies from distant locations. Some say this ability comes from divine investiture, others call it magic. Some don't even care how it works and are just glad to be of help.\n\tEach level of this ability grants a single point of healing power. The base range is 3 and increases by 1 at every fifth level.\n\tMost status ailments will be removed when healed of a non-trivial amount of damage.\n\nCost: 2 points per level plus 5 more every fifth level\nMax Level: 25";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Far Healing";

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
			{
				if(Level < 5)
					return Level * 2;
				else if(Level < 10)
					return (Level - 1) * 2 + 7;
				else if(Level < 15)
					return (Level - 2) * 2 + 14;
				else if(Level < 20)
					return (Level - 3) * 2 + 21;
				else if(Level < 25)
					return (Level - 4) * 2 + 28;
				else if(Level == 25)
					return (Level - 5) * 2 + 35;

				return 0x7FFFFFFF; // Why not?
			}
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
					switch(Level)
					{
					case 4:
					case 9:
					case 14:
					case 19:
					case 24:
						return 7;
						break;
					default:
						return 2;
 						break;
					}

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
			{return "Ranged Heal";}
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
			{return true;}
		}

		/// <summary>
		/// The range of this attack.
		/// </summary>
		public int Range
		{
			get
			{return 3 + Level / 5;}
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
