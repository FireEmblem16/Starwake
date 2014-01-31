class AbilityNoWeaponDrop extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.Level < 10)
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static function bool PreventDeath(Pawn Killed, Controller Killer, class<DamageType> DamageType, vector HitLocation, int AbilityLevel, bool bAlreadyPrevented)
{
	local OldWeaponHolder OldWeaponHolder;

	if (bAlreadyPrevented || Killed.Weapon == None || Vehicle(Killed) != None)
		return false;

	if (Killed.Controller != None)
		Killed.Controller.LastPawnWeapon = Killed.Weapon.Class;
	Killed.Weapon.DetachFromPawn(Killed);

	if (AbilityLevel > 1)
	{
		OldWeaponHolder = Killed.spawn(class'OldWeaponHolder',Killed.Controller);
		OldWeaponHolder.Weapon = Killed.Weapon;
		Killed.DeleteInventory(Killed.Weapon);
		OldWeaponHolder.Weapon.SetOwner(Killed.Controller); //this forces the weapon to stay relevant to the player who will soon reclaim it
		if (RPGWeapon(OldWeaponHolder.Weapon) != None)
			RPGWeapon(OldWeaponHolder.Weapon).ModifiedWeapon.SetOwner(Killed.Controller);
		OldWeaponHolder.AmmoAmounts[0] = OldWeaponHolder.Weapon.AmmoAmount(0);
		OldWeaponHolder.AmmoAmounts[1] = OldWeaponHolder.Weapon.AmmoAmount(1);
	}

	Killed.Weapon = None;

	return false;
}

static simulated function ModifyPawn(Pawn Other, int AbilityLevel)
{
	local OldWeaponHolder OldWeaponHolder;
	//local Ammunition Ammo;

	if (Other.Role != ROLE_Authority || AbilityLevel < 2)
		return;

	foreach Other.DynamicActors(class'OldWeaponHolder', OldWeaponHolder)
		if (OldWeaponHolder.Owner == Other.Controller)
		{
			/*if (OldWeaponHolder.Weapon.Ammo[0] != None)
			{
				Ammo = Ammunition(Other.FindInventoryType(OldWeaponHolder.Weapon.Ammo[0].Class));
				if (Ammo != None)
				{
					Ammo.AddAmmo(OldWeaponHolder.Weapon.Ammo[0].AmmoAmount);
					OldWeaponHolder.Weapon.Ammo[0].Destroy();
				}
				else
				{
					OldWeaponHolder.Weapon.Ammo[0].GiveTo(Other);
					OldWeaponHolder.Weapon.Ammo[0].AmmoAmount -= OldWeaponHolder.Weapon.Ammo[0].InitialAmount;
				}
			}
			if (OldWeaponHolder.Weapon.Ammo[1] != None && OldWeaponHolder.Weapon.Ammo[1] != OldWeaponHolder.Weapon.Ammo[0])
			{
				Ammo = Ammunition(Other.FindInventoryType(OldWeaponHolder.Weapon.Ammo[1].Class));
				if (Ammo != None)
				{
					Ammo.AddAmmo(OldWeaponHolder.Weapon.Ammo[1].AmmoAmount);
					OldWeaponHolder.Weapon.Ammo[1].Destroy();
				}
				else
				{
					OldWeaponHolder.Weapon.Ammo[1].GiveTo(Other);
					OldWeaponHolder.Weapon.Ammo[1].AmmoAmount -= OldWeaponHolder.Weapon.Ammo[1].InitialAmount;
				}
			}*/
			OldWeaponHolder.Weapon.GiveTo(Other);
			OldWeaponHolder.Weapon.AddAmmo(OldWeaponHolder.AmmoAmounts[0] - OldWeaponHolder.Weapon.AmmoAmount(0), 0);
			OldWeaponHolder.Weapon.AddAmmo(OldWeaponHolder.AmmoAmounts[1] - OldWeaponHolder.Weapon.AmmoAmount(1), 1);
			OldWeaponHolder.Weapon = None;
			OldWeaponHolder.Destroy();
			return;
		}
}

defaultproperties
{
	AbilityName="Denial"
	Description="The first level of this ability simply prevents you from dropping a weapon when you die (but you don't get it either). The second level allows you to respawn with the weapon and ammo you were using when you died. You need to be at least Level 10 to purchase this ability. (Max Level: 2)"
	StartingCost=10
	CostAddPerLevel=0
	MaxLevel=2
}
