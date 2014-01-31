using System;
using System.Collections.Generic;
using System.Threading;
using System.Windows.Forms;
using Engine.CWCPackets;
using Engine.CWCPackets.EventPackets;
using Engine.CWCPackets.MessagePackets;
using Engine.CWCPackets.PlayerExchangePackets;
using Engine.Game.Screens;
using ExampleNetworkedGame.Properties;
using Networking;

namespace ExampleNetworkedGame
{
	public partial class HeartsWaitingScreen : Form
	{
		public HeartsWaitingScreen(Form top, NetworkStream nio, int player, bool is_host = false, bool hundred_reset = false)
		{
			InitializeComponent();
			Disposed += new EventHandler(DisposeExtension);

			this.nio = nio;
			this.top = top;
			
			this.is_host = is_host;
			hreset = hundred_reset;
			seat = player;
			
			button1.Visible = is_host;

			if(is_host)
				names.Add(new Tuple<string,int>(local_name,player)); // names does not need to be sorted
			else
			{
				nio.QueuePacket(new CWCPlayerJoinedPacket(player,local_name).ToString()); // Send joining packet (we will handle problems [like being denied to join] in the handle packets function)
				waiting_for_confirm = true;
			}

			net = new Thread(new ThreadStart(HandlePackets));
			net.Name = "Networking Thread";
			net.Start();

			return;
		}

		protected void HandlePackets()
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
						if(is_host)
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
									nio.QueuePacket(new HeartsOptionsPacket(hreset).ToString()); // The player needs to know about the game options

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
							case "Hearts Options":
								break; // Hosts should never get these
							case "Message":
								CWCMessagePacket message = new CWCMessagePacket(packet);

								if(message.Valid)
								{
									messages.Add(new Tuple<string,string>(message.Sender,message.Text));
									nio.QueuePacket(str); // Send the message out to everyone else
								}

								break;
							}
						else
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
							case "Hearts Options":
								HeartsOptionsPacket options = new HeartsOptionsPacket(packet);

								if(options.Valid)
									hreset = options.HundredReset;

								break;
							case "Message":
								CWCMessagePacket message = new CWCMessagePacket(packet);

								if(message.Valid)
									messages.Add(new Tuple<string,string>(message.Sender,message.Text));

								break;
							}
				}

				Thread.Sleep(is_host ? 25 : 100); // Clients have much less to do and should wait longer for more packets to show up
				break;
			}

			return;
		}

		protected void DisposeExtension(object sender, EventArgs e)
		{
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

				nio.Close();
				nio = null;

				top.Show();
			}

			return;
		}

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
				n.Add("AI" + i); // Only players will change the name values so AI players will already be in names

			for(int i = 0;i < names.Count;i++)
				n[names[i].Item2] = names[i].Item1; // This allows us to not have to sort names

			bool[] humans = new bool[4];

			for(int i = 0;i < 4;i++)
				humans[i] = false;

			for(int i = 0;i < names.Count;i++)
				humans[names[i].Item2] = true;

			new HeartsTable(top,Resources.ResourceManager,nio,n,seat,is_host,humans,hreset).Show();

			unprevoked = false;

			if(InvokeRequired)
				Invoke(new DV(Dispose));
			else
				Dispose();
			
			return;
		}

		protected delegate void DV();

		protected Thread net;
		protected NetworkStream nio;
		protected Form top;

		protected bool is_host;
		protected int seat;
		protected bool hreset;

		protected string local_name = (DateTime.Now.Millisecond * DateTime.Now.Second).ToString();
		protected List<Tuple<string,int>> names = new List<Tuple<string,int>>(4);

		protected bool unprevoked = true;
		protected bool waiting_for_confirm = false;
		protected bool clicked = false;

		protected List<Tuple<string,string>> messages = new List<Tuple<string,string>>();
	}
}
