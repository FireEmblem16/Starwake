using System;
using System.Collections.Generic;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;
using Engine.Cards;
using Engine.Cards.CardTypes;
using Engine.Cards.CardTypes.Suits;
using Engine.Cards.CardTypes.Values;
using Engine.Game;
using Engine.Player;
using Engine.Player.AI;
using ExampleFormGame.Properties;

namespace ExampleFormGame
{
    public partial class Table : Form
    {
        public Table(Form ret)
        {
            InitializeComponent();
			
			base_size = new Size(600,600);
			//Size = base_size;
			CardSize = new Tuple<float,float>(72.0f,96.0f);
			CardScale = new Tuple<float,float>(1.33f,1.33f);

			this.Disposed += new EventHandler(Table_Dispose);
			this.Resize += new EventHandler(Scaler);
            ReturnTo = ret;

            return;
        }

		private void Table_Load(object sender, EventArgs e)
		{
			game = new HeartsGameState(false,false,true); // Single player game
			//game = new HeartsGameState(false); // AI game
			
			game.StateChanged += StateChanged;
			game.StartTrick += StartTrick;
			game.EndTrick += EndTrick;
			game.StartRound += StartRound;
			game.Passing += Pass;
			game.EndRound += EndRound;
			game.Finished += Over;

			points_taken = new bool[4][];

			for(int i = 0;i < 4;i++)
			{
				points_taken[i] = new bool[14];

				for(int j = 0;j < 14;j++)
					points_taken[i][j] = false;
			}

			SouthHand = new PictureBox[13];

			for(int i = 0;i < 13;i++)
			{
				SouthHand[i] = new PictureBox();

				SouthHand[i].Location = new System.Drawing.Point((Bounds.Width >> 1) - (int)game.South.CardsInHand.CardsInHand * 6 + 12 * i - 36,Bounds.Height - 146);
				SouthHand[i].Name = "pictureBox" + i;
				SouthHand[i].Size = new System.Drawing.Size(72,96);
				SouthHand[i].TabIndex = 0;
				SouthHand[i].TabStop = false;
				SouthHand[i].Image = Resources.ResourceManager.GetObject(game.South.CardsInHand.Cards[i].ToString().Replace(' ','_')) as Image;
				SouthHand[i].Visible = true;
				SouthHand[i].BorderStyle = BorderStyle.None;
				SouthHand[i].SizeMode = PictureBoxSizeMode.StretchImage;

				int temp_i = i;
				SouthHand[i].Click += new EventHandler((s,evt) => SelectCard(temp_i));
				SouthHand[i].DoubleClick += new EventHandler((s,evt) => SelectCardD(temp_i));
			}

			for(int i = 12;i >= 0;i--)
				Controls.Add(SouthHand[i]);

			// This code works for local single player (as does the rest of the code in this file)
			// Do not attempt to use it for network games
			t = new Thread(new ThreadStart(() => {Pass(game); RedetermineLead(); while(!game.GameFinished){HeartsPlayer player = game.GetHeartsPlayer(game.ActivePlayer); if(player.IsAI) game.ApplyMove((player as AIPlayer).TakeTurn(game)); else game.ApplyMove(GetNextMove());} return;}));
			t.Name = "Game Thread";
			t.Start();

			return;
		}

		private void Scaler(object sender, EventArgs e)
		{
			Size size = Size;

			// We will use width for scaling the window
			Size = new Size(size.Width,size.Width);
			CardScale = new Tuple<float,float>(((float)size.Width) / base_size.Width,((float)size.Width) / base_size.Width);
			
			Invalidate();
			Invoke(new DV(Update));
			return;
		}
		
		private Size base_size;

		/// <summary>
		/// Fixes lead issues we get on the first pass.
		/// </summary>
		protected void RedetermineLead()
		{
			int new_lead = 0;

			// Find who has the two of clubs now and start the leaders log
			Card two_of_clubs = new StandardCard(new Clubs(),new ValueN(2.0,"Duece"));

			for(int i = 0;i < game.NumberOfPlayers;i++)
				if(game.GetPlayer(i).CardsInHand.Cards.Contains(two_of_clubs))
				{
					new_lead = i;
					break;
				}

			game.Leader = new_lead;
			return;
		}

		/// <summary>
		/// Gets the next move of the human player from the GUI.
		/// </summary>
		/// <returns>Returns the move that this player wants to make.</returns>
		protected Move GetNextMove()
		{
			while(!card_selected)
				Thread.Sleep(100);

			Move ret = new HeartsMove(game.South.CardsInHand.Cards[selected_card],game.ActivePlayer);

			card_selected = false;
			selected_card = -1;

			Invoke(new SB(SetBorder),BorderStyle.None);			
			return ret;
		}

		protected delegate void SB(BorderStyle bs);

		protected void SetBorder(BorderStyle bs)
		{
			for(int i = 0;i < 13;i++)
				SouthHand[i].BorderStyle = bs;

			return;
		}

		/// <summary>
		/// An event fired on game state changes.
		/// </summary>
		/// <param name="state">The state that changed.</param>
		/// <param name="m">The last move or null if the state change was not move related.</param>
		/// <param name="undo">If true then the move provided has been undone.</param>
		protected void StateChanged(GameState state, Move m, bool undo = false)
		{
			// Draw hands, point cards taken and current trick
			Invalidate();
			Invoke(new DV(Update));
			
			Thread.Sleep(250); // Take a moment
			return;
		}

		/// <summary>
		/// Does all the drawing not part of a control.
		/// </summary>
		/// <param name="e">The paint event arguments.</param>
		protected override void OnPaint(PaintEventArgs e)
		{
			base.OnPaint(e);
			
			DrawNorth(e);
			DrawEast(e);
			DrawWest(e);
			UpdateSouth();
			DrawPoints(e);

			if(!skip_trick)
				DrawTrick(e);

			return;
		}

		/// <summary>
		/// Draws the cards in north's hand.
		/// </summary>
		/// <param name="e">The paint event arguments.</param>
		protected void DrawNorth(PaintEventArgs e)
		{
			for(int i = 0;i < game.North.CardsInHand.CardsInHand;i++)
				e.Graphics.DrawImage(Resources.b1fv,(float)(DesktopBounds.Width >> 1) + (12 * i - (int)game.North.CardsInHand.CardsInHand * 6) * CardScale.Item1 - CardSize.Item1 / 2.0f,12.0f * CardScale.Item2,CardSize.Item1,CardSize.Item2); // The *6 is a * 12 / 2
			
			return;
		}

		/// <summary>
		/// Draws the cards in east's hand.
		/// </summary>
		/// <param name="e">The paint event arguments.</param>
		protected void DrawEast(PaintEventArgs e)
		{
			for(int i = 0;i < game.East.CardsInHand.CardsInHand;i++)
				e.Graphics.DrawImage(Resources.b1fh,DesktopBounds.Width - 6.0f - 12.0f * CardScale.Item2 - CardSize.Item2,(float)(DesktopBounds.Height >> 1) + (12 * i - (int)game.East.CardsInHand.CardsInHand * 6) * CardScale.Item1 - CardSize.Item1 / 2.0f,CardSize.Item2,CardSize.Item1); // The *6 is a * 12 / 2
            
			return;
		}

		/// <summary>
		/// Draws the cards in west's hand.
		/// </summary>
		/// <param name="e">The paint event arguments.</param>
		protected void DrawWest(PaintEventArgs e)
		{
			for(int i = 0;i < game.West.CardsInHand.CardsInHand;i++)
				e.Graphics.DrawImage(Resources.b1fh,12.0f * CardScale.Item2,(float)(DesktopBounds.Height >> 1) + (12 * i - (int)game.West.CardsInHand.CardsInHand * 6) * CardScale.Item1 - CardSize.Item1 / 2.0f,CardSize.Item2,CardSize.Item1); // The *6 is a * 12 / 2
			
			return;
		}

		/// <summary>
		/// Draws the cards in south's hand.
		/// </summary>
		protected void UpdateSouth()
		{
			Player player = game.GetPlayer(2);
			int count = (int)player.CardsInHand.CardsInHand;

			for(int i = 0;i < count;i++)
			{
				SouthHand[i].Location = new Point((int)((Bounds.Width >> 1) + (12 * i - count * 6) * CardScale.Item1 - CardSize.Item1 / 2.0),(int)(Bounds.Height - 30 - 12 * CardScale.Item2 - CardSize.Item2)); // The *6 is a *12 / 2
				SouthHand[i].Size = new System.Drawing.Size((int)CardSize.Item1,(int)CardSize.Item2);
				SouthHand[i].Image = Resources.ResourceManager.GetObject(player.CardsInHand.Cards[i].ToString().Replace(' ','_')) as Image;
				SouthHand[i].Visible = true;
			}
			
			for(int i = count;i < SouthHand.Length;i++)
				SouthHand[i].Visible = false;

			return;
		}

		/// <summary>
		/// Draws the trick in its current progress.
		/// </summary>
		/// <param name="e">The paint event arguments.</param>
		protected void DrawTrick(PaintEventArgs e)
		{
			IList<Card> trick = game.TrickSoFar();
			int leader = game.Leader;

			Card North = null;
			Card South = null;
			Card East = null;
			Card West = null;

#region Unsightly Code
			switch(leader)
			{
			case 0:
				if(trick.Count > 0)
				{
					North = trick[0];

					if(trick.Count > 1)
					{
						East = trick[1];

						if(trick.Count > 2)
						{
							South = trick[2];

							if(trick.Count > 3)
								West = trick[3];
						}
					}
				}

				break;
			case 1:
				if(trick.Count > 0)
				{
					East = trick[0];

					if(trick.Count > 1)
					{
						South = trick[1];

						if(trick.Count > 2)
						{
							West = trick[2];

							if(trick.Count > 3)
								North = trick[3];
						}
					}
				}

				break;
			case 2:
				if(trick.Count > 0)
				{
					South = trick[0];

					if(trick.Count > 1)
					{
						West = trick[1];

						if(trick.Count > 2)
						{
							North = trick[2];

							if(trick.Count > 3)
								East = trick[3];
						}
					}
				}

				break;
			case 3:
				if(trick.Count > 0)
				{
					West = trick[0];

					if(trick.Count > 1)
					{
						North = trick[1];

						if(trick.Count > 2)
						{
							East = trick[2];

							if(trick.Count > 3)
								South = trick[3];
						}
					}
				}

				break;
			}
#endregion
			
			if(North != null)
			{
				e.Graphics.TranslateTransform((float)((Bounds.Width >> 1) + 27.0f),(float)((Bounds.Height >> 1) - 45.0f));
				e.Graphics.RotateTransform(180.0f);
				e.Graphics.DrawImage(Resources.ResourceManager.GetObject(North.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
				e.Graphics.RotateTransform(-180.0f);
				e.Graphics.TranslateTransform(-(float)((Bounds.Width >> 1) + 27.0f),-(float)((Bounds.Height >> 1) - 45.0f));
			}

			if(East != null)
			{
				e.Graphics.TranslateTransform((float)((Bounds.Width >> 1) + 27.0f),(float)((Bounds.Height >> 1) + 9.0f));
				e.Graphics.RotateTransform(270.0f);
				e.Graphics.DrawImage(Resources.ResourceManager.GetObject(East.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
				e.Graphics.RotateTransform(-270.0f);
				e.Graphics.TranslateTransform(-(float)((Bounds.Width >> 1) + 27.0f),-(float)((Bounds.Height >> 1) + 9.0f));
			}

			if(South != null)
			{
				e.Graphics.TranslateTransform((float)((Bounds.Width >> 1) - 27.0f),(float)((Bounds.Height >> 1) + 9.0f));
				e.Graphics.DrawImage(Resources.ResourceManager.GetObject(South.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
				e.Graphics.TranslateTransform(-(float)((Bounds.Width >> 1) - 27.0f),-(float)((Bounds.Height >> 1) + 9.0f));
			}

			if(West != null)
			{
				e.Graphics.TranslateTransform((float)((Bounds.Width >> 1) - 27.0f),(float)((Bounds.Height >> 1) - 45.0f));
				e.Graphics.RotateTransform(90.0f);
				e.Graphics.DrawImage(Resources.ResourceManager.GetObject(West.ToString().Replace(' ','_')) as Image,0.0f,0.0f,CardSize.Item1,CardSize.Item2);
				e.Graphics.RotateTransform(-90.0f);
				e.Graphics.TranslateTransform(-(float)((Bounds.Width >> 1) - 27.0f),-(float)((Bounds.Height >> 1) - 45.0f));
			}

			return;
		}

		/// <summary>
		/// Draws all taken points.
		/// </summary>
		/// <param name="e">The paint event arguments.</param>
		protected void DrawPoints(PaintEventArgs e)
		{
			List<string>[] taken = new List<string>[4];
			string[] value_names = {"Duece","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Knave","Queen","King","Ace"};

			for(int i = 0;i < 4;i++)
			{
				taken[i] = new List<string>();

				for(int j = 0;j < 14;j++)
					if(points_taken[i][j])
						if(j != 13)
							taken[i].Add(value_names[j] + "_of_Hearts");
						else
							taken[i].Add("Queen_of_Spades");
			}
			
			if(taken[0].Count != 0)
			{
				e.Graphics.TranslateTransform(CardSize.Item1 + taken[0].Count * 12.0f * CardScale.Item1,30.0f * CardScale.Item2);
				e.Graphics.RotateTransform(180.0f);

				for(int i = 0;i < taken[0].Count;i++)
					e.Graphics.DrawImage(Resources.ResourceManager.GetObject(taken[0][i]) as Image,i * 12.0f * CardScale.Item1,0.0f,CardSize.Item1,CardSize.Item2);

				e.Graphics.RotateTransform(-180.0f);
				e.Graphics.TranslateTransform(-(CardSize.Item1 + taken[0].Count * 12.0f * CardScale.Item1),-30.0f * CardScale.Item2);
			}

			if(taken[1].Count != 0)
			{
				e.Graphics.TranslateTransform(Bounds.Width - 35.0f * CardScale.Item2,CardSize.Item1 + taken[1].Count * 12.0f * CardScale.Item1);
				e.Graphics.RotateTransform(270.0f);

				for(int i = 0;i < taken[1].Count;i++)
					e.Graphics.DrawImage(Resources.ResourceManager.GetObject(taken[1][i]) as Image,i * 12.0f * CardScale.Item1,0.0f,CardSize.Item1,CardSize.Item2);

				e.Graphics.RotateTransform(-270.0f);
				e.Graphics.TranslateTransform(-(Bounds.Width - 35.0f * CardScale.Item2),-(CardSize.Item1 + taken[1].Count * 12.0f * CardScale.Item1));
			}
			
			if(taken[2].Count != 0)
				for(int i = 0;i < taken[2].Count;i++)
					e.Graphics.DrawImage(Resources.ResourceManager.GetObject(taken[2][i]) as Image,Bounds.Width - CardSize.Item1 - (taken[2].Count - i + 1) * 12.0f * CardScale.Item1,Bounds.Height - 30.0f * (CardScale.Item2 + 1.0f),CardSize.Item1,CardSize.Item2);
			
			if(taken[3].Count != 0)
			{
				e.Graphics.TranslateTransform(30.0f * CardScale.Item2,Bounds.Height - 30.0f - CardSize.Item1 - taken[3].Count * 12.0f * CardScale.Item1);
				e.Graphics.RotateTransform(90.0f);

				for(int i = 0;i < taken[3].Count;i++)
					e.Graphics.DrawImage(Resources.ResourceManager.GetObject(taken[3][i]) as Image,i * 12.0f * CardScale.Item1,0.0f,CardSize.Item1,CardSize.Item2);

				e.Graphics.RotateTransform(-90.0f);
				e.Graphics.TranslateTransform(-30.0f * CardScale.Item2,-(Bounds.Height - 30.0f - CardSize.Item1 - taken[3].Count * 12.0f * CardScale.Item1));
			}

			return;
		}

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
			// Let the player see the trick
			Invalidate();
			Invoke(new DV(Update));
			Thread.Sleep(1000);
			
			HeartsGameState hg = game as HeartsGameState;

			// Find out if points where taken
			IList<Card> trick = hg.TrickSoFar();
			int taker = (hg.Leader + hg.HighestCard(trick)) % hg.NumberOfPlayers;

			foreach(Card c in trick)
			{
				int points = hg.Points(c);

				if(points == 1)
					points_taken[taker][(int)c.Value.MaxValue - 2] = true;
				else if(points == 13)
					points_taken[taker][13] = true;
			}

			Invalidate(); // The points need to be redrawn
			Invoke(new DV(Update));
			return;
		}

		/// <summary>
		/// An event that is fired when a new round is started.
		/// </summary>
		/// <param name="state">The state of the game at the start of the round.</param>
		protected void StartRound(GameState state)
		{
			// Redraw the screen
			Invalidate();
			Invoke(new DV(Update));

			return;
		}

		/// <summary>
		/// An event fired during the passing phase.
		/// </summary>
		/// <param name="state">The state of the game when passing begins.</param>
		protected void Pass(GameState state)
		{
			game.South.CardsInHand.SortBySuit();
            
			// Reset points taken stuff
			for(int i = 0;i < 4;i++)
				for(int j = 0;j < 14;j++)
					points_taken[i][j] = false;
			
			Invalidate(); // We meed to be able to see our hand
			Invoke(new DV(Update));

			// Pass cards (we totally forgot to make this a thing in the game state but that's okay because it's simple to do here [but not there])
			if(game.Round % 4 != 0)
			{
				passing = true;

				Invoke(new text(set_text),"Hearts - Pass 3");

				Move m1 = GetNextMove();
				game.South.CardsInHand.PlayCard((m1 as HeartsMove).Play);
				Invalidate();
				Invoke(new DV(Update));

				Invoke(new text(set_text),"Hearts - Pass 2");

				Move m2 = GetNextMove();
				game.South.CardsInHand.PlayCard((m2 as HeartsMove).Play);
				Invalidate();
				Invoke(new DV(Update));

				Invoke(new text(set_text),"Hearts - Pass 1");

				Move m3 = GetNextMove();
				game.South.CardsInHand.PlayCard((m3 as HeartsMove).Play);
				Invalidate();
				Invoke(new DV(Update));

				Invoke(new text(set_text),"Hearts");

				List<Card> rand = new List<Card>(9);

				for(int i = 0;i < 12;i++)
					if(i / 3 != 2)
						rand.Add(game.GetPlayer(i / 3).CardsInHand.PlayCard(0)); // Cards are not sorted yet, for some reason

#region Unsightly Code
				switch(game.Round % 4)
				{
				case 1: // Left
					game.North.CardsInHand.DrawCard(rand[6]); // Take from west
					game.North.CardsInHand.DrawCard(rand[7]); // Take from west
					game.North.CardsInHand.DrawCard(rand[8]); // Take from west
					
					game.South.CardsInHand.DrawCard(rand[3]); // Take from east
					game.South.CardsInHand.DrawCard(rand[4]); // Take from east
					game.South.CardsInHand.DrawCard(rand[5]); // Take from east

					game.East.CardsInHand.DrawCard(rand[0]); // Take from north
					game.East.CardsInHand.DrawCard(rand[1]); // Take from north
					game.East.CardsInHand.DrawCard(rand[2]); // Take from north

					game.West.CardsInHand.DrawCard((m1 as HeartsMove).Play); // Take from south
					game.West.CardsInHand.DrawCard((m2 as HeartsMove).Play); // Take from south
					game.West.CardsInHand.DrawCard((m3 as HeartsMove).Play); // Take from south

					break;
				case 2: // Right
					game.North.CardsInHand.DrawCard(rand[3]); // Take from east
					game.North.CardsInHand.DrawCard(rand[4]); // Take from east
					game.North.CardsInHand.DrawCard(rand[5]); // Take from east
					
					game.South.CardsInHand.DrawCard(rand[6]); // Take from west
					game.South.CardsInHand.DrawCard(rand[7]); // Take from west
					game.South.CardsInHand.DrawCard(rand[8]); // Take from west

					game.East.CardsInHand.DrawCard((m1 as HeartsMove).Play); // Take from south
					game.East.CardsInHand.DrawCard((m2 as HeartsMove).Play); // Take from south
					game.East.CardsInHand.DrawCard((m3 as HeartsMove).Play); // Take from south

					game.West.CardsInHand.DrawCard(rand[0]); // Take from north
					game.West.CardsInHand.DrawCard(rand[1]); // Take from north
					game.West.CardsInHand.DrawCard(rand[2]); // Take from north

					break;
				case 3: // Across
					game.North.CardsInHand.DrawCard((m1 as HeartsMove).Play); // Take from south
					game.North.CardsInHand.DrawCard((m2 as HeartsMove).Play); // Take from south
					game.North.CardsInHand.DrawCard((m3 as HeartsMove).Play); // Take from south
					
					game.South.CardsInHand.DrawCard(rand[0]); // Take from north
					game.South.CardsInHand.DrawCard(rand[1]); // Take from north
					game.South.CardsInHand.DrawCard(rand[2]); // Take from north

					game.East.CardsInHand.DrawCard(rand[6]); // Take from west
					game.East.CardsInHand.DrawCard(rand[7]); // Take from west
					game.East.CardsInHand.DrawCard(rand[8]); // Take from west

					game.West.CardsInHand.DrawCard(rand[3]); // Take from east
					game.West.CardsInHand.DrawCard(rand[4]); // Take from east
					game.West.CardsInHand.DrawCard(rand[5]); // Take from east

					break;
				}
#endregion

				game.South.CardsInHand.SortBySuit();

				passing = false;
			}

			return;
		}

		protected void set_text(string txt)
		{
			Text = txt;
			return;
		}

		/// <summary>
		/// Fired when a round ends.
		/// </summary>
		/// <param name="state">The state of the game at the end of the round.</param>
		protected void EndRound(GameState state)
		{
			skip_trick = true;
			Invalidate();
			Invoke(new DV(Update));
			skip_trick = false;

			MessageBox.Show("North: " + game.North.Score + " East: " + game.East.Score + " South: " + game.South.Score + " West: " + game.West.Score,"Scores",MessageBoxButtons.OK);
			return;
		}

		/// <summary>
		/// An event fired when a game state reaches a finish state.
		/// </summary>
		/// <param name="state">The game state in its final state.</param>
		protected void Over(GameState state)
		{
			// We're done here
			if(this.InvokeRequired)
				Invoke(new dispose(Dispose));
			else
				Dispose();
			
			return;
		}

        private void Table_Dispose(object sender, EventArgs e)
        {
			t.Abort(); // Nowhere will we suspend this so no exceptions should occur
			t = null;
			
            ReturnTo.Show();
            return;
        }

		/// <summary>
		/// Handles card click events.
		/// </summary>
		/// <param name="index">The index of the card clicked.</param>
		protected void SelectCard(int index)
		{
			// If the current player is not south then don't even bother
			if(game.ActivePlayer != 2 && !passing)
				return;

			if(!DoubleClickSelect)
				selected_card = index; // Doing this allows use to do single clicking

			if(selected_card == index && selected_card >= 0 && selected_card < game.South.CardsInHand.CardsInHand && (passing || game.IsValid(new HeartsMove(game.South.CardsInHand.Cards[selected_card],game.ActivePlayer))))
				card_selected = true;

			if(DoubleClickSelect)
			{
				if(selected_card != -1)
					SouthHand[selected_card].BorderStyle = BorderStyle.None;

				selected_card = index; // Doing this allows use to do double clicking
				SouthHand[index].BorderStyle = BorderStyle.FixedSingle;
			}
			
			return;
		}

		/// <summary>
		/// Handles card double click events.
		/// </summary>
		/// <param name="index">The index of the card clicked.</param>
		protected void SelectCardD(int index)
		{
			// If the current player is not south then don't even bother
			if(game.ActivePlayer != 2 && !passing)
				return;

			selected_card = index;

			if(selected_card >= 0 && selected_card < game.South.CardsInHand.CardsInHand && (passing || game.IsValid(new HeartsMove(game.South.CardsInHand.Cards[selected_card],game.ActivePlayer))))
				card_selected = true;

			return;
		}

		/// <summary>
		/// The size of a card being drawn.
		/// The default is 72x96.
		/// </summary>
		private Tuple<float,float> CardSize
		{get; set;}

		/// <summary>
		/// The scaling of a card being drawn.
		/// </summary>
		private Tuple<float,float> CardScale
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

		protected delegate void dispose();
		protected delegate void DV();
		protected delegate void text(string txt);

        protected Form ReturnTo;
		protected volatile HeartsGameState game;
		protected Thread t;
		
		protected PictureBox[] SouthHand;

		protected bool[][] points_taken;

		protected bool card_selected;
		protected int selected_card;
		public bool DoubleClickSelect = true;

		protected bool passing = false;

		protected bool skip_trick = false;
    }
}
