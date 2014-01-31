class AdrenRegenInv extends Inventory;

var int RegenAmount;

function PostBeginPlay()
{
	SetTimer(5.0, true);

	Super.PostBeginPlay();
}

function Timer()
{
	local Controller C;

	if (Instigator == None || Instigator.Health <= 0)
	{
		Destroy();
		return;
	}

	C = Instigator.Controller;
	if (C == None && Instigator.DrivenVehicle != None)
		 C = Instigator.DrivenVehicle.Controller;
	if (C == None)
	{
		Destroy();
		return;
	}

	if (!Instigator.InCurrentCombo())
		C.AwardAdrenaline(RegenAmount);
}
