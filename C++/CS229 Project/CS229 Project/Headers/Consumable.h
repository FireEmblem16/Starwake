///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Consumable.h //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// This indicates that if anything collides with this item it will be consumed.///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _CONSUMABLE_H
#define _CONSUMABLE_H

#include "CollisionBehaviour.h"

class Consumable : public CollisionBehaviour
{
public:
	virtual CollisionBehaviour* CreateNew();

	// Returns a number from -8 to 8 which determines where the Item will be
	// sent after being collided with. A zero indicates that it will not move.
	// A negative number implies that it will travel as far as it can in the
	// indicated direction and 1 to 8 indicates a direction for a single step.
	// Returns 9 if the Item is consumed.
	// 1 2 3      -1 -2 -3
	// 4 0 5      -4 00 -5
	// 6 7 8      -6 -7 -8
	virtual int Collide(int collide_from);
};

#endif
