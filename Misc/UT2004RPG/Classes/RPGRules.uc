class RPGRules extends GameRules;

var MutUT2004RPG RPGMut;
var int PointsPerLevel;
var float LevelDiffExpGainDiv;
var bool bAwardedFirstBlood;

function PostBeginPlay()
{
	local GameObjective GO;

	SetTimer(Level.TimeDilation, true);

	//hack to deal with Assault's stupid hardcoded scoring setup
	if (Level.Game.IsA('ASGameInfo'))
		foreach AllActors(class'GameObjective', GO)
			GO.Score = 0;

	Super.PostBeginPlay();
}

function RPGStatsInv GetStatsInvFor(Controller C, optional bool bMustBeOwner)
{
	local Inventory Inv;

	for (Inv = C.Inventory; Inv != None; Inv = Inv.Inventory)
		if ( RPGStatsInv(Inv) != None && ( !bMustBeOwner || Inv.Owner == C || Inv.Owner == C.Pawn
						   || (Vehicle(C.Pawn) != None && Inv.Owner == Vehicle(C.Pawn).Driver) ) )
			return RPGStatsInv(Inv);

	//fallback - shouldn't happen
	if (C.Pawn != None)
	{
		Inv = C.Pawn.FindInventoryType(class'RPGStatsInv');
		if ( Inv != None && ( !bMustBeOwner || Inv.Owner == C || Inv.Owner == C.Pawn
				      || (Vehicle(C.Pawn) != None && Inv.Owner == Vehicle(C.Pawn).Driver) ) )
			return RPGStatsInv(Inv);
	}

	return None;
}

function ScoreKill(Controller Killer, Controller Killed)
{
	local RPGPlayerDataObject KillerData, KilledData;
	local int x, MonsterScore, LevelDifference;
	local Inventory Inv, NextInv;
	local RPGStatsInv StatsInv;
	local vector TossVel, U, V, W;

	Super.ScoreKill(Killer, Killed);

	//make killed pawn drop any artifacts he's got
	if (Killed.Pawn != None)
	{
		Inv = Killed.Pawn.Inventory;
		while (Inv != None)
		{
			NextInv = Inv.Inventory;
			if (RPGArtifact(Inv) != None)
			{
				TossVel = Vector(Killed.Pawn.GetViewRotation());
				TossVel = TossVel * ((Killed.Pawn.Velocity Dot TossVel) + 500) + Vect(0,0,200);
				TossVel += VRand() * (100 + Rand(250));
				Inv.Velocity = TossVel;
				Killed.Pawn.GetAxes(Killed.Pawn.Rotation, U, V, W);
				Inv.DropFrom(Killed.Pawn.Location + 0.8 * Killed.Pawn.CollisionRadius * U - 0.5 * Killed.Pawn.CollisionRadius * V);
			}
			Inv = NextInv;
		}
	}

	if (Killer == None || Killed == None)
		return;

	//exp for killing a monster
	if (Killed.Pawn != None && Killed.Pawn.IsA('Monster'))
	{
		MonsterScore = int(Killed.Pawn.GetPropertyText("ScoringValue"));

		StatsInv = GetStatsInvFor(Killer);
		if (StatsInv != None)
		{
			KillerData = StatsInv.DataObject;
			for (x = 0; x < KillerData.Abilities.length; x++)
				KillerData.Abilities[x].static.ScoreKill(Killer, Killed, true, KillerData.AbilityLevels[x]);
			KillerData.Experience += MonsterScore;
			RPGMut.CheckLevelUp(KillerData, Killer.PlayerReplicationInfo);
		}
		return;
	}

	if ( Killer == Killed || !Killed.bIsPlayer || !Killer.bIsPlayer
	     || (Killer.PlayerReplicationInfo != None && Killer.PlayerReplicationInfo.Team != None && Killed.PlayerReplicationInfo != None && Killer.PlayerReplicationInfo.Team == Killed.PlayerReplicationInfo.Team) )
		return;

	//get data
	StatsInv = GetStatsInvFor(Killer);
	if (StatsInv == None)
	{
		Log("KillerData not found for "$Killer.GetHumanReadableName());
		return;
	}
	KillerData = StatsInv.DataObject;

	StatsInv = GetStatsInvFor(Killed);
	if (StatsInv == None)
	{
		Log("KilledData not found for "$Killed.GetHumanReadableName());
		return;
	}
	KilledData = StatsInv.DataObject;

	for (x = 0; x < KillerData.Abilities.length; x++)
		KillerData.Abilities[x].static.ScoreKill(Killer, Killed, true, KillerData.AbilityLevels[x]);
	for (x = 0; x < KilledData.Abilities.length; x++)
		KilledData.Abilities[x].static.ScoreKill(Killer, Killed, false, KilledData.AbilityLevels[x]);

	LevelDifference = Max(0, KilledData.Level - KillerData.Level);
	if (LevelDifference > 0)
		LevelDifference = int(float(LevelDifference*LevelDifference) / LevelDiffExpGainDiv);
	//cap gained exp to enough to get to Killed's level
	if (KilledData.Level - KillerData.Level > 0 && LevelDifference > (KilledData.Level - KillerData.Level) * KilledData.NeededExp)
		LevelDifference = (KilledData.Level - KillerData.Level) * KilledData.NeededExp;
	KillerData.Experience += LevelDifference + 1;

	//bonus experience for multikills
	if (UnrealPlayer(Killer) != None && UnrealPlayer(Killer).MultiKillLevel > 0)
		KillerData.Experience += Min(Square(float(UnrealPlayer(Killer).MultiKillLevel)), 100);
	else if (AIController(Killer) != None && Killer.Pawn != None && Killer.Pawn.Inventory != None)
		Killer.Pawn.Inventory.OwnerEvent('RPGScoreKill');	//hack to record multikills for bots (handled by RPGStatsInv)

	//bonus experience for sprees
	if (Killer.Pawn != None && Killer.Pawn.GetSpree() % 5 == 0)
		KillerData.Experience += int(Square(float(Killer.Pawn.GetSpree() / 5 + 1)));

	//bonus experience for ending someone else's spree
	if (Killed.Pawn != None && Killed.Pawn.GetSpree() > 4)
		KillerData.Experience += Killed.Pawn.GetSpree() * 2 / 5;

	//bonus experience for first blood
	if (!bAwardedFirstBlood && TeamPlayerReplicationInfo(Killer.PlayerReplicationInfo) != None && TeamPlayerReplicationInfo(Killer.PlayerReplicationInfo).bFirstBlood)
	{
		KillerData.Experience += 2 * Max(KilledData.Level - KillerData.Level, 1);
		bAwardedFirstBlood = true;
	}

	//level up
	RPGMut.CheckLevelUp(KillerData, Killer.PlayerReplicationInfo);
}

//Give experience for game objectives
//Too bad Epic's code pretty much doesn't use this, but mods might take advantage of it
function ScoreObjective(PlayerReplicationInfo Scorer, Int Score)
{
	local RPGStatsInv StatsInv;

	if (Scorer != None && Scorer.Owner != None)
	{
		StatsInv = GetStatsInvFor(Controller(Scorer.Owner));
		if (StatsInv != None)
		{
			StatsInv.DataObject.Experience += Score;
			RPGMut.CheckLevelUp(StatsInv.DataObject, Scorer);
		}
	}

	Super.ScoreObjective(Scorer, Score);
}

function int NetDamage(int OriginalDamage, int Damage, pawn injured, pawn instigatedBy, vector HitLocation, out vector Momentum, class<DamageType> DamageType)
{
	local RPGPlayerDataObject InjuredData, InstigatedData;
	local RPGStatsInv InjuredStatsInv, InstigatedStatsInv;
	local int x, MonsterLevel;
	local FriendlyMonsterController C;

	if (injured == None || instigatedBy == None || injured.Controller == None || instigatedBy.Controller == None)
		return Super.NetDamage(OriginalDamage, Damage, injured, instigatedBy, HitLocation, Momentum, DamageType);

	C = FriendlyMonsterController(injured.Controller);
	if (C != None && C.Master != None)
	{
		if (C.Master == instigatedBy.Controller)
			Damage = OriginalDamage;
		else if (C.SameTeamAs(instigatedBy.Controller))
			Damage *= TeamGame(Level.Game).FriendlyFireScale;
	}

	if ( DamageType.default.bSuperWeapon || Damage >= 1000
	     || (Monster(injured) != None && FriendlyMonsterController(injured.Controller) == None && Monster(instigatedBy) != None && FriendlyMonsterController(instigatedBy.Controller) == None) )
		return Super.NetDamage(OriginalDamage, Damage, injured, instigatedBy, HitLocation, Momentum, DamageType);

	if (Damage <= 0)
	{
		Damage = Super.NetDamage(OriginalDamage, Damage, injured, instigatedBy, HitLocation, Momentum, DamageType);
		if (Damage <= 0)
			return Damage;
	}

	//get data
	InstigatedStatsInv = GetStatsInvFor(instigatedBy.Controller);
	if (InstigatedStatsInv != None)
		InstigatedData = InstigatedStatsInv.DataObject;

	InjuredStatsInv = GetStatsInvFor(injured.Controller);
	if (InjuredStatsInv != None)
		InjuredData = InjuredStatsInv.DataObject;

	if (InstigatedData == None || InjuredData == None)
	{
		if (Level.Game.IsA('Invasion'))
		{
			MonsterLevel = int(Level.Game.GetPropertyText("WaveNum")) + 1;
			if (RPGMut.bAutoAdjustInvasionLevel && RPGMut.Players.Length > 0 && RPGMut.CurrentLowestLevelPlayer != None)
				MonsterLevel += Max(0, (RPGMut.CurrentLowestLevelPlayer.Level - 20) * RPGMut.InvasionAutoAdjustFactor);
		}
		else if (RPGMut.CurrentLowestLevelPlayer != None)
			MonsterLevel = RPGMut.CurrentLowestLevelPlayer.Level;
		else
			MonsterLevel = 1;
		if ( InstigatedData == None && ( (Monster(instigatedBy) != None && FriendlyMonsterController(instigatedBy.Controller) == None)
						 || TurretController(instigatedBy.Controller) != None ) )
		{
			InstigatedData = RPGPlayerDataObject(Level.ObjectPool.AllocateObject(class'RPGPlayerDataObject'));
			InstigatedData.Attack = MonsterLevel / 2 * PointsPerLevel;
			InstigatedData.Defense = InstigatedData.Attack;
			InstigatedData.Level = MonsterLevel;
		}
		if ( InjuredData == None && ( (Monster(injured) != None && FriendlyMonsterController(injured.Controller) == None)
					      || TurretController(injured.Controller) != None ) )
		{
			InjuredData = RPGPlayerDataObject(Level.ObjectPool.AllocateObject(class'RPGPlayerDataObject'));
			InjuredData.Attack = MonsterLevel / 2 * PointsPerLevel;
			InjuredData.Defense = InjuredData.Attack;
			InjuredData.Level = MonsterLevel;
		}
	}

	if (InstigatedData == None)
	{
		//This should never happen
		Log("InstigatedData not found for "$instigatedBy.GetHumanReadableName());
		return Super.NetDamage(OriginalDamage, Damage, injured, instigatedBy, HitLocation, Momentum, DamageType);
	}
	if (InjuredData == None)
	{
		//This should never happen
		Log("InjuredData not found for "$injured.GetHumanReadableName());
		return Super.NetDamage(OriginalDamage, Damage, injured, instigatedBy, HitLocation, Momentum, DamageType);
	}

	//headshot bonus EXP
	if (DamageType == class'DamTypeSniperHeadShot' && InstigatedStatsInv != None)
	{
		InstigatedData.Experience++;
		RPGMut.CheckLevelUp(InstigatedData, InstigatedBy.PlayerReplicationInfo);
	}

	Damage += int((float(Damage) * (1.0 + float(InstigatedData.Attack) * 0.005)) - (float(Damage) * (1.0 + float(InjuredData.Defense) * 0.005)));

	if (Damage < 1)
		Damage = 1;

	//if player doing damage has an RPGWeapon, let it modify damage
	if (RPGWeapon(InstigatedBy.Weapon) != None)
		RPGWeapon(InstigatedBy.Weapon).AdjustTargetDamage(Damage, Injured, HitLocation, Momentum, DamageType);

	//Allow Abilities to react to damage
	if (InstigatedStatsInv != None)
	{
		for (x = 0; x < InstigatedData.Abilities.length; x++)
			InstigatedData.Abilities[x].static.HandleDamage(Damage, injured, instigatedBy, Momentum, DamageType, true, InstigatedData.AbilityLevels[x]);
	}
	else
		Level.ObjectPool.FreeObject(InstigatedData);
	if (InjuredStatsInv != None)
	{
		for (x = 0; x < InjuredData.Abilities.length; x++)
			InjuredData.Abilities[x].static.HandleDamage(Damage, injured, instigatedBy, Momentum, DamageType, false, InjuredData.AbilityLevels[x]);
	}
	else
		Level.ObjectPool.FreeObject(InjuredData);

	return Super.NetDamage(OriginalDamage, Damage, injured, instigatedBy, HitLocation, Momentum, DamageType);
}

function bool OverridePickupQuery(Pawn Other, Pickup item, out byte bAllowPickup)
{
	local RPGStatsInv StatsInv;
	local int x;

	//increase value of ammo pickups based on Max Ammo stat
	if (Other.Controller != None)
	{
		StatsInv = GetStatsInvFor(Other.Controller);
		if (StatsInv != None)
		{
			if (Ammo(item) != None)
				Ammo(item).AmmoAmount = int(Ammo(item).default.AmmoAmount * (1.0 + float(StatsInv.Data.AmmoMax) / 100.f));

			for (x = 0; x < StatsInv.Data.Abilities.length; x++)
				if (StatsInv.Data.Abilities[x].static.OverridePickupQuery(Other, item, bAllowPickup, StatsInv.Data.AbilityLevels[x]))
					return true;
		}
	}

	return Super.OverridePickupQuery(Other, item, bAllowPickup);
}

function bool PreventDeath(Pawn Killed, Controller Killer, class<DamageType> damageType, vector HitLocation)
{
	local bool bAlreadyPrevented;
	local int x;
	local RPGStatsInv StatsInv;
	local FriendlyMonsterKillMarker M;
	local TeamPlayerReplicationInfo TPRI;
	local Controller KilledController;

	bAlreadyPrevented = Super.PreventDeath(Killed, Killer, damageType, HitLocation);

	if (Killed.Controller != None)
		KilledController = Killed.Controller;
	else if (Killed.DrivenVehicle != None && Killed.DrivenVehicle.Controller != None)
		KilledController = Killed.DrivenVehicle.Controller;
	if (KilledController != None)
		StatsInv = GetStatsInvFor(KilledController, true);

	if (StatsInv != None)
	{
		//FIXME should Pawn even be calling PreventDeath() if its Controller is bPendingDelete?
		if (!KilledController.bPendingDelete)
		{
			for (x = 0; x < StatsInv.Data.Abilities.length; x++)
				if (StatsInv.Data.Abilities[x].static.PreventDeath(Killed, Killer, damageType, HitLocation, StatsInv.Data.AbilityLevels[x], bAlreadyPrevented))
					bAlreadyPrevented = true;
		}

		//tell StatsInv its owner died
		if (!bAlreadyPrevented)
			StatsInv.OwnerDied();
	}

	if (bAlreadyPrevented)
		return true;

	//Hack to give master credit for all his/her monster's kills
	if (FriendlyMonsterController(Killer) != None && FriendlyMonsterController(Killer).Master != None)
	{
		M = spawn(class'FriendlyMonsterKillMarker', Killed);
		M.Killer = FriendlyMonsterController(Killer).Master;
		M.Health = Killed.Health;
		M.DamageType = damageType;
		M.HitLocation = HitLocation;
		return true;
	}

	//Hack to give EXP and game stats (but NOT points) for killing someone else's monster
	if (FriendlyMonsterController(Killed.Controller) != None)
	{
		//don't count this monster as part of an Invasion wave
		if (Invasion(Level.Game) != None)
			Invasion(Level.Game).NumMonsters++;

		if (Killer != None && Killer != Killed && Killer.bIsPlayer)
		{
			if (FriendlyMonsterController(Killed.Controller).Master != Killer)
			{
				Level.Game.GameRulesModifiers.ScoreKill(Killer, Killed.Controller);
				TPRI = TeamPlayerReplicationInfo(Killer.PlayerReplicationInfo);
				if (TPRI != None)
				{
					TPRI.Kills++;
					TPRI.AddWeaponKill(damageType);
				}
			}

			M = spawn(class'FriendlyMonsterKillMarker', Killed);
			M.Health = Killed.Health;
			M.DamageType = damageType;
			M.HitLocation = HitLocation;
			Killed.Controller.Destroy();
			return true;
		}
	}

	return false;
}

function bool PreventSever(Pawn Killed, name boneName, int Damage, class<DamageType> DamageType)
{
	local int x;
	local RPGStatsInv StatsInv;

	if (Killed.Controller != None)
	{
		StatsInv = GetStatsInvFor(Killed.Controller, true);
		if (StatsInv != None)
			for (x = 0; x < StatsInv.Data.Abilities.length; x++)
				if (StatsInv.Data.Abilities[x].static.PreventSever(Killed, boneName, Damage, DamageType, StatsInv.Data.AbilityLevels[x]))
					return true;
	}

	return Super.PreventSever(Killed, boneName, Damage, DamageType);
}

function Timer()
{
	local Controller C;
	local Inventory Inv;
	local RPGStatsInv StatsInv;
	local int x;

	if (Level.Game.bGameEnded)
	{
		if (TeamInfo(Level.Game.GameReplicationInfo.Winner) != None)
		{
			for (C = Level.ControllerList; C != None; C = C.NextController)
				if (C.PlayerReplicationInfo != None && C.PlayerReplicationInfo.Team == Level.Game.GameReplicationInfo.Winner)
				{
					StatsInv = GetStatsInvFor(C);
					if (StatsInv != None)
					{
						StatsInv.DataObject.Experience += RPGMut.EXPForWin;
						RPGMut.CheckLevelUp(StatsInv.DataObject, C.PlayerReplicationInfo);
					}
				}
		}
		else if ( PlayerReplicationInfo(Level.Game.GameReplicationInfo.Winner) != None
			  && Controller(PlayerReplicationInfo(Level.Game.GameReplicationInfo.Winner).Owner) != None )
		{
			StatsInv = GetStatsInvFor(Controller(PlayerReplicationInfo(Level.Game.GameReplicationInfo.Winner).Owner));
			if (StatsInv != None)
			{
				StatsInv.DataObject.Experience += RPGMut.EXPForWin;
				RPGMut.CheckLevelUp(StatsInv.DataObject, PlayerReplicationInfo(Level.Game.GameReplicationInfo.Winner));
				Log(PlayerReplicationInfo(Level.Game.GameReplicationInfo.Winner).PlayerName@"won the match, awarded "$RPGMut.EXPForWin$" EXP");
			}
		}

		//Bots get a configurable amount of bonus levels after the game to counter that they're in only a fraction
		//of the total games played on the machine
		for (C = Level.ControllerList; C != None; C = C.NextController)
			if (Bot(C) != None)
			{
				StatsInv = GetStatsInvFor(C);
				if (StatsInv != None)
				{
					for (x = 0; x < RPGMut.BotBonusLevels; x++)
					{
						StatsInv.DataObject.Experience += StatsInv.DataObject.NeededExp;
						RPGMut.CheckLevelUp(StatsInv.DataObject, None);
					}
					RPGMut.BotLevelUp(Bot(C), StatsInv.DataObject);
				}
			}

		SetTimer(0, false);
	}
	else if (Level.Game.ResetCountDown == 2)
	{
		//unattach all RPGStatsInv from any pawns because the game is resetting and all pawns are about to be destroyed
		//this is done here to insure it happens right before the game actually resets anything
		for (C = Level.ControllerList; C != None; C = C.NextController)
			if (C.bIsPlayer)
				for (Inv = C.Inventory; Inv != None; Inv = Inv.Inventory)
					if (RPGStatsInv(Inv) != None && Inv.Owner != C && Inv.Owner != None)
					{
						RPGStatsInv(Inv).OwnerDied();
						break;
					}
	}
}

function bool HandleRestartGame()
{
	RPGMut.SaveData();

	return Super.HandleRestartGame();
}

defaultproperties
{
}
