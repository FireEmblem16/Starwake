///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// MoveInfo.cpp ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the MoveInfo class.                                              ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _MOVE_INFO_CPP
#define _MOVE_INFO_CPP

#include "../Headers/MoveInfo.h"

bool ItemData::operator ==(ItemData& rhs)
{
	return (type == rhs.type && color == rhs.color);
}

void MoveInfo::AddVisable(ItemData data)
{
	visables.push_back(data);
	return;
}

ItemData MoveInfo::GetVisable(size_t i)
{
	if(i < 0 || i >= visables.size())
		return ItemData();

	return visables[i];
}

ItemData MoveInfo::GetVisable(Target p)
{
	for(size_t i = 0;i < visables.size();i++)
		if(Target(visables[i].x,visables[i].y) == p)
			return visables[i];

	// We don't have an item at p so return a failiur item
	static ItemData fail;
	return fail;
}

size_t MoveInfo::Visables()
{
	return visables.size();
}

int MoveInfo::DetermineX(Target& t1, Target& t2, Target& t3)
{
	// t3 is uncertain
	if(t3.x == -1)
		return -1;

	// These are the value we will use to determine our actual x value
	Target* left;
	Target* right;
	Target* unused;

	// We need to consult targets with different x values, they may all be different but not necessarily
	if(t1.x == t2.x)
	{
		if(t1.x < t3.x)
		{
			left = &t1;
			right = &t3;
		}
		else
		{
			left = &t3;
			right = &t1;
		}

		unused = &t2;
	}
	else
	{
		if(t1.x < t2.x)
		{
			left = &t1;
			right = &t2;
		}
		else
		{
			left = &t2;
			right = &t1;
		}

		unused = &t3;
	}

	// If we are on far ends then we are sure that the value is one greater than our left value
	if(right->x - left->x == 2)
		return left->x + 1;

	// We have targets with adjacent x values so we need to determine where the middle is
	if(unused->x > right->x || unused->x == left->x && abs(unused->y - left->y) == 1)
		return right->x;
	else if(unused->x < left->x || unused->x == right->x && abs(unused->y - right->y) == 1)
		return left->x;

	// We were given a set of targets which could be in multiple places
	return -1;
}

int MoveInfo::DetermineY(Target& t1, Target& t2, Target& t3)
{
	// t3 is uncertain
	if(t3.y == -1)
		return -1;

	// These are the value we will use to determine our actual y value
	Target* top;
	Target* bottom;
	Target* unused;

	// We need to consult targets with different y values, they may all be different but not necessarily
	if(t1.y == t2.y)
	{
		if(t1.y < t3.y)
		{
			bottom = &t1;
			top = &t3;
		}
		else
		{
			bottom = &t3;
			top = &t1;
		}

		unused = &t2;
	}
	else
	{
		if(t1.y < t2.y)
		{
			bottom = &t1;
			top = &t2;
		}
		else
		{
			bottom = &t2;
			top = &t1;
		}

		unused = &t3;
	}

	// If we are on far ends then we are sure that the value is one greater than our top value
	if(top->y - bottom->y == 2)
		return bottom->y + 1;

	// We have targets with adjacent y values so we need to determine where the middle is
	if(unused->y > top->y || unused->y == bottom->y && abs(unused->x - bottom->x) == 1)
		return top->y;
	else if(unused->y < bottom->y || unused->y == top->y && abs(unused->x - top->x) == 1)
		return bottom->y;

	// We were given a set of targets which could be in multiple places
	return -1;
}

Target MoveInfo::Triangulate()
{
	// This is our fail value
	static Target fail(-1,-1);

	// Check if we are capable of triagulating
	if(visables.size() < 2)
		return fail;

	// We can triangulate with two visables if they are in corners
	if(visables.size() == 2)
		if(abs(visables[0].x - visables[1].x) == 2 && abs(visables[0].y - visables[1].y) == 2)
			return Target((visables[0].x + visables[1].x) / 2,(visables[0].y + visables[1].y) / 2);

	// We can use the first two targets no matter what
	Target p0(visables[0].x,visables[0].y);
	Target p1(visables[1].x,visables[1].y);
	
	// We have to find a target that is non-linear with the other two
	Target p2 = fail;
	for(size_t i = 2;i < visables.size() && p2 == fail;i++)
		if((visables[i].x != p0.x || visables[i].x != p1.x) && (visables[i].y != p0.y || visables[i].y != p1.y))
		{
			// We need to check for L blocks as the can be in two different locations which means they can't be triangulated
			if(p0.Distance(p1) < 1.1f)
			{
				// If we make an L block we can't use the third target
				if(abs(p0.x - visables[i].x) == 2 && p0.y == visables[i].y)
					continue;
				else if(abs(p1.x - visables[i].x) == 2 && p1.y == visables[i].y)
					continue;
				else if(abs(p0.y - visables[i].y) == 2 && p0.x == visables[i].x)
					continue;
				else if(abs(p1.y - visables[i].y) == 2 && p1.x == visables[i].x)
					continue;
			}

			p2 = Target(visables[i].x,visables[i].y);
			break;
		}

	// If we couldn't find anything then we failed
	if(p2 == fail)
		return fail;

	// Triangulate
	Target ret(DetermineX(p0,p1,p2),DetermineY(p0,p1,p2));

	// If we have even one -1 we failed
	if(ret.x == -1 || ret.y == -1)
		return fail;

	// Success
	return ret;
}

// This makes no garuntees as weird shit can go down, however unlikely.
// A future triangulation should make up for it however.
Target MoveInfo::GuessPush(MoveInfo* last, Target here)
{
	// Get what is currently in our old spot
	ItemData herenow = GetVisable(here);
	ItemData oldversion;
	oldversion.x = -1; // Make sure we have a fail check
	
	for(size_t i = 0;i < last->visables.size();i++)
		if(last->visables[i] == herenow) // This is the best we can do since objects don't have to have a unique name
		{
			oldversion = last->visables[i];
			break;
		}

	// This shouldn't happen but it's good to make sure, just in case
	if(oldversion.x == -1)
		return here;

	// Return our new position
	return here + here - Target(oldversion.x,oldversion.y);
}

// Add from left to right, add -1 if no known value
void MoveInfo::AddProbeDistance(int d)
{
	probe_distances.push_back(d);
	return;
}

// Starting from the left and proceeding right
int MoveInfo::GetProbeDistance(uint32_t i)
{
	if(i < 0 || i >= probe_distances.size())
		return -1;

	return probe_distances[i];
}

uint32_t MoveInfo::ProbeSize()
{
	return probe_distances.size();
}

void MoveInfo::SetSuccess(string str)
{
	if(str != "true" && str != "false" && str != "null")
		return;

	success = str;
	return;
}

string MoveInfo::GetSuccess()
{
	if(success == "")
		return "null";

	return success;
}

void MoveInfo::SetEnergy(int e)
{
	energy = e;
	return;
}

// Returns -1 if energy was not given
int MoveInfo::GetEnergy()
{
	return energy;
}

bool MoveInfo::HasImpassAtXY(Target p)
{
	return HasImpassAtXY(p.x,p.y);
}

bool MoveInfo::HasImpassAtXY(uint32_t x, uint32_t y)
{
	// This is the indexes were we find something at (x,y)
	vector<int> indexes;

	// Look to see if we have something at (x,y)
	for(uint32_t i = 0;i < visables.size();i++)
		if(visables[i].x == x && visables[i].y == y)
			indexes.push_back(i);

	// Check each item at (x,y) to see if it is impassable
	// One impass makes the entire location an impass
	for(uint32_t i = 0;i < indexes.size();i++)
		if(IsImpass(visables[indexes[i]].type))
			return true;

	// We found no problems at (x,y)
	return false;
}

bool MoveInfo::IsImpass(string type)
{
	// Look for the type we were given
	if(!type.compare("ball"))
		return false;
	else if(!type.compare("block"))
		return false;
	else if(!type.compare("earth-rock"))
		return true;
	else if(!type.compare("energy-pill"))
		return false;
	else if(!type.compare("robot"))
		return true;
	else if(!type.compare("rumulan-rock"))
		return true;
	else if(!type.compare("hole"))
		return true;
	else if(!type.compare("jammer"))
		return false;
	else if(!type.compare("lava"))
		return false;
	else if(!type.compare("mud"))
		return false;
	else if(!type.compare("water"))
		return false;

	// We were given an undocumented type so assume worst case
	return true;
}

bool MoveInfo::IsObject(string type)
{
	// Look for the type we were given
	if(!type.compare("ball"))
		return true;
	else if(!type.compare("block"))
		return true;
	else if(!type.compare("earth-rock"))
		return true;
	else if(!type.compare("energy-pill"))
		return true;
	else if(!type.compare("robot"))
		return true;
	else if(!type.compare("rumulan-rock"))
		return true;
	else if(!type.compare("hole"))
		return false;
	else if(!type.compare("jammer"))
		return false;
	else if(!type.compare("lava"))
		return false;
	else if(!type.compare("mud"))
		return false;
	else if(!type.compare("water"))
		return false;

	// We were given an undocumented type so assume worst case
	return true;
}

bool MoveInfo::HasEnergy(string type)
{
	if(!type.compare("energy-pill"))
		return true;
	else if(!type.compare("robot"))
		return true;

	return false;
}

template <class T> T MoveInfo::abs(T x)
{
	if(x > 0)
		return x;

	return -x;
}

#endif
