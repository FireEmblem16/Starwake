class ArtifactLightningRod extends RPGArtifact;

var float TargetRadius;
var int DamagePerHit;
var class<xEmitter> HitEmitterClass;

function BotConsider()
{
	if (Instigator.Controller.Adrenaline < 30)
		return;

	if (bActive && (Instigator.Controller.Enemy == None || !Instigator.Controller.CanSee(Instigator.Controller.Enemy)))
		Activate();
	else if ( !bActive && Instigator.Controller.Enemy != None
		  && Instigator.Controller.Enemy.Health < 70 && Instigator.Controller.CanSee(Instigator.Controller.Enemy) && NoArtifactsActive() && FRand() < 0.7 )
		Activate();
}

state Activated
{
	function Timer()
	{
		local Controller C;
		local int Targets;
		local xEmitter HitEmitter;

		//need to be moving for it to do anything... so can't just sit somewhere and camp
		if (VSize(Instigator.Velocity) ~= 0)
		{
			CostPerSec = default.CostPerSec;
			return;
		}

		for (C = Level.ControllerList; C != None; C = C.NextController)
			if ( C.Pawn != None && C.Pawn != Instigator && C.Pawn.Health > 0 && !C.SameTeamAs(Instigator.Controller)
			     && VSize(C.Pawn.Location - Instigator.Location) < TargetRadius && FastTrace(C.Pawn.Location, Instigator.Location) )
			{
				HitEmitter = spawn(HitEmitterClass,,, Instigator.Location, rotator(C.Pawn.Location - Instigator.Location));
				if (HitEmitter != None)
					HitEmitter.mSpawnVecA = C.Pawn.Location;
				C.Pawn.TakeDamage(DamagePerHit, Instigator, C.Pawn.Location, vect(0,0,0), class'DamTypeLightningRod');
				Targets++;
			}

		CostPerSec = default.CostPerSec + Targets * 3;
	}

	function BeginState()
	{
		SetTimer(0.5, true);
		bActive = true;
	}

	function EndState()
	{
		SetTimer(0, false);
		bActive = false;
	}
}

defaultproperties
{
	TargetRadius=4000
	DamagePerHit=10
	HitEmitterClass=class'LightningBolt'

	CostPerSec=5
	PickupClass=class'ArtifactLightningRodPickup'
	IconMaterial=Material'UTRPGTextures.Icons.LightningIcon'
	ItemName="Lightning Rod"
}
