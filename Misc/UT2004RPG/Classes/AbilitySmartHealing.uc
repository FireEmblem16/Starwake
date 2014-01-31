class AbilitySmartHealing extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	local int x;	

	if (Data.HealthBonus < 50 + 15 * (CurrentLevel + 1))
		return 0;
	
	for (x = 0; x < Data.Abilities.length; x++)
		if (Data.Abilities[x] == class'AbilityRegen')
			return Super.Cost(Data, CurrentLevel);
	
	return 0;
}

static function bool OverridePickupQuery(Pawn Other, Pickup item, out byte bAllowPickup, int AbilityLevel)
{
	local int HealMax;

	if (TournamentHealth(item) != None)
	{
		HealMax = TournamentHealth(item).GetHealMax(Other);
		if (Other.Health + TournamentHealth(item).HealingAmount < HealMax)
		{
			Other.GiveHealth(int(float(TournamentHealth(item).HealingAmount) * 0.25 * AbilityLevel), HealMax);
			bAllowPickup = 1;
			return true;
		}
	}

	return false;
}

defaultproperties
{
	AbilityName="Smart Healing"
	Description="Causes healing items to heal you an addition 25% per level. You need to have a Health Bonus stat of at least 50 plus 15 for every level of this ability you wish to purchase. Also requires at least one level of Regeneration. (Max Level: 4)"
	StartingCost=5
	CostAddPerLevel=0
	MaxLevel=4
}
