//I'm getting tired of using this hack...
//This particular one gives friendly monsters a tinted skin so everyone knows
//which team it's on
class FriendlyMonsterSkinner extends ReplicationInfo;

var Monster MonsterOwner;
var byte Team;
var array<ColorModifier> MonsterSkins;
var config color TeamColor[2];

replication
{
	reliable if (Role == ROLE_Authority)
		MonsterOwner, Team;
}

function PostBeginPlay()
{
	Super.PostBeginPlay();

	MonsterOwner = Monster(Owner);
	if (MonsterOwner == None)
		Destroy();
}

function SetInitialState()
{
	Super.SetInitialState();
	disable('Tick'); //not needed on server
}

simulated function CreateMonsterSkins()
{
	local int x;

	if (MonsterSkins.length == 0)
	{
		if (bDeleteMe)
			return;

		MonsterSkins.length = MonsterOwner.Skins.length;
		for (x = 0; x < MonsterOwner.Skins.length; x++)
		{
			MonsterSkins[x] = ColorModifier(Level.ObjectPool.AllocateObject(class'ColorModifier'));
			MonsterSkins[x].Color = TeamColor[Team];
			MonsterSkins[x].Material = MonsterOwner.Skins[x];
		}
	}

	MonsterOwner.Skins = MonsterSkins;
}

simulated function Destroyed()
{
	local int x;

	for (x = 0; x < MonsterSkins.length; x++)
		Level.ObjectPool.FreeObject(MonsterSkins[x]);
	MonsterSkins.length = 0;

	Super.Destroyed();
}

simulated function Tick(float deltaTime)
{
	if (MonsterOwner != None)
		CreateMonsterSkins();
}

defaultproperties
{
     TeamColor(0)=(B=160,G=160,R=255,A=255)
     TeamColor(1)=(B=255,G=160,R=160,A=255)
     bGameRelevant=True
}
