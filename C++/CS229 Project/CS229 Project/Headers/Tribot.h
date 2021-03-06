///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Tribot.h //////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// This behavior will cause an object with it to gather things of it's color.  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _TRIBOT_H
#define _TRIBOT_H

#include "Behaviour.h"
#include "Gatherer.h"

class Tribot : public Behaviour
{
public:
	Tribot(string c);

	virtual Behaviour* CreateNew();
};

#endif
