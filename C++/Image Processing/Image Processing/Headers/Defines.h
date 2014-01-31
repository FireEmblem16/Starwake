/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Defines.h ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Contains a bunch of standard definitions that we can simply include.        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _DEFINES_H
#define _DEFINES_H

#include <stdio.h>

/* These are standard return values from main and "bool" functions */
#define FAIL 0x1
#define SUCCESS 0x0

/* Define truth values */
#define TRUE 0x1
#define FALSE 0x0

/* Define commonly used data types as small names */
typedef unsigned char uint8_t;
typedef unsigned short uint16_t;
typedef unsigned int uint32_t;
/* uint64_t is already defined in stdint.h and is the only one defined properly */
#ifndef uint64_t
typedef unsigned long long uint64_t;
#endif

#ifndef int8_t
typedef signed char int8_t;
#endif

#ifndef int32_t
typedef signed int int32_t;
#endif

/* No clue why this isn't already declared */
typedef FILE* PFILE;

/* Returns TRUE if str is a number and FALSE otherwise */
extern uint32_t isnumber(uint8_t*);

/* Returns the closest integer to the given number */
extern float roundf(float);

#endif
