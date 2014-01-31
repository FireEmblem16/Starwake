#ifndef VECTOR_CPP
#define VECTOR_CPP

#include "Vector.h"

Vector::Vector()
{
	v = new GLfloat[3];

	v[0] = 0.0f;
	v[1] = 0.0f;
	v[2] = 0.0f;

	return;
}

Vector::Vector(Vector& v2)
{
	v = new GLfloat[3];

	v[0] = v2.v[0];
	v[1] = v2.v[1];
	v[2] = v2.v[2];

	return;
}

Vector::Vector(GLfloat x, GLfloat y, GLfloat z)
{
	v = new GLfloat[3];

	v[0] = x;
	v[1] = y;
	v[2] = z;

	return;
}

Vector::Vector(GLfloat* v)
{
	this->v = new GLfloat[3];

	this->v[0] = v[0];
	this->v[1] = v[1];
	this->v[2] = v[2];

	return;
}

Vector Vector::operator =(Vector v2)
{
	v[0] = v2[0];
	v[1] = v2[1];
	v[2] = v2[2];

	return *this;
}

Vector::~Vector()
{
	delete[] v;
}

bool Vector::operator ==(Vector v2)
{
	for(uint32_t i = 0;i < 3;i++)
		if(v[i] != v2[i])
			return false;

	return true;
}

bool Vector::operator !=(Vector v2)
{
	for(uint32_t i = 0;i < 3;i++)
		if(v[i] == v2[i])
			return false;

	return true;
}

Vector Vector::operator +(Vector v2)
{
	return *(new Vector(v[0] + v2[0],v[1] + v2[1],v[2] + v2[2]));
}

Vector Vector::operator -(Vector v2)
{
	return *(new Vector(v[0] - v2[0],v[1] - v2[1],v[2] - v2[2]));
}

Vector Vector::operator *(GLfloat c)
{
	return *(new Vector(v[0] * c,v[1] * c,v[2] * c));
}

Vector Vector::operator /(GLfloat c)
{
	return *(new Vector(v[0] / c,v[1] / c,v[2] / c));
}

Vector Vector::operator +=(Vector v2)
{
	v[0] += v2[0];
	v[1] += v2[1];
	v[2] += v2[2];

	return *this;
}

Vector Vector::operator -=(Vector v2)
{
	v[0] -= v2[0];
	v[1] -= v2[1];
	v[2] -= v2[2];

	return *this;
}

Vector Vector::operator *=(GLfloat c)
{
	v[0] *= c;
	v[1] *= c;
	v[2] *= c;

	return *this;
}

Vector Vector::operator /=(GLfloat c)
{
	v[0] /= c;
	v[1] /= c;
	v[2] /= c;

	return *this;
}

Vector::operator GLfloat*()
{
	return v;
}

GLfloat Vector::magnitude()
{
	return sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
}

GLfloat Vector::dot(Vector v2)
{
	return v[0]*v2[0] + v[1]*v2[1] + v[2]*v2[2];
}

Vector Vector::cross(Vector v2)
{
	return Vector(v[1]*v2[2] - v[2]*v2[1],v[2]*v2[0] - v[0]*v2[2],v[0]*v2[1] - v[1]*v2[0]);
}

Vector Vector::unit()
{
	return Vector(*this / magnitude());
}

void Vector::normalize()
{
	*this = unit();

	return;
}

#endif