// panic.cpp
// Manages the beloved kernel panic screen.

#ifndef PANIC_CPP
#define PANIC_CPP

#include <hal.h>
#include <stdarg.h>
#include <stdio.h>
#include <DebugDisplay.h>

void _cdecl kernel_panic(const char* fmt,...)
{
	disable_interrupts();

	va_list args;
	static char buf[1024];

	va_start(args,fmt);
	vsprintf(buf,fmt,args);
	va_end(args);

	char* disclaimer = "Kernel Panic!\nUnfortunately for you, Naomi OS has encountered\na fatal error and has to shut down.\n\n";

	DebugClrScr(33);
	DebugGotoXY(0,0);
	DebugSetColor(33);

	DebugPuts(disclaimer);

	DebugPrintf("*** STOP: %s",buf);

	for(;;);
}

#endif