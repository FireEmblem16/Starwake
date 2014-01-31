using System;
using System.Windows.Forms;
using Networking;

namespace ExampleNetworkedGame
{
	public partial class HeartsOptions : Form
	{
		public HeartsOptions(Form top, NetworkStream nio, int player)
		{
			InitializeComponent();
			this.Disposed += new EventHandler(ExtendDispose);

			this.top = top;
			this.nio = nio;
			this.player = player;

			return;
		}

		private void ExtendDispose(object sender, EventArgs e)
		{
			if(unprovoked)
			{
				top.Show();
				nio.Close();
			}

			// Drop the extra references
			nio = null;
			top = null;

			return;
		}

		private void button1_Click(object sender, EventArgs e)
		{
			// We need to go to the joining screen (host version)
			new HeartsWaitingScreen(top,nio,player,true,checkBox1.Checked).Show();
			unprovoked = false;

			if(InvokeRequired)
				Invoke(new DV(Dispose));
			else
				Dispose();

			return;
		}

		protected delegate void DV();

		protected NetworkStream nio;
		protected int player;
		protected Form top;

		protected bool unprovoked = true;
	}
}
