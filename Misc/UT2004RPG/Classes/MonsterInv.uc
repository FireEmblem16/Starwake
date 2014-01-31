class MonsterInv extends Inventory;

var int AbilityLevel;
var array<class<Monster> > MonsterList;
var FriendlyMonsterController CurrentMonster;

function PostBeginPlay()
{
	local Object O;

	Super.PostBeginPlay();

	foreach AllObjects(class'Object', O)
		if (class<Monster>(O) != None && O != class'Monster' && class<Monster>(O).default.Mesh != class'xPawn'.default.Mesh)
			MonsterList[MonsterList.length] = class<Monster>(O);

	SetTimer(10.0, true);
}

function GiveTo(Pawn Other, optional Pickup Pickup)
{
	local Controller C;
	local FriendlyMonsterController F;

	Super.GiveTo(Other, Pickup);

	for (C = Level.ControllerList; C != None; C = C.NextController)
	{
		F = FriendlyMonsterController(C);
		if (F != None && F.Master != None && F.Master == Instigator.Controller)
		{
			CurrentMonster = F;
			return;
		}
	}

	SpawnMonster(true);
}

function Timer()
{
	if (Instigator == None || Instigator.Health <= 0 || Instigator.Controller == None)
	{
		Destroy();
		return;
	}

	if (CurrentMonster != None)
		return;

	SpawnMonster(false);
}

function SpawnMonster(bool bNearOwner)
{
	local int x, Count;
	local NavigationPoint N, BestDest;
	local float Dist, BestDist;
	local vector SpawnLocation;
	local rotator SpawnRotation;
	local FriendlyMonsterController C;
	local Monster P;
	local Inventory Inv;
	local RPGStatsInv StatsInv;

	do
	{
		x = Rand(MonsterList.length);
		Count++;
	} until ( MonsterList[x].default.ScoringValue == AbilityLevel
		  || (AbilityLevel == 8 && MonsterList[x].default.ScoringValue > 8) || Count > 1000)

	if (Count > 1000)
	{
		if (AbilityLevel > 0)
		{
			AbilityLevel--;
			SpawnMonster(bNearOwner);
			return;
		}
		else
		{
			AbilityLevel = 12;
			return;
		}
	}

	if (bNearOwner)
	{
		BestDist = 50000.f;
		for (N = Level.NavigationPointList; N != None; N = N.NextNavigationPoint)
		{
			Dist = VSize(N.Location - Instigator.Location);
			if (Dist < BestDist && Dist > MonsterList[x].default.CollisionRadius * 2)
			{
				BestDest = N;
				BestDist = VSize(N.Location - Instigator.Location);
			}
		}
	}
	else
	{
		Count = 0;
		do
		{
			BestDest = Instigator.Controller.FindRandomDest();
			Count++;
		} until ( (VSize(BestDest.Location - Instigator.Location) > 1000 && !FastTrace(BestDest.Location, Instigator.Location))
			  || Count > 1000 )
	}

	if (BestDest != None)
		SpawnLocation = BestDest.Location + (MonsterList[x].default.CollisionHeight - BestDest.CollisionHeight) * vect(0,0,1);
	else
		SpawnLocation = Instigator.Location + MonsterList[x].default.CollisionHeight * vect(0,0,1.5);
	SpawnRotation.Yaw = rotator(SpawnLocation - Instigator.Location).Yaw;

	P = spawn(MonsterList[x],,, SpawnLocation, SpawnRotation);
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
	else
		Log("WARNING: Couldn't find RPGStatsInv for "$Instigator.GetHumanReadableName());

	CurrentMonster = C;
}
