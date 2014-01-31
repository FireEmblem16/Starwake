///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Target.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the Target class.                                                ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _TARGET_CPP
#define _TARGET_CPP

#include "../Headers/Target.h"

Target::Target(int x_init, int y_init, int e_init)
{
	x = x_init;
	y = y_init;
	e = e_init;

	return;
}

Target::Target(const Target& rhs)
{
	x = rhs.x;
	y = rhs.y;
	e = rhs.e;

	return;
}

Target& Target::operator =(const Target& rhs)
{
	if(this == &rhs)
		return *this;

	x = rhs.x;
	y = rhs.y;
	e = rhs.e;

	return *this;
}

float Target::Distance(const Target& to) const
{
	int xt = x - to.x;
	int yt = y - to.y;

	return sqrt((float)(xt * xt + yt * yt));
}

float Target::Magnitude() const
{
	return sqrt((float)(x * x + y * y));
}

bool Target::operator ==(const Target& rhs) const
{
	return (x == rhs.x && y == rhs.y);
}

bool Target::operator !=(const Target& rhs) const
{
	return (x != rhs.x || y != rhs.y);
}

bool Target::CloserThan(const Target& t2, const Target& point) const
{
	return (Distance(point) < t2.Distance(point));
}

bool Target::AsCloseAs(const Target& t2, const Target& point) const
{
	return (Distance(point) <= t2.Distance(point));
}

bool Target::FurtherThan(const Target& t2, const Target& point) const
{
	return (Distance(point) > t2.Distance(point));
}

bool Target::AsFarAs(const Target& t2, const Target& point) const
{
	return (Distance(point) >= t2.Distance(point));
}

Target Target::operator +(const Target& rhs) const
{
	return Target(x + rhs.x,y + rhs.y,e + rhs.e);
}

Target Target::operator -(const Target& rhs) const
{
	return Target(x - rhs.x,y - rhs.y,e - rhs.e);
}

float Target::sqrt(float X)
{
	// This formula doesn't like zero
	if(X == 0.0f)
		return 0.0f;

	// Since I like complex numbers we are going to do this
	if(X < 0.0f)
		return sqrt(-X);

	// This is our first term
	float a_n = 1.0f;
	
	// Now we need to do some intense math
	for(uint32_t i = 0;i < 6;i++)
		a_n = 0.5f * (a_n + X / a_n);

	// We have a sqrt approximation accurate to at least 13 places
	return a_n;
}

#endif
