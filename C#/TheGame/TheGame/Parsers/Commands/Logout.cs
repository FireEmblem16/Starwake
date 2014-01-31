using System;
using System.IO;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	public class Logout : Command
	{
		/// <summary>
		/// Creates a new logout command.
		/// </summary>
		public Logout()
		{
			valid = true;
			return;
		}

		/// <summary>
		/// Creates a new logout command.
		/// </summary>
		/// <param name="source">The extra parameters to the logout command.</param>
		public Logout(StringTokenizer source)
		{
			if(source.TokensLeft == 0)
				valid = true;

			return;
		}

		/// <summary>
		/// Registers this command with the command factory.
		/// </summary>
		public static void Initialize()
		{
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("logout",CreateLogoutCommand));
			return;
		}

		/// <summary>
		/// Creates a new logout command.
		/// </summary>
		/// <param name="source">The extra parameters to the logout command.</param>
		/// <returns>Returns a logout command.</returns>
		protected static Command CreateLogoutCommand(StringTokenizer source)
		{return new Logout(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(valid)
			{
				if(!state.LoggedIn)
					return false;

				using(StreamWriter fout = new StreamWriter("units" + Path.DirectorySeparatorChar + state.UserName + ".units"))
					foreach(Unit u in state.UserUnits)
						fout.Write("UNIT START\n" + u.ToString() + "\nUNIT END\n");
				
				state.Map.SaveMap(state.UserName);
				state.UserName = "";

				win.SetColor(ConsoleColor.White,ConsoleColor.Black);
				win.Write("Logout successful.",state.Output);
			}
			else
				return false;

			return true;
		}

		/// <summary>
		/// Print the manual entry for this command.
		/// </summary>
		/// <param name="win">The window to write on.</param>
		/// <param name="Output">The area to write in.</param>
		public void Man(Window win, Rectangle Output)
		{
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.Write("logout - Logs out the current user.",Output);
			
			return;
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;
	}
}
