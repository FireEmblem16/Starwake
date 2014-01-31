#pragma warning disable 0162

using System.Collections.Generic;
using System.Windows.Forms;
using Engine.CWCPackets;
using Engine.Game.Screens;
using HeartsGame.Code;
using HeartsGame.Properties;
using Networking;

namespace HeartsGame
{
	/// <summary>
	/// A waiting screen for hearts.
	/// </summary>
	public partial class HeartsWaitingScreen : WaitingScreen
	{
		/// <summary>
		/// Creates a new waiting screen.
		/// </summary>
		/// <param name="top">The window to return to if this screen exits for any reason other than starting the game.</param>
		/// <param name="nio">The network stream for sending and recieving data to and from players. This class is not responsible for opening or closing this stream nor managing the connections to the stream.</param>
		/// <param name="player">The index of the local player.</param>
		/// <param name="name">The name of the local player.</param>
		/// <param name="is_host">If true then this machine is hosting the game.</param>
		public HeartsWaitingScreen(Form top, NetworkStream nio, int player, string name, bool is_host = false, bool hundred_reset = false) : base(top,nio,player,name,is_host)
		{
			hreset = hundred_reset;
			return;
		}

		/// <summary>
		/// Called when all other initialization logic has finished to provided custom initialization logic.
		/// </summary>
		protected override void Initialize()
		{return;}

		/// <summary>
		/// Sends out the game options information over the network stream.
		/// </summary>
		protected override void QueueOptions()
		{
			nio.QueuePacket(new HeartsOptionsPacket(hreset).ToString());
			return;
		}

		/// <summary>
		/// Called when a packet is recieved and this class does not know what to do with it.
		/// </summary>
		/// <returns>Returns true if the packet was used and false otherwise.</returns>
		/// <remarks>If the packet was used then it should be removed by this function. If false then it should be left in the stream.</remarks>
		protected override bool UnknownPacket()
		{
			// We need to deal with passing packets
			string peek = nio.PeekString();

			if(peek != null) // Should never be given where this function is called from but just in case
			{
				CWCPacket packet = new CWCPacket(peek);

				switch(packet.PacketID)
				{
				case "Hearts Options":
					if(is_host)
						break; // This is a packet only clients are concerned with
						
					HeartsOptionsPacket options = new HeartsOptionsPacket(packet);

					if(options.Valid)
						hreset = options.HundredReset;

					nio.GetString(); // We are done with the packet
					return true;
					break;
				}
			}

			return false;
		}

		/// <summary>
		/// Gets a name for the AI at the given seat.
		/// </summary>
		/// <param name="seat">The seat the AI is at.</param>
		/// <returns>Returns the name of the AI.</returns>
		protected override string GetAIName(int seat)
		{return "AI " + seat;}

		/// <summary>
		/// The table for this game.
		/// </summary>
		/// <param name="top">The form to return to when the game finishes.</param>
		/// <param name="nio">The network stream for sending and recieving data to and from players. This class is not responsible for opening or closing this stream nor managing the connections to the stream.</param>
		/// <param name="names">The names of the players in the game. If AI players are present then names should be provided in their positions.</param>
		/// <param name="seat">The index of the player at this table.</param>
		/// <param name="host">If ture then this table is hosting the game.</param>
		/// <param name="humans">An array indicating which players are human and which are AI (true is human, false is AI).</param>
		/// <returns>Returns a table of this game type.</returns>
		/// <remarks>Should return a new copy every call.</remarks>
		protected override Form CreateTable(Form top, NetworkStream nio, IList<string> names, int seat, bool host, bool[] humans)
		{return new HeartsData().Table(top,nio,names,seat,host,humans,Resources.ResourceManager,hreset);}

		/// <summary>
		/// Called when this window has been disposed to handle any additional cleaning logic.
		/// </summary>
		/// <param name="unprevoked">If true then the window was disposed without starting a game.</param>
		/// <remarks>Networking is still available at this point.</remarks>
		protected override void CleanUp(bool unprevoked)
		{return;}
		
		/// <summary>
		/// If true then we have the hundred point reset option in play.
		/// </summary>
		protected bool hreset;
	}
}
