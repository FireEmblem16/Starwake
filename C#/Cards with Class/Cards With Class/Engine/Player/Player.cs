﻿using Engine.Cards;
using Engine.Game;

namespace Engine.Player
{
	/// <summary>
	/// Represents what it means to be a player.
	/// </summary>
	public interface Player
	{
		/// <summary>
		/// Given a move encoded in m, will perform all the necessary work to make the player reflect the move occuring.
		/// </summary>
		/// <param name="m">The move to make.</param>
		/// <returns>Returns true if the move is valid and false otherwise.</returns>
		bool MakePlay(Move m);

		/// <summary>
		/// Given a move encoded in m, will reverse any changes that were made to this class from performing the move.
		/// </summary>
		/// <param name="m">The move to undo.</param>
		void UndoMove(Move m);

		/// <summary>
		/// Creates a deep copy of this player.
		/// </summary>
		/// <returns>Returns a deep copy of this player.</returns>
		Player Clone();

		/// <summary>
		/// This player's hand.
		/// </summary>
		Hand CardsInHand
		{get;}

		/// <summary>
		/// If true then this player is an AI player.
		/// </summary>
		bool IsAI
		{get;}
	}
}
