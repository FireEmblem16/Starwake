#ifndef VECTOR_H
#define VECTOR_H

#include <math.h>
#include <GL/gl.h>

#include "Defines.h"

class Vector
{
public:
	Vector();
	Vector(Vector& v2);
	Vector(GLfloat x, GLfloat y, GLfloat z);
	Vector(GLfloat* v);
	Vector operator =(Vector v2);
	~Vector();

	bool operator ==(Vector v2);
	bool operator !=(Vector v2);

	Vector operator +(Vector v2);
	Vector operator -(Vector v2);
	Vector operator *(GLfloat c);
	Vector operator /(GLfloat c);

	Vector operator +=(Vector v2);
	Vector operator -=(Vector v2);
	Vector operator *=(GLfloat c);
	Vector operator /=(GLfloat c);
	
	operator GLfloat*();
	
	GLfloat magnitude();
	GLfloat dot(Vector v2);
	Vector cross(Vector v2);

	Vector unit();
	void normalize();
protected:
	GLfloat* v;
};

#endif