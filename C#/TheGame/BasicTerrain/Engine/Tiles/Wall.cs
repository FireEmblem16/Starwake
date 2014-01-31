using System;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.Tiles
{
	/// <summary>
	/// A tile representing a wall.
	/// </summary>
	public class Wall : Tile
	{
		/// <summary>
		/// Creates a new wall tile.
		/// </summary>
		public Wall()
		{return;}

		/// <summary>
		/// Registers this tile with the tile factory.
		/// </summary>
		public static void Initialize()
		{
			TileFactory.RegisterTile(new Pair<string,GenerateTile>("Wall",CreateWall));
			return;
		}

		/// <summary>
		/// Creates a new tile of this type.
		/// </summary>
		protected static Tile CreateWall()
		{return new Wall();}

		/// <summary>
		/// Draws the tile at the specified position.
		/// </summary>
		/// <param name="win">The window to draw with.</param>
		/// <param name="x">The x coordinate to draw at.</param>
		/// <param name="y">The y coordinate to draw at.</param>
		/// <param name="fog_of_war">If true then the tile should be covered in fog of war.</param>
		public void Draw(Window win, int x, int y, bool fog_of_war)
		{
			win.CursorPosition = new Pair<int,int>(x,y);

			if(fog_of_war)
				win.SetColor(ConsoleColor.DarkYellow,ConsoleColor.Black);
			else
				win.SetColor(ConsoleColor.Yellow,ConsoleColor.Black);
	
			win.Write("=");
			return;
		}

		/// <summary>
		/// Returns the character that represents this tile and the colors used to draw it.
		/// </summary>
		/// <param name="fog_of_war">If true then the tile should be covered in fog of war.</param>
		/// <returns>Returns the character that represents this tile and the colors it is drawn with ordered fore then back.</returns>
		public Pair<string,Pair<ConsoleColor,ConsoleColor>> GetImage(bool fog_of_war)
		{
			if(fog_of_war)
				return new Pair<string,Pair<ConsoleColor,ConsoleColor>>("=",new Pair<ConsoleColor,ConsoleColor>(ConsoleColor.DarkYellow,ConsoleColor.Black));

			return new Pair<string,Pair<ConsoleColor,ConsoleColor>>("=",new Pair<ConsoleColor,ConsoleColor>(ConsoleColor.Yellow,ConsoleColor.Black));
		}

		/// <summary>
		/// The name of the tile type.
		/// </summary>
		public string Name
		{
			get
			{return "Wall";}
		}

		/// <summary>
		/// If true then units on the ground can enter this tile.
		/// </summary>
		public bool CanGroundedUnitsEnter
		{
			get
			{return false;}
		}

		/// <summary>
		/// If true then grounded mounted units can enter this tile.
		/// </summary>
		public bool CanGroundedMountedUnitsEnter
		{
			get
			{return false;}
		}

		/// <summary>
		/// If true then units that can swim can enter this tile.
		/// </summary>
		public bool CanMarineUnitsEnter
		{
			get
			{return false;}
		}

		/// <summary>
		/// If true then flying units can enter this tile.
		/// </summary>
		public bool CanFlyingUnitsEnter
		{
			get
			{return true;}
		}

		/// <summary>
		/// If true then flying mounted units can enter this tile.
		/// </summary>
		public bool CanFlyingMountedUnitsEnter
		{
			get
			{return true;}
		}

		/// <summary>
		/// If true then this tile can be attacked through with a ranged attack.
		/// Similarly, if true then this tile does not obstruct the use of other abilities such as heal.
		/// </summary>
		public bool CanFireThrough
		{
			get
			{return true;}
		}

		/// <summary>
		/// If true then ranged magic can pass through this tile.
		/// </summary>
		public bool CanCastThrough
		{
			get
			{return true;}
		}

		/// <summary>
		/// If true then units can see through this tile.
		/// </summary>
		public bool CanSeeThrough
		{
			get
			{return false;}
		}
	}
}
