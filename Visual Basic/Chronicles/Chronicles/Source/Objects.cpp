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

#ifndef OBJECTS_CPP
#define OBJECTS_CPP

#include "Objects.h"

#pragma region StaticObject
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer)
{
	float Scl[3]={100.0f,100.0f,100.0f};
	float Zero[3]={0.0f,0.0f,0.0f};

	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Zero;
	this->AngularAccelaration=Zero;
	this->AngularVelocity=Zero;
	this->Position=Zero;
	this->Rotation=Zero;
	this->ScalarAccelaration=Zero;
	this->ScalarVelocity=Zero;
	this->Scale=Scl;
	this->Velocity=Zero;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer, float XPos, float YPos, float ZPos)
{
	float Pos[3]={XPos,YPos,ZPos};
	float Scl[3]={100.0f,100.0f,100.0f};
	float Zero[3]={0.0f,0.0f,0.0f};

	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Zero;
	this->AngularAccelaration=Zero;
	this->AngularVelocity=Zero;
	this->Position=Pos;
	this->Rotation=Zero;
	this->ScalarAccelaration=Zero;
	this->ScalarVelocity=Zero;
	this->Scale=Scl;
	this->Velocity=Zero;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer, float XPos, float YPos, float ZPos, float XRotPos, float YRotPos, float ZRotPos, float XScl, float YScl, float ZScl)
{
	float Pos[3]={XPos,YPos,ZPos};
	float Scl[3]={XScl,YScl,ZScl};
	float Rot[3]={XRotPos,YRotPos,ZRotPos};
	float Zero[3]={0.0f,0.0f,0.0f};

	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Zero;
	this->AngularAccelaration=Zero;
	this->AngularVelocity=Zero;
	this->Position=Pos;
	this->Rotation=Rot;
	this->ScalarAccelaration=Zero;
	this->ScalarVelocity=Zero;
	this->Scale=Scl;
	this->Velocity=Zero;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer, float XPos, float YPos, float ZPos, float XRotPos, float YRotPos, float ZRotPos, float XScl, float YScl, float ZScl, float XVel, float YVel, float ZVel, float XAccel, float YAccel, float ZAccel, float XAVel, float YAVel, float ZAVel, float XAAccel, float YAAccel, float ZAAccel, float XSVel, float YSVel, float ZSVel, float XSAccel, float YSAccel, float ZSAccel)
{
	float Accel[3]={XAccel,YAccel,ZAccel};
	float AAccel[3]={XAAccel,YAAccel,ZAAccel};
	float SAccel[3]={XSAccel,YSAccel,ZSAccel};
	float Vel[3]={XVel,YVel,ZVel};
	float AVel[3]={XAVel,YAVel,ZAVel};
	float SVel[3]={XSVel,YSVel,ZSVel};
	float Pos[3]={XPos,YPos,ZPos};
	float Rot[3]={XRotPos,YRotPos,ZRotPos};
	float Scl[3]={XScl,YScl,ZScl};

	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Accel;
	this->AngularAccelaration=AAccel;
	this->AngularVelocity=AVel;
	this->Position=Pos;
	this->Rotation=Rot;
	this->ScalarAccelaration=SAccel;
	this->ScalarVelocity=SVel;
	this->Scale=Scl;
	this->Velocity=Vel;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer, Vector3 Pos)
{
	float Scl[3]={100.0f,100.0f,100.0f};
	float Zero[3]={0.0f,0.0f,0.0f};

	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Zero;
	this->AngularAccelaration=Zero;
	this->AngularVelocity=Zero;
	this->Position=Pos;
	this->Rotation=Zero;
	this->ScalarAccelaration=Zero;
	this->ScalarVelocity=Zero;
	this->Scale=Scl;
	this->Velocity=Zero;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer, Vector3 Pos, Vector3 Rot, Vector3 Scl)
{
	float Zero[3]={0.0f,0.0f,0.0f};

	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Zero;
	this->AngularAccelaration=Zero;
	this->AngularVelocity=Zero;
	this->Position=Pos;
	this->Rotation=Rot;
	this->ScalarAccelaration=Zero;
	this->ScalarVelocity=Zero;
	this->Scale=Scl;
	this->Velocity=Zero;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::StaticObject(char* Name, bool AdjustRate, int* RefreshPointer, Vector3 Pos, Vector3 Rot, Vector3 Scl, Vector3 Vel, Vector3 Accel, Vector3 AVel, Vector3 AAccel, Vector3 SVel, Vector3 SAccel)
{
	this->ObjectName=Name;
	this->Visible=true;

	this->Accelaration=Accel;
	this->AngularAccelaration=AAccel;
	this->AngularVelocity=AVel;
	this->Position=Pos;
	this->Rotation=Rot;
	this->ScalarAccelaration=SAccel;
	this->ScalarVelocity=SVel;
	this->Scale=Scl;
	this->Velocity=Vel;

	this->ObjectEffectName="";
	this->ObjectEffectNumber=0;
	this->RefreshRatePointer=RefreshPointer;
	this->AdjustToFrameRate=AdjustRate;

	strcpy(this->ObjectFullPath,"Objects\\");
	strcat(this->ObjectFullPath,this->ObjectName);
	strcat(this->ObjectFullPath,"\\Object.X");

	strcpy(this->ObjectEffectFullPath,"Effects\\");
	strcat(this->ObjectEffectFullPath,this->ObjectEffectName);

	if(dbFileExist(this->ObjectFullPath)==1)
	{
		for(this->ObjectNumber=1;dbObjectExist(this->ObjectNumber)==1;this->ObjectNumber++);
		dbLoadObject(this->ObjectFullPath,1);
		dbShowObject(this->ObjectNumber);

		ScaleObjectV(this->Scale);
		RotateObjectV(this->Rotation);
		PositionObjectV(this->Position);
	}

	return;
}
StaticObject::~StaticObject()
{
	delete this->ObjectName;
	dbDeleteObject(this->ObjectNumber);
	dbDeleteEffect(this->ObjectEffectNumber);

	return;
}
Vector3 StaticObject::GetAccelaration()
{
	return this->Accelaration;
}
Vector3 StaticObject::GetAngularAccelaration()
{
	return this->AngularAccelaration;
}
Vector3 StaticObject::GetAngularVelocity()
{
	return this->AngularVelocity;
}
Vector3 StaticObject::GetScalarAccelaration()
{
	return this->ScalarAccelaration;
}
Vector3 StaticObject::GetScalarVelocity()
{
	return this->ScalarVelocity;
}
Vector3 StaticObject::GetVelocity()
{
	return this->Velocity;
}
Vector3 StaticObject::IncAccelaration(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->Accelaration+=Temp;

	return this->Accelaration;
}
Vector3 StaticObject::IncAccelaration(Vector3 Vector)
{
	this->IncAccelaration(Vector.X,Vector.Y,Vector.Z);

	return this->Accelaration;
}
Vector3 StaticObject::IncAngularAccelaration(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->AngularAccelaration+=Temp;

	return this->AngularAccelaration;
}
Vector3 StaticObject::IncAngularAccelaration(Vector3 Vector)
{
	this->IncAngularAccelaration(Vector.X,Vector.Y,Vector.Z);

	return this->AngularAccelaration;
}
Vector3 StaticObject::IncScalarAccelaration(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->ScalarAccelaration+=Temp;

	return this->ScalarAccelaration;
}
Vector3 StaticObject::IncScalarAccelaration(Vector3 Vector)
{
	this->IncScalarAccelaration(Vector.X,Vector.Y,Vector.Z);

	return this->ScalarAccelaration;
}
Vector3 StaticObject::IncAngularVelocity(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->AngularVelocity+=Temp;

	return this->AngularVelocity;
}
Vector3 StaticObject::IncAngularVelocity(Vector3 Vector)
{
	this->IncAngularVelocity(Vector.X,Vector.Y,Vector.Z);

	return this->AngularVelocity;
}
Vector3 StaticObject::IncScalarVelocity(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->ScalarVelocity+=Temp;

	return this->ScalarVelocity;
}
Vector3 StaticObject::IncScalarVelocity(Vector3 Vector)
{
	this->IncScalarVelocity(Vector.X,Vector.Y,Vector.Z);

	return this->ScalarVelocity;
}
Vector3 StaticObject::IncVelocity(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->Velocity+=Temp;

	return this->Velocity;
}
Vector3 StaticObject::IncVelocity(Vector3 Vector)
{
	this->IncVelocity(Vector.X,Vector.Y,Vector.Z);

	return this->Velocity;
}
Vector3 StaticObject::SetAccelaration(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->Accelaration=Temp;

	return this->Accelaration;
}
Vector3 StaticObject::SetAccelaration(Vector3 Vector)
{
	this->SetAccelaration(Vector.X,Vector.Y,Vector.Z);

	return this->Accelaration;
}
Vector3 StaticObject::SetAngularAccelaration(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->AngularAccelaration=Temp;

	return this->AngularAccelaration;
}
Vector3 StaticObject::SetAngularAccelaration(Vector3 Vector)
{
	this->SetAngularAccelaration(Vector.X,Vector.Y,Vector.Z);

	return this->AngularAccelaration;
}
Vector3 StaticObject::SetScalarAccelaration(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->ScalarAccelaration=Temp;

	return this->ScalarAccelaration;
}
Vector3 StaticObject::SetScalarAccelaration(Vector3 Vector)
{
	this->SetScalarAccelaration(Vector.X,Vector.Y,Vector.Z);

	return this->ScalarAccelaration;
}
Vector3 StaticObject::SetAngularVelocity(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->AngularVelocity=Temp;

	return this->AngularVelocity;
}
Vector3 StaticObject::SetAngularVelocity(Vector3 Vector)
{
	this->SetAngularVelocity(Vector.X,Vector.Y,Vector.Z);

	return this->AngularVelocity;
}
Vector3 StaticObject::SetScalarVelocity(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->ScalarVelocity=Temp;

	return this->ScalarVelocity;
}
Vector3 StaticObject::SetScalarVelocity(Vector3 Vector)
{
	this->SetScalarVelocity(Vector.X,Vector.Y,Vector.Z);

	return this->ScalarVelocity;
}
Vector3 StaticObject::SetVelocity(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->Velocity=Temp;

	return this->Velocity;
}
Vector3 StaticObject::SetVelocity(Vector3 Vector)
{
	this->SetVelocity(Vector.X,Vector.Y,Vector.Z);

	return this->Velocity;
}
void StaticObject::PositionObjectV(Vector3 Vector)
{
	dbPositionObject(this->ObjectNumber,Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::RotateObjectV(Vector3 Vector)
{
	dbRotateObject(this->ObjectNumber,Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::ScaleObjectV(Vector3 Vector)
{
	dbScaleObject(this->ObjectNumber,Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::MoveObject(float X, float Y, float Z)
{
	if(this->AdjustToFrameRate)
	{
		float Temp[3]={X*this->Mod,Y*this->Mod,Z*this->Mod};

		this->Position+=Temp;
	}
	else
	{
		float Temp[3]={X,Y,Z};

		this->Position+=Temp;
	}

	this->PositionObjectV(this->Position);

	return;
}
void StaticObject::MoveObject(Vector3 Vector)
{
	this->MoveObject(Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::PositionObject(float X, float Y, float Z)
{
	float Pos[3]={X,Y,Z};

	this->Position=Pos;

	this->PositionObjectV(this->Position);

	return;
}
void StaticObject::PositionObject(Vector3 Vector)
{
	this->PositionObject(Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::RescaleObject(float X, float Y, float Z)
{
	float Scl[3]={X,Y,Z};
	
	this->Scale=Scl;

	this->ScaleObjectV(this->Scale);

	return;
}
void StaticObject::RescaleObject(Vector3 Vector)
{
	this->RescaleObject(Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::RotateObject(float X, float Y, float Z)
{
	if(this->AdjustToFrameRate)
	{
		float Temp[3]={X*this->Mod,Y*this->Mod,Z*this->Mod};

		this->Rotation+=Temp;
	}
	else
	{
		float Temp[3]={X,Y,Z};

		this->Rotation+=Temp;
	}

	this->RotateObjectV(this->Rotation);

	return;
}
void StaticObject::RotateObject(Vector3 Vector)
{
	this->RotateObject(Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::ScaleObject(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->Scale*=Temp;

	this->ScaleObjectV(this->Scale);

	return;
}
void StaticObject::ScaleObject(Vector3 Vector)
{
	this->ScaleObject(Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::SetObjectRotation(float X, float Y, float Z)
{
	float Temp[3]={X,Y,Z};

	this->Rotation=Temp;

	this->RotateObjectV(this->Rotation);

	return;
}
void StaticObject::SetObjectRotation(Vector3 Vector)
{
	this->SetObjectRotation(Vector.X,Vector.Y,Vector.Z);

	return;
}
void StaticObject::Update()
{
	if(this->RefreshRatePointer[0]!=0)
		this->Mod=60.0f/this->RefreshRatePointer[0];
	else
		this->Mod=1.0f;

	if(this->GetScalarAccelaration()!=0.0f)
		this->ScalarVelocity*=this->ScalarAccelaration;
	if(this->GetScalarVelocity()!=0.0f)
		this->ScaleObject(this->GetScalarVelocity());

	this->AngularVelocity+=this->AngularAccelaration;
	this->RotateObject(this->GetAngularVelocity());

	this->Velocity+=this->Accelaration;
	this->MoveObject(this->GetVelocity());

	return;
}
#pragma endregion

#pragma region AnimatedObject

#pragma endregion

#pragma region ParticleEmitter

#pragma endregion

#endif
