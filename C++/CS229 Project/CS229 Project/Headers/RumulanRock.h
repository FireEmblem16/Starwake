///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// Rumulan Rock.h /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Represents a rumulan rock in a simulation environment. See Interactions.txt ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _RUMULAN_ROCK_H
#define _RUMULAN_ROCK_H

#include <string>
#include <stdarg.h>
#include "Defines.h"
#include "Rock.h"
#include "Unprobeable.h"
using std::string;

// Contains an rumulan rock that belongs to a simulation environment.
class RumulanRock : public Rock
{
public:
	RumulanRock(string Type, string Display, string Name = "Object", string Color = "blue");
	RumulanRock(const RumulanRock& rhs);
	RumulanRock& operator =(const RumulanRock& rhs);
	virtual Item* Clone();
};

#endif
