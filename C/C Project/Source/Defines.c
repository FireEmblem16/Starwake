/*
///////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////// Defines.h ////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Contains a bunch of standard definitions that we can simply include.        ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _DEFINES_C
#define _DEFINES_C

#include <ctype.h>
#include "../Headers/Defines.h"

/* Returns TRUE if str is a number and FALSE otherwise.
 */
uint32_t isnumber(uint8_t* str)
{
    /* If our number is negative then ignore the minus sign */
    if(*str == '-')
        str++;

    /* Loop for anything that isn't a number */
    while(*str)
        if(!isdigit(*str++))
            return FALSE;

    /* We didn't detect anything wrong */
    return TRUE;
}

/* Returns the closest integer to the given number */
float roundf(float a)
{
    return (float)((int)(a + 0.5f));
}

#endif
