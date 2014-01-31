class ArtifactLightningRodPickup extends RPGArtifactPickup;

defaultproperties
{
	PickupMessage="You got the Lightning Rod!"
	InventoryType=class'ArtifactLightningRod'
	DrawType=DT_StaticMesh
	DrawScale=0.250
	StaticMesh=StaticMesh'UTRPGStatics.Artifacts.Lightning'
	PickupSound=Sound'PickupSounds.SniperRiflePickup'
	PickupForce="SniperRiflePickup"
	AmbientGlow=128
}
