class ParadoxPlayerInput extends PlayerInput within ParadoxPlayerController;

simulated exec function TutorialAbility()
{
	local ParadoxPastController past;
	local ParadoxPawn prawns;
	prawns = Spawn(class 'ParadoxPawn',,,locationArray[0],,,);
	past = Spawn(class'ParadoxPastController',,,locationArray[0],,,);
	past.setLocationArray(Outer.locationArray);
	past.Possess(prawns,false);
	Outer.locationArray.length = 0;
	bTutorialWorking = !bTutorialWorking;
	`log("bTutorialWorking?: "@bTutorialWorking);
}