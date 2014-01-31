using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Reflection;
using System.Windows.Forms;
using Networking;

namespace Engine.Game.Screens
{
	/// <summary>
	/// A window that allows games to be selected.
	/// </summary>
	public partial class GameSelectScreen : Form
	{
		/// <summary>
		/// Creates a new window that allows games to be selected.
		/// </summary>
		/// <param name="top">The form to return to if a game is not started.</param>
		public GameSelectScreen(Form top)
		{
			InitializeComponent();
			LoadGames();

			return_to = top;
			return;
		}

		/// <summary>
		/// Loads every game we can find.
		/// </summary>
		private void LoadGames()
		{
			games = new List<GameData>();

			// If we don't have a mods folder then create it
			if(!Directory.Exists("mods"))
				Directory.CreateDirectory("mods");

			string[] modules = Directory.GetFiles("mods","*.dll",SearchOption.AllDirectories);
			
			foreach(string mod in modules)
				try
				{InitializeAssembly(Assembly.LoadFrom(mod));}
				catch
				{}

			// Sort the games by name
			games.Sort((data1,data2) => {return data1.GameName.CompareTo(data2.GameName);});

			SetupDisplay();
			return;
		}

		/// <summary>
		/// Attempts to call the static initialization function on every type in the given assembly.
		/// </summary>
		/// <param name="asm">The assembly to initialize.</param>
		private void InitializeAssembly(Assembly asm)
		{
			Type[] types = asm.GetTypes();
			
			foreach(Type t in types)
			{
				// We are only looking for GameData classes
				Type[] interfaces = t.GetInterfaces();
				bool found = false;

				foreach(Type t2 in interfaces)
					if(t2 == typeof(GameData))
					{
						found = true;
						break;
					}

				if(!found)
					continue;

				ConstructorInfo func = t.GetConstructor(System.Type.EmptyTypes);

				if(func != null)
					games.Add(func.Invoke(null) as GameData);
			}

			return;
		}

		/// <summary>
		/// Sets the display to reflect the game types we've loaded.
		/// </summary>
		private void SetupDisplay()
		{
			games_comboBox.Items.Clear();

			for(int i = 0;i < games.Count;i++)
				games_comboBox.Items.Add(games[i].GameName);

			if(games.Count > 0)
				games_comboBox.SelectedIndex = 0;
			
			return;
		}

		/// <summary>
		/// Called when a new game type is selected.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void games_comboBox_SelectedIndexChanged(object sender, EventArgs e)
		{
			description_richTextBox.Text = games[games_comboBox.SelectedIndex].Description;
			min_players.Text = games[games_comboBox.SelectedIndex].MinPlayers.ToString();
			max_players.Text = games[games_comboBox.SelectedIndex].MaxPlayers.ToString();
			expected_time.Text = games[games_comboBox.SelectedIndex].ExpectedTime.ToString();
			
			graphic.Image = games[games_comboBox.SelectedIndex].Icon;
			graphic.SizeMode = PictureBoxSizeMode.StretchImage;

			seat_comboBox.Items.Clear();

			for(int i = 0;i < games[games_comboBox.SelectedIndex].MaxPlayers;i++)
				seat_comboBox.Items.Add("Seat " + (i + 1));

			if(seat_comboBox.Items.Count > 0)
				seat_comboBox.SelectedIndex = 0;
			
			return;
		}

		/// <summary>
		/// Refreshes the list of available games.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void refresh_button_Click(object sender, EventArgs e)
		{
			LoadGames();
			return;
		}

		/// <summary>
		/// Hosts a new game of the selected game type.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void host_button_Click(object sender, EventArgs e)
		{
			unprevoked = false;
			Hide();
			
			games[games_comboBox.SelectedIndex].Options(this,0,"The Host").Show();
			return;
		}

		/// <summary>
		/// Starts up the server browser window to browse available games.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void server_button_Click(object sender, EventArgs e)
		{


			return;
		}

		/// <summary>
		/// Joins a game at the address specified in the address textbox.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void direct_button_Click(object sender,EventArgs e)
		{
			unprevoked = false;
			Hide();

			NetworkStream nio = new SymetricNetworkStream(true);

			List<IPAddress> connect_to = new List<IPAddress>();
            connect_to.Add(IPAddress.Parse(ip_textBox.Text));
            nio.Connect(connect_to.ToArray());

			games[games_comboBox.SelectedIndex].WaitingRoom(this,nio,seat_comboBox.SelectedIndex,name_textBox.Text == "" ? "Anonymous" + new Random().Next() : name_textBox.Text,false).Show();
			return;
		}

		/// <summary>
		/// Makes the x button go back instead of disposing.
		/// </summary>
		/// <param name="e">The event arguments.</param>
		protected override void OnFormClosing(FormClosingEventArgs e)
		{
			if(unprevoked)
			{
				Hide();
				return_to.Show();
			}

			e.Cancel = true;
			unprevoked = true;
			return;
		}

		/// <summary>
		/// The list of games that we've found.
		/// </summary>
		private List<GameData> games;

		/// <summary>
		/// The window to return to if no game is selected to start.
		/// </summary>
		private Form return_to;

		/// <summary>
		/// If true then we want to go back to the previous window.
		/// </summary>
		private bool unprevoked = true;
	}
}
