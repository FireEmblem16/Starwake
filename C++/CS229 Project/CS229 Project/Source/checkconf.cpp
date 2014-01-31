///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// checkconf.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Takes a single filename command line argument that contains the path name   ///
/// of the configuration file to read. The configuration file is validated and  ///
/// then a representation of simulation environment with its properties and     ///
/// objects is outputed to stdout sorted in row-column order.                   ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _CHECKCONF_CPP
#define _CHECKCONF_CPP

#include <iostream>
#include "../Headers/Defines.h"
#include "../Headers/XMLParser.h"
#include "../Headers/Simulation.h"
using std::cerr;
using std::cout;
using std::endl;

#include <sstream>
using std::stringstream;

// See start of file
int main(int argc, char** argv)
{
	// Check if we have the appropriate number of arguments
	if(argc != 2)
	{
		cerr << (argc < 2 ? "Too few arguments were provided." : "Too many arguments were provided.") << endl;
		return FAIL;
	}

	Simulation sim(0,0);

	XMLParser parser(&sim,argv[1]);
	uint32_t err = parser.Load();

	// Print errors
	if(err > 0)
	{
		if(err < 0xFFFFFFFF)
			cerr << "Number of errors observed in config file: " << err << endl;
		else
			cerr << "Unable to open config file" << endl;

		return FAIL;
	}
	
	// Output what we loaded
	parser.WriteInfo();

	// The program executed sucessfully
	return SUCCESS;
}

#endif
