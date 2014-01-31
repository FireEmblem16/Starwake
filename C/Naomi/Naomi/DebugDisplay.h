// DebugDisplay.h
// Allows the display of debug information with absolute compatability and no hardware abstraction.

#ifndef DEBUGDISPLAY_H
#define DEBUGDISPLAY_H

extern void DebugPutc(unsigned char c);
extern void DebugClrScr(const unsigned short c);
extern void DebugPuts(char* str);
extern int DebugPrintf(const char* str, ...);
extern unsigned DebugSetColor(const unsigned c);
extern void DebugGetXY(unsigned* x,unsigned* y);
extern void DebugGotoXY(unsigned x, unsigned y);
extern int DebugGetHorz();
extern int DebugGetVert();

#endif