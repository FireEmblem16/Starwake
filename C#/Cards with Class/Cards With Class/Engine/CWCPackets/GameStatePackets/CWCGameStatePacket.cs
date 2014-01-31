using System;
using Engine.Game;

namespace Engine.CWCPackets.GameStatePackets
{
	/// <summary>
	/// Contains a game state in a packet.
	/// </summary>
	public class CWCGameStatePacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCGameStatePacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCGameStatePacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCGameStatePacket(string packet) : base(packet)
        {
            if(PacketID != "Game State")
            {
                SetFail();
                return;
            }

            string[] split = Message.Split(new string[] {"state"},2,StringSplitOptions.None);
            
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

            split = split[1].Substring(1).Split(new string[] {"/state"},2,StringSplitOptions.None);

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

            State = split[0].Substring(0,split[0].Length - 1);
            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="player">The game state.</param>
		public CWCGameStatePacket(GameState state) : base("Game State","<state>" + state.Serialize() + "</state>")
		{
			State = state.Serialize();
			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            State = "";

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

            return "<tag>Game State</tag><state>" + State + "</state>"; // There is no junk after a state
        }

        /// <summary>
        /// The game state.
        /// </summary>
        public string State
        {get; protected set;}
	}
}
