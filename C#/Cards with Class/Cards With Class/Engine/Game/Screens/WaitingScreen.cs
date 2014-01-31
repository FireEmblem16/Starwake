using System;
using System.Collections.Generic;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;
using Engine.CWCPackets;
using Engine.CWCPackets.EventPackets;
using Engine.CWCPackets.MessagePackets;
using Engine.CWCPackets.PlayerExchangePackets;
using Networking;

namespace Engine.Game.Screens
{
	/// <summary>
	/// A waiting screen that let's all players connect to the game.
	/// </summary>
	public abstract partial class WaitingScreen : Form
	{
#region Constructors
		/// <summary>
		/// Creates a new waiting screen.
		/// </summary>
		/// <param name="top">The window to return to if this screen exits for any reason other than starting the game.</param>
		/// <param name="nio">The network stream for sending and recieving data to and from players. This class is not responsible for opening or closing this stream nor managing the connections to the stream.</param>
		/// <param name="player">The index of the local player.</param>
		/// <param name="name">The name of the local player.</param>
		/// <param name="is_host">If true then this machine is hosting the game.</param>
		protected WaitingScreen(Form top, NetworkStream nio, int player, string name, bool is_host = false)
		{
			InitializeComponent();
			Disposed += new EventHandler(DisposeExtension);

			this.nio = nio;
			this.top = top;
			
			this.is_host = is_host;
			seat = player;
			LocalPlayer = name;

			HostSleep = 25;
			ClientSleep = 100;
			
			button1.Visible = is_host;
			
			richTextBox1.ReadOnly = true;
			richTextBox1.BackColor = Color.White;
			textBox1.KeyDown += new KeyEventHandler(textbox1_KeyDown);

			if(is_host)
				names.Add(new Tuple<string,int>(name,player)); // names does not need to be sorted
			else
			{
				nio.QueuePacket(new CWCPlayerJoinedPacket(player,name).ToString()); // Send joining packet (we will handle problems [like being denied to join] in the handle packets function)
				waiting_for_confirm = true;
			}

			if(is_host)
				net = new Thread(new ThreadStart(HostHandlePackets));
			else
				net = new Thread(new ThreadStart(ClientHandlePackets));

			net.Name = "Networking Thread";
			net.Start();

			Initialize();
			return;
		}
#endregion

#region Helper Functions
		/// <summary>
		/// Posts the provided message.
		/// </summary>
		/// <param name="msg">The message.</param>
		/// <param name="sender">The name of the player who sent the message.</param>
		private void PostMessage(string msg, string sender)
		{
			if(InvokeRequired)
				Invoke(new DS(SecretPostMessage),msg,sender);
			else
				SecretPostMessage(msg,sender);

			return;
		}

		/// <summary>
		/// Posts the provided message.
		/// </summary>
		/// <param name="msg">The message.</param>
		/// <param name="sender">The name of the player who sent the message.</param>
		private void SecretPostMessage(string msg, string sender)
		{
			// If this is the first post then we don't have a newline but we do need to have a name tag
			if(FirstPost)
			{
				richTextBox1.SelectionFont = new Font(richTextBox1.Font,FontStyle.Bold);
				richTextBox1.SelectionColor = Color.FromArgb(102,2,60); // Tryian Purple
				richTextBox1.AppendText(sender + ": ");
			}
			else if(sender != messages[messages.Count - 1].Item1)
			{
				// If this isn't the first post and it's not a follow up post then add an extra newline
				richTextBox1.AppendText("\n");
			
				richTextBox1.SelectionFont = new Font(richTextBox1.Font,FontStyle.Bold);
				richTextBox1.SelectionColor = Color.FromArgb(102,2,60); // Tryian Purple
				richTextBox1.AppendText(sender + ": ");
			}	
			
			richTextBox1.SelectionFont = new Font(richTextBox1.Font,FontStyle.Regular);
			richTextBox1.SelectionColor = Color.Black;
			richTextBox1.AppendText(msg + "\n");
			
			richTextBox1.SelectionStart = richTextBox1.Text.Length;
			richTextBox1.ScrollToCaret();

			messages.Add(new Tuple<string,string>(sender,msg));
			return;
		}
#endregion

#region Network Communications
		/// <summary>
		/// Handles network communications for the host.
		/// </summary>
		private void HostHandlePackets()
		{
			bool listen = true;

			// Handle connection and leaving packets
			while(listen)
			{
				string str = nio.GetString(); // We don't need to be conservative in our use of packets here

				if(str != null)
				{
					CWCPacket packet = new CWCPacket(str);

					if(packet.Valid)
						switch(packet.PacketID)
						{
						case "Player Joined":
							CWCPlayerJoinedPacket join = new CWCPlayerJoinedPacket(packet);

							if(join.Valid)
							{
								// The host needs to determine if a seat is available so even in the unlikely chance that we get unlucky with packet order this is the best we can do
								for(int i = 0;i < names.Count;i++)
									if(names[i].Item2 == join.Player)
									{
										nio.QueuePacket(new CWCPlayerJoinFailedPacket(join.Player).ToString());
										break; // The player failed to join so we're done here
									}

								names.Add(new Tuple<string,int>(join.Name,join.Player)); // names do not need to be sorted
									
								nio.QueuePacket(new CWCPlayerJoinSuccessPacket(join.Player,join.Name).ToString()); // The player was accepted so send out the acceptance packet
								QueueOptions(); // The player needs to know about the game options

								// If we have four names then we have four players so start the game
								// Again, if we are unlucky with our packet order bad things could happen but that is still very unlikely and probably malicious in nature
								if(names.Count == 4)
								{
									listen = false; // We don't need this network thread anymore so let it die naturally
									Invoke(new EventHandler(button1_Click),null,null); // Emulate the button click to start the game
								}
							}

							break;
						case "Player Left":
							CWCPlayeLeftPacket left = new CWCPlayeLeftPacket(packet);

							if(left.Valid)
							{
								for(int i = 0;i < names.Count;i++)
									if(names[i].Item2 == left.Player)
									{
										names.RemoveAt(i);
										break; // At any one time there is at most one of this player index
									}

								nio.QueuePacket(str); // The host knows but everyone else also needs to be informed that the player left
							}

							break;
						case "Player Join Success":
							break; // Hosts should never get these
						case "Player Join Failed":
							break; // Hosts should never get these
						case "Start Game":
							break; // Hosts should never get these
						case "Message":
							CWCMessagePacket message = new CWCMessagePacket(packet);

							if(message.Valid)
							{
								PostMessage(message.Text,message.Sender);
								nio.QueuePacket(str); // Send the message out to everyone else
							}
							
							break;
						default:
							if(!UnknownPacket())
								nio.GetString(); // We have no idea what this packet is; our child has no idea what the packet is; no one knows what the packet is; trash it

							break;
						}
				}

				Thread.Sleep(is_host ? 25 : 100); // Clients have much less to do and should wait longer for more packets to show up
			}

			return;
		}

		/// <summary>
		/// Handles network communications for clients.
		/// </summary>
		private void ClientHandlePackets()
		{
			bool listen = true;

			// Handle connection and leaving packets
			while(listen)
			{
				string str = nio.GetString(); // We don't need to be conservative in our use of packets here

				if(str != null)
				{
					CWCPacket packet = new CWCPacket(str);

					if(packet.Valid)
						switch(packet.PacketID)
						{
						case "Player Joined":
							break; // Clients should never get these
						case "Player Left":
							CWCPlayeLeftPacket left = new CWCPlayeLeftPacket(packet);

							// If this were the local player seat then either someone tried to take the local player's seat or the local player has already aborted this thread so we don't need to worry about that
							if(left.Valid)
								for(int i = 0;i < names.Count;i++)
									if(names[i].Item2 == left.Player)
									{
										names.RemoveAt(i);
										break; // At any one time there is at most one of this player index
									}

							break;
						case "Player Join Success":
							CWCPlayerJoinSuccessPacket success = new CWCPlayerJoinSuccessPacket(packet);

							if(success.Valid)
							{
								if(seat == success.Player && waiting_for_confirm)
									waiting_for_confirm = false; // We joined the game successfully

								// names does not need to be sorted but we do need to make sure we don't have extras (we could get a join packet before a leave packet)
								for(int i = 0;i < names.Count;i++)
									if(names[i].Item2 == success.Player)
									{
										names.RemoveAt(i);
										break; // There is at most one duplicate
									}

								// Clients don't add their name until the successfully join so do so here even if we recieve our own packet
								names.Add(new Tuple<string,int>(success.Name,success.Player)); // names does not need to be sorted
							}

							break;
						case "Player Join Failed":
							CWCPlayerJoinFailedPacket failed = new CWCPlayerJoinFailedPacket(packet);

							if(failed.Valid)
							{
								// Check if the local player failed to join
								if(seat == failed.Player && waiting_for_confirm)
								{
									waiting_for_confirm = false; // We couldn't join the game
									listen = false; // Let the network exit naturally

									if(InvokeRequired)
										Invoke(new DV(Dispose));
									else
										Dispose();
								}

							}

							break;
						case "Start Game":
							listen = false; // Break out of the listen loop (it will be aborted later but its good form to let it end naturally if possible)
							Invoke(new EventHandler(button1_Click),null,null); // Emulate the button click to start the game
								
							break;
						case "Message":
							CWCMessagePacket message = new CWCMessagePacket(packet);

							if(message.Valid)
								PostMessage(message.Text,message.Sender);

							break;
						default:
							if(!UnknownPacket())
								nio.GetString(); // We have no idea what this packet is; our child has no idea what the packet is; no one knows what the packet is; trash it

							break;
						}
				}

				Thread.Sleep(is_host ? 25 : 100); // Clients have much less to do and should wait longer for more packets to show up
			}

			return;
		}

		/// <summary>
		/// Sends the given message from the given person.
		/// </summary>
		/// <param name="msg">The message to send.</param>
		private void SendMessage(string msg)
		{
			nio.QueuePacket(new CWCMessagePacket(msg,LocalPlayer).ToString());
			return;
		}
#endregion

#region Windows Form Functions
		/// <summary>
		/// Called when the button to start the game is clicked.
		/// </summary>
		/// <param name="sender">The originator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void button1_Click(object sender, EventArgs e)
		{
			// To be careful, make sure that this code can never run more than once
			if(clicked)
				return;

			clicked = true;

			try
			{net.Abort();}
			catch
			{}

			net = null;

			if(is_host) // Hosts need to signal that the game is starting while clients can just start the game
				nio.QueuePacket(new CWCGameStart().ToString()); // Send out the game start packet (note that it does not need the network thread to be sent)

			List<string> n = new List<string>(4);

			for(int i = 0;i < 4;i++)
				n.Add(GetAIName(i)); // Only players will change the name values so AI players will already be in names

			for(int i = 0;i < names.Count;i++)
				n[names[i].Item2] = names[i].Item1; // This allows us to not have to sort names

			bool[] humans = new bool[4];

			for(int i = 0;i < 4;i++)
				humans[i] = false;

			for(int i = 0;i < names.Count;i++)
				humans[names[i].Item2] = true;

			Hide();
			CreateTable(top,nio,n,seat,is_host,humans).Show();
			
			unprevoked = false;

			if(InvokeRequired)
				Invoke(new DV(Dispose));
			else
				Dispose();
			
			return;
		}

		/// <summary>
		/// Sends a message.
		/// </summary>
		/// <param name="sender">The source of the sending event.</param>
		/// <param name="e">The event arguments.</param>
		private void textbox1_KeyDown(object sender, KeyEventArgs e)
		{
			if(!e.Alt && !e.Control && !e.Shift && (e.KeyCode == Keys.Return || e.KeyCode == Keys.Enter))
			{
				SendMessage(textBox1.Text);

				if(is_host)
					PostMessage(textBox1.Text,LocalPlayer);
				
				textBox1.Text = "";
				e.SuppressKeyPress = true;
			}

			return;
		}
#endregion

#region Event Handlers
		/// <summary>
		/// Fired when this window is disposed.
		/// </summary>
		/// <param name="sender">The object originating the event.</param>
		/// <param name="e">The event arguments.</param>
		protected void DisposeExtension(object sender, EventArgs e)
		{
			CleanUp(unprevoked);

			if(unprevoked)
			{
				try
				{net.Abort();}
				catch
				{}

				net = null;

				// Only send the leave packet if we had actually joined the game
				// It may be possible to try to join, leave and then have the host send a join confirmation but it would be very hard and very malicious to time that so it's an acceptable bug
				if(!is_host && !waiting_for_confirm)
					nio.QueuePacket(new CWCPlayeLeftPacket(seat).ToString()); // Send leaving packet

				Thread.Sleep(100); // Wait for the packet to be sent

				nio.Close();
				nio = null;

				top.Show();
			}

			return;
		}
#endregion

#region Abstract Functions
		/// <summary>
		/// Called when all other initialization logic has finished to provided custom initialization logic.
		/// </summary>
		protected abstract void Initialize();

		/// <summary>
		/// Sends out the game options information over the network stream.
		/// </summary>
		protected abstract void QueueOptions();

		/// <summary>
		/// Called when a packet is recieved and this class does not know what to do with it.
		/// </summary>
		/// <returns>Returns true if the packet was used and false otherwise.</returns>
		/// <remarks>If the packet was used then it should be removed by this function. If false then it should be left in the stream.</remarks>
		protected abstract bool UnknownPacket();

		/// <summary>
		/// Gets a name for the AI at the given seat.
		/// </summary>
		/// <param name="seat">The seat the AI is at.</param>
		/// <returns>Returns the name of the AI.</returns>
		protected abstract string GetAIName(int seat);

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
		protected abstract Form CreateTable(Form top, NetworkStream nio, IList<string> names, int seat, bool host, bool[] humans);

		/// <summary>
		/// Called when this window has been disposed to handle any additional cleaning logic.
		/// </summary>
		/// <param name="unprevoked">If true then the window was disposed without starting a game.</param>
		/// <remarks>Networking is still available at this point.</remarks>
		protected abstract void CleanUp(bool unprevoked);
#endregion

#region Delegates
		/// <summary>
		/// A delegate that accepts no parameters and returns nothing.
		/// </summary>
		protected delegate void DV();

		/// <summary>
		/// A delegate that takes two strings and returns nothing.
		/// </summary>
		/// <param name="str1">The first string.</param>
		/// <param name="str2">The second string.</param>
		protected delegate void DS(string str1, string str2);
#endregion

#region Properties
		/// <summary>
		/// The index of the player at this table.
		/// </summary>
		public int ThePlayer
		{
			get
			{return seat;}
			private set
			{
				seat = value;
				return;
			}
		}

		/// <summary>
		/// The messages recieved by this table.
		/// </summary>
		/// <remarks>Read only.</remarks>
		public IList<Tuple<string,string>> Messages
		{
			get
			{return messages.AsReadOnly();}
		}

		/// <summary>
		/// The names of the players in this game.
		/// </summary>
		/// <remarks>Read only.</remarks>
		public IList<Tuple<string,int>> Players
		{
			get
			{return names.AsReadOnly();}
		}
#endregion

#region Member Variables
		/// <summary>
		/// The network stream for sending and recieving information.
		/// </summary>
		protected NetworkStream nio
		{get; private set;}

		/// <summary>
		/// If true then this is the host.
		/// </summary>
		protected bool is_host
		{get; private set;}

		/// <summary>
		/// How long the host sleeps in between checking for packets.
		/// </summary>
		protected int HostSleep
		{get; set;}

		/// <summary>
		/// How long the client sleeps in between checking for packets.
		/// </summary>
		protected int ClientSleep
		{get; set;}

		/// <summary>
		/// The networking thread.
		/// </summary>
		private Thread net;

		/// <summary>
		/// The window to return to if a game does not start.
		/// </summary>
		private Form top;

		/// <summary>
		/// The seat of the local player.
		/// </summary>
		private int seat;

		/// <summary>
		/// The local player's name.
		/// </summary>
		protected string LocalPlayer
		{get; private set;}

		/// <summary>
		/// If true then the game has not been started.
		/// </summary>
		private bool unprevoked = true;

		/// <summary>
		/// If true then we are waiting for confirmation that we can join this game.
		/// </summary>
		private bool waiting_for_confirm = false;

		/// <summary>
		/// If true then we have already done game start logic.
		/// </summary>
		private bool clicked = false;

		/// <summary>
		/// The names of every player connected.
		/// </summary>
		private List<Tuple<string,int>> names = new List<Tuple<string,int>>();

		/// <summary>
		/// Messages reieved including their source player.
		/// </summary>
		private List<Tuple<string,string>> messages = new List<Tuple<string,string>>();

		/// <summary>
		/// If true then no messages have been posted yet.
		/// </summary>
		private bool FirstPost
		{
			get
			{return messages.Count == 0;}
		}
#endregion
	}
}
