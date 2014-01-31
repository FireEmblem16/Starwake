///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Object.cpp ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the object class.                                                ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _OBJECT_CPP
#define _OBJECT_CPP

#include "../Headers/Object.h"

Object::Object(string Type, string Display, string Name, string Color) : Item(Type,Display,Name,true)
{
	color = Color;

	return;
}

Object::Object(const Object& rhs) : Item(rhs.type,rhs.disp,rhs.name,true)
{
	color = rhs.color;
	return;
}

Object& Object::operator =(const Object& rhs)
{
	if(this == &rhs)
		return *this;

	color = rhs.color;

	Item::operator =(rhs);
	return *this;
}

Item* Object::Clone()
{
	Object* ret = new Object("","");
	*ret = *this;
	return (Item*)ret;
}

string Object::ToString()
{
	return Item::ToString() + (color == "blue" ? "" : string("\nColor: " + color));
}

string Object::ToXML(uint32_t xloc, uint32_t yloc)
{
	stringstream x, y;
	x << xloc;
	y << yloc;

	return "<Object>\ntype = " + type + "\nname = " + name + "\ndisplay = " + disp + "\ncolor = " + color + "\nxloc = " + x.str() + "\nyloc = " + y.str() + "\n</Object>";
}

string Object::GetColor()
{
	return color;
}

// Sets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully altered. char* for strings please.
uint32_t Object::SetParams(string arg, ...)
{
	// We will return this
	uint32_t ret = 0;

	// We will use this to grab parameters
	va_list passed;
	va_start(passed,arg);

	// Available names: color
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
		if(!temp.compare("color"))
		{
			// Get our next parameter
			const char* s = va_arg(passed,const char*);

			// Change our string
			color = string(s);

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Item::SetParams(temp,ptr);
		}
		
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
uint32_t Object::GetParams(string arg, ...)
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
		if(!temp.compare("color"))
		{
			// Get our next parameter
			string* s = va_arg(passed,string*);

			// Change our string
			*s = color;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Item::GetParams(temp,ptr);
		}
		
		// Update our param string
		if(index_2 == string::npos)
			param = "";
		else
			param = param.substr(index_2);
	}

	va_end(passed);
	return ret;
}

#endif
