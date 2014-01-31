using System;
using Engine.Player;

namespace Engine.CWCPackets.MovePackets
{
    /// <summary>
    /// A packet for the Cards with Class game for move data.
    /// </summary>
    public class CWCMovePacket : CWCPacket
    {
        /// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCMovePacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCMovePacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCMovePacket(string packet) : base(packet)
        {
            if(PacketID != "Move")
            {
                SetFail();
                return;
            }

            string[] split = Message.Split(new string[] {"move"},2,StringSplitOptions.None);
            
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

            split = split[1].Substring(1).Split(new string[] {"/move"},2,StringSplitOptions.None);
            split[0] = split[0].TrimEnd();

            if(split.Length != 2 || split[1].Trim() != ">" || split[0][split[0].Length - 1] != '<')
            {
                SetFail();
                return;
            }

            Move = split[0].Substring(0,split[0].Length - 1);
            return;
        }

        /// <summary>
        /// Creates a new packet from the given information.
        /// </summary>
        /// <param name="move">The move the packet should have.</param>
        public CWCMovePacket(Move move) : base("Move","<move>" + move.Serialize() + "</move>")
        {
            Move = move.Serialize();
            return;
        }

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Move = "";

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

            return "<tag>Move</tag><move>" + Move + "</move>"; // There is no junk after move
        }

        /// <summary>
        /// The serializd version of a move.
        /// </summary>
        public string Move
        {get; protected set;}
    }
}
