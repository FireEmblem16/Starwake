using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace TheGame.Engine
{
	/// <summary>
	/// Contains global variables for the game.
	/// </summary>
	public static class Settings
	{
		/// <summary>
		/// The minimum number of points that a unit can be created with.
		/// </summary>
		public const int UnitMinPoints = 50;

		/// <summary>
		/// The maximum number of points that a unit can be created with.
		/// </summary>
		public const int UnitMaxPoints = 200;

		/// <summary>
		/// The maximum number of points worth of units that can be brought into a match by one player.
		/// </summary>
		public const int SquadMaxPoints = 4000;

		/// <summary>
		/// The default width of a map.
		/// </summary>
		public const int DefaultMapWidth = 30;

		/// <summary>
		/// The default height of a map.
		/// </summary>
		public const int DefaultMapHeight = 15;

		/// <summary>
		/// The maximum number of items to place on the command stack.
		/// </summary>
		public const int CommandStackDepth = 20;

		/// <summary>
		/// The maximum length that a name can be.
		/// </summary>
		public const int MaxNameLength = 30;
	}
}
