class RW_NoMomentum extends RPGWeapon
	HideDropDown
	CacheExempt;

function AdjustPlayerDamage(out int Damage, Pawn InstigatedBy, Vector HitLocation, out Vector Momentum, class<DamageType> DamageType)
{
	if (!bIdentified)
		Identify();
	Momentum = vect(0,0,0);
	Super.AdjustPlayerDamage(Damage, InstigatedBy, HitLocation, Momentum, DamageType);
}

defaultproperties
{
	bCanHaveZeroModifier=true
	PreFixPos="Sturdy "
	AIRatingBonus=0.7
}
