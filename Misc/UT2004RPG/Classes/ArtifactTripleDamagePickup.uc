class ArtifactTripleDamagePickup extends RPGArtifactPickup;

defaultproperties
{
	PickupMessage="You got the Triple Damage!"
	InventoryType=class'ArtifactTripleDamage'
	DrawType=DT_StaticMesh
	DrawScale=0.9
	StaticMesh=StaticMesh'E_Pickups.TripleDamage'
	AmbientGlow=254
	Style=STY_AlphaZ
	ScaleGlow=0.6
	PickupForce="UDamagePickup"
	PickupSound=sound'PickupSounds.UDamagePickUp'
}
