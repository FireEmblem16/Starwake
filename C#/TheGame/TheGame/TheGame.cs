#pragma warning disable 0162

using System;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Parsers.Commands;
using TheGame.Utility;

namespace TheGame
{
	/// <summary>
	/// The main game class that runs all the game logic.
	/// </summary>
	public class TheGame
	{
		/// <summary>
		/// Creates a new game.
		/// </summary>
		public TheGame()
		{
			win = new Window("Game",200,70);
			input = new CommandParser();
			state = new GameState();

			GameMainLoop();
			return;
		}

		/// <summary>
		/// The main game loop.
		/// </summary>
		public void GameMainLoop()
		{
			win.CursorPosition = state.Output.Position;
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);

			win.Write("Welcome to the game. Type help for a list of available commands.",state.Output);
			state.Map.DrawMapFogless(win,0,0);

			while(true)
			{
				win.CursorPosition = new Pair<int,int>(0,50);
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);

				win.Write(state.UserName + ">");
				Command cmd = input.NextCommand(win,win.CursorX,win.CursorY,200);

				input.ClearCommandField(win,200,0,50);
				win.ClearBox(state.Output); // Behavior is poor when writing to the last position on the console so do not do that.
				
				if(!cmd.Execute(win,state))
					cmd.Man(win,state.Output);
			}
			
			return;
		}

		protected Window win;
		protected CommandParser input;
		protected GameState state;
	}
}
