///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Mud.cpp //////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the mud class.                                                  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _MUD_CPP
#define _MUD_CPP

#include "../Headers/Mud.h"

Mud::Mud(string Type, string Display, string Name, uint32_t EnergyCost, uint32_t TurnCost) : Property(Type,Display,Name,EnergyCost,TurnCost)
{
	default_energy_cost = 2;
	return;
}

Mud::Mud(const Mud& rhs) : Property(rhs.type,rhs.disp,rhs.name,rhs.energy_cost,rhs.turn_cost)
{return;}

Mud& Mud::operator =(const Mud& rhs)
{
	if(this == &rhs)
		return *this;

	Property::operator =(rhs);
	return *this;
}

Item* Mud::Clone()
{
	Mud* ret = new Mud("","");
	*ret = *this;
	return (Item*)ret;
}

#endif
