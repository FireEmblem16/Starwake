///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Energy Pill.h /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Represents an energy pill in a simulation environment. See Interactions.txt ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ENERGY_PILL_H
#define _ENERGY_PILL_H

#include <sstream>
#include <string>
#include <stdarg.h>
#include "Consumable.h"
#include "Defines.h"
#include "Object.h"
using std::string;
using std::stringstream;

// Contains an energy pill that belongs to a simulation environment.
class EnergyPill : public Object
{
public:
	EnergyPill(string Type, string Display, string Name = "Object", string Color = "blue", uint32_t energy = 25);
	EnergyPill(const EnergyPill& rhs);
	EnergyPill& operator =(const EnergyPill& rhs);
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

	uint32_t GetEnergy();
private:
	uint32_t energy_contents;
};

#endif
