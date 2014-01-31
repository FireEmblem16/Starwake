class AbilityShieldStrength extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.HealthBonus < 42 * (CurrentLevel + 1))
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static simulated function ModifyPawn(Pawn Other, int AbilityLevel)
{
	if (xPawn(Other) != None)
		xPawn(Other).ShieldStrengthMax = xPawn(Other).default.ShieldStrengthMax + 50 * AbilityLevel;
}

defaultproperties
{
	AbilityName="Shields Up!"
	Description="Increases your maximum shield by 50 per level. You must have a Health Bonus stat of 42 per level of this ability you wish to purchase. (Max Level: 3)"
	StartingCost=19
	CostAddPerLevel=6
	MaxLevel=3
}
