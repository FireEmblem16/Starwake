class ParadoxPlayerController extends PlayerController;

defaultproperties
{
   CameraClass=class'ParadoxGame.ParadoxPlayerCamera'
   InputClass=class'ParadoxGame.ParadoxPlayerInput'
}

var array<vector> locationArray;
var bool bTutorialWorking;


simulated function PostBeginPlay()
{
	super.PostBeginPlay();
	setTimer(0.1,true);
}

function Timer()
{
	locationArray.AddItem(Pawn.Location);
}

/**
 * Draw a crosshair. This function is called by the Engine.HUD class.
 */
function DrawHUD( HUD H )
{

	local float CrosshairSize;
	super.DrawHUD(H);

	H.Canvas.SetDrawColor(0,255,0,255);

	CrosshairSize = 4;

	H.Canvas.SetPos(H.CenterX - CrosshairSize, H.CenterY);
	H.Canvas.DrawRect(2*CrosshairSize + 1, 1);

	H.Canvas.SetPos(H.CenterX, H.CenterY - CrosshairSize);
	H.Canvas.DrawRect(1, 2*CrosshairSize + 1);
	//DrawDebugSphere(Pawn.Location, 30.0f, 20, 255, 0 , 0 , true);
}