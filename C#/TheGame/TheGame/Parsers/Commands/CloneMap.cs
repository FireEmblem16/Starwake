using System;
using System.IO;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	public class CloneMap : Command
	{
		/// <summary>
		/// Creates a new clonemap command.
		/// </summary>
		/// <param name="source">The extra parameters to the clonemap command.</param>
		public CloneMap(StringTokenizer source)
		{
			if(source.TokensLeft == 1)
			{
				value = source.NextToken();

				if(File.Exists(value + ".map"))
					valid = true;
			}

			return;
		}

		/// <summary>
		/// Registers this command with the command factory.
		/// </summary>
		public static void Initialize()
		{
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("clnmap",CreateCloneMapCommand));
			return;
		}

		/// <summary>
		/// Creates a new clonemap command.
		/// </summary>
		/// <param name="source">The extra parameters to the clonemap command.</param>
		/// <returns>Returns a clonemap command.</returns>
		protected static Command CreateCloneMapCommand(StringTokenizer source)
		{return new CloneMap(source);}

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

				win.ClearBox(new Rectangle(0,0,state.Map.Width,state.Map.Height));
				state.Map = new Board(value);
				state.Map.DrawMapFogless(win,0,0);

				win.SetColor(ConsoleColor.White,ConsoleColor.Black);
				win.Write(value + "'s map has been cloned.",state.Output);
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
			win.Write("clnmap <name> - Clones your map to the map owned by user <name>. You must be logged in to use this command.",Output);
			
			return;
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;

		/// <summary>
		/// The name of the user whose map we want to clone.
		/// </summary>
		protected string value = "";
	}
}
