///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// XML Parser.cpp /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the XML parser class.                                            ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _XML_PARSER_CPP
#define _XML_PARSER_CPP

#include "../Headers/XMLParser.h"

XMLParser::XMLParser(Simulation* dest, const char* file)
{
	sim = dest;
	input.open(file,std::ios::in);

	return;
}

XMLParser::~XMLParser()
{
	input.close();
	return;
}

// Loads everything found in the config file. If the config file could not be opened then
// 0xFFFFFFFF is returned. Returns an integer equal to the number of illegal
// objects/properties in the config file. Returns 0xFFFFFFFF if we failed the load entierly.
uint32_t XMLParser::Load()
{
	// Check if the file was opened properly
	if(!input.good())
		return 0xFFFFFFFF;

	// This will hold the number of errors we encountered
	uint32_t err = 0;

	// We will use this variable to see what the return value of LoadNext() was
	int ret = 0;

	// We will use this to see if we are loading the first value
	bool first = true;

	// Load items until there was nothing left to load
	while(ret = LoadNext(first))
	{
		if(ret == -1) // Check if LoadNext() errored
		{
			// We got another error
			err++;

			// If we failed the first value we failed entirely
			if(first)
			{
				cerr << "First item in configuration file must be Simulation tags and data." << endl;
				return 0xFFFFFFFF;
			}
			else // Print a regular error
				cerr << "Error #" << err << " when attempting to read configuration file." << endl;
		}

		// We can't have to first reads
		first = false;
	}
	
	// We finished reading the config file
	return err;
}

void XMLParser::WriteGridOnly()
{
	// Check that we have a valid simluation pointer and that we've
	// loaded the simulation successfully into memory
	if(sim == NULL || !input.eof())
		return;

	// Write out all data starting with the simulation simulation
	for(uint32_t i = 0;i < sim->GetWidth();i++)
	{
		for(uint32_t j = 0;j < sim->GetHeight();j++)
		{
			// Get any entries at (i,j)
			Entry* entry = sim->GetEntriesAt(j,i);

			// If there are no entries here then output ____
			if(entry == NULL)
				cout << "____" << (j == sim->GetHeight() - 1 ? "\n" : " ");
			else
			{
				// Get the object if we have one
				Item* object = entry->GetObject();

				// If we don't have an object we output __ and if we do we output its display value
				if(object == NULL)
					cout << "__";
				else
				{
					// Get the display name
					string disp;
					object->GetParams("display",&disp); // This will not error
					
					cout << disp;
				}

				// If we have more than one property then output XX and if we have none then we output __
				if(entry->Items() > 2 || (entry->Items() > 1 && object == NULL))
					cout << "XX" << (j == sim->GetHeight() - 1 ? "\n" : " ");
				else if(entry->Items() == 0 || (entry->Items() == 1 && object != NULL))
					cout << "__" << (j == sim->GetHeight() - 1 ? "\n" : " ");
				else if(entry->Items() == 1) // Since the others failed if there is only one item then it must be a property
				{
					// There is exactly one item in the entry
					Item* item1 = entry->GetItem(0);

					// We will output this
					string disp;

					// Get the display name, it will not error
					item1->GetParams("display",&disp);

					// output the name
					cout << disp << (j == sim->GetHeight() - 1 ? "\n" : " ");
				}
				else
				{
					// There are exactly two items in the entry
					Item* item1 = entry->GetItem(0);
					Item* item2 = entry->GetItem(1);

					// We will output this
					string disp;

					// Get the appropriate display name
					if(item1->IsObject())
						item2->GetParams("display",&disp); // This will not error
					else if(item2->IsObject())
						item1->GetParams("display",&disp); // This will not error

					cout << disp << (j == sim->GetHeight() - 1 ? "\n" : " ");
				}
			}
		}
	}
	
	// Format output
	cout << endl;

	return;
}

void XMLParser::WriteInfo()
{
	// Check that we have a valid simluation pointer and that we've
	// loaded the simulation successfully into memory
	if(sim == NULL || !input.eof())
		return;

	// Write the entire image based grid out
	WriteGridOnly();

	// We use this to make numbers
	stringstream out;

	// Print out all the data in row-column order
	for(uint32_t i = 0;i < sim->GetWidth();i++)
		for(uint32_t j = 0;j < sim->GetHeight();j++)
		{
			// Get any entries at (i,j)
			Entry* entry = sim->GetEntriesAt(j,i);

			// If there are no entries here then forget about it
			if(entry == NULL)
				continue;
			
			out.str("");
			out << i << ", " << j;
			cout << "Location: " << out.str() << "\n";

			for(uint32_t k = 0;k < entry->Items();k++)
				cout << entry->GetItem(k)->ToString() << "\n";

			cout << endl;
		}

	// Flush the buffer
	cout.flush();

	return;
}

bool XMLParser::ContainsStartTag(string str)
{
	// Remove any comments and whitespace
	str = RemoveComments(str);
	str = RemoveWhiteSpace(str);

	if(str == "")
		return false;

	// < should be the first character
	if(str[0] != '<')
		return false;

	// The last character should be >
	if(str[str.length() - 1] != '>')
		return false;

	// Tags should be...a tag
	if(str.length() < 3)
		return false;

	// We found a tag
	return true;
}

bool XMLParser::ContainsEndTag(string str)
{
	// Remove any comments and whitespace
	str = RemoveComments(str);
	str = RemoveWhiteSpace(str);

	if(str == "")
		return false;

	// < should be the first character
	if(str[0] != '<')
		return false;

	// The second character should be /
	if(str[1] != '/')
		return false;

	// The last character should be >
	if(str[str.length() - 1] != '>')
		return false;

	// Tags should be...a tag
	if(str.length() < 4)
		return false;

	// We found a tag
	return true;
}

bool XMLParser::ContainsParam(string str)
{
	// Remove any comments and whitespace
	str = RemoveComments(str);
	str = RemoveWhiteSpace(str);

	if(str == "")
		return false;

	// We should have one equals sign
	size_t index = str.find_first_of('=');

	// Make sure we have an equals sign
	if(index == string::npos)
		return false;

	// We can't have more than one equal sign because it would be ambiguous
	if(str.find_first_of('=',index + 1) != string::npos)
		return false;

	// Make sure we have a name for the param
	if(index == 0)
		return false;

	// Make sure we have a value after the equals sign
	if(index == str.length() - 1)
		return false;

	// We didn't find anything wrong with the parameter
	return true;
}

// Searches for param names
uint32_t XMLParser::ContainsX(vector<string>* vec, string str)
{
	// This is what we will return
	uint32_t ret = 0;

	// Loop through each unsorted element to determine how many [str]s we have
	for(uint32_t i = 0;i < vec->size();i++)
		if(ContainsParam((*vec)[i]) && !GetParamName(RemoveWhiteSpace(RemoveComments((*vec)[i]))).compare(str))
			ret++; // We found a match so increase the count

	// This is how many we found of str
	return ret;
}

// Searches for param names. Returns 0xFFFFFFFF if not found.
uint32_t XMLParser::IndexOf(vector<string>* vec, string str)
{
	// Loop through each unsorted element to determine how many [str]s we have
	for(uint32_t i = 0;i < vec->size();i++)
		if(ContainsParam((*vec)[i]) && !GetParamName(RemoveWhiteSpace(RemoveComments((*vec)[i]))).compare(str))
			return i; // We found a match so return the index

	// This is how many we found of str
	return 0xFFFFFFFF;
}


// Returns -1 if the function errored, 0 if the function had nothing left
// to read and 1 if successful. The function attempts to read in the next object
// in the configuration file.
int XMLParser::LoadNext(bool first)
{
	// See if there's anything left
	if(!input.good() || input.eof())
		return 0;
	
	// getlines until we find a valid start tag or eof (if we hit eof then we return 0 or -1 if we hit a tag on the last line)
	string line("");
	while(!input.eof() && !ContainsStartTag(line))
		getline(input,line,'\n');

	// Eror check
	if(input.eof())
		return (ContainsStartTag(line) ? -1 : 0);

	// load all strings until a valid end tag or eof into a vector (if eof then we errored)
	vector<string> description;
	description.push_back(RemoveWhiteSpace(RemoveComments(line)));
	while(!input.eof() && !ContainsEndTag(line))
	{
		getline(input,line,'\n');

		// If we have a parameter or tag put it into our vector
		// If we don't it is irrelevant to our interests
		if(ContainsParam(line) || ContainsStartTag(line) || ContainsEndTag(line))
			description.push_back(RemoveWhiteSpace(RemoveComments(line)));
	}

	// Error check
	if(input.eof() && !ContainsEndTag(line))
		return -1;

	// check the vector for start and end tag and make sure there's only one each and the right kind (if there's not we errored)
	// we are garunteed to have at least two lines, a start and end tag as first and last respectively
	// we are also garunteed to have only one end tag
	for(uint32_t i = 1;i < description.size() - 1;i++)
		if(ContainsStartTag(description[i]))
			return -1;

	// Check that we have the right start and end tag
	if(!GetTagName(description[0]).compare(GetTagName(description[description.size() - 1])))
		return -1;

	// If this is the first read then it should be a simulation tag
	// If it's not the first read and simulation we error and if it is we process it and return 1
	// If we found a simulation and it's not the first one we also error.
	if(!first && !GetTagName(description[0]).compare("simulation"))
		return -1;
	else if(first && GetTagName(description[0]).compare("simulation"))
		return -1;
	else if(first && !GetTagName(description[0]).compare("simulation"))
	{
		// Read in our simluation data which needs to be processed differently
		// Simulation tags must have width, height and be four entries long
		if(ContainsX(&description,"width") != 1 && ContainsX(&description,"height") != 1 && description.size() != 4)
			return -1;

		// Get the values of width and height
		string height = GetParamValue(description[IndexOf(&description,"height")]);
		string width = GetParamValue(description[IndexOf(&description,"width")]);

		// Create some int values
		uint32_t h = 0;
		uint32_t w = 0;

		// Make sure that these values are numbers and if they are get them
		if(sscanf(height.c_str(),"%u",&h) == EOF)
			return -1;

		if(sscanf(width.c_str(),"%u",&w) == EOF)
			return -1;

		// Set the simulation values
		if(!sim->SetHeight(h))
			cerr << "An invalid sim height was provided, a default value was used instead." << endl;

		if(!sim->SetWidth(w))
			cerr << "An invalid sim width was provided, a default value was used instead." << endl;

		// We finished loading successfully
		return 1;
	}

	// check the vector for the type, xloc, yloc, and display (if we don't have all of them we errored)
	if(ContainsX(&description,"type") != 1 && ContainsX(&description,"xloc") != 1 && ContainsX(&description,"yloc") != 1 && ContainsX(&description,"display") != 1)
		return -1;

	// ask a function for a new object of type Item that is the appropriate type based on the read in type value (if it returns NULL we errored)
	Item* item = CreateItem(GetParamValue(description[IndexOf(&description,"type")]));

	// Error check
	if(item == NULL)
		return -1;

	// Check that the display provided is two characters long
	if(GetParamValue(description[IndexOf(&description,"display")]).length() != 2)
		return -1;

	// Create some values to hold our location
	uint32_t x = -1;
	uint32_t y = -1;

	// Make sure that these values are numbers and if they are get them
	if(sscanf(GetParamValue(description[IndexOf(&description,"xloc")]).c_str(),"%u",&x) == EOF)
		return -1;
	else // We don't need this value anymore
		description.erase(description.begin() + IndexOf(&description,"xloc"));

	if(sscanf(GetParamValue(description[IndexOf(&description,"yloc")]).c_str(),"%u",&y) == EOF)
		return -1;
	else // We don't need this value anymore
		description.erase(description.begin() + IndexOf(&description,"yloc"));

	// Add the item to the simulation which is zero indexed as opposed to the config file with is one based
	if(!sim->AddItemAt(item,x - 1,y - 1))
	{
		cerr << "Attempted to load multiple objects into the same location in configuration file" << endl;
		return -1;
	}

	// go into the vector and set the variables using SetParams (if each call doesn't return 1 then we errored)
	for(uint32_t i = 1;i < description.size() - 1;i++)
	{
		// Get the parameter value as a string
		string str = GetParamValue(description[i]);
		
		// Get the c string version of str
		const char* val = str.c_str();
		
		// Get the param name
		string p_name = GetParamName(description[i]);

		if(p_name == "paramA" || p_name == "paramB" || p_name == "paramC")
		{
			// Get the value of str we should actually pass
			double pass = FloatParamValue(p_name,val);

			if(item->SetParams(GetParamName(description[i]),pass) != 1)
			{
				cerr << "Attempted to load a variable that does not exist: " << description[i] << endl;
				return -1;
			}
		}
		else
		{
			// Get the value of str we should actually pass
			if(!p_name.compare("type") || !p_name.compare("name") || !p_name.compare("display") || !p_name.compare("color"))
			{
				if(item->SetParams(GetParamName(description[i]),val) != 1)
				{
					cerr << "Attempted to load a variable that does not exist: " << description[i] << endl;
					return -1;
				}
			}
			else
			{
				if(item->SetParams(GetParamName(description[i]),ParamValue(p_name,val)) != 1)
				{
					cerr << "Attempted to load a variable that does not exist: " << description[i] << endl;
					return -1;
				}
			}

			
		}
	}

	// We successfully read in a new item
	return 1;
}

// Creates a new Item specified by the type given by [type].
// Returns NULL if we were given an invalid type.
Item* XMLParser::CreateItem(string type)
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

	// Return what we are supposed to
	return ret;
}

// Assumes no ws or comments
string XMLParser::GetTagName(string str)
{
	// If we have a tag we know where the name is
	if(ContainsStartTag(str))
	{
		// Cut the <> out
		str = str.substr(1,str.length() - 2);

		// This is where we will save our new string
		string ret("");

		// We need this
		locale loc;

		// Tags are case insensitive so we send them to lower case
		for(size_t i=0; i<str.length(); ++i)
			ret += tolower(str[i],loc);

		// Return the string
		return ret;
	}
	else if(ContainsEndTag(str))
	{
		// Cut the <> out
		str = str.substr(1,str.length() - 3);

		// This is where we will save our new string
		string ret("");

		// We need this
		locale loc;

		// Tags are case insensitive so we send them to lower case
		for(size_t i=0; i<str.length(); ++i)
			ret += tolower(str[i],loc);

		// Return the string
		return ret;
	}
	
	// We failed to provide a tag so return nothing
	// Since tags must be at least one long they will never be ""
	return "";
}

// Assumes no ws or comments
string XMLParser::GetParamName(string str)
{
	// If we don't have a parameter we can't return anything useful
	if(!ContainsParam(str))
		return false;

	// Check if we even have a parameter
	if(str.find_first_of("=") == string::npos)
		return "";

	// Return the parameter name
	return str.substr(0,str.find_first_of("="));
}

// Assumes no ws or comments
string XMLParser::GetParamValue(string str)
{
	// If we don't have a parameter we can't return anything useful
	if(!ContainsParam(str))
		return false;

	// Check if we even have a parameter
	if(str.find_first_of("=") == string::npos)
		return "";

	// Return the parameter name
	return str.substr(str.find_first_of("=") + 1);
}

// Returns a double since only doubles may be passed on ...
double XMLParser::FloatParamValue(string param, const char* str)
{
	// Create a place to return this value.
	float ret = 0.0f;

	// Get the value
	if(sscanf(str,"%f",&ret) == EOF)
		return -1.0f;

	// Return the value
	return (double)ret;
}

// Returns a pointer to c strings or an unsigned integer depending on what type of param we are using.
// Returns 0xFFFFFFFF if we didn't get a number when we expected it.
uint32_t XMLParser::ParamValue(string param, const char* str)
{
	// We will use this for storage
	uint32_t ret;

	// Get the value
	if(sscanf(str,"%u",&ret) == EOF)
		return 0xFFFFFFFF;

	// Return the number we were given
	return ret;
}

string XMLParser::RemoveComments(string str)
{
	return str.substr(0,str.find("//"));
}

string XMLParser::RemoveParamWhiteSpace(string str, string whitespace)
{
	// This will hold the [param=] part
	string part1;

	// This will hold what the param equals
	string part2;

	// Figure out where the equals sign is
	size_t e_index = str.find_first_of('=');

	// Remove the = for ease of codeing
	str.replace(e_index,1,"");

	// Figure out where to chop off the first string at
	e_index = str.find_first_not_of(whitespace,e_index);

	// Figure out what part1 is and add the = back into it
	part1 = RemoveWhiteSpace(str.substr(0,e_index),whitespace) + string("=");

	// Check if there is white space after the param value
	size_t index = str.find_last_not_of(whitespace);

	// Figure out what part2 is
	if(index == string::npos)
		part2 = str.substr(e_index);
	else
		part2 = str.substr(e_index,index - e_index + 1);

	// Return our complete string
	return part1 + part2;
}

string XMLParser::RemoveWhiteSpace(string str, string whitespace)
{
	// Parameter values can have white space so we need to not remove that part
	size_t e_index = str.find_first_of('=');
	if(e_index != string::npos)
		return RemoveParamWhiteSpace(str,whitespace);

	// This is the next of our next whitespace character
	size_t index = 0;

	// Remove whitespaces until we can't find any more
	while((index = str.find_first_of(whitespace,index)) != string::npos)
		str.replace(index,1,"");

	// Return our modified string
	return str;
}

#endif
