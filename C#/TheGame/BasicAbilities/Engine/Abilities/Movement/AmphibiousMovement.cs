using TheGame.Engine.Abilities.PassiveAbilities.Movement;
using TheGame.Engine.Tiles;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.Movement
{
	/// <summary>
	/// Movement by a unit on the ground or in the water.
	/// </summary>
	public class AmphibiousMovement : GroundMovement
	{
		/// <summary>
		/// Creates a new default ground/water movement.
		/// </summary>
		public AmphibiousMovement() : base()
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
		{return new AmphibiousMovement();}

		/// <summary>
		/// Called when the ability is added to a unit.
		/// </summary>
		/// <param name="u">The unit this ability is added to.</param>
		public override void OnAdd(Unit u)
		{
			u.AddAbility(new GroundMovementModifier());
			u.AddAbility(new MarineMovementModifier());

			return;
		}

		/// <summary>
		/// Called when the ability is removed from a unit.
		/// </summary>
		/// <param name="u">The unit this ability is removed from.</param>
		public override void OnRemove(Unit u)
		{
			u.RemoveAbility(typeof(GroundMovementModifier));
			u.RemoveAbility(typeof(MarineMovementModifier));

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
			if(!t.CanMarineUnitsEnter)
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
			{return "Amphibious\n----------\n\tIt is not typical for soldiers to rush into battle armored properly to allow them to swim in a fight. Many a knight has learned this cruel lesson first hand after falling off a boat. Amphibious units have recieved special training and equipment that allow them to swim during combat, storm beaches, forge rivers and so on.\n\tEach level of Amphibious grants one movement speed.\n\nCost: Lv.1 - 5 points  Lv.2 - 15 points  Lv.3 30 points Lv.4 - 50 points\nMax Level: 4";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected new const string name = "Movement: Amphibious";

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
					return 5;
				case 2:
					return 15;
				case 3:
					return 30;
				case 4:
					return 50;
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
					return 10;
				case 2:
					return 15;
				case 3:
					return 20;
				case 4:
					return -1;
				}

				return -1;
			}
		}

		/// <summary>
		/// If true then this unit can enter water tiles.
		/// </summary>
		public override bool CanSwim
		{
			get
			{return true;}
		}
	}
}
