///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Item.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the item class.                                                  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ITEM_CPP
#define _ITEM_CPP

#include "../Headers/Item.h"

Item::Item(string Type, string Display, string Name, bool isobject)
{
	type = Type;
	disp = Display;
	name = Name;
	isobj = isobject;
	isghost = false;
	nature = new Behaviour();

	return;
}

Item::Item(const Item& rhs)
{
	type = rhs.type;
	disp = rhs.disp;
	name = rhs.name;
	isobj = rhs.isobj;
	isghost = rhs.isghost;
	nature = rhs.nature->CreateNew();

	return;
}

Item& Item::operator =(const Item& rhs)
{
	if(this == &rhs)
		return *this;

	type = rhs.type;
	disp = rhs.disp;
	name = rhs.name;
	isobj = rhs.isobj;
	isghost = rhs.isghost;

	delete nature;
	nature = rhs.nature->CreateNew();

	return *this;
}

Item::~Item()
{
	delete nature;
	return;
}

string Item::ToString()
{
	// We will return this
	string ret("Type: " + RequestTypeRename() + "\nName: ");

	// We need to do some manipulations on this
	string temp(name);

	// Check if we have a default name
	if(isobj && name == "Object" || !isobj && name == "Property")
		temp = disp;
	
	// Add the name to the return value
	ret += temp;

	// Return the string version of this Item
	return ret;
}

bool Item::IsObject()
{
	return isobj && !isghost;
}

void Item::MakeGhost(bool yes)
{
	isghost = yes;
	return;
}

Behaviour* Item::GetNature()
{
	return nature;
}

void Item::ChangeNature(Behaviour* nat)
{
	delete nature;
	nature = nat;
	return;
}

string Item::GetDisplay()
{
	return disp;
}

string Item::GetName()
{
	return name;
}

string Item::GetType()
{
	return type;
}

// Sets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully altered. char* for strings please.
uint32_t Item::SetParams(string arg, ...)
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
		if(!temp.compare("type"))
		{
			// Get our next parameter
			const char* s = va_arg(passed,const char*);

			// Change our string
			type = string(s);

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("name"))
		{
			// Get our next parameter
			const char* s = va_arg(passed,const char*);

			// Change our string
			name = string(s);

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("display"))
		{
			// Get our next parameter
			const char* s = va_arg(passed,const char*);

			// Change our string
			disp = string(s);

			// Update the number of sucessful changes
			ret++;
		}
		else
			va_arg(passed,PTR); // We got something wrong but we still had an argument
		
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
uint32_t Item::GetParams(string arg, ...)
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
		if(!temp.compare("type"))
		{
			// Get our next parameter
			string* s = va_arg(passed,string*);

			// Change our string
			*s = type;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("name"))
		{
			// Get our next parameter
			string* s = va_arg(passed,string*);

			// Change our string
			*s = name;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("display"))
		{
			// Get our next parameter
			string* s = va_arg(passed,string*);

			// Change our string
			*s = disp;

			// Update the number of sucessful changes
			ret++;
		}
		else
			va_arg(passed,PTR); // We got something wrong but we still had an argument
		
		// Update our param string
		if(index_2 == string::npos)
			param = "";
		else
			param = param.substr(index_2);
	}

	va_end(passed);
	return ret;
}

// Returns a string representation of type in an output friendly form
string Item::RequestTypeRename()
{
	// Look for what we need
	if(!type.compare("ball"))
		return string("Ball");
	else if(!type.compare("block"))
		return string("Block");
	else if(!type.compare("earth-rock"))
		return string("Earth Rock");
	else if(!type.compare("energy-pill"))
		return string("Energy Pill");
	else if(!type.compare("robot"))
		return string("Robot");
	else if(!type.compare("rumulan-rock"))
		return string("Rumulan Rock");
	else if(!type.compare("hole"))
		return string("Hole");
	else if(!type.compare("jammer"))
		return string("Jammer");
	else if(!type.compare("lava"))
		return string("Lava");
	else if(!type.compare("mud"))
		return string("Mud");
	else if(!type.compare("water"))
		return string("Water");

	// We got a bad type.
	return "";
}

#endif
