using System;
using System.Collections.Generic;
using Engine.Cards;
using Engine.CWCPackets;

namespace HeartsGame.Code
{
	/// <summary>
	/// A packet of three cards to pass.
	/// </summary>
	public class HeartsPassingPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public HeartsPassingPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsPassingPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsPassingPacket(string packet) : base(packet)
        {
            if(PacketID != "Passing Move")
            {
                SetFail();
                return;
            }

            string[] split = Message.Split(new string[] {"player"},2,StringSplitOptions.None);
            
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

            split = split[1].Substring(1).Split(new string[] {"/player"},2,StringSplitOptions.None);

			if(split.Length != 2)
			{
				SetFail();
				return;
			}

            split[0] = split[0].TrimEnd();
			split[1] = split[1].TrimStart();

            if(split[0][split[0].Length - 1] != '<' || split[1][0] != '>')
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

			Player = itest;
			
			if(split[1].TrimStart()[0] != '>')
			{
				SetFail();
				return;
			}

			split[1] = split[1].TrimStart().Substring(1);
			split = split[1].Split(new string[] {"move"},7,StringSplitOptions.None);

			if(split.Length != 7)
			{
				SetFail();
				return;
			}

			for(int i = 0;i < 3;i++)
				if(split[(i << 1) + 1][split[(i << 1) + 1].Length - 1] != '/')
				{
					SetFail();
					return;
				}
				else
					split[(i << 1) + 1] = split[(i << 1) + 1].Substring(0,split[(i << 1) + 1].Length - 1);

			for(int i = 0;i < split.Length;i++)
				split[i] = split[i].Trim();

			if(split[0] != "<" || split[split.Length - 1] != ">")
			{
				SetFail();
				return;
			}

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

			Card1 = split[1].Substring(1,split[1].Length - 2).Trim();
			Card2 = split[3].Substring(1,split[3].Length - 2).Trim();
			Card3 = split[5].Substring(1,split[5].Length - 2).Trim();
			
            return;
        }

        /// <summary>
        /// Creates a new packet from the given information.
        /// </summary>
		/// <param name="player">The player doing the passing.</param>
		/// <param name="cards">The cards being passed.</param>
        public HeartsPassingPacket(int player, IList<Card> cards) : base("Passing Move",cards.Count == 3 ? "<player>" + player + "</player><move>" + cards[0] + "</move><move>" + cards[1] + "</move><move>" + cards[2] + "</move>" : "")
        {
			if(cards.Count != 3)
			{
				SetFail();
				return;
			}

            Card1 = cards[0].ToString();
			Card2 = cards[1].ToString();
			Card3 = cards[2].ToString();

			Player = player;
            return;
        }

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Card1 = "";
			Card2 = "";
			Card3 = "";
			Player = -1;

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

            return "<tag>Passing Move</tag><player>" + Player + "</player><move>" + Card1 + "</move><move>" + Card2 + "</move><move>" + Card3 + "</move>"; // There is no junk after a passing move
        }

		/// <summary>
		/// An array of serialized cards.
		/// </summary>
		public string[] Cards
		{
			get
			{return new string[] {Card1,Card2,Card3};}
		}

        /// <summary>
        /// The serialized version of the first card.
        /// </summary>
        public string Card1
        {get; protected set;}

		/// <summary>
        /// The serialized version of the second card.
        /// </summary>
        public string Card2
        {get; protected set;}

		/// <summary>
        /// The serialized version of the third card.
        /// </summary>
        public string Card3
        {get; protected set;}

		/// <summary>
		/// The passing player.
		/// </summary>
		public int Player
		{get; protected set;}
	}
}
