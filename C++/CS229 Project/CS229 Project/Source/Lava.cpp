///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Lava.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the lava class.                                                  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _LAVA_CPP
#define _LAVA_CPP

#include "../Headers/Lava.h"

Lava::Lava(string Type, string Display, string Name, uint32_t EnergyCost) : Property(Type,Display,Name,EnergyCost)
{
	default_energy_cost = 5;
	return;
}

Lava::Lava(const Lava& rhs) : Property(rhs.type,rhs.disp,rhs.name,rhs.energy_cost,rhs.turn_cost)
{return;}

Lava& Lava::operator =(const Lava& rhs)
{
	if(this == &rhs)
		return *this;

	Property::operator =(rhs);
	return *this;
}

Item* Lava::Clone()
{
	Lava* ret = new Lava("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
