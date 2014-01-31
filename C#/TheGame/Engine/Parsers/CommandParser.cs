using System;
using System.Collections.Generic;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// Parses commands from user input.
	/// </summary>
	public class CommandParser
	{
		/// <summary>
		/// Creates a new command parser.
		/// </summary>
		/// <param name="delimiter">The delimiter to use for seperating tokens.</param>
		public CommandParser(string delimiter = " ")
		{
			Delim = delimiter;
			cmd_stack = new List<string>(Settings.CommandStackDepth);
			esp = -1;
			
			return;
		}

		/// <summary>
		/// Obtains the next command from the player.
		/// </summary>
		/// <param name="win">The window we will write and read to.</param>
		/// <param name="x">The x coordinate of where in the window we want to display input to.</param>
		/// <param name="y">The y coordinate of where in the window we want to display input to.</param>
		/// <param name="max_command_length">The maximum length that any command may have.</param>
		/// <returns>Returns a command parsed from the player's input or an invalid command if none could be found.</returns>
		public Command NextCommand(Window win, int x, int y, int max_command_length)
		{
			StringTokenizer tokens = new StringTokenizer(NextString(win,x,y,max_command_length),Delim);
			
			if(tokens.TokensLeft == 0)
				return new InvalidCommand();

			return CommandFactory.CreateCommand(tokens.NextToken(),tokens);
		}

		/// <summary>
		/// Obtains the next string from the player.
		/// </summary>
		/// <param name="win">The window we will write and read to.</param>
		/// <param name="x">The x coordinate of where in the window we want to display input to.</param>
		/// <param name="y">The y coordinate of where in the window we want to display input to.</param>
		/// <param name="max_string_length">The maximum length that the string may have.</param>
		/// <returns>Returns a string from the player's input.</returns>
		public string NextString(Window win, int x, int y, int max_string_length)
		{
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.CursorX = x;
			win.CursorY = y;

			string command = "";

			ConsoleKeyInfo next_key = Console.ReadKey(true);
			InputType key_type = DetermineInputType(next_key);

			while(key_type != InputType.TERMINATE_LINE)
			{
				HandleKey(win,ref command,x,y,max_string_length,next_key,key_type);

				next_key = Console.ReadKey(true);
				key_type = DetermineInputType(next_key);
			}

			// If our history is too big erase the last one
			if(cmd_stack.Count == Settings.CommandStackDepth)
				cmd_stack.RemoveAt(cmd_stack.Count - 1);

			// Add the current command to the history if it was not the last command
			if(cmd_stack.Count == 0 || cmd_stack[0] != command)
				cmd_stack.Insert(0,command);

			// Reset esp
			esp = -1;
			return command;
		}

		/// <summary>
		/// A helper function that handles hey input.
		/// </summary>
		protected void HandleKey(Window win, ref string command, int x, int y, int max_command_length, ConsoleKeyInfo next_key, InputType key_type)
		{
			if(key_type == InputType.TEXT && command.Length < max_command_length)
			{
				if(command.Length < max_command_length)
				{
					Pair<int,int> current = win.CursorPosition; // Save our current position so we can get back to it
					int len = win.GetStrokeDifference(new Pair<int,int>(x,y),current);

					if(len == 0)
						command = next_key.KeyChar + command;
					else if(len != command.Length)
						command = command.Substring(0,len) + next_key.KeyChar + command.Substring(len);
					else
						command += next_key.KeyChar;

					win.CursorPosition = new Pair<int,int>(x,y);
					win.Write(command);

					win.CursorPosition = current;
					win.CursorPosition = win.GetCursorPositionAfter(1);

					if(command.Length == max_command_length && win.CursorPosition.Equals(win.GetCursorPositionAfter(max_command_length,x,y)))
						win.CursorPosition = win.GetLastCursorPosition();
				}
			}
			else if(key_type == InputType.BACKSPACE)
			{
				if(command.Length == max_command_length && win.CursorPosition.Equals(win.GetCursorPositionAfter(max_command_length - 1,x,y))) // If we are at the end of the command and we have it maxed out
				{
					win.Write(" ");
					command = command.Substring(0,command.Length - 1);

					win.CursorPosition = win.GetLastCursorPosition();
				}
				else if(command.Length > 0) // We are somewhere in a non-empty string
				{
					Pair<int,int> current = win.CursorPosition; // Save our current position so we can get back to it
					int len = win.GetStrokeDifference(new Pair<int,int>(x,y),current);

					if(len > 0)
					{
						command = command.Substring(0,len - 1) + command.Substring(len);

						win.CursorPosition = new Pair<int,int>(x,y);
						win.Write(command + " ");
					
						win.CursorPosition = current;
						win.CursorPosition = win.GetLastCursorPosition();
					}
				}
			}
			else if(key_type == InputType.ARROW)
			{
				if(next_key.Key == ConsoleKey.LeftArrow && !win.CursorPosition.Equals(new Pair<int,int>(x,y)))
					win.CursorPosition = win.GetLastCursorPosition();
				else if(next_key.Key == ConsoleKey.RightArrow && !win.CursorPosition.Equals(win.GetCursorPositionAfter(max_command_length - 1,x,y)) && !win.CursorPosition.Equals(win.GetCursorPositionAfter(command.Length,x,y)))
					win.CursorPosition = win.GetCursorPositionAfter(1);
				else if(next_key.Key == ConsoleKey.UpArrow)
				{
					if(esp < Settings.CommandStackDepth - 1 && esp < cmd_stack.Count - 1)
						esp++;

					// Even if we are at the top of the stack we want to reset the command
					command = cmd_stack[esp];

					// Clear the old command in case it is longer than the new one
					ClearCommandField(win,max_command_length,x,y);

					// Write the historical command
					win.CursorPosition = new Pair<int,int>(x,y);
					win.Write(command);
					
					// Create an artificial end stroke so we can get to the end of the command
					HandleKey(win,ref command,x,y,max_command_length,new ConsoleKeyInfo((char)0x03,ConsoleKey.End,false,false,false),InputType.END);
				}
				else // Down Arrow
				{
					if(esp > -1)
						esp--;

					if(esp == -1)
						command = "";
					else
						command = cmd_stack[esp];

					// Clear the old command in case it is longer than the new one
					ClearCommandField(win,max_command_length,x,y);

					// Write the historical command
					win.CursorPosition = new Pair<int,int>(x,y);
					win.Write(command);
					
					// Create an artificial end stroke so we can get to the end of the command
					HandleKey(win,ref command,x,y,max_command_length,new ConsoleKeyInfo((char)0x03,ConsoleKey.End,false,false,false),InputType.END);
				}
			}
			else if(key_type == InputType.HOME)
				win.CursorPosition = new Pair<int,int>(x,y);
			else if(key_type == InputType.END)
				if(command.Length == max_command_length)
					win.CursorPosition = win.GetCursorPositionAfter(max_command_length - 1,x,y);
				else
					win.CursorPosition = win.GetCursorPositionAfter(command.Length,x,y);
			else if(key_type == InputType.AUTO_COMPLETE)
			{

			}

			return;
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
		/// Clears the command field of all input.
		/// </summary>
		/// <param name="win">The window we are writing on.</param>
		/// <param name="max_command_length">The maximum length of a command.</param>
		/// <param name="x">The x coordinate that commands start at.</param>
		/// <param name="y">The y coordinate that commands start at.</param>
		public void ClearCommandField(Window win, int max_command_length, int x, int y)
		{
			string clear = "";

			for(int i = 0;i < max_command_length;i++)
				clear += " ";

			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.CursorPosition = new Pair<int,int>(x,y);

			win.Write(clear);
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
		/// The delimiter to use for seperating tokens.
		/// </summary>
		public string Delim
		{get; set;}

		/// <summary>
		/// The history of command inputs.
		/// </summary>
		protected List<string> cmd_stack;

		/// <summary>
		/// The stack pointer.
		/// </summary>
		protected int esp;
	}
}
