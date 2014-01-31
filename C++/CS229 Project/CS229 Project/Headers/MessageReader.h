///////////////////////////////////////////////////////////////////////////////////
////////////////////////////////// MessageReader.h ////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// This class will read input from standard in for messages to or from the     ///
/// simulator.                                                                  ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _MESSAGE_READER_H
#define _MESSAGE_READER_H

// I HATE this warning, it is also not a warning when using g++
#pragma warning(disable:4996)

#include <iostream>
#include <sstream>
#include <stdio.h>
#include "Defines.h"
#include "MessageReader.h"
#include "Command.h"
#include "MoveInfo.h"
using std::cin;
using std::cout;
using std::endl;
using std::stringstream;

class MessageReader
{
public:
	MessageReader();

	void JustProbed(uint32_t size);
	uint32_t ProbeSize();
	uint32_t MessageNumber();

	bool HasCommand();
	void ReadCommand(); // Use this command if we are on the robot side of the stream
	void ReadMoveCommand(); // Use this command if we are on the simulation side of the stream
	void ReadMoveCommand(PFILE in); // Use this command if we are on the simulation side of the stream
	Command GetCommand();

	MoveInfo* GenerateMoveInfo();

	static void WriteCommand(Command* command);
	static void WriteCommand(Command* command, PFILE out);
private:
	bool ReadInit(Command* command);
	bool ReadMove(Command* command);
	bool ReadTerminate(Command* command);

	bool ReadMoveForward(Command* commmand);
	bool ReadTurn(Command* commmand);
	bool ReadFire(Command* commmand);
	bool ReadProbe(Command* commmand);
	bool ReadDoNothing(Command* commmand);

	bool ReadMoveForward(Command* commmand, PFILE in);
	bool ReadTurn(Command* commmand, PFILE in);
	bool ReadFire(Command* commmand, PFILE in);
	bool ReadProbe(Command* commmand, PFILE in);
	bool ReadDoNothing(Command* commmand, PFILE in);

	bool TypeHasEnergy(string type);

	string RemoveWhiteSpace(string str, string whitespace = " \n\t\r");

	uint32_t message;
	bool ready;
	Command cmd;

	bool wait_for_probe;
	uint32_t probe_size;
};

#endif
