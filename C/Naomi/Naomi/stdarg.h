// stdarg.h
// Defines several macros which provide information about c and c++ variable length parameters.

#ifndef STDARG_H
#define	STDARG_H

#include <va_list.h>

#ifdef __cplusplus
extern "C"
{
#endif

// Width of stack == width of int
#define	STACKITEM int

// Round up width of objects pushed on stack. The expression before the and ensures that we get 0 for objects of size 0.
#define	VA_SIZE(TYPE) ((sizeof(TYPE) + sizeof(STACKITEM) - 1) & ~(sizeof(STACKITEM) - 1))

// &(LASTARG) points to the LEFTMOST argument of the function call (before the ...)
#define	va_start(AP, LASTARG) (AP = ((va_list)&(LASTARG) + VA_SIZE(LASTARG)))
#define va_end(AP)
#define va_arg(AP, TYPE) (AP += VA_SIZE(TYPE), *((TYPE *)(AP - VA_SIZE(TYPE))))

#ifdef __cplusplus
}
#endif

#endif