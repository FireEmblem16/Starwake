using System;
using System.Windows.Forms;

namespace Engine.Game.Screens
{
	/// <summary>
	/// A title screen for the game Cards with Class.
	/// </summary>
	public partial class TitleScreen : Form
	{
#region Constructors
		/// <summary>
		/// Creates a new title screen.
		/// </summary>
		/// <param name="selector">The window that allows the player to select a game to play. The default window is used if null is provided.</param>
		public TitleScreen(Form selector = null, Form options = null, Form about = null)
		{
			InitializeComponent();

			if(selector == null)
				SelectScreen = new GameSelectScreen(this);
			else
				SelectScreen = selector;

			if(options == null)
				OptionsScreen = new ProgramOptionsScreen(this);
			else
				OptionsScreen = options;

			if(about == null)
				AboutScreen = new AboutScreen();
			else
				AboutScreen = about;

			return;
		}
#endregion

#region Helper Functions

#endregion

#region Windows Form Functions
		/// <summary>
		/// Triggered when the play button is selected.
		/// </summary>
		/// <param name="sender">The sender of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void button1_Click(object sender, EventArgs e)
		{
			Hide();
			SelectScreen.Show();

			return;
		}

		/// <summary>
		/// Triggered when the options button is selected.
		/// </summary>
		/// <param name="sender">The sender of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void button2_Click(object sender, EventArgs e)
		{
			Hide();
			OptionsScreen.Show();

			return;
		}

		/// <summary>
		/// Triggered when the about button is selected.
		/// </summary>
		/// <param name="sender">The sender of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void button3_Click(object sender, EventArgs e)
		{
			AboutScreen.ShowDialog();
			return;
		}
#endregion

#region Event Handlers

#endregion

#region Abstract Functions

#endregion

#region Delegates

#endregion

#region Properties

#endregion

#region Member Variables
		/// <summary>
		/// The screen that allows players to select games.
		/// </summary>
		protected Form SelectScreen;

		/// <summary>
		/// The screen that allows players to change program options.
		/// </summary>
		protected Form OptionsScreen;

		/// <summary>
		/// The screen that allows players to bask in the glory of the designers.
		/// </summary>
		protected Form AboutScreen;
#endregion
	}
}
