namespace TheGame.Engine
{
	/// <summary>
	/// The phase (or event) of the turn.
	/// </summary>
	public enum TurnPhase
	{
		BEFORE_MOVE = 0,
		AFTER_MOVE = 1,
		BEFORE_ABILITY = 2,
		AFTER_ABILITY = 3,
		BEFORE_ATTACK = 4,
		AFTER_ATTACK = 5,
		BEFORE_DAMAGE = 6,
		AFTER_DAMAGE = 7,
		BEFORE_HEAL = 8,
		AFTER_HEAL = 9,
		BEFORE_POST_MOVE = 10,
		AFTER_POST_MOVE = 11,
		ON_DEATH = 12,
		ON_KILL = 13,
		EOT = 14
	}
}
