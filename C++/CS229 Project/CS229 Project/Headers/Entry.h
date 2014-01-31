///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Simulation.h //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Defines the representation of an entry in the simulation environment.       ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ENTRY_H
#define _ENTRY_H

#include <vector>
#include "Defines.h"
#include "Item.h"
using std::vector;

// Contains information on an entry in a simulation environment at one location.
class Entry
{
public:
	Entry(int x_pos, int y_pos);
	Entry(const Entry& rhs);
	Entry& operator =(const Entry& rhs);
	~Entry();

	bool AddItem(Item* item);
	Item* GetItem(uint32_t index); // Returns the [index]th item, actual, not a copy
	Item* GetType(string type);
	void RemoveItemByAddress(Item* item);

	bool ContainsObject();
	uint32_t ContainsManyObjects();
	bool ContainsType(string type);
	bool ContainsType(string type, uint32_t atleast);
	Item* GetObject(); // Returns NULL if there isn't an object in this entry
	Item* GetObject(uint32_t num); // one indexed

	bool EntryVisible(uint32_t index);

	// 1 2 3
	// 4 0 5
	// 6 7 8
	int CollisionFrom(Entry* old);

	uint32_t GetHash(uint32_t w); // Returns x + w * y
	uint32_t GetX();
	uint32_t GetY();
	uint32_t Items(); // Returns the number of items in this entry.
private:
	vector<Item*>* items; // Contains all objects and properties at this location

	uint32_t x;
	uint32_t y;
};

#endif
