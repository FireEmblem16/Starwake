class RPGStatsMenu extends GUIPage
	DependsOn(RPGStatsInv);

var RPGStatsInv StatsInv;

var moEditBox WeaponSpeedBox, HealthBonusBox, AdrenalineMaxBox, AttackBox, DefenseBox, AmmoMaxBox, PointsAvailableBox;
//Index of first stat display, first + button and first numeric edit in controls array
var int StatDisplayControlsOffset, ButtonControlsOffset, AmtControlsOffset;
var int NumButtonControls;
var GUIListBox Abilities;

function InitComponent(GUIController MyController, GUIComponent MyOwner)
{
	//MyController.FontStack[MyController.FontStack.length] = new class'AbilityListFont';
	//MyController.RegisterStyle(class'STY_AbilityList');
	//MyController.RegisterStyle(class'STY_ResetButton');

	Super.InitComponent(MyController, MyOwner);

	WeaponSpeedBox = moEditBox(Controls[2]);
	HealthBonusBox = moEditBox(Controls[3]);
	AdrenalineMaxBox = moEditBox(Controls[4]);
	AttackBox = moEditBox(Controls[5]);
	DefenseBox = moEditBox(Controls[6]);
	AmmoMaxBox = moEditBox(Controls[7]);
	PointsAvailableBox = moEditBox(Controls[8]);
	Abilities = GUIListBox(Controls[16]);
}

function bool CloseClick(GUIComponent Sender)
{
	Controller.CloseMenu(false);

	return true;
}

function MyOnClose(optional bool bCanceled)
{
	//local int x;

	/*for (x = 0; x < Controller.FontStack.length; x++)
		if (AbilityListFont(Controller.FontStack[x]) != None)
		{
			Controller.FontStack.Remove(x, 1);
			break;
		}
	for (x = 0; x < Controller.StyleStack.Length; x++)
		if (STY_AbilityList(Controller.StyleStack[x]) != None || STY_ResetButton(Controller.StyleStack[x]) != None)
		{
			Controller.StyleStack.Remove(x, 1);
			x--;
		}*/

	if (StatsInv != None)
	{
		StatsInv.StatsMenu = None;
		StatsInv = None;
	}

	Super.OnClose(bCanceled);
}

function bool LevelsClick(GUIComponent Sender)
{
	Controller.OpenMenu("UT2004RPG.RPGPlayerLevelsMenu");
	StatsInv.ProcessPlayerLevel = RPGPlayerLevelsMenu(Controller.TopPage()).ProcessPlayerLevel;
	StatsInv.ServerRequestPlayerLevels();

	return true;
}

//Initialize, using the given RPGStatsInv for the stats data and for client->server function calls
function InitFor(RPGStatsInv Inv)
{
	local int x, y, Index, Cost, Level;
	local RPGPlayerDataObject TempDataObject;

	StatsInv = Inv;
	StatsInv.StatsMenu = self;

	WeaponSpeedBox.SetText(string(StatsInv.Data.WeaponSpeed));
	HealthBonusBox.SetText(string(StatsInv.Data.HealthBonus));
	AdrenalineMaxBox.SetText(string(StatsInv.Data.AdrenalineMax));
	AttackBox.SetText(string(StatsInv.Data.Attack));
	DefenseBox.SetText(string(StatsInv.Data.Defense));
	AmmoMaxBox.SetText(string(StatsInv.Data.AmmoMax));
	PointsAvailableBox.SetText(string(StatsInv.Data.PointsAvailable));

	if (StatsInv.Data.PointsAvailable <= 0)
		DisablePlusButtons();
	else
		EnablePlusButtons();

	//show/hide buttons if stat caps reached
	for (x = 0; x < 6; x++)
		if ( StatsInv.StatCaps[x] >= 0
		     && int(moEditBox(Controls[StatDisplayControlsOffset+x]).GetText()) >= StatsInv.StatCaps[x] )
		{
			Controls[ButtonControlsOffset+x].SetVisibility(false);
			Controls[AmtControlsOffset+x].SetVisibility(false);
		}

	TempDataObject = RPGPlayerDataObject(StatsInv.Level.ObjectPool.AllocateObject(class'RPGPlayerDataObject'));
	TempDataObject.InitFromDataStruct(StatsInv.Data);

	//Fill the ability listbox
	Abilities.List.Clear();
	for (x = 0; x < StatsInv.AllAbilities.length; x++)
	{
		Index = -1;
		for (y = 0; y < StatsInv.Data.Abilities.length; y++)
			if (StatsInv.AllAbilities[x] == StatsInv.Data.Abilities[y])
			{
				Index = y;
				y = StatsInv.Data.Abilities.length;
			}
		if (Index == -1)
			Level = 0;
		else
			Level = StatsInv.Data.AbilityLevels[Index];

		if (Level >= StatsInv.AllAbilities[x].default.MaxLevel)
			Abilities.List.Add(StatsInv.AllAbilities[x].default.AbilityName@"(Current Level: "$Level$" [MAX]"$")", StatsInv.AllAbilities[x], string(Cost));
		else
		{
			Cost = StatsInv.AllAbilities[x].static.Cost(TempDataObject, Level);

			if (Cost <= 0)
				Abilities.List.Add(StatsInv.AllAbilities[x].default.AbilityName@"(Current Level: "$Level$", Can't Buy"$")", StatsInv.AllAbilities[x], string(Cost));
			else
				Abilities.List.Add(StatsInv.AllAbilities[x].default.AbilityName@"(Current Level: "$Level$", Cost: "$Cost$")", StatsInv.AllAbilities[x], string(Cost));
		}
	}
	UpdateAbilityButtons(Abilities);

	StatsInv.Level.ObjectPool.FreeObject(TempDataObject);
}

function bool StatPlusClick(GUIComponent Sender)
{
	local int x, SenderIndex;

	for (x = ButtonControlsOffset; x < ButtonControlsOffset + NumButtonControls; x++)
		if (Controls[x] == Sender)
		{
			SenderIndex = x;
			break;
		}

	SenderIndex -= ButtonControlsOffset;
	DisablePlusButtons();
	StatsInv.ServerAddPointTo(int(GUINumericEdit(Controls[SenderIndex + AmtControlsOffset]).Value), EStatType(SenderIndex));

	return true;
}

function DisablePlusButtons()
{
	local int x;

	for (x = ButtonControlsOffset; x < ButtonControlsOffset + NumButtonControls; x++)
		Controls[x].MenuStateChange(MSAT_Disabled);
}

function EnablePlusButtons()
{
	local int x;

	for (x = ButtonControlsOffset; x < ButtonControlsOffset + NumButtonControls; x++)
		Controls[x].MenuStateChange(MSAT_Blurry);

	for (x = AmtControlsOffset; x < AmtControlsOffset + NumButtonControls; x++)
	{
		GUINumericEdit(Controls[x]).MaxValue = StatsInv.Data.PointsAvailable;
		GUINumericEdit(Controls[x]).CalcMaxLen();
		if (int(GUINumericEdit(Controls[x]).Value) > StatsInv.Data.PointsAvailable)
			GUINumericEdit(Controls[x]).SetValue(StatsInv.Data.PointsAvailable);
	}
}

function bool UpdateAbilityButtons(GUIComponent Sender)
{
	local int Cost;

	Cost = int(Abilities.List.GetExtra());
	if (Cost <= 0 || Cost > StatsInv.Data.PointsAvailable)
		Controls[18].MenuStateChange(MSAT_Disabled);
	else
		Controls[18].MenuStateChange(MSAT_Blurry);

	return true;
}

function bool ShowAbilityDesc(GUIComponent Sender)
{
	local class<RPGAbility> Ability;

	Ability = class<RPGAbility>(Abilities.List.GetObject());
	Controller.OpenMenu("UT2004RPG.RPGAbilityDescMenu");
	RPGAbilityDescMenu(Controller.TopPage()).t_WindowTitle.Caption = Ability.default.AbilityName;
	RPGAbilityDescMenu(Controller.TopPage()).MyScrollText.SetContent(Ability.default.Description);

	return true;
}

function bool BuyAbility(GUIComponent Sender)
{
	DisablePlusButtons();
	Controls[18].MenuStateChange(MSAT_Disabled);
	StatsInv.ServerAddAbility(class<RPGAbility>(Abilities.List.GetObject()));

	return true;
}

function bool ResetClick(GUIComponent Sender)
{
	Controller.OpenMenu("UT2004RPG.RPGResetConfirmPage");
	RPGResetConfirmPage(Controller.TopPage()).StatsMenu = self;
	return true;
}

defaultproperties
{
     StatDisplayControlsOffset=2
     ButtonControlsOffset=9
     AmtControlsOffset=19
     NumButtonControls=6
     bRenderWorld=True
     bAllowedAsLast=True
     OnClose=RPGStatsMenu.MyOnClose
     Begin Object Class=FloatingImage Name=FloatingFrameBackground
         Image=Texture'2K4Menus.NewControls.Display1'
         DropShadow=None
         ImageStyle=ISTY_Stretched
         ImageRenderStyle=MSTY_Normal
         WinTop=0.020000
         WinLeft=0.000000
         WinWidth=1.000000
         WinHeight=0.980000
         RenderWeight=0.000003
     End Object
     Controls(0)=FloatingImage'UT2004RPG.RPGStatsMenu.FloatingFrameBackground'

     Begin Object Class=GUIButton Name=CloseButton
         Caption="Close"
         WinTop=0.900000
         WinLeft=0.550000
         WinWidth=0.200000
         OnClick=RPGStatsMenu.CloseClick
         OnKeyEvent=CloseButton.InternalOnKeyEvent
     End Object
     Controls(1)=GUIButton'UT2004RPG.RPGStatsMenu.CloseButton'

     Begin Object Class=moEditBox Name=WeaponSpeedSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Weapon Speed Bonus (%)"
         OnCreateComponent=WeaponSpeedSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.117448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(2)=moEditBox'UT2004RPG.RPGStatsMenu.WeaponSpeedSelect'

     Begin Object Class=moEditBox Name=HealthBonusSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Health Bonus"
         OnCreateComponent=HealthBonusSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.197448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(3)=moEditBox'UT2004RPG.RPGStatsMenu.HealthBonusSelect'

     Begin Object Class=moEditBox Name=AdrenalineMaxSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Max Adrenaline"
         OnCreateComponent=AdrenalineMaxSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.277448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(4)=moEditBox'UT2004RPG.RPGStatsMenu.AdrenalineMaxSelect'

     Begin Object Class=moEditBox Name=AttackSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Damage Bonus (0.5%)"
         OnCreateComponent=AttackSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.357448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(5)=moEditBox'UT2004RPG.RPGStatsMenu.AttackSelect'

     Begin Object Class=moEditBox Name=DefenseSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Damage Reduction (0.5%)"
         OnCreateComponent=DefenseSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.437448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(6)=moEditBox'UT2004RPG.RPGStatsMenu.DefenseSelect'

     Begin Object Class=moEditBox Name=MaxAmmoSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Max Ammo Bonus (%)"
         OnCreateComponent=MaxAmmoSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.517448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(7)=moEditBox'UT2004RPG.RPGStatsMenu.MaxAmmoSelect'

     Begin Object Class=moEditBox Name=PointsAvailableSelect
         bReadOnly=True
         CaptionWidth=0.775000
         Caption="Stat Points Available"
         OnCreateComponent=PointsAvailableSelect.InternalOnCreateComponent
         IniOption="@INTERNAL"
         WinTop=0.597448
         WinLeft=0.250000
         WinWidth=0.362500
         WinHeight=0.040000
     End Object
     Controls(8)=moEditBox'UT2004RPG.RPGStatsMenu.PointsAvailableSelect'

     Begin Object Class=GUIButton Name=WeaponSpeedButton
         Caption="+"
         WinTop=0.127448
         WinLeft=0.737500
         WinWidth=0.040000
         OnClick=RPGStatsMenu.StatPlusClick
         OnKeyEvent=WeaponSpeedButton.InternalOnKeyEvent
     End Object
     Controls(9)=GUIButton'UT2004RPG.RPGStatsMenu.WeaponSpeedButton'

     Begin Object Class=GUIButton Name=HealthBonusButton
         Caption="+"
         WinTop=0.207448
         WinLeft=0.737500
         WinWidth=0.040000
         OnClick=RPGStatsMenu.StatPlusClick
         OnKeyEvent=HealthBonusButton.InternalOnKeyEvent
     End Object
     Controls(10)=GUIButton'UT2004RPG.RPGStatsMenu.HealthBonusButton'

     Begin Object Class=GUIButton Name=AdrenalineMaxButton
         Caption="+"
         WinTop=0.287448
         WinLeft=0.737500
         WinWidth=0.040000
         OnClick=RPGStatsMenu.StatPlusClick
         OnKeyEvent=AdrenalineMaxButton.InternalOnKeyEvent
     End Object
     Controls(11)=GUIButton'UT2004RPG.RPGStatsMenu.AdrenalineMaxButton'

     Begin Object Class=GUIButton Name=AttackButton
         Caption="+"
         WinTop=0.367448
         WinLeft=0.737500
         WinWidth=0.040000
         OnClick=RPGStatsMenu.StatPlusClick
         OnKeyEvent=AttackButton.InternalOnKeyEvent
     End Object
     Controls(12)=GUIButton'UT2004RPG.RPGStatsMenu.AttackButton'

     Begin Object Class=GUIButton Name=DefenseButton
         Caption="+"
         WinTop=0.447448
         WinLeft=0.737500
         WinWidth=0.040000
         OnClick=RPGStatsMenu.StatPlusClick
         OnKeyEvent=DefenseButton.InternalOnKeyEvent
     End Object
     Controls(13)=GUIButton'UT2004RPG.RPGStatsMenu.DefenseButton'

     Begin Object Class=GUIButton Name=AmmoMaxButton
         Caption="+"
         WinTop=0.527448
         WinLeft=0.737500
         WinWidth=0.040000
         OnClick=RPGStatsMenu.StatPlusClick
         OnKeyEvent=AmmoMaxButton.InternalOnKeyEvent
     End Object
     Controls(14)=GUIButton'UT2004RPG.RPGStatsMenu.AmmoMaxButton'

     Begin Object Class=GUIButton Name=LevelsButton
         Caption="See Player Levels"
         WinTop=0.900000
         WinLeft=0.225000
         WinWidth=0.250000
         OnClick=RPGStatsMenu.LevelsClick
         OnKeyEvent=LevelsButton.InternalOnKeyEvent
     End Object
     Controls(15)=GUIButton'UT2004RPG.RPGStatsMenu.LevelsButton'

     Begin Object Class=GUIListBox Name=AbilityList
         bVisibleWhenEmpty=True
         OnCreateComponent=AbilityList.InternalOnCreateComponent
         StyleName="AbilityList"
         Hint="These are the abilities you can purchase with stat points."
         WinTop=0.700000
         WinLeft=0.215000
         WinWidth=0.435000
         WinHeight=0.150000
         OnClick=RPGStatsMenu.UpdateAbilityButtons
     End Object
     Controls(16)=GUIListBox'UT2004RPG.RPGStatsMenu.AbilityList'

     Begin Object Class=GUIButton Name=AbilityDescButton
         Caption="Info"
         WinTop=0.700000
         WinLeft=0.675000
         WinWidth=0.100000
         OnClick=RPGStatsMenu.ShowAbilityDesc
         OnKeyEvent=AbilityDescButton.InternalOnKeyEvent
     End Object
     Controls(17)=GUIButton'UT2004RPG.RPGStatsMenu.AbilityDescButton'

     Begin Object Class=GUIButton Name=AbilityBuyButton
         Caption="Buy"
         WinTop=0.810000
         WinLeft=0.675000
         WinWidth=0.100000
         OnClick=RPGStatsMenu.BuyAbility
         OnKeyEvent=AbilityBuyButton.InternalOnKeyEvent
     End Object
     Controls(18)=GUIButton'UT2004RPG.RPGStatsMenu.AbilityBuyButton'

     Begin Object Class=GUINumericEdit Name=WeaponSpeedAmt
         Value="5"
         MinValue=1
         MaxValue=5
         WinTop=0.117448
         WinLeft=0.645000
         WinWidth=0.080000
         OnDeActivate=WeaponSpeedAmt.ValidateValue
     End Object
     Controls(19)=GUINumericEdit'UT2004RPG.RPGStatsMenu.WeaponSpeedAmt'

     Begin Object Class=GUINumericEdit Name=HealthBonusAmt
         Value="5"
         MinValue=1
         MaxValue=5
         WinTop=0.197448
         WinLeft=0.645000
         WinWidth=0.080000
         OnDeActivate=HealthBonusAmt.ValidateValue
     End Object
     Controls(20)=GUINumericEdit'UT2004RPG.RPGStatsMenu.HealthBonusAmt'

     Begin Object Class=GUINumericEdit Name=AdrenalineMaxAmt
         Value="5"
         MinValue=1
         MaxValue=5
         WinTop=0.277448
         WinLeft=0.645000
         WinWidth=0.080000
         OnDeActivate=AdrenalineMaxAmt.ValidateValue
     End Object
     Controls(21)=GUINumericEdit'UT2004RPG.RPGStatsMenu.AdrenalineMaxAmt'

     Begin Object Class=GUINumericEdit Name=AttackAmt
         Value="5"
         MinValue=1
         MaxValue=5
         WinTop=0.357448
         WinLeft=0.645000
         WinWidth=0.080000
         OnDeActivate=AttackAmt.ValidateValue
     End Object
     Controls(22)=GUINumericEdit'UT2004RPG.RPGStatsMenu.AttackAmt'

     Begin Object Class=GUINumericEdit Name=DefenseAmt
         Value="5"
         MinValue=1
         MaxValue=5
         WinTop=0.437448
         WinLeft=0.645000
         WinWidth=0.080000
         OnDeActivate=DefenseAmt.ValidateValue
     End Object
     Controls(23)=GUINumericEdit'UT2004RPG.RPGStatsMenu.DefenseAmt'

     Begin Object Class=GUINumericEdit Name=MaxAmmoAmt
         Value="5"
         MinValue=1
         MaxValue=5
         WinTop=0.517448
         WinLeft=0.645000
         WinWidth=0.080000
         OnDeActivate=MaxAmmoAmt.ValidateValue
     End Object
     Controls(24)=GUINumericEdit'UT2004RPG.RPGStatsMenu.MaxAmmoAmt'

     Begin Object Class=GUIButton Name=ResetButton
         Caption="Reset"
         FontScale=FNS_Small
         StyleName="ResetButton"
         WinTop=0.050000
         WinLeft=0.225000
         WinWidth=0.065000
         WinHeight=0.025000
         OnClick=RPGStatsMenu.ResetClick
         OnKeyEvent=ResetButton.InternalOnKeyEvent
     End Object
     Controls(25)=GUIButton'UT2004RPG.RPGStatsMenu.ResetButton'

     Begin Object Class=GUIHeader Name=TitleBar
         bUseTextHeight=True
         Caption="Stat Improvement"
         WinHeight=0.043750
         RenderWeight=0.100000
         bBoundToParent=True
         bScaleToParent=True
         bAcceptsInput=True
         bNeverFocus=False
         ScalingType=SCALE_X
     End Object
     Controls(26)=GUIHeader'UT2004RPG.RPGStatsMenu.TitleBar'

     WinLeft=0.200000
     WinWidth=0.600000
     WinHeight=1.000000
}
