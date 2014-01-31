using System;
using System.Collections.Generic;
using System.IO;
using TheGame.Engine.Abilities.PassiveAbilities.DamageModifier;
using TheGame.Engine.Tiles;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine
{
	/// <summary>
	/// Represents a single player's territory in a game.
	/// </summary>
	public class Board
	{
		/// <summary>
		/// Creates a new board with the terrain being barren wasteland.
		/// </summary>
		/// <param name="w">The width of the board.</param>
		/// <param name="h">The height of the board.</param>
		public Board(int w, int h)
		{
			tiles = new Dictionary<Pair<int,int>,Tile>();
			AllyUnits = new Dictionary<Pair<int,int>,Unit>();
			EnemyUnits = new Dictionary<Pair<int,int>,Unit>();

			comp = new LocComparer();
			sunny = new List<Pair<int,int>>(w * h);
			see_invis = new List<Pair<int,int>>(w * h);

			Width = w;
			Height = h;

			return;
		}

		/// <summary>
		/// Creates a new board with the terrain as specified.
		/// </summary>
		/// <param name="w">The width of the board.</param>
		/// <param name="h">The height of the board.</param>
		/// <param name="source">The tiles to use for the board given row by row top to bottom. If is assumed that the size of the array equals the width times the height.</param>
		/// <param name="rotate_board">If true then the board will be rotated 180 degrees.</param>
		public Board(int w, int h, Tile[] source, bool rotate_board = false)
		{
			tiles = new Dictionary<Pair<int,int>,Tile>();
			AllyUnits =  new Dictionary<Pair<int,int>,Unit>();
			EnemyUnits =  new Dictionary<Pair<int,int>,Unit>();

			comp = new LocComparer();
			sunny = new List<Pair<int,int>>(w * h);
			see_invis = new List<Pair<int,int>>(w * h);

			Width = w;
			Height = h;

			if(rotate_board)
				for(int i = 0;i < Height;i++)
					for(int j = 0;j < Width;j++)
						SetTile(j,i,source[j + i * Width]);
			else
				for(int i = 0;i < Height;i++)
					for(int j = 0;j < Width;j++)
						SetTile(j,i,source[Height * Width - 1 - (j + i * Width)]);

			return;
		}

		/// <summary>
		/// Creates a single board from two boards.
		/// </summary>
		/// <param name="player">This player's board. This will be the bottom board.</param>
		/// <param name="opponent">The opponent's board that should be given rotated 180 degrees.</param>
		/// <remarks>It is assumed that the width of both boards is the same.</remarks>
		public Board(Board player, Board opponent)
		{
			tiles = new Dictionary<Pair<int,int>,Tile>();
			AllyUnits =  new Dictionary<Pair<int,int>,Unit>();
			EnemyUnits =  new Dictionary<Pair<int,int>,Unit>();

			Width = player.Width;
			Height = player.Height + opponent.Height;

			comp = new LocComparer();
			sunny = new List<Pair<int,int>>(Width * Height);
			see_invis = new List<Pair<int,int>>(Width * Height);

			// Place the opponent's board on top of the player's
			for(int i = 0;i < Width;i++)
			{
				for(int j = 0;j < opponent.Height;j++)
					SetTile(i,j,opponent.GetTile(i,j));
				
				for(int j = 0;j < player.Height;j++)
					SetTile(i,j + opponent.Height,player.GetTile(i,j));
			}

			// These units need to be moved down
			foreach(KeyValuePair<Pair<int,int>,Unit> p in player.AllyUnits)
				AllyUnits.Add(new Pair<int,int>(p.Key.val1,p.Key.val2 + opponent.Height),p.Value);

			foreach(KeyValuePair<Pair<int,int>,Unit> p in player.EnemyUnits)
				EnemyUnits.Add(new Pair<int,int>(p.Key.val1,p.Key.val2 + opponent.Height),p.Value);

			// These units can be added where they are
			foreach(KeyValuePair<Pair<int,int>,Unit> p in opponent.AllyUnits)
				AllyUnits.Add(p.Key,p.Value);

			foreach(KeyValuePair<Pair<int,int>,Unit> p in opponent.EnemyUnits)
				EnemyUnits.Add(p.Key,p.Value);

			return;
		}

		/// <summary>
		/// Creates a new board from the given file.
		/// If the file is not found a default map is loaded instead with size 30x15 filled with plains.
		/// </summary>
		/// <param name="file">The file the board is saved in.</param>
		/// <exception cref="IOException"></exception>
		public Board(string file)
		{
			tiles = new Dictionary<Pair<int,int>,Tile>();
			AllyUnits =  new Dictionary<Pair<int,int>,Unit>();
			EnemyUnits =  new Dictionary<Pair<int,int>,Unit>();
			comp = new LocComparer();

			if(!File.Exists("maps" + Path.DirectorySeparatorChar + file + ".map"))
			{
				Width = Settings.DefaultMapWidth;
				Height = Settings.DefaultMapHeight;

				sunny = new List<Pair<int,int>>(Width * Height);
				see_invis = new List<Pair<int,int>>(Width * Height);
				return;
			}
			
			using(StreamReader fin = new StreamReader("maps" + Path.DirectorySeparatorChar + file + ".map"))
			{
				Width = int.Parse(fin.ReadLine());
				Height = int.Parse(fin.ReadLine());

				for(int i = 0;i < Width;i++)
					for(int j = 0;j < Height;j++)
						try
						{SetTile(i,j,TileFactory.GenerateTile(fin.ReadLine()));}
						catch
						{throw new IOException(file + " is corrupted.");}
			}

			sunny = new List<Pair<int,int>>(Width * Height);
			see_invis = new List<Pair<int,int>>(Width * Height);
			return;
		}

		/// <summary>
		/// Draws the map at the specified window coordinates.
		/// </summary>
		/// <param name="win">The window to draw with.</param>
		/// <param name="x">The x coordinate to start with.</param>
		/// <param name="y">The y coordinate to start with.</param>
		/// <param name="allycolor">The color to draw allies with.</param>
		/// <param name="enemycolor">The color to draw enemies with.</param>
		public void DrawMap(Window win, int x, int y, ConsoleColor allycolor = ConsoleColor.Blue, ConsoleColor enemycolor = ConsoleColor.Red)
		{
			for(int i = 0;i < Width;i++)
				for(int j = 0;j < Height;j++)
				{
					Pair<string,Pair<ConsoleColor,ConsoleColor>> draw = GetImageAt(new Pair<int,int>(x + i,y + j),allycolor,enemycolor);

					win.SetColor(draw.val2.val1,draw.val2.val2);
					win.CursorPosition = new Pair<int,int>(x + i,y + j);
					win.Write(draw.val1);
				}

			return;
		}

		/// <summary>
		/// Draws the map at the specified window coordinates without fog of war.
		/// </summary>
		/// <param name="win">The window to draw with.</param>
		/// <param name="x">The x coordinate to start with.</param>
		/// <param name="y">The y coordinate to start with.</param>
		/// <param name="allycolor">The color to draw allies with.</param>
		/// <param name="enemycolor">The color to draw enemies with.</param>
		public void DrawMapFogless(Window win, int x, int y, ConsoleColor allycolor = ConsoleColor.Blue, ConsoleColor enemycolor = ConsoleColor.Red)
		{
			for(int i = 0;i < Width;i++)
				for(int j = 0;j < Height;j++)
				{
					Pair<string,Pair<ConsoleColor,ConsoleColor>> draw = GetImageAtFogless(new Pair<int,int>(x + i,y + j),allycolor,enemycolor);

					win.SetColor(draw.val2.val1,draw.val2.val2);
					win.CursorPosition = new Pair<int,int>(x + i,y + j);
					win.Write(draw.val1);
				}

			return;
		}

		/// <summary>
		/// Gets the image of the board at the given position.
		/// </summary>
		/// <param name="loc">The board position to get the image of.</param>
		/// <param name="allycolor">The color to draw ally units.</param>
		/// <param name="enemycolor">The color to draw enemy units.</param>
		/// <returns>Returns what would be drawn at the given location.</returns>
		public Pair<string,Pair<ConsoleColor,ConsoleColor>> GetImageAt(Pair<int,int> loc, ConsoleColor allycolor = ConsoleColor.Blue, ConsoleColor enemycolor = ConsoleColor.Red)
		{
			Pair<string,Pair<ConsoleColor,ConsoleColor>> ret;
			Tile t;

			tiles.TryGetValue(new Pair<int,int>(loc.val1,loc.val2),out t);
			
			// Get the base image that was drawn
			ret = t.GetImage(IsInFogOfWar(loc.val1,loc.val2));

			// If we are in the fog of war we must not have an ally and we can't see the location anyways
			if(IsInFogOfWar(loc.val1,loc.val2))
				return ret;

			// Check if we have to draw an ally or enemy
			Unit ally;
			Unit enemy;

			bool founda = AllyUnits.TryGetValue(loc,out ally);
			bool founde = EnemyUnits.TryGetValue(loc,out enemy);

			if(founda && founde) // If have both we draw the melee symbol
				if(ret.val2.val2 == ConsoleColor.White)
					ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>("%",new Pair<ConsoleColor,ConsoleColor>(ConsoleColor.Black,ConsoleColor.White));
				else
					ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>("%",new Pair<ConsoleColor,ConsoleColor>(ConsoleColor.White,ret.val2.val2));
			else if(founda) // Found only an ally to draw
				ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>(ally.Name.Substring(0,1),new Pair<ConsoleColor,ConsoleColor>(allycolor,ret.val2.val2));
			else if(founde) // Found only an enemy to draw
				ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>(ally.Name.Substring(0,1),new Pair<ConsoleColor,ConsoleColor>(enemycolor,ret.val2.val2));

			return ret;
		}

		/// <summary>
		/// Gets the image of the board at the given position without fog of war.
		/// </summary>
		/// <param name="loc">The board position to get the image of.</param>
		/// <param name="allycolor">The color to draw ally units.</param>
		/// <param name="enemycolor">The color to draw enemy units.</param>
		/// <returns>Returns what would be drawn at the given location.</returns>
		public Pair<string,Pair<ConsoleColor,ConsoleColor>> GetImageAtFogless(Pair<int,int> loc, ConsoleColor allycolor = ConsoleColor.Blue, ConsoleColor enemycolor = ConsoleColor.Red)
		{
			Pair<string,Pair<ConsoleColor,ConsoleColor>> ret;
			Tile t;

			tiles.TryGetValue(new Pair<int,int>(loc.val1,loc.val2),out t);
			
			// Get the base image that was drawn
			ret = t.GetImage(false);

			// Check if we have to draw an ally or enemy
			Unit ally;
			Unit enemy;

			bool founda = AllyUnits.TryGetValue(loc,out ally);
			bool founde = EnemyUnits.TryGetValue(loc,out enemy);

			if(founda && founde) // If have both we draw the melee symbol
				if(ret.val2.val2 == ConsoleColor.White)
					ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>("%",new Pair<ConsoleColor,ConsoleColor>(ConsoleColor.Black,ConsoleColor.White));
				else
					ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>("%",new Pair<ConsoleColor,ConsoleColor>(ConsoleColor.White,ret.val2.val2));
			else if(founda) // Found only an ally to draw
				ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>(ally.Name.Substring(0,1),new Pair<ConsoleColor,ConsoleColor>(allycolor,ret.val2.val2));
			else if(founde) // Found only an enemy to draw
				ret = new Pair<string,Pair<ConsoleColor,ConsoleColor>>(ally.Name.Substring(0,1),new Pair<ConsoleColor,ConsoleColor>(enemycolor,ret.val2.val2));

			return ret;
		}

		/// <summary>
		/// Saves the board to the given file.
		/// </summary>
		/// <param name="file">The filename to save the board to.</param>
		public void SaveMap(string file)
		{
			using(StreamWriter fout = new StreamWriter("maps" + Path.DirectorySeparatorChar + file + ".map"))
			{
				fout.WriteLine(Width);
				fout.WriteLine(Height);

				for(int i = 0;i < Width;i++)
					for(int j = 0;j < Height;j++)
					{
						Tile t;
						tiles.TryGetValue(new Pair<int,int>(i,j),out t); // If this fails the file is corrupted anyways so we might as well corrupt the file further
						
						fout.WriteLine(t.Name);
					}
			}

			return;
		}

		/// <summary>
		/// Determines if a given location is in fog of war.
		/// </summary>
		/// <param name="x">The x coordinate of the location to check.</param>
		/// <param name="y">The y coordinate of the location to check.</param>
		/// <returns>Returns true if the location is in fog of war and false otherwise.</returns>
		/// <exception cref="ArgumentException"></exception>
		public bool IsInFogOfWar(int x, int y)
		{
			if(x < 0 || y < 0 || x >= Width || y >= Height)
				throw new ArgumentException("Argument indicies out of bounds. No such tile exists.");
			
			Tile ret;

			if(!tiles.TryGetValue(new Pair<int,int>(x,y),out ret))
				throw new ArgumentException("No such tile exists.");

			return sunny.BinarySearch(new Pair<int,int>(x,y),comp) < 0; // Negative return value means it is not in the list which means it is not sunny and therefore in fog of war
		}

		/// <summary>
		/// Determines if invisible units can be see by the player at the given location.
		/// </summary>
		/// <param name="x">The x coordinate to check.</param>
		/// <param name="y">The y coordinate to check.</param>
		/// <returns>Returns true if invisible units can be seen at the location and false otherwise.</returns>
		/// <exception cref="ArgumentException"></exception>
		public bool CanSeeInvisible(int x, int y)
		{
			if(x < 0 || y < 0 || x >= Width || y >= Height)
				throw new ArgumentException("Argument indicies out of bounds. No such tile exists.");

			Tile ret;

			if(!tiles.TryGetValue(new Pair<int,int>(x,y),out ret))
				throw new ArgumentException("No such tile exists.");

			return see_invis.BinarySearch(new Pair<int,int>(x,y),comp) >= 0; // Non-negative return value means it is in the list and therefore we can see invisible units here
		}

		/// <summary>
		/// Gets the tile at the specified position.
		/// </summary>
		/// <param name="x">The x coordinate of the tile.</param>
		/// <param name="y">The y coordinate of the tile.</param>
		/// <returns>Returns the tile at the specified position.</returns>
		/// <exception cref="ArgumentException"></exception>
		public Tile GetTile(int x, int y)
		{
			if(x < 0 || y < 0 || x >= Width || y >= Height)
				throw new ArgumentException("Argument indicies out of bounds. No such tile exists.");

			Tile ret;

			if(!tiles.TryGetValue(new Pair<int,int>(x,y),out ret))
				throw new ArgumentException("No such tile exists.");

			return ret;
		}

		/// <summary>
		/// Places the given tile at the specified position.
		/// </summary>
		/// <param name="x">The x coordinate of the tile.</param>
		/// <param name="y">The y coordinate of the tile.</param>
		/// <param name="t">The tile to place.</param>
		/// <exception cref="ArgumentException"></exception>
		public void SetTile(int x, int y, Tile t)
		{
			if(x < 0 || y < 0 || x >= Width || y >= Height)
				throw new ArgumentException("Argument indicies out of bounds.");

			tiles.Remove(new Pair<int,int>(x,y));
			tiles.Add(new Pair<int,int>(x,y),t);

			return;
		}

		/// <summary>
		/// Adds an ally unit at the given location.
		/// </summary>
		/// <param name="ally">The unit to add.</param>
		/// <param name="loc">The location to put the unit at.</param>
		/// <returns>Returns true if the unit was added to the board and false if it could not be added.</returns>
		public bool AddAllyAt(Unit ally, Pair<int,int> loc)
		{
			if(GetAllyAt(loc) != null)
				return false;

			AllyUnits.Add(loc,ally);
			return true;
		}

		/// <summary>
		/// Adds an enemy unit at the given location.
		/// </summary>
		/// <param name="enemy">The unit to add.</param>
		/// <param name="loc">The location to put the unit at.</param>
		/// <returns>Returns true if the unit was added to the board and false if it could not be added.</returns>
		public bool AddEnemyAt(Unit enemy, Pair<int,int> loc)
		{
			if(GetEnemyAt(loc) != null)
				return false;

			EnemyUnits.Add(loc,enemy);
			return true;
		}

		/// <summary>
		/// Gets the allied unit, if any, that is at the given position on the board.
		/// </summary>
		/// <param name="loc">The location to check at.</param>
		/// <returns>Returns the allied unit at the given location or null if there is no unit there.</returns>
		public Unit GetAllyAt(Pair<int,int> loc)
		{
			Unit ret = null;
			AllyUnits.TryGetValue(loc,out ret);

			return ret;
		}

		/// <summary>
		/// Gets the enemy unit, if any, that is at the given position on the board.
		/// </summary>
		/// <param name="loc">The location to check at.</param>
		/// <returns>Returns the enemy unit at the given location or null if there is no unit there.</returns>
		public Unit GetEnemyAt(Pair<int,int> loc)
		{
			Unit ret = null;
			EnemyUnits.TryGetValue(loc,out ret);

			return ret;
		}

		/// <summary>
		/// Moves an ally from it's current location to a new location or removes it from the board if the destination is null.
		/// </summary>
		/// <param name="from">The location the unit is currently at.</param>
		/// <param name="to">The location the unit is to be moved to.</param>
		/// <returns>Returns true if the unit was moved and false if it was not.</returns>
		/// <remarks>Note that two units can not occupy the same location and attemption to do so will not work.</remarks>
		public bool MoveAlly(Pair<int,int> from, Pair<int,int> to = null)
		{
			if(to == null)
				return AllyUnits.Remove(from);

			Unit u = null;

			if(AllyUnits.ContainsKey(to) || !AllyUnits.TryGetValue(from,out u))
				return false;

			AllyUnits.Remove(from);
			AllyUnits.Add(to,u);
			
			return true;
		}

		/// <summary>
		/// Moves an enemy from it's current location to a new location or removes it from the board if the destination is null.
		/// </summary>
		/// <param name="from">The location the unit is currently at.</param>
		/// <param name="to">The location the unit is to be moved to.</param>
		/// <returns>Returns true if the unit was moved and false if it was not.</returns>
		/// <remarks>Note that two units can not occupy the same location and attemption to do so will not work.</remarks>
		public bool MoveEnemy(Pair<int,int> from, Pair<int,int> to = null)
		{
			if(to == null)
				return EnemyUnits.Remove(from);

			Unit u = null;

			if(EnemyUnits.ContainsKey(to) || !EnemyUnits.TryGetValue(from,out u))
				return false;

			EnemyUnits.Remove(from);
			EnemyUnits.Add(to,u);

			return true;
		}

		/// <summary>
		/// Removes the ally at the given location, if any, from the board.
		/// </summary>
		/// <param name="from">The location of the allied unit.</param>
		/// <returns>Returns true if there was an allied unit removed and false otherwise.</returns>
		public bool RemoveAlly(Pair<int,int> from)
		{return MoveAlly(from);}

		/// <summary>
		/// Removes the enemy at the given location, if any, from the board.
		/// </summary>
		/// <param name="from">The location of the enemy unit.</param>
		/// <returns>Returns true if there was an enemy unit removed and false otherwise.</returns>
		public bool RemoveEnemy(Pair<int,int> from)
		{return MoveEnemy(from);}

		/// <summary>
		/// Recalculates fog of war and invisibility.
		/// </summary>
		public void RecalculateSight()
		{
			// Update fog of war
			sunny.Clear();

			foreach(KeyValuePair<Pair<int,int>,Unit> k in AllyUnits)
				foreach(Pair<int,int> loc in k.Value.AbilityVision.GetVisibleLocations(this,k.Key.val1,k.Key.val2))
				{
					int index = sunny.BinarySearch(loc,comp);

					if(index < 0)
						sunny.Insert(~index,loc);
				}

			// Update invisiblity
			see_invis.Clear();

			foreach(KeyValuePair<Pair<int,int>,Unit> k in AllyUnits)
				if(k.Value.AbilityVision.SeeInvisible)
					foreach(Pair<int,int> loc in k.Value.AbilityVision.GetVisibleLocations(this,k.Key.val1,k.Key.val2))
					{
						int index = see_invis.BinarySearch(loc,comp);

						if(index < 0)
							see_invis.Insert(~index,loc);
					}

			return;
		}

		/// <summary>
		/// Ends the turn by performing all necessary upkeep such as triggering passives and recalculating sight.
		/// </summary>
		/// <param name="state">The current state of the game.</param>
		/// <param name="moves">The moves that were peformed this turn.</param>
		public void EOT(GameState state, Dictionary<Unit,Move> moves)
		{
			float dummy = 0.0f;
			ATTACK_TYPE dummya = ATTACK_TYPE.TYPELESS;
			
			// We could go through AllyUnits and EnemyUnits but they may change during update so we'll just go through the moves list
			foreach(KeyValuePair<Unit,Move> k in moves)
				k.Key.TriggerPassives(TurnPhase.EOT,state,k.Value,ref dummy,ref dummya,k.Key); // Extra values ignored

			// Now that units updates are done recalc sight
			RecalculateSight();

			return;
		}

		/// <summary>
		/// The actual board itself.
		/// </summary>
		protected Dictionary<Pair<int,int>,Tile> tiles;

		/// <summary>
		/// Contains all the units on this board that are under the player's control.
		/// </summary>
		protected Dictionary<Pair<int,int>,Unit> AllyUnits;

		/// <summary>
		/// Contains all the units on this board that are not under the player's control.
		/// </summary>
		protected Dictionary<Pair<int,int>,Unit> EnemyUnits;
		
		/// <summary>
		/// The current width of the board.
		/// </summary>
		public int Width
		{
			get
			{return width;}
			
			set
			{
				if(value > Width)
					for(int i = Width;i < value;i++)
						for(int j = 0;j < Height;j++)
							tiles.Add(new Pair<int,int>(i,j),new Plain());
				else // If we are not changing the width then the loop will not execute
					for(int i = value;i < Width;i++)
						for(int j = 0;j < Height;j++)
							tiles.Remove(new Pair<int,int>(i,j));

				width = value;
				return;
			}
		}

		/// <summary>
		/// Stores the width of the board.
		/// </summary>
		protected int width = 0;

		/// <summary>
		/// The current height of the board.
		/// </summary>
		public int Height
		{
			get
			{return height;}
			
			set
			{
				if(value > Height)
					for(int i = Height;i < value;i++)
						for(int j = 0;j < Width;j++)
							tiles.Add(new Pair<int,int>(j,i),new Plain());
				else // If we are not changing the height then nothing will happen
					for(int i = value;i < Height;i++)
						for(int j = 0;j < Width;j++)
							tiles.Remove(new Pair<int,int>(j,i));

				height = value;
				return;
			}
		}

		/// <summary>
		/// Stores the height of the board.
		/// </summary>
		protected int height = 0;

		/// <summary>
		/// Returns a rectangle representing the dimensions of the board.
		/// </summary>
		public Rectangle Bounds
		{
			get
			{return new Rectangle(0,0,Width,Height);}
		}

		/// <summary>
		/// Contains all the locations that are not covered by fog of war.
		/// </summary>
		protected List<Pair<int,int>> sunny;

		/// <summary>
		/// Contains all the locations that invisible things can be seen at.
		/// </summary>
		protected List<Pair<int,int>> see_invis;

		/// <summary>
		/// So we don't have to keep making them and tossing them away.
		/// </summary>
		protected LocComparer comp;

		/// <summary>
		/// Compares elements of our registered items to keep them sorted.
		/// </summary>
		protected class LocComparer : IComparer<Pair<int,int>>
		{
			/// <summary>
			/// Compares the given objects.
			/// </summary>
			/// <param name="x">The first object.</param>
			/// <param name="y">The second object.</param>
			/// <returns>Returns negative if the name of the first command is less than the second, zero if equal, and positive if the first is greater than the second.</returns>
			public int Compare(Pair<int,int> x, Pair<int,int> y)
			{
				if(x.val2 != y.val2)
					return x.val2 - y.val2;

				return x.val1 - y.val1;
			}
		}
	}
}
