using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Engine.Game.Screens;
using Networking;
using System.Drawing;

namespace Engine.Game
{
	/// <summary>
	/// The structure of a card game.
	/// Each type that implements this interface should have a default constructor.
	/// </summary>
	public interface GameData
	{
		/// <summary>
		/// The table for this game.
		/// </summary>
		/// <param name="top">The form to return to when the game finishes.</param>
		/// <param name="nio">The network stream for sending and recieving data to and from players. This class is not responsible for opening or closing this stream nor managing the connections to the stream.</param>
		/// <param name="names">The names of the players in the game. If AI players are present then names should be provided in their positions.</param>
		/// <param name="seat">The index of the player at this table.</param>
		/// <param name="host">If ture then this table is hosting the game.</param>
		/// <param name="args">Extra arguments needed to create the table.</param>
		/// <param name="humans">An array indicating which players are human and which are AI (true is human, false is AI).</param>
		/// <returns>Returns a table of this game type or null if the correct number and type of extra arguments were not provided.</returns>
		/// <remarks>Should return a new copy every call.</remarks>
		GameTable Table(Form top, NetworkStream nio, IList<string> names, int seat, bool host, bool[] humans, params object[] args);

		/// <summary>
		/// The options screen that allows the host to change settings.
		/// </summary>
		/// <param name="top">The window to return to if this options screen exits for any reason other than moving to the waiting room.</param>
		/// <param name="seat">The index of the player at this game.</param>
		/// <param name="name">The name of the local player.</param>
		/// <param name="args">Extra arguments needed to create the options screen.</param>
		/// <returns>Returns a game options screen for this game type or null if the correct number and type of extra arguments were not provided.</returns>
		/// <remarks>Should return a new copy every call.</remarks>
		GameOptionsScreen Options(Form top, int seat, string name, params object[] args);

		/// <summary>
		/// A waiting room to allow players to join the host's game.
		/// </summary>
		/// <param name="top">The window to return to if this waiting room exits for any reason other than the game starting.</param>
		/// <param name="nio">The network stream for sending and recieving data to and from players. This class is not responsible for opening or closing this stream nor managing the connections to the stream.</param>
		/// <param name="seat">The index of the player at this table.</param>
		/// <param name="name">The name of the local player.</param>
		/// <param name="host">If ture then this table is hosting the game.</param>
		/// <param name="args">Extra arguments needed to create the table. Client waiting rooms should be able to be made with zero extra arguments.</param>
		/// <returns>Returns a waiting room for this game type or null if the correct number and type of extra arguments were not provided.</returns>
		/// <remarks>Should return a new copy every call.</remarks>
		WaitingScreen WaitingRoom(Form top, NetworkStream nio, int seat, string name, bool host, params object[] args);

		/// <summary>
		/// Displays the rules of this game.
		/// </summary>
		/// <remarks>Should return a new copy every call.</remarks>
		RuleScreen Rules
		{get;}

		/// <summary>
		/// The name of this game.
		/// </summary>
		string GameName
		{get;}

		/// <summary>
		/// A brief description of the game.
		/// </summary>
		string Description
		{get;}

		/// <summary>
		/// The image representing this game.
		/// </summary>
		/// <remarks>Images are best square with a dimension that is a multiple of 230. If null then no image will be drawn.</remarks>
		Image Icon
		{get;}

		/// <summary>
		/// How long an average game can take to play.
		/// </summary>
		TimeSpan ExpectedTime
		{get;}

		/// <summary>
		/// The minimum number of players.
		/// </summary>
		int MinPlayers
		{get;}

		/// <summary>
		/// The maximum number of players.
		/// </summary>
		int MaxPlayers
		{get;}
	}
}
