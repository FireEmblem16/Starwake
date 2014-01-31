class ShieldRegenInv extends Inventory;

var int RegenAmount;

function PostBeginPlay()
{
	SetTimer(1.0, true);

	Super.PostBeginPlay();
}

function Timer()
{
	local int s;
	local int m;
	local int toadd;

	if (Instigator == None || Instigator.Health <= 0)
	{
		Destroy();
		return;
	}

	s = Instigator.GetShieldStrength();
	m = xPawn(Instigator).ShieldStrengthMax;
	toadd = RegenAmount;

	if(RegenAmount + s > m)
		toadd = m - s;

	if (toadd > 0)
		Instigator.AddShieldStrength(toadd);
}