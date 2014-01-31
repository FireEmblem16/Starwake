///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////// generateconf.cpp /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Needs three arguments at least to run. The first should be the path to the  ///
/// desired configuration file to load. The second should be an integer that    ///
/// indicates the number of turns to simulate. The third parameter should be    ///
/// either -quiet or -verbose which will specify if the simulation should print ///
/// the grid every turn (verbose) or not (quiet).                               ///
/// Additionally for each robot in the configuration file a file descriptor is  ///
/// required to it's stdin and stdout of a robot executable in that order. We   ///
/// will write to stdin and read from stdout. The descriptor pairs should be    ///
/// given in the order that the robots are defined in the configuration file.   ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _GENERATECONF_CPP
#define _GENERATECONF_CPP

#include <iostream>
#include <sstream>
#include <string>
#include "../Headers/Command.h"
#include "../Headers/Defines.h"
#include "../Headers/Lava.h"
#include "../Headers/MessageReader.h"
#include "../Headers/MoveInfo.h"
#include "../Headers/Robot.h"
#include "../Headers/Simulation.h"
#include "../Headers/XMLParser.h"
using std::cerr;
using std::cin;
using std::cout;
using std::endl;
using std::string;
using std::stringstream;

bool IsNumber(string str);

void RemoveEnergy(Simulation* sim, uint32_t x, uint32_t y, Robot::Facing dir, float energy, Robot* robot);
void NextXY(int* x, int* y, Robot::Facing dir, Simulation* sim);

// [dest] must be large enough to hold [width] number of [uint32_t]s.
void DetermineProbeDistances(Simulation* sim, uint32_t x, uint32_t y, Robot::Facing dir, uint32_t width, uint32_t* dest);

void CreateInitializeCommand(Simulation* sim_data, Robot* robot, Entry* entry, Command* Initialize, stringstream* cstream);
void CreateMoveCommand(Simulation* sim_data, Robot* robot, Entry* entry, Command* RequestMove, stringstream* cstream, uint32_t turn, int success);

int main(int argc, char** argv)
{
	argc = 6;
	argv[1] = new char[16];
	argv[2] = new char[16];
	argv[3] = new char[16];
	argv[4] = new char[16];
	argv[5] = new char[16];
	argv[1] = "proj.in\0";
	argv[2] = "100\0";
	argv[3] = "-verbose\0";
	argv[4] = "1\0";
	argv[5] = "2\0";

	// Error check our command line, we will have an even number of arguments if valid
	if(argc < 4 || argc & 0x1 || !IsNumber(argv[2]) || string(argv[3]) != "-quiet" && string(argv[3]) != "-verbose")
	{
		cerr << "Too few arguments were provided to the program." << endl;
		return FAIL;
	}

	// This is the simulation in it's finalized state
	Simulation sim_data(0,0);
	
	// This will hold the state we hope for the simulation to be in after the next turn
	Simulation sim_next(0,0);
	
	// Load our simulation data
	XMLParser parser(&sim_data,argv[1]);
	uint32_t err = parser.Load();
	sim_next.SetHeight(sim_data.GetHeight());
	sim_next.SetWidth(sim_data.GetWidth());

	// Print errors
	if(err > 0)
	{
		if(err < 0xFFFFFFFF)
			cerr << "Number of errors observed in config file: " << err << endl;
		else
			cerr << "Unable to open config file" << endl;

		return FAIL;
	}

	uint32_t turns_left = 0;

	// Determine how many turns to simulate, we will also reuse cstream as it is a big object
	stringstream cstream;
	cstream << argv[2];
	cstream >> turns_left;
	cstream.str("");

	// Determine if we are in quite or verbose mode for later
	bool quiet = (string(argv[3]) == "-quiet");

	// Check if we have the right amount of file descriptors
	if(Robot::GetCount() * 2 != argc - 4)
	{
		cerr << "We were given an invalid amount of file descriptors. Given: " << (argc - 4) << " Needed: " << (Robot::GetCount() * 2) << endl;
		return FAIL;
	}

	// We find and keep these here to make things easy later on
	uint32_t NumOfRobots = Robot::GetCount();
	Robot** robots = new Robot*[NumOfRobots];
	Entry** entries = new Entry*[NumOfRobots];
	bool* active = new bool[NumOfRobots];
	int* success = new int[NumOfRobots];
	uint32_t* mudded = new uint32_t[NumOfRobots];
	MessageReader messager;

	// Look for every robot and load in and out file descriptors
	for(uint32_t i = 0;i < sim_data.Entries();i++)
	{
		Entry* entry = sim_data.GetEntry(i);
		Item* item = entry->GetObject();

		if(item != NULL && item->GetType() == "robot")
		{
			Robot* robot = (Robot*)item;

			int indescriptor = 0;
			int outdescriptor = 0;

			cstream << argv[4 + 2 * robot->GetID()] << " " << argv[4 + 2 * robot->GetID() + 1];
			cstream >> indescriptor >> outdescriptor;
			cstream.str("");

			PFILE infile = fdopen(indescriptor,"w");
			PFILE outfile = fdopen(outdescriptor,"r");

			robot->SetStdin(infile);
			robot->SetStdout(outfile);

			robots[robot->GetID()] = robot;
			entries[robot->GetID()] = entry;
			active[robot->GetID()] = true;
			success[robot->GetID()] = 0;
			robot->SetFacing(Robot::NORTH);
			mudded = 0;
			
			Command Initialize;
			CreateInitializeCommand(&sim_data,robot,entry,&Initialize,&cstream);
			messager.WriteCommand(&Initialize,robot->GetStdin());
		}
	}

	// We need to know turn % 5
	uint32_t turn = 1;

	// Simulate turns until we run out
	while(turns_left-- > 0)
	{
		Command* commands = new Command[NumOfRobots];

		// At the moment we seem to have no objects capable of independant movement other than robots
		// This means that we only need to check the consequences of their actions, therefore poke all of them
		for(uint32_t i = 0;i < NumOfRobots;i++)
		{
			if(active[i])
			{
				Command RequestMove;
				CreateMoveCommand(&sim_data,robots[i],entries[i],&RequestMove,&cstream,turn,success[i]);
				messager.WriteCommand(&RequestMove,robots[i]->GetStdin());
			}
		}

		// See what our poke has produced
		for(uint32_t i = 0;i < NumOfRobots;i++)
		{
			if(active[i])
			{
				messager.ReadMoveCommand(robots[i]->GetStdout());
				commands[i] = messager.GetCommand();
			}
		}

		// Clean the grid to ensure we don't get errors
		sim_next.Clear();

		// Make all robots ghosts to make things easy
		for(uint32_t i = 0;i < NumOfRobots;i++)
			robots[i]->MakeGhost(true);

		// As robots have greater power, see what they do first
		for(uint32_t i = 0;i < NumOfRobots;i++)
		{
			float cost = 0.0f;

			switch(commands[i].GetCommand())
			{
			case Command::FORWARD:
				cost = 0.0f;
				success[i] = 1;

				if(entries[i]->ContainsType("mud"))
				{
					Mud* mud = (Mud*)entries[i]->GetType("mud");

					if(++mudded[i] >= mud->GetTurnCost())
					{
						mudded[i] = 0;
						cost += (float)mud->GetEnergyCost();
					}
					else
					{
						success[i] = -1;
						break;
					}
				}

				if(entries[i]->ContainsType("water"))
				{
					Water* water = (Water*)entries[i]->GetType("water");
					cost += (float)water->GetEnergyCost();
				}

				{
					int x = (int)entries[i]->GetX();
					int y = (int)entries[i]->GetY();
					
					NextXY(&x,&y,robots[i]->GetFacing(),&sim_data);

					// We allow things to drive off of the simulation and die
					if(!sim_next.AddItemAt(robots[i],x,y))
						active[i] = false;
					else
						entries[i] = sim_next.GetEntriesAt(x,y);
				}

				cost += (float)robots[i]->GetMoveCost();

				break;
			case Command::TURN:
				success[i] = 1;

				switch(robots[i]->GetFacing())
				{
				case Robot::NORTH:
					if(commands[i].GetParam(0) == "+90")
						robots[i]->SetFacing(Robot::EAST);
					else
						robots[i]->SetFacing(Robot::WEST);
					
					break;
				case Robot::EAST:
					if(commands[i].GetParam(0) == "+90")
						robots[i]->SetFacing(Robot::SOUTH);
					else
						robots[i]->SetFacing(Robot::NORTH);
					
					break;
				case Robot::SOUTH:
					if(commands[i].GetParam(0) == "+90")
						robots[i]->SetFacing(Robot::WEST);
					else
						robots[i]->SetFacing(Robot::EAST);
					break;
				case Robot::WEST:
					if(commands[i].GetParam(0) == "+90")
						robots[i]->SetFacing(Robot::NORTH);
					else
						robots[i]->SetFacing(Robot::SOUTH);
					break;
				}

				cost = (float)robots[i]->GetTurnCost();

				if(!sim_next.AddItemAt(robots[i],entries[i]->GetX(),entries[i]->GetY()))
					active[i] = false;

				break;
			case Command::FIRE:
				success[i] = 1;

				{
					float e = 0.0f;
					cstream << commands[i].GetParam(0);
					cstream >> e;
					cstream.str("");

					cost = robots[i]->GetParamA() + (robots[i]->GetParamB() * e);

					RemoveEnergy(&sim_data,entries[i]->GetX(),entries[i]->GetY(),robots[i]->GetFacing(),(float)robots[i]->GetParamC() * e,robots[i]);
				}

				if(!sim_next.AddItemAt(robots[i],entries[i]->GetX(),entries[i]->GetY()))
					active[i] = false;

				break;
			case Command::PROBE:
				success[i] = 1;
				robots[i]->SetProbed(true);

				// Scope problems
				{
					uint32_t width = 0;
					cstream << commands[i].GetParam(0);
					cstream >> width;
					cstream.str("");

					robots[i]->SetProbeWidth(width);
					cost = (float)(robots[i]->GetProbeCost() * width);
				}

				if(!sim_next.AddItemAt(robots[i],entries[i]->GetX(),entries[i]->GetY()))
					active[i] = false;

				break;
			case Command::NOTHING:
				success[i] = 1;
				if(!sim_next.AddItemAt(robots[i],entries[i]->GetX(),entries[i]->GetY()))
					active[i] = false;

				break;
			}

			// If we don't have enough energy then we fail
			if(robots[i]->GetEnergy() - (uint32_t)((float)(robots[i]->GetEnergy() + robots[i]->GetRecharge()) - cost) > robots[i]->GetEnergy())
			{
				success[i] = -1;
				robots[i]->SetParams("energy-contents",robots[i]->GetEnergy() + robots[i]->GetRecharge());
			}
			else
				robots[i]->SetParams("energy-contents",(uint32_t)((float)(robots[i]->GetEnergy() + robots[i]->GetRecharge()) - cost));
		}

		// Add all of our objects and properties to the grid
		for(uint32_t i = 0;i < sim_data.Entries();i++)
		{
			Entry* entry = sim_data.GetEntry(i);

			for(uint32_t j = 0;j < entry->Items();j++)
			{
				Item* item = entry->GetItem(j);

				if(item->GetType() == "robot")
					continue;
				else
					sim_next.AddItemAt(item,entry->GetX(),entry->GetY());
			}
		}

		// Now that we've moved we don't want ghosts
		for(uint32_t i = 0;i < NumOfRobots;i++)
			robots[i]->MakeGhost(false);

		for(uint32_t x = 0;x < sim_next.GetWidth();x++)
			for(uint32_t y = 0;y < sim_next.GetHeight();y++)
			{
				Entry* entry = sim_next.GetEntriesAt(x,y);

				if(entry == NULL)
					continue;

				uint32_t objs = entry->ContainsManyObjects();

				if(objs > 1)
				{
					// We will store objects with their old entries here
					Item** items = new Item*[objs];
					Entry** locs = new Entry*[objs];

					for(uint32_t i = 0, j = 0;i < entry->Items();i++)
						if(entry->GetItem(i)->IsObject())
							items[j++] = entry->GetItem(i);

					// Get the old entries for each item at x,y
					for(uint32_t i = 0;i < objs;i++)
						locs[i] = sim_data.FindItemByAddress(items[i]);

					// For all the items in conflict we need to determine what to do with them
					for(uint32_t i = 0;i < objs;i++)
					{
						// If this item hasn't moved then we will either be pushed of not move at all, if there is one or many other objects respectevly
						if(locs[i]->GetHash(sim_next.GetWidth()) == entry->GetHash(sim_next.GetWidth()))
						{
							// If ony one object is trying to push us then move out of the way as per our collider, otherwise move the other object back
							if(objs == 2)
							{
								// Figure out how we are going to be offset
								int collision = items[i]->GetNature()->Collide(entry->CollisionFrom(locs[i]));

								// Note we don't allow diagonals in this simulation
								switch(collision)
								{
								case 0:
									// We aren't moving anywhere so send the other guy back
									sim_next.RemoveItemByAddress(items[i == 0 ? 1 : 0]);
									
									items[i == 0 ? 1 : 0]->MakeGhost(true);

									if(!sim_next.AddItemAt(items[i == 0 ? 1 : 0],locs[i == 0 ? 1 : 0]->GetX(),locs[i == 0 ? 1 : 0]->GetY()))
									{
										if(items[i == 0 ? 1 : 0]->GetType() == "robot")
										{
											active[((Robot*)items[i == 0 ? 1 : 0])->GetID()] = false;
											success[((Robot*)items[i == 0 ? 1 : 0])->GetID()] = -1;
										}
									}
									else if(items[i == 0 ? 1 : 0]->GetType() == "robot")
										entries[((Robot*)items[i == 0 ? 1 : 0])->GetID()] = sim_next.FindItemByAddress(items[i == 0 ? 1 : 0]);

									items[i == 0 ? 1 : 0]->MakeGhost(false);
									break;
								case 2:
								case -2:
									sim_next.RemoveItemByAddress(items[i]);

									items[i]->MakeGhost(true);
									
									if(!sim_next.AddItemAt(items[i],locs[i]->GetX(),locs[i]->GetY() - 1))
									{
										if(items[i]->GetType() == "robot")
										{
											active[((Robot*)items[i])->GetID()] = false;
											success[((Robot*)items[i])->GetID()] = -1;
										}
									}
									else if(items[i]->GetType() == "robot")
										entries[((Robot*)items[i])->GetID()] = sim_next.FindItemByAddress(items[i]);

									items[i]->MakeGhost(false);
									break;
								case 4:
								case -4:
									sim_next.RemoveItemByAddress(items[i]);

									items[i]->MakeGhost(true);
									
									if(!sim_next.AddItemAt(items[i],locs[i]->GetX() - 1,locs[i]->GetY()))
									{
										if(items[i]->GetType() == "robot")
										{
											active[((Robot*)items[i])->GetID()] = false;
											success[((Robot*)items[i])->GetID()] = -1;
										}
									}
									else if(items[i]->GetType() == "robot")
										entries[((Robot*)items[i])->GetID()] = sim_next.FindItemByAddress(items[i]);

									items[i]->MakeGhost(false);
									break;
								case 5:
								case -5:
									sim_next.RemoveItemByAddress(items[i]);

									items[i]->MakeGhost(true);
									
									if(!sim_next.AddItemAt(items[i],locs[i]->GetX() + 1,locs[i]->GetY()))
									{
										if(items[i]->GetType() == "robot")
										{
											active[((Robot*)items[i])->GetID()] = false;
											success[((Robot*)items[i])->GetID()] = -1;
										}
									}
									else if(items[i]->GetType() == "robot")
										entries[((Robot*)items[i])->GetID()] = sim_next.FindItemByAddress(items[i]);

									items[i]->MakeGhost(false);
									break;
								case 7:
								case -7:
									sim_next.RemoveItemByAddress(items[i]);

									items[i]->MakeGhost(true);
									
									if(!sim_next.AddItemAt(items[i],locs[i]->GetX(),locs[i]->GetY() + 1))
									{
										if(items[i]->GetType() == "robot")
										{
											active[((Robot*)items[i])->GetID()] = false;
											success[((Robot*)items[i])->GetID()] = -1;
										}
									}
									else if(items[i]->GetType() == "robot")
										entries[((Robot*)items[i])->GetID()] = sim_next.FindItemByAddress(items[i]);

									items[i]->MakeGhost(false);
									break;
								case 9:
									sim_next.RemoveItemByAddress(items[i]);

									break;
								}
							}
							else // If we have multiple objects trying to enter this grid then that is no good
								continue;
						}
						else // This item moved and we need to be pushed back if at least one other moving object (and update sim_data so that we don't need special code to just loop this)
						{
							if(objs == 2) // We do everything necessary for this above
								continue;
							else
							{
								sim_next.RemoveItemByAddress(items[i]);
									
								items[i]->MakeGhost(true);

								if(!sim_next.AddItemAt(items[i],locs[i]->GetX(),locs[i]->GetY()))
								{
									if(items[i]->GetType() == "robot")
									{
										active[((Robot*)items[i])->GetID()] = false;
										success[((Robot*)items[i])->GetID()] = -1;
									}
								}
								else if(items[i]->GetType() == "robot")
									entries[((Robot*)items[i])->GetID()] = sim_next.FindItemByAddress(items[i]);

								items[i]->MakeGhost(false);
							}
						}
					}

					// We made a change to the grid so we need to go through the whole thing again
					x = 0;
					y = 0;

					delete[] items;
					delete[] locs;
				}
			}

		// Holes are the ultimate destruction
		for(uint32_t i = 0;i < sim_next.Entries();i++)
			if(sim_next.GetEntry(i)->ContainsType("hole")) // If we are in a hole we are gone for good so we can use if hole else if lava here
			{
				if(sim_next.GetEntry(i)->ContainsObject())
				{
					Item* item = sim_next.GetEntry(i)->GetObject();

					if(item->GetType() == "robot")
						active[((Robot*)item)->GetID()] = false;

					sim_next.RemoveItemByAddress(item);
				}
			}
			else if(sim_next.GetEntry(i)->ContainsType("lava"))
			{
				if(sim_next.GetEntry(i)->ContainsObject())
				{
					if(sim_next.GetEntry(i)->GetObject()->GetType() == "robot")
					{
						Robot* robot = (Robot*)sim_next.GetEntry(i)->GetObject();
						Lava* lava = (Lava*)sim_next.GetEntry(i)->GetType("lava");

						if(robot->GetEnergy() - lava->GetEnergyCost() > robot->GetEnergy())
						{
							active[robot->GetID()] = false;
							sim_next.RemoveItemByAddress(robot);
						}
						else
							robot->SetParams("energy-contents",robot->GetEnergy() - lava->GetEnergyCost());
					}
				}
			}

		// Send deactivation command to all robots which have fallen out of the grid
		for(uint32_t i = 0;i < NumOfRobots;i++)
			if(!active[i])
			{
				Command Terminate;
				Terminate.SetMessageCommand(Command::TERMINATE);
				messager.WriteCommand(&Terminate,robots[i]->GetStdin());
			}

		// Copy our new simulation data
		sim_data = sim_next;

		// Record a successful turn
		turn++;
		delete[] commands;

		// If in verbose mode we need to display the grid and wait for the user to hit return
		if(!quiet)
		{
			parser.WriteGridOnly();
			char buf[1024];
			cin.getline(buf,1024);
		}
	}

	// Send termination code to all active robots
	for(uint32_t i = 0;i < NumOfRobots;i++)
		if(active[i])
		{
			Command Terminate;
			Terminate.SetMessageCommand(Command::TERMINATE);
			messager.WriteCommand(&Terminate,robots[i]->GetStdin());
		}


	delete[] robots;
	delete[] entries;
	delete[] active;
	delete[] success;

	parser.WriteInfo();
	return SUCCESS;
}

bool IsNumber(string str)
{
	if(str == "" || str == "-" || str == "." || str == "-.")
		return false;

	size_t index = 0;
	bool decimal = false;

	if(str[index] == '-')
		index++;

	while(index < str.length() && (str[index] >= '0' && str[index] <= '9' || str[index] == '.' && !decimal))
		if(str[index++] == '.')
			decimal = true;

	if(index == str.length())
		return true;

	return false;
}

// Offset is essentially which probe beam we are prowling
void InitializeXY(int* x, int* y, int offset, Robot::Facing dir, Simulation* sim)
{
	switch(dir)
	{
	case Robot::NORTH:
		(*y)--;
		(*x) += offset;
		break;
	case Robot::EAST:
		(*x)++;
		(*y) -= offset;
		break;
	case Robot::SOUTH:
		(*y)++;
		(*x) -= offset;
		break;
	case Robot::WEST:
		(*x)--;
		(*y) += offset;
		break;
	}

	if(*x < 0 || *x >= (int)sim->GetWidth() || *y < 0 || *y >= (int)sim->GetHeight())
		*x = -1;

	return;
}

void NextXY(int* x, int* y, Robot::Facing dir, Simulation* sim)
{
	switch(dir)
	{
	case Robot::NORTH:
		(*y)--;
		break;
	case Robot::EAST:
		(*x)++;
		break;
	case Robot::SOUTH:
		(*y)++;
		break;
	case Robot::WEST:
		(*x)--;
		break;
	}

	if(*x < 0 || *x >= (int)sim->GetWidth() || *y < 0 || *y >= (int)sim->GetHeight())
		*x = -1;

	return;
}

void RemoveEnergy(Simulation* sim, uint32_t x, uint32_t y, Robot::Facing dir, float energy, Robot* robot)
{
	int width = 1;

	for(int i = 0, px = (int)x, py = (int)y;i < width;i++, px = (int)x, py = (int)y)
	{
		InitializeXY(&px,&py,i - width / 2,dir,sim);
		int ipx = px, ipy = py;
		bool found = false;

		while(!found && x != -1)
		{
			Entry* entry = sim->GetEntriesAt(px,py);
			
			if(entry != NULL && entry->ContainsObject())
			{
				found = true;
				break;
			}

			if(!found)
				NextXY(&px,&py,dir,sim);
		}

		if(found && sim->GetEntriesAt((uint32_t)px,(uint32_t)py)->GetObject()->GetType() == "robot")
			robot->SetParams("energy-contents",(uint32_t)((float)(robot->GetEnergy()) - energy));
	}

	return;
}

// [dest] must be large enough to hold [width] number of [uint32_t]s.
void DetermineProbeDistances(Simulation* sim, uint32_t x, uint32_t y, Robot::Facing dir, int width, uint32_t* dest)
{
	for(int i = 0, px = (int)x, py = (int)y;i < width;i++, px = (int)x, py = (int)y)
	{
		InitializeXY(&px,&py,i - width / 2,dir,sim);
		int ipx = px, ipy = py;
		bool found = false;
		bool jammed = false;

		while(!found && !jammed && x != -1)
		{
			Entry* entry = sim->GetEntriesAt(px,py);

			for(uint32_t j = 0;entry != NULL && j < entry->Items();j++)
				if(entry->ContainsType("jammer"))
				{
					jammed = true;
					break;
				}
				else if(entry->GetItem(j)->GetNature()->Probe())
				{
					found = true;
					break;
				}
				else if(j == entry->Items() - 1 && entry->ContainsObject())
				{
					jammed = true;
					break;
				}

			if(!found)
				NextXY(&px,&py,dir,sim);
		}

		// Since we travel in a straight line we can do this
		dest[i] = (x == -1 || jammed ? 0 : abs((int)(ipx - px + ipy - py)));
	}

	return;
}

void CreateInitializeCommand(Simulation* sim_data, Robot* robot, Entry* entry, Command* Initialize, stringstream* cstream)
{
	Initialize->SetMessageCommand(Command::INIT);
	cstream->clear();

	*cstream << robot->GetEnergy();
	Initialize->AddParam(cstream->str());
	cstream->str("");

	*cstream << entry->GetX();
	Initialize->AddParam(cstream->str());
	cstream->str("");

	*cstream << entry->GetY();
	Initialize->AddParam(cstream->str());
	cstream->str("");

	Initialize->AddParam(robot->GetColor());

	*cstream << sim_data->GetHeight();
	Initialize->AddParam(cstream->str());
	cstream->str("");

	*cstream << sim_data->GetWidth();
	Initialize->AddParam(cstream->str());
	cstream->str("");

	*cstream << Robot::GetCount();
	Initialize->AddParam(cstream->str());
	cstream->str("");

	return;
}

void CreateMoveCommand(Simulation* sim_data, Robot* robot, Entry* entry, Command* RequestMove, stringstream* cstream, uint32_t turn, int success)
{
	RequestMove->SetMessageCommand(Command::MOVE);
	cstream->clear();

	uint32_t can_see = 0;

	if(success > 0)
		RequestMove->AddParam("true");
	else if(success < 0)
		RequestMove->AddParam("false");
	else
		RequestMove->AddParam("null");

	// Check how many items are visible to this robot
	for(uint32_t x = entry->GetX() - 1;x < entry->GetX() + 2;x++)
		for(uint32_t y = entry->GetY() - 1; y < entry->GetY() + 2;y++)
			for(uint32_t index = 0;index < entry->Items();index++)
				if(entry->EntryVisible(index))
					can_see++;

	*cstream << can_see;
	RequestMove->AddParam(cstream->str());
	cstream->str("");

	// Loop through each item and add information if it is visible
	for(uint32_t x = entry->GetX() - 1;x < entry->GetX() + 2;x++)
		for(uint32_t y = entry->GetY() - 1; y < entry->GetY() + 2;y++)
			for(uint32_t index = 0;index < entry->Items();index++)
				if(entry->EntryVisible(index))
				{
					Item* item = entry->GetItem(index);

					RequestMove->AddParam(item->GetType());

					*cstream << x;
					RequestMove->AddParam(cstream->str());
					cstream->str("");

					*cstream << y;
					RequestMove->AddParam(cstream->str());
					cstream->str("");

					if(item->IsObject())
						RequestMove->AddParam(((Object*)item)->GetColor());

					if(MoveInfo::HasEnergy(item->GetType()))
					{
						int energy = 0;
						if(item->GetParams("energy-contents",&energy) != 1)
							if(item->GetParams("energy-cost",&energy) != 1)
								continue;

						*cstream << energy;
						RequestMove->AddParam(cstream->str());
						cstream->str("");
					}
				}

	// If we probed we need to report what we found
	if(robot->Probed())
	{
		uint32_t* vals = new uint32_t[robot->GetProbeWidth()];
		DetermineProbeDistances(sim_data,entry->GetX(),entry->GetY(),robot->GetFacing(),(int)robot->GetProbeWidth(),vals);

		for(uint32_t i = 0;i < robot->GetProbeWidth();i++)
		{
			*cstream << vals[i];
			RequestMove->AddParam(cstream->str());
			cstream->str("");
		}

		delete[] vals;
		robot->SetProbed(false);
	}

	// Every fifth turn we need to send the robot's current energy
	if(!(turn % 5))
	{
		*cstream << robot->GetEnergy();
		RequestMove->AddParam(cstream->str());
		cstream->str("");
	}

	return;
}

#endif
