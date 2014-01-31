//applies weapon stats to each player's weapon and manages client<->server interaction
//although attached to a player's Pawn so it can receive events from it, it is also the inventory of the
//Controller for persistence. Just before the Pawn dies the RPGStatsInv is removed from its inventory chain
//so it doesn't get destroyed, and is then reconnected to the Controller's new Pawn when it respawns.
class RPGStatsInv extends Inventory
	DependsOn(RPGPlayerDataObject);

var RPGPlayerDataObject DataObject; //object holding player's data (server side only)
var RPGPlayerDataObject.RPGPlayerData Data; //struct version of data in DataObject - replicated to clients to show on HUD and stat menu
var MutUT2004RPG RPGMut; //server side only
var array<class<RPGAbility> > AllAbilities; //all abilities available
var int StatCaps[6]; //curse the need for it
var RPGStatsMenu StatsMenu; //clients only - pointer to stats menu if it exists so we don't have to do an iterator search
var bool bGotInstigator; //netplay only - set to true first tick after Instigator has been replicated
var bool bSentInitialData; //sent initial data that requires function replication (ability list, stat caps, etc)

struct OldRPGWeaponInfo
{
	var RPGWeapon Weapon;
	var class<Weapon> ModifiedClass;
};
var array<OldRPGWeaponInfo> OldRPGWeapons; //used to prevent throwing a weapon just to try for a different modifier

//bot multikill tracking (the stock code doesn't track multikills for bots)
var int BotMultiKillLevel;
var float BotLastKillTime;

enum EStatType
{
	STAT_WSpeed,
	STAT_HealthBonus,
	STAT_AdrenalineMax,
	STAT_Attack,
	STAT_Defense,
	STAT_AmmoMax
};

delegate ProcessPlayerLevel(string PlayerString);

replication
{
	reliable if (bNetDirty && Role == ROLE_Authority)
		Data;
	reliable if (Role < ROLE_Authority)
		ServerAddPointTo, ServerAddAbility, ServerRequestPlayerLevels, ServerResetData;
	reliable if (Role == ROLE_Authority)
		ClientUpdateStatMenu, ClientAddAbility, ClientAdjustFireRate, ClientSendPlayerLevel, ClientReInitMenu,
		ClientResetData, ClientReceiveAbilityInfo, ClientReceiveAllowedAbility, ClientReceiveStatCap,
		ClientModifyVehicle, ClientUnModifyVehicle;
	unreliable if (Role == ROLE_Authority)
		ClientAdjustMaxAmmo;
}

function GiveTo(pawn Other, optional Pickup Pickup)
{
	local Inventory Inv;
	local ShieldAltFire S;
	local int x;

	Super.GiveTo(Other, Pickup);

	OwnerEvent('ChangedWeapon'); //Set initial weapon's FireRate
	Timer(); //Set initial ammo's maxammo values

	//hack to make shieldgun charge to its new MaxAmmo value
	Inv = Instigator.FindInventoryType(class'ShieldGun');
	if (Inv != None)
	{
		S = ShieldAltFire(ShieldGun(Inv).GetFireMode(1));
		if (S != None)
			S.SetTimer(S.AmmoRegenTime, true);
	}

	if (!bSentInitialData)
	{
		if (Instigator.Controller != Level.GetLocalPlayerController())
			for (x = 0; x < Data.Abilities.length; x++)
				ClientReceiveAbilityInfo(x, Data.Abilities[x], Data.AbilityLevels[x]);
		for (x = 0; x < RPGMut.Abilities.length; x++)
			ClientReceiveAllowedAbility(x, RPGMut.Abilities[x]);
		for (x = 0; x < 6; x++)
			ClientReceiveStatCap(x, RPGMut.StatCaps[x]);

		bSentInitialData = true;
	}
}

function bool HandlePickupQuery(Pickup item)
{
	local bool bResult;

	bResult = Super.HandlePickupQuery(item);

	//we only set the timer (which updates MaxAmmo on all ammos the player owns) when the player picks up something
	//with ammo to save CPU - however, this makes it unsimulatable without function replication
	if (Ammo(item) != None || (!bResult && WeaponPickup(item) != None))
		SetTimer(0.1, false);

	return bResult;
}

function OwnerEvent(name EventName)
{
	if (EventName == 'ChangedWeapon' && Instigator != None && Instigator.Weapon != None)
	{
		AdjustFireRate(Instigator.Weapon);
		ClientAdjustFireRate(); //OwnerEvent() is serverside-only, so need to call a client version
	}
	else if (EventName == 'RPGScoreKill')
	{
		//the stock code doesn't record multikills for bots, so do it here where we can track it and increment EXP appropriately
		//the fact that we can also add the info to the stats for the bots is a cool bonus :)
		//however, unlike the normal player multikill code, the multikill level is lost on death due to Inventory being lost on death
		if (Level.TimeSeconds - BotLastKillTime < 4)
		{
			Instigator.Controller.AwardAdrenaline(DeathMatch(Level.Game).ADR_MajorKill);
			if ( TeamPlayerReplicationInfo(Instigator.PlayerReplicationInfo) != None )
			{
				TeamPlayerReplicationInfo(Instigator.PlayerReplicationInfo).MultiKills[BotMultiKillLevel] += 1;
				if ( BotMultiKillLevel > 0 )
					TeamPlayerReplicationInfo(Instigator.PlayerReplicationInfo).MultiKills[BotMultiKillLevel-1] -= 1;
			}
			BotMultiKillLevel++;
			UnrealMPGameInfo(Level.Game).SpecialEvent(Instigator.PlayerReplicationInfo,"multikill_"$BotMultiKillLevel);
			DataObject.Experience += int(Square(float(BotMultiKillLevel)));
			RPGMut.CheckLevelUp(DataObject, Instigator.PlayerReplicationInfo);
		}
		else
			BotMultiKillLevel=0;

		BotLastKillTime = Level.TimeSeconds;
	}

	Super.OwnerEvent(EventName);
}

simulated function AdjustFireRate(Weapon W)
{
	local int x;
	local float Modifier;
	local WeaponFire FireMode[2];

	FireMode[0] = W.GetFireMode(0);
	FireMode[1] = W.GetFireMode(1);
	if (MinigunFire(FireMode[0]) != None) //minigun needs a hack because it fires differently than normal weapons
	{
		Modifier = 1.f + 0.01 * Data.WeaponSpeed;
		MinigunFire(FireMode[0]).BarrelRotationsPerSec = MinigunFire(FireMode[0]).default.BarrelRotationsPerSec * Modifier;
		MinigunFire(FireMode[0]).FireRate = 1.f / (MinigunFire(FireMode[0]).RoundsPerRotation * MinigunFire(FireMode[0]).BarrelRotationsPerSec);
		MinigunFire(FireMode[0]).MaxRollSpeed = 65536.f*MinigunFire(FireMode[0]).BarrelRotationsPerSec;
		MinigunFire(FireMode[1]).BarrelRotationsPerSec = MinigunFire(FireMode[1]).default.BarrelRotationsPerSec * Modifier;
		MinigunFire(FireMode[1]).FireRate = 1.f / (MinigunFire(FireMode[1]).RoundsPerRotation * MinigunFire(FireMode[1]).BarrelRotationsPerSec);
		MinigunFire(FireMode[1]).MaxRollSpeed = 65536.f*MinigunFire(FireMode[1]).BarrelRotationsPerSec;
	}
	else if (!FireMode[0].IsA('TransFire') && !FireMode[0].IsA('BallShoot') && !FireMode[0].IsA('MeleeSwordFire'))
	{
		Modifier = 1.f + 0.01 * Data.WeaponSpeed;
		if (ShieldFire(FireMode[0]) != None) //shieldgun primary needs a hack to do charging speedup
			ShieldFire(FireMode[0]).FullyChargedTime = ShieldFire(FireMode[0]).default.FullyChargedTime / Modifier;
		FireMode[0].FireRate = FireMode[0].default.FireRate / Modifier;
		FireMode[0].FireAnimRate = FireMode[0].default.FireAnimRate * Modifier;
		FireMode[0].MaxHoldTime = FireMode[0].default.MaxHoldTime / Modifier;
		FireMode[1].FireRate = FireMode[1].default.FireRate / Modifier;
		FireMode[1].FireAnimRate = FireMode[1].default.FireAnimRate * Modifier;
		FireMode[1].MaxHoldTime = FireMode[1].default.MaxHoldTime / Modifier;
	}
	for (x = 0; x < Data.Abilities.length; x++)
		Data.Abilities[x].static.ModifyWeapon(W, Data.AbilityLevels[x]);
}

//Call AdjustFireRate() clientside
simulated function ClientAdjustFireRate()
{
	if (Instigator != None && Instigator.Weapon != None)
		AdjustFireRate(Instigator.Weapon);
}

//Adjust player's maxammo values and then call ClientAdjustMaxAmmo() to do same on client
function Timer()
{
	if (Instigator != None)
	{
		AdjustMaxAmmo();
		ClientAdjustMaxAmmo();
	}
}

simulated function AdjustMaxAmmo()
{
	local Inventory Inv;
	local int Count;
	local float Modifier;

	Modifier = 1.0 + float(Data.AmmoMax) * 0.01;
	for (Inv = Instigator.Inventory; Inv != None; Inv = Inv.Inventory)
	{
		if (Ammunition(Inv) != None)
		{
			Ammunition(Inv).MaxAmmo = Ammunition(Inv).default.MaxAmmo * Modifier;
			if (Ammunition(Inv).AmmoAmount > Ammunition(Inv).MaxAmmo)
				Ammunition(Inv).AmmoAmount = Ammunition(Inv).MaxAmmo;
		}
		Count++;
		if (Count > 1000)
			break;
	}
}

//Call AdjustMaxAmmo() clientside
simulated function ClientAdjustMaxAmmo()
{
	if (Level.NetMode == NM_Client && Instigator != None)
		AdjustMaxAmmo();
}

function DropFrom(vector StartLocation)
{
	if (Instigator != None && Instigator.Controller != None)
		SetOwner(Instigator.Controller);
}

//owning pawn died
function OwnerDied()
{
	local int x;
	local Controller C;

	for (x = 0; x < OldRPGWeapons.Length; x++)
		if (OldRPGWeapons[x].Weapon != None)
			OldRPGWeapons[x].Weapon.RemoveReference();
	OldRPGWeapons.length = 0;

	//prevent RPGStatsInv from being destroyed and keep it relevant to owning player
	if (Instigator != None)
	{
		C = Instigator.Controller;
		if (C == None && Instigator.DrivenVehicle != None)
			C = Instigator.DrivenVehicle.Controller;
		Instigator.DeleteInventory(self);
		SetOwner(C);
	}
}

//Called by owning player's stat menu to add stat points to a statistic
function ServerAddPointTo(int Amount, EStatType Stat)
{
	if (DataObject.PointsAvailable < Amount)
		return;

	switch (Stat)
	{
		case STAT_WSpeed:
			if (RPGMut.StatCaps[0] >= 0 && RPGMut.StatCaps[0] - DataObject.WeaponSpeed < Amount)
				Amount = RPGMut.StatCaps[0] - DataObject.WeaponSpeed;
			DataObject.WeaponSpeed += Amount;
			Data.WeaponSpeed = DataObject.WeaponSpeed;
			break;
		case STAT_HealthBonus:
			if (RPGMut.StatCaps[1] >= 0 && RPGMut.StatCaps[1] - DataObject.HealthBonus < Amount * 2)
				Amount = (RPGMut.StatCaps[1] - DataObject.HealthBonus) / 2;
			DataObject.HealthBonus += 2 * Amount;
			Data.HealthBonus = DataObject.HealthBonus;
			if (Instigator != None)
				Instigator.HealthMax += 2 * Amount;
			break;
		case STAT_AdrenalineMax:
			if (RPGMut.StatCaps[2] >= 0 && RPGMut.StatCaps[2] - DataObject.AdrenalineMax < Amount)
				Amount = RPGMut.StatCaps[2] - DataObject.AdrenalineMax;
			DataObject.AdrenalineMax += Amount;
			Data.AdrenalineMax = DataObject.AdrenalineMax;
			if (Instigator != None && Instigator.Controller != None)
				Instigator.Controller.AdrenalineMax += Amount;
			break;
		case STAT_Attack:
			if (RPGMut.StatCaps[3] >= 0 && RPGMut.StatCaps[3] - DataObject.Attack < Amount)
				Amount = RPGMut.StatCaps[3] - DataObject.Attack;
			DataObject.Attack += Amount;
			Data.Attack = DataObject.Attack;
			break;
		case STAT_Defense:
			if (RPGMut.StatCaps[4] >= 0 && RPGMut.StatCaps[4] - DataObject.Defense < Amount)
				Amount = RPGMut.StatCaps[4] - DataObject.Defense;
			DataObject.Defense += Amount;
			Data.Defense = DataObject.Defense;
			break;
		case STAT_AmmoMax:
			if (RPGMut.StatCaps[5] >= 0 && RPGMut.StatCaps[5] - DataObject.AmmoMax < Amount)
				Amount = RPGMut.StatCaps[5] - DataObject.AmmoMax;
			DataObject.AmmoMax += Amount;
			Data.AmmoMax = DataObject.AmmoMax;
			break;
	}
	DataObject.PointsAvailable -= Amount;
	Data.PointsAvailable = DataObject.PointsAvailable;

	ClientUpdateStatMenu(Amount, Stat);
}

//After server adds points to a statistic, it calls this to update the local player's stat menu
simulated function ClientUpdateStatMenu(int Amount, EStatType Stat)
{
	if (Level.NetMode == NM_Client) //already did this on listen/standalone servers
	{
		switch (Stat)
		{
			case STAT_WSpeed:
				Data.WeaponSpeed += Amount;
				break;
			case STAT_HealthBonus:
				Data.HealthBonus += 2 * Amount;
				break;
			case STAT_AdrenalineMax:
				Data.AdrenalineMax += Amount;
				if (Instigator != None && Instigator.Controller != None)
					Instigator.Controller.AdrenalineMax += Amount;
				break;
			case STAT_Attack:
				Data.Attack += Amount;
				break;
			case STAT_Defense:
				Data.Defense += Amount;
				break;
			case STAT_AmmoMax:
				Data.AmmoMax += Amount;
				break;
		}
		Data.PointsAvailable -= Amount;
	}

	if (StatsMenu != None)
		StatsMenu.InitFor(self);
}

//Called by owning player's stat menu to buy an ability
function ServerAddAbility(class<RPGAbility> Ability)
{
	local int x, Index, Cost;
	local bool bAllowed;

	bAllowed = false;
	for (x = 0; x < RPGMut.Abilities.length; x++)
		if (RPGMut.Abilities[x] == Ability)
		{
			bAllowed = true;
			break;
		}
	if (!bAllowed)
		return;

	Index = -1;
	for (x = 0; x < DataObject.Abilities.length; x++)
		if (DataObject.Abilities[x] == Ability)
		{
			Cost = Ability.static.Cost(DataObject, DataObject.AbilityLevels[x]);
			if (Cost <= 0 || Cost > DataObject.PointsAvailable)
				return;
			Index = x;
			break;
		}
	if (Index == -1)
	{
		Cost = Ability.static.Cost(DataObject, 0);
		if (Cost <= 0 || Cost > DataObject.PointsAvailable)
			return;
		Index = DataObject.Abilities.length;
		DataObject.AbilityLevels[Index] = 0;
	}

	DataObject.Abilities[Index] = Ability;
	DataObject.AbilityLevels[Index]++;
	DataObject.PointsAvailable -= Cost;
	Data.Abilities[Index] = Ability;
	Data.AbilityLevels[Index]++;
	Data.PointsAvailable = DataObject.PointsAvailable;

	//Activate ability immediately
	if (Instigator != None)
	{
		Ability.static.ModifyPawn(Instigator, DataObject.AbilityLevels[Index]);
		if (Instigator.Weapon != None)
			Ability.static.ModifyWeapon(Instigator.Weapon, DataObject.AbilityLevels[Index]);
		if (Instigator.Controller != None && Vehicle(Instigator.Controller.Pawn) != None)
			ModifyVehicle(Vehicle(Instigator.Controller.Pawn));
	}

	//Send to client
	ClientAddAbility(Ability, Cost);
}

//After server adds an ability, it calls this to do the same on the client
simulated function ClientAddAbility(class<RPGAbility> Ability, int Cost)
{
	local int x, Index;

	if (Level.NetMode == NM_Client) //already did this on listen/standalone servers
	{
		Index = -1;
		for (x = 0; x < Data.Abilities.length; x++)
			if (Data.Abilities[x] == Ability)
			{
				Index = x;
				break;
			}
		if (Index == -1)
		{
			Index = Data.Abilities.length;
			Data.AbilityLevels[Index] = 0;
		}

		Data.Abilities[Index] = Ability;
		Data.AbilityLevels[Index]++;
		Data.PointsAvailable -= Cost;

		//Activate ability immediately
		if (Instigator != None)
		{
			Ability.static.ModifyPawn(Instigator, Data.AbilityLevels[Index]);
			if (Instigator.Weapon != None)
				Ability.static.ModifyWeapon(Instigator.Weapon, Data.AbilityLevels[Index]);
			if (Instigator.Controller != None && Vehicle(Instigator.Controller.Pawn) != None)
				ModifyVehicle(Vehicle(Instigator.Controller.Pawn));
		}
	}

	if (StatsMenu != None)
		StatsMenu.InitFor(self);
}

simulated function Tick(float deltaTime)
{
	local bool bLevelUp;
	local int x;
	local float RealNextFireTime;
	local WeaponFire FireMode[2];

	//Set initial values clientside (e.g. ModifyPawn() changes that don't get replicated for whatever reason)
	//We don't use PostNetBeginPlay() because it doesn't seem to be guaranteed that the values have all been replicated
	//at the time that function is called
	if (Level.NetMode == NM_Client)
	{
		if (Instigator != None && Instigator.Controller != None)
		{
			CheckPlayerViewShake();
			if (!bGotInstigator)
			{
				Instigator.Controller.AdrenalineMax = Data.AdrenalineMax;
				for (x = 0; x < Data.Abilities.length; x++)
					Data.Abilities[x].static.ModifyPawn(Instigator, Data.AbilityLevels[x]);
				bGotInstigator = true;
			}
		}
		else
			bGotInstigator = false;

		return;
	}

	if (Instigator != None)
	{
		CheckPlayerViewShake();

		//Awful hack to get around WeaponFire capping FireRate to tick delta
		if (Instigator.Weapon != None)
		{
			FireMode[0] = Instigator.Weapon.GetFireMode(0);
			FireMode[1] = Instigator.Weapon.GetFireMode(1);
			if (FireMode[0].bIsFiring && !FireMode[0].bFireOnRelease && !FireMode[0].bNowWaiting)
			{
				x = 0;
				while (FireMode[0].NextFireTime + FireMode[0].FireRate < Level.TimeSeconds && x < 10000)
				{
					RealNextFireTime = FireMode[0].NextFireTime + FireMode[0].FireRate;
					FireMode[0].ModeDoFire();
					FireMode[0].NextFireTime = RealNextFireTime;
					x++;
				}
			}
			if (FireMode[1].bIsFiring && !FireMode[1].bFireOnRelease && !FireMode[1].bNowWaiting)
			{
				x = 0;
				while (FireMode[1].NextFireTime + FireMode[1].FireRate < Level.TimeSeconds && x < 10000)
				{
					RealNextFireTime = FireMode[1].NextFireTime + FireMode[1].FireRate;
					FireMode[1].ModeDoFire();
					FireMode[1].NextFireTime = RealNextFireTime;
					x++;
				}
			}
		}
	}

	//update data with 'official' data from mutator if necessary
	if (DataObject.Experience != Data.Experience || DataObject.Level != Data.Level)
	{
		if (DataObject.Level > Data.Level)
			bLevelUp = true;
		DataObject.CreateDataStruct(Data, true);
		if (bLevelUp)
			ClientReInitMenu();
	}
}

//High level players can potentially take a lot of damage in one hit, which screws up PlayerController
//viewshaking functions because they're scaled to damage but not capped so if a player survives
//a high damage hit it'll totally fuck up his screen.
simulated function CheckPlayerViewShake()
{
	local PlayerController PC;
	local float ShakeScaling;

	PC = PlayerController(Instigator.Controller);
	if (PC == None)
		return;

	ShakeScaling = VSize(PC.ShakeRotMax) / 7500;
	if (ShakeScaling <= 1)
		return;

	PC.ShakeRotMax /= ShakeScaling;
	PC.ShakeRotTime /= ShakeScaling;
	PC.ShakeOffsetMax /= ShakeScaling;
}

//Just update the menu. Used when a levelup occurs while the menu is open (rare, but possible)
simulated function ClientReInitMenu()
{
	if (StatsMenu != None)
		StatsMenu.InitFor(self);
}

simulated function Destroyed()
{
	local int x;

	if (Role == ROLE_Authority)
	{
		for (x = 0; x < OldRPGWeapons.Length; x++)
			if (OldRPGWeapons[x].Weapon != None)
				OldRPGWeapons[x].Weapon.RemoveReference();
		OldRPGWeapons.length = 0;
	}

	if (StatsMenu != None)
		StatsMenu.StatsInv = None;
	StatsMenu = None;

	if (DataObject != None)
	{
		if (Role == ROLE_Authority)
			DataObject.SaveConfig();
		DataObject = None;
	}
	//Log("DESTROYED"@self);

	Super.Destroyed();
}

function ServerRequestPlayerLevels()
{
	local Controller C;
	local Inventory Inv;
	local RPGStatsInv StatsInv;

	if (RPGMut == None)
		return;

	for (C = Level.ControllerList; C != None; C = C.NextController)
	{
		if (C.bIsPlayer)
		{
			for (Inv = C.Inventory; Inv != None; Inv = Inv.Inventory)
			{
				StatsInv = RPGStatsInv(Inv);
				if (StatsInv != None)
					ClientSendPlayerLevel(StatsInv.DataObject.Name$": "$StatsInv.DataObject.Level);
			}
		}
	}
}

simulated function ClientSendPlayerLevel(string PlayerString)
{
	ProcessPlayerLevel(PlayerString);
}

//Reset the player's data. Called by the client from the stats menu, after clicking the obscenely small button and confirming it
function ServerResetData(PlayerReplicationInfo PRI)
{
	local int x;
	local string OwnerID;

	if (RPGMut != None && DataObject.Level > RPGMut.StartingLevel)
	{
		OwnerID = DataObject.OwnerID;

		// hmmm... ClearConfig() doesn't reset the actual object instance
		// and ResetConfig() just plain crashes.
		DataObject.ClearConfig();
		DataObject = new(None, string(DataObject.Name)) class'RPGPlayerDataObject';

		DataObject.OwnerID = OwnerID;
		DataObject.Level = RPGMut.StartingLevel;
		DataObject.PointsAvailable = RPGMut.PointsPerLevel * (RPGMut.StartingLevel - 1);
		DataObject.AdrenalineMax = 100;
		if (RPGMut.Levels.length > RPGMut.StartingLevel)
			DataObject.NeededExp = RPGMut.Levels[RPGMut.StartingLevel];
		else if (RPGMut.InfiniteReqEXPValue != 0)
		{
			if (RPGMut.InfiniteReqEXPOp == 0)
				DataObject.NeededExp = RPGMut.Levels[RPGMut.Levels.length - 1] + RPGMut.InfiniteReqEXPValue * (DataObject.Level - (RPGMut.Levels.length - 1));
			else
			{
				DataObject.NeededExp = RPGMut.Levels[RPGMut.Levels.length - 1];
				for (x = RPGMut.Levels.length - 1; x < RPGMut.StartingLevel; x++)
					DataObject.NeededExp += int(float(DataObject.NeededEXP) * float(RPGMut.InfiniteReqEXPValue) / 100.f);
			}
		}
		else
			DataObject.NeededExp = RPGMut.Levels[RPGMut.Levels.length - 1];
		DataObject.CreateDataStruct(data, false);
		if (Instigator != None && Instigator.Health > 0)
		{
			Level.Game.SetPlayerDefaults(Instigator);
			OwnerEvent('ChangedWeapon');
			Timer();
		}
		Level.Game.BroadCastLocalized(self, class'GainLevelMessage', Data.Level, PRI);
		if (RPGMut.HighestLevelPlayerName ~= string(DataObject.Name))
		{
			//FIXME highest level player will be incorrect until the next time the real highest level
			//player logs on. Fixable, but it would be really slow, so not going to bother unless
			//someone really cares.
			RPGMut.HighestLevelPlayerLevel = 0;
			RPGMut.SaveConfig();
		}
		ClientResetData();
	}
}

simulated function ClientResetData()
{
	Data.Abilities.length = 0;
	Data.AbilityLevels.length = 0;
}

simulated function ClientReceiveAbilityInfo(int Index, class<RPGAbility> Ability, int Level)
{
	Data.Abilities[Index] = Ability;
	Data.AbilityLevels[Index] = Level;
}

simulated function ClientReceiveAllowedAbility(int Index, class<RPGAbility> Ability)
{
	AllAbilities[Index] = Ability;
}

//Even though it's a static length array, StatCaps doesn't seem to replicate the normal way...
simulated function ClientReceiveStatCap(int Index, int Cap)
{
	StatCaps[Index] = Cap;
}

simulated function ModifyVehicle(Vehicle V)
{
	local int i;
	local ONSVehicle OV;
	local ONSWeaponPawn WP;

	//for some reason we need this to continue sending data updates to the client
	if (Owner == Instigator)
		SetOwner(V);

	OV = ONSVehicle(V);
	if (OV != None)
	{
		for (i = 0; i < OV.Weapons.length; i++)
			OV.Weapons[i].SetFireRateModifier(1.f + 0.01 * Data.WeaponSpeed);
	}
	else
	{
		WP = ONSWeaponPawn(V);
		if (WP != None)
			WP.Gun.SetFireRateModifier(1.f + 0.01 * Data.WeaponSpeed);
		else if (V.Weapon != None) //some other type of vehicle using standard weapon system
			AdjustFireRate(V.Weapon);
	}

	for (i = 0; i < Data.Abilities.length; i++)
		Data.Abilities[i].static.ModifyVehicle(V, Data.AbilityLevels[i]);
}

simulated function ClientModifyVehicle(Vehicle V)
{
	if (V != None)
		ModifyVehicle(V);
}

simulated function UnModifyVehicle(Vehicle V)
{
	local int i;

	if (Owner == V)
		SetOwner(Instigator);

	for (i = 0; i < Data.Abilities.length; i++)
		Data.Abilities[i].static.UnModifyVehicle(V, Data.AbilityLevels[i]);
}

simulated function ClientUnModifyVehicle(Vehicle V)
{
	if (V != None)
		UnModifyVehicle(V);
}

defaultproperties
{
     bReplicateInstigator=True
}
