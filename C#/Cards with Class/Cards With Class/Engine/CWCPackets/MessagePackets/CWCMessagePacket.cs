using System;

namespace Engine.CWCPackets.MessagePackets
{
	/// <summary>
	/// A packet that contains a text message.
	/// </summary>
	public class CWCMessagePacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCMessagePacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCMessagePacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCMessagePacket(string packet) : base(packet)
        {
            if(PacketID != "Message")
            {
                SetFail();
                return;
            }

            string[] split = Message.Split(new string[] {"sender"},2,StringSplitOptions.None);
            
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

            split = split[1].Substring(1).Split(new string[] {"/sender"},2,StringSplitOptions.None);
            split[0] = split[0].TrimEnd();

            if(split.Length != 2 || split[1].Trim()[0] != '>' || split[0][split[0].Length - 1] != '<')
            {
                SetFail();
                return;
            }

            Sender = split[0].Substring(0,split[0].Length - 1);
			split[1] = split[1].Substring(1).TrimStart();

			split = split[1].Split(new string[] {"message"},2,StringSplitOptions.None);

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

			split = split[1].Substring(1).Split(new string[] {"/message"},2,StringSplitOptions.None);

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

			Text = split[0].Substring(0,split[0].Length - 1);
            return;
        }

        /// <summary>
        /// Creates a new packet from the given information.
        /// </summary>
        /// <param name="msg">The message the packet should carry.</param>
		/// <param name="sender">The name of the sender of the message.</param>
        public CWCMessagePacket(string msg, string sender) : base("Message","<sender>" + sender + "</sender><message>" + msg + "</message>")
        {
            Text = msg;
			Sender = sender;

            return;
        }

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Text = "";
			Sender = "";

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

            return "<tag>Message</tag><sender>" + Sender + "</sender><message>" + Text + "</message>"; // There is no junk after a message
        }

        /// <summary>
        /// The text of the message.
        /// </summary>
        public string Text
        {get; protected set;}

		/// <summary>
		/// The name of the sender.
		/// </summary>
		public string Sender
		{get; protected set;}
	}
}
