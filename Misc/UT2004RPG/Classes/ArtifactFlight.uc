class ArtifactFlight extends RPGArtifact;

var Emitter FlightTrail;

function BotConsider()
{
	if ( !bActive && Instigator.Controller.RouteGoal.Location.Z - 100 > Instigator.Location.Z
	     && Instigator.Controller.Adrenaline > 5 * VSize(Instigator.Controller.RouteGoal.Location - Instigator.Location) / Instigator.AirSpeed )
		Activate();
	else if ( bActive && Instigator.Controller.RouteGoal.Location.Z - 100 < Instigator.Location.Z && !FastTrace(Instigator.Location + vect(0,0,-200), Instigator.Location))
		Activate();
}

state Activated
{
	function BeginState()
	{
		if (PlayerController(Instigator.Controller) != None)
			Instigator.Controller.GotoState('PlayerFlying');
		else
			Instigator.SetPhysics(PHYS_Flying);
		bActive = true;
		FlightTrail = Instigator.spawn(class'FlightEffect', Instigator);
	}

	function EndState()
	{
		if (Instigator != None && Instigator.Controller != None)
		{
			Instigator.SetPhysics(PHYS_Falling);
			if (PlayerController(Instigator.Controller) != None)
				Instigator.Controller.GotoState(Instigator.LandMovementState);
		}
		bActive = false;
		if (FlightTrail != None)
			FlightTrail.Kill();
	}
}

defaultproperties
{
	CostPerSec=5
	PickupClass=class'ArtifactFlightPickup'
	IconMaterial=Material'UTRPGTextures.Icons.FlightIcon'
	ItemName="Boots of Flight"
}
