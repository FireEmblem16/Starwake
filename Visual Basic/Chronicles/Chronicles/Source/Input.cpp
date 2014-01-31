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

#ifndef INPUT_CPP
#define INPUT_CPP

#include "Input.h"

#pragma region MouseHandler
MouseHandler::MouseHandler(bool SeeMouse, char* MouseCursor, Window* Win, float Speeder)
{
	this->ShowMouse=SeeMouse;
	this->CursorName=MouseCursor;
	this->CurrentWindow=Win;
	this->BindMouseToPosition=true;

	if(this->CursorName!="")
		this->Multiplier=Speeder;
	else
		this->Multiplier=1.0f;

	for(int i=0;i<100;i++)
	{
		this->LeftClick[i]=false;
		this->RightClick[i]=false;
		this->MouseButton3[i]=false;
		this->MouseButton4[i]=false;
	}

	for(int i=0;i<10;i++)
	{
		this->MousePosX[i]=0;
		this->MousePosY[i]=0;
		this->MousePosZ[i]=0;
	}

	if(this->CursorName!="")
	{
		if(dbFileExist(this->CursorName)==1)
		{
			dbLoadImage(this->CursorName,1);
			for(this->CursorImage=1;dbImageExist(this->CursorImage)==1;this->CursorImage++);
			dbLoadImage(this->CursorName,this->CursorImage,1);

			this->CursorHeight=dbGetImageHeight(this->CursorImage);
			this->CursorWidth=dbGetImageWidth(this->CursorImage);
		}
		else
		{
			this->CursorName="";
			this->CursorImage=0;
			this->CursorHeight=0;
			this->CursorWidth=0;
		}
	}

	if(this->ShowMouse&&this->CursorName=="")
		dbShowMouse();
	else
		dbHideMouse();

	this->MousePosX[0]=dbMouseX();
	this->MousePosY[0]=dbMouseY();
	this->MousePosZ[0]=dbMouseZ();

	return;
}
MouseHandler::~MouseHandler()
{
	delete this->CursorName;
	delete this->CurrentWindow;
	dbDeleteImage(this->CursorImage);

	return;
}
bool MouseHandler::Button3Click()
{
	return (!this->MouseButton3[0]&&this->MouseButton3[1]);
}
bool MouseHandler::Button3DoubleClick(int MaximumLag)
{
	if(MaximumLag>96)
		return false;

	int index=1;
	int index2;

	if(this->MouseButton3[0]&&!this->MouseButton3[1])
		return false;

	for(index2=index-1;this->MouseButton3[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	for(index2=index-1;!this->MouseButton3[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	return true;
}
bool MouseHandler::Button3HoldFor(int length)
{
	if(length>100)
		length=100;

	for(int i=0;this->MouseButton3[i];i++)
		if(i==length-1)
			return true;

	return false;
}
bool MouseHandler::Button4Click()
{
	return (!this->MouseButton4[0]&&this->MouseButton4[1]);
}
bool MouseHandler::Button4DoubleClick(int MaximumLag)
{
	if(MaximumLag>96)
		return false;

	int index=1;
	int index2;

	if(this->MouseButton4[0]&&!this->MouseButton4[1])
		return false;

	for(index2=index-1;this->MouseButton4[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	for(index2=index-1;!this->MouseButton4[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	return true;
}
bool MouseHandler::Button4HoldFor(int length)
{
	if(length>100)
		length=100;

	for(int i=0;this->MouseButton4[i];i++)
		if(i==length-1)
			return true;

	return false;
}
bool MouseHandler::GetMouseBoundToPosition()
{
	return this->BindMouseToPosition;
}
bool MouseHandler::IsClicked(int Button)
{
	switch(Button)
	{
	case 1:
		return this->MouseLeftClick();
		break;
	case 2:
		return this->MouseRightClick();
		break;
	case 3:
		return this->Button3Click();
		break;
	case 4:
		return this->Button4Click();
		break;
	default:
		return false;
		break;
	}

	return false;
}
bool MouseHandler::IsDoubleClicked(int Button, int MaximumLag)
{
	switch(Button)
	{
	case 1:
		return this->MouseLeftDoubleClick(MaximumLag);
		break;
	case 2:
		return this->MouseRightDoubleClick(MaximumLag);
		break;
	case 3:
		return this->Button3DoubleClick(MaximumLag);
		break;
	case 4:
		return this->Button4DoubleClick(MaximumLag);
		break;
	default:
		return false;
		break;
	}

	return false;
}
bool MouseHandler::IsHoldFor(int Button, int Length)
{
	switch(Button)
	{
	case 1:
		return this->MouseLeftHoldFor(Length);
		break;
	case 2:
		return this->MouseRightHoldFor(Length);
		break;
	case 3:
		return this->Button3HoldFor(Length);
		break;
	case 4:
		return this->Button4HoldFor(Length);
		break;
	default:
		return false;
		break;
	}

	return false;
}
bool MouseHandler::MouseLeftClick()
{
	return (!this->LeftClick[0]&&this->LeftClick[1]);
}
bool MouseHandler::MouseLeftDoubleClick(int MaximumLag)
{
	if(MaximumLag>96)
		return false;

	int index=1;
	int index2;

	if(this->LeftClick[0]||!this->LeftClick[1])
		return false;

	for(index2=index-1;this->LeftClick[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	for(index2=index-1;!this->LeftClick[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	return true;
}
bool MouseHandler::MouseLeftHoldFor(int length)
{
	if(length>100)
		length=100;

	for(int i=0;this->LeftClick[i];i++)
		if(i==length-1)
			return true;

	return false;
}
bool MouseHandler::MouseRightClick()
{
	return (!this->RightClick[0]&&this->RightClick[1]);
}
bool MouseHandler::MouseRightDoubleClick(int MaximumLag)
{
	if(MaximumLag>96)
		return false;

	int index=1;
	int index2;

	if(this->RightClick[0]||!this->RightClick[1])
		return false;

	for(index2=index-1;this->RightClick[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	for(index2=index-1;!this->RightClick[index]&&index-index2<=MaximumLag;index++);

	if(index-index2>MaximumLag)
		return false;

	return true;
}
bool MouseHandler::MouseRightHoldFor(int length)
{
	if(length>100)
		length=100;

	for(int i=0;this->RightClick[i];i++)
		if(i==length-1)
			return true;

	return false;
}
char* MouseHandler::GetCursorName()
{
	return this->CursorName;
}
char* MouseHandler::GetFirstClick$()
{
	if(this->MouseLeftClick())
		return "Left Click";
	else if(this->MouseRightClick())
		return "Right Click";
	else if(this->Button3Click())
		return "Mouse Button 3 Click";
	else if(this->Button4Click())
		return "Mouse Button 4 Click";
	else
		return "";
}
float MouseHandler::GetMultiplier()
{
	return this->Multiplier;
}
int MouseHandler::GetCursorHeight()
{
	return this->CursorHeight;
}
int MouseHandler::GetCursorImage()
{
	return this->CursorImage;
}
int MouseHandler::GetCursorWidth()
{
	return this->CursorWidth;
}
int MouseHandler::GetFirstClick()
{
	if(this->MouseLeftClick())
		return 1;
	else if(this->MouseRightClick())
		return 2;
	else if(this->Button3Click())
		return 3;
	else if(this->Button4Click())
		return 4;
	else
		return -1;
}
int MouseHandler::MouseXPos()
{
	return this->MousePosX[0];
}
int MouseHandler::MouseYPos()
{
	return this->MousePosY[0];
}
int MouseHandler::MouseZPos()
{
	return this->MousePosZ[0];
}
void MouseHandler::ApplyToApp()
{
	this->SetCursorPosition(this->MousePosX[0],this->MousePosY[0],this->MousePosX[0]);
	this->SetMouseView(this->ShowMouse);

	return;
}
void MouseHandler::MoveMouse(int X, int Y, int Z)
{
	if(this->CursorName!="")
	{
		this->MousePosX[0]+=X;
		this->MousePosY[0]+=Y;
		this->MousePosZ[0]+=Z;

		if(this->BindMouseToPosition)
			dbPositionMouse(this->MousePosX[0],this->MousePosY[0]);
	}

	return;
}
void MouseHandler::SetCursorFile(char* name)
{
	if(dbFileExist(name)==1)
	{
		this->CursorName=name;

		dbDeleteImage(this->CursorImage);
		dbLoadImage(this->CursorName,this->CursorImage);
		dbHideMouse();

		this->CursorHeight=dbGetImageHeight(this->CursorImage);
		this->CursorWidth=dbGetImageWidth(this->CursorImage);
		this->ShowMouse=true;
	}
	else if(name=="")
	{
		this->CursorName=name;
		this->ShowMouse=true;

		dbDeleteImage(this->CursorImage);
		dbShowMouse();
	}

	return;
}
void MouseHandler::SetCursorImage(int img)
{
	this->CursorImage=img;

	return;
}
void MouseHandler::SetCursorPosition(int X, int Y)
{
	dbPositionMouse(X,Y);

	this->MousePosX[0]=X;
	this->MousePosY[0]=Y;

	return;
}
void MouseHandler::SetCursorPosition(int X, int Y, int Z)
{
	dbPositionMouse(X,Y);

	this->MousePosX[0]=X;
	this->MousePosY[0]=Y;
	this->MousePosZ[0]=Z;

	return;
}
void MouseHandler::SetMouseBoundToCursor(bool Binding)
{
	this->BindMouseToPosition=Binding;

	return;
}
void MouseHandler::SetMouseView(bool view)
{
	this->ShowMouse=view;

	if(this->ShowMouse&&this->CursorName=="")
		dbShowMouse();
	else
		dbHideMouse();

	return;
}
void MouseHandler::SetMultiplier(float Speeder)
{
	this->Multiplier=Speeder;

	return;
}
void MouseHandler::Update()
{
	for(int i=99;i>=0;i--)
	{
		this->LeftClick[i]=this->LeftClick[i-1];
		this->RightClick[i]=this->RightClick[i-1];
		this->MouseButton3[i]=this->MouseButton3[i-1];
		this->MouseButton4[i]=this->MouseButton4[i-1];
	}

	for(int i=9;i>=0;i--)
	{
		this->MousePosX[i]=this->MousePosX[i-1];
		this->MousePosY[i]=this->MousePosY[i-1];
		this->MousePosZ[i]=this->MousePosZ[i-1];
	}

	this->MousePosX[0]=this->MousePosX[1]+dbMouseMoveX()*this->Multiplier;
	this->MousePosY[0]=this->MousePosY[1]+dbMouseMoveY()*this->Multiplier;
	this->MousePosZ[0]=this->MousePosZ[1]+dbMouseMoveZ()*this->Multiplier;
	
	if(MousePosX[0]<0)
		MousePosX[0]=0;
	if(MousePosX[0]>this->CurrentWindow->GetCurrentWidth())
		MousePosX[0]=this->CurrentWindow->GetCurrentWidth();
	if(MousePosY[0]<0)
		MousePosY[0]=0;
	if(MousePosY[0]>this->CurrentWindow->GetCurrentHeight())
		MousePosY[0]=this->CurrentWindow->GetCurrentHeight();

	if(this->MousePosZ[0]>100)
		this->MousePosZ[0]=100;
	else if(this->MousePosZ[0]<0)
		this->MousePosZ[0]=0;
	
	int MouseClickValue=dbMouseClick();

	if(MouseClickValue>=8)
	{
		this->MouseButton4[0]=true;
		MouseClickValue-=8;
	}
	else
		this->MouseButton4[0]=false;
	if(MouseClickValue>=4)
	{
		this->MouseButton3[0]=true;
		MouseClickValue-=4;
	}
	else
		this->MouseButton3[0]=false;
	if(MouseClickValue>=2)
	{
		this->RightClick[0]=true;
		MouseClickValue-=2;
	}
	else
		this->RightClick[0]=false;
	if(MouseClickValue==1)
	{
		this->LeftClick[0]=true;
		MouseClickValue--;
	}
	else
		this->LeftClick[0]=false;

	if(this->ShowMouse&&this->CursorName!="")
		dbPasteImage(this->CursorImage,this->MousePosX[0]-this->CursorWidth/2,this->MousePosY[0]-this->CursorHeight/2,1);

	return;
}
#pragma endregion

#pragma region KeyHandler
KeyHandler::KeyHandler()
{
	char Temp[257][14]={"Null Key","Escape","1 Key","2 Key","3 Key","4 Key","5 Key","6 Key","7 Key","8 Key","9 Key","0 Key","- Key","= Key","Backspace","Tab","Q",
				  "W","E","R","T","Y","U","I","O","P","[","]","Right Enter","Left Control","A","S","D",
				  "F","G","H","J","K","L",";","\'","`","Left Shift","\\","Z","X","C","V","B",
				  "N","M",",",".","/","Right Shift","*","Left Alt","Space","Caps Lock","F1","F2","F3","F4","F5","F6",
				  "F7","F8","F9","F10","Num Lock","","7","8","9","-","4","5","6","+","1","2",
				  "3","0","Num .","SysRq","","","F11","F12","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","Right Enter","Right Control","","","",
				  "","","","","","","","","","","","","","","","",
				  "","Function F3","","","/ Num","","Print Screen","Right Alt","","","","","","","","",
				  "","","","","Pause","Break","Home","Up","Page Up","","Left","","Right","","End","Down",
				  "Page Down","Insert","Delete","","","","","","","","Left Windows","Right Windows","Thing","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","",""};
	
	for(int i=0;i<257;i++)
		for(int j=0;j<14;j++)
			this->Keys[i][j]=Temp[i][j];

	for(int i=0;i<256;i++)
	{
		KeyState1[i]=false;
		KeyState2[i]=false;
		KeyState3[i]=false;
		KeyState4[i]=false;
		KeyState5[i]=false;
	}

	this->Bound=false;
	this->MouseSyncX=0;
	this->MouseSyncY=0;
	this->MouseLeft=-1;
	this->MouseRight=-1;
	this->MouseDown=-1;
	this->MouseUp=-1;

	return;
}
KeyHandler::KeyHandler(MouseHandler* Mouse, int XSpeed, int YSpeed, int BindMouseXLeft, int BindMouseXRight, int BindMouseYUp, int BindMouseYDown)
{
	char Temp[257][14]={"Null Key","Escape","1 Key","2 Key","3 Key","4 Key","5 Key","6 Key","7 Key","8 Key","9 Key","0 Key","- Key","= Key","Backspace","Tab","Q",
				  "W","E","R","T","Y","U","I","O","P","[","]","Right Enter","Left Control","A","S","D",
				  "F","G","H","J","K","L",";","\'","`","Left Shift","\\","Z","X","C","V","B",
				  "N","M",",",".","/","Right Shift","*","Left Alt","Space","Caps Lock","F1","F2","F3","F4","F5","F6",
				  "F7","F8","F9","F10","Num Lock","","7","8","9","-","4","5","6","+","1","2",
				  "3","0","Num .","SysRq","","","F11","F12","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","Right Enter","Right Control","","","",
				  "","","","","","","","","","","","","","","","",
				  "","Function F3","","","/ Num","","Print Screen","Right Alt","","","","","","","","",
				  "","","","","Pause","Break","Home","Up","Page Up","","Left","","Right","","End","Down",
				  "Page Down","Insert","Delete","","","","","","","","Left Windows","Right Windows","Thing","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","",""};
	
	for(int i=0;i<257;i++)
		for(int j=0;j<14;j++)
			this->Keys[i][j]=Temp[i][j];

	for(int i=0;i<256;i++)
	{
		KeyState1[i]=false;
		KeyState2[i]=false;
		KeyState3[i]=false;
		KeyState4[i]=false;
		KeyState5[i]=false;
	}

	this->CurrentMouse=Mouse;

	if(this->CurrentMouse!=NULL)
		this->Bound=true;
	else
		this->Bound=false;

	this->MouseSyncX=XSpeed;
	this->MouseSyncY=YSpeed;
	this->MouseLeft=BindMouseXLeft;
	this->MouseRight=BindMouseXRight;
	this->MouseDown=BindMouseYDown;
	this->MouseUp=BindMouseYUp;

	return;
}
KeyHandler::KeyHandler(MouseHandler* Mouse, int XSpeed, int YSpeed, char* BindMouseXLeft, char* BindMouseXRight, char* BindMouseYUp, char* BindMouseYDown)
{
	char Temp[257][14]={"Null Key","Escape","1 Key","2 Key","3 Key","4 Key","5 Key","6 Key","7 Key","8 Key","9 Key","0 Key","- Key","= Key","Backspace","Tab","Q",
				  "W","E","R","T","Y","U","I","O","P","[","]","Right Enter","Left Control","A","S","D",
				  "F","G","H","J","K","L",";","\'","`","Left Shift","\\","Z","X","C","V","B",
				  "N","M",",",".","/","Right Shift","*","Left Alt","Space","Caps Lock","F1","F2","F3","F4","F5","F6",
				  "F7","F8","F9","F10","Num Lock","","7","8","9","-","4","5","6","+","1","2",
				  "3","0","Num .","SysRq","","","F11","F12","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","Right Enter","Right Control","","","",
				  "","","","","","","","","","","","","","","","",
				  "","Function F3","","","/ Num","","Print Screen","Right Alt","","","","","","","","",
				  "","","","","Pause","Break","Home","Up","Page Up","","Left","","Right","","End","Down",
				  "Page Down","Insert","Delete","","","","","","","","Left Windows","Right Windows","Thing","","","",
				  "","","","","","","","","","","","","","","","",
				  "","","","","","","","","","","","","","","",""};
	
	for(int i=0;i<257;i++)
		for(int j=0;j<14;j++)
			this->Keys[i][j]=Temp[i][j];

	for(int i=0;i<256;i++)
	{
		KeyState1[i]=false;
		KeyState2[i]=false;
		KeyState3[i]=false;
		KeyState4[i]=false;
		KeyState5[i]=false;
	}

	this->CurrentMouse=Mouse;

	if(this->CurrentMouse!=NULL)
		this->Bound=true;
	else
		this->Bound=false;

	this->MouseSyncX=XSpeed;
	this->MouseSyncY=YSpeed;
	this->MouseLeft=this->CharToInt(BindMouseXLeft)-1;
	this->MouseRight=this->CharToInt(BindMouseXRight)-1;
	this->MouseDown=this->CharToInt(BindMouseYDown)-1;
	this->MouseUp=this->CharToInt(BindMouseYUp)-1;

	return;
}
KeyHandler::~KeyHandler()
{
	delete this->CurrentMouse;

	return;
}
bool KeyHandler::CompareStrings(char* a, char* b)
{
	if(strlen(a)!=strlen(b))
		return false;

	for(int i=0;a[i]==b[i];i++)
		if(i==strlen(a)-1)
			return true;

	return false;
}
bool KeyHandler::IsBound()
{
	return this->Bound;
}
bool KeyHandler::IsHeld(char* Key)
{
	int i=CharToInt(Key);

	if(i>=1&&i<=256)
		return this->KeyState1[i-1]&&this->KeyState2[i-1]&&this->KeyState3[i-1]&&this->KeyState4[i-1]&&this->KeyState5[i-1];
	else
		return false;
}
bool KeyHandler::IsHeld(int Key)
{
	if(Key>=0&&Key<256)
		return this->KeyState1[Key]&&this->KeyState2[Key]&&this->KeyState3[Key]&&this->KeyState4[Key]&&this->KeyState5[Key];
	else
		return false;
}
bool KeyHandler::IsMashed(char* Key)
{
	int i=CharToInt(Key);

	if(i>=1&&i<=256)
		if(this->KeyState1[i-1])
			return !this->KeyState2[i-1]&&(this->KeyState3[i-1]||this->KeyState4[i-1]||this->KeyState5[i-1]);
	
	return false;
}
bool KeyHandler::IsMashed(int Key)
{
	if(Key>=0&&Key<256)
		if(this->KeyState1[Key])
			return !this->KeyState2[Key]&&(this->KeyState3[Key]||this->KeyState4[Key]||this->KeyState5[Key]);
	
	return false;
}
bool KeyHandler::IsPressed(char* Key)
{
	int i=CharToInt(Key);

	if(i>=1&&i<=256)
		return this->IsPressed(i-1);
	else
		return false;
}
bool KeyHandler::IsPressed(int Key)
{
	if(Key>=0&&Key<256)
		return this->KeyState1[Key];
	else
		return false;
}
char* KeyHandler::GetFirstKey$()
{
	int i;

	for(i=0;!this->KeyState1[i]&&i<256;i++);

	if(i==256)
		return this->IntToChar(0);
	else
		return this->IntToChar(i+1);
}
char* KeyHandler::IntToChar(int Key)
{
	if(Key<0||Key>256)
		return "";

	return this->Keys[Key];
}
char* KeyHandler::MouseBindDown$()
{
	return this->IntToChar(this->MouseDown+1);
}
char* KeyHandler::MouseBindLeft$()
{
	return this->IntToChar(this->MouseLeft+1);
}
char* KeyHandler::MouseBindRight$()
{
	return this->IntToChar(this->MouseRight+1);
}
char* KeyHandler::MouseBindUp$()
{
	return this->IntToChar(this->MouseUp+1);
}
int KeyHandler::CharToInt(char* Key)
{
	for(int i=1;i<257;i++)
		if(this->CompareStrings(Key,Keys[i]))
			return i;

	return 0;
}
int KeyHandler::GetFirstKey()
{
	int i;

	for(i=0;!this->KeyState1[i]&&i<256;i++);

	if(i==256)
		return -1;
	else
		return i;
}
int KeyHandler::GetMouseSpeedX()
{
	return this->MouseSyncX;
}
int KeyHandler::GetMouseSpeedY()
{
	return this->MouseSyncY;
}
int KeyHandler::KeyBound(int Binding)
{
	if(this->IsPressed(Binding))
		return 1;
	else
		return 0;
}
int KeyHandler::MouseBindDown()
{
	return this->MouseDown;
}
int KeyHandler::MouseBindLeft()
{
	return this->MouseLeft;
}
int KeyHandler::MouseBindRight()
{
	return this->MouseRight;
}
int KeyHandler::MouseBindUp()
{
	return this->MouseUp;
}
void KeyHandler::BindMouse(bool Bind)
{
	this->Bound=Bind;
	
	return;
}
void KeyHandler::BindMouseXLeft(char* i)
{
	int Key=this->CharToInt(i)-1;

	if(Key<0||Key>=256)
		return;

	this->MouseLeft=Key;

	return;
}
void KeyHandler::BindMouseXLeft(int Key)
{
	if(Key<0||Key>=256)
		return;

	this->MouseLeft=Key;

	return;
}
void KeyHandler::BindMouseXRight(char* i)
{
	int Key=this->CharToInt(i)-1;

	if(Key<0||Key>=256)
		return;

	this->MouseRight=Key;

	return;
}
void KeyHandler::BindMouseXRight(int Key)
{
	if(Key<0||Key>=256)
		return;

	this->MouseRight=Key;

	return;
}
void KeyHandler::BindMouseYDown(char* i)
{
	int Key=this->CharToInt(i)-1;

	if(Key<0||Key>=256)
		return;

	this->MouseDown=Key;

	return;
}
void KeyHandler::BindMouseYDown(int Key)
{
	if(Key<0||Key>=256)
		return;

	this->MouseDown=Key;

	return;
}
void KeyHandler::BindMouseYUp(char* i)
{
	int Key=this->CharToInt(i)-1;

	if(Key<0||Key>=256)
		return;

	this->MouseUp=Key;

	return;
}
void KeyHandler::BindMouseYUp(int Key)
{
	if(Key<0||Key>=256)
		return;

	this->MouseUp=Key;

	return;
}
void KeyHandler::SetMouse(MouseHandler* Mouse)
{
	this->CurrentMouse=Mouse;

	if(Mouse!=NULL)
		this->Bound=true;
	else
		this->Bound=false;

	return;
}
void KeyHandler::SetMouseSpeedX(int X)
{
	if(X<0)
		return;

	this->MouseSyncX=X;

	return;
}
void KeyHandler::SetMouseSpeedY(int Y)
{
	if(Y<0)
		return;

	this->MouseSyncY=Y;

	return;
}
void KeyHandler::Update()
{
	for(int i=255;i>=0;i--)
	{
		KeyState5[i]=KeyState4[i];
		KeyState4[i]=KeyState3[i];
		KeyState3[i]=KeyState2[i];
		KeyState2[i]=KeyState1[i];
	}

	for(int i=1;i<257;i++)
		KeyState1[i-1]=dbKeyState(i);

	if(this->CurrentMouse!=NULL)
		if(this->Bound||this->CurrentMouse->GetCursorName()!="")
			this->CurrentMouse->MoveMouse(this->MouseSyncX*this->KeyBound(this->MouseRight)-this->MouseSyncX*this->KeyBound(this->MouseLeft),this->MouseSyncY*this->KeyBound(this->MouseDown)-this->MouseSyncY*this->KeyBound(this->MouseUp),0);

	return;
}
#pragma endregion

#pragma region DeviceHandler
DeviceHandler::DeviceHandler()
{
	this->MouseSyncX=0;
	this->MouseSyncY=0;
	this->PercentBindX=1.0f;
	this->PercentBindY=1.0f;
	this->MouseDown=-1;
	this->MouseLeft=-1;
	this->MouseRight=-1;
	this->MouseUp=-1;
	this->CurrentMouse=NULL;
	this->Bound=false;

	for(int i=0;i<100;i++)
		for(int j=0;j<32;j++)
			this->Buttons[j][i]=false;

	for(int i=0;i<100;i++)
		for(int j=0;j<4;j++)
			this->HatAngles[j][i]=-1;

	for(int i=0;i<100;i++)
	{
		this->JoyStickSliderA[i]=0;
		this->JoyStickSliderB[i]=0;
		this->JoyStickSliderC[i]=0;
		this->JoyStickSliderD[i]=0;
		this->JoyStickTwistX[i]=0;
		this->JoyStickTwistY[i]=0;
		this->JoyStickTwistZ[i]=0;
		this->JoyStickX[i]=0;
		this->JoyStickY[i]=0;
		this->JoyStickZ[i]=0;
	}

	dbPerformChecklistControlDevices();
	for(this->CurrentDevice=1;dbChecklistString(this->CurrentDevice)==NULL;this->CurrentDevice++);
	this->CurrentDevice$=dbChecklistString(this->CurrentDevice);
	if(this->CurrentDevice$!=NULL&&this->CurrentDevice$!="")
		dbSetControlDevice(this->CurrentDevice$);
	else
		this->CurrentDevice=0;
	if(dbChecklistValueA(this->CurrentDevice)==1)
	{
		this->ForceFeedback=true;
		this->ForceFeedbackOn=true;
	}
	else
	{
		this->ForceFeedback=false;
		this->ForceFeedbackOn=false;
	}
	dbEmptyChecklist();

	return;
}
DeviceHandler::DeviceHandler(MouseHandler* Mouse, int XSpeed, int YSpeed, float PercentXBind, float PercentYBind, char* Down, char* Left, char* Right, char* Up)
{
	this->MouseSyncX=XSpeed;
	this->MouseSyncY=YSpeed;
	this->PercentBindX=PercentXBind;
	this->PercentBindY=PercentYBind;
	this->MouseDown=this->CharToInputInt(Down);
	this->MouseLeft=this->CharToInputInt(Left);
	this->MouseRight=this->CharToInputInt(Right);
	this->MouseUp=this->CharToInputInt(Up);
	this->CurrentMouse=Mouse;
	this->Bound=true;

	for(int i=0;i<100;i++)
		for(int j=0;j<32;j++)
			this->Buttons[j][i]=false;

	for(int i=0;i<100;i++)
		for(int j=0;j<4;j++)
			this->HatAngles[j][i]=-1;

	for(int i=0;i<100;i++)
	{
		this->JoyStickSliderA[i]=0;
		this->JoyStickSliderB[i]=0;
		this->JoyStickSliderC[i]=0;
		this->JoyStickSliderD[i]=0;
		this->JoyStickTwistX[i]=0;
		this->JoyStickTwistY[i]=0;
		this->JoyStickTwistZ[i]=0;
		this->JoyStickX[i]=0;
		this->JoyStickY[i]=0;
		this->JoyStickZ[i]=0;
	}

	dbPerformChecklistControlDevices();
	this->CurrentDevice=1;
	this->CurrentDevice$=dbChecklistString(this->CurrentDevice);
	if(this->CurrentDevice$!=NULL&&this->CurrentDevice$!="")
		dbSetControlDevice(this->CurrentDevice$);
	else
		this->CurrentDevice=0;
	if(dbChecklistValueA(this->CurrentDevice)==1)
	{
		this->ForceFeedback=true;
		this->ForceFeedbackOn=true;
	}
	else
	{
		this->ForceFeedback=false;
		this->ForceFeedbackOn=false;
	}
	dbEmptyChecklist();

	return;
}
DeviceHandler::DeviceHandler(MouseHandler* Mouse, int XSpeed, int YSpeed, float PercentXBind, float PercentYBind, int Down, int Left, int Right, int Up)
{
	this->MouseSyncX=XSpeed;
	this->MouseSyncY=YSpeed;
	this->PercentBindX=PercentXBind;
	this->PercentBindY=PercentYBind;
	this->MouseDown=Down;
	this->MouseLeft=Left;
	this->MouseRight=Right;
	this->MouseUp=Up;
	this->CurrentMouse=Mouse;
	this->Bound=true;

	for(int i=0;i<100;i++)
		for(int j=0;j<32;j++)
			this->Buttons[j][i]=false;

	for(int i=0;i<100;i++)
		for(int j=0;j<4;j++)
			this->HatAngles[j][i]=-1;

	for(int i=0;i<100;i++)
	{
		this->JoyStickSliderA[i]=0;
		this->JoyStickSliderB[i]=0;
		this->JoyStickSliderC[i]=0;
		this->JoyStickSliderD[i]=0;
		this->JoyStickTwistX[i]=0;
		this->JoyStickTwistY[i]=0;
		this->JoyStickTwistZ[i]=0;
		this->JoyStickX[i]=0;
		this->JoyStickY[i]=0;
		this->JoyStickZ[i]=0;
	}

	dbPerformChecklistControlDevices();
	for(this->CurrentDevice=1;dbChecklistString(this->CurrentDevice)==NULL;this->CurrentDevice++);
	this->CurrentDevice$=dbChecklistString(this->CurrentDevice);
	if(this->CurrentDevice$!=NULL&&this->CurrentDevice$!="")
		dbSetControlDevice(this->CurrentDevice$);
	else
		this->CurrentDevice=0;
	if(dbChecklistValueA(this->CurrentDevice)==1)
	{
		this->ForceFeedback=true;
		this->ForceFeedbackOn=true;
	}
	else
	{
		this->ForceFeedback=false;
		this->ForceFeedbackOn=false;
	}
	dbEmptyChecklist();

	return;
}
DeviceHandler::~DeviceHandler()
{
	delete this->CurrentDevice$;
	delete this->CurrentMouse;

	return;
}
bool DeviceHandler::GetForceFeedback()
{
	return this->ForceFeedback;
}
bool DeviceHandler::GetForceFeedbackOn()
{
	return this->ForceFeedbackOn;
}
bool DeviceHandler::GetIsGenericInput(char* InputChannel$)
{
	float Percent=0.0f;
	int InputChannel=this->CharToInputInt(InputChannel$);

	if(InputChannel<32)
		return this->IsPressed(InputChannel);

	if(InputChannel<64)
		return (this->GetHatAngle((InputChannel-32)/8)==((InputChannel-32)%8)*45);

	if(InputChannel<84)
		return this->GetIsJoy(InputChannel-63,Percent);

	return false;
}
bool DeviceHandler::GetIsGenericInput(char* InputChannel$, float Percent)
{
	int InputChannel=this->CharToInputInt(InputChannel$);

	if(InputChannel<32)
		return this->IsPressed(InputChannel);

	if(InputChannel<64)
		return (this->GetHatAngle((InputChannel-32)/8)==((InputChannel-32)%8)*45);

	if(InputChannel<84)
		return this->GetIsJoy(InputChannel-63,Percent);

	return false;
}
bool DeviceHandler::GetIsGenericInput(int InputChannel)
{
	float Percent=0.0f;

	if(InputChannel<32)
		return this->IsPressed(InputChannel);

	if(InputChannel<64)
		return (this->GetHatAngle((InputChannel-32)/8)==((InputChannel-32)%8)*45);

	if(InputChannel<84)
		return this->GetIsJoy(InputChannel-63,Percent);

	return false;
}
bool DeviceHandler::GetIsGenericInput(int InputChannel, float Percent)
{
	if(InputChannel<32)
		return this->IsPressed(InputChannel);

	if(InputChannel<64)
		return (this->GetHatAngle((InputChannel-32)/8)==((InputChannel-32)%8)*45);

	if(InputChannel<84)
		return this->GetIsJoy(InputChannel-63,Percent);

	return false;
}
bool DeviceHandler::GetIsJoy(int Joystick, float MinimumPercent)
{
	switch(Joystick)
	{
	case 1:
		return this->IsJoyStickXMin(MinimumPercent);
		break;
	case 2:
		return this->IsJoyStickXMax(MinimumPercent);
		break;
	case 3:
		return this->IsJoyStickYMin(MinimumPercent);
		break;
	case 4:
		return this->IsJoyStickYMax(MinimumPercent);
		break;
	case 5:
		return this->IsJoyStickZMin(MinimumPercent);
		break;
	case 6:
		return this->IsJoyStickZMax(MinimumPercent);
		break;
	case 7:
		return this->IsJoyStickTwistXMin(MinimumPercent);
		break;
	case 8:
		return this->IsJoyStickTwistXMax(MinimumPercent);
		break;
	case 9:
		return this->IsJoyStickTwistYMin(MinimumPercent);
		break;
	case 10:
		return this->IsJoyStickTwistYMax(MinimumPercent);
		break;
	case 11:
		return this->IsJoyStickTwistZMin(MinimumPercent);
		break;
	case 12:
		return this->IsJoyStickTwistZMax(MinimumPercent);
		break;
	case 13:
		return this->IsJoyStickSliderAMin(MinimumPercent);
		break;
	case 14:
		return this->IsJoyStickSliderAMax(MinimumPercent);
		break;
	case 15:
		return this->IsJoyStickSliderBMin(MinimumPercent);
		break;
	case 16:
		return this->IsJoyStickSliderBMax(MinimumPercent);
		break;
	case 17:
		return this->IsJoyStickSliderCMin(MinimumPercent);
		break;
	case 18:
		return this->IsJoyStickSliderCMax(MinimumPercent);
		break;
	case 19:
		return this->IsJoyStickSliderDMin(MinimumPercent);
		break;
	case 20:
		return this->IsJoyStickSliderDMax(MinimumPercent);
		break;
	default:
		return NULL;
		break;
	}
}
bool DeviceHandler::GetIsJoyUndirectional(int Joystick, float MinimumPercent)
{
	switch(Joystick)
	{
	case 1:
		return this->IsJoyStickX(MinimumPercent);
		break;
	case 2:
		return this->IsJoyStickY(MinimumPercent);
		break;
	case 3:
		return this->IsJoyStickZ(MinimumPercent);
		break;
	case 4:
		return this->IsJoyStickTwistX(MinimumPercent);
		break;
	case 5:
		return this->IsJoyStickTwistY(MinimumPercent);
		break;
	case 6:
		return this->IsJoyStickTwistZ(MinimumPercent);
		break;
	case 7:
		return this->IsJoyStickSliderA(MinimumPercent);
		break;
	case 8:
		return this->IsJoyStickSliderB(MinimumPercent);
		break;
	case 9:
		return this->IsJoyStickSliderC(MinimumPercent);
		break;
	case 10:
		return this->IsJoyStickSliderD(MinimumPercent);
		break;
	default:
		return NULL;
		break;
	}
}
bool DeviceHandler::HatRotate(int Hat, int MaximumLag)
{
	return (this->HatRotateLeft(Hat,MaximumLag)||this->HatRotateRight(Hat,MaximumLag));
}
bool DeviceHandler::HatRotateLeft(int Hat, int MaximumLag)
{
	if(this->HatAngles[Hat][0]!=-1)
	{
		bool FlagOfFirst;
		int i;
		int lasti;
		int pos;
		unsigned int start=this->HatAngles[Hat][0]/45;
		FlagOfFirst=false;
		i=0;
		if(start==0)
			pos=7;
		else
			pos=start-1;

		for(int j=0;j<9;j++)
		{
			if(i==99)
				return false;

			lasti=i;

			for(pos++;(this->HatAngles[Hat][i]==pos*45||this->HatAngles[Hat][i]==-1)&&i-lasti<=MaximumLag;i++);

			if(i-lasti>MaximumLag)
				return false;

			if(pos==7)
			{
				if(this->HatAngles[Hat][i]!=0)
					return false;

				pos=-1;
			}
			else
				if(this->HatAngles[Hat][i]!=(pos+1)*45)
					return false;

			if(pos==start&&FlagOfFirst)
				return true;
			else
				FlagOfFirst=true;
		}
	}

	return false;
}
bool DeviceHandler::HatRotateRight(int Hat, int MaximumLag)
{
	if(this->HatAngles[Hat][0]!=-1)
	{
		bool FlagOfFirst;
		int i;
		int lasti;
		int pos;
		unsigned int start=this->HatAngles[Hat][0]/45;
		FlagOfFirst=false;
		i=0;
		if(start==7)
			pos=0;
		else
			pos=start+1;

		for(int j=0;j<9;j++)
		{
			if(i==99)
				return false;

			lasti=i;

			for(pos--;(this->HatAngles[Hat][i]==pos*45||this->HatAngles[Hat][i]==-1)&&i-lasti<=MaximumLag;i++);

			if(i-lasti>MaximumLag)
				return false;

			if(pos==0)
			{
				if(this->HatAngles[Hat][i]!=45*7)
					return false;

				pos=8;
			}
			else
				if(this->HatAngles[Hat][i]!=(pos-1)*45)
					return false;

			if(pos==start&&FlagOfFirst)
				return true;
			else
				FlagOfFirst=true;
		}
	}

	return false;
}
bool DeviceHandler::IsHoldFor(int Button, int Length)
{
	int i;

	for(i=0;this->Buttons[Button][i]&&i<Length;i++);

	if(i==Length)
		return true;
	else
		return false;
}
bool DeviceHandler::IsHatPosition(int Hat, int Position)
{
	if(Hat<4&&Hat>=0)
		if(this->GetHatAngle(Hat)==Position*45)
			return true;
	
	return false;
}
bool DeviceHandler::IsMashed(int Button, int MinimumMashSpeed)
{
	int i;

	if(Button>=0&&Button<32)
		if(this->Buttons[Button][0]&&!this->Buttons[Button][1])
		{
			for(i=2;!this->Buttons[Button][i]&&i<MinimumMashSpeed+1;i++);
			if(i!=MinimumMashSpeed)
				return true;
			else
				return false;
		}

	return false;
}
bool DeviceHandler::IsPressed(int Button)
{
	return this->Buttons[Button][0];
}
bool DeviceHandler::IsJoyStickSliderA(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickASliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickASliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickASliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderAMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickASliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickASliderPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickASliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderAMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickASliderPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickASliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickASliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderB(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickBSliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickBSliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickBSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderBMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickBSliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickBSliderPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickBSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderBMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickBSliderPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickBSliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickBSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderC(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickCSliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickCSliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickCSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderCMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickCSliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickCSliderPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickCSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderCMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickCSliderPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickCSliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickCSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderD(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickDSliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickDSliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickDSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderDMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickDSliderPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickDSliderPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickDSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickSliderDMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickDSliderPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickDSliderPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickDSliderPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistX(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickXTwistPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickXTwistPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickXTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistXMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickXTwistPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickXTwistPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickXTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistXMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickXTwistPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickXTwistPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickXTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistY(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickYTwistPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickYTwistPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickYTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistYMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickYTwistPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickYTwistPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickYTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistYMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickYTwistPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickYTwistPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickYTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistZ(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickZTwistPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickZTwistPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickZTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistZMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickZTwistPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickZTwistPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickZTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickTwistZMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickZTwistPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickZTwistPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickZTwistPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickX(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickXPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickXPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickXPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickXMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickXPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickXPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickXPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickXMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickXPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickXPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickXPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickY(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickYPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickYPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickYPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickYMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickYPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickYPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickYPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickYMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickYPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickYPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickYPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickZ(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickZPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickZPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickZPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickZMax(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickZPosition()>Percent)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickZPosition()>Percent*-1)
			return true;
	}
	else
		if(this->JoyStickZPosition()==0)
			return true;

	return false;
}
bool DeviceHandler::IsJoyStickZMin(float Percent)
{
	if(Percent>0)
	{
		if(this->JoyStickZPosition()<Percent*-1)
			return true;
	}
	else if(Percent<0)
	{
		if(this->JoyStickZPosition()<Percent)
			return true;
	}
	else
		if(this->JoyStickZPosition()==0)
			return true;

	return false;
}
char* DeviceHandler::AngleToChar(int Angle)
{
	switch(Angle)
	{
	case 0:
		return "North";
		break;
	case 45:
		return "North-East";
		break;
	case 90:
		return "East";
		break;
	case 135:
		return "South-East";
		break;
	case 180:
		return "South";
		break;
	case 225:
		return "South-West";
		break;
	case 270:
		return "West";
		break;
	case 315:
		return "North-West";
		break;
	case -1:
	default:
		return "Center";
		break;
	}

	return "Center";
}
char* DeviceHandler::GetCurrentDevice$()
{
	return this->CurrentDevice$;
}
char* DeviceHandler::GetHatAngle$(int Hat)
{
	return this->AngleToChar(this->GetHatAngle(Hat));
}
char* DeviceHandler::IntToInputName(int InputType)
{
	switch(InputType)
	{
	case 0:
		return "Button 0";
		break;
	case 1:
		return "Button 1";
		break;
	case 2:
		return "Button 2";
		break;
	case 3:
		return "Button 3";
		break;
	case 4:
		return "Button 4";
		break;
	case 5:
		return "Button 5";
		break;
	case 6:
		return "Button 6";
		break;
	case 7:
		return "Button 7";
		break;
	case 8:
		return "Button 8";
		break;
	case 9:
		return "Button 9";
		break;
	case 10:
		return "Button 10";
		break;
	case 11:
		return "Button 11";
		break;
	case 12:
		return "Button 12";
		break;
	case 13:
		return "Button 13";
		break;
	case 14:
		return "Button 14";
		break;
	case 15:
		return "Button 15";
		break;
	case 16:
		return "Button 16";
		break;
	case 17:
		return "Button 17";
		break;
	case 18:
		return "Button 18";
		break;
	case 19:
		return "Button 19";
		break;
	case 20:
		return "Button 20";
		break;
	case 21:
		return "Button 21";
		break;
	case 22:
		return "Button 22";
		break;
	case 23:
		return "Button 23";
		break;
	case 24:
		return "Button 24";
		break;
	case 25:
		return "Button 25";
		break;
	case 26:
		return "Button 26";
		break;
	case 27:
		return "Button 27";
		break;
	case 28:
		return "Button 28";
		break;
	case 29:
		return "Button 29";
		break;
	case 30:
		return "Button 30";
		break;
	case 31:
		return "Button 31";
		break;
	case 32:
		return "Hat 1 North";
		break;
	case 33:
		return "Hat 1 North-East";
		break;
	case 34:
		return "Hat 1 East";
		break;
	case 35:
		return "Hat 1 South-East";
		break;
	case 36:
		return "Hat 1 South";
		break;
	case 37:
		return "Hat 1 South-West";
		break;
	case 38:
		return "Hat 1 West";
		break;
	case 39:
		return "Hat 1 North-West";
		break;
	case 40:
		return "Hat 2 North";
		break;
	case 41:
		return "Hat 2 North-East";
		break;
	case 42:
		return "Hat 2 East";
		break;
	case 43:
		return "Hat 2 South-East";
		break;
	case 44:
		return "Hat 2 South";
		break;
	case 45:
		return "Hat 2 South-West";
		break;
	case 46:
		return "Hat 2 West";
		break;
	case 47:
		return "Hat 2 North-West";
		break;
	case 48:
		return "Hat 3 North";
		break;
	case 49:
		return "Hat 3 North-East";
		break;
	case 50:
		return "Hat 3 East";
		break;
	case 51:
		return "Hat 3 South-East";
		break;
	case 52:
		return "Hat 3 South";
		break;
	case 53:
		return "Hat 3 South-West";
		break;
	case 54:
		return "Hat 3 West";
		break;
	case 55:
		return "Hat 3 North-West";
		break;
	case 56:
		return "Hat 4 North";
		break;
	case 57:
		return "Hat 4 North-East";
		break;
	case 58:
		return "Hat 4 East";
		break;
	case 59:
		return "Hat 4 South-East";
		break;
	case 60:
		return "Hat 4 South";
		break;
	case 61:
		return "Hat 4 South-West";
		break;
	case 62:
		return "Hat 4 West";
		break;
	case 63:
		return "Hat 4 North-West";
		break;
	case 64:
		return "JoyStickX Min";
		break;
	case 65:
		return "JoyStickX Max";
		break;
	case 66:
		return "JoyStickY Min";
		break;
	case 67:
		return "JoyStickY Max";
		break;
	case 68:
		return "JoyStickZ Min";
		break;
	case 69:
		return "JoyStickZ Max";
		break;
	case 70:
		return "JoyStickTwistX Min";
		break;
	case 71:
		return "JoyStickTwistX Max";
		break;
	case 72:
		return "JoyStickTwistY Min";
		break;
	case 73:
		return "JoyStickTwistY Max";
		break;
	case 74:
		return "JoyStickTwistZ Min";
		break;
	case 75:
		return "JoyStickTwistZ Max";
		break;
	case 76:
		return "JoyStickSliderA Min";
		break;
	case 77:
		return "JoyStickSliderA Max";
		break;
	case 78:
		return "JoyStickSliderB Min";
		break;
	case 79:
		return "JoyStickSliderB Max";
		break;
	case 80:
		return "JoyStickSliderC Min";
		break;
	case 81:
		return "JoyStickSliderC Max";
		break;
	case 82:
		return "JoyStickSliderD Min";
		break;
	case 83:
		return "JoyStickSliderD Max";
		break;
	default:
		return "";
		break;
	}

	return "";
}
char* DeviceHandler::MouseBindDown$()
{
	return this->IntToInputName(this->MouseDown);
}
char* DeviceHandler::MouseBindLeft$()
{
	return this->IntToInputName(this->MouseLeft);
}
char* DeviceHandler::MouseBindRight$()
{
	return this->IntToInputName(this->MouseRight);
}
char* DeviceHandler::MouseBindUp$()
{
	return this->IntToInputName(this->MouseUp);
}
float DeviceHandler::GetJoyPosition(int Joystick)
{
	switch(Joystick)
	{
	case 1:
		return this->JoyStickXPosition();
		break;
	case 2:
		return this->JoyStickYPosition();
		break;
	case 3:
		return this->JoyStickZPosition();
		break;
	case 4:
		return this->JoyStickXTwistPosition();
		break;
	case 5:
		return this->JoyStickYTwistPosition();
		break;
	case 6:
		return this->JoyStickZTwistPosition();
		break;
	case 7:
		return this->JoyStickASliderPosition();
		break;
	case 8:
		return this->JoyStickBSliderPosition();
		break;
	case 9:
		return this->JoyStickCSliderPosition();
		break;
	case 10:
		return this->JoyStickDSliderPosition();
		break;
	default:
		return NULL;
		break;
	}
}
float DeviceHandler::GetPercentBindX()
{
	return this->PercentBindX;
}
float DeviceHandler::GetPercentBindY()
{
	return this->PercentBindY;
}
float DeviceHandler::JoyStickASliderPosition()
{
	return ((float)(this->JoyStickSliderA[0]-32767))/32767;
}
float DeviceHandler::JoyStickBSliderPosition()
{
	return ((float)(this->JoyStickSliderB[0]-32767))/32767;
}
float DeviceHandler::JoyStickCSliderPosition()
{
	return ((float)(this->JoyStickSliderC[0]-32767))/32767;
}
float DeviceHandler::JoyStickDSliderPosition()
{
	return ((float)(this->JoyStickSliderD[0]-32767))/32767;
}
float DeviceHandler::JoyStickXPosition()
{
	return ((float)this->JoyStickX[0])/1000;
}
float DeviceHandler::JoyStickYPosition()
{
	return ((float)this->JoyStickY[0])/1000;
}
float DeviceHandler::JoyStickZPosition()
{
	return ((float)this->JoyStickZ[0])/1000;
}
float DeviceHandler::JoyStickXTwistPosition()
{
	return ((float)(this->JoyStickTwistX[0]-32767))/32767;
}
float DeviceHandler::JoyStickYTwistPosition()
{
	return ((float)(this->JoyStickTwistY[0]-32767))/32767;
}
float DeviceHandler::JoyStickZTwistPosition()
{
	return ((float)(this->JoyStickTwistZ[0]-32767))/32767;
}
int DeviceHandler::CharToAngle(char* Angle)
{
	if(Angle=="North")
		return 0;
	else if(Angle=="North-East")
		return 45;
	else if(Angle=="East")
		return 90;
	else if(Angle=="South-East")
		return 135;
	else if(Angle=="South")
		return 180;
	else if(Angle=="South-West")
		return 225;
	else if(Angle=="West")
		return 270;
	else if(Angle=="North-West")
		return 315;
	else if(Angle=="Center")
		return -1;
	else
		return -1;

	return -1;
}
int DeviceHandler::CharToInputInt(char* InputName)
{
	if(InputName=="Button 0")
		return 0;
	else if(InputName=="Button 1")
		return 1;
	else if(InputName=="Button 2")
		return 2;
	else if(InputName=="Button 3")
		return 3;
	else if(InputName=="Button 4")
		return 4;
	else if(InputName=="Button 5")
		return 5;
	else if(InputName=="Button 6")
		return 6;
	else if(InputName=="Button 7")
		return 7;
	else if(InputName=="Button 8")
		return 8;
	else if(InputName=="Button 9")
		return 9;
	else if(InputName=="Button 10")
		return 10;
	else if(InputName=="Button 11")
		return 11;
	else if(InputName=="Button 12")
		return 12;
	else if(InputName=="Button 13")
		return 13;
	else if(InputName=="Button 14")
		return 14;
	else if(InputName=="Button 15")
		return 15;
	else if(InputName=="Button 16")
		return 16;
	else if(InputName=="Button 17")
		return 17;
	else if(InputName=="Button 18")
		return 18;
	else if(InputName=="Button 19")
		return 19;
	else if(InputName=="Button 20")
		return 20;
	else if(InputName=="Button 21")
		return 21;
	else if(InputName=="Button 22")
		return 22;
	else if(InputName=="Button 23")
		return 23;
	else if(InputName=="Button 24")
		return 24;
	else if(InputName=="Button 25")
		return 25;
	else if(InputName=="Button 26")
		return 26;
	else if(InputName=="Button 27")
		return 27;
	else if(InputName=="Button 28")
		return 28;
	else if(InputName=="Button 29")
		return 29;
	else if(InputName=="Button 30")
		return 30;
	else if(InputName=="Button 31")
		return 31;
	else if(InputName=="Hat 1 North")
		return 32;
	else if(InputName=="Hat 1 North-East")
		return 33;
	else if(InputName=="Hat 1 East")
		return 34;
	else if(InputName=="Hat 1 South-East")
		return 35;
	else if(InputName=="Hat 1 South")
		return 36;
	else if(InputName=="Hat 1 South-West")
		return 37;
	else if(InputName=="Hat 1 West")
		return 38;
	else if(InputName=="Hat 1 North-West")
		return 39;
	else if(InputName=="Hat 2 North")
		return 40;
	else if(InputName=="Hat 2 North-East")
		return 41;
	else if(InputName=="Hat 2 East")
		return 42;
	else if(InputName=="Hat 2 South-East")
		return 43;
	else if(InputName=="Hat 2 South")
		return 44;
	else if(InputName=="Hat 2 South-West")
		return 45;
	else if(InputName=="Hat 2 West")
		return 46;
	else if(InputName=="Hat 2 North-West")
		return 47;
	else if(InputName=="Hat 3 North")
		return 48;
	else if(InputName=="Hat 3 North-East")
		return 49;
	else if(InputName=="Hat 3 East")
		return 50;
	else if(InputName=="Hat 3 South-East")
		return 51;
	else if(InputName=="Hat 3 South")
		return 52;
	else if(InputName=="Hat 3 South-West")
		return 53;
	else if(InputName=="Hat 3 West")
		return 54;
	else if(InputName=="Hat 3 North-West")
		return 55;
	else if(InputName=="Hat 4 North")
		return 56;
	else if(InputName=="Hat 4 North-East")
		return 57;
	else if(InputName=="Hat 4 East")
		return 58;
	else if(InputName=="Hat 4 South-East")
		return 59;
	else if(InputName=="Hat 4 South")
		return 60;
	else if(InputName=="Hat 4 South-West")
		return 61;
	else if(InputName=="Hat 4 West")
		return 62;
	else if(InputName=="Hat 4 North-West")
		return 63;
	else if(InputName=="JoyStickX Min")
		return 64;
	else if(InputName=="JoyStickX Max")
		return 65;
	else if(InputName=="JoyStickY Min")
		return 66;
	else if(InputName=="JoyStickY Max")
		return 67;
	else if(InputName=="JoyStickZ Min")
		return 68;
	else if(InputName=="JoyStickZ Max")
		return 69;
	else if(InputName=="JoyStickTwistX Min")
		return 70;
	else if(InputName=="JoyStickTwistX Max")
		return 71;
	else if(InputName=="JoyStickTwistY Min")
		return 72;
	else if(InputName=="JoyStickTwistY Max")
		return 73;
	else if(InputName=="JoyStickTwistZ Min")
		return 74;
	else if(InputName=="JoyStickTwistZ Max")
		return 75;
	else if(InputName=="JoyStickSliderA Min")
		return 76;
	else if(InputName=="JoyStickSliderA Max")
		return 77;
	else if(InputName=="JoyStickSliderB Min")
		return 78;
	else if(InputName=="JoyStickSliderB Max")
		return 79;
	else if(InputName=="JoyStickSliderC Min")
		return 80;
	else if(InputName=="JoyStickSliderC Max")
		return 81;
	else if(InputName=="JoyStickSliderD Min")
		return 82;
	else if(InputName=="JoyStickSliderD Max")
		return 83;
	else if(InputName==""||InputName==NULL)
		return -1;

	return -1;
}
int DeviceHandler::GetCurrentDevice()
{
	return this->CurrentDevice;
}
int DeviceHandler::GetFirstInput(float MinimumPercent)
{
	for(int i=0;i<32;i++)
		if(this->IsPressed(i))
			return i;

	for(int i=0;i<4;i++)
		if(this->GetHatAngle(i)!=-1)
			return (((this->GetHatAngle(i)/45)+32)+(i*8));

	for(int i=1;i<21;i++)
		if(this->GetIsJoy(i,MinimumPercent))
			return i+63;

	return -1;
}
int DeviceHandler::GetHatAngle(int Hat)
{
	return this->HatAngles[Hat][0];
}
int DeviceHandler::GetMouseSpeedX()
{
	return this->MouseSyncX;
}
int DeviceHandler::GetMouseSpeedY()
{
	return this->MouseSyncY;
}
int DeviceHandler::InputBound(int InputType)
{
	if(this->GetIsGenericInput(InputType))
		return 1;
	else
		return 0;
}
int DeviceHandler::InputBound(int InputType, float Percent)
{
	if(this->GetIsGenericInput(InputType,Percent))
		return 1;
	else
		return 0;
}
int DeviceHandler::MouseBindDown()
{
	return this->MouseDown;
}
int DeviceHandler::MouseBindLeft()
{
	return this->MouseLeft;
}
int DeviceHandler::MouseBindRight()
{
	return this->MouseRight;
}
int DeviceHandler::MouseBindUp()
{
	return this->MouseUp;
}
void DeviceHandler::BindMouseXLeft(char* InputType)
{
	this->MouseLeft=this->CharToInputInt(InputType);
}
void DeviceHandler::BindMouseXLeft(int InputType)
{
	this->MouseLeft=InputType;
}
void DeviceHandler::BindMouseXRight(char* InputType)
{
	this->MouseRight=this->CharToInputInt(InputType);
}
void DeviceHandler::BindMouseXRight(int InputType)
{
	this->MouseRight=InputType;
}
void DeviceHandler::BindMouseYDown(char* InputType)
{
	this->MouseDown=this->CharToInputInt(InputType);
}
void DeviceHandler::BindMouseYDown(int InputType)
{
	this->MouseDown=InputType;
}
void DeviceHandler::BindMouseYUp(char* InputType)
{
	this->MouseUp=this->CharToInputInt(InputType);
}
void DeviceHandler::BindMouseYUp(int InputType)
{
	this->MouseUp=InputType;
}
void DeviceHandler::SetCurrentDevice(char* Device)
{
	char* test;
	int i;
	
	dbPerformChecklistControlDevices();
	for(i=1;(test=dbChecklistString(i))!=Device&&test!=NULL&&test!="";i++);
	if(test!=NULL&&test!="")
	{
		this->CurrentDevice=i;
		this->CurrentDevice$=Device;
		dbSetControlDevice(this->CurrentDevice$);

		if(dbChecklistValueA(this->CurrentDevice)==1)
			this->ForceFeedback=true;
		else
			this->ForceFeedback=false;
	}
	dbEmptyChecklist();

	return;
}
void DeviceHandler::SetCurrentDevice(int Device)
{
	char* test;
	
	dbPerformChecklistControlDevices();
	test=dbChecklistString(Device);
	if(test!=NULL&&test!="")
	{
		this->CurrentDevice=Device;
		this->CurrentDevice$=test;
		dbSetControlDevice(this->CurrentDevice$);

		if(dbChecklistValueA(this->CurrentDevice)==1)
			this->ForceFeedback=true;
		else
			this->ForceFeedback=false;
	}
	dbEmptyChecklist();

	return;
}
void DeviceHandler::SetForceFeedback(bool OnOff)
{
	if(this->ForceFeedback)
		this->ForceFeedbackOn=true;
	else
		this->ForceFeedbackOn=false;

	return;
}
void DeviceHandler::SetMouse(MouseHandler* Mouse)
{
	this->CurrentMouse=Mouse;

	if(Mouse!=NULL)
		this->Bound=true;
	else
		this->Bound=false;

	return;
}
void DeviceHandler::SetMouseSpeedX(int X)
{
	if(X<0)
		return;

	this->MouseSyncX=X;

	return;
}
void DeviceHandler::SetMouseSpeedY(int Y)
{
	if(Y<0)
		return;

	this->MouseSyncY=Y;

	return;
}
void DeviceHandler::SetPercentBindX(float Percent)
{
	this->PercentBindX=Percent;

	return;
}
void DeviceHandler::SetPercentBindY(float Percent)
{
	this->PercentBindY=Percent;

	return;
}
void DeviceHandler::Update()
{
	if(this->CurrentDevice$!=NULL&&this->CurrentDevice$!="")
	{
		for(int i=99;i>0;i--)
		{
			for(int j=31;j>=0;j--)
				this->Buttons[j][i]=this->Buttons[j][i-1];

			for(int j=0;j<4;j++)
				this->HatAngles[j][i]=this->HatAngles[j][i-1];

			this->JoyStickX[i]=this->JoyStickX[i-1];
			this->JoyStickY[i]=this->JoyStickY[i-1];
			this->JoyStickZ[i]=this->JoyStickZ[i-1];
			this->JoyStickSliderA[i]=this->JoyStickSliderA[i-1];
			this->JoyStickSliderB[i]=this->JoyStickSliderB[i-1];
			this->JoyStickSliderC[i]=this->JoyStickSliderC[i-1];
			this->JoyStickSliderD[i]=this->JoyStickSliderD[i-1];
			this->JoyStickTwistX[i]=this->JoyStickTwistX[i-1];
			this->JoyStickTwistY[i]=this->JoyStickTwistY[i-1];
			this->JoyStickTwistZ[i]=this->JoyStickTwistZ[i-1];
		}

		for(int i=0;i<32;i++)
			this->Buttons[i][0]=dbJoystickFireX(i);

		for(int i=0;i<4;i++)
			if(dbJoystickHatAngle(i)!=-1)
				this->HatAngles[i][0]=dbJoystickHatAngle(i)/100;
			else
				this->HatAngles[i][0]=-1;

		this->JoyStickX[0]=dbJoystickX();
		this->JoyStickY[0]=dbJoystickY();
		this->JoyStickZ[0]=dbJoystickZ();
		this->JoyStickTwistX[0]=dbJoystickTwistX();
		this->JoyStickTwistY[0]=dbJoystickTwistY();
		this->JoyStickTwistZ[0]=dbJoystickTwistZ();
		this->JoyStickSliderA[0]=dbJoystickSliderA();
		this->JoyStickSliderB[0]=dbJoystickSliderB();
		this->JoyStickSliderC[0]=dbJoystickSliderC();
		this->JoyStickSliderD[0]=dbJoystickSliderD();
	}

	if(this->CurrentMouse!=NULL)
		if(this->Bound||this->CurrentMouse->GetCursorName()!="")
			this->CurrentMouse->MoveMouse(this->MouseSyncX*this->InputBound(this->MouseRight,this->PercentBindX)-this->MouseSyncX*this->InputBound(this->MouseLeft,this->PercentBindX),this->MouseSyncY*this->InputBound(this->MouseDown,this->PercentBindY)-this->MouseSyncY*this->InputBound(this->MouseUp,this->PercentBindY),0);

	return;
}
#pragma endregion

#endif