using System;
using System.Collections.Generic;
using TheGame.Utility;

namespace TheGame.Engine
{
	/// <summary>
	/// Contains many variables that represent the game's state.
	/// </summary>
	public class GameState
	{
		/// <summary>
		/// Creates an empty game state.
		/// </summary>
		public GameState()
		{return;}

		/// <summary>
		/// The user's map.
		/// </summary>
		public Board Map = new Board(Settings.DefaultMapWidth,Settings.DefaultMapHeight);

		/// <summary>
		/// The username of the current player.
		/// Empty string is used instead of null to denote no player.
		/// </summary>
		public string UserName = "";

		/// <summary>
		/// Contains a list of all the units the user has created.
		/// </summary>
		public List<Unit> UserUnits = new List<Unit>();

		/// <summary>
		/// The username of the current opposing player, if any.
		/// Empty string is used instead of null to denote no player.
		/// </summary>
		public string OpponentName = "";

		/// <summary>
		/// If true then it is possible to view the stats of enemy units.
		/// </summary>
		public bool CanInspectEnemies = true;

		/// <summary>
		/// The random number generator for the game. The host of a game should provide a seed to use.
		/// </summary>
		public Random rand;

		/// <summary>
		/// The location where output should be directed.
		/// </summary>
		public Rectangle Output = new Rectangle(0,53,200,16);

		/// <summary>
		/// True if a user is logged in and false otherwise.
		/// </summary>
		public bool LoggedIn
		{
			get
			{return UserName != "";}
		}
	}
}
