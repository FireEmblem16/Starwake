using System;

namespace Engine.CWCPackets.PlayerExchangePackets
{
	/// <summary>
	/// A packet for a player succeeding to join a game.
	/// </summary>
	public class CWCPlayerJoinSuccessPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCPlayerJoinSuccessPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPlayerJoinSuccessPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPlayerJoinSuccessPacket(string packet) : base(packet)
        {
            if(PacketID != "Player Join Success")
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
			split[1] = split[1].Substring(1).TrimStart();

			split = split[1].Split(new string[] {"name"},2,StringSplitOptions.None);

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

			split = split[1].Substring(1).Split(new string[] {"/name"},2,StringSplitOptions.None);

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

			Name = split[0].Substring(0,split[0].Length - 1);
            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="player">The player that joined.</param>
		/// <param name="name">The name of the player that joined.</param>
		public CWCPlayerJoinSuccessPacket(int player, string name) : base("Player Join Success","<player>" + player + "</player><name>" + name + "</name>")
		{
			Player = player;
			Name = name;

			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Player = int.MinValue;
			Name = "";

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

            return "<tag>Player Join Success</tag><player>" + Player + "</player><name>" + Name + "</name>"; // There is no junk after a join success
        }

        /// <summary>
        /// The player that joined.
        /// </summary>
        public int Player
        {get; protected set;}

		/// <summary>
		/// The name of the player that joined.
		/// </summary>
		public string Name
		{get; protected set;}
	}
}
