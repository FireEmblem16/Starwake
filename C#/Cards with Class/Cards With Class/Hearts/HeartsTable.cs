#pragma warning disable 0162

using System;
using System.Collections.Generic;
using System.Drawing;
using System.Resources;
using System.Threading;
using System.Windows.Forms;
using Engine.Cards;
using Engine.Cards.CardTypes;
using Engine.Cards.CardTypes.Suits;
using Engine.Cards.CardTypes.Values;
using Engine.Cards.DeckTypes;
using Engine.CWCPackets;
using Engine.CWCPackets.MessagePackets;
using Engine.CWCPackets.MovePackets;
using Engine.Game;
using Engine.Game.Screens;
using Engine.Player;
using Engine.Player.AI;
using HeartsGame.Code;
using Networking;

namespace HeartsGame
{
	/// <summary>
	/// A hearts table.
	/// </summary>
	public class HeartsTable : GameTable
	{
#region Constructors
		/// <summary>
		/// Creats a new hearts table.
		/// </summary>
		/// <param name="ret">The window to return to once the game is over.</param>
		/// <param name="resources">Provides resources for this table. Requires an image (72x96 recommended [96x72 for horizontal card back]) for every card in the standard deck with image names the same as the standard card names (except with spaces replaced by underscores). The vertical and horizontal card backs should be named Vertical_Back and Horizontal_Back.</param>
		/// <param name="nio">The network stream to communicate with. Should already be connected to everyone currently in the game (undefined behavior if also connected to the local computer).</param>
		/// <param name="names">The names of the players in the game. Should be ordered. If AI are present then they should also be in the array ordered.</param>
		/// <param name="player">The index of this player in the game.</param>
		/// <param name="host">If this is the host player or not.</param>
		/// <param name="human">An array indicating if the player at the corresponding index is human or an AI. There should be an element for every player.</param>
		/// <param name="hundred_rule">If true then the hundred reset rule will be in effect.</param>
		public HeartsTable(Form ret, ResourceManager resources, NetworkStream nio, IList<string> names, int player, bool host, bool[] human, bool hundred_rule = false) : base(ret,"Hearts",nio,names,player,host)
        {
			passing_info = new List<Tuple<Card,int>>(4 * 3);
			humans = new bool[4];

			for(int i = 0;i < 4;i++)
				humans[i] = human[i];

			hundred_reset = hundred_rule;

			DoubleClickToSelect = true;
			Joining = false;

			this.resources = resources;
			return;
		}
#endregion

#region GameTable Functions
		/// <summary>
		/// Initializes the table window including custom features such as size, border, resizeability, etc...
		/// </summary>
		protected override void InitializeWindow()
		{
			// Although these are the defaults (and some are redundant) we will set them to show how to do this
			MaximumSize = new Size(800,1000);
			MinimumSize = new Size(800,1000);
			Size = new Size(800,1000);
			
            // Set up chat GUI
            textBox1 = new TextBox();
            textBox1.Location = new System.Drawing.Point(100,945);
            textBox1.Name = "textBox1";
            textBox1.Size = new System.Drawing.Size(685,20);
            
            Controls.Add(textBox1);
            
            richTextBox1 = new RichTextBox();
            richTextBox1.Location = new System.Drawing.Point(100,800);
            richTextBox1.Name = "richTextBox1";
            richTextBox1.ReadOnly = true;
            richTextBox1.Size = new System.Drawing.Size(685,140);
            richTextBox1.Text = "";
            richTextBox1.ReadOnly = true;
			richTextBox1.BackColor = Color.White;
            textBox1.KeyDown += new KeyEventHandler(textbox1_KeyDown);

            Controls.Add(richTextBox1);

            // Set up kicking functionality for the host
            if(is_host)
            {
                button1 = new Button();
                button1.Location = new System.Drawing.Point(10,940);
                button1.Name = "button1";
                button1.Size = new System.Drawing.Size(75,25);
                button1.TabIndex = 0;
                button1.Text = "Kick";
                button1.UseVisualStyleBackColor = true;
                button1.Click += new System.EventHandler(KickPlayer);

                Controls.Add(button1);

                comboBox1 = new ComboBox();
                comboBox1.FormattingEnabled = true;
			    comboBox1.Location = new System.Drawing.Point(10,915);
			    comboBox1.Name = "comboBox1";
			    comboBox1.Size = new System.Drawing.Size(75,25);
			    comboBox1.Sorted = false;
			    comboBox1.TabIndex = 0;

                foreach(string str in Players)
                    comboBox1.Items.Add(str);

                Controls.Add(comboBox1);
            }

			FormBorderStyle = FormBorderStyle.FixedSingle;
			ShowInTaskbar = true;
			MaximizeBox = false;
			MinimizeBox = true;
			
			BackColor = Color.Green;

			// Set the card size and scale
			CardSize = new Tuple<float,float>(72.0f,96.0f); // Set the base size of the cards
			CardScale = new Tuple<float,float>(1.33f,1.33f); // Scale the cards to the window size

			return;
		}

		/// <summary>
		/// Called when the table is loaded to initialize the game state.
		/// </summary>
		protected override void InitializeGame()
		{
			HeartsGameState gtemp = new HeartsGameState(humans[0],humans[1],humans[2],humans[3],hundred_reset);
			TheGame = gtemp;

			gtemp.StartTrick += StartTrick;
			gtemp.EndTrick += EndTrick;
			gtemp.StartRound += StartRound;
			gtemp.Passing += Pass;
			gtemp.EndRound += EndRound;
			gtemp.Finished += Finish;

			// Change default AI on the very off chance it matters
			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
				if(TheGame.GetPlayer(i).IsAI)
					(TheGame.GetPlayer(i) as AIPlayer).AI = GetDefaultAI(i);

			// Set up the information for drawing the local player's hand
			points_taken = new bool[TheGame.NumberOfPlayers][];

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
			{
				points_taken[i] = new bool[14];

				for(int j = 0;j < 14;j++)
					points_taken[i][j] = false;
			}

			PlayerHand = new PictureBox[13];

			for(int i = 0;i < 13;i++)
			{
				PlayerHand[i] = new PictureBox();

				PlayerHand[i].Location = new System.Drawing.Point(0,0);
				PlayerHand[i].Name = "pictureBox" + i;
				PlayerHand[i].Size = new System.Drawing.Size(72,96);
				PlayerHand[i].TabIndex = 0;
				PlayerHand[i].TabStop = false;
				PlayerHand[i].Image = resources.GetObject(TheGame.GetPlayer(ThePlayer).CardsInHand.Cards[i].ToString().Replace(' ','_')) as Image;
				PlayerHand[i].Visible = true;
				PlayerHand[i].BorderStyle = BorderStyle.None;
				PlayerHand[i].SizeMode = PictureBoxSizeMode.StretchImage;

				int temp_i = i;
				PlayerHand[i].Click += new EventHandler((s,evt) => SelectCard(temp_i));
				PlayerHand[i].DoubleClick += new EventHandler((s,evt) => SelectCardD(temp_i));
			}

			for(int i = 12;i >= 0;i--)
				Controls.Add(PlayerHand[i]);

            // Keep the chat GUI in front of the player's hand
            textBox1.BringToFront();
            richTextBox1.BringToFront();

            if(is_host)
            {
                button1.BringToFront();
                comboBox1.BringToFront();
            }

			return;
		}

		/// <summary>
		/// Called when the game thread is running but just before the first move is taken in the game state.
		/// </summary>
		protected override void PregameLogic()
		{
			Pass(TheGame); // The first pass event is not triggered in the game state so we have to do it here
			RedetermineLead(); // The two of clubs may have moved so redetermine the lead

			return;
		}
		
		/// <summary>
		/// Gets the next move of the local player at this table.
		/// </summary>
		/// <returns>Returns the move that the local player desires.</returns>
		protected override Move LocalGetNextMove()
		{
			while(!card_selected)
				Thread.Sleep(is_host ? HostSleep : ClientSleep);

			Move ret;
			
			try
			{ret = new HeartsMove(TheGame.GetPlayer(ThePlayer).CardsInHand.Cards[selected_card],TheGame.ActivePlayer);} // Apparently you can shinanigan an exception here
			catch
			{
				card_selected = false;
				selected_card = -1;

				Invoke(new SetBorders(SetHandBorders),BorderStyle.None);

				return LocalGetNextMove();
			}

			card_selected = false;
			selected_card = -1;

			Invoke(new SetBorders(SetHandBorders),BorderStyle.None);
			return ret;
		}

		/// <summary>
		/// Invalidates everything that is drawn to represent the given players.
		/// </summary>
		/// <param name="player">The players to invalidate.</param>
		protected override void InvalidatePlayers(int[] players)
		{
			if(players == null || players.Length == 0)
				return;

			// Get the first region
			Region inval = GetPlayerRegion(players[0]);

			// Union with all the other players to invalidate
			for(int i = 1;i < players.Length;i++)
				inval.Union(GetPlayerRegion(players[i]));

			InvokeOnFormThread(new DV(Invalidate)); // Regions are wrong???
			//InvokeOnFormThread(new InvalidateCall(Invalidate),inval); // Invalidate all the player regions (update is invoked in the base class)
			InvokeOnFormThread(new DV(Update)); // Force a syncronous update

			return;
		}

		/// <summary>
		/// Draws the table.
		/// </summary>
		/// <remarks>Note that it is not often the case that the entire table needs to be redrawn although there is no harm in doing so every time so long as doing so does not slow down drawing.</remarks>
		protected override void Draw(System.Drawing.Graphics g)
		{
			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
				DrawPlayer(g,i);

			return;
		}

        /// <summary>
        /// Posts the provided message.
        /// </summary>
        /// <param name="msg">The message.</param>
        /// <param name="sender">The name of the player who sent the message.</param>
        protected override void PostMessage(string msg, string sender)
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
		
		/// <summary>
		/// Call when the game thread is running just after the game state has entered a final state.
		/// </summary>
		protected override void PostgameLogic()
		{return;}

		/// <summary>
		/// Called when the game finishes just before the table is disposed.
		/// </summary>
		protected override void ExitLogic()
		{return;}

		/// <summary>
		/// Called when the table is disposed to handle any additional cleanup logic.
		/// </summary>
		protected override void CleanUp()
		{return;}

		/// <summary>
		/// Called to unserialize a serialized move.
		/// </summary>
		/// <param name="str">The move in serial form.</param>
		/// <remarks>Returns the unserialized move or null if the move was invalid.</remarks>
		protected override Move UnserializeMove(string str)
		{return HeartsMove.Unserialize(str);}

		/// <summary>
		/// Called to unserialize a game state.
		/// </summary>
		/// <param name="str">The game state in serial form.</param>
		/// <returns>Returns the unserialized game state or the current game state if it was invalid.</returns>
		/// <remarks>Must copy every event in the old game state into the new one.</remarks>
		protected override GameState UnserializeGameState(string str)
		{return HeartsGameState.Unserialize(TheGame as HeartsGameState,str);}

		/// <summary>
		/// Called when the game is successfully joined in progress.
		/// </summary>
		protected override void JoinSucceeded()
		{
			Joining = false;
			return; // We will get the game state at some point and getting that will force the screen to redraw
		}

		/// <summary>
		/// Called when the game can not be joined for one reason or another (usually because an attempt was made to replace a human).
		/// </summary>
		protected override void JoinFailed()
		{
			if(Joining)
				InvokeOnFormThread(new Terminate(Dispose));

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
				case "Passing Move":
					if(!is_host)
						break; // This is a packet only the host is concerned with

					HeartsPassingPacket passed = new HeartsPassingPacket(packet);

					// Add the new data to our passing info
					if(passed.Valid) // We would check to see that we're waiting for passing packets but it could be possible to get one before we enter that state so we should just accept passwords anytime
					{
						List<Card> plays = new List<Card>(3);

						foreach(string card in passed.Cards)
							foreach(Card c in TheGame.GetPlayer(passed.Player).CardsInHand.Cards)
								if(c.ToString() == card)
								{
									plays.Add(c);
									break;
								}

						// We only accept completely valid packets
						if(plays.Count == 3)
						{
							nio.QueuePacket(new CWCMoveConfirmationPacket(passed.Player,true).ToString());

							foreach(Card c in plays)
								passing_info.Add(new Tuple<Card,int>(c,passed.Player));
						}
						else
							nio.QueuePacket(new CWCMoveConfirmationPacket(passed.Player,false).ToString());
					}

					// In case we've gotten packet spam, keep only the latest passing information from each player
					// Note that it is possible but very unlikely that data gets interleaved with data from this local player (very unlikely) so we will keep the first three we find and trash everything else we find
					for(int i = passing_info.Count - 1,count = 0;i >= 0;i--)
						if(passing_info[i].Item2 == passed.Player && ++count > 3) // If we find data with the same player as the one we just added count it and if we have 3 or more trash it
							passing_info.RemoveAt(i); // We are going down in index so we don't need to modify i to continue searching properly

					// It could be possible to get all the information we need but before we need it (maybe?) so we have this tertiary variable
					if(waiting_for_passes != -1 && passing_info.Count == TheGame.NumberOfPlayers * 3)
						waiting_for_passes--; // We have all the information so signal that we have it

					nio.GetString(); // We are done with the packet
					return true;
					break;
				case "Passing List":
					if(is_host)
						break; // This host should never get these but if it does then trash it instantly

					HeartsPassingListPacket pass_list = new HeartsPassingListPacket(packet);

					if(pass_list.Valid && pass_list.Moves.Count == 12) // Make sure that this is a full packet
					{
						StandardDeck deck = new StandardDeck(); // We can't get to the deck in the hearts game state so we'll just make another
						passing_info.Clear(); // Just in case (should always do nothing)

						// Turn our strings into cards
						foreach(Tuple<string,int> t in pass_list.Moves)
							foreach(Card c in deck.Deck)
								if(c.ToString() == t.Item1)
								{
									passing_info.Add(new Tuple<Card,int>(c,t.Item2));
									break;
								}

						// It could be possible to get the information we need but before we need it (maybe?) so we have this tertiary variable
						if(waiting_for_passes != -1)
							if(passing_info.Count == TheGame.NumberOfPlayers * 3)
								waiting_for_passes--; // We have all the information so signal that we have it
							else
								passing_info.Clear(); // This was junk so trash what we got from it
					}

					nio.GetString(); // We are done with the packet
					return true;
					break;
				case "Hands List":
					if(is_host)
						break;

					if(waiting_for_hands == 0)
						return true; // We want to keep this packet around

					HeartsHandsPacket hands = new HeartsHandsPacket(packet);

					if(hands.Valid)
					{
						StandardDeck deck = new StandardDeck(); // We can't get to the deck in the hearts game state so we'll just make another

						// Empty player hands
						while(TheGame.GetPlayer(hands.Player).CardsInHand.CardsInHand > 0)
							TheGame.GetPlayer(hands.Player).CardsInHand.PlayCard(0);

						// Turn our strings into cards
						foreach(string t in hands.Cards)
							foreach(Card c in deck.Deck)
								if(c.ToString() == t)
								{
									TheGame.GetPlayer(hands.Player).CardsInHand.DrawCard(c);
									break;
								}

						waiting_for_hands--;
					}

					nio.GetString(); // We are done with the packet
					return true;
					break;
				}
			}

			return false;
		}

		/// <summary>
		/// Called when the game needs a copy of the default AI. This occurs principly when a player leaves the game.
		/// </summary>
		/// <param name="player">In case AI defaults are different for each player, the player that is leaving is given.</param>
		protected override AIBehavior GetDefaultAI(int player)
		{return new HeartsABP();}
#endregion

#region Drawing Functions
		/// <summary>
		/// Draws everything about the given player.
		/// This includes the player's hand, the
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="player">The player to draw.</param>
		protected void DrawPlayer(System.Drawing.Graphics g, int player)
		{
			// Normalize the player so that the local player is at index 2 (south)
			int normalized = (player - ThePlayer + 2) % TheGame.NumberOfPlayers;

			while(normalized < 0)
				normalized += TheGame.NumberOfPlayers;

			// Get the trick so far if we need it
			IList<Card> trick = null;
			int trick_index = int.MaxValue; // This works well as a bad value since we only check if it's too big

			if(!skip_trick)
			{
				trick = (TheGame as HeartsGameState).TrickSoFar();
				trick_index = (TheGame.NumberOfPlayers - (TheGame as HeartsGameState).Leader + player) % TheGame.NumberOfPlayers; // Always non-negative
			}

			// Draw the player's hand
			switch(normalized)
			{
			case 0: // North
				DrawNorthHand(g,(int)TheGame.GetPlayer(player).CardsInHand.CardsInHand);
				DrawNorthPoints(g,player);

				// If the player has a card in the trick draw it
				if(!skip_trick && trick_index < trick.Count)
					DrawNorthPlay(g,trick[trick_index]);

				break;
			case 1: // East
				DrawEastHand(g,(int)TheGame.GetPlayer(player).CardsInHand.CardsInHand);
				DrawEastPoints(g,player);

				// If the player has a card in the trick draw it
				if(!skip_trick && trick_index < trick.Count)
					DrawEastPlay(g,trick[trick_index]);
				
				break;
			case 2: // South (local player)
				UpdateLocalPlayer();
				DrawSouthPoints(g,player);

				// If the player has a card in the trick draw it
				if(!skip_trick && trick_index < trick.Count)
					DrawSouthPlay(g,trick[trick_index]);

				break;
			case 3: // West
				DrawWestHand(g,(int)TheGame.GetPlayer(player).CardsInHand.CardsInHand);
				DrawWestPoints(g,player);

				// If the player has a card in the trick draw it
				if(!skip_trick && trick_index < trick.Count)
					DrawWestPlay(g,trick[trick_index]);

				break;
			}

			return;
		}

		/// <summary>
		/// Draws the northern hand with the given number of cards.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="count">The number of cards to draw.</param>
		protected void DrawNorthHand(System.Drawing.Graphics g, int count)
		{
			for(int i = 0;i < count;i++)
				g.DrawImage(resources.GetObject("Vertical_Back") as Image,(float)(FakeDesktopBounds.Width >> 1) + (12 * i - count * 6) * CardScale.Item1 - CardSize.Item1 / 2.0f,12.0f * CardScale.Item2,CardSize.Item1,CardSize.Item2); // The *6 is a *12 / 2

			return;
		}

		/// <summary>
		/// Draws the eastern hand with the given number of cards.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="count">The number of cards to draw.</param>
		protected void DrawEastHand(System.Drawing.Graphics g, int count)
		{
			for(int i = 0;i < count;i++)
				g.DrawImage(resources.GetObject("Horizontal_Back") as Image,FakeDesktopBounds.Width - 6.0f - 12.0f * CardScale.Item2 - CardSize.Item2,(float)(FakeDesktopBounds.Height >> 1) + (12 * i - count * 6) * CardScale.Item1 - CardSize.Item1 / 2.0f,CardSize.Item2,CardSize.Item1); // The *6 is a *12 / 2

			return;
		}

		/// <summary>
		/// Draws the western hand with the given number of cards.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="count">The number of cards to draw.</param>
		protected void DrawWestHand(System.Drawing.Graphics g, int count)
		{
			for(int i = 0;i < count;i++)
				g.DrawImage(resources.GetObject("Horizontal_Back") as Image,12.0f * CardScale.Item2,(float)(FakeDesktopBounds.Height >> 1) + (12 * i - count * 6) * CardScale.Item1 - CardSize.Item1 / 2.0f,CardSize.Item2,CardSize.Item1); // The *6 is a *12 / 2

			return;
		}

		/// <summary>
		/// Updates the local player's hand for drawing.
		/// </summary>
		protected void UpdateLocalPlayer()
		{
			Player player = TheGame.GetPlayer(ThePlayer);
			int count = (int)player.CardsInHand.CardsInHand;

			for(int i = 0;i < count && i < 13;i++)
			{
				PlayerHand[i].Location = new Point((int)((FakeBounds.Width >> 1) + (12 * i - count * 6) * CardScale.Item1 - CardSize.Item1 / 2.0),(int)(FakeBounds.Height - 30 - 12.0f * CardScale.Item2 - CardSize.Item2)); // The *6 is a *12 / 2
				PlayerHand[i].Size = new System.Drawing.Size((int)CardSize.Item1,(int)CardSize.Item2);
				try{PlayerHand[i].Image = resources.GetObject(player.CardsInHand.Cards[i].ToString().Replace(' ','_')) as Image;}catch{} // Host trys to draw an extra card for some reason after the first pass so i guess we do this and call it good
				PlayerHand[i].Visible = true;
			}
			
			for(int i = count;i < PlayerHand.Length;i++)
				PlayerHand[i].Visible = false;

			return;
		}

		/// <summary>
		/// Draws the points taken by the given player in the north position.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="player">The player whose points we want to draw.</param>
		protected void DrawNorthPoints(System.Drawing.Graphics g, int player)
		{
			List<string> taken = new List<string>(14);
			string[] value_names = {"Duece","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Knave","Queen","King","Ace"};

			for(int j = 0;j < 14;j++)
				if(points_taken[player][j])
					if(j != 13)
						taken.Add(value_names[j] + "_of_Hearts");
					else
						taken.Add("Queen_of_Spades");
			
			if(taken.Count == 0)
				return;

			g.TranslateTransform(CardSize.Item1 + taken.Count * 12.0f * CardScale.Item1,30.0f * CardScale.Item2);
			g.RotateTransform(180.0f);

			for(int i = 0;i < taken.Count;i++)
				g.DrawImage(resources.GetObject(taken[i]) as Image,12.0f * i * CardScale.Item1,0.0f,CardSize.Item1,CardSize.Item2);

			g.RotateTransform(-180.0f);
			g.TranslateTransform(-(CardSize.Item1 + 12.0f * taken.Count * CardScale.Item1),-30.0f * CardScale.Item2);
			return;
		}

		/// <summary>
		/// Draws the points taken by the given player in the north position.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="player">The player whose points we want to draw.</param>
		protected void DrawEastPoints(System.Drawing.Graphics g, int player)
		{
			List<string> taken = new List<string>(14);
			string[] value_names = {"Duece","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Knave","Queen","King","Ace"};

			for(int j = 0;j < 14;j++)
				if(points_taken[player][j])
					if(j != 13)
						taken.Add(value_names[j] + "_of_Hearts");
					else
						taken.Add("Queen_of_Spades");
			
			if(taken.Count == 0)
				return;

			g.TranslateTransform(FakeBounds.Width - 35.0f * CardScale.Item2,CardSize.Item1 + taken.Count * 12.0f * CardScale.Item1);
			g.RotateTransform(270.0f);

			for(int i = 0;i < taken.Count;i++)
				g.DrawImage(resources.GetObject(taken[i]) as Image,12.0f * i * CardScale.Item1,0.0f,CardSize.Item1,CardSize.Item2);

			g.RotateTransform(-270.0f);
			g.TranslateTransform(-(FakeBounds.Width - 35.0f * CardScale.Item2),-(CardSize.Item1 + 12.0f * taken.Count * CardScale.Item1));

			return;
		}

		/// <summary>
		/// Draws the points taken by the given player in the north position.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="player">The player whose points we want to draw.</param>
		protected void DrawSouthPoints(System.Drawing.Graphics g, int player)
		{
			List<string> taken = new List<string>(14);
			string[] value_names = {"Duece","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Knave","Queen","King","Ace"};

			for(int j = 0;j < 14;j++)
				if(points_taken[player][j])
					if(j != 13)
						taken.Add(value_names[j] + "_of_Hearts");
					else
						taken.Add("Queen_of_Spades");
			
			if(taken.Count == 0)
				return;
			
			for(int i = 0;i < taken.Count;i++)
				g.DrawImage(resources.GetObject(taken[i]) as Image,FakeBounds.Width - CardSize.Item1 - (taken.Count - i + 1) * 12.0f * CardScale.Item1,FakeBounds.Height - 30.0f * (CardScale.Item2 + 1.0f),CardSize.Item1,CardSize.Item2);

			return;
		}

		/// <summary>
		/// Draws the points taken by the given player in the north position.
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="player">The player whose points we want to draw.</param>
		protected void DrawWestPoints(System.Drawing.Graphics g, int player)
		{
			List<string> taken = new List<string>(14);
			string[] value_names = {"Duece","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Knave","Queen","King","Ace"};

			for(int j = 0;j < 14;j++)
				if(points_taken[player][j])
					if(j != 13)
						taken.Add(value_names[j] + "_of_Hearts");
					else
						taken.Add("Queen_of_Spades");
			
			if(taken.Count == 0)
				return;

			g.TranslateTransform(30.0f * CardScale.Item2,FakeBounds.Height - 30.0f - CardSize.Item1 - taken.Count * 12.0f * CardScale.Item1);
			g.RotateTransform(90.0f);

			for(int i = 0;i < taken.Count;i++)
				g.DrawImage(resources.GetObject(taken[i]) as Image,12.0f * i * CardScale.Item1,0.0f,CardSize.Item1,CardSize.Item2);

			g.RotateTransform(-90.0f);
			g.TranslateTransform(-30.0f * CardScale.Item2,-(FakeBounds.Height - 30.0f - CardSize.Item1 - 12.0f * taken.Count * CardScale.Item1));

			return;
		}

		/// <summary>
		/// Draws the card played by the northern player (if any).
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="card">The card to draw.</param>
		protected void DrawNorthPlay(System.Drawing.Graphics g, Card card)
		{
			g.TranslateTransform((float)((FakeBounds.Width >> 1) + 27.0f),(float)((FakeBounds.Height >> 1) - 45.0f));
			g.RotateTransform(180.0f);
			g.DrawImage(resources.GetObject(card.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
			g.RotateTransform(-180.0f);
			g.TranslateTransform(-(float)((FakeBounds.Width >> 1) + 27.0f),-(float)((FakeBounds.Height >> 1) - 45.0f));

			return;
		}

		/// <summary>
		/// Draws the card played by the eastern player (if any).
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="card">The card to draw.</param>
		protected void DrawEastPlay(System.Drawing.Graphics g, Card card)
		{
			g.TranslateTransform((float)((FakeBounds.Width >> 1) + 27.0f),(float)((FakeBounds.Height >> 1) + 9.0f));
			g.RotateTransform(270.0f);
			g.DrawImage(resources.GetObject(card.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
			g.RotateTransform(-270.0f);
			g.TranslateTransform(-(float)((FakeBounds.Width >> 1) + 27.0f),-(float)((FakeBounds.Height >> 1) + 9.0f));

			return;
		}

		/// <summary>
		/// Draws the card played by the southern player (if any).
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="card">The card to draw.</param>
		protected void DrawSouthPlay(System.Drawing.Graphics g, Card card)
		{
			g.TranslateTransform((float)((FakeBounds.Width >> 1) - 27.0f),(float)((FakeBounds.Height >> 1) + 9.0f));
			g.DrawImage(resources.GetObject(card.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
			g.TranslateTransform(-(float)((FakeBounds.Width >> 1) - 27.0f),-(float)((FakeBounds.Height >> 1) + 9.0f));

			return;
		}

		/// <summary>
		/// Draws the card played by the western player (if any).
		/// </summary>
		/// <param name="g">The graphics to draw with.</param>
		/// <param name="card">The card to draw.</param>
		protected void DrawWestPlay(System.Drawing.Graphics g, Card card)
		{
			g.TranslateTransform((float)((FakeBounds.Width >> 1) - 27.0f),(float)((FakeBounds.Height >> 1) - 45.0f));
			g.RotateTransform(90.0f);
			g.DrawImage(resources.GetObject(card.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
			g.RotateTransform(-90.0f);
			g.TranslateTransform(-(float)((FakeBounds.Width >> 1) - 27.0f),-(float)((FakeBounds.Height >> 1) - 45.0f));

			return;
		}

		/// <summary>
		/// Gets the region that the given player covers.
		/// </summary>
		/// <param name="player">The player to get a region for.</param>
		/// <returns>Returns the region covering the given player including the card they play into a trick and their points.</returns>
		protected Region GetPlayerRegion(int player)
		{
			Region ret = GetPlayerHandRegion(player);
			ret.Union(GetPlayerTrickRegion(player));
			ret.Union(GetPlayerPointsRegion(player));

			return ret;
		}


		/// <summary>
		/// Gets the region that contains the given player's hand.
		/// </summary>
		/// <param name="player">The player to get a region for.</param>
		/// <returns>Returns the region covering the given player's hand.</returns>
		protected Region GetPlayerHandRegion(int player)
		{
			int count_card = (int)TheGame.GetPlayer(player).CardsInHand.CardsInHand + 1; // If we lose a card we still need to redraw that space
			Region ret = null;
			
			switch(player)
			{
			case 0:
				ret = new Region(new RectangleF((FakeDesktopBounds.Width >> 1) - 6.0f * count_card * CardScale.Item1 - CardSize.Item1 / 2.0f,12.0f * CardScale.Item2,CardSize.Item1 + 12.0f * count_card * CardScale.Item1,CardSize.Item2));
				break;
			case 1:
				ret = new Region(new RectangleF(FakeDesktopBounds.Width - 6.0f - 12.0f * CardScale.Item2,(FakeDesktopBounds.Height >> 1) - count_card * 6 * CardScale.Item1 - CardSize.Item1 / 2.0f,CardSize.Item2,CardSize.Item1 + 12.0f * count_card * CardScale.Item1));
				break;
			case 2:
				ret = new Region(new RectangleF((FakeDesktopBounds.Width >> 1) - 6.0f * count_card * CardScale.Item1 - CardSize.Item1 / 2.0f,FakeBounds.Height - 30.0f - 12.0f * CardScale.Item2 - CardSize.Item2,CardSize.Item1 + 12.0f * count_card * CardScale.Item1,CardSize.Item2));
				break;
			case 3:
				ret = new Region(new RectangleF(12.0f * CardScale.Item2,(FakeDesktopBounds.Height >> 1) - count_card * 6.0f * CardScale.Item1 - CardSize.Item1 / 2.0f,CardSize.Item2,CardSize.Item1 + 12.0f * count_card * CardScale.Item1));
				break;
			}

			return ret;
		}

		/// <summary>
		/// Gets the region that contains the card the given player plays into a trick.
		/// </summary>
		/// <param name="player">The player to get a region for.</param>
		/// <returns>Returns the region covering the given player's play in a trick.</returns>
		protected Region GetPlayerTrickRegion(int player)
		{
			Region ret = null;

			switch(player)
			{
			case 0:
				ret = new Region(new RectangleF((FakeBounds.Width >> 1) + 27.0f - CardSize.Item1,(FakeBounds.Height >> 1) - 45.0f - CardSize.Item2,CardSize.Item1,CardSize.Item2));
				break;
			case 1:
				ret = new Region(new RectangleF((FakeBounds.Width >> 1) + 27.0f,(FakeBounds.Height >> 1) + 9.0f - CardSize.Item1,CardSize.Item2,CardSize.Item1));
				break;
			case 2:
				ret = new Region(new RectangleF((FakeBounds.Width >> 1) - 27.0f,(FakeBounds.Height >> 1) + 9.0f,CardSize.Item1,CardSize.Item2));
				break;
			case 3:
				ret = new Region(new RectangleF((FakeBounds.Width >> 1) - 27.0f - CardSize.Item2,(FakeBounds.Height >> 1) - 45.0f,CardSize.Item2,CardSize.Item1));
				break;
			}

			return ret;
		}

		/// <summary>
		/// Gets the region covering the given player's points.
		/// </summary>
		/// <param name="player">The player's points to get a region for.</param>
		/// <returns>Returns the region covering the given player's points.</returns>
		protected Region GetPlayerPointsRegion(int player)
		{
			int count_cards = 0;

			for(int i = 0;i < 14;i++)
				if(points_taken[player][i])
					++count_cards;

			if(count_cards == 0)
				return new Region(new Rectangle(0,0,0,0));

			Region ret = null;
			
			switch(player)
			{
			case 0:
				ret = new Region(new RectangleF(12.0f * CardScale.Item1,0.0f,CardSize.Item1 + count_cards * CardScale.Item1,30.0f * CardScale.Item2));
				break;
			case 1:
				ret = new Region(new RectangleF(FakeBounds.Width - 35.0f * CardScale.Item2,12.0f * CardScale.Item1,30.0f * CardScale.Item2,CardSize.Item1 + count_cards * 12.0f * CardScale.Item1));
				break;
			case 2:
				ret = new Region(new RectangleF(FakeBounds.Width - CardSize.Item1 - (count_cards + 1) * 12.0f * CardScale.Item1,FakeBounds.Height - 30.0f * (CardScale.Item2 + 1.0f),CardSize.Item1 + count_cards * 12.0f * CardScale.Item1,30.0f * CardScale.Item2));
				break;
			case 3:
				ret = new Region(new RectangleF(0.0f,FakeBounds.Height - 30.0f - CardSize.Item1 - 12.0f * count_cards * CardScale.Item1,30.0f * CardScale.Item2,CardSize.Item1 + count_cards * 12.0f * CardScale.Item1));
				break;
			}

			return ret;
		}
#endregion

#region Helper Functions
		/// <summary>
		/// Sends out the hands packet to clients.
		/// </summary>
		private void HostSendHands()
		{
			List<Card> hands = new List<Card>(13);

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
			{
				for(int j = 0;j < TheGame.GetPlayer(i).CardsInHand.CardsInHand;j++)
					hands.Add(TheGame.GetPlayer(i).CardsInHand.Cards[j]);

				nio.QueuePacket(new HeartsHandsPacket(hands,i).ToString());
				hands.Clear();
			}

			return;
		}

		/// <summary>
		/// Waits for a hands list packet.
		/// </summary>
		private void ClientGetHands()
		{
			waiting_for_hands = 4;

			while(waiting_for_hands > 0)
				Thread.Sleep(ClientSleep);

			return;
		}

        /// <summary>
		/// Sends the given message from the given person.
		/// </summary>
		/// <param name="msg">The message to send.</param>
		private void SendMessage(string msg)
		{
			nio.QueuePacket(new CWCMessagePacket(msg,ThePlayerName).ToString());
			return;
		}

        /// <summary>
        /// Kicks a player for the host.
        /// </summary>
		/// <param name="sender">The originator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void KickPlayer(object sender, EventArgs e)
        {
            int index = comboBox1.SelectedIndex;
            int itemp = index;

            if(index < 0)
                return;

            // Find the player index (it should be the same as the selected index but who knows what could happen)
            string find = comboBox1.Items[index] as string;

            for(index = 0;Players[index] != (comboBox1.Items[index] as string);index++);

            // Kicking AI is dumb
            if(TheGame.GetPlayer(index).IsAI)
                return;

			// Host can't kick the host
			if(index == ThePlayer)
				return;

			TheGame.PlayerLeft(index,GetDefaultAI(index)); // Replace them for they are replaceable
			nio.QueuePacket(new CWCMessagePacket(Players[index] + " has left the game. An AI also called " + Players[index] + " has usurped the position.","").ToString()); // Spread the word

            // On the off chance that names have changed redo this
            comboBox1.Items.Clear();

            foreach(string str in Players)
                comboBox1.Items.Add(str);

            comboBox1.SelectedIndex = itemp;

            return;
        }

		/// <summary>
		/// Fixes lead issues we get on the first pass.
		/// </summary>
		protected void RedetermineLead()
		{
			int new_lead = 0;

			// Find who has the two of clubs now and start the leaders log
			Card two_of_clubs = new StandardCard(new Clubs(),new ValueN(2.0,"Duece"));

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
				if(TheGame.GetPlayer(i).CardsInHand.Cards.Contains(two_of_clubs))
				{
					new_lead = i;
					break;
				}

			(TheGame as HeartsGameState).Leader = new_lead;
			return;
		}

		/// <summary>
		/// Handles card click events.
		/// </summary>
		/// <param name="index">The index of the card clicked.</param>
		protected void SelectCard(int index)
		{
			// If the current player is not the local player then don't even bother
			if(TheGame.ActivePlayer != ThePlayer && !passing)
				return;

			if(!DoubleClickToSelect)
				selected_card = index; // Doing this allows use to do single clicking

			if(selected_card == index && selected_card >= 0 && selected_card < TheGame.GetPlayer(ThePlayer).CardsInHand.CardsInHand && (passing || TheGame.IsValid(new HeartsMove(TheGame.GetPlayer(ThePlayer).CardsInHand.Cards[selected_card],TheGame.ActivePlayer))))
				card_selected = true;

			if(DoubleClickToSelect)
			{
				if(selected_card != -1)
					PlayerHand[selected_card].BorderStyle = BorderStyle.None;

				selected_card = index; // Doing this allows use to do double clicking
				InvokeOnFormThread(new SetBorder(SetCardBorder),new object[] {PlayerHand[index],BorderStyle.FixedSingle});
			}
			
			return;
		}

		/// <summary>
		/// Handles card double click events.
		/// </summary>
		/// <param name="index">The index of the card clicked.</param>
		protected void SelectCardD(int index)
		{
			// If the current player is not the local player then don't even bother
			if(TheGame.ActivePlayer != ThePlayer && !passing)
				return;

			selected_card = index;

			if(selected_card >= 0 && selected_card < TheGame.GetPlayer(ThePlayer).CardsInHand.CardsInHand && (passing || TheGame.IsValid(new HeartsMove(TheGame.GetPlayer(ThePlayer).CardsInHand.Cards[selected_card],TheGame.ActivePlayer))))
				card_selected = true;

			return;
		}

		/// <summary>
		/// Used to set a border style of a card.
		/// </summary>
		/// <param name="pic">The picturebox to change the border of.</param>
		/// <param name="bs">The border style.</param>
		protected void SetCardBorder(PictureBox pic, BorderStyle bs)
		{
			pic.BorderStyle = bs;
			return;
		}

		/// <summary>
		/// Sets the borders of the given control.
		/// </summary>
		/// <param name="ctrl">The control to set.</param>
		/// <param name="bs">The border style to have.</param>
		protected void SetHandBorders(BorderStyle bs)
		{
			foreach(PictureBox pb in PlayerHand)
				pb.BorderStyle = bs;
			
			return;
		}
#endregion

#region Windows Form Functions
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
					InvokeOnFormThread(new DS(PostMessage),textBox1.Text,ThePlayerName);
				
				textBox1.Text = "";
				e.SuppressKeyPress = true;
			}

			return;
		}
#endregion

#region Events
        /// <summary>
		/// An event that is fired when a new trick is started.
		/// </summary>
		/// <param name="state">The state of the game at the start of the trick.</param>
		protected void StartTrick(GameState state)
		{return;}
		
		/// <summary>
		/// An event that is fired when a trick ends.
		/// </summary>
		/// <param name="state">The state of the game at the end of the trick.</param>
		protected void EndTrick(GameState state)
		{
			HeartsGameState hg = TheGame as HeartsGameState;

			// Find out if points where taken
			IList<Card> trick = hg.TrickSoFar();
			int taker = (hg.Leader + hg.HighestCard(trick)) % hg.NumberOfPlayers;

			InvalidatePlayers(new int[] {taker}); // Draw the player that took the points to redraw the points they have
			Thread.Sleep(1500); // Give the player time to take in the trick (the trick is updates as invalidating the player invalidates the trick)

			foreach(Card c in trick)
			{
				int points = hg.Points(c);

				if(points == 1)
					points_taken[taker][(int)c.Value.MaxValue - 2] = true;
				else if(points == 13)
					points_taken[taker][13] = true;
			}

			InvalidatePlayers(new int[] {taker}); // Draw the player that took the points to redraw the points they have
			return;
		}

		/// <summary>
		/// An event that is fired when a new round is started.
		/// </summary>
		/// <param name="state">The state of the game at the start of the round.</param>
		protected void StartRound(GameState state)
		{
			int[] inval = new int[TheGame.NumberOfPlayers];

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
				inval[i] = i;

			InvalidatePlayers(inval);
			return;
		}

		/// <summary>
		/// An event fired during the passing phase.
		/// </summary>
		/// <param name="state">The state of the game when passing begins.</param>
		protected void Pass(GameState state)
		{
			// Players need up-to-date hands to pass cards from them
			if(is_host)
				HostSendHands();
			else
				ClientGetHands();

			// Reset points taken stuff
			for(int i = 0;i < 4;i++)
				for(int j = 0;j < 14;j++)
					points_taken[i][j] = false;

			// Invalidate the players and the trick
			int[] inval = new int[TheGame.NumberOfPlayers];

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
				inval[i] = i;

			// Sort the local player's hand
			TheGame.GetPlayer(ThePlayer).CardsInHand.SortBySuit();

			InvalidatePlayers(inval); // The trick is also invalidated by this

			// Determine what kind of passing round it is
			int pass_type = (TheGame as HeartsGameState).Round % 4;

			// Pass cards until it is a no passing round
			if(pass_type != 0) // First round is round 1, not 0
			{
				passing = true;

				// Need to redraw player
				InvalidatePlayers(new int[] {ThePlayer});

				switch(pass_type)
				{
				case 1: // Pass left
					MessageBox.Show("Pass three cards left.","Pass Left",MessageBoxButtons.OK);
					break;
				case 2:
					MessageBox.Show("Pass three cards right.","Pass Right",MessageBoxButtons.OK);
					break;
				case 3:
					MessageBox.Show("Pass three cards across.","Pass Across",MessageBoxButtons.OK);
					break;
				}

				List<Card> passes = new List<Card>(3);

				// Get local passes
				for(int i = 0;i < 3;i++)
				{
					passes.Add(TheGame.GetPlayer(ThePlayer).CardsInHand.PlayCard((LocalGetNextMove() as HeartsMove).Play));
					InvalidatePlayers(new int[] {ThePlayer});
				}

				if(!is_host) // Clients send information to the host
				{
					nio.QueuePacket(new HeartsPassingPacket(ThePlayer,passes).ToString());

					CWCMoveConfirmationPacket confirm;

					// See if our move was confirmed
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

						if(!nio.HasPacket)
							Thread.Sleep(ClientSleep); // Wait and see if a packet shows up noting that it will probably take a while (client can afford to wait a lot longer than the host)
					}

					// If the passing wasn't successful then try again
					if(!confirm.Success)
					{
						// Add local cards back in
						for(int i = 0;i < passes.Count;i++)
							TheGame.GetPlayer(ThePlayer).CardsInHand.DrawCard(passes[i]);

						Pass(state);
						return;
					}
				}
				else // The host will collect information and then send it all out
				{
					foreach(Card c in passes)
						passing_info.Add(new Tuple<Card,int>(c,ThePlayer));

					// Add all passes from AI players
					for(int i = 0;i < TheGame.NumberOfPlayers;i++)
						if(TheGame.GetPlayer(i).IsAI)
							for(int j = 0;j < 3;j++)
								passing_info.Add(new Tuple<Card,int>(TheGame.GetPlayer(i).CardsInHand.Cards[j],i)); // The AI hands are never sorted so just grab the first three cards

					// If this is a single player game make sure we don't idle for packets that will never come
					if(passing_info.Count == TheGame.NumberOfPlayers * 3)
						waiting_for_passes--;
				}

				// Wait until we get all of the passes (networking will take care of them)
				waiting_for_passes++; // We might get information in a weird order so use a tertiary state

				while(waiting_for_passes != 0)
					Thread.Sleep(is_host ? HostSleep : ClientSleep);

				// Add local cards back in (makes things easier later)
				for(int i = 0;i < passes.Count;i++)
					TheGame.GetPlayer(ThePlayer).CardsInHand.DrawCard(passes[i]);

				// Copy passing info so we don't have to worry about threading
				List<Tuple<Card,int>> copy = new List<Tuple<Card,int>>(passing_info);

				// Reset our passing information
				waiting_for_passes = 0; // Just in case something crazy happened
				passing_info.Clear();

				// Sort the passing info we copied for easy computation (although information recieved by clients should already be sorted we should sort anyways just in case and besides it doesn't take long)
				copy.Sort((t1,t2) => {return t1.Item2.CompareTo(t2.Item2);});

				// If we are the host then send out the passing information to everyone
				if(is_host)
					nio.QueuePacket(new HeartsPassingListPacket(copy).ToString());

				// Create some space to store where we want to move things to
				int[] dest = new int[TheGame.NumberOfPlayers];

				// Move the cards around
				switch(pass_type)
				{
				case 1: // Pass left
					// Cards at x go to x + 1
					for(int i = 0;i < TheGame.NumberOfPlayers;i++)
					{
						dest[i] = (i + 1) % TheGame.NumberOfPlayers;

						while(dest[i] < 0) // C# does negative mods which is annoying since I hate that residue group
							dest[i] += TheGame.NumberOfPlayers;
					}

					break;
				case 2: // Pass right
					// Cards at x go to x - 1
					for(int i = 0;i < TheGame.NumberOfPlayers;i++)
					{
						dest[i] = (i - 1) % TheGame.NumberOfPlayers;

						while(dest[i] < 0) // C# does negative mods which is annoying since I hate that residue group
							dest[i] += TheGame.NumberOfPlayers;
					}

					break;
				case 3: // Pass across
					// Cards at x go to x + NumberOfPlayers / 2
					for(int i = 0;i < TheGame.NumberOfPlayers;i++)
					{
						dest[i] = (i + (TheGame.NumberOfPlayers >> 1)) % TheGame.NumberOfPlayers;

						while(dest[i] < 0) // C# does negative mods which is annoying since I hate that residue group
							dest[i] += TheGame.NumberOfPlayers;
					}

					break;
				}

				// Move cards around
				for(int i = 0;i < TheGame.NumberOfPlayers;i++)
					for(int j = 0;j < 3;j++) // Move every card passed by player i to player dest[i]
						TheGame.GetPlayer(dest[i]).CardsInHand.DrawCard(TheGame.GetPlayer(i).CardsInHand.PlayCard(copy[i * 3 + j].Item1)); // passing_info is sorted by player source

				// Sort the local player's hand again since new cards were added
				TheGame.GetPlayer(ThePlayer).CardsInHand.SortBySuit();
				passing = false;

				// Redraw the hands
				InvalidatePlayers(inval);
			}
			else
				MessageBox.Show("No passing this round.","No Pass",MessageBoxButtons.OK);

			return;
		}

		/// <summary>
		/// Fired when a round ends.
		/// </summary>
		/// <param name="state">The state of the game at the end of the round.</param>
		protected void EndRound(GameState state)
		{
			// Redraw everything but skip drawing the trick
			skip_trick = true;
			int[] inval = new int[TheGame.NumberOfPlayers];

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
				inval[i] = i;

			InvalidatePlayers(inval);
			skip_trick = false;
			
			HeartsGameState game = TheGame as HeartsGameState;
			MessageBox.Show("Make this score box better!\nNorth: " + game.North.Score + " East: " + game.East.Score + " South: " + game.South.Score + " West: " + game.West.Score,"Scores",MessageBoxButtons.OK);
			
			return;
		}

		/// <summary>
		/// An event fired when a game state reaches a finish state.
		/// </summary>
		/// <param name="state">The game state in its final state.</param>
		protected void Finish(GameState state)
		{
			int min = 999;
			List<int> players = new List<int>(TheGame.NumberOfPlayers);
			HeartsGameState game = TheGame as HeartsGameState;

			for(int i = 0;i < TheGame.NumberOfPlayers;i++)
			{
				int score = game.GetHeartsPlayer(i).Score;

				if(score < min)
				{
					min = score;

					players.Clear();
					players.Add(i);
				}
				else if(score == min)
					players.Add(i);
			}

			string firstline = Players[players[0]];

			for(int i = 1;i < players.Count;i++)
				firstline += " and " + Players[players[i]];

			firstline += players.Count == 1 ? " wins." : " tie for the win.";

			MessageBox.Show("Make this score box better!\n" + firstline + "\nFinal Scores were North: " + game.North.Score + " East: " + game.East.Score + " South: " + game.South.Score + " West: " + game.West.Score,"Game Over",MessageBoxButtons.OK);
			return;
		}
#endregion

#region Delegates
		/// <summary>
		/// Used by invalidation logic.
		/// </summary>
		/// <param name="r">The region to invalidate.</param>
		protected delegate void InvalidateCall(Region r);
		
		/// <summary>
		/// Used to set a border style.
		/// </summary>
		/// <param name="pic">The picturebox to change the border of.</param>
		/// <param name="bs">The border style.</param>
		protected delegate void SetBorder(PictureBox pic, BorderStyle bs);

		/// <summary>
		/// Used to set border styles.
		/// </summary>
		/// <param name="bs">The border style.</param>
		protected delegate void SetBorders(BorderStyle bs);
#endregion

#region Properties
		/// <summary>
		/// If set to true then we are attempting to join a game.
		/// </summary>
		public bool Joining
		{get; set;}

		/// <summary>
		/// The size of a card being drawn.
		/// The default is 72x96.
		/// </summary>
		public Tuple<float,float> CardSize
		{get; set;}

		/// <summary>
		/// The scaling of a card being drawn.
		/// </summary>
		public Tuple<float,float> CardScale
		{
			get
			{return new Tuple<float,float>(CardSize.Item1 / 72.0f,CardSize.Item2 / 96.0f);}
			set
			{
				if(value == null)
					return;

				CardSize = new Tuple<float,float>(72.0f * value.Item1,96.0f * value.Item2);
				return;
			}
		}

        /// <summary>
        /// A fake set of bounds to allow for a chat window.
        /// </summary>
        protected Rectangle FakeBounds
        {
            get
            {return new Rectangle(Bounds.X,Bounds.Y,Bounds.Width,Bounds.Height - 200);}
        }

        /// <summary>
        /// A fake set of bounds to allow for a chat window.
        /// </summary>
        protected Rectangle FakeDesktopBounds
        {
            get
            {return new Rectangle(DesktopBounds.X,DesktopBounds.Y,DesktopBounds.Width,DesktopBounds.Height - 200);}
        }

        /// <summary>
		/// If true then no messages have been posted yet.
		/// </summary>
		private bool FirstPost
		{
			get
			{return messages.Count == 0;}
		}
#endregion

#region Member Varaiables
		/// <summary>
		/// The cards in the local player's hand.
		/// </summary>
		protected PictureBox[] PlayerHand;

		/// <summary>
		/// Which points have been taken so far.
		/// </summary>
		protected bool[][] points_taken;

		/// <summary>
		/// If true at index i then player i is human.
		/// </summary>
		protected bool[] humans;

		/// <summary>
		/// If true then the hundred reset option is enabled.
		/// </summary>
		protected bool hundred_reset;

		/// <summary>
		/// If true then we are in the passing phase.
		/// </summary>
		protected bool passing = false;

		/// <summary>
		/// If true then a card is currently selected.
		/// </summary>
		protected bool card_selected;

		/// <summary>
		/// The index of the selected card.
		/// </summary>
		protected int selected_card;

		/// <summary>
		/// If one then we are waiting for more passing information.
		/// If zero we are not.
		/// If negative one then have passing info already.
		/// </summary>
		protected int waiting_for_passes = 0;

		/// <summary>
		/// Contains information about passing.
		/// First value is the card to pass, the second value is the player passing it.
		/// </summary>
		protected List<Tuple<Card,int>> passing_info;

		/// <summary>
		/// If true then we should not draw the trick.
		/// This is used to avoid drawing the trick at the end of the round.
		/// </summary>
		protected bool skip_trick = false;

		/// <summary>
		/// Provides resources for this table.
		/// </summary>
		protected ResourceManager resources;

        /// <summary>
        /// The textbox displaying a local chat message not yet sent.
        /// </summary>
        protected TextBox textBox1;

        /// <summary>
        /// The textbox displaying chat messages.
        /// </summary>
        protected RichTextBox richTextBox1;

        /// <summary>
        /// A combo box for kicking players.
        /// </summary>
        protected ComboBox comboBox1;

        /// <summary>
        /// A button for kicking players.
        /// </summary>
        protected Button button1;

		/// <summary>
		/// If true then we are waiting for hand information.
		/// </summary>
		protected int waiting_for_hands = 0;
#endregion
	}
}
