#pragma warning disable 0162

using System;
using System.Collections.Generic;
using Engine.Cards;
using Engine.Cards.CardTypes;
using Engine.Cards.CardTypes.Suits;
using Engine.Cards.CardTypes.Values;
using Engine.Cards.DeckTypes;
using Engine.Game;
using Engine.Player;
using Engine.Player.AI;

namespace ExampleFormGame
{
    /// <summary>
    /// The gamestate for a game of hearts.
    /// </summary>
    public class HeartsGameState : GameState
	{
#region Constructors
		/// <summary>
        /// Creates a new gamestate for the game of hearts.
        /// </summary>
		/// <param name="north_human">If true then north will be a human player.</param>
        /// <param name="west_human">If true then west will be a human player.</param>
        /// <param name="south_human">If true then south will be a human player.</param>
        /// <param name="east_human">If true then east will be a human player.</param>
        /// <param name="hundred_reset">If true then if a player attains exactly 100 points then they will be sent back to zero.</param>
        /// <param name="AI">The AI to use if a non-default AI is desired.</param>
        public HeartsGameState(bool north_human = true, bool west_human = false, bool south_human = false, bool east_human = false, bool hundred_reset = false, AIBehavior AI = null)
        {
            TheDeck = new StandardDeck();
			
			if(north_human)
				North = new HeartsPlayer(TheDeck.Draw(13));
			else
				North = new HeartsAI(TheDeck.Draw(13),AI);

			if(south_human)
				South = new HeartsPlayer(TheDeck.Draw(13));
			else
				South = new HeartsAI(TheDeck.Draw(13),AI);
			
			if(east_human)
				East = new HeartsPlayer(TheDeck.Draw(13));
			else
				East = new HeartsAI(TheDeck.Draw(13),AI);

			if(west_human)
				West = new HeartsPlayer(TheDeck.Draw(13));
			else
				West = new HeartsAI(TheDeck.Draw(13),AI);
			
			North.CardsInHand.SortBySuit();
			South.CardsInHand.SortBySuit();
			East.CardsInHand.SortBySuit();
			West.CardsInHand.SortBySuit();

			log = new List<HeartsMove>();
			leaders_log = new List<int>();

			// First player is the one holding the two of clubs
			Card two_of_clubs = new StandardCard(new Clubs(),new ValueN(2.0,"Duece"));

			for(int i = 0;i < NumberOfPlayers;i++)
				if(GetPlayer(i).CardsInHand.Cards.Contains(two_of_clubs))
				{
					ActivePlayer = i;
					break;
				}

			leaders_log.Add(ActivePlayer);

			Round = 1;
			Trick = 1;
			EOT = false;

			HundredReset = hundred_reset;
			HeartsBroken = false;
			PointsTaken = new bool[NumberOfPlayers];

			for(int i = 0;i < NumberOfPlayers;i++)
				PointsTaken[i] = false;

            return;
        }
#endregion

#region GameState Functions
        /// <summary>
        /// Determines if the provided move is valid.
        /// </summary>
        /// <param name="move">The move to check.</param>
        /// <returns>Returns true if the move is valid and false otherwise.</returns>
        public bool IsValid(Move move)
        {return IsValid(move as HeartsMove);}

		/// <summary>
        /// Makes whatever changes are necessary to the game state by making the provided move.
        /// </summary>
        /// <param name="move">The move to make.</param>
        /// <returns>Returns true if the move is valid and false if it is not.</returns>
        public bool ApplyMove(Move move)
        {
            HeartsMove mv = move as HeartsMove;

			// Check if the move is valid
            if(mv == null || !IsValid(mv))
				return false;

			// Move is valid so do it
			if(!GetPlayer(mv.Player).MakePlay(move))
				return false;

			// Banish the card to the discard pile
			TheDeck.Discard(mv.Play);

			// Log the move
			log.Add(mv);
			
			// Break hearts
			if(mv.Play.Suit.Equals(new Hearts()))
				HeartsBroken = true;

			// Move the game along
			ActivePlayer = (ActivePlayer + 1) % NumberOfPlayers;

			// If we've finished a trick do trick collection logic
			if(ActivePlayer == leaders_log[leaders_log.Count - 1])
			{
				// We are at the end of the trick for the moment
				EOT = true;

				// Get the cards in the trick
				IList<Card> trick = TrickSoFar();

				// Find the trick taker
				int taker_index = (leaders_log[leaders_log.Count - 1] + HighestCard(trick)) % NumberOfPlayers;
				HeartsPlayer taker = GetHeartsPlayer(taker_index);

				// Add any points in the trick
				foreach(Card c in trick)
				{
					int points = Points(c);
					taker.Score += points;

					// Keep track of who has taken points this round to determine moon shooting
					if(points != 0)
						PointsTaken[taker_index] = true;
				}

				// Fire the end of trick event
				if(EndTrick != null)
					EndTrick(this);

				// Increment to the next trick
				++Trick;

				// We can only go to a new round when we have finished our 13th trick
				if(Trick == 14)
				{
					// Check if someone has shot
					int one = -1;
					bool two = false;

					for(int i = 0;i < NumberOfPlayers;i++)
						if(one < 0 && PointsTaken[i])
							one = i;
						else if(!two && PointsTaken[i])
						{
							two = true;
							break;
						}

					// If we didn't find a second point taker then the first one has shot
					if(!two)
					{
						// This player has shot
						// For ease we will subtract 52 and add 26
						GetHeartsPlayer(one).Score -= 52;

						North.Score += 26;
						South.Score += 26;
						East.Score += 26;
						West.Score += 26;
					}

					// Check if anyone is at 100 points if necessary
					if(HundredReset)
						for(int i = 0;i < NumberOfPlayers;i++)
							if(GetHeartsPlayer(i).Score == 100)
								GetHeartsPlayer(i).Score = 0;

					// Check if a player has gone over 100 points
					if(GameFinished)
					{
						--Trick; // Just because its right and proper
						
						// Signal that the game state has changed
						if(StateChanged != null)
							StateChanged(this,mv);

						// Signal that the game is over
						if(Finished != null)
							Finished(this);

						return true;
					}

					// Signal that a round has ended
					if(EndRound != null)
						EndRound(this);

					// No one has lost so start another round
					Redeal();

					for(int i = 0;i < NumberOfPlayers;i++)
						PointsTaken[i] = false;

					++Round;
					Trick = 1;
					HeartsBroken = false;

					log.Clear(); // We only log for a single round at a time
					leaders_log.Clear();

					// Fire the passing event
					if(Passing != null)
						Passing(this);

					// Find who has the two of clubs now and start the leaders log
					Card two_of_clubs = new StandardCard(new Clubs(),new ValueN(2.0,"Duece"));

					for(int i = 0;i < NumberOfPlayers;i++)
						if(GetPlayer(i).CardsInHand.Cards.Contains(two_of_clubs))
						{
							ActivePlayer = i;
							break;
						}

					leaders_log.Add(ActivePlayer);

					// We should move on to the next trick now instead of waiting
					EOT = false;

					// Signal that a new round is starting
					if(StartRound != null)
						StartRound(this);
				}

				// If we haven't started a new round then we need to figure out who's leading next
				if(EOT)
				{
					leaders_log.Add(taker_index); // The new leader is the person who took the trick
					ActivePlayer = taker_index;

					// We have moved on from the end of the trick phase
					EOT = false;
				}

				// We fire the trick start now after new rounds start (and the event is fired) and after EOT is false again
				if(StartTrick != null)
					StartTrick(this);
			}

			// Signal that the game state has changed
			if(StateChanged != null)
				StateChanged(this,mv);

            return true;
        }

        /// <summary>
		/// Undoes the last move that occured to the game state.
		/// The only event fired is the state change event.
		/// </summary>
		/// <returns>Returns true if the last move could be undone and false otherwise.</returns>
        public bool UndoMove()
        {
			if(log.Count == 0)
				return false;

			HeartsMove last = log[log.Count - 1];
			log.RemoveAt(log.Count - 1);

			// First undo the move in the player and pull the card out of the discard
			GetPlayer(last.Player).UndoMove(last);
			TheDeck.DiscardPile.Remove(last.Play);

			// Check if we might need to unbreak hearts
			if(last.Play.Suit.Equals(new Hearts()))
			{
				// Check if there are any hearts played before this one in the log
				bool good = false;

				for(int i = log.Count - 1;i >= 0;i--)
					if(log[i].Play.Suit.Equals(new Hearts()))
					{
						good = true;
						break;
					}

				// We couldn't find a heart so unbreak them
				if(!good)
					HeartsBroken = false;
			}

			// Roll the turn back
			int old_active = ActivePlayer;
			--ActivePlayer;

			// If we went too far then fix that
			if(ActivePlayer < 0)
				ActivePlayer = NumberOfPlayers - 1;

			// If we need to rollback the trick, do so
			if(old_active == leaders_log[leaders_log.Count - 1])
			{
				// Remove the latest leader
				if(Trick != 1) // If we are at the start of the round we don't want to lose our first leader
				{
					log.Add(null); // For testing GameFinished

					if(!GameFinished) // If the game was finished then we jumped out of our apply before adding a new leader to the leader log
						leaders_log.RemoveAt(leaders_log.Count - 1);

					log.RemoveAt(log.Count - 1); // We are done with the null

					// We are going back one trick
					--Trick;
				}

				// The active player is the person just before the last leader
				ActivePlayer = leaders_log[leaders_log.Count - 1] - 1;

				// Handle wrap around
				if(ActivePlayer < 0)
					ActivePlayer += NumberOfPlayers;

				// Get the trick that we no longer have finished to remove points (note that we can never undo to a previous round so we don't have to worry about undo a shoot)
				// Note that we don't have to mess with EOT since we already removed the last card played at the start of the undo
				IList<Card> trick = TrickSoFar();
				trick.Add(last.Play);

				// Find out who took the trick
				int taker_index = (leaders_log[leaders_log.Count - 1] + HighestCard(trick)) % NumberOfPlayers;
				HeartsPlayer taker = GetHeartsPlayer(taker_index);

				// Remove any points in the trick
				foreach(Card c in trick)
					taker.Score -= Points(c);

				// Recalculate the points taken array for this player
				PointsTaken[taker_index] = false;

				for(int i = 0;i < Trick - 1 && !PointsTaken[taker_index];i++) // We are on an incomplete trick so don't check the last one and break if we find some points
				{
					List<Card> past = new List<Card>(NumberOfPlayers);

					// Get the ith trick
					for(int j = 0;j < NumberOfPlayers;j++)
						past.Add(log[i * NumberOfPlayers + j].Play);

					// Find out who took this trick
					int past_taker = (leaders_log[i] + HighestCard(past)) % NumberOfPlayers;

					// If this is not the player in question then don't bother doing anything further this trick
					if(past_taker != taker_index)
						continue;

					// Check if there are any points in this trick
					foreach(Card c in past)
						if(Points(c) != 0)
						{
							PointsTaken[taker_index] = true;
							break; // We have at least one point card so we don't have to do anything else
						}
				}

				// We can't revisit previous rounds so we don't have to worry about shooting or hundred point resets or anything else and thus we are done
			}

			// Fire the state change event for the undo
			if(StateChanged != null)
				StateChanged(this,last,true);

			return true;
		}

		/// <summary>
		/// Gets the player with the specified index.
		/// </summary>
		/// <param name="index">The index to check. This value should be between zero and one less than the number of players.</param>
		/// <returns>Returns the player at the specified index.</returns>
		public Player GetPlayer(int index)
		{
			switch(index)
			{
			case 0:
				return North;
				break;
			case 3:
				return West;
				break;
			case 2:
				return South;
				break;
			case 1:
				return East;
				break;
			}

			return null;
		}

		/// <summary>
		/// Gets the player with the specified index.
		/// </summary>
		/// <param name="index">The index to check. This value should be between zero and one less than the number of players.</param>
		/// <returns>Returns the player at the specified index.</returns>
		public HeartsPlayer GetHeartsPlayer(int index)
		{
			switch(index)
			{
			case 0:
				return North;
				break;
			case 3:
				return West;
				break;
			case 2:
				return South;
				break;
			case 1:
				return East;
				break;
			}

			return null;
		}

		/// <summary>
		/// If a player leaves the game then they are replaced with an AI player.
		/// </summary>
		/// <param name="index">The index of the player that left.</param>
		/// <param name="replacement">The replacement AI. If null then a default behavior will be used.</param>
		/// <remarks>An AI player can leave the game to be replaced by a new AI player.</remarks>
		public void PlayerLeft(int index, AIBehavior replacement = null)
		{
			int points = 0;

			switch(index)
			{
			case 0:
				points = North.Score;
				North = new HeartsAI(North.CardsInHand.Cards,replacement);
				North.Score = points;

				break;
			case 1:
				points = East.Score;
				East = new HeartsAI(East.CardsInHand.Cards,replacement);
				East.Score = points;

				break;
			case 2:
				points = South.Score;
				South = new HeartsAI(South.CardsInHand.Cards,replacement);
				South.Score = points;

				break;
			case 3:
				points = West.Score;
				West = new HeartsAI(West.CardsInHand.Cards,replacement);
				West.Score = points;

				break;
			}

			return;
		}

		/// <summary>
		/// Called to let a player join the game replacing an AI player.
		/// </summary>
		/// <param name="index">The index of the AI player to replace.</param>
		/// <returns>Returns true if the player joined the game and false otherwise.</returns>
		/// <remarks>Only AI players can be booted for a human player to join.</remarks>
		public bool PlayerJoined(int index)
		{
			int points = 0;

			switch(index)
			{
			case 0:
				if(North is HeartsAI)
					return false;

				points = North.Score;
				North = new HeartsPlayer(North.CardsInHand.Cards);
				North.Score = points;

				break;
			case 1:
				if(East is HeartsAI)
					return false;

				points = East.Score;
				East = new HeartsPlayer(East.CardsInHand.Cards);
				East.Score = points;

				break;
			case 2:
				if(South is HeartsAI)
					return false;

				points = South.Score;
				South = new HeartsPlayer(South.CardsInHand.Cards);
				South.Score = points;

				break;
			case 3:
				if(West is HeartsAI)
					return false;

				points = West.Score;
				West = new HeartsPlayer(West.CardsInHand.Cards);
				West.Score = points;

				break;
			}

			return true;
		}

        /// <summary>
        /// Clones this game state. All events will be null in the returned copy.
        /// </summary>
        /// <returns>Returns a deep copy of this state.</returns>
        public GameState Clone()
        {
            HeartsGameState ret = new HeartsGameState();
            
			ret.TheDeck = new StandardDeck();
			ret.TheDeck.Draw(52); // Throw everything into the discard
			
			foreach(Card c in TheDeck.DiscardPile)
				ret.TheDeck.Discard(c);
			
			ret.North = North.Clone() as HeartsPlayer;
			ret.South = South.Clone() as HeartsPlayer;
			ret.East = East.Clone() as HeartsPlayer;
			ret.West = West.Clone() as HeartsPlayer;

			ret.HundredReset = HundredReset;

            ret.ActivePlayer = ActivePlayer;
			
			ret.Round = Round;
			ret.Trick = Trick;
			
			ret.PointsTaken = new bool[NumberOfPlayers];
			ret.EOT = EOT;

			for(int i = 0;i < ret.PointsTaken.Length;i++)
				ret.PointsTaken[i] = PointsTaken[i];

			foreach(HeartsMove m in log)
				ret.log.Add(new HeartsMove(m));

			ret.leaders_log.Clear(); // We don't want the initially placed element for the overwritten initial hands

			foreach(int i in leaders_log)
				ret.leaders_log.Add(i);

			ret.HeartsBroken = HeartsBroken;

			ret.StateChanged = null;
			ret.Finished = null;
			ret.StartRound = null;
			ret.StartTrick = null;
			ret.EndRound = null;
			ret.EndTrick = null;

            return ret;
        }

		/// <summary>
		/// Serializes the game state.
		/// </summary>
		/// <returns>Returns the game state in string form.</returns>
		/// <remarks>Only clients will ever need to unserialize a game state and they do not need AI algorithms so we're just going to skim over that. We also skip over events.</remarks>
		public string Serialize()
		{
			string ret = "";

			// First serialize the discard pile
			ret += TheDeck.CountDiscardPile + "\t";
			ret += TheDeck.ToString(DeckStringFormat.DISCARD_PILE_CONTENTS).Replace(", ","\t").Split(new char[] {'['},2)[1].Replace("]","") + "\t";
			
			// Serialize North
			ret += North.IsAI + "\t";
			ret += North.CardsInHand.CardsInHand + "\t";

			foreach(Card c in North.CardsInHand.Cards)
				ret += c.ToString() + "\t";

			// Serialize South
			ret += South.IsAI + "\t";
			ret += South.CardsInHand.CardsInHand + "\t";

			foreach(Card c in South.CardsInHand.Cards)
				ret += c.ToString() + "\t";

			// Serialize East
			ret += East.IsAI + "\t";
			ret += East.CardsInHand.CardsInHand + "\t";

			foreach(Card c in East.CardsInHand.Cards)
				ret += c.ToString() + "\t";

			// Serialize West
			ret += West.IsAI + "\t";
			ret += West.CardsInHand.CardsInHand + "\t";

			foreach(Card c in West.CardsInHand.Cards)
				ret += c.ToString() + "\t";

			// Serialize log
			ret += log.Count + "\t";

			foreach(HeartsMove move in log)
				ret += move.Play.ToString() + "\t" + move.Player + "\t";

			// Serialize leaders log
			ret += leaders_log.Count + "\t";

			foreach(int i in leaders_log)
				ret += i + "\t";

			// Serialize a bunch of states
			ret += Trick + "\t" + Round + "\t" + EOT + "\t" + HundredReset + "\t" + HeartsBroken + "\t";

			// Serialize points taken
			ret += PointsTaken.Length + "\t";

			for(int i = 0;i < PointsTaken.Length;i++)
				ret += PointsTaken[i] + "\t";

			// Serialize the active player
			ret += ActivePlayer;

			return ret;
		}

		/// <summary>
		/// Unserializes the game state.
		/// </summary>
		/// <param name="old">The old game state. We need this to copy events from it to this new game state.</param>
		/// <param name="str">The game state to unserialize.</param>
		/// <returns>Returns the game state in the given string.</returns>
		/// <remarks>Only clients will ever need to unserialize a game state so we skipped over AI algorithms. We also skip over events.</remarks>
		public static HeartsGameState Unserialize(HeartsGameState old, string str)
		{
			HeartsGameState ret = new HeartsGameState();
			string[] split = str.Split('\t');
			int min_len = 0;

			// Copy events
			old.CopyEventsTo(ret);

#region Unserialize Discards
			// Pull out the cards in the discard pile (remember they need to be discarded in reverse order that we get them)
			int dcount;

			// See how many discards there are
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out dcount) || dcount < 0)
				return null;
			
			List<Card> discards = new List<Card>(dcount);
			min_len += dcount; // We need something from split for every discard

			if(split.Length < min_len)
				return null;

			// Get every discard
			for(int i = 0;i < dcount;i++)
			{
				// Find the card
				int index = ret.TheDeck.Deck.IndexOf(new StandardCard(new Hearts(),new ValueN(0.0,""),split[i + min_len - dcount]));

				if(index < 0)
					return null;

				discards.Add(ret.TheDeck.Deck[index]);
			}

			// Discard all of these cards (we could discard them to the bottom instead of the top but that would take longer)
			for(int i = discards.Count - 1;i >= 0;i--)
				ret.TheDeck.Discard(discards[i]);
#endregion

#region Unserialize Players
			bool is_ai;

			// Check if North is an AI or not (not that it really matters)
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out is_ai))
				return null;

			int hcount;

			// Start unserializeing North
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out hcount) || hcount < 0)
				return null;

			List<Card> hand = new List<Card>(hcount);
			min_len += hcount;

			// Get every card in North's hand
			for(int i = 0;i < hcount;i++)
			{
				// Find the card
				int index = ret.TheDeck.Deck.IndexOf(new StandardCard(new Hearts(),new ValueN(0.0,""),split[i + min_len - hcount]));

				if(index < 0)
					return null;

				hand.Add(ret.TheDeck.Deck[index]);
			}

			// Initialize North
			if(is_ai)
				ret.North = new HeartsAI(hand);
			else
				ret.North = new HeartsPlayer(hand);

			// Sort North's hand
			ret.North.CardsInHand.SortBySuit();

			// Check if South is an AI or not (not that it really matters)
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out is_ai))
				return null;

			// Start unserializeing South
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out hcount) || hcount < 0)
				return null;

			hand = new List<Card>(hcount);
			min_len += hcount;

			// Get every card in South's hand
			for(int i = 0;i < hcount;i++)
			{
				// Find the card
				int index = ret.TheDeck.Deck.IndexOf(new StandardCard(new Hearts(),new ValueN(0.0,""),split[i + min_len - hcount]));

				if(index < 0)
					return null;

				hand.Add(ret.TheDeck.Deck[index]);
			}

			// Initialize South
			if(is_ai)
				ret.South = new HeartsAI(hand);
			else
				ret.South = new HeartsPlayer(hand);

			// Sort South's hand
			ret.South.CardsInHand.SortBySuit();

			// Check if East is an AI or not (not that it really matters)
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out is_ai))
				return null;

			// Start unserializeing East
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out hcount) || hcount < 0)
				return null;

			hand = new List<Card>(hcount);
			min_len += hcount;

			// Get every card in East's hand
			for(int i = 0;i < hcount;i++)
			{
				// Find the card
				int index = ret.TheDeck.Deck.IndexOf(new StandardCard(new Hearts(),new ValueN(0.0,""),split[i + min_len - hcount]));

				if(index < 0)
					return null;

				hand.Add(ret.TheDeck.Deck[index]);
			}

			// Initialize East
			if(is_ai)
				ret.East = new HeartsAI(hand);
			else
				ret.East = new HeartsPlayer(hand);

			// Sort East's hand
			ret.East.CardsInHand.SortBySuit();

			// Check if West is an AI or not (not that it really matters)
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out is_ai))
				return null;

			// Start unserializeing West
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out hcount) || hcount < 0)
				return null;

			hand = new List<Card>(hcount);
			min_len += hcount;

			// Get every card in West's hand
			for(int i = 0;i < hcount;i++)
			{
				// Find the card
				int index = ret.TheDeck.Deck.IndexOf(new StandardCard(new Hearts(),new ValueN(0.0,""),split[i + min_len - hcount]));

				if(index < 0)
					return null;

				hand.Add(ret.TheDeck.Deck[index]);
			}

			// Initialize West
			if(is_ai)
				ret.West = new HeartsAI(hand);
			else
				ret.West = new HeartsPlayer(hand);

			// Sort West's hand
			ret.West.CardsInHand.SortBySuit();
#endregion

#region Unserialize Logs
			int lcount;

			// See how many logs there are
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out lcount) || lcount < 0)
				return null;

			ret.log = new List<HeartsMove>(lcount);
			min_len += (lcount << 1);

			// Make sure we have the whole log
			if(split.Length < min_len)
				return null;

			for(int i = 0;i < lcount;i++)
			{
				// Find the card
				int index = ret.TheDeck.Deck.IndexOf(new StandardCard(new Hearts(),new ValueN(0.0,""),split[(i << 1) + min_len - (lcount << 1)]));

				if(index < 0)
					return null;

				int player;

				// Get the player for the move
				if(!int.TryParse(split[(i << 1) + min_len - (lcount << 1) + 1],out player) || player < 0 || player > 3)
					return null;

				// Add the move to the log
				ret.log.Add(new HeartsMove(ret.TheDeck.Deck[index],player));
			}

			// See how many leader logs there are
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out lcount) || lcount < 0)
				return null;

			ret.leaders_log = new List<int>(lcount);
			min_len += lcount;

			// Make sure we have the whole leaders log
			if(split.Length < min_len)
				return null;

			for(int i = 0;i < lcount;i++)
			{
				int player;

				// Get the leader
				if(!int.TryParse(split[i + min_len - lcount],out player) || player < 0 || player > 3)
					return null;

				ret.leaders_log.Add(player);
			}
#endregion

#region Unserialize States
			int itemp;

			// Get the trick number
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out itemp) || itemp < 1 || itemp > 14)
				return null;

			ret.Trick = itemp;

			// Get the round number
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out itemp) || itemp < 1) // Rounds can go on forever in theory
				return null;

			ret.Round = itemp;

			bool btemp;

			// Get EOT value
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out btemp))
				return null;

			ret.EOT = btemp;

			// Get hundred reset state
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out btemp))
				return null;

			ret.HundredReset = btemp;

			// Get hearts broken state
			if(split.Length < ++min_len || !bool.TryParse(split[min_len - 1],out btemp))
				return null;

			ret.HeartsBroken = btemp;
#endregion

#region Unserialize Points Taken
			int ptcount;

			// Get the length of the points taken array (should be the number of players [4] but whatever)
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out ptcount) || ptcount < 0)
				return null;

			ret.PointsTaken = new bool[ptcount];
			min_len += ptcount;

			if(split.Length < min_len)
				return null;

			// Get every entry in the array
			for(int i = 0;i < ptcount;i++)
			{
				bool pti;

				// Get the value for the ith player
				if(!bool.TryParse(split[i + min_len - ptcount],out pti))
					return null;

				ret.PointsTaken[i] = pti;
			}
#endregion

#region Unserialize Active Player
			int active;

			// Get the active player
			if(split.Length < ++min_len || !int.TryParse(split[min_len - 1],out active) || active < 0)
				return null;

			ret.ActivePlayer = active;
#endregion

			// We don't want extra junk in the state (it could have meaning we don't know about)
			if(split.Length != min_len)
				return null;

			return ret;
		}
#endregion

#region HelperFunctions
		/// <summary>
		/// Determines if the provided move is valid.
		/// </summary>
		/// <param name="move">The move to check.</param>
		/// <returns>Returns true if the move can be made and false otherwise.</returns>
		public bool IsValid(HeartsMove move)
		{
			if(move.Player != ActivePlayer)
				return false;

			// We have to lead the two of clubs every round
			if(log.Count == 0 && !move.Play.Equals(new StandardCard(new Clubs(),new ValueN(2.0,"Duece"))))
				return false;

			// We can't lead points first round unless we really have no other choice
			if(log.Count < 4 && Points(move.Play) > 0)
			{
				foreach(Card c in GetPlayer(move.Player).CardsInHand.Cards)
					if(Points(c) == 0)
						return false;

				return true; // We only have point cards so any of them are fine to play
			}

			// Get details of the current trick
			IList<Card> trick = TrickSoFar();
			Card lead = trick.Count > 0 ? trick[0] : null;

			// Get the hand of the relevant player
			Hand hand = GetPlayer(move.Player).CardsInHand;

			// Check if this is the first play of the trick
			if(lead == null)
				if(HeartsBroken || !move.Play.Suit.Equals(new Hearts())) // Any lead is valid if hearts is broken and when its not then any non-heart is valid
					return true;
				else // Hearts is not borken and we are trying to lead a heart which is only valid if we only have hearts
				{
					foreach(Card c in hand.Cards)
						if(!c.Suit.Equals(move.Play.Suit)) // The suit is hearts so let's use it
							return false; // We have a non-heart

					return true; // We only have hearts
				}

			// Check that the play is either on suit or the player has no other choice
			// Also make sure that if hearts has shown up that it is either being broken, has been broken or the player has no other option (all of which are already done above)
			if(move.Play.Suit.Equals(lead.Suit))
				return true; // If we're on suit then we're good
			else // Any move is valid if we do not have any cards of the lead suit
				foreach(Card c in hand.Cards)
					if(c.Suit.Equals(lead.Suit))
						return false; // We have an on suit card so this move is invalid

			return true;
		}

		/// <summary>
		/// Returns the cards already played in the trick.
		/// </summary>
		/// <returns>Returns a list of cards already in the trick. These will be ordered from first play to last.</returns>
		public IList<Card> TrickSoFar()
		{
			List<Card> ret = new List<Card>(4);

            if(leaders_log.Count == 0)
                return ret;

			int plays = ActivePlayer - leaders_log[leaders_log.Count - 1];

			// Handle wrap around
			if(plays < 0)
				plays += NumberOfPlayers;

			// Determine if we have 0 or N plays so far
			if(plays == 0 && EOT)
				plays = NumberOfPlayers;

			for(int i = plays;i > 0;i--)
				ret.Add(log[log.Count - i].Play);

			return ret;
		}

		/// <summary>
		/// Returns the index of the highest card played in the trick.
		/// </summary>
		/// <param name="trick">The cards in the trick. This must be ordered from fist play to last. It need not be a complete trick.</param>
		/// <returns>Returns the index of the highest card in the trick. If there are no cards in the trick -1 is returned.</returns>
		public int HighestCard(IList<Card> trick)
		{
			// Check if there are cards in the trick yet
			if(trick.Count == 0)
				return -1;

			// If there's only one card in the trick we are done
			if(trick.Count == 1)
				return 0;

			int ret = 0; // First card is always on suit
			CardSuit lead = trick[0].Suit;

			for(int i = 1;i < trick.Count;i++)
				if(trick[i].Suit.Equals(lead)) // Check if we are on suit
					if(trick[i].Value.MaxValue > trick[ret].Value.MaxValue) // Aces are high in the standard deck (value 14)
						ret = i;

			return ret;
		}

		/// <summary>
		/// Calculates how many points the given card is worth.
		/// </summary>
		/// <param name="c">The card to check.</param>
		/// <returns>Returns the point value of the provided card.</returns>
		public int Points(Card c)
		{
			if(c.Suit.Equals(new Hearts()))
				return 1;
			else if(c.Equals(new StandardCard(new Spades(),new ValueN(12.0,"Queen"))))
				return 13;

			return 0;
		}

		/// <summary>
		/// Redeals 13 cards to each player.
		/// </summary>
		protected void Redeal()
		{
			TheDeck.Reshuffle();

			North.CardsInHand.DrawCards(TheDeck.Draw(13));
			South.CardsInHand.DrawCards(TheDeck.Draw(13));
			East.CardsInHand.DrawCards(TheDeck.Draw(13));
			West.CardsInHand.DrawCards(TheDeck.Draw(13));

			return;
		}

		/// <summary>
		/// Copies the events to the given game state from this game state.
		/// Does nothing if the two game states are not of the same type.
		/// </summary>
		/// <param name="new_state">The game state to copy events to.</param>
		protected void CopyEventsTo(GameState new_state)
		{
			if(StateChanged != null)
				foreach(Delegate d in StateChanged.GetInvocationList())
					new_state.StateChanged += d as GameStateChanged;
			
			if(Finished != null)
				foreach(Delegate d in Finished.GetInvocationList())
					new_state.Finished += d as GameOver;

			if(StartRound != null)
				foreach(Delegate d in StartRound.GetInvocationList())
					(new_state as HeartsGameState).StartRound += d as NewRound;

			if(StartTrick != null)
				foreach(Delegate d in StartTrick.GetInvocationList())
					(new_state as HeartsGameState).StartTrick += d as NewTrick;

			if(EndRound != null)
				foreach(Delegate d in EndRound.GetInvocationList())
					(new_state as HeartsGameState).EndRound += d as EndOfRound;

			if(EndTrick != null)
				foreach(Delegate d in EndTrick.GetInvocationList())
					(new_state as HeartsGameState).EndTrick += d as EndOfTrick;

			return;
		}
#endregion

#region AI HelperFunctions
		/// <summary>
        /// Used for AI purposes. Since rounds endings can not be undone (and we don't really want to check past them anyways) this will only do round ending logic without starting a new round.
		/// Also fires no events.
        /// </summary>
        /// <param name="move">The move to make.</param>
        /// <returns>Returns true if the move is valid and false if it is not.</returns>
        public bool ApplyRoundlessMove(Move move)
        {
            HeartsMove mv = move as HeartsMove;

			// Check if the move is valid
            if(mv == null || !IsValid(mv))
				return false;

			// Move is valid so do it
			if(!GetPlayer(mv.Player).MakePlay(move))
				return false;

			// Banish the card to the discard pile
			TheDeck.Discard(mv.Play);

			// Log the move
			log.Add(mv);

			// Break hearts
			if(mv.Play.Suit.Equals(new Hearts()))
				HeartsBroken = true;

			// Move the game along
			ActivePlayer = (ActivePlayer + 1) % NumberOfPlayers;

			// If we've finished a trick do trick collection logic
			if(ActivePlayer == leaders_log[leaders_log.Count - 1])
			{
				// We are at the end of the trick for the moment
				EOT = true;

				// Get the cards in the trick
				IList<Card> trick = TrickSoFar();

				// Find the trick taker
				int taker_index = (leaders_log[leaders_log.Count - 1] + HighestCard(trick)) % NumberOfPlayers;
				HeartsPlayer taker = GetHeartsPlayer(taker_index);

				// Add any points in the trick
				foreach(Card c in trick)
				{
					int points = Points(c);
					taker.Score += points;

					// Keep track of who has taken points this round to determine moon shooting
					if(points != 0)
						PointsTaken[taker_index] = true;
				}

				// Increment to the next trick
				++Trick;

				// We can only go to a new round when we have finished our 13th trick
				if(Trick == 14)
				{
					// Check if someone has shot
					int one = -1;
					bool two = false;

					for(int i = 0;i < NumberOfPlayers;i++)
						if(one < 0 && PointsTaken[i])
							one = i;
						else if(!two && PointsTaken[i])
						{
							two = true;
							break;
						}

					// If we didn't find a second point taker then the first one has shot
					if(!two)
					{
						// This player has shot
						// For ease we will subtract 52 and add 26
						GetHeartsPlayer(one).Score -= 52;

						North.Score += 26;
						South.Score += 26;
						East.Score += 26;
						West.Score += 26;
					}

					// Check if anyone is at 100 points if necessary
					if(HundredReset)
						for(int i = 0;i < NumberOfPlayers;i++)
							if(GetHeartsPlayer(i).Score == 100)
								GetHeartsPlayer(i).Score = 0;

					// Check if a player has gone over 100 points
					if(GameFinished)
					{
						--Trick; // Just because its right and proper
						return true;
					}

					// Do not start a new round
				}

				// If we haven't started a new round then we need to figure out who's leading next
				if(EOT)
				{
					leaders_log.Add(taker_index); // The new leader is the person who took the trick
					ActivePlayer = taker_index;

					// We have moved on from the end of the trick phase
					EOT = false;
				}
			}

            return true;
        }

		/// <summary>
		/// Used for AI purposes. Undoes an application of a roundless move. This means will take into account if the game finished and we have to undo finish logic.
		/// Also fires no events.
		/// </summary>
		/// <returns>Returns true if the last move could be undone and false otherwise.</returns>
        public bool UndoRoundlessMove()
        {
			if(log.Count == 0)
				return false;

			HeartsMove last = log[log.Count - 1];
			log.RemoveAt(log.Count - 1);

			// First undo the move in the player and pull the card out of the discard
			GetPlayer(last.Player).UndoMove(last);
			TheDeck.TakeFromDiscard(TheDeck.DiscardPile.IndexOf(last.Play));
			
			// Check if we might need to unbreak hearts
			if(last.Play.Suit.Equals(new Hearts()))
			{
				// Check if there are any hearts played before this one in the log
				bool good = false;

				for(int i = log.Count - 1;i >= 0;i--)
					if(log[i].Play.Suit.Equals(new Hearts()))
					{
						good = true;
						break;
					}

				// We couldn't find a heart so unbreak them
				if(!good)
					HeartsBroken = false;
			}

			// Roll the turn back
			int old_active = ActivePlayer;
			--ActivePlayer;

			// If we went too far then fix that
			if(ActivePlayer < 0)
				ActivePlayer = NumberOfPlayers - 1;

			// If we need to rollback the trick, do so
			if(old_active == leaders_log[leaders_log.Count - 1])
			{
				// Remove the latest leader
				if(Trick != 1) // If we are at the start of the round we don't want to lose our first leader
				{
					log.Add(null); // For testing GameFinished

					if(!GameFinished) // If the game was finished then we jumped out of our apply before adding a new leader to the leader log
						leaders_log.RemoveAt(leaders_log.Count - 1);

					log.RemoveAt(log.Count - 1); // We are done with the null

					// We are going back one trick
					--Trick;
				}

				// The active player is the person just before the last leader
				ActivePlayer = leaders_log[leaders_log.Count - 1] - 1;

				// Handle wrap around
				if(ActivePlayer < 0)
					ActivePlayer += NumberOfPlayers;

				// Get the trick that we no longer have finished to remove points (note that we can never undo to a previous round so we don't have to worry about undo a shoot)
				// Note that we don't have to mess with EOT since we already removed the last card played at the start of the undo
				IList<Card> trick = TrickSoFar();
				trick.Add(last.Play);

				// Find out who took the trick
				int taker_index = (leaders_log[leaders_log.Count - 1] + HighestCard(trick)) % NumberOfPlayers;
				HeartsPlayer taker = GetHeartsPlayer(taker_index);

				// Remove any points in the trick
				foreach(Card c in trick)
					taker.Score -= Points(c);

				// Recalculate the points taken array for this player
				PointsTaken[taker_index] = false;

				for(int i = 0;i < Trick - 1 && !PointsTaken[taker_index];i++) // We are on an incomplete trick so don't check the last one and break if we find some points
				{
					List<Card> past = new List<Card>(NumberOfPlayers);

					// Get the ith trick
					for(int j = 0;j < NumberOfPlayers;j++)
						past.Add(log[i * NumberOfPlayers + j].Play);

					// Find out who took this trick
					int past_taker = (leaders_log[i] + HighestCard(past)) % NumberOfPlayers;

					// If this is not the player in question then don't bother doing anything further this trick
					if(past_taker != taker_index)
						continue;

					// Check if there are any points in this trick
					foreach(Card c in past)
						if(Points(c) != 0)
						{
							PointsTaken[taker_index] = true;
							break; // We have at least one point card so we don't have to do anything else
						}
				}

				// We have to now undo end of round logic
				if(log.Count == 51) // If we've played all but one card then we just undid the end of the round
				{
					// We finished the game so our trick is going to be off by one
					Trick = 13;

					// We are undoing round end logic so undo the point change for the last trick for now
					foreach(Card c in trick)
						taker.Score += Points(c);

					// Total up everyone's points for the round
					int[] points = new int[NumberOfPlayers];

					for(int i = 0;i < NumberOfPlayers;i++)
						points[i] = 0;

					// For ease we'll make this (btw totally duplicating code from above but who cares)
					IList<IList<Card>> tricks = new List<IList<Card>>(Trick + 1);

					for(int i = 0;i < Trick - 1;i++) // Don't try adding the last trick because it won't work
					{
						tricks.Add(new List<Card>(NumberOfPlayers));

						for(int j = 0;j < NumberOfPlayers;j++)
							tricks[i].Add(log[i * NumberOfPlayers + j].Play);
					}

					tricks.Add(trick); // The last trick

					// Now actually calculate points
					for(int i = 0;i < tricks.Count;i++)
					{
						int trick_taker = (leaders_log[i] + HighestCard(tricks[i])) % NumberOfPlayers;

						foreach(Card c in tricks[i])
							points[trick_taker] += Points(c);
					}

					// Check if someone shot
					int shooter = -1;

					for(int i = 0;i < NumberOfPlayers;i++)
						if(points[i] == 26)
						{
							shooter = i;
							break;
						}

					// Someone shot so change points appropriately
					if(shooter != -1)
					{
						points[shooter] -= 52; // This will net the shooter -26 net points which will yield +26 to score (modified by points not taken yet in the last trick)

						// Add 26 to the points scored by each player
						for(int i = 0;i < NumberOfPlayers;i++)
							points[i] += 26;
					}

					// Check if we have 100 points rollback
					if(HundredReset)
					{
						// Now check if anyone that scored points is at 0 to see if we need to set their score back to 100
						for(int i = 0;i < NumberOfPlayers;i++)
							if(GetHeartsPlayer(i).Score == 0 && points[i] > 0) // A player has zero points but did not take zero points or shoot so they must have hit 100
								GetHeartsPlayer(i).Score = 100;
					}

					if(shooter != -1)
						for(int i = 0;i < NumberOfPlayers;i++)
							GetHeartsPlayer(i).Score -= points[i]; // No one has shot yet so remove those points from scores (note that the shooting player gets +26 as required)

					// Reapply the point change for not taking the last trick
					foreach(Card c in trick)
						taker.Score -= Points(c);
				}
			}

			return true;
		}
#endregion

#region Variables
		/// <summary>
		/// If true then play proceeds clockwise (or from low index to high index).
		/// If false then play proceeds counter-clockwise (or from high index to low index).
		/// </summary>
		public bool Clockwise
		{
			get
			{return true;}
		}

		/// <summary>
		/// The index of the active player.
		/// </summary>
		public int ActivePlayer
		{get; protected set;}

		/// <summary>
		/// The leader of the current trick.
		/// </summary>
		/// <remarks>Use the setter with caution.</remarks>
		public int Leader
		{
			get
			{
                if(leaders_log.Count == 0)
                    return 0;

                return leaders_log[leaders_log.Count - 1];
            }
			set
			{
				if(ActivePlayer == Leader)
					ActivePlayer = value;

				leaders_log[leaders_log.Count - 1] = value;
				return;
			}
		}

		/// <summary>
		/// The number of participating players in this game state.
		/// </summary>
		public int NumberOfPlayers
		{
			get
			{return 4;}
		}

		/// <summary>
		/// Fired when this game state changes.
		/// </summary>
		public event GameStateChanged StateChanged;

		/// <summary>
		/// Fired when this game state reaches a finish state.
		/// </summary>
		public event GameOver Finished;

		/// <summary>
		/// Fired when a new round is started.
		/// </summary>
		public event NewRound StartRound;

		/// <summary>
		/// Fired when the passing phase begins.
		/// </summary>
		public event PassingEvent Passing;

		/// <summary>
		/// Fired when a round is ended.
		/// </summary>
		public event EndOfRound EndRound;

		/// <summary>
		/// Fired when a new trick is started.
		/// </summary>
		public event NewTrick StartTrick;

		/// <summary>
		/// Fired when a trick is ended.
		/// </summary>
		public event EndOfTrick EndTrick;

        /// <summary>
        /// The north player.
        /// </summary>
		/// <remarks>This is player 0.</remarks>
        public HeartsPlayer North
        {get; set;}

        /// <summary>
        /// The south player.
        /// </summary>
		/// <remarks>This is player 2.</remarks>
        public HeartsPlayer South
        {get; set;}

        /// <summary>
        /// The east player.
        /// </summary>
		/// <remarks>This is player 1.</remarks>
        public HeartsPlayer East
        {get; set;}

        /// <summary>
        /// The west player.
        /// </summary>
		/// <remarks>This is player 3.</remarks>
        public HeartsPlayer West
        {get; set;}

		/// <summary>
		/// The current round of the game.
		/// </summary>
		public int Round
		{get; protected set;}

		/// <summary>
		/// The current trick of the round.
		/// </summary>
		public int Trick
		{get; protected set;}

		/// <summary>
		/// If true then if a player attains exactly 100 points they will be sent back to zero.
		/// </summary>
		public bool HundredReset
		{get; protected set;}

		/// <summary>
		/// If an index is true then the player at that index has taken a point this turn.
		/// </summary>
		public bool[] PointsTaken
		{get; protected set;}

		/// <summary>
		/// If true then hearts have been broken.
		/// </summary>
		public bool HeartsBroken
		{get; protected set;}

		/// <summary>
		/// If true then the game is over.
		/// </summary>
		public bool GameFinished
		{
			get
			{
				// We can't end end the game if we are not at the end of a round (noting that when we reach the end of the game we leave the state at the end of the round)
				if(log.Count != TheDeck.CountDeckSize)
					return false;

				if(HundredReset) // Just in case we will only check for scores greater than 100 if we reset to 0 at 100 points
				{
					if(North.Score > 100)
						return true;
					else if(South.Score > 100)
						return true;
					else if(East.Score > 100)
						return true;
					else if(West.Score > 100)
						return true;
				}
				else if(North.Score >= 100)
					return true;
				else if(South.Score >= 100)
					return true;
				else if(East.Score >= 100)
					return true;
				else if(West.Score >= 100)
					return true;

				return false;
			}
		}

		/// <summary>
		/// Contains a log of moves made so far during the current round.
		/// </summary>
		public IList<HeartsMove> Log
		{
			get
			{return log.AsReadOnly();}
		}

		/// <summary>
		/// Logs all moves from the beginning of the gamestate.
		/// </summary>
		protected List<HeartsMove> log;

		/// <summary>
		/// Logs the leaders for every trick in a round.
		/// </summary>
		protected List<int> leaders_log;

		/// <summary>
		/// The deck this game is played with.
		/// </summary>
		protected StandardDeck TheDeck;

		/// <summary>
		/// If true then we are between the end of a trick and the start of a new one.
		/// In other words if we call TrickSoFar we will get the full trick instead of an empty trick if this is true.
		/// </summary>
		protected bool EOT;
#endregion
	}

	/// <summary>
	/// An event that is fired just before new round logic is executed.
	/// </summary>
	/// <param name="state">The state of the game at the end of a round.</param>
	public delegate void EndOfRound(GameState state);

	/// <summary>
	/// An event that is fired at the beginning of the passing round.
	/// </summary>
	/// <param name="state">The state of the game at the beginning of the passing round.</param>
	public delegate void PassingEvent(GameState state);

	/// <summary>
	/// An event that is fired when a new round is started.
	/// </summary>
	/// <param name="state">The state of the game at the start of the round.</param>
	public delegate void NewRound(GameState state);

	/// <summary>
	/// An event that is fired just before new trick logic is executed.
	/// </summary>
	/// <param name="state">The state of the game at the end of a trick.</param>
	public delegate void EndOfTrick(GameState state);

	/// <summary>
	/// An event that is fired when a new trick is started.
	/// </summary>
	/// <param name="state">The state of the game at the start of the trick.</param>
	public delegate void NewTrick(GameState state);

    /// <summary>
    /// A move for a game of hearts.
    /// </summary>
    public class HeartsMove : Move
    {
        /// <summary>
        /// Creates a new hearts move.
        /// </summary>
        /// <param name="c">The card to play. A clone will be made.</param>
		/// <param name="player">The player making this move.</param>
        public HeartsMove(Card c, int player)
        {
            Play = c.Clone();
			Player = player;

			return;
        }

		/// <summary>
		/// Creates a copy of the provided move.
		/// </summary>
		/// <param name="hm">The move to copy.</param>
		public HeartsMove(HeartsMove hm)
		{
			Play = hm.Play.Clone();
			Player = hm.Player;

			return;
		}

        /// <summary>
        /// Serializes the move.
        /// </summary>
        /// <returns>Returns a string representing the move.</returns>
        public string Serialize()
        {return Player.ToString() + "\n" + Play.ToString();}

        /// <summary>
        /// Unserializes a move from the provided string.
        /// </summary>
        /// <param name="str">The string to transform into a move.</param>
        /// <returns>Returns a move if the provided string is valid and null otherwise.</returns>
        public static Move Unserialize(string str)
        {
			string[] split = str.Split('\n');
			int player;

			if(!int.TryParse(split[0],out player))
				return null;

            foreach(Card c in unserializer.Deck)
				if(c.ToString() == split[1])
					return new HeartsMove(c.Clone(),player);

            return null;
        }

        /// <summary>
        /// The card to play.
        /// </summary>
        public Card Play
        {get; protected set;}

		/// <summary>
		/// The player who made this move.
		/// </summary>
		public int Player
		{get; protected set;}

        /// <summary>
        /// Used to unserialize cards.
        /// </summary>
        protected static StandardDeck unserializer = new StandardDeck();
    }
}
