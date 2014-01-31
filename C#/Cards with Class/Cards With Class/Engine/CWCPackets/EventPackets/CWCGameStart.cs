using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Engine.CWCPackets.EventPackets
{
	public class CWCGameStart : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCGameStart(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCGameStart(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCGameStart(string packet) : base(packet)
        {
			if(PacketID != "Start Game")
				SetFail();

			Message = "";
			return;
		}

        /// <summary>
        /// Creates a new packet from the given information.
        /// </summary>
        public CWCGameStart() : base("Start Game","")
        {return;}
	}
}
