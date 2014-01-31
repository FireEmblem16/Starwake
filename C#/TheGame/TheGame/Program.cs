using System;
using System.IO;
using System.Reflection;
using TheGame.Engine.Tiles;
using TheGame.Engine.Abilities.ActivatableAbility.Attack;

namespace TheGame
{
	/// <summary>
	/// The main class that starts up the game.
	/// </summary>
	public class Program
	{
		/// <summary>
		/// The entry point.
		/// </summary>
		/// <param name="args">Command line arguments.</param>
		public static void Main(string[] args)
		{
			Program prgm = new Program();
			
			prgm.InitializeGame(args);
			prgm.StartGame(args);
			
			return;
		}

		/// <summary>
		/// Initializes game data
		/// </summary>
		/// <param name="args">Command line arguments.</param>
		public void InitializeGame(string[] args)
		{
			// Ensure necessary folders exist
			if(!Directory.Exists("mods"))
				Directory.CreateDirectory("mods");

			if(!Directory.Exists("units"))
				Directory.CreateDirectory("units");

			if(!Directory.Exists("maps"))
				Directory.CreateDirectory("maps");

			LoadAssemblyData();
			return;
		}

		/// <summary>
		/// Starts the game.
		/// </summary>
		/// <param name="args">Command line arguments.</param>
		public void StartGame(string[] args)
		{
			new TheGame();
			return;
		}

		/// <summary>
		/// Gathers any extra data the program can find and calls all static initializations that it can locate.
		/// </summary>
		/// <param name="LoadMods">If true then we will try to load mods into the game.</param>
		protected int LoadAssemblyData(bool LoadMods = true)
		{
			// Load and initialize our own assembly first
			InitializeAssembly(Assembly.GetAssembly(typeof(Program)));

			// Load and initialize our basic references
			InitializeAssembly(Assembly.GetAssembly(typeof(Tile))); // The Engine
			InitializeAssembly(Assembly.GetAssembly(typeof(RangedAttack))); // Basic Abilities
			InitializeAssembly(Assembly.GetAssembly(typeof(Mountain))); // Basic Terrain
			
			// If we are loading mods then look in the mods folder for dlls to load
			if(LoadMods)
			{
				string[] modules = Directory.GetFiles("mods","*.dll",SearchOption.AllDirectories);
				int loaded = 0; // How many mods we've successfully loaded

				foreach(string mod in modules)
					try
					{
						InitializeAssembly(Assembly.LoadFrom(mod));
						loaded++;
					}
					catch
					{}

				return loaded;
			}
			
			return 0;
		}

		/// <summary>
		/// Attempts to call the static initialization function on every type in the given assembly.
		/// </summary>
		/// <param name="asm">The assembly to initialize.</param>
		protected void InitializeAssembly(Assembly asm)
		{
			Type[] types = asm.GetTypes();
			
			foreach(Type t in types)
			{
				MethodInfo func = t.GetMethod("Initialize");

				if(func != null)
					func.Invoke(null,null);
			}

			return;
		}
	}
}
