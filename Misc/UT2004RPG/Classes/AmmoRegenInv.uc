class AmmoRegenInv extends Inventory;

var int RegenAmount;

function PostBeginPlay()
{
	SetTimer(3.0, true);

	Super.PostBeginPlay();
}

function Timer()
{
	local Inventory Inv;
	local Ammunition Ammo;
	local Weapon W;

	if (Instigator == None || Instigator.Health <= 0)
	{
		Destroy();
		return;
	}

	for (Inv = Instigator.Inventory; Inv != None; Inv = Inv.Inventory)
	{
		W = Weapon(Inv);
		if (W != None)
		{
			if ( W.bNoAmmoInstances && W.AmmoClass[0] != None && W.AmmoClass[0].default.MaxAmmo >= 5 && !W.IsA('Redeemer')
			     && !W.IsA('Painter') && !W.IsA('SCannon') )
			{
				W.AddAmmo(RegenAmount * (1 + W.AmmoClass[0].default.MaxAmmo / 100), 0);
				if (W.AmmoClass[0] != W.AmmoClass[1] && W.AmmoClass[1] != None)
					W.AddAmmo(RegenAmount * (1 + W.AmmoClass[1].default.MaxAmmo / 100), 1);
			}
		}
		else
		{
			Ammo = Ammunition(Inv);
			if ( Ammo != None && Ammo.default.MaxAmmo >= 5 && !Inv.IsA('RedeemerAmmo') && !Inv.IsA('BallAmmo') && !Inv.IsA('TransAmmo')
			     && !Inv.IsA('SCannonAmmo') )
				Ammo.AddAmmo(RegenAmount * (1 + Ammo.default.MaxAmmo / 100));
		}
	}
}
