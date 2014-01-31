#ifndef NAOMI_H
#define NAOMI_H

#include <stdint.h>
#include <kybrd.h>
#include <hal.h>

extern void sleep(uint32_t time);

extern char getchar();
extern KEYCODE getkey();

#endif