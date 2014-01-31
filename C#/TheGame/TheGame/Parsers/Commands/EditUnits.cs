using System;
using System.Collections.Generic;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;
using TheGame.Engine.Abilities;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// An editing command that allows the user to edit their units.
	/// </summary>
	public class EditUnits : Command
	{
		/// <summary>
		/// Creates a new edit units command.
		/// </summary>
		public EditUnits()
		{
			valid = true;
			return;
		}

		/// <summary>
		/// Creates a new edit units command.
		/// </summary>
		/// <param name="source">The extra parameters to the edit units command.</param>
		public EditUnits(StringTokenizer source)
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
			CommandFactory.RegisterCommand(new Pair<string,GenerateCommand>("edtunits",CreateEditUnitsCommand));
			return;
		}

		/// <summary>
		/// Creates a new edit units command.
		/// </summary>
		/// <param name="source">The extra parameters to the edit units command.</param>
		/// <returns>Returns a edit units command.</returns>
		protected static Command CreateEditUnitsCommand(StringTokenizer source)
		{return new EditUnits(source);}

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
				win.Write("Editing " + state.UserName + "\'s units. Press escape to leave edit mode.",state.Output);

				bool old_cursor_visible = Console.CursorVisible;
				Console.CursorVisible = false;

				// Create a menu for selecting units
				List<string> units = new List<string>(state.UserUnits.Count + 1);

				foreach(Unit u in state.UserUnits)
					units.Add(u.Name + "(" + u.PointsUsed + "/" + u.PointTotal + ")" + (u.IsLeader() ? "*" : ""));

				units.Add("<NEW>");

				// Create the units menu
				Menu m = new Menu("Select a Unit",units,0,15);
				int units_index = m.Display(win,state.Map.Width + 2,1);

				// These are the things we can do to a unit
				// Never changes so we stick it outside the while loop
				List<string> options = new List<string>();
				options.Add("Add/Remove Abilities");
				options.Add("Level Abilities");
				options.Add("Change Point Total");
				options.Add("Rename");
				options.Add("Duplicate");
				options.Add("Delete");

				// Edit units until we have choosen escape
				while(units_index != -1)
				{
					if(units_index != m.Items.Count - 1)
					{
						// Create a menu for choosing editing options
						Unit u = state.UserUnits[units_index];
						Menu ops = new Menu(u.Name + "(" + u.PointsUsed + "/" + u.PointTotal + ")" + (u.IsLeader() ? "*" : ""),options,0,15);
						int options_index = ops.Display(win,state.Map.Width + m.Width + 3,1);

						// Edit the unit until we choose escape
						while(options_index != -1)
						{
							bool double_break = false;

							switch(options_index)
							{
							case 0:
								AddRemoveAbilities(u,win,state.Map.Width + m.Width + ops.Width + 4,1,state.Output);
								break;
							case 1:
								LevelAbilities(u,win,state.Map.Width + m.Width + ops.Width + 4,1,state.Output);
								break;
							case 2:
								ChangePointTotal(u,win,state.Map.Width + m.Width + ops.Width + 4,1,state.Output);
								break;
							case 3:
							{
								string old_name = u.Name;
								Rename(u,state,win,state.Map.Width + m.Width + ops.Width + 4,1,state.Output);

								// If we rename the unit we need to change the selected index of first menu and we have to create a new one to force the change (at time of coding)
								if(old_name != u.Name)
								{
									units.Clear();

									foreach(Unit un in state.UserUnits)
										units.Add(un.Name + "(" + un.PointsUsed + "/" + un.PointTotal + ")" + (un.IsLeader() ? "*" : ""));

									units.Add("<NEW>");

									m = new Menu(m.Title,units,state.UserUnits.BinarySearch(new Unit(u.Name,0),comp),15);
								}

								break;
							}
							case 4:
								if(Duplicate(u,state,win,state.Map.Width + m.Width + ops.Width + 4,1,state.Output))
								{
									units.Clear();

									foreach(Unit un in state.UserUnits)
										units.Add(un.Name + "(" + un.PointsUsed + "/" + un.PointTotal + ")" + (un.IsLeader() ? "*" : ""));

									units.Add("<NEW>");
								
									m = new Menu(m.Title,units,state.UserUnits.BinarySearch(new Unit(u.Name,0),comp),15);
								}

								break;
							case 5:
							{
								int index = state.UserUnits.BinarySearch(u,comp);
							
								if(index >= 0)
								{
									state.UserUnits.RemoveAt(index);
									double_break = true;
								}

								break;
							}
							}

							// When we delete the unit we need exit the menu so we have this check
							if(double_break)
								break;

							// Refresh the menu title
							ops.Title = u.Name + "(" + (u.PointTotal - u.PointsFree) + "/" + u.PointTotal + ")" + (u.IsLeader() ? "*" : "");

							// Get next edit option
							options_index = ops.Display(win,state.Map.Width + m.Width + 3,1);
						}

						// Refresh the units menu
						units.Clear();

						foreach(Unit un in state.UserUnits)
							units.Add(un.Name + "(" + un.PointsUsed + "/" + un.PointTotal + ")" + (un.IsLeader() ? "*" : ""));

						units.Add("<NEW>");

						m = new Menu(m.Title,units,m.SelectedIndex,15);
					}
					else
					{
						Unit new_unit = CreateNewUnit(state,win,state.Map.Width + m.Width + 3,1,state.Output);

						if(new_unit != null)
						{
							// Refresh the units menu
							units.Clear();

							foreach(Unit un in state.UserUnits)
								units.Add(un.Name + "(" + un.PointsUsed + "/" + un.PointTotal + ")" + (un.IsLeader() ? "*" : ""));

							units.Add("<NEW>");

							m = new Menu(m.Title,units,m.SelectedIndex + 1,15);
						}
					}

					// Get next unit
					units_index = m.Display(win,state.Map.Width + 2,1);
				}

				// Preform cleanup on the cursor
				Console.CursorVisible = old_cursor_visible;
			}
			else
				return false;

			return true;
		}

		/// <summary>
		/// Allows the user to create a new unit which is added to the provided state.
		/// </summary>
		/// <param name="state">The state of the game. We will need this to check that no other units have the same name.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		/// <returns>Returns the new unit or null if no unit was made.</returns>
		protected Unit CreateNewUnit(GameState state, Window win, int x, int y, Rectangle output)
		{
			CommandParser text_box = new CommandParser("\n");
			
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			
			win.CursorPosition = new Pair<int,int>(x,y);
			win.Write("Enter Name for New Unit");
			
			win.CursorPosition = new Pair<int,int>(x,y + 1);
			win.Write("-----------------------");

			string new_name = text_box.NextString(win,x,y + 2,Settings.MaxNameLength);
			win.ClearBox(new Rectangle(x,y,Settings.MaxNameLength + 19,3));

			Unit u = new Unit(new_name,Settings.UnitMinPoints);
			int index = state.UserUnits.BinarySearch(u,comp);

			// If we already have a unit with this name we can reuse the name
			if(index >= 0)
			{
				win.ClearBox(output);
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);

				win.Write("The name " + new_name + " is already in use.",output);
				return null;
			}
			else
				state.UserUnits.Insert(~index,u);
			
			return u;
		}

		/// <summary>
		/// Allows the user to add and remove abilities from the given unit.
		/// </summary>
		/// <param name="u">The unit we are editing.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		protected void AddRemoveAbilities(Unit u, Window win, int x, int y, Rectangle output)
		{
			// Extra data
			List<string> all_abilities = AbilityFactory.Abilities();
			List<string> abilities = u.GetAbilities(); // Doesn't matter that it's not sorted
			List<string> tooltips = new List<string>(all_abilities.Count);

			foreach(string str in all_abilities)
				tooltips.Add(AbilityFactory.GenerateAbility(str).Documentation);

			// The list we will display
			List<string> abilities_suffixed = new List<string>(abilities.Count);

			foreach(string str in all_abilities)
				abilities_suffixed.Add(str + (abilities.Contains(str) ? "* [" + u.GetAbility(AbilityFactory.GenerateAbility(str).GetType()).PointCost + "]" : " [" + AbilityFactory.GenerateAbility(str).PointCost + "]"));

			Menu m = new Menu(u.Name + "(" + u.PointsUsed + "/" + u.PointTotal + ")" + "\'s Abilities",abilities_suffixed,tooltips,0,15);
			int index = m.DisplayWithTips(win,x,y,output);

			while(index != -1)
			{
				Ability dummy = AbilityFactory.GenerateAbility(all_abilities[index]);
				bool success = true;

				if(u.HasAbility(dummy.GetType())) // This is probably faster than searching through a bunch of strings
					u.RemoveAbility(dummy.GetType());
				else
					success = u.AddAbility(dummy);

				// Perform updates in a really inefficient way if needed
				if(success)
				{
					abilities = u.GetAbilities();
					abilities_suffixed.Clear();

					foreach(string str in all_abilities)
						abilities_suffixed.Add(str + (abilities.Contains(str) ? "* [" + u.GetAbility(AbilityFactory.GenerateAbility(str).GetType()).PointCost + "]" : " [" + AbilityFactory.GenerateAbility(str).PointCost + "]"));
				
					m = new Menu(u.Name + "(" + u.PointsUsed + "/" + u.PointTotal + ")" + "\'s Abilities",abilities_suffixed,tooltips,m.SelectedIndex,15,m.TopIndex);
				}

				// Get next selection
				index = m.DisplayWithTips(win,x,y,output);
			}

			return;
		}

		/// <summary>
		/// Allows the user to level and unlevel abilities from the given unit.
		/// </summary>
		/// <param name="u">The unit we are editing.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		protected void LevelAbilities(Unit u, Window win, int x, int y, Rectangle output)
		{
			List<string> ability_names = u.GetAbilities();

			Menu m = new Menu(u.Name + "(" + u.PointsUsed + "/" + u.PointTotal + ")" + "\'s Abilities",u.GetAbilities(false,true,true),u.GetDocumentation(),0,15);
			int index = m.DisplayWithTips(win,x,y,output);

			while(index != -1)
			{
				PowerLevel(u,u.GetAbility(AbilityFactory.GenerateAbility(ability_names[index]).GetType()),win,x + m.Width + 1,y,output);

				// We need to update the list of items
				m = new Menu(u.Name + "(" + u.PointsUsed + "/" + u.PointTotal + ")" + "\'s Abilities",u.GetAbilities(false,true,true),u.GetDocumentation(),m.SelectedIndex,15);
				index = m.DisplayWithTips(win,x,y,output);
			}

			return;
		}

		/// <summary>
		/// Allows the user to change the level of the given ability.
		/// </summary>
		/// <param name="u">The unit we are editing.</param>
		/// <param name="a">The ability we are leveling.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		protected void PowerLevel(Unit u, Ability a, Window win, int x, int y, Rectangle output)
		{
			// Shouldn't happen but just in case
			if(a == null || a.MaxLevel == 1)
				return;

			// The documentation should already be out but just in case we'll write it again
			win.ClearBox(output);
			win.Write(a.Documentation,output);

			// Create a dummy copy of the ability to see how high we can get the level
			Ability dummy = AbilityFactory.GenerateAbility(a.Name);

			while(dummy.Level < dummy.MaxLevel)
				if(!dummy.LevelUp())
					break;

			while(dummy.PointCost - a.PointCost > u.PointsFree)
				if(!dummy.Unlevel())
					return; // This should never happen since we already have the ability but in case it does we can't do anything useful with the ability

			int max = dummy.Level;

			// Create and display the "menu" for choosing the level
			List<string> levels = new List<string>(max);
			
			for(int i = max;i > 0;i--,dummy.Unlevel())
				levels.Add(i.ToString() + " [" + dummy.PointCost + "]"); // Add levels in reverse order to make it feel more like leveling "up" and leveling "down"

			Menu m = new Menu("Choose Level",levels,max - a.Level,1);
			int index = max - (m.Display(win,x,y) + 1);

			if(index != -1)
				if(index + 1 < a.Level) // Index is zero based and level is one based
				{
					while(a.Level > index + 1)
						if(!a.Unlevel())
							break; // Should never happen
				}
				else
					while(a.Level < index + 1)
						if(!a.LevelUp())
							break; // Should never happen

			return;
		}

		/// <summary>
		/// Allows the user to rename the given unit.
		/// </summary>
		/// <param name="u">The unit we are editing.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="state">The state of the game. We will need this to check that no other units have the same name.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		protected void Rename(Unit u, GameState state, Window win, int x, int y, Rectangle output)
		{
			CommandParser text_box = new CommandParser("\n");
			
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			
			win.CursorPosition = new Pair<int,int>(x,y);
			win.Write("Enter New Name for " + u.Name);
			
			string name_underscore = "";
			for(int i = 0;i < u.Name.Length;i++,name_underscore += "-");

			win.CursorPosition = new Pair<int,int>(x,y + 1);
			win.Write("-------------------" + name_underscore);

			string new_name = text_box.NextString(win,x,y + 2,Settings.MaxNameLength);
			win.ClearBox(new Rectangle(x,y,Settings.MaxNameLength + 19,3));

			Unit dummy = new Unit(new_name,0);
			
			// Make sure we don't have any bad characters
			if(new_name.Contains("@"))
			{
				win.ClearBox(output);
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);

				win.Write("Names cannot include the character @.",output);
				return;
			}

			// If we already have a unit with this name we can reuse the name
			if(state.UserUnits.BinarySearch(dummy,comp) >= 0)
			{
				win.ClearBox(output);
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);

				win.Write("The name " + new_name + " is already in use.",output);
				return;
			}

			u.Rename(new_name);
			return;
		}

		/// <summary>
		/// Allows the user to duplicate the unit.
		/// </summary>
		/// <param name="u">The unit we are editing.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="state">The state of the game. We will need this to check that no other units have the same name.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		protected bool Duplicate(Unit u, GameState state, Window win, int x, int y, Rectangle output)
		{
			CommandParser text_box = new CommandParser("\n");
			
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			
			win.CursorPosition = new Pair<int,int>(x,y);
			win.Write("Enter Name for New Unit");
			
			win.CursorPosition = new Pair<int,int>(x,y + 1);
			win.Write("-----------------------");

			string new_name = text_box.NextString(win,x,y + 2,Settings.MaxNameLength);
			win.ClearBox(new Rectangle(x,y,Settings.MaxNameLength + 19,3));

			if(new_name == null || new_name == "")
				return false;

			string unit = u.ToString();
			int end_of_name = unit.IndexOf("\n");
			u = new Unit(new StringTokenizer(new_name + unit.Substring(end_of_name,unit.Length - end_of_name),"\n"));
			
			int index = state.UserUnits.BinarySearch(u,comp);

			// If we already have a unit with this name we can reuse the name
			if(index >= 0)
			{
				win.ClearBox(output);
				win.SetColor(ConsoleColor.White,ConsoleColor.Black);

				win.Write("The name " + new_name + " is already in use.",output);
				return false;
			}

			state.UserUnits.Insert(~index,u);
			return true;
		}

		/// <summary>
		/// Allows the user to change the point total of the given unit.
		/// </summary>
		/// <param name="u">The unit we are editing.</param>
		/// <param name="win">The window to write on.</param>
		/// <param name="x">The x position to start writing at.</param>
		/// <param name="y">The y position to start writing at.</param>
		/// <param name="output">Where we can output messages to the user at.</param>
		protected void ChangePointTotal(Unit u, Window win, int x, int y, Rectangle output)
		{
			int min = Settings.UnitMinPoints;

			if(min < u.PointsUsed)
				min = u.PointsUsed;

			List<string> points = new List<string>(Settings.UnitMaxPoints - min + 1);
			
			// Reverse order so arrow keys feel natural
			for(int i = Settings.UnitMaxPoints;i >= min;i--)
				points.Add(i.ToString());

			Menu m = new Menu("Set Point Total",points,Settings.UnitMaxPoints - u.PointTotal,1);
			int index = m.Display(win,x,y);

			if(index != -1)
				u.ChangePoints(Settings.UnitMaxPoints - index);
			
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
			win.Write("edtunits - Allows the current user to edit their units. The user must be logged in.",Output);

			return;
		}

		/// <summary>
		/// If true then the command is valid and if false it is not.
		/// </summary>
		protected bool valid = false;

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
