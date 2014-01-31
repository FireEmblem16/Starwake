namespace TheGame.Engine.Abilities.PassiveAbilities.Ailment
{
	/// <summary>
	/// A passive ability that is a status effect like poison.
	/// Note that status effects do not need to be initialized because the classes that will create them will have knowledge of them.
	/// </summary>
	public interface StatusEffect : PassiveAbility
	{}
}
