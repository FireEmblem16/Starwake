class AbilityAdrenalineRegen extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.AdrenalineMax < 100 + 8 * (CurrentLevel + 1))
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static simulated function ModifyPawn(Pawn Other, int AbilityLevel)
{
	local AdrenRegenInv R;
	local Inventory Inv;

	if (Other.Role != ROLE_Authority)
		return;

	//remove old one, if it exists
	//might happen if player levels up this ability while still alive
	Inv = Other.FindInventoryType(class'AdrenRegenInv');
	if (Inv != None)
		Inv.Destroy();

	R = Other.spawn(class'AdrenRegenInv', Other,,,rot(0,0,0));
	R.RegenAmount = AbilityLevel;
	R.GiveTo(Other);
}

defaultproperties
{
	AbilityName="Adrenal Drip"
	Description="Gives 1 adrenaline per 5 seconds per level. Does not give adrenaline while performing a combo. You must have spent eight points in your Adrenaline Max stat for each level of this ability you want to purchase. (Max Level: 5)"
	StartingCost=5
	CostAddPerLevel=2
	MaxLevel=5
}
