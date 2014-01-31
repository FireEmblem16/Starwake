//The artifact manager spawns artifacts at random PathNodes.
//It tries to make sure there's at least one artifact of every type available
class RPGArtifactManager extends Info
	config(UT2004RPG);

var config int ArtifactDelay; //spawn an artifact every this many seconds - zero disables
var config int MaxArtifacts;
var config int MaxHeldArtifacts; //maximum number of artifacts a player can hold
var config array<class<RPGArtifact> > Artifacts;
var array<RPGArtifact> CurrentArtifacts;
var array<PathNode> PathNodes;

var localized string PropsDisplayText[3];
var localized string PropsDescText[3];

function PostBeginPlay()
{
	local NavigationPoint N;
	local int x;

	Super.PostBeginPlay();

	for (x = 0; x < Artifacts.length; x++)
		if (Artifacts[x] == None || !Artifacts[x].static.ArtifactIsAllowed(Level.Game))
		{
			Artifacts.Remove(x, 1);
			x--;
		}

	if (ArtifactDelay > 0 && MaxArtifacts > 0 && Artifacts.length > 0)
	{
		for (N = Level.NavigationPointList; N != None; N = N.NextNavigationPoint)
			if (PathNode(N) != None && !N.IsA('FlyingPathNode'))
				PathNodes[PathNodes.length] = PathNode(N);
	}
	else
		Destroy();
}

function MatchStarting()
{
	SetTimer(ArtifactDelay, true);
}

function Timer()
{
	local int Chance, Count, x;
	local bool bTryAgain;

	for (x = 0; x < CurrentArtifacts.length; x++)
		if (CurrentArtifacts[x] == None)
		{
			CurrentArtifacts.Remove(x, 1);
			x--;
		}

	if (CurrentArtifacts.length >= MaxArtifacts)
		return;

	if (CurrentArtifacts.length >= Artifacts.length)
	{
		//there's one of everything already
		Chance = Rand(Artifacts.length);
		SpawnArtifact(Chance);
		return;
	}

	while (Count < 250)
	{
		Chance = Rand(Artifacts.length);
		for (x = 0; x < CurrentArtifacts.length; x++)
			if (CurrentArtifacts[x].Class == Artifacts[Chance])
			{
				bTryAgain = true;
				x = CurrentArtifacts.length;
			}
		if (!bTryAgain)
		{
			SpawnArtifact(Chance);
			return;
		}
		bTryAgain = false;
		Count++;
	}
}

function SpawnArtifact(int Index)
{
	local Pickup APickup;
	local Controller C;
	local RPGArtifact Inv;
	local int NumMonsters, PickedMonster, CurrentMonster;

	if (Level.Game.IsA('Invasion'))
	{
		NumMonsters = int(Level.Game.GetPropertyText("NumMonsters"));
		if (NumMonsters <= CurrentArtifacts.length)
			return;
		do
		{
			PickedMonster = Rand(NumMonsters);
			for (C = Level.ControllerList; C != None; C = C.NextController)
				if (C.Pawn != None && C.Pawn.IsA('Monster') && !C.IsA('FriendlyMonsterController'))
				{
					if (CurrentMonster >= PickedMonster)
					{
						//Assumes monster doesn't get inventory from anywhere else!
						if (RPGArtifact(C.Pawn.Inventory) == None)
						{
							Inv = spawn(Artifacts[Index]);
							Inv.GiveTo(C.Pawn);
							break;
						}
					}
					else
						CurrentMonster++;
				}
		} until (Inv != None)

		if (Inv != None)
			CurrentArtifacts[CurrentArtifacts.length] = Inv;
	}
	else
	{
		APickup = spawn(Artifacts[Index].default.PickupClass,,, PathNodes[Rand(PathNodes.length)].Location);
		if (APickup == None)
			return;
		APickup.RespawnEffect();
		APickup.RespawnTime = 0.0;
		APickup.AddToNavigation();
		APickup.bDropped = true;
		APickup.Inventory = spawn(Artifacts[Index]);
		CurrentArtifacts[CurrentArtifacts.length] = RPGArtifact(APickup.Inventory);
	}
}

static function FillPlayInfo(PlayInfo PlayInfo)
{
	local int i;

	Super.FillPlayInfo(PlayInfo);

	PlayInfo.AddSetting("UT2004RPG", "MaxArtifacts", default.PropsDisplayText[i++], 3, 10, "Text", "2;0:25");
	PlayInfo.AddSetting("UT2004RPG", "ArtifactDelay", default.PropsDisplayText[i++], 30, 10, "Text", "3;1:100");
	PlayInfo.AddSetting("UT2004RPG", "MaxHeldArtifacts", default.PropsDisplayText[i++], 0, 10, "Text", "2;0:99");
}

static function string GetDescriptionText(string PropName)
{
	switch (PropName)
	{
		case "MaxArtifacts":	return default.PropsDescText[0];
		case "ArtifactDelay":	return default.PropsDescText[1];
		case "MaxHeldArtifacts":return default.PropsDescText[2];
	}
}

defaultproperties
{
     ArtifactDelay=30
     MaxArtifacts=6
     Artifacts(0)=Class'UT2004RPG.ArtifactInvulnerability'
     Artifacts(1)=Class'UT2004RPG.ArtifactFlight'
     Artifacts(2)=Class'UT2004RPG.ArtifactTripleDamage'
     Artifacts(3)=Class'UT2004RPG.ArtifactLightningRod'
     Artifacts(4)=Class'UT2004RPG.ArtifactTeleport'
     Artifacts(5)=Class'UT2004RPG.ArtifactMonsterSummon'
     PropsDisplayText(0)="Max Artifacts"
     PropsDisplayText(1)="Artifact Spawn Delay"
     PropsDisplayText(2)="Max Artifacts a Player Can Hold"
     PropsDescText(0)="Maximum number of artifacts in the level at once."
     PropsDescText(1)="Spawn an artifact every this many seconds."
     PropsDescText(2)="The maximum number of artifacts a player can carry at once (0 for infinity)"
     bBlockZeroExtentTraces=False
     bBlockNonZeroExtentTraces=False
}
