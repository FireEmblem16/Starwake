///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// MoveInfo.h ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// This class contains information about an object's surroundings.             ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _MOVE_INFO_H
#define _MOVE_INFO_H

#include <string>
#include <vector>
#include "Defines.h"
#include "Target.h"
using std::string;
using std::vector;

// Contains data about a visable object
class ItemData
{
public:
	string type;
	int x;
	int y;
	string color;
	uint32_t energy;

	bool operator ==(ItemData& rhs);
};

// Contains data about a move command given to a robot
class MoveInfo
{
public:
	static bool IsImpass(string type);
	static bool IsObject(string type);
	static bool HasEnergy(string type);

	void AddVisable(ItemData data);
	ItemData GetVisable(size_t i);
	ItemData GetVisable(Target p);
	size_t Visables();

	Target Triangulate(); // Returns (-1,-1) if we fail
	Target GuessPush(MoveInfo* last, Target here); // Always returns a good value since this is our last resort

	void AddProbeDistance(int d); // Add from left to right, add -1 if no known value
	int GetProbeDistance(uint32_t i); // Starting from the left and proceeding right
	uint32_t ProbeSize();

	void SetSuccess(string str);
	string GetSuccess();

	void SetEnergy(int e);
	int GetEnergy(); // Returns -1 if energy was not given

	bool HasImpassAtXY(Target p);
	bool HasImpassAtXY(uint32_t x, uint32_t y);

	template <class T> T abs(T x); // Requres > to be defined relative to 0
private:
	int energy; // -1 if not given
	string success; // true if the last action was successful, false if not, and null if nothing happened
	vector<ItemData> visables;
	vector<int> probe_distances;

	int DetermineX(Target& t1, Target& t2, Target& t3);
	int DetermineY(Target& t1, Target& t2, Target& t3);
};

#endif
