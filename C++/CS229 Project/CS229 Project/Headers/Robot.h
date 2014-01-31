///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////// Robot.h ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Represents a robot in a simulation environment. See Interactions.txt        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _ROBOT_H
#define _ROBOT_H
#include <iostream>
#include <sstream>
#include <string>
#include <stdarg.h>
#include <stdio.h>
#include "Behaviour.h"
#include "Defines.h"
#include "Object.h"
#include "SoftPlace.h"
using std::cout;
using std::string;
using std::stringstream;

// Contains an robot that belongs to a simulation environment.
class Robot : public Object
{
public:
	Robot(string Type, string Display, string Name = "Object", string Color = "blue", uint32_t energy = 100,
			uint32_t Recharge = 1, uint32_t MoveCost = 2, uint32_t TurnCost = 1, uint32_t ProbeCost = 1,
			float ParamA = 0.1f, float ParamB = 1.6f, float ParamC = 1.3f);
	Robot(const Robot& rhs);
	Robot& operator =(const Robot& rhs);
	virtual Item* Clone();

	enum Facing {NORTH = 1, EAST = 2, SOUTH = 3, WEST = 4};

	// Sets parameters given by their names listed in arg in that order.
	// If an invalid parameter is given nothing happens. Returns the
	// number of parameters successfully altered. char* for strings please.
	virtual uint32_t SetParams(string arg, ...);

	// Gets parameters given by their names listed in arg in that order.
	// If an invalid parameter is given nothing happens. Returns the
	// number of parameters successfully obtained. The arguments in ...
	// should be pointers to where the data should be stored.
	virtual uint32_t GetParams(string arg, ...);

	// Returns a string representing data about this Item.
	virtual string ToString();
	virtual string ToXML(uint32_t xloc, uint32_t yloc);

	void SetBehaviour(Behaviour* b);

	uint32_t GetEnergy();
	uint32_t GetRecharge();
	uint32_t GetMoveCost();
	uint32_t GetTurnCost();
	uint32_t GetProbeCost();

	float GetParamA();
	float GetParamB();
	float GetParamC();

	PFILE GetStdin();
	PFILE GetStdout();

	void SetStdin(PFILE fin);
	void SetStdout(PFILE fout);

	uint32_t GetID();
	static uint32_t GetCount();

	bool Probed();
	void SetProbed(bool b);

	uint32_t GetProbeWidth();
	void SetProbeWidth(uint32_t w);

	Facing GetFacing();
	void SetFacing(Facing f);
private:
	uint32_t energy;
	uint32_t recharge;
	uint32_t move_cost;
	uint32_t turn_cost;
	uint32_t probe_cost;

	float paramA;
	float paramB;
	float paramC;

	PFILE in;
	PFILE out;

	uint32_t iD; // This exists soley to easy the work done in simulate.cpp
	static uint32_t count; // This exists soley to easy the work done in simulate.cpp

	bool probed;
	uint32_t probe_width;
	Facing dir;
};

#endif
