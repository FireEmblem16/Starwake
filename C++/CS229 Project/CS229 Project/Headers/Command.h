///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Command.h ///////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// This class contains information about a command sent to stdin or being sent ///
/// out to stdout.                                                              ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
#ifndef _COMMAND_H
#define _COMMAND_H

#include <string>
#include <vector>
using std::string;
using std::vector;

class Command
{
public:
	Command();
	Command(const Command& rhs);
	Command& operator =(const Command& rhs);

	enum MessageCode {INIT = 0, MOVE = 1, TERMINATE = 2, NOT_SET = 5};
	enum MoveCode {FORWARD = 0, TURN = 1, FIRE = 2, PROBE = 3, NOTHING = 4, NO_VAL = 5};

	void SetMessageCommand(MessageCode codeval);
	void SetMoveCommand(MoveCode codeval);

	void AddParam(string param);
	void RemoveParam(size_t i);
	void Clear();

	bool MessageCommand();
	bool MoveCommand();

	int GetCommand();
	string GetParam(size_t i);
	size_t Params();
private:
	MessageCode me_code;
	MoveCode mv_code;
	vector<string> data;
};

#endif
