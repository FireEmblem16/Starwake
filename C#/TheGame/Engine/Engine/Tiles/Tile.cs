using System;
using System.Collections.Generic;
using System.Reflection;
using TheGame.Graphics;
using TheGame.Utility;

namespace TheGame.Engine.Tiles
{
	/// <summary>
	/// Represents a single tile of territory.
	/// </summary>
	public interface Tile
	{
		/// <summary>
		/// Draws the tile at the specified position.
		/// </summary>
		/// <param name="win">The window to draw with.</param>
		/// <param name="x">The x coordinate to draw at.</param>
		/// <param name="y">The y coordinate to draw at.</param>
		/// <param name="fog_of_war">If true then the tile should be covered in fog of war.</param>
		void Draw(Window win, int x, int y, bool fog_of_war);

		/// <summary>
		/// Returns the character that represents this tile and the colors used to draw it.
		/// </summary>
		/// <param name="fog_of_war">If true then the tile should be covered in fog of war.</param>
		/// <returns>Returns the character that represents this tile and the colors it is drawn with ordered fore then back.</returns>
		Pair<string,Pair<ConsoleColor,ConsoleColor>> GetImage(bool fog_of_war);

		/// <summary>
		/// The name of the tile type.
		/// </summary>
		string Name
		{get;}

		/// <summary>
		/// If true then units on the ground can enter this tile.
		/// </summary>
		bool CanGroundedUnitsEnter
		{get;}

		/// <summary>
		/// If true then grounded mounted units can enter this tile.
		/// </summary>
		bool CanGroundedMountedUnitsEnter
		{get;}

		/// <summary>
		/// If true then units that can swim can enter this tile.
		/// </summary>
		bool CanMarineUnitsEnter
		{get;}

		/// <summary>
		/// If true then flying units can enter this tile.
		/// </summary>
		bool CanFlyingUnitsEnter
		{get;}

		/// <summary>
		/// If true then flying mounted units can enter this tile.
		/// </summary>
		bool CanFlyingMountedUnitsEnter
		{get;}

		/// <summary>
		/// If true then this tile can be attacked through with a ranged attack.
		/// </summary>
		bool CanFireThrough
		{get;}

		/// <summary>
		/// If true then ranged magic can pass through this tile.
		/// </summary>
		bool CanCastThrough
		{get;}

		/// <summary>
		/// If true then units can see through this tile.
		/// </summary>
		bool CanSeeThrough
		{get;}
	}

	/// <summary>
	/// Contains functions that aid in Tile creation.
	/// </summary>
	public static class TileFactory
	{
		/// <summary>
		/// Being a static class this gets called when the assembly is loaded.
		/// </summary>
		static TileFactory()
		{
			tile_generator = new Factory<string,Tile>();
			return;
		}

		/// <summary>
		/// Registers a new tile in the factory.
		/// </summary>
		/// <param name="tile">The tile to add. The first value is the name of the tile. The second value is the delegate that will produce the tile.</param>
		public static void RegisterTile(Pair<string,GenerateTile> tile)
		{
			tile_generator.Register(new Pair<string,MethodInfo>(tile.val1,tile.val2.Method));
			return;
		}

		/// <summary>
		/// Remvoes a tile from the factory.
		/// </summary>
		/// <param name="tile">The name of the tile to remove.</param>
		public static void UnregisterTile(string tile)
		{
			tile_generator.Unregister(tile);
			return;
		}

		/// <summary>
		/// Unregisters all tiles.
		/// </summary>
		public static void UnregisterAllTiles()
		{
			tile_generator.UnregisterAll();
			return;
		}

		/// <summary>
		/// Creates a tile with the given type.
		/// </summary>
		/// <param name="name">The name of tile to create.</param>
		/// <returns>Returns a tile that has the given name.</returns>
		/// <exception cref="ArgumentException"></exception>
		public static Tile GenerateTile(string name)
		{
			Tile ret = tile_generator.CreateItem(name,null);

			if(ret == default(Tile))
				throw new ArgumentException(name + " - No such tile type exists.");

			return ret;
		}

		/// <summary>
		/// Creates a list of available tiles.
		/// </summary>
		/// <returns>Returns a list of strings listing all the tile names.</returns>
		public static List<string> Tiles()
		{
			return tile_generator.Items();
		}

		/// <summary>
		/// Creates tiles based on their names.
		/// </summary>
		private static Factory<string,Tile> tile_generator;
	}

	/// <summary>
	/// Generates a tile.
	/// </summary>
	/// <returns>Returns a tile.</returns>
	public delegate Tile GenerateTile();
}
