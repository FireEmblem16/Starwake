namespace TheGame.Engine.Abilities.PassiveAbilities.Movement
{
	/// <summary>
	/// Allows the type of movement a unit has to modify the type of damage it deals.
	/// For example, mounted units should deal mounted damage.
	/// Note that we can only get these from taking the movement type so we don't initialize any of the subclasses.
	/// </summary>
	public interface MovementAttackModifier : PassiveAbility
	{}
}
