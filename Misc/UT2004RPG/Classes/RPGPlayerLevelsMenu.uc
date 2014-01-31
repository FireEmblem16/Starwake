//Shows levels of all players in game
class RPGPlayerLevelsMenu extends LargeWindow;

var bool bClean;
var automated GUIScrollTextBox MyScrollText;
var automated GUIButton CloseButton;
var localized string DefaultText;

function InitComponent(GUIController MyController, GUIComponent MyOwner)
{
	Super.InitComponent(MyController, MyOwner);

	bClean = true;
	MyScrollText.SetContent(DefaultText);
}

function bool CloseClick(GUIComponent Sender)
{
	Controller.CloseMenu(false);

	return true;
}

function ProcessPlayerLevel(string PlayerString)
{
	if (PlayerString == "")
	{
		bClean = true;
		MyScrollText.SetContent(DefaultText);
	}
	else
	{
		if (bClean)
			MyScrollText.SetContent(PlayerString);
		else
			MyScrollText.AddText(PlayerString);

		bClean = false;
	}
}

defaultproperties
{
     Begin Object Class=GUIScrollTextBox Name=InfoText
         bNoTeletype=True
         CharDelay=0.002500
         EOLDelay=0.000000
         TextAlign=TXTA_Center
         OnCreateComponent=InfoText.InternalOnCreateComponent
         WinTop=0.143750
         WinHeight=0.650000
         bBoundToParent=True
         bScaleToParent=True
         bNeverFocus=True
     End Object
     MyScrollText=GUIScrollTextBox'UT2004RPG.RPGPlayerLevelsMenu.InfoText'

     Begin Object Class=GUIButton Name=ButtonClose
         Caption="Close"
         WinTop=0.800000
         WinLeft=0.400000
         WinWidth=0.200000
         OnClick=RPGPlayerLevelsMenu.CloseClick
         OnKeyEvent=ButtonClose.InternalOnKeyEvent
     End Object
     CloseButton=GUIButton'UT2004RPG.RPGPlayerLevelsMenu.ButtonClose'

     DefaultText="Receiving Player Levels from Server..."
     WindowName="Players' Levels"
     bAllowedAsLast=True
     WinTop=0.100000
     WinHeight=0.800000
}
