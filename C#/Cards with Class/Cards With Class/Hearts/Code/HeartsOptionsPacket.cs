using System;
using Engine.CWCPackets;

namespace HeartsGame.Code
{
	/// <summary>
	/// A packet containing options for a hearts game.
	/// </summary>
	public class HeartsOptionsPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public HeartsOptionsPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsOptionsPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsOptionsPacket(string packet) : base(packet)
        {
            if(PacketID != "Hearts Options")
            {
                SetFail();
                return;
            }

            string[] split = Message.Split(new string[] {"hundred"},2,StringSplitOptions.None);
            
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

            split = split[1].Substring(1).Split(new string[] {"/hundred"},2,StringSplitOptions.None);

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

			HundredReset = btest;
            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="player">The option for hundred point resets.</param>
		public HeartsOptionsPacket(bool hundred) : base("Hearts Option","<hundred>" + hundred + "</hundred>")
		{
			HundredReset = hundred;
			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            HundredReset = false;

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

            return "<tag>Hearts Options</tag><hundred>" + HundredReset + "</hundred>"; // There is no junk after the packet
        }

        /// <summary>
        /// If true then scoring exactly one hundred points resets that player's score to zero.
        /// </summary>
        public bool HundredReset
        {get; protected set;}
	}
}
