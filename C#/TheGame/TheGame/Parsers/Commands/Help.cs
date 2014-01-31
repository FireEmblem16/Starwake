using System;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// Displays help information.
	/// </summary>
	public class Help : Command
	{
		/// <summary>
		/// Creates a help command for help.
		/// </summary>
		public Help()
		{return;}

		/// <summary>
		/// Creates a new help command.
		/// </summary>
		/// <param name="source">The extra parameters to the help command.</param>
		public Help(StringTokenizer source)
		{
			if(source.TokensLeft == 1)
				cmd = source.NextToken();

			return;
		}

		/// <summary>
		/// Registers this command with the command factory.
		/// </summary>
		public static void Initialize()
		{
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("help",CreateHelpCommand));
			return;
		}

		/// <summary>
		/// Creates a new help command.
		/// </summary>
		/// <param name="source">The extra parameters to the help command.</param>
		/// <returns>Returns a help command.</returns>
		protected static Command CreateHelpCommand(StringTokenizer source)
		{return new Help(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(cmd == "")
			{
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);
				win.Write("Available Commands - " + CommandFactory.CommandList(),state.Output);
			}
			else
			{
				Command c = CommandFactory.CreateCommand(cmd,new StringTokenizer(""));

				if(c.GetType() == typeof(InvalidCommand))
					return false;
				else
					c.Man(win,state.Output);
			}

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
			win.Write("help <command> - Displays help information about <command> or lists the available command if no command is given.",Output);

			return;
		}

		/// <summary>
		/// The command we want help for.
		/// </summary>
		protected string cmd = "";
	}
}
