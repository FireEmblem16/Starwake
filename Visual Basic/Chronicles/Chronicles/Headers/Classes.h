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

#ifndef CLASSES_H
#define CLASSES_H
#pragma region Declarations
#ifndef CMATH
#define CMATH
#include <cmath>
#endif
#pragma endregion

class Vector3
{
public:
	Vector3();
	Vector3(double,double,double);
	Vector3(float,float,float);
	Vector3(int,int,int);
	~Vector3();
	bool operator ==(Vector3);
	bool operator ==(double);
	bool operator ==(double*);
	bool operator ==(float);
	bool operator ==(float*);
	bool operator ==(int);
	bool operator ==(int*);
	bool operator !=(Vector3);
	bool operator !=(double);
	bool operator !=(double*);
	bool operator !=(float);
	bool operator !=(float*);
	bool operator !=(int);
	bool operator !=(int*);
	bool operator >=(Vector3);
	bool operator >=(double);
	bool operator >=(double*);
	bool operator >=(float);
	bool operator >=(float*);
	bool operator >=(int);
	bool operator >=(int*);
	bool operator <=(Vector3);
	bool operator <=(double);
	bool operator <=(double*);
	bool operator <=(float);
	bool operator <=(float*);
	bool operator <=(int);
	bool operator <=(int*);
	bool operator <(Vector3);
	bool operator <(double);
	bool operator <(double*);
	bool operator <(float);
	bool operator <(float*);
	bool operator <(int);
	bool operator <(int*);
	bool operator >(Vector3);
	bool operator >(double);
	bool operator >(double*);
	bool operator >(float);
	bool operator >(float*);
	bool operator >(int);
	bool operator >(int*);
	float GetMagnitude();
	float X;
	float Y;
	float Z;
	Vector3 operator =(Vector3);
	Vector3 operator =(double);
	Vector3 operator =(double*);
	Vector3 operator =(float);
	Vector3 operator =(float*);
	Vector3 operator =(int);
	Vector3 operator =(int*);
	Vector3 operator +(Vector3);
	Vector3 operator +(double);
	Vector3 operator +(double*);
	Vector3 operator +(float);
	Vector3 operator +(float*);
	Vector3 operator +(int);
	Vector3 operator +(int*);
	Vector3 operator -(Vector3);
	Vector3 operator -(double);
	Vector3 operator -(double*);
	Vector3 operator -(float);
	Vector3 operator -(float*);
	Vector3 operator -(int);
	Vector3 operator -(int*);
	Vector3 operator *(Vector3);
	Vector3 operator *(double);
	Vector3 operator *(double*);
	Vector3 operator *(float);
	Vector3 operator *(float*);
	Vector3 operator *(int);
	Vector3 operator *(int*);
	Vector3 operator /(Vector3);
	Vector3 operator /(double);
	Vector3 operator /(double*);
	Vector3 operator /(float);
	Vector3 operator /(float*);
	Vector3 operator /(int);
	Vector3 operator /(int*);
	Vector3 operator +=(Vector3);
	Vector3 operator +=(double);
	Vector3 operator +=(double*);
	Vector3 operator +=(float);
	Vector3 operator +=(float*);
	Vector3 operator +=(int);
	Vector3 operator +=(int*);
	Vector3 operator -=(Vector3);
	Vector3 operator -=(double);
	Vector3 operator -=(double*);
	Vector3 operator -=(float);
	Vector3 operator -=(float*);
	Vector3 operator -=(int);
	Vector3 operator -=(int*);
	Vector3 operator *=(Vector3);
	Vector3 operator *=(double);
	Vector3 operator *=(double*);
	Vector3 operator *=(float);
	Vector3 operator *=(float*);
	Vector3 operator *=(int);
	Vector3 operator *=(int*);
	Vector3 operator /=(Vector3);
	Vector3 operator /=(double);
	Vector3 operator /=(double*);
	Vector3 operator /=(float);
	Vector3 operator /=(float*);
	Vector3 operator /=(int);
	Vector3 operator /=(int*);
	Vector3 operator ++();
	Vector3 operator --();
};

#endif