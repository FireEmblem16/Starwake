#pragma warning disable 0162

using System;
using System.Collections.Generic;
using System.Threading;
using System.Windows.Forms;
using Engine.CWCPackets;
using Engine.CWCPackets.GameStatePackets;
using Engine.CWCPackets.MessagePackets;
using Engine.CWCPackets.MovePackets;
using Engine.CWCPackets.PlayerExchangePackets;
using Engine.Player;
using Engine.Player.AI;
using Networking;

namespace Engine.Game.Screens
{
    /// <summary>
    /// The abstract of a game table.
    /// </summary>
    public abstract partial class GameTable : Form
	{
#region Constructors
		/// <summary>
        /// Constructs the basics of a game table for a client.
        /// </summary>
		/// <param name="return_to">The window to return to when the game is over.</param>
		/// <param name="title">The string to display in the title bar.</param>
		/// <param name="nio">The network stream for sending and recieving data to and from players. This class is not responsible for opening or closing this stream nor managing the connections to the stream.</param>
		/// <param name="players">The names of the players in the game. If AI players are present then names should be provided in their positions.</param>
		/// <param name="player">The index of the player at this table.</param>
		/// <param name="host">If ture then this table is hosting the game.</param>
        protected GameTable(Form return_to, string title, NetworkStream nio, IList<string> players, int player, bool host = false)
        {
			ThePlayer = player;
			is_host = host;
			this.nio = nio;

			HostSleep = 25;
			ClientSleep = 100;
			waiting_for_state = false;

			messages = new List<Tuple<string,string>>();
			this.players = new List<string>(players);

            exit_to = return_to;

            InitializeComponent();
			InitializeWindow();
			Disposed += new EventHandler(DisposeExtension);
            
			Text = title;
			return;
        }
#endregion

#region HelperFunctions
		/// <summary>
		/// Invokes the given function on the same thread this form is running on if required.
		/// </summary>
		/// <param name="func">The function to invoke.</param>
		/// <param name="args">The arguments to the function, if any.</param>
		public object InvokeOnFormThread(Delegate func, params object[] args)
		{
			if(InvokeRequired)
				return Invoke(func,args);
			else
				return func.DynamicInvoke(args);

			return null;
		}

		/// <summary>
		/// Runs the game for a host.
		/// </summary>
		private void HostRunner()
		{
			HostSendGameState(); // Send out the initial game state
			PregameLogic();
			
			while(!TheGame.GameFinished)
			{
				Engine.Player.Player active = TheGame.GetPlayer(TheGame.ActivePlayer);
				Move play;

				if(active.IsAI)
					play = (active as AIPlayer).TakeTurn(TheGame);
				else
					play = HostGetNextMove(); // It is important to note that the move that we get that is valid is not necessarily from the active player (this can cause problems in games with AI but games that require interruption rules should probably not have AI)
				
				if(!TheGame.IsValid(play))
				{
					HostSendMoveConfirmation(play.Player,false); // The player is not necessarily the active player
					continue; // The move was invalid so wait for another one
				}
				else
					HostSendMoveConfirmation(play.Player,true); // The player is not necessarily the active player

				HostNotifyMove(play);

				TheGame.ApplyMove(play); // We deffer the move application until here so that we can handle events properly
				InvalidatePlayers(new int[] {play.Player});
			}
			
			PostgameLogic();
			return;
		}

		/// <summary>
		/// Runs the game for a client.
		/// </summary>
		private void ClientRunner()
		{
			ClientGetGameState(); // Get the initial game state
			PregameLogic();
			
			while(!TheGame.GameFinished)
			{
				if(TheGame.ActivePlayer == ThePlayer)
					while(!ClientNotifyMove(LocalGetNextMove()));

				TheGame.ApplyMove(ClientGetNextMove());
				InvalidatePlayers(new int[] {ThePlayer});
			}
			
			PostgameLogic();
			return;
		}
#endregion

#region Network Communications
		/// <summary>
		/// Gets the next move made by the active human player for the host.
		/// </summary>
		/// <returns>Returns the desired move.</returns>
		private Move HostGetNextMove()
		{
			if(TheGame.ActivePlayer == ThePlayer)
				return LocalGetNextMove();

			CWCMovePacket move;

			while(true)
			{
				string peek = nio.PeekString();

				if(peek != null)
				{
					CWCPacket next = new CWCPacket(peek);

					if(next.PacketID == "Move")
					{
						nio.GetString(); // Pull this packet out of the stream
						move = new CWCMovePacket(next);

						if(move.Valid) // Check if the packet is valid
							break;
					}
				}

				if(!nio.HasPacket)
					Thread.Sleep(HostSleep); // Wait and see if a packet shows up noting that it will probably take a while
			}

			return UnserializeMove(move.Move); // It's okay if this returns null
		}

		/// <summary>
		/// Sends the game state out to everyone.
		/// </summary>
		private void HostSendGameState()
		{
			nio.QueuePacket(new CWCGameStatePacket(TheGame).ToString());
			return;
		}
		
		/// <summary>
		/// Runs on its own thread and handles network information for the host that are not about moves.
		/// </summary>
		private void HostHandleMiscPackets()
		{
			while(true)
			{
				string peek = nio.PeekString();

				if(peek != null)
				{
					CWCPacket packet = new CWCPacket(peek);

					switch(packet.PacketID)
					{
					case "Move":
						Thread.Sleep(HostSleep); // We handle these packets elsewhere so don't do anything to upset the delicate order
						break;
					case "Move Confirmation":
						nio.GetString(); // Trash the string like the trash this packet is (a host should never get them)
						break;
					case "Player Join":
						CWCPlayerJoinedPacket join = new CWCPlayerJoinedPacket(packet);

						if(join.Valid && TheGame.PlayerJoined(join.Player))
						{
							nio.QueuePacket(new CWCPlayerJoinSuccessPacket(join.Player,join.Name).ToString()); // Let the player know they joined successfully
							nio.QueuePacket(new CWCMessagePacket(join.Name + " has joined the game.","").ToString()); // Spread the word
							HostSendGameState(); // A new player has joined and they need the game state (there's also no harm in everyone updating to it)
							
							// Change the players
							players[join.Player] = join.Name;
							nio.QueuePacket(new CWCPlayersPacket(Players).ToString()); // The new player needs to know the names of everyone in the game and there's no hard sending it to everyone else
						}
						else
							nio.QueuePacket(new CWCPlayerJoinFailedPacket(join.Player).ToString()); // Let the player know the failed to join

						break;
					case "Player Leave":
						CWCPlayeLeftPacket left = new CWCPlayeLeftPacket(packet);

						if(left.Valid)
						{
							TheGame.PlayerLeft(left.Player,GetDefaultAI(left.Player)); // Replace them for they are replaceable
							nio.QueuePacket(new CWCMessagePacket(players[left.Player] + " has left the game. An AI also called " + players[left.Player] + " has usurped the position.","").ToString()); // Spread the word
						}
                        
						nio.GetString(); // Trash the packet
						break;
					case "Player Join Failed":
						nio.GetString(); // Trash this junk and search for diamonds instead (this is a packet sent to people that fail to join which the host should never have happen to them)
						break;
					case "Player Join Success":
						nio.GetString(); // We should never get this as the host
						break;
					case "Game State":
						nio.GetString(); // The host never needs to update its game state against some other one
						break;
					case "Player Names":
						nio.GetString(); // We should never get these packets as the host
						break;
					case "Message":
						CWCMessagePacket msg = new CWCMessagePacket(packet);

						if(msg.Valid) // Check that the packet is valid (even if it is and the message is the empty string we don't want empty string messages)
                            InvokeOnFormThread(new DS(PostMessage),msg.Text,msg.Sender);

						nio.GetString(); // Trash the packet
						nio.QueuePacket(peek); // Send the packet back out to everyone
						break;
					default:
						if(!UnknownPacket())
							nio.GetString(); // We have no idea what this packet is; our child has no idea what the packet is; no one knows what the packet is; trash it

						break;
					}
				}

				if(!nio.HasPacket)
					Thread.Sleep(HostSleep); // Wait a bit for more packets
			}

			return;
		}

		/// <summary>
		/// Notifies every player in the game about the given move.
		/// </summary>
		/// <param name="move">The move to send to every player.</param>
		/// <remarks>Host does not send the move to itself.</remarks>
		private void HostNotifyMove(Move move)
		{
			nio.QueuePacket(new CWCMovePacket(move).ToString());
			return;
		}

		/// <summary>
		/// Sends out a confirmation packet that the move by the given player is valid or not.
		/// </summary>
		/// <param name="player">The player that sent the move.</param>
		/// <param name="success">True if the move was valid and false otherwise.</param>
		private void HostSendMoveConfirmation(int player, bool success = true)
		{
			nio.QueuePacket(new CWCMoveConfirmationPacket(player,success).ToString());
			return;
		}

		/// <summary>
		/// Gets the next move made by the active human player for the client.
		/// </summary>
		/// <returns>Returns the desired move.</returns>
		private Move ClientGetNextMove()
		{
			CWCMovePacket move;

			while(true)
			{
				string peek = nio.PeekString();

				if(peek != null)
				{
					CWCPacket next = new CWCPacket(peek);

					if(next.PacketID == "Move")
					{
						nio.GetString(); // Pull this packet out of the stream
						move = new CWCMovePacket(next);

						if(move.Valid) // Check if the packet is valid
							break;
					}
					else if(next.PacketID == "Move Confirmation")
						nio.GetString(); // This is not handled in the network thread and needs to be trashed because we are hogging the thread that usually handles it
				}

				if(!nio.HasPacket)
					Thread.Sleep(ClientSleep); // Wait and see if a packet shows up noting that it will probably take a while (client can afford to wait a lot longer than the host)
			}

			return UnserializeMove(move.Move); // It's okay if this returns null
		}

		/// <summary>
		/// Gets a game state from the network communications.
		/// </summary>
		private void ClientGetGameState()
		{
			waiting_for_state = true;

			// Wait until we get a game state packet
			while(waiting_for_state)
				Thread.Sleep(ClientSleep);

			return;
		}

		/// <summary>
		/// Runs on its own thread and handles network information for the client that are not about moves.
		/// </summary>
		private void ClientHandleMiscPackets()
		{
			while(true)
			{
				string peek = nio.PeekString();

				if(peek != null)
				{
					CWCPacket packet = new CWCPacket(peek);

					switch(packet.PacketID)
					{
					case "Move":
						Thread.Sleep(ClientSleep); // We handle these packets elsewhere so don't do anything to upset the delicate order
						break;
					case "Move Confirmation":
						Thread.Sleep(ClientSleep); // We handle these packets elsewhere so don't do anything to upset the delicate order
						break;
					case "Player Join":
						nio.GetString(); // We don't care so just trash the packet
						break;
					case "Player Leave":
						nio.GetString(); // We don't care so just trash the packet
						break;
					case "Player Join Failed":
						CWCPlayerJoinFailedPacket fail = new CWCPlayerJoinFailedPacket(packet);

						if(fail.Valid && fail.Player == ThePlayer) // We only care if the local player fails to join the game
							JoinFailed();

						nio.GetString(); // Trash the packet
						break;
					case "Player Join Success":
						CWCPlayerJoinSuccessPacket join = new CWCPlayerJoinSuccessPacket(packet);

						if(join.Valid)
						{
							players[join.Player] = join.Name; // Update the player names

							if(join.Player == ThePlayer) // We only care if the local player manages to join the game
								JoinSucceeded();
						}

						nio.GetString(); // Trash the packet
						break;
					case "Game State":
						CWCGameStatePacket gamestate = new CWCGameStatePacket(packet);

						if(gamestate.Valid)
						{
							TheGame = UnserializeGameState(gamestate.State); // We are the client so there's no possible harm in updating to the host's game state
							waiting_for_state = false; // If we were or if we weren't we still can set this

							Invalidate(); // Everything is out of date so update it all
						}

						nio.GetString(); // Trash the packet
						break;
					case "Player Names":
						CWCPlayersPacket names = new CWCPlayersPacket(packet);

						if(names.Valid)
							players = new List<string>(names.PlayerNames); // We are the client so there's no possible harm in agreeing with something the host sends out

						nio.GetString(); // Trash the packet
						break;
					case "Message":
						CWCMessagePacket msg = new CWCMessagePacket(packet);

						if(msg.Valid) // Check that the packet is valid (even if it is and the message is the empty string we don't want empty string messages)
							InvokeOnFormThread(new DS(PostMessage),msg.Text,msg.Sender);

						nio.GetString(); // Trash the packet
						break;
					default:
						if(!UnknownPacket())
							nio.GetString(); // We have no idea what this packet is; our child has no idea what the packet is; no one knows what the packet is; trash it

						break;
					}
				}

				if(!nio.HasPacket)
					Thread.Sleep(HostSleep); // Wait a bit for more packets
			}

			return;
		}

		/// <summary>
		/// Notifies the host about the given move the player wishes to make.
		/// </summary>
		/// <param name="move">The move to make.</param>
		/// <returns>Returns true if the host accepts it as a valid move.</returns>
		private bool ClientNotifyMove(Move move)
		{
			nio.QueuePacket(new CWCMovePacket(move).ToString());

			CWCMoveConfirmationPacket confirm;

			while(true)
			{
				string peek = nio.PeekString();

				if(peek != null)
				{
					CWCPacket next = new CWCPacket(peek);

					if(next.PacketID == "Move Confirmation")
					{
						nio.GetString(); // Trash the front of the queue
						confirm = new CWCMoveConfirmationPacket(next);

						if(confirm.Valid && confirm.Player == ThePlayer) // Check if the packet is valid and for this player
							break;
					}
					else if(next.PacketID == "Move")
						nio.GetString(); // This is not handled in the network thread and needs to be trashed because we are hogging the thread that usually handles it
				}

				// The network thread will dispose of other packets
				if(!nio.HasPacket)
					Thread.Sleep(ClientSleep); // Wait and see if a packet shows up noting that it will probably take a while (client can afford to wait a lot longer than the host)
			}

			return confirm.Success;
		}
#endregion

#region Windows Form Functions
		/// <summary>
		/// Loads the table into memory.
		/// </summary>
		/// <param name="sender">The event originator.</param>
		/// <param name="e">The event.</param>
		private void GameTable_Load(object sender, EventArgs e)
		{
			// Initialize the game so we can do our own initialization code
			InitializeGame();

			TheGame.StateChanged += GameStateChanged;
			TheGame.Finished += GameFinished;
			
			// Make sure we don't seg fault on something stupid
			for(int i = players.Count;i < TheGame.NumberOfPlayers;i++)
				players.Add("Player " + i);

			nio.Flush(); // Get rid of any junk left behind

			// We would handle this in the constructor but it is too likely that other windows are still using nio so we can't use up packets before now
			net_thread = is_host ? new Thread(new ThreadStart(HostHandleMiscPackets)) : new Thread(new ThreadStart(ClientHandleMiscPackets));
			net_thread.Name = "Network Communications Thread";
			net_thread.Start();

			game_thread = is_host ? new Thread(new ThreadStart(HostRunner)) : new Thread(new ThreadStart(ClientRunner));
			game_thread.Name = "Game Thread";
			game_thread.Start();

			return;
		}

		/// <summary>
		/// Called when the table is being drawn.
		/// </summary>
		/// <param name="e">The paint event.</param>
		protected sealed override void OnPaint(PaintEventArgs e)
		{
			base.OnPaint(e);
			Draw(e.Graphics);
			
			return;
		}
#endregion

#region Event Handlers
		/// <summary>
		/// An event fired when the state of the game has changed.
		/// </summary>
		/// <param name="game">The new state of the game.</param>
		/// <param name="m">The last move or null if the state change was not move related.</param>
		/// <param name="undo">If true then the move provided has been undone.</param>
		private void GameStateChanged(GameState game, Move m, bool undo = false)
		{
			InvalidatePlayers(new int[] {m.Player});
			return;
		}

		/// <summary>
		/// An event fired when the game reaches an ending state.
		/// </summary>
		/// <param name="game">The state of the game at the end of the game.</param>
		private void GameFinished(GameState game)
		{
			ExitLogic();
			InvokeOnFormThread(new Terminate(Dispose));

			return;
		}

		/// <summary>
		/// Called when the table is disposed.
		/// </summary>
		/// <param name="sender">The source of the event.</param>
		/// <param name="e">The event.</param>
		private void DisposeExtension(object sender, EventArgs e)
		{
			try
			{game_thread.Abort();}
			catch
			{} // Just in case

			try
			{net_thread.Abort();}
			catch
			{} // Just in case

			game_thread = null;
			net_thread = null;

			nio.Close(); // Close the network
			nio = null; // We don't want the reference handing around but we don't want to close the stream either

			CleanUp();
			exit_to.Show();

			return;
		}
#endregion

#region Abstract Functions
		/// <summary>
		/// Initializes the table window including custom features such as size, border, resizeability, etc...
		/// </summary>
		protected abstract void InitializeWindow();

		/// <summary>
		/// Called when the table is loaded to initialize the game state.
		/// </summary>
		protected abstract void InitializeGame();

		/// <summary>
		/// Called when the game thread is running but just before the first move is taken in the game state.
		/// </summary>
		protected abstract void PregameLogic();

		/// <summary>
		/// Gets the next move of the local player at this table.
		/// </summary>
		/// <returns>Returns the move that the local player desires.</returns>
		protected abstract Move LocalGetNextMove();

		/// <summary>
		/// Invalidates everything that is drawn to represent the given players.
		/// </summary>
		/// <param name="player">The players to invalidate.</param>
		protected abstract void InvalidatePlayers(int[] players);

		/// <summary>
		/// Draws the table.
		/// </summary>
		/// <remarks>Note that it is not often the case that the entire table needs to be redrawn although there is no harm in doing so every time so long as doing so does not slow down drawing.</remarks>
		protected abstract void Draw(System.Drawing.Graphics g);
		
        /// <summary>
        /// Posts the provided message.
        /// </summary>
        /// <param name="msg">The message.</param>
        /// <param name="sender">The name of the player who sent the message.</param>
        protected abstract void PostMessage(string msg, string sender);

		/// <summary>
		/// Call when the game thread is running just after the game state has entered a final state.
		/// </summary>
		protected abstract void PostgameLogic();

		/// <summary>
		/// Called when the game finishes just before the table is disposed.
		/// </summary>
		protected abstract void ExitLogic();

		/// <summary>
		/// Called when the table is disposed to handle any additional cleanup logic.
		/// </summary>
		protected abstract void CleanUp();

		/// <summary>
		/// Called to unserialize a serialized move.
		/// </summary>
		/// <param name="str">The move in serial form.</param>
		/// <remarks>Returns the unserialized move or null if the move was invalid.</remarks>
		protected abstract Move UnserializeMove(string str);

		/// <summary>
		/// Called to unserialize a game state.
		/// </summary>
		/// <param name="str">The game state in serial form.</param>
		/// <returns>Returns the unserialized game state or the current game state if it was invalid.</returns>
		/// <remarks>Must copy every event in the old game state into the new one.</remarks>
		protected abstract GameState UnserializeGameState(string str);

		/// <summary>
		/// Called when the game is successfully joined in progress.
		/// </summary>
		protected abstract void JoinSucceeded();

		/// <summary>
		/// Called when the game can not be joined for one reason or another (usually because an attempt was made to replace a human).
		/// </summary>
		protected abstract void JoinFailed();

		/// <summary>
		/// Called when a packet is recieved and this class does not know what to do with it.
		/// </summary>
		/// <returns>Returns true if the packet was used and false otherwise.</returns>
		/// <remarks>If the packet was used then it should be removed by this function. If false then it should be left in the stream.</remarks>
		protected abstract bool UnknownPacket();

		/// <summary>
		/// Called when the game needs a copy of the default AI. This occurs principly when a player leaves the game.
		/// </summary>
		/// <param name="player">In case AI defaults are different for each player, the player that is leaving is given.</param>
		protected abstract AIBehavior GetDefaultAI(int player);
#endregion

#region Delegates
		/// <summary>
		/// A wrapper for functions that take no arguments and return void.
		/// </summary>
		protected delegate void DV();

		/// <summary>
		/// A delegate that takes two strings and returns nothing.
		/// </summary>
		/// <param name="str1">The first string.</param>
		/// <param name="str2">The second string.</param>
		protected delegate void DS(string str1, string str2);

		/// <summary>
        /// A wrapper for Dispose for the GameFinished event.
        /// </summary>
        protected delegate void Terminate();
#endregion

#region Properties
		/// <summary>
        /// The game we are playing.
        /// </summary>
        public GameState TheGame
        {get; protected set;}

		/// <summary>
		/// The index of the player at this table.
		/// </summary>
		public int ThePlayer
		{get; protected set;}

        /// <summary>
        /// The name of the local player at this table.
        /// </summary>
        public string ThePlayerName
        {
            get
            {return players[ThePlayer];}
        }

		/// <summary>
		/// If true then to select a card either two clicks or a double click are required.
		/// If false then a single click (or double click) will select a card.
		/// </summary>
		public bool DoubleClickToSelect
		{get; set;}

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
		public IList<string> Players
		{
			get
			{return players.AsReadOnly();}
		}
#endregion

#region Member Variables
		/// <summary>
		/// Network communications can be made with this.
		/// </summary>
		protected NetworkStream nio
		{get; private set;}

		/// <summary>
		/// If true then this is table is the host.
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
		/// The thread running the game logic.
		/// </summary>
		private Thread game_thread;

		/// <summary>
		/// The thread running extra network processing.
		/// </summary>
		private Thread net_thread;

		/// <summary>
        /// The form we should return to when we exit the window.
        /// </summary>
        private Form exit_to;

		/// <summary>
		/// Contains text messages recieved by this table.
		/// </summary>
		protected volatile List<Tuple<string,string>> messages;

		/// <summary>
		/// The names of the players in the game.
		/// </summary>
		private List<string> players;

		/// <summary>
		/// If true then we are waiting to recieve the initial game state.
		/// </summary>
		private bool waiting_for_state;
#endregion
	}
}
