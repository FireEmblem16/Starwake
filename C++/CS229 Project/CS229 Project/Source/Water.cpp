///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Water.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the water class.                                                 ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _WATER_CPP
#define _WATER_CPP

#include "../Headers/Water.h"

Water::Water(string Type, string Display, string Name, uint32_t EnergyCost) : Property(Type,Display,Name,EnergyCost)
{
	default_energy_cost = 2;
	return;
}

Water::Water(const Water& rhs) : Property(rhs.type,rhs.disp,rhs.name,rhs.energy_cost,rhs.turn_cost)
{return;}

Water& Water::operator =(const Water& rhs)
{
	if(this == &rhs)
		return *this;

	Property::operator =(rhs);
	return *this;
}

Item* Water::Clone()
{
	Water* ret = new Water("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
