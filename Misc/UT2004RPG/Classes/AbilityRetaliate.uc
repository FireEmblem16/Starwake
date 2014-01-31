class AbilityRetaliate extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.Defense < 10 * (CurrentLevel + 1))
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static function HandleDamage(int Damage, Pawn Injured, Pawn Instigator, out vector Momentum, class<DamageType> DamageType, bool bOwnedByInstigator, int AbilityLevel)
{
	if (bOwnedByInstigator || DamageType == class'DamTypeRetaliation' || Injured == Instigator || Instigator == None)
		return;

	Instigator.TakeDamage(int(float(Damage) * (0.05 * AbilityLevel)), Injured, Instigator.Location, vect(0,0,0), class'DamTypeRetaliation');
}

defaultproperties
{
	AbilityName="Retaliation"
	Description="Whenever you are damaged by another player, 5% of the damage per level is also done to the player that hurt you. Your Damage Bonus stat and your oponent's Damage Reduction stat are applied to this extra damage. You can't retaliate to retaliation damage. You must have a Damage Reduction of at least 10 times the level of this ability you wish to purchase. (Max Level: 10)"
	StartingCost=4
	CostAddPerLevel=2
	MaxLevel=10
}
