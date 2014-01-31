#ifndef _UTIL_H
#define _UTIL_H

#include <stdio.h>
#include <string.h>
#include "defines.h"

#ifdef WINDOWS
#include <math.h>
#endif

/* Prints out an error given by [err] to the file stream [out].*/
/* Returns SUCCESS if successful, including no error being printed due to [err] being invalid.*/
/* Returns NULL_POINTER if [out] is invalid.*/
/* User is responsible for closing [out].*/
uint32_t write_error(uint32_t err,PFILE out);

/* Determines if the given character is a white space character.*/
/* Returns TRUE if it is and FALSE otherwise.*/
/* Whitespace characters include ' ' '\t' '\n' '\r'*/
uint32_t is_white_space(uint8_t c);

/* Returns TRUE if the given c string is equivalent to a decimal zero.*/
/* Returns FALSE otherwise.*/
uint32_t is_zero_decimal(c_string str);

/* Returns TRUE if the given c string is equivalent to a real number zero.*/
/* Returns FALSE otherwise.*/
uint32_t is_zero_real(c_string str);

/* Prints out the given integer [i] in unsigned form to [out] in little endian format. */
/* Returns SUCCESS if [i] was successfully written to [out]. */
/* Returns NULL_POINTER if [out] is invalid. */
/* Returns IO_EXCEPTION if [out] errors. */
uint32_t write_integer(uint32_t i, PFILE out);

/* Prints out the given short [i] in unsigned form to [out] in little endian format. */
/* Returns SUCCESS if [i] was successfully written to [out]. */
/* Returns NULL_POINTER if [out] is invalid. */
/* Returns IO_EXCEPTION if [out] errors. */
uint32_t write_short(uint16_t i, PFILE out);

/* Returns the first index of [c] starting at [start] in [str]. */
/* Returns -1 if [c] is not found, [start] is out of bounds or [str] is not valid. */
uint32_t index_of(c_string str, uint8_t c, uint32_t start, uint8_t reverse_search);

/* Rounds the given double to an integer.*/
/* Rounds up if the fractional part is greater than or equal to one half.*/
double round(double d);

/* Returns the factorial of [X]. */
double factorial(uint32_t X);

#ifndef WINDOWS
/* This value is a replacement for HUGE_VAL since */
double HUGE_VAL();

/* Preforms the mathematical functions implied by the name, in radians if relevent. */
double sin(double theta);
double cos(double theta);
double fmod(double X,double Y);
double floor(double X);
double ceil(double X);
double log10(double X);

double sqrt(double X);
#endif

#endif