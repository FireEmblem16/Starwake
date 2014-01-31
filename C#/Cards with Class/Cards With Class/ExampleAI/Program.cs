#pragma warning disable 0067

using System;
using System.Collections.Generic;
using Engine.Cards;
using Engine.Cards.DeckTypes;
using Engine.Cards.Hands;
using Engine.Game;
using Engine.Player;
using Engine.Player.AI;
using Engine.Player.AI.ABP;

namespace Cards_With_Class
{
	/// <summary>
	/// Runs the program.
	/// </summary>
	public class Program
	{
		/// <summary>
		/// Program entry point.
		/// </summary>
		/// <param name="args">The command line arguments given to the program.</param>
		public static void Main(string[] args)
		{
			StandardHand hand = new StandardHand();
			Deck deck = new StandardDeck();

			foreach(Card c in deck.Draw(7))
				hand.DrawCard(c);
			
			hand.SortBySuit();
			hand.SortByValue();

			GS test_state = new GS();
			test_state.hand1 = new StandardHand(deck.Draw(10));
			test_state.hand2 = new StandardHand(deck.Draw(10));

			ABP test_abp = new ABP();
			GSMove m = test_abp.GetNextMove(test_state) as GSMove;
			
			return;
		}

		public class ABP : AlphaBetaPruner
		{
			public ABP()
			{
				SearchDepth = 7;
				return;
			}

			/// <summary>
			/// Obtains an enumerator for all possible moves on the current state.
			/// </summary>
			/// <param name="state">The state to enumerate moves for.</param>
			/// <returns>Returns an enumerator for all possible moves.</returns>
			/// <remarks>If better moves are returned first the pruning will be quicker.</remarks>
			protected override IEnumerator<Tuple<GameState,Move>> GetMoveEnumerator(GameState state)
			{
				// This is an example so it's not well coded
				// It would be much better to enumerate the moves one at a time so that we don't have to clone the game state so much
				GS s = state as GS;
				List<Tuple<GameState,Move>> moves = new List<Tuple<GameState,Move>>();

				if(s.ActivePlayer == 0)
					for(int i = 0;i < s.hand1.CardsInHand;i++)
					{
						GSMove m = new GSMove();
						m.card_to_play = i;

						GS next = s.Clone() as GS;
						next.ApplyMove(m);

						moves.Add(new Tuple<GameState,Move>(next,m));
					}
				else
					for(int i = 0;i < s.hand2.CardsInHand;i++)
					{
						GSMove m = new GSMove();
						m.card_to_play = i;

						GS next = s.Clone() as GS;
						next.ApplyMove(m);

						moves.Add(new Tuple<GameState,Move>(next,m));
					}

				return moves.GetEnumerator();
			}

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
				GS s = state as GS;
				double ret = 1000.0; // Normalizing factor to insure a positive value

				// Idea is we want to have the smallest hand possible while opponent has biggest hand
				// No real AI but it has the same effect as far as testing goes
				// Very primitive and does not account for trying to keep hand value as low as possible on a winning path (not that it matters)
				foreach(Card c in s.hand1.Cards)
					ret -= c.Value.MaxValue;

				foreach(Card c in s.hand2.Cards)
					ret += c.Value.MaxValue;

				if(ret > 1025.0) // We win the game!... I guess
					ret = 2000.0;

				return (uint)ret;
			}

			/// <summary>
			/// Creates a deep copy of this AIBehavior.
			/// </summary>
			/// <returns>Returns a copy of this AIBehavior.</returns>
			public override AIBehavior Clone()
			{return new ABP();}
		}

		public class GS : GameState
		{
			public GS()
			{
				Clockwise = true;
				ActivePlayer = 0;
				NumberOfPlayers = 2;

				return;
			}

			/// <summary>
			/// Makes whatever changes are necessary to the game state by making the provided move.
			/// </summary>
			/// <param name="move">The move to make.</param>
			/// <returns>Returns true if the move is valid and false if it is not.</returns>
			public bool ApplyMove(Move move)
			{
				GSMove mv = move as GSMove;

				if(ActivePlayer == 0)
				{
					hand1.PlayCard(mv.card_to_play);
					ActivePlayer = 1;
				}
				else
				{
					hand2.PlayCard(mv.card_to_play);
					ActivePlayer = 0;
				}

				return true;
			}

			/// <summary>
			/// Undoes the last move that occured to the game state.
			/// </summary>
			/// <returns>Returns true if the last move could be undone and false otherwise.</returns>
			public bool UndoMove()
			{return false;} // We do not store enough information to undo moves

            /// <summary>
		    /// Gets the player with the specified index.
		    /// </summary>
		    /// <param name="index">The index to check. This value should be between zero and one less than the number of players.</param>
		    /// <returns>Returns the player at the specified index.</returns>
		    public Player GetPlayer(int index)
            {return null;}

			public void PlayerLeft(int index, AIBehavior replacement = null)
			{return;}

			public bool PlayerJoined(int index)
			{return false;}

			/// <summary>
			/// Clones this game state.
			/// </summary>
			/// <returns>Returns a deep copy of this state.</returns>
			public GameState Clone()
			{
				GS ret = new GS();
				ret.hand1 = new StandardHand(hand1.Cards);
				ret.hand2 = new StandardHand(hand2.Cards);
				ret.ActivePlayer = ActivePlayer;
				ret.Clockwise = Clockwise;
				ret.NumberOfPlayers = NumberOfPlayers;

				return ret;
			}

			/// <summary>
			/// Serializes the game state.
			/// </summary>
			/// <returns>Returns the game state in string form.</returns>
			public string Serialize()
			{return "";}

			/// <summary>
			/// Unserializes the game state.
			/// </summary>
			/// <param name="str">The game state to unserialize.</param>
			/// <returns>Returns the game state in the given string.</returns>
			public static GS Unserialize(string str)
			{return null;}

            /// <summary>
            /// Determines if the provided move is valid.
            /// </summary>
            /// <param name="move">The move to check.</param>
            /// <returns>Returns true if the move is valid and false otherwise.</returns>
            public bool IsValid(Move move)
            {return true;}

			/// <summary>
			/// If true then play proceeds clockwise (or from low index to high index).
			/// If false then play proceeds counter-clockwise (or from high index to low index).
			/// </summary>
			public bool Clockwise
			{get; protected set;}

			/// <summary>
			/// The index of the active player.
			/// </summary>
			public int ActivePlayer
			{get; protected set;}

			/// <summary>
			/// The number of participating players in this game state.
			/// </summary>
			public int NumberOfPlayers
			{get; protected set;}

			/// <summary>
			/// If true then the game is over.
			/// </summary>
			public bool GameFinished
			{get; protected set;}

			/// <summary>
			/// Fired when this game state changes.
			/// </summary>
			public event GameStateChanged StateChanged;

			/// <summary>
			/// Fired when this game state reaches a finish state.
			/// </summary>
			public event GameOver Finished;

			public Hand hand1
			{get; set;}

			public Hand hand2
			{get; set;}
		}

		public class GSMove : Move
		{
			public GSMove()
			{return;}

            public GSMove(int play)
            {
                card_to_play = play;
                return;
            }

            public string Serialize()
            {return card_to_play.ToString();}

            public static GSMove Unserialize(string str)
            {
                int test;

                if(!int.TryParse(str,out test))
                    return null;

                return new GSMove(test);
            }

			/// <summary>
			/// The player that this move is for.
			/// </summary>
			public int Player
			{
				get
				{return -1;}
			}

			public int card_to_play;
		}
	}
}
