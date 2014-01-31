class AbilityJumpZ extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.Level < 4 * (CurrentLevel + 1))
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static simulated function ModifyPawn(Pawn Other, int AbilityLevel)
{
	Other.JumpZ = Other.default.JumpZ * (1.0 + 0.2 * float(AbilityLevel));
}

defaultproperties
{
	AbilityName="Power Jump"
	Description="Increases your jumping height by 20% per level. The Speed adrenaline combo will stack with this effect. You must be a Level equal to four times the ability level you wish to have before you can purchase it. (Max Level: 5)"
	StartingCost=7
	CostAddPerLevel=0
	MaxLevel=5
}
