class ArtifactTeleport extends RPGArtifact;

var Emitter myEmitter;
var float AdrenalineUsed;

function BotConsider()
{
	if (bActive)
		return;

	if ( (Instigator.Health + Instigator.ShieldStrength < 70 || (Bot(Instigator.Controller) != None && Bot(Instigator.Controller).NeedWeapon()))
	      && (Instigator.Controller.Enemy == None || !Instigator.Controller.CanSee(Instigator.Controller.Enemy))
	      && (Instigator.Controller.Adrenaline > 2 * CostPerSec || NoArtifactsActive()) )
		Activate();
}

function DoEffect();

state Activated
{
	function BeginState()
	{
		local int x;

		myEmitter = spawn(class'TeleportChargeEffect', Instigator,, Instigator.Location, Instigator.Rotation);
		myEmitter.SetBase(Instigator);
		if (Instigator.PlayerReplicationInfo != None && Instigator.PlayerReplicationInfo.Team != None && Instigator.PlayerReplicationInfo.Team.TeamIndex == 1)
			for (x = 0; x < myEmitter.Emitters[0].ColorScale.Length; x++)
				myEmitter.Emitters[0].ColorScale[x].Color = class'Hud'.default.BlueColor;

		bActive = true;
		AdrenalineUsed = CostPerSec;
	}

	simulated function Tick(float deltaTime)
	{
		Global.Tick(deltaTime);

		AdrenalineUsed -= deltaTime * CostPerSec;
		if (AdrenalineUsed <= 0)
		{
			Instigator.Controller.Adrenaline -= AdrenalineUsed; //add some back if went a bit over
			DoEffect();
		}
	}

	function DoEffect()
	{
		local NavigationPoint Dest;
		local vector PrevLocation;
		local int EffectNum;

		if (myEmitter != None)
		{
			myEmitter.SetBase(None);
			myEmitter.Kill();
			myEmitter = None;
		}

		Dest = Instigator.Controller.FindRandomDest();
		PrevLocation = Instigator.Location;
		Instigator.SetLocation(Dest.Location + vect(0,0,40));
		if (xPawn(Instigator) != None)
			xPawn(Instigator).DoTranslocateOut(PrevLocation);
		if (Instigator.PlayerReplicationInfo != None && Instigator.PlayerReplicationInfo.Team != None)
			EffectNum = Instigator.PlayerReplicationInfo.Team.TeamIndex;
		Instigator.SetOverlayMaterial(class'TransRecall'.default.TransMaterials[EffectNum], 1.0, false);
		Instigator.PlayTeleportEffect(false, false);

		GotoState('');
	}

	function EndState()
	{
		if (myEmitter != None)
			myEmitter.Destroy();
		bActive = false;
	}
}

defaultproperties
{
	CostPerSec=25
	MinActivationTime=1.0
	ItemName="Teleporter"
	PickupClass=class'ArtifactTeleportPickup'
	IconMaterial=Material'UTRPGTextures.Icons.TeleporterIcon'
}
