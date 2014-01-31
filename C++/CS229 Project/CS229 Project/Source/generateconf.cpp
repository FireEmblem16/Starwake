///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// generateconf.cpp /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Generates a configuration file from arguments given on the command line.    ///
/// Valid arguments are as follows (noting that all indexes are one based):     ///
///                                                                             ///
/// If not using a special argument, the first two parameters should be the     ///
/// width then height of simulation we want to create.                          ///
///                                                                             ///
/// -[Type]         Tells us we want to create an object of this type. After    ///
///                 this tag is found all following tags until a new type tag   ///
///                 are to be parameters used to create and define this object. ///
///                 The only required secondary tag is -l [x,y] which will      ///
///                 tell us where to place the object. Alternativly the flag    ///
///                 -c [p] will create p copies of the object and place them    ///
///                 onto the grid at random if possible. These tags are         ///
///                 mutually exclusive to one another.                          ///
/// -l [x,y]        The location to place an object.                            ///
/// -c [p]          Creates p objects over the simulation placed at random.     ///
///                 Note that if we are creating Objects we may run out of      ///
///                 grid space and we will fail to create more but if we are    ///
///                 generating Properties we will always have room though a     ///
///                 location may have multiple instances of a property.         ///
/// -[param] [C]    Initializes the parameter param of the object currently     ///
///                 being defined to C. If it does not have the parameter param ///
///                 a warning will be issued but the object will still be made. ///
///                 If a parameter is specified more than once the last one     ///
///                 given will be the resulting value.                          ///
/// -threeblock     Generates a simulation with three red and blue blocks with  ///
///                 even spacing five from the left and right walls respectivly ///
///                 with a red and blue robot on the opposite sides. The        ///
///                 simulation size will be 27x30.                              ///
/// -fiveitem       As threeblock but creates a simulation of size 35x30 and    ///
///                 places block, ball, block, ball, block.                     ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _GENERATECONF_CPP
#define _GENERATECONF_CPP

#pragma warning(disable:4018)
#define NUMBER_OF_TYPES 11

#include <ctime>
#include <iostream>
#include <sstream>
#include <string>
#include "../Headers/Ball.h"
#include "../Headers/Block.h"
#include "../Headers/Defines.h"
#include "../Headers/EarthRock.h"
#include "../Headers/EnergyPill.h"
#include "../Headers/Entry.h"
#include "../Headers/Hole.h"
#include "../Headers/Item.h"
#include "../Headers/Jam.h"
#include "../Headers/Lava.h"
#include "../Headers/Mud.h"
#include "../Headers/Robot.h"
#include "../Headers/RumulanRock.h"
#include "../Headers/Simulation.h"
#include "../Headers/Water.h"
using std::cerr;
using std::cout;
using std::endl;
using std::string;
using std::stringstream;

template <class T> T abs(T X);
long rand(long upperbound, long min);
size_t TypeIndex(string type);
uint32_t CountType(string type, bool skip_add = false);

bool UpdateDisplay(Item* item, stringstream* cstream, bool force = false);

bool IsFloatParam(string param);
bool IsIntParam(string param);
bool IsStringParam(string param);
bool IsValidFlag(string flag);

bool IsNumber(string str);

void PrintHelp();
Item* CreateItem(string type);
bool IsValidTypeFlag(string type);
void OutputSimulationXML(Simulation* sim);
int CreateThreeBlock(char* path);
int CreateFiveItem(char* path);

int main(int argc, char** argv)
{
	// We must have at least the height and width of the simulation to function, unless given special arguments
	if(argc < 3)
	{
		if(argc == 2 && string(argv[1]) == "-threeblock")
			return CreateThreeBlock(argv[0]);
		else if(argc == 2 && string(argv[1]) == "-fiveitem")
			return CreateFiveItem(argv[0]);

		cerr << "At least the height and width of the simulation must be given.\n" << endl;
		PrintHelp();

		return FAIL;
	}

	stringstream in;
	int width = -1, height = -1;
	in << argv[1] << " " << argv[2];
	in >> width >> height;

	// Check that we were given integers
	if(width == -1 || height == -1)
	{
		cerr << (width == -1 ? (height == -1 ? string("Width and height were") : string("Width was")) : string("Height was"))  + " not given.\n" << endl;
		PrintHelp();

		return FAIL;
	}

	// We are actually going to build the simulation so it will do all the error checking and organization for us
	Simulation sim(height,width);

	// This will contain where we currently are during our parsing
	int index = 3;
	int endindex = 3;

	// We don't want to reconstruct this every loop
	stringstream cstream;

	// Create a loop of checks so that we can avoid extra checks
	while(index < argc)
	{
		// Read until we find an object to define
		while(index < argc && !IsValidTypeFlag(argv[index]))
			cerr << "An argument was found outside of an object definition: " << argv[index++] << endl;
		
		// Loop while we have an object to define
		while(index < argc && IsValidTypeFlag(argv[index]))
		{
			// Figure out where the end index is
			while(++endindex < argc && !IsValidTypeFlag(argv[endindex]));

			// We will place our item here in the simulation
			// If we don't get a location or a c value we will fail automatically and get an error to report
			int x = -1, y = -1, c = -1;

			// Create the type of item we want to define
			Item* item = CreateItem(string(argv[index++]).substr(1));

			if(item == NULL)
				continue;

			// Go through each item parameter
			while(index < endindex)
			{
				string param = string(argv[index++]).substr(1);

				// Check if we ran out of parameters
				if(index >= endindex)
				{
					cerr << "Ran out of parameters in the attempt to define a " << item->GetType() << ". Currently named " << item->GetName() << ".\n" << "Parameter name: " << param << endl;

					PrintHelp();
					return FAIL;
					continue;
				}

				// We don't want to skip a flag
				if(IsValidFlag(string(argv[index]).substr(1)) && *argv[index] == '-')
				{
					cerr << "Parameter " << param << " was missing it's value. Next found value is " << argv[index] << endl;

					PrintHelp();
					return FAIL;
					continue;
				}

				if(IsStringParam(param))
				{
					if(item->SetParams(param,argv[index++]) != 1)
						cerr << "Parameter " << param << " is not defined for a " << item->GetType() << ". Current item name is " << item->GetName() << "." << endl;
				}
				else if(IsFloatParam(param))
				{
					if(!IsNumber(argv[index]))
					{
						cerr << "Parameter " << param << " requires a float. Given value is " << argv[index] << "." << endl;

						PrintHelp();
						return FAIL;
						continue;
					}

					double dummyf = 0.0f;
					cstream << argv[index++];
					cstream >> dummyf;
					cstream.clear();

					if(item->SetParams(param,dummyf) != 1)
						cerr << "Parameter " << param << " is not defined for a " << item->GetType() << ". Current item name is " << item->GetName() << "." << endl;
				}
				else if(IsIntParam(param))
				{
					if(!IsNumber(argv[index]))
					{
						cerr << "Parameter " << param << " requires a float. Given value is " << argv[index] << "." << endl;
						
						PrintHelp();
						return FAIL;
						continue;
					}

					uint32_t dummyi = 0;
					cstream << argv[index++];
					cstream >> dummyi;
					cstream.clear();

					if(item->SetParams(param,dummyi) != 1)
						cerr << "Parameter " << param << " is not defined for a " << item->GetType() << ". Current item name is " << item->GetName() << "." << endl;
				}
				else if(param == "l")
				{
					if(c != -1 || x != -1)
					{
						cerr << "Attempted to redefine how a " << item->GetType() << " will be placed. Currently named " << item->GetName() << "." << endl;
						continue;
					}

					string strval = string(argv[index++]);
					size_t comma_index = strval.find_first_of(',');

					if(comma_index == string::npos)
					{
						cerr << "-l requires a comma deleniated pair of integers. Found value was " << argv[index] << "." << endl;
						cerr << "Attempted while defining a " << item->GetType() << ".Currently named " << item->GetName() << "." << endl;

						PrintHelp();
						return FAIL;
						continue;
					}

					string lhs = strval.substr(0,comma_index);
					string rhs = strval.substr(comma_index + 1);

					if(!IsNumber(lhs) || !IsNumber(rhs))
					{
						cerr << "-l requires a comma deleniated pair of integers. Found value was " << argv[index] << "." << endl;
						cerr << "Attempted while defining a " << item->GetType() << ".Currently named " << item->GetName() << "." << endl;

						PrintHelp();
						return FAIL;
						continue;
					}

					cstream << lhs << " " << rhs;
					cstream >> x >> y;
					cstream.clear();
				}
				else if(param == "c")
				{
					if(c != -1 || x != -1)
					{
						cerr << "Attempted to redefine how a " << item->GetType() << " will be placed. Currently named " << item->GetName() << "." << endl;
						continue;
					}

					if(!IsNumber(argv[index]))
					{
						cerr << "-c requires an integer as a second parameter, given value was: " << argv[index - 1] << endl;
						cerr << "Attempted while defining a " << item->GetType() << ".Currently named " << item->GetName() << "." << endl;
						continue;
					}

					cstream << argv[index++];
					cstream >> c;
					cstream.clear();
				}
				else
				{
					// We got a bad flag so skip it and error
					cerr << "An invalid flag was given in the attempt to define a " << item->GetType() << ". Currently named " << item->GetName() << ".\n" << "Parameter name: " << param << " Parameter value: " << argv[index++] << endl;

					// Just in case we were to accidentally skip a flag because we missed a parameter
					if(IsValidFlag(string(argv[index - 1]).substr(1)))
						index--;
				}
			}

			// Add the type to our known count
			CountType(item->GetType());

			// If display wasn't defined properly we have to do so ourselves
			bool force_if_c = UpdateDisplay(item,&cstream);

			if(x != -1)
			{
				if(!sim.AddItemAt(item,x-1,y-1))
				{
					cerr << "Unable to position item " << item->GetName() << " of type " << item->GetType() << " at position " << x << "," << y << "." << endl;
					cerr << "Reason: ";

					if(x < 1 || y < 1 || x > sim.GetWidth() || y > sim.GetHeight())
						cerr << "Out of bounds." << endl;
					else if(item->IsObject())
						cerr << "An Object already exists at the location." << endl;
					else
						cerr << "Unknown." << endl;
				}
			}
			else if(c != -1)
			{
				if(c < 1)
					cerr << "Can not place a non-positive number of random objects." << endl;
				
				while(c-- > 0)
				{
					x = rand(sim.GetWidth(),1);
					y = rand(sim.GetHeight(),1);

					if(!sim.AddItemAt(item,x-1,y-1))
					{
						c++;

						if(item->IsObject())
						{
							bool foundempty = false;

							for(uint32_t i = 0;i < sim.GetWidth() && !foundempty;i++)
								for(uint32_t j = 0;j < sim.GetHeight() && !foundempty;j++)
									if(sim.GetEntriesAt(i,j) == NULL || !sim.GetEntriesAt(i,j)->ContainsObject())
										foundempty = true;

							if(!foundempty)
								c = 0;
						}
					}
					else if(c > 0)
					{
						// Add the type to our known count
						CountType(item->GetType());

						// We want a new copy of item
						item = item->Clone();

						// If display wasn't defined properly we have to do so ourselves
						UpdateDisplay(item,&cstream,force_if_c);
					}
				}
			}
			else
			{
				cerr << "No definition on how to place the object currently being defined was found." << endl;
				cerr << "Attempted while defining a " << item->GetType() << " named " << item->GetName() << "." << endl;
			}

			// We do this in case we had something go wrong
			index = endindex;
		}
	}

	OutputSimulationXML(&sim);
	return SUCCESS;
}

template <class T> T abs(T X)
{
	if(X < 0)
		return -X;

	return X;
}

long rand(long upperbound, long min)
{
	static time_t seed = time(NULL);
	seed = (8253729 * seed + 2396403);
	return (long)abs(seed % upperbound + min);
}

size_t TypeIndex(string type)
{
	// Look for the type we were given
	if(!type.compare("ball"))
		return 0;
	else if(!type.compare("block"))
		return 1;
	else if(!type.compare("earth-rock"))
		return 2;
	else if(!type.compare("energy-pill"))
		return 3;
	else if(!type.compare("robot"))
		return 4;
	else if(!type.compare("rumulan-rock"))
		return 5;
	else if(!type.compare("hole"))
		return 6;
	else if(!type.compare("jammer"))
		return 7;
	else if(!type.compare("lava"))
		return 8;
	else if(!type.compare("mud"))
		return 9;
	else if(!type.compare("water"))
		return 10;

	// We were given a type we can't use
	return -1;
}

uint32_t CountType(string type, bool skip_add)
{
	static uint32_t* counts = new uint32_t[NUMBER_OF_TYPES];
	static bool not_init = true;

	if(not_init)
	{
		for(uint32_t i = 0;i < NUMBER_OF_TYPES;i++)
			counts[i] = 0;

		not_init = false;
	}

	size_t index = TypeIndex(type);

	if(index == -1)
		return 0;

	if(skip_add)
		return counts[index] % 9 == 0 ? 9 : counts[index] % 9;

	return ++counts[index] % 9 == 0 ? 9 : counts[index] % 9;
}

bool UpdateDisplay(Item* item, stringstream* cstream, bool force)
{
	// If display wasn't defined properly we have to do so ourselves
	if(force || item->GetDisplay().length() != 2)
	{
		string display;
		
		*cstream << (char)(item->GetType()[0] + 'A' - 'a') << CountType(item->GetType(),true);
		*cstream >> display;
		cstream->clear();

		item->SetParams("display",display.c_str());
		return true;
	}

	return false;
}

bool IsFloatParam(string param)
{
	if(param == "paramA" || param == "paramB" || param == "paramC")
		return true;

	return false;
}

bool IsIntParam(string param)
{
	if(param == "energy-contents" || param == "recharge" || param == "move-cost" || param == "turn-cost" || param == "probe-cost" || param == "energy-cost" || param == "turn-cost")
		return true;

	return false;
}

bool IsStringParam(string param)
{
	if(param == "name" || param == "type" || param == "color" || param == "display")
		return true;

	return false;
}

bool IsValidFlag(string flag)
{
	return IsStringParam(flag) || IsFloatParam(flag) || flag == "l" || flag == "c" || IsIntParam(flag);
}

bool IsNumber(string str)
{
	if(str == "" || str == "-" || str == "." || str == "-.")
		return false;

	size_t index = 0;
	bool decimal = false;

	if(str[index] == '-')
		index++;

	while(index < str.length() && (str[index] >= '0' && str[index] <= '9' || str[index] == '.' && !decimal))
		if(str[index++] == '.')
			decimal = true;

	if(index == str.length())
		return true;

	return false;
}

void PrintHelp()
{
	static char* help = "Valid arguments are as follows (noting that all indexes are one based):\n\n"
				 "If not using a special argument, the first two parameters should be the\n"
				 "width then height of simulation we want to create.\n\n"
				 "-[Type]         Tells us we want to create an object of this type. After\n"
				 "                this tag is found all following tags until a new type tag\n"
				 "                are to be parameters used to create and define this object.\n"
				 "                The only required secondary tag is -l [x,y] which will\n"
				 "                tell us where to place the object. Alternativly the flag\n"
				 "                -c [p] will create p copies of the object and place them\n"
				 "                onto the grid at random if possible. These tags are\n"
				 "                mutually exclusive to one another.\n"
				 "-l [x,y]        The location to place an object.\n"
				 "-c [p]          Creates p objects over the simulation placed at random.\n"
				 "                Note that if we are creating Objects we may run out of\n"
				 "                grid space and we will fail to create more but if we are\n"
				 "                generating Properties we will always have room though a\n"
				 "                location may have multiple instances of a property.\n"
				 "-[param] [C]    Initializes the parameter param of the object currently\n"
				 "                being defined to C. If it does not have the parameter param\n"
				 "                a warning will be issued but the object will still be made.\n"
				 "                If a parameter is specified more than once the last one\n"
				 "                given will be the resulting value.\n"
				 "-threeblock     Generates a simulation with three red and blue blocks with\n"
				 "                even spacing five from the left and right walls respectivly\n"
				 "                with a red and blue robot on the opposite sides. The\n"
				 "                simulation size will be 27x30.\n"
				 "-fiveitem       As threeblock but creates a simulation of size 35x30 and\n"
				 "                places block, ball, block, ball, block.\n";

	cout << help << endl;
	return;
}

// Creates a new Item specified by the type given by [type].
// Returns NULL if we were given an invalid type.
Item* CreateItem(string type)
{
	// This is what we will return
	Item* ret = NULL;

	// Look for what we need
	if(!type.compare("ball"))
		ret = new Ball(type,"");
	else if(!type.compare("block"))
		ret = new Block(type,"");
	else if(!type.compare("earth-rock"))
		ret = new EarthRock(type,"");
	else if(!type.compare("energy-pill"))
		ret = new EnergyPill(type,"");
	else if(!type.compare("robot"))
		ret = new Robot(type,"");
	else if(!type.compare("rumulan-rock"))
		ret = new RumulanRock(type,"");
	else if(!type.compare("hole"))
		ret = new Hole(type,"");
	else if(!type.compare("jammer"))
		ret = new Jam(type,"");
	else if(!type.compare("lava"))
		ret = new Lava(type,"");
	else if(!type.compare("mud"))
		ret = new Mud(type,"");
	else if(!type.compare("water"))
		ret = new Water(type,"");

	return ret;
}

bool IsValidTypeFlag(string type)
{
	// Look for the type we were given
	if(!type.compare("-ball"))
		return true;
	else if(!type.compare("-block"))
		return true;
	else if(!type.compare("-earth-rock"))
		return true;
	else if(!type.compare("-energy-pill"))
		return true;
	else if(!type.compare("-robot"))
		return true;
	else if(!type.compare("-rumulan-rock"))
		return true;
	else if(!type.compare("-hole"))
		return true;
	else if(!type.compare("-jammer"))
		return true;
	else if(!type.compare("-lava"))
		return true;
	else if(!type.compare("-mud"))
		return true;
	else if(!type.compare("-water"))
		return true;

	// We were given a type we can't use
	return false;
}

void OutputSimulationXML(Simulation* sim)
{
	// This must come first in the configuration file
	cout << sim->ToXML() << endl;

	for(uint32_t i = 0;i < sim->Entries();i++)
	{
		Entry* entry = sim->GetEntry(i);

		for(uint32_t j = 0;j < entry->Items();j++) // Add 1 since config file is one indexed and program is zero indexed
			cout << entry->GetItem(j)->ToXML(entry->GetX() + 1,entry->GetY() + 1) << endl;
	}

	return;
}

int CreateThreeBlock(char* path)
{
	// Create an artificial command line string and call main
	static char* width = "30";
	static char* height = "27";
	static char* block = "-block";
	static char* robot = "-robot";
	static char* l = "-l";
	static char* color = "-color";
	static char* blue = "Blue";
	static char* red = "Red";
	
	static char* box1rloc = "5,7";
	static char* box2rloc = "5,14";
	static char* box3rloc = "5,21";

	static char* box1bloc = "26,7";
	static char* box2bloc = "26,14";
	static char* box3bloc = "26,21";

	static char* brobot = "1,27";
	static char* rrobot = "30,27";

	static char* args[] = {path,width,height,block,color,red,l,box1rloc,block,color,red,l,box2rloc,block,color,red,l,box3rloc,
				   block,color,blue,l,box1bloc,block,color,blue,l,box2bloc,block,color,blue,l,box3bloc,
				   robot,color,blue,l,brobot,robot,color,red,l,rrobot};
	return main(43,args);
}

int CreateFiveItem(char* path)
{
	// Create an artificial command line string and call main
	static char* width = "30";
	static char* height = "35";
	static char* block = "-block";
	static char* ball = "-ball";
	static char* robot = "-robot";
	static char* l = "-l";
	static char* color = "-color";
	static char* blue = "Blue";
	static char* red = "Red";
	
	static char* geo1rloc = "5,6";
	static char* geo2rloc = "5,12";
	static char* geo3rloc = "5,18";
	static char* geo4rloc = "5,24";
	static char* geo5rloc = "5,30";

	static char* geo1bloc = "26,6";
	static char* geo2bloc = "26,12";
	static char* geo3bloc = "26,18";
	static char* geo4bloc = "26,24";
	static char* geo5bloc = "26,30";

	static char* brobot = "1,35";
	static char* rrobot = "30,35";

	static char* args[] = {path,width,height,block,color,red,l,geo1rloc,ball,color,red,l,geo2rloc,block,color,red,l,geo3rloc,ball,color,red,l,geo4rloc,block,color,red,l,geo5rloc,
				   block,color,blue,l,geo1bloc,ball,color,blue,l,geo2bloc,block,color,blue,l,geo3bloc,ball,color,blue,l,geo4bloc,block,color,blue,l,geo5bloc,
				   robot,color,blue,l,brobot,robot,color,red,l,rrobot};
	return main(63,args);
}

#endif
