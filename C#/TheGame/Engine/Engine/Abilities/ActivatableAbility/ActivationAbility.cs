using TheGame.Engine.ActivatedAbilities;
using TheGame.Utility;

namespace TheGame.Engine.Abilities.ActivatableAbility
{
	/// <summary>
	/// Represents an ability that can be activated.
	/// </summary>
	public interface ActivationAbility : Ability
	{
		/// <summary>
		/// Creates an activation of this ability.
		/// </summary>
		/// <param name="target">The target location for this ability.</param>
		/// <returns>Returns a activation of this ability.</returns>
		ActivatedAbility Activate(Pair<int,int> target);

		/// <summary>
		/// The label to use for this ability during gameplay.
		/// </summary>
		string Label
		{get;}

		/// <summary>
		/// If true then this ability is a default ability used when no other ability is specified.
		/// </summary>
		/// <example>An example of this is the default melee attack which occurs by default after movement.</example>
		bool IsDefaultAbility
		{get;}
	}
}
