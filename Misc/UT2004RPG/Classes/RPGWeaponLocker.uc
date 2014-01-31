class RPGWeaponLocker extends WeaponLocker
	notplaceable;

var MutUT2004RPG RPGMut;

function inventory SpawnCopy( pawn Other )
{
	local inventory Copy;
	local RPGWeapon OldWeapon;
	local RPGStatsInv StatsInv;
	local class<RPGWeapon> NewWeaponClass;
	local int x;
	local bool bRemoveReference;

	if ( Inventory != None )
		Inventory.Destroy();

	//if player previously had a weapon of class InventoryType, force modifier to be the same
	StatsInv = RPGStatsInv(Other.FindInventoryType(class'RPGStatsInv'));
	if (StatsInv != None)
		for (x = 0; x < StatsInv.OldRPGWeapons.length; x++)
			if (StatsInv.OldRPGWeapons[x].ModifiedClass == InventoryType)
			{
				OldWeapon = StatsInv.OldRPGWeapons[x].Weapon;
				if (OldWeapon == None)
				{
					StatsInv.OldRPGWeapons.Remove(x, 1);
					x--;
				}
				else
				{
					NewWeaponClass = OldWeapon.Class;
					StatsInv.OldRPGWeapons.Remove(x, 1);
					bRemoveReference = true;
					break;
				}
			}

	if (NewWeaponClass == None)
		NewWeaponClass = RPGMut.GetRandomWeaponModifier(class<Weapon>(InventoryType), Other);

	Copy = spawn(NewWeaponClass,Other,,,rot(0,0,0));
	RPGWeapon(Copy).Generate(OldWeapon);
	RPGWeapon(Copy).SetModifiedWeapon(Weapon(spawn(InventoryType,Other,,,rot(0,0,0))), ((bDropped && OldWeapon != None && OldWeapon.bIdentified) || RPGMut.bNoUnidentified));

	Copy.GiveTo(Other, self);

	if (bRemoveReference)
		OldWeapon.RemoveReference();

	return Copy;
}

function Tick(float deltaTime)
{
	local int i;

	MaxDesireability = 0;

	if (bHidden)
		return;
	for (i = 0; i < Weapons.Length; i++)
		MaxDesireability += Weapons[i].WeaponClass.Default.AIRating;
	SpawnLockerWeapon();

	disable('Tick');
	bStasis = true;
}

auto state LockerPickup
{
	simulated function Touch( actor Other )
	{
		local Weapon Copy;
		local int i;
		local Inventory Inv;
		local Pawn P;

		// If touched by a player pawn, let him pick this up.
		if( ValidTouch(Other) )
		{
			P = Pawn(Other);
			if ( (PlayerController(P.Controller) != None) && (Viewport(PlayerController(P.Controller).Player) != None) )
			{
				if ( (Effect != None) && !Effect.bHidden )
					Effect.TurnOff(30);
			}
			if ( Role < ROLE_Authority )
				return;
			if ( !AddCustomer(P) )
				return;
			TriggerEvent(Event, self, P);
			for ( i=0; i<Weapons.Length; i++ )
			{
				InventoryType = Weapons[i].WeaponClass;
				Copy = None;
				for (Inv = P.Inventory; Inv != None; Inv = Inv.Inventory)
					if ( Inv.Class == Weapons[i].WeaponClass
					     || (RPGWeapon(Inv) != None && RPGWeapon(Inv).ModifiedWeapon.Class == Weapons[i].WeaponClass) )
					{
						Copy = Weapon(Inv);
						break;
					}
				if ( Copy != None )
					Copy.FillToInitialAmmo();
				else if ( Level.Game.PickupQuery(P, self) )
				{
					Copy = Weapon(SpawnCopy(P));
					if ( Copy != None )
					{
						Copy.PickupFunction(P);
						if ( Weapons[i].ExtraAmmo > 0 )
							Copy.AddAmmo(Weapons[i].ExtraAmmo, 0);
					}
				}
			}

			AnnouncePickup(P);
		}
	}
}

defaultproperties
{
     bStatic=False
     bCollideWorld=False
}
