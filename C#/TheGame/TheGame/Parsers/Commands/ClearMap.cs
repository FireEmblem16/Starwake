using System;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	public class ClearMap : Command
	{
		/// <summary>
		/// Creates a new clearmap command.
		/// </summary>
		/// <param name="source">The extra parameters to the clearmap command.</param>
		public ClearMap(StringTokenizer source)
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
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("clrmap",CreateClearMapCommand));
			return;
		}

		/// <summary>
		/// Creates a new clearmap command.
		/// </summary>
		/// <param name="source">The extra parameters to the clearmap command.</param>
		/// <returns>Returns a clearmap command.</returns>
		protected static Command CreateClearMapCommand(StringTokenizer source)
		{return new ClearMap(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(valid)
			{
				win.ClearBox(new Rectangle(0,0,state.Map.Width,state.Map.Height));
				state.Map = new Board(state.Map.Width,state.Map.Height);
				state.Map.DrawMapFogless(win,0,0);

				win.SetColor(ConsoleColor.White,ConsoleColor.Black);
				win.Write("The map has been cleared.",state.Output);
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
			win.Write("clrmap - Reverts the current map to an unbroken landscape of plains. The change is permanent; use it with caution.",Output);
			
			return;
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;
	}
}
