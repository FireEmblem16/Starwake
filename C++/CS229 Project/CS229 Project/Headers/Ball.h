///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// Ball.h /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Represents a ball in a simulation environment. See Interactions.txt         ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _BALL_H
#define _BALL_H

#include <string>
#include <stdarg.h>
#include "Defines.h"
#include "Geometry.h"
#include "RollingPlace.h"
using std::string;

// Contains an ball that belongs to a simulation environment.
class Ball : public Geometry
{
public:
	Ball(string Type, string Display, string Name = "Object", string Color = "blue");
	Ball(const Ball& rhs);
	Ball& operator =(const Ball& rhs);
	virtual Item* Clone();
};

#endif
