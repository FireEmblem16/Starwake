using TheGame.Utility;

namespace TheGame.Engine.Abilities.Vision
{
	/// <summary>
	/// Vision that can see invisible units so long as they are within the units sight radius.
	/// </summary>
	public class InvisibleVision : NormalVision
	{
		/// <summary>
		/// Creates a new vision that can see invisible units.
		/// </summary>
		public InvisibleVision() : base()
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
		{return new InvisibleVision();}

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		public override int MaxLevel
		{
			get
			{return 3;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public override int PointCost
		{
			get
			{return 6 * Level;}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public override int PointCostToNextLevel
		{
			get
			{
				if(Level < MaxLevel)
					return 6;

				return -1;
			}
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
			{return "Keen Eyes\n---------\n[See Vision: Normal Eyes]\n\tThe master at arms often said see with your mind, not with your eyes. To that the blind recruit demonstrated his prowess in battle. He lost outright just as he expected.\n\tSight radius is the same as normal vision. A unit with this ability can see other soldiers that are trying to stay out of sight.\n\nCost: 6 points per level";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected new const string name = "Vision: Keen Eyes";

		/// <summary>
		/// If true then this unit can see invisible units.
		/// </summary>
		public override bool SeeInvisible
		{
			get
			{return true;}
		}
	}
}
