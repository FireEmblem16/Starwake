using System;
using System.Net;
using System.Threading;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Networking;
using TheGame.Utility;
using System.IO;
using System.Collections.Generic;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// Hosts a game.
	/// </summary>
	public class Host : Command
	{
		/// <summary>
		/// Creates a new host command.
		/// </summary>
		/// <param name="source">The extra parameters to the host command.</param>
		public Host(StringTokenizer source)
		{
			if(source.TokensLeft == 0)
				valid = true;

			return;
		}

		/// <summary>
		/// Registers this command with the host factory.
		/// </summary>
		public static void Initialize()
		{
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("host",CreateHostCommand));
			return;
		}

		/// <summary>
		/// Creates a new host command.
		/// </summary>
		/// <param name="source">The extra parameters to the host command.</param>
		/// <returns>Returns a host command.</returns>
		protected static Command CreateHostCommand(StringTokenizer source)
		{return new Host(source);}

		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		public bool Execute(Window win, GameState state)
		{
			if(valid)
			{
				// Must be loggin in
				if(!state.LoggedIn)
					return false;

				// Make sure we have a unit with leadership
				bool leader = false;

				foreach(Unit u in state.UserUnits)
					if(u.HasAbility(typeof(Engine.Abilities.PassiveAbilities.DamageModifier.Leadership)))
					{
						leader = true;
						break;
					}

				if(!leader)
					return false;

				NetworkCommunicator nio = new NetworkCommunicator(IPAddress.None);
				
				// Wait for a game
				nio.WaitFor(str => {if(str == "soineslksasfahslfhldsalfksjd") return true; return false;});
				
				// Get who we are talking to
				nio.DefaultDestination = nio.NextPacket().Source.Address;

				// Tell them we accept
				nio.Write("asldfkjaslkefjslkjsestslo");

				// Send our units and map file
				nio.Write("alskdfnasoifnlsdnknfasdf" + File.ReadAllText("units" + Path.DirectorySeparatorChar + state.UserName + ".units"));
				nio.Write("weorijsldksoiefjslkdjfls" + File.ReadAllText("maps" + Path.DirectorySeparatorChar + state.UserName + ".map"));

				// Accept opposing units and map file
				nio.WaitFor(str => {if(str.Substring(0,24) == "alskdfnasoifnlsdnknfasdf") return true; return false;});
				File.WriteAllText("units" + Path.DirectorySeparatorChar + "askndfosnfzlknelfknlknsakfnsdknslkdfn.units",nio.NextPacket().Data.Substring(24));

				nio.WaitFor(str => {if(str.Substring(0,24) == "weorijsldksoiefjslkdjfls") return true; return false;});
				File.WriteAllText("units" + Path.DirectorySeparatorChar + "askndfosnfzlknelfknlknsakfnsdknslkdfn.map",nio.NextPacket().Data.Substring(24));

				// Load opposing units
				List<Unit> enemy_units = new List<Unit>();

				using(StreamReader fin = new StreamReader("units" + Path.DirectorySeparatorChar + "askndfosnfzlknelfknlknsakfnsdknslkdfn.units"))
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

						enemy_units.Add(new Unit(new StringTokenizer(unit.Substring(0,unit.Length - 1),"\n")));
					}

				enemy_units.Sort((u1, u2) => u1.Name.CompareTo(u2.Name));

				// Have the user place units (use seperate function)
				List<Pair<Pair<int,int>,string>> units = PlaceUnits(win,state);

				// Create full board
				state.Map = new Board(state.Map,new Board("askndfosnfzlknelfknlknsakfnsdknslkdfn"));

				// Send our unit placement
				string send = "lskndvaoisenlkdsf";

				foreach(Pair<Pair<int,int>,string> p in units)
					send += "@" + p.val1.val1.ToString() + "@" + p.val1.val2.ToString() + "@" + p.val2;

				nio.Write(send);

				// Get opposing unit placement
				nio.WaitFor(str => {if(str.Substring(0,17) == "lskndvaoisenlkdsf") return true; return false;});
				Scanner placement = new Scanner(nio.NextPacket().Data.Substring(17),new string[] {"@"});

				while(placement.HasNextItem)
				{
					int x = placement.Next<int>();
					int y = placement.Next<int>();
					string name = placement.Next<string>();

					state.Map.AddEnemyAt(enemy_units[enemy_units.BinarySearch(new Unit(name,0))],new Pair<int,int>(x,y));
				}

				// Create and send random seed and create RND in state
				int seed = DateTime.Now.Millisecond;
				state.rand = new Random(seed);
				
				nio.Write("asdkflskjdfaosiejflxkdf;oaij9alksjdfi" + seed.ToString());

				// Make sure the map is drawn
				state.Map.RecalculateSight();
				state.Map.DrawMap(win,0,0);

				// Do game logic (in seperate function)
				GameLogic(win,state,nio);
				
				// Destroy opposing unit and map temp files
				File.Delete("units" + Path.DirectorySeparatorChar + "askndfosnfzlknelfknlknsakfnsdknslkdfn.units");
				File.Delete("map" + Path.DirectorySeparatorChar + "askndfosnfzlknelfknlknsakfnsdknslkdfn.map");

				// Reload personal units
				state.UserUnits.Clear();

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

				state.UserUnits.Sort((u1, u2) => u1.Name.CompareTo(u2.Name));

				// Reload personal map
				state.Map = new Board(state.UserName);

				// Redraw
				state.Map.DrawMapFogless(win,0,0);
			}
			else
				return false;
			
			return true;
		}

		/// <summary>
		/// Runs the game.
		/// </summary>
		/// <param name="win">The window to draw on.</param>
		/// <param name="state">The state of the game.</param>
		/// <param name="nio">The network stream to communicate with.</param>
		public void GameLogic(Window win, GameState state, NetworkCommunicator nio)
		{


			return;
		}

		/// <summary>
		/// Allows the user to place units on the board.
		/// </summary>
		/// <param name="win">The window to draw on.</param>
		/// <param name="state">The state of the game.</param>
		/// <returns>Returns the locations units were placed at.</returns>
		protected List<Pair<Pair<int,int>,string>> PlaceUnits(Window win, GameState state)
		{
			// Hide the cursor
			bool old_cursor_visible = Console.CursorVisible;
			Console.CursorVisible = false;

			// We need to track points (for speed and code ease)
			int points = 0;

			// Redraw the map and point notification
			state.Map.RecalculateSight();
			state.Map.DrawMap(win,0,0);

			win.ClearBox(new Rectangle(0,state.Map.Height,30,1)); // A width of 30 should be enough for any point total we could have

			win.CursorPosition = new Pair<int,int>(0,state.Map.Height);
			win.Write("Squad Points: " + points + "/" + Settings.SquadMaxPoints);

			// Setup return value
			List<Pair<Pair<int,int>,string>> ret = new List<Pair<Pair<int,int>,string>>();

			// Create a menu for selecting an action and selecting a unit
			List<string> unit_names = new List<string>(state.UserUnits.Count);

			foreach(Unit u in state.UserUnits)
				unit_names.Add(GetExtendedName(u));
			
			Menu action = new Menu("Action",new string[] {"Place Unit","Remove Unit","Finish"},0,3);
			Menu units = new Menu("Select a Unit",unit_names,0,15);
			
			// Start placing units
			// Note that placing a unit on top of another removes the first and placing an already placed unit moves it
			// When placing a unit, change the name of the unit to the name plus a * to indicate that it is placed
			while(true)
			{
				// Get a location to choose an action on
				Pair<int,int> loc = SelectPosition(win,state);

				// Choose an action
				action.Display(win,state.Map.Width + 2,1);

				// We need to check if we should break the while loop
				bool done = false;

				switch(action.SelectedIndex)
				{
				case 0: // Place Unit
					// Select a unit to place
					units.Display(win,state.Map.Width + action.Width + 3,1);

					// Make sure we actually want to place something
					if(units.SelectedIndex < 0)
						break;

					// If we are placing a unit on top of another we need to first remove the old one
					Unit remove = state.Map.GetAllyAt(loc);

					// If we did not wiff then we need to remove some data
					if(remove != default(Unit))
					{
						state.Map.RemoveAlly(loc); // Remove from the board
						ret.Remove(new Pair<Pair<int,int>,string>(loc,remove.Name)); // Remove from return value
						units.Items[units.Items.IndexOf(GetExtendedName(remove) + "*")] = GetExtendedName(remove); // Fix menu entry

						points -= remove.PointsUsed; // Reduce point total
					}

					// Get the unit we want to place
					Unit place = state.UserUnits[units.SelectedIndex];

					// Check if we already place the unit before
					Pair<Pair<int,int>,string> val = ret.Find(p => p.val2.Equals(place.Name));

					// If we didn't get the default value (should be null but take no chances) then we need to shuffle the unit around
					if(val != default(Pair<Pair<int,int>,string>))
					{
						ret.Remove(val); // Remove the value we just got
						ret.Add(new Pair<Pair<int,int>,string>(loc,place.Name)); // Add in the new location

						// Move the unit around
						state.Map.RemoveAlly(val.val1);
						state.Map.AddAllyAt(place,loc);
					}
					else
					{
						points += place.PointsUsed; // Increase point total since we placed the unit

						units.Items[units.Items.IndexOf(GetExtendedName(place))] = GetExtendedName(place) + "*"; // Update menu entry
						ret.Add(new Pair<Pair<int,int>,string>(loc,place.Name)); // Add return value
						state.Map.AddAllyAt(place,loc); // Add to board
					}
					
					break;
				case 1: // Remove Unit
					Unit u = state.Map.GetAllyAt(loc);

					// If there's no unit at the location do nothing
					if(u == null)
						break;

					// Remove the unit from the map
					state.Map.RemoveAlly(loc);

					// Remove the unit from the return value
					ret.Remove(new Pair<Pair<int,int>,string>(loc,u.Name));

					// Change the value in the units menu
					units.Items[units.Items.IndexOf(GetExtendedName(u) + "*")] = GetExtendedName(u);
					
					// Reduce point total
					points -= u.PointsUsed;

					break;
				case 2: // Finish (only valid if exactly one unit with leadership is present and total points are less than the max; write error on fail)
					int first = -1; // A random index that needs saving

					// First find a unit with leadership
					if((first = ret.FindIndex(p => {if(state.UserUnits.Find(unit => unit.Name == p.val2).HasAbility(typeof(Engine.Abilities.PassiveAbilities.DamageModifier.Leadership))) return true; return false;})) < 0 || ret.FindIndex(first + 1,p => {if(state.UserUnits.Find(unit => unit.Name == p.val2).HasAbility(typeof(Engine.Abilities.PassiveAbilities.DamageModifier.Leadership))) return true; return false;}) >= 0)
					{
						win.ClearBox(state.Output);
						win.Write("You must have exactly one unit with leadership on the map.",state.Output);

						break;
					}

					// Make sure we don't have too many points of units
					if(points > Settings.SquadMaxPoints)
					{
						win.ClearBox(state.Output);
						win.Write("You need " + (points - Settings.SquadMaxPoints) + " less points of units (" + Settings.SquadMaxPoints + " max points).",state.Output);

						break;
					}

					done = true;
					break;
				}

				// Redraw the map
				state.Map.RecalculateSight();
				state.Map.DrawMap(win,0,0);
				
				// If we have finished placing units then break
				if(done)
					break;
				else
				{
					// If we are not done we need to redraw the point notification area
					win.ClearBox(new Rectangle(0,state.Map.Height,30,1)); // A width of 30 should be enough for any point total we could have
					
					win.CursorPosition = new Pair<int,int>(0,state.Map.Height);
					win.Write("Squad Points: " + points + "/" + Settings.SquadMaxPoints);
				}
			}

			// Restore cursor visiblity (map has been recently redrawn so no problems there)
			Console.CursorVisible = old_cursor_visible;

			// Sort return value for convenience
			ret.Sort((p1, p2) => p1.val2.CompareTo(p2.val2));

			return ret;
		}

		/// <summary>
		/// Allows the user to select a position on the board.
		/// </summary>
		/// <param name="win">The window to draw on.</param>
		/// <param name="state">The state of the game.</param>
		/// <returns>Returns the locations selected.</returns>
		protected Pair<int,int> SelectPosition(Window win, GameState state)
		{
			// A nice variable
			Pair<string,Pair<ConsoleColor,ConsoleColor>> img = state.Map.GetImageAt(new Pair<int,int>(0,0));

			// Put the cursor at the origin
			win.CursorPosition = new Pair<int,int>(0,0);
			win.SetColor(img.val2.val1,ConsoleColor.Magenta);
			
			// Display the cursor
			win.Write(img.val1);

			// Move the cursor back
			win.CursorPosition = win.GetLastCursorPosition(new Rectangle(0,0,state.Map.Width,state.Map.Height));

			// Get the first key input
			ConsoleKeyInfo next_key = Console.ReadKey(true);
			InputType key_type = DetermineInputType(next_key);

			// Move the cursor around until enter is pressed
			while(key_type != InputType.TERMINATE_LINE)
			{
				if(key_type == InputType.ARROW)
				{
					// Redraw the tile we were on
					win.SetColor(img.val2.val1,img.val2.val2);
					win.Write(img.val1);

					// Move the cursor back so we can copy paste the movement logic
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

					img = state.Map.GetImageAt(win.CursorPosition);

					win.SetColor(img.val2.val1,ConsoleColor.Magenta);
					win.Write(img.val1);

					win.CursorPosition = win.GetLastCursorPosition(new Rectangle(0,0,state.Map.Width,state.Map.Height));
				}

				// Get the next key
				next_key = Console.ReadKey(true);
				key_type = DetermineInputType(next_key);
			}

			return win.CursorPosition;
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
		/// Get's the full extended unit name.
		/// </summary>
		/// <param name="u">The units to get the full name for.</param>
		/// <returns>Returns the full name of the unit.</returns>
		protected string GetExtendedName(Unit u)
		{return u.Name + " (" + u.PointsUsed + ")" + (u.HasAbility(typeof(Engine.Abilities.PassiveAbilities.DamageModifier.Leadership)) ? " L" : "");}

		/// <summary>
		/// Print the manual entry for this command.
		/// </summary>
		/// <param name="win">The window to write on.</param>
		/// <param name="Output">The area to write in.</param>
		public void Man(Window win, Rectangle Output)
		{
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.Write("host - Hosts a game server (you must also have a unit with leadership).",Output);
			
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
