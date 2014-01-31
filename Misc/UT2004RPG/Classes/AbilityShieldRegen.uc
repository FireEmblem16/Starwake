//Regeneration ability
class AbilityShieldRegen extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	local int x;
	local int b;
	
	b = -1;
	
	for (x = 0; x < Data.Abilities.length; x++)
		if (Data.Abilities[x] == class'AbilityShieldStrength')
			b = 1;
			
	if(b < 0)
		return 0;
	
	for (x = 0; x < Data.Abilities.length; x++)
		if (Data.Abilities[x] == class'AbilityRegen')
			return Super.Cost(Data, CurrentLevel);
			
	return 0;
}

static simulated function ModifyPawn(Pawn Other, int AbilityLevel)
{
	local ShieldRegenInv R;
	local Inventory Inv;

	if (Other.Role != ROLE_Authority)
		return;

	//remove old one, if it exists
	//might happen if player levels up this ability while still alive
	Inv = Other.FindInventoryType(class'ShieldRegenInv');
	if (Inv != None)
		Inv.Destroy();

	R = Other.spawn(class'ShieldRegenInv', Other,,,rot(0,0,0));
	R.RegenAmount = AbilityLevel;
	R.GiveTo(Other);
}

defaultproperties
{
	AbilityName="Shield Regeneration"
	Description="Heals 1 shield per second per level. Does not heal past max shield amount. You must have at least one level of Shield Strength and Regeneration to purchase this ability. (Max Level: 5)"
	StartingCost=7
	CostAddPerLevel=3
	MaxLevel=5
}
