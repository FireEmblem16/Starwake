// main.cpp
// The starting point of the kernel.

#pragma warning(disable:4702)

#ifdef _DEBUG
#include <DebugDisplay.h>
#include <Debugcmd.h>
#endif

#include <mmngr_phys.h>

void test();

int _cdecl main()
{
	test();
	
	return 1;
}

void test()
{
#ifdef _DEBUG
	DebugClrScr(33);
	DebugSetColor(33);
	DebugGotoXY(0,0);
	
	DebugRuncmd();
#endif

	return;
}