/*
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////// Error.c /////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
/// Provides some standard error inferfacing to report problems to the user.    ///
///////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////
*/
#ifndef _ERROR_C
#define	_ERROR_C

#include "../Headers/Defines.h"
#include "../Headers/Error.h"

/* Error code map */
const char* errors[] =
{"No Error.","Too many arguments were given to the program.","The program did not recieve"
" enough arguments to run.","Invalid number of arguments: Check your syntax.",
"An invalid image coloring format was given.","An invalid argument was given.","Invalid "
"argument: An integer was expected.","Invalid argument: A string was expected.","Error: "
"Abrupt end of file detected.","A supplied argument was out of range.","An invalid size "
"was supplied for an image channel.","An invalid image format was provided.","A null "
"pointer was encountered.","File not found.","Non-fatal error: Unable to close a file.",
"Something unexpected and unforseen occured of which no one understands.","An invalid "
"kernel file was provided.","A color kernel was expected and a black white kernel was "
"provided.","A black white kernel was expected and a color kernel was provided.",
"An invalid kernel size was provided."};

/* This will take an error code and output a relevant string to stderr */
void ReportError(uint32_t error_code)
{
    fprintf(stderr,"%s\n",errors[error_code]);
    return;
}

/* This will take an error code and output a relevant string to stderr */
void ReportDebugError(uint32_t error_code, uint8_t* where, uint32_t line)
{
    fprintf(stderr,"%s in %s at line %u\n",errors[error_code],where,line);
    return;
}

#endif
