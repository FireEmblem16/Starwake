///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Object.h ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Represents a solid object in a simulation environment.                      ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _OBJECT_H
#define _OBJECT_H

#include <sstream>
#include <string>
#include <stdarg.h>
#include "Defines.h"
#include "Item.h"
using std::string;
using std::stringstream;

// Contains an object that belongs to a simulation environment.
class Object : public Item
{
public:
	Object(string Type, string Display, string Name = "Object", string Color = "blue");
	Object(const Object& rhs);
	Object& operator =(const Object& rhs);
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

	string GetColor();
protected:
	string color;
};

#endif
