///////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////// Robot.cpp /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the robot class.                                                 ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ROBOT_CPP
#define _ROBOT_CPP

#include "../Headers/Robot.h"

uint32_t Robot::count = 0;

Robot::Robot(string Type, string Display, string Name, string Color, uint32_t Energy,
				uint32_t Recharge, uint32_t MoveCost, uint32_t TurnCost, uint32_t ProbeCost,
				float ParamA, float ParamB, float ParamC) : Object(Type,Display,Name,Color)
{
	energy = Energy;
	recharge = Recharge;
	move_cost = MoveCost;
	turn_cost = TurnCost;
	probe_cost = ProbeCost;
	paramA = ParamA;
	paramB = ParamB;
	paramC = ParamC;

	SoftPlace* s = new SoftPlace();
	nature->ChangeCollisionBehaviour(s);
	delete s;

	iD = count;
	count++;

	probed = false;
	probe_width = 0;
	dir = NORTH;

	return;
}

Robot::Robot(const Robot& rhs) : Object(rhs.type,rhs.disp,rhs.name,rhs.color)
{
	energy = rhs.energy;
	recharge = rhs.recharge;
	move_cost = rhs.move_cost;
	turn_cost = rhs.turn_cost;
	probe_cost = rhs.probe_cost;
	paramA = rhs.paramA;
	paramB = rhs.paramB;
	paramC = rhs.paramC;

	iD = count;
	count++;

	return;
}

Robot& Robot::operator =(const Robot& rhs)
{
	if(this == &rhs)
		return *this;

	energy = rhs.energy;
	recharge = rhs.recharge;
	move_cost = rhs.move_cost;
	turn_cost = rhs.turn_cost;
	probe_cost = rhs.probe_cost;
	paramA = rhs.paramA;
	paramB = rhs.paramB;
	paramC = rhs.paramC;
	
	Object::operator =(rhs);
	return *this;
}

Item* Robot::Clone()
{
	iD = count;
	count++;

	Robot* ret = new Robot("","");
	*ret = *this;
	return (Item*)ret;
}

string Robot::ToString()
{
	// Create some local variables
	string ec_str;
	stringstream out;

	// Get the string version of energy_cost
	if(energy != 100)
	{
		out << energy;
		ec_str = out.str();
	}

	return Object::ToString() + (energy == 100 ? string("") : string("\nEnergy Contents: " + ec_str));
}

string Robot::ToXML(uint32_t xloc, uint32_t yloc)
{
	string ret = "<Object>\ntype = " + type + "\nname = " + name + "\ndisplay = " + disp + "\ncolor = " + color + "\nxloc = ";

	stringstream temp;
	temp << xloc;

	ret += temp.str() + "\nyloc = ";
	
	temp.str("");
	temp << yloc;

	ret += temp.str() + "\nenergy-contents = ";

	temp.str("");
	temp << energy;

	ret += temp.str() + "\nrecharge = ";

	temp.str("");
	temp << recharge;

	ret += temp.str() + "\nmove-cost = ";

	temp.str("");
	temp << move_cost;

	ret += temp.str() + "\nturn-cost = ";

	temp.str("");
	temp << turn_cost;

	ret += temp.str() + "\nprobe-cost = ";

	temp.str("");
	temp << probe_cost;

	ret += temp.str() + "\nparamA = ";

	temp.str("");
	temp << paramA;

	ret += temp.str() + "\nparamB = ";

	temp.str("");
	temp << paramB;

	ret += temp.str() + "\nparamC = ";

	temp.str("");
	temp << paramC;

	ret += temp.str() + "\n</Object>";

	return ret;
}

// Sets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully altered. char* for strings please.
uint32_t Robot::SetParams(string arg, ...)
{
	// We will return this
	uint32_t ret = 0;

	// We will use this to grab parameters
	va_list passed;
	va_start(passed,arg);

	// Available names: energy-contents, recharge, move-cost, turn-cost, probe-cost, paramA, paramB, paramC
	string param = arg;

	// While we have a non-empty string left get more arguments
	while(param.compare(""))
	{
		// Find the first non-whitespace character
		size_t index = param.find_first_not_of(" \n\t\r");

		// Find the next non-whitespace character
		size_t index_2 = param.find_first_of(" \n\t\r",index);

		// Get the next item to alter
		string temp = param.substr(index,index_2 - index);

		// Check if we have something to return
		if(!temp.compare("energy-contents"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			energy = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("recharge"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			recharge = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("move-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			move_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("turn-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			turn_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("probe-cost"))
		{
			// Get our next parameter
			uint32_t i = va_arg(passed,uint32_t);

			// Change our string
			probe_cost = i;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("paramA"))
		{
			// Get our next parameter
			double f = va_arg(passed,double);

			// Change our string
			paramA = (float)f;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("paramB"))
		{
			// Get our next parameter
			double f = va_arg(passed,double);

			// Change our string
			paramB = (float)f;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("paramC"))
		{
			// Get our next parameter
			double f = va_arg(passed,double);

			// Change our string
			paramC = (float)f;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Object::SetParams(temp,ptr);
		}
		
		// Update our param string
		if(index_2 == string::npos)
			param = "";
		else
			param = param.substr(index_2);
	}

	va_end(passed);
	return ret;
}

// Gets parameters given by their names listed in arg in that order.
// If an invalid parameter is given nothing happens. Returns the
// number of parameters successfully obtained. The arguments in ...
// should be pointers to where the data should be stored.
uint32_t Robot::GetParams(string arg, ...)
{
	// We will return this
	uint32_t ret = 0;

	// We will use this to grab parameters
	va_list passed;
	va_start(passed,arg);

	// Available names: type, name, display
	string param = arg;

	// While we have a non-empty string left get more arguments
	while(param.compare(""))
	{
		// Find the first non-whitespace character
		size_t index = param.find_first_not_of(" \n\t\r");

		// Find the next non-whitespace character
		size_t index_2 = param.find_first_of(" \n\t\r",index);

		// Get the next item to alter
		string temp = param.substr(index,index_2 - index);

		// Check if we have something to return
		if(!temp.compare("energy-contents"))
		{
			// Get our next parameter
			uint32_t* i = va_arg(passed,uint32_t*);

			// Change our string
			*i = energy;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("recharge"))
		{
			// Get our next parameter
			uint32_t* i = va_arg(passed,uint32_t*);

			// Change our string
			*i = recharge;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("move-cost"))
		{
			// Get our next parameter
			uint32_t* i = va_arg(passed,uint32_t*);

			// Change our string
			*i = move_cost;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("turn-cost"))
		{
			// Get our next parameter
			uint32_t* i = va_arg(passed,uint32_t*);

			// Change our string
			*i = turn_cost;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("probe-cost"))
		{
			// Get our next parameter
			uint32_t* i = va_arg(passed,uint32_t*);

			// Change our string
			*i = probe_cost;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("paramA"))
		{
			// Get our next parameter
			float* f = va_arg(passed,float*);

			// Change our string
			*f = paramA;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("paramB"))
		{
			// Get our next parameter
			float* f = va_arg(passed,float*);

			// Change our string
			*f = paramB;

			// Update the number of sucessful changes
			ret++;
		}
		else if(!temp.compare("paramC"))
		{
			// Get our next parameter
			float* f = va_arg(passed,float*);

			// Change our string
			*f = paramC;

			// Update the number of sucessful changes
			ret++;
		}
		else
		{
			// Get the next pointer or raw data to send on it's merry way
			PTR ptr = va_arg(passed,PTR);

			// Check out what happens if we look deeper
			ret += Object::GetParams(temp,ptr);
		}
		
		// Update our param string
		if(index_2 == string::npos)
			param = "";
		else
			param = param.substr(index_2);
	}

	va_end(passed);
	return ret;
}

void Robot::SetBehaviour(Behaviour* b)
{
	// Check that we were actually given something
	if(b == NULL)
		return;

	delete nature;
	nature = b;

	return;
}

uint32_t Robot::GetEnergy()
{
	return energy;
}

uint32_t Robot::GetRecharge()
{
	return recharge;
}

uint32_t Robot::GetMoveCost()
{
	return move_cost;
}

uint32_t Robot::GetTurnCost()
{
	return turn_cost;
}

uint32_t Robot::GetProbeCost()
{
	return probe_cost;
}

float Robot::GetParamA()
{
	return paramA;
}

float Robot::GetParamB()
{
	return paramB;
}

float Robot::GetParamC()
{
	return paramC;
}

PFILE Robot::GetStdin()
{
	return in;
}

PFILE Robot::GetStdout()
{
	return out;
}

void Robot::SetStdin(PFILE fin)
{
	in = fin;
	return;
}

void Robot::SetStdout(PFILE fout)
{
	out = fout;
	return;
}

uint32_t Robot::GetID()
{
	return iD;
}

uint32_t Robot::GetCount()
{
	return count;
}

bool Robot::Probed()
{
	return probed;
}

void Robot::SetProbed(bool b)
{
	probed = b;
	return;
}

uint32_t Robot::GetProbeWidth()
{
	return probe_width;	
}

void Robot::SetProbeWidth(uint32_t w)
{
	probe_width = w;
	return;
}

Robot::Facing Robot::GetFacing()
{
	return dir;
}

void Robot::SetFacing(Robot::Facing f)
{
	dir = f;
	return;
}

#endif
