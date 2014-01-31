class ArtifactTripleDamage extends RPGArtifact;

var Weapon LastWeapon;

function BotConsider()
{
	if (Instigator.Controller.Adrenaline < 30)
		return;

	if (bActive && (Instigator.Controller.Enemy == None || !Instigator.Controller.CanSee(Instigator.Controller.Enemy)))
		Activate();
	else if ( !bActive && Instigator.Controller.Enemy != None && Instigator.Weapon != None && Instigator.Weapon.AIRating > 0.5
		  && Instigator.Controller.Enemy.Health > 70 && Instigator.Controller.CanSee(Instigator.Controller.Enemy) && NoArtifactsActive() && FRand() < 0.7 )
		Activate();
}

function Activate()
{
	if (!bActive && Instigator.HasUDamage())
		return;

	Super.Activate();
}

function bool HandlePickupQuery(Pickup Item)
{
	if (Super.HandlePickupQuery(Item))
		return true;
	if (UDamagePack(Item) != None && bActive)
		Activate();

	return false;
}

state Activated
{
	function BeginState()
	{
		Instigator.DamageScaling *= 1.5;
		//Instigator.EnableUDamage(Instigator.Controller.Adrenaline / CostPerSec);
		Instigator.EnableUDamage(1000000);
		bActive = true;
	}

	function EndState()
	{
		if (Instigator != None)
		{
			Instigator.DamageScaling /= 1.5;
			Instigator.DisableUDamage();
		}
		bActive = false;
	}
}

defaultproperties
{
	CostPerSec=7
	PickupClass=class'ArtifactTripleDamagePickup'
	ItemName="Triple Damage"
	IconMaterial=Material'UTRPGTextures.Icons.TripleDamageIcon'
}
