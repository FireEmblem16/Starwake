///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Entry.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the entry class.                                                 ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ENTRY_CPP
#define _ENTRY_CPP

#include "../Headers/Entry.h"

Entry::Entry(int x_pos, int y_pos)
{
	x = x_pos;
	y = y_pos;
	items = new vector<Item*>();

	return;
}

Entry::Entry(const Entry& rhs)
{
	x = rhs.x;
	y = rhs.y;
	items = new vector<Item*>(*rhs.items);

	return;
}

Entry& Entry::operator =(const Entry& rhs)
{
	if(this == &rhs)
		return *this;

	x = rhs.x;
	y = rhs.y;
	
	delete items;
	items = new vector<Item*>(*rhs.items);

	return *this;
}

Entry::~Entry()
{
	delete items;

	return;
}

bool Entry::AddItem(Item* item)
{
	// Look to see if this entry has an object in it already if item is an object.
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->IsObject() && item->IsObject())
			return false;

	items->push_back(item);
	return true;
}

// Returns the actual [index]th item. Returns NULL if index is out of bounds.
Item* Entry::GetItem(uint32_t index)
{
	if(index < 0 || index >= items->size())
		return NULL;

	return (*items)[index];
}

Item* Entry::GetType(string type)
{
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->GetType() == type)
			return (*items)[i];

	return NULL;
}

void Entry::RemoveItemByAddress(Item* item)
{
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i] == item)
			items->erase(items->begin() + i--);

	return;
}

bool Entry::ContainsObject()
{
	// Look for the object
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->IsObject())
			return true;

	// We didn't find an object
	return false;
}

uint32_t Entry::ContainsManyObjects()
{
	uint32_t count = 0;

	// Look for the object
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->IsObject())
			count++;

	// We didn't find an at least two objects
	return count;
}

bool Entry::ContainsType(string type)
{
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->GetType() == type)
			return true;

	return false;
}

bool Entry::ContainsType(string type, uint32_t atleast)
{
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->GetType() == type)
			if(--atleast == 0)
				return true;

	return false;
}

Item* Entry::GetObject()
{
	// Look for the object
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->IsObject())
			return (*items)[i];

	// We didn't find an object
	return NULL;
}

// One indexed
Item* Entry::GetObject(uint32_t num)
{
	// Look for the object
	for(uint32_t i = 0;i < items->size();i++)
		if((*items)[i]->IsObject())
			if(--num == 0)
				return (*items)[i];

	// We didn't find an object
	return NULL;
}

// Not used for probing
bool Entry::EntryVisible(uint32_t index)
{
	Item* item = (*items)[index];

	if(ContainsType("fog") && item->GetType() != "fog")
		return false;

	return true;
}

//   y=0
//x=0 1 2 3
//    4 0 5
//    6 7 8
int Entry::CollisionFrom(Entry* old)
{
	int xx = x - old->x;
	int yy = y - old->y;

	if(xx > 0 && yy == 0)
		return 4;
	else if(xx < 0 && yy == 0)
		return 5;
	else if(xx == 0 && yy > 0)
		return 2;
	else if(xx == 0 && yy < 0)
		return 7;
	else if(xx > 0 && yy > 0)
		return 1;
	else if(xx > 0 && yy < 0)
		return 6;
	else if(xx < 0 && yy > 0)
		return 3;
	else if(xx < 0 && yy < 0)
		return 8;

	return 0;
}

uint32_t Entry::GetHash(uint32_t w)
{
	return x + y * w;
}

uint32_t Entry::GetX()
{
	return x;
}

uint32_t Entry::GetY()
{
	return y;
}

uint32_t Entry::Items()
{
	return items->size();
}

#endif
