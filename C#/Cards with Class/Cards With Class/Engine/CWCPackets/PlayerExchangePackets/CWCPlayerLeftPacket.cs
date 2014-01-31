﻿using System;

namespace Engine.CWCPackets.PlayerExchangePackets
{
	/// <summary>
	/// A packet for a player leaving a game.
	/// </summary>
	public class CWCPlayeLeftPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCPlayeLeftPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPlayeLeftPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPlayeLeftPacket(string packet) : base(packet)
        {
            if(PacketID != "Player Left")
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
            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="player">The player that joined.</param>
		public CWCPlayeLeftPacket(int player) : base("Player Left","<player>" + player + "</player>")
		{
			Player = player;
			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Player = int.MinValue;

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

            return "<tag>Player Left</tag><player>" + Player + "</player>"; // There is no junk after a leave
        }

        /// <summary>
        /// The player that is leaving.
        /// </summary>
        public int Player
        {get; protected set;}
	}
}
