///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// PillSeeker.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the PillSeeker class.                                            ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _CENTURION_CPP
#define _CENTURION_CPP

#include "../Headers/Centurion.h"

Centurion::Centurion() : targets(), last_location(0,0)
{
	face = NORTH; // All robots begin facing up
	phase = ZeroZero;
	reverse = true;

	// Add our new target
	targets.push_back(Target(0,0,-100));

	return;
}

MoveBehaviour* Centurion::CreateNew()
{
	return new Centurion();
}

// Looks into the spaces in info and adds any pills found.
void Centurion::AddNewTargets(MoveInfo* info)
{
	return;
}

void Centurion::RemoveTarget(Target rm)
{
	// Look for rm in info
	for(uint32_t i = 0;i < targets.size();i++)
		if(targets[i] == rm && targets[i].e == rm.e) // The e's should always be the same but...who knows
		{
			targets.erase(targets.begin() + i);
			return;
		}

	// If we get here we have a bad target, not that it really matters
	return;
}

// Returns true
bool Centurion::HasTarget(Target p)
{
	return true;
}

// Returns (-1,-1) if we have no current target.
Target Centurion::GetTarget(Target here)
{
	// Check if we have a valid target
	if(targets.size() == 0)
		return Target(-1,-1);

	// This is the index of the closest target
	size_t closest = 0;

	// Look for the closest target
	for(size_t i = 1;i < targets.size();i++)
		if(targets[closest].FurtherThan(targets[i],here))
			closest = i; // The second target is closer so we should go after it
		else if(targets[closest].AsFarAs(targets[i],here) && targets[closest].e < targets[i].e)
			closest = i; // We have more energy in the second target so we should probably go after that one

	// Return what we found
	return targets[closest];
}

// We target (0,0) first then (x,0) then (x,y) then (0,y) then (0,0) then go in reverse
void Centurion::SearchAndDestroy(MoveInfo* info, Target here, Target dim)
{
	targets.clear();

	switch(phase)
	{
	case ZeroZero:
		if(reverse)
		{
			reverse = false;
			phase = XZero;
			targets.push_back(Target(dim.x - 1,0,-100));
		}

		break;
	case XZero:
		if(reverse)
		{
			phase = ZeroZero;
			targets.push_back(Target(0,0,-100));
		}
		else
		{
			phase = XY;
			targets.push_back(Target(dim.x - 1,dim.y - 1,-100));
		}

		break;
	case XY:
		if(reverse)
		{
			phase = XZero;
			targets.push_back(Target(dim.x - 1,0,-100));
		}
		else
		{
			phase = ZeroY;
			targets.push_back(Target(0,dim.y - 1,-100));
		}

		break;
	case ZeroY:
		if(reverse)
		{
			phase = XY;
			targets.push_back(Target(dim.x - 1,dim.y - 1,-100));
		}
		else
		{
			phase = ZeroZeroRoundTwo;
			targets.push_back(Target(0,0,-100));
		}

		break;
	case ZeroZeroRoundTwo:
		if(!reverse)
		{
			reverse = true;
			phase = ZeroY;
			targets.push_back(Target(0,dim.y - 1,-100));
		}

		break;
	}

	return;
}

// Brute force this algorithm since clever tricks would probably take too much time
Target Centurion::GetDirection(Target here, Direction dir)
{
	switch(face)
	{
	case NORTH:
		switch(dir)
		{
		case FORWARD:
			return here + Target(0,1);
			break;
		case BACK:
			return here + Target(0,-1);
			break;
		case RIGHT:
			return here + Target(1,0);
			break;
		case LEFT:
			return here + Target(-1,0);
			break;
		}

		break;
	case SOUTH:
		switch(dir)
		{
		case FORWARD:
			return here + Target(0,-1);
			break;
		case BACK:
			return here + Target(0,1);
			break;
		case RIGHT:
			return here + Target(-1,0);
			break;
		case LEFT:
			return here + Target(1,0);
			break;
		}

		break;
	case EAST:
		switch(dir)
		{
		case FORWARD:
			return here + Target(1,0);
			break;
		case BACK:
			return here + Target(-1,0);
			break;
		case RIGHT:
			return here + Target(0,-1);
			break;
		case LEFT:
			return here + Target(0,1);
			break;
		}

		break;
	case WEST:
		switch(dir)
		{
		case FORWARD:
			return here + Target(-1,0);
			break;
		case BACK:
			return here + Target(1,0);
			break;
		case RIGHT:
			return here + Target(0,1);
			break;
		case LEFT:
			return here + Target(0,-1);
			break;
		}
	
		break;
	}

	return here;
}

bool Centurion::OnGrid(Target p, Target dim)
{
	if(p.x < 0 || p.y < 0 || p.x >= dim.x || p.y >= dim.y)
		return false;

	return true;
}

// Attempts to move closer to there from here
// If can't try to go a different route, prioritizing straight movement so we don't go in a circle
// If we must turn then we try as hard as possible to not go to last
// If we turn then we will turn in the direction that will "bring us closer" to there
// Will move straight through anything that isn't impassable
// If we are going to rotate we will return our rotate value in e as a Direction
Target Centurion::DetermineMoveDirection(MoveInfo* info, Target here, Target there, Target last, Target dim)
{
	// We will return this
	Target ret = here;

	// We prioritize movement in this order
	Direction order_of_moving[] = {FORWARD,LEFT,RIGHT,BACK};

	// Look to see which direction gets us closest to our target
	for(uint32_t i = 0;i < 4;i++)
	{
		// Check if we are closer or not
		if(ret.Distance(there) > GetDirection(here,order_of_moving[i]).Distance(there))
			if(!info->HasImpassAtXY(GetDirection(here,order_of_moving[i]))) // Check if we can move to our new location
				ret = GetDirection(here,order_of_moving[i]);
	}

	// We failed to find a closer distance so now find a spot that isn't closer but is better than standing still
	if(ret == here)
	{
		// Look to see which direction gets us closest to our target
		for(uint32_t i = 0;i < 4;i++)
		{
			// Check if we can move to this new location
			if(!info->HasImpassAtXY(GetDirection(here,order_of_moving[i]))) 
				if(ret.Distance(there) < GetDirection(here,order_of_moving[i]).Distance(there) && GetDirection(here,order_of_moving[i]) != last) // Check if we are closer or not
					ret = GetDirection(here,order_of_moving[i]);
				else if(ret == here && GetDirection(here,order_of_moving[i]) != last) // If we are still moving to here we have a problem, also don't move to last
					ret = GetDirection(here,order_of_moving[i]);
		}
	}

	// If we aren't going forward or staying still we need to set e
	if(ret != here && ret != GetDirection(here,FORWARD))
		if(ret == GetDirection(here,LEFT)) // We rotate counter-clockwise iff we need to move left
			ret = ret + Target(0,0,-1);
		else // We rotate clockwise if we need to go right or backwards
			ret = ret + Target(0,0,1);

	// This is where we need to go
	return ret;
}

void Centurion::Rotate(Direction d)
{
	if(d != LEFT && d != RIGHT)
		return;

	switch(face)
	{
	case NORTH:
		if(d == LEFT)
			face = WEST;
		else
			face = EAST;

		break;
	case SOUTH:
		if(d == LEFT)
			face = EAST;
		else
			face = WEST;

		break;
	case EAST:
		if(d == LEFT)
			face = NORTH;
		else
			face = SOUTH;

		break;
	case WEST:
		if(d == LEFT)
			face = SOUTH;
		else
			face = NORTH;

		break;
	}

	return;
}

// Will now attempt to explain the behaviour of a pillseeker.
// 1  - Update our available targets
// 2  - Get the closest target
// 3  - If we have no closest target goto step 4, else goto step 6
// 4  - We have to find something to do so add the next target
// 6  - We have a target so move towards if as directly as possible
// 7  - If we have something in our way then go around it
// 8  - Repeat
string Centurion::Move(MoveInfo* info, uint32_t x, uint32_t y, uint32_t last_x, uint32_t last_y, uint32_t width, uint32_t height)
{
	// If we are given (-1,-1) if means we want to do nothing as we aren't initialized somehow
	if(x == -1 && y == -1)
		return string("4");

	// First we update our targets
	AddNewTargets(info);

	// Figure out where the closest target is
	Target closest = GetTarget(Target(x,y));

	// Check if we have a target towards which we should move
	if(closest == Target(-1,-1)) // We do not have a valid target so look for one
	{
		SearchAndDestroy(info,Target(x,y),Target(width,height));
		turn_counter = -targets[0].e; // This is how many turns we have to get there.

		return Move(info,x,y,last_x,last_y,width,height);
	}
	else // We have a valid target, if e is zero or negative then we have a destination that isn't a pill
	{
		if(closest.e < 0) // If we have a negative e value then we have a turn counter
		{
			turn_counter--;

			// If we no longer have turns to get to our target we need to remove it and try again
			if(turn_counter < 0)
			{
				RemoveTarget(closest);
				return Move(info,x,y,last_x,last_y,width,height);
			}
		}

		// If we are at our target or close if e is zero or negative then we no longer need it
		if(closest == Target(x,y))
		{
			// Remote the target and try again
			RemoveTarget(closest);
			return Move(info,x,y,last_x,last_y,width,height);
		}
		else // We haven't made it to our target yet so figure out how to get there
		{
			// Determine where we should move to
			Target moveto = DetermineMoveDirection(info,Target(x,y),closest,Target(last_x,last_y),Target(width,height));

			// If e is not zero then we need to rotate, otherwise we move forward
			if(moveto.e > 0)
				return string("1 +90");
			else if(moveto.e < 0)
				return string("1 -90");
			else
				return string("0");
		}
	}

	// We literally couldn't figure out anything to do...somehow
	return string("4");
}

long Centurion::abs(long X)
{
	if(X < 0)
		return -X;

	return X;
}

long Centurion::rand(long upperbound, long min)
{
	static time_t seed = time(NULL);
	seed = (8253729 * seed + 2396403);
	return abs(seed % upperbound + min);
}

#endif