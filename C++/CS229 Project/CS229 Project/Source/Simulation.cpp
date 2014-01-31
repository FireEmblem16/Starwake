///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// Simulation.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the simulation environment.                                      ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _SIMULATION_CPP
#define _SIMULATION_CPP

#include "../Headers/Simulation.h"

Simulation::Simulation(uint32_t h, uint32_t w)
{
	height = (h > 35 ? 35 : (h < 1 ? 1 : h));
	width = (w > 35 ? 35 : (w < 1 ? 1 : w));
	entries = new vector<Entry>();

	return;
}

Simulation::Simulation(const Simulation& rhs)
{
	height = rhs.height;
	width = rhs.width;
	entries = new vector<Entry>(*rhs.entries);

	return;
}

Simulation& Simulation::operator =(const Simulation& rhs)
{
	if(this == &rhs)
		return *this;

	height = rhs.height;
	width = rhs.width;

	delete entries;
	entries = new vector<Entry>(*rhs.entries);

	return *this;
}

Simulation::~Simulation()
{
	delete entries;

	return;
}

// Adds an item to the entry at (x,y). The item may be an object or property.
// If there is no entry at (x,y) then a new entry will be added ordered by
// row-column order. There can be no more than one object in any entry at one.
bool Simulation::AddItemAt(Item* item, uint32_t x, uint32_t y)
{
	// Check if we are in the grid
	if(x < 0 || x >= width || y < 0 || y >= height)
		return false;

	// Check if we already have an entry at (x,y)
	Entry* entry = GetEntriesAt(x,y);

	// If we already have an entry then just add the value
	if(entry != NULL)
		return entry->AddItem(item);
	else
	{
		// Create a new entry
		entry = new Entry(x,y);
		entry->AddItem(item);

		// Get our new entry's hash value
		uint32_t hash = entry->GetHash(width);

		// There are two weird bound condition for us to check and one is outside the loop
		if(entries->size() == 0 || (*entries)[0].GetHash(width) > hash)
		{
			entries->insert(entries->begin(),*entry);
			return true;
		}

		// Look for the entry
		for(uint32_t first = 0,last = entries->size() - 1,mid = (first + last) / 2;first <= last;mid = (first + last) / 2)
		{
			// Get the hash code of the current entry we are looking at
			Entry T_entry1 = (*entries)[mid];
			uint32_t T_hash1 = T_entry1.GetHash(width);
			
			// We have a weird bounds check
			if(mid == entries->size() - 1)
			{
				// Figure out where our entry should be placed
				if(hash < T_hash1)
					entries->insert(entries->end(),*entry);
				else
					entries->push_back(*entry);

				// We added what we needed to
				return true;
			}

			// Get the hash code of the next entry
			Entry T_entry2 = (*entries)[mid + 1];
			uint32_t T_hash2 = T_entry2.GetHash(width);

			// If we have the correct position then enter the entry
			if(T_hash1 < hash && hash < T_hash2)
			{
				entries->insert(entries->begin() + mid + 1,*entry);
				return true;
			}

			// We didn't find what we wanted so change the first or last number appropriatly
			if(T_hash1 > hash)
				last = mid - 1;
			else
				first = mid + 1;
		}
	}

	return false;
}

// Returns the actual value of the entry found at (x,y).
// Returns NULL if there is no entry at that location.
Entry* Simulation::GetEntriesAt(uint32_t x, uint32_t y)
{
	// Check for out of bounds expections and if we even have an entry
	if(x < 0 || x >= width || y < 0 || y >= height || entries->size() == 0)
		return NULL;

	// Get the hash code of this position
	uint32_t hash = x + y * width;

	// Look for the entry
	for(int first = 0,last = entries->size() - 1,mid = (first + last) / 2;first <= last;mid = (first + last) / 2)
	{
		// Get the hash code of the current entry we are looking at
		Entry T_entry = (*entries)[mid];
		uint32_t T_hash = T_entry.GetHash(width);

		// If we have the correct position then return the entry
		if(T_hash == hash)
			return &(*entries)[mid];

		// We didn't find what we wanted so change the first or last number appropriatly
		if(T_hash > hash)
			last = mid - 1;
		else
			first = mid + 1;
	}

	// We didn't find what we wanted
	return NULL;
}

// Returns the actual value of the [index]th entry.
// Returns NULL if [index] is out of bounds.
Entry* Simulation::GetEntry(uint32_t index)
{
	if(index < 0 || index >= entries->size())
		return NULL;

	return &(*entries)[index];
}

uint32_t Simulation::Entries()
{
	return entries->size();
}

Entry* Simulation::FindItemByAddress(Item* item)
{
	for(uint32_t i = 0;i < entries->size();i++)
	{
		Entry entry = (*entries)[i];

		for(uint32_t j = 0;j < entry.Items();j++)
			if(entry.GetItem(j) == item)
				return &(*entries)[i];
	}

	return NULL;
}

void Simulation::RemoveItemByAddress(Item* item)
{
	for(uint32_t i = 0;i < entries->size();i++)
	{
		(*entries)[i].RemoveItemByAddress(item);

		if((*entries)[i].Items() == 0)
			entries->erase(entries->begin() + i--);
	}

	return;
}

uint32_t Simulation::GetHeight()
{
	return height;
}

uint32_t Simulation::GetWidth()
{
	return width;
}

// Returns false if a max or min size is used instead of h
bool Simulation::SetHeight(uint32_t h)
{
	height = (h > 35 ? 35 : (h < 1 ? 1 : h));
	return (h > 35 ? false : (h < 1 ? false : true));
}

// Returns false if a max or min size is used instead of w
bool Simulation::SetWidth(uint32_t w)
{
	width = (w > 35 ? 35 : (w < 1 ? 1 : w));
	return (w > 35 ? false : (w < 1 ? false : true));
}

string Simulation::ToXML()
{
	stringstream w, h;
	w << width;
	h << height;

	return "<Simulation>\nwidth = " + w.str() + "\nheight = " + h.str() + "\n</Simulation>";
}

void Simulation::Clear()
{
	delete entries;
	entries = new vector<Entry>();
	return;
}

#endif
