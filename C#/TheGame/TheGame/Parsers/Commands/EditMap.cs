using System;
using System.Collections.Generic;
using TheGame.Engine;
using TheGame.Engine.Tiles;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// A command that allows users to edit their map.
	/// </summary>
	public class EditMap : Command
	{
		/// <summary>
		/// Creates a new editmap command.
		/// </summary>
		public EditMap()
		{
			valid = true;
			return;
		}

		/// <summary>
		/// Creates a new editmap command.
		/// </summary>
		/// <param name="source">The extra parameters to the editmap command.</param>
		public EditMap(StringTokenizer source)
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
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("edtmap",CreateEditMapCommand));
			return;
		}

		/// <summary>
		/// Creates a new editmap command.
		/// </summary>
		/// <param name="source">The extra parameters to the editmap command.</param>
		/// <returns>Returns a editmap command.</returns>
		protected static Command CreateEditMapCommand(StringTokenizer source)
		{return new EditMap(source);}

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

				win.SetColor(ConsoleColor.White,ConsoleColor.Black);
				win.Write("Use the arrow keys to move around the map and press enter to modify the map. Press escape to leave edit mode.",state.Output);

				bool old_cursor_visible = Console.CursorVisible;
				Console.CursorVisible = false;

				// Move the cursor and change it to map mode
				Pair<string,Pair<ConsoleColor,ConsoleColor>> cursor_foreground = state.Map.GetTile(0,0).GetImage(false);
				
				win.CursorPosition = new Pair<int,int>(0,0);
				win.SetColor(cursor_foreground.val2.val1,ConsoleColor.Magenta);
				
				win.Write(cursor_foreground.val1);
				win.CursorPosition = win.GetLastCursorPosition(new Rectangle(0,0,state.Map.Width,state.Map.Height));

				// Get the first key input
				ConsoleKeyInfo next_key = Console.ReadKey(true);
				InputType key_type = DetermineInputType(next_key);
				
				// Create a menu for selecting map terrain types
				Menu m = new Menu("Select a Terrain",TileFactory.Tiles(),0,10);

				// Edit the map until we get the escape sequence
				while(key_type != InputType.ESCAPE)
				{
					if(key_type == InputType.ARROW)
					{
						state.Map.GetTile(win.CursorX,win.CursorY).Draw(win,win.CursorX,win.CursorY,false);
						win.CursorPosition = win.GetLastCursorPosition(new Rectangle(0,0,state.Map.Width,state.Map.Height));

						if(next_key.Key == ConsoleKey.UpArrow)
							if(win.CursorY == 0)
								win.CursorY = state.Map.Height - 1;
							else
								win.CursorY--;
						else if(next_key.Key == ConsoleKey.DownArrow)
							if(win.CursorY == state.Map.Height - 1)
								win.CursorY = 0;
							else
								win.CursorY++;
						else if(next_key.Key == ConsoleKey.LeftArrow)
							if(win.CursorX == 0)
								win.CursorX = state.Map.Width - 1;
							else
								win.CursorX--;
						else
							if(win.CursorX == state.Map.Width - 1)
								win.CursorX = 0;
							else
								win.CursorX++;

						cursor_foreground = state.Map.GetTile(win.CursorX,win.CursorY).GetImage(false);

						win.SetColor(cursor_foreground.val2.val1,ConsoleColor.Magenta);
						win.Write(cursor_foreground.val1);

						win.CursorPosition = win.GetLastCursorPosition(new Rectangle(0,0,state.Map.Width,state.Map.Height));
					}
					else if(key_type == InputType.TERMINATE_LINE)
					{
						Pair<int,int> cursor_loc = win.CursorPosition;
						
						int index = m.Display(win,1,state.Map.Height + 1);
						win.CursorPosition = cursor_loc;

						if(index != -1)
						{
							state.Map.SetTile(win.CursorX,win.CursorY,TileFactory.GenerateTile(m.Items[index]));
							cursor_foreground = state.Map.GetTile(win.CursorX,win.CursorY).GetImage(false);

							win.SetColor(cursor_foreground.val2.val2,ConsoleColor.Magenta);
							win.Write(cursor_foreground.val1);

							win.CursorPosition = win.GetLastCursorPosition(new Rectangle(0,0,state.Map.Width,state.Map.Height));
						}
					}

					// Get the next key
					next_key = Console.ReadKey(true);
					key_type = DetermineInputType(next_key);
				}

				// Preform cleanup on the cursor
				Console.CursorVisible = old_cursor_visible;
				state.Map.GetTile(win.CursorX,win.CursorY).Draw(win,win.CursorX,win.CursorY,false);

				// Save the map just to be safe
				state.Map.SaveMap(state.UserName);
			}
			else
				return false;

			return true;
		}

		/// <summary>
		/// Determines the input type of the provided key stroke.
		/// </summary>
		/// <param name="key">The key stroke to consider.</param>
		/// <returns>Returns the type of input that the key stroke is.</returns>
		protected InputType DetermineInputType(ConsoleKeyInfo key)
		{
			if((key.Modifiers & (ConsoleModifiers.Control | ConsoleModifiers.Alt)) != 0)
				return InputType.COMMAND;

			if(key.Key == ConsoleKey.UpArrow || key.Key == ConsoleKey.DownArrow || key.Key == ConsoleKey.LeftArrow || key.Key == ConsoleKey.RightArrow)
				return InputType.ARROW;

			if(key.Key == ConsoleKey.Home)
				return InputType.HOME;

			if(key.Key == ConsoleKey.End)
				return InputType.END;

			if(key.KeyChar == '\u0000')
				return InputType.OTHER;

			if(key.Key == ConsoleKey.Tab)
				return InputType.AUTO_COMPLETE;

			if(key.Key == ConsoleKey.Backspace)
				return InputType.BACKSPACE;

			if(key.Key == ConsoleKey.Enter)
				return InputType.TERMINATE_LINE;

			if(key.Key == ConsoleKey.Escape)
				return InputType.ESCAPE;

			return InputType.TEXT;
		}

		/// <summary>
		/// Print the manual entry for this command.
		/// </summary>
		/// <param name="win">The window to write on.</param>
		/// <param name="Output">The area to write in.</param>
		public void Man(Window win, Rectangle Output)
		{
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.Write("edtmap - Allows the current user to edit their map. The user must be logged in to edit the map.",Output);

			return;
		}

		/// <summary>
		/// Represents the type of key that is a character represents.
		/// </summary>
		protected enum InputType
		{
			COMMAND = 0,
			TEXT = 1,
			BACKSPACE = 2,
			AUTO_COMPLETE = 3,
			TERMINATE_LINE = 4,
			ARROW = 5,
			ESCAPE = 6,
			HOME = 7,
			END = 8,
			OTHER = 9
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;
	}
}
