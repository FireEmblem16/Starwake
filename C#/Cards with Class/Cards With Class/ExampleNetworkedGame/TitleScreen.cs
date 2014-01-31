using System;
using System.Windows.Forms;
using Networking;
using System.Collections.Generic;
using System.Net;

namespace ExampleNetworkedGame
{
	public partial class Form1 : Form
	{
		public Form1()
		{
			InitializeComponent();
			domainUpDown1.SelectedIndex = 0;

			return;
		}

		/// <summary>
		/// Button click for hosting a new game.
		/// The chair the host wants to take is in the spinner.
		/// </summary>
		/// <param name="sender">The originator of the click event.</param>
		/// <param name="e">The event.</param>
		private void button1_Click(object sender, EventArgs e)
		{
			Hide();

			// Make a network stream with no connections
			NetworkStream nio = new SymetricNetworkStream(false);

            // Go to the options screen
			new HeartsOptions(this,nio,domainUpDown1.SelectedIndex).Show();

			return;
		}

		/// <summary>
		/// Button click for joining a game at the address given in the text box above.
		/// The chair the user wants to take is in the spinner.
		/// </summary>
		/// <param name="sender">The originator of the click event.</param>
		/// <param name="e">The event.</param>
		private void button2_Click(object sender, EventArgs e)
		{
			Hide();

			// Make a network stream and connect it to the host (host address is in the textbox)
			NetworkStream nio = new SymetricNetworkStream(true);
			
			// Connect to the given address
			List<IPAddress> connect_to = new List<IPAddress>();
            connect_to.Add(IPAddress.Parse(textBox1.Text));
            nio.Connect(connect_to.ToArray());

			// We need to go to the joining screen (client version)
			new HeartsWaitingScreen(this,nio,domainUpDown1.SelectedIndex).Show();

			return;
		}
	}
}
