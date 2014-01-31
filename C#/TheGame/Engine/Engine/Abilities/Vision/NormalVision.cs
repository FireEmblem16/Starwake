using System;
using System.Collections.Generic;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Vision
{
	/// <summary>
	/// Vision that can not see invisible units except when they are adjacent.
	/// </summary>
	public class NormalVision : Vision
	{
		/// <summary>
		/// Creates a new normal vision ability.
		/// </summary>
		public NormalVision()
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
		{return new NormalVision();}

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
		/// Obtains all the possible places this unit can see to from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can see.</returns>
		public List<Pair<int,int>> GetVisibleLocations(int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();

			for(int i = -SightRadius;i <= SightRadius;i++)
				for(int j = -SightRadius;j <= SightRadius;j++)
					if(i + j <= SightRadius) // The origin is visible and yes it matters that that is a choice
						ret.Add(new Pair<int,int>(i,j));

			return ret;
		}

		/// <summary>
		/// Obtains all the possible places this unit can see to from the given origin.
		/// Considerations will be made as to whether or not this unit can actually see to a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider sight on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can see.</returns>
		public List<Pair<int,int>> GetVisibleLocations(Board b, int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();
			DetermineVisibility(b,ret,SightRadius,x,y,new Pair<int,int>(x,y));

			return ret;
		}

		/// <summary>
		/// Travels through the board and determines what can be seen.
		/// </summary>
		/// <param name="b">The board to travel though.</param>
		/// <param name="list">The list of places already determined to be visible.</param>
		/// <param name="sight_left">The distance left that the unit can see.</param>
		/// <param name="x">The x coordinate to check.</param>
		/// <param name="y">The y coordinate to check.</param>
		/// <param name="origin">The original location we started from.</param>
		protected void DetermineVisibility(Board b, List<Pair<int,int>> list, int sight_left, int x, int y, Pair<int,int> origin)
		{
			// If we're out of bounds forget it
			if(x < 0 || x >= b.Width || y < 0 || y >= b.Height)
				return;

			// If we've already visited this place then it will be in the list (can't see through doesn't mean it won't be visible)
			if(list.Contains(new Pair<int,int>(x,y)))
				return;

			// This tile is visible
			list.Add(new Pair<int,int>(x,y));

			// If we are out of eye power we are done
			if(sight_left == 0)
				return;

			// If this tile can't be seen through we shouldn't bother going deeper from this tile
			if(!b.GetTile(x,y).CanSeeThrough)
				return;

			// We only want to check tiles that increase the distance from the origin (so we don't double back around a mountain or something)
			int taxicab_dist = Math.Abs(x - origin.val1) + Math.Abs(y - origin.val2);
			
			// Check left
			if(Math.Abs((x - 1) - origin.val1) + Math.Abs(y - origin.val2) > taxicab_dist && x >= 0)
				DetermineVisibility(b,list,sight_left - 1,x - 1,y,origin);

			// Check right
			if(Math.Abs((x + 1) - origin.val1) + Math.Abs(y - origin.val2) > taxicab_dist && x < b.Width)
				DetermineVisibility(b,list,sight_left - 1,x + 1,y,origin);

			// Check up
			if(Math.Abs(x - origin.val1) + Math.Abs((y - 1) - origin.val2) > taxicab_dist && y >= 0)
				DetermineVisibility(b,list,sight_left - 1,x,y - 1,origin);

			// Check down
			if(Math.Abs(x - origin.val1) + Math.Abs((y + 1) - origin.val2) > taxicab_dist && y < b.Height)
				DetermineVisibility(b,list,sight_left - 1,x,y + 1,origin);

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
			{return "Normal Eyes\n-----------\n\tStandard vision. Neither good nor poor. Soldiers with this quality of eyesight can see anything not trying to stay hidden.\n\tEach level of this ability grants 1 extra sight radius. The radius at the first level is 3.\n\nCost: 3 points per level; first level is free\nMax Level: 4";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Vision: Normal Eyes";

		/// <summary>
		/// The level the ability is currently at.
		/// </summary>
		public int Level
		{get; protected set;}

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		public virtual int MaxLevel
		{
			get
			{return 4;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public virtual int PointCost
		{
			get
			{return 3 * (Level - 1);}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public virtual int PointCostToNextLevel
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
		/// How far the unit can see.
		/// </summary>
		public int SightRadius
		{
			get
			{return Level + 2;}
		}

		/// <summary>
		/// How far the unit can possibly see.
		/// </summary>
		public int MaxRadius
		{
			get
			{return MaxLevel + 2;}
		}

		/// <summary>
		/// If true then this unit can see invisible units.
		/// </summary>
		public virtual bool SeeInvisible
		{
			get
			{return false;}
		}
	}
}
