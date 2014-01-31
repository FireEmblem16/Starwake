///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// Rumulan Rock.cpp /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the rumulan rock class.                                          ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _RUMULAN_ROCK_CPP
#define _RUMULAN_ROCK_CPP

#include "../Headers/RumulanRock.h"

RumulanRock::RumulanRock(string Type, string Display, string Name, string Color) : Rock(Type,Display,Name,Color)
{
	Unprobeable* newprobe = new Unprobeable();
	nature->ChangeProbeBehaviour(newprobe);
	delete newprobe;
	
	return;
}

RumulanRock::RumulanRock(const RumulanRock& rhs) : Rock(rhs.type,rhs.disp,rhs.name,rhs.color)
{return;}

RumulanRock& RumulanRock::operator =(const RumulanRock& rhs)
{
	if(this == &rhs)
		return *this;

	Rock::operator =(rhs);
	return *this;
}

Item* RumulanRock::Clone()
{
	RumulanRock* ret = new RumulanRock("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
