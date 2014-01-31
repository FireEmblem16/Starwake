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

#ifndef CLASSES_CPP
#define CLASSES_CPP

#include "Classes.h"

#pragma region Vector3
Vector3::Vector3()
{
	this->X=0.0f;
	this->Y=0.0f;
	this->Z=0.0f;

	return;
}
Vector3::Vector3(double X, double Y, double Z)
{
	this->X=X;
	this->Y=Y;
	this->Z=Z;

	return;
}
Vector3::Vector3(float X, float Y, float Z)
{
	this->X=X;
	this->Y=Y;
	this->Z=Z;

	return;
}
Vector3::Vector3(int X, int Y, int Z)
{
	this->X=X;
	this->Y=Y;
	this->Z=Z;

	return;
}
Vector3::~Vector3()
{
	return;
}
bool Vector3::operator ==(Vector3 Vector3Two)
{
	return (X==Vector3Two.X)&&(Y==Vector3Two.Y)&&(Z==Vector3Two.Z);
}
bool Vector3::operator ==(double Num)
{
	return GetMagnitude()==(float)Num;
}
bool Vector3::operator ==(double* Num)
{
	return (X==(float)Num[0])&&(Y==(float)Num[1])&&(Z==(float)Num[2]);
}
bool Vector3::operator ==(float Num)
{
	return GetMagnitude()==Num;
}
bool Vector3::operator ==(float* Num)
{
	return (X==Num[0])&&(Y==Num[1])&&(Z==Num[2]);
}
bool Vector3::operator ==(int Num)
{
	return GetMagnitude()==(float)Num;
}
bool Vector3::operator ==(int* Num)
{
	return (X==(float)Num[0])&&(Y==(float)Num[1])&&(Z==(float)Num[2]);
}
bool Vector3::operator !=(Vector3 Vector3Two)
{
	return !((X==Vector3Two.X)&&(Y==Vector3Two.Y)&&(Z==Vector3Two.Z));
}
bool Vector3::operator !=(double Num)
{
	return GetMagnitude()!=(float)Num;
}
bool Vector3::operator !=(double* Num)
{
	return !((X==(float)Num[0])&&(Y==(float)Num[1])&&(Z==(float)Num[2]));
}
bool Vector3::operator !=(float Num)
{
	return GetMagnitude()!=Num;
}
bool Vector3::operator !=(float* Num)
{
	return !((X==Num[0])&&(Y==Num[1])&&(Z==Num[2]));
}
bool Vector3::operator !=(int Num)
{
	return GetMagnitude()!=(float)Num;
}
bool Vector3::operator !=(int* Num)
{
	return !((X==(float)Num[0])&&(Y==(float)Num[1])&&(Z==(float)Num[2]));
}
bool Vector3::operator >=(Vector3 Vector3Two)
{
	return (X>=Vector3Two.X)&&(Y>=Vector3Two.Y)&&(Z>=Vector3Two.Z);
}
bool Vector3::operator >=(double Num)
{
	return GetMagnitude()>=(float)Num;
}
bool Vector3::operator >=(double* Num)
{
	return (X>=(float)Num[0])&&(Y>=(float)Num[1])&&(Z>=(float)Num[2]);
}
bool Vector3::operator >=(float Num)
{
	return GetMagnitude()>=Num;
}
bool Vector3::operator >=(float* Num)
{
	return (X>=Num[0])&&(Y>=Num[1])&&(Z>=Num[2]);
}
bool Vector3::operator >=(int Num)
{
	return GetMagnitude()>=(float)Num;
}
bool Vector3::operator >=(int* Num)
{
	return (X>=(float)Num[0])&&(Y>=(float)Num[1])&&(Z>=(float)Num[2]);
}
bool Vector3::operator <=(Vector3 Vector3Two)
{
	return (X<=Vector3Two.X)&&(Y<=Vector3Two.Y)&&(Z<=Vector3Two.Z);
}
bool Vector3::operator <=(double Num)
{
	return GetMagnitude()<=(float)Num;
}
bool Vector3::operator <=(double* Num)
{
	return (X<=(float)Num[0])&&(Y<=(float)Num[1])&&(Z<=(float)Num[2]);
}
bool Vector3::operator <=(float Num)
{
	return GetMagnitude()<=Num;
}
bool Vector3::operator <=(float* Num)
{
	return (X<=Num[0])&&(Y<=Num[1])&&(Z<=Num[2]);
}
bool Vector3::operator <=(int Num)
{
	return GetMagnitude()<=(float)Num;
}
bool Vector3::operator <=(int* Num)
{
	return (X<=(float)Num[0])&&(Y<=(float)Num[1])&&(Z<=(float)Num[2]);
}
bool Vector3::operator <(Vector3 Vector3Two)
{
	return (X<Vector3Two.X)&&(Y<Vector3Two.Y)&&(Z<Vector3Two.Z);
}
bool Vector3::operator <(double Num)
{
	return GetMagnitude()<(float)Num;
}
bool Vector3::operator <(double* Num)
{
	return (X<(float)Num[0])&&(Y<(float)Num[1])&&(Z<(float)Num[2]);
}
bool Vector3::operator <(float Num)
{
	return GetMagnitude()<Num;
}
bool Vector3::operator <(float* Num)
{
	return (X<Num[0])&&(Y<Num[1])&&(Z<Num[2]);
}
bool Vector3::operator <(int Num)
{
	return GetMagnitude()<(float)Num;
}
bool Vector3::operator <(int* Num)
{
	return (X<(float)Num[0])&&(Y<(float)Num[1])&&(Z<(float)Num[2]);
}
bool Vector3::operator >(Vector3 Vector3Two)
{
	return (X>Vector3Two.X)&&(Y>Vector3Two.Y)&&(Z>Vector3Two.Z);
}
bool Vector3::operator >(double Num)
{
	return GetMagnitude()>(float)Num;
}
bool Vector3::operator >(double* Num)
{
	return (X>(float)Num[0])&&(Y>(float)Num[1])&&(Z>(float)Num[2]);
}
bool Vector3::operator >(float Num)
{
	return GetMagnitude()>Num;
}
bool Vector3::operator >(float* Num)
{
	return (X>Num[0])&&(Y>Num[1])&&(Z>Num[2]);
}
bool Vector3::operator >(int Num)
{
	return GetMagnitude()>(float)Num;
}
bool Vector3::operator >(int* Num)
{
	return (X>(float)Num[0])&&(Y>(float)Num[1])&&(Z>(float)Num[2]);
}
float Vector3::GetMagnitude()
{
	return sqrt(pow(this->X,2)+pow(this->Y,2)+pow(this->Z,2));
}
Vector3 Vector3::operator =(Vector3 Vector3Two)
{
	Vector3 temp;

	X=Vector3Two.X;
	Y=Vector3Two.Y;
	Z=Vector3Two.Z;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator =(double Num)
{
	Vector3 temp;

	X=Num;
	Y=Num;
	Z=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator =(double* Num)
{
	Vector3 temp;

	X=Num[0];
	Y=Num[1];
	Z=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator =(float Num)
{
	Vector3 temp;

	X=Num;
	Y=Num;
	Z=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator =(float* Num)
{
	Vector3 temp;

	X=Num[0];
	Y=Num[1];
	Z=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator =(int Num)
{
	Vector3 temp;

	X=Num;
	Y=Num;
	Z=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator =(int* Num)
{
	Vector3 temp;

	X=Num[0];
	Y=Num[1];
	Z=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +(Vector3 Vector3Two)
{
	Vector3 temp;

	temp.X=X+Vector3Two.X;
	temp.Y=Y+Vector3Two.Y;
	temp.Z=Z+Vector3Two.Z;

	return temp;
}
Vector3 Vector3::operator +(double Num)
{
	Vector3 temp;

	temp.X=X+Num;
	temp.Y=Y+Num;
	temp.Z=Z+Num;

	return temp;
}
Vector3 Vector3::operator +(double* Num)
{
	Vector3 temp;

	temp.X=X+Num[0];
	temp.Y=Y+Num[1];
	temp.Z=Z+Num[2];

	return temp;
}
Vector3 Vector3::operator +(float Num)
{
	Vector3 temp;

	temp.X=X+Num;
	temp.Y=Y+Num;
	temp.Z=Z+Num;

	return temp;
}
Vector3 Vector3::operator +(float* Num)
{
	Vector3 temp;

	temp.X=X+Num[0];
	temp.Y=Y+Num[1];
	temp.Z=Z+Num[2];

	return temp;
}
Vector3 Vector3::operator +(int Num)
{
	Vector3 temp;

	temp.X=X+Num;
	temp.Y=Y+Num;
	temp.Z=Z+Num;

	return temp;
}
Vector3 Vector3::operator +(int* Num)
{
	Vector3 temp;

	temp.X=X+Num[0];
	temp.Y=Y+Num[1];
	temp.Z=Z+Num[2];

	return temp;
}
Vector3 Vector3::operator -(Vector3 Vector3Two)
{
	Vector3 temp;

	temp.X=X-Vector3Two.X;
	temp.Y=Y-Vector3Two.Y;
	temp.Z=Z-Vector3Two.Z;

	return temp;
}
Vector3 Vector3::operator -(double Num)
{
	Vector3 temp;

	temp.X=X-Num;
	temp.Y=Y-Num;
	temp.Z=Z-Num;

	return temp;
}
Vector3 Vector3::operator -(double* Num)
{
	Vector3 temp;

	temp.X=X-Num[0];
	temp.Y=Y-Num[1];
	temp.Z=Z-Num[2];

	return temp;
}
Vector3 Vector3::operator -(float Num)
{
	Vector3 temp;

	temp.X=X-Num;
	temp.Y=Y-Num;
	temp.Z=Z-Num;

	return temp;
}
Vector3 Vector3::operator -(float* Num)
{
	Vector3 temp;

	temp.X=X-Num[0];
	temp.Y=Y-Num[1];
	temp.Z=Z-Num[2];

	return temp;
}
Vector3 Vector3::operator -(int Num)
{
	Vector3 temp;

	temp.X=X-Num;
	temp.Y=Y-Num;
	temp.Z=Z-Num;

	return temp;
}
Vector3 Vector3::operator -(int* Num)
{
	Vector3 temp;

	temp.X=X-Num[0];
	temp.Y=Y-Num[1];
	temp.Z=Z-Num[2];

	return temp;
}
Vector3 Vector3::operator *(Vector3 Vector3Two)
{
	Vector3 temp;

	temp.X=X*Vector3Two.X;
	temp.Y=Y*Vector3Two.Y;
	temp.Z=Z*Vector3Two.Z;

	return temp;
}
Vector3 Vector3::operator *(double Num)
{
	Vector3 temp;

	temp.X=X*Num;
	temp.Y=Y*Num;
	temp.Z=Z*Num;

	return temp;
}
Vector3 Vector3::operator *(double* Num)
{
	Vector3 temp;

	temp.X=X*Num[0];
	temp.Y=Y*Num[1];
	temp.Z=Z*Num[2];

	return temp;
}
Vector3 Vector3::operator *(float Num)
{
	Vector3 temp;

	temp.X=X*Num;
	temp.Y=Y*Num;
	temp.Z=Z*Num;

	return temp;
}
Vector3 Vector3::operator *(float* Num)
{
	Vector3 temp;

	temp.X=X*Num[0];
	temp.Y=Y*Num[1];
	temp.Z=Z*Num[2];

	return temp;
}
Vector3 Vector3::operator *(int Num)
{
	Vector3 temp;

	temp.X=X*Num;
	temp.Y=Y*Num;
	temp.Z=Z*Num;

	return temp;
}
Vector3 Vector3::operator *(int* Num)
{
	Vector3 temp;

	temp.X=X*Num[0];
	temp.Y=Y*Num[1];
	temp.Z=Z*Num[2];

	return temp;
}
Vector3 Vector3::operator /(Vector3 Vector3Two)
{
	Vector3 temp;

	temp.X=X/Vector3Two.X;
	temp.Y=Y/Vector3Two.Y;
	temp.Z=Z/Vector3Two.Z;

	return temp;
}
Vector3 Vector3::operator /(double Num)
{
	Vector3 temp;

	temp.X=X/Num;
	temp.Y=Y/Num;
	temp.Z=Z/Num;

	return temp;
}
Vector3 Vector3::operator /(double* Num)
{
	Vector3 temp;

	temp.X=X/Num[0];
	temp.Y=Y/Num[1];
	temp.Z=Z/Num[2];

	return temp;
}
Vector3 Vector3::operator /(float Num)
{
	Vector3 temp;

	temp.X=X/Num;
	temp.Y=Y/Num;
	temp.Z=Z/Num;

	return temp;
}
Vector3 Vector3::operator /(float* Num)
{
	Vector3 temp;

	temp.X=X/Num[0];
	temp.Y=Y/Num[1];
	temp.Z=Z/Num[2];

	return temp;
}
Vector3 Vector3::operator /(int Num)
{
	Vector3 temp;

	temp.X=X/Num;
	temp.Y=Y/Num;
	temp.Z=Z/Num;

	return temp;
}
Vector3 Vector3::operator /(int* Num)
{
	Vector3 temp;

	temp.X=X/Num[0];
	temp.Y=Y/Num[1];
	temp.Z=Z/Num[2];

	return temp;
}
Vector3 Vector3::operator +=(Vector3 Vector3Two)
{
	Vector3 temp;

	X+=Vector3Two.X;
	Y+=Vector3Two.Y;
	Z+=Vector3Two.Z;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +=(double Num)
{
	Vector3 temp;

	X+=Num;
	Y+=Num;
	Z+=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +=(double* Num)
{
	Vector3 temp;

	X+=Num[0];
	Y+=Num[1];
	Z+=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +=(float Num)
{
	Vector3 temp;

	X+=Num;
	Y+=Num;
	Z+=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +=(float* Num)
{
	Vector3 temp;

	X+=Num[0];
	Y+=Num[1];
	Z+=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +=(int Num)
{
	Vector3 temp;

	X+=Num;
	Y+=Num;
	Z+=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator +=(int* Num)
{
	Vector3 temp;

	X+=Num[0];
	Y+=Num[1];
	Z+=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(Vector3 Vector3Two)
{
	Vector3 temp;

	X-=Vector3Two.X;
	Y-=Vector3Two.Y;
	Z-=Vector3Two.Z;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(double Num)
{
	Vector3 temp;

	X-=Num;
	Y-=Num;
	Z-=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(double* Num)
{
	Vector3 temp;

	X-=Num[0];
	Y-=Num[1];
	Z-=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(float Num)
{
	Vector3 temp;

	X-=Num;
	Y-=Num;
	Z-=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(float* Num)
{
	Vector3 temp;

	X-=Num[0];
	Y-=Num[1];
	Z-=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(int Num)
{
	Vector3 temp;

	X-=Num;
	Y-=Num;
	Z-=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator -=(int* Num)
{
	Vector3 temp;

	X-=Num[0];
	Y-=Num[1];
	Z-=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(Vector3 Vector3Two)
{
	Vector3 temp;

	X*=Vector3Two.X;
	Y*=Vector3Two.Y;
	Z*=Vector3Two.Z;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(double Num)
{
	Vector3 temp;

	X*=Num;
	Y*=Num;
	Z*=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(double* Num)
{
	Vector3 temp;

	X*=Num[0];
	Y*=Num[1];
	Z*=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(float Num)
{
	Vector3 temp;

	X*=Num;
	Y*=Num;
	Z*=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(float* Num)
{
	Vector3 temp;

	X*=Num[0];
	Y*=Num[1];
	Z*=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(int Num)
{
	Vector3 temp;

	X*=Num;
	Y*=Num;
	Z*=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator *=(int* Num)
{
	Vector3 temp;

	X*=Num[0];
	Y*=Num[1];
	Z*=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(Vector3 Vector3Two)
{
	Vector3 temp;

	X/=Vector3Two.X;
	Y/=Vector3Two.Y;
	Z/=Vector3Two.Z;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(double Num)
{
	Vector3 temp;

	X/=Num;
	Y/=Num;
	Z/=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(double* Num)
{
	Vector3 temp;

	X/=Num[0];
	Y/=Num[1];
	Z/=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(float Num)
{
	Vector3 temp;

	X/=Num;
	Y/=Num;
	Z/=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(float* Num)
{
	Vector3 temp;

	X/=Num[0];
	Y/=Num[1];
	Z/=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(int Num)
{
	Vector3 temp;

	X/=Num;
	Y/=Num;
	Z/=Num;

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator /=(int* Num)
{
	Vector3 temp;

	X/=Num[0];
	Y/=Num[1];
	Z/=Num[2];

	temp.X=X;
	temp.Y=Y;
	temp.Z=Z;

	return temp;
}
Vector3 Vector3::operator ++()
{
	Vector3 temp;

	temp.X=++X;
	temp.Y=++Y;
	temp.Z=++Z;

	return temp;
}
Vector3 Vector3::operator --()
{
	Vector3 temp;

	temp.X=--X;
	temp.Y=--Y;
	temp.Z=--Z;

	return temp;
}
#pragma endregion

#endif