class GhostInv extends Inventory;

var int OwnerAbilityLevel;
var array<Material> OldInstigatorSkins;
var Material OldInstigatorRepSkin;
var array<ColorModifier> GhostSkins;
var Controller OwnerController;
var vector RevivePoint;
var bool bDisabled;
var color GhostColor;
var sound GhostSound;

replication
{
	reliable if (bNetDirty && Role==ROLE_Authority)
		bDisabled;
}

function GiveTo(Pawn Other, optional Pickup Pickup)
{
	local int Count;
	local vector HitNormal, TestLocation;
	local float Dist;
	local NavigationPoint RandNavPt;

	Super.GiveTo(Other, Pickup);

	if (Instigator == None)
	{
		bDisabled = true;
		return;
	}

	OldInstigatorRepSkin = Instigator.RepSkin;
	Instigator.RepSkin = None;

	if (Instigator.Controller == None)
	{
		Instigator.TakeDamage(1, None, Instigator.Location, vect(0,0,0), class'DamageType');
		return;
	}

	if (Instigator.Weapon != None)
		Instigator.Weapon.HolderDied();
	do
	{
		Count++;
		RandNavPt = Instigator.Controller.FindRandomDest();
		if (RandNavPt == None)
			break;
		if (RandNavPt.IsA('FlyingPathNode') && !Instigator.bCanFly)
			continue;
	       	RevivePoint = RandNavPt.Location + vect(0,0,40);
	       	Dist = VSize(RevivePoint - Instigator.Location);
	} until ((Dist < 15000 && (Dist > 1000 || !FastTrace(RevivePoint, Instigator.Location))) || Count == 100000)
	if (RandNavPt == None || Count == 100000) //crappy level with no pathing - pick a random direction for the poor sucker
	{
		TestLocation = Instigator.Location + VRand() * 2000;
		if (Instigator.Trace(RevivePoint, HitNormal, TestLocation, Instigator.Location, true, Instigator.GetCollisionExtent()) == None)
			RevivePoint = TestLocation;
	}

	OwnerController = Instigator.Controller;
	if (PlayerController(Instigator.Controller) != None)
	{
		Instigator.RemoteRole = ROLE_SimulatedProxy;
		PlayerController(OwnerController).CleanOutSavedMoves();
		OwnerController.Pawn = None;
		Instigator.Controller = None;
		OwnerController.GotoState('BaseSpectating');
		PlayerController(OwnerController).ClientGotoState('BaseSpectating', '');
		PlayerController(OwnerController).SetViewTarget(Instigator);
		PlayerController(OwnerController).ClientSetViewTarget(Instigator);
		PlayerController(OwnerController).ClientSetBehindView(true);
	}
	else
	{
		Instigator.Controller.PendingStasis();
		Instigator.Controller.Pawn = None;
		Instigator.Controller = None;
	}

	Instigator.bIgnoreOutOfWorld = true;
       	Instigator.SetCollision(false);
	Instigator.bCollideWorld = false;
	Instigator.SetPhysics(PHYS_Flying);
	Instigator.Health = 9999; //heh...I wonder if anyone will get the joke?
	if (Vehicle(Instigator) != None)
		Instigator.bNoTeamBeacon = true;
	Instigator.Velocity = Normal(RevivePoint - Instigator.Location) * (Instigator.AirSpeed + Instigator.AirSpeed / 2 * (OwnerAbilityLevel - 1));
	Instigator.AmbientSound = GhostSound;

	//listen/standalone stuff
	if (Level.NetMode != NM_DedicatedServer)
	{
		CreateGhostSkins();
		Instigator.Skins = GhostSkins;
		if (Instigator.Weapon != None && Instigator.Weapon.ThirdPersonActor != None)
			Instigator.Weapon.ThirdPersonActor.bHidden = true;
	}
}

simulated function PostNetBeginPlay()
{
	if (Role < ROLE_Authority)
		SetTimer(0.1, true);
}

function DropFrom(vector StartLocation)
{
	Destroy();
}

simulated function CreateGhostSkins()
{
	local int x;

	OldInstigatorSkins = Instigator.Skins;
	for (x = 0; x < Instigator.Skins.length; x++)
	{
		GhostSkins[x] = ColorModifier(Level.ObjectPool.AllocateObject(class'ColorModifier'));
		GhostSkins[x].Material = Instigator.Skins[x];
		GhostSkins[x].AlphaBlend = true;
		GhostSkins[x].RenderTwoSided = true;
		GhostSkins[x].Color = GhostColor;
	}
}

simulated function Timer()
{
	//client-side stuff
	if (Instigator == None)
		return;
	if (bDisabled)
	{
		Deactivate();
		return;
	}
	if (GhostSkins.length == 0)
		CreateGhostSkins();
	Instigator.Skins = GhostSkins;
	if (Instigator.Weapon != None && Instigator.Weapon.ThirdPersonActor != None)
		Instigator.Weapon.ThirdPersonActor.bHidden = true;
	return;
}

function Tick(float deltaTime)
{
	if (bDisabled)
		return;
	if (VSize(Instigator.Location - RevivePoint) > VSize(Instigator.Velocity) * deltaTime)
	{
		//refresh Instigator velocity to counter air friction
		Instigator.Velocity = Normal(RevivePoint - Instigator.Location) * (Instigator.AirSpeed + Instigator.AirSpeed / 2 * (OwnerAbilityLevel - 1));
		if (Instigator.HasAnim('HitL'))
			Instigator.PlayAnim('HitL',,0.1);
		return;
	}

	if (PlayerController(OwnerController) == None)
		OwnerController.bStasis = false;
	OwnerController.Possess(Instigator);
	Instigator.bIgnoreOutOfWorld = false;
	Instigator.AmbientSound = None;
	Instigator.Velocity = vect(0,0,0);
	Instigator.SetCollision(true, true, true);
	Instigator.bCollideWorld = true;
	Instigator.SetPhysics(PHYS_Falling);
	if (Vehicle(Instigator) != None)
		Instigator.bNoTeamBeacon = Instigator.default.bNoTeamBeacon;
	switch(OwnerAbilityLevel)
	{
	case 1:
		Instigator.Health = 1;
		break;
	case 2:
		Instigator.Health = Instigator.default.Health;
		break;
	case 3:
		Instigator.Health = Instigator.HealthMax;
		break;
	}
	Level.Game.SetPlayerDefaults(Instigator);
	disable('Tick');
	bDisabled = true;
	Deactivate();
}

simulated function Deactivate()
{
	local int x;

	if (Role == ROLE_Authority)
		Instigator.RepSkin = OldInstigatorRepSkin;
	if (Level.NetMode != NM_DedicatedServer)
	{
		Instigator.Skins = OldInstigatorSkins;
		if (GhostSkins.length != 0)
		{
			for (x = 0; x < GhostSkins.length; x++)
				Level.ObjectPool.FreeObject(GhostSkins[x]);
			GhostSkins.length = 0;
		}
		if (Instigator.Weapon != None && Instigator.Weapon.ThirdPersonActor != None)
			Instigator.Weapon.ThirdPersonActor.bHidden = false;
	}
}

simulated function Destroyed()
{
	if (GhostSkins.length != 0)
		Deactivate();

	Super.Destroyed();
}

defaultproperties
{
	GhostColor=(R=255,G=255,B=255,A=64)
	GhostSound=sound'GeneralAmbience.Texture19'
	bHidden=true
	RemoteRole=ROLE_SimulatedProxy
	bOnlyRelevantToOwner=false
	bAlwaysRelevant=true
	bReplicateInstigator=true
}
