///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// Earth Rock.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the earth rock class.                                            ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _EARTH_ROCK_CPP
#define _EARTH_ROCK_CPP

#include "../Headers/EarthRock.h"

EarthRock::EarthRock(string Type, string Display, string Name, string Color) : Rock(Type,Display,Name,Color)
{return;}

EarthRock::EarthRock(const EarthRock& rhs) : Rock(rhs.type,rhs.disp,rhs.name,rhs.color)
{return;}

EarthRock& EarthRock::operator =(const EarthRock& rhs)
{
	if(this == &rhs)
		return *this;

	Rock::operator =(rhs);
	return *this;
}

Item* EarthRock::Clone()
{
	EarthRock* ret = new EarthRock("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
