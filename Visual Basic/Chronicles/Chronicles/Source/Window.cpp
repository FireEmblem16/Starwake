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

#ifndef WINDOW_CPP
#define WINDOW_CPP

#include "Window.h"

#pragma region Window
Window::Window(int FPS, bool Full, bool SysKeys, bool Esc, int X, int Y, char* CD, char* Title)
{
	dbHideWindow();

	this->RefreshRate=FPS;
	this->FullScreen=Full;
	this->EscapeOn=Esc;
	this->SystemKeysOn=SysKeys;
	this->WindowTitle=Title;
	this->ActiveFPS=dbScreenFPS();

	this->WindowXPos=X;
	this->WindowYPos=Y;
	this->CurrentDirectory=CD;

	this->SetWindowTitle(this->WindowTitle);
	this->SetWindowPosition(this->WindowXPos,this->WindowYPos);
	this->SetCD(this->CurrentDirectory);
	this->CommandString=dbCl$();

	dbPerformChecklistForGraphicsCards();
	for(this->NumberOfGraphicsCards=0;NULL!=dbChecklistString(this->NumberOfGraphicsCards+1);this->NumberOfGraphicsCards++);
	if(this->NumberOfGraphicsCards>0)
	{
		this->CurrentGraphicsCard=1;
		
		this->CurrentGraphicsCard$=dbChecklistString(this->CurrentGraphicsCard);
		dbSetGraphicsCard(dbChecklistString(this->CurrentGraphicsCard));
	}
	dbEmptyChecklist();

	dbPerformChecklistForDisplayModes();
	for(this->NumberOfResolutions=0;NULL!=dbChecklistString(this->NumberOfResolutions+1);this->NumberOfResolutions++);
	if(this->NumberOfResolutions>0)
	{
		if(this->FullScreen)
			this->CurrentResolution=1;
		else
			for(this->CurrentResolution=1;dbChecklistValueA(this->CurrentResolution)!=800&&dbChecklistValueB(this->CurrentResolution)!=600;this->CurrentResolution++);
		
		this->CurrentResolution$=dbChecklistString(this->CurrentResolution);
		dbSetDisplayMode(dbChecklistValueA(this->CurrentResolution),dbChecklistValueB(this->CurrentResolution),dbChecklistValueC(this->CurrentResolution));
	}
	dbEmptyChecklist();

	this->SetRefreshRate(this->RefreshRate);
	this->Depth=dbScreenDepth();
	this->Height=dbScreenHeight();
	this->Width=dbScreenWidth();

	if(this->FullScreen)
		dbSetWindowOff();
	else
	{
		dbSetWindowOn();
		dbSetWindowLayout(1,1,1);
	}

	if(this->SystemKeysOn)
		dbEnableSystemKeys();
	else
		dbDisableSystemKeys();

	if(this->EscapeOn)
		dbEnableEscapeKey();
	else
		dbDisableEscapeKey();

	dbShowWindow();
	dbWindowToFront();

	return;
}
Window::~Window()
{
	delete this->CommandString;
	delete this->CurrentDirectory;
	delete this->CurrentGraphicsCard$;
	delete this->CurrentResolution$;
	delete this->WindowTitle;

	return;
}
bool Window::GetEscapeOn()
{
	return this->EscapeOn;
}
bool Window::GetFullScreen()
{
	return this->FullScreen;
}
bool Window::GetSystemKeysOn()
{
	return this->SystemKeysOn;
}
char* Window::GetCD()
{
	return this->CurrentDirectory;
}
char* Window::GetCommandLineString()
{
	return this->CommandString;
}
char* Window::GetCurrentGraphicsCard$()
{
	return this->CurrentGraphicsCard$;
}
char* Window::GetCurrentResolution$()
{
	return this->CurrentResolution$;
}
char* Window::GetWindowTitle()
{
	return this->WindowTitle;
}
int Window::GetCurrentDepth()
{
	return this->Depth;
}
int Window::GetCurrentGraphicsCard()
{
	return this->CurrentGraphicsCard;
}
int Window::GetCurrentHeight()
{
	return this->Height;
}
int Window::GetCurrentResolution()
{
	return this->CurrentResolution;
}
int Window::GetCurrentWidth()
{
	return this->Width;
}
int Window::GetFPS()
{
	return this->ActiveFPS;
}
int Window::GetNumberOfGraphicsCards()
{
	return this->NumberOfGraphicsCards;
}
int Window::GetNumberOfResolutions()
{
	return this->NumberOfResolutions;
}
int Window::GetRefreshRate()
{
	return this->RefreshRate;
}
int Window::GetWindowXPos()
{
	return this->WindowXPos;
}
int Window::GetWindowYPos()
{
	return this->WindowYPos;
}
int* Window::GetRefreshPtr()
{
	return &(this->ActiveFPS);
}
void Window::ApplyToApp()
{
	this->SetCD(this->CurrentDirectory);
	this->SetCurrentGraphicsCard(this->CurrentGraphicsCard);
	this->SetCurrentResolution(this->CurrentResolution);
	this->SetEscapeKey(this->EscapeOn);
	this->SetFullScreen(this->FullScreen);
	this->SetRefreshRate(this->RefreshRate);
	this->SetSystemKeys(this->SystemKeysOn);
	this->SetWindowPosition(this->WindowXPos,this->WindowYPos);
	this->SetWindowTitle(this->WindowTitle);

	return;
}
void Window::SetCD(char* CD)
{
	this->CurrentDirectory=CD;

	dbCD(this->CurrentDirectory);

	return;
}
void Window::SetCurrentGraphicsCard(int GC)
{
	if(this->CurrentGraphicsCard!=GC)
	{
		this->CurrentGraphicsCard=GC;

		dbPerformChecklistForGraphicsCards();
		this->CurrentGraphicsCard$=dbChecklistString(this->CurrentGraphicsCard);
		dbSetGraphicsCard(dbChecklistString(this->CurrentGraphicsCard));
		dbEmptyChecklist();

		dbPerformChecklistForDisplayModes();
		for(this->NumberOfResolutions=0;NULL!=dbChecklistString(this->NumberOfResolutions+1);this->NumberOfResolutions++);
		for(this->SetCurrentResolution(1);dbChecklistValueA(this->CurrentResolution)==this->Width&&dbChecklistValueB(this->CurrentResolution)==this->Height&&dbChecklistValueC(this->CurrentResolution)==this->Depth;this->CurrentResolution++);
		if(this->CurrentResolution>this->NumberOfResolutions)
		{
			this->SetCurrentResolution(1);

			SetWindowPosition(this->WindowXPos,this->WindowYPos);
		}
		dbEmptyChecklist();
	}

	return;
}
void Window::SetCurrentResolution(int Res)
{
	if(this->CurrentResolution!=Res)
	{
		this->CurrentResolution=Res;

		dbPerformChecklistForDisplayModes();
		this->CurrentResolution$=dbChecklistString(this->CurrentResolution);
		dbSetDisplayMode(dbChecklistValueA(this->CurrentResolution),dbChecklistValueB(this->CurrentResolution),dbChecklistValueC(this->CurrentResolution));
		dbEmptyChecklist();

		this->Depth=dbScreenDepth();
		this->Height=dbScreenHeight();
		this->Width=dbScreenWidth();
		SetWindowPosition(this->WindowXPos,this->WindowYPos);
	}

	return;
}
void Window::SetEscapeKey(bool Esc)
{
	this->EscapeOn=Esc;

	if(this->EscapeOn)
		dbEnableEscapeKey();
	else
		dbDisableEscapeKey();

	return;
}
void Window::SetFullScreen(bool Full)
{
	this->FullScreen=Full;

	if(this->FullScreen)
		dbSetWindowOff();
	else
	{
		dbSetWindowOn();
		dbSetWindowLayout(1,1,1);
		SetWindowTitle(this->WindowTitle);
		SetWindowPosition(this->WindowXPos,this->WindowYPos);
	}

	return;
}
void Window::SetRefreshRate(int Rate)
{
	this->RefreshRate=Rate;
	dbSyncOn();
	dbSyncRate(this->RefreshRate);

	return;
}
void Window::SetSystemKeys(bool position)
{
	this->SystemKeysOn=position;

	if(this->SystemKeysOn)
		dbEnableSystemKeys();
	else
		dbDisableSystemKeys();

	return;
}
void Window::SetWindowPosition(int X, int Y)
{
	this->WindowXPos=X;
	this->WindowYPos=Y;

	dbSetWindowPosition(this->WindowXPos,this->WindowYPos);
	
	return;
}
void Window::SetWindowTitle(char* title)
{
	this->WindowTitle=title;

	dbSetWindowTitle(this->WindowTitle);

	return;
}
void Window::Sync()
{
	if(this->FullScreen)
		dbFastSync();
	else
		dbSync();

	this->ActiveFPS=dbScreenFPS();

	return;
}
#pragma endregion

#endif