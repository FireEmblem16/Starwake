class AbilityReduceSelfDamage extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.HealthBonus < 20 || Data.Defense < 33)
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static function HandleDamage(int Damage, Pawn Injured, Pawn Instigator, out vector Momentum, class<DamageType> DamageType, bool bOwnedByInstigator, int AbilityLevel)
{
	if (Injured != Instigator || !bOwnedByInstigator || DamageType == class'Fell')
		return;

	//Kind of hack-y, but better than allowing abilities to directly modify the damage, which would introduce all sorts of "Who gets to go first?" types of problems
	if (Instigator.ShieldStrength > 0)
		Instigator.ShieldStrength += int(float(Damage) * 0.25 * AbilityLevel);
	else
		Instigator.Health += int(float(Damage) * 0.25 * AbilityLevel);
}

defaultproperties
{
	AbilityName="Cautiousness"
	Description="Reduces self damage by 25% per level. Your Health Bonus stat must be at least 20 and your Damage Reduction stat at least 33 to purchase this ability. (Max Level: 4)"
	StartingCost=9
	CostAddPerLevel=0
	MaxLevel=4
}
