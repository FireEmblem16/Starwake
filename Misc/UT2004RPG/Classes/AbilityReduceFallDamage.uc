class AbilityReduceFallDamage extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.HealthBonus < 42)
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static simulated function ModifyPawn(Pawn Other, int AbilityLevel)
{
	Other.MaxFallSpeed = Other.default.MaxFallSpeed * (1.0 + 0.25 * float(AbilityLevel));
}

defaultproperties
{
	AbilityName="Iron Legs"
	Description="Increases the distance you can safely fall by 25% per level and reduces fall damage for distances still beyond your capacity to handle. Your Health Bonus stat must be at least 42 to purchase this ability. (Max Level: 10)"
	StartingCost=3
	CostAddPerLevel=1
	MaxLevel=10
	BotChance=6 //slightly higher because it makes the bots willing to take more paths
}
