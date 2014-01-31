using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using TheGame.Utility;
using TheGame.Graphics;
using TheGame.Engine;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// Explains the rules of the game.
	/// </summary>
	public class Rules : Command
	{
		/// <summary>
		/// Creates a new rules command.
		/// </summary>
		/// <param name="source">The extra parameters to the rules command.</param>
		public Rules(StringTokenizer source)
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
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("rules",CreateRulesCommand));
			return;
		}

		/// <summary>
		/// Creates a new rules command.
		/// </summary>
		/// <param name="source">The extra parameters to the rules command.</param>
		/// <returns>Returns a rules command.</returns>
		protected static Command CreateRulesCommand(StringTokenizer source)
		{return new Rules(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(!valid)
				return false;

			// Clear the window for reading
			win.Clear();

			// Hide the cursor
			bool visible = Console.CursorVisible;
			Console.CursorVisible = false;

			// Write page 1
			win.CursorPosition = new Pair<int,int>(0,0);
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);

			win.Write("Hello World!");

			// Wait for key input to advance
			WaitForInput(ConsoleKey.Enter);

			// Write page 2
			win.CursorPosition = new Pair<int,int>(0,0);
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);

			win.Write("How are you gentleman?");

			// Wait for key input to finish
			WaitForInput(ConsoleKey.Enter);

			// Restore cursor visibility status
			Console.CursorVisible = visible;

			// Redraw the map
			state.Map.DrawMapFogless(win,0,0);

			return true;
		}

		/// <summary>
		/// Waits until input is provided.
		/// </summary>
		protected void WaitForInput()
		{
			Console.ReadKey(true);
			return;
		}

		/// <summary>
		/// Waits until specific input is provided.
		/// </summary>
		/// <param name="key">The key to wait for.</param>
		protected void WaitForInput(ConsoleKey key)
		{
			while(Console.ReadKey(true).Key != key);
			return;
		}

		/// <summary>
		/// Waits until specific input is provided.
		/// </summary>
		/// <param name="key">The keys to wait for.</param>
		protected void WaitForInput(List<ConsoleKey> keys)
		{
			while(!keys.Contains(Console.ReadKey(true).Key));
			return;
		}

		/// <summary>
		/// Print the manual entry for this command.
		/// </summary>
		/// <param name="win">The window to write on.</param>
		/// <param name="Output">The area to write in.</param>
		public void Man(Window win, Rectangle Output)
		{
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.Write("rules - Displays the rules of the game in detail.",Output);

			return;
		}

		/// <summary>
		/// True if the command is valid.
		/// </summary>
		protected bool valid;
	}
}
