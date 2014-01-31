using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Engine.Player
{
	/// <summary>
	/// Outlines what a move is in a game.
	/// </summary>
	public interface Move
	{
        /// <summary>
        /// Serializes the move.
        /// </summary>
        /// <returns>Returns a string representing the move.</returns>
        string Serialize();

		/// <summary>
		/// The player that this move is for.
		/// </summary>
		int Player
		{get;}
	}
}
