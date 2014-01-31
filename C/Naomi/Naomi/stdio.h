// stdio.h
// Defines a bunch of standard input-output functions.

#ifndef STDIO_H
#define STDIO_H

#include <stdarg.h>

extern int vsprintf(char* str, const char* format, va_list ap);
extern long strtol(const char* nptr, char** endptr, int base);
extern unsigned long strtoul(const char* nptr, char** endptr, int base);
extern int atoi(const char* str);

#endif