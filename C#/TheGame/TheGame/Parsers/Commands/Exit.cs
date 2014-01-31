using System;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// The exit command which terminates the program immediately.
	/// </summary>
	public class Exit : Command
	{
		/// <summary>
		/// Creates a exit command for help.
		/// </summary>
		public Exit()
		{
			valid = true;
			return;
		}

		/// <summary>
		/// Creates a new exit command.
		/// </summary>
		/// <param name="source">The extra parameters to the exit command.</param>
		public Exit(StringTokenizer source)
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
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("exit",CreateExitCommand));
			return;
		}

		/// <summary>
		/// Creates a new exit command.
		/// </summary>
		/// <param name="source">The extra parameters to the exit command.</param>
		/// <returns>Returns a exit command.</returns>
		protected static Command CreateExitCommand(StringTokenizer source)
		{return new Exit(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(valid)
			{
				// Logout the user before terminating
				if(state.LoggedIn)
					new Logout().Execute(win,state);

				Environment.Exit(0);
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
			win.Write("exit - Terminates the game.",Output);

			return;
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;
	}
}
