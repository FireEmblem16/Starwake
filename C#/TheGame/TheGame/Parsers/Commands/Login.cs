using System;
using System.Collections.Generic;
using System.IO;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	public class Login : Command
	{
		/// <summary>
		/// Creates a new login command.
		/// </summary>
		/// <param name="source">The extra parameters to the login command.</param>
		public Login(StringTokenizer source)
		{
			if(source.TokensLeft == 1)
			{
				value = source.NextToken();

				if(value.Length <= Settings.MaxNameLength)
					valid = true;
			}

			return;
		}

		/// <summary>
		/// Registers this command with the command factory.
		/// </summary>
		public static void Initialize()
		{
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("login",CreateLoginCommand));
			return;
		}

		/// <summary>
		/// Creates a new login command.
		/// </summary>
		/// <param name="source">The extra parameters to the login command.</param>
		/// <returns>Returns a login command.</returns>
		protected static Command CreateLoginCommand(StringTokenizer source)
		{return new Login(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(valid)
			{
				// If we are logged in first logout the old account
				if(state.LoggedIn)
					new Logout().Execute(win,state);

				state.UserName = value;

				win.ClearBox(new Rectangle(0,0,state.Map.Width,state.Map.Height));
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);

				try
				{
					state.Map = new Board(state.UserName);
					win.Write("Login successful: Username now " + value + ".",state.Output);
				}
				catch
				{
					state.Map = new Board(30,15);
					win.Write(state.UserName + "\'s map has been corrupted. A new map has been created.",state.Output);
				}

				// Save the map to the file just in case we started a new one
				state.Map.SaveMap(state.UserName);

				// The units file may become corrupted but it will never throw errors nor will we notice
				state.UserUnits.Clear();

				if(File.Exists("units" + Path.DirectorySeparatorChar + state.UserName + ".units"))
					using(StreamReader fin = new StreamReader("units" + Path.DirectorySeparatorChar + state.UserName + ".units"))
						while(!fin.EndOfStream)
						{
							while(fin.ReadLine() != "UNIT START");

							string unit = "";
							string temp = fin.ReadLine();

							while(temp != "UNIT END")
							{
								unit += temp + "\n";
								temp = fin.ReadLine();
							}

							state.UserUnits.Add(new Unit(new StringTokenizer(unit.Substring(0,unit.Length - 1),"\n")));
						}
				else
					File.Create("units" + Path.DirectorySeparatorChar + state.UserName + ".units").Close(); // Logout will get it later but this is more safe overall for the program (specifically other commands)

				state.UserUnits.Sort(comp); // Units were probably sorted in the file and we could do it faster sorting as we go but this simpler and we don't really care about performance here
				state.Map.DrawMapFogless(win,0,0);
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
			win.Write("login <name> - Sets your username to <name>. The username must be " + Settings.MaxNameLength + " characters or less.",Output);

			return;
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;

		/// <summary>
		/// The name to login as.
		/// </summary>
		protected string value = "";

		/// <summary>
		/// So that we don't have to keep making them and discarding them.
		/// </summary>
		protected static UnitComparer comp = new UnitComparer();

		/// <summary>
		/// Compares units.
		/// </summary>
		protected class UnitComparer : IComparer<Unit>
		{
			public int Compare(Unit x,Unit y)
			{
				return x.Name.CompareTo(y.Name);
			}
		}
	}
}
