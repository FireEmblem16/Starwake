///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Centurion.h ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Wanders the perimeter of the grid.                                          ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _CENTURION_H
#define _CENTURION_H

#include <climits>
#include <ctime>
#include <string>
#include <vector>
#include "Defines.h"
#include "MoveBehaviour.h"
#include "MoveInfo.h"
#include "Target.h"
using std::string;
using std::vector;

class Centurion : public MoveBehaviour
{
public:
	Centurion();
	virtual MoveBehaviour* CreateNew();

	enum Direction {NORTH = 0, FORWARD = 0, SOUTH = 1, BACK = 1, EAST = 2, RIGHT = 2, WEST = 3, LEFT = 3, NOT_SET = 4};
	enum Phase {ZeroZero = 0, XZero = 1, XY = 2, ZeroY = 3, ZeroZeroRoundTwo = 5};

	// If given (-1,-1) will choose to do nothing, takes last (0xFFFF,0xFFFF) if we haven't moved yet
	virtual string Move(MoveInfo* info, uint32_t x, uint32_t y, uint32_t last_x, uint32_t last_y, uint32_t width, uint32_t height);
	Target GetDirection(Target here, Direction dir);
	void Rotate(Direction d = RIGHT); // We can only rotate LEFT or RIGHT
private:
	void UpdateFaceAndVelocity(Target here, Target last);
	void AddNewTargets(MoveInfo* info);
	void RemoveTarget(Target rm);
	bool HasTarget(Target p); // Checks e too, since non-positive e values are not energy pills
	Target GetTarget(Target here);
	void SearchAndDestroy(MoveInfo* info, Target here, Target dim);
	Target DetermineMoveDirection(MoveInfo* info, Target here, Target there, Target last, Target dim);

	long abs(long X);
	long rand(long upperbound = LONG_MAX, long min = 0);
	bool OnGrid(Target p, Target dim);

	Direction face;
	Target last_location;
	vector<Target> targets;

	int turn_counter;
	Phase phase;
	bool reverse;
};

#endif
