///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// Item.h /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Defines and Item in the simulation environment.                             ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ITEM_H
#define _ITEM_H

#include <string>
#include <stdarg.h>
#include "Defines.h"
#include "Behaviour.h"
using std::string;

// Contains an item that can be an object or a property.
// Default simulation behaviour is to be probable, stationary and reject collisions.
class Item
{
public:
	Item(string Type, string Display, string Name, bool isobject);
	Item(const Item& rhs);
	Item& operator =(const Item& rhs);
	virtual Item* Clone() = 0;
	~Item();

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
	virtual string ToXML(uint32_t xloc, uint32_t yloc) = 0;

	bool IsObject();
	void MakeGhost(bool yes); // If yes == true this item will not be treated as an object

	Behaviour* GetNature();
	void ChangeNature(Behaviour* nat);

	string GetDisplay();
	string GetName();
	string GetType();
protected:
	string type;
	string disp;
	string name;

	Behaviour* nature;
private:
	string RequestTypeRename();

	bool isobj;
	bool isghost;
};

#endif
