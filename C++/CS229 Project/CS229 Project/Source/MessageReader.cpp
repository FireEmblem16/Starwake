///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// MessageReader.h /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the MessageReader class.                                         ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _MESSAGE_READER_CPP
#define _MESSAGE_READER_CPP

#include "../Headers/MessageReader.h"

MessageReader::MessageReader()
{
	message = 0;
	ready = false;
	wait_for_probe = false;
	probe_size = 0;

	return;
}

void MessageReader::JustProbed(uint32_t size)
{
	// We must have a probe size of at least one
	if(size < 1)
		return;

	wait_for_probe = true;
	probe_size = size;

	return;
}

uint32_t MessageReader::ProbeSize()
{
	return probe_size;
}

uint32_t MessageReader::MessageNumber()
{
	return message;
}

bool MessageReader::HasCommand()
{
	return ready;
}

// Use this command if we are on the robot side of the stream
void MessageReader::ReadCommand()
{
	// We will hold or command value in here and then branch based on it
	int command = -1;

	// Get the next command type
	cin >> command;

	// Check that we actually got a valid command
	if(command < 0 || command > 2)
		return;

	// We will use this as our new command but in case we error don't change anything yet
	Command newcmd;

	// Set the command type
	newcmd.SetMessageCommand((Command::MessageCode)command);

	// We will know if we errored if this is true
	bool error = false;

	// Branch based on what kind of input we need to read
	switch(command)
	{
	case Command::INIT:
		error = ReadInit(&newcmd);
		break;
	case Command::MOVE:
		error = ReadMove(&newcmd);
		break;
	case Command::TERMINATE:
		error = ReadTerminate(&newcmd);
		break;
	}

	// Check if we errored while trying to read the command
	if(!error)
		return;

	// Update variables
	cmd = newcmd;
	ready = true;
	return;
}

// Use this command if we are on the simulation side of the stream
void MessageReader::ReadMoveCommand()
{
	// We will hold or command value in here and then branch based on it
	int command = -1;

	// Get the next command type
	cin >> command;

	// Check that we actually got a valid command
	if(command < 0 || command > 2)
		return;

	// We will use this as our new command but in case we error don't change anything yet
	Command newcmd;

	// Set the command type
	newcmd.SetMoveCommand((Command::MoveCode)command);

	// We will know if we errored if this is true
	bool error = false;

	// Branch based on what kind of input we need to read
	switch(command)
	{
	case Command::FORWARD:
		error = ReadMoveForward(&newcmd);
		break;
	case Command::TURN:
		error = ReadTurn(&newcmd);
		break;
	case Command::FIRE:
		error = ReadFire(&newcmd);
		break;
	case Command::PROBE:
		error = ReadProbe(&newcmd);
		break;
	case Command::NOTHING:
		error = ReadDoNothing(&newcmd);
		break;
	}

	// Check if we errored while trying to read the command
	if(error)
		return;

	// Update variables
	cmd = newcmd;
	ready = true;
	wait_for_probe = false;
	probe_size = 0;
	return;
}

// Use this command if we are on the simulation side of the stream
void MessageReader::ReadMoveCommand(PFILE in)
{
	// We will hold or command value in here and then branch based on it
	int command = -1;

	// Get the next command type
	fscanf(in,"%i",&command);

	// Check that we actually got a valid command
	if(command < 0 || command > 2)
		return;

	// We will use this as our new command but in case we error don't change anything yet
	Command newcmd;

	// Set the command type
	newcmd.SetMoveCommand((Command::MoveCode)command);

	// We will know if we errored if this is true
	bool error = false;

	// Branch based on what kind of input we need to read
	switch(command)
	{
	case Command::FORWARD:
		error = ReadMoveForward(&newcmd,in);
		break;
	case Command::TURN:
		error = ReadTurn(&newcmd,in);
		break;
	case Command::FIRE:
		error = ReadFire(&newcmd,in);
		break;
	case Command::PROBE:
		error = ReadProbe(&newcmd,in);
		break;
	case Command::NOTHING:
		error = ReadDoNothing(&newcmd,in);
		break;
	}

	// Check if we errored while trying to read the command
	if(error)
		return;

	// Update variables
	cmd = newcmd;
	ready = true;
	wait_for_probe = false;
	probe_size = 0;
	return;
}

Command MessageReader::GetCommand()
{
	if(!ready)
		return Command();

	ready = false;
	return cmd;
}

MoveInfo* MessageReader::GenerateMoveInfo()
{
	// Check that we have a move command so we can do something useful
	if(!cmd.MessageCommand() || cmd.GetCommand() != Command::MOVE)
		return NULL;

	// We will be returning this
	MoveInfo* ret = new MoveInfo();

	// This is the param we are currently processing
	size_t index = 0;

	// The fist value in our command is the success parameter
	ret->SetSuccess(cmd.GetParam(index++));

	// We will use this for data parsing
	stringstream formatter;

	// Figure out how many visable items we have
	uint32_t items = 0;

	// Get how many visable items we have
	formatter << cmd.GetParam(index++);
	formatter >> items;

	// For each item we need to generate it
	for(uint32_t i = 0;i < items;i++)
	{
		// We are going to add these to our return value
		ItemData data;

		// Set our data type
		data.type = cmd.GetParam(index++);

		// Get our x location
		formatter << cmd.GetParam(index++);
		formatter >> data.x;

		// Get our y location
		formatter << cmd.GetParam(index++);
		formatter >> data.y;

		// Set our data color
		data.color = cmd.GetParam(index++);

		// Check if we have an energy value with this object
		if(TypeHasEnergy(data.type))
		{
			// Get our energy value
			formatter << cmd.GetParam(index++);
			formatter >> data.energy;
		}

		// Add the data
		ret->AddVisable(data);
	}

	// If we probed then we need to add that data too, if we didn't, probe size is zero
	if(probe_size > 0)
		for(uint32_t i = 0;i < probe_size;i++)
		{
			// Use this to provide an address
			int temp = -1;

			// Get our distance value
			formatter << cmd.GetParam(index++);
			formatter >> temp;

			// Add the distance we found
			ret->AddProbeDistance(temp);
		}

	// If we are on a message of multiple 5 then we were given our current energy
	if(message % 5)
	{
		// Use this to provide an address
		int temp = -1;

		// Get our distance value
		formatter << cmd.GetParam(index++);
		formatter >> temp;

		// Set the energy we found
		ret->SetEnergy(temp);
	}

	// Return what we've created
	return ret;
}

void MessageReader::WriteCommand(Command* command)
{
	// Check if we have a command to write, NOT_SET and NO_VAL have the same value
	if(command == NULL || command->GetCommand() == Command::NOT_SET)
		return;

	// Output our command value
	cout << command->GetCommand();

	// Output every parameter of the command
	for(size_t i = 0;i < command->Params();i++)
		cout << " " << command->GetParam(i);

	// We like to end things with new lines and we might as well flush the buffer too
	cout << endl;

	// Command successfully written
	return;
}

void MessageReader::WriteCommand(Command* command, PFILE out)
{
	// Check if we have a command to write, NOT_SET and NO_VAL have the same value
	if(command == NULL || command->GetCommand() == Command::NOT_SET)
		return;

	// Output our command value
	fprintf(out,"%i",command->GetCommand());

	// Output every parameter of the command
	for(size_t i = 0;i < command->Params();i++)
		fprintf(out," %s",command->GetParam(i).c_str());

	// We like to end things with new lines and we might as well flush the buffer too
	fprintf(out,"\n");

	return;
}

bool MessageReader::ReadInit(Command* command)
{
	// Getting this means we are reading message zero, or maybe we are reloading...or something
	message = 0;

	// Since we are just starting or restarting we don't expect a probe to resolve
	wait_for_probe = false;
	probe_size = 0;

	// We will store the next value obtained from cin here
	string next;

	// This type of message has seven parameters
	for(uint32_t i = 0;i < 7;i++)
	{
		// Get the next value passed to us
		cin >> next;

		// Add the param we just got if it isn't empty
		if(next != string(""))
			command->AddParam(RemoveWhiteSpace(next));
	}

	return true;
}

bool MessageReader::ReadMove(Command* command)
{
	// We are reading the next message
	++message;

	// We will use these things to process our input
	stringstream formater;
	string next;

	// Get the success parameter
	cin >> next;

	// Add the success parameter to our command
	command->AddParam(RemoveWhiteSpace(next));

	// Get the number of things this robot can see
	cin >> next;

	// Remove any whitespace we may have
	next = RemoveWhiteSpace(next);
	
	// Add the number to our command
	command->AddParam(next);

	// This will contain how many things the robot can see
	uint32_t things = 0xFFFFFFFF;

	// Get the int version of our number
	formater << next;
	formater >> things;

	// For each thing we see we are given 4 pieces of data and a 5th
	// piece of data if the object we can see has energy cost or contents
	for(uint32_t i = 0;i < things;i++)
	{
		// The first value is a type
		cin >> next;

		// Get the for sure value of type
		string type = RemoveWhiteSpace(next);

		// Add the type to our command
		command->AddParam(type);

		for(uint32_t j = 0;j < 4;j++)
		{
			if(j == 3 && !TypeHasEnergy(type))
				continue;

			// Get the next param value
			cin >> next;

			// If we are supposed to read in an int check that we did
			if(j != 2)
			{
				// If we have an integer then we should remove whitespace
				next = RemoveWhiteSpace(next);
			}
			else if(!MoveInfo::IsObject(type))
				continue; // Properties don't have colors

			// Add the next param to the command
			command->AddParam(next);
		}
	}

	// If we probed then we will be recieving more information
	if(wait_for_probe)
	{
		// We need to read in n integers that the probe returned
		for(uint32_t i = 0;i < probe_size;i++)
		{
			cin >> next;

			command->AddParam(RemoveWhiteSpace(next));
		}
	}
	else
		probe_size = 0; // We set this to zero if we weren't waiting on a probe so we can generate stuff easier

	// If we are on a message that is a multiple of five we are given the
	// energy contents of what we reading for
	if(!(message % 5))
	{
		cin >> next;

		command->AddParam(RemoveWhiteSpace(next));
	}

	// If we just probed we don't expect one now, even if we are
	// going to probe this turn as we will reset this value to true
	wait_for_probe = false;
	return true;
}

bool MessageReader::ReadTerminate(Command* command)
{return true;}

bool MessageReader::ReadMoveForward(Command* commmand)
{return true;}

bool MessageReader::ReadTurn(Command* command)
{
	// This should be 90 or -90
	string param;

	// Read the param in
	cin >> param;

	// Remove whitespace
	param = RemoveWhiteSpace(param);

	// Error check
	if(param != "-90" && param != "+90")
		return false;

	// Add the param to our command
	command->AddParam(param);
	return true;
}

bool MessageReader::ReadFire(Command* command)
{
	// This should be a float
	string param;

	// Read the param in
	cin >> param;

	// Remove whitespace
	param = RemoveWhiteSpace(param);

	// Add the param to our command
	command->AddParam(param);
	return true;
}

bool MessageReader::ReadProbe(Command* command)
{
	// This should be an integer
	string param;

	// Read the param in
	cin >> param;

	// Remove whitespace
	param = RemoveWhiteSpace(param);

	// Add the param to our command
	command->AddParam(param);
	return true;
}

bool MessageReader::ReadDoNothing(Command* command)
{return true;}

bool MessageReader::ReadMoveForward(Command* commmand, PFILE in)
{return true;}

bool MessageReader::ReadTurn(Command* command, PFILE in)
{
	// This should be 90 or -90
	char paramchar[1024];

	// Read the param in
	fscanf(in,"%s",&paramchar);

	// Convert to string
	string param(paramchar);

	// Remove whitespace
	param = RemoveWhiteSpace(param);

	// Error check
	if(param != "-90" && param != "+90")
		return false;

	// Add the param to our command
	command->AddParam(param);
	return true;
}

bool MessageReader::ReadFire(Command* command, PFILE in)
{
	// This should be a float
	char paramchar[1024];

	// Read the param in
	fscanf(in,"%s",&paramchar);

	// Convert to string
	string param(paramchar);

	// Remove whitespace
	param = RemoveWhiteSpace(param);

	// Add the param to our command
	command->AddParam(param);
	return true;
}

bool MessageReader::ReadProbe(Command* command, PFILE in)
{
	// This should be an integer
	char paramchar[1024];

	// Read the param in
	fscanf(in,"%s",&paramchar);

	// Convert to string
	string param(paramchar);

	// Remove whitespace
	param = RemoveWhiteSpace(param);

	// Add the param to our command
	command->AddParam(param);
	return true;
}

bool MessageReader::ReadDoNothing(Command* command, PFILE in)
{return true;}

bool MessageReader::TypeHasEnergy(string type)
{
	// Look for the type we were given
	if(!type.compare("ball"))
		return false;
	else if(!type.compare("block"))
		return false;
	else if(!type.compare("earth-rock"))
		return false;
	else if(!type.compare("energy-pill"))
		return true;
	else if(!type.compare("robot"))
		return true;
	else if(!type.compare("rumulan-rock"))
		return false;
	else if(!type.compare("hole"))
		return false;
	else if(!type.compare("jammer"))
		return false;
	else if(!type.compare("lava"))
		return true;
	else if(!type.compare("mud"))
		return true;
	else if(!type.compare("water"))
		return true;

	// We were given a type we can't use
	return false;
}

string MessageReader::RemoveWhiteSpace(string str, string whitespace)
{
	// This is the next of our next whitespace character
	size_t index = 0;

	// Remove whitespaces until we can't find any more
	while((index = str.find_first_of(whitespace,index)) != string::npos)
		str.replace(index,1,"");

	// Return our modified string
	return str;
}

#endif
