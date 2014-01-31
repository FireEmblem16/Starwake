using System;
using System.Collections;
using System.Collections.Generic;
using Engine.Cards;
using Engine.Cards.CardTypes;
using Engine.Cards.CardTypes.Suits;
using Engine.Cards.CardTypes.Values;
using Engine.Game;
using Engine.Player;
using Engine.Player.AI;
using Engine.Player.AI.ABP;

namespace ExampleNetworkedGame
{
	/// <summary>
	/// An AI for a game of hearts.
	/// </summary>
	public class HeartsAI : HeartsPlayer, AIPlayer
	{
		/// <summary>
		/// Creates a new AI for a game of hearts.
		/// </summary>
		/// <param name="cards">The cards that this player starts with.</param>
		/// <param name="behavior">The way the AI makes decisions. If null a default AI will be used.</param>
		public HeartsAI(IEnumerable<Card> cards, AIBehavior behavior = null) : base(cards)
		{
			if(behavior == null)
				AI = new HeartsABP();
			else
				AI = behavior.Clone();

			return;
		}

		/// <summary>
		/// This AI player will take its turn based on the provided game state and return the move it wishes to make.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		public Move TakeTurn(GameState state)
		{return AI.GetNextMove(state);}

		/// <summary>
		/// Creates a deep copy of this player.
		/// </summary>
		/// <returns>Returns a deep copy of this player.</returns>
		public override Player Clone()
		{
			HeartsAI ret = new HeartsAI(CardsInHand.Cards,AI);
			ret.Score = Score;

			return ret;
		}

		/// <summary>
		/// The way this AI player will actually make decisions on what to do.
		/// </summary>
		public AIBehavior AI
		{get; set;}

		/// <summary>
		/// If true then this player is an AI player.
		/// </summary>
		public override bool IsAI
		{
			get
			{return true;}
		}
	}

    /// <summary>
    /// An ABP for the game of hearts.
    /// </summary>
    public class HeartsABP : AlphaBetaPruner
    {
        /// <summary>
        /// Creates a new ABP for a hearts game.
        /// </summary>
		/// <param name="depth">The search depth in the ABP tree.</param>
        public HeartsABP(uint depth = 12)
        {
            SearchDepth = depth;
            return;
        }

        /// <summary>
        /// Obtains an enumerator for all possible moves on the current state.
        /// </summary>
        /// <param name="state">The state to enumerate moves for.</param>
        /// <returns>Returns an enumerator for all possible moves.</returns>
        /// <remarks>If better moves are returned first the pruning will be quicker.</remarks>
        protected override IEnumerator<Tuple<GameState, Move>> GetMoveEnumerator(GameState state)
        {return new HeartsEnumerator(state);}

        /// <summary>
        /// Assigns a score to the given game state.
        /// Lower values states are worse states than higher value states.
        /// </summary>
        /// <param name="state">The state to evaluate.</param>
		/// <param name="active_player">The player we want to maximize for.</param>
        /// <returns>Returns a score for the given state.</returns>
        /// <remarks>Zero is not an acceptable value.</remarks>
        protected override uint Evaluate(GameState state, int active_player)
        {
            HeartsGameState s = state as HeartsGameState;
			uint ret = uint.MaxValue >> 4; // Zero is unacceptable so start with a large number just in case
            
			// All we need to do is minimize the active player's score (this is a simple AI)
			// The exception is, all things being equal, play the higher card (if someone wants to write an AI that can shoot they can do so on their own)
			// We also want to dump point cards and playing the last of a suit is good

			// Score should be the main focus
			ret += (uint)s.GetHeartsPlayer(active_player).Score * 10000;
			
			// Find the last play made by the player (which must exist)
			IList<HeartsMove> log = s.Log;
			Card play = null;

			for(int i = log.Count - 1;i >= 0;i--)
				if(log[i].Player == active_player)
				{
					play = log[i].Play;
					break;
				}

			// Interleave value into our evaluation
			ret += (uint)play.Value.MaxValue * 5; // This is, of course, way less than 10000 at most and is positive

			// We want to get rid of the queen of spades as fast as possible
			if(play.Equals(new StandardCard(new Spades(),new ValueN(12.0,"Queen"))))
				if(s.TrickSoFar().Count != 0) // We don't want to lead the queen of spades if we can avoid it
					ret -= 25;
				else
					ret += 2000; // This sends us way over other related cards
			else if(play.Suit.Equals(new Hearts()))
				if(s.TrickSoFar().Count != 0) // We want to avoid leading points
					ret -= 25;
				else
					ret += 1000; // This keeps us under the massive point spike of the queen of spades but still way better than other plays

			// See if the player has more of the suit
			bool has_other_of_suit = false;

			foreach(Card c in s.GetHeartsPlayer(active_player).CardsInHand.Cards)
				if(c.Suit.Equals(play.Suit) && !c.Equals(play)) // Same suit but not same card
				{
					has_other_of_suit = true;
					break;
				}

			// If we have no more of the suit then this is a good move to play
			if(!has_other_of_suit)
				ret += 1000 + (uint)(15.0 - play.Value.MaxValue) * 5; // This is slightly better than playing an ace of hearts but not as good as a queen of spades

            return ret;
        }

		/// <summary>
		/// Creates a deep copy of this AIBehavior.
		/// </summary>
		/// <returns>Returns a copy of this AIBehavior.</returns>
		public override AIBehavior Clone()
		{return new HeartsABP(SearchDepth);}

        /// <summary>
        /// Enumerates moves for a game of hearts.
        /// </summary>
		/// <remarks>This enumerator is valid even if the game state has changes made to it.</remarks>
        protected class HeartsEnumerator : StandardEnumerator
        {
            /// <summary>
            /// Creates a new enumerator for a game of hearts.
            /// </summary>
            /// <param name="state">The current state to enumerate from.</param>
            public HeartsEnumerator(GameState state) : base(state,(game,move) => (game as HeartsGameState).ApplyRoundlessMove(move),(game) => (game as HeartsGameState).UndoRoundlessMove())
            {return;}

            /// <summary>
            /// Gets the move for the current item.
            /// </summary>
            /// <param name="consider">The card to consider playing.</param>
            /// <param name="player">The player considering the move.</param>
            /// <returns>Returns the move for the current item.</returns>
            protected override Move GetCurrentMove(Card consider, int player)
            {return new HeartsMove(cards[current],player_index);}
        }
    }
}
