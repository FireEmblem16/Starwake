class ArtifactTeleportPickup extends RPGArtifactPickup;

defaultproperties
{
	PickupMessage="You got the Teleporter!"
	InventoryType=class'ArtifactTeleport'
	DrawType=DT_Mesh
	DrawScale=2.0
	Mesh=Mesh'Weapons.TransBeacon'
	bAmbientGlow=true
	AmbientGlow=128
	PickupSound=Sound'WeaponSounds.Translocator.TranslocatorModuleRegeneration'
	PickupForce="TranslocatorModuleRegeneration"
}
