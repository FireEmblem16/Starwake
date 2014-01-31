class RW_Protection extends RPGWeapon
	HideDropDown
	CacheExempt;

function AdjustPlayerDamage(out int Damage, Pawn InstigatedBy, Vector HitLocation, out Vector Momentum, class<DamageType> DamageType)
{
	if (!bIdentified)
		Identify();
	Damage -= Damage * (0.1 * Modifier);
	Super.AdjustPlayerDamage(Damage, InstigatedBy, HitLocation, Momentum, DamageType);
}

defaultproperties
{
	MaxModifier=5
	PostFixPos=" of Protection"
	ModifierOverlay=Shader'XGameShaders.PlayerShaders.PlayerShieldSh'
	AIRatingBonus=0.04
}
