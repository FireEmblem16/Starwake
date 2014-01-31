class DamTypeLightningRod extends DamageType
	abstract;

static function GetHitEffects(out class<xEmitter> HitEffects[4], int VictemHealth)
{
    HitEffects[0] = class'HitSmoke';
    if (Rand(25) > VictemHealth)
	HitEffects[1] = class'HitFlame';
}

defaultproperties
{
    DeathString="%o was electrocuted by %k's lightning rod."
    MaleSuicide="%o had an electrifying experience."
    FemaleSuicide="%o had an electrifying experience."

    DamageOverlayMaterial=Material'XGameShaders.PlayerShaders.LightningHit'
    DamageOverlayTime=1.0
    GibPerterbation=0.25
    bCauseConvulsions=true
}
