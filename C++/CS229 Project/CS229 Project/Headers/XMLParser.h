///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// XML Parser.h //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Parsers simulation data from an XML style configuration file.               ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _XML_PARSER_H
#define _XML_PARSER_H

// I HATE this warning, it is also not a warning when using g++
#pragma warning(disable:4996)

#include <algorithm>
#include <cctype>
#include <cstdio>
#include <fstream>
#include <ios>
#include <iostream>
#include <locale>
#include <sstream>
#include <string>
#include <vector>
#include "Ball.h"
#include "Block.h"
#include "Defines.h"
#include "EarthRock.h"
#include "EnergyPill.h"
#include "Hole.h"
#include "Item.h"
#include "Jam.h"
#include "Lava.h"
#include "Mud.h"
#include "Robot.h"
#include "RumulanRock.h"
#include "Simulation.h"
#include "Water.h"
using std::cerr;
using std::endl;
using std::ifstream;
using std::string;
using std::stringstream;
using std::vector;
using std::transform;
using std::locale;

class XMLParser
{
public:
	XMLParser(Simulation* dest, const char* file); // sim is where our parsed data will be loaded to
	~XMLParser();

	// Returns an integer equal to the number of illegal objects/properties in the config file
	// Returns 0xFFFFFFFF if we failed the load entierly.
	uint32_t Load();

	// Writes information about this parser and it's parsed simulation to cout.
	void WriteGridOnly();
	void WriteInfo();
private:
	bool ContainsStartTag(string str);
	bool ContainsEndTag(string str);
	bool ContainsParam(string str);

	uint32_t ContainsX(vector<string>* vec, string str); // Searches for param names
	uint32_t IndexOf(vector<string>* vec, string str); // Searches for param names, returns 0xFFFFFFFF if not found
	
	int LoadNext(bool first = false); // Returns -1 if the function errored, 0 if the function had nothing left to read and 1 if successful
	Item* CreateItem(string type); // Creates a new Item specified by the type given by [type]
	
	string GetTagName(string str); // Assumes no ws or comments
	string GetParamName(string str); // Assumes no ws or comments
	string GetParamValue(string str); // Assumes no ws or comments
	uint32_t ParamValue(string param, const char* str); // Returns a pointer to c strings or an unsigned integer depending on what type of param we are using
	double FloatParamValue(string param, const char* str); // Returns a double since only doubles may be passed on ...

	string RemoveComments(string str);
	string RemoveParamWhiteSpace(string str, string whitespace = " \n\t\r");
	string RemoveWhiteSpace(string str, string whitespace = " \n\t\r");

	ifstream input;
	Simulation* sim;
};

#endif
