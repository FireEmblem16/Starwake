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

#ifndef WINDOW_H
#define WINDOW_H
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
#pragma endregion

class Window
{
public:
	Window(int,bool,bool,bool,int,int,char*,char*);
	~Window();
	bool GetEscapeOn();
	bool GetFullScreen();
	bool GetSystemKeysOn();
	char* GetCD();
	char* GetCommandLineString();
	char* GetCurrentGraphicsCard$();
	char* GetCurrentResolution$();
	char* GetWindowTitle();
	int GetCurrentDepth();
	int GetCurrentGraphicsCard();
	int GetCurrentHeight();
	int GetCurrentResolution();
	int GetCurrentWidth();
	int GetFPS();
	int GetNumberOfGraphicsCards();
	int GetNumberOfResolutions();
	int GetRefreshRate();
	int GetWindowXPos();
	int GetWindowYPos();
	int* GetRefreshPtr();
	void ApplyToApp();
	void SetCD(char*);
	void SetCurrentGraphicsCard(int);
	void SetCurrentResolution(int);
	void SetEscapeKey(bool);
	void SetFullScreen(bool);
	void SetRefreshRate(int);
	void SetSystemKeys(bool);
	void SetWindowPosition(int,int);
	void SetWindowTitle(char*);
	void Sync();
protected:
	bool EscapeOn;
	bool FullScreen;
	bool SystemKeysOn;
	char* CommandString;
	char* CurrentDirectory;
	char* CurrentGraphicsCard$;
	char* CurrentResolution$;
	char* WindowTitle;
	int ActiveFPS;
	int CurrentGraphicsCard;
	int CurrentResolution;
	int Depth;
	int Height;
	int NumberOfGraphicsCards;
	int NumberOfResolutions;
	int RefreshRate;
	int Width;
	int WindowXPos;
	int WindowYPos;
};

#endif