class ArtifactMonsterSummonPickup extends RPGArtifactPickup;

defaultproperties
{
	PickupMessage="You got the Summoning Charm!"
	InventoryType=class'ArtifactMonsterSummon'
	DrawType=DT_StaticMesh
	DrawScale=0.25
	StaticMesh=StaticMesh'UTRPGStatics.Artifacts.MonsterCoin'
	bAmbientGlow=true
	AmbientGlow=128
	PickupSound=Sound'WeaponSounds.Translocator.TranslocatorModuleRegeneration'
	PickupForce="TranslocatorModuleRegeneration"
}
