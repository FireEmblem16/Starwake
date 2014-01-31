using System;
using System.Collections.Generic;
using System.Reflection;
using TheGame.Engine;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Parsers.Commands
{
	/// <summary>
	/// Represents an action that can be preformed in the game.
	/// </summary>
	public interface Command
	{
		/// <summary>
		/// Executes the command.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <returns>Returns true if the command was executed successfully and false otherwise.</returns>
		bool Execute(Window win, GameState state);

		/// <summary>
		/// Print the manual entry for this command.
		/// </summary>
		/// <param name="win">The window to write on.</param>
		/// <param name="Output">The area to write in.</param>
		void Man(Window win, Rectangle Output);
	}

	/// <summary>
	/// Does nothing.
	/// </summary>
	public class InvalidCommand : Command
	{
		public InvalidCommand()
		{return;}

		public bool Execute(Window win, GameState state)
		{return false;}

		public void Man(Window win, Rectangle Output)
		{
			win.SetColor(ConsoleColor.White,ConsoleColor.Black);
			win.Write("Invalid Command. Type help for a list of available commands.",Output);
			
			return;
		}
	}

	/// <summary>
	/// Constructs commands from StringTokenizers.
	/// </summary>
	public static class CommandFactory
	{
		/// <summary>
		/// Initializes the class data. Being a static class this is called when the assembly is loaded.
		/// </summary>
		static CommandFactory()
		{
			command_factory = new Factory<string,Command>();
			return;
		}

		/// <summary>
		/// Registers a new command in the factory.
		/// </summary>
		/// <param name="command">The command to add. The first value is the name of the command. The second value is the delegate that will produce the command.</param>
		public static void RegisterCommand(Pair<string,GenerateCommand> command)
		{
			command_factory.Register(new Pair<string,MethodInfo>(command.val1.ToLower(),command.val2.Method));
			return;
		}

		/// <summary>
		/// Remvoes a command from the factory.
		/// </summary>
		/// <param name="command">The name of the command to remove.</param>
		public static void UnregisterCommand(string command)
		{
			command_factory.Unregister(command.ToLower());
			return;
		}

		/// <summary>
		/// Unregisters all commands.
		/// </summary>
		public static void UnregisterAllCommands()
		{
			command_factory.UnregisterAll();
			return;
		}

		/// <summary>
		/// Creates a new command from the given information.
		/// </summary>
		/// <param name="name">The name of the command to create.</param>
		/// <param name="source">Any additional information the command will need to be created.</param>
		/// <returns>Returns the appropriate command or an invalid command if the given command name was not found.</returns>
		public static Command CreateCommand(string name, StringTokenizer source)
		{
			object[] parameters = new object[1];
			parameters[0] = source;

			Command ret = command_factory.CreateItem(name.ToLower(),parameters);

			if(ret == default(Command))
				return new InvalidCommand();

			return ret;
		}

		/// <summary>
		/// Creates a list of available commands.
		/// </summary>
		/// <returns>Returns a string listing all commands.</returns>
		public static string CommandList()
		{
			return command_factory.List();
		}

		/// <summary>
		/// Creates a list of available commands.
		/// </summary>
		/// <returns>Returns a list of strings listing all the commands.</returns>
		public static List<string> Commands()
		{
			return command_factory.Items();
		}

		/// <summary>
		/// All the command generators.
		/// </summary>
		private static Factory<string,Command> command_factory;
	}

	/// <summary>
	/// Generates a command given a set of arguments.
	/// </summary>
	/// <param name="source">The arguments to the command.</param>
	/// <returns>Returns a command.</returns>
	public delegate Command GenerateCommand(StringTokenizer source);
}
