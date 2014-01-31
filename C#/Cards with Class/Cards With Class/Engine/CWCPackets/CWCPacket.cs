
namespace Engine.CWCPackets
{
    /// <summary>
    /// The bare bones of what a packet is for the Cards With Class game.
    /// </summary>
    public class CWCPacket
    {
        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPacket(byte[] packet) : this(packet.ToString())
        {
			Valid = true;
			return;
		}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPacket(string packet)
        {
            string[] split = packet.Split(new char[] {'>'},3);
            
            if(split.Length != 3 || split[0].Replace("<","").Trim() != "tag")
            {
                SetFail();
                return;
            }
            
            Message = split[2];
            split = split[1].Split(new char[] {'<'},2);
                
            if(split.Length != 2 || split[1].Trim() != "/tag")
            {
                SetFail();
                return;
            }
            
            PacketID = split[0].Trim();
			Valid = true;
            return;
        }

        /// <summary>
        /// Creates a new packet from the given information.
        /// </summary>
        /// <param name="ID">The ID for the packet to have.</param>
        /// <param name="msg">The message for the packet to have.</param>
        public CWCPacket(string ID, string msg)
        {
            PacketID = ID;
            Message = msg;

			Valid = true;
            return;
        }

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected virtual void SetFail()
        {
            PacketID = "";
            Message = "";

			Valid = false;
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

            return "<tag>" + PacketID + "</tag>" + Message;
        }

        /// <summary>
        /// The type of packet this is.
        /// </summary>
        public string PacketID
        {get; protected set;}

        /// <summary>
        /// The contents of the packet.
        /// </summary>
        public string Message
        {get; protected set;}

		/// <summary>
		/// If true then the packet is valid.
		/// </summary>
		public bool Valid
		{get; protected set;}
    }
}
