class ArtifactMonsterSummon extends RPGArtifact;

var array<class<Monster> > MonsterList;
var class<Monster> ChosenMonster;
var float AdrenalineUsed;
var array<Monster> SummonedMonsters;
var int MaxMonsters;
var localized string TooManyMonstersMessage;

static function bool ArtifactIsAllowed(GameInfo Game)
{
	//make sure invasion monsters get loaded
	if (DynamicLoadObject("SkaarjPack.Invasion", class'Class', true) == None)
		return false;

	return true;
}

function PostBeginPlay()
{
	local Object O;

	Super.PostBeginPlay();

	foreach AllObjects(class'Object', O)
		if (class<Monster>(O) != None && O != class'Monster' && class<Monster>(O).default.Mesh != class'xPawn'.default.Mesh)
			MonsterList[MonsterList.length] = class<Monster>(O);
}

function BotConsider()
{
	if (bActive)
		return;

	if ( Instigator.Health + Instigator.ShieldStrength < 100 && Instigator.Controller.Enemy != None
	     && Instigator.Controller.Adrenaline > 2 * CostPerSec && FRand() < 0.15 && NoArtifactsActive() )
		Activate();
}

function Activate()
{
	local int i;

	if (bActive)
	{
		Super.Activate();
		return;
	}

	for (i = 0; i < SummonedMonsters.length; i++)
		if (SummonedMonsters[i] == None || SummonedMonsters[i].Health <= 0)
		{
			SummonedMonsters.Remove(i, 1);
			i--;
		}

	if (SummonedMonsters.length < MaxMonsters)
		Super.Activate();
	else if (Instigator != None)
		Instigator.ReceiveLocalizedMessage(MessageClass, 2, None, None, Class);
}

static function string GetLocalString(optional int Switch, optional PlayerReplicationInfo RelatedPRI_1, optional PlayerReplicationInfo RelatedPRI_2)
{
	if (Switch == 2)
		return Default.TooManyMonstersMessage;

	return Super.GetLocalString(Switch, RelatedPRI_1, RelatedPRI_2);
}

function DoEffect();

state Activated
{
	function BeginState()
	{
		local int x, Count;

		do
		{
			x = Rand(MonsterList.length);
			Count++;
		} until (Instigator.Controller.Adrenaline >= MonsterList[x].default.ScoringValue * 10 || Count > 10000)
		if (Count > 10000)
		{
			GotoState('');
			return;
		}

		ChosenMonster = MonsterList[x];
		bActive = true;
		AdrenalineUsed = ChosenMonster.default.ScoringValue * 10;
	}

	simulated function Tick(float deltaTime)
	{
		Global.Tick(deltaTime);

		AdrenalineUsed -= deltaTime * CostPerSec;
		if (AdrenalineUsed <= 0)
		{
			Instigator.Controller.Adrenaline -= AdrenalineUsed; //add some back if went a bit over
			AdrenalineUsed = 0;
			DoEffect();
		}
	}

	function DoEffect()
	{
		local NavigationPoint N, BestDest;
		local float Dist, BestDist;
		local vector SpawnLocation;
		local rotator SpawnRotation;
		local FriendlyMonsterController C;
		local Monster P;
		local Inventory Inv;
		local RPGStatsInv StatsInv;
		local int x;

		BestDist = 50000.f;
		for (N = Level.NavigationPointList; N != None; N = N.NextNavigationPoint)
		{
			Dist = VSize(N.Location - Instigator.Location);
			if (Dist < BestDist && Dist > ChosenMonster.default.CollisionRadius * 2)
			{
				BestDest = N;
				BestDist = VSize(N.Location - Instigator.Location);
			}
		}

		if (BestDest != None)
			SpawnLocation = BestDest.Location + (ChosenMonster.default.CollisionHeight - BestDest.CollisionHeight) * vect(0,0,1);
		else
			SpawnLocation = Instigator.Location + ChosenMonster.default.CollisionHeight * vect(0,0,1.5);
		SpawnRotation.Yaw = rotator(SpawnLocation - Instigator.Location).Yaw;

		P = spawn(ChosenMonster,,, SpawnLocation, SpawnRotation);
		if (P == None)
		{
			//try again later
			return;
		}
		if (P.Controller != None)
			P.Controller.Destroy();
		C = spawn(class'FriendlyMonsterController',,, SpawnLocation, SpawnRotation);
		C.Possess(P);
		C.SetMaster(Instigator.Controller);
		//allow Instigator's abilities to affect the monster
		for (Inv = Instigator.Controller.Inventory; Inv != None; Inv = Inv.Inventory)
		{
			StatsInv = RPGStatsInv(Inv);
			if (StatsInv != None)
				break;
		}
		if (StatsInv == None) //fallback, should never happen
			StatsInv = RPGStatsInv(Instigator.FindInventoryType(class'RPGStatsInv'));
		if (StatsInv != None)
		{
			for (x = 0; x < StatsInv.Data.Abilities.length; x++)
				StatsInv.Data.Abilities[x].static.ModifyPawn(P, StatsInv.Data.AbilityLevels[x]);
			if (C.Inventory == None)
				C.Inventory = StatsInv;
			else
			{
				for (Inv = C.Inventory; Inv.Inventory != None; Inv = Inv.Inventory)
				{}
				Inv.Inventory = StatsInv;
			}
		}

		SummonedMonsters[SummonedMonsters.length] = P;
		GotoState('');
	}
}

defaultproperties
{
	CostPerSec=25
	MinActivationTime=0.25
	ItemName="Summoning Charm"
	PickupClass=class'ArtifactMonsterSummonPickup'
	IconMaterial=Material'UTRPGTextures.Icons.SummoningCharmIcon'
	MaxMonsters=4
	TooManyMonstersMessage="The Summoning Charm cannot support any more monsters in this world."
}
