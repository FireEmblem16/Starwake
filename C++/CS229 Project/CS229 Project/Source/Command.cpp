///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Command.h ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Implements the Command class.                                               ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _COMMAND_CPP
#define _COMMAND_CPP

#include "../Headers/Command.h"

Command::Command()
{
	me_code = NOT_SET;
	mv_code = NO_VAL;
	data.clear();

	return;
}

Command::Command(const Command& rhs)
{
	me_code = rhs.me_code;
	mv_code = rhs.mv_code;
	data = vector<string>(rhs.data);

	return;
}

Command& Command::operator =(const Command& rhs)
{
	if(this == &rhs)
		return *this;

	me_code = rhs.me_code;
	mv_code = rhs.mv_code;
	data = vector<string>(rhs.data);

	return *this;
}

void Command::SetMessageCommand(MessageCode codeval)
{
	me_code = codeval;
	mv_code = NO_VAL;
	
	return;
}

void Command::SetMoveCommand(MoveCode codeval)
{
	mv_code = codeval;
	me_code = NOT_SET;

	return;
}

void Command::AddParam(string param)
{
	data.push_back(param);
	return;
}

void Command::RemoveParam(size_t i)
{
	if(i < 0 || i >= data.size())
		return;

	data.erase(data.begin() + i);
	return;
}

void Command::Clear()
{
	me_code = NOT_SET;
	mv_code = NO_VAL;
	data.clear();

	return;
}

bool Command::MessageCommand()
{
	return me_code != NOT_SET;
}

bool Command::MoveCommand()
{
	return mv_code != NO_VAL;
}

int Command::GetCommand()
{
	return (me_code == NOT_SET ? mv_code : me_code);
}

string Command::GetParam(size_t i)
{
	if(i < 0 || i >= data.size())
		return string("");

	return data[i];
}

size_t Command::Params()
{
	return data.size();
}

#endif
