#ifndef SLEEP_CPP
#define SLEEP_CPP

#include <naomi.h>

void sleep(uint32_t time)
{
	static int ticks = time + get_tick_count();
	while(ticks > get_tick_count());

	return;
}

#endif