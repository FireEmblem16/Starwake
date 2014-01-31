using System;
using System.Windows.Forms;

namespace Engine.Game.Screens
{
	public partial class ProgramOptionsScreen : Form
	{
		public ProgramOptionsScreen(Form top)
		{
			InitializeComponent();
			return_to = top;
			
			return;
		}

		protected override void OnFormClosing(FormClosingEventArgs e)
		{
			Hide();
			return_to.Show();

			e.Cancel = true;
			return;
		}

		protected Form return_to;
	}
}
