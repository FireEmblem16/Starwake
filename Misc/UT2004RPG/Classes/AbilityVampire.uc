class AbilityVampire extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.Attack < 50 + 5 * CurrentLevel)
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static function HandleDamage(int Damage, Pawn Injured, Pawn Instigator, out vector Momentum, class<DamageType> DamageType, bool bOwnedByInstigator, int AbilityLevel)
{
	local int Health;

	if (!bOwnedByInstigator || DamageType == class'DamTypeRetaliation' || Injured == Instigator || Instigator == None)
		return;

	Health = int(float(Damage) * 0.05 * float(AbilityLevel));
	if (Health == 0)
		Health = 1;
	Instigator.GiveHealth(Health, Instigator.HealthMax + 50);
}

defaultproperties
{
	AbilityName="Vampirism"
	Description="Whenever you damage another player, you are healed for 5% of the damage per level (minimum 1, up to your starting health amount + 50). You can't gain health from self-damage and you can't gain health from damage caused by the Retaliation ability. You must have a Damage Bonus of at least 50 plus 5 for every level of this ability you have to purchase this ability. (Max Level: 10)"
	StartingCost=3
	CostAddPerLevel=1
	MaxLevel=10
}
