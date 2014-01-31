using System;
using System.Windows.Forms;

namespace Engine.Game.Screens
{
	/// <summary>
	/// Informs players about our glorious work.
	/// </summary>
	public partial class AboutScreen : Form
	{
		/// <summary>
		/// Creates a new about window.
		/// </summary>
		public AboutScreen()
		{
			InitializeComponent();
			return;
		}

		/// <summary>
		/// Exits the form.
		/// </summary>
		/// <param name="sender">The initiator of the event.</param>
		/// <param name="e">The event arguments.</param>
		private void button1_Click(object sender, EventArgs e)
		{
			Hide();
			return;
		}
	}
}
