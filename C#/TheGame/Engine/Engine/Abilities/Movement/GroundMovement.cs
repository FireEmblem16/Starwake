using System;
using System.Collections.Generic;
using TheGame.Engine.Abilities.PassiveAbilities.Movement;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Movement
{
	/// <summary>
	/// Movement by a unit on the ground.
	/// </summary>
	public class GroundMovement : Movement
	{
		/// <summary>
		/// Creates a new default ground movement.
		/// </summary>
		public GroundMovement()
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
		{return new GroundMovement();}

		/// <summary>
		/// Called when the ability is added to a unit.
		/// </summary>
		/// <param name="u">The unit this ability is added to.</param>
		public virtual void OnAdd(Unit u)
		{
			u.AddAbility(new GroundMovementModifier());
			return;
		}

		/// <summary>
		/// Called when the ability is removed from a unit.
		/// </summary>
		/// <param name="u">The unit this ability is removed from.</param>
		public virtual void OnRemove(Unit u)
		{
			u.RemoveAbility(typeof(GroundMovementModifier));
			return;
		}

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
		/// Obtains all the possible places this unit could move to from the given origin.
		/// </summary>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can move to.</returns>
		public List<Pair<int,int>> GetAvailableLocations(int x, int y)
		{
			List<Pair<int,int>> ret = new List<Pair<int,int>>();

			for(int i = -MovementRange;i <= MovementRange;i++)
				for(int j = -MovementRange;j <= MovementRange;j++)
					if(Math.Abs(i) + Math.Abs(j) <= MovementRange)
						ret.Add(new Pair<int,int>(x + i,y + j));

			return ret;
		}

		/// <summary>
		/// Obtains all the possible places this unit could move to from the given origin.
		/// Considerations will be made as to whether or not this unit can actually make it to a given point on the provided board.
		/// </summary>
		/// <param name="b">The board to consider movement on.</param>
		/// <param name="x">The x coordinate to start at.</param>
		/// <param name="y">The y coordinate to start at.</param>
		/// <returns>Returns a list of ordered pairs representing all possible places this unit can move to.</returns>
		public List<Pair<int,int>> GetAvailableLocations(Board b, int x, int y)
		{return BFS(b,new Pair<int,int>(x,y));}

		/// <summary>
		/// Performs a breadth first search of this unit's list of movements over the given board's restrictions.
		/// </summary>
		/// <param name="b">The board to move on.</param>
		/// <param name="offset">The offset on the board to be checking.</param>
		protected List<Pair<int,int>> BFS(Board b, Pair<int,int> offset)
		{
			List<Pair<int,Pair<int,int>>> potential = new List<Pair<int,Pair<int,int>>>();
			potential.Add(new Pair<int,Pair<int,int>>(0,offset)); // We can always move in place since moving is our melee attack

			List<Pair<int,int>> ret = new List<Pair<int,int>>();
			ret.Add(offset);

			while(potential.Count != 0)
			{
				Pair<int,Pair<int,int>> next = potential[0];
				potential.RemoveAt(0);

				ret.Add(next.val2); // We won't add any invalid locations

				// If we've reached our full movement we can go no further
				if(next.val1 == MovementRange)
					continue;

				// Get the tile we are on (if any)
				if(next.val2.val1 < 0 || next.val2.val1 >= b.Width || next.val2.val2 < 0 || next.val2.val2 >= b.Height)
					continue;

				// If we can't pass through the tile we do not continue down this path
				if(!CanMoveThroughTile(b.GetTile(next.val2.val1,next.val2.val2),b.GetAllyAt(next.val2),b.GetEnemyAt(next.val2),next.val2 == offset))
					continue;
				
				// Add adjacent movement
				Pair<int,int> loc = new Pair<int,int>(next.val2.val1,next.val2.val2 + 1);

				// We must be on the board, able to enter the tile and we must not have already visited the position
				if(b.Bounds.IsInside(loc) && CanEnterTile(b.GetTile(loc.val1,loc.val2),b.GetAllyAt(loc),b.GetEnemyAt(loc),false) && !ret.Contains(loc)) // These contains calls will not be numerous for performance issues
					potential.Add(new Pair<int,Pair<int,int>>(next.val1 + 1,loc));

				loc = new Pair<int,int>(next.val2.val1,next.val2.val2 - 1);

				if(b.Bounds.IsInside(loc) && CanEnterTile(b.GetTile(loc.val1,loc.val2),b.GetAllyAt(loc),b.GetEnemyAt(loc),false) && !ret.Contains(loc))
					potential.Add(new Pair<int,Pair<int,int>>(next.val1 + 1,loc));

				loc = new Pair<int,int>(next.val2.val1 + 1,next.val2.val2);

				if(b.Bounds.IsInside(loc) && CanEnterTile(b.GetTile(loc.val1,loc.val2),b.GetAllyAt(loc),b.GetEnemyAt(loc),false) && !ret.Contains(loc))
					potential.Add(new Pair<int,Pair<int,int>>(next.val1 + 1,loc));

				loc = new Pair<int,int>(next.val2.val1 - 1,next.val2.val2);

				if(b.Bounds.IsInside(loc) && CanEnterTile(b.GetTile(loc.val1,loc.val2),b.GetAllyAt(loc),b.GetEnemyAt(loc),false) && !ret.Contains(loc))
					potential.Add(new Pair<int,Pair<int,int>>(next.val1 + 1,loc));
			}

			return ret;
		}

		/// <summary>
		/// Returns true if a unit with this movement type can enter the provided tile and false otherwise.
		/// </summary>
		/// <param name="t">The tile to check.</param>
		/// <param name="ally_unit">If there is an allied unit in the tile it will be given here.</param>
		/// <param name="enemy_unit">If there is an enemy unit in the tile it will be given here.</param>
		/// <param name="current_position">If true then the tile is at the unit's current position.</param>
		/// <returns>Returns true if this unit can enter the tile and false otherwise.</returns>
		protected virtual bool CanEnterTile(Tile t, Unit ally_unit = null, Unit enemy_unit = null, bool current_position = false)
		{
			if(!t.CanGroundedUnitsEnter)
				return false;
			
			return true;
		}

		/// <summary>
		/// Returns true if a unit with this movement type can pass through the provided tile (after having already entered it) and false otherwise.
		/// For instance a ground unit can not pass through a tile with an enemy unit in it but a flying unit can pass through if the enemy is not a flying unit.
		/// </summary>
		/// <param name="t">The tile to check.</param>
		/// <param name="ally_unit">If there is an allied unit in the tile it will be given here.</param>
		/// <param name="enemy_unit">If there is an enemy unit in the tile it will be given here.</param>
		/// <param name="current_position">If true then the tile is at the unit's current position.</param>
		/// <returns>Returns true if this unit can pass through the tile and false otherwise.</returns>
		protected virtual bool CanMoveThroughTile(Tile t, Unit ally_unit = null, Unit enemy_unit = null, bool current_position = false)
		{
			if(enemy_unit != null && !current_position)
				return false;

			return true;
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
			{return "Foot Soldier\n-----------\n\tFoot soldiers usually make up the bulk of an army. They are cheap, easy to train and commanders usually have reserves of them. While they are rather slow, their numbers can easily overwhelm smaller, faster squads when they are prepared. They are also capable of moving over some more treacherous than some of their more clumsy foes and allies.\n\tEach level of this ability grant 1 movement speed.\n\nCost: Lv.1 - 0 points   Lv.2 - 8 points   Lv.3 - 20 points   Lv.4 - 36 points\nMax Level: 4";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Movement: Foot Soldier";

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
			{
				switch(Level)
				{
				case 1:
					return 0;
				case 2:
					return 8;
				case 3:
					return 20;
				case 4:
					return 36;
				}

				return 0;
			}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public virtual int PointCostToNextLevel
		{
			get
			{
				switch(Level)
				{
				case 1:
					return 8;
				case 2:
					return 12;
				case 3:
					return 16;
				case 4:
					return -1;
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
		/// How many spaces this unit can move in a turn.
		/// </summary>
		public int MovementRange
		{
			get
			{return Level;}
		}

		/// <summary>
		/// If true then this unit is allowed to move in the post combat movement phase.
		/// </summary>
		public virtual bool CanMovePostCombat
		{
			get
			{return false;}
		}
		
		/// <summary>
		/// If true then this unit can enter water tiles.
		/// </summary>
		public virtual bool CanSwim
		{
			get
			{return false;}
		}

		/// <summary>
		/// If true then 
		/// </summary>
		public virtual bool Mounted
		{
			get
			{return false;}
		}

		/// <summary>
		/// If true then this unit can fly and enter any tile not forbidden to flying units.
		/// </summary>
		public virtual bool CanFly
		{
			get
			{return false;}
		}
	}
}
