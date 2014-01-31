using System;
using System.Windows.Forms;
using Engine.Game.Screens;

namespace CardWithClass
{
	static class Program
	{
		/// <summary>
		/// The main entry point for the application.
		/// </summary>
		[STAThread]
		static void Main()
		{
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new TitleScreen());
		}
	}
}
