class ParadoxPastController extends AIController;

var array<vector> locationArray;
var int index;

event Possess(Pawn inPawn, bool bVehicleTransition)
{
	super.Possess(inPawn,bVehicleTransition);
	Pawn.SetMovementPhysics();
}

function setLocationArray(array<vector> locs)
{
	locationArray=locs;
}

auto state Idle
{
	Begin:
	if(locationArray.length > 0 && index < locationArray.length)
	{
		GotoState('Reenact');
	}
	else
	{
		goto'Begin';
	}
}

state Reenact
{
	Begin:
	if(index < locationArray.length)
	{
		MoveTo(locationArray[index]);
		index++;
		goto 'Begin';
	}
	else
	{
		GotoState('Idle');
	}
}