using TheGame.Engine.Abilities.PassiveAbilities.Movement;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Movement
{
	/// <summary>
	/// Movement by a unit on the ground on some sort of mount.
	/// </summary>
	public class GroundMountedMovement : GroundMovement
	{
		/// <summary>
		/// Creates a new default ground mount movement.
		/// </summary>
		public GroundMountedMovement() : base()
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
		{return new GroundMountedMovement();}

		/// <summary>
		/// Called when the ability is added to a unit.
		/// </summary>
		/// <param name="u">The unit this ability is added to.</param>
		public override void OnAdd(Unit u)
		{
			u.AddAbility(new GroundMovementModifier());
			u.AddAbility(new MountedMovementModifier());

			return;
		}

		/// <summary>
		/// Called when the ability is removed from a unit.
		/// </summary>
		/// <param name="u">The unit this ability is removed from.</param>
		public override void OnRemove(Unit u)
		{
			u.RemoveAbility(typeof(GroundMovementModifier));
			u.RemoveAbility(typeof(MountedMovementModifier));

			return;
		}

		/// <summary>
		/// Returns true if a unit with this movement type can enter the provided tile and false otherwise.
		/// </summary>
		/// <param name="t">The tile to check.</param>
		/// <param name="ally_unit">If there is an allied unit in the tile it will be given here.</param>
		/// <param name="enemy_unit">If there is an enemy unit in the tile it will be given here.</param>
		/// <param name="current_position">If true then the tile is at the unit's current position.</param>
		/// <returns>Returns true if this unit can enter the tile and false otherwise.</returns>
		protected override bool CanEnterTile(Tile t, Unit ally_unit = null, Unit enemy_unit = null, bool current_position = false)
		{
			if(!t.CanGroundedMountedUnitsEnter)
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
		protected override bool CanMoveThroughTile(Tile t, Unit ally_unit = null, Unit enemy_unit = null, bool current_position = false)
		{
			if(enemy_unit != null && !current_position)
				return false;

			return true;
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
			{return "Mounted Knight\n--------------\n\tMounted knights ride across fields at great speeds often performing hit and run tactics, bulstering failing formations and outflanking their foes. Their main advantage lies not in soaking up damage nor knocking out enemies ten at a time but in their extreme speed. Mounted knights are capable of moving a second time in one turn after all other soldiers have moved or utilized an ability.\n\tEach level grants 1 movement speed (and 1 movement speed in the second move phase).\n\nCost: Lv.1 - 6 points   Lv.2 - 18 points   Lv.3 - 36 points   Lv.4 - 60 points   Lv.5 - 90 points\nMax Level: 5";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected new const string name = "Movement: Mounted Knight";

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		public override int MaxLevel
		{
			get
			{return 5;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public override int PointCost
		{
			get
			{
				switch(Level)
				{
				case 1:
					return 6;
				case 2:
					return 18;
				case 3:
					return 36;
				case 4:
					return 60;
				case 5:
					return 90;
				}

				return 0;
			}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public override int PointCostToNextLevel
		{
			get
			{
				switch(Level)
				{
				case 1:
					return 12;
				case 2:
					return 18;
				case 3:
					return 24;
				case 4:
					return 30;
				case 5:
					return -1;
				}

				return -1;
			}
		}

		/// <summary>
		/// If true then this unit is allowed to move in the post combat movement phase.
		/// </summary>
		public override bool CanMovePostCombat
		{
			get
			{return true;}
		}

		/// <summary>
		/// If true then 
		/// </summary>
		public override bool Mounted
		{
			get
			{return true;}
		}
	}
}
