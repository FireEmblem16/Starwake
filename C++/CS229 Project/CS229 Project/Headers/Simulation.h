///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Simulation.h //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Defines the representation of a simulation environment.                     ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _SIMULATION_H
#define _SIMULATION_H

#include <sstream>
#include <string>
#include <vector>
#include <cstring> // Get this for NULL
#include "Defines.h"
#include "Entry.h"
using std::string;
using std::stringstream;
using std::vector;

// Contains information on a simulated environment
class Simulation
{
public:
	Simulation(uint32_t h, uint32_t w);
	Simulation(const Simulation& rhs);
	Simulation& operator =(const Simulation& rhs);
	~Simulation();

	bool AddItemAt(Item* item, uint32_t x, uint32_t y); // Adds an item to (x,y), may be an object or property
	Entry* GetEntriesAt(uint32_t x, uint32_t y); // Returns the actual value, not a copy
	Entry* GetEntry(uint32_t index); // Returns the actual value of the [index]th entry, not a copy
	uint32_t Entries(); // Returns the number of entries in the simulation

	Entry* FindItemByAddress(Item* item);
	void RemoveItemByAddress(Item* item);

	uint32_t GetHeight();
	uint32_t GetWidth();

	bool SetHeight(uint32_t h); // Returns false if a max or min size is used instead of h
	bool SetWidth(uint32_t w); // Returns false if a max or min size is used instead of w

	string ToXML();

	void Clear();
private:
	vector<Entry>* entries;

	uint32_t height;
	uint32_t width;
};

#endif
