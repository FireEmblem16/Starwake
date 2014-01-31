using System;

namespace Engine.CWCPackets.MovePackets
{
    public class CWCMoveConfirmationPacket : CWCPacket
    {
        /// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCMoveConfirmationPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCMoveConfirmationPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCMoveConfirmationPacket(string packet) : base(packet)
        {
            if(PacketID != "Move Confirmation")
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

			split = split[1].Split(new string[] {"success"},2,StringSplitOptions.None);

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

			split = split[1].Substring(1).Split(new string[] {"/success"},2,StringSplitOptions.None);

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

			bool btest;

			if(!bool.TryParse(split[0].Substring(0,split[0].Length - 1).Trim(),out btest))
			{
				SetFail();
				return;
			}

			Success = btest;
            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="player">The player this packet is a confirmation for.</param>
		/// <param name="success">If the move was accepted or not.</param>
		public CWCMoveConfirmationPacket(int player, bool success) : base("Move Confirmation","<player>" + player + "</player><success>" + success + "</success>")
		{
			Player = player;
			Success = success;

			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Player = int.MinValue;
			Success = false;

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

            return "<tag>Move Confirmation</tag><player>" + Player + "</player><success>" + Success + "</success>"; // There is no junk after a move
        }

        /// <summary>
        /// The player that this is a confirmation for.
        /// </summary>
        public int Player
        {get; protected set;}

		/// <summary>
		/// If true then the move was accepted as valid.
		/// If false then the move was invalid.
		/// </summary>
		/// <remarks>The host will send the move packet to all players (excluding itself) so there is no need to save that packet.</remarks>
		public bool Success
		{get; protected set;}
    }
}
