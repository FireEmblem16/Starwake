class AbilityAdrenalineSurge extends RPGAbility
	abstract;

static simulated function int Cost(RPGPlayerDataObject Data, int CurrentLevel)
{
	if (Data.AdrenalineMax < 120 || Data.Attack < 30)
		return 0;
	else
		return Super.Cost(Data, CurrentLevel);
}

static function ScoreKill(Controller Killer, Controller Killed, bool bOwnedByKiller, int AbilityLevel)
{
	if (!bOwnedByKiller)
		return;

	if (Killed.Level.Game.IsA('Invasion') && Killed.Pawn != None && Killed.Pawn.IsA('Monster'))
	{
		Killer.AwardAdrenaline(float(Killed.Pawn.GetPropertyText("ScoringValue")) * 0.5 * AbilityLevel);
		return;
	}

	if ( !(!Killed.Level.Game.bTeamGame || ((Killer != None) && (Killer != Killed) && (Killed != None)
		&& (Killer.PlayerReplicationInfo != None) && (Killed.PlayerReplicationInfo != None)
		&& (Killer.PlayerReplicationInfo.Team != Killed.PlayerReplicationInfo.Team))) )
		return;	//no bonus for team kills or suicides

	if (UnrealPlayer(Killer) != None && UnrealPlayer(Killer).MultiKillLevel > 0)
		Killer.AwardAdrenaline(Deathmatch(Killer.Level.Game).ADR_MajorKill * 0.5 * AbilityLevel);

	if (UnrealPawn(Killed.Pawn) != None && UnrealPawn(Killed.Pawn).spree > 4)
		Killer.AwardAdrenaline(Deathmatch(Killer.Level.Game).ADR_MajorKill * 0.5 * AbilityLevel);

	if ( Killer.PlayerReplicationInfo.Kills == 1 && TeamPlayerReplicationInfo(Killer.PlayerReplicationInfo) != None
	     && TeamPlayerReplicationInfo(Killer.PlayerReplicationInfo).bFirstBlood )
		Killer.AwardAdrenaline(Deathmatch(Killer.Level.Game).ADR_MajorKill * 0.5 * AbilityLevel);

	if (Killer.bIsPlayer && Killed.bIsPlayer)
		Killer.AwardAdrenaline(Deathmatch(Killer.Level.Game).ADR_Kill * 0.5 * AbilityLevel);
}

defaultproperties
{
	AbilityName="Adrenal Surge"
	Description="For each level of this ability, you gain 50% more adrenaline from all kill related adrenaline bonuses. You must have a Damage Bonus of at least 30 and an Adrenaline Max stat at least 120 to purchase this ability. (Max Level: 3)"
	StartingCost=7
	CostAddPerLevel=6
	MaxLevel=3
}
