///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// Energy Pill.cpp /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the energy pill class.                                           ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ENERGY_PILL_CPP
#define _ENERGY_PILL_CPP

#include "../Headers/EnergyPill.h"

EnergyPill::EnergyPill(string Type, string Display, string Name, string Color, uint32_t energy) : Object(Type,Display,Name,Color)
{
	energy_contents = energy;

	Consumable* newcol = new Consumable();
	nature->ChangeCollisionBehaviour(newcol);
	delete newcol;

	return;
}

EnergyPill::EnergyPill(const EnergyPill& rhs) : Object(rhs.type,rhs.disp,rhs.name,rhs.color)
{
	energy_contents = rhs.energy_contents;
	return;
}

EnergyPill& EnergyPill::operator =(const EnergyPill& rhs)
{
	if(this == &rhs)
		return *this;

	energy_contents = rhs.energy_contents;

	Object::operator =(rhs);
	return *this;
}

Item* EnergyPill::Clone()
{
	EnergyPill* ret = new EnergyPill("","");
	*ret = *this;
	return (Item*)ret;
}

string EnergyPill::ToString()
{
	// Create some local variables
	string ec_str;
	stringstream out;

	// Get the string version of energy_cost
	if(energy_contents != 25)
	{
		out << energy_contents;
		ec_str = out.str();
	}

	return Object::ToString() + (energy_contents == 25 ? string("") : string("\nEnergy Contents: " + ec_str));
}

string EnergyPill::ToXML(uint32_t xloc, uint32_t yloc)
{
	stringstream x, y, e;
	x << xloc;
	y << yloc;
	e << energy_contents;

	return "<Object>\ntype = " + type + "\nname = " + name + "\ndisplay = " + disp + "\ncolor = " + color + "\nxloc = " + x.str() + "\nyloc = " + y.str() + "\nenergy-contents = " + e.str() + "\n</Object>";
}

// Sets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully altered. char* for strings please.
uint32_t EnergyPill::SetParams(string arg, ...)
{
	// We will return this
	uint32_t ret = 0;

	// We will use this to grab parameters
	va_list passed;
	va_start(passed,arg);

	// Available names: energy-contents, recharge, move-cost, turn-cost, probe-cost, paramA, paramB, paramC
	string param = arg;

	// While we have a non-empty string left get more arguments
	while(param.compare(""))
	{
		// Find the first non-whitespace character
		size_t index = param.find_first_not_of(" \n\t\r");

		// Find the next non-whitespace character
		size_t index_2 = param.find_first_of(" \n\t\r",index);

		// Get the next item to alter
		string temp = param.substr(index,index_2 - index);

		// Check if we have something to return
		if(!temp.compare("energy-contents"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			energy_contents = i;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Object::SetParams(temp,ptr);
		}
		
		// Update our param string
		if(index_2 == string::npos)
			param = "";
		else
			param = param.substr(index_2);
	}

	va_end(passed);
	return ret;
}

// Gets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully obtained. The arguments in ...
// should be pointers to where the data should be stored.
uint32_t EnergyPill::GetParams(string arg, ...)
{
	// We will return this
	uint32_t ret = 0;

	// We will use this to grab parameters
	va_list passed;
	va_start(passed,arg);

	// Available names: type, name, display
	string param = arg;

	// While we have a non-empty string left get more arguments
	while(param.compare(""))
	{
		// Find the first non-whitespace character
		size_t index = param.find_first_not_of(" \n\t\r");

		// Find the next non-whitespace character
		size_t index_2 = param.find_first_of(" \n\t\r",index);

		// Get the next item to alter
		string temp = param.substr(index,index_2 - index);

		// Check if we have something to return
		if(!temp.compare("energy-contents"))
		{
			// Get our next parameter
			uint32_t* i = va_arg(passed,uint32_t*);

			// Change our string
			*i = energy_contents;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Object::GetParams(temp,ptr);
		}
		
		// Update our param string
		if(index_2 == string::npos)
			param = "";
		else
			param = param.substr(index_2);
	}

	va_end(passed);
	return ret;
}

uint32_t EnergyPill::GetEnergy()
{
	return energy_contents;
}

#endif
