///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Ball.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the ball class.                                                  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _BALL_CPP
#define _BALL_CPP

#include "../Headers/Ball.h"

Ball::Ball(string Type, string Display, string Name, string Color) : Geometry(Type,Display,Name,Color)
{
	RollingPlace* newcol = new RollingPlace();
	nature->ChangeCollisionBehaviour(newcol);
	delete newcol;

	return;
}

Ball::Ball(const Ball& rhs) : Geometry(rhs.type,rhs.disp,rhs.name,rhs.color)
{return;}

Ball& Ball::operator =(const Ball& rhs)
{
	if(this == &rhs)
		return *this;

	Geometry::operator =(rhs);
	return *this;
}

Item* Ball::Clone()
{
	Ball* ret = new Ball("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
