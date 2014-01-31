#pragma region Title
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////// Chronacles of Mu Alpha Theta ///////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////This game is completely fictional...mostly/////////////////////////
/////////////////////////////////Produced by Omacron Games//////////////////////////////////
//////////////////This game is not available for redistributing or copying//////////////////
/////This game is not open source so if you are reading this you better have permission/////
////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
#pragma endregion

#ifndef INPUT_H
#define INPUT_H
#pragma region Declarations
#ifndef DARKGDK
#include "DarkGDK.h"
#endif
#ifndef DARKGDK2
#include "DarkGDK2.h"
#endif
#ifndef CLASSES_H
#include "Classes.h"
#endif
#ifndef WINDOW_H
#include "Window.h"
#endif
#ifndef CMATH
#define CMATH
#include <cmath>
#endif
#pragma endregion

class MouseHandler
{
public:
	MouseHandler(bool,char*,Window*,float);
	~MouseHandler();
	bool Button3Click();
	bool Button3DoubleClick(int);
	bool Button3HoldFor(int);
	bool Button4Click();
	bool Button4DoubleClick(int);
	bool Button4HoldFor(int);
	bool GetMouseBoundToPosition();
	bool MouseLeftClick();
	bool MouseLeftDoubleClick(int);
	bool MouseLeftHoldFor(int);
	bool MouseRightClick();
	bool MouseRightDoubleClick(int);
	bool MouseRightHoldFor(int);
	bool IsClicked(int);
	bool IsDoubleClicked(int,int);
	bool IsHoldFor(int,int);
	char* GetCursorName();
	char* GetFirstClick$();
	float GetMultiplier();
	int GetCursorHeight();
	int GetCursorImage();
	int GetCursorWidth();
	int GetFirstClick();
	int MouseXPos();
	int MouseYPos();
	int MouseZPos();
	void ApplyToApp();
	void MoveMouse(int,int,int);
	void SetCursorFile(char*);
	void SetCursorImage(int);
	void SetCursorPosition(int,int);
	void SetCursorPosition(int,int,int);
	void SetMouseBoundToCursor(bool);
	void SetMouseView(bool);
	void SetMultiplier(float);
	void Update();
protected:
	bool BindMouseToPosition;
	bool ShowMouse;
	bool LeftClick[100];
	bool MouseButton3[100];
	bool MouseButton4[100];
	bool RightClick[100];
	char* CursorName;
	float Multiplier;
	int CursorHeight;
	int CursorImage;
	int CursorWidth;
	int MousePosX[10];
	int MousePosY[10];
	int MousePosZ[10];
	Window* CurrentWindow;
};

class KeyHandler
{
public:
	KeyHandler();
	KeyHandler(MouseHandler*,int,int,int,int,int,int);
	KeyHandler(MouseHandler*,int,int,char*,char*,char*,char*);
	~KeyHandler();
	bool IsBound();
	bool IsHeld(char*);
	bool IsHeld(int);
	bool IsMashed(char*);
	bool IsMashed(int);
	bool IsPressed(char*);
	bool IsPressed(int);
	char* GetFirstKey$();
	char* IntToChar(int);
	char* MouseBindDown$();
	char* MouseBindLeft$();
	char* MouseBindRight$();
	char* MouseBindUp$();
	int CharToInt(char*);
	int GetFirstKey();
	int GetMouseSpeedX();
	int GetMouseSpeedY();
	int MouseBindDown();
	int MouseBindLeft();
	int MouseBindRight();
	int MouseBindUp();
	void BindMouse(bool);
	void BindMouseXLeft(char*);
	void BindMouseXLeft(int);
	void BindMouseXRight(char*);
	void BindMouseXRight(int);
	void BindMouseYDown(char*);
	void BindMouseYDown(int);
	void BindMouseYUp(char*);
	void BindMouseYUp(int);
	void SetMouse(MouseHandler*);
	void SetMouseSpeedX(int);
	void SetMouseSpeedY(int);
	void Update();
protected:
	bool Bound;
	bool CompareStrings(char*,char*);
	bool KeyState1[256];
	bool KeyState2[256];
	bool KeyState3[256];
	bool KeyState4[256];
	bool KeyState5[256];
	char Keys[257][14];
	int KeyBound(int);
	int MouseDown;
	int MouseLeft;
	int MouseRight;
	int MouseSyncX;
	int MouseSyncY;
	int MouseUp;
	MouseHandler* CurrentMouse;
};

class DeviceHandler
{
public:
	DeviceHandler();
	DeviceHandler(MouseHandler*,int,int,float,float,char*,char*,char*,char*);
	DeviceHandler(MouseHandler*,int,int,float,float,int,int,int,int);
	~DeviceHandler();
	bool GetForceFeedback();
	bool GetForceFeedbackOn();
	bool GetIsGenericInput(char*);
	bool GetIsGenericInput(char*,float);
	bool GetIsGenericInput(int);
	bool GetIsGenericInput(int,float);
	bool GetIsJoy(int,float);
	bool GetIsJoyUndirectional(int,float);
	bool HatRotate(int,int);
	bool HatRotateLeft(int,int);
	bool HatRotateRight(int,int);
	bool IsHatPosition(int,int);
	bool IsHoldFor(int,int);
	bool IsJoyStickSliderA(float);
	bool IsJoyStickSliderAMin(float);
	bool IsJoyStickSliderAMax(float);
	bool IsJoyStickSliderB(float);
	bool IsJoyStickSliderBMin(float);
	bool IsJoyStickSliderBMax(float);
	bool IsJoyStickSliderC(float);
	bool IsJoyStickSliderCMin(float);
	bool IsJoyStickSliderCMax(float);
	bool IsJoyStickSliderD(float);
	bool IsJoyStickSliderDMin(float);
	bool IsJoyStickSliderDMax(float);
	bool IsJoyStickTwistX(float);
	bool IsJoyStickTwistXMin(float);
	bool IsJoyStickTwistXMax(float);
	bool IsJoyStickTwistY(float);
	bool IsJoyStickTwistYMin(float);
	bool IsJoyStickTwistYMax(float);
	bool IsJoyStickTwistZ(float);
	bool IsJoyStickTwistZMin(float);
	bool IsJoyStickTwistZMax(float);
	bool IsJoyStickX(float);
	bool IsJoyStickXMin(float);
	bool IsJoyStickXMax(float);
	bool IsJoyStickY(float);
	bool IsJoyStickYMin(float);
	bool IsJoyStickYMax(float);
	bool IsJoyStickZ(float);
	bool IsJoyStickZMin(float);
	bool IsJoyStickZMax(float);
	bool IsMashed(int,int);
	bool IsPressed(int);
	char* AngleToChar(int);
	char* GetCurrentDevice$();
	char* GetHatAngle$(int);
	char* IntToInputName(int);
	char* MouseBindDown$();
	char* MouseBindLeft$();
	char* MouseBindRight$();
	char* MouseBindUp$();
	float GetJoyPosition(int);
	float GetPercentBindX();
	float GetPercentBindY();
	float JoyStickASliderPosition();
	float JoyStickBSliderPosition();
	float JoyStickCSliderPosition();
	float JoyStickDSliderPosition();
	float JoyStickXPosition();
	float JoyStickYPosition();
	float JoyStickZPosition();
	float JoyStickXTwistPosition();
	float JoyStickYTwistPosition();
	float JoyStickZTwistPosition();
	int CharToAngle(char*);
	int CharToInputInt(char*);
	int GetCurrentDevice();
	int GetFirstInput(float);
	int GetHatAngle(int);
	int GetMouseSpeedX();
	int GetMouseSpeedY();
	int InputBound(int);
	int InputBound(int,float);
	int MouseBindDown();
	int MouseBindLeft();
	int MouseBindRight();
	int MouseBindUp();
	void BindMouseXLeft(char*);
	void BindMouseXLeft(int);
	void BindMouseXRight(char*);
	void BindMouseXRight(int);
	void BindMouseYDown(char*);
	void BindMouseYDown(int);
	void BindMouseYUp(char*);
	void BindMouseYUp(int);
	void SetCurrentDevice(char*);
	void SetCurrentDevice(int);
	void SetForceFeedback(bool);
	void SetMouse(MouseHandler*);
	void SetMouseSpeedX(int);
	void SetMouseSpeedY(int);
	void SetPercentBindX(float);
	void SetPercentBindY(float);
	void Update();
protected:
	bool Bound;
	bool ForceFeedback;
	bool ForceFeedbackOn;
	bool Buttons[32][100];
	char* CurrentDevice$;
	float PercentBindX;
	float PercentBindY;
	int CurrentDevice;
	int MouseDown;
	int MouseLeft;
	int MouseRight;
	int MouseSyncX;
	int MouseSyncY;
	int MouseUp;
	int JoyStickX[100];
	int JoyStickY[100];
	int JoyStickZ[100];
	int HatAngles[4][100];
	unsigned int JoyStickSliderA[100];
	unsigned int JoyStickSliderB[100];
	unsigned int JoyStickSliderC[100];
	unsigned int JoyStickSliderD[100];
	unsigned int JoyStickTwistX[100];
	unsigned int JoyStickTwistY[100];
	unsigned int JoyStickTwistZ[100];
	MouseHandler* CurrentMouse;
};

#endif