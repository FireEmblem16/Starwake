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

#pragma region Declarations
#define Chronicles
#define DARKGDK
#define DARKGDK2
#include "DarkGDK.h"
#include "DarkGDK2.h"
#include "Variables.h"
#include "Functions.h"
#include "Classes.h"
#include "Input.h"
#include "Window.h"
#include "Objects.h"
#include "File Handler.h"

#pragma endregion

void DarkGDK(void)
{
	bool a=true;
	bool gameplay=true;

	Window* GameWindow=new Window(60,false,true,false,0,0,"Resources\\","Chronicles of Mu Alpha Theta");
	MouseHandler* Mouse=new MouseHandler(true,"Cursors\\Cursor BlueGold Small.png",GameWindow,1.1f);
	KeyHandler* KeyBoard=new KeyHandler(Mouse,5,5,"A","D","W","S");
	DeviceHandler* Controller=new DeviceHandler(Mouse,5,5,0.5f,0.5f,"JoyStickY Max","JoyStickX Min","JoyStickX Max","JoyStickY Min");
	StaticObject* Chest=new StaticObject("Chest",true,GameWindow->GetRefreshPtr(),Vector3(-0.25f,0.0f,2.0f),Vector3(0,0,0),Vector3(50.0f,50.0f,50.0f),Vector3(0.001f,0.001f,0.001f),Vector3(0,0,0),Vector3(2.0f,1.0f,3.0f),Vector3(0,0,0),Vector3(0.99f,0.99f,0.99f),Vector3(0,0,0));
	//StaticObject* Chest2=new StaticObject("Box",true,GameWindow->GetRefreshPtr(),Vector3(-0.25f,0.0f,2.0f),Vector3(0,0,0),Vector3(50.0f,50.0f,50.0f));
	SaveFile* Save=new SaveFile("Save Data\\Test File",0xE2);
	Mouse->SetMouseBoundToCursor(false);
//I can load objects here but it doesn't like to go through Chest2 but I can use the same object twice. Debug later.
	dbPositionCamera(0.0f,0.0f,0.0f);

	dbLoopObject(1);
	dbLoopObject(2);
	
	while(LoopGDK()&&gameplay)
	{
		Chest->Update();
		//Chest2->Update();

		if(KeyBoard->IsPressed(0)||Controller->GetIsGenericInput("Button 0",0.5f))
			gameplay=false;

		Mouse->Update();
		KeyBoard->Update();
		Controller->Update();
		GameWindow->Sync();
	}

	delete Chest;
	//delete Chest2;

	return;
}