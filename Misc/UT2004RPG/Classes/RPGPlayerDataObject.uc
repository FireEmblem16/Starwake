//RPGPlayerDataObject is used for saving player data. Using PerObjectConfig objects over arrays of structs is faster
//because native code can do the name search. Additionally, structs have a 1024 character limit when converted
//to text for .ini saving, which is not an issue for objects since they are not stored on one line.
class RPGPlayerDataObject extends Object
	config(UT2004RPG)
	PerObjectConfig;

//Player name is the object name
var config string OwnerID; //unique PlayerID of person who owns this name ("Bot" for bots)

var config int Level, Experience, WeaponSpeed, HealthBonus, AdrenalineMax, Attack, Defense, AmmoMax,
	       PointsAvailable, NeededExp;

//these two should really be a struct but can't be since they need to be able to be put into RPGPlayerData struct
//and structs inside structs are not supported
var config array<class<RPGAbility> > Abilities;
var config array<int> AbilityLevels;

//AI related
var config class<RPGAbility> BotAbilityGoal; //Bot is saving points towards this ability
var config int BotGoalAbilityCurrentLevel; //Bot's current level in the ability it wants (so don't have to search for it)

//This struct is used for the data when it needs to be replicated, since an Object cannot be
struct RPGPlayerData
{
	var int Level, Experience, WeaponSpeed, HealthBonus, AdrenalineMax;
	var int Attack, Defense, AmmoMax, PointsAvailable, NeededExp;
	var array<class<RPGAbility> > Abilities;
	var array<int> AbilityLevels;
};

function CreateDataStruct(out RPGPlayerData Data, bool bOnlyEXP)
{
	Data.Level = Level;
	Data.Experience = Experience;
	Data.NeededExp = NeededExp;
	Data.PointsAvailable = PointsAvailable;
	if (bOnlyEXP)
		return;

	Data.WeaponSpeed = WeaponSpeed;
	Data.HealthBonus = HealthBonus;
	Data.AdrenalineMax = AdrenalineMax;
	Data.Attack = Attack;
	Data.Defense = Defense;
	Data.AmmoMax = AmmoMax;
	Data.Abilities = Abilities;
	Data.AbilityLevels = AbilityLevels;
}

function InitFromDataStruct(RPGPlayerData Data)
{
	Level = Data.Level;
	Experience = Data.Experience;
	NeededExp = Data.NeededExp;
	PointsAvailable = Data.PointsAvailable;
	WeaponSpeed = Data.WeaponSpeed;
	HealthBonus = Data.HealthBonus;
	AdrenalineMax = Data.AdrenalineMax;
	Attack = Data.Attack;
	Defense = Data.Defense;
	AmmoMax = Data.AmmoMax;
	Abilities = Data.Abilities;
	AbilityLevels = Data.AbilityLevels;
}

defaultproperties
{
}
