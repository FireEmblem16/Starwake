///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Property.h ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Defines a field property in a simulation environment.                       ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _PROPERTY_H
#define _PROPERTY_H

#include <string>
#include <stdarg.h>
#include <sstream>
#include "Defines.h"
#include "Item.h"
#include "Unprobeable.h"
using std::string;
using std::stringstream;

// Contains a property that can exist in a simulation environment.
class Property : public Item
{
public:
	Property(string Type, string Display, string Name = "Property", uint32_t EnergyCost = 0, uint32_t TurnCost = 0);
	Property(const Property& rhs);
	Property& operator =(const Property& rhs);
	virtual Item* Clone();

	// Sets parameters given by their names listed in arg in that order.
	// If an invalid parameter is given nothing happens. Returns the
	// number of parameters successfully altered. char* for strings please.
	virtual uint32_t SetParams(string arg, ...);

	// Gets parameters given by their names listed in arg in that order.
	// If an invalid parameter is given nothing happens. Returns the
	// number of parameters successfully obtained. The arguments in ...
	// should be pointers to where the data should be stored.
	virtual uint32_t GetParams(string arg, ...);

	// Returns a string representing data about this Item.
	virtual string ToString();
	virtual string ToXML(uint32_t xloc, uint32_t yloc);

	uint32_t GetEnergyCost();
	uint32_t GetTurnCost();
protected:
	virtual string GetEnergyCostString();
	uint32_t default_energy_cost;

	uint32_t energy_cost;
	uint32_t turn_cost;
};

#endif
