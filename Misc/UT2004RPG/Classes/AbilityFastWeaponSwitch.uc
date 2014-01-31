class AbilityFastWeaponSwitch extends RPGAbility
	abstract;

static function bool AbilityIsAllowed(GameInfo Game, MutUT2004RPG RPGMut)
{
	//if (RPGMut.WeaponModifierChance <= 0)
	//	return false;

	return true;
}

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.WeaponSpeed < 70)
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static simulated function ModifyWeapon(Weapon Weapon, int AbilityLevel)
{
	local float Modifier;
	local RPGWeapon RW;

	if (!Weapon.Instigator.IsLocallyControlled())
		return;

	Modifier = 1.0 + (0.15 * AbilityLevel);
	RW = RPGWeapon(Weapon);
	if (RW != None)
	{
		RW.ModifiedWeapon.BringUpTime = RW.ModifiedWeapon.default.BringUpTime / Modifier;
		RW.ModifiedWeapon.PutDownTime = RW.ModifiedWeapon.default.PutDownTime / Modifier;
		RW.ModifiedWeapon.MinReloadPct = RW.ModifiedWeapon.default.MinReloadPct / Modifier;
		RW.ModifiedWeapon.PutDownAnimRate = RW.ModifiedWeapon.default.PutDownAnimRate * Modifier;
		RW.ModifiedWeapon.SelectAnimRate = RW.ModifiedWeapon.default.SelectAnimRate * Modifier;
	}
	else
	{	Weapon.BringUpTime = Weapon.default.BringUpTime / Modifier;
		Weapon.PutDownTime = Weapon.default.PutDownTime / Modifier;
		Weapon.MinReloadPct = Weapon.default.MinReloadPct / Modifier;
		Weapon.PutDownAnimRate = Weapon.default.PutDownAnimRate * Modifier;
		Weapon.SelectAnimRate = Weapon.default.SelectAnimRate * Modifier;
	}
}

defaultproperties
{
	AbilityName="Speed Switcher"
	Description="For each level of this ability, you switch weapons 15% faster. You need to have at least 70 Weapon Speed before you can purchase this ability. (Max Level: 10)"
	StartingCost=2
	CostAddPerLevel=1
	MaxLevel=10
	BotChance=3
}
