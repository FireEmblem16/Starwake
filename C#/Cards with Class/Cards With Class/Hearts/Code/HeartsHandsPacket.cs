using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Engine.CWCPackets;
using Engine.Cards;

namespace HeartsGame.Code
{
	/// <summary>
	/// Contains all the cards in each player's hand in a game of hearts.
	/// </summary>
	public class HeartsHandsPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public HeartsHandsPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsHandsPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsHandsPacket(string packet) : base(packet)
        {
            if(PacketID != "Hands List")
            {
                SetFail();
                return;
            }

			// Get how many cards and players we will have to parse
            string[] split = Message.Split(new string[] {"count"},2,StringSplitOptions.None);
            
            if(split.Length != 2)
            {
                SetFail();
                return;
            }

            split[1] = split[1].TrimStart();

            if(split[0].Trim() != "<" || split[1][0] != '>')
            {
                SetFail();
                return;
            }

            split = split[1].Substring(1).Split(new string[] {"/count"},2,StringSplitOptions.None);

			if(split.Length != 2)
			{
				SetFail();
				return;
			}

            split[0] = split[0].TrimEnd();
			split[1] = split[1].TrimStart();

            if(split[0][split[0].Length - 1] != '<' || split[1].TrimStart()[0] != '>')
            {
                SetFail();
                return;
            }

            int itest;
			
			if(!int.TryParse(split[0].Substring(0,split[0].Length - 1).Trim(),out itest))
			{
				SetFail();
				return;
			}

			// Get the player
			split = split[1].TrimStart().Substring(1).Split(new string[] {"player"},2,StringSplitOptions.None);

			if(split.Length != 2)
			{
				SetFail();
				return;
			}

			if(split[0].Trim() != "<" || split[1].TrimStart()[0] != '>')
			{
				SetFail();
				return;
			}

			split = split[1].TrimStart().Substring(1).Split(new string[] {"/player"},2,StringSplitOptions.None);

			if(split.Length != 2)
			{
				SetFail();
				return;
			}

			if(split[0].TrimEnd()[split[0].Length - 1] != '<')
			{
				SetFail();
				return;
			}

			split[0] = split[0].TrimEnd();
			split[0] = split[0].Substring(0,split[0].Length - 1).Trim();

			int itest2;

			if(!int.TryParse(split[0],out itest2))
			{
				SetFail();
				return;
			}

			Player = itest2;

			// Initialize moves
			cards = new List<string>(itest);

			// Start working on the list of cards
			split[1] = split[1].TrimStart().Substring(1);
			split = split[1].Split(new string[] {"card"},(itest << 1) + 1,StringSplitOptions.None);

			if(split.Length != (itest << 1) + 1)
			{
				SetFail();
				return;
			}

			for(int i = 0;i < itest;i++)
				if(split[(i << 1) + 1][split[(i << 1) + 1].Length - 1] != '/')
				{
					SetFail();
					return;
				}
				else
					split[(i << 1) + 1] = split[(i << 1) + 1].Substring(0,split[(i << 1) + 1].Length - 1);

			for(int i = 0;i < split.Length;i++)
				split[i] = split[i].Trim();

			if(split[0] != "<" || split[split.Length - 1][0] != '>') // The last index is not > but > and the list of players
			{
				SetFail();
				return;
			}

			split[split.Length - 1] = split[split.Length - 1].Substring(1).TrimStart();

			for(int i = 1;i < split.Length - 1;i++)
				if(split[i][0] != '>' || split[i][split[i].Length - 1] != '<')
				{
					SetFail();
					return;
				}

			for(int i = 2;i < split.Length - 2;i+=2)
				if(split[i].Substring(1,split[i].Length - 2).Trim() != "")
				{
					SetFail();
					return;
				}

			for(int i = 0 ;i < itest;i++)
				cards.Add(split[(i << 1) + 1].Substring(1,split[(i << 1) + 1].Length - 2).Trim());

            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="hands">The cards.</param>
		/// <param name="player">The player this hand is for.</param>
		public HeartsHandsPacket(IList<Card> hands, int player) : base("Hands List","<count>" + hands.Count + "</count><player>" + player + "</player>" + ListToString(hands))
		{
			cards = new List<string>(hands.Count);
			Player = player;

			foreach(Card t in hands)
				cards.Add(t.ToString());

			return;
		}

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="hands">The cards.</param>
		/// <param name="player">The player this hand is for.</param>
		public HeartsHandsPacket(IList<string> hands, int player) : base("Hands List","<count>" + hands.Count + "</count><player>" + player + "</player>" + ListToString(hands))
		{
			cards = new List<string>(hands.Count);
			Player = player;

			foreach(string t in hands)
				cards.Add(t);

			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            cards = new List<string>();

            base.SetFail();
            return;
        }

		/// <summary>
        /// Reverts this packet to a string.
        /// </summary>
        /// <returns>Returns the string version of this packet.</returns>
        public override string ToString()
        {
            if(!Valid)
                return "";

            return "<tag>Hands List</tag><count>" + Cards.Count + "</count><player>" + Player + "</player>" + ListToString(Cards); // There is no junk after the packet
        }

		/// <summary>
		/// Creates the message section from the list of passes provided.
		/// </summary>
		/// <param name="names">The hands to pack.</param>
		/// <returns>Returns the moves packaged into partial packet form.</returns>
		protected static string ListToString(IList<Card> hands)
		{
			string part1 = "";

			foreach(Card t in hands)
				part1 += "<card>" + t.ToString() + "</card>";

			return part1;
		}

		/// <summary>
		/// Creates the message section from the list of passes provided.
		/// </summary>
		/// <param name="names">The hands to pack.</param>
		/// <returns>Returns the moves packaged into partial packet form.</returns>
		protected static string ListToString(IList<string> hands)
		{
			string part1 = "";

			foreach(string t in hands)
				part1 += "<card>" + t + "</card>";

			return part1;
		}

        /// <summary>
        /// The players in the packet.
        /// </summary>
        public IList<string> Cards
        {
			get
			{return cards.AsReadOnly();}
			protected set
			{
				if(value == null)
					cards = null;
				else
					cards = new List<string>(value);

				return;
			}
		}

		/// <summary>
		/// The player this is a hand for.
		/// </summary>
		public int Player
		{get; protected set;}

		/// <summary>
		/// Contains the actual data for Cards. 
		/// </summary>
		protected List<string> cards;
	}
}
