using TheGame.Utility;

namespace TheGame.Engine.Abilities.Visibility
{
	/// <summary>
	/// Represents a unit not making any particular effort to conceal itself.
	/// </summary>
	public class Visible : Visibility
	{
		/// <summary>
		/// Creates a new visible ability.
		/// </summary>
		public Visible()
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
		{return new Visible();}

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
		{return false;}
		
		/// <summary>
		/// Decreases the level of the ability by one if able.
		/// </summary>
		/// <returns>Returns true if the ability was deleveled and false otherwise.</returns>
		public bool Unlevel()
		{return false;}

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
			{return "Boisterous\n----------\n\tA master of stealth...these soldiers are not. Too focused on the battle at hand to stay hidden in the shadows, they can be seen from great distances by even the most unskilled eyes.\n\nCost: 0 points";}
		}

		/// <summary>
		/// The source name of the ability.
		/// </summary>
		protected const string name = "Visibility: Boisterous";

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
			{return 1;}
		}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		public int PointCost
		{
			get
			{return 0;}
		}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		public int PointCostToNextLevel
		{
			get
			{return -1;}
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
		/// If true the unit is invisible.
		/// </summary>
		public bool Invisible
		{
			get
			{return false;}
		}
	}
}
