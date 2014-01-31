class MutUT2004RPG extends Mutator
	config(UT2004RPG);

var array<RPGPlayerDataObject> Players; //players currently playing
var config int SaveDuringGameInterval; //periodically save during game - to avoid losing data from game crash or server kill
var config int StartingLevel; //starting level - cannot be less than 1
var config int PointsPerLevel; //stat points per levelup
var config array<int> Levels; //Experience needed for each level, NOTE: Levels[x] is exp needed for Level x+1
var config int InfiniteReqEXPOp; //For each level beyond the last in the Levels list, do this operation... (0 == Add, 1 == Multiply)
var config int InfiniteReqEXPValue; //...with this value to the EXP required for next level
var config float LevelDiffExpGainDiv; //divisor to extra experience from defeating someone of higher level (a value of 1 results in level difference squared EXP)
var config int MaxLevelupEffectStacking; //maximum number of levelup effects that can be spawned if player gains multiple levels at once
var config int EXPForWin; //EXP for winning the match (given to each member of team in team games)
var config int BotBonusLevels; //extra levelups bots gain after matches to counter that they're in only a fraction of the matches played
var config int StatCaps[6]; //by popular demand :(
var config array<class<RPGAbility> > Abilities; //List of Abilities available to players
var config array<class<RPGAbility> > RemovedAbilities; //These Abilities failed an AbilityIsAllowed() check so try to re-add them next game
var config float WeaponModifierChance; //chance any given pickup results in a weapon modifier (0 to 1)
var config bool bNoUnidentified; //no unidentified items
var config bool bReset; //delete all player data on next startup

//A modifier a weapon might be given
struct WeaponModifier
{
	var class<RPGWeapon> WeaponClass;
	var int Chance; //chance this modifier will be used, relative to all others in use
};

var config array<WeaponModifier> WeaponModifiers;
var int TotalModifierChance; //precalculated total Chance of all WeaponModifiers
var config int Version;
var bool bHasInteraction;
//var config bool bExperiencePickups; //replace adrenaline pickups with experience pickups
var config bool bMagicalStartingWeapons; //weapons given at start can have magical properties (same probability as picked up weapons)
var config bool bAutoAdjustInvasionLevel; //auto adjust invasion monsters' level based on lowest level player
var config float InvasionAutoAdjustFactor; //affects how dramatically monsters increase in level for each level of the lowest level player
var int BotSpendAmount; //bots that are buying stats spend points in increments of this amount
var config string HighestLevelPlayerName; //Highest level player ever to display in server query for all to see :)
var config int HighestLevelPlayerLevel;
var RPGPlayerDataObject CurrentLowestLevelPlayer; //Data of lowest level player currently playing (for auto-adjusting monsters, etc)

var localized string PropsDisplayText[18];
var localized string PropsDescText[18];
var localized string PropsExtras;

function PostBeginPlay()
{
	local RPGRules G;
	local int x;
	local Pickup P;
	local RPGPlayerDataObject DataObject;
	local array<string> PlayerNames;

	G = spawn(class'RPGRules');
	G.RPGMut = self;
	G.PointsPerLevel = PointsPerLevel;
	G.LevelDiffExpGainDiv = LevelDiffExpGainDiv;
	if ( Level.Game.GameRulesModifiers == None )
		Level.Game.GameRulesModifiers = G;
	else
		Level.Game.GameRulesModifiers.AddGameRules(G);

	if (bReset)
	{
		//load em all up, and delete them one by one
		PlayerNames = class'RPGPlayerDataObject'.static.GetPerObjectNames("UT2004RPG",, 1000000);
		for (x = 0; x < PlayerNames.length; x++)
		{
			DataObject = new(None, PlayerNames[x]) class'RPGPlayerDataObject';
			DataObject.ClearConfig();
		}

		bReset = false;
		SaveConfig();
	}

	for (x = 0; x < WeaponModifiers.length; x++)
		TotalModifierChance += WeaponModifiers[x].Chance;

	spawn(class'RPGArtifactManager');

	if (SaveDuringGameInterval > 0.0)
		SetTimer(SaveDuringGameInterval, true);

	if (StartingLevel < 1)
	{
		StartingLevel = 1;
		SaveConfig();
	}

	BotSpendAmount = PointsPerLevel * 3;

	//HACK - if another mutator played with the weapon pickups in *BeginPlay() (like Random Weapon Swap does)
	//we won't get CheckRelevance() calls on those pickups, so find any such pickups here and force it
	foreach DynamicActors(class'Pickup', P)
		if (P.bScriptInitialized && !P.bGameRelevant && !CheckRelevance(P))
			P.Destroy();

	Super.PostBeginPlay();
}

function bool CheckReplacement(Actor Other, out byte bSuperRelevant)
{
	local int x;
	local FakeMonsterWeapon w;
	local RPGWeaponPickup p;
	local RPGWeaponLocker L;
	//local ExperiencePickup EXP;

	//hack to allow players to pick up above normal ammo from inital ammo pickup;
	//MaxAmmo will be set to a good value later by the player's RPGStatsInv
	if (Ammunition(Other) != None && ShieldAmmo(Other) == None)
		Ammunition(Other).MaxAmmo = 999;

	/*if (AdrenalinePickup(Other) != None && bExperiencePickups)
	{
		EXP = ExperiencePickup(ReplaceWithActor(Other, "UT2004RPG.ExperiencePickup"));
		if (EXP != None)
			EXP.RPGMut = self;
		return false;
	}*/

	if (WeaponModifierChance > 0)
	{
		if (WeaponLocker(Other) != None && RPGWeaponLocker(Other) == None)
		{
			L = RPGWeaponLocker(ReplaceWithActor(Other, "UT2004RPG.RPGWeaponLocker"));
			if (L != None)
			{
				L.SetLocation(Other.Location);
				L.RPGMut = self;
				L.Weapons = WeaponLocker(Other).Weapons;
				L.bSentinelProtected = WeaponLocker(Other).bSentinelProtected;
				for (x = 0; x < L.Weapons.length; x++)
					if (L.Weapons[x].WeaponClass == class'LinkGun')
						L.Weapons[x].WeaponClass = class'RPGLinkGun';
			}
			Other.GotoState('Disabled');
		}

		if (WeaponPickup(Other) != None && RPGWeaponPickup(Other) == None)
		{
			p = RPGWeaponPickup(ReplaceWithActor(Other, "UT2004RPG.RPGWeaponPickup"));
			if (p != None)
			{
				p.RPGMut = self;
				p.FindPickupBase();
				p.GetPropertiesFrom(class<WeaponPickup>(Other.Class));
			}
			return false;
		}

		//various weapon hacks to work around casts of Pawn.Weapon
		if (xWeaponBase(Other) != None)
		{
			if (xWeaponBase(Other).WeaponType == class'LinkGun')
				xWeaponBase(Other).WeaponType = class'RPGLinkGun';
		}
		if (Weapon(Other) != None)
		{
			for (x = 0; x < Weapon(Other).NUM_FIRE_MODES; x++)
			{
				if (Weapon(Other).FireModeClass[x] == class'ShockProjFire')
					Weapon(Other).FireModeClass[x] = class'RPGShockProjFire';
				else if (Weapon(Other).FireModeClass[x] == class'PainterFire')
					Weapon(Other).FireModeClass[x] = class'RPGPainterFire';
			}
		}
	}

	//Give monsters a fake weapon
	if (Other.IsA('Monster'))
	{
		Pawn(Other).HealthMax = Pawn(Other).default.Health;
		w = spawn(class'FakeMonsterWeapon',Other,,,rot(0,0,0));
		w.GiveTo(Pawn(Other));
	}

	//force adrenaline on if artifacts enabled
	//FIXME maybe disable all combos?
	if ( Controller(Other) != None && class'RPGArtifactManager'.default.ArtifactDelay > 0 && class'RPGArtifactManager'.default.MaxArtifacts > 0
	     && class'RPGArtifactManager'.default.Artifacts.length > 0 )
		Controller(Other).bAdrenalineEnabled = true;


	return true;
}

//Replace an actor and then return the new actor
function Actor ReplaceWithActor(actor Other, string aClassName)
{
	local Actor A;
	local class<Actor> aClass;

	if ( aClassName == "" )
		return None;

	aClass = class<Actor>(DynamicLoadObject(aClassName, class'Class'));
	if ( aClass != None )
		A = Spawn(aClass,Other.Owner,Other.tag,Other.Location, Other.Rotation);
	if ( Other.IsA('Pickup') )
	{
		if ( Pickup(Other).MyMarker != None )
		{
			Pickup(Other).MyMarker.markedItem = Pickup(A);
			if ( Pickup(A) != None )
			{
				Pickup(A).MyMarker = Pickup(Other).MyMarker;
				A.SetLocation(A.Location
					+ (A.CollisionHeight - Other.CollisionHeight) * vect(0,0,1));
			}
			Pickup(Other).MyMarker = None;
		}
		else if ( A.IsA('Pickup') )
			Pickup(A).Respawntime = 0.0;
	}
	if ( A != None )
	{
		A.event = Other.event;
		A.tag = Other.tag;
		return A;
	}
	return None;
}

function ModifyPlayer(Pawn Other)
{
	local RPGPlayerDataObject data;
	local int x;
	local RPGStatsInv StatsInv;
	local Inventory Inv;
	local array<Weapon> StartingWeapons;
	local class<Weapon> StartingWeaponClass;
	local RPGWeapon MagicWeapon;

	Super.ModifyPlayer(Other);

	if (Other.Controller == None || !Other.Controller.bIsPlayer)
		return;
	StatsInv = RPGStatsInv(Other.FindInventoryType(class'RPGStatsInv'));
	if (StatsInv != None)
	{
		if (StatsInv.Instigator != None)
			for (x = 0; x < StatsInv.Data.Abilities.length; x++)
				StatsInv.Data.Abilities[x].static.ModifyPawn(StatsInv.Instigator, StatsInv.Data.AbilityLevels[x]);
		return;
	}
	else
	{
		for (Inv = Other.Controller.Inventory; Inv != None; Inv = Inv.Inventory)
		{
			StatsInv = RPGStatsInv(Inv);
			if (StatsInv != None)
				break;
		}
	}

	if (StatsInv != None)
		data = StatsInv.DataObject;
	else
	{
		data = new(None, Other.PlayerReplicationInfo.PlayerName) class'RPGPlayerDataObject';
		if (data.Level == 0) //new player
		{
			data.Level = StartingLevel;
			data.PointsAvailable = PointsPerLevel * (StartingLevel - 1);
			data.AdrenalineMax = 100;
			if (Levels.length > StartingLevel)
				data.NeededExp = Levels[StartingLevel];
			else if (InfiniteReqEXPValue != 0)
			{
				if (InfiniteReqEXPOp == 0)
					data.NeededExp = Levels[Levels.length - 1] + InfiniteReqEXPValue * (data.Level - (Levels.length - 1));
				else
				{
					data.NeededExp = Levels[Levels.length - 1];
					for (x = Levels.length - 1; x < StartingLevel; x++)
						data.NeededExp += int(float(data.NeededEXP) * float(InfiniteReqEXPValue) / 100.f);
				}
			}
			else
				data.NeededExp = Levels[Levels.length - 1];
			if (PlayerController(Other.Controller) != None)
				data.OwnerID = PlayerController(Other.Controller).GetPlayerIDHash();
			else
				data.OwnerID = "Bot";
			Players[Players.length] = data;
		}
		else //returning player
		{
			if ( (PlayerController(Other.Controller) != None && !(PlayerController(Other.Controller).GetPlayerIDHash() ~= data.OwnerID))
			     || (Bot(Other.Controller) != None && data.OwnerID != "Bot") )
			{
				//imposter using somebody else's name
				if (PlayerController(Other.Controller) != None)
					PlayerController(Other.Controller).ReceiveLocalizedMessage(class'RPGNameMessage', 0);
				Level.Game.ChangeName(Other.Controller, Other.PlayerReplicationInfo.PlayerName$"_Imposter", true);
				if (string(data.Name) ~= Other.PlayerReplicationInfo.PlayerName) //initial name change failed
					Level.Game.ChangeName(Other.Controller, string(Rand(65000)), true); //That's gotta suck, having a number for a name
				ModifyPlayer(Other);
				return;
			}
			Players[Players.length] = data;
			ValidateData(data);
		}
	}

	if (data.PointsAvailable > 0 && Bot(Other.Controller) != None)
	{
		x = 0;
		do
		{
			BotLevelUp(Bot(Other.Controller), data);
			x++;
		} until (data.PointsAvailable <= 0 || data.BotAbilityGoal != None || x > 1000)
	}

	if (CurrentLowestLevelPlayer == None || data.Level < CurrentLowestLevelPlayer.Level)
		CurrentLowestLevelPlayer = data;

	//spawn the stats inventory item
	if (StatsInv == None)
	{
		StatsInv = spawn(class'RPGStatsInv',Other,,,rot(0,0,0));
		if (Other.Controller.Inventory == None)
			Other.Controller.Inventory = StatsInv;
		else
		{
			for (Inv = Other.Controller.Inventory; Inv.Inventory != None; Inv = Inv.Inventory)
			{}
			Inv.Inventory = StatsInv;
		}
	}
	StatsInv.DataObject = data;
	data.CreateDataStruct(StatsInv.Data, false);
	StatsInv.RPGMut = self;
	StatsInv.GiveTo(Other);

	if (WeaponModifierChance > 0)
	{
		x = 0;
		for (Inv = Other.Inventory; Inv != None; Inv = Inv.Inventory)
		{
			if (Weapon(Inv) != None && RPGWeapon(Inv) == None)
				StartingWeapons[StartingWeapons.length] = Weapon(Inv);
			x++;
			if (x > 1000)
				break;
		}

		for (x = 0; x < StartingWeapons.length; x++)
		{
			StartingWeaponClass = StartingWeapons[x].Class;
			StartingWeapons[x].Destroy();
			if (bMagicalStartingWeapons)
				MagicWeapon = spawn(GetRandomWeaponModifier(StartingWeaponClass, Other), Other,,, rot(0,0,0));
			else
				MagicWeapon = spawn(class'RPGWeapon', Other,,, rot(0,0,0));
			MagicWeapon.Generate(None);
			MagicWeapon.SetModifiedWeapon(spawn(StartingWeaponClass,Other,,,rot(0,0,0)), bNoUnidentified);
			MagicWeapon.GiveTo(Other);
		}
		Other.Controller.ClientSwitchToBestWeapon();
	}

	//set pawn's properties
	Other.Health = Other.default.Health + data.HealthBonus;
	Other.HealthMax = Other.default.HealthMax + data.HealthBonus;
	Other.SuperHealthMax = Other.HealthMax + (Other.default.SuperHealthMax - Other.default.HealthMax);
	Other.Controller.AdrenalineMax = data.AdrenalineMax;
	for (x = 0; x < data.Abilities.length; x++)
		data.Abilities[x].static.ModifyPawn(Other, data.AbilityLevels[x]);
}

function DriverEnteredVehicle(Vehicle V, Pawn P)
{
	local Inventory Inv;
	local RPGStatsInv StatsInv;
	local int DefHealth, i;
	local float DefLinkHealMult, HealthPct;
	local array<RPGArtifact> Artifacts;

	if (V.Controller != None)
	{
		for (Inv = V.Controller.Inventory; Inv != None; Inv = Inv.Inventory)
		{
			StatsInv = RPGStatsInv(Inv);
			if (StatsInv != None)
				break;
		}
	}

	if (StatsInv == None)
		StatsInv = RPGStatsInv(P.FindInventoryType(class'RPGStatsInv'));
	if (StatsInv != None)
	{
		//FIXME maybe give it inventory to remember original values instead so it works with other mods that change vehicle properties?
		if (ASVehicleFactory(V.ParentFactory) != None)
		{
			DefHealth = ASVehicleFactory(V.ParentFactory).VehicleHealth;
			DefLinkHealMult = ASVehicleFactory(V.ParentFactory).VehicleLinkHealMult;
		}
		else
		{
			DefHealth = V.default.Health;
			DefLinkHealMult = V.default.LinkHealMult;
		}
		HealthPct = float(V.Health) / V.HealthMax;
		V.HealthMax = DefHealth + StatsInv.Data.HealthBonus;
		V.Health = HealthPct * V.HealthMax;
		V.LinkHealMult = DefLinkHealMult * (V.HealthMax / DefHealth); //FIXME maybe make faster link healing an ability instead?

		StatsInv.ModifyVehicle(V);
		StatsInv.ClientModifyVehicle(V);
	}
	else
		Warn("Couldn't find RPGStatsInv for "$P.GetHumanReadableName());

	//move all artifacts from driver to vehicle, so player can still use them
	for (Inv = P.Inventory; Inv != None; Inv = Inv.Inventory)
		if (RPGArtifact(Inv) != None)
			Artifacts[Artifacts.length] = RPGArtifact(Inv);

	for (i = 0; i < Artifacts.length; i++)
	{
		if (Artifacts[i] == P.SelectedItem)
			V.SelectedItem = Artifacts[i];
		P.DeleteInventory(Artifacts[i]);
		Artifacts[i].GiveTo(V);
	}

	Super.DriverEnteredVehicle(V, P);
}

function DriverLeftVehicle(Vehicle V, Pawn P)
{
	local Inventory Inv;
	local RPGStatsInv StatsInv;
	local array<RPGArtifact> Artifacts;
	local int i;

	if (P.Controller != None)
	{
		for (Inv = P.Controller.Inventory; Inv != None; Inv = Inv.Inventory)
		{
			StatsInv = RPGStatsInv(Inv);
			if (StatsInv != None)
				break;
		}
	}

	if (StatsInv == None)
		StatsInv = RPGStatsInv(P.FindInventoryType(class'RPGStatsInv'));
	if (StatsInv != None)
	{
		StatsInv.UnModifyVehicle(V);
		StatsInv.ClientUnModifyVehicle(V);
	}
	else
		Warn("Couldn't find RPGStatsInv for "$P.GetHumanReadableName());

	//move all artifacts from vehicle to driver
	for (Inv = V.Inventory; Inv != None; Inv = Inv.Inventory)
		if (RPGArtifact(Inv) != None)
			Artifacts[Artifacts.length] = RPGArtifact(Inv);

	for (i = 0; i < Artifacts.length; i++)
	{
		if (Artifacts[i] == V.SelectedItem)
			P.SelectedItem = Artifacts[i];
		V.DeleteInventory(Artifacts[i]);
		Artifacts[i].GiveTo(P);
	}

	Super.DriverLeftVehicle(V, P);
}

//Check the player data at the given index for errors (too many/not enough stat points, invalid abilities)
//Converts the data by giving or taking the appropriate number of stat points and refunding points for abilities bought that are no longer allowed
//This allows the server owner to change points per level settings and/or the abilities allowed and have it affect already created players properly
function ValidateData(RPGPlayerDataObject Data)
{
	local int TotalPoints, x, y;
	local bool bAllowedAbility;

	//check stat caps
	if (StatCaps[0] >= 0)
		Data.WeaponSpeed = Min(Data.WeaponSpeed, StatCaps[0]);
	if (StatCaps[1] >= 0)
		Data.HealthBonus = Min(Data.HealthBonus, StatCaps[1]);
	if (StatCaps[2] >= 0)
		Data.AdrenalineMax = Min(Data.AdrenalineMax, StatCaps[2]);
	if (StatCaps[3] >= 0)
		Data.Attack = Min(Data.Attack, StatCaps[3]);
	if (StatCaps[4] >= 0)
		Data.Defense = Min(Data.Defense, StatCaps[4]);
	if (StatCaps[5] >= 0)
		Data.AmmoMax = Min(Data.AmmoMax, StatCaps[5]);

	TotalPoints += Data.WeaponSpeed + Data.Attack + Data.Defense + Data.AmmoMax;
	TotalPoints += Data.HealthBonus / 2;
	TotalPoints += Data.AdrenalineMax - 100;
	for (x = 0; x < Data.Abilities.length; x++)
	{
		bAllowedAbility = false;
		for (y = 0; y < Abilities.length; y++)
			if (Data.Abilities[x] == Abilities[y])
			{
				bAllowedAbility = true;
				y = Abilities.length;		//kill loop without break due to UnrealScript bug that causes break to kill both loops
			}
		if (bAllowedAbility)
		{
			for (y = 0; y < Data.AbilityLevels[x]; y++)
				TotalPoints += Data.Abilities[x].static.Cost(Data, y);
		}
		else
		{
			for (y = 0; y < Data.AbilityLevels[x]; y++)
				Data.PointsAvailable += Data.Abilities[x].static.Cost(Data, y);
			Log("Ability"@Data.Abilities[x]@"was in"@Data.Name$"'s data but is not an available ability - removed (stat points refunded)");
			Data.Abilities.Remove(x, 1);
			Data.AbilityLevels.Remove(x, 1);
			x--;
		}
	}
	TotalPoints += Data.PointsAvailable;

	if ( TotalPoints != ((Data.Level - 1) * PointsPerLevel) )
	{
		Data.PointsAvailable += ((Data.Level - 1) * PointsPerLevel) - TotalPoints;
		Log(Data.Name$" had "$TotalPoints$" total stat points at Level "$Data.Level$", should be "$((Data.Level - 1) * PointsPerLevel)$", PointsAvailable changed by "$(((Data.Level - 1) * PointsPerLevel) - TotalPoints)$" to compensate");
	}
}

//Do a bot's levelup
function BotLevelUp(Bot B, RPGPlayerDataObject Data)
{
	local int WSpeedChance, HealthBonusChance, AdrenalineMaxChance, AttackChance, DefenseChance, AmmoMaxChance, AbilityChance;
	local int Chance, TotalAbilityChance;
	local int x, y, Index;
	local bool bHasAbility, bAddAbility;

	if (Data.BotAbilityGoal != None)
	{
		if (Data.BotAbilityGoal.static.Cost(Data, Data.BotGoalAbilityCurrentLevel) > Data.PointsAvailable)
			return;

		Index = -1;
		for (x = 0; x < Data.Abilities.length; x++)
			if (Data.Abilities[x] == Data.BotAbilityGoal)
			{
				Index = x;
				break;
			}
		if (Index == -1)
			Index = Data.Abilities.length;
		Data.PointsAvailable -= Data.BotAbilityGoal.static.Cost(Data, Data.BotGoalAbilityCurrentLevel);
		Data.Abilities[Index] = Data.BotAbilityGoal;
		Data.AbilityLevels[Index]++;
		Data.BotAbilityGoal = None;
		return;
	}

	//Bots always allocate all their points to one stat - random, but tilted towards the bot's tendencies

	WSpeedChance = 2;
	HealthBonusChance = 2;
	AdrenalineMaxChance = 1;
	AttackChance = 2;
	DefenseChance = 2;
	AmmoMaxChance = 1; //less because bots don't get ammo half the time as it is, so it's not as useful a stat for them
	AbilityChance = 3;

	if (B.Aggressiveness > 0.25)
	{
		WSpeedChance += 3;
		AttackChance += 3;
		AmmoMaxChance += 2;
	}
	if (B.Accuracy < 0)
	{
		WSpeedChance++;
		DefenseChance++;
		AmmoMaxChance += 2;
	}
	if (B.FavoriteWeapon == class'SniperRifle')
		WSpeedChance += 2;
	if (B.Tactics > 0.9)
	{
		HealthBonusChance += 3;
		AdrenalineMaxChance += 3;
		DefenseChance += 3;
	}
	else if (B.Tactics > 0.4)
	{
		HealthBonusChance += 2;
		AdrenalineMaxChance += 2;
		DefenseChance += 2;
	}
	else if (B.Tactics > 0)
	{
		HealthBonusChance++;
		AdrenalineMaxChance++;
		DefenseChance++;
	}
	if (B.StrafingAbility < 0)
	{
		HealthBonusChance++;
		AdrenalineMaxChance++;
		DefenseChance += 2;
	}
	if (B.CombatStyle < 0)
	{
		HealthBonusChance += 2;
		AdrenalineMaxChance += 2;
		DefenseChance += 2;
	}
	else if (B.CombatStyle > 0)
	{
		AttackChance += 2;
		AmmoMaxChance++;
	}
	if (Data.Level < 20)
		AbilityChance--;	//very few abilities to choose from at this low level so reduce chance
	else
	{
		//More likely to buy an ability if don't have that many
		y = 0;
		for (x = 0; x < Data.AbilityLevels.length; x++)
			y += Data.AbilityLevels[x];
		if (y < (Data.Level - 20) / 10)
			AbilityChance++;
	}

	if (Data.AmmoMax >= 50)
		AmmoMaxChance = Max(AmmoMaxChance / 1.5, 1);
	if (Data.AdrenalineMax >= 175)
		AdrenalineMaxChance /= 1.5;  //too much adrenaline and you'll never get to use any combos!

	//disable choosing of stats that are maxxed out
	if (StatCaps[0] >= 0 && Data.WeaponSpeed >= StatCaps[0])
		WSpeedChance = 0;
	if (StatCaps[1] >= 0 && Data.HealthBonus >= StatCaps[1])
		HealthBonusChance = 0;
	if (StatCaps[2] >= 0 && Data.AdrenalineMax >= StatCaps[2])
		AdrenalineMaxChance = 0;
	if (StatCaps[3] >= 0 && Data.Attack >= StatCaps[3])
		AttackChance = 0;
	if (StatCaps[4] >= 0 && Data.Defense >= StatCaps[4])
		DefenseChance = 0;
	if (StatCaps[5] >= 0 && Data.AmmoMax >= StatCaps[5])
		AmmoMaxChance = 0;

	//choose a stat
	Chance = Rand(WSpeedChance + HealthBonusChance + AdrenalineMaxChance + AttackChance + DefenseChance + AmmoMaxChance + AbilityChance);
	bAddAbility = false;
	if (Chance < WSpeedChance)
		Data.WeaponSpeed += Min(Data.PointsAvailable, BotSpendAmount);
	else if (Chance < WSpeedChance + HealthBonusChance)
		Data.HealthBonus += Min(Data.PointsAvailable, BotSpendAmount) * 2;
	else if (Chance < WSpeedChance + HealthBonusChance + AdrenalineMaxChance)
		Data.AdrenalineMax += Min(Data.PointsAvailable, BotSpendAmount);
	else if (Chance < WSpeedChance + HealthBonusChance + AdrenalineMaxChance + AttackChance)
		Data.Attack += Min(Data.PointsAvailable, BotSpendAmount);
	else if (Chance < WSpeedChance + HealthBonusChance + AdrenalineMaxChance + AttackChance + DefenseChance)
		Data.Defense += Min(Data.PointsAvailable, BotSpendAmount);
	else if (Chance < WSpeedChance + HealthBonusChance + AdrenalineMaxChance + AttackChance + DefenseChance + AmmoMaxChance)
		Data.AmmoMax += Min(Data.PointsAvailable, BotSpendAmount);
	else
		bAddAbility = true;
	if (!bAddAbility)
		Data.PointsAvailable -= Min(Data.PointsAvailable, BotSpendAmount);
	else
	{
		TotalAbilityChance = 0;
		for (x = 0; x < Abilities.length; x++)
		{
			bHasAbility = false;
			for (y = 0; y < Data.Abilities.length; y++)
				if (Abilities[x] == Data.Abilities[y])
				{
					bHasAbility = true;
					TotalAbilityChance += Abilities[x].static.BotBuyChance(B, Data, Data.AbilityLevels[y]);
					y = Data.Abilities.length; //kill loop without break
				}
			if (!bHasAbility)
				TotalAbilityChance += Abilities[x].static.BotBuyChance(B, Data, 0);
		}
		if (TotalAbilityChance == 0)
			return; //no abilities can be bought
		Chance = Rand(TotalAbilityChance);
		TotalAbilityChance = 0;
		for (x = 0; x < Abilities.length; x++)
		{
			bHasAbility = false;
			for (y = 0; y < Data.Abilities.length; y++)
				if (Abilities[x] == Data.Abilities[y])
				{
					bHasAbility = true;
					TotalAbilityChance += Abilities[x].static.BotBuyChance(B, Data, Data.AbilityLevels[y]);
					if (Chance < TotalAbilityChance)
					{
						Data.BotAbilityGoal = Abilities[x];
						Data.BotGoalAbilityCurrentLevel = Data.AbilityLevels[y];
						Index = y;
					}
					y = Data.Abilities.length; //kill loop without break
				}
			if (!bHasAbility)
			{
				TotalAbilityChance += Abilities[x].static.BotBuyChance(B, Data, 0);
				if (Chance < TotalAbilityChance)
				{
					Data.BotAbilityGoal = Abilities[x];
					Data.BotGoalAbilityCurrentLevel = 0;
					Index = Data.Abilities.length;
					Data.AbilityLevels[Index] = 0;
				}
			}
			if (Chance < TotalAbilityChance)
				break; //found chosen ability
		}
		if (Data.BotAbilityGoal.static.Cost(Data, Data.BotGoalAbilityCurrentLevel) <= Data.PointsAvailable)
		{
			Data.PointsAvailable -= Data.BotAbilityGoal.static.Cost(Data, Data.BotGoalAbilityCurrentLevel);
			Data.Abilities[Index] = Data.BotAbilityGoal;
			Data.AbilityLevels[Index]++;
			Data.BotAbilityGoal = None;
		}
	}
}

function CheckLevelUp(RPGPlayerDataObject data, PlayerReplicationInfo MessagePRI)
{
	local LevelUpEffect Effect;
	local int Count;

	while (data.Experience >= data.NeededExp && Count < 10000)
	{
		Count++;
		data.Level++;
		data.PointsAvailable += PointsPerLevel;
		data.Experience -= data.NeededExp;

		if (Levels.length > data.Level)
			data.NeededExp = Levels[data.Level];
		else if (InfiniteReqEXPValue != 0)
		{
			if (InfiniteReqEXPOp == 0)
				data.NeededExp = Levels[Levels.length - 1] + InfiniteReqEXPValue * (data.Level - (Levels.length - 1));
			else
				data.NeededExp += int(float(data.NeededEXP) * float(InfiniteReqEXPValue) / 100.f);
		}
		else
			data.NeededExp = Levels[Levels.length - 1];

		if (MessagePRI != None)
		{
			if (Count <= MaxLevelupEffectStacking && Controller(MessagePRI.Owner) != None && Controller(MessagePRI.Owner).Pawn != None)
			{
				Effect = Controller(MessagePRI.Owner).Pawn.spawn(class'LevelUpEffect', Controller(MessagePRI.Owner).Pawn);
				Effect.SetDrawScale(Controller(MessagePRI.Owner).Pawn.CollisionRadius / Effect.CollisionRadius);
				Effect.Initialize();
			}
		}

		if (data.Level > HighestLevelPlayerLevel)
		{
			HighestLevelPlayerName = string(data.Name);
			HighestLevelPlayerLevel = data.Level;
			SaveConfig();
		}
	}

	if (Count > 0 && MessagePRI != None)
		Level.Game.BroadCastLocalized(self, class'GainLevelMessage', data.Level, MessagePRI);
}

function class<RPGWeapon> GetRandomWeaponModifier(class<Weapon> WeaponType, Pawn Other)
{
	local int x, Chance;

	if (FRand() < WeaponModifierChance)
	{
		Chance = Rand(TotalModifierChance);
		for (x = 0; x < WeaponModifiers.Length; x++)
		{
			Chance -= WeaponModifiers[x].Chance;
			if (Chance < 0 && WeaponModifiers[x].WeaponClass.static.AllowedFor(WeaponType, Other))
				return WeaponModifiers[x].WeaponClass;
		}
	}

	return class'RPGWeapon';
}

function NotifyLogout(Controller Exiting)
{
	local Inventory Inv;
	local RPGStatsInv StatsInv;
	local Controller C;
	local int i;
	local RPGPlayerDataObject DataObject;

	for (Inv = Exiting.Inventory; Inv != None; Inv = Inv.Inventory)
	{
		StatsInv = RPGStatsInv(Inv);
		if (StatsInv != None)
			break;
	}

	if (StatsInv == None)
		return;

	DataObject = StatsInv.DataObject;
	StatsInv.Destroy();

	//remove from Players list
	for (i = 0; i < Players.length; i++)
		if (Players[i] == DataObject)
		{
			Players.Remove(i, 1);
			break;
		}

	if (DataObject != CurrentLowestLevelPlayer)
		return;

	//find who is now the lowest level player
	CurrentLowestLevelPlayer = None;
	for (C = Level.ControllerList; C != None; C = C.NextController)
		if (C.bIsPlayer)
			for (Inv = C.Inventory; Inv != None; Inv = Inv.Inventory)
				if ( RPGStatsInv(Inv) != None && ( CurrentLowestLevelPlayer == None
								  || RPGStatsInv(Inv).DataObject.Level < CurrentLowestLevelPlayer.Level ) )
					CurrentLowestLevelPlayer = RPGStatsInv(Inv).DataObject;
}

simulated function Tick(float deltaTime)
{
	local PlayerController PC;
	local int x;

	if (!bHasInteraction && Level.NetMode != NM_DedicatedServer)
	{
		PC = Level.GetLocalPlayerController();
		if (PC != None)
		{
			PC.Player.InteractionMaster.AddInteraction("UT2004RPG.RPGInteraction", PC.Player);
			if (GUIController(PC.Player.GUIController) != None)
			{
				GUIController(PC.Player.GUIController).RegisterStyle(class'STY_AbilityList');
				GUIController(PC.Player.GUIController).RegisterStyle(class'STY_ResetButton');
			}
			bHasInteraction = true;
			disable('Tick');
		}
	}

	if (Role == ROLE_Authority)
	{
		for (x = 0; x < Abilities.length; x++)
		{
			if (Abilities[x] == None)
			{
				Abilities.Remove(x, 1);
				x--;
			}
			else if (!Abilities[x].static.AbilityIsAllowed(Level.Game, self))
			{
				RemovedAbilities[RemovedAbilities.length] = Abilities[x];
				Abilities.Remove(x, 1);
				x--;
			}
		}
		//See if any abilities that weren't allowed last game are allowed this time
		//(so user doesn't have to fix ability list when switching gametypes/mutators a lot)
		for (x = 0; x < RemovedAbilities.length; x++)
			if (RemovedAbilities[x].static.AbilityIsAllowed(Level.Game, self))
			{
				Abilities[Abilities.length] = RemovedAbilities[x];
				RemovedAbilities.Remove(x, 1);
				x--;
			}
		SaveConfig();
		disable('Tick');
	}
}

function Timer()
{
	SaveData();
}

function SaveData()
{
	local int i;

	for (i = 0; i < Players.length; i++)
		Players[i].SaveConfig();
}

function GetServerDetails(out GameInfo.ServerResponseLine ServerState)
{
	local int i, NumPlayers;
	local float AvgLevel;
	local Controller C;
	local Inventory Inv;

	Super.GetServerDetails(ServerState);

	i = ServerState.ServerInfo.Length;

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "UT2004RPG Version";
	ServerState.ServerInfo[i++].Value = ""$(Version / 10)$"."$int(Version % 10);

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "Starting Level";
	ServerState.ServerInfo[i++].Value = string(StartingLevel);

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "Stat Points Per Level";
	ServerState.ServerInfo[i++].Value = string(PointsPerLevel);

	//find average level of players currently on server
	for (C = Level.ControllerList; C != None; C = C.NextController)
	{
		if (C.bIsPlayer)
		{
			for (Inv = C.Inventory; Inv != None; Inv = Inv.Inventory)
				if (RPGStatsInv(Inv) != None)
				{
					AvgLevel += RPGStatsInv(Inv).DataObject.Level;
					NumPlayers++;
				}
		}
	}
	if (NumPlayers > 0)
		AvgLevel = AvgLevel / NumPlayers;

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "Current Avg. Level";
	ServerState.ServerInfo[i++].Value = ""$AvgLevel;

	if (HighestLevelPlayerLevel > 0)
	{
		ServerState.ServerInfo.Length = i+1;
		ServerState.ServerInfo[i].Key = "Highest Level Player";
		ServerState.ServerInfo[i++].Value = HighestLevelPlayerName@"("$HighestLevelPlayerLevel$")";
	}

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "Magic Weapon Chance";
	ServerState.ServerInfo[i++].Value = string(int(WeaponModifierChance*100))$"%";

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "Magical Starting Weapons";
	ServerState.ServerInfo[i++].Value = string(bMagicalStartingWeapons);

	ServerState.ServerInfo.Length = i+1;
	ServerState.ServerInfo[i].Key = "Artifacts";
	ServerState.ServerInfo[i++].Value = string(class'RPGArtifactManager'.default.MaxArtifacts > 0 && class'RPGArtifactManager'.default.ArtifactDelay > 0);

	if (Level.Game.IsA('Invasion'))
	{
		ServerState.ServerInfo.Length = i+1;
		ServerState.ServerInfo[i].Key = "Auto Adjust Invasion Monster Level";
		ServerState.ServerInfo[i++].Value = string(bAutoAdjustInvasionLevel);
		if (bAutoAdjustInvasionLevel)
		{
			ServerState.ServerInfo.Length = i+1;
			ServerState.ServerInfo[i].Key = "Monster Adjustment Factor";
			ServerState.ServerInfo[i++].Value = string(InvasionAutoAdjustFactor);
		}
	}
}

function Destroyed()
{
	SaveData();
	Players.length = 0;

	Super.Destroyed();
}

static function FillPlayInfo(PlayInfo PlayInfo)
{
	local int i;

	Super.FillPlayInfo(PlayInfo);

	PlayInfo.AddSetting("UT2004RPG", "SaveDuringGameInterval", default.PropsDisplayText[i++], 1, 10, "Text", "3;0:999");
	PlayInfo.AddSetting("UT2004RPG", "StartingLevel", default.PropsDisplayText[i++], 1, 10, "Text", "2;1:99");
	PlayInfo.AddSetting("UT2004RPG", "PointsPerLevel", default.PropsDisplayText[i++], 5, 10, "Text", "2;1:99");
	PlayInfo.AddSetting("UT2004RPG", "LevelDiffExpGainDiv", default.PropsDisplayText[i++], 1, 10, "Text", "5;0.001:100.0",,, true);
	PlayInfo.AddSetting("UT2004RPG", "EXPForWin", default.PropsDisplayText[i++], 10, 10, "Text", "3;0:999");
	PlayInfo.AddSetting("UT2004RPG", "BotBonusLevels", default.PropsDisplayText[i++], 4, 10, "Text", "2;0:99");
	PlayInfo.AddSetting("UT2004RPG", "bReset", default.PropsDisplayText[i++], 0, 10, "Check");
	PlayInfo.AddSetting("UT2004RPG", "WeaponModifierChance", default.PropsDisplayText[i++], 50, 10, "Text", "4;0.0:1.0");
	PlayInfo.AddSetting("UT2004RPG", "bMagicalStartingWeapons", default.PropsDisplayText[i++], 0, 10, "Check");
	PlayInfo.AddSetting("UT2004RPG", "bNoUnidentified", default.PropsDisplayText[i++], 0, 10, "Check");
	PlayInfo.AddSetting("UT2004RPG", "bAutoAdjustInvasionLevel", default.PropsDisplayText[i++], 1, 10, "Check");
	PlayInfo.AddSetting("UT2004RPG", "InvasionAutoAdjustFactor", default.PropsDisplayText[i++], 1, 10, "Text", "4;0.01:3.0");
	PlayInfo.AddSetting("UT2004RPG", "MaxLevelupEffectStacking", default.PropsDisplayText[i++], 1, 10, "Text", "2;1:10",,, true);
	PlayInfo.AddSetting("UT2004RPG", "StatCaps", default.PropsDisplayText[i++], 1, 14, "Text",,,, true);
	PlayInfo.AddSetting("UT2004RPG", "InfiniteReqEXPOp", default.PropsDisplayText[i++], 1, 12, "Select", default.PropsExtras,,, true);
	PlayInfo.AddSetting("UT2004RPG", "InfiniteReqEXPValue", default.PropsDisplayText[i++], 1, 13, "Text", "3;0:999",,, true);
	PlayInfo.AddSetting("UT2004RPG", "Levels", default.PropsDisplayText[i++], 1, 11, "Text",,,, true);
	//FIXME perhaps make Abilities menu a "Select" option, using .int or .ucl to find all available abilities?
	PlayInfo.AddSetting("UT2004RPG", "Abilities", default.PropsDisplayText[i++], 1, 15, "Text",,,, true);
	//PlayInfo.AddSetting("UT2004RPG", "bExperiencePickups", "Experience Pickups", 0, 10, "Check");

	class'RPGArtifactManager'.static.FillPlayInfo(PlayInfo);
}

static function string GetDescriptionText(string PropName)
{
	switch (PropName)
	{
		case "SaveDuringGameInterval":	return default.PropsDescText[0];
		case "StartingLevel":		return default.PropsDescText[1];
		case "PointsPerLevel":		return default.PropsDescText[2];
		case "LevelDiffExpGainDiv":	return default.PropsDescText[3];
		case "EXPForWin":		return default.PropsDescText[4];
		case "BotBonusLevels":		return default.PropsDescText[5];
		case "bReset":			return default.PropsDescText[6];
		case "WeaponModifierChance":	return default.PropsDescText[7];
		case "bMagicalStartingWeapons":	return default.PropsDescText[8];
		case "bNoUnidentified":		return default.PropsDescText[9];
		case "bAutoAdjustInvasionLevel":return default.PropsDescText[10];
		case "InvasionAutoAdjustFactor":return default.PropsDescText[11];
		case "MaxLevelupEffectStacking":return default.PropsDescText[12];
		case "StatCaps":		return default.PropsDescText[13];
		case "InfiniteReqEXPOp":	return default.PropsDescText[14];
		case "InfiniteReqEXPValue":	return default.PropsDescText[15];
		case "Levels":			return default.PropsDescText[16];
		case "Abilities":		return default.PropsDescText[17];
	}
}

defaultproperties
{
     SaveDuringGameInterval=5
     StartingLevel=1
     PointsPerLevel=5
     Levels(1)=15
     Levels(2)=20
     Levels(3)=25
     Levels(4)=30
     Levels(5)=35
     Levels(6)=40
     Levels(7)=45
     Levels(8)=50
     Levels(9)=55
     Levels(10)=60
     Levels(11)=65
     Levels(12)=70
     Levels(13)=75
     Levels(14)=80
     Levels(15)=85
     Levels(16)=90
     Levels(17)=95
     Levels(18)=100
     Levels(19)=105
     Levels(20)=110
     Levels(21)=115
     Levels(22)=120
     Levels(23)=125
     Levels(24)=130
     Levels(25)=135
     Levels(26)=140
     Levels(27)=145
     Levels(28)=150
     LevelDiffExpGainDiv=1.500000
     MaxLevelupEffectStacking=5
     EXPForWin=10
     BotBonusLevels=4
     StatCaps(0)=1000
     StatCaps(1)=-1
     StatCaps(2)=-1
     StatCaps(3)=-1
     StatCaps(4)=-1
     StatCaps(5)=-1
     Abilities(0)=Class'UT2004RPG.AbilityRegen'
     Abilities(1)=Class'UT2004RPG.AbilityAdrenalineRegen'
     Abilities(2)=Class'UT2004RPG.AbilityAmmoRegen'
     Abilities(3)=Class'UT2004RPG.AbilityCounterShove'
     Abilities(4)=Class'UT2004RPG.AbilityJumpZ'
     Abilities(5)=Class'UT2004RPG.AbilityReduceFallDamage'
     Abilities(6)=Class'UT2004RPG.AbilityRetaliate'
     Abilities(7)=Class'UT2004RPG.AbilitySpeed'
     Abilities(8)=Class'UT2004RPG.AbilityShieldStrength'
     Abilities(9)=Class'UT2004RPG.AbilityNoWeaponDrop'
     Abilities(10)=Class'UT2004RPG.AbilityVampire'
     Abilities(11)=Class'UT2004RPG.AbilityHoarding'
     Abilities(12)=Class'UT2004RPG.AbilityReduceSelfDamage'
     Abilities(13)=Class'UT2004RPG.AbilitySmartHealing'
     Abilities(14)=Class'UT2004RPG.AbilityAirControl'
     Abilities(15)=Class'UT2004RPG.AbilityGhost'
     Abilities(16)=Class'UT2004RPG.AbilityUltima'
     Abilities(17)=Class'UT2004RPG.AbilityAdrenalineSurge'
     Abilities(18)=Class'UT2004RPG.AbilityFastWeaponSwitch'
     Abilities(19)=Class'UT2004RPG.AbilityAwareness'
     Abilities(20)=Class'UT2004RPG.AbilityMonsterSummon'
     WeaponModifierChance=0.500000
     WeaponModifiers(0)=(WeaponClass=Class'UT2004RPG.RW_Protection',Chance=2)
     WeaponModifiers(1)=(WeaponClass=Class'UT2004RPG.RW_Force',Chance=2)
     WeaponModifiers(2)=(WeaponClass=Class'UT2004RPG.RW_Piercing',Chance=1)
     WeaponModifiers(3)=(WeaponClass=Class'UT2004RPG.RW_Penetrating',Chance=1)
     WeaponModifiers(4)=(WeaponClass=Class'UT2004RPG.RW_Infinity',Chance=1)
     WeaponModifiers(5)=(WeaponClass=Class'UT2004RPG.RW_Damage',Chance=2)
     WeaponModifiers(6)=(WeaponClass=Class'UT2004RPG.RW_NoMomentum',Chance=1)
     WeaponModifiers(7)=(WeaponClass=Class'UT2004RPG.RW_Energy',Chance=1)
     WeaponModifiers(8)=(WeaponClass=Class'UT2004RPG.RW_Luck',Chance=2)
     WeaponModifiers(9)=(WeaponClass=Class'UT2004RPG.RW_Poison',Chance=2)
     Version=10
     bAutoAdjustInvasionLevel=True
     InvasionAutoAdjustFactor=0.300000
     PropsDisplayText(0)="Autosave Interval (seconds)"
     PropsDisplayText(1)="Starting Level"
     PropsDisplayText(2)="Stat Points per Level"
     PropsDisplayText(3)="Divisor to EXP from Level Diff"
     PropsDisplayText(4)="EXP for Winning"
     PropsDisplayText(5)="Extra Bot Levelups After Match"
     PropsDisplayText(6)="Reset Player Data Next Game"
     PropsDisplayText(7)="Magic Weapon Chance"
     PropsDisplayText(8)="Magical Starting Weapons"
     PropsDisplayText(9)="No Unidentified Items"
     PropsDisplayText(10)="Auto Adjust Invasion Monster Level"
     PropsDisplayText(11)="Monster Adjustment Factor"
     PropsDisplayText(12)="Max Levelup Effects at Once"
     PropsDisplayText(13)="Stat Caps"
     PropsDisplayText(14)="Infinite Required EXP Operation"
     PropsDisplayText(15)="Infinite Required EXP Value"
     PropsDisplayText(16)="EXP Required for Each Level"
     PropsDisplayText(17)="Allowed Abilities"
     PropsDescText(0)="During the game, all data will be saved every this many seconds."
     PropsDescText(1)="New players start at this Level."
     PropsDescText(2)="The number of stat points earned from a levelup."
     PropsDescText(3)="Lower values = more exp when killing someone of higher level."
     PropsDescText(4)="The EXP gained for winning a match."
     PropsDescText(5)="Extra levels bots gain after a match because individual bots don't play often."
     PropsDescText(6)="If checked, player data will be reset before the next match begins."
     PropsDescText(7)="Chance of any given weapon having magical properties."
     PropsDescText(8)="If checked, weapons given to players when they spawn can have magical properties."
     PropsDescText(9)="If checked, magical weapons will always be identified."
     PropsDescText(10)="If checked, Invasion monsters' level will be adjusted based on the lowest level player."
     PropsDescText(11)="Invasion monsters will be adjusted based on this fraction of the weakest player's level."
     PropsDescText(12)="The maximum number of levelup particle effects that can be spawned on a character at once."
     PropsDescText(13)="Limit on how high stats can go. Values less than 0 mean no limit. The stats are: 1: Weapon Speed 2: Health Bonus 3: Max Adrenaline 4: Damage Bonus 5: Damage Reduction 6: Max Ammo Bonus"
     PropsDescText(14)="Allows you to make the EXP required for the next level always increase, no matter how high a level you get. This option controls how it increases."
     PropsDescText(15)="Allows you to make the EXP required for the next level always increase, no matter how high a level you get. This option is the value for the previous option's operation."
     PropsDescText(16)="Change the EXP required for each level. Levels after the last in your list will use the last value in the list."
     PropsDescText(17)="Change the list of abilities players can choose from."
     PropsExtras="0;Add Specified Value;1;Add Specified Percent"
     bAddToServerPackages=True
     GroupName="RPG"
     FriendlyName="UT2004 RPG"
     Description="UT2004 with a persistent experience level system, magic weapons, and artifacts."
     bAlwaysRelevant=True
     RemoteRole=ROLE_SimulatedProxy
}
