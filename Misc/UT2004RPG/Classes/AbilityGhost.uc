class AbilityGhost extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	local int x;

	if (Data.HealthBonus < 45 || Data.Defense < 37)
		return 0;
	for (x = 0; x < Data.Abilities.length; x++)
		if (Data.Abilities[x] == class'AbilityUltima')
			return 0;

	return Super.Cost(Data, CurrentLevel);
}

static function bool PreventDeath(Pawn Killed, Controller Killer, class<DamageType> DamageType, vector HitLocation, int AbilityLevel, bool bAlreadyPrevented)
{
	local GhostInv Inv;
	local Vehicle V;

	if (Killed.Location.Z < Killed.Region.Zone.KillZ)
		return false;

	//spacefighters destroy all their inventory on possess, so if we do anything here it will never die
	//because our marker will get destroyed afterward
	if ( Killed.IsA('ASVehicle_SpaceFighter')
	     || (Killed.DrivenVehicle != None && Killed.DrivenVehicle.IsA('ASVehicle_SpaceFighter')) )
		return false;

	//this ability doesn't work with SVehicles or any kind of turret (can't change their physics)
	if (Killed.bStationary || Killed.IsA('SVehicle'))
	{
		//but maybe we can save the driver!
		V = Vehicle(Killed);
		if (V != None && !V.bRemoteControlled && !V.bEjectDriver && V.Driver != None)
			V.Driver.Died(Killer, DamageType, HitLocation);
		return false;
	}

	Inv = GhostInv(Killed.FindInventoryType(class'GhostInv'));
	if (Inv != None)
		return false;

	//ability won't work if pawn is still attached to the vehicle
	if (Killed.DrivenVehicle != None)
	{
		Killed.Health = 1; //so vehicle will properly kick pawn out
		Killed.DrivenVehicle.KDriverLeave(true);
	}

	Inv = Killed.spawn(class'GhostInv', Killed,,, rot(0,0,0));
	Inv.OwnerAbilityLevel = AbilityLevel;
	Inv.GiveTo(Killed);
	return true;
}

static function bool PreventSever(Pawn Killed, name boneName, int Damage, class<DamageType> DamageType, int AbilityLevel)
{
	local GhostInv Inv;

	Inv = GhostInv(Killed.FindInventoryType(class'GhostInv'));
	if (Inv != None)
		return false;

	return true;
}

defaultproperties
{
	AbilityName="Ghost"
	Description="The first time each spawn that you take damage that would kill you, instead of dying you will become non-corporeal and move to a new location, where you will continue your life. At level 1 you will move slowly as a ghost and return with a health of 1. At level 2 you will move somewhat more quickly and will return with 100 health. At level 3 you will move fastest and will return with your normal starting health. You need to have at least 45 Health Bonus and 37 Damage Reduction to purchase this ability. You can't have both Ghost and Ultima at the same time. (Max Level: 3)"
	StartingCost=18
	CostAddPerLevel=4
	MaxLevel=3
}
