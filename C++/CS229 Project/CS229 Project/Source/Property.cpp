///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// Property.cpp ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the property class.                                              ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _PROPERTY_CPP
#define _PROPERTY_CPP

#include "../Headers/Property.h"

Property::Property(string Type, string Display, string Name, uint32_t EnergyCost, uint32_t TurnCost) : Item(Type,Display,Name,false)
{
	energy_cost = EnergyCost;
	turn_cost = TurnCost;
	default_energy_cost = 0;

	Unprobeable* s = new Unprobeable();
	nature->ChangeProbeBehaviour(s);
	delete s;
	
	return;
}

Property::Property(const Property& rhs) : Item(rhs.type,rhs.disp,rhs.name,false)
{
	energy_cost = rhs.energy_cost;
	turn_cost = rhs.turn_cost;
	default_energy_cost = rhs.default_energy_cost;

	return;
}

Property& Property::operator =(const Property& rhs)
{
	if(this == &rhs)
		return *this;

	energy_cost = rhs.energy_cost;
	turn_cost = rhs.turn_cost;
	default_energy_cost = rhs.default_energy_cost;

	Item::operator =(rhs);
	return *this;
}

Item* Property::Clone()
{
	Property* ret = new Property("","");
	*ret = *this;
	return (Item*)ret;
}

string Property::GetEnergyCostString()
{
	// Create some local variables
	string ec_str;
	stringstream out;

	// Get the string version of energy_cost
	if(energy_cost != default_energy_cost)
	{
		out << energy_cost;
		ec_str = out.str();
	}

	return (energy_cost == default_energy_cost ? string("") : string("\nEnergy Cost: " + ec_str));
}

string Property::ToString()
{
	return Item::ToString() + GetEnergyCostString();
}

string Property::ToXML(uint32_t xloc, uint32_t yloc)
{
	stringstream x, y, t, e;
	x << xloc;
	y << yloc;
	e << energy_cost;
	t << turn_cost;

	return "<Property>\ntype = " + type + "\nname = " + name + "\ndisplay = " + disp + "\nxloc = " + x.str() + "\nyloc = " + y.str() + "\nenergy-cost = " + e.str() + "\nturn-cost = " + t.str() + "\n</Property>";
}

// Sets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully altered. char* for strings please.
uint32_t Property::SetParams(string arg, ...)
{
	// We will return this
	uint32_t ret = 0;

	// We will use this to grab parameters
	va_list passed;
	va_start(passed,arg);

	// Available names: color
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
		if(!temp.compare("energy-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			energy_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("turn-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			turn_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Item::SetParams(temp,ptr);
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
uint32_t Property::GetParams(string arg, ...)
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
		if(!temp.compare("energy-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			energy_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("turn-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			turn_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Item::GetParams(temp,ptr);
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

uint32_t Property::GetEnergyCost()
{
	return energy_cost;
}

uint32_t Property::GetTurnCost()
{
	return turn_cost;
}

#endif
