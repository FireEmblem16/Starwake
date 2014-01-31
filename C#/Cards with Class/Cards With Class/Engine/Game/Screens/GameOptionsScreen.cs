using System;
using System.Windows.Forms;
using Networking;

namespace Engine.Game.Screens
{
	/// <summary>
	/// The abstract of an options screen.
	/// </summary>
	public abstract partial class GameOptionsScreen : Form
	{
#region Constructors
		/// <summary>
		/// Creates a new options screen.
		/// </summary>
		/// <param name="top">The window to return to if this window exits without hosting a game.</param>
		/// <param name="player">The seat the host wants.</param>
		/// <param name="name">The name of the local player.</param>
		protected GameOptionsScreen(Form top, int player, string name)
		{
			InitializeComponent();
			this.Disposed += new EventHandler(ExtendDispose);

			this.top = top;
			ThePlayer = player;
			LocalName = name;

			Initialize();
			return;
		}
#endregion

#region Helper Functions

#endregion

#region Windows Form Functions
		/// <summary>
		/// An event that is fired when the host game button is clicked.
		/// </summary>
		/// <param name="sender">The originator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void button1_Click(object sender, EventArgs e)
		{
			GetWaitingRoom().Show();
			unprovoked = false;

			if(InvokeRequired)
				Invoke(new DV(Dispose));
			else
				Dispose();

			return;
		}
#endregion

#region Event Handlers
		/// <summary>
		/// An event fired when this window is disposed.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void ExtendDispose(object sender, EventArgs e)
		{
			if(unprovoked)
				top.Show();

			CleanUp(unprovoked);

			// Drop the extra references
			top = null;

			return;
		}
#endregion

#region Abstract Functions
		/// <summary>
		/// Called after the window is initialized to perform further initialization.
		/// </summary>
		protected abstract void Initialize();

		/// <summary>
		/// Gets a waiting room to transfer to once a game is hosted.
		/// </summary>
		/// <returns>Returns a waiting room for this game type.</returns>
		protected abstract WaitingScreen GetWaitingRoom();

		/// <summary>
		/// Called when this window is disposed to perform any additional clean up.
		/// </summary>
		/// <param name="unprevoked">If true then no game was hosted.</param>
		protected abstract void CleanUp(bool unprevoked);
#endregion

#region Delegates
		/// <summary>
		/// A delegate that takes no parameters and returns nothing.
		/// </summary>
		private delegate void DV();
#endregion

#region Properties

#endregion

#region Member Variables
		/// <summary>
		/// The form to return to when this window exits without hosting a game.
		/// </summary>
		protected Form top
		{get; private set;}

		/// <summary>
		/// The seat of the local player.
		/// </summary>
		protected int ThePlayer
		{get; private set;}
		
		/// <summary>
		/// The name of the local player.
		/// </summary>
		protected string LocalName
		{get; private set;}

		/// <summary>
		/// If true then no game has been hosted.
		/// </summary>
		private bool unprovoked = true;
#endregion
	}
}
