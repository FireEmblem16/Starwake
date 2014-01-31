class RW_Damage extends RPGWeapon
	HideDropDown
	CacheExempt;

function AdjustTargetDamage(out int Damage, Actor Victim, Vector HitLocation, out Vector Momentum, class<DamageType> DamageType)
{
	if (!bIdentified)
		Identify();
	Damage = Max(1, Damage * (1.0 + 0.1 * Modifier));
	Momentum *= 1.0 + 0.1 * Modifier;
}

defaultproperties
{
	MaxModifier=5
	MinModifier=-3
	AIRatingBonus=0.03
}
