using System;
using System.Collections.Generic;
using Engine.Cards;
using Engine.CWCPackets;

namespace ExampleNetworkedGame
{
	/// <summary>
	/// Contains all the data to pass cards in a game of hearts.
	/// </summary>
	public class HeartsPassingListPacket : CWCPacket
	{
		/// <summary>
        /// Creates a new packet from the base packet provided.
        /// </summary>
        /// <param name="packet">The packet to build on.</param>
        public HeartsPassingListPacket(CWCPacket packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given byte array.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsPassingListPacket(byte[] packet) : this(packet.ToString())
        {return;}

        /// <summary>
        /// Creates a new packet from the given string.
        /// </summary>
        /// <param name="packet">The raw packet.</param>
        public HeartsPassingListPacket(string packet) : base(packet)
        {
            if(PacketID != "Passing List")
            {
                SetFail();
                return;
            }

			// Get how many cards and players we will have to parse
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

			if(split[1].TrimStart()[0] != '>')
			{
				SetFail();
				return;
			}

			// Initialize moves
			moves = new List<Tuple<string,int>>(itest);

			// Start working on the list of cards
			split[1] = split[1].TrimStart().Substring(1);
			split = split[1].Split(new string[] {"card"},(itest << 1) + 1,StringSplitOptions.None);

			if(split.Length != (itest << 1) + 1)
			{
				SetFail();
				return;
			}

			for(int i = 0;i < itest;i++)
				if(split[(i << 1) + 1][split[(i << 1) + 1].Length - 1] != '/')
				{
					SetFail();
					return;
				}
				else
					split[(i << 1) + 1] = split[(i << 1) + 1].Substring(0,split[(i << 1) + 1].Length - 1);

			for(int i = 0;i < split.Length;i++)
				split[i] = split[i].Trim();

			if(split[0] != "<" || split[split.Length - 1][0] != '>') // The last index is not > but > and the list of players
			{
				SetFail();
				return;
			}

			split[split.Length - 1] = split[split.Length - 1].Substring(1).TrimStart();

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
				moves.Add(new Tuple<string,int>(split[(i << 1) + 1].Substring(1,split[(i << 1) + 1].Length - 2).Trim(),-1)); // We don't have the player yet so add a temporary bad value

			// Move on to the player list
			split = split[split.Length - 1].Split(new string[] {"player"},(itest << 1) + 1,StringSplitOptions.None);

			if(split.Length != (itest << 1) + 1)
			{
				SetFail();
				return;
			}

			for(int i = 0;i < itest;i++)
				if(split[(i << 1) + 1][split[(i << 1) + 1].Length - 1] != '/')
				{
					SetFail();
					return;
				}
				else
					split[(i << 1) + 1] = split[(i << 1) + 1].Substring(0,split[(i << 1) + 1].Length - 1);

			for(int i = 0;i < split.Length;i++)
				split[i] = split[i].Trim();

			if(split[0] != "<" || split[split.Length - 1][0] != '>') // The last index is not > but > and the list of players
			{
				SetFail();
				return;
			}

			split[split.Length - 1] = split[split.Length - 1].Substring(1).TrimStart();

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
			{
				int itemp;

				if(!int.TryParse(split[(i << 1) + 1].Substring(1,split[(i << 1) + 1].Length - 2).Trim(),out itemp))
				{
					SetFail();
					return;
				}

				moves[i] = new Tuple<string,int>(moves[i].Item1,itemp);//split[(i << 1) + 1].Substring(1,split[(i << 1) + 1].Length - 2).Trim());
			}

            return;
        }

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="passes">The passes to make.</param>
		public HeartsPassingListPacket(IList<Tuple<Card,int>> passes) : base("Passing List","<count>" + passes.Count + "</count>" + ListToString(passes))
		{
			moves = new List<Tuple<string,int>>(passes.Count);

			foreach(Tuple<Card,int> t in passes)
				moves.Add(new Tuple<string,int>(t.Item1.ToString(),t.Item2));

			return;
		}

		/// <summary>
		/// Creates a new packet with the given information.
		/// </summary>
		/// <param name="passes">The passes to make.</param>
		public HeartsPassingListPacket(IList<Tuple<string,int>> passes) : base("Passing List","<count>" + passes.Count + "</count>" + ListToString(passes))
		{
			moves = new List<Tuple<string,int>>(passes.Count);

			foreach(Tuple<string,int> t in passes)
				moves.Add(new Tuple<string,int>(t.Item1,t.Item2));

			return;
		}

        /// <summary>
        /// Sets all parse information to failure values.
        /// </summary>
        protected override void SetFail()
        {
            moves = new List<Tuple<string,int>>();

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

            return "<tag>Passing List</tag><count>" + Moves.Count + "</count>" + ListToString(Moves); // There is no junk after the packet
        }

		/// <summary>
		/// Creates the message section from the list of passes provided.
		/// </summary>
		/// <param name="names">The moves to pack.</param>
		/// <returns>Returns the moves packaged into partial packet form.</returns>
		protected static string ListToString(IList<Tuple<Card,int>> passes)
		{
			string part1 = "";
			string part2 = "";

			foreach(Tuple<Card,int> t in passes)
			{
				part1 += "<card>" + t.Item1.ToString() + "/<card>";
				part2 += "<player>" + t.Item2 + "</player>";
			}

			return part1 + part2;
		}

		/// <summary>
		/// Creates the message section from the list of passes provided.
		/// </summary>
		/// <param name="names">The moves to pack.</param>
		/// <returns>Returns the moves packaged into partial packet form.</returns>
		protected static string ListToString(IList<Tuple<string,int>> passes)
		{
			string part1 = "";
			string part2 = "";

			foreach(Tuple<string,int> t in passes)
			{
				part1 += "<card>" + t.Item1 + "/<card>";
				part2 += "<player>" + t.Item2 + "</player>";
			}

			return part1 + part2;
		}

        /// <summary>
        /// The players in the packet.
        /// </summary>
        public IList<Tuple<string,int>> Moves
        {
			get
			{return moves.AsReadOnly();}
			protected set
			{
				if(value == null)
					moves = null;
				else
					moves = new List<Tuple<string,int>>(value);

				return;
			}
		}

		/// <summary>
		/// Contains the actual data for Moves. 
		/// </summary>
		protected List<Tuple<string,int>> moves;
	}
}
