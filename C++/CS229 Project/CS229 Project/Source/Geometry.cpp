///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// Geometry.cpp ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the geometry class.                                              ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _GEOMETRY_CPP
#define _GEOMETRY_CPP

#include "../Headers/Geometry.h"

Geometry::Geometry(string Type, string Display, string Name, string Color) : Object(Type,Display,Name,Color)
{return;}

Geometry::Geometry(const Geometry& rhs) : Object(rhs.type,rhs.disp,rhs.name,rhs.color)
{return;}

Geometry& Geometry::operator =(const Geometry& rhs)
{
	if(this == &rhs)
		return *this;

	Object::operator =(rhs);
	return *this;
}

Item* Geometry::Clone()
{
	Geometry* ret = new Geometry("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
