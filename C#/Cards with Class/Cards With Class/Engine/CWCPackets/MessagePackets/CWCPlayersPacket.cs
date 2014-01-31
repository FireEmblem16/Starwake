using System;
using System.Collections.Generic;

namespace Engine.CWCPackets.MessagePackets
{
	/// <summary>
	/// A packet containing the names of every player in order.
	/// </summary>
	public class CWCPlayersPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public CWCPlayersPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPlayersPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public CWCPlayersPacket(string packet) : base(packet)
        {
            if(PacketID != "Player Names")
            {
                SetFail();
                return;
            }

            string[] split = Message.Split(new string[] {"count"},2,StringSplitOptions.None);
            
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

            split = split[1].Substring(1).Split(new string[] {"/count"},2,StringSplitOptions.None);

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

			Players = new List<string>(itest);
			
			if(split[1].TrimStart()[0] != '>')
			{
				SetFail();
				return;
			}

			split[1] = split[1].TrimStart().Substring(1);
			split = split[1].Split(new string[] {"name"},(itest << 1) + 1,StringSplitOptions.None);

			if(split.Length != (itest << 1) + 1)
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

			for(int i = 0 ;i < itest;i++)
				Players.Add(split[(i << 1) + 1].Substring(1,split[(i << 1) + 1].Length - 2)); // We'll accept whatever whitespace padding is on the names since it seems to be important, i guess
			
            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="player">The player this packet is a confirmation for.</param>
		/// <param name="success">If the move was accepted or not.</param>
		public CWCPlayersPacket(IList<string> players) : base("Player Names","<count>" + players.Count + "</count>" + ListToString(players))
		{
			Players = new List<string>(players);
			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            Players = null;

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

            return "<tag>Player Names</tag><count>" + Players.Count + "</count>" + ListToString(Players); // There is no junk after the packet
        }

		/// <summary>
		/// Creates the names part of packet data for the given names.
		/// </summary>
		/// <param name="names">The names to pack.</param>
		/// <returns>Returns the names packaged into partial packet form.</returns>
		protected static string ListToString(IList<string> names)
		{
			string ret = "";

			foreach(string str in names)
				ret += "<name>" + str + "/<name>";

			return ret;
		}

        /// <summary>
        /// The players in the packet.
        /// </summary>
        public IList<string> PlayerNames
        {
			get
			{return Players.AsReadOnly();}
			protected set
			{
				if(value == null)
					Players = null;
				else
					Players = new List<string>(value);

				return;
			}
		}

		/// <summary>
		/// Contains the actual data for PlayerNames. 
		/// </summary>
		protected List<string> Players;
	}
}
