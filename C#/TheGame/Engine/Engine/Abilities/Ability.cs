
using System;
using System.Collections.Generic;
using System.Reflection;
using TheGame.Utility;
namespace TheGame.Engine.Abilities
{
	/// <summary>
	/// The basic definition of what it means to be an ability.
	/// </summary>
	public interface Ability
	{
		/// <summary>
		/// Called when the ability is added to a unit.
		/// </summary>
		/// <param name="u">The unit this ability is added to.</param>
		void OnAdd(Unit u);

		/// <summary>
		/// Called when the ability is removed from a unit.
		/// </summary>
		/// <param name="u">The unit this ability is removed from.</param>
		void OnRemove(Unit u);

		/// <summary>
		/// Increases the level of the ability by one if able.
		/// </summary>
		/// <returns>Returns true if the ability was leveled up and false otherwise.</returns>
		bool LevelUp();
		
		/// <summary>
		/// Decreases the level of the ability by one if able.
		/// </summary>
		/// <returns>Returns true if the ability was deleveled and false otherwise.</returns>
		bool Unlevel();

		/// <summary>
		/// The name of the ability.
		/// </summary>
		string Name
		{get;}

		/// <summary>
		/// Documents and explains how the ability works, what it does and the costs.
		/// </summary>
		string Documentation
		{get;}

		/// <summary>
		/// The level the ability is currently at.
		/// </summary>
		int Level
		{get;}

		/// <summary>
		/// The maximum level of this ability.
		/// </summary>
		int MaxLevel
		{get;}

		/// <summary>
		/// The number of points required to have this ability.
		/// </summary>
		int PointCost
		{get;}

		/// <summary>
		/// The number of points required to level up the ability.
		/// Negative if the ability can not be leveled up further.
		/// </summary>
		int PointCostToNextLevel
		{get;}

		/// <summary>
		/// If true then this ability is visible to the user when editing.
		/// </summary>
		bool VisibleToUser
		{get;}
	}

	/// <summary>
	/// Contains functions that aid in Ability creation.
	/// </summary>
	public static class AbilityFactory
	{
		/// <summary>
		/// Being a static class this gets called when the assembly is loaded.
		/// </summary>
		static AbilityFactory()
		{
			ability_generator = new Factory<string,Ability>();
			return;
		}

		/// <summary>
		/// Registers a new ability in the factory.
		/// </summary>
		/// <param name="ability">The tile to add. The first value is the name of the ability. The second value is the delegate that will produce the ability.</param>
		public static void RegisterAbility(Pair<string,GenerateAbility> ability)
		{
			ability_generator.Register(new Pair<string,MethodInfo>(ability.val1,ability.val2.Method));
			return;
		}

		/// <summary>
		/// Remvoes an ability from the factory.
		/// </summary>
		/// <param name="Ability">The name of the ability to remove.</param>
		public static void UnregisterAbility(string ability)
		{
			ability_generator.Unregister(ability);
			return;
		}

		/// <summary>
		/// Unregisters all abilities.
		/// </summary>
		public static void UnregisterAllAbilities()
		{
			ability_generator.UnregisterAll();
			return;
		}

		/// <summary>
		/// Creates an ability with the given type.
		/// </summary>
		/// <param name="name">The name of the ability to create.</param>
		/// <returns>Returns an ability that has the given name.</returns>
		/// <exception cref="ArgumentException"></exception>
		public static Ability GenerateAbility(string name)
		{
			Ability ret = ability_generator.CreateItem(name,null);

			if(ret == default(Ability))
				throw new ArgumentException(name + " - No such ability exists.");

			return ret;
		}

		/// <summary>
		/// Creates a list of available tiles.
		/// </summary>
		/// <returns>Returns a list of strings listing all the tile names.</returns>
		public static List<string> Abilities()
		{
			return ability_generator.Items();
		}

		/// <summary>
		/// Creates abilities based on their names.
		/// </summary>
		private static Factory<string,Ability> ability_generator;
	}

	/// <summary>
	/// Generates an ability.
	/// </summary>
	/// <returns>Returns an ability.</returns>
	public delegate Ability GenerateAbility();
}
