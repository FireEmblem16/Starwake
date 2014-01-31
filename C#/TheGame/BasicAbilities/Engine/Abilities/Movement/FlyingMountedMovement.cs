using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.Abilities.PassiveAbilities.Movement;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Movement
{
	/// <summary>
	/// Movement by a unit in the air by mount.
	/// </summary>
	public class FlyingMountedMovement : GroundMovement
	{
		/// <summary>
		/// Creates a new default flying mount movement.
		/// </summary>
		public FlyingMountedMovement() : base()
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
		{return new FlyingMountedMovement();}

		/// <summary>
		/// Called when the ability is added to a unit.
		/// </summary>
		/// <param name="u">The unit this ability is added to.</param>
		public override void OnAdd(Unit u)
		{
			u.AddAbility(new FlyingArmor());
			u.AddAbility(new FlyingMovementModifier());
			u.AddAbility(new MountedMovementModifier());

			return;
		}

		/// <summary>
		/// Called when the ability is removed from a unit.
		/// </summary>
		/// <param name="u">The unit this ability is removed from.</param>
		public override void OnRemove(Unit u)
		{
			u.RemoveAbility(typeof(FlyingArmor));
			u.RemoveAbility(typeof(FlyingMovementModifier));
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
			if(!t.CanFlyingMountedUnitsEnter)
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
			if(enemy_unit != null && enemy_unit.AbilityMovement.CanFly && !current_position)
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
			{return "Pegasus Mount\n-------------\n[See Movement: Mounted Knight]\n[See Movement: Flight]\n\tAs the name suggests, these are knights that have peagsus mounts to fly through the sky. Although popularly known as majestic, gentle creatures, pegasi can be fierce as well as anyone that survives a pegasus knight's wrath can tell you.\n\tEach level of Pegasus Mount grants one movement speed. Also grants all the bonuses and penalties of flying movement and mounted movement.\n\nCost: Lv.1 - 8 points  Lv.2 - 24 points  Lv.3 - 48 points  Lv.4 - 80 points  Lv.5 - 120 points  Lv.6 - 168 points\nMax Level: 6";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected new const string name = "Movement: Pegasus Mount";

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		public override int MaxLevel
		{
			get
			{return 6;}
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
					return 8;
				case 2:
					return 24;
				case 3:
					return 48;
				case 4:
					return 80;
				case 5:
					return 120;
				case 6:
					return 168;
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
					return 16;
				case 2:
					return 24;
				case 3:
					return 32;
				case 4:
					return 40;
				case 5:
					return 48;
				case 6:
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

		/// <summary>
		/// If true then this unit can fly and enter any tile not forbidden to flying units.
		/// </summary>
		public override bool CanFly
		{
			get
			{return true;}
		}
	}
}
