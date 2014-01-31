class RW_Infinity extends RPGWeapon
	HideDropDown
	CacheExempt;

static function bool AllowedFor(class<Weapon> Weapon, Pawn Other)
{
	if ( ClassIsChildOf(Weapon, class'Redeemer') || ClassIsChildOf(Weapon, class'Painter')
	     || ( Weapon.default.FireModeClass[0] != None && Weapon.default.FireModeClass[0].default.AmmoClass != None
	          && Weapon.default.FireModeClass[0].default.AmmoClass.default.MaxAmmo < 5 ) )
		return false;

	return true;
}

simulated function bool StartFire(int Mode)
{
	if (!bIdentified && Role == ROLE_Authority)
		Identify();

	return Super.StartFire(Mode);
}

function bool ConsumeAmmo(int Mode, float Load, bool bAmountNeededIsMax)
{
	if (!bIdentified)
		Identify();

	return true;
}

simulated function WeaponTick(float dt)
{
	MaxOutAmmo();

	Super.WeaponTick(dt);
}

defaultproperties
{
	PostFixPos=" of Infinity"
	MaxModifier=0
	bCanHaveZeroModifier=true
	AIRatingBonus=0.05
}
