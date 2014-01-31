///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// Behaviour.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the Behaviour class.                                             ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _PILL_ADDICT_CPP
#define _PILL_ADDICT_CPP

#include "../Headers/Behaviour.h"

Behaviour::Behaviour()
{
	probe = new Probeable();
	action = new Stationary();
	collider = new HardPlace();

	return;
}

Behaviour::~Behaviour()
{
	delete probe;
	delete action;
	delete collider;

	return;
}

Behaviour* Behaviour::CreateNew()
{
	Behaviour* ret = new Behaviour();

	ProbeBehaviour* p = probe->CreateNew();
	ret->ChangeProbeBehaviour(p);
	delete p;
	
	MoveBehaviour* m = action->CreateNew();
	ret->ChangeMoveBehaviour(m);
	delete m;
	
	CollisionBehaviour* c = collider->CreateNew();
	ret->ChangeCollisionBehaviour(c);
	delete c;

	return ret;
}

void Behaviour::ChangeCollisionBehaviour(CollisionBehaviour* newcollider)
{
	delete collider;
	collider = newcollider->CreateNew();
	return;
}

void Behaviour::ChangeMoveBehaviour(MoveBehaviour* newaction)
{
	delete action;
	action = newaction->CreateNew();
	return;
}

void Behaviour::ChangeProbeBehaviour(ProbeBehaviour* newprobe)
{
	delete probe;
	probe = newprobe->CreateNew();
	return;
}

CollisionBehaviour* Behaviour::GetCollisionBehaviour()
{
	return collider;
}

MoveBehaviour* Behaviour::GetMoveBehaviour()
{
	return action;
}

ProbeBehaviour* Behaviour::GetProbeBehaviour()
{
	return probe;
}

// Returns a number from -8 to 8 which determines where the Item will be
// sent after being collided with. A zero indicates that it will not move.
// A negative number implies that it will travel as far as it can in the
// indicated direction and 1 to 8 indicates a direction for a single step.
// Returns 9 if the Item is consumed.
// 1 2 3      -1 -2 -3
// 4 0 5      -4 00 -5
// 6 7 8      -6 -7 -8
int Behaviour::Collide(int collide_from)
{
	return collider->Collide(collide_from);
}

string Behaviour::Move(MoveInfo* info, uint32_t x, uint32_t y, uint32_t last_x, uint32_t last_y, uint32_t width, uint32_t height)
{
	return action->Move(info,x,y,last_x,last_y,width,height);
}

bool Behaviour::Probe()
{
	return probe->Probe();
}

#endif
