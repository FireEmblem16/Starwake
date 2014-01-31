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

#ifndef OBJECTS_H
#define OBJECTS_H
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
#ifndef INPUT_H
#include "Input.h"
#endif
#pragma endregion

class StaticObject
{
public:
	StaticObject(char*,bool,int*);
	StaticObject(char*,bool,int*,float,float,float);
	StaticObject(char*,bool,int*,float,float,float,float,float,float,float,float,float);
	StaticObject(char*,bool,int*,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float,float);
	StaticObject(char*,bool,int*,Vector3);
	StaticObject(char*,bool,int*,Vector3,Vector3,Vector3);
	StaticObject(char*,bool,int*,Vector3,Vector3,Vector3,Vector3,Vector3,Vector3,Vector3,Vector3,Vector3);
	~StaticObject();
	Vector3 GetAccelaration();
	Vector3 GetAngularAccelaration();
	Vector3 GetAngularVelocity();
	Vector3 GetScalarAccelaration();
	Vector3 GetScalarVelocity();
	Vector3 GetVelocity();
	Vector3 IncAccelaration(float,float,float);
	Vector3 IncAccelaration(Vector3);
	Vector3 IncAngularAccelaration(float,float,float);
	Vector3 IncAngularAccelaration(Vector3);
	Vector3 IncScalarAccelaration(float,float,float);
	Vector3 IncScalarAccelaration(Vector3);
	Vector3 IncAngularVelocity(float,float,float);
	Vector3 IncAngularVelocity(Vector3);
	Vector3 IncScalarVelocity(float,float,float);
	Vector3 IncScalarVelocity(Vector3);
	Vector3 IncVelocity(float,float,float);
	Vector3 IncVelocity(Vector3);
	Vector3 SetAccelaration(float,float,float);
	Vector3 SetAccelaration(Vector3);
	Vector3 SetAngularAccelaration(float,float,float);
	Vector3 SetAngularAccelaration(Vector3);
	Vector3 SetScalarAccelaration(float,float,float);
	Vector3 SetScalarAccelaration(Vector3);
	Vector3 SetAngularVelocity(float,float,float);
	Vector3 SetAngularVelocity(Vector3);
	Vector3 SetScalarVelocity(float,float,float);
	Vector3 SetScalarVelocity(Vector3);
	Vector3 SetVelocity(float,float,float);
	Vector3 SetVelocity(Vector3);
	void MoveObject(float,float,float);
	void MoveObject(Vector3);
	void PositionObject(float,float,float);
	void PositionObject(Vector3);
	void RescaleObject(float,float,float);
	void RescaleObject(Vector3);
	void RotateObject(float,float,float);
	void RotateObject(Vector3);
	void ScaleObject(float,float,float);
	void ScaleObject(Vector3);
	void SetObjectRotation(float,float,float);
	void SetObjectRotation(Vector3);
	void Update();
protected:
	bool AdjustToFrameRate;
	bool Visible;
	char ObjectEffectFullPath[1000];
	char ObjectFullPath[1000];
	char ObjectTextureFullPath[1000];
	char* ObjectEffectName;
	char* ObjectName;
	float Mod;
	int ObjectEffectNumber;
	int ObjectNumber;
	int* RefreshRatePointer;
	Vector3 Accelaration;
	Vector3 AngularAccelaration;
	Vector3 AngularVelocity;
	Vector3 Position;
	Vector3 Rotation;
	Vector3 ScalarAccelaration;
	Vector3 ScalarVelocity;
	Vector3 Scale;
	Vector3 Velocity;
	void PositionObjectV(Vector3);
	void RotateObjectV(Vector3);
	void ScaleObjectV(Vector3);
};

class AnimatedObject:public StaticObject
{
public:
	
protected:
	bool AnimationOn;
	int AnimationFrame;
	int EndFrame;
	int MaxFrame;
	int StartFrame;
};

class ParticleEmitter:public AnimatedObject
{
public:
	
protected:
	float ScaleOfParticles;
	int NumberOfParticles;
};

#endif