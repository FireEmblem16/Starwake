///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// Consumable.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the Consumable class.                                            ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _CONSUMABLE_CPP
#define _CONSUMABLE_CPP

#include "../Headers/Consumable.h"

CollisionBehaviour* Consumable::CreateNew()
{
	return new Consumable();
}

// Returns a number from -8 to 8 which determines where the Item will be
// sent after being collided with. A zero indicates that it will not move.
// A negative number implies that it will travel as far as it can in the
// indicated direction and 1 to 8 indicates a direction for a single step.
// Returns 9 if the Item is consumed.
// 1 2 3      -1 -2 -3
// 4 0 5      -4 00 -5
// 6 7 8      -6 -7 -8
int Consumable::Collide(int collide_from)
{
	return 9;
}

#endif
