using System;
using System.Windows.Forms;
using Engine.Game.Screens;
using HeartsGame.Code;
using Networking;

namespace HeartsGame
{
	public partial class HeartsOptions : GameOptionsScreen
	{
		/// <summary>
		/// Creates a new game options screen for hearts.
		/// </summary>
		/// <param name="top">The window to return to if this window exits without hosting a game.</param>
		/// <param name="player">The seat the host wants.</param>
		/// <param name="name">The name of the local player.</param>
		public HeartsOptions(Form top, int player, string name) : base(top,player,name)
		{return;}

		/// <summary>
		/// Called after the window is initialized to perform further initialization.
		/// </summary>
		protected override void Initialize()
		{
			hundred_reset = new CheckBox();
			hundred_reset.AutoSize = true;
			hundred_reset.Checked = false;
			hundred_reset.Location = new System.Drawing.Point(10,10);
			hundred_reset.Name = "checkBox1";
			hundred_reset.Size = new System.Drawing.Size(102,17);
			hundred_reset.TabIndex = 1;
			hundred_reset.Text = "100 Point Reset";
			hundred_reset.UseVisualStyleBackColor = true;
			groupBox1.Controls.Add(hundred_reset);

			return;
		}

		/// <summary>
		/// Gets a waiting room to transfer to once a game is hosted.
		/// </summary>
		/// <returns>Returns a waiting room for this game type.</returns>
		protected override WaitingScreen GetWaitingRoom()
		{return new HeartsData().WaitingRoom(top,new SymetricNetworkStream(false),ThePlayer,LocalName,true,hundred_reset.Checked);}

		/// <summary>
		/// Called when this window is disposed to perform any additional clean up.
		/// </summary>
		/// <param name="unprevoked">If true then no game was hosted.</param>
		protected override void CleanUp(bool unprevoked)
		{
			hundred_reset = null;
			return;
		}

		/// <summary>
		/// If checked then the hundred point reset rule is in effect.
		/// </summary>
		protected CheckBox hundred_reset;
	}
}
