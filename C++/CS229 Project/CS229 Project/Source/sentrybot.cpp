///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// sentrybot.cpp //////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Runs a robot AI that patrols the edges of the simulation.                   ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _SENTRY_BOT_CPP
#define _SENTRY_BOT_CPP

#include <iostream>
#include <sstream>
#include <string>
#include "../Headers/Centurion.h"
#include "../Headers/Command.h"
#include "../Headers/MessageReader.h"
#include "../Headers/MoveInfo.h"
#include "../Headers/Sentry.h"
#include "../Headers/Robot.h"
#include "../Headers/Target.h"
using std::cout;
using std::string;
using std::stringstream;

int main(int argc, char** argv)
{
	// This is our robot
	Robot robot("robot","RO","MySentryBot");
	robot.ChangeNature(new Sentry());

	// Read in our first command
	MessageReader in;
	in.ReadCommand();

	// We will store all our commands here
	Command cmd;

	// These are special "global" values
	Target here(-1,-1); // Initialized to the do nothing value
	Target last(0xFFFF,0xFFFF); // The last location we visited, initialzed to way off the grid
	Target dim(0,0);
	int NumberOfOtherRobots = 0;
	bool moving = false; // True if we tryed moving last turn
	bool rotating_left = false;
	bool rotating_right = false;

	// We need these to not be disappear
	MoveInfo* m;
	MoveInfo* last_m = NULL;

	// Loop until we are told to terminate
	while(((Command::MessageCode)cmd.GetCommand()) != Command::TERMINATE)
	{
		// Get our next command
		cmd = in.GetCommand();

		// Check what kind of command we were given, we may not have a valid one due to an invalid command string
		if(((Command::MessageCode)cmd.GetCommand()) == Command::INIT)
		{
			// We'll parse things with this
			stringstream stream;

			// The zeroeth param is our initial energy value
			stream.clear();
			stream << cmd.GetParam(0);

			// Get our initial energy
			int energy = 0;
			stream >> energy;
			stream.str("");
			stream.clear();

			// The first and second params are our x and y values respectively
			stream << cmd.GetParam(1) << " " << cmd.GetParam(2);

			// Get and set our initial x and y values
			int x = 0, y = 0;
			stream >> x >> y;
			stream.str("");
			stream.clear();
			here.x = x;
			here.y = y;

			// The fourth and fifth params are our y and x dimensions respectively
			stream << cmd.GetParam(5) << " " << cmd.GetParam(4);

			// Get and set our initial x and y values
			int x_dim = 0, y_dim = 0;
			stream >> x_dim >> y_dim;
			stream.str("");
			stream.clear();
			dim.x = x_dim;
			dim.y = y_dim;

			// The sixth param is the number of other robots in the simulation
			stream << cmd.GetParam(6);
			stream >> NumberOfOtherRobots;
			stream.str("");
			stream.clear();

			// Set all of our new values
			robot.SetParams("energy-contents color",energy,cmd.GetParam(3).c_str());
		}
		else if(((Command::MessageCode)cmd.GetCommand()) == Command::MOVE)
		{
			// Get our command in a parsed form
			m = in.GenerateMoveInfo();
			
			// We may need to update our energy
			if(m->GetEnergy() != -1)
				robot.SetParams("energy-contents",m->GetEnergy());

			// Check if we successfully moved
			if(m->GetSuccess() == "true")
			{
				// Do what we must to update our status
				if(moving)
				{
					last = here;
					here = ((Centurion*)robot.GetNature()->GetMoveBehaviour())->GetDirection(here,Centurion::FORWARD);
				}
				else
				{
					// If see an object at [here] then we were pushed out of the way since we don't see ourselves
					if(m->IsObject(m->GetVisable(here).type))	
					{
						// We first try triangulating and hope we get somewhere with that
						Target tri = m->Triangulate();
						last = here;

						// If we were able to triangulate then we are pretty much done, otherwise we have to guess and hope for the best
						if(tri != Target(-1,-1))
							here = tri;
						else
							here = m->GuessPush(last_m,last);
					}

					if(rotating_left)
						((Centurion*)robot.GetNature()->GetMoveBehaviour())->Rotate(Centurion::LEFT);
					else if(rotating_right)
						((Centurion*)robot.GetNature()->GetMoveBehaviour())->Rotate(Centurion::RIGHT);
				}
			}
			else if(m->GetSuccess() == "false") // Note that null is also valid so this is not a dichotomy
			{
				// Even if we failed to do something we may have been pushed out of the way
				if(m->IsObject(m->GetVisable(here).type))	
				{
					// We first try triangulating and hope we get somewhere with that
					Target tri = m->Triangulate();
					last = here;

					// If we were able to triangulate then we are pretty much done, otherwise we have to guess and hope for the best
					if(tri != Target(-1,-1))
						here = tri;
					else
						here = m->GuessPush(last_m,last);
				}
			}
			
			// We don't want any of these to be true anymore
			moving = false;
			rotating_left = false;
			rotating_right = false;

			// Output what we've decided to do
			string move_cmd = robot.GetNature()->Move(m,here.x,here.y,last.x,last.y,dim.x,dim.y);
			cout << move_cmd << endl;

			// Check if we are moving
			if(move_cmd == "0")
				moving = true;
			else if(move_cmd == "1 +90")
				rotating_right = true;
			else if(move_cmd == "1 -90")
				rotating_left = true;

			// Clean up
			if(last_m != NULL)
				delete last_m;

			last_m = m;
		}

		// Read our next command from out input
		in.ReadCommand();
	}

	// We don't need this anymore
	delete m;

	// Robot executed successfully
	return SUCCESS;
}

#endif
