///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Behaviour.h //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// This is the abstraction of what it means to behave in a simulation.         ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _BEHAVIOUR_H
#define _BEHAVIOUR_H

#include <string>
#include "MoveInfo.h"
#include "CollisionBehaviour.h"
#include "ProbeBehaviour.h"
#include "MoveBehaviour.h"
#include "HardPlace.h"
#include "Probeable.h"
#include "Stationary.h"
using std::string;

// Contains a what it means to behave in a simulation.
class Behaviour
{
public:
	Behaviour();
	~Behaviour();
	virtual Behaviour* CreateNew();

	void ChangeCollisionBehaviour(CollisionBehaviour* newcollider);
	void ChangeMoveBehaviour(MoveBehaviour* newaction);
	void ChangeProbeBehaviour(ProbeBehaviour* newprobe);

	CollisionBehaviour* GetCollisionBehaviour();
	MoveBehaviour* GetMoveBehaviour();
	ProbeBehaviour* GetProbeBehaviour();

	// Returns a number from -8 to 8 which determines where the Item will be
	// sent after being collided with. A zero indicates that it will not move.
	// A negative number implies that it will travel as far as it can in the
	// indicated direction and 1 to 8 indicates a direction for a single step.
	// Returns 9 if the Item is consumed.
	// 1 2 3      -1 -2 -3
	// 4 0 5      -4 00 -5
	// 6 7 8      -6 -7 -8
	int Collide(int collide_from);
	string Move(MoveInfo* info, uint32_t x, uint32_t y, uint32_t last_x, uint32_t last_y, uint32_t width, uint32_t height);
	bool Probe();
protected:
	CollisionBehaviour* collider;
	MoveBehaviour* action;
	ProbeBehaviour* probe;
};

#endif
