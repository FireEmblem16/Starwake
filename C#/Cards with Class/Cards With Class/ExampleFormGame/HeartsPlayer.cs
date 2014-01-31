using System.Collections.Generic;
using Engine.Cards;
using Engine.Cards.Hands;
using Engine.Game;
using Engine.Player;

namespace ExampleFormGame
{
	/// <summary>
	/// A human player for a game of hearts.
	/// </summary>
	public class HeartsPlayer : StandardPlayer
	{
		/// <summary>
		/// Creates a new player for a game of hearts.
		/// </summary>
		/// <param name="cards">The cards this player starts with.</param>
		public HeartsPlayer(IEnumerable<Card> cards) : base(cards)
		{
			Score = 0;
			return;
		}

		/// <summary>
		/// Given a move encoded in m, will perform all the necessary work to make the player reflect the move occuring.
		/// </summary>
		/// <param name="m">The move to make.</param>
		/// <returns>Returns true if the move is valid and false otherwise.</returns>
		public override bool MakePlay(Move m)
		{
			HeartsMove move = m as HeartsMove;

			if(move == null)
				return false;

			try
			{CardsInHand.PlayCard(move.Play);}
			catch
			{return false;}
			
			return true;
		}

		/// <summary>
		/// Given a move encoded in m, will reverse any changes that were made to this class from performing the move.
		/// </summary>
		/// <param name="m">The move to undo.</param>
		public override void UndoMove(Move m)
		{
			HeartsMove move = m as HeartsMove;

			if(move == null)
				return;

			CardsInHand.DrawCard(move.Play);
			return;
		}

		/// <summary>
		/// Creates a deep copy of this player.
		/// </summary>
		/// <returns>Returns a deep copy of this player.</returns>
		public override Player Clone()
		{
			HeartsPlayer ret = new HeartsPlayer(CardsInHand.Cards);
			ret.Score = Score;

			return ret;
		}

		/// <summary>
		/// The current score of this player.
		/// </summary>
		public int Score
		{get; set;}
	}
}
